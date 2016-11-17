package com.sun3d.culturalShanghai.Util;

import java.util.List;

import com.sun3d.culturalShanghai.object.UserBehaviorInfo;

/**
 * 
 * @author liningkang 常量
 */
public class AppConfigUtil {

	public static final String FILE_NAME = "CulturalSh";
	public static final String FILE_USER = "user";
	public static final String FILE_CACHE = "cache";
	public static final String FILE_IAMGECACHE = "image_cache";
	public static final String USER_LOCAT_PATH = "user_locat_path";
	public static final int USER_REQUEST_CODE = 100;
	public static final int RESULT_LOCAT_CODE = 101;
	public static final int PERSONAL_RESULT_USER_CODE = 201;
	public static final int PERSONAL_RESULT_VENUE_CODE = 202;
	public static final int PERSONAL_RESULT_MESSGE_CODE = 203;
	public static final String PRE_FILE_USER = "save_user";
	public static final String PRE_FILE_ADDRESS = "save_address";
	public static final String PRE_FILE_SHAREHISTORY = "save_history";
	public static final String PRE_FILE_VENUE_ADDRESS = "save_Venueaddress";
	public static final String PRE_FILE_NAME = "save_name";
	public static final String PRE_FILE_TYPE = "save_type";
	public static final String PRE_FILE_ALL_AREA = "save_all_area";
	public static final String PRE_FILE_ALL_TAG = "save_all_tag";
	public static final String PRE_FILE_NAME_KEY = "save_name_key";
	public static final String APP_LOAD = "app_load";
	public static final String INTENT_TYPE = "intent_type";
	public static final String APP_ENCODING = "utf-8";
	public static final String APP_MIMETYPE = "text/html";

	public static final int LOADING_LOGIN_BACK = 102;
	public static final String INTENT_SHAREINFO = "shareInfo";
	public static final String INTENT_SCREENINFO = "screenInfo";
	public static final String SCREENSHOT = "/screenshot.jpg";

	public static final String USER_TYPE_TAG = "userTypeTag";

	/**
	 * 用户常量
	 */
	public static class User {
		public static final String USER_IS_LOGIN = "isLogin";
		public static final String USER_ICON = "userIcon.jpg";
	}

	public static class Page {
		public static final int PageDefaHight = 200;
	}

	public static class UploadImage {
		public static String KEY = "file";
	}

	public static class Group {
		public static final int ADD_GROUP_BACK = 202;
	}

	public static class Local {
		public static String ImageTag = "localwenhuayun";
	}

	public static class LocalLocation {
		public static String Location_latitude = "";
		public static String Location_longitude = "";
	}
}
