package com.sun3d.culturalShanghai.handler;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.object.EventInfo;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityDetail_webview {
	private WebView reminder_web;
	private LinearLayout content;
	private Context mContext;
	private Activity activity;
	private EventInfo eventInfo;
	private TextView tv;

	public LinearLayout getContent() {
		return content;
	}

	public void setWebData(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
		ViewUtil.setWebViewSettings(reminder_web, mContext);
		String text = eventInfo.getActivityTips();
		String str = ViewUtil.subString(text, true, null);
		String str1 = str.replace("  温馨提示：", "");
		// ColorStateList redColors = ColorStateList.valueOf(0xffff0000);
		// SpannableStringBuilder spanBuilder = new
		// SpannableStringBuilder(str1);
		// spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 80, null, null),
		// 2,
		// str1.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		reminder_web.loadDataWithBaseURL(null, ViewUtil.initContent(
				str1, mContext),
				AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING, null);
		// reminder_web.loadDataWithBaseURL(null,
		// ViewUtil.initContent(str1, mContext),
		// AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING, null);
	}

	public ActivityDetail_webview(Context context, Activity activity) {
		this.mContext = context;
		this.activity = activity;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activitydetail_webview, null);
		tv = (TextView) content.findViewById(R.id.tv);
		tv.setTypeface(MyApplication.GetTypeFace());
		reminder_web = (WebView) content.findViewById(R.id.reminder_web);

	}
}
