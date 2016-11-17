package com.sun3d.culturalShanghai.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.SeatInfo;

public class SeatGridAdapter extends BaseAdapter {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<SeatInfo> list;
	private String[] RealySeat;

	public SeatGridAdapter(Context mContext, List<SeatInfo> list, String RealySeat) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.RealySeat = RealySeat.split(",");
	}

	public void setData(List<SeatInfo> list) {
		this.list = list;
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
		ViewHolder mHolder = null;
		// arg1 = m.get(arg0);
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.setitem_layout, null);
			mHolder = new ViewHolder();
			mHolder.mSeat = (TextView) arg1.findViewById(R.id.seat_view);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}

		SeatInfo mSeatInfo = list.get(arg0);
		if (mSeatInfo.getIsSeat()) {
			String seat = mSeatInfo.getSeatRow() + "_" + mSeatInfo.getSeatColumn();
			mHolder.mSeat.setText("");
			mHolder.mSeat.setTag("false");
			for (String str : RealySeat) {
				if (seat.equals(str)) {
					mSeatInfo.setSeatStatus(4);
					mHolder.mSeat.setTag("true");
				}
			}
			setSeatBg(mHolder.mSeat, mSeatInfo.getSeatStatus());

		} else {
			mHolder.mSeat.setText(String.valueOf(mSeatInfo.getRowNumber()));
			mHolder.mSeat.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));

		}
		// m.put(arg0, arg1);
		return arg1;
	}

	private void setSeatBg(View mSeat, int status) {
		switch (status) {
		case 1:// 正常
			mSeat.setBackgroundColor(mContext.getResources().getColor(R.color.seat_red_color));
			break;
		case 2:// 已售
			mSeat.setBackgroundColor(mContext.getResources().getColor(R.color.seat_gray_color));
			break;
		case 3:// 占用
			mSeat.setBackgroundColor(mContext.getResources().getColor(R.color.seat_while_color));
			break;
		case 4:// 已选
			mSeat.setBackgroundColor(mContext.getResources().getColor(R.color.seat_green_color));
			break;
		default:
			break;
		}
	}

	private class ViewHolder {
		private TextView mSeat;
	}
}
