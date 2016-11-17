package com.sun3d.culturalShanghai.windows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.GetPhoneInfoUtil;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.SplashActivity;
import com.sun3d.culturalShanghai.adapter.WindowsAdapter;
import com.sun3d.culturalShanghai.adapter.WindowsTwoAdapter;
import com.sun3d.culturalShanghai.calender.CalendarActivity;
import com.sun3d.culturalShanghai.calender.CalendarAdapter;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.EventAddressInfo;
import com.sun3d.culturalShanghai.object.SearchInfo;
import com.sun3d.culturalShanghai.object.SearchListInfo;
import com.sun3d.culturalShanghai.object.WindowInfo;

/**
 * 选择活动时候 首页的右上角搜索的按键后呈现出来的Popupwindow
 * 
 * @author wenff
 * 
 */
public class EventWindows implements OnGestureListener, OnClickListener {
	private PopupWindow popup;
	private View mView;
	private LinearLayout mCalender;
	private List<DayClass> selectDay;
	private GestureDetector gestureDetector;
	private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private CalendarAdapter calV;
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	private String currentDate = "";
	private Context mContext;
	private GridView mGridView;
	private TextView mTopTime;
	private TextView mStartTime;
	private TextView mEndTime;
	private ScrollView mScrollView;
	private Button mSend;
	private TextView history_tv;
	public static ActivityConditionInfo activityInfo;
	private TextView hide, hide_classication, hide_hotword;
	private static ArrayList<HashMap<String, String>> list_event = new ArrayList<HashMap<String, String>>();
	private final int GRIDVIEW_NUM = 4;
	/**
	 * 条件数据
	 */
	private SearchListInfo searchListInfo;
	private List<SearchInfo> moodList;
	private List<SearchInfo> personalList;
	private List<SearchInfo> typeList;
	private EditText etContent;
	private boolean isMain = false;
	private RelativeLayout mTop;
	private GridView mTimeGrid;
	private List<SearchInfo> timeList;
	private WindowsAdapter mTimeAdapter;
	private boolean hide_bool = false;
	private boolean hide_bool_classication = false;
	private boolean hide_bool_hot_word = false;
	private SimpleAdapter adapter;
	private ListView listview;
	private ArrayList<HashMap<String, String>> list;
	private TextView all_delete;

	private class DayClass {
		public int id;
		/**
		 * 阳历
		 */
		private String scheduleDay;
		/**
		 * 星期几
		 */
		private String week;
		/**
		 * 阴历
		 */
		private String LunarDay;
		private int day;
		private int month;
	}

	public static EventWindows getInstance(Context mContext) {
		if (searchWindows == null) {
			searchWindows = new EventWindows(mContext);
		}
		list_event = SharedPreManager.wffreadEventAddressInfor();
		return searchWindows;
	}

	@SuppressLint("SimpleDateFormat")
	public EventWindows(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		currentDate = sdf.format(date); // 当期日期
		year_c = Integer.parseInt(currentDate.split("-")[0]);
		month_c = Integer.parseInt(currentDate.split("-")[1]);
		day_c = Integer.parseInt(currentDate.split("-")[2]);
		searchListInfo = MyApplication.getInstance().getSearchListInfo();
		activityInfo = new ActivityConditionInfo();
		initView();
		getData();
	}

