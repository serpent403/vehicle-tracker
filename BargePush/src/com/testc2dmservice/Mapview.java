package com.testc2dmservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Mapview extends MapActivity {
	MapView mapView;
	MenuItem itema,itemb,itemc;
	//	Button all, troubled, particular;
	MyOverlayItem[] overlays;
	public static int numberOfBarges;
	HelloItemizedOverlay itemizedoverlay;
	List<Overlay> mapOverlays;
	ArrayList<Barge> bargelist;
	public static ArrayList<Barge> statlist;
	int[] chkbxarray;
	ProgressDialog progress;
	Context mContext = this;
	AlertDialog alertDialog;
	MyTask myTask;//AsyncTask
	Timer t;
	boolean flag=true;
	String view = "all";
	Context c = this;
	SQLiteDatabase db;
	DbHelper dbHelper;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Log.i("cycle", "onCreate");
		mapView = (MapView) findViewById(R.id.mapview);

//		Log.i("cycle" , getCallingActivity().toString());
		
		progress = new ProgressDialog(mContext);
		progress.setMessage("Loading...");
		progress.show();
		if (!isNetworkAvailable()) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.dialog_connection,
					(ViewGroup) findViewById(R.id.layout_root));
			TextView text = (TextView) layout.findViewById(R.id.text_con);
			Button ok = (Button) layout.findViewById(R.id.ok);
			text.setText("NetWork Not Available");
			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.barge);

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setView(layout);
			alertDialog = builder.create();
			ok.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Mapview.this.finish();
				}
			});
			progress.dismiss();
			alertDialog.show();
		} else{

			myTask = new MyTask(progress);
			myTask.execute();

		}
		
		/*new Timer().scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Poll poll = new Poll();
				bargelist = poll.poll(mContext);
				if (numberOfBarges != 0) {
					itemizedoverlay.removeAll();
					if (view.equals("all")) {						
						itema.isEnabled();
					} else if (view.equals("troubled")) {
						
					} else {
						
					}
					
				}
			}
		}, 11000, 10000);

*/
		registerForContextMenu(mapView);

	}

	
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	
    	Log.i("cycle", "onStop");
//    	t.cancel();
//    	Log.i("refresh", "Refreshing cancelled!");
    
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	
    	
    	Log.i("cycle", "onDestroy");
    	
    }
 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.all_barges:
			view = "all";
			if (numberOfBarges != 0) {
				itemizedoverlay.removeAll();
				putAllBarges();
			}

			return true;

		case R.id.trb_barges:
			view = "particular";
			if (numberOfBarges != 0) {
				itemizedoverlay.removeAll();
				putTroubledBarges();
			}            

			return true;

		case R.id.select_barges:
			view = "selected";
			if (numberOfBarges != 0) {
				Intent intent = new Intent().setClass(
						getApplicationContext(), SelectBarge.class);
				startActivityForResult(intent, 0);
			}

			return true;
	
			
			
		default:
			return super.onOptionsItemSelected(item);


		}


	}

	// this Mapview activity has been called from the options screen
	private void putAllBarges() {
		for (int i = 0; i < numberOfBarges; i++) {
			GeoPoint point = new GeoPoint((int) (Double.parseDouble(bargelist
					.get(i).lat) * 1E6), (int) (Double.parseDouble(bargelist
							.get(i).lng) * 1E6));
			overlays[i] = new MyOverlayItem(point, bargelist.get(i).id,
					bargelist.get(i).name, mContext, bargelist.get(i).troubled,
					false);

			itemizedoverlay.addOverlay(overlays[i]);

		}

		// add the overlays to the map
		mapOverlays.add(itemizedoverlay);

		mapView.invalidate();

	}

	// adding troubled barges to the map
	private void putTroubledBarges() {
		for (int i = 0; i < numberOfBarges; i++) {
			if (bargelist.get(i).troubled) {
				GeoPoint point = new GeoPoint(
						(int) (Double.parseDouble(bargelist.get(i).lat) * 1E6),
						(int) (Double.parseDouble(bargelist.get(i).lng) * 1E6));
           			overlays[i] = new MyOverlayItem(point, bargelist.get(i).id,
						bargelist.get(i).name, mContext,
						bargelist.get(i).troubled, false);
				itemizedoverlay.addOverlay(overlays[i]);

			}

		}

		// add the overlays to the map
		mapOverlays.add(itemizedoverlay);

		mapView.invalidate();

	}
	
	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK && !isNetworkAvailable()) {
			alertDialog.dismiss();
			this.finish();
		}
		else if(keyCode == KeyEvent.KEYCODE_BACK){
		myTask.cancel(true);
		progress.dismiss();
		this.finish();
		}
		return false;
	}
