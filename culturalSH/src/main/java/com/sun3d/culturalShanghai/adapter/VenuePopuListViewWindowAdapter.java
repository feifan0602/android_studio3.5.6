package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VenuePopuListViewWindowAdapter extends BaseAdapter {
	private ArrayList<Wff_VenuePopuwindow> list;
	private Context co;

	public VenuePopuListViewWindowAdapter(ArrayList<Wff_VenuePopuwindow> list, Context con) {
		this.list = list;
		this.co = con;
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

		ViewHodler mHodler = null;
		if (arg1 == null) {
			mHodler = new ViewHodler();
			arg1 = View.inflate(co, R.layout.venue_listview_popwindow, null);
			mHodler.mName = (TextView) arg1.findViewById(R.id.venue_tv);
			mHodler.mName.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHodler);
		} else {
			mHodler = (ViewHodler) arg1.getTag();
		}
		mHodler.mName.setText(list.get(arg0).getTagName());
		return arg1;
	}

	private class ViewHodler {
		private TextView mName;

	}
}
