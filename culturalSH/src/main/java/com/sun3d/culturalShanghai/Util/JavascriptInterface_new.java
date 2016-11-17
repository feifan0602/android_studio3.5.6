package com.sun3d.culturalShanghai.Util;

import org.json.JSONObject;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.ActivityOrderDetail;
import com.sun3d.culturalShanghai.activity.ActivityRoomDateilsActivity;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.activity.SearchListActivity;
import com.sun3d.culturalShanghai.activity.SearchTagListActivity;
import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.calender.CalendarActivity;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;
import com.sun3d.culturalShanghai.object.MyOrderInfo;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * @author yangyoutao
 */

public class JavascriptInterface_new {
	private Activity context;
	private BannerWebView web_view;
	private String TAG = "JavascriptInterface_new";

	public JavascriptInterface_new(Activity context, BannerWebView web_activity) {
		this.context = context;
		this.web_view = web_activity;
	}

	public JavascriptInterface_new(Activity context) {
		this.context = context;
	}

	/**
	 * 获取手机当前的网络情况
	 * 
	 * @return
	 */
	@JavascriptInterface
	public int currentNetworkState() {

		int netNum = 0;
		if (NetWorkUtil.isConnectedByWifi()) {
			netNum = 1;
		} else if (NetWorkUtil.isConnected()) {
			netNum = 2;
		} else {
			netNum = 0;
		}
		Log.i(TAG, "currentNetworkState==  " + netNum);
		return netNum;
	}

	/**
	 * 获取用户的信息
	 * 
	 * @return
	 */
	@JavascriptInterface
	public String appsdk_getUserInfo() {
		Log.i(TAG, "appsdk_getUserInfo==  ");
		JSONObject json = new JSONObject();
		try {
			json.put("userId", MyApplication.loginUserInfor.getUserId());
			json.put("userName", MyApplication.loginUserInfor.getUserName());
			if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
					|| AppConfigUtil.LocalLocation.Location_longitude
							.equals("0.0")) {
				json.put("userLat", MyApplication.Location_latitude);
				json.put("userLon", MyApplication.Location_longitude);
			} else {
				json.put("userLat",
						AppConfigUtil.LocalLocation.Location_latitude + "");
				json.put("userLon",
						AppConfigUtil.LocalLocation.Location_longitude + "");
			}
			json.put("userLat", MyApplication.Location_latitude);
			json.put("userLon", MyApplication.Location_longitude);
			json.put("platform", "android");
			json.put("appVersion", MyApplication.getVersionName(context));
			json.put("sysVersion", MyApplication.currentapiVersion);
			Log.i(TAG, "appsdk_getUserInfo  ==  " + json.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	/**
	 * 复制用户信息给APP
	 * 
	 * @param jsonStr
	 */
	@JavascriptInterface
	public void sendUserInfoToApp(String jsonStr) {
		Log.i(TAG, "sendUserInfoToApp==  " + jsonStr);
	}

	/**
	 * 通过APP 访问H5的下一级页面
	 * 
	 * @param jsonStr
	 */
	@JavascriptInterface
	public void accessDetailPageByApp(String Url) {
		Log.i(TAG, "accessDetailPageByApp==  " + Url);
		Intent i = new Intent();
		i.setClass(context, BannerWebView.class);
		Bundle bundle = new Bundle();
		bundle.putString("url", Url);
		i.putExtras(bundle);
		context.startActivity(i);

	}

	/**
	 * 活动或活动室预定完后返回至APP详情页面
	 * 
	 * @param pageType
	 */
	@JavascriptInterface
	public void appPageJumpAfterBooking(int pageType) {
		Log.i(TAG, "appPageJumpAfterBooking==  " + pageType);
		if (pageType == 1) {
			// 返回活动详情
			handler.sendEmptyMessage(4);
		} else {
			// 返回活动室详情
			handler.sendEmptyMessage(4);
		}
	}

	/**
	 * 更改APP导航条标题
	 * 
	 * @param navTitle
	 */
	@JavascriptInterface
	public void changeNavTitle(String navTitle) {
		Log.i(TAG, "changeNavTitle==  " + navTitle);
		Message msg = new Message();
		msg.obj = navTitle;
		msg.what = 1;
		handler.sendMessage(msg);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (web_view != null && web_view.middle_tv != null) {
					web_view.middle_tv.setText(msg.obj.toString());
				}
				break;
			case 2:
				if (web_view != null && web_view.right_iv != null) {
					web_view.right_iv.setVisibility(View.VISIBLE);
				}

				break;
			case 3:
				if (web_view != null && web_view.right_iv != null) {
					web_view.right_iv.setVisibility(View.INVISIBLE);
				}
				break;
			case 4:
				if (web_view != null) {
					web_view.finish();
				}
				break;
			default:
				break;
			}
		};
	};

