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
import com.sun3d.culturalShanghai.adapter.SpecialAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.NewInfo;

public class MoreSpecialActivity extends Activity {
	private Context mContext;
	private ListView mListView;
	private List<NewInfo> list;
	private SpecialAdapter mAdapter;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private String activityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivitys(this);
		setContentView(R.layout.activity_more_special);
		mContext = this;
		initView();
		initData();
		getData();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listView);
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		TextView mTitle = (TextView) findViewById(R.id.title_content);
		findViewById(R.id.title_right).setVisibility(View.GONE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText("实况直击");
		findViewById(R.id.title_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		activityId = getIntent().getExtras().getString("activityId");
	}

	private void initData() {
		list = new ArrayList<NewInfo>();
		mAdapter = new SpecialAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, NewDetailsActivity.class);
                // intent.putExtra("newId", list.get(arg2).getNewId());
                NewInfo newInfo = list.get(arg2);
                intent.putExtra("NewInfo", newInfo);
				mContext.startActivity(intent);
			}
		});
	}

	private void setData() {
		mAdapter.setList(list);
		mAdapter.notifyDataSetChanged();
	}

	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("referId", activityId);
		// params.put("referId", "692e4fd1468c4a7e804218ff208d88b3");
		params.put("pageIndex", "0");
		params.put("pageNum", "20");
		mLoadingHandler.startLoading();
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYSHOWINDEX, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				mLoadingHandler.stopLoading();
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					list = JsonUtil.getNewList(resultStr);
					setData();
				} else {
					ToastUtil.showToast(resultStr);
				}
			}
		});
	}

}
