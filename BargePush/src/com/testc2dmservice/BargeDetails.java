package com.testc2dmservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class BargeDetails extends Activity {

	TextView tv, text;
	Button call, message, done;
	String phoneNumber;
	SharedPreferences sharedPrefs;
	String bargeId, prefKey;
	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	Context mContext = this;
	EditText takeno;
	long a = 0;
	boolean finish;
	String determine;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bargedetails);

		finish = false;
		tv = (TextView) findViewById(R.id.textView3);
		String details = (getIntent()).getStringExtra("bargeDetails");		
		String bargeDetails[] = details.split("aaaa");
		bargeId = bargeDetails[0];
		tv.setText(bargeDetails[1]);
		viewInit();
		assign();
	}

	private void viewInit() {
		call = (Button) findViewById(R.id.call);
		message = (Button) findViewById(R.id.message);
		sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		prefKey = "barge_" + bargeId + "_num";
		phoneNumber = sharedPrefs.getString(prefKey, "");
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_dialog,
				(ViewGroup) findViewById(R.id.layout_root));

		text = (TextView) layout.findViewById(R.id.text);
		done = (Button) layout.findViewById(R.id.sendno);
		takeno = (EditText) layout.findViewById(R.id.takeno);
		text.setText("phone no. is not available please enter the no.");
		ImageView image = (ImageView) layout.findViewById(R.id.image);
		image.setImageResource(R.drawable.barge);
		builder = new AlertDialog.Builder(mContext);
		builder.setView(layout);
		alertDialog = builder.create();
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phoneNumber = takeno.getText().toString();
				check();
			}
		});

	}

	private void assign() {

		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				determine = "call";
				if (phoneNumber.equals("0")) {
					alertDialog.show();
				} else
					check();

			}
		});

		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				determine = "message";
				if (phoneNumber.equals("0")) {
					alertDialog.show();
				} else
					check();

			}
		});
	}

	private void sendIntent() {
		if (determine.equals("call")) {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:+91" + phoneNumber));
			startActivity(callIntent);
		} else if (determine.equals("message")) {
			String smsText = "";
			Uri uri = Uri.parse("smsto:" + "+91" + phoneNumber);
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra("sms_body", smsText);
			startActivity(intent);
		}

		finish();
	}

	public void check() {
		boolean bln = true;
		try {
			a = Long.parseLong(phoneNumber);
		} catch (Exception ex) {
			text.setText("Please enter only Integers");
			alertDialog.show();
			bln = false;
		}
		if (((int) Math.log10(a) + 1) == 10) {
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString(prefKey, phoneNumber);
			editor.commit();
			sendIntent();
		} else {
			if (bln) {
				text.setText("U Have entered a wrong no.");
				alertDialog.show();
			}
		}

	}
}
