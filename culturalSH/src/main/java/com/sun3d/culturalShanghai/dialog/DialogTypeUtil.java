package com.sun3d.culturalShanghai.dialog;

import android.app.Dialog;
import android.view.WindowManager;

import com.sun3d.culturalShanghai.MyApplication;

/**
 * DialogType,每一个dialog用途
 * 
 * @author yangyoutao
 * 
 */
public class DialogTypeUtil {
	/**
	 * Intent 传递的key值
	 */
	public static final String DialogType = "DialogType";
	/**
	 * Intent 传递的value值
	 */
	public static final String DialogContent = "DialogContent";

	/**
	 * 消息DialogType
	 */
	public static class MessageDialog {
		/**
		 * 呼叫电话
		 */
		public static final int MSGTYPE_TELLPHONE = 10;
		/**
		 * 显示文本信息
		 */
		public static final int MSGTYPE_SHOW_TEXT = 11;
		/**
		 * 预定活动
		 */
		public static final int MSGTYPE_SCHEDULE_ACTIVITY = 12;
		/**
		 * 预定场馆
		 */
		public static final int MSGTYPE_SCHEDULE_VENUE = 13;
		/**
		 * 取消场馆预约
		 */
		public static final int MSGTYPE_CANCEL_VENUE = 14;
		/**
		 * 取消待审核
		 */
		public static final int MSGTYPE_CANCEL_CHECK_ROOM = 40;

		/**
		 * 取消活动预约
		 */
		public static final int MSGTYPE_CANCEL_ACTIVITY = 15;
		/**
		 * 确认预定
		 */
		public static final int MSGTYPE_TRUE_SCHEDULE = 16;
		/**
		 * 版本更新
		 */
		public static final int MSGTYPE_VERSION_UPDATER = 17;
		/**
		 * 团体申请加入
		 */
		public static final int MSGTYPE_GROUP_ADD = 18;
		/**
		 * 非团体会员点评场馆提示消息
		 */
		public static final int MSGTYPE_NOGROUP_SHOW = 19;
		/**
		 * 电子票
		 */
		public static final int MSGTYPE_ELECTRONIC_TICKET = 20;
		/**
		 * QQ与weixin分享
		 */
		public static final int MSGTYPE_ELECTRONIC_SHARE = 21;
		/**
		 * 待審核的場館取消
		 */
		public static final int MSGTYPE_CANCEL_CHECK_ORDER_ACTIVITY = 22;
	}

	/**
	 * 用户界面Dialog相关Type
	 */
	public static class UserDialogType {
		/**
		 * 快速登录
		 */
		public static final int USER_FAST_LOGIN = 21;
		/**
		 * 登录
		 */
		// public static final int USER_LOGIN = 22;
		/**
		 * 用户注册
		 */
		public static final int USER_REGUSTERED = 23;
		/**
		 * 忘记密码
		 */
		public static final int USER_FORGOT_PASSWORD = 24;
		/**
		 * 找回密码
		 */
		public static final int USER_FIND_PASSWORD = 25;
		/**
		 * 用户个性设置
		 */
		public static final int USER_PERSONALITY_SETTING = 26;
		/**
		 * 编辑头像
		 */
		public static final int USER_EDIT_USERICON = 27;
		/**
		 * 编辑昵称
		 */
		public static final int USER_EDIT_LINKNAME = 28;
		/**
		 * 编辑生日Birthday
		 */
		public static final int USER_EDIT_BIRTHDAY = 29;
		/**
		 * 编辑地区
		 */
		public static final int USER_EDIT_REGION = 290;
		/**
		 * 绑定手机号
		 */
		public static final int USER_BINDING_PHONE = 291;
		/**
		 * 绑定邮箱
		 */
		public static final int USER_BINDING_EMAIL = 292;
		/**
		 * 修改密码
		 */
		public static final int USER_EDIT_PASSWORD = 293;
		/**
		 * 退出登录
		 */
		public static final int USER_RETURN = 333;
		/**
		 * 编辑性别
		 */
		public static final int USER_EDIT_SEX = 294;
		/**
		 * 编辑团体图片
		 */
		public static final int USER_EDIT_GROUPPICTRUE = 295;
		/**
		 * 编辑评论图片
		 */
		public static final int USER_EDIT_COMMENTPICTRUE = 296;
	}

	/**
	 * 第三方分享DiaLog的平台区别Type
	 */
	public static class ThirdPartyDialogType {
		/**
		 * 第三方分享
		 */
		public static final int THIRDPARTY_SHARE = 30;
		/**
		 * 活动支付
		 */
		public static final int THIRDPARTY_PAY = 31;
		/**
		 * 快速登录
		 */
		public static final int THIRDPARTY_FAST_LOGIN = 32;
	}

	/**
	 * 设置dialog全屏
	 * 
	 * @param dg
	 */
	public static void setFullScreen(Dialog dg) {
		WindowManager.LayoutParams lp = dg.getWindow().getAttributes();
		lp.width = MyApplication.getWindowWidth();// 设置宽度
		lp.height = MyApplication.getWindowHeight();// 设置高度
		dg.getWindow().setAttributes(lp);
	}
}
