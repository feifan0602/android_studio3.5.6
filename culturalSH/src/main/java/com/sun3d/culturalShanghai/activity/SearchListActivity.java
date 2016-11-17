package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.SearchListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.HttpResponseText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class SearchListActivity extends Activity implements OnClickListener,
		RefreshListenter {
	private PullToRefreshListView listview;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private String ActivityOrVenue;
	private String KeyWord_Str;
	private SearchListAdapter adapter;
	private HttpResponseText httpBackContent;
	private JSONObject json;
	private String TAG = "SearchListActivity";
	private String URL = "";
	private String Num_Url = "";
	private ImageView left_iv;
	private TextView middle_tv;
	private List<EventInfo> mList;
	private List<EventInfo> list;
	/**
	 * 默认活动为 0 场馆为 1
	 */
	private int ActivityOrVenue_Adapter = 0;
	/**
	 * 用来记录现在遍历的 活动室数量和 活动数量
	 */
	private int pos;
	private TextView null_tv;
	private int mPage = 1;
	private boolean addMoreBool = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchlist_layout);
		MyApplication.getInstance().addActivitys(this);
		initView();
		getBundle();
		getListData();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mList.clear();

				if (list.size() == 0) {
					null_tv.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
				} else {
					// 这里进行假分页
					if (list.size() < 10) {
						mList.addAll(list);
					} else {
						for (int i = 0; i < 10 * mPage; i++) {
							if (i < list.size()) {
								mList.add(list.get(i));
							}
						}
					}
					if (mPage == 1) {
						adapter = new SearchListAdapter(
								SearchListActivity.this, mList,
								ActivityOrVenue_Adapter);
						listview.setAdapter(adapter);
					} else {
						adapter.setList(mList);
					}
					if (ActivityOrVenue.equals("venue")) {
						getNumData(0);
					} else {
						listview.onRefreshComplete();
					}

				}
				mLoadingHandler.stopLoading();
				break;
			case 2:
				// 这里一个是加载更多 没数据 一个是 刚进来就没数据
				if (addMoreBool) {
					listview.onRefreshComplete();
					mLoadingHandler.isNotContent();
				} else {
					listview.onRefreshComplete();
					mLoadingHandler.stopLoading();
					Toast.makeText(SearchListActivity.this, "已经全部加载完毕", Toast.LENGTH_LONG)
							.show();
				}
				break;
			case 3:
				adapter.setList(mList);
				listview.onRefreshComplete();
				mLoadingHandler.stopLoading();
				break;
			case 4:
				listview.onRefreshComplete();
				Toast.makeText(SearchListActivity.this, "已经全部加载完毕了哦", Toast.LENGTH_LONG)
						.show();
				break;
			case 5:
				null_tv.setVisibility(View.VISIBLE);
				null_tv.setText("暂无数据，请重新搜索......");
				listview.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 请求服务器获取数据
	 */
	private void getListData() {
		list.clear();
		MyHttpRequest.onStartHttpPostJSON(URL, json, new HttpRequestCallback() {
			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				Log.i(TAG, "statusCode  ==  " + statusCode+"  result Str--  "+resultStr);
				if (statusCode == 1) {
					try {
						list = JsonUtil.getSearchActivityVenueList(resultStr);
						if (list.size() > 0) {
							handler.sendEmptyMessage(1);
						} else {
							handler.sendEmptyMessage(2);
						}
						Log.i(TAG, "list  ==  " + mList.size());

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					handler.sendEmptyMessage(5);
				}
			}
		});
	}

	/**
	 * 获取传过来的数据
	 */
	private void getBundle() {
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		ActivityOrVenue = bundle.getString("ActivityOrVenue");
		KeyWord_Str = bundle.getString("KeyWord");
		try {
			json = new JSONObject();
			json.put("keyword", KeyWord_Str);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (ActivityOrVenue.equals("activity")) {
			URL = HttpUrlList.SearchList.SEARCHACTIVITY;
			ActivityOrVenue_Adapter = 0;
		} else if (ActivityOrVenue.equals("venue")) {
			Num_Url = HttpUrlList.Venue.URL_GET_VENUE_NUMOFROOMS;
			URL = HttpUrlList.SearchList.SEARCHVENUE;
			ActivityOrVenue_Adapter = 1;
		}

	}

	private void getNumData(int position) {
		pos = position;
		if (pos >= list.size()) {
			handler.sendEmptyMessage(4);
			return;
		}
		HashMap<String, String> _param = new HashMap<String, String>();
		_param.put("venueId", mList.get(position).getVenueId());
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Venue.URL_GET_VENUE_NUMOFROOMS, _param,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						try {
							JSONObject json = new JSONObject(resultStr);
							JSONObject jo = json.getJSONObject("data");
							mList.get(pos)
									.setActCount(jo.optInt("actCount", 0));
							mList.get(pos).setRoomCount(
									jo.optInt("roomCount", 0));
							pos++;
							if (pos == mList.size()) {
								handler.sendEmptyMessage(3);
							} else {
								getNumData(pos);

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	private void initView() {
		mList = new ArrayList<EventInfo>();
		list = new ArrayList<EventInfo>();
		left_iv = (ImageView) findViewById(R.id.left_iv);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("搜索结果");
		null_tv = (TextView) findViewById(R.id.null_tv);
		null_tv.setTypeface(MyApplication.GetTypeFace());
		left_iv.setOnClickListener(this);
		listview = (PullToRefreshListView) findViewById(R.id.main_list);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.view_blank, null);
		listview.getRefreshableView().addHeaderView(view);
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		mLoadingHandler.setOnRefreshListenter(this);
		listview.setOnItemClickListener(myItemOnclick);
		listview.setOnRefreshListener(onrefrensh);
	}

	OnItemClickListener myItemOnclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			EventInfo info = (EventInfo) parent
					.getItemAtPosition(arg2);
			if (ActivityOrVenue_Adapter == 0) {
				intent.setClass(SearchListActivity.this,
						ActivityDetailActivity.class);
				intent.putExtra("eventId", info.getActivityId());
				MyApplication.currentCount = -1;
				MyApplication.spike_Time = -1;
				MyApplication.total_availableCount = -1;
			} else {
				intent.setClass(SearchListActivity.this,
						VenueDetailActivity.class);
				intent.putExtra("venueId", info.getVenueId());
			}
			SearchListActivity.this.startActivity(intent);

		}
	};
	OnRefreshListener2 onrefrensh = new OnRefreshListener2<View>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<View> refreshView) {
			// TODO Auto-generated method stub
			// 下拉刷新
			mPage = 1;
			addMoreBool = true;
			getListData();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<View> refreshView) {
			mPage = mPage + 1;
			addMoreBool = false;
			addMore();
		}
	};

	private void addMore() {
		int num_pos;
		int stratIndex = 10 * (mPage - 1);
		for (int i = stratIndex; i < 10 * mPage; i++) {
			if (i < list.size()) {
				mList.add(list.get(i));
			}
		}
		if (ActivityOrVenue.equals("venue")) {
			num_pos = mPage - 1;
			getNumData(10 * num_pos);
		}
		if (mList.size() != 0) {
			adapter.setList(mList);
		}

		listview.onRefreshComplete();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_iv:
			finish();
			break;
		default:
			break;
		}

	}

	private void showPopuwindow(int i) {
		View mView;
		switch (i) {
		case 1:
			mView = View.inflate(SearchListActivity.this,
					R.layout.home_popuwindow_shopping, null);
			break;

		default:
			break;
		}
	}

	@Override
	public void onLoadingRefresh() {
		getListData();
	}

}
