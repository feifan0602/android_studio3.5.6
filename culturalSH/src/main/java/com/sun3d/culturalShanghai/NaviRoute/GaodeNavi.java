package com.sun3d.culturalShanghai.NaviRoute;

import android.content.Context;
import android.content.Intent;

import com.amap.api.maps.model.LatLng;
import com.sun3d.culturalShanghai.MyApplication;

/**
 * 百度导航调用
 * 
 * @author yangyoutao
 * 
 */
public class GaodeNavi {
	public static String GAODENAVIMODE_TRANSIT = "1";// 公交
	public static String GAODENAVIMODE_DRIVING = "2";// 驾车
	public static String GAODENAVIMODE_WALKING = "4";// 步行

	/**
	 * 开启高德导航
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
		String urltitle = "androidamap://route?sourceApplication=文化云&";
		String urlStart = "slat=" + startStr_lat + "&slon=" + startStr_lon + "&sname=" + startName
				+ "&";
		String urlEnd = "dlat=" + end.latitude + "&dlon=" + end.longitude + "&dname=" + endName
				+ "&";
		String urlOver = "dev=0&m=0&t=" + mode;
		String uri = urltitle + urlStart + urlEnd + urlOver;
		Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(uri));
		intent.setPackage("com.autonavi.minimap");
		context.startActivity(intent);
	}
}