	/**
	 * 不同的Type 显示不同的布局
	 */
	public void showSearch(RelativeLayout mTop, boolean isMain) {
		this.isMain = isMain;
		this.mTop = mTop;
		activityInfo.setTabType(MyApplication.getInstance().getActivityType());
		// 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		if ("1".equals(MyApplication.getInstance().getActivityType())) {
			mLocaitonLayout.setVisibility(View.GONE);
			mClassicationLayout.setVisibility(View.GONE);
			mHotWordLayout.setVisibility(View.GONE);
			mTimeLayout.setVisibility(View.GONE);
		} else if ("2".equals(MyApplication.getInstance().getActivityType())) {
			mTimeLayout.setVisibility(View.GONE);
			mLocaitonLayout.setVisibility(View.VISIBLE);
			mClassicationLayout.setVisibility(View.VISIBLE);
			mHotWordLayout.setVisibility(View.VISIBLE);
		} else {
			mLocaitonLayout.setVisibility(View.VISIBLE);
			mClassicationLayout.setVisibility(View.VISIBLE);
			mHotWordLayout.setVisibility(View.VISIBLE);
			mTimeLayout.setVisibility(View.GONE);
		}
		if (searchListInfo == null || searchListInfo.getMoodList() == null) {
			// 当spash界面没有获取到数据时重新获取
			getData(mTop);
		} else {
			initData();
			if (isMain) {
				onReset();
			}
			if (!popup.isShowing()) {
				popup.showAsDropDown(mTop, 0, -DensityUtil.dip2px(mContext, 50));
			}
		}
	}

	/**
	 * 不同的Type 显示不同的布局
	 */
	public void showSearchTest(RelativeLayout mTop, boolean isMain) {
		this.isMain = isMain;
		// this.mTop = mTop;
		// activityInfo.setTabType(MyApplication.getInstance().getActivityType());
		// // 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		// if ("1".equals(MyApplication.getInstance().getActivityType())) {
		// mLocaitonLayout.setVisibility(View.GONE);
		// mClassicationLayout.setVisibility(View.GONE);
		// mHotWordLayout.setVisibility(View.GONE);
		// mTimeLayout.setVisibility(View.GONE);
		// } else if ("2".equals(MyApplication.getInstance().getActivityType()))
		// {
		// mTimeLayout.setVisibility(View.GONE);
		// mLocaitonLayout.setVisibility(View.VISIBLE);
		// mClassicationLayout.setVisibility(View.VISIBLE);
		// mHotWordLayout.setVisibility(View.VISIBLE);
		// } else {
		// mLocaitonLayout.setVisibility(View.VISIBLE);
		// mClassicationLayout.setVisibility(View.VISIBLE);
		// mHotWordLayout.setVisibility(View.VISIBLE);
		// mTimeLayout.setVisibility(View.GONE);
		// }
		// if (searchListInfo == null || searchListInfo.getMoodList() == null) {
		// // 当spash界面没有获取到数据时重新获取
		// getData(mTop);
		// } else {
		// initData();
		// if (isMain) {
		// onReset();
		// }
		if (!popup.isShowing()) {
			popup.showAsDropDown(mTop, 0, -DensityUtil.dip2px(mContext, 50));
		}
		// }
	}

	private void initData() {
		// 获取数据
		onListData();
		// 选择tab
		onSelectTab();
		// 选择心情 现在的区域
		onSelectMood();
		// 选择人群 3.5的热门话题
		onSelectPersonal();
		// 选择时间
		onSelectTime();
		// 选择类型 3.5的分类
		onSelectType();
		// 选择地区
		onSelectAdress();
	}

	private void onListData() {
		list = MyApplication.getInstenceActivityList();
		if (list_event != null && list_event.size() != 0) {
			adapter = new SimpleAdapter(mContext, list_event,
					R.layout.textview_layout, new String[] { "tv" },
					new int[] { R.id.tv });
			listview.setVisibility(View.VISIBLE);
			history_tv.setVisibility(View.VISIBLE);
			all_delete.setVisibility(View.VISIBLE);
		} else {
			adapter = new SimpleAdapter(mContext, list,
					R.layout.textview_layout, new String[] { "tv" },
					new int[] { R.id.tv });
		}

		listview.setAdapter(adapter);
		MyApplication.setListViewHeightBasedOnChildren(listview);
		adapter.notifyDataSetChanged();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				HashMap<String, String> map = (HashMap<String, String>) arg0
						.getItemAtPosition(arg2);
				String tv = map.get("tv");
				activityInfo.setActivityKeyWord(tv);
				onStartActivity();
			}
		});
	}

	/**
	 * 显示搜索界面
	 * 
	 * @param
	 */
	private void initView() {
		mView = View.inflate(mContext, R.layout.windows_event, null);
		if (GetPhoneInfoUtil.getManufacturers().equals("Meizu")) {
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					WindowsUtil.getwindowsHight(mContext)
							- WindowsUtil.getDownMenuHeight(mContext));
		} else if (GetPhoneInfoUtil.getManufacturers().equals("HUAWEI")
				&& !WindowsUtil.getMenu(mContext)) {
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					WindowsUtil.getwindowsHight(mContext)
							- WindowsUtil.getDownMenuHeight(mContext) / 2 + 20);
		} else {
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
		}

		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		popup.setAnimationStyle(R.style.popwin_anim_style);
		mScrollView = (ScrollView) mView.findViewById(R.id.search_scrollView);
		listview = (ListView) mView.findViewById(R.id.history);
		all_delete = (TextView) mView.findViewById(R.id.all_delete);
		all_delete.setTypeface(MyApplication.GetTypeFace());
		all_delete.setOnClickListener(this);
		LinearLayout mTitleLayout = (LinearLayout) mView
				.findViewById(R.id.title);
		LinearLayout mSaveLayout = (LinearLayout) mView
				.findViewById(R.id.window_save);
		history_tv = (TextView) mView.findViewById(R.id.history_tv);
		history_tv.setTypeface(MyApplication.GetTypeFace());
		etContent = (EditText) mTitleLayout.findViewById(R.id.search_et);
		etContent.setHint("搜索活动");
		etContent.setTypeface(MyApplication.GetTypeFace());
		etContent.setOnKeyListener(onKey);
		mTab = (RadioGroup) mView.findViewById(R.id.search_tab);
		mTab1 = (RadioButton) mView.findViewById(R.id.search_tab1);
		mTab1.setChecked(true);
		LinearLayout mCrowdLayout = (LinearLayout) mView
				.findViewById(R.id.window_crowd);
		LinearLayout mMoodLayout = (LinearLayout) mView
				.findViewById(R.id.window_mood);
		LinearLayout mTypeLayout = (LinearLayout) mView
				.findViewById(R.id.window_type);
		mTimeLayout = (LinearLayout) mView.findViewById(R.id.window_time);
		mCalender = (LinearLayout) mView.findViewById(R.id.search_calender);
		mLocaitonLayout = (LinearLayout) mView
				.findViewById(R.id.window_location);
		mClassicationLayout = (LinearLayout) mView
				.findViewById(R.id.window_classification);
		mHotWordLayout = (LinearLayout) mView.findViewById(R.id.window_hotword);
		mCrowdGrid = (GridView) mCrowdLayout.findViewById(R.id.grid);
		mMoodGrid = (GridView) mMoodLayout.findViewById(R.id.grid);
		mTypeGrid = (GridView) mTypeLayout.findViewById(R.id.grid);
		mTimeGrid = (GridView) mTimeLayout.findViewById(R.id.grid);
		mLocationGrid = (GridView) mLocaitonLayout.findViewById(R.id.grid);
		mClassicationGrid = (GridView) mClassicationLayout
				.findViewById(R.id.grid);
		mHotWordGrid = (GridView) mHotWordLayout.findViewById(R.id.grid);
		hide = (TextView) mLocaitonLayout.findViewById(R.id.hide);
		hide.setTypeface(MyApplication.GetTypeFace());
		hide.setOnClickListener(this);

		hide_classication = (TextView) mClassicationLayout
				.findViewById(R.id.hide);
		hide_classication.setTypeface(MyApplication.GetTypeFace());
		hide_classication.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hide_bool_classication) {
					mClassicationGrid.setAdapter(mTypeTwoAdapter);
					hide_bool_classication = false;
					hide_classication.setText("展开");
				} else {
					mClassicationGrid.setAdapter(mTypeAdapter);
					hide_bool_classication = true;
					hide_classication.setText("收起");
				}
			}
		});
		hide_hotword = (TextView) mHotWordLayout.findViewById(R.id.hide);
		hide_hotword.setTypeface(MyApplication.GetTypeFace());
		hide_hotword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hide_bool_hot_word) {
					mHotWordGrid.setAdapter(mCrowdTwoAdapter);
					hide_bool_hot_word = false;
					hide_hotword.setText("展开");
				} else {
					mHotWordGrid.setAdapter(mCrowdAdapter);
					hide_bool_hot_word = true;
					hide_hotword.setText("收起");
				}
			}
		});
		TextView mCrowdText = (TextView) mCrowdLayout
				.findViewById(R.id.grid_name);
		TextView mMoodText = (TextView) mMoodLayout
				.findViewById(R.id.grid_name);
		TextView mTimeText = (TextView) mTimeLayout
				.findViewById(R.id.grid_name);
		TextView mLocationText = (TextView) mLocaitonLayout
				.findViewById(R.id.grid_name);
		TextView mClassicationText = (TextView) mClassicationLayout
				.findViewById(R.id.grid_name);
		TextView mHotWordText = (TextView) mHotWordLayout
				.findViewById(R.id.grid_name);
		mCrowdText.setText("人群");
		mTimeText.setText("时间");
		mMoodText.setText("心情");
		mLocationText.setText("热门区域");
		mClassicationText.setText("热门分类");
		mHotWordText.setText("热门搜索");
		mLocationText.setTypeface(MyApplication.GetTypeFace());
		mClassicationText.setTypeface(MyApplication.GetTypeFace());
		mHotWordText.setTypeface(MyApplication.GetTypeFace());
		mTimeLayout.setVisibility(View.GONE);
		// 重新设定
		TextView search_reset = (TextView) mTitleLayout
				.findViewById(R.id.search_reset);
		search_reset.setTypeface(MyApplication.GetTypeFace());
		search_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// onReset();
				if (popup != null) {
					popup.dismiss();
					Log.i("ceshi", "取消=== ");
//					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);  
////					imm.showSoftInput(mTop,InputMethodManager.SHOW_FORCED);  
//					imm.hideSoftInputFromWindow(popup.getApplicationWindowToken(), 0);
//					Log.i("ceshi", "取消000=== ");
				}
			}
		});
		mSend = (Button) mSaveLayout.findViewById(R.id.save);
		// 保存选择条件
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onStartActivity();
			}
		});
		// 取消
		mView.findViewById(R.id.search_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (popup != null) {
							popup.dismiss();
						}
					}
				});
	}

	/**
	 * 选择时间
	 */
	private void onSelectTime() {
		timeList = new ArrayList<SearchInfo>();
		if (searchListInfo.getTimeList() != null) {
			timeList.addAll(searchListInfo.getTimeList());
			mTimeAdapter = new WindowsAdapter(mContext, timeList);
			mTimeGrid.setAdapter(mTimeAdapter);
			mTimeGrid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

					for (int i = 0; i < timeList.size(); i++) {
						if (i == arg2) {
							timeList.get(arg2).setSeleced(true);
							if (timeList.size() - 2 == arg2) {// 时间预定
								mCalender.setVisibility(View.VISIBLE);
								onTimeReserve();
								activityInfo.setTimeType("");
							} else {
								mCalender.setVisibility(View.GONE);
								activityInfo.setActivityStartTime("");
								activityInfo.setActivityEndTime("");
								activityInfo.setTimeType(timeList.get(arg2)
										.getTagId());
							}
						} else {
							timeList.get(i).setSeleced(false);
						}
					}
					mTimeAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	/**
	 * 选择地区
	 */
	private String AdressTagId = "";
	/**
	 * 这个是区域 ID
	 */
	private String AreaId = "";
	/**
	 * 这个是activityName
	 */
	private String ActivityName = "";

	
	private void onSelectAdress() {
		addresList = new ArrayList<SearchInfo>();
		addresList.addAll(searchListInfo.getMoodList());
		mLocationAdapter = new WindowsAdapter(mContext, addresList);
		mLocationTwoAdapter = new WindowsTwoAdapter(mContext, addresList);
		mLocationGrid.setAdapter(mLocationTwoAdapter);
		// smLocationGrid.setAdapter(mLocationAdapter);
		// smLocationGrid.setAdapter(mLocationAdapter);
		mLocationGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AreaId = addresList.get(arg2).getTagId();
				isMain = true;
				onStartActivity();
			}
		});

	}

	/**
	 * 获取筛选条件
	 */
	public void getData() {
		MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
		Map<String, String> params = new HashMap<String, String>();
		// 获取活动筛选条件
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.SearchUrl.HTTP_APPHOTACTIVITY, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "--获取数据-" + resultStr);

						// TODO Auto-generated method stub
						// List<SearchInfo> timeList = new
						// ArrayList<SearchInfo>();
						// timeList.add(new SearchInfo("1", "今天"));
						// timeList.add(new SearchInfo("2", "明天"));
						// timeList.add(new SearchInfo("3", "本周末"));
						// timeList.add(new SearchInfo("4", "本周"));
						// timeList.add(new SearchInfo("", "自定义"));
						// timeList.add(new SearchInfo("", "全部"));
						SearchListInfo info = new SearchListInfo();
						info = JsonUtil.getSearchListInfo(resultStr);
						// info.setTimeList(timeList);
						MyApplication.getInstance().setSearchListInfo(info);

					}
				});

		// 获取场馆筛选条件
		MyHttpRequest.onHttpPostParams(HttpUrlList.Window.VENUE_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						WindowInfo info = new WindowInfo();
						info = JsonUtil.getWindowList(resultStr, 1);
						MyApplication.getInstance().setVenueWindowList(info);
					}
				});

	}

	/**
	 * 选择类型 分类
	 */
	private void onSelectType() {
		typeList = new ArrayList<SearchInfo>();
		typeList.addAll(searchListInfo.getTypeList());
		mTypeAdapter = new WindowsAdapter(mContext, typeList);
		mTypeTwoAdapter = new WindowsTwoAdapter(mContext, typeList);
		mClassicationGrid.setAdapter(mTypeTwoAdapter);
		mClassicationGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// for (int i = 0; i < typeList.size(); i++) {
				AdressTagId = typeList.get(arg2).getTagId();
				isMain = true;
				// isMain = true;
				// activityInfo.setActivityType("3");
				onStartActivity();
				// 设置EventWindows 呈现的布局
				// activityInfo.getActivityType();

				// if (i == arg2) {
				// typeList.get(arg2).setSeleced(true);
				//
				// } else {
				// typeList.get(i).setSeleced(false);
				// }
				// }
				mTypeAdapter.notifyDataSetChanged();
			}
		});

	}

	/**
	 * 选择tab
	 */
	public void onSelectTab() {
		mTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.search_tab1:
					activityInfo.setTabType("1");
					mLocaitonLayout.setVisibility(View.GONE);
					mClassicationLayout.setVisibility(View.GONE);
					mHotWordLayout.setVisibility(View.GONE);

					break;
				case R.id.search_tab2:
					activityInfo.setTabType("2");
					mLocaitonLayout.setVisibility(View.VISIBLE);
					mClassicationLayout.setVisibility(View.VISIBLE);
					mHotWordLayout.setVisibility(View.VISIBLE);

					break;
				case R.id.search_tab3:
					activityInfo.setTabType("3");
					mLocaitonLayout.setVisibility(View.VISIBLE);
					mClassicationLayout.setVisibility(View.VISIBLE);
					mHotWordLayout.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 选择心情 3.5是地区的选择
	 */
	private void onSelectMood() {
		moodList = new ArrayList<SearchInfo>();
		moodList.addAll(searchListInfo.getMoodList());
		mMoodAdapter = new WindowsAdapter(mContext, moodList);
		mMoodGrid.setAdapter(mMoodAdapter);
		mMoodGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO Auto-generated method stub
				// for (int i = 0; i < moodList.size(); i++) {
				// if (i == arg2) {
				// moodList.get(arg2).setSeleced(true);
				// activityInfo.setActivityMood(moodList.get(arg2)
				// .getTagId());
				// } else {
				// moodList.get(i).setSeleced(false);
				// }
				// }
				// onStartActivity(moodList.get(arg2).getTagName());
				// mMoodAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 选择人群 热门话题
	 */
	private void onSelectPersonal() {
		personalList = new ArrayList<SearchInfo>();
		personalList.addAll(searchListInfo.getPersonalList());
		mCrowdAdapter = new WindowsAdapter(mContext, personalList);
		mCrowdTwoAdapter = new WindowsTwoAdapter(mContext, personalList);
		mHotWordGrid.setAdapter(mCrowdTwoAdapter);
		mHotWordGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// // TODO Auto-generated method stub
				Log.i("ceshi", "看看数据===  "
						+ personalList.get(arg2).getTagName() + "  per ="
						+ personalList.get(arg2).toString());
				ActivityName = personalList.get(arg2).getTagName();

				isMain = true;
				onStartActivity();
				// for (int i = 0; i < personalList.size(); i++) {
				// if (i == arg2) {
				// personalList.get(arg2).setSeleced(true);
				// activityInfo.setActivityCrowd(personalList.get(arg2)
				// .getTagId());
				// } else {
				// personalList.get(i).setSeleced(false);
				// }
				// }
				// onStartActivity(personalList.get(arg2).getTagName());
				// mCrowdAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 获取标签数据
	 */
	private void getData(final RelativeLayout mTop) {
		// 没有获取到标签图
		MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
		Map<String, String> params = new HashMap<String, String>();
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.SearchUrl.HTTP_APPHOTACTIVITY, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						SearchListInfo info = new SearchListInfo();
						info = JsonUtil.getSearchListInfo(resultStr);
						MyApplication.getInstance().setSearchListInfo(info);
						searchListInfo = MyApplication.getInstance()
								.getSearchListInfo();

						// showSearch(mTop, isMain);
					}
				});
	}

	/**
	 * 监听回车事件
	 */
	OnKeyListener onKey = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
					if (TextUtils.isEmpty(etContent.getText())) {
						ToastUtil.showToast("请输入关键字");
					} else {
						onStartActivity();
					}

				}
				return true;
			}
			return false;
		}
	};
	private List<SearchInfo> addresList;
	private RadioGroup mTab;
	private static EventWindows searchWindows;
	private GridView mCrowdGrid;
	private GridView mMoodGrid;
	private GridView mTypeGrid;
	/**
	 * 这个是区域的布局
	 */
	private GridView mLocationGrid, mClassicationGrid, mHotWordGrid;
	private WindowsAdapter mMoodAdapter;
	private WindowsAdapter mCrowdAdapter;
	private WindowsAdapter mTypeAdapter;
	private WindowsAdapter mLocationAdapter;
	private WindowsTwoAdapter mMoodTwoAdapter;
	private WindowsTwoAdapter mCrowdTwoAdapter;
	private WindowsTwoAdapter mTypeTwoAdapter;
	private WindowsTwoAdapter mLocationTwoAdapter;
	private RadioButton mTab1;
	private LinearLayout mLocaitonLayout;
	private LinearLayout mClassicationLayout;
	private LinearLayout mHotWordLayout;
	private LinearLayout mTimeLayout;

	/**
	 * 根据条件筛选
	 */
	private void onStartActivity() {
		// if (name.equals("") || name == null) {
		if (!TextUtils.isEmpty(etContent.getText())) {
			if (etContent.getText().toString().equals("%")) {
				ToastUtil.showToast("不能输入非法字符");
				return;
			}
			activityInfo.setActivityKeyWord(etContent.getText().toString());
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("tv", etContent.getText().toString());
			if (list_event != null && list_event.size() != 0) {
				list = list_event;
			}
			if (list.size() >= 5) {
				list.remove(0);
				list.add(map);
			} else {
				list.add(map);
			}
			EventAddressInfo event_info = new EventAddressInfo(list);
			saveEventAddress(event_info);
			listview.setVisibility(View.VISIBLE);
			history_tv.setVisibility(View.VISIBLE);
			all_delete.setVisibility(View.VISIBLE);
			adapter.notifyDataSetChanged();
		}
		if (isMain) {
			Intent intent = new Intent(mContext, EventListActivity.class);
			intent.putExtra("tagId", AdressTagId);
			intent.putExtra("searchType", "eventwindows");
			intent.putExtra("activityInfo", activityInfo);
			intent.putExtra("AreaId", AreaId);
			intent.putExtra("ActivityName", ActivityName);
			mContext.startActivity(intent);
			ActivityName = "";
			AreaId = "";
			AdressTagId = "";
		} else {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = activityInfo;
			MyApplication.getInstance().getmLableChoose().sendMessage(msg);
		}
		// } else {
		// activityInfo.setActivityKeyWord(name);

		// }
		if (popup != null) {
			popup.dismiss();
		}
	}

	/**
	 * 重设
	 */
	private void onReset() {
		if (searchListInfo != null) {
			for (int i = 0; i < searchListInfo.getMoodList().size(); i++) {
				if (searchListInfo.getMoodList().get(i).isSeleced()) {
					searchListInfo.getMoodList().get(i).setSeleced(false);
					break;
				}
			}
			for (int i = 0; i < searchListInfo.getPersonalList().size(); i++) {
				if (searchListInfo.getPersonalList().get(i).isSeleced()) {
					searchListInfo.getPersonalList().get(i).setSeleced(false);
					break;
				}
			}
			for (int i = 0; i < searchListInfo.getTypeList().size(); i++) {
				if (searchListInfo.getTypeList().get(i).isSeleced()) {
					searchListInfo.getTypeList().get(i).setSeleced(false);
					break;
				}
			}
			for (int i = 0; i < searchListInfo.getAddresList().size(); i++) {
				if (searchListInfo.getAddresList().get(i).isSeleced()) {
					searchListInfo.getAddresList().get(i).setSeleced(false);
					break;
				}
			}
			// for (int i = 0; i < timeList.size(); i++) {
			// if (timeList.get(i).isSeleced()) {
			// timeList.get(i).setSeleced(false);
			// break;
			// }
			// }
			if (mCalender.getVisibility() == View.VISIBLE)
				mCalender.setVisibility(View.GONE);
			activityInfo.clear();
			// mTab1.setChecked(true);
			// addresList.get(0).setSeleced(true);
			mCrowdAdapter.notifyDataSetChanged();
			mMoodAdapter.notifyDataSetChanged();
			mTypeAdapter.notifyDataSetChanged();
			mLocationAdapter.notifyDataSetChanged();
			if (mTimeAdapter != null) {
				mTimeAdapter.notifyDataSetChanged();
			}
			etContent.setText("");
		}
	}

	/**
	 * 时间预定
	 */
	private void onTimeReserve() {
		activityInfo.setTimeType("");
		mCalender.setVisibility(View.VISIBLE);
		onCalendar();
	}

	/**
	 * 日历操作
	 */
	private void onCalendar() {
		MyApplication.isReserverTimeDate = false;
		CalendarActivity.clickDateStrng = null;
		mGridView = (GridView) mCalender.findViewById(R.id.calender_gridview);
		mTopTime = (TextView) mCalender.findViewById(R.id.calendar_time);
		mStartTime = (TextView) mCalender.findViewById(R.id.search_start_time);
		mEndTime = (TextView) mCalender.findViewById(R.id.search_end_time);
		selectDay = new ArrayList<DayClass>();
		gestureDetector = new GestureDetector(mContext, this);
		calV = new CalendarAdapter(mContext, mContext.getResources(),
				jumpMonth, jumpYear, year_c, month_c, day_c);
		addGridView();
		mGridView.setAdapter(calV);
		addTextToTopTextView(mTopTime);
	}

	private void addGridView() {
		mGridView = (GridView) mCalender.findViewById(R.id.calender_gridview);
		mGridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mScrollView.requestDisallowInterceptTouchEvent(true);
				return gestureDetector.onTouchEvent(event);
			}
		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView中的每一个item的点击事件

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int showMonth = Integer.parseInt(calV.getShowMonth());
				if (position < calV.getCurrentFlag()) {
					ToastUtil.showToast("不能选择过去的日期");
					return;
				}
				DayClass mDayClass = new DayClass();
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position + 7
						&& position <= endPosition - 7) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					String scheduleLunarDay = calV.getDateByClickItem(position)
							.split("\\.")[1];
					// //这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					Boolean isADD = true;
					List<DayClass> ddc = new ArrayList<DayClass>();
					for (DayClass dc : selectDay) {
						if (dc.id == position) {
							arg0.getChildAt(dc.id).setBackgroundColor(
									mContext.getResources().getColor(
											R.color.activity_bg_color));
							ddc.add(dc);
							isADD = false;
						}
					}
					if (isADD) {
						int month = Integer.parseInt(scheduleMonth);
						if (month < 10) {
							scheduleMonth = "0" + scheduleMonth;
						}
						int day = Integer.parseInt(scheduleDay);
						if (day < 10) {
							scheduleDay = "0" + scheduleDay;
						}
						String daystr = scheduleYear + "-" + scheduleMonth
								+ "-" + scheduleDay;
						mDayClass.id = position;
						mDayClass.day = Integer.parseInt(scheduleDay);
						mDayClass.month = Integer.parseInt(scheduleMonth);
						mDayClass.scheduleDay = daystr;
						selectDay.add(mDayClass);
						arg1.setBackgroundColor(mCalender.getResources()
								.getColor(R.color.orange_color));
						if (selectDay.size() > 2) {
							arg0.getChildAt(selectDay.get(0).id)
									.setBackgroundColor(
											mContext.getResources().getColor(
													R.color.activity_bg_color));
							selectDay.remove(0);
						}
						mStartTime.setText(selectDay.get(0).scheduleDay);
						if (selectDay.size() > 1) {
							if (selectDay.get(0).month != selectDay.get(1).month) {
								if (selectDay.get(0).month > selectDay.get(1).month) {
									mEndTime.setText(selectDay.get(0).scheduleDay);
									mStartTime.setText(selectDay.get(1).scheduleDay);
								} else if (selectDay.get(0).day > selectDay
										.get(1).day) {
									mEndTime.setText(selectDay.get(1).scheduleDay);
									mStartTime.setText(selectDay.get(0).scheduleDay);
								}
							} else {
								if (selectDay.get(0).day < selectDay.get(1).day) {
									mEndTime.setText(selectDay.get(1).scheduleDay);
								} else {
									mEndTime.setText(selectDay.get(0).scheduleDay);
									mStartTime.setText(selectDay.get(1).scheduleDay);
								}
							}
						}
						activityInfo.setActivityStartTime(mStartTime.getText()
								.toString());
						activityInfo.setActivityEndTime(mEndTime.getText()
								.toString());
					} else {
						selectDay.removeAll(ddc);
					}
				}
			}

		});

	}

	/**
	 * 添加头部的年份 闰哪月等信息
	 * 
	 * @param view
	 */
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年")
				.append(calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 30) {
			// 像左滑动
			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月

			calV = new CalendarAdapter(mContext, mContext.getResources(),
					jumpMonth, jumpYear, year_c, month_c, day_c);
			mGridView.setAdapter(calV);
			addTextToTopTextView(mTopTime);
			gvFlag++;

			return true;
		} else if (e1.getX() - e2.getX() < -30) {
			// 向右滑动
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			calV = new CalendarAdapter(mContext, mContext.getResources(),
					jumpMonth, jumpYear, year_c, month_c, day_c);
			mGridView.setAdapter(calV);
			gvFlag++;
			addTextToTopTextView(mTopTime);

			return true;
		}
		return false;

	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.hide:
			if (hide_bool) {
				mLocationGrid.setAdapter(mLocationTwoAdapter);
				hide_bool = false;
				hide.setText("展开");
			} else {
				mLocationGrid.setAdapter(mLocationAdapter);
				hide_bool = true;
				hide.setText("收起");
			}

			break;
		case R.id.all_delete:
			list.clear();
			adapter.notifyDataSetChanged();
			SharedPreManager.clearEventAddressInfo();
			listview.setVisibility(View.GONE);
			history_tv.setVisibility(View.GONE);
			all_delete.setVisibility(View.GONE);
			break;

		default:
			break;
		}

	}

	/**
	 * 保存搜索信息
	 * 
	 * @param userinfo
	 */
	private void saveEventAddress(EventAddressInfo event_info) {
		if (event_info == null) {
			return;
		}
		String str = JsonHelperUtil.toJSON(event_info).toString();
		SharedPreManager.saveEventAddressInfor(str);
	}
}
