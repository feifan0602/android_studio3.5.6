/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.sun3d.culturalShanghai.thirdparty;

import java.util.HashMap;

import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.mob.tools.utils.UIHandler;
import com.sun3d.culturalShanghai.Util.ToastUtil;

/**
 * 演示获取用户资料
 * <p>
 * 启动页面时传递一个int类型的字段type，用于标记获取自己的资料（type = 0） 还是别人的资料（type =
 * 1）。如果尝试获取别人的资料，示例代码会获取不同 平台ShareSDK的官方帐号的资料。
 * <p>
 * 如果资料获取成功，会通过{@link JsonPage}展示
 */
public class GetInforPage implements Callback, PlatformActionListener {
	private String TAG = "GetInforPage";
	private static GetInforPage page = new GetInforPage();
	/** 官方新浪微博 */
	public static final String SDK_SINAWEIBO_UID = "3189087725";
	/** 官方腾讯微博 */
	public static final String SDK_TENCENTWEIBO_UID = "shareSDK";

	public static void getInfor(String platformName) {
		Platform plat = ShareSDK.getPlatform(platformName);
		String name = plat.getName();
		plat.setPlatformActionListener(page);
		String account = null;
		if ("SinaWeibo".equals(name)) {
			account = SDK_SINAWEIBO_UID;
		} else if ("TencentWeibo".equals(name)) {
			account = SDK_TENCENTWEIBO_UID;
		}
		plat.showUser(account);
	}

	public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);

		Message msg2 = new Message();
		msg2.what = 1;
		msg2.obj = res;
		UIHandler.sendMessage(msg2, this);
	}

	public void onError(Platform palt, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		UIHandler.sendMessage(msg, this);
	}

	public void onCancel(Platform plat, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	/** 处理操作结果 */
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 1: {

			String str = JsonPage.setData((HashMap<String, Object>) msg.obj);
			Log.d(TAG, str);
			ToastUtil.showToast(str);
		}
			break;
		default: {
			Platform plat = (Platform) msg.obj;
			String text = ActionToString.actionToString(msg.arg2);
			switch (msg.arg1) {
			case 1: {
				// 成功
				text = plat.getName() + " completed at " + text;
			}
				break;
			case 2: {
				// 失败
				text = plat.getName() + " caught error at " + text;
			}
				break;
			case 3: {
				// 取消
				text = plat.getName() + " canceled at " + text;
			}
				break;
			}
			Log.d(TAG, text);
			ToastUtil.showToast(text);
		}
			break;
		}
		return false;
	}

}
