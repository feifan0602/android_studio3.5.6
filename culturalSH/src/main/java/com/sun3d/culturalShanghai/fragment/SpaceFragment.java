package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JavascriptInterface_new;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.NetWorkUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.activity.SearchActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.adapter.SpaceHorListAdapter;
import com.sun3d.culturalShanghai.adapter.SpaceListAdapter;
import com.sun3d.culturalShanghai.adapter.SpacePopLeftAdapter;
import com.sun3d.culturalShanghai.adapter.SpacePopRightAdapter;
import com.sun3d.culturalShanghai.adapter.SpacePopThreeAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.SpaceInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.ScrollWebView;
import com.sun3d.culturalShanghai.view.ScrollWebView.OnScrollChangedCallback;
import com.umeng.analytics.MobclickAgent;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这个是空间的fragment
 * 
 * @author liningkang
 */
public class SpaceFragment extends Fragment implements OnClickListener,
		AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener,
		AMap.InfoWindowAdapter, AMap.OnMapClickListener, ImageLoadingListener,
		OnScrollChangedCallback, RefreshListenter {
	private View view;
	private TextView middle_tv;
	private ImageView left_iv, right_iv;
	private TextView collection_null;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private PullToRefreshListView mListView;
	private SpaceListAdapter mListAdapter;
	private String tagId = "";
	private SpacePopLeftAdapter mPopLeftAdapter;
	private SpaceHorListAdapter mHorListAdapter;
	private HorizontalListView space_hor_listview;
	private ScrollWebView web_view;
	private String venueType = "3";// 活动类型 1->距离 3.热门 4.所有活动（必选）
	private String TAG = "SpaceFragment";
	private boolean titleIsAnimating = false;
	private String currentSelectedTagType = "";
	/**
	 * 刷新用的参数 0 为第一页
	 */
	private int mPage = 0;
	private int startIndexPage = 0;
	// private ArrayList<Space_Info> mList = new ArrayList<Space_Info>();
	private ArrayList<SpaceInfo> mHor_List = new ArrayList<SpaceInfo>();
	private List<SpaceInfo> list;
	/**
	 * true 表示 listview false 表示 webview
	 */
	private boolean WebOrListView_bool = true;
	private Boolean isRefresh = false;
	/**
	 * 全部的list 没有插入广告 根据这个来插入广告
	 */
	private List<VenueDetailInfor> Total_list = new ArrayList<VenueDetailInfor>();
	private TextView shopping_areas_tv, preface_tv, sieve_tv;
	public LinearLayout sieve_ll;
	private ArrayList<Wff_VenuePopuwindow> list_area;
	private ArrayList<Wff_VenuePopuwindow> list_area_right;
	public PopupWindow pw, pw_adapter;
	private int title_heigth;
	private RelativeLayout mtitl;
	private boolean hasMeasured = false;
	/**
	 * 防止 动画的重复出现
	 */
	private boolean animation_change = true;
	private View itemView;
	private int mBgColor;
	/**
	 * 0 表示 本activity 1 表示 adapter
	 */
	private int mType = 1;
	private ListView listView_right;
	private View pop_right_view;
	private PopupWindow pop_frist_rigth;
	private ListView listView_left;
	private MapView mMapView; // 小地图，大地图
	private AMap aMap;
	private MarkerOptions markerOption;
	private double lat;
	private double lon;
	private LatLng latlng;
	private RelativeLayout titleLayout;
	private RelativeLayout map_rl;
	private TextView tv_map_location;
	private TextView mItemTitle;
	private TextView mItemAddress;
	private TextView mItemData;
	private TextView mItemDistance;
	private TextView mItemPrice;
	private TextView mItemTicket;
	private TextView map_book;
	private RelativeLayout mItem;
	private ImageView mItemImg;
	private Animation mShowAnimation;
	private Animation mHiddenAnimation;
	private Context mContext;
	Drawable drawable;
	private List<EventInfo> EventlistItem;
	/**
	 * 这个是来判断 map 和 fragment 的切换
	 */
	private boolean map_fg_change = true;
	private JSONObject json;
	private List<SpaceInfo> mList;
	private List<SpaceInfo> mBannerList;
	private List<SpaceInfo> ddlist = new ArrayList<SpaceInfo>();
	private int pos;
	private List<VenueDetailInfor> VenuelistItem;
	private boolean AnimationCloseOrOpen = true;
	private String url;
	/**
	 * 根据返回值 来更新 adapter 的选项信息
	 */
	private int num_type;

	private int lastVisibleCellIndex = 0;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SpaceFragment"); // 统计页面
		if (mMapView != null)
			mMapView.onResume();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SpaceFragment");
		if (mMapView != null)
			mMapView.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (mMapView != null)
			mMapView.onSaveInstanceState(outState);
	}

	private void initMap() {
		/**
		 * 初始化地图
		 */
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();
		}
		// 隐藏放大缩小按钮
		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setZoomControlsEnabled(false);
		// mGaoDeMapPoiSeahUtil = new GaoDeMapPoiSeahUtil(mContext, aMap);

	}

	public int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		Paint.FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	/**
	 * 返回自定义图标
	 * 
	 * @param text
	 * @return
	 */
	public View getBitMap(String color, String text, VenueDetailInfor eif) {

		int h = getFontHeight(16);

		TextView view = new TextView(mContext);
		view.setText(text);
		view.setTextSize(16);

		view.setPadding(h / 2, 0, h / 2, h + h / 2);
		view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.sh_icon_d);
		view.setTextColor(Color.WHITE);
		if (text == null || text == "") {
			view.setBackgroundResource(R.drawable.icon_map);
		} else {
			text.trim().substring(0, 1);
			if ("亲".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_qin);
			}
			if ("养".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_yang);
			}
			if ("影".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_ying);
			}
			if ("D".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_d);
			}
			if ("演".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_yan);
			}
			if ("充".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_chong);
			}
			if ("友".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_you);
			}
			if ("食".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_shi);
			}
			if ("旅".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_lv);
			}
			if ("赛".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_sai);
			}
			if ("展".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_zhan);
			}
			if ("运".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_yun);
			}
			if ("聚".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_ju);
			}
		}
		return view;
	}

	/**
	 * 這個是添加標示的方法
	 * 
	 * @param listItem
	 */
	public void addActivityMakrt(List<VenueDetailInfor> listItem) {
		aMap.clear();
		setUpMap();
		int i = 0;
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
		aMap.setOnInfoWindowClickListener(this);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnMapClickListener(this);
		aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层

		if (listItem != null && listItem.size() != 0) {
			for (VenueDetailInfor eif : listItem) {
				try {
					double lat = Double.parseDouble(eif.getVenueLat());
					double lon = Double.parseDouble(eif.getVenueLon());
					latlng = new LatLng(lat, lon);

					if (i <= 100) {
						if (Double.valueOf(eif.getDistance()) <= 50) {
							aMap.addMarker(
									new MarkerOptions()
											.position(latlng)
											.title(eif.getVenueName() + ","
													+ eif.getVenueId())
											.snippet(eif.getVenueAddress())
											.period(50)
											.icon(BitmapDescriptorFactory
													.fromView(getBitMap("", "",
															eif)))
											.draggable(true)).setObject(eif);
						}
					}
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 這個是添加標示的方法
	 * 
	 * @param listItem
	 */
	public void addActivityMakrtReturn(List<VenueDetailInfor> listItem) {
		aMap.clear();
		int i = 0;
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
		// aMap.setOnInfoWindowClickListener(this);
		// aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
		// aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		// aMap.setOnMapClickListener(this);
		// aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
		if (listItem != null && listItem.size() != 0) {

			for (VenueDetailInfor eif : listItem) {
				try {
					double lat = Double.parseDouble(eif.getVenueLat());
					double lon = Double.parseDouble(eif.getVenueLon());
					LatLng ll = new LatLng(lat, lon);

					if (i <= 100) {
						if (Double.valueOf(eif.getDistance()) <= 50) {
							aMap.addMarker(

									new MarkerOptions()
											.position(ll)
											.title(eif.getVenueName() + ","
													+ eif.getVenueId())
											.snippet(eif.getVenueAddress())
											.period(50)
											.icon(BitmapDescriptorFactory
													.fromView(getBitMap("", "",
															eif)))
											.draggable(true))

							.setObject(eif);
						}
					}
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		if (hidden) {
			mMapView.setVisibility(View.GONE);
			// warn_mapView.setVisibility(View.GONE);
		} else {
			mMapView.setVisibility(View.VISIBLE);
			// warn_mapView.setVisibility(View.VISIBLE);
		}
	}

	private void getLocal(double lat, double lon) {
		latlng = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	}

	/**
	 * 地图设置
	 */
	private void setUpMap() {
		if (AppConfigUtil.LocalLocation.Location_latitude != ""
				&& AppConfigUtil.LocalLocation.Location_longitude != "") {
			lat = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_latitude);
			lon = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_longitude);
			getLocal(lat, lon);
		} else {
			lat = Double.valueOf(MyApplication.Location_latitude);
			lon = Double.valueOf(MyApplication.Location_longitude);
			getLocal(lat, lon);
		}
		//
		// aMap.setLocationSource(new MyLocationSource(this, aMap));// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
		// 显示标记
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);
		aMap.addMarker(markerOption);
	}

	private void initViewMap(View view) {
		// 标题栏
		titleLayout = (RelativeLayout) view.findViewById(R.id.nearby_title);
		drawable = getResources()
				.getDrawable(R.drawable.sh_activity_title_pull);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		map_rl = (RelativeLayout) view.findViewById(R.id.map);
		tv_map_location = (TextView) view.findViewById(R.id.map_location);
		tv_map_location.setVisibility(View.VISIBLE);
		mItem = (RelativeLayout) view.findViewById(R.id.dialog_activity_map);
		mItemImg = (ImageView) view.findViewById(R.id.dialog_activity_map_img);
		mItemTitle = (TextView) view
				.findViewById(R.id.dialog_activity_map_title);
		mItemAddress = (TextView) view
				.findViewById(R.id.dialog_activity_map_address);
		mItemData = (TextView) view.findViewById(R.id.dialog_activity_map_data);
		mItemDistance = (TextView) view.findViewById(R.id.mItemDistance);
		mItemTicket = (TextView) view
				.findViewById(R.id.dialog_activity_map_ticket);
		mItemPrice = (TextView) view.findViewById(R.id.mItemPrice);
		map_book = (TextView) view.findViewById(R.id.map_book);

		mShowAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.dialog_bottom_up);
		mHiddenAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.dialog_bottom_down);
		mMapView = (MapView) view.findViewById(R.id.activity_amap);
		tv_map_location.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.space_fragment, null);
		mContext = getActivity();
		initViewMap(view);
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
		}
		initView(view);
		initMap();
		getHorData();
		getDataArea();

		return view;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 这是场馆的信息
			case 1:
				collection_null.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				// 这里是成功后 数据的加载
				if (isRefresh) {
					// 刷新的时候 来判断
					list.clear();
				}
				for (int i = 0; i < ddlist.size(); i++) {
					// 刚开始的时候添加头部的三个广告位
					if (i == 0 && mPage == 0) {
						mListAdapter.setBannerList(mBannerList);
					}
					list.add(ddlist.get(i));
				}
				Log.i(TAG, "看看 所有的数据  ==    " + list.size() + "   size==  "
						+ ddlist.size());
				mListAdapter.setList(list);
				mLoadingHandler.stopLoading();
				mListView.onRefreshComplete();
				getNumData(10 * mPage);
				break;
			// 服务器请求失败
			case 2:
				sieve_ll.setVisibility(View.VISIBLE);
				collection_null.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				mLoadingHandler.stopLoading();
				break;
			// 区域的信息
			case 3:
				mPopLeftAdapter = new SpacePopLeftAdapter(getActivity(),
						list_area);
				new SpacePopRightAdapter(getActivity(), list_area);
				break;
			// 这是每个场馆的活动室数量和活动数量
			case 4:
				mListAdapter.setList(list);
				mLoadingHandler.stopLoading();
				mListView.onRefreshComplete();
				break;
			case 5:
				mListView.onRefreshComplete();
				mLoadingHandler.stopLoading();
				Toast.makeText(mContext, "已经全部加载完毕", 1000).show();
				break;
			default:
				break;
			}
		};
	};
	Handler handler_img = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mHor_List.clear();
				mBannerList.clear();
				for (int i = 0; i < mList.size(); i++) {
					if (mList.get(i).getAdvertType().equals("A")) {
						mBannerList.add(mList.get(i));
					}
					if (mList.get(i).getAdvertType().equals("B")) {
						// 设置 横向listview默认选项的 变大效果
						if (mHor_List.size() == 0) {
							mList.get(i).setIndex(false);
						} else {
							mList.get(i).setIndex(true);
						}
						mHor_List.add(mList.get(i));
					}
				}
				if (mHor_List.size() > 0) {
					currentSelectedTagType = mHor_List.get(0).getAdvertUrl();
				}

				mHorListAdapter = new SpaceHorListAdapter(getActivity(),
						mHor_List);
				space_hor_listview.setAdapter(mHorListAdapter);
				getData(0);
				break;

			default:
				break;
			}
		};
	};

	private void initView(View view) {
		mBannerList = new ArrayList<SpaceInfo>();
		mList = new ArrayList<SpaceInfo>();
		pop_right_view = View.inflate(getActivity(),
				R.layout.windows_venue_popwindow_event_area, null);
		EventlistItem = new ArrayList<EventInfo>();
		VenuelistItem = new ArrayList<VenueDetailInfor>();
		web_view = (ScrollWebView) view.findViewById(R.id.webview);

		//
		//
		// web_view.getSettings().setUseWideViewPort(true);
		//
		// web_view.getSettings().setJavaScriptEnabled(true);
		// web_view.getSettings().setSupportZoom(true);
		// web_view.getSettings().setBuiltInZoomControls(true);
		// web_view.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// web_view.getSettings().setDefaultFontSize(10);

		mtitl = (RelativeLayout) view.findViewById(R.id.space_title);
		sieve_ll = (LinearLayout) view.findViewById(R.id.Sieve);
		shopping_areas_tv = (TextView) view.findViewById(R.id.shopping_areas);
		preface_tv = (TextView) view.findViewById(R.id.preface);
		sieve_tv = (TextView) view.findViewById(R.id.sieve);
		middle_tv = (TextView) view.findViewById(R.id.middle_tv);
		left_iv = (ImageView) view.findViewById(R.id.left_iv);
		left_iv.setImageResource(R.drawable.icon_maptop);
		left_iv.setOnClickListener(this);
		right_iv = (ImageView) view.findViewById(R.id.right_iv);
		right_iv.setImageResource(R.drawable.sh_activity_search_icon);
		right_iv.setOnClickListener(this);
		preface_tv.setTypeface(MyApplication.GetTypeFace());
		sieve_tv.setTypeface(MyApplication.GetTypeFace());
		shopping_areas_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("文化空间");
		collection_null = (TextView) view.findViewById(R.id.collection_null);
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		mLoadingHandler.setOnRefreshListenter(this);
		list = new ArrayList<SpaceInfo>();
		mListView = (PullToRefreshListView) view.findViewById(R.id.main_list);
		space_hor_listview = (HorizontalListView) view
				.findViewById(R.id.space_hor_listview);
		mListView.setOnRefreshListener(onrefrensh);

		ViewTreeObserver vto = mtitl.getViewTreeObserver();

		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (hasMeasured == false) {

					title_heigth = mtitl.getMeasuredHeight();
					mtitl.getMeasuredWidth();
					// 获取到宽度和高度后，可用于计算
					hasMeasured = true;

				}
				return true;
			}
		});

		mListAdapter = new SpaceListAdapter(getActivity(), true, this);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(itemclick);
		web_view.setOnScrollChangedCallback(this);
		space_hor_listview.setOnItemClickListener(horItemClick);
		mListView.setOnScrollListener(new My_ScrollListener());
		shopping_areas_tv.setOnClickListener(this);
		preface_tv.setOnClickListener(this);
		sieve_tv.setOnClickListener(this);
	}

	/**
	 * 获取广告位 和 横向选项框
	 */
	private void getHorData() {
		json = new JSONObject();
		try {
			json.put("advertPostion", 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MyHttpRequest.onStartHttpPostJSON(
				HttpUrlList.HomeFragment.PAGEADVERTRECOMMEND, json,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_OK == statusCode) {
							try {
								JSONObject json = new JSONObject(resultStr);
								String status = json.optString("status", "");
								Log.i(TAG, "getHorData==   " + status);
								if (status.equals("0")) {
									mLoadingHandler.isNotContent();
									return;
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

							try {
								mList = JsonUtil.getSpaceList(resultStr);
								handler_img.sendEmptyMessage(1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							handler_img.sendEmptyMessage(2);
						}
					}

				});

	}

	/**
	 * 下拉刷新
	 * 
	 * @param i
	 */
	private void onResh(int i) {
		if (NetWorkUtil.isConnected()) {
			if (WebOrListView_bool) {
				web_view.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				/*
				 * 这里还要分 有没有顶部广告的情况 有广告 sieve_ll.setVisibility(View.VISIBLE);
				 * 没有广告 则 mListAdapter.setGone()
				 */
				mListAdapter.setVisible();
				isRefresh = true;
				getData(0);
			} else {
				sieve_ll.setVisibility(View.GONE);
				web_view.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				mListAdapter.setGone();
			}
		} else {
			mListView.onRefreshComplete();
			mLoadingHandler.isNotNetConnection();
		}
	}

	/**
	 * 设置 webview 的设置 以及webview 的链接
	 */
	private void setWebUrl(String url) {
		WebSettings settings = web_view.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);// 必须使用
		settings.setDomStorageEnabled(true);//
		settings.setGeolocationEnabled(true);
		String dir = getActivity()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		settings.setGeolocationDatabasePath(dir);
		
		String ua = settings.getUserAgentString();
		settings.setUserAgentString(ua + ";  wenhuayun/"
				+ MyApplication.getVersionName(getActivity())
				+ " platform/android/");
		String ua1 = settings.getUserAgentString();
		web_view.addJavascriptInterface(new JavascriptInterface_new(
				getActivity()), "injs");// 必须使用
		WebChromeClient wvcc = new WebChromeClient() {
			public void onGeolocationPermissionsShowPrompt(String origin,
					GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		};
		web_view.setWebChromeClient(wvcc);
		web_view.setWebViewClient(wvc);
		web_view.loadUrl(url);
	}

	WebViewClient wvc = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			parseScheme(url);
			return true;
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			mLoadingHandler.stopLoading();
			super.onPageFinished(view, url);
		}
	};

	/**
	 * 来响应 webview 中的点击事件
	 * 
	 * @param url
	 * @return
	 */
	public boolean parseScheme(String url) {
		Log.i(TAG, "跳转的  url  ==  " + url);
		if (url.contains("com.wenhuayun.app")) {
			Intent intent = new Intent();
			if (url.contains("://activitydetail")) {
				// 跳转到活动详情
				intent.setClass(getActivity(), ActivityDetailActivity.class);
				intent.putExtra("eventId", url.split("=")[1]);
				this.startActivity(intent);
			} else if (url.contains("://venuedetail")) {
				Log.i(TAG, "跳入场馆");
				// 跳转到场馆详情
				intent.setClass(getActivity(), VenueDetailActivity.class);
				intent.putExtra("venueId", url.split("=")[1]);
				this.startActivity(intent);
			} else if (url.contains("://orderlist")) {
				// 我的订单页面
				intent.setClass(getActivity(), MyOrderActivity.class);
				this.startActivity(intent);
			} else if (url.contains("://usercenter")) {
				// 我的页面
				// MyApplication.change_ho_listviw = 4;
				// intent.setClass(this, MainFragmentActivity.class);
				// this.startActivity(intent);
			} else {
				this.url = url;
				// web_view.setWebChromeClient(wvcc);
				web_view.loadUrl(url);
			}
		} else {
			this.url = url;
			// web_view.setWebChromeClient(wvcc);
			web_view.loadUrl(url);
		}
		return true;
	}

	/**
	 * 上拉加载更多
	 * 
	 * @param i
	 */
	private void onAddmoreData(int i) {
		if (NetWorkUtil.isConnected()) {
			isRefresh = false;
			getData(i);
		} else {
			mListView.onRefreshComplete();
			mLoadingHandler.isNotNetConnection();
		}
	}

	OnRefreshListener2 onrefrensh = new OnRefreshListener2<View>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<View> refreshView) {
			// TODO Auto-generated method stub
			// 下拉刷新

			mPage = 0;
			onResh(mPage);

		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<View> refreshView) {
			mPage = mPage + 1;
			startIndexPage = HttpUrlList.HTTP_NUM_10 + startIndexPage;
			Log.i(TAG, "  加载  ==  " + startIndexPage);
			onAddmoreData(startIndexPage);

		}
	};

	private void getData(int page) {
		// 加载更多 不用 loading 页面
		if (isRefresh) {
			mLoadingHandler.startLoading();
		}

		MyApplication.getInstance().setVenueType(venueType);
		Map<String, String> mParams = new HashMap<String, String>();
		if (lat > 0 && lon > 0) {
			mParams.put(HttpUrlList.HTTP_LAT, lat + "");
			mParams.put(HttpUrlList.HTTP_LON, lon + "");
		} else {
			mParams.put(HttpUrlList.HTTP_LAT, 31.280842 + "");
			mParams.put(HttpUrlList.HTTP_LON, 121.433594 + "");
		}
		// mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, "10");
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		mParams.put("appType", venueType);
		mParams.put("activityType", tagId);
		mParams.put("venueType", currentSelectedTagType);
		// // 分类
		// if (MyApplication.venueType_classication != ""
		// && MyApplication.venueType_classication != null) {
		// mParams.put("venueType", MyApplication.venueType_classication);
		//
		//
		// }
		// 商圈
		if (MyApplication.venueMood != "" && MyApplication.venueMood != null) {
			mParams.put("venueMood", MyApplication.venueMood);
		}
		// 区域
		if (MyApplication.venueArea != "" && MyApplication.venueArea != null) {
			mParams.put("venueArea", MyApplication.venueArea);
		}
		// 是否可预订 2 可预订 1 全部
		if (MyApplication.venueIsReserve != ""
				&& MyApplication.venueIsReserve != null) {
			mParams.put("venueIsReserve", MyApplication.venueIsReserve);
		}
		// 排序 1 浏览数 2距离
		if (MyApplication.sortType != "" && MyApplication.sortType != null) {
			mParams.put("sortType", MyApplication.sortType);
		}
		// if (MyApplication.venueMoodName != ""
		// && MyApplication.venueMoodName != null) {
		// region.setText(MyApplication.venueMoodName);
		// }
		// if (MyApplication.venueType_classication_Name != ""
		// && MyApplication.venueType_classication_Name != null) {
		// classification.setText(MyApplication.venueType_classication_Name);
		// }
		// if (MyApplication.sortType_Name != ""
		// && MyApplication.sortType_Name != null) {
		// sort.setText(MyApplication.sortType_Name);
		// }
		// if (MyApplication.venueIsReserve_Name != ""
		// && MyApplication.venueIsReserve_Name != null) {
		// status.setText(MyApplication.venueIsReserve_Name);
		// }
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		Log.i(TAG, "  page  =  " + page);
		MyApplication.total_num(HttpUrlList.Venue.WFF_APPVENUELIST,
				"appVenueList", "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.WFF_APPVENUELIST,
				mParams, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {

							try {
								JSONObject json = new JSONObject(resultStr);
								String status = json.optString("status", "");
								if (status.equals("0")) {
									mLoadingHandler.isNotContent();
									return;
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

							mListView.setVisibility(View.VISIBLE);
							collection_null.setVisibility(View.GONE);
							ddlist = JsonUtil.getSpaceListInfo(resultStr);
							Log.i(TAG,
									"  page  =  " + mPage + "加載跟多 數據  ==  "
											+ resultStr + "  ddlist==  "
											+ ddlist.size());
							if (ddlist != null) {
								if (ddlist.size() > 0) {
									handler.sendEmptyMessage(1);
								} else {
									if (isRefresh) {
										handler.sendEmptyMessage(2);
									} else {
										handler.sendEmptyMessage(5);
									}

								}

							} else {
								mLoadingHandler.showErrorText("数据为空");
							}

						} else {
							mLoadingHandler.showErrorText(resultStr);
							ToastUtil.showToast("数据请求失败:" + resultStr);
						}

					}
				});
	}

	/**
	 * 这是 第二个 选项的 点击事件
	 */
	OnItemClickListener SecondItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			num_type = 2;
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) parent
					.getItemAtPosition(pos);
			preface_tv.setText(wvp.getTagName());
			// clear();
			MyApplication.sortType = wvp.getTagId();
			MyApplication.sortType_Name = wvp.getTagName();
			preface_tv.setText(MyApplication.sortType_Name);
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
			}
			mListAdapter.setText(num_type, wvp.getTagName());
			onResh(0);
		}
	};
	/**
	 * 这是 第三个 选项的 点击事件
	 */
	OnItemClickListener ThreeItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			num_type = 3;
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) parent
					.getItemAtPosition(pos);
			// clear();
			sieve_tv.setText(wvp.getTagName());
			MyApplication.venueIsReserve = wvp.getTagId();
			MyApplication.venueIsReserve_Name = wvp.getTagName();
			sieve_tv.setText(MyApplication.venueIsReserve_Name);
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
			}
			mListAdapter.setText(num_type, wvp.getTagName());
			onResh(0);
		}
	};
	/**
	 * 这是 listview 的点击事件
	 */
	OnItemClickListener itemclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			SpaceInfo vdi = (SpaceInfo) parent.getItemAtPosition(pos);
			// 传展馆ID 到展馆详情页面
			Intent intent = new Intent(getActivity(), VenueDetailActivity.class);
			intent.putExtra("venueId", vdi.getVenueId());
			startActivity(intent);
		}
	};
	/**
	 * 这是 横向 listview 的点击事件
	 */
	OnItemClickListener horItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			// 筛选条隐藏
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
			}
			// 这里暂时 用这个条件来判断 是webview 还是 listview
			SpaceInfo info = (SpaceInfo) parent.getItemAtPosition(pos);
			currentSelectedTagType = info.getAdvertUrl();
			for (int i = 0; i < mHor_List.size(); i++) {
				// 这里判断哪个被选中
				if (pos == i) {
					mHor_List.get(pos).setIndex(false);
				} else {
					mHor_List.get(i).setIndex(true);
				}
			}
			mHorListAdapter.notifyDataSetChanged();
			if (info.getAdvertLink() == 0) {
				WebOrListView_bool = true;
				// selectImg(info.getAdvertLinkType(), info.getAdvertUrl());
			} else {
				setWebUrl(info.getAdvertUrl());
				WebOrListView_bool = false;
			}
			mPage = 0;
			onResh(0);

		}
	};

	/**
	 * 区域
	 */
	OnItemClickListener leftItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			num_type = 1;
			if (pos == 0) {
				MyApplication.venueArea = "";
				MyApplication.venueMood = "";
				MyApplication.venueMoodName = "全上海";
				isRefresh = true;
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
				if (mType == 0) {
					sieve_ll.setVisibility(View.VISIBLE);
				} else {
					sieve_ll.setVisibility(View.GONE);
					mListAdapter.setText(num_type, "全上海");
					mListAdapter.setVisible();
				}
				onResh(0);
				return;
			}
			view = v;
			itemBackChanged(v);

			Wff_VenuePopuwindow wvpw = (Wff_VenuePopuwindow) parent
					.getItemAtPosition(pos);
			MyApplication.venueArea = wvpw.getDictCode();
			MyApplication.venueDicName = wvpw.getDictName();

			list_area_right = new ArrayList<Wff_VenuePopuwindow>();
			list_area_right.add(new Wff_VenuePopuwindow("", "全部"
					+ wvpw.getDictName()));
			for (int i = 0; i < wvpw.getDictList().length(); i++) {
				try {
					JSONObject json = wvpw.getDictList().getJSONObject(i);
					list_area_right.add(new Wff_VenuePopuwindow(json
							.optString("id"), json.optString("name")));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			SpacePopRightAdapter space_pop_right_adapter = new SpacePopRightAdapter(
					getActivity(), list_area_right);
			listView_right.setAdapter(space_pop_right_adapter);
			space_pop_right_adapter.notifyDataSetChanged();
		}
	};
	OnItemClickListener rightItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			num_type = 1;
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) parent
					.getItemAtPosition(pos);
			shopping_areas_tv.setText(wvp.getTagName());
			if (pos == 0) {
				MyApplication.venueMood = "";
				MyApplication.venueMoodName = wvp.getTagName();
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
				if (pop_frist_rigth != null) {
					pop_frist_rigth.dismiss();
				}
				if (mType == 0) {
					sieve_ll.setVisibility(View.VISIBLE);
				} else {
					sieve_ll.setVisibility(View.GONE);
					mListAdapter.setText(num_type, wvp.getTagName());
					mListAdapter.setVisible();
				}
				onResh(0);
				return;
			}

			MyApplication.venueMood = wvp.getTagId();
			MyApplication.venueMoodName = wvp.getTagName();
			if (pw != null && pw.isShowing()) {
				pw.dismiss();
			}
			if (pop_frist_rigth != null) {
				pop_frist_rigth.dismiss();
			}
			if (mType == 0) {
				sieve_ll.setVisibility(View.VISIBLE);
			} else {
				sieve_ll.setVisibility(View.GONE);
				mListAdapter.setText(num_type, wvp.getTagName());
				mListAdapter.setVisible();
			}
			onResh(0);
		}
	};

	/**
	 * 改变顶部 title的大小 以及 popwindow 的显示
	 */
	class My_ScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int arg2,
				int arg3) {
			if (titleIsAnimating == false) {
				if (firstVisibleItem > lastVisibleCellIndex) {
					if (animation_change) {
						// sieve_ll.setVisibility(View.VISIBLE);
						// mListAdapter.setGone();
						startTitleAnimation(1);

					}
				} else if (firstVisibleItem < lastVisibleCellIndex) {
					if (animation_change == false) {
						// sieve_ll.setVisibility(View.GONE);
						// mListAdapter.setVisible();
						startTitleAnimation(0);
					}
				}
			}
			lastVisibleCellIndex = firstVisibleItem;

			if ((firstVisibleItem == 0 && (mBannerList == null || mBannerList
					.size() == 0))
					|| (firstVisibleItem < 1 && mBannerList != null && mBannerList
							.size() > 0)) {// list上显示 cell中隐藏
				if (sieve_ll.getVisibility() != View.GONE) {
					sieve_ll.setVisibility(View.GONE);
					mListAdapter.setVisible();
				}
			} else {
				if (sieve_ll.getVisibility() != View.VISIBLE) {
					sieve_ll.setVisibility(View.VISIBLE);
					mListAdapter.setGone();
				}

			}

		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			if (arg1 == SCROLL_STATE_TOUCH_SCROLL) {
				if (pw != null && pw.isShowing()) {
					pw.dismiss();
				}
				if (mType == 1) {
					mListAdapter.setVisible();
				} else {
					sieve_ll.setVisibility(View.VISIBLE);
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shopping_areas:
			showPopuwindow(1, 0);
			break;
		case R.id.preface:
			showPopuwindow(2, 0);
			break;
		case R.id.sieve:
			showPopuwindow(3, 0);
			break;
		case R.id.arrow_up:

			break;
		case R.id.left_iv:
			if (map_fg_change) {
				map_fg_change = false;
				middle_tv.setText("附近");
				left_iv.setImageResource(R.drawable.icon_list);
				space_hor_listview.setVisibility(View.GONE);
				setVenueListData(mPage);
				map_rl.setVisibility(View.VISIBLE);
				if (WebOrListView_bool) {
					mListView.setVisibility(View.GONE);
				} else {
					web_view.setVisibility(View.GONE);
				}
			} else {
				map_fg_change = true;
				middle_tv.setText("文化空间");
				left_iv.setImageResource(R.drawable.icon_maptop);
				space_hor_listview.setVisibility(View.VISIBLE);
				map_rl.setVisibility(View.GONE);
				if (WebOrListView_bool) {
					mListView.setVisibility(View.VISIBLE);
				} else {
					web_view.setVisibility(View.VISIBLE);
				}
			}
			break;
		case R.id.right_iv:
			Intent i = new Intent();
			i.setClass(getActivity(), SearchActivity.class);
			i.putExtra("type", "venue");
			getActivity().startActivity(i);

			break;
		default:
			break;
		}
		titleIsAnimating = false;

	}

	public void showPopuwindow(int num, final int type) {
		View mView;
		LinearLayout close_ll;
		ListView listView;
		ArrayList<Wff_VenuePopuwindow> list;
		SpacePopThreeAdapter space_adapter;
		int height;
		TextView tv;
		if (pw != null && pw.isShowing()) {
			pw.dismiss();
		}
		switch (num) {
		// 这是区域 全上海 XXX 什么的
		case 1:
			mType = type;
			if (mType == 1) {
				mListAdapter.setGone();
			} else {
				sieve_ll.setVisibility(View.GONE);
			}
			mView = View.inflate(getActivity(),
					R.layout.home_popuwindow_shopping, null);
			listView_left = (ListView) mView.findViewById(R.id.listView_left);
			listView_right = (ListView) mView.findViewById(R.id.listView_right);

			listView_left.setAdapter(mPopLeftAdapter);
			listView_left.setOnItemClickListener(leftItem);
			listView_right.setOnItemClickListener(rightItem);

			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			close_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					if (mType == 0) {
						sieve_ll.setVisibility(View.VISIBLE);
					} else {
						mListAdapter.setVisible();
					}

				}
			});
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 2,
					MyApplication.getWindowHeight() / 4);
			// 这里要根据判断 popwindow 的显示位置
			if (type == 0) {
				pw.showAsDropDown(sieve_ll,
						MyApplication.getWindowWidth() / 28, 0);
			} else {
				pw.showAsDropDown(mListView.getRefreshableView().getChildAt(0),
						MyApplication.getWindowWidth() / 4,
						MyApplication.getWindowHeight() / 30);
			}

			break;
		// 这是智能排序
		case 2:
			mType = type;
			if (type == 1) {
				mListAdapter.setGone();
			} else {
				sieve_ll.setVisibility(View.GONE);
			}
			mView = View.inflate(getActivity(), R.layout.home_popuwindow_sieve,
					null);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			close_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					if (mType == 0) {
						sieve_ll.setVisibility(View.VISIBLE);
					} else {
						mListAdapter.setVisible();
					}

				}
			});
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("1", "热门程度"));
			list.add(new Wff_VenuePopuwindow("2", "离我最近"));
			space_adapter = new SpacePopThreeAdapter(list, getActivity());
			listView.setAdapter(space_adapter);
			listView.setOnItemClickListener(SecondItemClick);
			height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.9);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			if (type == 0) {
				pw.showAsDropDown(sieve_ll,
						MyApplication.getWindowWidth() / 10, 0);
			} else {
				pw.showAsDropDown(mListView.getRefreshableView().getChildAt(0),
						MyApplication.getWindowWidth() / 3,
						MyApplication.getWindowHeight() / 30);
			}

			break;
		// 这是筛选
		case 3:
			mType = type;
			if (type == 1) {
				mListAdapter.setGone();
			} else {
				sieve_ll.setVisibility(View.GONE);
			}

			mView = View.inflate(getActivity(),
					R.layout.home_popuwindow_preface, null);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			close_ll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					if (mType == 0) {
						sieve_ll.setVisibility(View.VISIBLE);
					} else {
						mListAdapter.setVisible();
					}

				}
			});
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("2", "可预订"));
			list.add(new Wff_VenuePopuwindow("1", "全部"));
			space_adapter = new SpacePopThreeAdapter(list, getActivity());
			listView.setAdapter(space_adapter);
			listView.setOnItemClickListener(ThreeItemClick);
			height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.9);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			if (type == 0) {
				pw.showAsDropDown(sieve_ll,
						MyApplication.getWindowWidth() / 10, 0);
			} else {
				pw.showAsDropDown(mListView.getRefreshableView().getChildAt(0),
						MyApplication.getWindowWidth() / 3,
						MyApplication.getWindowHeight() / 30);
			}

			break;
		}

	}

	/**
	 * 获取地址的信息
	 */
	private void getDataArea() {
		Map<String, String> mParams = new HashMap<String, String>();
		MyHttpRequest.onStartHttpGET(HttpUrlList.Venue.WFF_GETALLAREA, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								list_area = new ArrayList<Wff_VenuePopuwindow>();

								JSONObject json = new JSONObject(resultStr);
								String status = json.optString("status");
								Log.i(TAG, "getDataArea==   " + status);
								if (status.equals("200")) {
									JSONArray json_arr = json
											.optJSONArray("data");
									list_area.add(new Wff_VenuePopuwindow("",
											"全上海", "", new JSONArray()));
									for (int i = 0; i < json_arr.length(); i++) {
										JSONObject json_new = json_arr
												.getJSONObject(i);
										String dictId = json_new
												.optString("dictId");
										String dictName = json_new
												.optString("dictName");
										String dictCode = json_new
												.optString("dictCode");
										JSONArray dictList = json_new
												.optJSONArray("dictList");
										list_area.add(new Wff_VenuePopuwindow(
												dictId, dictName, dictCode,
												dictList));

									}
									handler.sendEmptyMessage(3);
								} else {
									mLoadingHandler.isNotContent();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

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

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (mItem.isShown()) {
			mItem.startAnimation(mHiddenAnimation);
		}
		mItem.setVisibility(View.GONE);
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// initData();

		addMyActivity(arg0);
		return true;
	}

	/**
	 * 最新活动自定义消息框
	 * 
	 * @param marker
	 */
	private void addMyActivity(Marker marker) {
		addActivityMakrtReturn(VenuelistItem);
		final VenueDetailInfor eif = (VenueDetailInfor) marker.getObject();
		// 显示标记
		marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.icon_mapon)));
		getLocal(Double.valueOf(eif.getVenueLat()),
				Double.valueOf(eif.getVenueLon()));
		if (!mItem.isShown()) {
			mItem.startAnimation(mShowAnimation);
		}
		double lat = Double.parseDouble(eif.getVenueLat());
		double lon = Double.parseDouble(eif.getVenueLon());
		LatLng ll1 = new LatLng(lat, lon);
		MarkerOptions markerOption1 = new MarkerOptions();
		markerOption1.position(ll1);
		markerOption1.draggable(true);
		markerOption1.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.icon_mapon)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption1.setFlat(true);
		aMap.addMarker(markerOption1);
		// view.setBackgroundResource(R.drawable.icon_mapon);
		mItem.setVisibility(View.VISIBLE);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlMiddle(eif.getVenueIconUrl()),
						mItemImg, this);
		mItemTitle.setText(eif.getVenueName());
		mItemAddress.setText(eif.getVenueAddress());
		if (eif.getVenueOpenTime() == null || eif.getVenuEndTime() == null) {
			// String start_time = eif.getVenueOpenTime().replaceAll("-", ".");
			// start_time = start_time.substring(5, 10);
			// mItemData.setText(start_time);
			mItemData.setVisibility(View.GONE);
		} else {
			String start_time = eif.getVenueOpenTime().replaceAll("-", ".");
			start_time = start_time.substring(5, 10);
			String end_time = eif.getVenuEndTime().replaceAll("-", ".");
			end_time = end_time.substring(5, 10);
			mItemData.setText(start_time + "-" + end_time);
		}
		// if (eif.getVenuePrice().equals("0")) {
		// mItemPrice.setText("免费");
		// } else {
		// mItemPrice.setText(eif.getVenuePrice() + "元");
		// }
		Float distance = Float.valueOf(eif.getDistance().substring(0, 3));

		if (distance > 1.0) {
			mItemDistance.setText(distance + "KM");
		} else {
			mItemDistance.setText(distance * 1000 + "M");
		}
		// if (eif.get != null
		// && eif.getActivityAbleCount().length() > 0) {
		// if (eif.getActivityAbleCount().equals("0")) {
		// mItemTicket.setVisibility(View.GONE);
		// map_book.setVisibility(View.GONE);
		// } else {
		// mItemTicket.setVisibility(View.GONE);
		// mItemTicket.setText("剩余" + eif.getActivityAbleCount() + "张票");
		// map_book.setVisibility(View.GONE);
		// // mItemTicket.setText("余票：" + eif.getActivityAbleCount());
		// }
		// } else {
		mItemTicket.setText("");
		map_book.setVisibility(View.GONE);
		// }

		// map_book.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put(HttpUrlList.EventUrl.ACTIVITY_ID, eif.getVenueId());
		//
		// params.put(HttpUrlList.HTTP_USER_ID,
		// MyApplication.loginUserInfor.getUserId());
		// MyHttpRequest.onHttpPostParams(
		// HttpUrlList.EventUrl.WFF_HTTP_EVENT_URL, params,
		// new HttpRequestCallback() {
		//
		// @Override
		// public void onPostExecute(int statusCode,
		// String resultStr) {
		// // TODO Auto-generated method stub
		// if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
		// if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
		// .getJsonStatus(resultStr)) {
		// eventInfo = JsonUtil
		// .getEventInfo(resultStr);
		// Intent intent = new Intent(
		// getActivity(),
		// EventReserveActivity.class);
		// intent.putExtra("EventInfo", eventInfo);
		// startActivity(intent);
		// } else {
		//
		// }
		// }
		// }
		// });
		// }
		// });
		//
		mItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mContext != null && eif.getVenueId() != null) {
					Intent intent = new Intent(mContext,
							VenueDetailActivity.class);
					intent.putExtra("venueId", eif.getVenueId());
					mContext.startActivity(intent);
				}
			}
		});
	}

	/**
	 * 0 表示 放大title 1表示 缩小title
	 * 
	 * @param animation_type
	 */
	public void startTitleAnimation(final int animation_type) {
		ValueAnimator animator_title;
		if (animation_type == 0) {
			animator_title = ValueAnimator.ofInt(0, title_heigth);
		} else {
			animator_title = ValueAnimator.ofInt(title_heigth, 0);
		}

		animator_title.setDuration(500);
		animator_title
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator valueAnimator) {
						android.widget.LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) mtitl
								.getLayoutParams();
						int margin_height = (Integer) valueAnimator
								.getAnimatedValue();
						ll.height = margin_height;
						mtitl.setLayoutParams(ll);
					}
				});
		animator_title.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				if (animation_type == 0) {
					animation_change = true;
				} else {
					animation_change = false;
				}
				titleIsAnimating = true;
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				titleIsAnimating = false;
			}

			@Override
			public void onAnimationCancel(Animator animator) {
				titleIsAnimating = false;
			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		animator_title.start();
	}

	@Override
	public void onScroll(int dx, int dy) {

		if (titleIsAnimating == false) {
			if (dy > 0) {
				if (AnimationCloseOrOpen) {
					startTitleAnimation(1);
					AnimationCloseOrOpen = false;
				}
			} else {
				if (AnimationCloseOrOpen) {

				} else {
					startTitleAnimation(0);
					AnimationCloseOrOpen = true;
				}

			}
		}
	}

	private void getNumData(int position) {
		Log.i(TAG, "查询==  " + position);
		pos = position;
		HashMap<String, String> _param = new HashMap<String, String>();
		if (position < list.size()) {
			_param.put("venueId", list.get(position).getVenueId());
		}
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Venue.URL_GET_VENUE_NUMOFROOMS, _param,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.i(TAG, "pos  ==  " + pos + "  str==  " + resultStr);
						try {
							JSONObject json = new JSONObject(resultStr);
							JSONObject jo = json.getJSONObject("data");
							list.get(pos).setActCount(jo.optInt("actCount", 0));
							list.get(pos).setRoomCount(
									jo.optInt("roomCount", 0));
							pos++;
							if (pos == list.size()) {
								// mListAdapter.notifyDataSetChanged();
								handler.sendEmptyMessage(4);
								return;
							} else {
								getNumData(pos);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

	}

	/**
	 * 场馆数据处理
	 */
	private void setVenueListData(int page) {

		Map<String, String> mParams = new HashMap<String, String>();
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("")) {
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}

		// mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, "10");
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
								addActivityMakrt(VenuelistItem);
							} else {

							}

						} else {
							mLoadingHandler.showErrorText(resultStr);
						}

					}
				});
	}

	@Override
	public void onLoadingRefresh() {
		onResh(0);
	}
}
