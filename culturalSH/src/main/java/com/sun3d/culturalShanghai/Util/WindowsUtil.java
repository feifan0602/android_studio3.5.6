package com.sun3d.culturalShanghai.Util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.sun3d.culturalShanghai.MyApplication;

public class WindowsUtil {
	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Context mContext) {

		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();

		return width;
	}

	/** 获取屏幕的高度 */
	public final static int getwindowsHight(Context mContext) {
		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		int height = manager.getDefaultDisplay().getHeight();
		return height;

	}

	/**
	 * 获取虚拟菜单栏的高度
	 * 
	 * @param mContext
	 * @return
	 */

	public static int getDownMenuHeight(Context mContext) {
		int height = 0;
		Resources resources = mContext.getResources();
		int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
		if (rid > 0) {
			Log.d("sam test", resources.getBoolean(rid) + ""); // 获取导航栏是否显示true
																// or false
		}

		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0) {
			Log.d("sam test", resources.getDimensionPixelSize(resourceId) + ""); // 获取高度
			height = resources.getDimensionPixelSize(resourceId) + MyApplication.stystemTitleHeight;
		}
		Log.d("sam test", height + ""); // 获取高度
		return height;
	}

	/**
	 * 是否有虚拟键盘
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean getMenu(Context mContext) {
		return ViewConfiguration.get(mContext).hasPermanentMenuKey();// true无，false有
	}

	/**
	 * 手机震动
	 * 
	 * @param activity
	 * @param milliseconds
	 */
	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
}
