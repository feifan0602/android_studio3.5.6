package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.MyCommentAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyCommentBean;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCommentActivity extends Activity implements OnClickListener,
		RefreshListenter {
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private ListView my_comment_listview;
	private MyCommentAdapter ma;
	private ImageView left_iv;
	private TextView middle_tv;
	private ArrayList<MyCommentBean> list;
	private LinearLayout activity_ll, venue_ll;
	private ImageView activity_view, venue_view;
	private TextView activity_tv, venue_tv;
	private TextView collection_null;
	public int venue_num = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_comment);
		initview();
		super.onCreate(savedInstanceState);
	}

	private void initview() {
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		collection_null = (TextView) findViewById(R.id.collection_null);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("我的评论");
		my_comment_listview = (ListView) findViewById(R.id.my_comment_listview);
		activity_ll = (LinearLayout) findViewById(R.id.activity_ll);
		venue_ll = (LinearLayout) findViewById(R.id.venue_ll);
		activity_ll.setOnClickListener(this);
		venue_ll.setOnClickListener(this);
		activity_view = (ImageView) findViewById(R.id.activity_view);
		venue_view = (ImageView) findViewById(R.id.venue_view);
		activity_tv = (TextView) findViewById(R.id.activity_tv);
		venue_tv = (TextView) findViewById(R.id.venue_tv);
		activity_tv.setTypeface(MyApplication.GetTypeFace());
		venue_tv.setTypeface(MyApplication.GetTypeFace());
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		getData();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyCommentActivity"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyCommentActivity");
	}

	private void getData() {
		Map<String, String> mActivityParams = new HashMap<String, String>();
		mActivityParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mActivityParams.put(HttpUrlList.HTTP_PAGE_NUM, "10");
		mActivityParams.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Collect.WFF_APPACTIVITYCOMMENTLIST,
				mActivityParams, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是评论的数据" + resultStr);
						// TODO Auto-generated method stub
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							try {
								list = new ArrayList<MyCommentBean>();
								list.clear();
								JSONObject json = new JSONObject(resultStr);
								int status = json.optInt("status");
								if (1 == status) {
									JSONArray arr = json.getJSONArray("data");
									if (arr.length() == 0) {
										collection_null
												.setVisibility(View.VISIBLE);
										my_comment_listview
												.setVisibility(View.GONE);
										mLoadingHandler.stopLoading();
										return;
									} else {
										collection_null
												.setVisibility(View.GONE);
										my_comment_listview
												.setVisibility(View.VISIBLE);
									}
									for (int i = 0; i < arr.length(); i++) {
										JSONObject jo = arr.getJSONObject(i);
										MyCommentBean myc = new MyCommentBean();
										myc.setActivityId(jo
												.optString("activityId"));
										myc.setCommentImgUrl(jo
												.optString("commentImgUrl"));
										myc.setCommentTime(jo
												.optString("commentTime"));
										myc.setCommentRemark(jo
												.optString("commentRemark"));
										myc.setVenueId(jo.optString("venueId"));
										myc.setVenueName(jo
												.optString("venueName"));
										myc.setCommentId(jo
												.optString("commentId"));
										myc.setActivityName(jo
												.optString("activityName"));
										list.add(myc);
									}
									handler.sendEmptyMessage(1);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				ma = new MyCommentAdapter(MyCommentActivity.this, list);
				my_comment_listview.setAdapter(ma);
				mLoadingHandler.stopLoading();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_iv:
			finish();
			break;
		case R.id.activity_ll:
			clearll();
			activity_view.setImageResource(R.color.text_color_5e);
			activity_tv.setTextColor(getResources().getColor(
					R.color.text_color_5e));
			mLoadingHandler.startLoading();
			venue_num = 1;
			getData();
			break;
		case R.id.venue_ll:
			clearll();
			venue_view.setImageResource(R.color.text_color_5e);
			venue_tv.setTextColor(getResources()
					.getColor(R.color.text_color_5e));
			venue_num = 2;
			mLoadingHandler.startLoading();
			getVenueData();
			break;
		default:
			break;
		}

	}

	private void getVenueData() {
		Map<String, String> mActivityParams = new HashMap<String, String>();
		mActivityParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mActivityParams.put(HttpUrlList.HTTP_PAGE_NUM, "10");
		mActivityParams.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Collect.WFF_APPVENUECOMMENTLIST, mActivityParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是场馆评论的数据"
								+ resultStr);
						// TODO Auto-generated method stub
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							try {
								list = new ArrayList<MyCommentBean>();
								list.clear();
								JSONObject json = new JSONObject(resultStr);
								int status = json.optInt("status");
								if (1 == status) {
									JSONArray arr = json.getJSONArray("data");
									if (arr.length() == 0) {
										collection_null
												.setVisibility(View.VISIBLE);
										my_comment_listview
												.setVisibility(View.GONE);
										mLoadingHandler.stopLoading();
										return;
									} else {
										collection_null
												.setVisibility(View.GONE);
										my_comment_listview
												.setVisibility(View.VISIBLE);
									}

									for (int i = 0; i < arr.length(); i++) {
										JSONObject jo = arr.getJSONObject(i);
										MyCommentBean myc = new MyCommentBean();
										myc.setActivityId(jo
												.optString("activityId"));
										myc.setCommentImgUrl(jo
												.optString("commentImgUrl"));
										myc.setCommentTime(jo
												.optString("commentTime"));
										myc.setCommentRemark(jo
												.optString("commentRemark"));
										myc.setVenueId(jo.optString("venueId"));
										myc.setVenueName(jo
												.optString("venueName"));
										myc.setCommentId(jo
												.optString("commentId"));
										myc.setActivityName(jo
												.optString("activityName"));
										list.add(myc);
									}
									handler.sendEmptyMessage(1);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				});
	}

	private void clearll() {
		activity_view.setImageResource(R.color.text_color_df);
		venue_view.setImageResource(R.color.text_color_df);
		activity_tv
				.setTextColor(getResources().getColor(R.color.text_color_49));
		venue_tv.setTextColor(getResources().getColor(R.color.text_color_49));
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub

	}
}
