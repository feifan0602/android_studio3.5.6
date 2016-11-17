package com.sun3d.culturalShanghai.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.handler.RoomBookHandler;
import com.sun3d.culturalShanghai.handler.RoomBookHandler.RoomBookInfo;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyUserInfo;
import com.sun3d.culturalShanghai.object.RoomEventInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyRoomStutasActivity extends Activity implements OnClickListener {
	private TextView middle_tv;
	private ImageView left_iv;
	private TextView venue_phone, venue_time, venue_username, venue_name,
			venue_title_name, next_tv;
	private ImageView Partition_iv;
	private TextView Myroom_tv;
	private Button btn;
	private List<MyUserInfo> MyUserList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myroom_stutas_layout);
		MyApplication.getInstance().addActivitys(this);
		initview();
		initdata();
	}

	private void initdata() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.GET_USERINFO,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							MyUserList = JsonUtil.getMyUserInfoList(resultStr);
							handler.sendEmptyMessage(1);
						}
					}
				});
	}

	Handler handler = new Handler() {
		Intent intent;

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				if (RoomBookInfo.userType != 2
						|| RoomBookInfo.tuserIsDisplay != 1) {
					Partition_iv.setVisibility(View.VISIBLE);
					Myroom_tv.setVisibility(View.VISIBLE);
					btn.setVisibility(View.VISIBLE);
				} else {
					Partition_iv.setVisibility(View.GONE);
					Myroom_tv.setVisibility(View.GONE);
					btn.setVisibility(View.GONE);
				}
				if (RoomBookInfo.userType == 2) {
					// 已经实名认证了
					// 已经实名认证 判断是否资质认证
					if (RoomBookInfo.tuserIsDisplay != 1) {
						// 未通过资质认证
						btn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								intent = new Intent(MyRoomStutasActivity.this,
										BannerWebView.class);
								intent.putExtra(
										"url",
										HttpUrlList.IP
												+ "/wechatRoom/authTeamUser.do?type=app&userId="
												+ MyApplication.loginUserInfor
														.getUserId()
												+ "&roomOrderId="
												+ RoomBookHandler.RoomBookInfo.cmsRoomOrderId
												+ "&tuserName="
												+ RoomBookHandler.RoomBookInfo.reserve_user_person
												+ "&tuserId="
												+ RoomBookHandler.RoomBookInfo.tuserId
												+ "&tuserIsDisplay="
												+ RoomBookHandler.RoomBookInfo.tuserIsDisplay
												+ "&userType="
												+ MyApplication.loginUserInfor
														.getUserType());
								MyApplication.room_activity = 1;
								startActivity(intent);
							}
						});

					}
				} else {
					btn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							intent = new Intent(MyRoomStutasActivity.this,
									BannerWebView.class);
							// + "&roomOrderId="
							// + orderInfo
							// .getRoomOrderId()
							// + "&tuserName="
							// + orderInfo
							// .getTuserTeamName()
							// + "&tuserId="
							// + orderInfo
							// .getTuserId()
							// + "&tuserIsDisplay="
							// + orderInfo
							// .getTuserIsDisplay()
							// + "&userType="
							// + MyApplication.loginUserInfor
							// .getUserType());
							// venue_title_name.setText(RoomBookHandler.RoomBookInfo.roomName);
							// venue_phone.setText(RoomBookHandler.RoomBookInfo.orderName
							// + "  "
							// + RoomBookHandler.RoomBookInfo.orderMobileNum);
							// venue_time.setText(RoomBookHandler.RoomBookInfo.orderRoomDate
							// + " "
							// + RoomBookHandler.RoomBookInfo.openPeriod);
							// venue_username
							// .setText(RoomBookHandler.RoomBookInfo.reserve_user_person);
							// venue_name.setText(RoomBookHandler.RoomBookInfo.venueName);
							intent.putExtra(
									"url",
									HttpUrlList.IP
											+ "/wechatUser/auth.do?type=app&userId="
											+ MyApplication.loginUserInfor
													.getUserId()
											+ "&roomOrderId="
											+ RoomBookHandler.RoomBookInfo.cmsRoomOrderId
											+ "&tuserName="
											+ RoomBookHandler.RoomBookInfo.reserve_user_person
											+ "&tuserId="
											+ RoomBookHandler.RoomBookInfo.tuserId
											+ "&tuserIsDisplay="
											+ RoomBookHandler.RoomBookInfo.tuserIsDisplay
											+ "&userType="
											+ MyApplication.loginUserInfor
													.getUserType());
							MyApplication.room_activity = 1;
							startActivity(intent);
						}
					});
				}
				break;

			default:
				break;
			}
		};
	};

	private void initview() {
		Partition_iv = (ImageView) findViewById(R.id.Partition_iv);
		Myroom_tv = (TextView) findViewById(R.id.Myroom_tv);
		btn = (Button) findViewById(R.id.btn);

		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("活动室预订");
		next_tv = (TextView) findViewById(R.id.next_tv);
		next_tv.setOnClickListener(this);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		venue_title_name = (TextView) findViewById(R.id.venue_title_name);
		venue_phone = (TextView) findViewById(R.id.venue_phone);
		venue_time = (TextView) findViewById(R.id.venue_time);
		venue_username = (TextView) findViewById(R.id.venue_username);
		venue_name = (TextView) findViewById(R.id.venue_name);

		venue_name.setTypeface(MyApplication.GetTypeFace());
		venue_username.setTypeface(MyApplication.GetTypeFace());
		venue_time.setTypeface(MyApplication.GetTypeFace());
		venue_phone.setTypeface(MyApplication.GetTypeFace());
		venue_title_name.setTypeface(MyApplication.GetTypeFace());

		venue_title_name.setText(RoomBookHandler.RoomBookInfo.roomName);
		venue_phone.setText(RoomBookHandler.RoomBookInfo.orderName + "  "
				+ RoomBookHandler.RoomBookInfo.orderMobileNum);
		venue_time.setText(RoomBookHandler.RoomBookInfo.orderRoomDate + " "
				+ RoomBookHandler.RoomBookInfo.openPeriod);
		venue_username
				.setText(RoomBookHandler.RoomBookInfo.reserve_user_person);
		venue_name.setText(RoomBookHandler.RoomBookInfo.venueName);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_iv:
			finish();
			break;
		case R.id.next_tv:
			Intent intent = new Intent(this, MyOrderActivity.class);
			intent.putExtra("type", "venue");
			MyApplication.room_activity = 0;
			startActivity(intent);
			setResult(AppConfigUtil.LOADING_LOGIN_BACK);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 102) {
			finish();
		}
	}
}
