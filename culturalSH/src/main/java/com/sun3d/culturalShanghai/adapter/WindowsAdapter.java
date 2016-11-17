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
import com.sun3d.culturalShanghai.object.SearchInfo;
import com.sun3d.culturalShanghai.windows.EventWindows;
import com.sun3d.culturalShanghai.windows.VenueWindows;

public class WindowsAdapter extends BaseAdapter {
	private Context mContext;
	private List<SearchInfo> list;

	public WindowsAdapter(Context mContext, List<SearchInfo> list) {
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHodler mHodler = null;
		if (arg1 == null) {
			mHodler = new ViewHodler();
			arg1 = View.inflate(mContext, R.layout.adapter_window_item, null);
			mHodler.mName = (TextView) arg1.findViewById(R.id.name);
			mHodler.mName.setTypeface(MyApplication.GetTypeFace());
			mHodler.adapter_windows_ll = (LinearLayout) arg1
					.findViewById(R.id.adapter_windows_ll);
			
			arg1.setTag(mHodler);
		} else {
			mHodler = (ViewHodler) arg1.getTag();
		}
		final SearchInfo info = list.get(arg0);
		mHodler.mName.setText(info.getTagName());
		// mHodler.adapter_windows_ll.setOnClickListener(new
		// View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// if (MainFragmentActivity.mTabType==0){
		// intent.setClass(mContext, EventListActivity.class);
		// intent.putExtra("searchType", "eventwindows");
		// intent.putExtra("activityInfo", EventWindows.activityInfo);
		// intent.putExtra("tagId",info.getTagId());
		// mContext.startActivity(intent);
		// }else {
		// list.remove(0);
		// SearchInfo info = list.get(arg0);
		// intent.setClass(mContext, VenueListActivity.class);
		// intent.putExtra(AppConfigUtil.INTENT_SCREENINFO,
		// VenueWindows.screenInfo);
		// intent.putExtra("tagId",info.getTagId());
		// mContext.startActivity(intent);
		// }
		//
		// }
		// });
		if (info.isSeleced()) {
			mHodler.mName.setTextColor(mContext.getResources().getColor(
					R.color.white_color));
			mHodler.mName.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.shape_window_item_red_rect));
		} else {
			mHodler.mName.setTextColor(mContext.getResources().getColor(
					R.color.windows_text_color));
			mHodler.mName.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.shape_window_item_white_rect));
		}
		// if (arg0 == list.size()) {
		// mHodler.mLine.setVisibility(View.GONE);
		// } else {
		// mHodler.mLine.setVisibility(View.VISIBLE);
		// }
		return arg1;
	}

	private class ViewHodler {
		private TextView mName;
		private View mLine;
		private LinearLayout adapter_windows_ll;
	}

}
