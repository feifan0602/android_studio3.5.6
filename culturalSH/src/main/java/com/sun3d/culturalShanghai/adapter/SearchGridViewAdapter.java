package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.object.SearchHotInfo;
import com.sun3d.culturalShanghai.object.SearchInfo;
import com.sun3d.culturalShanghai.windows.EventWindows;
import com.sun3d.culturalShanghai.windows.VenueWindows;

public class SearchGridViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> list;

	public SearchGridViewAdapter(Context mContext, List<String> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();

	}

	public void setData(List<String> Hotlist) {
		this.list = Hotlist;
		notifyDataSetChanged();
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHodler mHodler = null;
		if (arg1 == null) {
			mHodler = new ViewHodler();
			arg1 = View.inflate(mContext, R.layout.search_gridadapter_item,
					null);
			mHodler.mName = (TextView) arg1.findViewById(R.id.name);
			mHodler.adapter_windows_ll = (LinearLayout) arg1
					.findViewById(R.id.adapter_windows_ll);
			mHodler.mName.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHodler);
		} else {
			mHodler = (ViewHodler) arg1.getTag();
		}
		mHodler.mName.setText(list.get(arg0));
		return arg1;
	}

	private class ViewHodler {
		private TextView mName;
		private View mLine;
		private LinearLayout adapter_windows_ll;
	}

}
