package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.SpaceListAdapter.ViewHolder_Banner;
import com.sun3d.culturalShanghai.object.Space_Pop_Right_Info;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpacePopRightAdapter extends BaseAdapter {
	private ArrayList<Wff_VenuePopuwindow> mList;
	private Context mContext;
	private ViewHolder vh;

	public SpacePopRightAdapter(Context context,
			ArrayList<Wff_VenuePopuwindow> list) {
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
					R.layout.space_pop_item_right_adapter, null);
			vh.tv = (TextView) convertView.findViewById(R.id.tv);
			vh.tv.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv.setText(mList.get(arg0).getTagName());
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

}
