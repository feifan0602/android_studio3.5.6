package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.VenueListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ScreenInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.sun3d.culturalShanghai.windows.VenueWindows;
import com.umeng.analytics.MobclickAgent;

/**
 * 场馆的搜索的listview
 * 
 * @author wenff
 * 
 */
public class VenueListActivity extends Activity implements OnClickListener {
	private Context mContext;
	private RelativeLayout mTitleLayout;
	private TextView mTitle;
	private PullToRefreshListView mListView;
	private static List<VenueDetailInfor> list;
	private VenueListAdapter mAdapter;
	private ScreenInfo screenInfo;
	private String venueType, venueArea, venueName;
	private int mPage = 0;
	private Map<String, String> mParams;
	private Map<String, String> mParams_new;
	private LoadingHandler mLoadingHandler;
	private final String mPageName = "VenueListActivity";
	private Boolean isRefresh = false;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

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
				screenInfo = (ScreenInfo) msg.obj;
				initData();
				getData(mPage);
			}
		}
	};
	private RelativeLayout loadingLayout;

	public static List<VenueDetailInfor> getMainListData() {
		return list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_venue_list);
		MyApplication.getInstance().addActivitys(this);
		MyApplication.getInstance().setVenueListHandler(mHandler);
		mContext = this;
		initView();
		initData();
		getData(mPage);
	}

	private void initView() {
		screenInfo = (ScreenInfo) getIntent().getSerializableExtra(
				AppConfigUtil.INTENT_SCREENINFO);

		venueType = (String) getIntent().getExtras().getString("venueType");
		venueArea = (String) getIntent().getExtras().getString("venueArea");
		venueName = (String) getIntent().getExtras().getString("venueName");

		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mTitleLayout = (RelativeLayout) findViewById(R.id.title);
		mTitleLayout.findViewById(R.id.title_left).setOnClickListener(this);
		mTitleLayout.findViewById(R.id.title_right).setOnClickListener(this);
		mTitle = (TextView) mTitleLayout.findViewById(R.id.title_content);
		mTitle.setTypeface(MyApplication.GetTypeFace());
		// mTitle.setTextColor(mContext.getResources().getColor(R.color.orange_color));
		Drawable drawable = getResources().getDrawable(
				R.drawable.sh_activity_title_pull);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		// mTitle.setCompoundDrawables(null, null, drawable, null);// 画在右边
		// mTitle.setOnClickListener(this);
		mTitle.setVisibility(View.VISIBLE);
		mListView = (PullToRefreshListView) findViewById(R.id.venue_list);
		mLoadingHandler = new LoadingHandler(loadingLayout);

		mListView.setOnScrollListener(new PauseOnScrollListener(MyApplication
				.getInstance().getImageLoader(), true, false));
		mLoadingHandler.startLoading();
		mParams = new HashMap<String, String>();
		mParams_new = new HashMap<String, String>();
		if (venueType != null && venueType != "") {
			mParams_new.put("venueType", venueType);
		}
		if (venueArea != null && venueArea != "") {
			mParams_new.put("venueArea", venueArea);
		}

		if (screenInfo == null) {
			return;
		}
		// 获取输入框的数据

		if (screenInfo.getSerach() == null || screenInfo.getSerach().equals("")) {
			if (venueName != null && venueName != "") {
				mParams_new.put("venueName", venueName);
			}
		} else {
			mParams_new
					.put(HttpUrlList.Venue.VENUENAME, screenInfo.getSerach());
		}
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
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
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams_new.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				onResh();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				onAddmoreData();
			}

		});
	}

	private void onResh() {
		isRefresh = true;
		mPage = 0;
		getData(mPage);
	}

	private void onAddmoreData() {
		mPage = HttpUrlList.HTTP_NUM + mPage;
		getData(mPage);
	}

	private void initData() {
		// 类型，默认距离
		mParams.put(HttpUrlList.Venue.APPTYPE, screenInfo.getTabType());
		// 区域
		mParams.put(HttpUrlList.Venue.AREACODE, screenInfo.getAdress());
		// 人群
		mParams.put(HttpUrlList.Venue.VENUECROWD, screenInfo.getCrowd());
		// 场馆类型
		mParams.put(HttpUrlList.Venue.VENUETYPE, screenInfo.getType());
		// 是否可预订
		mParams.put(HttpUrlList.Venue.VENUEISRESERVE,
				screenInfo.getVenueIsReserve());
		mParams.put(HttpUrlList.Venue.VENUENAME, screenInfo.getSerach());

		mLoadingHandler.startLoading();
		list = new ArrayList<VenueDetailInfor>();
		mAdapter = new VenueListAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 < list.size()) {
					Intent intent = new Intent(mContext,
							VenueDetailActivity.class);
					intent.putExtra("venueId", list.get(arg2).getVenueId());
					startActivity(intent);
				}
			}
		});

	}

	/**
	 * 获取数据
	 * 
	 * @param page
	 */
	private void getData(int page) {
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		mParams_new.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		Log.i("ceshi", "数据===  " + mParams_new);
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.WFF_APPCMSVENUELIST,
				mParams_new, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingHandler.stopLoading();
						mListView.onRefreshComplete();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							Log.d("statusCode", statusCode + "这个是场所搜索条件的数据---"
									+ resultStr);
							List<VenueDetailInfor> mList = new ArrayList<VenueDetailInfor>();
							mList = JsonUtil.getVenueListInfoList(resultStr);
							setTitleText(JsonUtil.count);
							if (mList != null && mList.size() > 0) {
								if (isRefresh) {
									mList.clear();
									isRefresh = false;
								}
								list.addAll(mList);
								mAdapter.setList(list);
								mAdapter.notifyDataSetChanged();
								LoadingTextShowPopWindow.showLoadingText(
										mContext, mTitleLayout, "数据加载成功");
							} else {
								LoadingTextShowPopWindow.showLoadingText(
										mContext, mTitleLayout, "已经全部加载完成");
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 设置标题
	 * 
	 * @param count
	 */
	private void setTitleText(String count) {
		mTitle.setText("为您筛选" + count + "条");
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			if (list != null && list.size() > 0) {
				Intent intent = new Intent(mContext, SearchNearbyActivity.class);
				intent.putExtra("ListType", "2");
				startActivity(intent);
			} else {
				ToastUtil.showToast("场馆列表没有数据");
			}
			break;
		case R.id.title_content:
			VenueWindows.getInstance(mContext).show(mTitleLayout, false,"场馆");
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoadingTextShowPopWindow.dismissPop();
	}
}
