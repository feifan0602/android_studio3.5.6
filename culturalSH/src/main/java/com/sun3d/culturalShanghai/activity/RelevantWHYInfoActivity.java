package com.sun3d.culturalShanghai.activity;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GetPhoneInfoUtil;
import com.sun3d.culturalShanghai.Util.Utils;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author zhoutanping 关于文化云界面
 * */
public class RelevantWHYInfoActivity extends Activity implements
		OnClickListener {

	private TextView whyVersion;
	private Context mContext;
	private TextView middle_tv;
	private ImageView left_iv;
	private WebView mWebview;

	// http://192.168.41.147:8080/wechatUser/preCulture.do?type=app
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relevant_why);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		initView();
	}
	public void setDetailsData(String text, Boolean isAll) {
		mWebview.loadDataWithBaseURL(null, ViewUtil.initContent(
				ViewUtil.subString(text, isAll, null), mContext),
				AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING, null);

	}
	private void initView() {
		mWebview = (WebView) findViewById(R.id.webview);
		ViewUtil.setWebViewSettings(mWebview, this);
		mWebview.loadUrl("http://192.168.41.147:8080/wechatUser/preCulture.do?type=app");
		
		
		// TODO Auto-generated method stub
		// RelativeLayout aountTitle = (RelativeLayout)
		// findViewById(R.id.title);
		// aountTitle.findViewById(R.id.title_left).setOnClickListener(this);
		// aountTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		// TextView mTitle = (TextView)
		// aountTitle.findViewById(R.id.title_content);
		// mTitle.setVisibility(View.VISIBLE);
		// mTitle.setText("关于文化云");
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		middle_tv.setText("关于文化云");
		left_iv.setOnClickListener(this);
		left_iv.setImageResource(R.drawable.back);
		whyVersion = (TextView) findViewById(R.id.relevant_why_version);
		String version = "Ver：" + Utils.getVersion(this);
		whyVersion.setText(version);
		findViewById(R.id.relevant_why_info).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (view.getId()) {
		case R.id.relevant_why_info:
			intent = new Intent(this, AboutInfoActivity.class);
			intent.putExtra(HttpUrlList.WebUrl.WEB_URL, "0");
			startActivity(intent);
			break;
		case R.id.left_iv:
			finish();
			break;

		default:
			break;
		}
	}

}
