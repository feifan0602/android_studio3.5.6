package com.sun3d.culturalShanghai.manager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.TrafficStats;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.sun3d.culturalShanghai.MyApplication;

/**
 * @ClassName: DeviceManager
 * @Description: 设备管理器
 * @author liningkang
 * @Version 1.0
 * 
 */
@SuppressLint({ "UseSparseArrays" })
public class DeviceManager {

	private static TelephonyManager telephonyManager; // 手机管理器
	private static WifiManager wifiManager; // wifi管理器
	private static PackageManager packageManager; // 包管理器
	private static DisplayMetrics displayMetrics; // 显示管理器
	private static ActivityManager activityManager; // 活动管理器
	private static WindowManager windowManager;

	public static WindowManager getWindowManager() {
		if (windowManager == null) {
			windowManager = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
		}
		return windowManager;
	}

	/**
	 * 返回手机管理器
	 * 
	 * @return
	 */
	public static TelephonyManager getTelephonyManager() {

		if (telephonyManager == null) {
			telephonyManager = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
		}

		return telephonyManager;
	}

	/**
	 * 返回无线管理器
	 * 
	 * @return
	 */
	public static WifiManager getWifiManager() {

		if (wifiManager == null) {
			wifiManager = (WifiManager) MyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
		}

		return wifiManager;
	}

	/**
	 * 返回包管理器
	 * 
	 * @return
	 */
	public static PackageManager getPackageManager() {

		if (packageManager == null) {
			packageManager = MyApplication.getContext().getPackageManager();
		}

		return packageManager;
	}

	/**
	 * 返回显示器管理器
	 * 
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics() {

		if (displayMetrics == null) {
			displayMetrics = new DisplayMetrics();
		}

		return displayMetrics;
	}

	/**
	 * 返回活动管理器
	 * 
	 * @return
	 */
	public static ActivityManager getActivityManager() {

		if (activityManager == null) {
			activityManager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		}

		return activityManager;
	}

	/**
	 * 返回当前应用的名称
	 * 
	 * @return
	 */
	public static String getContextName() {

		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;

		try {
			packageManager = MyApplication.getContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);

		return applicationName;
	}

	/**
	 * 获取语言信息
	 * 
	 * @return
	 */
	public static String getLanguage() {

		Locale l = Locale.getDefault();

		return String.format("%s-%s", l.getLanguage(), l.getCountry());
	}

