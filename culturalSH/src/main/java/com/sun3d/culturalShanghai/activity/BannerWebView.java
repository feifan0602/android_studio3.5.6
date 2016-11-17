package com.sun3d.culturalShanghai.activity;

import java.io.File;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.MediaUtility;
import com.sun3d.culturalShanghai.Util.TestWebChromeClient;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JavascriptInterface_new;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

public class BannerWebView extends Activity implements OnClickListener {
	private WebView mWebview;
	private String url_load;
	public TextView middle_tv;
	public ImageView left_iv, right_iv;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private boolean isLoadUrl = false;
	private String intent_text = "";
	private ValueCallback<Uri> mFilePathCallback;
	private ValueCallback<Uri[]> mFilePathCallbacks;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private WebChromeClient wvcc;
	public String content_text;
	public String img_url;
	private String TAG = "BannerWebView";
	private String auth_type = "";
	/**
	 * 分享的数据
	 */
	private String title = "", desc = "", imgUrl = "", link = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banner_webview);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void initView() {
		MyApplication.getInstance().addActivitys(this);
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		right_iv = (ImageView) findViewById(R.id.right_iv);
		middle_tv.setVisibility(View.VISIBLE);
		middle_tv.setTypeface(MyApplication.GetTypeFace());

		right_iv.setImageResource(R.drawable.wff_share);
		right_iv.setOnClickListener(this);
		mWebview = (WebView) findViewById(R.id.banner_web);
		url_load = getIntent().getExtras().getString("url");
		if (getIntent().getExtras().getString("auth") != null) {
			auth_type = getIntent().getExtras().getString("auth");
		}
		WebSettings settings = mWebview.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);// 必须使用
		String dir = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		settings.setDomStorageEnabled(true);//
		settings.setGeolocationEnabled(true);
		settings.setGeolocationDatabasePath(dir);
		// 修改ua使得web端正确判断 3.5.2需要判断是否是android 进入的
		String ua = settings.getUserAgentString();
		settings.setUserAgentString(ua + ";  wenhuayun/"
				+ MyApplication.getVersionName(this) + " platform/android/");
		String ua1 = settings.getUserAgentString();
		mWebview.addJavascriptInterface(
				new JavascriptInterface_new(this, this), "injs");// 必须使用

		wvcc = new WebChromeClient() {
			public void onGeolocationPermissionsShowPrompt(String origin,
					GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if (url_load.contains("/wechatRoom/authTeamUser.do")) {
					// 资质认证
					middle_tv.setText("资质认证");
				} else if (url_load.contains("/wechatUser/auth.do")) {
					// 实名认证
					middle_tv.setText("实名认证");
				} else if (url_load.contains("/wechatUser/preIntegralRule.do")) {
					// 积分规则
					middle_tv.setText("积分规则");
				} else {
					/**
					 * 没有取到 变更 title的消息
					 */
					if (!title.equals("")) {
						intent_text = title;
						middle_tv.setText(title);
					}

				}

				mLoadingHandler.stopLoading();
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				// TODO Auto-generated method stub
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public void onReceivedTouchIconUrl(WebView view, String url,
					boolean precomposed) {
				// TODO Auto-generated method stub
				super.onReceivedTouchIconUrl(view, url, precomposed);
			}

			@Override
			public void onReceivedIcon(WebView view, Bitmap icon) {
				// TODO Auto-generated method stub
				super.onReceivedIcon(view, icon);
			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				mFilePathCallback = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				BannerWebView.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						FILECHOOSER_RESULTCODE);

			}

			// For Android 3.0+
			public void openFileChooser(ValueCallback uploadMsg,
					String acceptType) {
				mFilePathCallback = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("*/*");
				BannerWebView.this.startActivityForResult(
						Intent.createChooser(i, "File Browser"),
						FILECHOOSER_RESULTCODE);
			}

			// For Android 4.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				mFilePathCallback = uploadMsg;
				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.addCategory(Intent.CATEGORY_OPENABLE);
				i.setType("image/*");
				BannerWebView.this.startActivityForResult(
						Intent.createChooser(i, "File Chooser"),
						BannerWebView.FILECHOOSER_RESULTCODE);

			}

			public boolean onShowFileChooser(WebView webView,
					ValueCallback<Uri[]> filePathCallback,
					WebChromeClient fileChooserParams) {
				mFilePathCallbacks = filePathCallback;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("*/*");
				BannerWebView.this.startActivityForResult(
						Intent.createChooser(intent, "File Chooser"),
						BannerWebView.FILECHOOSER_RESULTCODE);
				return true;
			}

		};

		mWebview.setWebChromeClient(wvcc);

		// 创建WebViewClient对象
		WebViewClient wvc = new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				parseScheme(url);
				return true;
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				if (!isLoadUrl) {
					isLoadUrl = true;
					Log.i(TAG, " page start ==  " + url);
					view.loadUrl(url);

				}
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				url_load = url;
				Log.i(TAG, "WEBVIEW  FINISH==  " + url);
				view.loadUrl("javascript:window.injs.getHtmlContentOrImg(1,document.body.innerText)");
				view.loadUrl("javascript:window.injs.getHtmlContentOrImg(2,document.getElementsByTagName('img')[0].src)");

				view.evaluateJavascript("javascript:getShareInfo()",
						new ValueCallback<String>() {

							@Override
							public void onReceiveValue(String value) {
								if (value != null) {
									try {
										value = value.replace("\\", "");
										value = value.substring(1,
												value.length() - 1);
										Log.i(TAG, "  value==  " + value);
										JSONObject json = new JSONObject(value);
										title = json.optString("title", "");
										desc = json.optString("desc", "");
										imgUrl = json.optString("imgUrl", "");
										link = json.optString("link", "");
									} catch (Exception e) {
										e.printStackTrace();
									}

								}

							}
						});
				super.onPageFinished(view, url);
			}
		};
		mWebview.setWebViewClient(wvc);
		mWebview.loadUrl(url_load);

	}

	public boolean parseScheme(String url) {
		Log.i(TAG, "跳转的  url  ==  " + url);

		if (url.contains("com.wenhuayun.app")) {
			Intent intent = new Intent();
			if (url.contains("://activitydetail")) {
				// 跳转到活动详情
				intent.setClass(this, ActivityDetailActivity.class);
				intent.putExtra("eventId", url.split("=")[1]);
				this.startActivity(intent);
			} else if (url.contains("://venuedetail")) {
				Log.i(TAG, "跳入场馆");
				// 跳转到场馆详情
				intent.setClass(this, VenueDetailActivity.class);
				intent.putExtra("venueId", url.split("=")[1]);
				this.startActivity(intent);
			} else if (url.contains("://orderlist")) {
				// 我的订单页面
				intent.setClass(this, MyOrderActivity.class);
				setResult(102, intent);
				this.startActivity(intent);
			} else if (url.contains("://usercenter")) {
				// 我的页面
				// MyApplication.change_ho_listviw = 4;
				// intent.setClass(this, MainFragmentActivity.class);
				// this.startActivity(intent);
				finish();
			} else {
				url_load = url;
				Log.i(TAG, "parseScheme  url==" + url_load);
				mWebview.setWebChromeClient(wvcc);
				mWebview.loadUrl(url);
			}
		} else {
			url_load = url;
			Log.i(TAG, "parseScheme000   url==" + url_load);
			mWebview.setWebChromeClient(wvcc);
			mWebview.loadUrl(url);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_iv:
			Intent intent = new Intent(this, UserDialogActivity.class);
			FastBlur.getScreen((Activity) this);
			ShareInfo info1 = new ShareInfo();
			if (!link.equals("")) {
				MyApplication.shareLink = link;
			} else {
				MyApplication.shareLink = url_load;
			}
			if (!title.equals("")) {
				info1.setTitle(title);
			} else {
				info1.setTitle(intent_text);
			}
			if (!desc.equals("")) {
				info1.setContent(desc);
			} else {
				info1.setContent(content_text);
			}
			if (!imgUrl.equals("")) {
				info1.setImageUrl(imgUrl);
			} else {
				info1.setImageUrl(img_url);
			}
			if (!link.equals("")) {
				info1.setContentUrl(link);
			} else {
				info1.setContentUrl(url_load);
			}
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info1);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		case R.id.left_iv:
			Log.i(TAG, " url  ==  " + url_load);
			if (url_load.contains("wechatActivity/preActivityOrder.do")
					|| url_load.contains("wechatActivity/finishSeat.do")) {
				finish();
				return;
			}

			if (mWebview.canGoBack()) {
				mWebview.goBack();
			} else {
				finish();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == AppConfigUtil.LOADING_LOGIN_BACK) {
			// 登录的回调
			Log.i(TAG, "登陆后  刷新");
			mWebview.reload();
		}
		if (requestCode == OpenFileWebChromeClient.REQUEST_FILE_PICKER) {
			if (mFilePathCallback != null) {
				Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
						: intent.getData();
				if (result != null) {
					String path = MediaUtility.getPath(getApplicationContext(),
							result);
					Uri uri = Uri.fromFile(new File(path));
					mFilePathCallback.onReceiveValue(uri);
				} else {
					mFilePathCallback.onReceiveValue(null);
				}
			}
			if (mFilePathCallbacks != null) {
				Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
						: intent.getData();
				if (result != null) {
					String path = MediaUtility.getPath(getApplicationContext(),
							result);
					Uri uri = Uri.fromFile(new File(path));
					mFilePathCallbacks.onReceiveValue(new Uri[] { uri });
				} else {
					mFilePathCallbacks.onReceiveValue(null);
				}
			}

			mFilePathCallback = null;
			mFilePathCallbacks = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			// do something here
			if (url_load.contains("wechatActivity/preActivityOrder.do")
					|| url_load.contains("wechatActivity/finishSeat.do")) {
				finish();
				return true;
			}

			if (mWebview.canGoBack()) {
				mWebview.goBack();
			} else {
				finish();
			}
			return true;

		}

		return super.onKeyDown(keyCode, event);
	}

}
