package com.sun3d.culturalShanghai.NaviRoute;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

import com.amap.api.maps.model.LatLng;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.thirdparty.GetTokenPage;

public class NaviRoute {
	private static String BaiDupackageName = "com.baidu.BaiduMap";// 百度导航的包名
	private static String GaodepackageName = "com.autonavi.minimap";// 高德导航的包名
	private static String TencentpackageName = "com.tencent.map";// 高德导航的包名
	public static final int Navi_TRANSIT = 1;// 公交
	public static final int Navi_DRIVING = 2;// 驾车
	public static final int Navi_WALKING = 3;// 步行

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	public static void startNavi(Context context, LatLng start, String startName, LatLng end,
			String endName, int mode) {
		if (GetTokenPage.isApkInstalled(context, GaodepackageName)) {// 安装高德了，启动高德地图导航
			GaodeNavi.startNavi(context, start, startName, end, endName, getMode(1, mode));
		} else if (GetTokenPage.isApkInstalled(context, BaiDupackageName)) {// 安装百度地图，启动百度地图导航
			BaiDuNavi.startNavi(context, start, startName, end, endName, getMode(2, mode));
		} else {// 否则，用腾旭地图试试，如果不信。那我也没办法
			openApp(context, TencentpackageName);
		}
	}

	/**
	 * 不同导航不同模式
	 * 
	 * @param type
	 * @param mode
	 * @return
	 */
	private static String getMode(int type, int mode) {
		String mMode = "";
		if (type == 1) {// 高德导航
			switch (mode) {
			case Navi_TRANSIT:
				mMode = GaodeNavi.GAODENAVIMODE_TRANSIT;
				break;
			case Navi_DRIVING:
				mMode = GaodeNavi.GAODENAVIMODE_DRIVING;
				break;
			case Navi_WALKING:
				mMode = GaodeNavi.GAODENAVIMODE_WALKING;
				break;
			default:
				break;
			}
		} else {// 百度导航
			switch (mode) {
			case Navi_TRANSIT:
				mMode = BaiDuNavi.BAIDUNAVIMODE_TRANSIT;
				break;
			case Navi_DRIVING:
				mMode = BaiDuNavi.BAIDUNAVIMODE_DRIVING;
				break;
			case Navi_WALKING:
				mMode = BaiDuNavi.BAIDUNAVIMODE_WALKING;
				break;
			default:
				break;
			}
		}
		return mMode;
	}

	/**
	 * 通过包名打开APP
	 * 
	 * @param context
	 * @param packageNameAPP
	 */
	private static void openApp(Context context, String packageNameAPP) {
		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(packageNameAPP, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			ToastUtil.showToast("您没有安装地图应用，无法进行导航。请下载百度地图或者高德地图");
			return;
		}

		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(
				resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);
			intent.setComponent(cn);
			context.startActivity(intent);
		}
	}

}
