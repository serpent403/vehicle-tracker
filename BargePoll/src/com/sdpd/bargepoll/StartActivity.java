package com.sdpd.bargepoll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartActivity extends Activity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
			public void run() {
				 //TODO Auto-generated method stub
				Intent n = new Intent(StartActivity.this,MenuActivity.class);
				//n.putExtra("flag", flagVal);
				startActivity(n);
			}
        	
        }	
        ,5000);//delay of 5s
    
    
        
    }
	
}
