package com.ruikong.funcloud;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.media.Ringtone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomBellAdapter extends BaseAdapter {
	List<AudioRec> list;
	Context context;

	public CustomBellAdapter(ArrayList<AudioRec> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_bell, null);
		}
		TextView tvBell = (TextView) convertView
				.findViewById(R.id.tv_bell_item);
		TextView tvtime = (TextView) convertView
				.findViewById(R.id.tv_time);
		AudioRec audio = list.get(position);
		tvBell.setText(audio.getFileName());
		if (audio.getIsUpload()){
			tvBell.setTextColor(0xff00d210);
			tvtime.setTextColor(0xff00d210);
		}
		else
		{
			tvBell.setTextColor(0xff555555);
			tvtime.setTextColor(0xff555555);
		}

		tvtime.setText(audio.getTime());

		return convertView;
	}

}
