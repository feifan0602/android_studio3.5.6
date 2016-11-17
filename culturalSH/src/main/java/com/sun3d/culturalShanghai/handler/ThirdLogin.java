package com.sun3d.culturalShanghai.handler;

import java.util.HashMap;
import java.util.Map;

import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;

/**
 * 第三方登录
 * 
 * @author yangyoutao
 * 
 */
public class ThirdLogin {
	public static final String WenHuaYunQ = "1";
	public static final String QQ = "2";
	public static final String SINAWeibo = "3";
	public static final String Weixin = "4";

	public static void thirdlogin(String openId, String type, String userBirthStr,
			String userNickName, String userHeadImgUrl, int userSex, HttpRequestCallback mCallback) {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("operId", openId);
		params.put("registerOrigin", type);
		params.put("userBirthStr", userBirthStr);
		params.put("userNickName", userNickName);
		params.put("userHeadImgUrl", userHeadImgUrl);
		params.put("userSex", String.valueOf(userSex));
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_thirdlogin, params, mCallback);
	}

}
