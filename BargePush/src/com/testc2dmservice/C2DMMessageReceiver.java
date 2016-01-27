package com.testc2dmservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class C2DMMessageReceiver extends BroadcastReceiver {

	SQLiteDatabase db;
	DbHelper dbHelper;
	SharedPreferences sPrefs;
	public static boolean firstMessage = false;

	@Override
	public void onReceive(Context context, Intent intent) {

		// obtain the action of the intent that is broadcasted to this
		// application
		String action = intent.getAction();

		// compare the action with the desired action
		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {

			/*
			 * the data is sent from the app server to the c2dm server in a
			 * key/value pair fashion that is sent again to the application in
			 * the same fashion in the form of an intent The data is obtained
			 * from the intent by specifying the appropriate keys used in the
			 * server
			 */
			String payload = intent.getStringExtra("payload");
			// TODO Send this to my application server to get the real data
			// Lets make something visible to show that we received the message

			// display the payload(message) as a notification to the user
			if (firstMessage) {
				firstMessage = false;
			} else
				addToDb(context, payload);

		}
	}

	private void addToDb(Context context, String payload) {
		// get the update barges details from the preferences
		sPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		// get the boolean of whether the user wants update at status bar or not
		boolean notif = sPrefs.getBoolean("notifications", false);

		// get the booleans of all the barge update conditions
		// the boolean of barge_num is in barge_update[num-1]
		boolean[] barge_update = { sPrefs.getBoolean("barge_1", false),
				sPrefs.getBoolean("barge_2", false),
				sPrefs.getBoolean("barge_3", false),
				sPrefs.getBoolean("barge_4", false),
				sPrefs.getBoolean("barge_5", false),
				sPrefs.getBoolean("barge_6", false)

		};

		String[] bargeNotification = payload.split("/");

		dbHelper = new DbHelper(context);
		db = dbHelper.getReadableDatabase();
		ContentValues values = new ContentValues();

		values.put(DbHelper.B_ID, bargeNotification[0]);
		values.put(DbHelper.B_NAME, bargeNotification[1]);
		values.put(DbHelper.B_LAT, bargeNotification[2]);
		values.put(DbHelper.B_LONG, bargeNotification[3]);
		values.put(DbHelper.B_TROUBLED, bargeNotification[4]);
		values.put(DbHelper.B_TIME, bargeNotification[5]);
		values.put(DbHelper.B_MESSAGE, bargeNotification[6]);

		// checking if the user wants to gets notification from this barge or
		// not
		// if the user wants the notifs from this barge then the barge details
		// must be added to the db
		// or else just ignore it
		String barge_name = bargeNotification[1];
		String numChar = "" + barge_name.charAt(barge_name.length() - 1);
		numChar = numChar.trim();
		int num = Integer.parseInt(numChar);

		if (barge_update[num - 1]) {
			// insert barge details into database and notify the user
			db.insert(DbHelper.TABLE, null, values);
			if (notif) {
				// give notification at the status bar
				createNotification(context);

			}

		}

		db.close();
		dbHelper.close();

	}

	// method to display the message as a notification to the user
	public void createNotification(Context context) {

		// You do not instantiate the NotificationManager class directly;
		// instead,
		// retrieve it through getSystemService(String) method.
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Notification class represents how a persistent notification is to be
		// presented to
		// the user using the NotificationManager.
		// This constructor is deprecated. Use Notification.Builder instead.
		Notification notification = new Notification(R.drawable.icon,
				"Message received", System.currentTimeMillis());

		// Hide the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// create a new intent object go to the MessgaeResultActivity Class when
		// the notification is clicked
		Intent intent = new Intent(context, OptionScreen.class);
		intent.putExtra("from", "notifications");

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		// put the message and pass the pending intent that needs to be notified
		notification.setLatestEventInfo(context, "Message",
				"New message received", pendingIntent);

		// sends the notification to the user
		notificationManager.notify(0, notification);

	}

}
