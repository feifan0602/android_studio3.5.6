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
import com.sun3d.culturalShanghai.adapter.ActivityMoreCodeAdapter;
import com.sun3d.culturalShanghai.handler.CodeInfo;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MoreCodeInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MyMoreCodeActivtiy extends Activity implements OnClickListener {
	private int index = 0;
	private int indexNum = 20;
	private List<MoreCodeInfo> list;
	private ActivityMoreCodeAdapter aca;
	private PullToRefreshListView mListView;
	private TextView title_content;
	private ImageButton title_left;
	private List<MoreCodeInfo> mList;
	/**
	 * true 刷新 false 加载
	 */
	private boolean resh_add = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_more_order);
		init();
		initData();
	}

	private void init() {
		mList = new ArrayList<MoreCodeInfo>();
		title_content = (TextView) findViewById(R.id.title_content);
		title_content.setVisibility(View.VISIBLE);
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_left.setOnClickListener(this);
		title_content.setText("更多积分");
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
		aca = new ActivityMoreCodeAdapter(MyMoreCodeActivtiy.this, mList);
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

	private void initData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		// params.put(HttpUrlList.HTTP_USER_ID,
		// "7bb597cf87654aaeac98ad961b2e3148");

		params.put(HttpUrlList.HTTP_PAGE_INDEX, index + "");
		params.put(HttpUrlList.HTTP_PAGE_NUM, indexNum + "");

		MyHttpRequest.onHttpPostParams(HttpUrlList.Code.MY_MORE_CODE_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是积分" + resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil.getMyMoreCode(resultStr);
							if (resh_add == false) {
								if (list.size()==0) {
									handler.sendEmptyMessage(2);
									return;
								}else {
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
				aca.setList(mList);
				mListView.onRefreshComplete();
				break;
			case 2:
				ToastUtil.showToast("已经全部加载完毕");
				mListView.onRefreshComplete();
				break;
			default:
				break;
			}
		};
	};
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;

		default:
			break;
		}

	}

}