	/**
	 * 获取设备Id
	 * 
	 * @return
	 */
	public static String getDeviceId() {
		String deviceId = "";
		try {
			TelephonyManager telephonyManager;
			telephonyManager = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephonyManager.getDeviceId();
			deviceId = telephonyManager.getSubscriberId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (deviceId == null || deviceId.length() == 0) ? "" : deviceId;
	}

	/**
	 * 获取国际移动设备身份码IMEI
	 * 
	 * @return
	 */
	public static String getIMEI() {

		String IMEI = "";

		try {
			IMEI = getTelephonyManager().getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (IMEI == null || IMEI.length() == 0) ? "" : IMEI;
	}

	/**
	 * 获取国际移动用户识别码IMSI
	 * 
	 * @return
	 */
	public static String getIMSI() {

		String IMSI = "";

		try {
			IMSI = getTelephonyManager().getSubscriberId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (IMSI == null || IMSI.length() == 0) ? "" : IMSI;
	}

	/**
	 * 获取电话号码
	 * 
	 * @return
	 */
	public static String getPhoneNumber() {

		String phoneNumber = "";

		try {
			phoneNumber = getTelephonyManager().getLine1Number();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (phoneNumber == null || phoneNumber.length() == 0) ? "" : phoneNumber;
	}

	/**
	 * 获取SIM卡号
	 * 
	 * @return
	 */
	public static String getSimNumber() {

		String simNumber = "";

		try {
			simNumber = getTelephonyManager().getSimSerialNumber();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (simNumber == null || simNumber.length() == 0) ? "" : simNumber;
	}

	/**
	 * 获取网络提供商
	 * 
	 * @return
	 */
	public static String getNetProvider() {

		String networkOperator = "";

		try {
			networkOperator = getTelephonyManager().getNetworkOperator();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (networkOperator == null || networkOperator.length() == 0) ? "" : networkOperator;
	}

	/**
	 * 获取传输网络类型
	 * 
	 * @return
	 */
	public static int getNetworkType() {

		int networkTypeId = -1;

		try {
			networkTypeId = getTelephonyManager().getNetworkType();
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<Integer, String> ntMap = new HashMap<Integer, String>();

		ntMap.put(TelephonyManager.NETWORK_TYPE_UNKNOWN, "未知");
		ntMap.put(TelephonyManager.NETWORK_TYPE_GPRS, "GPRS（移动/联通 2G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_EDGE, "EDGE（移动/联通 2G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_UMTS, "UMTS（移动/联通3G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_CDMA, "CDMA（电信2G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_EVDO_0, "EVDO0（电信3G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_EVDO_A, "EVDOA（电信3G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_1xRTT, "1xRTT");
		ntMap.put(TelephonyManager.NETWORK_TYPE_HSDPA, "HSDPA（移动/联通3G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_HSUPA, "HSUPA");
		ntMap.put(TelephonyManager.NETWORK_TYPE_HSPA, "HSPA");
		ntMap.put(TelephonyManager.NETWORK_TYPE_IDEN, "IDEN");
		ntMap.put(TelephonyManager.NETWORK_TYPE_EVDO_B, "EVDOB（电信3G）");
		ntMap.put(TelephonyManager.NETWORK_TYPE_LTE, "LTE");
		ntMap.put(TelephonyManager.NETWORK_TYPE_EHRPD, "EHRPD");
		ntMap.put(TelephonyManager.NETWORK_TYPE_HSPAP, "HSPAP");

		String networkType = ntMap.get(networkTypeId);

		return networkTypeId;
	}

	/**
	 * 获取手机制式
	 * 
	 * @return
	 */
	public static int getPhoneType() {

		int phoneTypeId = -1;

		try {
			phoneTypeId = getTelephonyManager().getPhoneType();
		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<Integer, String> ptMap = new HashMap<Integer, String>();

		ptMap.put(TelephonyManager.PHONE_TYPE_NONE, "未知");
		ptMap.put(TelephonyManager.PHONE_TYPE_GSM, "GSM（移动/联通）");
		ptMap.put(TelephonyManager.PHONE_TYPE_CDMA, "CDMA（电信）");
		ptMap.put(TelephonyManager.PHONE_TYPE_SIP, "SIP");

		String phoneType = ptMap.get(phoneTypeId);

		return phoneTypeId;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	@SuppressLint("InlinedApi")
	public static String getProductModel() {

		String productModel = "";

		try {
			productModel = android.os.Build.MODEL;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return productModel;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static String getOSVersion() {

		String osVersion = "";

		try {
			osVersion = android.os.Build.VERSION.RELEASE;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return osVersion;
	}

	/**
	 * 获取sdk版本
	 * 
	 * @return
	 */
	public static int getSDKVersion() {

		int sdkVersion = -1;

		try {
			sdkVersion = android.os.Build.VERSION.SDK_INT;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sdkVersion;
	}

	/**
	 * 获取mac地址
	 * 
	 * @return
	 */
	public static String getMacAddress() {

		String macAddress = "";

		try {
			macAddress = getWifiManager().getConnectionInfo().getMacAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return macAddress;
	}

	/**
	 * 获取IP地址
	 * 
	 * @return
	 */
	public static int getIpAddress() {

		int ipAddress = -1;

		try {
			ipAddress = getWifiManager().getConnectionInfo().getIpAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ipAddress;
	}

	/**
	 * 获取本应用版本名称
	 * 
	 * @return
	 */
	public static String getVersionName() {

		String verName = "";

		try {
			PackageInfo info = getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0);
			verName = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return verName;
	}

	/**
	 * 获取应用版本Code
	 * 
	 * @return
	 */
	public static String getVersionCode() {
		String verName = "";
		try {
			PackageInfo info = getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0);
			verName = info.versionCode + "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return verName;
	}

	/**
	 * 获取本应用包名
	 * 
	 * @return
	 */
	public static String getPackageName() {
		String pkgName = "";
		try {
			pkgName = MyApplication.getContext().getPackageName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pkgName;
	}

	/**
	 * 获取手机运营商
	 * 
	 * @return
	 */
	public static String getMobileInfo() {

		String simNumber = "";

		try {
			simNumber = getTelephonyManager().getSimSerialNumber();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (simNumber.startsWith("460")) {
			simNumber = simNumber.substring(3, 5);
		}

		// 03 电信，02 移动，01 联通
		Log.i("tag", "test sim info = " + simNumber);

		return simNumber;
	}

	/**
	 * 获取分辨率
	 * 
	 * @return
	 */
	public static String getResolution() {

		String resolution = "";

		try {
			getWindowManager().getDefaultDisplay().getMetrics(getDisplayMetrics());
			int width = displayMetrics.widthPixels;
			int height = displayMetrics.heightPixels;
			resolution = width + " " + height;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resolution;
	}

	/**
	 * 获取分辨率 -- 宽度
	 * 
	 * @return
	 */
	public static int getResolutionWidth() {

		int width = 0;

		try {
			getWindowManager().getDefaultDisplay().getMetrics(getDisplayMetrics());
			width = displayMetrics.widthPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return width;
	}

	/**
	 * 获取分辨率 -- 高度
	 * 
	 * @return
	 */
	public static int getResolutionHeight() {

		int height = 0;

		try {
			getWindowManager().getDefaultDisplay().getMetrics(getDisplayMetrics());
			height = displayMetrics.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return height;
	}

	/**
	 * 获取应用程序名称
	 * 
	 * @return
	 */
	public static String getApplicationName() {

		ApplicationInfo applicationInfo = null;

		try {
			applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}

		String applicationName = (String) getPackageManager().getApplicationLabel(applicationInfo);

		return applicationName;
	}

	/**
	 * 判断是系统应用还是三方应用
	 * 
	 * @param appInfo
	 *            应用信息
	 * @return 判断结果
	 */
	public static boolean isSysApp(ApplicationInfo appInfo) {

		if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {

			return false;
		} else if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

			return false;
		}

		return true;
	}

	/**
	 * 获取MetaValue
	 * 
	 * @param metaKey
	 * @return
	 */
	public static String getMetaValue(String metaKey) {

		Bundle metaData = null;
		String value = null;

		if (MyApplication.getContext() == null || metaKey == null) {

			return "";
		}

		try {
			ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				value = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}

		return value;
	}

	/**
	 * 判断应用对象的包是否存在
	 * 
	 * @param pkgName
	 * @return
	 */
	public static boolean isExitsApp(String pkgName) {

		if (pkgName == null || "".equals(pkgName)) {

			return false;
		}

		try {
			getPackageManager().getPackageInfo(pkgName, 0);

			return true;
		} catch (NameNotFoundException e) {

			return false;
		}
	}

	/**
	 * 获取短信记录列表
	 * 
	 * @return 短信列表
	 */
	@SuppressWarnings("unused")
	public static String getSmsInPhone() {

		final String SMS_URI_ALL = "content://sms/";
		final String SMS_URI_INBOX = "content://sms/inbox";
		final String SMS_URI_SEND = "content://sms/sent";
		final String SMS_URI_DRAFT = "content://sms/draft";

		StringBuilder smsBuilder = new StringBuilder();

		try {

			ContentResolver cr = MyApplication.getContext().getContentResolver();
			String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
			Uri uri = Uri.parse(SMS_URI_ALL);
			Cursor cur = cr.query(uri, projection, null, null, "date desc");

			if (cur.moveToFirst()) {

				String name;
				String phoneNumber;
				String smsbody;
				String date;
				String type;

				int nameColumn = cur.getColumnIndex("person");
				int phoneNumberColumn = cur.getColumnIndex("address");
				int smsbodyColumn = cur.getColumnIndex("body");
				int dateColumn = cur.getColumnIndex("date");
				int typeColumn = cur.getColumnIndex("type");

				do {
					name = cur.getString(nameColumn);
					phoneNumber = cur.getString(phoneNumberColumn);
					smsbody = cur.getString(smsbodyColumn);

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
					date = dateFormat.format(d);

					int typeId = cur.getInt(typeColumn);

					if (typeId == 1) {
						type = "接收";
					} else if (typeId == 2) {
						type = "发送";
					} else {
						type = "";
					}

					smsBuilder.append("[");
					smsBuilder.append(name + ",");
					smsBuilder.append(phoneNumber + ",");
					smsBuilder.append(smsbody + ",");
					smsBuilder.append(date + ",");
					smsBuilder.append(type);
					smsBuilder.append("] ");

					if (smsbody == null) {
						smsbody = "";
					}

				} while (cur.moveToNext());
			} else {
				smsBuilder.append("no result!");
			}

			smsBuilder.append("getSmsInPhone has executed!");
		} catch (SQLiteException ex) {
			ex.printStackTrace();
		}

		return smsBuilder.toString();
	}

	/**
	 * 获取流量统计
	 * 
	 */
	public static void getTrafficStats() {

		String rxBytes, txBytes;

		if (TrafficStats.getTotalRxBytes() == TrafficStats.UNSUPPORTED) {
			rxBytes = "对不起，您的手机不支持下行流量统计";
		} else {
			double receiveTotal = TrafficStats.getTotalRxBytes() / 1024.0;
			rxBytes = receiveTotal + "K";
		}
		if (TrafficStats.getTotalTxBytes() == TrafficStats.UNSUPPORTED) {
			txBytes = "对不起，您的手机不支持上行流量统计";
		} else {
			double transTotal = TrafficStats.getTotalTxBytes() / 1024.0;
			txBytes = transTotal + "K";
		}

	}

	/**
	 * 获取主机名称
	 * 
	 */
	public static void getHostName() {

		String id = Settings.Secure.getString(MyApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
		if (id != null && id.length() > 0) {
			String name = new String("android_").concat(id);

		}
	}

	/**
	 * 获取手机的IP地址
	 * 
	 * @return
	 */
	public static String getIpAdr() {

		try {
			String ipv4;

			List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());

			for (NetworkInterface ni : nilist) {
				List<InetAddress> ialist = Collections.list(ni.getInetAddresses());

				for (InetAddress address : ialist) {

					if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {

						return ipv4;
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}

		return "";
	}
}
