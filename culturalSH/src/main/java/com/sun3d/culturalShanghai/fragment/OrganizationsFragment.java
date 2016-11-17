package com.sun3d.culturalShanghai.fragment;

import org.json.JSONObject;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JavascriptFristOrganization;
import com.sun3d.culturalShanghai.Util.JavascriptInterface_new;
import com.sun3d.culturalShanghai.Util.NetWorkUtil;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpUrlList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrganizationsFragment extends Fragment implements RefreshListenter {
	private View view;
	private Context mContext;
	public WebView webView;
	public TextView middle_tv;

	private ImageView left_iv;
	private String TAG = "OrganizationsFragment";
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.organization_fragment, null);
		mContext = getActivity();
		initView(view);
		return view;
	}

	private void initView(View v) {
		loadingLayout = (RelativeLayout) v.findViewById(R.id.loading);
		left_iv = (ImageView) v.findViewById(R.id.left_iv);
		left_iv.setVisibility(View.INVISIBLE);
		webView = (WebView) v.findViewById(R.id.web_view);
		middle_tv = (TextView) v.findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		if (NetWorkUtil.isConnected()) {
			setWebUrl();
		} else {
			mLoadingHandler.isNotNetConnection();
		}

	}

	private void StartLoading() {
		webView.setVisibility(View.GONE);
		mLoadingHandler.startLoading();
	}

	private void StopLoading() {
		webView.setVisibility(View.VISIBLE);
		mLoadingHandler.stopLoading();
	}

	/**
	 * 设置 webview 的设置 以及webview 的链接
	 */
	public void setWebUrl() {
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setJavaScriptEnabled(true);// 必须使用
		settings.setDomStorageEnabled(true);//
		String ua = settings.getUserAgentString();
		settings.setUserAgentString(ua + ";  wenhuayun/"
				+ MyApplication.getVersionName(getActivity())
				+ " platform/android/");
		String ua1 = settings.getUserAgentString();
		webView.addJavascriptInterface(new JavascriptFristOrganization(
				getActivity(), this), "injs");// 必须使用
		webView.setWebChromeClient(wvcc);
		webView.setWebViewClient(wvc);
		webView.loadUrl(HttpUrlList.IP + "/wechatAssn/toAssnList.do");
		Log.i(TAG, "url  ==  " + HttpUrlList.IP + "/wechatAssn/toAssnList.do");

	}

	WebChromeClient wvcc = new WebChromeClient() {
		public void onReceivedTitle(WebView view, String title) {
			/**
			 * 没有取到 变更 title的消息
			 */
			if (!title.equals("")) {
				middle_tv.setText(title);
			}
		};
	};
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
			StartLoading();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			StopLoading();
			super.onPageFinished(view, url);
		}
	};

	/**
	 * 来响应 webview 中的点击事件
	 * 
	 * @param url
	 * @return
	 */
	public boolean parseScheme(String url) {
		webView.loadUrl(url);
		return true;
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		setWebUrl();
	}

}
