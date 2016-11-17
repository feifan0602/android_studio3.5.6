/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.sun3d.culturalShanghai.thirdparty;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.mob.tools.utils.UIHandler;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ThirdBindActivity;
import com.sun3d.culturalShanghai.handler.ThirdLogin;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.object.WeiXinInfo;

/** 演示授权并获取获取AccessToken */
public class GetTokenPage implements Callback, OnClickListener, PlatformActionListener {
	private String TAG = "GetTokenPage";
	private static GetTokenPage page = new GetTokenPage();
	private static String name;
	private static HttpRequestCallback mnowCallback;
	private static String login_type = "";
	public final static String QQpackageName = "com.tencent.mobileqq";
	public final static String SinapackageName = "com.sina.weibo";
	public final static String WeixinpackageName = "com.tencent.mm";

	/**
	 * QQ登录
	 */
	public static void GetQQToken(Context context, HttpRequestCallback mCallback) {
		if (!isApkInstalled(context, QQpackageName)) {
			if (mCallback == null) {
				ToastUtil.showToast("QQ没有安装.");
				return;
			}
			mCallback.onPostExecute(HttpCode.HTTP_Request_Failure_CODE, "QQ没有安装.");
			return;
		}
		mnowCallback = null;
		mnowCallback = mCallback;
		login_type = ThirdLogin.QQ;
		Platform plat = ShareSDK.getPlatform(ShareName.QQ);
		plat.setPlatformActionListener(page);
		plat.authorize();
		name = ShareName.QQ;
	}

	/**
	 * 检测是否安装相关平台
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static final boolean isApkInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 新浪微博登录
	 */
	public static void GetSinaWeiboToken(Context context, HttpRequestCallback mCallback) {
		if (!isApkInstalled(context, SinapackageName)) {
			if (mCallback == null) {
				ToastUtil.showToast("新浪微博没有安装。");
				return;
			}
			mCallback.onPostExecute(HttpCode.HTTP_Request_Failure_CODE, "新浪微博没有安装。");
			return;
		}
		mnowCallback = null;
		mnowCallback = mCallback;
		login_type = ThirdLogin.SINAWeibo;
		Platform plat = ShareSDK.getPlatform(ShareName.SinaWeibo);
		plat.setPlatformActionListener(page);
		plat.authorize();
		name = ShareName.SinaWeibo;
	}

	/**
	 * 微信登录
	 */
	public static void GetWechatToken(Context context, HttpRequestCallback mCallback) {
		if (!isApkInstalled(context, WeixinpackageName)) {
			if (mCallback == null) {
				ToastUtil.showToast("微信没有安装.");
				return;
			}
			mCallback.onPostExecute(HttpCode.HTTP_Request_Failure_CODE, "微信没有安装.");
			return;
		}
		mnowCallback = null;
		mnowCallback = mCallback;
		login_type = ThirdLogin.Weixin;
		Platform plat = ShareSDK.getPlatform(ShareName.Wechat);
		plat.setPlatformActionListener(page);
		plat.authorize();
		name = ShareName.Wechat;
	}

	public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	public void onCancel(Platform plat, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	public void onError(Platform plat, int action, Throwable t) {
		t.printStackTrace();
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	/** 通过Toast显示操作结果 */
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		if (plat != null) {
			String text = ActionToString.actionToString(msg.arg2);
			switch (msg.arg1) {
			case 1: {
				// 成功
				text = plat.getName() + "get token: " + plat.getDb().getToken() + "-"
						+ plat.getDb().getUserName() + "-:头像：" + plat.getDb().getUserIcon() + "-"
						+ plat.getDb().getUserId();
				String thirdId = "";
				if (login_type.equals(ThirdLogin.Weixin)) {
					WeiXinInfo weiinfo = JsonUtil.getWeiXinInfo(plat.getDb().exportData());
					Log.d("WeiXinInfo", "weiinfo:" + weiinfo.toString());
					thirdId = weiinfo.getUnionid();
				} else {
					thirdId = plat.getDb().getUserId();
				}
				if (mnowCallback != null) {
					ThirdLogin.thirdlogin(thirdId, login_type, "", plat.getDb().getUserName(), plat
							.getDb().getUserIcon(), 1, mnowCallback);
				} else {
					MyApplication.Third_OpenId = thirdId;
					MyApplication.Third_LoginType = login_type;
					MyApplication.getInstance().getMyBindHandler()
							.sendEmptyMessage(ThirdBindActivity.SENDBING);
				}
				// GetInforPage.getInfor(name);
			}
				break;
			case 2: {
				// 失败
				text = plat.getName() + "认证登录失败";
				if (mnowCallback != null) {
					mnowCallback.onPostExecute(HttpCode.HTTP_Request_Failure_CODE, text);
				}
			}
				break;
			case 3: {
				// 取消
				text = plat.getName() + "认证登录取消";
				if (mnowCallback != null) {
					mnowCallback.onPostExecute(HttpCode.HTTP_Request_Failure_CODE, text);
				}
			}
				break;
			}
			Log.d(TAG, text + "----123");
		} else {
			Log.d(TAG, "失败");
		}

		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
