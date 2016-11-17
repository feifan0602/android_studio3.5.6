package com.sun3d.culturalShanghai.Util;

import android.util.Log;

import com.sun3d.culturalShanghai.http.HttpCode;

/**
 * 服务器返回码判断
 * 
 * @author yangyoutao
 * 
 */
public class ErrorStatusUtil {
	/**
	 * 用户相关错误码
	 * 
	 * @param status
	 */
	public static void seachServerStatus(int status, String mes) {
		switch (status) {
		case HttpCode.ServerCode_User.ServerCode_unSeachUser:
			ToastUtil.showToast(mes);
			break;
		case HttpCode.ServerCode_User.ServerCode_NullPassWord:
			ToastUtil.showToast("密码为空");
			break;
		case HttpCode.ServerCode_User.ServerCode_ErrorPhone:
			ToastUtil.showToast(mes);
			break;
		case HttpCode.ServerCode_User.ServerCode_NullPhone:
			ToastUtil.showToast("手机号码为空！");
			break;
		case HttpCode.ServerCode_User.ServerCode_NullVip:
			ToastUtil.showToast(mes);
			break;
		case HttpCode.ServerCode_User.ServerCode_repeatUserName:
			ToastUtil.showToast(mes);
			break;
		case HttpCode.ServerCode_User.ServerCode_repeatUserPhone:
			ToastUtil.showToast(mes);
			break;
		case HttpCode.ServerCode_User.ServerCode_unUserId:
			ToastUtil.showToast(mes);
			break;
		case HttpCode.ServerCode_User.ServerCode_updaterError:
			ToastUtil.showToast("更新失败！");
			break;
		case 10107:
			ToastUtil.showToast(mes);
			break;
		case 10108:
			ToastUtil.showToast(mes);
			break;
		case 10119:
			ToastUtil.showToast("用户验证码或手机号码错误");
			break;
		case 10120:
			ToastUtil.showToast("2次输入密码不一致");
			break;
		case 10121:
			ToastUtil.showToast("用户id或展馆id缺失");
			break;
		case 10122:
			ToastUtil.showToast("用户已收藏该展馆");
			break;
		case HttpCode.ServerCode_User.REGISTER_REPEAT:
			ToastUtil.showToast("用户名已经存在");
			break;
		case HttpCode.ServerCode_User.REGITER_CODE_ERROR:
			ToastUtil.showToast("验证码错误");
			break;
		case HttpCode.ServerCode_Phone.PHONE_EXIST:
			ToastUtil.showToast("手机号已经存在");
			break;
		case HttpCode.ServerCode_Phone.PHONE_EXCEED:
			ToastUtil.showToast("发送次数超过3次");
			break;
		case HttpCode.ServerCode_Phone.PHONE_ERROR:
			ToastUtil.showToast("发送短信失败");
			break;
		default:
			ToastUtil.showToast(mes);
			break;
		}
	}

	/**
	 * 找回密码发送验证码返回状态解析
	 * 
	 * @param status
	 * @param str
	 */
	public static void getforgetPwCodestatus(int status, String str) {
		switch (status) {
		case 1:
			ToastUtil.showToast("短信发送失败！");
			break;
		case 14141:
			ToastUtil.showToast("手机号码未注册！");
			break;
		case 13106:
			ToastUtil.showToast("发送次数超过三次。");
			break;
		case 14144:
			ToastUtil.showToast("手机号码不存在！");
			break;
		default:
			ToastUtil.showToast(str);
			Log.i("tag", str);
			break;
		}

	}

	/**
	 * 忘记密码 重置密码返回解析
	 * 
	 * @param status
	 * @param str
	 */
	public static void getforgetPwFixPWstatus(int status, String str) {
		switch (status) {
		case 14147:
			ToastUtil.showToast("验证码错误！");
			break;
		case 14141:
			ToastUtil.showToast("手机号码未注册！");
			break;
		case 14145:
			ToastUtil.showToast("输入密码为空！");
			break;
		case 14146:
			ToastUtil.showToast("新密码与原密码相同！");
			break;
		default:
			ToastUtil.showToast(str);
			break;
		}

	}

	/**
	 * 电子票信息返回
	 */
	public static void getElectronicTicket(int status) {
		switch (status) {
		case 11111:
			ToastUtil.showToast("活动订单id缺失");
			break;
		case 11112:
			ToastUtil.showToast("系统错误");
			break;

		default:
			break;
		}
	}
}
