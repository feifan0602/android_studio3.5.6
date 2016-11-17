package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.ActivityRoomListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

/**
 * 相关活动室
 * 
 * @author wenff
 * 
 */
public class ActivityRoomActivity extends Activity implements OnClickListener {
	private String TAG = "ActivityRoomActivity";
	private PullToRefreshListView mListView;
	private Context mContext;
	private List<ActivityRoomInfo> list;
	private String venueId;
	private Map<String, String> mParams;
	private ActivityRoomListAdapter mActivityRoomListAdapter;
	private LoadingHandler mLoadingHandler;
	private String venueName;
	private TextView title;
	private final String mPageName = "ActivityRoomActivity";
	private int page = 0;
	private Boolean isResh = true;
	private RelativeLayout mTitle;
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoadingTextShowPopWindow.dismissPop();
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exhi_bition);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		init();
	}

	private void init() {
		setTitle();
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		venueId = this.getIntent().getStringExtra("venueId");
		venueName = this.getIntent().getStringExtra("venueName");

		list = new ArrayList<ActivityRoomInfo>();
		mListView = (PullToRefreshListView) findViewById(R.id.exhibition_listview);
		mActivityRoomListAdapter = new ActivityRoomListAdapter(mContext, list);
		mListView.setAdapter(mActivityRoomListAdapter);
		// addData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						ActivityRoomDateilsActivity.class);
				intent.putExtra("Id", list.get(arg2).getRoomId());
				startActivityForResult(intent, AppConfigUtil.LOADING_LOGIN_BACK);
			}
		});
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
		onResh();
	}

	private void onResh() {
		isResh = true;
		getData(page);
	}

	private void onAddmoreData() {
		isResh = false;
		page = HttpUrlList.HTTP_NUM + page;
		getData(page);
	}

	private void getData(int page) {
		mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, venueId);
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.ActivityRoomUrl.ROOM_LIST_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mListView.onRefreshComplete();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<ActivityRoomInfo> nowlist = JsonUtil
									.getRoomList(resultStr);
							if (nowlist.size() > 0) {
								if (isResh) {
									isResh = false;
									list.clear();
								}
								list.addAll(nowlist);
								mActivityRoomListAdapter.setData(list);
								mActivityRoomListAdapter.notifyDataSetChanged();
								LoadingTextShowPopWindow.showLoadingText(
										mContext, mTitle, "");
							} else {
								LoadingTextShowPopWindow.showLoadingText(
										mContext, mTitle, "数据加载完成");
							}
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}

					}
				});
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText("相关活动室");// titlestr
		title.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_room, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.LOADING_LOGIN_BACK:
			finish();
			break;

		default:
			break;
		}
	}
}
