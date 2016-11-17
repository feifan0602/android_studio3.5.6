package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.HomeCenterPopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAreaAdapter;
import com.sun3d.culturalShanghai.adapter.SearchTagListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

public class SearchTagListActivity extends Activity implements OnClickListener {
	private PullToRefreshListView listview;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private SearchTagListAdapter adapter;
	private String TAG = "SearchTagListActivity";
	private ImageView left_iv;
	private TextView middle_tv;
	/**
	 * 默认活动为 0 场馆为 1
	 */
	private int ActivityOrVenue_Adapter = 0;
	/**
	 * 用来记录现在遍历的 活动室数量和 活动数量
	 */
	private int pos;
	private int mPage = 0;
	private TextView select_tv1, select_tv2, select_tv3;
	/**
	 * 智能排序 1-智能排序 2-热门排序 3-最新上线 4-即将结束
	 */
	public int sortType = 1;
	/**
	 * 1-免费 2-收费
	 */
	public String activityIsFree = null;
	/**
	 * 是否预定 1-不可预定 2-可预定
	 */
	public String activityIsReservation = null;
	public String venueArea = null;
	public String venueDicName = "";
	public String venueMood = null;
	public String venueMoodName = "";
	private List<EventInfo> mEventList;
	private List<EventInfo> mList;
	private String TagIdAndTagName = "";
	private String TagId = "";
	private String TagName = "";
	public PopupWindow pw;
	private LinearLayout sieve_ll;
	private ListView listView_right;
	/**
	 * 这是和场馆一样的接口
	 */
	public ArrayList<Wff_VenuePopuwindow> list_area;
	public ArrayList<Wff_VenuePopuwindow> list_area_z;
	private HomePopuWindowAdapter hpwa;
	private View itemView;
	private int mBgColor;
	private View view;
	private Wff_VenuePopuwindow wvpw;
	/**
	 * true 表示没有数据 false 加载到最后
	 */
	private boolean addMore = true;
	private TextView null_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchtaglist_layout);
		initView();
		getBundle();
		initDataArea();
		getListData(mPage);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				null_tv.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				sieve_ll.setVisibility(View.VISIBLE);
				adapter.setList(mList);
				listview.onRefreshComplete();
				mLoadingHandler.stopLoading();
				break;
			case 2:

				// 分为两种情况 一个是加载到底 一个是进来就没数据
				if (addMore) {
					// 没数据
					null_tv.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
					sieve_ll.setVisibility(View.VISIBLE);
					mLoadingHandler.stopLoading();
				} else {
					// 加载到底
					listview.onRefreshComplete();
					Toast.makeText(SearchTagListActivity.this, "已经全部加载完毕", 1000)
							.show();
				}
				break;
			case 3:

				break;
			default:
				break;
			}
		};
	};

	/**
	 * 请求服务器获取数据
	 */
	private void getListData(int page_num) {
		String urlString = HttpUrlList.MyEvent.WFF_APPTOPACTIVITYLIST;
		if (addMore) {
			mLoadingHandler.startLoading();
		}
		HashMap<String, String> mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page_num));
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mParams.put("tagId", TagId);
		mParams.put("activityType", TagId);
		// 商圈
		if (venueMood != "" && venueMood != null) {
			mParams.put("activityLocation", venueMood);
		} else {
			mParams.put("activityLocation", "");
		}
		// 区域
		if (venueArea != "" && venueArea != null) {
			mParams.put("activityArea", venueArea);
		} else {
			mParams.put("activityArea", "");
		}
		if (sortType != 1) {
			mParams.put("sortType", sortType + "");
		} else {
			mParams.put("sortType", "");
		}
		if (activityIsFree != "" && activityIsFree != null) {
			mParams.put("activityIsFree", activityIsFree);
		} else {
			mParams.put("activityIsFree", "");
		}
		if (activityIsReservation != "" && activityIsReservation != null) {
			mParams.put("activityIsReservation", activityIsReservation);
		} else {
			mParams.put("activityIsReservation", "");
		}

		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_latitude.equals("")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("")) {
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}
		MyHttpRequest.onHttpPostParams(urlString, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								Log.i(TAG, "看看请求的数据==  " + resultStr);
								mEventList = JsonUtil.getEventList(resultStr);
								if (mEventList.size() > 0) {
									mList.addAll(mEventList);
									handler.sendEmptyMessage(1);
								} else {
									handler.sendEmptyMessage(2);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
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
		if (bundle.getString("TagId") != null) {
			TagId = bundle.getString("TagId", "");
		} else if (bundle.getString("TagIdAndTagName") != null) {
			TagIdAndTagName = bundle.getString("TagIdAndTagName");
			TagName = TagIdAndTagName.split(";")[0];
			TagId = TagIdAndTagName.split(";")[1];
		}

	}

	private void initView() {
		list_area = new ArrayList<Wff_VenuePopuwindow>();
		list_area_z = new ArrayList<Wff_VenuePopuwindow>();
		mList = new ArrayList<EventInfo>();
		mEventList = new ArrayList<EventInfo>();
		sieve_ll = (LinearLayout) findViewById(R.id.Sieve);
		adapter = new SearchTagListAdapter(this, mEventList, sortType);
		select_tv1 = (TextView) findViewById(R.id.shopping_areas);
		select_tv2 = (TextView) findViewById(R.id.preface);
		select_tv3 = (TextView) findViewById(R.id.sieve);
		select_tv1.setTypeface(MyApplication.GetTypeFace());
		select_tv2.setTypeface(MyApplication.GetTypeFace());
		select_tv3.setTypeface(MyApplication.GetTypeFace());
		select_tv1.setOnClickListener(this);
		select_tv2.setOnClickListener(this);
		select_tv3.setOnClickListener(this);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText(MyApplication.SearchTagTitle);
		null_tv = (TextView) findViewById(R.id.null_tv);
		null_tv.setTypeface(MyApplication.GetTypeFace());
		left_iv.setOnClickListener(this);
		listview = (PullToRefreshListView) findViewById(R.id.main_list);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.view_blank, null);
		listview.getRefreshableView().addHeaderView(view);
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);

		listview.setAdapter(adapter);
		listview.setOnItemClickListener(myItemOnclick);
		listview.setOnRefreshListener(onrefrensh);
	}

	OnItemClickListener myItemOnclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			EventInfo eventInfo = (EventInfo) parent.getItemAtPosition(arg2);
			Intent intent = new Intent(SearchTagListActivity.this,
					ActivityDetailActivity.class);
			intent.putExtra("eventId", eventInfo.getEventId());
			MyApplication.currentCount = -1;
			MyApplication.spike_Time = -1;
			MyApplication.total_availableCount = -1;
			SearchTagListActivity.this.startActivity(intent);

		}
	};
	OnRefreshListener2 onrefrensh = new OnRefreshListener2<View>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<View> refreshView) {
			// TODO Auto-generated method stub
			// 下拉刷新
			mPage = 0;
			onResh();

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<View> refreshView) {
			addMore();
		}
	};

	private void onResh() {
		// TODO Auto-generated method stub
		mList.clear();
		getListData(mPage);
	}

	private void addMore() {
		mPage = HttpUrlList.HTTP_NUM + mPage;
		addMore = false;
		getListData(mPage);
		listview.onRefreshComplete();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_iv:
			finish();
			break;
		case R.id.shopping_areas:
			showPopuwindow(1);
			break;
		case R.id.preface:
			showPopuwindow(2);
			break;
		case R.id.sieve:
			showPopuwindow(3);
			break;
		default:
			break;
		}

	}

	private void showPopuwindow(int i) {
		View mView;
		ListView listView;
		LinearLayout close_ll;
		TextView tv;
		int height;
		ArrayList<Wff_VenuePopuwindow> list;
		if (pw != null) {
			pw.dismiss();
		}
		sieve_ll.setVisibility(View.GONE);
		switch (i) {
		// 全部的商区
		case 1:

			mView = View.inflate(SearchTagListActivity.this,
					R.layout.home_popuwindow_shopping, null);
			ListView listView_left = (ListView) mView
					.findViewById(R.id.listView_left);
			listView_right = (ListView) mView.findViewById(R.id.listView_right);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			close_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw.isShowing()) {
						pw.dismiss();
					}
					sieve_ll.setVisibility(View.VISIBLE);

				}
			});
			hpwa = new HomePopuWindowAdapter(list_area,
					SearchTagListActivity.this);
			listView_left.setAdapter(hpwa);
			listView_left.setOnItemClickListener(left_item);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 2,
					MyApplication.getWindowHeight() / 4);
			showFristPopWindow();

			break;
		case 2:
			mView = View.inflate(SearchTagListActivity.this,
					R.layout.home_popuwindow_preface, null);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("智能排序"));
			list.add(new Wff_VenuePopuwindow("热门排序"));
			list.add(new Wff_VenuePopuwindow("最新上线"));
			list.add(new Wff_VenuePopuwindow("即将结束"));
			list.add(new Wff_VenuePopuwindow("离我最近"));
			hpwa = new HomePopuWindowAdapter(list, SearchTagListActivity.this);
			listView.setAdapter(hpwa);
			height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.9);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			showSecondPopWindow();
			close_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null) {
						pw.dismiss();
						sieve_ll.setVisibility(View.VISIBLE);
					}
				}
			});
			listView.setOnItemClickListener(second_item);
			break;
		case 3:
			mView = View.inflate(SearchTagListActivity.this,
					R.layout.home_popuwindow_sieve, null);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw.isShowing()) {
						pw.dismiss();
					}
					sieve_ll.setVisibility(View.VISIBLE);
					activityIsFree = "";
					activityIsReservation = "";
					getListData(0);
					select_tv3.setText("全部");
				}
			});
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("免费"));
			list.add(new Wff_VenuePopuwindow("在线预订"));
			HomeCenterPopuWindowAdapter hcpwa = new HomeCenterPopuWindowAdapter(
					list, SearchTagListActivity.this);
			listView.setAdapter(hcpwa);
			height = MyApplication.getWindowHeight() / 4;

			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			showThreePopWindow();
			close_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null) {
						pw.dismiss();
						sieve_ll.setVisibility(View.VISIBLE);
					}
				}
			});
			listView.setOnItemClickListener(three_item);
			break;

		default:
			break;
		}

	}

	OnItemClickListener three_item = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mList.clear();
			addMore = true;
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
					.getItemAtPosition(arg2);
			if (arg2 == 0) {
				activityIsFree = "1";
				activityIsReservation = "";
			} else if (arg2 == 1) {
				activityIsFree = "";
				activityIsReservation = "2";
			}
			if (pw != null) {
				pw.dismiss();
				select_tv3.setText(wvp.getDictName());
				sieve_ll.setVisibility(View.VISIBLE);
			}
			getListData(0);

		}
	};

	OnItemClickListener second_item = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mList.clear();
			addMore = true;
			// TODO Auto-generated method stub
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
					.getItemAtPosition(arg2);
			sortType = arg2 + 1;
			adapter.setType(sortType);
			Log.i(TAG, "pos===  " + sortType);
			if (pw != null) {
				pw.dismiss();
				select_tv2.setText(wvp.getDictName());
				sieve_ll.setVisibility(View.VISIBLE);
			}
			getListData(0);

		}
	};
	OnItemClickListener right_item = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mList.clear();
			addMore = true;
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
					.getItemAtPosition(arg2);
			if (arg2 == 0) {
				venueMood = "";
				venueMoodName = wvp.getTagName();
				select_tv1.setText(wvp.getTagName());

				sieve_ll.setVisibility(View.VISIBLE);
				if (pw != null) {
					pw.dismiss();
				}
				getListData(0);
				return;
			}
			venueMood = wvp.getTagId();
			venueMoodName = wvp.getTagName();
			select_tv1.setText(wvp.getTagName());
			getListData(0);
			if (pw != null) {
				pw.dismiss();
			}
		}
	};
	OnItemClickListener left_item = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			
			addMore = true;
			if (arg2 == 0) {
				venueArea = "";
				venueDicName = "";
				venueMood = "";
				venueMoodName = "";
				select_tv1.setText("全上海");
				getListData(0);
				pw.dismiss();
				mList.clear();
				sieve_ll.setVisibility(View.VISIBLE);
				return;
			}
			view = arg1;
			itemBackChanged(arg1);

			wvpw = (Wff_VenuePopuwindow) parent.getItemAtPosition(arg2);
			venueArea = wvpw.getDictCode();
			venueDicName = wvpw.getDictName();
			list_area_z = new ArrayList<Wff_VenuePopuwindow>();
			list_area_z.add(new Wff_VenuePopuwindow("", "全部"
					+ wvpw.getDictName()));
			for (int i = 0; i < wvpw.getDictList().length(); i++) {
				try {
					JSONObject json = wvpw.getDictList().getJSONObject(i);
					list_area_z.add(new Wff_VenuePopuwindow(json
							.optString("id"), json.optString("name")));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			HomePopuWindowAreaAdapter vpwaa = new HomePopuWindowAreaAdapter(
					list_area_z, SearchTagListActivity.this);
			listView_right.setAdapter(vpwaa);
			listView_right.setOnItemClickListener(right_item);
		}
	};

	/**
	 * 改变listitem的背景色
	 * 
	 * @param view
	 */
	private void itemBackChanged(View view) {
		if (itemView == null) {
			itemView = view;
		}
		mBgColor = R.color.text_color_003b;
		itemView.setBackgroundResource(mBgColor);
		mBgColor = R.color.text_color_26;
		// 将上次点击的listitem的背景色设置成透明
		view.setBackgroundResource(mBgColor);
		// 设置当前点击的listitem的背景色
		itemView = view;

	}

	private void showThreePopWindow() {
		pw.showAsDropDown(sieve_ll, (sieve_ll.getWidth() - pw.getWidth()) / 2,
				0);
	}

	private void showSecondPopWindow() {
		pw.showAsDropDown(sieve_ll, (sieve_ll.getWidth() - pw.getWidth()) / 2,
				0);
	}

	private void showFristPopWindow() {
		// 这个是 activity 本身的选项框

		pw.showAsDropDown(sieve_ll, (sieve_ll.getWidth() - pw.getWidth()) / 2,
				0);
	}

	private void initAreaList(String data) {

		try {
			list_area = new ArrayList<Wff_VenuePopuwindow>();
			JSONObject json = new JSONObject(data);
			JSONArray json_arr = json.optJSONArray("data");
			list_area.add(new Wff_VenuePopuwindow("", "全上海", "",
					new JSONArray()));
			for (int i = 0; i < json_arr.length(); i++) {
				JSONObject json_new = json_arr.getJSONObject(i);
				String dictId = json_new.optString("dictId");
				String dictName = json_new.optString("dictName");
				String dictCode = json_new.optString("dictCode");
				JSONArray dictList = json_new.optJSONArray("dictList");
				list_area.add(new Wff_VenuePopuwindow(dictId, dictName,
						dictCode, dictList));

			}
		} catch (Exception ex) {

		}

	}

	private void initDataArea() {
		String area = SharedPreManager.getAllArea();
		if (area != null) {
			initAreaList(area);
		} else {
			Map<String, String> mParams = new HashMap<String, String>();
			MyHttpRequest.onStartHttpGET(HttpUrlList.Venue.WFF_GETALLAREA,
					mParams, new HttpRequestCallback() {

						@Override
						public void onPostExecute(int statusCode,
								String resultStr) {
							if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
								try {
									JSONObject json = new JSONObject(resultStr);
									String status = json.optString("status");
									if (status.equals("200")) {

										if (resultStr.length() > 100) {
											SharedPreManager
													.saveAllArea(resultStr);
										}
										initAreaList(resultStr);
										handler.sendEmptyMessage(3);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
		}
	}
}
