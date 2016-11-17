package com.sun3d.culturalShanghai.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.MyLoveActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.animation.ContainerAnimation;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.SlideShowView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class VenueMoreAdapter extends BaseAdapter implements OnTouchListener, OnClickListener {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<EventInfo> list;
	private Boolean isShowView = false;
	private Boolean isShowTopPage = false;
	private View topView = null;
	private int onclickindex = 2;
	public Boolean isFrast = true;
	private PullToRefreshListView contanView = null;

	public VenueMoreAdapter(Context mContext, List<EventInfo> list, boolean isShowAddress) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		isShowView = false;
		topView = null;
	}

	public int getOnclickindex() {
		return onclickindex;
	}

	public void setOnclickindex(int onclickindex) {
		this.onclickindex = onclickindex;
	}

	public void setBannerIsRefresh(boolean isrefresh) {
		if (isrefresh) {
			topView = null;
		}
	}

	public void addBannerContanView(PullToRefreshListView view) {
		contanView = view;
	}

	public void isShowFootView(Boolean isShow) {
		isShowView = isShow;
	}

	public void isActivityMainList(Boolean isShow) {
		isShowTopPage = isShow;
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

		if (!isShowTopPage) {
			convertView = addMainView(arg0, convertView);
		} else {
			convertView = m.get(arg0);
			convertView = addAcrivitiTopPage(arg0, convertView);
			m.put(arg0, convertView);
		}
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Log.d("onTouch", "onTouch");
				return false;
			}
		});
		return convertView;
	}

	ContainerAnimation ca = new ContainerAnimation();

	@SuppressLint("NewApi")
	private View addAcrivitiTopPage(int arg0, View convertView) {
		if (arg0 > 1) {
			return addMainView(arg0, convertView);
		} else if (arg0 == 0) {
			MyHandler mMyHandler = null;
			if (topView == null) {
				mMyHandler = new MyHandler();
				topView = (LinearLayout) View.inflate(mContext, R.layout.adapter_index_tab_host,
						null);
				mMyHandler.top_layout = (SlideShowView) topView.findViewById(R.id.slideshowView);
				topView.setTag(mMyHandler);
			} else {
				mMyHandler = (MyHandler) topView.getTag();
			}
			return topView;
		} else {
			MyHandler mMyHandler = null;
			if (convertView == null) {
				mMyHandler = new MyHandler();
				convertView = (LinearLayout) View.inflate(mContext, R.layout.index_tab_host, null);
				mMyHandler.mIvAddType = (ImageView) convertView
						.findViewById(R.id.activity_add_type);
				mMyHandler.mTvWeek = (TextView) convertView.findViewById(R.id.activity_week_tv);
				mMyHandler.mTvMap = (TextView) convertView.findViewById(R.id.activity_map_tv);
				convertView.setTag(mMyHandler);
			} else {
				mMyHandler = (MyHandler) convertView.getTag();
			}
//			new ActivityTabHandler(mContext, convertView, new TabCallback() {
//
//				@Override
//				public void setTab(String tabName) {
//					// TODO Auto-generated method stub
//
//				}
//			});
			mMyHandler.mIvAddType.setOnClickListener(this);
			mMyHandler.mTvWeek.setOnClickListener(this);
			mMyHandler.mTvMap.setOnClickListener(this);
			return convertView;
		}

	}

	private class MyHandler {
		private SlideShowView top_layout;
		private HorizontalListView mHorListView;
		private ImageView mIvAddType;
		private TextView mTvWeek;
		private TextView mTvMap;
	}

	private View addMainView(final int arg0, View convertView) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_event_list_item, null);
			mHolder.img = (ImageView) convertView.findViewById(R.id.item_img);
			mHolder.title = (TextView) convertView.findViewById(R.id.item_title);
			mHolder.time = (TextView) convertView.findViewById(R.id.item_time);
			mHolder.label = (ImageView) convertView.findViewById(R.id.item_label);
			mHolder.address = (TextView) convertView.findViewById(R.id.item_address);
			mHolder.type = (TextView) convertView.findViewById(R.id.item_type);
			mHolder.value = (TextView) convertView.findViewById(R.id.item_value);
			mHolder.foot = (TextView) convertView.findViewById(R.id.footview);
			mHolder.tiketcount = (TextView) convertView.findViewById(R.id.item_tiketcount);
			mHolder.tiketcountLayout = (LinearLayout) convertView
					.findViewById(R.id.item_tiketcount_layout);
			mHolder.item_toplayout = (LinearLayout) convertView.findViewById(R.id.item_toplayout);
			mHolder.item_mapaddress = (RelativeLayout) convertView
					.findViewById(R.id.item_mapaddress);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final EventInfo eventInfo = list.get(arg0);
		if ("".equals(eventInfo.getActivityTagName()) || eventInfo.getActivityTagName() == null) {
			mHolder.type.setVisibility(View.GONE);
		} else {
			mHolder.type.setText(eventInfo.getActivityTagName());
		}
		mHolder.tiketcountLayout.setVisibility(View.VISIBLE);
		mHolder.address.setVisibility(View.VISIBLE);
		mHolder.item_mapaddress.setVisibility(View.VISIBLE);
		mHolder.tiketcount.setText(eventInfo.getActivityAbleCount());
		if (eventInfo.getActivityIsReservation().equals("1")) {
			mHolder.tiketcountLayout.setVisibility(View.GONE);
		} else {
			mHolder.tiketcountLayout.setVisibility(View.VISIBLE);
		}
		if (eventInfo.getEventPrice().equals("0") | eventInfo.getEventPrice().length() == 0) {
			mHolder.value.setText("免费");
			mHolder.value.setCompoundDrawables(null, null, null, null);
		} else {
			mHolder.value.setText("收费");
		}

		if (eventInfo.getActivityRecommend().equals("Y")) {// 最热
//			mHolder.label.setImageResource(R.drawable.sh_icon_event_list_hot_icon);
		} else if (eventInfo.getActivityRecommend().equals("N")) {// 最新
//			mHolder.label.setImageResource(R.drawable.sh_icon_event_list_new_icon);
		} else {
			mHolder.label.setVisibility(View.GONE);
		}
		mHolder.item_toplayout.setTag(eventInfo);
		mHolder.item_mapaddress.setTag(eventInfo);

		mHolder.time.setText("日期： "
				+ TextUtil.getDate(eventInfo.getActivityStartTime(), eventInfo.getEventEndTime()));
		mHolder.title.setText(eventInfo.getEventName());
		mHolder.address.setText("地点： " + eventInfo.getEventAddress());
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()), mHolder.img,
						Options.getRoundOptions(10));
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
		TextView type;
		ImageView label;
		TextView address;
		TextView value;
		TextView tiketcount;
		LinearLayout item_toplayout;
		LinearLayout tiketcountLayout;
		RelativeLayout item_mapaddress;
		TextView foot;
	}

	private void onclick(View arg0) {
		if (ButtonUtil.isDelayClick()) {
			EventInfo eventInfo = (EventInfo) arg0.getTag();
			Intent intent = new Intent(mContext, ActivityDetailActivity.class);
			// Intent intent = new Intent(mContext, EventDetailsActivity.class);
			intent.putExtra("eventId", eventInfo.getEventId());
			mContext.startActivity(intent);
		}
	}

	private void onTabOnclick(View view) {
		if (ButtonUtil.isDelayClick()) {
			switch (view.getId()) {
			case R.id.tab_nebaty_btn:
				onclickindex = 0;
				break;
			case R.id.tab_start_btn:
				onclickindex = 1;
				break;
			case R.id.tab_hot_btn:
				onclickindex = 2;
				break;
			default:
				break;
			}
		}
	}

	private float index_X, index_Y;

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
					if (arg0.getId() == R.id.tab_nebaty_btn | arg0.getId() == R.id.tab_start_btn
							| arg0.getId() == R.id.tab_hot_btn) {
						onTabOnclick(arg0);
					} else {
						onclick(arg0);
					}

				}
			}

			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub附近活动
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.activity_week_tv:
			intent = new Intent(mContext, ThisWeekActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.activity_map_tv:
			// intent = new Intent(mContext, ActivityMap.class);
			// intent.putExtra("ListType", "1");
			// intent.putExtra("Activity_themeId", Activity_themeId);
			// intent.putExtra("Activity_typeId", Activity_typeId);
			// intent.putExtra("Activity_themetype", Activity_theme);
			// intent.putExtra("Activity_typetype", Activity_type);
			// mContext.startActivity(intent);
			break;
		case R.id.activity_add_type:
			intent = new Intent(mContext, MyLoveActivity.class);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

}
