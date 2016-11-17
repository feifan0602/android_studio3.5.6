package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.ActivityCodeAdapter;
import com.sun3d.culturalShanghai.handler.CodeInfo;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCodeActivity extends Activity implements OnClickListener {
	private TextView title_content;
	private ImageButton title_left;
	private PullToRefreshListView mListView;
	private ActivityCodeAdapter aca;
	private int index = 0;
	private int indexNum = 20;
	private List<CodeInfo> list;
	private List<CodeInfo> mList;
	private TextView total_code;
	private TextView more_code;
	/**
	 * true 刷新 false 加载
	 */
	private boolean resh_add = true;
	private TextView code_more;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acrivity_my_code_layout);
		init();
		initData();
	}

	private void initData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());

		params.put(HttpUrlList.HTTP_PAGE_INDEX, index + "");
		params.put(HttpUrlList.HTTP_PAGE_NUM, indexNum + "");

		MyHttpRequest.onHttpPostParams(HttpUrlList.Code.MY_CODE_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是积分" + resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil.getMyCode(resultStr);
							if (resh_add == false) {
								if (list.size() == 0) {
									handler.sendEmptyMessage(2);
									return;
								} else {
									mList.addAll(list);
								}

							} else {
								mList.clear();
								mList.addAll(list);
							}

							handler.sendEmptyMessage(1);
						} else {
							ToastUtil.showToast("服务器请求错误");
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mLoadingHandler.stopLoading();
				if (list != null && list.size() != 0) {

					aca.setList(mList);
					String total_code_str = "你有"
							+ +list.get(0).getIntegralNow() + "积分";
					ColorStateList redColors = ColorStateList
							.valueOf(0xffff0000);
					SpannableStringBuilder spanBuilder = new SpannableStringBuilder(
							total_code_str);
					spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 80,
							null, null), 2, total_code_str.length() - 2,
							Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
					total_code.setText(spanBuilder);

					// Html.fromHtml("客户结构框架和公开<h1>什么情况</h1>")
					mListView.onRefreshComplete();
					
				}

				break;
			case 2:
				mListView.onRefreshComplete();
				ToastUtil.showToast("已经全部加载完毕");
				break;
			default:
				break;
			}
		};
	};

	private void init() {
		mList = new ArrayList<CodeInfo>();
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		code_more = (TextView) findViewById(R.id.code_more);
		code_more.setOnClickListener(this);
		more_code = (TextView) findViewById(R.id.more_code);
		more_code.setTypeface(MyApplication.GetTypeFace());
		more_code.setOnClickListener(this);
		total_code = (TextView) findViewById(R.id.total_code);
		total_code.setTypeface(MyApplication.GetTypeFace());
		title_content = (TextView) findViewById(R.id.title_content);
		title_content.setVisibility(View.VISIBLE);
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		title_content.setText("我的积分");
		title_content.setTypeface(MyApplication.GetTypeFace());
		mListView = (PullToRefreshListView) findViewById(R.id.main_list);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullDownToRefresh");
				onResh("");
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载更多
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullUpToRefresh");
				onAddmoreData();
			}
		});
		aca = new ActivityCodeAdapter(MyCodeActivity.this, mList);
		mListView.setAdapter(aca);
	}

	public void onAddmoreData() {
		resh_add = false;
		index = HttpUrlList.HTTP_NUM + index;
		initData();
	}

	public void onResh(String tagId) {
		resh_add = true;
		index = 0;
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.more_code:
			Intent i = new Intent();
			i.setClass(this, MyMoreCodeActivtiy.class);
			this.startActivity(i);
			break;
		case R.id.code_more:
			Intent intent = new Intent(this, BannerWebView.class);
			intent.putExtra("url", HttpUrlList.IP
					+ "/wechatUser/preIntegralRule.do?type=app&userId="
					+ MyApplication.loginUserInfor.getUserId());
			startActivity(intent);
			break;
		default:
			break;
		}

	}
}
