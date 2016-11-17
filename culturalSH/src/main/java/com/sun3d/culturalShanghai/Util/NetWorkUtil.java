package com.sun3d.culturalShanghai.Util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;

import com.sun3d.culturalShanghai.MyApplication;

/**
 * @ClassName: NetWorkUtil
 * @Description: 网络判断工具
 * @author Aloneter
 * @date 2014-4-16 下午1:37:34
 * @Version 1.0
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class NetWorkUtil {

	/**
	 * 判断网络是否可用
	 * 
	 * @return
	 */
	public static boolean isConnected() {

		Context context = MyApplication.getContext();
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {

			return false;
		} else {
			NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();

			if (networkInfo != null && networkInfo.isConnected()) {

				return true;
			}
		}

		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @return
	 */
	public static boolean isConnectedByWifi() {

		return isConnecteByType(ConnectivityManager.TYPE_WIFI);
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @return
	 */
	public static boolean isConnectedByMobile() {

		return isConnecteByType(ConnectivityManager.TYPE_MOBILE);
	}

	/**
	 * 根据网络类型判断是否可用
	 * 
	 * @param networkType
	 * @return
	 */
	@SuppressLint("NewApi")
	private static boolean isConnecteByType(int networkType) {

		Context context = MyApplication.getContext();

		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(networkType);

			if (mWiFiNetworkInfo != null) {

				return mWiFiNetworkInfo.isConnected();
			}
		}

		return false;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public static void networkException() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());
	}

}
