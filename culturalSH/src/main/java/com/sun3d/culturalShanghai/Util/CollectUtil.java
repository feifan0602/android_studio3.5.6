package com.sun3d.culturalShanghai.Util;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.view.FastBlur;

public class CollectUtil {

	/**
	 * 添加活动收藏
	 */
	public static void addActivity(Context mContext, String activityId,
			final CollectCallback collect) {
		if (MyApplication.UserIsLogin) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(HttpUrlList.Collect.ACTIVITY_ID, activityId);
			params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.ADD_EVENT_URL, params,
					new HttpRequestCallback() {
						@Override
						public void onPostExecute(int statusCode, String resultStr) {
							// TODO Auto-generated method stub
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								collect.onPostExecute(true);
							} else {
								collect.onPostExecute(false);
							}
						}
					});
		} else {
			login(mContext);
		}
	}

	/**
	 * 取消活动收藏
	 */
	public static void cancelActivity(Context mContext, String activityId,
			final CollectCallback collect) {
		if (MyApplication.UserIsLogin) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(HttpUrlList.Collect.ACTIVITY_ID, activityId);
			params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.CANCEL_EVENT_URL, params,
					new HttpRequestCallback() {

						@Override
						public void onPostExecute(int statusCode, String resultStr) {
							// TODO Auto-generated method stub
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								collect.onPostExecute(true);
							} else {
								collect.onPostExecute(false);
							}
						}
					});
		} else {
			login(mContext);
		}
	}


	/**
	 * 添加场馆收藏
	 */
	public static void addVenue(Context mContext, String venueId, final CollectCallback collect) {
		if (MyApplication.UserIsLogin) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(HttpUrlList.Collect.VENUE_ID, venueId);
			params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.ADD_VENUE_URL, params,
					new HttpRequestCallback() {

						@Override
						public void onPostExecute(int statusCode, String resultStr) {
							// TODO Auto-generated method stub
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								collect.onPostExecute(true);
							} else {
								collect.onPostExecute(false);
							}
						}
					});
		} else {
			login(mContext);
		}
	}

	/**
	 * 取消场馆收藏
	 */
	public static void cancelVenue(Context mContext, String venueId, final CollectCallback collect) {
		if (MyApplication.UserIsLogin) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(HttpUrlList.Collect.VENUE_ID, venueId);
			params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.CANCEL_VENUE_URL, params,
					new HttpRequestCallback() {

						@Override
						public void onPostExecute(int statusCode, String resultStr) {
							// TODO Auto-generated method stub
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								collect.onPostExecute(true);
							} else {
								collect.onPostExecute(false);
							}
						}
					});
		} else {
			login(mContext);
		}
	}

	/**
	 * 添加团体收藏
	 */
	public static void addGroup(Context mContext, String groupId, final CollectCallback collect) {
		if (MyApplication.UserIsLogin) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(HttpUrlList.Group.GROUP_ID, groupId);
			params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.ADD_GROUP_URL, params,
					new HttpRequestCallback() {

						@Override
						public void onPostExecute(int statusCode, String resultStr) {
							// TODO Auto-generated method stub
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								collect.onPostExecute(true);
							} else {
								collect.onPostExecute(false);
							}
						}
					});
		} else {
			login(mContext);
		}
	}

	/**
	 * 取消团体收藏
	 */
	public static void cancelGroup(Context mContext, String groupId, final CollectCallback collect) {
		if (MyApplication.UserIsLogin) {
			Map<String, String> params = new HashMap<String, String>();
			params.put(HttpUrlList.Group.GROUP_ID, groupId);
			params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.CANCEL_GROUP_URL, params,
					new HttpRequestCallback() {
						@Override
						public void onPostExecute(int statusCode, String resultStr) {
							// TODO Auto-generated method stub
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								collect.onPostExecute(true);
							} else {
								collect.onPostExecute(false);
							}
						}
					});
		} else {
			login(mContext);
		}
	}

	private static void login(Context mContext) {
		Intent intent = new Intent(mContext, UserDialogActivity.class);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
		FastBlur.getScreen((Activity) mContext);
		mContext.startActivity(intent);
	}
}
