package edu.uml.cs.isense.waffle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.uml.cs.isense.R;

public class Waffle {
	public boolean dontToastMeTwice;
	public boolean exitAppViaBack;
	private Context mContext;

	public Waffle(Context c) {
		this.dontToastMeTwice = false;
		this.exitAppViaBack = false;
		this.mContext = c;
	}

	@SuppressLint("NewApi")
	public void make(String message, int length, String image_id) {

		if (!dontToastMeTwice) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = null;
			if (image_id.equals("check"))
				layout = inflater.inflate(R.layout.toast_layout_check, null);
			else if (image_id.equals("x"))
				layout = inflater.inflate(R.layout.toast_layout_x, null);
			else
				return;

			/*ImageView image = (ImageView) layout.findViewById(R.id.image);
			if (image_id.equals("check"))
				image.setImageResource(R.drawable.checkmark);
			else
				image.setImageResource(R.drawable.red_x);*/

			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText(message);

			Toast toast = new Toast(mContext);
			toast.setGravity(Gravity.BOTTOM, 0, 50);
			if (length == Toast.LENGTH_SHORT)
				toast.setDuration(Toast.LENGTH_SHORT);
			else
				toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();

			new NoToastTwiceTask().execute();
		}

	}

	@SuppressLint("NewApi")
	private class NoToastTwiceTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected void onPreExecute() {
			dontToastMeTwice = true;
			exitAppViaBack = true;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			try {
				Thread.sleep(1500);
				exitAppViaBack = false;
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				exitAppViaBack = false;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void voids) {
			dontToastMeTwice = false;
		}
	}

}
