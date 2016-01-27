package com.testc2dmservice;

import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DataBasesActivity extends Activity {
	/** Called when the activity is first created. */

//	Button all, troubled, particular;
	SQLiteDatabase db;
	DbHelper dbHelper;
	private ArrayList<Barge> notifsList, troublednotifsList,
			selectednotifsList;
	private NotifsListAdapter allViewadapter, troubledViewadapter,
			selectedViewadap;
	private ListView listView;
	int numberofbarges = Mapview.numberOfBarges;
	String display, qryString;
	Context mContext = this;
	ProgressDialog progress;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listactivity);
		
		listView = (ListView) findViewById(R.id.listView1);
		
		progress = new ProgressDialog(mContext);
		progress.setMessage("Loading...");
		progress.show();
		new MyTask(progress).execute();
		registerForContextMenu(listView);
	
	}
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.menu_inbox, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	        
	        case R.id.all_inbox:
	        	Log.d("Tag","Came in all barges");
//	            Toast.makeText(this, "That's good", Toast.LENGTH_LONG).show();
				listView.setAdapter(allViewadapter);
				allViewadapter.notifyDataSetChanged();
				display = "all";
	                	
	            return true;
	        
	        case R.id.trb_inbox:
	        	Log.d("Tag","Came in trb barges");
//	        	Toast.makeText(this, "Get Life", Toast.LENGTH_LONG).show();
				listView.setAdapter(troubledViewadapter);
				troubledViewadapter.notifyDataSetChanged();
				display = "troubled";
	        	
	        	return true;
	        
	        case R.id.select_inbox:
	        	Log.d("Tag","Came in select barges");
