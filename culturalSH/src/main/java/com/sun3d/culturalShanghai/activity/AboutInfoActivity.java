package com.sun3d.culturalShanghai.activity;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JavascriptInterface_new;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.BannerInfo;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * webview页面（包括 使用帮助、关于文化云服务协议、首页轮播页面的分享）
 * 
 * @author zhoutanping
 * @param <favicon>
 * */
public class AboutInfoActivity<favicon> extends Activity implements
		OnClickListener {

	private WebView aountInfoWeb;
	private Context mContext;
	private LoadingDialog mLoadingDialog;
	private String TAG = "AboutInfoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivitys(this);
		setContentView(R.layout.activity_about_info);
		mContext = this;
		initView();
		setTitle();

	}

	private void initView() {
		mLoadingDialog = new LoadingDialog(this);
		aountInfoWeb = (WebView) findViewById(R.id.aount_info_web);
		WebSettings webSettings = aountInfoWeb.getSettings();
		webSettings.setSupportZoom(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setDomStorageEnabled(true);//
		webSettings.setLoadWithOverviewMode(true);
		aountInfoWeb.addJavascriptInterface(new JavascriptInterface_new(this),
				"injs");// 必须使用
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		String mUrl = getIntent().getStringExtra(HttpUrlList.WebUrl.WEB_URL);
		BannerInfo bf = (BannerInfo) getIntent().getSerializableExtra(
				"shareInfo");

		RelativeLayout aountTitle = (RelativeLayout) findViewById(R.id.aount_info_title);
		aountTitle.findViewById(R.id.title_left).setOnClickListener(this);
		ImageButton mTitleRight = (ImageButton) aountTitle
				.findViewById(R.id.title_right);
		mTitleRight.setOnClickListener(this);
		mTitleRight.setImageResource(R.drawable.sh_icon_title_share);
		TextView mTitle_tv = (TextView) aountTitle
				.findViewById(R.id.title_content);

		mTitle_tv.setVisibility(View.VISIBLE);
		if ("0".equals(mUrl)) {
			mTitle_tv.setText("软件许可及服务协议");
			mTitle_tv.setTypeface(MyApplication.GetTypeFace());
			aountTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
			aountInfoWeb.loadUrl(HttpUrlList.WebUrl.WEB_AOUNT);
		} else if ("1".equals(mUrl)) {
			aountTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
			mTitle_tv.setText("使用帮助");
			mTitle_tv.setTypeface(MyApplication.GetTypeFace());
			aountInfoWeb.loadUrl(HttpUrlList.WebUrl.WEB_HELP);

//			aountInfoWeb.setWebViewClient(new WebViewClient() {
//				public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
//					view.loadUrl(url);
//					return true;
//				}
//			});
		} else if ("2".equals(mUrl)) {// 首页轮播
			mTitle_tv.setTypeface(MyApplication.GetTypeFace());
			mTitle_tv.setText(bf.getAdvertTitle());
			mTitleRight.setVisibility(View.VISIBLE);
			mTitleRight.setTag(bf);
			aountInfoWeb.loadUrl(bf.getAdvertConnectUrl());
			aountInfoWeb.setWebViewClient(wvc);
		}
	}

	private WebViewClient wvc = new WebViewClient() {
		@Override
		public void onPageFinished(WebView view, String url) {
			// 开始
			// mLoadingDialog.startDialog("请稍候");
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// 结束
			// mLoadingDialog.cancelDialog();
			super.onPageStarted(view, url, favicon);
		}
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (view.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			BannerInfo bf = (BannerInfo) view.getTag();
			intent = new Intent(mContext, UserDialogActivity.class);
			FastBlur.getScreen((Activity) mContext);
			ShareInfo info = new ShareInfo();
			info.setTitle(bf.getAdvertTitle());
			info.setImageUrl(bf.getAdvertPicUrl());
			info.setContentUrl(bf.getAdvertConnectUrl());
			info.setContent(ViewUtil.getTextFromHtml(bf.getAdvertContent()
					.toString()));

			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
