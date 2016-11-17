package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Utils;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyOrderInfo;

public class MyNewSeatAdapter extends BaseAdapter {
	private Context mContext;
	private String[] seatNum;
	public List<MyOrderInfo> list;
	private int len;

	public MyNewSeatAdapter(Context mContext,
			List<MyOrderInfo> list, int len) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.len = len;
		this.seatNum = Utils.initSeats(list.get(len).getBookInfo().getActivitySeats());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return seatNum.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SeatViewHolder svh = null;
		if (convertView == null) {
			svh = new SeatViewHolder();
			convertView = View.inflate(mContext,
					R.layout.fragment_new_gridview_item, null);
			svh.mTitle = (TextView) convertView.findViewById(R.id.but_check);
			svh.butText = (TextView) convertView.findViewById(R.id.but_text);
			convertView.setTag(svh);
		} else {
			svh = (SeatViewHolder) convertView.getTag();
		}

		final MyActivityBookInfo info = list.get(len).getBookInfo();
		Log.i("MyNewSeatAdapter--" + position, info.toString());
		
		if(info.getOrderPayStatus().equals("5")){
			svh.mTitle.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
			svh.mTitle.setBackground(mContext.getResources().getDrawable(R.drawable.shape_item_order_item_gray));
		}
		if (info.getoSummary().length > 0) {
			svh.butText.setText(seatNum[position].toString());
		}

		return convertView;
	}

	class SeatViewHolder {
		private TextView butText;
		private TextView mTitle;
	}

}