	@JavascriptInterface
	public void accessAppPage(int pageType) {
		Log.i(TAG, "accessAppPage==  " + pageType);
		accessAppPage(pageType, null);
	}

	/**
	 * 显示或隐藏APP
	 */
	@JavascriptInterface
	public void setAppShareButtonStatus(boolean isShow) {
		Log.i(TAG, "setAppShareButtonStatus==  " + isShow);
		if (isShow) {
			handler.sendEmptyMessage(2);
		} else {
			handler.sendEmptyMessage(3);
		}
	}

	/**
	 * H5访问APP页面 url 详情页ID 或 搜索关键字 都没有 传空字符串
	 * 
	 * @param pageType
	 */
	@JavascriptInterface
	public void accessAppPage(int pageType, String url) {
		Log.i(TAG, "accessAppPage  ==  " + pageType + "  url==  " + url);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		MyOrderInfo info = new MyOrderInfo();
		switch (pageType) {
		// 活动详情
		case 1:
			intent.setClass(context, ActivityDetailActivity.class);
			intent.putExtra("eventId", url);
			break;
		// 场馆详情
		case 2:
			intent.setClass(context, VenueDetailActivity.class);
			intent.putExtra("venueId", url);
			break;
		// 活动室详情
		case 3:
			intent.setClass(context, ActivityRoomDateilsActivity.class);
			intent.putExtra("Id", url);
			break;
		// 活动列表
		case 4:
			intent.setClass(context, EventListActivity.class);
			bundle.putString("KeyWord", url);
			bundle.putString("ActivityOrVenue", "activity");
			intent.putExtras(bundle);
			break;
		// 场馆列表
		case 5:
			intent.setClass(context, VenueListActivity.class);
			bundle.putString("KeyWord", url);
			bundle.putString("ActivityOrVenue", "venue");
			intent.putExtras(bundle);
			break;
		// 个人中心
		case 6:
			intent.setClass(context, MainFragmentActivity.class);
			MyApplication.change_fragment = 4;
			break;
		// 我的订单列表
		case 7:
			intent = new Intent(context, MyOrderActivity.class);
			MyApplication.room_activity = Integer.valueOf(url);
			break;
		// 活动订单详情
		case 8:
			intent.setClass(context, ActivityOrderDetail.class);
			bundle.putString("activityOrderId", url);
			bundle.putString("ActivityOrRoom", "0");
			// 2 表示 历史的订单
			bundle.putInt("now_histroy", 0);
			intent.putExtras(bundle);
			// 这里 为了不改变原先的结构 而如此设置
			MyActivityBookInfo mybookinfo = new MyActivityBookInfo();
			mybookinfo.setActivityOrderId(url);
			info.setBookInfo(mybookinfo);
			intent.putExtra("orderInfo", info);
			break;
		// 活动室订单详情
		case 9:
			intent.setClass(context, ActivityOrderDetail.class);
			bundle.putString("activityOrderId", url);
			bundle.putString("ActivityOrRoom", "1");
			// 2 表示 历史的订单
			bundle.putInt("now_histroy", 0);
			intent.putExtras(bundle);
			// 这里 为了不改变原先的结构 而如此设置
			MyActivityRoomInfo myroomInfo = new MyActivityRoomInfo();
			myroomInfo.setRoomOrderId(url);
			info.setRoomInfo(myroomInfo);
			intent.putExtra("orderInfo", info);
			break;
		// APP登陆页面
		case 10:
			intent = new Intent(context, UserDialogActivity.class);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
			break;
		// 返回到社团的首页
		case 11:
			intent = new Intent(context, MainFragmentActivity.class);
			MyApplication.change_fragment = 3;
			break;
		// 文化日历列表
		case 12:
			intent = new Intent(context, CalendarActivity.class);
			break;
		// 带筛选的活动列表
		case 13:
			intent = new Intent(context, SearchTagListActivity.class);
			bundle.putString("TagIdAndTagName", url);
			break;
		// 退出webview
		case 100:
			context.finish();
			break;
		default:
			break;

		}
		if (pageType == 10) {
			context.startActivityForResult(intent,
					AppConfigUtil.LOADING_LOGIN_BACK);
		} else {
			context.startActivity(intent);
		}
	}

