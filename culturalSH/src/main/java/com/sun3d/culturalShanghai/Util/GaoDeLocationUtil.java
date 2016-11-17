package com.sun3d.culturalShanghai.Util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class GaoDeLocationUtil implements AMapLocationListener {
	private String TAG="GaoDeLocationUtil";
	// 声明AMapLocationClient类对象，定位发起端
	private AMapLocationClient mLocationClient = null;
	// 声明mLocationOption对象，定位参数
	public AMapLocationClientOption mLocationOption = null;
	// 声明mListener对象，定位监听器
	// 标识，用于判断是否只显示一次定位信息和用户重新定位
	private boolean isFirstLoc = true;
	private Context mContext;

	public GaoDeLocationUtil(Context mContext) {
		this.mContext = mContext;
	}

	@Override
	public void onLocationChanged(AMapLocation aMapLocation) {
		if (aMapLocation != null) {
			if (aMapLocation.getErrorCode() == 0) {
				//定位成功回调信息，设置相关消息
				aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
				double la= aMapLocation.getLatitude();//获取纬度
				double lo= aMapLocation.getLongitude();//获取经度
				aMapLocation.getAccuracy();//获取精度信息
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(aMapLocation.getTime());
				df.format(date);//定位时间
				aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
				aMapLocation.getCountry();//国家信息
				aMapLocation.getProvince();//省信息
				aMapLocation.getCity();//城市信息
				aMapLocation.getDistrict();//城区信息
				aMapLocation.getStreet();//街道信息
				aMapLocation.getStreetNum();//街道门牌号信息
				aMapLocation.getCityCode();//城市编码
				aMapLocation.getAdCode();//地区编码

				// 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
				if (isFirstLoc) {
					//设置缩放级别
					//将地图移动到定位点
					//点击定位按钮 能够将地图的中心移动到定位点
					//添加图钉
					//  aMap.addMarker(getMarkerOptions(amapLocation));
					//获取定位信息
					StringBuffer buffer = new StringBuffer();
					buffer.append(aMapLocation.getCountry() + ""
							+ aMapLocation.getProvince() + ""
							+ aMapLocation.getCity() + ""
							+ aMapLocation.getProvince() + ""
							+ aMapLocation.getDistrict() + ""
							+ aMapLocation.getStreet() + ""
							+ aMapLocation.getStreetNum());
					isFirstLoc = false;
				}
				if (mOnLocationListener != null) {
					mOnLocationListener.onLocationSuccess(aMapLocation);
				}

			} else {
				//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				if (mOnLocationListener != null) {
					mOnLocationListener.onLocationFailure("定位失败");
				}
			}
		}
	};

	public  void startLocation() {
		// 初始化定位
		mLocationClient = new AMapLocationClient(mContext);
		// 设置定位回调监听
		mLocationClient.setLocationListener(this);
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption
				.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(true);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mLocationClient.startLocation();
	}
	private static OnLocationListener mOnLocationListener;

	public static void setOnLocationListener(OnLocationListener onLocationListener) {
		mOnLocationListener = onLocationListener;
	}

	public static interface OnLocationListener {
		public void onLocationSuccess(AMapLocation location);

		public void onLocationFailure(String error);
	}
}
