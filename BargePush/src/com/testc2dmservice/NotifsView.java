package com.testc2dmservice;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifsView extends LinearLayout {
	public TextView tv1, tv2, tv3;

	public NotifsView(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.contactview, this, true);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textViewTime);
	}

	public void setIdentity(String identity) {
		tv1.setText(identity);
	}

	public void setMessage(String message) {
		tv2.setText(message);
	}

	public void setTime(String time) {
		tv3.setText(time);
	}

}