*/
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// requestCode is what we gave while calling the activity, above
		// startActivityForResult(intent, 0); u can see here it is zero
		if (resultCode == Activity.RESULT_OK) {
			chkbxarray = data.getIntArrayExtra("chkbxarray");
			putSelectedBarges();
		}
	}

	private void putSelectedBarges() {
		itemizedoverlay.removeAll();
		for (int i = 0; i < numberOfBarges; i++) {
			if (chkbxarray[i] == 1) {
				GeoPoint point = new GeoPoint(
						(int) (Double.parseDouble(bargelist.get(i).lat) * 1E6),
						(int) (Double.parseDouble(bargelist.get(i).lng) * 1E6));
				overlays[i] = new MyOverlayItem(point, bargelist.get(i).id,
						bargelist.get(i).name, mContext,
						bargelist.get(i).troubled, false);
				itemizedoverlay.addOverlay(overlays[i]);

			}
		}

		// add the overlays to the map
		mapOverlays.add(itemizedoverlay);

		mapView.invalidate();

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class MyTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progress;

		public MyTask(ProgressDialog progress) {
			this.progress = progress;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (numberOfBarges != 0) {
				mapView.setBuiltInZoomControls(true);
				MapController myMapController = mapView.getController();
				myMapController.setCenter(new GeoPoint((int) (15.406 * 1E6),
						(int) (73.773 * 1E6)));
				myMapController.setZoom(13);
				if (getIntent().getStringExtra("from").equals(
						"DataBasesActivity")) {
					putSelectedBarges();
				}
				else putAllBarges();
				statlist = bargelist;
			}


			progress.dismiss();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			// checking for network

			// xml resources retrieved here
			//			inti();

			// assign click listeners to the 3 filter buttons
			//			assign();

			// poll the barges details from the server
			Poll poll = new Poll();

			// retrieve the barge details as a List of Barge objects
			bargelist = poll.poll(mContext);

			// get the number of barges in the server
			numberOfBarges = bargelist.size();

			// an array of MyOverlayItems
			if (numberOfBarges != 0) {


				// allows the user to zoom in or out of the Map

				mapOverlays = mapView.getOverlays();

				// get the overlay icon from the res/drawable folder to be drawn
				// on the map
				Drawable drawable = mContext.getResources().getDrawable(
						R.drawable.overlay_green);

				itemizedoverlay = new HelloItemizedOverlay(drawable, mContext);
				overlays = new MyOverlayItem[numberOfBarges];
			}

			// returns the intent that started this activity
			Intent intent = getIntent();
			// this Mapview activity has been called from the inbox
			if (intent.getStringExtra("from").equals("DataBasesActivity")) {
				int bargeid = Integer.parseInt(intent.getStringExtra("id"));
				chkbxarray = new int[numberOfBarges];
				// in the chkbxarray set the selected barge value as 1 and the
				// remaining as 0
				for (int z = 0; z < numberOfBarges; z++) {
					if (z + 1 == bargeid)
						chkbxarray[z] = 1;
					else
						chkbxarray[z] = 0;
				}
			}
			return null;

		}


	}




}



