package com.sun3d.culturalShanghai.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JavascriptInterface;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.NewInfo;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 实况直击
 * 
 * @author liningkang
 * 
 */
public class NewDetailsActivity extends Activity implements OnClickListener {
	private WebView mWebView;
	private String addressUrl = "http://www.wenhuayun.cn/mcIndex/mcMobileIndex.do";
	private Context mContext;
	private NewInfo mNewInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivitys(this);
		setContentView(R.layout.activity_new_details);
		mContext = this;
		initView();
	}

	private void initView() {
		RelativeLayout mTitleLayout = (RelativeLayout) findViewById(R.id.aount_info_title);
		ImageButton mRight = (ImageButton) mTitleLayout.findViewById(R.id.title_right);
		mTitleLayout.findViewById(R.id.title_left).setOnClickListener(this);
		mRight.setOnClickListener(this);
		mRight.setImageResource(R.drawable.sh_icon_title_share);

		TextView mTitle = (TextView) mTitleLayout.findViewById(R.id.title_content);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText("实况直击");
		mWebView = (WebView) findViewById(R.id.activity_new_dateils);
		// String newId = this.getIntent().getExtras().getString("newId");
		mNewInfo = (NewInfo) this.getIntent().getExtras().getSerializable("NewInfo");
		String newId = mNewInfo.getNewId();
		 addressUrl = HttpUrlList.WebUrl.NEWURL + "?dataId=" + newId +
		 "&reqFrom=android&asm=" + System.currentTimeMillis();
		setWebView(mWebView);
		mWebView.loadUrl(addressUrl);
	}

	/**
	 * 对WebView进行基础设置
	 * 
	 * @param webView
	 */
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void setWebView(WebView webView) {
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);// 必须使用
		settings.setDomStorageEnabled(true);//
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		webView.addJavascriptInterface(new JavascriptInterface(this), "injs");// 必须使用
		webView.setWebChromeClient(new WebChromeClient());
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			Intent intent = new Intent(this, UserDialogActivity.class);
			FastBlur.getScreen(this);
			ShareInfo info = new ShareInfo();
			info.setTitle(mNewInfo.getNewsTitle());
			info.setImageUrl(mNewInfo.getNewsImgUrl());
			info.setContentUrl(addressUrl);
			info.setContent(mNewInfo.getNewsDesc());
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType, DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
