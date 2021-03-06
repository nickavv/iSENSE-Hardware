package edu.uml.cs.isense.gcollector.objects;

import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Application;
import android.content.Context;
import edu.uml.cs.isense.gcollector.R;
import edu.uml.cs.isense.comm.RestAPI;
import edu.uml.cs.isense.objects.ExperimentField;

public class DataFieldManager extends Application {

	int eid;
	RestAPI rapi;
	Context mContext;

	ArrayList<ExperimentField> expFields;
	public LinkedList<String> order;
	JSONArray dataSet;
	Fields f;
	SensorCompatibility sc = new SensorCompatibility();
	public boolean[] enabledFields = {  false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false, false };

	public DataFieldManager(int eid, RestAPI rapi, Context mContext, Fields f) {
		this.eid = eid;
		this.rapi = rapi;
		this.order = new LinkedList<String>();
		this.mContext = mContext;
		this.dataSet = new JSONArray();
		this.f = f;
	}

	public void getOrder() {
		expFields = rapi.getExperimentFields(eid);

		for (ExperimentField field : expFields) {
			switch (field.type_id) {

			// Temperature
			case 1:
				if (field.unit_name.toLowerCase().contains("f"))
					order.add(mContext.getString(R.string.temperature_f));
				else if (field.unit_name.toLowerCase().contains("c")) {
					order.add(mContext.getString(R.string.temperature_c));
				} else if (field.unit_name.toLowerCase().contains("k")) {
					order.add(mContext.getString(R.string.temperature_k));
				} else {
					order.add(mContext.getString(R.string.null_string));
				}
				break;
				
			// Potential Altitude
			case 2:
			case 3:
				if (field.field_name.toLowerCase().contains("altitude")) {
					order.add(mContext.getString(R.string.altitude));
				} else {
					order.add(mContext.getString(R.string.null_string));
				}
				break;

			// Time
			case 7:
				order.add(mContext.getString(R.string.time));
				break;

			// Light
			case 8:
			case 9:
			case 29:
				order.add(mContext.getString(R.string.luminous_flux));
				break;

			// Angle
			case 10:
				if (field.unit_name.toLowerCase().contains("deg")) {
					order.add(mContext.getString(R.string.heading_deg));
				} else if (field.unit_name.toLowerCase().contains("rad")) {
					order.add(mContext.getString(R.string.heading_rad));
				} else {
					order.add(mContext.getString(R.string.null_string));
				}
				break;

			// Geospacial
			case 19:
				if (field.field_name.toLowerCase().contains("lat")) {
					order.add(mContext.getString(R.string.latitude));
				} else if (field.field_name.toLowerCase().contains("lon")) {
					order.add(mContext.getString(R.string.longitude));
				} else {
					order.add(mContext.getString(R.string.null_string));
				}
				break;

			// Numeric/Custom
			case 21:
			case 22:
				if (field.field_name.toLowerCase().contains("mag")) {
					if (field.field_name.toLowerCase().contains("x")) {
						order.add(mContext.getString(R.string.magnetic_x));
					} else if (field.field_name.toLowerCase().contains("y")) {
						order.add(mContext.getString(R.string.magnetic_y));
					} else if (field.field_name.toLowerCase().contains("z")) {
						order.add(mContext.getString(R.string.magnetic_z));
					} else if ((field.field_name.toLowerCase()
							.contains("total"))
							|| (field.field_name.toLowerCase()
									.contains("average"))
							|| (field.field_name.toLowerCase().contains("mean"))) {
						order.add(mContext.getString(R.string.magnetic_total));
					}
				} else if (field.field_name.toLowerCase().contains("altitude")) {
					order.add(mContext.getString(R.string.altitude));
				} else
					order.add(mContext.getString(R.string.null_string));
				break;

			// Acceleration
			case 25:
				if (field.field_name.toLowerCase().contains("x")) {
					order.add(mContext.getString(R.string.accel_x));
				} else if (field.field_name.toLowerCase().contains("y")) {
					order.add(mContext.getString(R.string.accel_y));
				} else if (field.field_name.toLowerCase().contains("z")) {
					order.add(mContext.getString(R.string.accel_z));
				} else if ((field.field_name.toLowerCase().contains("total"))
						|| (field.field_name.toLowerCase().contains("average"))
						|| (field.field_name.toLowerCase().contains("mean"))) {
					order.add(mContext.getString(R.string.accel_total));
				} else {
					order.add(mContext.getString(R.string.null_string));
				}
				break;

			// Pressure
			case 27:
				order.add(mContext.getString(R.string.pressure));
				break;

			// No match (Just about every other category)
			default:
				order.add(mContext.getString(R.string.null_string));
				break;

			}

		}

	}

