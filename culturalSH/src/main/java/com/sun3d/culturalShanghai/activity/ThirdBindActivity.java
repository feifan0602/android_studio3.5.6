/**
 * @author ZhouTanPing E-mail:strong.ping@foxmail.com 
 * @version 创建时间：2015-10-15 下午5:26:42 
 */
package com.sun3d.culturalShanghai.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.thirdparty.GetTokenPage;
import com.umeng.analytics.MobclickAgent;

/**
 * @author zhoutanping
 * 
 *         第三方帐号绑定界面
 * 
 */
public class ThirdBindActivity extends Activity implements OnClickListener {

	// 显示是否已绑定（或未绑定）微博，微信，QQ
	private TextView bindWeibo, bindWeixin, bindQq;
	// 0未绑定， 1已绑定
	private int weiboWhether = 0, weixinWhether = 0, qqWhether = 0;
	private RelativeLayout bindWeibolayout, bindWeixinlayout, bindQqlayout;
	private int onclickType = 1;// 1为QQ,2为微博，3为微信
	private Context mContext;
	private final String mPageName = "ThirdBindActivity";

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_thirdbind_layout);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		MyApplication.getInstance().setMyBindHandler(MyBindHandler);
		initView();
	}

	/**
	 * 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		RelativeLayout title = (RelativeLayout) findViewById(R.id.title);
		title.findViewById(R.id.title_content).setVisibility(View.VISIBLE);
		((TextView) title.findViewById(R.id.title_content))
				.setText(getResources()
						.getString(R.string.user_bind_third_text));
		title.findViewById(R.id.title_right).setVisibility(View.GONE);
		title.findViewById(R.id.title_left).setOnClickListener(this);
		bindWeibolayout = (RelativeLayout) findViewById(R.id.user_bind_weibo);

		bindWeixinlayout = (RelativeLayout) findViewById(R.id.user_bind_weixin);

		bindQqlayout = (RelativeLayout) findViewById(R.id.user_bind_qq);

		bindWeibo = (TextView) findViewById(R.id.bind_weibo_whether);
		bindWeixin = (TextView) findViewById(R.id.bind_weixin_whether);
		bindQq = (TextView) findViewById(R.id.bind_qq_whether);
		initData();
	}

	private void initData() {
		Drawable drawable = getResources().getDrawable(R.drawable.sh_icon_null);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight()); // 设置边界
		
		if (MyApplication.loginUserInfor.getRegisterOrigin().indexOf("2") != -1) {// QQ绑定和登录的
			bindQq.setText(getResources().getString(R.string.bind_yes));
			qqWhether = 1;
			bindQq.setCompoundDrawables(null, null, drawable, null);
		} else {
			bindQqlayout.setOnClickListener(this);
		}

		if (MyApplication.loginUserInfor.getRegisterOrigin().indexOf("3") != -1) {// 新浪微博绑定和登录的
			bindWeibo.setText(getResources().getString(R.string.bind_yes));
			weiboWhether = 1;
			bindWeibo.setCompoundDrawables(null, null, drawable, null);
		} else {
			bindWeibolayout.setOnClickListener(this);
		}

		if (MyApplication.loginUserInfor.getRegisterOrigin().indexOf("4") != -1) {// 微信绑定和登录的
			bindWeixin.setText(getResources().getString(R.string.bind_yes));
			weixinWhether = 1;
			bindWeixin.setCompoundDrawables(null, null, drawable, null);
		} else {
			bindWeixinlayout.setOnClickListener(this);
		}

	}

	public static final int SENDBING = 001;
	private Handler MyBindHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SENDBING:
				startBind();
				break;

			default:
				break;
			}
		}

	};

	private void startBind() {
		String type = "";
		switch (onclickType) {
		case 1:// QQ
			type = "2";
			break;
		case 2:// 微博
			type = "3";
			break;
		case 3:// 微信
			type = "4";
			break;
		default:
			break;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("openId", MyApplication.Third_OpenId);
		params.put("register_type", type);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.USER_THIRD_BIND,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							int code = JsonUtil.getJsonStatus(resultStr);
							String origin = MyApplication.loginUserInfor
									.getRegisterOrigin();
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								switch (onclickType) {
								case 1:// QQ
									MyApplication.loginUserInfor
											.setRegisterOrigin(origin + ",2");
									break;
								case 2:// 微博
									MyApplication.loginUserInfor
											.setRegisterOrigin(origin + ",3");
									break;
								case 3:// 微信
									MyApplication.loginUserInfor
											.setRegisterOrigin(origin + ",4");
									break;
								default:
									break;
								}
								initData();
								bindWeibolayout.setEnabled(false);
								bindWeixinlayout.setEnabled(false);
								bindQqlayout.setEnabled(false);
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}

						} else {
							ToastUtil.showToast(resultStr);
						}

					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.user_bind_weibo:// 绑定微博
			onclickType = 2;
			GetTokenPage.GetSinaWeiboToken(mContext, null);
			// if (weiboWhether == 0) {
			//
			// bindWeibo.setText(getResources().getString(R.string.bind_yes));
			// weiboWhether = 1;
			// } else {
			// bindWeibo.setText(getResources().getString(R.string.bind_no));
			// weiboWhether = 0;
			// }
			break;
		case R.id.user_bind_weixin:// 绑定微信
			onclickType = 3;
			GetTokenPage.GetWechatToken(mContext, null);
			// if (weixinWhether == 0) {
			//
			// bindWeixin.setText(getResources().getString(R.string.bind_yes));
			// weixinWhether = 1;
			// } else {
			// bindWeixin.setText(getResources().getString(R.string.bind_no));
			// weixinWhether = 0;
			// }
			break;
		case R.id.user_bind_qq:// 绑定QQ
			onclickType = 1;
			GetTokenPage.GetQQToken(mContext, null);
			// if (qqWhether == 0) {
			// bindQq.setText(getResources().getString(R.string.bind_yes));
			// qqWhether = 1;
			// } else {
			// bindQq.setText(getResources().getString(R.string.bind_no));
			// qqWhether = 0;
			// }
			break;
		default:
			break;
		}
	}

}
