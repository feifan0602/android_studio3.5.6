package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ScreenshotUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.ActivityOrderDetailAdapter;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.ActivityOrderDetailInfo;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;
import com.sun3d.culturalShanghai.object.MyOrderInfo;
import com.sun3d.culturalShanghai.thirdparty.MyShare;
import com.sun3d.culturalShanghai.thirdparty.ShareName;
import com.sun3d.culturalShanghai.view.FastBlur;

public class ActivityOrderDetail extends Activity implements OnClickListener {
	private String TAG = "ActivityOrderDetail";
	private ListView listview;
	private ActivityOrderDetailAdapter detail_adapter;
	private TextView title_content;
	private ImageButton title_left;
	private String activityOrderId;
	private List<ActivityOrderDetailInfo> list;
	private List<ActivityOrderDetailInfo> list_info;
	private TextView orderNumber, orderTime, activityName, activityAddress,
			orderValidateCode, venueName, venueAddress, order_cancle,
			order_status;
	private ImageView title_img;
	private LoadingHandler mLoadingHandler;
	private ImageView order_qr_code;
	/**
	 * 判断是从活动室 还是活动 场馆来的 2表示已取消
	 */
	private int now_histroy;
	/**
	 * 0 是活动
	 */
	private String ActivityOrRoom;
	private RelativeLayout loadingLayout;
	private LinearLayout title_ll;
	private LinearLayout address_ll;
	private MyOrderInfo content;
	private LoadingDialog mLoadingDialog;
	private LinearLayout ValidateCode_ll;
	private TextView code_tv, take_code, help_center;

	// MyOrderActivity.getInstance().goToDialog(orderInfo);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acrivity_order_detail);
		init();

