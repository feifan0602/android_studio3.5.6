package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.NaviRoute.NaviActivity;
import com.sun3d.culturalShanghai.NaviRoute.NaviRoute;
import com.sun3d.culturalShanghai.NaviRoute.Utils;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil.OnLocationListener;
import com.sun3d.culturalShanghai.Util.GaoDeMapPoiSeahUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.object.NearbyMapInfor;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 导航页面
 * 
 * @author wenff
 * 
 */
public class BigMapViewActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, AMapNaviListener {
	private MapView mMapView;
	private TextView mMapViewLable;
	private AMap aMap;
	private RelativeLayout mLayoutTitle;
	private ImageView BigEnlarge, BigNerrow;// 放大缩小事件
	private ImageButton mTitleLeft, mTitleRight;
	private TextView mTitleLable;
	private Context mContext;
	private String mType;
	private GaoDeMapPoiSeahUtil mGaoDeMapPoiSeahUtil;
	private RadioGroup mRadioGroup;
	private AMapNavi mAMapNavi;// 导航对象
	private LoadingDialog mLoadingDialog;
	private String loadingText = "定位中";
	private TextView address;
	private TextView navigit;
	private String endName;
	private final String mPageName = "BigMapViewActivity";
	private LatLng ll_mySite;
	private String BigMapViewActivity = "BigMapViewActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_mapview);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		initView();
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
		}
		initMap();
		try {
			setInitValue();
		} catch (Exception e) {
			Log.i("tag", BigMapViewActivity);
		}

	}

	private void initView() {
		mType = this.getIntent().getStringExtra(AppConfigUtil.INTENT_TYPE);
		mMapView = (MapView) findViewById(R.id.big_mapview);
		address = (TextView) findViewById(R.id.map_labeladdress);
		address.setVisibility(View.GONE);
		mMapViewLable = (TextView) findViewById(R.id.big_mapview_lable);
		BigEnlarge = (ImageView) findViewById(R.id.big_mapview_enlarge);
		BigNerrow = (ImageView) findViewById(R.id.big_mapview_nerrow);
		mLayoutTitle = (RelativeLayout) findViewById(R.id.big_mapview_title);
		mTitleLeft = (ImageButton) mLayoutTitle.findViewById(R.id.title_left);
		mTitleRight = (ImageButton) mLayoutTitle.findViewById(R.id.title_right);
		mTitleRight.setVisibility(View.GONE);
		navigit = (TextView) mLayoutTitle.findViewById(R.id.title_send);
		navigit.setText("导航");
		navigit.setTextColor(0xff8b969a);
		navigit.setVisibility(View.GONE);
		// navigit.setOnClickListener(this);
		mTitleLable = (TextView) mLayoutTitle.findViewById(R.id.title_content);
		mTitleLable.setVisibility(View.VISIBLE);
		mRadioGroup = (RadioGroup) findViewById(R.id.big_mapview_radigb);
		mRadioGroup.setOnCheckedChangeListener(this);
		mTitleLeft.setOnClickListener(this);
		mMapViewLable.setOnClickListener(this);
		mTitleRight.setOnClickListener(this);
		BigEnlarge.setOnClickListener(this);
		BigNerrow.setOnClickListener(this);

	}

	/**
	 * 初始化地图
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();

		}
		UiSettings mUiSettings = aMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(false);
	}

	/**
	 * 设置地图
	 */
	private void setUpMap() {

		if (AppConfigUtil.LocalLocation.Location_latitude.length() == 0
				| AppConfigUtil.LocalLocation.Location_longitude.length() == 0) {
			return;
		}

		double lat = Double
				.parseDouble(AppConfigUtil.LocalLocation.Location_latitude);
		double lon = Double
				.parseDouble(AppConfigUtil.LocalLocation.Location_longitude);
		LatLng AddreslatLng = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(AddreslatLng));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
		// aMap.setLocationSource(new MyLocationSource(this, aMap));// 设置定位监听
		// aMap.getUiSettings().setMyLocationButtonEnabled(false);//
		// 设置默认定位按钮是否显示
		// aMap.setMyLocationEnabled(true);//
		// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// //
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		// aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		aMap.addMarker(new MarkerOptions()
				.position(AddreslatLng)
				.title("我的位置")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_marker))
				.draggable(true));

	}

	/**
	 * 设置默认图片
	 * 
	 * @param lon
	 * @param lat
	 */
	private void setDefultAddress(double lon, double lat) {
		LatLng latLng = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		aMap.addMarker(new MarkerOptions()
				.position(latLng)
				.title("我的位置")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.location_marker))
				.draggable(true));
	}

	/**
	 * 初始化值
	 */
	public void setInitValue() {
		String titleContent = this.getIntent().getStringExtra("titleContent");
		titleContent = titleContent == null ? "" : titleContent;
		mTitleLable.setText(titleContent);
		mGaoDeMapPoiSeahUtil = new GaoDeMapPoiSeahUtil(mContext, aMap);
		if ("1".equals(mType)) {// 我的位置
			navigit.setVisibility(View.GONE);
			mTitleRight.setVisibility(View.GONE);
			mRadioGroup.setVisibility(View.GONE);
			NearbyMapInfor mNearbyMapInfor = (NearbyMapInfor) this.getIntent()
					.getSerializableExtra("data");
			mGaoDeMapPoiSeahUtil.addActivityMakrt(
					mNearbyMapInfor.getEventlistItem(),
					mNearbyMapInfor.getVenuelistItem(), 2);
		} else if ("2".equals(mType)) {// 场馆，活动的位置
			mMapViewLable.setVisibility(View.GONE);
			mTitleLable.setText("地图");
			address.setVisibility(View.VISIBLE);
			aMap.clear();
			mNaviEnd = null;
			mNaviStart = null;
			mEndPoints.clear();
			mStartPoints.clear();
			mAMapNavi = AMapNavi.getInstance(this);
			// mAMapNavi.setAMapNaviListener(this);
			double lon = Double.parseDouble(this.getIntent().getStringExtra(
					"lon"));
			double lat = Double.parseDouble(this.getIntent().getStringExtra(
					"lat"));
			endName = this.getIntent().getStringExtra("address");
			address.setText("地址：" + endName);
			mNaviEnd = new NaviLatLng(lat, lon);
			mEndPoints.add(mNaviEnd);
			LatLng latLng = new LatLng(lat, lon);
			// 初始化当前位置
			aMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			aMap.addMarker(new MarkerOptions()
					.position(latLng)
					.title(address.getText().toString())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.sh_index_map_icon))
					.draggable(true));
			mTitleRight.setImageResource(R.drawable.sh_icon_title_share);
			getMyLocation();

		}
	}

	/**
	 * 获取我的位置
	 */
	private void getMyLocation() {
		mLoadingDialog = new LoadingDialog(mContext);
		mLoadingDialog.startDialog(loadingText);
		GaoDeLocationUtil mGaoDeLocationUtil=new GaoDeLocationUtil(mContext);
		mGaoDeLocationUtil.startLocation();
		mGaoDeLocationUtil.setOnLocationListener(new OnLocationListener() {

			@Override
			public void onLocationSuccess(AMapLocation location) {
				// TODO Auto-generated method stub

				mLoadingDialog.cancelDialog();
				ll_mySite = new LatLng(location.getLatitude(), location
						.getLongitude());

				AppConfigUtil.LocalLocation.Location_latitude = String
						.valueOf(location.getLatitude());
				AppConfigUtil.LocalLocation.Location_longitude = String
						.valueOf(location.getLongitude());
				LatLng latLng;
				if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
						|| AppConfigUtil.LocalLocation.Location_longitude
								.equals("0.0")) {
					AppConfigUtil.LocalLocation.Location_latitude = MyApplication.Location_latitude;
					AppConfigUtil.LocalLocation.Location_longitude = MyApplication.Location_longitude;
					mNaviStart = new NaviLatLng(Double
							.valueOf(MyApplication.Location_latitude), Double
							.valueOf(MyApplication.Location_longitude));
					mStartPoints.add(mNaviStart);
					latLng = new LatLng(Double
							.valueOf(MyApplication.Location_latitude), Double
							.valueOf(MyApplication.Location_longitude));
				} else {
					mNaviStart = new NaviLatLng(location.getLatitude(),
							location.getLongitude());
					mStartPoints.add(mNaviStart);
					latLng = new LatLng(location.getLatitude(), location
							.getLongitude());
				}

				// 初始化当前位置
				// aMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
				aMap.addMarker(new MarkerOptions()
						.position(latLng)
						.title("我的位置")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.location_marker))
						.draggable(true));
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
		if (mMapView != null) {
			mMapView.onDestroy();
			mMapView = null;
		}
		if (aMap != null) {
			aMap.clear();
			aMap = null;
		}
	}

	/**
	 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域 判断是否启用动画效果
	 */
	@SuppressWarnings("unused")
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
		if (true) {
			aMap.animateCamera(update, 1000, callback);
		} else {
			aMap.moveCamera(update);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			if ("2".equals(mType)) {
				Intent intent = new Intent(mContext, UserDialogActivity.class);
				FastBlur.getScreen((Activity) mContext);
				ShareInfo info = new ShareInfo();
				info.setTitle("没有");
				intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
				intent.putExtra(DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
				startActivity(intent);
			}

			break;
		case R.id.big_mapview_lable:
			finish();
			break;
		case R.id.big_mapview_enlarge:
			changeCamera(CameraUpdateFactory.zoomIn(), null);
			break;
		case R.id.big_mapview_nerrow:
			changeCamera(CameraUpdateFactory.zoomOut(), null);
			break;
		case R.id.title_send:// 导航
			// if (mNaviEnd != null) {
			// LatLng end = new LatLng(mNaviEnd.getLatitude(),
			// mNaviEnd.getLongitude());
			// mGaoDeMapPoiSeahUtil.startAMapNavi(end);
			// }

			break;
		default:
			break;
		}
	}

	// 起点终点坐标
	private NaviLatLng mNaviStart;
	private NaviLatLng mNaviEnd;
	// 起点终点列表
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();

	// 计算驾车路线
	private void calculateDriveRoute() {
		mLoadingDialog = new LoadingDialog(mContext);
		mLoadingDialog.startDialog("驾车路线规划中");
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (!isSuccess) {
			mLoadingDialog.cancelDialog();
			ToastUtil.showToast("路线计算失败,请重试");
		}

	}

	// 计算步行路线
	private void calculateFootRoute() {
		mLoadingDialog = new LoadingDialog(mContext);
		mLoadingDialog.startDialog("步行路线规划中");
		boolean isSuccess = mAMapNavi.calculateWalkRoute(mNaviStart, mNaviEnd);
		if (!isSuccess) {
			mLoadingDialog.cancelDialog();
			ToastUtil.showToast("路线计算失败,请重试");
		}
	}

	/**
	 * 导航页面
	 */
	private void naviActivity() {

		mLoadingDialog.cancelDialog();
		Intent gpsIntent = new Intent(BigMapViewActivity.this,
				NaviActivity.class);
		Bundle bundle = new Bundle();
		bundle.putBoolean(Utils.ISEMULATOR, false);
		bundle.putInt(Utils.ACTIVITYINDEX, Utils.SIMPLEROUTENAVI);
		gpsIntent.putExtras(bundle);
		gpsIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(gpsIntent);

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub\
		// 构造导航参数
		RadioButton rbcar = (RadioButton) arg0.findViewById(R.id.car);
		RadioButton rbbus = (RadioButton) arg0.findViewById(R.id.bus);
		switch (arg0.getCheckedRadioButtonId()) {
		case R.id.car:
			rbcar.setTextColor(Color.WHITE);
			rbbus.setTextColor(getResources().getColor(R.color.orange_color));
			// calculateDriveRoute();
			LatLng end = null;
			if (mNaviEnd != null) {
				end = new LatLng(mNaviEnd.getLatitude(),
						mNaviEnd.getLongitude());
				// mGaoDeMapPoiSeahUtil.startAMapNavi(end);
			}
			NaviRoute.startNavi(BigMapViewActivity.this, ll_mySite, "我的位置",
					end, endName, NaviRoute.Navi_DRIVING);
			break;
		case R.id.bus:
			rbcar.setTextColor(getResources().getColor(R.color.orange_color));
			rbbus.setTextColor(Color.WHITE);
			// String urltitle = "androidamap://route?sourceApplication=文化云&";
			// String urlStart = "slat=" +
			// AppConfigUtil.LocalLocation.Location_latitude + "&slon="
			// + AppConfigUtil.LocalLocation.Location_longitude +
			// "&sname=我的位置&";
			// String urlEnd = "dlat=" + mNaviEnd.getLatitude() + "&dlon=" +
			// mNaviEnd.getLongitude()
			// + "&dname=" + endName + "&";
			// String urlOver = "dev=0&m=0&t=1";
			// if (mGaoDeMapPoiSeahUtil.haveAppliction("com.autonavi.minimap",
			// mNaviEnd)) {
			// Intent intent = new Intent("android.intent.action.VIEW",
			// android.net.Uri.parse(urltitle + urlStart + urlEnd + urlOver));
			// intent.setPackage("com.autonavi.minimap");
			// startActivity(intent);
			// }
			LatLng end1 = new LatLng(mNaviEnd.getLatitude(),
					mNaviEnd.getLongitude());
			NaviRoute.startNavi(BigMapViewActivity.this, ll_mySite, "我的位置",
					end1, endName, NaviRoute.Navi_TRANSIT);
			break;
		case R.id.foot:
			calculateFootRoute();
			break;
		default:
			break;
		}

	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		mLoadingDialog.cancelDialog();
		ToastUtil.showToast("路线计算失败,请重试");
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub
		naviActivity();
	}

	@Override
	public void hideCross() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hideLaneInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyParallelRoad(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCalculateMultipleRoutesSuccess(int[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviInfoUpdate(NaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showCross(AMapNaviCross arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAimlessModeStatistics(AimLessModeStat arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	@Deprecated
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {
		// TODO Auto-generated method stub

	}

}
