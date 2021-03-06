package edu.uml.cs.isense.gcollector.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.uml.cs.isense.gcollector.GroupEntry;
import edu.uml.cs.isense.gcollector.R;
import edu.uml.cs.isense.gcollector.objects.DataSet;
import edu.uml.cs.isense.supplements.OrientationManager;
import edu.uml.cs.isense.waffle.Waffle;

public class QueueUploader extends Activity implements OnClickListener {
	
	private static final int DELETE_DATASET_REQUESTED = 101;
	
	public static int lastSID = -1;

	private static Context mContext;
	private static LinearLayout scrollQueue;
	private Runnable sdUploader;
	private ProgressDialog dia;
	public static QueueParentAssets qpa;
	private boolean uploadSuccess = true;
	
	private DataSet lastDataSetLongClicked;
	private View lastViewLongClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.queueprompt);

		mContext = this;
	
		qpa = new QueueParentAssets(GroupEntry.uploadQueue,
				GroupEntry.activityName, GroupEntry.mContext);
		
		Button upload = (Button) findViewById(R.id.upload);
		upload.setOnClickListener(this);

		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);

		Context c = qpa.mContext;
		String pn = qpa.parentName;
		Queue<DataSet> q = getUploadQueue(qpa.uploadQueue, qpa.parentName,
				qpa.mContext);
		qpa = new QueueParentAssets(q, pn, c);

		scrollQueue = (LinearLayout) findViewById(R.id.scrollqueue);
		fillScrollQueue(scrollQueue);
	}

	// Works through list of data to be uploaded and creates the list of blocks
	private void fillScrollQueue(LinearLayout scrollQueue) {
		
		String previous = "";

		for (final DataSet ds : qpa.mirrorQueue) {
			switch (ds.type) {
			case DATA:
				final View data = View.inflate(mContext, R.layout.queueblock_data,
						null);
				
				data.setBackgroundResource(R.drawable.listelement_bkgd_changer);

				makeBlock(data, ds);
				previous = checkPrevious(previous, scrollQueue,
						(String) ds.getName());

				scrollQueue.addView(data);
				ds.setUploadable(true);

				data.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						CheckedTextView ctv = (CheckedTextView) v
								.findViewById(R.id.name);
						ctv.toggle();
						//flashClick(v);

						if (ctv.isChecked())
							ctv.setCheckMarkDrawable(R.drawable.bluecheck);
						else
							ctv.setCheckMarkDrawable(R.drawable.red_x);

						ds.setUploadable(ctv.isChecked());

					}

				});

				data.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						lastDataSetLongClicked = ds;
						lastViewLongClicked = data;
						Intent iDeleteDataSet = new Intent(mContext,
								DeleteDataSetFromUploadQueue.class);
						startActivityForResult(iDeleteDataSet, DELETE_DATASET_REQUESTED);
						return false;
					}

				});
				break;

			case PIC:
				final View pic = View
						.inflate(mContext, R.layout.queueblock_pic, null);
				
				pic.setBackgroundResource(R.drawable.listelement_bkgd_changer);

				makeBlock(pic, ds);
				previous = checkPrevious(previous, scrollQueue,
						(String) ds.getName());

				scrollQueue.addView(pic);
				ds.setUploadable(true);
				
				pic.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						CheckedTextView ctv = (CheckedTextView) v
								.findViewById(R.id.name);
						ctv.toggle();

						if (ctv.isChecked())
							ctv.setCheckMarkDrawable(R.drawable.bluecheck);
						else
							ctv.setCheckMarkDrawable(R.drawable.red_x);

						ds.setUploadable(ctv.isChecked());

					}

				});

				pic.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						lastDataSetLongClicked = ds;
						lastViewLongClicked = pic;
						Intent iDeleteDataSet = new Intent(mContext,
								DeleteDataSetFromUploadQueue.class);
						startActivityForResult(iDeleteDataSet, DELETE_DATASET_REQUESTED);
						return false;
					}

				});
				break;
			}
		}

	}

	// Adds empty space after experiment groups
	private String checkPrevious(String previous, LinearLayout scrollQueue,
			String ds) {

		LinearLayout space = new LinearLayout(mContext);
		space.setPadding(0, 10, 0, 10);

		if ((!previous.equals(ds)) && (!previous.equals("")))
			scrollQueue.addView(space);

		return ds;
	}

	// Fills the text fields in the list element blocks
	private void makeBlock(View view, DataSet ds) {
		CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.name);
		ctv.setText(ds.getName() + " - " + ds.getType());

		TextView eid = (TextView) view.findViewById(R.id.experimentid);
		eid.setText(ds.getEID());

		TextView desc = (TextView) view.findViewById(R.id.description);
		desc.setText(ds.getDesc());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.upload:
			lastSID = -1;
			if (qpa.mirrorQueue.isEmpty()) {
				storeQueue(qpa.uploadQueue, qpa.parentName, qpa.mContext);
				setResult(RESULT_OK);
				finish();
				return;
			}
			else new UploadSDTask().execute();
			qpa.uploadQueue = new LinkedList<DataSet>();
			break;

		case R.id.cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}

	// Control task for uploading data from SD card
	class UploadSDTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected void onPreExecute() {
			OrientationManager.disableRotation(QueueUploader.this);

			DataSet ds = qpa.mirrorQueue.remove();
			createRunnable(ds);
			dia = new ProgressDialog(QueueUploader.this);
			dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dia.setMessage("Please wait while your data are uploaded to iSENSE...");
			dia.setCancelable(false);
			dia.show();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			sdUploader.run();
			dia.setProgress(100);
			return null;
		}

		@Override
		protected void onPostExecute(Void voids) {
			Waffle w = new Waffle(QueueUploader.mContext);

			if (uploadSuccess)
				w.make("Upload Success", Waffle.LENGTH_SHORT,
						Waffle.IMAGE_CHECK);
			else
				w.make("Upload failed.", Waffle.LENGTH_SHORT, Waffle.IMAGE_X);
			dia.dismiss();

			OrientationManager.enableRotation(QueueUploader.this);

			if (qpa.mirrorQueue.isEmpty()) {
				storeQueue(qpa.uploadQueue, qpa.parentName, qpa.mContext);
				setResult(RESULT_OK);
				finish();
				return;
			} else
				continueUploading();
		}
	}

	// Create an uploader particular to the DataSet to be uploaded in the queue
	void createRunnable(final DataSet ds) {
		sdUploader = new Runnable() {

			@Override
			public void run() {
				if (ds.isUploadable()) {
					uploadSuccess = ds.upload();
				} else {
					qpa.uploadQueue.add(ds);
				}
			}

		};

	}

	// Calls the next upload task if there are more DataSets in the queue
	private void continueUploading() {
		new UploadSDTask().execute();
	}

	// Saves Q_COUNT and uploadQueue into memory for later use
	public static void storeQueue(Queue<DataSet> queue, String parentName,
			Context context) {

		// Save Q_COUNT in SharedPrefs
		final SharedPreferences mPrefs = context.getSharedPreferences(
				parentName, Context.MODE_PRIVATE);
		final SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
		int Q_COUNT = queue.size();

		mPrefsEditor.putInt(parentName + "Q_COUNT", Q_COUNT);
		mPrefsEditor.commit();

		// writes uploadqueue.ser
		File uploadQueueFile = new File(
				Environment.getExternalStorageDirectory() + "/iSENSE/"
						+ parentName + ".ser");
		ObjectOutput out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(uploadQueueFile));

			// Serializes DataSets from uploadQueue into uploadqueue.ser
			while (Q_COUNT > 0) {
				DataSet ds = queue.remove();
				out.writeObject(ds);
				Q_COUNT--;
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Rebuilds uploadQueue from Q_COUNT and uploadqueue.ser
	public static Queue<DataSet> getUploadQueue(Queue<DataSet> uploadQueue,
			String parentName, Context context) {

		uploadQueue = new LinkedList<DataSet>();

		// Makes sure there is an iSENSE folder
		File folder = new File(Environment.getExternalStorageDirectory()
				+ "/iSENSE");
		if (!folder.exists())
			folder.mkdir();

		// Gets Q_COUNT back from Shared Prefs
		final SharedPreferences mPrefs = context.getSharedPreferences(
				parentName, Context.MODE_PRIVATE);
		int Q_COUNT = mPrefs.getInt(parentName + "Q_COUNT", 0);

		try {
			// Deserialize the file as a whole
			File file = new File(folder.getAbsolutePath() + "/" + parentName
					+ ".ser");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));
			// Deserialize the objects one by one
			for (int i = 0; i < Q_COUNT; i++) {
				DataSet dataSet = (DataSet) in.readObject();
				uploadQueue.add(dataSet);
			}
			in.close();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uploadQueue;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == DELETE_DATASET_REQUESTED) {
			if (resultCode == RESULT_OK) {
				qpa.uploadQueue.remove(lastDataSetLongClicked);
				qpa.mirrorQueue.remove(lastDataSetLongClicked);
				scrollQueue.removeView(lastViewLongClicked);
				storeQueue(qpa.uploadQueue, qpa.parentName, qpa.mContext);
			}
		}
		
	}
	
	

}
