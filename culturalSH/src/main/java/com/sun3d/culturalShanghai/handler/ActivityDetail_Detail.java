package com.sun3d.culturalShanghai.handler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.manager.DeviceManager;
import com.sun3d.culturalShanghai.view.ScrollScrollView;

/**
 * 活动详情
 * 
 * @author yangyoutao
 * 
 */
public class ActivityDetail_Detail implements OnClickListener {
	private LinearLayout content;
	private Context mContext;
	private WebView mWebView;
	private ImageView height_change;
	private boolean height_bool = true;
	private LinearLayout activity_detail_ll;
	private TextView detail;
	private ProgressBar bar;
	private ScrollScrollView scroll_view;
	private TextView activity_tv;
	// 请求头
	private int head_height = 0;
	// 温馨提示
	private int web_height = 0;
	private int distance_scrollto = 0;
	private String TAG = "ActivityDetail_Detail";
	public ImageView left_line_img, right_line_img, line_img_bottom;
	private ActivityDetailActivity detail_activity;
	private LinearLayout title_activity;
	private TextView activity_detail_Only_tv;

	public void setHeadHeight(int i) {
		head_height = i;
	}

	public void setWebHeight(int i) {
		web_height = i;
	}

	public LinearLayout getContent() {
		return content;
	}

	public ActivityDetail_Detail(Context context, ScrollScrollView scroll,
			ActivityDetailActivity activity_detail) {
		this.scroll_view = scroll;
		this.mContext = context;
		this.detail_activity = activity_detail;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_detatillayout, null);
		title_activity = (LinearLayout) content
				.findViewById(R.id.title_activity);
		activity_detail_Only_tv = (TextView) content
				.findViewById(R.id.activity_detail_Only_tv);
		left_line_img = (ImageView) content.findViewById(R.id.left_line_img);
		right_line_img = (ImageView) content.findViewById(R.id.right_line_img);
		line_img_bottom = (ImageView) content
				.findViewById(R.id.line_img_bottom);
		activity_tv = (TextView) content.findViewById(R.id.activity_tv);
		detail = (TextView) content.findViewById(R.id.activity_detail_tv);
		activity_tv.setTypeface(MyApplication.GetTypeFace());
		detail.setTypeface(MyApplication.GetTypeFace());
		detail.setOnClickListener(this);
		activity_detail_ll = (LinearLayout) content
				.findViewById(R.id.activity_detail_ll);
		bar = (ProgressBar) content.findViewById(R.id.myProgressBar);
		mWebView = (WebView) content.findViewById(R.id.activity_dateils);
		ViewUtil.setWebViewSettings(mWebView, mContext);
		//mWebView.loadUrl("javascript:" + "alert('tet')");
		mWebView.setWebChromeClient(new WebChromeClient() 
		{

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					bar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

		});

		height_change = (ImageView) content.findViewById(R.id.height_change);

		height_change.setOnClickListener(this);
		activity_tv.setOnClickListener(this);

	}

	public void setLayout(boolean bool) {
		if (bool) {
			activity_detail_Only_tv.setVisibility(View.GONE);
			title_activity.setVisibility(View.VISIBLE);
		} else {
			activity_detail_Only_tv.setVisibility(View.VISIBLE);
			line_img_bottom.setVisibility(View.GONE);
			title_activity.setVisibility(View.GONE);
			detail_activity.left_line_img.setVisibility(View.GONE);
			detail_activity.right_line_img.setVisibility(View.GONE);
			left_line_img.setVisibility(View.GONE);
			right_line_img.setVisibility(View.GONE);
		}
	}

	public void setDetailsData(String text, Boolean isAll) {
		if (text == null || text == "" || text.equals("")) {
			activity_detail_ll.setVisibility(View.GONE);
			mWebView.setVisibility(View.GONE);
			detail.setVisibility(View.GONE);
		} else {
			activity_detail_ll.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.VISIBLE);
			detail.setVisibility(View.VISIBLE);
			mWebView.loadDataWithBaseURL(null, ViewUtil.initContent(
					ViewUtil.subString(text, isAll, null), mContext),
					AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING,
					null);
			LinearLayout.LayoutParams lParams = (LayoutParams) mWebView
					.getLayoutParams();
			if (text.length() < 190) {
				height_change.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.height_change:
			if (height_bool) {
				LinearLayout.LayoutParams lParams = (LayoutParams) mWebView
						.getLayoutParams();
				lParams.height = lParams.MATCH_PARENT;
				mWebView.setLayoutParams(lParams);
				height_change.setImageResource(R.drawable.arrow_up);
				height_bool = false;
			} else {
				height_bool = true;
				LinearLayout.LayoutParams lParams = (LayoutParams) mWebView
						.getLayoutParams();
				lParams.height = 200;
				mWebView.setLayoutParams(lParams);
				height_change.setImageResource(R.drawable.arrow_down);
			}

			break;
		// 这是活动单位
		case R.id.activity_tv:
			clearLineColor();
			right_line_img.setBackgroundResource(R.color.text_color_72);
			detail_activity.right_line_img
					.setBackgroundResource(R.color.text_color_72);
			detail_activity.activity_Layout_ll.setVisibility(View.VISIBLE);

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) detail_activity.movBarImageView
					.getLayoutParams();
			params.leftMargin = 25 + DeviceManager.getResolutionWidth() / 2;
			detail_activity.movBarImageView.setLayoutParams(params);

			distance_scrollto = activity_detail_ll.getHeight() + head_height
					+ MyApplication.getInstance().getWindowHeight() / 6;
			scroll_view.scrollTo(0, distance_scrollto);
			break;
		// 这是活动详情
		case R.id.activity_detail_tv:
			clearLineColor();
			left_line_img.setBackgroundResource(R.color.text_color_72);
			detail_activity.left_line_img
					.setBackgroundResource(R.color.text_color_72);
			RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) detail_activity.movBarImageView
					.getLayoutParams();
			params1.leftMargin = 25;
			detail_activity.movBarImageView.setLayoutParams(params1);
			distance_scrollto = head_height;
			scroll_view.scrollTo(0, distance_scrollto);
			break;
		default:
			break;
		}

	}

	private void clearLineColor() {
		detail_activity.left_line_img
				.setBackgroundResource(R.color.text_color_cc);
		detail_activity.right_line_img
				.setBackgroundResource(R.color.text_color_cc);
		right_line_img.setBackgroundResource(R.color.text_color_cc);
		left_line_img.setBackgroundResource(R.color.text_color_cc);
	}
}
