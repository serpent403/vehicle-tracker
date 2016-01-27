package com.testc2dmservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OptionScreen extends Activity {
	/*-----------------------------------------------------------*/
	Button but1, but2, about, settings, help;
	SharedPreferences sp, sp_barge_1;

	/*-----------------------------------------------------------*/
	Context c = this;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			Intent intent = getIntent();
			if (intent.getStringExtra("from").equals("notifications")) {
				Intent intnt = new Intent(this, DataBasesActivity.class);
				startActivity(intnt);
			}
		} catch (Exception ex) {

		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("firstRun", true)) {
			register();
			prefs.edit().putBoolean("firstRun", false).commit();
			C2DMMessageReceiver.firstMessage = true;
		}
		init();

		/*---------------------------------------------------------*/
		// get the shared preferences
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

		// notifications
		if (sp.contains("notifications")) {
			if (sp.getBoolean("notifications", false)) {

			} else {
			}

		} else {
		}

		// barge updates
		if (sp.getBoolean("barge_1", false)) {
		} else {
		}

		// barges phone number

		/*---------------------------------------------------------*/

	}

	/*-----------------------------------------------------------*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit
			LayoutInflater inflater = (LayoutInflater) c
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog_end,
					(ViewGroup) findViewById(R.id.layout_root));
			TextView text = (TextView) layout.findViewById(R.id.text_end);
			Button yes = (Button) layout.findViewById(R.id.yes);
			Button no = (Button) layout.findViewById(R.id.no);
			text.setText("Are you sure you want to Quit!");
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.barge);

			AlertDialog.Builder builder = new AlertDialog.Builder(c);
			builder.setView(layout);
			alertDialog = builder.create();
			yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					OptionScreen.this.finish();
				}
			});
			no.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					alertDialog.dismiss();
				}
			});
			alertDialog.show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	/*-----------------------------------------------------------*/

	public void init() {
		but1 = (Button) findViewById(R.id.button1);
		but2 = (Button) findViewById(R.id.button2);
		/*-----------------------------------------------------------*/
		settings = (Button) findViewById(R.id.b_settings);
		help = (Button) findViewById(R.id.b_help);
		about = (Button)findViewById(R.id.b_about);
		/*-----------------------------------------------------------*/
		assign();
	}

	public void assign() {

		// click to view barges on map
		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent().setClass(c, Mapview.class);
				intent.putExtra("from", "OptionScreen");
				startActivity(intent);
			}

		});

		// click to view inbox
		but2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent().setClass(c,
						DataBasesActivity.class);
				startActivity(intent);
			}

		});

		/*-----------------------------------------------------------*/
		// click to view preferences
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent().setClass(c, PrefsActivity.class);
				startActivity(intent);
			}

		});
		/*-----------------------------------------------------------*/
		
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent().setClass(c, Help.class);
				startActivity(intent);
			}

		});

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent().setClass(c, About.class);
				startActivity(intent);
			}

		});

		
		
		
		
	}

	public void register() {

		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");

		intent.putExtra("app",
				PendingIntent.getBroadcast(this, 0, new Intent(), 0));

		// Sender currently not used[This changes for different users]
		intent.putExtra("sender", "rtc2dmtest@gmail.com");

		startService(intent);

	}

}
