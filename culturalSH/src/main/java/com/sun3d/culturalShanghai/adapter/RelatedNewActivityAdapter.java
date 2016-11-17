package com.sun3d.culturalShanghai.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.EventDetailsActivity;
import com.sun3d.culturalShanghai.handler.Tab_labelHandler;
import com.sun3d.culturalShanghai.object.EventInfo;

@SuppressLint("UseSparseArrays")
public class RelatedNewActivityAdapter extends BaseAdapter implements
		OnTouchListener {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<EventInfo> list;
	private Boolean isShowView = false;
	private boolean isShowAddress;

	public RelatedNewActivityAdapter(Context mContext, List<EventInfo> list,
			boolean isShowAddress) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		isShowView = false;
		this.isShowAddress = isShowAddress;
	}

	public void isShowFootView(Boolean isShow) {
		isShowView = isShow;
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

	public void setList(List<EventInfo> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Log.d("getView", "arg0:" + arg0);
		convertView = addMainView(arg0, convertView);
		return convertView;
	}

	private View addMainView(final int arg0, View convertView) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.adapter_related_activity_list_item, null);
			mHolder.img = (ImageView) convertView.findViewById(R.id.item_img);
			mHolder.title = (TextView) convertView
					.findViewById(R.id.item_title);
			mHolder.item_sub = (TextView) convertView
					.findViewById(R.id.item_sub);
			mHolder.time = (TextView) convertView.findViewById(R.id.item_time);
			mHolder.label = (ImageView) convertView
					.findViewById(R.id.item_label);
			mHolder.address = (TextView) convertView
					.findViewById(R.id.item_address);
			mHolder.value = (TextView) convertView
					.findViewById(R.id.item_value);
			mHolder.foot = (TextView) convertView.findViewById(R.id.footview);
			mHolder.tiket_reserve = (TextView) convertView
					.findViewById(R.id.item_reserve);
			mHolder.tiketcount = (TextView) convertView
					.findViewById(R.id.item_tiketcount);
			mHolder.tiketcountLayout = (LinearLayout) convertView
					.findViewById(R.id.item_tiketcount_layout);
			mHolder.item_toplayout = (LinearLayout) convertView
					.findViewById(R.id.item_toplayout);
			mHolder.item_mapaddress = (RelativeLayout) convertView
					.findViewById(R.id.item_mapaddress);
			mHolder.title.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_sub.setTypeface(MyApplication.GetTypeFace());
			mHolder.time.setTypeface(MyApplication.GetTypeFace());
			mHolder.address.setTypeface(MyApplication.GetTypeFace());
			mHolder.value.setTypeface(MyApplication.GetTypeFace());
			mHolder.foot.setTypeface(MyApplication.GetTypeFace());
			mHolder.tiket_reserve.setTypeface(MyApplication.GetTypeFace());
			mHolder.tiketcount.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		final EventInfo eventInfo = list.get(arg0);

		if (eventInfo.getActivityAbleCount() != null
				&& eventInfo.getActivityAbleCount().length() > 0) {
			mHolder.tiketcount.setText(eventInfo.getActivityAbleCount());
		} else {
			mHolder.tiketcount.setText("无");
		}
		if (eventInfo.getActivityIsReservation().equals("1")) {
			mHolder.tiketcountLayout.setVisibility(View.GONE);
			mHolder.tiket_reserve.setVisibility(View.GONE);
		} else {
			mHolder.tiket_reserve.setVisibility(View.VISIBLE);
			mHolder.tiketcountLayout.setVisibility(View.VISIBLE);
		}
		if (list.get(arg0).getEventPrice().equals("0")
				| list.get(arg0).getEventPrice().length() == 0) {
			mHolder.value.setText("免费");
			mHolder.value.setCompoundDrawables(null, null, null, null);
		} else {
			// mHolder.value.setText("免费");
			mHolder.value.setText(list.get(arg0).getEventPrice() + "元/人");
		}

		if (eventInfo.getActivityRecommend().equals("Y")) {// 最热
//			mHolder.label
//					.setImageResource(R.drawable.sh_icon_event_list_hot_icon);
			mHolder.label.setVisibility(View.GONE);
		} else if (eventInfo.getActivityRecommend().equals("N")) {// 最新
			mHolder.label
					.setImageResource(R.drawable.sh_icon_event_list_new_icon);
			mHolder.label.setVisibility(View.GONE);
		} else {
			mHolder.label.setVisibility(View.GONE);
		}
		// mHolder.value.setText(eventInfo.getActivityJoinMethod());
		mHolder.item_toplayout.setTag(eventInfo);
		mHolder.item_mapaddress.setTag(eventInfo);
		String start_time = eventInfo.getActivityStartTime();
		String end_time = eventInfo.getEventEndTime();
		start_time = start_time.replaceAll("-", ".").substring(5, 10);
		end_time = end_time.replaceAll("-", ".").substring(5, 10);
		if (!eventInfo.getActivityStartTime().equals(
				eventInfo.getEventEndTime())) {
			mHolder.time.setText(start_time + "-" + end_time);
		} else {
			mHolder.time.setText(start_time);
		}
		mHolder.title.setText(eventInfo.getEventName());
		mHolder.item_sub.setText(eventInfo.getTagName().replaceAll(",", " "));
		mHolder.address.setText("地址:" + eventInfo.getEventAddress());
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlSmall(eventInfo.getEventIconUrl()),
						mHolder.img, Options.getListOptions());
		mHolder.item_toplayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onclick(arg0);
			}
		});
		mHolder.item_mapaddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onclick(arg0);
			}
		});
		if (arg0 == list.size() - 1) {
			if (isShowView) {
				mHolder.foot.setVisibility(View.VISIBLE);
			}
		} else {
			mHolder.foot.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView title;
		TextView time;
		ImageView label;
		TextView address;
		TextView value;
		TextView item_sub;
		TextView tiketcount;
		TextView tiket_reserve;
		LinearLayout item_toplayout;
		LinearLayout tiketcountLayout;
		RelativeLayout item_mapaddress;
		TextView foot;
	}

	private void onclick(View arg0) {
		if (ButtonUtil.isDelayClick()) {
			EventInfo eventInfo = (EventInfo) arg0.getTag();
			Intent intent = new Intent(mContext, ActivityDetailActivity.class);
			intent.putExtra("eventId", eventInfo.getEventId());
			mContext.startActivity(intent);
		}
	}

	private float index_X, index_Y;
	private Boolean isClick = true;

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			index_X = arg1.getRawX();
			index_Y = arg1.getRawY();
			break;
		case MotionEvent.ACTION_UP:
			if (Math.abs(arg1.getRawY() - index_Y) < 20) {
				if (Math.abs(arg1.getRawX() - index_X) < 20) {
					onclick(arg0);
				}
			}

			break;

		default:
			break;
		}
		return true && isClick;
	}

	public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
		mOnMyClickListener = onMyClickListener;
	}

	private OnMyClickListener mOnMyClickListener;

	public interface OnMyClickListener {
		public void onMyClick(View arg0, Tab_labelHandler mTab_labelHandler);
	}
}
