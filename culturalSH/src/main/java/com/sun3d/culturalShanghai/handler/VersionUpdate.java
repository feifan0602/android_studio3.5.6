package com.sun3d.culturalShanghai.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.APPVersionInfo;
import com.sun3d.culturalShanghai.service.DownloadAPKService;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 验证类
 * 
 * @author yangyoutao
 * 
 */
public class VersionUpdate {
	private static APPVersionInfo mAPPVersionInfo;

	/**
	 * 检测状况
	 * 
	 * @param mcontext
	 */
	public static void onVersionUpdate(final Context mcontext, final Boolean isToactShow) {

		MyHttpRequest.onHttpPostParams(HttpUrlList.Version.APP_VESIONUPDATER_URL, null,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mAPPVersionInfo = JsonUtil.getVersionInfo(resultStr);
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							setVersion(mcontext, isToactShow);
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 版本检测
	 * 
	 * @param mcontext
	 */
	private static void setVersion(Context mcontext, Boolean isToactShow) {
		if (mAPPVersionInfo != null) {
			if (mAPPVersionInfo.getServiceVersionNumber() > MyApplication.getInstance()
					.getVersionCode(mcontext)) {
				if (mAPPVersionInfo.getIsForcedUpdate()) {
					DownloadAPKService.downNewFile(mAPPVersionInfo.getUpdateUrl(), 100, "文化云:"
							+ mAPPVersionInfo.getExternalVnumber());
				} else {
					FastBlur.getScreen((Activity) mcontext);
					Intent intent = new Intent(mcontext, MessageDialog.class);
					intent.putExtra(DialogTypeUtil.DialogType,
							DialogTypeUtil.MessageDialog.MSGTYPE_VERSION_UPDATER);
					intent.putExtra(DialogTypeUtil.DialogContent,
							"更新版本：" + mAPPVersionInfo.getExternalVnumber() + "\r\n"
									+ mAPPVersionInfo.getUpdateDescr());
					mcontext.startActivity(intent);
				}
			} else {
				if (isToactShow) {
					ToastUtil.showToast("您已经是最新版本了！");
				}

			}

		}
	}

	/**
	 * 更新状况
	 */
	public static void updater() {
		DownloadAPKService.downNewFile(mAPPVersionInfo.getUpdateUrl(), 100, "文化云："
				+ mAPPVersionInfo.getExternalVnumber());
	}

}
