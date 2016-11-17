package com.sun3d.culturalShanghai.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.GetPhoneInfoUtil;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.adapter.WindowsAdapter;
import com.sun3d.culturalShanghai.adapter.WindowsTwoAdapter;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.EventAddressInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;
import com.sun3d.culturalShanghai.object.SearchInfo;
import com.sun3d.culturalShanghai.object.WindowInfo;

/**
 * 选择场馆时候 首页的右上角搜索的按键后呈现出来的Popupwindow
 * 
 * @author wenff
 * 
 */
public class VenueWindows implements OnClickListener {
	private View mView;
	private PopupWindow popup;
	private LinearLayout mSendLayout;
	private WindowsAdapter mTypeAdapter;
	private WindowsTwoAdapter mTypeTwoAdapter;
	private GridView mAdressGrid;
	private static ArrayList<HashMap<String, String>> list_venue = new ArrayList<HashMap<String, String>>();
	private WindowsAdapter mAdressAdapter;
	private WindowsTwoAdapter mAdressTwoAdapter;
	private List<SearchInfo> mTypeList;
	private List<SearchInfo> mAdressList;
	private List<SearchInfo> mClassicationList;
	private Context mContext;
	private RadioGroup mTab;
	private static VenueWindows venueWindows;
	private static WindowInfo windowList;
	public static ScreenInfo screenInfo;
	private boolean isMain;
	private EditText etContent;
	private TextView hide, hide_classication, hide_hotword;
	private boolean hide_bool = true;
	private boolean hide_bool_classication = true;
	private boolean hide_bool_hot_word = true;
	private LinearLayout mClassicationLayout;
	private RelativeLayout mTop;
	private SimpleAdapter adapter;
	private ListView listview;
	private String venueType, venueArea, venueName;
	private ArrayList<HashMap<String, String>> list;
	private TextView all_delete;
	private TextView history_tv;
	private LinearLayout mHotWordLayout;
	/**
	 * 这个是区域的布局
	 */
	private GridView mLocationGrid, mClassicationGrid, mHotWordGrid;

	public static VenueWindows getInstance(Context mContext) {
		if (venueWindows == null) {
			venueWindows = new VenueWindows(mContext);
			screenInfo = new ScreenInfo();
		}
		list_venue = SharedPreManager.wffreadVenueAddressInfor();
		return venueWindows;
	}

	public VenueWindows(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		initView();
		getData();
	}