	/**
	 * 获取 h5 的網頁正文 和 图片鏈接 1 表示 正文 2 表示 图片链接
	 * 
	 * @param type
	 * @param content
	 * @return
	 */
	@JavascriptInterface
	public JSONObject getHtmlContentOrImg(int type, String content) {
		if (type == 1) {
			// 正文 内容
			content = MyApplication.replaceBlank(content);
			// content.replaceAll("\n", "");
			if (content.length() > 50) {
				content = content.substring(0, 50);
			}
			web_view.content_text = content;
		} else if (type == 2) {
			web_view.img_url = content;
		}
		JSONObject json = new JSONObject();

		return json;
	}

	@JavascriptInterface
	public boolean userIsLogin() {
		boolean login = true;
		if (!MyApplication.UserIsLogin) {
			// 没有登陆
			login = false;
		} else {
			login = true;
		}
		Log.i(TAG, "userIsLogin  ==  " + login);
		return login;
	}

	/**
	 * 获取用户的信息
	 * 
	 * @return
	 */
	@JavascriptInterface
	public String getUserInfo() {
		Log.i(TAG, "getUserInfo  ==  ");
		JSONObject json = new JSONObject();
		try {
			json.put("userId", MyApplication.loginUserInfor.getUserId());
			json.put("userName", MyApplication.loginUserInfor.getUserName());
			if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
					|| AppConfigUtil.LocalLocation.Location_longitude
							.equals("0.0")) {
				json.put("userLat", MyApplication.Location_latitude);
				json.put("userLon", MyApplication.Location_longitude);
			} else {
				json.put("userLat",
						AppConfigUtil.LocalLocation.Location_latitude + "");
				json.put("userLon",
						AppConfigUtil.LocalLocation.Location_longitude + "");
			}
			json.put("userLat", MyApplication.Location_latitude);
			json.put("userLon", MyApplication.Location_longitude);
			json.put("platform", "android");
			json.put("appVersion", MyApplication.getVersionName(context));
			json.put("sysVersion", MyApplication.currentapiVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "getUserInfo  ==  " + json.toString());
		return json.toString();
	}

	@JavascriptInterface
	public boolean appsdk_UserIsLogin() {

		boolean login = true;
		if (!MyApplication.UserIsLogin) {
			// 没有登陆
			login = false;
		} else {
			login = true;
		}
		Log.i(TAG, "appsdk_UserIsLogin  ==  " + login);
		return login;
	}

	@JavascriptInterface
	public void loadMagazine() {
	}

	@JavascriptInterface
	public void showSource(String html) {
		Log.d("HTML", html);
	}

	@JavascriptInterface
	public void onAndroidShare(String url) {
		Intent intent = new Intent(context, UserDialogActivity.class);
		FastBlur.getScreen((Activity) context);
		ShareInfo info = new ShareInfo();
		info.setContentUrl(url);
		intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
		context.startActivity(intent);

	}
}
