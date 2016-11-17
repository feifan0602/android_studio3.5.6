package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;

public class RoomReserveGridAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> list;
	private int specing = 30;
	private int initindex = 0;
	private String[] StrStatus;// 0.已过期 1.未过期

	public RoomReserveGridAdapter(Context mContext, List<String> list, String[] StrStatus) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.StrStatus = StrStatus;
	}

	public void setData(List<String> list, String[] StrStatus) {
		this.list = list;
		this.StrStatus = StrStatus;
		this.notifyDataSetChanged();

	}

	public void setinitIndex(int index) {
		initindex = index;
		this.notifyDataSetChanged();
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
		TextView text = new TextView(mContext);
		text.setText(list.get(arg0));
		text.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		text.setBackgroundResource(R.drawable.shape_white_rect);
		text.setPadding(DensityUtil.px2dip(mContext, specing),
				DensityUtil.px2dip(mContext, specing), DensityUtil.px2dip(mContext, specing),
				DensityUtil.px2dip(mContext, specing));
		text.setTextColor(mContext.getResources().getColor(R.color.dialog_content_color));
		if (initindex == arg0) {
			text.setBackgroundResource(R.drawable.shape_orge_rect);
			text.setTextColor(mContext.getResources().getColor(R.color.white_color));
		}
		if (StrStatus != null && StrStatus[arg0] != null && StrStatus[arg0].equals("0")) {

			text.setBackgroundColor(0xffd2d2d2);
			text.setTextColor(mContext.getResources().getColor(R.color.event_text_color));
		}

		return text;
	}

}
