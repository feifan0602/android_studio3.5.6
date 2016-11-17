package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;

/**
 * 登陆页面
 * 
 * @author wenff
 * 
 */
public class Activity_Login extends Activity implements OnClickListener {
	private TextView right_tv, middle_tv;
	private ImageView left_iv;
	private String forgetPwCode;
	private String forgetPwPhone;
	private EditText fastlogin_input_account, fastlogin_input_password;
	private TextView third_fastlogin_weixin, third_fastlogin_qq,
			third_fastlogin_sina, fastlogin_forgotpw;
	private Button fastlogin_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		initview();
		super.onCreate(savedInstanceState);
	}

	private void initview() {
		fastlogin_input_account = (EditText) findViewById(R.id.fastlogin_input_account);
		fastlogin_input_password = (EditText) findViewById(R.id.fastlogin_input_password);
		third_fastlogin_weixin = (TextView) findViewById(R.id.third_fastlogin_weixin);
		third_fastlogin_qq = (TextView) findViewById(R.id.third_fastlogin_qq);
		third_fastlogin_sina = (TextView) findViewById(R.id.third_fastlogin_sina);
		fastlogin_forgotpw = (TextView) findViewById(R.id.fastlogin_forgotpw);
		fastlogin_login = (Button) findViewById(R.id.fastlogin_login);
		third_fastlogin_weixin.setOnClickListener(this);
		third_fastlogin_qq.setOnClickListener(this);
		third_fastlogin_sina.setOnClickListener(this);
		fastlogin_forgotpw.setOnClickListener(this);
		fastlogin_login.setOnClickListener(this);

		right_tv = (TextView) findViewById(R.id.right_tv);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		middle_tv.setText("登录");
		left_iv.setOnClickListener(this);
		right_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_tv:

			break;
		case R.id.left_iv:
			finish();
			break;
		case R.id.third_fastlogin_weixin:
			
			break;
		case R.id.third_fastlogin_qq:
			
			break;
		case R.id.third_fastlogin_sina:
			
			break;
		case R.id.fastlogin_forgotpw:
			
			break;
		case R.id.fastlogin_login:
			
			break;
		default:
			break;
		}

	}

}