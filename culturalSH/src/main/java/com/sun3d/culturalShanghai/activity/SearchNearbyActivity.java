package com.sun3d.culturalShanghai.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GaoDeMapPoiSeahUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.ActivityListAdapter;
import com.sun3d.culturalShanghai.adapter.VenueListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.NearbyMapInfor;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.windows.EventWindows;
import com.umeng.analytics.MobclickAgent;

/**
 * 检索附近信息
 */
@SuppressLint("NewApi")
public class SearchNearbyActivity extends Activity implements OnClickListener,
		OnItemClickListener, RefreshListenter {
	private Context mContext;
	private ListView nearby_listview;
	private ImageButton title_left;
	private ImageButton title_right;
	private TextView mNearbyMapControls;// 小地图，大地图的图标
	private TextView nearby_distance;
	private ImageView nearby_distance_img;
	private List<EventInfo> EventlistItem;
	private List<VenueDetailInfor> VenuelistItem;
	public int lastY;
	private MapView mMapView; // 小地图，大地图
	private RelativeLayout titleLayout;
	private Context mcontext;
	private RelativeLayout invis;
	private View hearder;
	private TextView hearder_distance;
	private ImageView hearder_distance_img;
	private AMap aMap;
	private TextView title;
	private MarkerOptions markerOption;
	private LoadingHandler mLoadingHandler;
	private GaoDeMapPoiSeahUtil mGaoDeMapPoiSeahUtil;
	private int ListType = 1;// 1为活动，2为展馆。
	private ActivityListAdapter mActivityAdapter;
	private VenueListAdapter mVenueListAdapter;
	private NearbyMapInfor mNearbyMapInfor;
	private final String mPageName = "SearchNearbyActivity";
	private float distanceCha = (float) 1.5;
	private LinearLayout footViewLayout;
	private TextView mFootText;
	private int mPage = 0;
	private List<UserBehaviorInfo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_nearby);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		mcontext = this;
		initView();
		list = MyApplication.getInstance().getSelectTypeList();
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
		}
		initMap();
		initData();
	}

	public String getTag(List<UserBehaviorInfo> list) {
		StringBuffer tagAll = new StringBuffer();
		StringBuffer tag = new StringBuffer();
		if (list.size() == 0) {
			for (int i = 0; i < list.size(); i++) {
				tag.append(list.get(i).getTagId());
				tag.append(",");
			}
		}
		if (tag.length() > 0) {
			tag = tag.replace(tag.length() - 1, tag.length(), "");
		}
		return tag.toString();
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		ListType = Integer
				.parseInt(this.getIntent().getStringExtra("ListType"));
		mNearbyMapInfor = new NearbyMapInfor();
		switch (ListType) {
		case 1:// 活动
			VenuelistItem = null;
			mNearbyMapInfor.setVenuelistItem(null);
			EventlistItem = new ArrayList<EventInfo>();
			mActivityAdapter = new ActivityListAdapter(mcontext, EventlistItem,
					false);
			nearby_listview.setAdapter(mActivityAdapter);
			setActivityListData(mPage);
			break;
		case 2:// 场馆
			EventlistItem = null;
			mNearbyMapInfor.setEventlistItem(null);
			VenuelistItem = new ArrayList<VenueDetailInfor>();
			mVenueListAdapter = new VenueListAdapter(mcontext, VenuelistItem);
			nearby_listview.setAdapter(mVenueListAdapter);
			nearby_listview.setDividerHeight(5);
			setVenueListData(mPage);
			break;
		default:
			mLoadingHandler.stopLoading();
			break;
		}

	}

	/**
	 * 设置活动数据处理
	 */
	private void setActivityListData(int page) {
		Map<String, String> mParams = new HashMap<String, String>();
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")) {
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}
		mParams.put("appType", "1");// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		mParams.put("activityTypeId", getTag(list));
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.ACTIVITYLISTURL,
				mParams, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								List<EventInfo> list = JsonUtil
										.getEventList(resultStr);
								if (list.size() > 0) {
									EventlistItem.addAll(list);
									hearder_distance
											.setText(getDistance(EventlistItem
													.get(0).getDistance())
													+ "Km");
									setDistanceImgBg(getDistance(EventlistItem
											.get(0).getDistance()));
									mActivityAdapter.setList(EventlistItem);
									mNearbyMapInfor
											.setEventlistItem(EventlistItem);
									mGaoDeMapPoiSeahUtil.addActivityMakrt(
											EventlistItem, VenuelistItem, 1);
								} else {
									mFootText.setText(mContext.getResources()
											.getString(R.string.list_foot));
								}
								if (list.size() < HttpUrlList.HTTP_NUM) {
									nearby_listview
											.removeFooterView(footViewLayout);
								}
								mLoadingHandler.stopLoading();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								mLoadingHandler.showErrorText(e.toString());
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 修改显示图片的
	 * 
	 * @param distance
	 */
	private void setDistanceImgBg(String distance) {
		BigDecimal bgsmo = new BigDecimal(distance);
		if (bgsmo.floatValue() >= distanceCha) {
			nearby_distance_img.setImageResource(R.drawable.sh_icon_oncar);
			hearder_distance_img.setImageResource(R.drawable.sh_icon_oncar);
		} else {
			nearby_distance_img.setImageResource(R.drawable.sh_icon_onfoot);
			hearder_distance_img.setImageResource(R.drawable.sh_icon_onfoot);
		}
	}

	/**
	 * 场馆数据处理
	 */
	private void setVenueListData(int page) {

		Map<String, String> mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_LAT,
				AppConfigUtil.LocalLocation.Location_latitude + "");
		mParams.put(HttpUrlList.HTTP_LON,
				AppConfigUtil.LocalLocation.Location_longitude + "");
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		mParams.put("appType", "1");// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.VENUE_LIST_SCREEN,
				mParams, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingHandler.stopLoading();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<VenueDetailInfor> list = JsonUtil
									.getVenueListInfoList(resultStr);
							if (list.size() > 0) {
								VenuelistItem.addAll(list);
								hearder_distance
										.setText(getDistance(VenuelistItem.get(
												0).getDistance())
												+ "Km");
								setDistanceImgBg(getDistance(VenuelistItem.get(
										0).getDistance()));
								mVenueListAdapter.setList(VenuelistItem);
								mNearbyMapInfor.setVenuelistItem(VenuelistItem);
								mGaoDeMapPoiSeahUtil.addActivityMakrt(
										EventlistItem, VenuelistItem, 1);

							} else {
								mFootText.setText(mContext.getResources()
										.getString(R.string.list_foot));
							}
							if (list.size() < HttpUrlList.HTTP_NUM) {
								nearby_listview
										.removeFooterView(footViewLayout);
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}

					}
				});
	}

	/**
	 * 初始化地图
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();
		}
		// 隐藏放大缩小按钮
		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setZoomControlsEnabled(false);
		mGaoDeMapPoiSeahUtil = new GaoDeMapPoiSeahUtil(mcontext, aMap);

	}

	/**
	 * 地图设置
	 */
	private void setUpMap() {
		double lat;
		double lon;
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("")) {
			lat = Double.parseDouble(MyApplication.Location_latitude);
			lon = Double.parseDouble(MyApplication.Location_longitude);
		} else {
			lat = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_latitude);
			lon = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_longitude);
		}

		LatLng ll = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
		// aMap.setLocationSource(new MyLocationSource(this, aMap));// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		// 显示标记
		markerOption = new MarkerOptions();
		markerOption.position(ll);
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);
		aMap.addMarker(markerOption);

	}

	/**
	 * 初始化信息
	 */
	@SuppressLint("CutPasteId")
	private void initView() {
		// TODO Auto-generated method stub
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		hearder = View.inflate(this, R.layout.search_maolayout, null);// 头部内容
		invis = (RelativeLayout) findViewById(R.id.invis);
		nearby_distance = (TextView) findViewById(R.id.nearby_one_label);
		nearby_distance_img = (ImageView) findViewById(R.id.nearby_one_label_img);
		// 标题栏
		titleLayout = (RelativeLayout) findViewById(R.id.nearby_title);
		title_left = (ImageButton) titleLayout.findViewById(R.id.title_left);
		title_right = (ImageButton) titleLayout.findViewById(R.id.title_right);
		title = (TextView) titleLayout.findViewById(R.id.title_content);
		title.setVisibility(View.VISIBLE);
		title.setText("附  近");
		title_right.setImageResource(R.drawable.sh_icon_search_nearby);
		// title_right.setOnClickListener(this);
		title_right.setVisibility(View.GONE);
		hearder_distance = (TextView) hearder
				.findViewById(R.id.nearby_one_label);
		hearder_distance_img = (ImageView) hearder
				.findViewById(R.id.nearby_one_label_img);
		nearby_listview = (ListView) findViewById(R.id.nearby_listview);
		nearby_listview.addHeaderView(hearder, null, false);// 添加头部
		mMapView = (MapView) hearder.findViewById(R.id.nearby_map1);
		mNearbyMapControls = (TextView) hearder
				.findViewById(R.id.nearby_map_controls);
		// 地图放大后的控件
		mNearbyMapControls.setOnClickListener(this);
		title_left.setOnClickListener(this);
		footViewLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.list_foot, null);
		mFootText = (TextView) footViewLayout.findViewById(R.id.list_foot_text);
		nearby_listview.addFooterView(footViewLayout);
		nearby_listview.setOnItemClickListener(this);
		nearby_listview.setOnScrollListener(new PauseOnScrollListener(
				MyApplication.getInstance().getImageLoader(), true, false));
		nearby_listview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem >= 1) {
					invis.setVisibility(View.VISIBLE);

					if (ListType == 1 && EventlistItem != null
							&& firstVisibleItem < EventlistItem.size()) {
						nearby_distance.setText(getDistance(EventlistItem.get(
								firstVisibleItem).getDistance())
								+ "Km");
						setDistanceImgBg(getDistance(EventlistItem.get(
								firstVisibleItem).getDistance()));
					} else if (ListType == 2 && VenuelistItem != null
							&& firstVisibleItem < VenuelistItem.size()) {
						nearby_distance.setText(getDistance(VenuelistItem.get(
								firstVisibleItem).getDistance())
								+ "Km");
						setDistanceImgBg(getDistance(VenuelistItem.get(
								firstVisibleItem).getDistance()));
					}

				} else {
					if (ListType == 1 && EventlistItem != null
							&& firstVisibleItem < EventlistItem.size()) {
						setDistanceImgBg(getDistance(EventlistItem.get(
								firstVisibleItem).getDistance()));
					} else if (ListType == 2 && VenuelistItem != null
							&& firstVisibleItem < VenuelistItem.size()) {
						setDistanceImgBg(getDistance(VenuelistItem.get(
								firstVisibleItem).getDistance()));
					}
					invis.setVisibility(View.GONE);
				}
			}
		});
		hearder.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				// TODO Auto-generated method stub
				nearby_listview.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		footViewLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				mPage = mPage + HttpUrlList.HTTP_NUM;
				switch (ListType) {
				case 1:// 活动
					setActivityListData(mPage);
					break;
				case 2:// 场馆
					setVenueListData(mPage);
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 数据处理 保留一位小数 四舍五入
	 * 
	 * @param dis
	 * @return
	 */
	private String getDistance(String dis) {
		BigDecimal bgsmo = new BigDecimal(dis);
		double disdou = bgsmo.setScale(1, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		return String.valueOf(disdou);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		Intent intent = null;
		switch (view.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			EventWindows.getInstance(mcontext).showSearch(titleLayout, false);
			break;
		case R.id.nearby_map_controls: // 放大
			intent = new Intent(mcontext, BigMapViewActivity.class);
			intent.putExtra(AppConfigUtil.INTENT_TYPE, "1");
			intent.putExtra("titleContent", title.getText().toString());
			intent.putExtra("data", mNearbyMapInfor);
			startActivity(intent);
			if (version > 5) {
				overridePendingTransition(R.anim.getinto_animation,
						R.anim.exit_animation);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 条目单击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg2 - 1 > -1) {
			if (ListType == 1) {
				EventInfo eventInfo = EventlistItem.get(arg2 - 1);
				Intent intent = new Intent(mcontext, EventDetailsActivity.class);
				intent.putExtra("eventId", eventInfo.getEventId());
				startActivity(intent);
			} else if (ListType == 2) {
				Intent intent = new Intent(mcontext, VenueDetailActivity.class);
				intent.putExtra("venueId", VenuelistItem.get(arg2 - 1)
						.getVenueId());
				startActivity(intent);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		if (mMapView != null)
			mMapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
		if (mMapView != null)
			mMapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (mMapView != null)
			mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMapView != null)
			mMapView.onDestroy();
		if (aMap == null) {
			aMap.clear();
		}
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		initData();
	}

}
