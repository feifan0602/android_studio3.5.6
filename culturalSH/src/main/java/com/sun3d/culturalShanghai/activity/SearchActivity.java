package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.SearchGridViewAdapter;
import com.sun3d.culturalShanghai.adapter.SearchListViewAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.EventAddShareHistory;
import com.sun3d.culturalShanghai.object.SearchHotInfo;
import com.sun3d.culturalShanghai.object.SearchListViewInfo;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends Activity implements OnClickListener {
	private ScrollViewGridView scroll_gridview;
	private SearchGridViewAdapter mSearchGridAdapter;
	private ListView history_listview;
	private SearchListViewAdapter mSearchListAdapter;
	private TextView all_delete_tv;
	private TextView search_tv;
	private TextView search_reset;
	private String TAG = "SearchActivity";
	private TextView activity_tv, venue_tv;
	private PopupWindow pw;
	private TextView grid_name;
	private TextView history_tv;
	private ArrayList<SearchListViewInfo> mList;
	private List<SearchHotInfo> mGridList;
	private List<String> mActivityList;
	private List<String> mVenueList;
	private EditText search_et;
	// private static ArrayList<HashMap<String, String>> list_event = new
	// ArrayList<HashMap<String, String>>();
	// private ArrayList<HashMap<String, String>> list;
	private ArrayList<String> autoCompeleteNameList;
	private ListView autoCompelteteListView;
	private AutoCompeleteAdapter autoAdapter;
	private PopupWindow popupWindow;
	private boolean skIsInput;
	/**
	 * 1 表示Activity 2表示场馆
	 */
	private int activityOrVenue = 1;
	/**
	 * 热门搜索
	 */
	// private String HotSearch = "";

	private JSONObject json;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity_layout);
		MyApplication.getInstance().addActivitys(this);
		skIsInput = true;
		initView();
		getGridViewData();
	}

	private void initView() {

		autoAdapter = new AutoCompeleteAdapter(this);
		autoCompeleteNameList = new ArrayList<String>();
		// list = MyApplication.getInstenceActivityList();
		mActivityList = new ArrayList<String>();
		mVenueList = new ArrayList<String>();
		mList = new ArrayList<SearchListViewInfo>();
		mGridList = new ArrayList<SearchHotInfo>();
		search_et = (EditText) findViewById(R.id.search_et);
		search_et.addTextChangedListener(new MyTextWatcher());
		search_et.setOnKeyListener(onKey);
		history_tv = (TextView) findViewById(R.id.history_tv);
		history_tv.setTypeface(MyApplication.GetTypeFace());
		grid_name = (TextView) findViewById(R.id.grid_name);
		grid_name.setTypeface(MyApplication.GetTypeFace());
		scroll_gridview = (ScrollViewGridView) findViewById(R.id.scroll_gridview);
		history_listview = (ListView) findViewById(R.id.history_listview);
		all_delete_tv = (TextView) findViewById(R.id.all_delete);
		search_tv = (TextView) findViewById(R.id.search_tv);
		String type = getIntent().getStringExtra("type");

		if (type != null) {

			if (type.equals("activity")) {

				activityOrVenue = 1;
				search_tv.setText("活动");
			} else if (type.equals("venue")) {
				activityOrVenue = 2;
				search_tv.setText("场馆");
			}
		}

		search_reset = (TextView) findViewById(R.id.search_reset);
		search_reset.setTypeface(MyApplication.GetTypeFace());
		search_tv.setTypeface(MyApplication.GetTypeFace());
		all_delete_tv.setTypeface(MyApplication.GetTypeFace());
		search_reset.setOnClickListener(this);
		search_tv.setOnClickListener(this);
		all_delete_tv.setOnClickListener(this);
		ArrayList<HashMap<String, String>> list_event = SharedPreManager
				.readShareHistory();
		if (list_event.size() == 0) {
			searchHistoryViewHandler(false);
		} else {
			mList.clear();
			try {
				for (int i = 0; i < list_event.size(); i++) {
					SearchListViewInfo slvi = new SearchListViewInfo();
					slvi.setAddress_Str(list_event.get(i).get("tv"));
					slvi.setType(list_event.get(i).get("type"));
					mList.add(slvi);
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		mSearchListAdapter = new SearchListViewAdapter(this, mList);
		history_listview.setAdapter(mSearchListAdapter);
		history_listview.setOnItemClickListener(historyItemClick);

		MyApplication.setGridViewHeightBasedOnChildren(scroll_gridview);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all_delete:

			// list.clear();
			mList.clear();
			mSearchListAdapter.notifyDataSetChanged();
			SharedPreManager.clearShareHistory();
			SharedPreManager.clearEventAddressInfo();
			searchHistoryViewHandler(false);
			break;
		case R.id.search_tv:
			if (pw == null) {
				View view = View.inflate(this, R.layout.search_pop, null);
				activity_tv = (TextView) view.findViewById(R.id.activity_tv);
				venue_tv = (TextView) view.findViewById(R.id.venue_tv);
				activity_tv.setTypeface(MyApplication.GetTypeFace());
				venue_tv.setTypeface(MyApplication.GetTypeFace());
				activity_tv.setOnClickListener(this);
				venue_tv.setOnClickListener(this);
				pw = new PopupWindow(view, MyApplication.getWindowWidth() / 8,
						MyApplication.getWindowHeight() / 10);
				pw.showAsDropDown(search_tv, 20, 4);

				pw.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_bottom_radius));
			} else {
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				if (!pw.isShowing()) {
					View view = View.inflate(this, R.layout.search_pop, null);
					activity_tv = (TextView) view
							.findViewById(R.id.activity_tv);
					venue_tv = (TextView) view.findViewById(R.id.venue_tv);
					activity_tv.setTypeface(MyApplication.GetTypeFace());
					venue_tv.setTypeface(MyApplication.GetTypeFace());
					activity_tv.setOnClickListener(this);
					venue_tv.setOnClickListener(this);
					pw = new PopupWindow(view,
							MyApplication.getWindowWidth() / 8,
							MyApplication.getWindowHeight() / 10);
					pw.showAsDropDown(search_tv, 20, 4);
				} else {
					pw.dismiss();
				}
			}

			break;
		case R.id.search_reset:
			finish();
			break;
		case R.id.activity_tv:
			if (pw.isShowing()) {
				pw.dismiss();
			}
			activityOrVenue = 1;
			search_tv.setText("活动");
			mActivityList.clear();
			for (int i = 0; i < mGridList.size(); i++) {
				if (mGridList.get(i).getAdvertType().equals("A")) {
					String advertUrl = mGridList.get(i).getAdvertUrl();
					String[] Urls = advertUrl.split(",");
					for (int j = 0; j < Urls.length; j++) {
						mActivityList.add(Urls[j]);
					}
				}
			}
			mSearchGridAdapter.setData(mActivityList);
			break;
		case R.id.venue_tv:
			if (pw.isShowing()) {
				pw.dismiss();
			}
			activityOrVenue = 2;
			search_tv.setText("场馆");
			mVenueList.clear();
			for (int i = 0; i < mGridList.size(); i++) {
				if (mGridList.get(i).getAdvertType().equals("B")) {
					String advertUrl = mGridList.get(i).getAdvertUrl();
					String[] Urls = advertUrl.split(",");
					for (int j = 0; j < Urls.length; j++) {
						mVenueList.add(Urls[j]);
					}
				}
			}
			mSearchGridAdapter.setData(mVenueList);
			break;
		default:
			break;
		}

	}

	// //////////

	private class MyTextWatcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
			}
			if (skIsInput == false) {
				skIsInput = true;
				return;
			}
			if (s != null && s.length() >= 2) {

				final String oldStr = s.toString();
				new Handler().postDelayed(new Runnable() {
					public void run() {

						// Log.i(TAG, "outter ----- >");
						String currentStr = search_et.getText().toString();
						if (!currentStr.equals(oldStr)) {// 延时1s加载
							return;
						}
						// Log.i(TAG, "inner ----- >");

						JSONObject json = new JSONObject();
						try {
							json.put("keyword", currentStr);

						} catch (Exception e) {
							e.printStackTrace();
						}
						MyHttpRequest.onStartHttpPostJSON(
								HttpUrlList.SearchUrl.URL_GET_SK_MATCH, json,
								new HttpRequestCallback() {

									@Override
									public void onPostExecute(int statusCode,
											String resultStr) {

										Log.i(TAG, "看看返回的数据==   " + resultStr);
										// TODO Auto-generated method stub
										if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
											JSONObject json;
											try {
												autoCompeleteNameList.clear();
												json = new JSONObject(resultStr);
												JSONArray arr = json
														.getJSONArray("data");
												for (int i = 0; i < arr
														.length(); i++) {
													String ii = arr
															.optString(i);
													autoCompeleteNameList
															.add(ii);
												}
												updateDropdownList();

											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									}
								});
					}
				}, 100);

			} else {
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	}

	private void updateDropdownList() {
		if (popupWindow == null) {
			autoCompelteteListView = new ListView(this);
			autoCompelteteListView.setAdapter(autoAdapter);
			autoCompelteteListView.setBackground(getResources().getDrawable(
					R.drawable.bg_bottom_radius));
			autoCompelteteListView
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							skIsInput = false;
							String keyname = autoCompeleteNameList
									.get(position);
							search_et.setText(keyname);
							popupWindow.dismiss();
							jumpSearchListActivity(keyname);

						}

					});

			int width = search_et.getWidth();
			popupWindow = new PopupWindow(autoCompelteteListView, width, 0);
			popupWindow.setFocusable(false);
			popupWindow.setOutsideTouchable(false);
			// popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.color_gray_bg));

		}
		if (autoCompeleteNameList != null && autoCompeleteNameList.size() > 0) {

			popupWindow.dismiss();
			popupWindow.setHeight(autoCompeleteNameList.size() * 100);
			autoAdapter.setList(autoCompeleteNameList);
			autoAdapter.notifyDataSetChanged();
			popupWindow.showAsDropDown(search_et, 0, 4);

		}

	}

	private class AutoCompeleteAdapter extends BaseAdapter {
		private ArrayList<String> list;
		private Context context;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (list == null) {
				return 0;
			}
			return list.size();
		}

		public AutoCompeleteAdapter(Context text) {
			this.context = text;
		}

		public void setList(ArrayList<String> list) {
			this.list = list;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = (TextView) convertView;

			if (tv == null) {
				tv = new TextView(context);
				// LinearLayout.LayoutParams p = new
				// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
				tv.setGravity(Gravity.CENTER_VERTICAL);
				tv.setPadding(20, 0, 0, 0);
				tv.setLines(1);
				// p.leftMargin = 20;
				// tv.setLayoutParams(p);
				tv.setHeight(100);

			}
			tv.setText(list.get(position));
			return tv;
		}

	}

	// ////////////

	OnItemClickListener myItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int pos,
				long arg3) {
			String sk = (String) parent.getItemAtPosition(pos);
			search_et.setText(sk);
			// Toast.makeText(SearchActivity.this, sk, 1000).show();
			jumpSearchListActivity(sk);

		}
	};
	OnItemClickListener historyItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int pos,
				long arg3) {
			SearchListViewInfo info = (SearchListViewInfo) parent
					.getItemAtPosition(pos);
			String key = info.getAddress_Str();
			search_et.setText(key);
			jumpSearchListActivity(key);
		}
	};
	/**
	 * 监听回车事件
	 */
	OnKeyListener onKey = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
					if (TextUtils.isEmpty(search_et.getText())) {
						ToastUtil.showToast("请输入关键字");
					} else {
						jumpSearchListActivity(search_et.getText().toString());

						// if (activityOrVenue == 1) {
						// onStartActivity();
						// } else {
						// onStartVenue();
						// }

					}

				}
				return true;
			}
			return false;
		}

	};

	private void closePopupWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
		}

		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	// /**
	// * 根据条件筛选 选择活动
	// */
	// private void onStartActivity() {
	// Intent intent = new Intent(SearchActivity.this,
	// SearchListActivity.class);
	// Bundle bundle = new Bundle();
	// if (HotSearch.equals("")) {
	// if (!TextUtils.isEmpty(search_et.getText()))
	// {
	// if (search_et.getText().toString().equals("%")) {
	// ToastUtil.showToast("不能输入非法字符");
	// return;
	// }
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("type", "0");
	// map.put("tv", search_et.getText().toString());
	// if (list_event != null && list_event.size() != 0) {
	// list = list_event;
	// }
	// if (list.size() >= 5) {
	// list.remove(0);
	// list.add(map);
	// } else {
	// list.add(map);
	// }
	// SearchListViewInfo slvi = new SearchListViewInfo();
	// slvi.setAddress_Str(search_et.getText().toString());
	// slvi.setType("0");
	// mList.add(slvi);
	// mSearchListAdapter.notifyDataSetChanged();
	// EventAddShareHistory event_info = new EventAddShareHistory(list);
	// saveEventShareHistory(event_info);
	// bundle.putString("KeyWord", search_et.getText().toString());
	//
	// }
	// } else {
	// bundle.putString("KeyWord", HotSearch);
	// }
	// bundle.putString("ActivityOrVenue", "activity");
	// intent.putExtras(bundle);
	// SearchActivity.this.startActivity(intent);
	// }
	//
	// /**
	// * 根据条件筛选 选择场馆
	// */
	// private void onStartVenue() {
	// Intent intent = new Intent(SearchActivity.this,
	// SearchListActivity.class);
	// Bundle bundle = new Bundle();
	// if (HotSearch.equals("")) {
	//
	// if (!TextUtils.isEmpty(search_et.getText())) {
	// if (search_et.getText().toString().equals("%")) {
	// ToastUtil.showToast("不能输入非法字符");
	// return;
	// }
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("type", "1");
	// map.put("tv", search_et.getText().toString());
	// if (list_event != null && list_event.size() != 0) {
	// list = list_event;
	// }
	// if (list.size() >= 5) {
	// list.remove(0);
	// list.add(map);
	// } else {
	// list.add(map);
	// }
	// EventAddShareHistory event_info = new EventAddShareHistory(list);
	// saveEventShareHistory(event_info);
	// bundle.putString("KeyWord", search_et.getText().toString());
	// }
	// } else {
	// bundle.putString("KeyWord", HotSearch);
	// }
	// bundle.putString("ActivityOrVenue", "venue");
	// intent.putExtras(bundle);
	// SearchActivity.this.startActivity(intent);
	// }

	private void jumpSearchListActivity(String keyname) {
		if (TextUtils.isEmpty(keyname)) {
			ToastUtil.showToast("请输入关键字！");
			return;
		}
		if (keyname.equals("%")) {
			ToastUtil.showToast("不能输入非法字符！");
			return;
		}

		SearchListViewInfo slvi = new SearchListViewInfo();
		slvi.setAddress_Str(keyname);
		slvi.setType("0");

		if (mList.size() == 0) {
			searchHistoryViewHandler(true);
		} else {

			for (SearchListViewInfo m : mList) {
				String k = m.getAddress_Str();
				if (k.equals(keyname)) {
					mList.remove(m);
					break;

				}
			}
		}

		if (mList.size() >= 5) {
			mList.remove(mList.size() - 1);
		}
		mList.add(0, slvi);
		mSearchListAdapter.notifyDataSetChanged();

		EventAddShareHistory event_info = new EventAddShareHistory(mList);
		saveEventShareHistory(event_info);

		Intent intent = new Intent(SearchActivity.this,
				SearchListActivity.class);
		Bundle bundle = new Bundle();
		if (activityOrVenue == 1) {
			bundle.putString("ActivityOrVenue", "activity");
			bundle.putString("KeyWord", keyname);
		} else {
			bundle.putString("ActivityOrVenue", "venue");
			bundle.putString("KeyWord", keyname);
		}
		intent.putExtras(bundle);
		SearchActivity.this.startActivity(intent);
	}

	/**
	 * 保存搜索信息
	 * 
	 * @param userinfo
	 */
	private void saveEventShareHistory(EventAddShareHistory event_info) {
		if (event_info == null) {
			return;
		}

		String str = JsonHelperUtil.toJSON(event_info).toString();
		SharedPreManager.saveShareHistory(str);
	}

	/**
	 * 获取热门搜索的信息
	 */
	private void getGridViewData() {
		json = new JSONObject();
		try {
			json.put("advertPostion", 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MyHttpRequest.onStartHttpPostJSON(
				HttpUrlList.HomeFragment.PAGEADVERTRECOMMEND, json,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.i(TAG, "状态==  " + statusCode + " resultStr== "
								+ resultStr);
						if (HttpCode.HTTP_Request_OK == statusCode) {
							try {
								mGridList = JsonUtil
										.getSearchHotList(resultStr);
								handler.sendEmptyMessage(1);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							handler.sendEmptyMessage(2);
						}
					}

				});

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 默认为活动
				for (int i = 0; i < mGridList.size(); i++) {
					if (mGridList.get(i).getAdvertType().equals("A")) {
						String advertUrl = mGridList.get(i).getAdvertUrl();
						String[] Urls = advertUrl.split(",");
						for (int j = 0; j < Urls.length; j++) {
							mActivityList.add(Urls[j]);
						}
					}
				}
				mSearchGridAdapter = new SearchGridViewAdapter(
						SearchActivity.this, mActivityList);
				scroll_gridview.setAdapter(mSearchGridAdapter);
				scroll_gridview.setOnItemClickListener(myItemClick);
				break;

			default:
				break;
			}
		};
	};

	private void searchHistoryViewHandler(boolean isShow) {
		if (isShow) {
			all_delete_tv.setVisibility(View.VISIBLE);
			history_listview.setVisibility(View.VISIBLE);
			history_tv.setVisibility(View.VISIBLE);
			all_delete_tv.setVisibility(View.VISIBLE);

		} else {
			all_delete_tv.setVisibility(View.GONE);
			history_listview.setVisibility(View.GONE);
			history_tv.setVisibility(View.GONE);
			all_delete_tv.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		closePopupWindow();
	}

}
