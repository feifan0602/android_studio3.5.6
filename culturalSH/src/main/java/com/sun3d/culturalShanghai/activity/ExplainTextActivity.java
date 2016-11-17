package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

public class ExplainTextActivity extends Activity implements OnClickListener {
	private Context mContext;
	private WebView showView;
	private String content;
	public final static String event_explain = "event_explain";
	public final static String venue_explain = "venue_explain";
	public final static String product_explain = "product_explain";
	private final String mPageName = "ExplainTextActivity";

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explain_text);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		init();
	}

	private void init() {

		String string = getIntent().getStringExtra("type");
		String content = getIntent().getStringExtra("content");
		if ("".equals(string) || string == null || null == content) {
			return;
		}
		
		TextView textContent = (TextView) findViewById(R.id.explain_content);
		ImageView mIv = (ImageView) findViewById(R.id.explain_iv);
		if (string.equals(event_explain)) {
			setTitle("预订须知、退改说明");
			textContent.setText(content);
//			mIv.setImageResource(R.drawable.sh_explain_bg);
		} else if (string.equals(venue_explain)) {
			setTitle("预订须知、退改说明");
			mIv.setVisibility(View.VISIBLE);
			findViewById(R.id.text1).setVisibility(View.GONE);
			findViewById(R.id.text2).setVisibility(View.GONE);
			textContent.setVisibility(View.GONE);
			mIv.setImageResource(R.drawable.sh_venue_explain_bg);
		} else if (string.equals(product_explain)) {
			setTitle("产品介绍");
			mIv.setImageResource(R.drawable.sh_product_bg);
		}
	}

	/**
	 * 设置标题
	 */
	private void setTitle(String titleText) {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		ImageButton mTitleRight = (ImageButton) mTitle.findViewById(R.id.title_right);
		mTitleRight.setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText(titleText);
		title.setVisibility(View.VISIBLE);
	}

	// private void initData() {
	// content = this.getIntent().getStringExtra("content");
	// if (this.getIntent().getStringExtra("type").equals(Text_URL)) {
	// showView.loadUrl(content);
	// } else {
	// showView.loadDataWithBaseURL(null, ViewUtil.initContent(content,
	// mContext), AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING, null);
	//
	// }
	//
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.explain_text, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
	}

}
