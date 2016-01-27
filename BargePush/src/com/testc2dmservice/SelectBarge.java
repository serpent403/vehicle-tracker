package com.testc2dmservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class SelectBarge extends Activity {
	CheckBox[] chkbox;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getbarge);

		chkbox = new CheckBox[6];
		chkbox[0] = (CheckBox) findViewById(R.id.checkBox1);
		chkbox[1] = (CheckBox) findViewById(R.id.checkBox2);
		chkbox[2] = (CheckBox) findViewById(R.id.checkBox3);
		chkbox[3] = (CheckBox) findViewById(R.id.checkBox4);
		chkbox[4] = (CheckBox) findViewById(R.id.checkBox5);
		chkbox[5] = (CheckBox) findViewById(R.id.checkBox6);
		Button done = (Button) findViewById(R.id.button1);

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int[] chkbxarray = new int[6];
				for (int i = 0; i < 6; i++) {
					if (chkbox[i].isChecked())
						chkbxarray[i] = 1;
					else
						chkbxarray[i] = 0;
				}
				Intent intent = new Intent();
				intent.putExtra("chkbxarray", chkbxarray);
				// intent.putExtra("foodName",
				// this.foodName.getText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();

			}

		});

	}

}
