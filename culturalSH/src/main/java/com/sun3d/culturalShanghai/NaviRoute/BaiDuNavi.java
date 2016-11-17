package com.sun3d.culturalShanghai.NaviRoute;

import java.net.URISyntaxException;

import android.content.Context;
import android.content.Intent;

import com.amap.api.maps.model.LatLng;

/**
 * 百度导航调用
 * 
 * @author yangyoutao
 * 
 */
public class BaiDuNavi {
	public static String BAIDUNAVIMODE_TRANSIT = "transit";// 公交
	public static String BAIDUNAVIMODE_DRIVING = "driving";// 驾车
	public static String BAIDUNAVIMODE_WALKING = "walking";// 步行
	private static String Baidu_HeadeStr = "intent://map/direction?";// Android服务地址
	private static String Baidu_end = "&src=创图科技|文化云#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
	private static String cityName = "上海市";

	/**
	 * 开启百度导航
	 * 
	 * @param context
	 * @param start
	 * @param startName
	 * @param end
	 * @param endName
	 * @param mode
	 */
	public static void startNavi(Context context, LatLng start, String startName, LatLng end,
			String endName, String mode) {
		if (end == null) {
			return;
		}
		String startStr_lat = "";
		String startStr_lon = "";
		if (start != null) {
			startStr_lat = String.valueOf(start.latitude);
			startStr_lon = String.valueOf(start.longitude);
		}
		String uri = Baidu_HeadeStr + "origin=latlng:" + startStr_lat + "," + startStr_lon
				+ "|name:" + startName + "&destination=latlng:" + end.latitude + ","
				+ end.longitude + "|name:" + endName + "&mode=" + mode + "&origin_region="
				+ cityName + "&destination_region=" + cityName + Baidu_end;
		Intent intert;   
		try {
			intert = Intent.getIntent(uri);
			context.startActivity(intert);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
