package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.ActivityRoomAdapter.ViewHolderData;
import com.sun3d.culturalShanghai.handler.CodeInfo;
import com.sun3d.culturalShanghai.object.ActivityListRoomInfo;
import com.sun3d.culturalShanghai.object.KillInfo;
import com.sun3d.culturalShanghai.object.MessageInfo;
import com.sun3d.culturalShanghai.object.RoomDateInfo;
import com.sun3d.culturalShanghai.object.RoomDetailTimeSlotInfor;

public class ActivityRoomTimesAdapter extends BaseAdapter {
	private Context mContext;
	private List<ActivityListRoomInfo> list;
	private static final int TYPE_COUNT = 2;// item类型的总数
	private static final int TYPE_DATA = 0;// 日期
	private static final int TYPE_MAIN = 1;// 主類型
	private int currentType;// 当前item类型
	private View itemView;
	private int mBgColor;
	private Map<String, Boolean> map;

	public ActivityRoomTimesAdapter(Context mContext,
			List<ActivityListRoomInfo> list, Map<String, Boolean> map1) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.map = map1;
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

	public void setList(List<ActivityListRoomInfo> list) {
		this.list = list;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return TYPE_DATA;// 第一项
		} else {
			return TYPE_MAIN;// 广告类型
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		currentType = getItemViewType(pos);
		if (currentType == TYPE_DATA) {
			convertView = addFristView(pos, convertView);
		} else if (currentType == TYPE_MAIN) {
			convertView = addMainView(pos, convertView);
		}
		return convertView;

	}

	private View addFristView(int pos, View convertView) {
		ViewHolderData mHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.activity_room_date_tv_item, null);
			mHolder = new ViewHolderData();
			mHolder.text = (TextView) convertView.findViewById(R.id.text_frist);
			mHolder.text.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolderData) convertView.getTag();
		}
		String data = null;
		try {
			data = MyApplication.formatFractionalPart(MyApplication
					.dayForWeek(list.get(pos).getCurDate()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (data != null) {
			mHolder.text.setText(list.get(pos).getCurDate().split("-")[1] + "."
					+ list.get(pos).getCurDate().split("-")[2] + "   周" + data);
		}

		return convertView;
	}

	public View addMainView(final int pos, View view) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (view == null) {
			view = View.inflate(mContext,
					R.layout.activity_room_date_listview_item, null);
			mHolder = new ViewHolder();
			mHolder.time = (TextView) view.findViewById(R.id.time);
			mHolder.stutas = (TextView) view.findViewById(R.id.stutas);
			mHolder.bg = (LinearLayout) view.findViewById(R.id.bg);
			mHolder.time.setTypeface(MyApplication.GetTypeFace());
			mHolder.stutas.setTypeface(MyApplication.GetTypeFace());
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		if (list.get(pos).getBookStatus().equals("2")) {
			mHolder.bg.setBackgroundResource(R.color.text_color_f2eb);
			mHolder.stutas.setText("已被预订");
		} else if (list.get(pos).getBookStatus().equals("1")) {

			if (map.get(list.get(pos).getBookId()) == true) {
				// 未选中
				mHolder.bg.setBackgroundResource(R.color.text_color_dd);
				mHolder.time.setTextColor(mContext.getResources().getColor(
						R.color.text_color_26));
				mHolder.stutas.setTextColor(mContext.getResources().getColor(
						R.color.text_color_26));
			} else {
				// 选中的颜色
				mHolder.bg.setBackgroundResource(R.color.text_color_72);
				mHolder.time.setTextColor(mContext.getResources().getColor(
						R.color.text_color_ff));
				mHolder.stutas.setTextColor(mContext.getResources().getColor(
						R.color.text_color_ff));
			}

		} else if (list.get(pos).getBookStatus().equals("3")) {
			mHolder.bg.setBackgroundResource(R.color.text_color_f2);
			mHolder.stutas.setText("不开放");
		}
		mHolder.time.setText(list.get(pos).getOpenPeriod());

		return view;
	}

	class ViewHolder {
		TextView time;
		TextView stutas;
		LinearLayout bg;
	}

	class ViewHolderData {
		TextView text;
	};

	/**
	 * 改变listitem的背景色
	 * 
	 * @param view
	 */
	private void itemBackChanged(View view) {
		if (itemView == null) {
			itemView = view;
		}
		mBgColor = R.color.text_color_003b;
		itemView.setBackgroundResource(mBgColor);
		mBgColor = R.color.text_color_72;
		// 将上次点击的listitem的背景色设置成透明
		view.setBackgroundResource(mBgColor);
		// 设置当前点击的listitem的背景色
		itemView = view;

	}
}
