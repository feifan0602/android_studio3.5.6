package com.sun3d.culturalShanghai.adapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
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
import com.sun3d.culturalShanghai.activity.ActivityMap;
import com.sun3d.culturalShanghai.activity.MyLoveActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.animation.ContainerAnimation;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.fragment.NearbyFragment;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.SlideShowView;

@SuppressLint("UseSparseArrays")
public class NearbyListAdapter extends BaseAdapter implements OnTouchListener,
		OnClickListener {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context mContext;
	private List<EventInfo> list;
	private Boolean isShowView = false;
	private Boolean isShowTopPage = false;
	private View topView = null;
	private int onclickindex = 2;
	public Boolean isFrast = true;
	private PullToRefreshListView contanView = null;
	private int count;
	private String Activity_theme = "";
	private String Activity_themeId = "";
	private String Activity_type = "";
	private String Activity_typeId = "cf719729422c497aa92abdd47acdaa56";
	private NearbyFragment fragment;

	public NearbyListAdapter(Context mContext, List<EventInfo> list,
			boolean isShowAddress) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		isShowView = false;
		topView = null;
	}

	public NearbyListAdapter(NearbyFragment fragment, Context mContext,
			List<EventInfo> list, boolean isShowAddress) {
		// TODO Auto-generated constructor stub
		this.fragment = fragment;
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

	public void activityCount(int count) {
		this.count = count;
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
		// convertView.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// // TODO Auto-generated method stub
		// Log.d("onTouch", "onTouch");
		// return false;
		// }
		// });
		return convertView;
	}

	ContainerAnimation ca = new ContainerAnimation();

	@SuppressLint("NewApi")
	private View addAcrivitiTopPage(int arg0, View convertView) {
		// 这是第二项开始
		if (arg0 > 1) {
			convertView = addMainView(arg0, convertView);
		} else if (arg0 == 0) {
			/**
			 * 这个是viewpager的布局 3.5版本进行更改 一张大图和两张小图的布局文件
			 */
			convertView = addNull(arg0, convertView);
		} else {
			convertView = addNull(arg0, convertView);
		}
		return convertView;
		// /**
		// * 这个是viewpager的布局 3.5版本进行更改 这是顶部文件
		// */
		// MyHandler mMyHandler = null;
		// /**
		// * 这个是横向的listview 和两个imageview 的布局
		// */
		// if (convertView == null) {
		// mMyHandler = new MyHandler();
		// convertView = (LinearLayout) View.inflate(mContext,
		// R.layout.index_tab_host, null);
		// mMyHandler.mIvAddType = (ImageView) convertView
		// .findViewById(R.id.activity_add_type);
		// mMyHandler.mTvWeek = (TextView) convertView
		// .findViewById(R.id.activity_week_tv);
		// mMyHandler.mTvMap = (TextView) convertView
		// .findViewById(R.id.activity_map_tv);
		// mMyHandler.mCount = (TextView) convertView
		// .findViewById(R.id.tab_count);
		// convertView.setTag(mMyHandler);
		// } else {
		// //
		// mMyHandler = (MyHandler) convertView.getTag();
		// }
		// if (count == 0) {
		// mMyHandler.mCount.setVisibility(View.GONE);
		// } else {
		// mMyHandler.mCount.setVisibility(View.VISIBLE);
		// mMyHandler.mCount.setText(count + "");
		// }
		// // if(MyApplication.getInstance().getmTabHandler()!=null){
		// //
		// // }
		// new ActivityTabHandler(mContext, convertView, new TabCallback() {
		//
		// @Override
		// public void setTab(UserBehaviorInfo info) {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.obj = info;
		// msg.arg1 = 1001;
		// MyApplication.getInstance().getActivityHandler()
		// .sendMessage(msg);
		// }
		// });
		// mMyHandler.mIvAddType.setOnClickListener(this);
		// mMyHandler.mTvWeek.setOnClickListener(this);
		// mMyHandler.mTvMap.setOnClickListener(this);

		// MyHandler mMyHandler = null;
		//
		// if (topView == null) {
		// mMyHandler = new MyHandler();
		// topView = (LinearLayout) View.inflate(mContext,
		// R.layout.adapter_index_tab_host, null);
		// mMyHandler.top_layout = (SlideShowView) topView
		// .findViewById(R.id.slideshowView);
		// topView.setTag(mMyHandler);
		// } else {
		// mMyHandler = (MyHandler) topView.getTag();
		// }
		// mMyHandler.top_layout.setContianOnEvent(topView);
		// return topView;
		// else {
		// MyHandler mMyHandler = null;
		// /**
		// * 这个是横向的listview 和两个imageview 的布局
		// */
		// if (convertView == null) {
		// mMyHandler = new MyHandler();
		// convertView = (LinearLayout) View.inflate(mContext,
		// R.layout.index_tab_host, null);
		// mMyHandler.mIvAddType = (ImageView) convertView
		// .findViewById(R.id.activity_add_type);
		// mMyHandler.mTvWeek = (TextView) convertView
		// .findViewById(R.id.activity_week_tv);
		// mMyHandler.mTvMap = (TextView) convertView
		// .findViewById(R.id.activity_map_tv);
		// mMyHandler.mCount = (TextView) convertView
		// .findViewById(R.id.tab_count);
		// convertView.setTag(mMyHandler);
		// } else {
		// mMyHandler = (MyHandler) convertView.getTag();
		// }
		// if (count == 0) {
		// mMyHandler.mCount.setVisibility(View.GONE);
		// } else {
		// mMyHandler.mCount.setVisibility(View.VISIBLE);
		// mMyHandler.mCount.setText(count + "");
		// }
		// // if(MyApplication.getInstance().getmTabHandler()!=null){
		// //
		// // }
		// new ActivityTabHandler(mContext, convertView, new TabCallback() {
		//
		// @Override
		// public void setTab(UserBehaviorInfo info) {
		// // TODO Auto-generated method stub
		// Message msg = new Message();
		// msg.obj = info;
		// msg.arg1 = 1001;
		// MyApplication.getInstance().getActivityHandler()
		// .sendMessage(msg);
		// }
		// });
		// mMyHandler.mIvAddType.setOnClickListener(this);
		// mMyHandler.mTvWeek.setOnClickListener(this);
		// mMyHandler.mTvMap.setOnClickListener(this);
		// return convertView;
		// }

	}

	private View addNull(int arg0, View convertView) {
		ViewHolder_Frist mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder_Frist();
			convertView = View.inflate(mContext,
					R.layout.adapter_event_null_list_item, null);
			mHolder.big_img = (ImageView) convertView
					.findViewById(R.id.big_img);
			mHolder.small_img_left = (ImageView) convertView
					.findViewById(R.id.small_img_left);
			mHolder.small_img_right = (ImageView) convertView
					.findViewById(R.id.small_img_right);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder_Frist) convertView.getTag();
		}

		return convertView;
	}

	private View addFristView(int arg0, View convertView) {
		ViewHolder_Frist mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder_Frist();
			convertView = View.inflate(mContext,
					R.layout.adapter_event_frist_list_item, null);
			mHolder.big_img = (ImageView) convertView
					.findViewById(R.id.big_img);
			mHolder.small_img_left = (ImageView) convertView
					.findViewById(R.id.small_img_left);
			mHolder.small_img_right = (ImageView) convertView
					.findViewById(R.id.small_img_right);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder_Frist) convertView.getTag();
		}

		return convertView;
	}

	/**
	 * 这个是ViewHolder
	 * 
	 * @author wenff
	 * 
	 */
	private class MyHandler {
		private SlideShowView top_layout;
		private HorizontalListView mHorListView;
		private ImageView mIvAddType;
		private TextView mTvWeek;
		private TextView mTvMap;
		private TextView mCount;
	}

	private String getActivityPast = "";

	/**
	 * 这个是下半部分的listview的布局
	 * 
	 * @param arg0
	 * @param convertView
	 * @return
	 */
	private View addMainView(final int arg0, View convertView) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.adapter_event_nearby_list_item, null);
			mHolder.img = (ImageView) convertView.findViewById(R.id.item_img);
			mHolder.title = (TextView) convertView
					.findViewById(R.id.item_title);
			mHolder.item_subject = (TextView) convertView
					.findViewById(R.id.item_subject);
			mHolder.item_subject1 = (TextView) convertView
					.findViewById(R.id.item_subject1);
			mHolder.item_subject2 = (TextView) convertView
					.findViewById(R.id.item_subject2);
			mHolder.item_distance = (TextView) convertView
					.findViewById(R.id.item_distance);
			mHolder.time = (TextView) convertView.findViewById(R.id.item_time);
			mHolder.label = (ImageView) convertView
					.findViewById(R.id.item_label);
			mHolder.address = (TextView) convertView
					.findViewById(R.id.item_address);
			mHolder.type = (ImageView) convertView.findViewById(R.id.item_type);
			mHolder.value = (TextView) convertView
					.findViewById(R.id.item_value);
			mHolder.price = (TextView) convertView.findViewById(R.id.price);
			mHolder.price_last = (TextView) convertView
					.findViewById(R.id.price_last);

			mHolder.foot = (TextView) convertView.findViewById(R.id.footview);
			mHolder.tiketcount = (TextView) convertView
					.findViewById(R.id.item_tiketcount);
			mHolder.tiketcountLayout = (LinearLayout) convertView
					.findViewById(R.id.item_tiketcount_layout);
			mHolder.item_toplayout = (LinearLayout) convertView
					.findViewById(R.id.item_toplayout);
			mHolder.item_mapaddress = (RelativeLayout) convertView
					.findViewById(R.id.item_mapaddress);
			mHolder.yupiao = (ImageView) convertView
					.findViewById(R.id.item_yupiao);
			mHolder.price.setTypeface(MyApplication.GetTypeFace());
			mHolder.title.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_subject.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_subject1.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_subject2.setTypeface(MyApplication.GetTypeFace());
			mHolder.item_distance.setTypeface(MyApplication.GetTypeFace());
			mHolder.time.setTypeface(MyApplication.GetTypeFace());
			mHolder.address.setTypeface(MyApplication.GetTypeFace());
			mHolder.value.setTypeface(MyApplication.GetTypeFace());
			mHolder.price_last.setTypeface(MyApplication.GetTypeFace());
			mHolder.foot.setTypeface(MyApplication.GetTypeFace());
			mHolder.tiketcount.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final EventInfo eventInfo = list.get(arg0);
		// if (eventInfo.getActivityIsHot() == 0) {
		// mHolder.new_img.setVisibility(View.GONE);
		// } else {
		// mHolder.new_img.setBackgroundResource(R.drawable.hot_img);
		// }
		// if ("".equals(eventInfo.getActivitySubject())
		// || eventInfo.getActivitySubject() == null) {
		// mHolder.type.setVisibility(View.GONE);
		// } else {
		// mHolder.type.setBackgroundResource(R.drawable.new_img);
		// }
		mHolder.tiketcountLayout.setVisibility(View.VISIBLE);
		mHolder.address.setVisibility(View.VISIBLE);
		mHolder.item_mapaddress.setVisibility(View.VISIBLE);
		mHolder.tiketcount.setText(eventInfo.getActivityAbleCount());
		if (eventInfo.getActivityIsReservation().equals("1")) {
			mHolder.tiketcountLayout.setVisibility(View.GONE);
		} else {
			mHolder.tiketcountLayout.setVisibility(View.VISIBLE);
		}
		// if (eventInfo.getEventPrice().equals("0")
		// | eventInfo.getEventPrice().length() == 0) {
		// mHolder.value.setText("免费");
		// mHolder.value.setCompoundDrawables(null, null, null, null);
		// } else {
		// mHolder.value.setText("收费");
		// }
		if (eventInfo.getActivityIsFree() == 2) {
			// 表示不免费
			if (!eventInfo.getEventPrice().endsWith("0")) {
				mHolder.price.setText(eventInfo.getEventPrice());
				mHolder.price_last.setVisibility(View.VISIBLE);
			} else {
				mHolder.price_last.setVisibility(View.GONE);
				mHolder.price.setText("收费");
			}

		} else {
			mHolder.price_last.setVisibility(View.GONE);
			// 表示免费
			mHolder.price.setText("免费");
		}
		String activityStartTime = eventInfo.getActivityStartTime();
		activityStartTime = activityStartTime.replaceAll("-", "");
		Long starttime = Long.valueOf(activityStartTime);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		str = str.replaceAll("-", "");
		long now_time = Long.valueOf(str);
		if (eventInfo.getActivityRecommend().equals("Y")) {// 推荐
			mHolder.type.setVisibility(View.VISIBLE);
//			mHolder.type
//					.setBackgroundResource(R.drawable.sh_icon_event_list_hot_icon);
		} else if (eventInfo.getActivityIsHot() == 1) {// 最热
			mHolder.type.setVisibility(View.VISIBLE);
//			mHolder.type.setBackgroundResource(R.drawable.hot_img);
		} else if (now_time - starttime <= 1) {
			mHolder.type.setVisibility(View.VISIBLE);
//			mHolder.type.setBackgroundResource(R.drawable.new_img);
		} else {
			mHolder.type.setVisibility(View.GONE);
		}
		// 1：过期
		//
		// if (_activityModel.activityPast == 0)//未过期
		// {
		// if (_activityModel.activityIsReservation == 2)//可预订
		// {
		// if ( _activityModel.activityAbleCount)
		// {
		// _ticketConditonView.image = [UIImage
		// imageNamed:@"sh_haveTicket_icon.png"];
		// }
		// else
		// {
		// _ticketConditonView.image = [UIImage
		// imageNamed:@"sh_orderBookeUp.png"];
		// }
		// }
		// else//不可预订
		// {
		// _ticketConditonView.image = nil;
		// }
		// }
		// else//已过期
		// {
		// _ticketConditonView.image = [UIImage
		// imageNamed:@"sh_didFinish_icon.png"];
		// }

		if (null == eventInfo.getActivityPast()) {
			getActivityPast = "0";
		} else {
			getActivityPast = eventInfo.getActivityPast();
		}

		mHolder.yupiao.setVisibility(View.GONE);
		if ("0".equals(getActivityPast)) {
			if ("2".equals(eventInfo.getActivityIsReservation())) {
				if ("0".equals(eventInfo.getActivityAbleCount())) {
					mHolder.yupiao.setBackgroundResource(R.drawable.sh_dingwan);
				} else {
					mHolder.yupiao.setBackgroundResource(R.drawable.sh_yupiao);
				}
			} else {
				mHolder.yupiao.setVisibility(View.GONE);
			}
		} else {
			mHolder.yupiao.setBackgroundResource(R.drawable.sh_out_data);
		}

		if (eventInfo.getActivityRecommend().equals("Y")) {// 最热
//			mHolder.label
//					.setImageResource(R.drawable.sh_icon_event_list_hot_icon);
		} else if (eventInfo.getActivityRecommend().equals("N")) {// 最新
			mHolder.label
					.setImageResource(R.drawable.sh_icon_event_list_new_icon);
		} else {
			mHolder.label.setVisibility(View.GONE);
		}

		mHolder.item_toplayout.setTag(eventInfo);
		mHolder.item_mapaddress.setTag(eventInfo);
		if (eventInfo.getTagName() != "") {
			mHolder.item_subject1.setVisibility(View.VISIBLE);
			mHolder.item_subject1.setText(eventInfo.getTagName());
		} else {
			mHolder.item_subject1.setVisibility(View.GONE);
		}
		if (eventInfo.getActivitySubject().split("\\.").length > 1) {
			mHolder.item_subject2.setVisibility(View.VISIBLE);
			mHolder.item_subject.setText(eventInfo.getActivitySubject().split(
					"\\.")[1]);
			mHolder.item_subject2.setText(eventInfo.getActivitySubject().split(
					"\\.")[0]);
		} else {
			mHolder.item_subject.setText(eventInfo.getActivitySubject());
			mHolder.item_subject2.setVisibility(View.GONE);
		}

		String end_time = null;
		String start_time = null;
		String start = null;
		String end = null;

		if (eventInfo.getActivityStartTime() != null) {
			start = eventInfo.getActivityStartTime().replaceAll("-", ".");

		}

		if (eventInfo.getEventEndTime() != null) {
			end = eventInfo.getEventEndTime().replaceAll("-", ".");
		}
		Log.i("ceshi", "start==" + start + "end " + end);
		if (eventInfo.getActivityStartTime()
				.equals(eventInfo.getEventEndTime())
				|| eventInfo.getEventEndTime() == null) {
			mHolder.time.setText(start.substring(5, 10));
		} else {

			mHolder.time.setText(start.substring(5, 10) + "-"
					+ end.substring(5, 10));
		}

		// mHolder.time.setText("日期： " +
		// TextUtil.getDate(eventInfo.getActivityStartTime(),
		// eventInfo.getEventEndTime()));
		mHolder.title.setText(eventInfo.getEventName());
		mHolder.address.setText(eventInfo.getActivityLocationName());
		// if (eventInfo.getActivitySite().equals("")
		// || null == eventInfo.getActivitySite()) {
		// } else {
		// mHolder.address.setText(eventInfo.getActivitySite());
		// }
		Log.i("ceshi", "查看数据===  " + eventInfo.getDistance());
		if (Double.valueOf(eventInfo.getDistance()) > 1) {
			BigDecimal b = new BigDecimal(eventInfo.getDistance());
			String dictance = b.setScale(1, BigDecimal.ROUND_DOWN).toString();
			mHolder.item_distance.setText(dictance + "KM");
		} else {
			Double distance = Double.valueOf(eventInfo.getDistance()) * 1000;
			String s = String.valueOf(distance);
			String newD = s.substring(0, s.indexOf("."));
			mHolder.item_distance.setText(newD + "M");
		}

		// MyApplication.getInstance().getImageLoader().displayImage(TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()),
		// mHolder.img, Options.getRoundOptions(10));

		// MyApplication.getInstance()
		// .setImageView(MyApplication.getContext(),
		// TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()),
		// mHolder.img);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()),
						mHolder.img, Options.getRoundOptions(0));
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
		TextView price;
		TextView item_distance;
		TextView item_subject;
		TextView item_subject1;
		TextView item_subject2;
		TextView time;
		ImageView type;
		ImageView label;
		ImageView yupiao;
		TextView address;
		TextView value;
		TextView tiketcount;
		LinearLayout item_toplayout;
		LinearLayout tiketcountLayout;
		RelativeLayout item_mapaddress;
		TextView foot;
		TextView price_last;
	}

	class ViewHolder_Frist {
		ImageView big_img;
		ImageView small_img_left;
		ImageView small_img_right;
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
					if (arg0.getId() == R.id.tab_nebaty_btn
							| arg0.getId() == R.id.tab_start_btn
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
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.activity_week_tv:
			activityCount(0);
			notifyDataSetChanged();
			fragment.setCount(0);
			intent = new Intent(mContext, ThisWeekActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.activity_map_tv:
			intent = new Intent(mContext, ActivityMap.class);
			intent.putExtra("ListType", "1");
			intent.putExtra("Activity_themeId", Activity_themeId);
			intent.putExtra("Activity_typeId", Activity_typeId);
			intent.putExtra("Activity_themetype", Activity_theme);
			intent.putExtra("Activity_typetype", Activity_type);
			mContext.startActivity(intent);
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
