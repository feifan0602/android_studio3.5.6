package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.LookAdapter.ViewHolder;
import com.sun3d.culturalShanghai.object.LookInfo;
import com.sun3d.culturalShanghai.object.SearchListViewInfo;

public class SearchListViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<SearchListViewInfo> list;

	public SearchListViewAdapter(Context mContext, List<SearchListViewInfo> list) {
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

	public void setList(List<SearchListViewInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.searchlistview_item, null);
			mHolder.tv = (TextView) arg1.findViewById(R.id.tv);
			mHolder.tv.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mHolder.tv.setText(list.get(arg0).getAddress_Str());
		return arg1;
	}

	class ViewHolder {
		private TextView tv;

	}
}