	public JSONArray putData() {

		JSONArray dataJSON = new JSONArray();

		for (String s : this.order) {
			try {
				if (s.equals(mContext.getString(R.string.accel_x))) {
					dataJSON.put(f.accel_x);
					continue;
				}
				if (s.equals(mContext.getString(R.string.accel_y))) {
					dataJSON.put(f.accel_y);
					continue;
				}
				if (s.equals(mContext.getString(R.string.accel_z))) {
					dataJSON.put(f.accel_z);
					continue;
				}
				if (s.equals(mContext.getString(R.string.accel_total))) {
					dataJSON.put(f.accel_total);
					continue;
				}
				if (s.equals(mContext.getString(R.string.temperature_c))) {
					dataJSON.put(f.temperature_c);
					continue;
				}
				if (s.equals(mContext.getString(R.string.temperature_f))) {
					dataJSON.put(f.temperature_f);
					continue;
				}
				if (s.equals(mContext.getString(R.string.temperature_k))) {
					dataJSON.put(f.temperature_k);
					continue;
				}
				if (s.equals(mContext.getString(R.string.time))) {
					dataJSON.put(f.timeMillis);
					continue;
				}
				if (s.equals(mContext.getString(R.string.luminous_flux))) {
					dataJSON.put(f.lux);
					continue;
				}
				if (s.equals(mContext.getString(R.string.heading_deg))) {
					dataJSON.put(f.angle_deg);
					continue;
				}
				if (s.equals(mContext.getString(R.string.heading_rad))) {
					dataJSON.put(f.angle_rad);
					continue;
				}
				if (s.equals(mContext.getString(R.string.latitude))) {
					dataJSON.put(f.latitude);
					continue;
				}
				if (s.equals(mContext.getString(R.string.longitude))) {
					dataJSON.put(f.longitude);
					continue;
				}
				if (s.equals(mContext.getString(R.string.magnetic_x))) {
					dataJSON.put(f.mag_x);
					continue;
				}
				if (s.equals(mContext.getString(R.string.magnetic_y))) {
					dataJSON.put(f.mag_y);
					continue;
				}
				if (s.equals(mContext.getString(R.string.magnetic_z))) {
					dataJSON.put(f.mag_z);
					continue;
				}
				if (s.equals(mContext.getString(R.string.magnetic_total))) {
					dataJSON.put(f.mag_total);
					continue;
				}
				if (s.equals(mContext.getString(R.string.altitude))) {
					dataJSON.put(f.altitude);
					continue;
				}
				if (s.equals(mContext.getString(R.string.pressure))) {
					dataJSON.put(f.pressure);
					continue;
				}
				dataJSON.put(null);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return dataJSON;

	}

	public String writeSdCardLine() {

		StringBuilder b = new StringBuilder();
		boolean start = true;

		for (String s : this.order) {

			if (s.equals(mContext.getString(R.string.accel_x))) {
				if (start)
					b.append(f.accel_x);
				else
					b.append(", ").append(f.accel_x);
				continue;
			}
			if (s.equals(mContext.getString(R.string.accel_y))) {
				if (start)
					b.append(f.accel_y);
				else
					b.append(", ").append(f.accel_y);
				continue;
			}
			if (s.equals(mContext.getString(R.string.accel_z))) {
				if (start)
					b.append(f.accel_z);
				else
					b.append(", ").append(f.accel_z);
				continue;
			}
			if (s.equals(mContext.getString(R.string.accel_total))) {
				if (start)
					b.append(f.accel_total);
				else
					b.append(", ").append(f.accel_total);
				continue;
			}
			if (s.equals(mContext.getString(R.string.temperature_c))) {
				if (start)
					b.append(f.temperature_c);
				else
					b.append(", ").append(f.temperature_c);
				continue;
			}
			if (s.equals(mContext.getString(R.string.temperature_f))) {
				if (start)
					b.append(f.temperature_f);
				else
					b.append(", ").append(f.temperature_f);
				continue;
			}
			if (s.equals(mContext.getString(R.string.temperature_k))) {
				if (start)
					b.append(f.temperature_k);
				else
					b.append(", ").append(f.temperature_k);
				continue;
			}
			if (s.equals(mContext.getString(R.string.time))) {
				if (start)
					b.append(f.timeMillis);
				else
					b.append(", ").append(f.timeMillis);
				continue;
			}
			if (s.equals(mContext.getString(R.string.luminous_flux))) {
				if (start)
					b.append(f.lux);
				else
					b.append(", ").append(f.lux);
				continue;
			}
			if (s.equals(mContext.getString(R.string.heading_deg))) {
				if (start)
					b.append(f.angle_deg);
				else
					b.append(", ").append(f.angle_deg);
				continue;
			}
			if (s.equals(mContext.getString(R.string.heading_rad))) {
				if (start)
					b.append(f.angle_rad);
				else
					b.append(", ").append(f.angle_rad);
				continue;
			}
			if (s.equals(mContext.getString(R.string.latitude))) {
				if (start)
					b.append(f.latitude);
				else
					b.append(", ").append(f.latitude);
				continue;
			}
			if (s.equals(mContext.getString(R.string.longitude))) {
				if (start)
					b.append(f.longitude);
				else
					b.append(", ").append(f.longitude);
				continue;
			}
			if (s.equals(mContext.getString(R.string.magnetic_x))) {
				if (start)
					b.append(f.mag_x);
				else
					b.append(", ").append(f.mag_x);
				continue;
			}
			if (s.equals(mContext.getString(R.string.magnetic_y))) {
				if (start)
					b.append(f.mag_y);
				else
					b.append(", ").append(f.mag_y);
				continue;
			}
			if (s.equals(mContext.getString(R.string.magnetic_z))) {
				if (start)
					b.append(f.mag_z);
				else
					b.append(", ").append(f.mag_z);
				continue;
			}
			if (s.equals(mContext.getString(R.string.magnetic_total))) {
				if (start)
					b.append(f.mag_total);
				else
					b.append(", ").append(f.mag_total);
				continue;
			}
			if (s.equals(mContext.getString(R.string.altitude))) {
				if (start)
					b.append(f.altitude);
				else
					b.append(", ").append(f.altitude);
				continue;
			}
			if (s.equals(mContext.getString(R.string.pressure))) {
				if (start)
					b.append(f.pressure);
				else
					b.append(", ").append(f.pressure);
				continue;
			}
				
			start = false;
			
		}

		b.append("\n");

		return b.toString();
	}

	public SensorCompatibility checkCompatibility() {

		int apiLevel = android.os.Build.VERSION.SDK_INT;
		int apiVal = 0;
		int[][] dispatch = sc.compatDispatch;

		if (apiLevel <= 8)
			apiVal = 0;
		if (apiLevel > 8 && apiLevel < 14)
			apiVal = 1;
		if (apiLevel > 14)
			apiVal = 2;

		for (int i = 0; i <= 5; i++) {
			int temp = dispatch[apiVal][i];
			if (temp == 1)
				sc.compatible[i] = true;
		}

		return sc;
	}
	
	public String writeHeaderLine() {
		StringBuilder b = new StringBuilder();
		boolean start = true;
		
		for (String unitName : this.order) {
			if (start)
				b.append(unitName);			
			else 
				b.append(", ").append(unitName);
			
			start = false;
		}
		
		b.append("\n");
		
		return b.toString();
	}

}
