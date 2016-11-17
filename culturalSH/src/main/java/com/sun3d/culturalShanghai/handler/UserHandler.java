package com.sun3d.culturalShanghai.handler;

import java.util.HashMap;
import java.util.Map;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.MD5Util;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;

/**
 * 用户操作类
 * 
 * @author yangyoutao
 * 
 */
public class UserHandler {
	/**
	 * 登录到服务器
	 * 
	 * @param uername
	 * @param password
	 * @param mCallback
	 */
	public static void fastLogin(String uername, String password, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMobileNo", uername);
		params.put("userPwd", MD5Util.toMd5(password));
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_fasrlogin, params, mCallback);
	}

	/**
	 * 修改用户昵称，性别，生日(性别：1为男，2为女；生日格式：2015-02-12)
	 * 
	 * @param name
	 * @param sex
	 * @param birth
	 * @param mCallback
	 */
	public static void setUserNameSexBirth(String name, int sex, String birth, String region,
			HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		if (name != null && name.length() > 0) {
			params.put(HttpUrlList.UserUrl.REGISTER_NAME, name);
		}
		if (sex != -1) {
			params.put("userSex", String.valueOf(sex));
		} else {
			params.put("userSex", "0");
		}
		if (birth != null && birth.length() > 0) {
			params.put("userBirthStr", birth);
		}
		if (region != null && region.length() > 0) {
			params.put("userArea", region);
		}
		

		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_setUserInfo, params, mCallback);
	}

	/**
	 * 发送手机号码 获取验证码。
	 * 
	 * @param phone
	 * @param mCallback
	 */
	public static void sendPhoneCode(String phone, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("userMobileNo", phone);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_getphonecodeInfo, params, mCallback);

	}

	/**
	 * 绑定手机号码(更改为个人信息)
	 * 
	 * @param phone
	 * @param code
	 * @param mCallback
	 */
	public static void bindPhone(String phone, String code, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.HTTP_USER_ID, MyApplication.getInstance().loginUserInfor.getUserId());
		params.put(HttpUrlList.UserUrl.REGISTER_NAME, MyApplication.getInstance().loginUserInfor.getUserName());
		params.put(HttpUrlList.UserUrl.REGISTER_SEX, MyApplication.getInstance().loginUserInfor.getUserSex());
		params.put(HttpUrlList.UserUrl.REGISTER_BIRTH, MyApplication.getInstance().loginUserInfor.getUserBirth());
		params.put("userTelephone", phone);
//		params.put("userMobileNo", phone);
//		params.put("registerCode", code);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_setUserInfo, params, mCallback);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param olepw
	 * @param newpw
	 * @param mCallback
	 */
	public static void setUserPassWord(String olepw, String newpw, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("password", MD5Util.toMd5(olepw));
		params.put("newPassword", MD5Util.toMd5(newpw));
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_setuserpasswordInfo, params,
				mCallback);
	}

	/**
	 * 修改用户邮箱
	 * 
	 * @param email
	 * @param mCallback
	 */
	public static void setUserEmail(String email, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("userEmail", email);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.USER_SETEMAILINFO, params, mCallback);
	}

	/**
	 * 忘记密码，发送验证码
	 * 
	 * @param phone
	 * @param mCallback
	 */
	public static void getForgetPwgetCode(String phone, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMobileNo", phone);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.FORGETPW_GETCODE, params, mCallback);
	}

	/**
	 * 找回密码
	 * 
	 * @param phone
	 * @param newpw
	 * @param code
	 * @param mCallback
	 */
	public static void findPw(String phone, String newpw, String code, HttpRequestCallback mCallback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMobileNo", phone);
		params.put("newPassword", MD5Util.toMd5(newpw));
		params.put("code", code);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.FINDPW_FIXPW, params, mCallback);
	}
}
