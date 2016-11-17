package com.sun3d.culturalShanghai.Util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * @author yangyoutao
 */
public class JavascriptInterface {
	private Activity context;

	public JavascriptInterface(Activity context) {
		this.context = context;
	}
	public void showActivity(String str) {
		Log.i("ceshi", "点击详情事件");
		context.finish();
	}

	public void showSource(String html) {
		System.out.println("====>html=" + html);
		Log.d("HTML", html);
	}

	@android.webkit.JavascriptInterface
	public void onAndroidBack() {
		context.finish();
	}

	@android.webkit.JavascriptInterface
	public void onAndroidShare(String url) {
		ToastUtil.showToast("1111   " + url);
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
