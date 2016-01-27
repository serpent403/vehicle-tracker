package com.testc2dmservice;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay<MyOverlayItem> {

	private Context mContext;
	private ArrayList<MyOverlayItem> mOverlays = new ArrayList<MyOverlayItem>();

	public HelloItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	public void addOverlay(MyOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	public void removeOverlay(MyOverlayItem overlay) {
		mOverlays.remove(overlay);
		populate();
	}

	public void removeAll() {
		mOverlays.removeAll(mOverlays);
		populate();

	}

	@Override
	protected MyOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		DbHelper dbHelper = new DbHelper(mContext);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		MyOverlayItem item = mOverlays.get(index);
		String[] whereParams = new String[] { item.getTitle() };
		Barge barge = Mapview.statlist.get(Integer.parseInt(item.getTitle())-1);
		String etime = "";
		if(!barge.expArrivalTime.equals("NIL")){
		Double eat = Double.parseDouble(barge.expArrivalTime);
		int k=0,m=0,n=0; 
		if (eat != 0){
		 k = (int) (eat/3600.00);
		 Double l = eat%3600.00 ;
		 if (l!=0){
		 m = (int) (l/60.00);
		 n = (int) (l%60.00);
		 }
		}
		etime = k +" Hrs "+ m+" Mins " + n+ " Secs";
		}
		else etime = "Barge is Stagnent";
		Cursor crsr = db.query(DbHelper.TABLE, null, DbHelper.B_ID + "=?",
				whereParams, null, null, null);
		String bargeDetails = "";
		if (crsr.moveToLast()) {
			bargeDetails = item.getTitle()+ "aaaa" +"Barge ID : "+crsr.getString(crsr.getColumnIndex(DbHelper.B_ID)) + "\n"
					+"Barge Name : "+ barge.name
					+ "\n"
					+"Barge Latitude : "+ barge.lat
					+ "\n"
					+"Barge Longitude : "+ barge.lng
					+ "\nSpeed : "+barge.speed+ " Km/hr"
					+"\n Expected Arrival Time : " + etime 
					+"\nLast Notification Received : "+ crsr.getString(crsr.getColumnIndex(DbHelper.B_TIME))
					+ "\n"
					+"Recent Notification : "+ crsr.getString(crsr.getColumnIndex(DbHelper.B_MESSAGE))
					+ "\n"+"Status : ";
				if( crsr.getString(crsr.getColumnIndex(DbHelper.B_TROUBLED)).equals("1")){
					bargeDetails = bargeDetails + "Troubled";
				}
				else{
					bargeDetails = bargeDetails + "Not Troubled";
				}
		} else
			bargeDetails = item.getTitle() + "aaaa" + "Barge Name : "+"barge_" + item.getTitle()
					+ "\n"
					+"Barge Latitude : "+ barge.lat
					+ "\n"
					+"Barge Longitude : "+ barge.lng
					+ "\nSpeed : "+barge.speed+ " Km/hr"
					+"\nExpected Arrival Time : " + etime					
					+ "\n"+"No Recent Notifications";

		db.close();
		dbHelper.close();
		Intent intent = new Intent(mContext, BargeDetails.class);
		intent.putExtra("bargeDetails", bargeDetails);
		// intent.putExtra("bargeDetails", "This is a Dialog");
		mContext.startActivity(intent);
		return true;
	}
	
	
	@Override
    public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow)
    {
        super.draw(canvas, mapView, shadow);

        if (shadow == false)
        {
            //cycle through all overlays
            for (int index = 0; index < mOverlays.size(); index++)
            {
                OverlayItem item = mOverlays.get(index);

                // Converts lat/lng-Point to coordinates on the screen
                GeoPoint point = item.getPoint();
                Point ptScreenCoord = new Point() ;
                mapView.getProjection().toPixels(point, ptScreenCoord);

                //Paint
                Paint paint = new Paint();
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(20);
                paint.setARGB(150, 0, 0, 0); // alpha, r, g, b (Black, semi see-through)

                //show text to the right of the icon
                canvas.drawText(item.getTitle(), ptScreenCoord.x, ptScreenCoord.y+5, paint);
            }
        }
    }

	
	
	

	
	
	
	
	
	
	
	
	
	

}