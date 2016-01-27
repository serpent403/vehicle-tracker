package com.testc2dmservice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MyOverlayItem extends OverlayItem {

	Context mContext;
	boolean troubled, atDock;

	public MyOverlayItem(GeoPoint point, String title, String snippet,
			Context context, boolean trbled, boolean atdck) {
		super(point, title, snippet);
		mContext = context;
		troubled = trbled;
		atDock = atdck;
	}

	@Override
	public Drawable getMarker(int stateBitset) {
		Drawable drawable;
		try {
			if (troubled) {
				drawable = mContext.getResources().getDrawable(
						R.drawable.overlay_red);
				drawable.setBounds(-drawable.getIntrinsicWidth() / 2,
						-drawable.getIntrinsicHeight(),
						drawable.getIntrinsicWidth() / 2, 0);
				return drawable;
			} else if (atDock) {
				drawable = mContext.getResources().getDrawable(
						R.drawable.overlay_blue);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				return drawable;
			} else {
				drawable = mContext.getResources().getDrawable(
						R.drawable.overlay_green);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				return drawable;
			}

		} catch (Exception e) {

		}

		return null;
	}

}