	// /appHot/getVenue.do
	/**
	 * 显示
	 * 
	 * @param mTop
	 * @param isMain
	 */
	public void show(RelativeLayout mTop, boolean isMain, String Title) {
		this.isMain = isMain;
		this.mTop = mTop;
		screenInfo.setSerach(etContent.getText().toString());
		screenInfo.setTabType(MyApplication.getInstance().getVenueType());
		// 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		if ("1".equals(MyApplication.getInstance().getVenueType())) {
			mAdressLayout.setVisibility(View.GONE);
			mClassicationLayout.setVisibility(View.GONE);
			mHotWordLayout.setVisibility(View.GONE);
		} else {
			mAdressLayout.setVisibility(View.VISIBLE);
			mClassicationLayout.setVisibility(View.VISIBLE);
			mHotWordLayout.setVisibility(View.VISIBLE);
		}
		Log.i("main", isMain + "");
		if (windowList == null) {
			getData();
		} else {
			initData();
			if (isMain) {
				onReset();
			}
			if (!popup.isShowing()) {
				popup.showAsDropDown(mTop, 0, -DensityUtil.dip2px(mContext, 50));
			}
		}

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 获取数据
		onListData();
		// 选择tab
		onSelectTab();
		// 选择类型 3.5的热门搜索
		onSelectType();
		// 选择状态 3.5的分类
		onSelectStatus();
		// 选择位置 3.5的区域
		onSelectAdress();
	}

	private void onListData() {
		list = MyApplication.getInstenceVenueList();
		if (list_venue != null && list_venue.size() != 0) {
			adapter = new SimpleAdapter(mContext, list_venue,
					R.layout.textview_layout, new String[] { "tv" },
					new int[] { R.id.tv });
			listview.setVisibility(View.VISIBLE);
			history_tv.setVisibility(View.VISIBLE);
			all_delete.setVisibility(View.VISIBLE);
		} else {
			adapter = new SimpleAdapter(mContext, list,
					R.layout.textview_layout, new String[] { "tv" },
					new int[] { R.id.tv });
		}
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		MyApplication.setListViewHeightBasedOnChildren(listview);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				HashMap<String, String> map = (HashMap<String, String>) arg0
						.getItemAtPosition(arg2);
				String tv = map.get("tv");
				screenInfo.setSerach(tv);
				onStartActivity();
			}
		});
	}

	/**
	 * 初始化
	 */
	private void initView() {
		mView = View.inflate(mContext, R.layout.window_venue, null);
		if (GetPhoneInfoUtil.getManufacturers().equals("Meizu")) {
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					WindowsUtil.getwindowsHight(mContext)
							- WindowsUtil.getDownMenuHeight(mContext));
		} else if (GetPhoneInfoUtil.getManufacturers().equals("HUAWEI")
				&& !WindowsUtil.getMenu(mContext)) {
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					WindowsUtil.getwindowsHight(mContext)
							- WindowsUtil.getDownMenuHeight(mContext) / 2 + 20);
		} else {
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
		}

		popup.setFocusable(true);
		popup.setBackgroundDrawable(new BitmapDrawable());
		// popup.setAnimationStyle(R.style.popwin_anim_style);
		mSendLayout = (LinearLayout) mView.findViewById(R.id.window_save);
		history_tv = (TextView) mView.findViewById(R.id.history_tv);
		history_tv.setTypeface(MyApplication.GetTypeFace());
		Button mSend = (Button) mSendLayout.findViewById(R.id.save);
		etContent = (EditText) mView.findViewById(R.id.search_et);
		etContent.setTypeface(MyApplication.GetTypeFace());
		etContent.setHint("搜索场馆");
		etContent.setOnKeyListener(onKey);
		mTab = (RadioGroup) mView.findViewById(R.id.window_top);
		mTab1 = (RadioButton) mView.findViewById(R.id.window_tab1);
		mTab1.setChecked(true);
		all_delete = (TextView) mView.findViewById(R.id.all_delete);
		all_delete.setTypeface(MyApplication.GetTypeFace());
		all_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				list.clear();
				SharedPreManager.clearVenueAddressInfo();
				adapter.notifyDataSetChanged();
				listview.setVisibility(View.GONE);
				history_tv.setVisibility(View.GONE);
				all_delete.setVisibility(View.GONE);
			}
		});
		mAdressLayout = (LinearLayout) mView.findViewById(R.id.window_adress);
		mClassicationLayout = (LinearLayout) mView
				.findViewById(R.id.window_classification_venue);
		mHotWordLayout = (LinearLayout) mView.findViewById(R.id.window_hotword);
		listview = (ListView) mView.findViewById(R.id.history);
		mAdressGrid = (GridView) mAdressLayout.findViewById(R.id.grid);
		mClassicationGrid = (GridView) mClassicationLayout
				.findViewById(R.id.grid);
		mHotWordGrid = (GridView) mHotWordLayout.findViewById(R.id.grid);

		hide = (TextView) mAdressLayout.findViewById(R.id.hide);
		hide.setTypeface(MyApplication.GetTypeFace());
		hide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hide_bool) {
					mAdressGrid.setAdapter(mAdressTwoAdapter);
					hide_bool = false;
					hide.setText("展开");
				} else {
					mAdressGrid.setAdapter(mAdressAdapter);
					hide_bool = true;
					hide.setText("收起");
				}
			}
		});
		hide_classication = (TextView) mClassicationLayout
				.findViewById(R.id.hide);
		hide_classication.setTypeface(MyApplication.GetTypeFace());
		hide_classication.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hide_bool_classication) {
					mClassicationGrid.setAdapter(mStatusTwoAdapter);
					hide_bool_classication = false;
					hide_classication.setText("展开");
				} else {
					mClassicationGrid.setAdapter(mStatusAdapter);
					hide_bool_classication = true;
					hide_classication.setText("收起");
				}
			}
		});
		hide_hotword = (TextView) mHotWordLayout.findViewById(R.id.hide);
		hide_hotword.setTypeface(MyApplication.GetTypeFace());
		hide_hotword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hide_bool_hot_word) {
					mHotWordGrid.setAdapter(mTypeTwoAdapter);
					hide_bool_hot_word = false;
					hide_hotword.setText("展开");
				} else {
					mHotWordGrid.setAdapter(mTypeAdapter);
					hide_bool_hot_word = true;
					hide_hotword.setText("收起");
				}
			}
		});
		TextView mAdressText = (TextView) mAdressLayout
				.findViewById(R.id.grid_name);
		TextView mClassicationText = (TextView) mClassicationLayout
				.findViewById(R.id.grid_name);
		TextView mHotWordText = (TextView) mHotWordLayout
				.findViewById(R.id.grid_name);
		mAdressText.setText("热门区域");
		mClassicationText.setText("热门分类");
		mHotWordText.setText("热门搜索  ");
		mAdressText.setTypeface(MyApplication.GetTypeFace());
		mClassicationText.setTypeface(MyApplication.GetTypeFace());
		mHotWordText.setTypeface(MyApplication.GetTypeFace());
		mStatusList = new ArrayList<SearchInfo>();
		// mStatusList.add(new SearchInfo("1", "不可预订"));
		mStatusList.add(new SearchInfo("2", "可预订"));
		mStatusList.add(new SearchInfo("", "全部"));
		// 保存选择条件
		mSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onStartActivity();
			}
		});
		// 重新设定
		mView.findViewById(R.id.search_reset).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// onReset();
						if (popup != null) {
							popup.dismiss();
						}
					}
				});
		// 取消
		mView.findViewById(R.id.search_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (popup != null) {
							popup.dismiss();
						}
					}
				});
	}

	/**
	 * 监听回车事件
	 */
	OnKeyListener onKey = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_ENTER) {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
					if (TextUtils.isEmpty(etContent.getText())) {
						ToastUtil.showToast("请输入关键字");
					} else {
						onStartActivity();
					}

				}
				return true;
			}
			return false;
		}
	};
	private RadioButton mTab1;
	private LinearLayout mAdressLayout;
	private ArrayList<SearchInfo> mStatusList;
	private WindowsAdapter mStatusAdapter;
	private WindowsTwoAdapter mStatusTwoAdapter;

	/**
	 * 选择tab
	 */
	private void onSelectTab() {
		mTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.window_tab1:
					screenInfo.setTabType("1");
					mAdressLayout.setVisibility(View.GONE);
					break;
				// case R.id.window_tab2:
				// screenInfo.setTabType("2");
				// break;
				case R.id.window_tab3:
					screenInfo.setTabType("3");
					mAdressLayout.setVisibility(View.VISIBLE);
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 选择状态 3.5分类
	 */
	private void onSelectStatus() {
		// 是否可预定 1-否 2 -是（可选）
		mClassicationList = new ArrayList<SearchInfo>();
		// String[] str = new String[] { "上海市", "闸北区", "宝山区", "青浦区", "嘉定区",
		// "闵行区", "金山区" };
		// for (int i = 0; i < str.length; i++) {
		// mAdressList.add(new SearchInfo(str[i]));
		// }
		mClassicationList.addAll(windowList.getAdressList());

		mStatusAdapter = new WindowsAdapter(mContext, mClassicationList);
		mStatusTwoAdapter = new WindowsTwoAdapter(mContext, mClassicationList);
		mClassicationGrid.setAdapter(mStatusTwoAdapter);
		mClassicationGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i("ceshi", "mAdressList  =  " + windowList.getAdressList());
				venueType = mClassicationList.get(arg2).getTagId();
				isMain = true;
				// isMain = true;
				// activityInfo.setActivityType("3");
				onStartActivity();

			}
		});

	}

	/**
	 * 选择类型 3.5的热门活动
	 * 
	 * @param
	 */
	private void onSelectType() {
		mTypeList = new ArrayList<SearchInfo>();
		// String[] str = new String[] { "图书馆", "文化馆", "博物馆", "陈列馆" };
		// for (int i = 0; i < 4; i++) {
		// mTypeList.add(new SearchInfo(str[i]));
		// }

		mTypeList.addAll(windowList.getCrowdList());

		mTypeAdapter = new WindowsAdapter(mContext, mTypeList);
		mTypeTwoAdapter = new WindowsTwoAdapter(mContext, mTypeList);
		mHotWordGrid.setAdapter(mTypeTwoAdapter);
		mHotWordGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				venueName = mTypeList.get(arg2).getTagName();
				isMain = true;
				onStartActivity();

			}
		});
	}

	/**
	 * 选择位置 3.5的区域地区
	 */
	private void onSelectAdress() {
		mAdressList = new ArrayList<SearchInfo>();
		// String[] str = new String[] { "上海市", "闸北区", "宝山区", "青浦区", "嘉定区",
		// "闵行区", "金山区" };
		// for (int i = 0; i < str.length; i++) {
		// mAdressList.add(new SearchInfo(str[i]));
		// }
		mAdressList.addAll(windowList.getNatureList());
		mAdressAdapter = new WindowsAdapter(mContext, mAdressList);
		mAdressTwoAdapter = new WindowsTwoAdapter(mContext, mAdressList);
		mAdressGrid.setAdapter(mAdressTwoAdapter);

		mAdressGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				venueArea = mAdressList.get(arg2).getTagId();
				isMain = true;
				onStartActivity();
				// TODO Auto-generated method stub
				// for (int i = 0; i < mAdressList.size(); i++) {
				// if (i == arg2) {
				// mAdressList.get(arg2).setSeleced(true);
				// screenInfo.setAdress(mAdressList.get(arg2).getTagId());
				// } else {
				// mAdressList.get(i).setSeleced(false);
				// }
				// }
				mAdressAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 保存选择条件
	 */
	private void onStartActivity() {
		if (!TextUtils.isEmpty(etContent.getText())) {
			if (etContent.getText().toString().equals("%")) {
				ToastUtil.showToast("不能输入非法字符");
				return;
			}
			screenInfo.setSerach(etContent.getText().toString());
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("tv", etContent.getText().toString());
			if (list_venue != null && list_venue.size() != 0) {
				list = list_venue;
			}
			if (list.size() >= 5) {
				list.remove(0);
				list.add(map);
			} else {
				list.add(map);
			}
			EventAddressInfo event_info = new EventAddressInfo(list);
			saveVenuAddress(event_info);
			history_tv.setVisibility(View.VISIBLE);
			listview.setVisibility(View.VISIBLE);
			all_delete.setVisibility(View.VISIBLE);
			etContent.setText("");
		}
		if (popup != null) {
			popup.dismiss();
		}
		if (isMain) {
			Intent intent = new Intent(mContext, VenueListActivity.class);
			intent.putExtra("venueType", venueType);
			intent.putExtra("venueArea", venueArea);
			intent.putExtra("venueName", venueName);
			intent.putExtra(AppConfigUtil.INTENT_SCREENINFO, screenInfo);
			mContext.startActivity(intent);
			venueType = "";
			venueArea = "";
			venueName = "";
		} else {
			Message msg = new Message();
			msg.what = 0;
			msg.obj = screenInfo;
			MyApplication.getInstance().getVenueListHandler().sendMessage(msg);
		}
	}

	/**
	 * 重设
	 */
	private void onReset() {
		for (int i = 0; i < mTypeList.size(); i++) {
			if (mTypeList.get(i).isSeleced()) {
				mTypeList.get(i).setSeleced(false);
				break;
			}
		}
		for (int i = 0; i < mAdressList.size(); i++) {
			if (mAdressList.get(i).isSeleced()) {
				mAdressList.get(i).setSeleced(false);
				break;
			}
		}
		for (int i = 0; i < mStatusList.size(); i++) {
			if (mStatusList.get(i).isSeleced()) {
				mStatusList.get(i).setSeleced(false);
				break;
			}
		}
		screenInfo.clear();
		// screenInfo.setTabType("1");
		etContent.setText("");
		// mAdressList.get(0).setSeleced(true);
		// mTab1.setChecked(true);
		mTypeAdapter.notifyDataSetChanged();
		mStatusAdapter.notifyDataSetChanged();
		mAdressAdapter.notifyDataSetChanged();
	}

	// 获取场馆筛选条件
	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appType", "3");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Window.WFF_VENUE_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "pop---" + resultStr);
						// TODO Auto-generated method stub
						WindowInfo info = new WindowInfo();
						info = JsonUtil.getWindowList(resultStr, 1);
						MyApplication.getInstance().setVenueWindowList(info);
						windowList = MyApplication.getInstance()
								.getVenueWindowList();
						// initView();
						show(mTop, isMain, "场馆");
					}
				});
	}

	/**
	 * 保存搜索信息
	 * 
	 * @param userinfo
	 */
	private void saveVenuAddress(EventAddressInfo event_info) {
		if (event_info == null) {
			return;
		}
		String str = JsonHelperUtil.toJSON(event_info).toString();
		SharedPreManager.saveVenueAddressInfor(str);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
