package com.sun3d.culturalShanghai.http;

public class HttpCode {
	/**
	 * 返回页面状态码
	 */
	public static final int BACK = 101;
	/**
	 * 返回页面状态码(活动室)
	 */
	public static final int BACK_ROOM = 202;
	/**
	 * 審核的返回狀態嗎
	 */
	public static final int BACK_CHCEK_ROOM =303;
	/**
	 * 返回页面状态码(发送QQ好友)
	 */
	public static final int BACK_QQ = 222;
	/**
	 * 我的订单 中点击进去 返回刷新页面
	 */
	public static final int BACK_MYORDER = 221;
	/**
	 * 接口请求失败
	 */
	public static final int HTTP_Request_Failure_CODE = 0;
	/**
	 * 接口请求成功
	 */
	public static final int HTTP_Request_Success_CODE = 1;
	/**
	 *3.5.4  状态改变  接口请求成功
	 */
	public static final int HTTP_Request_OK = 1;
	/**
	 * Post请求方式
	 */
	public static final int HTTP_RequestType_Post = 2;
//	/**
//	 * 3.5.4JSON请求
//	 */
	public static final int HTTP_RequestType_JSONPost = 4;
	/**
	 * Get请求方式
	 */
	public static final int HTTP_RequestType_Get = 3;
	
//	public static final int HTTP_RequestType_Json = 4;

	/**
	 * 服务端数据返回
	 * 
	 */
	public static class serverCode {
		/**
		 * 数据返回成功
		 */
		public static final int DATA_Success_CODE = 0;

	}

	/**
	 * 注册、登录服务端返回状态码
	 * 
	 */
	public static class ServerCode_User {
		/**
		 * 登录成功
		 */
		public static final int ServerCode_Success = 0;
		/**
		 * 手机号码为空
		 */
		public static final int ServerCode_NullPhone = 11101;
		/**
		 * 密码为空
		 */
		public static final int ServerCode_NullPassWord = 11103;
		/**
		 * 会员未激活
		 */
		public static final int ServerCode_NullVip = 11102;
		/**
		 * 用户名密码错误
		 */
		public static final int ServerCode_ErrorPhone = 12105;
		/**
		 * 查无此人
		 */
		public static final int ServerCode_unSeachUser = 13108;
		/**
		 * 用户ID缺失
		 */
		public static final int ServerCode_unUserId = 10111;
		/**
		 * 用户名已存在
		 */
		public static final int ServerCode_repeatUserName = 10113;
		/**
		 * 用户手机号码已存在
		 */
		public static final int ServerCode_repeatUserPhone = 10114;
		/**
		 * 更新信息成功
		 */
		public static final int ServerCode_updaterSuccess = 10115;
		/**
		 * 更新信息失败
		 */
		public static final int ServerCode_updaterError = 10116;

		/**
		 * 用户名已经存在
		 */
		public static final int REGISTER_REPEAT = 10105;
		/**
		 * 验证码错误
		 */
		public static final int REGITER_CODE_ERROR = 13120;
	}

	/**
	 * 手机操作返回状态码
	 * 
	 */
	public static class ServerCode_Phone {
		/**
		 * 手机号已经存在
		 */
		public static final int ServerCode_RepeatPhone = 10118;
		/**
		 * 手机号码已经存在
		 */
		public static final int PHONE_EXIST = 13105;
		/**
		 * 发送次数超过3次
		 */
		public static final int PHONE_EXCEED = 13106;
		/**
		 * 发送短信失败
		 */
		public static final int PHONE_ERROR = 13107;
	}

	/**
	 * 评论返回状态码
	 * 
	 */
	public static class ServerCode_Comment {
		/**
		 * 评论返回
		 */
		public static final int CODE_BACK = 101;
		/**
		 * 登录成功
		 */
		public static final int ServerCode_Success = 0;
		/**
		 * 超过五次评论限制
		 */
		public static final int ServerCode_MaxNumCount = 10107;
	}
}
