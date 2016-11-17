package com.sun3d.culturalShanghai.activity;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TicketContentActivity extends Activity {
	private String content;
	private TextView content_tv;
	private ImageView left_iv;
	private TextView middle_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket_layout);
		init();
		getdata();
	}

	private void init() {
		content_tv = (TextView) findViewById(R.id.content_tv);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("活动须知,退改说明");
		left_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
	}

	private void getdata() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String content = bundle.getString("content");
		content_tv.setTypeface(MyApplication.GetTypeFace());
		content_tv.setText(content);
	}

}
