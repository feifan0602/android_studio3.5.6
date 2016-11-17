package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;

public class CollectionTypeListAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> list;

	public CollectionTypeListAdapter(Context mContext, List<String> list) {
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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.collectiontype_itemlayout, null);
			mHolder = new ViewHolder();
			mHolder.mTitle = (TextView) arg1.findViewById(R.id.item_tv);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mHolder.mTitle.setText(list.get(arg0));
		return arg1;
	}

	class ViewHolder {
		TextView mTitle;
	}
}
