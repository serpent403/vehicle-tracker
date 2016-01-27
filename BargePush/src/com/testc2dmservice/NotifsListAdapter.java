package com.testc2dmservice;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotifsListAdapter extends BaseAdapter {

	private Context context;
	private List<Barge> notifsList;
	private int selectedPosition;

	public NotifsListAdapter(Context context, List<Barge> ContactList) {
		this.context = context;
		this.notifsList = ContactList;
		this.selectedPosition = NO_SELECTION;

	}

	public int getSelectedPosition() {
		return this.selectedPosition;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notifsList.size();
	}

	@Override
	public Barge getItem(int position) {
		// TODO Auto-generated method stub
		return this.notifsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			NotifsView temp = new NotifsView(context);
			temp.setIdentity(notifsList.get(position).name);
			temp.setMessage(notifsList.get(position).message);
			temp.setTime(notifsList.get(position).time);
			return temp;
		} else {
			NotifsView temp = (NotifsView) convertView;
			temp.setIdentity(notifsList.get(position).name);
			temp.setMessage(notifsList.get(position).message);
			temp.setTime(notifsList.get(position).time);
			// ((ContactView)
			// convertView).set(ContactList.get(position).getContactRate());
			// from second item onwards similar object like the object we
			// created above will be sent while calling this method
		}
		return convertView;
	}

}
