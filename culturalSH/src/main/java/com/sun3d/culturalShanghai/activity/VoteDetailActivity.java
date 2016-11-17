package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JavascriptInterface;
import com.sun3d.culturalShanghai.Util.MyWebChromeClient;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 投票
 * 
 * @author liningkang
 * 
 */
public class VoteDetailActivity extends Activity implements OnClickListener {
	private WebView mWebView;
	private String addressUrl = "http://www.wenhuayun.cn/mcIndex/mcMobileIndex.do";

	private String str = "";
	private String activityId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivitys(this);
		setContentView(R.layout.activity_new_details);
		initView();
	}

	private void initView() {
		RelativeLayout aountTitle = (RelativeLayout) findViewById(R.id.aount_info_title);
		aountTitle.findViewById(R.id.title_left).setOnClickListener(this);
		ImageButton mRight = (ImageButton) aountTitle.findViewById(R.id.title_right);
		mRight.setVisibility(View.VISIBLE);
		mRight.setOnClickListener(this);
		mRight.setImageResource(R.drawable.sh_icon_title_share);
		TextView mTitle = (TextView) aountTitle.findViewById(R.id.title_content);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText("投票");
		mWebView = (WebView) findViewById(R.id.activity_new_dateils);
		activityId = getIntent().getExtras().getString("activityId");
		addressUrl = activityId + "&userId=" + MyApplication.loginUserInfor.getUserId() + "&reqFrom=android";
		setWebView(mWebView);
		mWebView.loadUrl(addressUrl);


	}

	private void setWebView(WebView webView) {
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);// 必须使用
		settings.setDomStorageEnabled(true);//
		webView.addJavascriptInterface(new JavascriptInterface(this), "injs");// 必须使用
		webView.setWebChromeClient(new MyWebChromeClient());

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				view.loadUrl("javascript:window.injs.showSource('<head>'+"
						+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});


	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			intent = new Intent(this, UserDialogActivity.class);
			FastBlur.getScreen(this);
			ShareInfo info = new ShareInfo();
			info.setTitle("投票");
//			info.setContent(mWebView.);
			info.setContentUrl(activityId);
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType, DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
