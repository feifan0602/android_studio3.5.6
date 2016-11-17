package com.sun3d.culturalShanghai.thirdparty;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.mob.tools.utils.UIHandler;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ShareInfo;

public class MyShare implements Callback, PlatformActionListener {
	private static MyShare mMyShare = new MyShare();
	private String TAG = "MyShare";
	private static String MyWebUrl = "http://www.wenhuayun.cn";

	/**
	 * 启动分享
	 * 
	 * @param platform
	 * @param mConetxt
	 */
	public static void showShare(String platform, Context mConetxt,
			ShareInfo shareInfo) {

		// if (platform.equals(ShareName.QQ)) {

		//
		// ShareParams qqsp = new ShareParams();
		// qqsp.setShareType(Platform.SHARE_WEBPAGE);
		// qqsp.setText(shareInfo.getContent());
		// // qqsp.setImageUrl(shareInfo.getImageUrl());
		// qqsp.setTitle(shareInfo.getTitle());
		// Platform qqs = ShareSDK.getPlatform(mConetxt, QQ.NAME);
		// qqs.setPlatformActionListener(mMyShare); // 设置分享事件回调
		// // 执行图文分享
		// qqs.share(qqsp);
		// } else {
		if (platform.equals(ShareName.QQ)) {
			if (!GetTokenPage.isApkInstalled(mConetxt,
					GetTokenPage.QQpackageName)) {
				ToastUtil.showToast("QQ没有安装.");
				return;
			}
		} else if (platform.equals(ShareName.SinaWeibo)) {
			if (!GetTokenPage.isApkInstalled(mConetxt,
					GetTokenPage.SinapackageName)) {
				ToastUtil.showToast("新浪微博没有安装.");
				return;
			}
		} else {
			if (!GetTokenPage.isApkInstalled(mConetxt,
					GetTokenPage.WeixinpackageName)) {
				ToastUtil.showToast("微信没有安装.");
				return;
			}
		}
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setTitle(shareInfo.getTitle());
		oks.setImageUrl(shareInfo.getImageUrl());
		if (platform.equals(ShareName.SinaWeibo)) {
			oks.setText(shareInfo.getContent() + shareInfo.getContentUrl());
		} else {
			oks.setText(shareInfo.getContent());
		}
		Log.i("tag", "shareurl : " + shareInfo.getContentUrl());
		oks.setSite(shareInfo.getContent());
		oks.setUrl(shareInfo.getContentUrl());
		oks.setTitleUrl(shareInfo.getContentUrl());
		oks.setSiteUrl(shareInfo.getContentUrl());
		oks.setUrl(shareInfo.getContentUrl());
		if (!TextUtils.isEmpty(platform)) {
			oks.setPlatform(platform);
			oks.setDialogMode();
			oks.setCallback(mMyShare);
		}
		oks.show(mConetxt);
		// }
	}

	/**
	 * 启动分享
	 * 
	 * @param platform
	 * @param mConetxt
	 */
	public static void showShare(String platform, Context mConetxt,
			String pathUrl) {
		if (platform.equals(ShareName.QQ)) {
			if (!GetTokenPage.isApkInstalled(mConetxt,
					GetTokenPage.QQpackageName)) {
				ToastUtil.showToast("QQ没有安装.");
				return;
			}
		} else if (platform.equals(ShareName.SinaWeibo)) {
			if (!GetTokenPage.isApkInstalled(mConetxt,
					GetTokenPage.SinapackageName)) {
				ToastUtil.showToast("新浪微博没有安装.");
				return;
			}
		} else {
			if (!GetTokenPage.isApkInstalled(mConetxt,
					GetTokenPage.WeixinpackageName)) {
				ToastUtil.showToast("微信没有安装.");
				return;
			}
		}
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		oks.setImagePath(pathUrl);
		if (!TextUtils.isEmpty(platform)) {
			oks.setPlatform(platform);
			oks.setDialogMode();
			oks.setCallback(mMyShare);
		}
		oks.show(mConetxt);
		// }
	}

	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}

	/** 通过Toast显示操作结果 */
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: {
			// 成功
			ForwardingIntegral();
			text = plat.getName() + "分享成功";
			ToastUtil.showToast(text);
		}
			break;
		case 2: {
			// 失败
			text = plat.getName() + "分享失败";
			ToastUtil.showToast(text);
		}
			break;
		case 3: {
			// 取消
			text = plat.getName() + "分享取消";
			ToastUtil.showToast(text);
		}
			break;
		}
		Log.d(TAG, text);

		return false;
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = arg1;
		msg.obj = arg0;
		UIHandler.sendMessage(msg, this);
	}

	/**
	 * 分享成功 就可以得到积分
	 */
	private void ForwardingIntegral() {
		/**
		 * 这个是要传入到服务器的参数
		 */
		Map<String, String> mParams = new HashMap<String, String>();

		mParams.put("userId", MyApplication.loginUserInfor.getUserId());
		mParams.put("shareLink", MyApplication.shareLink);

		String urlString = "";
		// 第一次取数据的时候
		urlString = HttpUrlList.MyEvent.WFF_FORWARDINGINTEGRAL;
		MyHttpRequest.onHttpPostParams(urlString, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "-得到积分可好  --"
								+ resultStr);
					}
				});

	}
}
