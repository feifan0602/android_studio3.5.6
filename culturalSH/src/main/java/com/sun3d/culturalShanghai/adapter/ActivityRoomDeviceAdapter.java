package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActivityRoomDeviceAdapter extends BaseAdapter {
	private ArrayList<String> list;
	private Context con;

	public ActivityRoomDeviceAdapter(ArrayList<String> list, Context con) {
		this.list = list;
		this.con = con;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = null;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(con).inflate(
					R.layout.activity_room_device, null);
			vh.tv = (TextView) arg1.findViewById(R.id.tv);
			vh.tv.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		vh.tv.setText(list.get(arg0));
		// TODO Auto-generated method stub
		return arg1;
	}

	class ViewHolder {
		TextView tv;
	};

}
