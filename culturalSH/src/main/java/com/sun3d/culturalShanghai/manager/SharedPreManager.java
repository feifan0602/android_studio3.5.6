package com.sun3d.culturalShanghai.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ParseException;
import android.util.Log;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.object.AddressInfor;
import com.sun3d.culturalShanghai.object.EventAddShareHistory;
import com.sun3d.culturalShanghai.object.EventAddressInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorType;
import com.sun3d.culturalShanghai.object.UserInfor;

public class SharedPreManager {
	private static String TAG = "SharedPreManager";
	static SharedPreferences mUser = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_USER, 0);
	static SharedPreferences mAddress = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_ADDRESS, 0);
	static SharedPreferences mShareHistory = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_SHAREHISTORY, 0);
	static SharedPreferences mAddress_Venue = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_VENUE_ADDRESS, 0);
	static SharedPreferences mMain = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_NAME, 0);
	static SharedPreferences mType = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_TYPE, 0);
	static SharedPreferences mAllArea = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_ALL_AREA, 0);

	public static void saveAllArea(String strArea) {
		Editor editor = mAllArea.edit();
		editor.putString(AppConfigUtil.PRE_FILE_ALL_AREA, strArea);
		editor.commit();
	}

	public static String getAllArea() {
		String area = mAllArea.getString(AppConfigUtil.PRE_FILE_ALL_AREA, null);
		return area;
	}
	public static void clearAllArea(){
		Editor editor = mAllArea.edit();
		editor.clear();
		editor.commit();
	}
	static SharedPreferences mAllTag = MyApplication.getContext()
			.getSharedPreferences(AppConfigUtil.PRE_FILE_ALL_TAG, 0);

	public static void saveAllTag(String strArea) {
		Editor editor = mAllTag.edit();
		editor.putString(AppConfigUtil.PRE_FILE_ALL_TAG, strArea);
		editor.commit();
	}

	public static String getAllTag() {
		String area = mAllTag.getString(AppConfigUtil.PRE_FILE_ALL_TAG, null);
		return area;
	}

	/**
	 * 保存活动页面用户行为分析页面上次显示的时间数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveActiviyTopPageGoneTime(long time) {
		Editor editor = mMain.edit();
		editor.putLong("ActiviyTopPageGoneTime", time);
		editor.commit();
	}

	/**
	 * 读取活动页面用户行为分析页面上次显示的时间数据
	 * 
	 * @param key
	 * @return
	 */
	public static long readActiviyTopPageGoneTime() {
		return mMain.getLong("ActiviyTopPageGoneTime", 0);
	}

	/**
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveString(String key, String values) {
		Editor editor = mMain.edit();
		editor.putString(key, values);
		editor.commit();
	}

	public static String readString(String key) {
		return mMain.getString(key, "");
	}

	/**
	 * 保存应用数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveMain(String key, boolean values) {
		Editor editor = mMain.edit();
		editor.putBoolean(key, values);
		editor.commit();
	}

	/**
	 * 读取应用数据
	 * 
	 * @param key
	 * @return
	 */
	public static boolean readMain(String key) {
		return mMain.getBoolean(key, false);
	}

	/**
	 * 保存搜索的历史shuju
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveShareHistory(String mHistory) {
		Editor editor = mShareHistory.edit();
		editor.putString("HistoryInfor", mHistory);
		editor.commit();
	}

	/**
	 * 保存活动搜索的历史数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveEventAddressInfor(String mAddressInfor) {
		Editor editor = mAddress.edit();
		editor.putString("AddressInfor", mAddressInfor);
		editor.commit();
	}

	/**
	 * 保存没有登录时候的标签
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveActivityType(String Type) {
		Editor editor = mType.edit();
		editor.putString("Type", Type);
		editor.commit();
	}

	/**
	 * 读取标签信息
	 * 
	 * @param key
	 * @return
	 */
	public static List<String> readTypeInfo() {
		String jsonStr = mType.getString("Type", "");
		List<String> list = new ArrayList<String>();
		Log.i(TAG, " jsonStr==  " + jsonStr.toString());
		try {
			JSONArray ja = new JSONArray(jsonStr);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.optJSONObject(i);
				String tagName = json.optString(i + "", "");
				list.add(tagName);
			}
			Log.i(TAG, "list size===  " + list.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		return list;
	}

	/**
	 * 清楚标签信息
	 * 
	 * @param key
	 * @return
	 */
	public static void clearTypeInfo() {
		Editor editor = mType.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 保存场所搜索的历史数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveVenueAddressInfor(String mAddressInfor) {
		Editor editor = mAddress_Venue.edit();
		editor.putString("AddressInfor", mAddressInfor);
		editor.commit();
	}

	/**
	 * 保存用户数据
	 * 
	 * @param key
	 * @param values
	 */
	public static void saveUserInfor(String mUserInfor) {
		MyApplication.UserIsLogin = true;
		Editor editor = mUser.edit();
		editor.putString("UserInfor", mUserInfor);
		editor.commit();
	}

	/**
	 * 读取歷史地址的信息
	 * 
	 * @param key
	 * @return
	 */
	// public static EventAddShareHistory readShareHistory() {
	// String jsonStr = mShareHistory.getString("HistoryInfor", "");
	// EventAddShareHistory mShareHistoryInfor = new EventAddShareHistory();
	// try {
	// JsonHelperUtil.toJavaBean(mShareHistoryInfor, jsonStr);
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.d(TAG, e.toString());
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.d(TAG, e.toString());
	// }
	// return mShareHistoryInfor;
	// }

	/**
	 * 读取地址信息
	 * 
	 * @param key
	 * @return
	 */
	public static EventAddressInfo readEventAddressInfor() {
		String jsonStr = mAddress.getString("AddressInfor", "");
		EventAddressInfo mAddressInfor = new EventAddressInfo();
		try {
			JsonHelperUtil.toJavaBean(mAddressInfor, jsonStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		return mAddressInfor;
	}

	/**
	 * 读取活动地址信息
	 * 
	 * @param key
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> wffreadEventAddressInfor() {
		String jsonStr = mAddress.getString("AddressInfor", "");
		Log.i("ceshi", "jsonStr==  " + jsonStr.toString());
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String str = json.getString("list");
			JSONArray ja = new JSONArray(str);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String list_str = jo.toString();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tv", list_str.split(":")[1].replaceAll("\"", "")
						.replaceAll("\\}", ""));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 3.5.4 读取历史信息SearchActivity
	 * 
	 * @param key
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> readShareHistory() {
		String jsonStr = mShareHistory.getString("HistoryInfor", "");
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String str = json.getString("list");
			JSONArray ja = new JSONArray(str);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String list_str = jo.toString();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tv",
						list_str.split(",")[1].split(":")[1].replaceAll("\"",
								"").replaceAll("\\}", ""));
				map.put("type", list_str.split(",")[0].split(":")[1]
						.replaceAll("\"", "").replaceAll("\\}", ""));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 读取場所地址信息
	 * 
	 * @param key
	 * @return
	 */
	public static ArrayList<HashMap<String, String>> wffreadVenueAddressInfor() {
		String jsonStr = mAddress_Venue.getString("AddressInfor", "");
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String str = json.getString("list");
			JSONArray ja = new JSONArray(str);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String list_str = jo.toString();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("tv", list_str.split(":")[1].replaceAll("\"", "")
						.replaceAll("\\}", ""));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 读取用户数据
	 * 
	 * @param key
	 * @return
	 */
	public static UserInfor readUserInfor() {
		String jsonStr = mUser.getString("UserInfor", "");
		if (jsonStr != null && jsonStr.startsWith("\ufeff")) {
			jsonStr = jsonStr.substring(1);
		}
		UserInfor mUserInfor = new UserInfor();
		try {
			JsonHelperUtil.toJavaBean(mUserInfor, jsonStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		Log.d(TAG, mUserInfor.toString());
		if (mUserInfor.getUserId() == null
				|| mUserInfor.getUserId().trim().length() == 0) {
			MyApplication.UserIsLogin = false;
		} else {
			MyApplication.UserIsLogin = true;
		}
		return mUserInfor;
	}

	/**
	 * 清楚用户数据
	 */
	public static void clearUserInfo() {
		Editor editor = mUser.edit();
		editor.clear();
		editor.commit();
		MyApplication.loginUserInfor = new UserInfor();
		MyApplication.UserIsLogin = false;
	}

	/**
	 * 3.5.4清除请求的历史数据
	 */
	public static void clearShareHistory() {
		Editor editor = mShareHistory.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 清楚活动搜索的数据
	 */
	public static void clearEventAddressInfo() {
		Editor editor = mAddress.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 清楚场所搜索的数据
	 */
	public static void clearVenueAddressInfo() {
		Editor editor = mAddress_Venue.edit();
		editor.clear();
		editor.commit();
	}
}
