package com.sun3d.culturalShanghai.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class Utils {
	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	public static String getCurrentData() {
		return getCurrentTime("yyyy-MM-dd");
	}

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	/**
	 * 我的活动，初始化在线选座
	 * */
	public static Boolean[] sizeBoo(String str) {
		String[] seatArray = str.split(",");
		Boolean[] boo = new Boolean[seatArray.length];
		if (seatArray != null && seatArray.length > 0) {
			for (int i = 0; i < seatArray.length; i++) {
				boo[i] = false;
			}
		}

		return boo;
	}

	/**
	 * 在线选座，数据转为显示数据。 如：2_3 ---> 2座3排
	 * */
	public static String[] initSeats(String str) {
		Log.d("initSeats", str);
		String[] seatArray = str.split(",");
		String[] seats = new String[seatArray.length];
		if (seatArray != null && seatArray.length > 0) {
			for (int i = 0; i < seatArray.length; i++) {
				if (seatArray[i].length() > 1) {
					seats[i] = seatArray[i].replace("_", "排") + "座";
				}
			}
		}
		return seats;
	}

	// 将字符串转为时间戳
	public static String getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		Date d;
		try {

			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}

	// 将时间戳转为字符串
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;

	}
	
	 /**
	  * 获取版本号
	  * @return 当前应用的版本号
	  */
	 public static String getVersion(Context context) {
	     try {
	         PackageManager manager = context.getPackageManager();
	         PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
	         String version = info.versionName;
	         return version;
	     } catch (Exception e) {
	         e.printStackTrace();
	         return "0.0";
	     }
	 }

}
