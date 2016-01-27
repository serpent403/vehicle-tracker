package com.testc2dmservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.IsolatedContext;
import android.util.Log;

public class Poll {

	ArrayList<Barge> bargelist = new ArrayList<Barge>();
	SharedPreferences sharedPrefs;
	int VALUE_BARGES = 6;

	public ArrayList<Barge> poll(Context context) {
		// to remove any previous data in the hash list
		// fillMaps.clear();

		// get the update barges details from the preferences
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		boolean[] barge_update = { sharedPrefs.getBoolean("barge_1", false),
				sharedPrefs.getBoolean("barge_2", false),
				sharedPrefs.getBoolean("barge_3", false),
				sharedPrefs.getBoolean("barge_4", false),
				sharedPrefs.getBoolean("barge_5", false),
				sharedPrefs.getBoolean("barge_6", false)

		};

		BufferedReader in = null;

		// this string saves the json format string echoed by php script
		String result = "";
		try {

			HttpClient client = new DefaultHttpClient();

			HttpGet request = new HttpGet();

			try {
				request.setURI(new URI(
						"http://www.goyalrajat.in/barge/bargeloc.php"));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			// the HTTP response info or code generated after the request is
			// executed
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			result = sb.toString();
			Log.i("res", result);
			// System.out.println(page);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// parse the json string
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				// with the help of this json object obtain all the query
				// records
				// one by one by looping
				JSONObject json_data = jArray.getJSONObject(i);

				Barge barge = new Barge();
				barge.id = Integer.toString(json_data.getInt("id"));
				barge.name = json_data.getString("name");
				barge.lat = Double.toString(json_data.getDouble("cur_lat"));
				barge.lng = Double.toString(json_data.getDouble("cur_lng"));
				barge.speed = Double.toString(json_data.getDouble("speed"));
				try {
					json_data.getDouble("t_eat");
//					Double temp = json_data.getDouble("t_eat");
//					temp = temp/3600.00;//converting to hrs
					barge.expArrivalTime = Double.toString(json_data.getDouble("t_eat"));
				}
				catch (Exception ex){
					barge.expArrivalTime = "NIL";
				}
				if (json_data.getInt("trb_flag") == 1)
					barge.troubled = true;
				else
					barge.troubled = false;

				if (barge_update[i]) {
					bargelist.add(barge);
				}

			}
		} catch (JSONException e) {
		}
		return bargelist;

	}
}
