package com.testc2dmservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

public class C2DMRegistrationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {

			// get registration_id here
			// the registration_id keeps changing so we need a mechanism to keep
			// updating it at the server
			final String registrationId = intent
					.getStringExtra("registration_id");
			// String error = intent.getStringExtra("error");

			// get ANDROID_ID or device_id here[use it to identify a device in
			// the DB]
			String deviceId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);

			// custom method to notify that the registration was a success
			createNotification(context, registrationId);

			// sending the registrationId and deviceId to server by executing it
			// in background using AsynchTask
			new SendPushNotification()
					.execute("http://www.goyalrajat.in/barge/send.php?registrationid="
							+ URLEncoder.encode(registrationId.toString())
							+ "&deviceid="
							+ URLEncoder.encode(deviceId).toString());

			// Save registration_id in the preference to be able to show it
			// later
			saveRegistrationId(context, registrationId);

		}

	}

	public void createNotification(Context context, String registrationId) {

		// You do not instantiate the NotifManag class directly; instead,
		// retrieve it through getSystemService(String).
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Notification class represents how a persistent notification is to be
		// presented to
		// the user using the NotificationManager.
		// This constructor is deprecated. Use Notification.Builder instead.
		Notification notification = new Notification(R.drawable.icon,
				"Registration successful", System.currentTimeMillis());

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// create a new intent object to go to the RegistrationResultActivity
		// Class when the notification is clicked
		Intent intent = new Intent(context, RegistrationResultActivity.class);

		// pass the registration_id to this Activity
		intent.putExtra("registration_id", registrationId);

		// must create a pending intent as it is a parameter for the next line
		// of code
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		// put the message and pass the pending intent that needs to be notified
		notification.setLatestEventInfo(context, "Registration",
				"Successfully registered", pendingIntent);

		// sends the notification to the user
		notificationManager.notify(0, notification);

	}

	private void saveRegistrationId(Context context, String registrationId) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		// instantiate an Editor object to change the preference
		Editor edit = prefs.edit();

		// edit.putString(key, value)
		edit.putString("authentication", registrationId);

		// commits any change in the preference
		edit.commit();

	}

}

// an aynchronous task to be executed when the registrationId is sent to the
// sever
class SendPushNotification extends AsyncTask<String, Void, Void> {

	// task to performed in the background when the application is running
	protected Void doInBackground(String... urls) {

		BufferedReader in = null;

		try {

			HttpClient client = new DefaultHttpClient();

			HttpGet request = new HttpGet();

			try {
				request.setURI(new URI(urls[0]));

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
			String page = sb.toString();
			System.out.println(page);
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
		return null;
	}

	protected void onPostExecute() {

	}

}