//	        	Toast.makeText(this, "Why?", Toast.LENGTH_LONG).show();
				display = "selected";
				Intent intent = new Intent().setClass(getApplicationContext(),
						SelectBarge.class);
				startActivityForResult(intent, 0);
	       	
	        	return true;
	        case R.id.erase_inbox:
	        	Log.d("Tag","Came in erase inbox");	    	
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.dialog_end,
						(ViewGroup) findViewById(R.id.layout_root));
				TextView text = (TextView) layout.findViewById(R.id.text_end);
				Button yes = (Button) layout.findViewById(R.id.yes);
				Button no = (Button) layout.findViewById(R.id.no);
				text.setText("Are you sure you want to erase the inbox?!");
				ImageView image = (ImageView) layout.findViewById(R.id.image);
				image.setImageResource(R.drawable.barge);

				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setView(layout);
				alertDialog = builder.create();
				yes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {						
						//<ERASE DATABASE HERE>
						alertDialog.dismiss();
						db.delete(DbHelper.TABLE, null, null);
						notifsList.clear();
						troublednotifsList.clear();
						selectednotifsList.clear();
						listView.setAdapter(allViewadapter);
						allViewadapter.notifyDataSetChanged();
						display = "all";
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
	        
	        default:
	            return super.onOptionsItemSelected(item);

	    
	        }
	   
	    
	    }
	  

	private void viewInit() {
		// TODO Auto-generated method stub
//		all = (Button) findViewById(R.id.notifall);
//		troubled = (Button) findViewById(R.id.notiftroubled);
//		particular = (Button) findViewById(R.id.notifparticular);
		
/*	
		all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listView.setAdapter(allViewadapter);
				allViewadapter.notifyDataSetChanged();
				display = "all";

			}
		});
		troubled.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listView.setAdapter(troubledViewadapter);
				troubledViewadapter.notifyDataSetChanged();
				display = "troubled";
			}
		});
		particular.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				display = "selected";
				Intent intent = new Intent().setClass(getApplicationContext(),
						SelectBarge.class);
				startActivityForResult(intent, 0);
			}
		});
*/	
	
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (display.equals("all")) {
					sendIntent(notifsList.get(position));
				} else if (display.equals("troubled")) {
					sendIntent(troublednotifsList.get(position));

				} else if (display.equals("selected")) {
					sendIntent(selectednotifsList.get(position));
				}
				return false;
			}

		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			int[] chkbxarray = data.getIntArrayExtra("chkbxarray");
			qryString = "";
			boolean setString = true;
			for (int k = 0; k < chkbxarray.length; k++) {
				if (chkbxarray[k] == 1) {
					int p = k + 1;
					if (setString) {
						setString = false;
						qryString = qryString + DbHelper.B_ID + "=" + p;
					} else
						qryString = qryString + " OR " + DbHelper.B_ID + "="
								+ p;
				}
			}
			// lop.execute("");
			queries();
		}
	}

	private void query() {
		Cursor crsr = db.query(DbHelper.TABLE, null, null, null, null, null,
				null);
		// Barge barge = new Barge();
		Barge barge;
		if (crsr.moveToFirst()) {
			do {
				barge = null;
				barge = new Barge();
				barge.id = crsr.getString(crsr.getColumnIndex(DbHelper.B_ID));
				barge.name = crsr.getString(crsr
						.getColumnIndex(DbHelper.B_NAME));
				barge.lat = crsr.getString(crsr.getColumnIndex(DbHelper.B_LAT));
				barge.lng = crsr
						.getString(crsr.getColumnIndex(DbHelper.B_LONG));
				barge.troubled = false;
				barge.time = crsr.getString(crsr
						.getColumnIndex(DbHelper.B_TIME));
				barge.message = crsr.getString(crsr
						.getColumnIndex(DbHelper.B_MESSAGE));
				if (Integer.parseInt(crsr.getString(crsr
						.getColumnIndex(DbHelper.B_TROUBLED))) == 1) {
					barge.troubled = true;
					troublednotifsList.add(barge);
				}
				notifsList.add(barge);
			} while (crsr.moveToNext());
		}
		Collections.reverse(notifsList);
		Collections.reverse(troublednotifsList);

	}

	private void queries() {
		Cursor crsr = db.query(DbHelper.TABLE, null, qryString, null, null,
				null, null);
		selectednotifsList.clear();
		Barge barge;
		if (crsr.moveToFirst()) {
			do {
				barge = new Barge();
				barge.id = crsr.getString(crsr.getColumnIndex(DbHelper.B_ID));
				barge.name = crsr.getString(crsr
						.getColumnIndex(DbHelper.B_NAME));
				barge.lat = crsr.getString(crsr.getColumnIndex(DbHelper.B_LAT));
				barge.lng = crsr
						.getString(crsr.getColumnIndex(DbHelper.B_LONG));
				barge.troubled = false;
				barge.time = crsr.getString(crsr
						.getColumnIndex(DbHelper.B_TIME));
				barge.message = crsr.getString(crsr
						.getColumnIndex(DbHelper.B_MESSAGE));
				if (Integer.parseInt(crsr.getString(crsr
						.getColumnIndex(DbHelper.B_TROUBLED))) == 1) {
					barge.troubled = true;
				}
				selectednotifsList.add(barge);
			} while (crsr.moveToNext());
		}
		Collections.reverse(selectednotifsList);
		listView.setAdapter(selectedViewadap);
		selectedViewadap.notifyDataSetChanged();

	}

	private void sendIntent(Barge barge) {
		Intent intent = new Intent(getApplicationContext(), Mapview.class);
		intent.putExtra("from", "DataBasesActivity");
		intent.putExtra("id", barge.id);
		/*
		 * intent.putExtra("name",barge.name); intent.putExtra("lat",barge.lat);
		 * intent.putExtra("lng",barge.lng);
		 * intent.putExtra("troubled",barge.troubled);
		 * intent.putExtra("time",barge.time);
		 * intent.putExtra("message",barge.message);
		 */
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// close databases
		db.close();
		dbHelper.close();

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
			progress.dismiss();
//			all.performClick();
			listView.setAdapter(allViewadapter);
			allViewadapter.notifyDataSetChanged();
			display = "all";

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			notifsList = new ArrayList<Barge>();
			troublednotifsList = new ArrayList<Barge>();
			selectednotifsList = new ArrayList<Barge>();
			allViewadapter = new NotifsListAdapter(mContext, notifsList);
			troubledViewadapter = new NotifsListAdapter(mContext,
					troublednotifsList);
			selectedViewadap = new NotifsListAdapter(mContext,
					selectednotifsList);
			dbHelper = new DbHelper(mContext);
			db = dbHelper.getWritableDatabase();
			display = "all";
			viewInit();
			query();
			return null;
			
		}
	
	}
	

}

