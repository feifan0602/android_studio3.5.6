package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.object.ActivityOtherInfo;

public class OtherListAdapter extends BaseAdapter {
	private Context mContext;
	private List<ActivityOtherInfo> list;
	private ViewHolder_Main vh_main = null;
	private String TAG = "OtherListAdapter";
	/**
	 * 0为活动 1为场馆
	 */
	private int mType;
	private ViewHolder_Venue main_vh;
	private View Venue_view;

	public OtherListAdapter(Context mContext, List<ActivityOtherInfo> list,
			int type) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.mType = type;
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

	public void setList(List<ActivityOtherInfo> list1) {
		this.list = list1;
		Log.i(TAG, "size==  " + list.size());
		notifyDataSetChanged();
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = addMainActivity(pos, convertView);
		return convertView;
	}

	private View addMainActivity(int pos, View convertView) {
		if (convertView == null) {
			vh_main = new ViewHolder_Main();
			convertView = View.inflate(mContext,
					R.layout.otherlistadapter_item, null);
			vh_main.img = (ImageView) convertView.findViewById(R.id.img);
			vh_main.tv = (TextView) convertView.findViewById(R.id.tv);
			vh_main.top_left_tv = (TextView) convertView
					.findViewById(R.id.top_left_tv);
			vh_main.tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.top_left_tv.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh_main);
		} else {
			vh_main = (ViewHolder_Main) convertView.getTag();
		}
		if (list.size() != 0) {
			ActivityOtherInfo lookInfo = list.get(pos);
			if (lookInfo.getTagName().equals("")
					|| lookInfo.getTagName().equals("null")) {
				vh_main.top_left_tv.setVisibility(View.INVISIBLE);
			} else {
				vh_main.top_left_tv.setVisibility(View.VISIBLE);
				vh_main.top_left_tv.setText(lookInfo.getTagName());
			}

			LayoutParams para;
			para = (LayoutParams) vh_main.img.getLayoutParams();
			// 设置
			para.width = MyApplication.getWindowWidth() / 2;
			para.height = (para.width * 2) / 3;
			vh_main.img.setLayoutParams(para);
			Log.i(TAG,
					"  图片 大小  ==  " + MyApplication.Img_Path
							+ lookInfo.getActivityIconUrl());
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlSmall(MyApplication.Img_Path
									+ lookInfo.getActivityIconUrl()),
							vh_main.img);
			if (!lookInfo.getActivityName().equals("")
					&& !lookInfo.getActivityName().equals("null")) {
				vh_main.tv.setVisibility(View.VISIBLE);
				vh_main.tv.setText(lookInfo.getActivityName());
			} else {
				vh_main.tv.setVisibility(View.GONE);
			}

		}
		return convertView;
	}

	class ViewHolder_Main {
		TextView top_left_tv;
		TextView tv;
		ImageView img;

	}

	class ViewHolder_Venue {
		TextView middle_tv;
		TextView name_tv;
		TextView address_tv;
		TextView activity_tv;
		TextView room_tv;
		ImageView space_bg_iv;
		TextView center_tv;
	}
}
