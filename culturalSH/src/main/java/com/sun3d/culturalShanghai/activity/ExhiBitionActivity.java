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

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.VenueProfileItemAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.umeng.analytics.MobclickAgent;

public class ExhiBitionActivity extends Activity implements OnClickListener {
	private String TAG = "ExhiBitionActivity";
	private ListView mListView;
	private String titlestr = "上海当代艺术馆";
	private Context mContext;
	private List<EventInfo> list;
	private VenueProfileItemAdapter mVenueProfileItemAdapter;
	private String venueId = "";
	private LoadingHandler mLoadingHandler;
	private final String mPageName = "ExhiBitionActivity";

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
		setContentView(R.layout.activity_exhi_bition);
		mContext = this;
		venueId = this.getIntent().getExtras().getString("Id");
		MyApplication.getInstance().addActivitys(this);
		init();
	}

	private void init() {
		setTitle();
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		list = new ArrayList<EventInfo>();
		mListView = (ListView) findViewById(R.id.exhibition_listview);
		mVenueProfileItemAdapter = new VenueProfileItemAdapter(this, list);
		mListView.setAdapter(mVenueProfileItemAdapter);
		addData();
	}

	/**
	 * 添加数据
	 */
	private void addData() {

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, EventDetailsActivity.class);
				intent.putExtra("eventId", list.get(arg2).getEventId());
				startActivity(intent);
			}
		});
		getActiviryListData(0);
	}

	/**
	 * 获取数据
	 * 
	 * @param page
	 */
	private void getActiviryListData(int page) {
		Map<String, String> mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, venueId);
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.ACTIVITY_LIST_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil.getAtivityList(resultStr);
							mVenueProfileItemAdapter.setData(list);
							mVenueProfileItemAdapter.notifyDataSetChanged();
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
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText(titlestr);
		title.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exhi_bition, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}

}
