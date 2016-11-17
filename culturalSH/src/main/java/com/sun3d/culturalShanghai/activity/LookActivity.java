package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.LookAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.LookInfo;
import com.umeng.analytics.MobclickAgent;

public class LookActivity extends Activity implements RefreshListenter {
	private Context mContext;
	private ListView mListView;
	private List<LookInfo> mList;
	private LookAdapter mAdapter;
	private LoadingHandler mLoadingHandler;
	private final String mPageName = "LoadActivity";

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
	}

	private void initView() {
		RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.title);
		mLayout.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView mTitle = (TextView) mLayout.findViewById(R.id.title_content);
		mTitle.setText("随便看看");
		mTitle.setVisibility(View.VISIBLE);
		mLayout.findViewById(R.id.title_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mListView = (ListView) findViewById(R.id.look_list);
		RelativeLayout mLoading = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(mLoading);
		mLoadingHandler.setOnRefreshListenter(this);

	}

	private void initData() {
		mList = new ArrayList<LookInfo>();
		mAdapter = new LookAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		getData();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				ActivityConditionInfo activityInfo = new ActivityConditionInfo();
				String activityTheme = mList.get(arg2).getTagId();
				activityInfo.setActivityTheme(activityTheme);
				Intent intent = new Intent(LookActivity.this, EventListActivity.class);
				intent.putExtra("activityInfo", activityInfo);
				intent.putExtra("searchType", "eventwindows");
				startActivity(intent);
			}
		});
	}

	private void setData() {
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		mLoadingHandler.startLoading();
		Map<String, String> params = new HashMap<String, String>();
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					if (JsonUtil.status == 0) {
						mList.addAll(JsonUtil.getLookList(resultStr));
						setData();
					} else {
						ToastUtil.showToast(resultStr);
					}
				} else {
					ToastUtil.showToast(resultStr);
				}
				mLoadingHandler.stopLoading();
			}
		});
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		getData();
	}
}
