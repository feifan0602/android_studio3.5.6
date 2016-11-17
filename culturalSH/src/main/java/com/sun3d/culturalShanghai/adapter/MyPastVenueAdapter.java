package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.MyVenueInfo;

public class MyPastVenueAdapter extends BaseAdapter {
	private Context mContext;
	private List<MyVenueInfo> list;

	public MyPastVenueAdapter(Context mContext, List<MyVenueInfo> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
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

	public void setList(List<MyVenueInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.fragment_past_venue_item, null);
			mHolder = new ViewHolder();
			mHolder.mTitle = (TextView) arg1.findViewById(R.id.venue_title);
			mHolder.mTime = (TextView) arg1.findViewById(R.id.venue_time);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		MyVenueInfo info = list.get(arg0);
		mHolder.mTitle.setText(info.getVenueName());
		mHolder.mTime.setText(info.getRoomOrderCreateTime());
		return arg1;
	}

	class ViewHolder {
		private TextView mTitle;
		private TextView mTime;
	}
}
