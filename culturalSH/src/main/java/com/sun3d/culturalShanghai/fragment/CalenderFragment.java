package com.sun3d.culturalShanghai.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GetPhoneInfoUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.adapter.CalendarPopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomeCenterPopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAreaAdapter;
import com.sun3d.culturalShanghai.adapter.WeekActivityListAdapter;
import com.sun3d.culturalShanghai.adapter.WeekItemAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.BannerInfo;
import com.sun3d.culturalShanghai.object.CalenderBannerInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.sun3d.culturalShanghai.view.CalendarView;
import com.sun3d.culturalShanghai.view.CalendarView.OnItemClickListener_calendar;
import com.sun3d.culturalShanghai.wff.calender.CalendarGridViewAdapter;
import com.sun3d.culturalShanghai.wff.calender.CalendarTool;
import com.sun3d.culturalShanghai.wff.calender.DateEntity;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.sun3d.culturalShanghai.windows.WeekPopupWindow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日历页面
 * 
 * @author wenff
 * 
 */
public class CalenderFragment extends Fragment implements OnClickListener,
		WeekActivityListAdapter.WeekLisenner {
	private final String mPageName = "ThisWeekActivity";
	/**
	 * 点击的位置
	 */
	public int pos = 1;
	public boolean now_time_position_change = true;
	private Context mContext;
	/**
	 * true 表示不拦截动画 false 表示拦截动画 防止日历抖动
	 */
	public boolean prevent = false;
	private PullToRefreshListView mListView;
	private int mPage = 0;
	private boolean animation_start = true;
	TextView right;
	Drawable drawable;
	private RelativeLayout mTitle;
	private LoadingHandler mLoadingHandler;
	private Map<String, String> mParams;
	private View Topview;
	private WeekActivityListAdapter mWeekActivityListAdapter;
	private LinearLayout topLanel;
	private View viewWeek;
	private TextView topLaBel;
	private ImageView topImg;
	private WeekPopupWindow mWeekPopupWindow = new WeekPopupWindow();
	private List<UserBehaviorInfo> mflagList;
	public static String today = "";
	private Boolean isRefresh = false;
	private Boolean isListViewRefresh = true;
	private String activityType = "2";// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
	private List<EventInfo> mList;
	private WeekActivityListAdapter mListAdapter;
	private Boolean isFirstResh = true;
	private String activityTime = "";
	private View view;
	private TextView middle_tv;
	private boolean addmore_bool = true;
	private ImageView right_iv, left_iv;
	private LinearLayout weeklayout;
	private LinearLayout weeklayout_wff;
	private static int month = 0;
	private HashMap<String, String> mEvery_Params;
	private int num;
	private TextView collection_null;
	private int mday;
	private int mMonth;
	public static List<BannerInfo> listBannerInfo = new ArrayList<BannerInfo>();
	/**
	 * 日历控件 wff3.5
	 */
	private GridView calendar;
	public LinearLayout calender_ll, head_sieve_ll, sieve_ll;
	private CalendarGridViewAdapter mAdapter;
	private Point mNowCalendarPoint;
	private List<DateEntity> mDateEntityList;
	private CalendarTool mCalendarTool;
	private int height_num = 5;
	private int height;
	/**
	 * 日历控件 wff3.5 设置高度
	 */
	private int calendar_height;
	private SimpleDateFormat format;
	/**
	 * 3.5.2 筛选
	 */
	public PopupWindow pw;
	/**
	 * 这是和场馆一样的接口
	 */
	private ArrayList<Wff_VenuePopuwindow> list_area;
	/**
	 * 智能排序 1-智能排序 2-热门排序 3-最新上线 4-即将结束
	 */
	private int sortType = 5;
	/**
	 * 1-免费 2-收费
	 */
	private String activityIsFree = "";
	/**
	 * 是否预定 1-不可预定 2-可预定
	 */
	private String activityIsReservation = "";
	public List<UserBehaviorInfo> mTypeList;
	private String selectActivityType = "";
	private HomePopuWindowAdapter hpwa;
	private CalendarPopuWindowAdapter cpwa;
	private Wff_VenuePopuwindow wvpw;
	private ArrayList<Wff_VenuePopuwindow> list_area_z;
	private View itemView;
	private int mBgColor;
	private TextView shopping_areas, preface, sieve;
	private boolean scroll_status = true;
	private String venueArea = "";
	private String venueDicName = "";
	private String venueMood = "";
	private String venueMoodName = "";

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API. See
	 * https://g.co/AppIndexing/AndroidStudio for more information.
	 */

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoadingTextShowPopWindow.dismissPop();
		WeekItemAdapter.init(false);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mContext = getActivity();
		view = inflater.inflate(R.layout.activity_this_week_fragment, null);
		today = TextUtil.getToDay();
		MyApplication.getInstance().addActivitys(getActivity());
		isFirstResh = true;
		isRefresh = false;
		init();
		initDataArea();
		initListData();
		getTypeList();
		getListData(mPage);
		getEveryDay("", "");
		startWill();

		return view;
	}

	private void startWill() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYWILL,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {

					}
				});
	}


	/**
	 * 刷新
	 */
	private final int REFRESH = 1;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH:
				if (mListView != null) {
					mListView.onRefreshComplete();
				}
				if (!isFirstResh) {
					// LoadingTextShowPopWindow.showLoadingText(ThisWeekActivity.this,
					// Topview, "数据刷新完成");
				}
				isFirstResh = false;
				break;
			// case 3:
			// listBannerInfo = new ArrayList<BannerInfo>();
			// listBannerInfo = (List<BannerInfo>) msg.obj;
			// break;
			default:
				break;
			}
		}

	};

	/**
	 * 设置标题 这里我重新布局过了
	 */
	private void setTitle(View view) {
		mTitle = (RelativeLayout) view.findViewById(R.id.title);
		// mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		// mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		// TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		// right = (TextView) mTitle.findViewById(R.id.title_send);
		// title.setText("近期活动");
		// right.setText("筛选");
		// right.setTextSize(17);
		// right.setTextColor(getResources().getColor(R.color.white_color));
		// drawable = getResources()
		// .getDrawable(R.drawable.sh_activity_title_pull);
		// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
		// drawable.getMinimumHeight());
		// right.setCompoundDrawables(null, null, drawable, null);
		// title.setVisibility(View.VISIBLE);
		// right.setVisibility(View.VISIBLE);
		// right.setOnClickListener(this);
	}

	/**
	 * 初始化
	 */
	private void init() {
		/**
		 * 3.5.2 筛选
		 */
		sieve_ll = (LinearLayout) view.findViewById(R.id.venue_ll);
		shopping_areas = (TextView) view.findViewById(R.id.shopping_areas);
		preface = (TextView) view.findViewById(R.id.preface);
		sieve = (TextView) view.findViewById(R.id.sieve);
		shopping_areas.setTypeface(MyApplication.GetTypeFace());
		preface.setTypeface(MyApplication.GetTypeFace());
		sieve.setTypeface(MyApplication.GetTypeFace());
		shopping_areas.setOnClickListener(this);
		preface.setOnClickListener(this);
		sieve.setOnClickListener(this);
		mTypeList = new ArrayList<UserBehaviorInfo>();
		setTitle(view);
		/**
		 * 日历的选择
		 */
		format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取日历控件对象
		calendar = (GridView) view.findViewById(R.id.calendar);
		calendar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return MotionEvent.ACTION_MOVE == event.getAction() ? true
						: false;
			}
		});
		Calendar c = Calendar.getInstance();
		mday = c.get(Calendar.DAY_OF_MONTH);
		mMonth = c.get(Calendar.MONTH) + 1;
		calendar.setOnItemClickListener(new CalendarItemClickListener());
		mCalendarTool = new CalendarTool(getActivity());
		mNowCalendarPoint = mCalendarTool.getNowCalendar();
		mDateEntityList = mCalendarTool.getDateEntityList(mNowCalendarPoint.x,
				mNowCalendarPoint.y);
		mAdapter = new CalendarGridViewAdapter(getActivity(), getResources(),
				this);
		mAdapter.setDateList(mDateEntityList);
		calendar.setAdapter(mAdapter);
		if (mDateEntityList.size() > 40) {
			MyApplication.setGridViewHeightBasedOnChildrenCalendertwo(calendar);
			LayoutParams l = (LayoutParams) calendar.getLayoutParams();
			height = l.height;
			height_num = 6;
		} else {
			MyApplication.setGridViewHeightBasedOnChildrenCalender(calendar);
			LayoutParams l = (LayoutParams) calendar.getLayoutParams();
			height = l.height;
			height_num = 5;
		}

		// calendar.setSelectMore(false); // 单选
		collection_null = (TextView) view.findViewById(R.id.collection_null);
		right_iv = (ImageView) view.findViewById(R.id.right_iv);
		middle_tv = (TextView) view.findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText(mNowCalendarPoint.x + "." + mNowCalendarPoint.y);
		left_iv = (ImageView) view.findViewById(R.id.left_iv);
		left_iv.setImageResource(R.drawable.back);
		left_iv.setVisibility(View.INVISIBLE);
		right_iv.setImageResource(R.drawable.arrow_right);
		left_iv.setOnClickListener(this);
		right_iv.setOnClickListener(this);
		// try {
		// // 设置日历日期
		// Date date = format.parse("2015-01-01");
		// calendar.setCalendarData(date);
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// String[] ya = calendar.getYearAndmonth().split("-");
		// middle_tv.setText(ya[0] + "." + ya[1]);
		// // 设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
		// calendar.setOnItemClickListener_calendar(new
		// OnItemClickListener_calendar() {
		//
		// @Override
		// public void OnItemClick(Date selectedStartDate,
		// Date selectedEndDate, Date downDate) {
		// if (calendar.isSelectMore()) {
		// } else {
		// activityTime = format.format(downDate);
		// String activitymouth = activityTime.substring(5, 7);
		// String todaymouth = today.substring(5, 7);
		// Log.i("ceshi", "看看日期===  activityTime1=== " + activitymouth
		// + "  today1 ==  " + todaymouth);
		// if (Integer.valueOf(activitymouth) == Integer
		// .valueOf(todaymouth)) {
		// String activityTime1 = activityTime.substring(8, 10);
		// String today1 = today.substring(8, 10);
		//
		// if (Integer.valueOf(today1) <= Integer
		// .valueOf(activityTime1)) {
		// mLoadingHandler.startLoading();
		// mList.clear();
		// getListData(0);
		// } else {
		// }
		// } else if (Integer.valueOf(activitymouth) >= Integer
		// .valueOf(todaymouth)) {
		// String activityTime1 = activityTime.substring(8, 10);
		// String today1 = today.substring(8, 10);
		// mLoadingHandler.startLoading();
		// mList.clear();
		// getListData(0);
		// }
		//
		// }
		// }
		// });
		// setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void OnItemClick(Date selectedStartDate,
		// Date selectedEndDate, Date downDate) {
		// if(calendar.isSelectMore()){
		// Toast.makeText(getApplicationContext(),
		// format.format(selectedStartDate)+"到"+format.format(selectedEndDate),
		// Toast.LENGTH_SHORT).show();
		// }else{
		// Toast.makeText(getApplicationContext(), format.format(downDate),
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		// });
		weeklayout_wff = (LinearLayout) view.findViewById(R.id.weeklayout_wff);
		RelativeLayout loadingLayout = (RelativeLayout) view
				.findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();

		Topview = (View) view.findViewById(R.id.activity_page_line);
		viewWeek = View.inflate(mContext, R.layout.weeklayout, null);
		weeklayout = (LinearLayout) viewWeek.findViewById(R.id.weeklayoutid);
		// topLanel = (LinearLayout) view.findViewById(R.id.tabtop);
		// topLaBel = (TextView) topLanel.findViewById(R.id.toptv);
		// topLaBel.setText(today);
		// middle_tv.setText(today);
		topImg = (ImageView) view.findViewById(R.id.guang_gao);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.exhibition_listview);

		View view_head = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_null_top, null);
		head_sieve_ll = (LinearLayout) view_head.findViewById(R.id.top_null_ll);
		mListView.getRefreshableView().addHeaderView(view_head);
		// calender_ll = (LinearLayout) view.findViewById(R.id.calender_ll);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				if (arg2 >= 1) {

					Intent intent = new Intent(mContext,
							ActivityDetailActivity.class);
					// Intent intent = new Intent(mContext,
					// EventDetailsActivity.class);
					if (arg2 != 0) {
						intent.putExtra("eventId", mList.get(arg2 - 1)
								.getEventId());
					} else {
						intent.putExtra("eventId", mList.get(arg2).getEventId());
					}

					mContext.startActivity(intent);

				}

			}
		});
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Log.d("PullToRefreshListView", "onPullDownToRefresh");
				onResh();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullUpToRefresh");
				// if (isListViewRefresh) {
				// onResh();
				// isListViewRefresh = false;
				// } else {
				onAddmoreData();
				// }

			}

		});
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if (arg1 == SCROLL_STATE_IDLE) {
					// sieve_ll.setVisibility(View.VISIBLE);

					// 停止滑动
					scroll_status = true;
				} else if (arg1 == SCROLL_STATE_TOUCH_SCROLL) {
					// sieve_ll.setVisibility(View.GONE);
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					sieve_ll.setVisibility(View.VISIBLE);
					// 手指还在上面滑动
					scroll_status = false;
				} else if (arg1 == SCROLL_STATE_FLING) {
					// sieve_ll.setVisibility(View.GONE);
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					sieve_ll.setVisibility(View.VISIBLE);
					// 正在滑动
					scroll_status = false;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				int top2 = head_sieve_ll.getTop();
				// if (top2 < 0 || firstVisibleItem >= 1) {
				// android.widget.FrameLayout.LayoutParams ll =
				// (android.widget.FrameLayout.LayoutParams) calender_ll
				// .getLayoutParams();
				// ll.setMargins(0, 30, 0, 0);
				// calender_ll.setVisibility(View.VISIBLE);
				// calender_ll.setLayoutParams(ll);
				// }else {
				// android.widget.FrameLayout.LayoutParams ll =
				// (android.widget.FrameLayout.LayoutParams) calender_ll
				// .getLayoutParams();
				// ll.setMargins(0, 50, 0, 0);
				// calender_ll.setVisibility(View.VISIBLE);
				// calender_ll.setLayoutParams(ll);
				// }

				num = firstVisibleItem;
				if (num >= 1 && animation_start == false) {
					ValueAnimator animator = ValueAnimator.ofInt(
							(int) (height), (int) (height / height_num));
					animator.setDuration(500);
					animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(
								ValueAnimator valueAnimator) {
							LayoutParams layoutParams = (LayoutParams) calendar
									.getLayoutParams();
							calendar_height = layoutParams.height;
							layoutParams.height = (Integer) valueAnimator
									.getAnimatedValue();
							calendar.setLayoutParams(layoutParams);

						}
					});
					animator.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animator) {
							animation_start = true;
							// FrameLayout.LayoutParams layoutParam = new
							// FrameLayout.LayoutParams(
							// LayoutParams.WRAP_CONTENT,
							// LayoutParams.WRAP_CONTENT);
							// layoutParam.setMargins(
							// MyApplication.getWindowWidth() / 5, 30,
							// 0, 0);
							// sieve_ll.setLayoutParams(layoutParam);
						}

						@Override
						public void onAnimationEnd(Animator animator) {
							int distance = 0;
							int windowheigth = 0;
							// Log.i("ceshi",
							// "看看高度===  "
							// + MyApplication.getWindowHeight()
							// + " pos== " + pos);
							windowheigth = MyApplication.getWindowHeight();
							Log.i("ceshi", "看看点击的 pos===  " + pos
									+ "  windowheigth==  " + windowheigth);
							if (pos <= 7) {
								distance = 0;
							} else if (pos < 14 && pos > 7) {
								distance = windowheigth / 13;
							} else if (pos < 21 && pos >= 14) {
								distance = (int) ((windowheigth / 6) * 0.9);
							} else if (pos < 28 && pos >= 21) {
								distance = (int) ((windowheigth / 4) * 0.9);
							} else if (pos < 35 && pos >= 28) {
								distance = (int) (windowheigth / 3.3);
							} else if (pos < 42 && pos >= 35) {
								distance = (int) (windowheigth * 1.2);
							}
							calendar.smoothScrollBy(distance, 1000);
						}

						@Override
						public void onAnimationCancel(Animator animator) {
						}

						@Override
						public void onAnimationRepeat(Animator animator) {

						}
					});
					animator.start();
				} else if (num <= 0 && animation_start) {
					ValueAnimator animator = ValueAnimator.ofInt(
							(int) (height / height_num), (int) (height));
					animator.setDuration(500);
					animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(
								ValueAnimator valueAnimator) {
							LayoutParams layoutParams = (LayoutParams) calendar
									.getLayoutParams();
							calendar_height = layoutParams.height;
							layoutParams.height = (Integer) valueAnimator
									.getAnimatedValue();
							calendar.setLayoutParams(layoutParams);
						}
					});
					animator.addListener(new Animator.AnimatorListener() {
						@Override
						public void onAnimationStart(Animator animator) {
							animation_start = false;
						}

						@Override
						public void onAnimationEnd(Animator animator) {
						}

						@Override
						public void onAnimationCancel(Animator animator) {
						}

						@Override
						public void onAnimationRepeat(Animator animator) {

						}
					});
					animator.start();
				}

			}
		});
		initListView();
	}

	/** 日历监听类 */
	class CalendarItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			DateEntity itemDate = (DateEntity) parent
					.getItemAtPosition(position);
			mAdapter.setSelect(position);
			now_time_position_change = false;
			pos = position;
			String todaymouth = today.substring(5, 7);
			String activityTime1 = today.substring(8, 10);
			String select_date;

			if (itemDate.month < 10) {
				String month = "0" + itemDate.month;
				select_date = itemDate.year + "-" + month + "-" + itemDate.day;
			} else {
				select_date = itemDate.year + "-" + itemDate.month + "-"
						+ itemDate.day;
			}

			if (itemDate.month == Integer.valueOf(todaymouth)) {
				if (itemDate.day >= Integer.valueOf(activityTime1)
						&& itemDate.day >= mday) {
					activityTime = select_date;
					mLoadingHandler.startLoading();
					mList.clear();
					getListData(0);
					mAdapter.notifyDataSetChanged();
				}
			} else if (itemDate.month >= Integer.valueOf(todaymouth)) {
				activityTime = select_date;
				mLoadingHandler.startLoading();
				mList.clear();
				getListData(0);
				mAdapter.notifyDataSetChanged();
			}
			// if (itemDate.month == Integer
			// .valueOf(todaymouth)) {
			// String activityTime1 = activityTime.substring(8, 10);
			// String today1 = today.substring(8, 10);
			//
			// if (Integer.valueOf(today1) <= Integer
			// .valueOf(activityTime1)) {
			// mLoadingHandler.startLoading();
			// mList.clear();
			// getListData(0);
			// } else {
			// }
			// } else if (Integer.valueOf(activitymouth) >= Integer
			// .valueOf(todaymouth)) {
			// String activityTime1 = activityTime.substring(8, 10);
			// String today1 = today.substring(8, 10);
			// mLoadingHandler.startLoading();
			// mList.clear();
			// getListData(0);
			// mLoadingHandler.startLoading();
			// mList.clear();
			// getListData(0);
			// Toast.makeText(
			// getActivity(),
			// "选中的是" + itemDate.year + "年" + itemDate.month + "月"
			// + itemDate.day + "日" + "--" + itemDate.weekDay,
			// Toast.LENGTH_SHORT).show();

		}
	}

	/**
	 * 初始化listview的数据
	 */
	private void initListData() {
		mPage = 0;
		isRefresh = true;
		mList = new ArrayList<EventInfo>();
		mListAdapter = new WeekActivityListAdapter(getActivity(), mList,
				listBannerInfo);
		mListAdapter.setWeekLisenner(this);
		mListView.setAdapter(mListAdapter);
		// getTypeTag();
	}

	private void onAddmoreData() {
		mPage = HttpUrlList.HTTP_NUM + mPage;
		addmore_bool = true;
		getListData(mPage);
	}

	private void onResh() {
		isRefresh = true;
		mPage = 0;
		getListData(mPage);
	}

	public StringBuffer getTag(HashMap<Integer, Boolean> map) {
		StringBuffer tagAll = new StringBuffer();
		StringBuffer tag = new StringBuffer();
		if (map.size() == 0) {
			return removedouhao(tagAll);
		} else {
			for (int i = 0; i < map.size(); i++) {
				if (map.get(i)) {
					if (i == 0) {
						return removedouhao(tagAll);
					} else {
						tag.append(mflagList.get(i - 1).getTagId());
						tag.append(",");
					}
				}
			}
		}
		Log.d("tagtagtag", tag.toString() + "====");

		return removedouhao(tag);
	}

	public StringBuffer removedouhao(StringBuffer stringBuffer) {

		if (stringBuffer.length() > 0) {
			stringBuffer = stringBuffer.replace(stringBuffer.length() - 1,
					stringBuffer.length(), "");
		}
		return stringBuffer;
	}

	/**
	 * 静态数据
	 */
	private void addflag() {
		mflagList = new ArrayList<UserBehaviorInfo>();
		mflagList.clear();
		mflagList.addAll(MyApplication.getInstance().getSelectTypeList());
		for (int i = 0; i < MyApplication.getInstance().getSelectTypeList()
				.size(); i++) {
			mflagList.get(i).setSelect(false);
		}
	}

	/**
	 * 获取广告
	 */
	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		if (activityTime.equals("")) {
			activityTime = TextUtil.getToDay();
		}
		params.put("date", activityTime);
		MyHttpRequest.onHttpPostParams(HttpUrlList.Banner.CALENDER_BANNER_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.i("ceshi", "看看是否有数据==  " + resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							listBannerInfo.clear();
							listBannerInfo = JsonUtil
									.getCalenderBannerInfo(resultStr);

						} else {

						}

					}
				});
	}

	/**
	 * 获取20条活动的数据
	 */
	private void getListData(int page) {
		mLoadingHandler.startLoading();
		getData();
		mParams = new HashMap<String, String>();
		// MyApplication.getInstance().setActivityType(activityType);
		// mParams.put(HttpUrlList.HTTP_USER_ID,
		// MyApplication.loginUserInfor.getUserId());
		if (activityTime.equals("")) {
			activityTime = TextUtil.getToDay();
		}

		// 商圈
		if (venueMood != "" && venueMood != null) {
			mParams.put("activityLocation", venueMood);
		}
		// 区域
		if (venueArea != "" && venueArea != null) {
			mParams.put("activityArea", venueArea);
		}
		mParams.put("activityType", selectActivityType);

		mParams.put("activityIsFree", activityIsFree);
		mParams.put("activityIsReservation", activityIsReservation);

		mParams.put("startDate", activityTime);
		mParams.put("endDate", activityTime);
		// mParams.put(HttpUrlList.HTTP_LAT,
		// AppConfigUtil.LocalLocation.Location_latitude + "");
		// mParams.put(HttpUrlList.HTTP_LON,
		// AppConfigUtil.LocalLocation.Location_longitude + "");
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")) {
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, 20 + "");
		// 活动类型标签id（可选）用逗号分隔，没有就传空字符串
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyEvent.WFF_APPACTIVITYCALENDARLIST, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "-看看日历数据--"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {

							try {
								List<EventInfo> mAddList = new ArrayList<EventInfo>();
								// mList.clear();
								mAddList = JsonUtil.getEventList(resultStr);
								if (mAddList.size() == 0 && mList.size() == 0) {
									collection_null.setVisibility(View.VISIBLE);
									mListView.setVisibility(View.GONE);
									// sieve_ll.setVisibility(View.GONE);
								} else {
									sieve_ll.setVisibility(View.VISIBLE);
									collection_null.setVisibility(View.GONE);
									mListView.setVisibility(View.VISIBLE);
									if (mAddList.size() > 0) {
										if (isRefresh) {
											isRefresh = false;
											mList.clear();
											// EventInfo nullingo = new
											// EventInfo();
											// mAddList.add(0, nullingo);
											// mAddList.add(1, nullingo);
										}
										if (listBannerInfo.size() != 0) {
											mList.add(mAddList.get(0));
										}
										mList.addAll(mAddList);
										mListAdapter.setList(mList,
												listBannerInfo);
									} else {
										if (addmore_bool) {

										} else {
											collection_null
													.setVisibility(View.VISIBLE);
											mListView.setVisibility(View.GONE);

											isFirstResh = true;
											addmore_bool = false;
										}
										LoadingTextShowPopWindow
												.showLoadingText(getActivity(),
														Topview, "数据为空");
										isFirstResh = true;
									}
									mLoadingHandler.stopLoading();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								mLoadingHandler.showErrorText(e.toString());
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
							ToastUtil.showToast("数据请求失败:" + resultStr);
						}
						handler.sendEmptyMessageDelayed(REFRESH, 200);
					}
				});
	}

	/**
	 * 获取每天的活动数
	 */
	private void getEveryDay(String date_start, String date_end) {
		MyApplication.getInstance().setActivityType(activityType);
		mEvery_Params = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// 获取前月的第一天
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		Calendar cal_2 = Calendar.getInstance();// 获取当前日期
		Calendar cal_3 = Calendar.getInstance();// 获取当前日期
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(cal_1.getTime());

		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH,
				ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());

		cal_2.add(Calendar.MONTH, month);

		cal_2.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		int curStartIndex;
		int curEndIndex;
		int dayInWeek = cal_2.get(Calendar.DAY_OF_WEEK);
		int monthStart = dayInWeek;
		if (monthStart == 1) {
			monthStart = 8;
		}
		monthStart -= 1; // 以日为开头-1，以星期一为开头-2
		curStartIndex = monthStart;
		if (curStartIndex >= 7) {
			MyApplication.line_data = 1;
		}
		cal_3.add(Calendar.MONTH, month);
		int MaxDay = cal_2.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal_3.set(Calendar.DAY_OF_MONTH, MaxDay);
		// curStartIndex 必须大于7 则消除第一行
		int monthDay = cal_3.get(Calendar.DAY_OF_MONTH);
		curEndIndex = monthStart + monthDay;
		// curEndIndex 大于35 则消除最后一行
		if (curEndIndex < 36) {
			MyApplication.line_data = 2;
		}
		if (curStartIndex < 7 && curEndIndex >= 36) {
			MyApplication.line_data = 0;
		}

		if (date_start == "") {
			mEvery_Params.put("startDate", firstDay.toString());
			mEvery_Params.put("endDate", last.toString());
		} else {
			mEvery_Params.put("startDate", date_start);
			mEvery_Params.put("endDate", date_end);
		}

		String urlString = "";
		urlString = HttpUrlList.MyEvent.WFF_APPEVERYDATEACTIVITYCOUNT;
		MyHttpRequest.onHttpPostParams(urlString, mEvery_Params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "--日历场数-" + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								ArrayList<String> mAddList = new ArrayList<String>();
								mAddList.clear();
								mAddList = JsonUtil.getEveryDataList(resultStr);
								mAdapter.setdate(mAddList);
								mAdapter.setNowTime(mMonth, mday);

								mAdapter.notifyDataSetChanged();
								// calendar.change_view();
								mLoadingHandler.stopLoading();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								mLoadingHandler.showErrorText(e.toString());
							}

						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
						handler.sendEmptyMessageDelayed(REFRESH, 200);
					}
				});
	}

	/**
	 * 初始化listview的数据
	 */
	private void initListView() {
		mPage = 0;
		mList = new ArrayList<EventInfo>();
		for (int i = 0; i < 15; i++) {
			mList.add(new EventInfo());
		}
		mWeekActivityListAdapter = new WeekActivityListAdapter(mContext, mList,
				listBannerInfo);
		mListView.setAdapter(mWeekActivityListAdapter);
		addflag();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.this_week, menu);
	// return true;
	// }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String[] ya;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal_1 = Calendar.getInstance();// 获取第一天
		Calendar cal_2 = Calendar.getInstance();// 获取最后一天
		String firstDay, lastDay;
		int MaxDay;
		switch (arg0.getId()) {

		case R.id.title_left:
			getActivity().finish();
			mflagList.clear();
			break;
		case R.id.title_send:
			// mWeekPopupWindow.showWeekPopupWindow(mContext, mTitle, right,
			// drawable, mflagList, new WeekPopupCallback() {
			// @Override
			// public void referShing() {
			// // TODO Auto-generated method stub
			// onResh();
			// }
			// });
			break;
		case R.id.right_iv:
			now_time_position_change = true;
			mListView.getRefreshableView().setSelection(0);
			if (month > 2) {
				right_iv.setVisibility(View.INVISIBLE);
				break;
			} else {
				if (month >= 0) {
					left_iv.setVisibility(View.VISIBLE);
				}
				if (month >= 2) {
					right_iv.setVisibility(View.INVISIBLE);
				} else {
					right_iv.setVisibility(View.VISIBLE);
				}
				mNowCalendarPoint = mCalendarTool.getNowCalendar();
				mDateEntityList.clear();
				if (mNowCalendarPoint.x >= 1990 && mNowCalendarPoint.x < 2038) {
					if (mNowCalendarPoint.y + 1 > 12) {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x + 1, 1);
					} else {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x, mNowCalendarPoint.y + 1);
					}
					mAdapter.setDateList(mDateEntityList);
					if (mDateEntityList.size() > 40) {
						MyApplication
								.setGridViewHeightBasedOnChildrenCalendertwo(calendar);
						LayoutParams l = (LayoutParams) calendar
								.getLayoutParams();
						height = l.height;
						height_num = 6;
					} else {
						MyApplication
								.setGridViewHeightBasedOnChildrenCalender(calendar);
						LayoutParams l = (LayoutParams) calendar
								.getLayoutParams();
						height = l.height;
						height_num = 5;
					}
					cal_1.add(Calendar.MONTH, month + 1);
					cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
					firstDay = format.format(cal_1.getTime());
					cal_2.add(Calendar.MONTH, month + 1);
					MaxDay = cal_2.getActualMaximum(Calendar.DAY_OF_MONTH);
					cal_2.set(Calendar.DAY_OF_MONTH, MaxDay);
					lastDay = format.format(cal_2.getTime());
					month = month + 1;
					getEveryDay(firstDay, lastDay);
					mAdapter.notifyDataSetChanged();
					mNowCalendarPoint = mCalendarTool.getNowCalendar();
					middle_tv.setText(mNowCalendarPoint.x + "."
							+ mNowCalendarPoint.y);
				}
			}
			// if (MyApplication.num_data == 7) {

			//

			//
			// /* 这里是判断 Calender的高度 */
			// // int curStartIndex;
			// // int curEndIndex;
			// // int dayInWeek = cal_1.get(Calendar.DAY_OF_WEEK);
			// // int monthStart = dayInWeek;
			// // if (monthStart == 1) {
			// // monthStart = 8;
			// // }
			// // monthStart -= 1; // 以日为开头-1，以星期一为开头-2
			// // curStartIndex = monthStart;
			// // if (curStartIndex >= 7) {
			// // MyApplication.line_data = 1;
			// // }
			// //
			// // // curStartIndex 必须大于7 则消除第一行
			// // int monthDay = cal_2.get(Calendar.DAY_OF_MONTH);
			// // curEndIndex = monthStart + monthDay;
			// // // curEndIndex 大于35 则消除最后一行
			// // if (curEndIndex < 36) {
			// // MyApplication.line_data = 2;
			// // }
			// // if (curStartIndex < 7 && curEndIndex >= 36) {
			// // MyApplication.line_data = 0;
			// // }
			// // Log.i("ceshi", "firstDay  ==  " + firstDay +
			// // " lastDay== "
			// // + lastDay + "  curStartIndex " + curStartIndex
			// // + "  curEndIndex  ==  " + curEndIndex
			// // + "  monthStart  ==" + monthStart
			// // + "  monthDay  ==" + monthDay);
			// month = month + 1;
			// getEveryDay(firstDay, lastDay);
			// String rightYearAndmonth = calendar.clickRightMonth();
			// ya = rightYearAndmonth.split("-");
			// middle_tv.setText(ya[0] + "." + ya[1]);
			//
			// }
			// MyApplication.num_data = 20;
			// // prevent = false;
			// mListView.getRefreshableView().setSelection(0);
			// mLoadingHandler.startLoading();
			// getListData(0);
			// } else {
			// if (month > 2) {
			// right_iv.setVisibility(View.INVISIBLE);
			// break;
			// } else {
			// if (month >= 0) {
			// left_iv.setVisibility(View.VISIBLE);
			// }
			// if (month >= 2) {
			// right_iv.setVisibility(View.INVISIBLE);
			// } else {
			// right_iv.setVisibility(View.VISIBLE);
			// }
			//
			// cal_1.add(Calendar.MONTH, month + 1);
			// cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			// firstDay = format.format(cal_1.getTime());
			// cal_2.add(Calendar.MONTH, month + 1);
			// MaxDay = cal_2.getActualMaximum(Calendar.DAY_OF_MONTH);
			// cal_2.set(Calendar.DAY_OF_MONTH, MaxDay);
			// lastDay = format.format(cal_2.getTime());
			//
			// /* 这里是判断 Calender的高度 */
			// // int curStartIndex;
			// // int curEndIndex;
			// // int dayInWeek = cal_1.get(Calendar.DAY_OF_WEEK);
			// // int monthStart = dayInWeek;
			// // if (monthStart == 1) {
			// // monthStart = 8;
			// // }
			// // monthStart -= 1; // 以日为开头-1，以星期一为开头-2
			// // curStartIndex = monthStart;
			// // if (curStartIndex >= 7) {
			// // MyApplication.line_data = 1;
			// // }
			// //
			// // // curStartIndex 必须大于7 则消除第一行
			// // int monthDay = cal_2.get(Calendar.DAY_OF_MONTH);
			// // curEndIndex = monthStart + monthDay;
			// // // curEndIndex 大于35 则消除最后一行
			// // if (curEndIndex < 36) {
			// // MyApplication.line_data = 2;
			// // }
			// // if (curStartIndex < 7 && curEndIndex >= 36) {
			// // MyApplication.line_data = 0;
			// // }
			// // Log.i("ceshi", "firstDay  ==  " + firstDay +
			// // " lastDay== "
			// // + lastDay + "  curStartIndex " + curStartIndex
			// // + "  curEndIndex  ==  " + curEndIndex
			// // + "  monthStart  ==" + monthStart
			// // + "  monthDay  ==" + monthDay);
			// month = month + 1;
			// getEveryDay(firstDay, lastDay);
			// String rightYearAndmonth = calendar.clickRightMonth();
			// ya = rightYearAndmonth.split("-");
			// middle_tv.setText(ya[0] + "." + ya[1]);
			//
			// }
			// }
			// }
			break;
		case R.id.left_iv:
			now_time_position_change = true;
			mListView.getRefreshableView().setSelection(0);
			if (month <= 0) {
				left_iv.setVisibility(View.INVISIBLE);
				break;
			} else {
				if (month <= 1) {
					right_iv.setVisibility(View.VISIBLE);
					left_iv.setVisibility(View.INVISIBLE);
				} else {
					right_iv.setVisibility(View.VISIBLE);
					left_iv.setVisibility(View.VISIBLE);
				}
				mDateEntityList.clear();
				mNowCalendarPoint = mCalendarTool.getNowCalendar();
				if (mNowCalendarPoint.x >= 1990 && mNowCalendarPoint.x < 2038) {
					if (mNowCalendarPoint.y - 1 <= 0) {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x - 1, 12);
					} else {
						mDateEntityList = mCalendarTool.getDateEntityList(
								mNowCalendarPoint.x, mNowCalendarPoint.y - 1);
					}

					mAdapter.setDateList(mDateEntityList);
					if (mDateEntityList.size() > 40) {
						MyApplication
								.setGridViewHeightBasedOnChildrenCalendertwo(calendar);
						LayoutParams l = (LayoutParams) calendar
								.getLayoutParams();
						height = l.height;
						height_num = 6;
					} else {
						MyApplication
								.setGridViewHeightBasedOnChildrenCalender(calendar);
						LayoutParams l = (LayoutParams) calendar
								.getLayoutParams();
						height = l.height;
						height_num = 5;
					}
					cal_1.add(Calendar.MONTH, month - 1);
					cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
					firstDay = format.format(cal_1.getTime());
					cal_2.add(Calendar.MONTH, month - 1);
					MaxDay = cal_2.getActualMaximum(Calendar.DAY_OF_MONTH);
					cal_2.set(Calendar.DAY_OF_MONTH, MaxDay);
					lastDay = format.format(cal_2.getTime());
					month = month - 1;
					getEveryDay(firstDay, lastDay);
					mAdapter.notifyDataSetChanged();
					mNowCalendarPoint = mCalendarTool.getNowCalendar();
					middle_tv.setText(mNowCalendarPoint.x + "."
							+ mNowCalendarPoint.y);
				}
			}
			// }
			// if (MyApplication.num_data == 7) {
			// if (month <= 0) {
			// left_iv.setVisibility(View.INVISIBLE);
			// break;
			// } else {
			// if (month <= 1) {
			// right_iv.setVisibility(View.VISIBLE);
			// left_iv.setVisibility(View.INVISIBLE);
			// } else {
			// right_iv.setVisibility(View.VISIBLE);
			// left_iv.setVisibility(View.VISIBLE);
			// }
			//
			// cal_1.add(Calendar.MONTH, month - 1);
			// cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			// firstDay = format.format(cal_1.getTime());
			// cal_2.add(Calendar.MONTH, month - 1);
			// MaxDay = cal_2.getActualMaximum(Calendar.DAY_OF_MONTH);
			// cal_2.set(Calendar.DAY_OF_MONTH, MaxDay);
			// lastDay = format.format(cal_2.getTime());
			//
			// // /* 这里是判断 Calender的高度 */
			// // int curStartIndex;
			// // int curEndIndex;
			// // int dayInWeek = cal_1.get(Calendar.DAY_OF_WEEK);
			// // int monthStart = dayInWeek;
			// // if (monthStart == 1) {
			// // monthStart = 8;
			// // }
			// // monthStart -= 1; // 以日为开头-1，以星期一为开头-2
			// // curStartIndex = monthStart;
			// // if (curStartIndex >= 7) {
			// // MyApplication.line_data = 1;
			// // }
			// //
			// // // curStartIndex 必须大于7 则消除第一行
			// // int monthDay = cal_2.get(Calendar.DAY_OF_MONTH);
			// // curEndIndex = monthStart + monthDay;
			// // // curEndIndex 大于35 则消除最后一行
			// // if (curEndIndex > 35) {
			// // MyApplication.line_data = 2;
			// // }
			// month = month - 1;
			// getEveryDay(firstDay, lastDay);
			// String leftYearAndmonth = calendar.clickLeftMonth();
			// ya = leftYearAndmonth.split("-");
			// middle_tv.setText(ya[0] + "." + ya[1]);
			//
			// }
			// MyApplication.num_data = 20;
			// // prevent = false;
			// mListView.getRefreshableView().setSelection(0);
			// mLoadingHandler.startLoading();
			// getListData(0);
			//
			// } else {
			// if (month <= 0) {
			// left_iv.setVisibility(View.INVISIBLE);
			// break;
			// } else {
			// if (month <= 1) {
			// right_iv.setVisibility(View.VISIBLE);
			// left_iv.setVisibility(View.INVISIBLE);
			// } else {
			// right_iv.setVisibility(View.VISIBLE);
			// left_iv.setVisibility(View.VISIBLE);
			// }
			//
			// cal_1.add(Calendar.MONTH, month - 1);
			// cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			// firstDay = format.format(cal_1.getTime());
			// cal_2.add(Calendar.MONTH, month - 1);
			// MaxDay = cal_2.getActualMaximum(Calendar.DAY_OF_MONTH);
			// cal_2.set(Calendar.DAY_OF_MONTH, MaxDay);
			// lastDay = format.format(cal_2.getTime());
			//
			// // /* 这里是判断 Calender的高度 */
			// // int curStartIndex;
			// // int curEndIndex;
			// // int dayInWeek = cal_1.get(Calendar.DAY_OF_WEEK);
			// // int monthStart = dayInWeek;
			// // if (monthStart == 1) {
			// // monthStart = 8;
			// // }
			// // monthStart -= 1; // 以日为开头-1，以星期一为开头-2
			// // curStartIndex = monthStart;
			// // if (curStartIndex >= 7) {
			// // MyApplication.line_data = 1;
			// // }
			// //
			// // // curStartIndex 必须大于7 则消除第一行
			// // int monthDay = cal_2.get(Calendar.DAY_OF_MONTH);
			// // curEndIndex = monthStart + monthDay;
			// // // curEndIndex 大于35 则消除最后一行
			// // if (curEndIndex > 35) {
			// // MyApplication.line_data = 2;
			// // }
			// month = month - 1;
			// getEveryDay(firstDay, lastDay);
			// String leftYearAndmonth = calendar.clickLeftMonth();
			// ya = leftYearAndmonth.split("-");
			// middle_tv.setText(ya[0] + "." + ya[1]);
			//
			// }
			//
			// }

			break;
		case R.id.shopping_areas:
			if (scroll_status) {
				showPopuwindow(1);
			}

			break;
		case R.id.preface:
			if (scroll_status) {
				showPopuwindow(2);
			}
			break;
		case R.id.sieve:
			if (scroll_status) {
				showPopuwindow(3);
			}
			break;
		case R.id.tv:
			if (pw.isShowing()) {
				pw.dismiss();
			}
			sieve_ll.setVisibility(View.VISIBLE);
			selectActivityType = mTypeList.get(0).getTagId();
			preface.setText(mTypeList.get(0).getTagName());
			getListData(0);
			isRefresh = true;
			addmore_bool = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void onWeekClick(String date) {
		// ToastUtil.showToast(date);
		activityTime = date;
		onResh();
		topLaBel.setText(TextUtil.Time2Format(date));
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public interface WeekPopupCallback {
		public void referShing();
	}

	private void showPopuwindow(int num) {
		View mView;
		ListView listView;
		ImageView arrow_up;
		TextView tv;
		int height;
		ArrayList<Wff_VenuePopuwindow> list;
		if (pw != null) {
			pw.dismiss();
		}
		switch (num) {
		// 全部的商区

		case 1:
			sieve_ll.setVisibility(View.GONE);
			mView = View.inflate(getActivity(),
					R.layout.home_popuwindow_shopping, null);
			ListView listView_left = (ListView) mView
					.findViewById(R.id.listView_left);
			final ListView listView_right = (ListView) mView
					.findViewById(R.id.listView_right);
			arrow_up = (ImageView) mView.findViewById(R.id.arrow_up);
			arrow_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw.isShowing()) {
						pw.dismiss();
					}
					sieve_ll.setVisibility(View.VISIBLE);
				}
			});
			hpwa = new HomePopuWindowAdapter(list_area, getActivity());
			listView_left.setAdapter(hpwa);
			listView_left.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1,
						int arg2, long arg3) {
					if (arg2 == 0) {
						venueArea = "";
						venueDicName = "";
						venueMood = "";
						venueMoodName = "";
						shopping_areas.setText("全上海");
						getListData(0);
						isRefresh = true;
						addmore_bool = false;
						pw.dismiss();
						sieve_ll.setVisibility(View.VISIBLE);
						return;
					}
					view = arg1;
					itemBackChanged(arg1);

					wvpw = (Wff_VenuePopuwindow) parent.getItemAtPosition(arg2);
					venueArea = wvpw.getDictCode();
					venueDicName = wvpw.getDictName();
					list_area_z = new ArrayList<Wff_VenuePopuwindow>();
					list_area_z.add(new Wff_VenuePopuwindow("", "全部"
							+ wvpw.getDictName()));
					for (int i = 0; i < wvpw.getDictList().length(); i++) {
						try {
							JSONObject json = wvpw.getDictList().getJSONObject(
									i);
							list_area_z.add(new Wff_VenuePopuwindow(json
									.optString("id"), json.optString("name")));
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					HomePopuWindowAreaAdapter vpwaa = new HomePopuWindowAreaAdapter(
							list_area_z, getActivity());
					listView_right.setAdapter(vpwaa);

					listView_right
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
											.getItemAtPosition(arg2);
									if (arg2 == 0) {
										venueMood = "";
										venueMoodName = wvp.getTagName();
										shopping_areas.setText(wvp.getTagName());
										if (pw != null) {
											pw.dismiss();
										}
										getListData(0);
										isRefresh = true;
										addmore_bool = false;
										sieve_ll.setVisibility(View.VISIBLE);
										return;
									}
									venueMood = wvp.getTagId();
									venueMoodName = wvp.getTagName();
									shopping_areas.setText(wvp.getTagName());
									sieve_ll.setVisibility(View.VISIBLE);
									getListData(0);
									isRefresh = true;
									addmore_bool = false;
									if (pw != null) {
										pw.dismiss();
									}
								}
							});
				}
			});
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 2,
					MyApplication.getWindowHeight() / 4);
			pw.showAsDropDown(sieve_ll, 40, 0);
			// pw.showAtLocation(, Gravity.CENTER, 0, 0);
			break;
		case 2:
			sieve_ll.setVisibility(View.GONE);
			mView = View.inflate(getActivity(),
					R.layout.home_popuwindow_preface, null);
			arrow_up = (ImageView) mView.findViewById(R.id.arrow_up);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			tv.setText(mTypeList.get(0).getTagName());
			for (int i = 1; i < mTypeList.size(); i++) {
				list.add(new Wff_VenuePopuwindow(mTypeList.get(i).getTagId(),
						mTypeList.get(i).getTagName()));
			}
			cpwa = new CalendarPopuWindowAdapter(list, getActivity());
			listView.setAdapter(cpwa);
			height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.9);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			pw.showAsDropDown(sieve_ll, 100, 0);
			arrow_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null) {
						pw.dismiss();
						sieve_ll.setVisibility(View.VISIBLE);
					}
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
							.getItemAtPosition(arg2);
					selectActivityType = wvp.getTagId();

					if (pw != null) {
						pw.dismiss();
						preface.setText(wvp.getTagName());
						sieve_ll.setVisibility(View.VISIBLE);
					}
					getListData(0);
					isRefresh = true;
					addmore_bool = false;

				}
			});
			break;
		case 3:
			sieve_ll.setVisibility(View.GONE);
			mView = View.inflate(getActivity(), R.layout.home_popuwindow_sieve,
					null);
			arrow_up = (ImageView) mView.findViewById(R.id.arrow_up);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw.isShowing()) {
						pw.dismiss();
					}
					sieve_ll.setVisibility(View.VISIBLE);
					activityIsFree = "";
					activityIsReservation = "";
					getListData(0);
					isRefresh = true;
					addmore_bool = false;
					sieve.setText("全部");
				}
			});
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("免费"));
			list.add(new Wff_VenuePopuwindow("在线预订"));
			HomeCenterPopuWindowAdapter hcpwa = new HomeCenterPopuWindowAdapter(
					list, getActivity());
			listView.setAdapter(hcpwa);
			height = MyApplication.getWindowHeight() / 4;
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			pw.showAsDropDown(sieve_ll, 100, 0);
			arrow_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null) {
						pw.dismiss();
						sieve_ll.setVisibility(View.VISIBLE);
					}
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
							.getItemAtPosition(arg2);
					if (arg2 == 0) {
						activityIsFree = "1";
						activityIsReservation = "";
					} else if (arg2 == 1) {
						activityIsFree = "";
						activityIsReservation = "2";
					}
					if (pw != null) {
						pw.dismiss();
						sieve.setText(wvp.getDictName());
						sieve_ll.setVisibility(View.VISIBLE);
					}
					getListData(0);
					isRefresh = true;

				}
			});
			break;

		default:
			break;
		}
	}

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
		mBgColor = R.color.text_color_26;
		// 将上次点击的listitem的背景色设置成透明
		view.setBackgroundResource(mBgColor);
		// 设置当前点击的listitem的背景色
		itemView = view;

	}

	private void initDataArea() {
		Map<String, String> mParams = new HashMap<String, String>();
		MyHttpRequest.onStartHttpGET(HttpUrlList.Venue.WFF_GETALLAREA, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "--获取区域数据-"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								list_area = new ArrayList<Wff_VenuePopuwindow>();
								JSONObject json = new JSONObject(resultStr);
								String status = json.optString("status");
								if (status.equals("200")) {
									JSONArray json_arr = json
											.optJSONArray("data");
									list_area.add(new Wff_VenuePopuwindow("",
											"全上海", "", new JSONArray()));
									for (int i = 0; i < json_arr.length(); i++) {
										JSONObject json_new = json_arr
												.getJSONObject(i);

										String dictId = json_new
												.optString("dictId");
										String dictName = json_new
												.optString("dictName");
										String dictCode = json_new
												.optString("dictCode");
										JSONArray dictList = json_new
												.optJSONArray("dictList");
										list_area.add(new Wff_VenuePopuwindow(
												dictId, dictName, dictCode,
												dictList));

									}
									handler.sendEmptyMessage(2);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	public void getTypeList() {

		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							mTypeList.addAll(JsonUtil
									.getTypeDataList(resultStr));
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}
}
