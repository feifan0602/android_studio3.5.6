package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.NaviRoute.NaviRoute;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil.OnLocationListener;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.MyPoiOverlay;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

public class NearbyParkingActivity extends Activity implements OnClickListener,
		OnMarkerClickListener, OnPoiSearchListener, InfoWindowAdapter {
	private Context mContext;
	private MapView mMapView;
	private AMap aMap;
	private LoadingDialog mLoadingDialog;
	private PoiSearch.Query query;// Poi查询条件类
	private int currentPageNum = 10;// 当前页面,返回信息数量
	private String deepType = "";// poi搜索类型空代表所有类型
	private PoiSearch poiSearch;
	private List<PoiItem> poiItems;// poi数据
	private MyPoiOverlay poiOverlay;// poi图层
	private int SearchBound = 1000;// 搜索范围 单位米。
	private String searchCity = "上海市";// 搜索城市
	private String searchWord = "停车";// 搜索关键字
	private LatLng ll_mySite;
	private final String mPageName = "NearbyParkingActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().initNetwork();
		setContentView(R.layout.activity_nearby_parking);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		init(savedInstanceState);
	}

	private void init(Bundle savedInstanceState) {
		setTitle();
		mLoadingDialog = new LoadingDialog(mContext);
		mMapView = (MapView) findViewById(R.id.big_mapview);
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
		}
		mLoadingDialog.startDialog("查找中");
		initMap();
	}

	/**
	 * 初始化地图
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mMapView.getMap();

		}
		UiSettings mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.big_mapview_title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText("附近停车场");// titlestr
		title.setVisibility(View.VISIBLE);
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
		if (mMapView != null) {
			mMapView.onDestroy();
			mMapView = null;
		}
		if (aMap != null) {
			aMap.clear();
			aMap = null;
		}
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		if (mMapView != null) {
			mMapView.onResume();
		}
		startPOI();
	}

	private void startPOI() {
		double lon = Double.parseDouble(this.getIntent().getStringExtra("lon"));
		double lat = Double.parseDouble(this.getIntent().getStringExtra("lat"));
		LatLonPoint lalo = new LatLonPoint(lat, lon);
		if (lalo != null) {
			startSearchParking(lalo);
		} else {
			ToastUtil.showToast("地址信息不正确");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * 查询附近停车场
	 * 
	 * @param lp
	 */
	public void startSearchParking(LatLonPoint lp) {
		aMap.clear();// 清理之前的图标;
		aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		LatLng ll = new LatLng(lp.getLatitude(), lp.getLongitude());
		aMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(10));
		aMap.addMarker(new MarkerOptions()
				.position(ll)
				.title(this.getIntent().getStringExtra("titleContent"))
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.sh_icon_mapicon)));
		query = new PoiSearch.Query(searchWord, deepType, searchCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(currentPageNum);// 设置每页最多返回多少条poiitem
		query.setPageNum(0);// 设置查第一页
		poiSearch = new PoiSearch(mContext, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.setBound(new SearchBound(lp, SearchBound));
		poiSearch.searchPOIAsyn();

	}

	/**
	 * 获取我的位置
	 */
	private void getMyLocation() {
		GaoDeLocationUtil mGaoDeLocationUtil=new GaoDeLocationUtil(mContext);
		mGaoDeLocationUtil.startLocation();
		mGaoDeLocationUtil.setOnLocationListener(new OnLocationListener() {

			@Override
			public void onLocationSuccess(AMapLocation location) {
				// TODO Auto-generated method stub
				if (mLoadingDialog != null) {
					mLoadingDialog.cancelDialog();
				}
				ll_mySite = new LatLng(location.getLatitude(), location
						.getLongitude());
				AppConfigUtil.LocalLocation.Location_latitude = String
						.valueOf(location.getLatitude());
				AppConfigUtil.LocalLocation.Location_longitude = String
						.valueOf(location.getLongitude());
			}

			@Override
			public void onLocationFailure(String error) {
				// TODO Auto-generated method stub
				mLoadingDialog.cancelDialog();
				ToastUtil.showToast(error);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nearby_parking, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		finish();

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		arg0.showInfoWindow();
		return false;
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = result
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						poiOverlay = new MyPoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.showToast("没有找到结果！");
					}
				}
			} else {
				ToastUtil.showToast("没有找到结果！");
			}
		} else if (rCode == 27) {
			ToastUtil.showToast("网络问题，请检查！");
		} else if (rCode == 32) {
			ToastUtil.showToast("Key不存在");
		} else {
			ToastUtil.showToast("其他错误，错误码：" + rCode);
		}
		getMyLocation();

	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.showToast(infomation);
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.poikeywordsearch_uri, null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());
		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());

		// ImageButton button = (ImageButton)
		// view.findViewById(R.id.start_amap_app);
		// 调起高德地图app导航功能
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// startAMapNavi(marker);
				LatLng end1 = new LatLng(marker.getPosition().latitude, marker
						.getPosition().longitude);
				NaviRoute.startNavi(NearbyParkingActivity.this, ll_mySite,
						"我的位置", end1, marker.getTitle(), NaviRoute.Navi_DRIVING);
			}
		});
		return view;
	}

	/**
	 * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
	 */
	public void startAMapNavi(Marker marker) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置
		naviPara.setTargetPoint(marker.getPosition());
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(4);
		try {
			// 调起高德地图导航
			ToastUtil.showToast("开始导航！");
			AMapUtils.openAMapNavi(naviPara, mContext);
		} catch (com.amap.api.maps.AMapException e) {
			// 如果没安装会进入异常，调起下载页面
			ToastUtil.showToast("您没有高德地图，请先下载才能使用该功能。");
			AMapUtils.getLatestAMapApp(mContext);
		}
	}

	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		if (mLoadingDialog != null) {
			mLoadingDialog.cancelDialog();
		}
	}
}