		if (ActivityOrRoom.equals("0")) {
			// 活动
			getListData(0);
		} else {
			// 场馆
			getRoomListData(0);
		}

	}

	private void getRoomListData(int page) {
		/**
		 * 这个是要传入到服务器的参数
		 */
		Map<String, String> mParams = new HashMap<String, String>();
		MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
		mParams.put("roomOrderId", activityOrderId);
		mParams.put("userId", MyApplication.loginUserInfor.getUserId());
		String urlString = "";
		urlString = HttpUrlList.MyEvent.WFF_USERROOMORDERDEATIL;
		MyHttpRequest.onHttpPostParams(urlString, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.i("ceshi", "看看详情数据===   " + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil
									.getActivityOrderDetailList(resultStr);
							handler.sendEmptyMessage(2);
						}
					}
				});
	}

	/**
	 * 获取20条活动的数据
	 */
	private void getListData(int page) {
		/**
		 * 这个是要传入到服务器的参数
		 */
		Map<String, String> mParams = new HashMap<String, String>();
		MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
		mParams.put("activityOrderId", activityOrderId);
		mParams.put("userId", MyApplication.loginUserInfor.getUserId());
		String urlString = "";
		// 第一次取数据的时候
		urlString = HttpUrlList.MyEvent.WFF_USERACTIVITYORDERDEATIL;
		MyHttpRequest.onHttpPostParams(urlString, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.i("ceshi", "看看详情数据000===   " + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil
									.getActivityOrderDetailList(resultStr);
							handler.sendEmptyMessage(1);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int week = 1;
			String day = null;
			switch (msg.what) {
			case 1:
				if (now_histroy == 2) {
					order_status.setVisibility(View.GONE);
					order_cancle.setOnClickListener(null);
					order_cancle.setText("已取消");
				} else {
					order_status.setVisibility(View.VISIBLE);
				}
				list_info = new ArrayList<ActivityOrderDetailInfo>();
				ValidateCode_ll.setVisibility(View.VISIBLE);
				orderNumber.setText("订  单  号:   "
						+ list.get(0).getOrderNumber());
				orderTime.setText("下单时间:   "
						+ MyApplication.getStrNewTime(list.get(0)
								.getOrderTime() + ""));
				// orderPayTime.setText("支付时间:" +
				// list.get(0).getOrderPayTime());
				activityName.setText(list.get(0).getActivityName());
				if (!list.get(0).getActivitySite().equals("")) {
					activityAddress.setText(list.get(0).getActivityAddress()
							+ "." + list.get(0).getActivitySite());
				} else {
					activityAddress.setText(list.get(0).getActivityAddress());
				}

				orderValidateCode.setText(list.get(0).getOrderValidateCode());
				if (!list.get(0).getActivitySite().equals("")) {
					venueName.setText(list.get(0).getActivityAddress() + "."
							+ list.get(0).getActivitySite());
				} else {
					venueName.setText(list.get(0).getVenueName());
				}
				venueAddress.setText(list.get(0).getActivityAddress());
				// 阿斯顿
				try {
					week = MyApplication.dayForWeek(list.get(0)
							.getActivityEventDateTime().split(" ")[0]);
					day = MyApplication.formatFractionalPart(week);
				} catch (Exception e) {
					e.printStackTrace();
				}

				list_info.add(new ActivityOrderDetailInfo("日    期:", (list
						.get(0).getActivityEventDateTime().split(" ")[0])
						+ "      周" + day));
				list_info.add(new ActivityOrderDetailInfo("时    间:", list
						.get(0).getActivityEventDateTime().split(" ")[1]));
				if (list.get(0).getActivitySalesOnline().equals("Y")
						&& list.get(0).getActivitySeats().length() > 0) {
					String[] seats = list.get(0).getActivitySeats().split(",");
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < seats.length; i++) {
						String[] seatArray = seats[i].split("_");
						if (seatArray.length > 1) {
							sb.append(seatArray[0] + "排" + seatArray[1]
									+ "座   ");
						}
					}
					if (sb.length() > 0) {
						list_info.add(new ActivityOrderDetailInfo("座    位:", sb
								.toString()));
					}
				}
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								TextUtil.getUrlSmall(list.get(0)
										.getActivityIconUrl()), title_img);
				list_info.add(new ActivityOrderDetailInfo("数    量:", list
						.get(0).getOrderVotes() + ""));
				if (list.get(0).getCostTotalCredit() == 0) {

				} else {
					list_info.add(new ActivityOrderDetailInfo("积    分:", list
							.get(0).getCostTotalCredit() + ""));
				}

				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(list.get(0).getActivityQrcodeUrl(),
								order_qr_code);

				detail_adapter = new ActivityOrderDetailAdapter(
						ActivityOrderDetail.this, list_info);
				listview.setAdapter(detail_adapter);
				MyApplication.setListViewHeightBasedOnChildren(listview);
				mLoadingHandler.stopLoading();
				break;
			case 2:
				if (now_histroy == 2) {
					order_status.setVisibility(View.GONE);
					order_cancle.setOnClickListener(null);
					// 历史状态
					// CheckStatus 审核状态 0.待审核 1.审核通过 2.审核未通过
					// 1-预定成功 2-取消预定 3-预定失败 4-已出票 5-已验票 6.已失效
					if (list.get(0).getCheckStatus().equals("2")) {
						order_cancle.setText("审核未通过");
					} else if (list.get(0).getCheckStatus().equals("6")) {
						order_cancle.setText("已过期");
					} else {
						order_cancle.setText("已取消");
					}

				} else {
					order_status.setVisibility(View.VISIBLE);
				}
				list_info = new ArrayList<ActivityOrderDetailInfo>();
				ValidateCode_ll.setVisibility(View.GONE);
				activityName.setText(list.get(0).getRoomName());
				activityAddress.setText(list.get(0).getVenueAddress());
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								TextUtil.getUrlSmall(list.get(0)
										.getRoomPicUrl()), title_img);
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(list.get(0).getRoomQrcodeUrl(),
								order_qr_code);
				orderNumber.setText("订  单  号:   "
						+ list.get(0).getOrderNumber());
				list_info.add(new ActivityOrderDetailInfo("日      期:", list
						.get(0).getDate()));
				list_info.add(new ActivityOrderDetailInfo("场      次:", list
						.get(0).getOpenPeriod()));
				// list_info.add(new ActivityOrderDetailInfo("价      格:", list
				// .get(0).getPrice()));
				list_info.add(new ActivityOrderDetailInfo("使  用  者:", list.get(
						0).getTuserName()));
				list_info.add(new ActivityOrderDetailInfo("预定联系人:", list.get(0)
						.getOrderName()));
				list_info.add(new ActivityOrderDetailInfo("联系人手机:", list.get(0)
						.getOrderTel()));
				orderTime.setText("下单时间:   "
						+ MyApplication.getStrNewTime(list.get(0)
								.getOrderTime() + ""));
				// orderPayTime.setText("支付时间:" +
				// list.get(0).getOrderPayTime());
				orderValidateCode.setText(list.get(0).getOrderValidateCode());

				// 先判断是否实名认证
				if (list.get(0).getUserType() == 1
						|| list.get(0).getUserType() == 4) {
					// 前往认证
					order_status.setBackgroundColor(ActivityOrderDetail.this
							.getResources().getColor(R.color.text_color_72));
					order_status.setText("前往认证");
					order_status.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(
									ActivityOrderDetail.this,
									BannerWebView.class);
							intent.putExtra("url", HttpUrlList.IP
									+ "/wechatUser/auth.do?type=app&userId="
									+ MyApplication.loginUserInfor.getUserId());
							ActivityOrderDetail.this.startActivity(intent);
						}
					});
				} else if (list.get(0).getUserType() == 3) {
					// 实名认证中
					order_status.setBackgroundColor(ActivityOrderDetail.this
							.getResources().getColor(R.color.text_color_72));
					order_status.setText("认证中");
					order_status.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(
									ActivityOrderDetail.this,
									BannerWebView.class);
							intent.putExtra("url", HttpUrlList.IP
									+ "/wechatUser/auth.do?type=app&userId="
									+ MyApplication.loginUserInfor.getUserId());
							ActivityOrderDetail.this.startActivity(intent);
						}
					});
				} else {
					if (list.get(0).getStr_UserId().length() <= 0) {
						// 进行资质认证
						order_status
								.setBackgroundColor(ActivityOrderDetail.this
										.getResources().getColor(
												R.color.text_color_72));
						order_status.setText("前往认证");
						order_status.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(
										ActivityOrderDetail.this,
										BannerWebView.class);
								intent.putExtra(
										"url",
										HttpUrlList.IP
												+ "/wechatRoom/authTeamUser.do?type=app&userId="
												+ MyApplication.loginUserInfor
														.getUserId());
								ActivityOrderDetail.this.startActivity(intent);
							}
						});

					} else {
						// 这里是 资质认证的
						if (list.get(0).getTuserIsDisplay().equals("1")) {
							// 不需要前往认证 已认证
							// 0.待审核, 1.已预订,2.已取消,3.已验票 4.已删除 5.已出票 6.已失效
							if (list.get(0).getBookStauts() == 0) {
								if (ActivityOrRoom.equals("1")) {
									order_status.setText("待使用");
								} else {
									order_status.setText("待审核");
								}

							} else if (list.get(0).getBookStauts() == 1) {
								order_status.setText("已预订");
							} else if (list.get(0).getBookStauts() == 2) {
								order_status.setText("已取消");
							} else if (list.get(0).getBookStauts() == 3) {
								order_status.setText("已验票");
							} else if (list.get(0).getBookStauts() == 4) {
								order_status.setText("已删除");
							} else if (list.get(0).getBookStauts() == 5) {
								order_status.setText("已出票 ");
							} else if (list.get(0).getBookStauts() == 6) {
								order_status.setText("已失效");
							}
						} else if (list.get(0).getTuserIsDisplay().equals("0")) {
							order_status
									.setBackgroundColor(ActivityOrderDetail.this
											.getResources().getColor(
													R.color.text_color_72));
							order_status.setText("认证中");
							order_status
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											Intent intent = new Intent(
													ActivityOrderDetail.this,
													BannerWebView.class);
											intent.putExtra(
													"url",
													HttpUrlList.IP
															+ "/wechatRoom/authTeamUser.do?type=app&userId="
															+ MyApplication.loginUserInfor
																	.getUserId());
											ActivityOrderDetail.this
													.startActivity(intent);
										}
									});
						} else {
							order_status
									.setBackgroundColor(ActivityOrderDetail.this
											.getResources().getColor(
													R.color.text_color_72));
							order_status.setText("前往认证");
							order_status
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											Intent intent = new Intent(
													ActivityOrderDetail.this,
													BannerWebView.class);
											intent.putExtra(
													"url",
													HttpUrlList.IP
															+ "/wechatRoom/authTeamUser.do?type=app&userId="
															+ MyApplication.loginUserInfor
																	.getUserId());
											ActivityOrderDetail.this
													.startActivity(intent);
										}
									});
						}
					}
				}

				venueName.setText(list.get(0).getVenueName());
				venueAddress.setText(list.get(0).getVenueAddress());

				// try {
				// week = MyApplication.dayForWeek(list.get(0)
				// .getActivityEventDateTime().split(" ")[0]);
				// day = MyApplication.formatFractionalPart(week);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//
				// if (list.get(0).getActivitySalesOnline().equals("Y")
				// && list.get(0).getActivitySeats().length() > 0) {
				// list_info.add(new ActivityOrderDetailInfo("座    位:", list
				// .get(0).getActivitySeats()));
				// }
				//
				// list_info.add(new ActivityOrderDetailInfo("数    量:", list
				// .get(0).getOrderVotes() + ""));
				// list_info.add(new ActivityOrderDetailInfo("积    分:",
				// "还不知道去哪个字段"));

				detail_adapter = new ActivityOrderDetailAdapter(
						ActivityOrderDetail.this, list_info);
				listview.setAdapter(detail_adapter);
				detail_adapter.notifyDataSetChanged();
				MyApplication.setListViewHeightBasedOnChildren(listview);
				mLoadingHandler.stopLoading();
				break;
			default:
				break;
			}
		};
	};

	private void init() {
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		Bundle bundle = getIntent().getExtras();
		activityOrderId = bundle.getString("activityOrderId");
		ActivityOrRoom = bundle.getString("ActivityOrRoom");
		now_histroy = bundle.getInt("now_histroy");
		content = (MyOrderInfo) getIntent().getExtras().get("orderInfo");
		mLoadingDialog = new LoadingDialog(this);
		code_tv = (TextView) findViewById(R.id.code_tv);
		take_code = (TextView) findViewById(R.id.take_code);
		code_tv.setTypeface(MyApplication.GetTypeFace());
		take_code.setTypeface(MyApplication.GetTypeFace());
		ValidateCode_ll = (LinearLayout) findViewById(R.id.ValidateCode_ll);
		help_center = (TextView) findViewById(R.id.help_center);
		help_center.setTypeface(MyApplication.GetTypeFace());
		help_center.setOnClickListener(this);
		address_ll = (LinearLayout) findViewById(R.id.address_ll);
		address_ll.setOnClickListener(this);
		title_ll = (LinearLayout) findViewById(R.id.title_ll);
		title_ll.setOnClickListener(this);
		title_img = (ImageView) findViewById(R.id.title_img);
		order_qr_code = (ImageView) findViewById(R.id.order_qr_code);
		orderNumber = (TextView) findViewById(R.id.orderNumber);
		orderTime = (TextView) findViewById(R.id.orderTime);
		// orderPayTime = (TextView) findViewById(R.id.orderPayTime);
		activityName = (TextView) findViewById(R.id.activityName);
		activityAddress = (TextView) findViewById(R.id.activityAddress);
		orderValidateCode = (TextView) findViewById(R.id.orderValidateCode);
		venueName = (TextView) findViewById(R.id.venueName);
		venueAddress = (TextView) findViewById(R.id.venueAddress);
		order_cancle = (TextView) findViewById(R.id.order_cancle);
		order_status = (TextView) findViewById(R.id.order_status);
		order_cancle.setOnClickListener(this);
		// order_status.setOnClickListener(this);
		order_status.setTypeface(MyApplication.GetTypeFace());
		order_cancle.setTypeface(MyApplication.GetTypeFace());
		venueName.setTypeface(MyApplication.GetTypeFace());
		venueAddress.setTypeface(MyApplication.GetTypeFace());
		orderValidateCode.setTypeface(MyApplication.GetTypeFace());
		orderNumber.setTypeface(MyApplication.GetTypeFace());
		orderTime.setTypeface(MyApplication.GetTypeFace());
		// orderPayTime.setTypeface(MyApplication.GetTypeFace());
		activityName.setTypeface(MyApplication.GetTypeFace());
		activityAddress.setTypeface(MyApplication.GetTypeFace());
		title_left = (ImageButton) findViewById(R.id.title_left);
		title_content = (TextView) findViewById(R.id.title_content);
		title_content.setVisibility(View.VISIBLE);
		title_content.setTypeface(MyApplication.GetTypeFace());
		title_content.setText("订单详情");
		title_left.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.listview);

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.help_center:
			intent = new Intent(this, AboutInfoActivity.class);
			intent.putExtra(HttpUrlList.WebUrl.WEB_URL, "1");
			startActivity(intent);
			break;
		case R.id.title_left:
			finish();
			break;
		case R.id.order_cancle:
			if (ActivityOrRoom.equals("0")) {
				// 活动
				intent = new Intent(this, MessageDialog.class);
				FastBlur.getScreen(this);
				intent.putExtra(DialogTypeUtil.DialogContent,
						content.getBookInfo());
				intent.putExtra(DialogTypeUtil.DialogType,
						DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_ACTIVITY);
				startActivityForResult(intent, HttpCode.BACK);
			} else if (ActivityOrRoom.equals("1")) {
				// 场馆
				intent = new Intent(this, MessageDialog.class);
				FastBlur.getScreen(this);
				intent.putExtra(DialogTypeUtil.DialogContent,
						content.getRoomInfo());
				intent.putExtra(DialogTypeUtil.DialogType,
						DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_VENUE);
				startActivityForResult(intent, HttpCode.BACK_ROOM);
			} else if (ActivityOrRoom.equals("3")) {
				// intent = new Intent(this, MessageDialog.class);
				// FastBlur.getScreen(this);
				// intent.putExtra(DialogTypeUtil.DialogContent,
				// content.getBookInfo());
				// // intent.putExtra("ID", list.get(0).getRoomId());
				// // Log.i(TAG, "传过去的 id ==  "+list.get(0).getRoomId());
				// intent.putExtra(DialogTypeUtil.DialogType,
				// DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_CHECK_ROOM);
				// startActivityForResult(intent, HttpCode.BACK_CHCEK_ROOM);
				onCancelRoomCheck(content.getBookInfo());
			}

			break;
		case R.id.order_status:
			// 应该有一个前往认证
			break;
		case R.id.title_ll:
			if (!ActivityOrRoom.equals("0")) {
				// 场馆的
				intent = new Intent();
				intent.setClass(this, ActivityRoomDateilsActivity.class);
				intent.putExtra("Id", list.get(0).getRoomId());
				this.startActivity(intent);
			} else {
				// 活动的
				intent = new Intent();
				intent.setClass(this, ActivityDetailActivity.class);
				intent.putExtra("eventId", list.get(0).getActivityId());
				this.startActivity(intent);
			}
			break;
		case R.id.address_ll:
			// if (eventInfo.getEventLat() != null
			// && eventInfo.getEventLat().length() > 0
			// && !eventInfo.getEventLat().equals("0")
			// && eventInfo.getEventLon() != null
			// && eventInfo.getEventLon().length() > 0
			// && !eventInfo.getEventLon().equals("0")) {
			// intent = new Intent(mContext, BigMapViewActivity.class);
			// intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
			// intent.putExtra("titleContent", eventInfo.getEventName());
			// // 传入场馆的位置
			// intent.putExtra("lat", eventInfo.getEventLat());
			// intent.putExtra("lon", eventInfo.getEventLon());
			// intent.putExtra("address", eventInfo.getEventAddress());
			// mContext.startActivity(intent);
			// } else {
			// ToastUtil.showToast("没有相关位置信息");
			// }
			Log.i("ceshi", "list ==titleContent " + list.get(0).getVenueName()
					+ "lat  ==  " + list.get(0).getVenueLat() + "  lon== "
					+ list.get(0).getVenueLon() + "  address == "
					+ list.get(0).getVenueAddress());
			if (!ActivityOrRoom.equals("0")) {
				intent = new Intent(this, BigMapViewActivity.class);
				intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
				intent.putExtra("titleContent", list.get(0).getVenueName());
				// 传入场馆的位置
				String lat = String.valueOf(list.get(0).getVenueLat());
				String lon = String.valueOf(list.get(0).getVenueLon());
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				intent.putExtra("address", list.get(0).getVenueAddress());
				startActivity(intent);
			} else {
				intent = new Intent(this, BigMapViewActivity.class);
				intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
				intent.putExtra("titleContent", list.get(0).getVenueName());
				// 传入场馆的位置
				String lat = String.valueOf(list.get(0).getVenueLat());
				String lon = String.valueOf(list.get(0).getVenueLon());
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				intent.putExtra("address", list.get(0).getVenueAddress());
				startActivity(intent);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case HttpCode.BACK:
			MyActivityBookInfo info = (MyActivityBookInfo) data
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			if (info != null && info.getIsSeatOnline()) {
				onCancel(info);
			} else {
				for (int i = 0; i < info.getoSummary().length; i++) {
					info.getoSummary()[i] = false;
				}
				// mAdapter.list.get(info.getIndex()).setBookInfo(info);
				onCancel(info);
			}
			break;
		case HttpCode.BACK_ROOM:
			MyActivityRoomInfo infoRoom = (MyActivityRoomInfo) data
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			if (infoRoom != null) {
				onCancelRoom(infoRoom);
			}

			break;
		case HttpCode.BACK_CHCEK_ROOM:
			MyActivityBookInfo infoRoom1 = (MyActivityBookInfo) data
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			if (infoRoom1 != null) {
				onCancelRoomCheck(infoRoom1);
			}
			break;
		case HttpCode.BACK_QQ:
			final String orderInfo = data
					.getStringExtra(DialogTypeUtil.DialogContent);
			// final String imgUrl = "/" + orderInfo + ".png";
			// ScreenshotUtil.getData(layoutView, imgUrl, new CollectCallback()
			// {
			// @Override
			// public void onPostExecute(boolean isOK) {
			// // TODO Auto-generated method stub
			// if (isOK) {
			// if ("QQ".equals(orderInfo.substring(
			// orderInfo.length() - 2, orderInfo.length()))) {
			// MyShare.showShare(ShareName.QQ,
			// MyOrderActivity.this,
			// FolderUtil.createImageCacheFile() + imgUrl);
			// } else if ("WX".equals(orderInfo.substring(
			// orderInfo.length() - 2, orderInfo.length()))) {
			// MyShare.showShare(ShareName.Wechat,
			// MyOrderActivity.this,
			// FolderUtil.createImageCacheFile() + imgUrl);
			// }
			// } else {
			// ToastUtil.showToast("分享失败");
			// }
			// }
			// });

			break;

		default:
			break;
		}
	}

	/**
	 * 取消活动室
	 */
	private void onCancelRoom(final MyActivityRoomInfo info) {
		mLoadingDialog.startDialog("请稍候");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.Venue.CANCEL_ROOMORDER_ID, info.getRoomOrderId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.CANCEL_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								setResult(HttpCode.BACK_MYORDER);
								finish();
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else {
							ToastUtil.showToast(resultStr);
						}

						mLoadingDialog.cancelDialog();
					}
				});
	}

	/**
	 * 取消待审核的场馆订单
	 */
	private void onCancelRoomCheck(final MyActivityBookInfo info) {
		mLoadingDialog.startDialog("请稍候");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.Venue.CANCEL_ROOMORDER_ID, info.getRoomOrderId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.CANCEL_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								setResult(HttpCode.BACK_MYORDER);
								finish();
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else {
							ToastUtil.showToast(resultStr);
						}

						mLoadingDialog.cancelDialog();
					}
				});
	}

	/**
	 * 取消活动 2645 宋
	 */
	public void onCancel(final MyActivityBookInfo info) {
		mLoadingDialog.startDialog("请稍等");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.MyEvent.CANCEL_EVENT_ID,
				info.getActivityOrderId());
		params.put(HttpUrlList.MyEvent.CANCEL_EVENT_SEAT, setSeat(info));
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.CANCEL_EVENT_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								ToastUtil.showToast("活动已取消");
								setResult(HttpCode.BACK_MYORDER);
								finish();
								// orderList.get(info.getIndex()).getBookInfo()
								// .setOrderPayStatus("2");
								// orderList.get(info.getIndex()).getBookInfo()
								// .setOrderVotes("0");
								// if (info.getIsSeatOnline()) {// 在线选座
								// disposeDate(info);
								// }
								// // mAdapter.list = orderList;
								// mAdapter.setData(orderList);
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else {
							ToastUtil.showToast(resultStr);
						}

						mLoadingDialog.cancelDialog();
					}
				});
	}

	/**
	 * 需要取消的在线选座（座位）
	 * */
	private String setSeat(final MyActivityBookInfo info) {
		String seat = "";
		if (!info.getIsSeatOnline()) {
			return seat;
		}
		String[] seatArray = info.getOrderLine().split(",");
		if (seatArray != null && seatArray.length > 0) {
			for (int i = 0; i < seatArray.length; i++) {
				if (info.getoSummary()[i]) {
					seat += seatArray[i] + ",";
				}
			}
		}
		return seat;
	}

}
