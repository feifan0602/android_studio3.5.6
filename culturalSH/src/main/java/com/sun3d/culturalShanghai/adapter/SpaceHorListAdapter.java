package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.SpaceListAdapter.ViewHolder_Banner;
import com.sun3d.culturalShanghai.object.SpaceInfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("ResourceAsColor") public class SpaceHorListAdapter extends BaseAdapter {
	private ArrayList<SpaceInfo> mList;
	private Context mContext;
	private ViewHolder vh;

	public SpaceHorListAdapter(Context context, ArrayList<SpaceInfo> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.space_item_hor_adapter, null);
			vh.tv = (TextView) convertView.findViewById(R.id.tv);
			vh.tv.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (mList.get(arg0).isIndex()) {
			vh.tv.setTextColor(Color.parseColor("#494D5B"));
			vh.tv.setTextSize(16f);
			vh.tv.setText(mList.get(arg0).getAdvertTitle());
		} else {
			vh.tv.setTextColor(Color.parseColor("#5E6D98"));
			vh.tv.setTextSize(17f);
			vh.tv.setText(mList.get(arg0).getAdvertTitle());
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

}
