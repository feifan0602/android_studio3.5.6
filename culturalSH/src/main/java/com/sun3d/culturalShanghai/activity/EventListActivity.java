package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.ActivityListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.windows.EventWindows;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

public class EventListActivity extends Activity implements OnClickListener {
	private Context mContext;
	private ListView mListView;
	private static List<EventInfo> list;
	private RelativeLayout titlelayout;
	private int mPage;
	private LinearLayout footViewLayout;
	private TextView mFootText;
	private ActivityConditionInfo activityConditionInfo;
	private static ActivityListAdapter mMainListAdapter;
	private LoadingHandler mLoadingHandler;
	private Map<String, String> mParams;
	private Map<String, String> mParams_new;
	private TextView title;
	private String url;
	private final String mPageName = "EventListActivity";

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				mPage = 0;
				list.clear();
				activityConditionInfo = (ActivityConditionInfo) msg.obj;
				Log.i("ceshi", "handler==  " + activityConditionInfo.toString());
				initData();
				setParam(2);
				getData(mPage);
			}
		}
	};
	private TextView mtitleWeather;

	public static List<EventInfo> getListData() {
		return list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_list);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		MyApplication.getInstance().setmLableChoose(mHandler);
		initView();

	}

	private void initView() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		list = new ArrayList<EventInfo>();
		setTitle();
		mListView = (ListView) findViewById(R.id.lable_listview);
		footViewLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.list_foot, null);
		mFootText = (TextView) footViewLayout.findViewById(R.id.list_foot_text);
		mListView.addFooterView(footViewLayout);
		mParams = new HashMap<String, String>();
		mParams_new = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mParams.put("appType", "1");
		/**
		 * 这里传入地理位置
		 */
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")) {
			mParams_new.put(HttpUrlList.HTTP_LAT,
					MyApplication.Location_latitude);
			mParams_new.put(HttpUrlList.HTTP_LON,
					MyApplication.Location_longitude);
		} else {
			mParams_new.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams_new.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}
		// 这里是传 pageNum
		mParams_new.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_LAT,
				AppConfigUtil.LocalLocation.Location_latitude + "");
		mParams.put(HttpUrlList.HTTP_LON,
				AppConfigUtil.LocalLocation.Location_longitude + "");

		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mListView.setOnScrollListener(new PauseOnScrollListener(MyApplication
				.getInstance().getImageLoader(), true, false));
		String searchType = getIntent().getExtras().getString("searchType");
		// if (searchType.equals("weather")) {
		// // setWeatherParam();
		// } else
		// if (searchType.equals("indexfragment")) {
		// setSearchParam(1);
		// } else {
		// setSearchParam(2);
		// }
		setSearchParam(1);
		initData();
		getData(mPage);
	}

	/**
	 * 设置天气条件
	 */
	// private void setWeatherParam() {
	// String activityTime = getIntent().getExtras().getString("activityTime");
	// mtitleWeather.setVisibility(View.VISIBLE);
	// mtitleWeather.setText(activityTime);
	// title.setVisibility(View.GONE);
	// // 时间
	// mParams.put("appType", "4");
	// mParams.put("activityTime", activityTime);
	// url = HttpUrlList.MyEvent.ACTIVITYLISTURL;
	// }

	/**
	 * 设置条件筛选 条件
	 */
	private void setSearchParam(int type) {
		mtitleWeather.setVisibility(View.GONE);
		title.setVisibility(View.VISIBLE);
		activityConditionInfo = (ActivityConditionInfo) getIntent()
				.getSerializableExtra("activityInfo");
		setParam(type);
		// if (activityConditionInfo == null) {
		// return;
		// }

	}

	private void setParam(int type) {
		String tagId = getIntent().getExtras().getString("tagId");
		String AreaId = getIntent().getExtras().getString("AreaId");
		String ActivityName = getIntent().getExtras().getString("ActivityName");
		// if (type == 1) {// 主页主题类型
		// // 主题
		// mParams.put(HttpUrlList.SearchUrl.HTTP_ACTIVITY_THEME,
		// activityConditionInfo.getActivityType());
		// } else {
		// 开始时间
		mParams.put(HttpUrlList.SearchUrl.HTTP_SEARCH_START_TIME,
				activityConditionInfo.getActivityStartTime());
		// 结束时间
		mParams.put(HttpUrlList.SearchUrl.HTTP_SEARCH_END_TIME,
				activityConditionInfo.getActivityEndTime());
		// 活动类型
		mParams.put(HttpUrlList.SearchUrl.HTTP_SEARCH_TYPE,
				activityConditionInfo.getActivityType());

		if (tagId == null) {
			mParams.put(HttpUrlList.SearchUrl.AREACODE, "");
		} else {
			mParams.put(HttpUrlList.SearchUrl.AREACODE, tagId);
		}
		// 主题
		mParams.put(HttpUrlList.SearchUrl.HTTP_ACTIVITY_THEME,
				activityConditionInfo.getActivityTheme());
		// 今，明，周末
		mParams.put(HttpUrlList.SearchUrl.HTTP_TIMETYPE,
				activityConditionInfo.getTimeType());
		// 关键字
		mParams.put(HttpUrlList.SearchUrl.HTTP_SEARCH_KEYWORD,
				activityConditionInfo.getActivityKeyWord());

		// }
		// 活动区域
		if (activityConditionInfo.getActivityKeyWord() == null
				|| activityConditionInfo.getActivityKeyWord().equals("")) {
			if (ActivityName == null) {
				mParams_new.put(HttpUrlList.SearchUrl.HTTP_SEARCH_KEYWORD, "");
			} else {
				mParams_new.put(HttpUrlList.SearchUrl.HTTP_SEARCH_KEYWORD,
						ActivityName);
			}
		} else {
			mParams_new.put(HttpUrlList.SearchUrl.HTTP_SEARCH_KEYWORD,
					activityConditionInfo.getActivityKeyWord());
		}

		// 这里是传 activityType
		mParams_new.put(HttpUrlList.SearchUrl.HTTP_SEARCH_TYPE, tagId);
		Log.i("https3", tagId);

		// 这里是传 ActivityArea
		if (AreaId == null) {
			mParams_new.put(HttpUrlList.SearchUrl.AREACODE, "");
		} else {
			mParams_new.put(HttpUrlList.SearchUrl.AREACODE, AreaId);
		}
		/**
		 * 这个是键盘输入的内容
		 */
		// if (activityConditionInfo.getActivityKeyWord() != null) {
		// mParams_new.put(HttpUrlList.SearchUrl.HTTP_SEARCH_KEYWORD,
		// activityConditionInfo.getActivityKeyWord());
		// }
		url = HttpUrlList.SearchUrl.WFF_APPCMSACTIVITYLISTBYCONDITION;
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		mMainListAdapter = new ActivityListAdapter(mContext, list, true);
		mListView.setAdapter(mMainListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				NowIndex = arg2;
				EventInfo eventInfo = list.get(arg2);
				Intent intent = new Intent(mContext, EventDetailsActivity.class);
				intent.putExtra("eventId", eventInfo.getEventId());
				startActivity(intent);
			}
		});
		footViewLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mFootText.setText(mContext.getResources().getString(
						R.string.list_foot_load));
				mPage = mPage + HttpUrlList.HTTP_NUM;
				getData(mPage);
			}
		});
	}

	/**
	 * 获取数据 这个是3.5根据条件搜索活动
	 */
	private void getData(int page) {
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		// 这里是传 pageIndex
		mParams_new.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		MyHttpRequest.onHttpPostParams(url, mParams_new,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingHandler.stopLoading();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							Log.d("statusCode", statusCode + "--根据条件获取数据-"
									+ resultStr);
							List<EventInfo> mAddList = new ArrayList<EventInfo>();
							try {
								mAddList = JsonUtil.getEventList(resultStr);
								setCountText(JsonUtil.count);
								if (mAddList.size() > 0) {
									list.addAll(mAddList);
									mMainListAdapter.setList(list);
									mMainListAdapter.notifyDataSetChanged();
									mFootText
											.setText(mContext
													.getResources()
													.getString(
															R.string.list_foot_more));
								} else {
									mFootText.setText(mContext.getResources()
											.getString(R.string.list_foot));
								}
								if (list.size() < HttpUrlList.HTTP_NUM) {
									mListView.removeFooterView(footViewLayout);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 添加数据
	 */
	private void addData() {

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	/**
	 * 设置筛选出来的条数
	 */
	private void setCountText(String count) {
		// mLableMore.setText("为您筛选" + count + "条");
		title.setTypeface(MyApplication.GetTypeFace());
		title.setText("为您筛选" + count + "条");
	}

	private static int NowIndex = -1;

	public static void setListCollction(int type) {
		if (NowIndex == -1) {
			return;
		}
		if (list == null || list.size() <= 0) {
			return;
		}
		if (mMainListAdapter == null) {
			return;
		}
		if (NowIndex >= 0 && NowIndex < list.size()) {
			if (type == 1) {
				list.get(NowIndex).setEventIsCollect("1");
			} else {
				list.get(NowIndex).setEventIsCollect("0");
			}
			mMainListAdapter.setList(list);
			mMainListAdapter.notifyDataSetChanged();
		}

	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		titlelayout = (RelativeLayout) findViewById(R.id.lable_title);
		title = (TextView) titlelayout.findViewById(R.id.title_content);
		mtitleWeather = (TextView) titlelayout
				.findViewById(R.id.title_content_weather);
		title.setVisibility(View.VISIBLE);
		// title.setText(getResources().getString(R.string.more_choose));
		// title.setTextColor(getResources().getColor(R.color.orange_color));
		Drawable drawable = getResources().getDrawable(
				R.drawable.sh_activity_title_pull);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界

		// title.setCompoundDrawables(null, null, drawable, null);// 画在右边
		// title.setOnClickListener(this);
		ImageButton mBack = (ImageButton) titlelayout
				.findViewById(R.id.title_left);
		mBack.setOnClickListener(this);
		titlelayout.findViewById(R.id.title_right).setOnClickListener(this);
		setCountText("0");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lable_choose, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_content:
			EventWindows.getInstance(mContext).showSearchTest(titlelayout,
					false);
			break;
		case R.id.title_right:
			if (list != null && list.size() > 0) {
				intent = new Intent(mContext, ActivityMap.class);
				intent.putExtra("ListType", "1");
				startActivity(intent);
			} else {
				ToastUtil.showToast("活动列表没有数据");
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mParams_new = null;
		LoadingTextShowPopWindow.dismissPop();
	}
}
