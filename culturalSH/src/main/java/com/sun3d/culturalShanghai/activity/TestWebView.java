package com.sun3d.culturalShanghai.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JavascriptInterface;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.handler.LoadingHandler;

@SuppressLint("SetJavaScriptEnabled")
public class TestWebView extends Activity {
	private WebView mWebview;
	private String url;
	private TextView middle_tv;
	private ImageView left_iv, right_iv;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banner_webview);
		initView();

	}

	@SuppressLint("JavascriptInterface")
	private void initView() {
		mWebview = (WebView) findViewById(R.id.banner_web);

		// 把本类的一个实例添加到js的全局对象window中，
		// 这样就可以使用window.injs来调用它的方法

		// 加载页面
		// 允许JavaScript执行
		mWebview.getSettings().setJavaScriptEnabled(true);
		// 找到Html文件，也可以用网络上的文件
		mWebview.loadUrl("file:///android_asset/index.html");
		// 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
		mWebview.addJavascriptInterface(new Contact(), "contact");

	}

	private final class Contact {
		// JavaScript调用此方法拨打电话
		public void call(String phone) {
			// startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
			// phone)));
			Toast.makeText(TestWebView.this, phone, Toast.LENGTH_LONG).show();
		}

		// Html调用此方法传递数据
		public void showcontacts() {
			String json = "[{\"name\":\"zxx\", \"amount\":\"9999999\", \"phone\":\"18600012345\"}]";
			// 调用JS中的方法
			mWebview.loadUrl("javascript:show('" + json + "')");
		}

		public void toast(String str) {
			Toast.makeText(TestWebView.this, "aaaaaaaaaaaa  --- " + str,
					Toast.LENGTH_LONG).show();
		}
	}

}
