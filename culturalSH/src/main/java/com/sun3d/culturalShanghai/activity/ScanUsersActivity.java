package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.PersionListViewAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

public class ScanUsersActivity extends Activity implements OnClickListener {
	private Context mContext;
	private PullToRefreshListView persionS_ListView;
	private PersionListViewAdapter mPersionListViewAdapter;
	private List<UserPersionSInfo> PersionList = new ArrayList<UserPersionSInfo>();
	private String activityId;
	private int page = 0;

	private final String mPageName = "ScanUsersActivity";
	private Boolean isresh = true;
	private RelativeLayout mTitle;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_users);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		init();
	}

	private void init() {
		activityId = this.getIntent().getStringExtra("activityId");
		setTitle();
		persionS_ListView = (PullToRefreshListView) findViewById(R.id.person_listview);
		mPersionListViewAdapter = new PersionListViewAdapter(mContext, PersionList);
		persionS_ListView.setAdapter(mPersionListViewAdapter);
		getJoinUsers(0);
		persionS_ListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				isresh = true;
				getJoinUsers(0);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				isresh = false;
				page = HttpUrlList.HTTP_NUM + page;
				getJoinUsers(page);
			}

		});
	}

	private void setData() {
		for (int i = 0; i < 10; i++) {
			UserPersionSInfo ups = new UserPersionSInfo();
			PersionList.add(ups);
		}
		mPersionListViewAdapter.setList(PersionList);
	}

	/**
	 * 获取报名用户
	 */
	private void getJoinUsers(int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", activityId);
		params.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		params.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTTO_USERS_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						persionS_ListView.onRefreshComplete();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							List<UserPersionSInfo> PersionListNow = JsonUtil
									.getJoinUserList(resultStr);
							if (PersionListNow != null && PersionListNow.size() > 0) {
								if (isresh) {
									PersionList.clear();
									isresh = false;
								}
								PersionList.addAll(PersionListNow);
								mPersionListViewAdapter.setList(PersionList);
								persionS_ListView.onRefreshComplete();
								LoadingTextShowPopWindow.showLoadingText(mContext, mTitle, "加载完成");
							} else {
								LoadingTextShowPopWindow.showLoadingText(mContext, mTitle, "加载完成");
							}
						} else {
							ToastUtil.showToast(resultStr);
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
		ImageButton mTitleRight = (ImageButton) mTitle.findViewById(R.id.title_right);
		mTitleRight.setVisibility(View.GONE);
		TextView mTitle_tv = (TextView) mTitle.findViewById(R.id.title_content);
		mTitle_tv.setText("查看用户");
		mTitle_tv.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan_users, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_left:
			finish();
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
