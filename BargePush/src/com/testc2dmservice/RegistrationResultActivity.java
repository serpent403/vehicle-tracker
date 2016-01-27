package com.testc2dmservice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class RegistrationResultActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_result);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			// obtain the resistration_id from the C2DMRegistrationReceiver
			// Class
			String registrationId = extras.getString("registration_id");

			// check whether the registration_id is valid
			if (registrationId != null && registrationId.length() > 0) {
				// set the id in a text view
				TextView view = (TextView) findViewById(R.id.result);
				view.setText(registrationId);
			}
		}

		super.onCreate(savedInstanceState);

	}

}