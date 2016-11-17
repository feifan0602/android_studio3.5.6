package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.ActivityKillAdapter.ViewHolder;
import com.sun3d.culturalShanghai.object.ActivityOrderDetailInfo;
import com.sun3d.culturalShanghai.object.MyOrderDetailInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActivityOrderDetailAdapter extends BaseAdapter {
	private List<ActivityOrderDetailInfo> list;
	private Context mContext;

	public ActivityOrderDetailAdapter(Context mContext,
			List<ActivityOrderDetailInfo> list) {
		super();
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
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.my_order_detail, null);
			mHolder = new ViewHolder();
			mHolder.left_tv = (TextView) arg1.findViewById(R.id.left_tv);
			mHolder.right_tv = (TextView) arg1.findViewById(R.id.rigth_tv);
			mHolder.left_tv.setTypeface(MyApplication.GetTypeFace());
			mHolder.right_tv.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mHolder.left_tv.setText(list.get(arg0).getKey());
		if (list.get(arg0).getKey().equals("积    分:")) {
			mHolder.right_tv.setTextColor(mContext.getResources().getColor(
					R.color.text_color_5E));
			mHolder.right_tv.setText("- " + list.get(arg0).getValue());
		} else if (list.get(arg0).getKey().equals("座    位:")) {
			mHolder.right_tv.setTextColor(mContext.getResources().getColor(
					R.color.text_color_45));
			mHolder.right_tv.setText(list.get(arg0).getValue());
		} else {
			mHolder.right_tv.setText(list.get(arg0).getValue());
		}

		return arg1;
	}

	class ViewHolder {
		TextView left_tv;
		TextView right_tv;
	}
}
