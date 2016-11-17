package com.sun3d.culturalShanghai.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.Utils;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.BigMapViewActivity;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;
import com.sun3d.culturalShanghai.object.MyOrderInfo;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;

public class MyOrderAdapter extends BaseAdapter {
	private final String TAG = "MyOrderAdapter";
	private MyOrderActivity mContext;
	public List<MyOrderInfo> list;
	private LoadingDialog mLoadingDialog;
	private int now_histroy;

	private ViewHolder mHolder = null;

	public MyOrderAdapter(MyOrderActivity mContext, List<MyOrderInfo> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		mLoadingDialog = new LoadingDialog(mContext);
	}

	public void setStatus(int num) {
		this.now_histroy = num;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MyOrderInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setData(List<MyOrderInfo> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (view == null) {
			mHolder = new ViewHolder();
			mHolder.view = view = View.inflate(mContext,
					R.layout.item_my_order, null);
			mHolder.activity_venue_img = (ImageView) view
					.findViewById(R.id.activity_venue_img);
			mHolder.img_url = (ImageView) view.findViewById(R.id.img_url);
			mHolder.activityOrderId = (TextView) view
					.findViewById(R.id.activityOrderId);
			mHolder.total_price = (TextView) view
					.findViewById(R.id.total_price);
			mHolder.orderTitle = (TextView) view.findViewById(R.id.order_title);
			mHolder.linearActivity = (View) view
					.findViewById(R.id.order_linear_activity);
			mHolder.linearRoom = (View) view
					.findViewById(R.id.order_linear_room);
			mHolder.orderLinear = (View) view.findViewById(R.id.order_linear);
			mHolder.orderRoomField = (TextView) view
					.findViewById(R.id.order_room_field);
			mHolder.orderRoomAdd = (TextView) view
					.findViewById(R.id.order_room_address);
			mHolder.orderRoomTime = (TextView) view
					.findViewById(R.id.order_room_time);
			mHolder.order_room_num = (TextView) view
					.findViewById(R.id.order_room_num);
			mHolder.order_room_hour = (TextView) view
					.findViewById(R.id.order_room_hour);
			mHolder.orderRoomNumber = (TextView) view
					.findViewById(R.id.order_room_number);
			mHolder.orderRoomGroup = (TextView) view
					.findViewById(R.id.order_room_group);
			mHolder.orderCode = (TextView) view.findViewById(R.id.order_code);
			mHolder.orderQrCode = (ImageView) view
					.findViewById(R.id.order_qr_code);
			mHolder.orderSeat = (ScrollViewGridView) view
					.findViewById(R.id.order_seat);
			mHolder.orderNumber = (TextView) view
					.findViewById(R.id.order_number);
			mHolder.orderTime = (TextView) view.findViewById(R.id.order_time);
			mHolder.seatlayout = (RelativeLayout) view
					.findViewById(R.id.my_event_seatlayout);
			mHolder.orderCancel = (Button) view.findViewById(R.id.order_cancel);
			mHolder.orderSendOut = (Button) view
					.findViewById(R.id.order_send_out);
			mHolder.itemView = (View) view.findViewById(R.id.item_view);
			mHolder.total_price.setTypeface(MyApplication.GetTypeFace());
			mHolder.activityOrderId.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderTitle.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderRoomField.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderRoomAdd.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderRoomTime.setTypeface(MyApplication.GetTypeFace());
			mHolder.order_room_num.setTypeface(MyApplication.GetTypeFace());
			mHolder.order_room_hour.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderRoomNumber.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderRoomGroup.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderCode.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderNumber.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderTime.setTypeface(MyApplication.GetTypeFace());
			mHolder.orderCancel.setTypeface(MyApplication.GetTypeFace());
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		// if (mContext.now_histroy == 1) {
		// // 进行中
		// mHolder.orderPayStatus.setVisibility(View.VISIBLE);
		// mHolder.orderCancel.setBackgroundColor(mContext.getResources()
		// .getColor(R.color.text_color_72));
		// } else {
		// // 历史订单
		// mHolder.orderPayStatus.setVisibility(View.GONE);
		// mHolder.orderCancel.setBackgroundColor(mContext.getResources()
		// .getColor(R.color.text_color_df));
		// }
		initView(mHolder);
		MyOrderInfo orderInfo = getItem(arg0);
		if (orderInfo.getActivityOrRoom().equals("0")) {// 活动
			orderInfo.getBookInfo().setIndex(arg0);
			setActivityView(orderInfo.getBookInfo(), mHolder, arg0);
		} else if (orderInfo.getActivityOrRoom().equals("1")) {// 场馆
			orderInfo.getRoomInfo().setIndex(arg0);
			setRoomView(orderInfo.getRoomInfo(), mHolder);
		} else if (orderInfo.getActivityOrRoom().equals("3")) {// 审核的
			orderInfo.getBookInfo().setIndex(arg0);
			setBeingActivityView(orderInfo.getBookInfo(), mHolder, arg0);
		}
		return view;
	}

	private void setBeingActivityView(final MyActivityBookInfo orderInfo,
			final ViewHolder mHolder, final int arg0) {
		if (null == orderInfo) {
			return;
		}
		mHolder.linearActivity.setVisibility(View.VISIBLE);
		mHolder.orderRoomNumber.setVisibility(View.GONE);
		mHolder.activityOrderId.setText(orderInfo.getRoomOrderNo());

		mHolder.orderRoomTime.setText(MyApplication.getStrNewTime(orderInfo
				.getOrderTime()));

		mHolder.orderTitle.setText(orderInfo.getRoomName());
		mHolder.orderRoomAdd.setText(orderInfo.getVenueName());
		/**
		 * tuserIsDisplay 认证状态 0 待认证 1认证通过 2冻结 3认证不通过 orderStatus 0 待审核
		 */
		// tuserIsDisplay
		Log.i("ceshi",
				"好像又错了===  " + MyApplication.loginUserInfor.getUserType()
						+ "   length==  " + orderInfo.getTuserId().length());
		if (orderInfo.getOrderStatus() == 0) {
			if (orderInfo.getTuserIsDisplay().equals("2")) {
				// 被冻结

			} else {
				if (MyApplication.MyUserType != 2) {
					// 需要实名认证
					if (MyApplication.MyUserType == 3) {
						// 认证中
						mHolder.orderCancel.setGravity(Gravity.CENTER);
						mHolder.orderCancel.setVisibility(View.VISIBLE);
						mHolder.orderCancel.setText("认证中");
						mHolder.orderCancel
								.setBackgroundColor(mContext.getResources()
										.getColor(R.color.text_color_72));
						mHolder.orderCancel
								.setTextColor(mContext.getResources().getColor(
										R.color.text_color_ff));
						mHolder.orderCancel
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										Intent intent = new Intent(mContext,
												BannerWebView.class);
										intent.putExtra(
												"url",
												HttpUrlList.IP
														+ "/wechatUser/auth.do?type=app&userId="
														+ MyApplication.loginUserInfor
																.getUserId()
														+ "&roomOrderId="
														+ orderInfo
																.getRoomOrderId()
														+ "&tuserName="
														+ orderInfo
																.getTuserTeamName()
														+ "&tuserId="
														+ orderInfo
																.getTuserId()
														+ "&tuserIsDisplay="
														+ orderInfo
																.getTuserIsDisplay()
														+ "&userType="
														+ MyApplication.loginUserInfor
																.getUserType());
										mContext.startActivityForResult(intent,
												102);
									}
								});
					} else {
						// 前往实名认证
						mHolder.orderCancel.setGravity(Gravity.CENTER);
						mHolder.orderCancel.setVisibility(View.VISIBLE);
						mHolder.orderCancel.setText("前往认证");
						mHolder.orderCancel
								.setBackgroundColor(mContext.getResources()
										.getColor(R.color.text_color_72));
						mHolder.orderCancel
								.setTextColor(mContext.getResources().getColor(
										R.color.text_color_ff));
						mHolder.orderCancel
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										Intent intent = new Intent(mContext,
												BannerWebView.class);
										intent.putExtra(
												"url",
												HttpUrlList.IP
														+ "/wechatUser/auth.do?type=app&userId="
														+ MyApplication.loginUserInfor
																.getUserId()
														+ "&roomOrderId="
														+ orderInfo
																.getRoomOrderId()
														+ "&tuserName="
														+ orderInfo
																.getTuserTeamName()
														+ "&tuserId="
														+ orderInfo
																.getTuserId()
														+ "&tuserIsDisplay="
														+ orderInfo
																.getTuserIsDisplay()
														+ "&userType="
														+ MyApplication.loginUserInfor
																.getUserType());
										mContext.startActivityForResult(intent,
												102);
									}
								});
					}

				} else {
					// 资质认证中 的判断
					// 实名认证已经通过 判断是否需要资质认证

					if (orderInfo.getTuserId().length() < 1) {
						// 资质还没认证
						mHolder.orderCancel.setGravity(Gravity.CENTER);
						mHolder.orderCancel.setVisibility(View.VISIBLE);
						mHolder.orderCancel.setText("前往认证");
						mHolder.orderCancel
								.setBackgroundColor(mContext.getResources()
										.getColor(R.color.text_color_72));
						mHolder.orderCancel
								.setTextColor(mContext.getResources().getColor(
										R.color.text_color_ff));
						mHolder.orderCancel
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										Intent intent = new Intent(mContext,
												BannerWebView.class);
										intent.putExtra(
												"url",
												HttpUrlList.IP
														+ "/wechatRoom/authTeamUser.do?type=app&userId="
														+ MyApplication.loginUserInfor
																.getUserId()
														+ "&roomOrderId="
														+ orderInfo
																.getRoomOrderId()
														+ "&tuserName="
														+ orderInfo
																.getTuserTeamName()
														+ "&tuserId="
														+ orderInfo
																.getTuserId()
														+ "&tuserIsDisplay="
														+ orderInfo
																.getTuserIsDisplay()
														+ "&userType="
														+ MyApplication.loginUserInfor
																.getUserType());
										mContext.startActivityForResult(intent,
												102);
									}
								});

					} else {
						if (orderInfo.getTuserIsDisplay().equals("1")) {
							// 实名认证和资质认证都通过了
							mHolder.orderCancel.setGravity(Gravity.RIGHT
									| Gravity.CENTER_VERTICAL);
							mHolder.orderCancel.setText("待审核");
							mHolder.orderCancel.setBackgroundColor(Color.WHITE);
							mHolder.orderCancel.setTextColor(mContext
									.getResources().getColor(
											R.color.text_color_26));
						} else if (orderInfo.getTuserIsDisplay().equals("0")) {
							// 资质认证中
							mHolder.orderCancel.setGravity(Gravity.CENTER);
							mHolder.orderCancel.setVisibility(View.VISIBLE);
							mHolder.orderCancel.setText("认证中");
							mHolder.orderCancel.setBackgroundColor(mContext
									.getResources().getColor(
											R.color.text_color_72));
							mHolder.orderCancel.setTextColor(mContext
									.getResources().getColor(
											R.color.text_color_ff));
							mHolder.orderCancel
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											Intent intent = new Intent(
													mContext,
													BannerWebView.class);
											intent.putExtra(
													"url",
													HttpUrlList.IP
															+ "/wechatRoom/authTeamUser.do?type=app&userId="
															+ MyApplication.loginUserInfor
																	.getUserId()
															+ "&roomOrderId="
															+ orderInfo
																	.getRoomOrderId()
															+ "&tuserName="
															+ orderInfo
																	.getTuserTeamName()
															+ "&tuserId="
															+ orderInfo
																	.getTuserId()
															+ "&tuserIsDisplay="
															+ orderInfo
																	.getTuserIsDisplay()
															+ "&userType="
															+ MyApplication.loginUserInfor
																	.getUserType());

											mContext.startActivityForResult(
													intent, 102);
										}
									});
						} else {
							// 资质前往认证
							mHolder.orderCancel.setGravity(Gravity.CENTER);
							mHolder.orderCancel.setVisibility(View.VISIBLE);
							mHolder.orderCancel.setText("前往认证");
							mHolder.orderCancel.setBackgroundColor(mContext
									.getResources().getColor(
											R.color.text_color_72));
							mHolder.orderCancel.setTextColor(mContext
									.getResources().getColor(
											R.color.text_color_ff));
							mHolder.orderCancel
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											Intent intent = new Intent(
													mContext,
													BannerWebView.class);
											intent.putExtra(
													"url",
													HttpUrlList.IP
															+ "/wechatRoom/authTeamUser.do?type=app&userId="
															+ MyApplication.loginUserInfor
																	.getUserId()
															+ "&roomOrderId="
															+ orderInfo
																	.getRoomOrderId()
															+ "&tuserName="
															+ orderInfo
																	.getTuserTeamName()
															+ "&tuserId="
															+ orderInfo
																	.getTuserId()
															+ "&tuserIsDisplay="
															+ orderInfo
																	.getTuserIsDisplay()
															+ "&userType="
															+ MyApplication.loginUserInfor
																	.getUserType());

											mContext.startActivityForResult(
													intent, 102);
										}
									});
						}

					}
				}
			}
		}

		if (orderInfo.getPrice().equals("0")) {
			mHolder.total_price.setText("总金额:  " + "免费");
		} else {

			mHolder.total_price.setText("总金额:  " + orderInfo.getPrice());
		}
		// if (orderInfo.getPrice()) {
		//
		// }
		mHolder.order_room_num.setText(orderInfo.getRoomTime().split(" ")[0]
				+ "    " + orderInfo.getRoomTime().split(" ")[1]);
		mHolder.order_room_hour.setText(orderInfo.getRoomTime().split(" ")[2]
				+ "    " + orderInfo.getRoomTime().split(" ")[3]);
		MyApplication.displayFromDrawable(R.drawable.myorder_venue_icon,
				mHolder.activity_venue_img);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlSmall(orderInfo.getRoomPicUrl()),
						mHolder.img_url, Options.getRoundOptions(0));
		Log.i(TAG, "图片地址==   " + orderInfo.getRoomPicUrl());

	}

	private void initView(ViewHolder mHolder) {
		if (null != mHolder) {
			mHolder.seatlayout.setVisibility(View.GONE);
			mHolder.linearRoom.setVisibility(View.GONE);
			mHolder.orderRoomField.setVisibility(View.GONE);
			mHolder.orderRoomGroup.setVisibility(View.GONE);
			mHolder.orderSendOut.setVisibility(View.GONE);
			mHolder.itemView.setVisibility(View.GONE);
			mHolder.orderRoomNumber.setVisibility(View.GONE);
			mHolder.linearActivity.setVisibility(View.GONE);
			// mHolder.orderCancel
			// .setBackgroundResource(R.drawable.shape_item_order_gray);
			mHolder.orderCancel.setOnClickListener(null);
		}
	}

	/**
	 * 设置活动item
	 * */
	private void setActivityView(final MyActivityBookInfo orderInfo,
			final ViewHolder mHolder, final int arg0) {
		if (null == orderInfo) {
			return;
		}
		mHolder.linearActivity.setVisibility(View.VISIBLE);
		mHolder.orderRoomNumber.setVisibility(View.GONE);
		// mHolder.orderRoomTime.setText(MyApplication.getStrNewTime(orderInfo
		// .getOrderTime()));
		// mHolder.orderTitle.setText(orderInfo.getRoomName());
		// mHolder.orderRoomAdd.setText(orderInfo.getVenueName());

		mHolder.orderTitle.setText(orderInfo.getActivityName());
		mHolder.orderRoomAdd.setText(orderInfo.getActivityAddress());
		mHolder.orderRoomTime.setText(MyApplication.getStringToDate(Long
				.valueOf(orderInfo.getOrderTime() + "000")));
		if (orderInfo.getPrice().equals("0")) {
			mHolder.total_price.setText("总金额: " + "免费");
		} else {
			mHolder.total_price.setText("总金额: " + orderInfo.getPrice());
		}

		mHolder.order_room_num.setText(orderInfo.getActivityEventDateTime());
		// mHolder.order_room_num.setText(orderInfo.getActivityEventDateTime()
		// .split(" ")[0].replaceAll("-", ".")
		// + " "
		// + orderInfo.getActivityEventDateTime().split(" ")[1]
		// .replaceAll("-", "."));
		if (orderInfo.getActivitySeats().contains("_")) {
			StringBuffer sb = new StringBuffer();
			String[] seats = orderInfo.getActivitySeats().split(",");
			for (int i = 0; i < seats.length; i++) {
				sb.append(seats[i].split("_")[0] + "排" + seats[i].split("_")[1]
						+ "座  ");
			}
			mHolder.order_room_hour.setText(sb.toString());
		} else {
			mHolder.order_room_hour.setText(orderInfo.getOrderVotes() + "人");
		}

		mHolder.orderRoomNumber.setText("人数：" + orderInfo.getOrderVotes());
		MyApplication.displayFromDrawable(R.drawable.myorder_activity_icon,
				mHolder.activity_venue_img);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlSmall(orderInfo.getActivityIconUrl()),
						mHolder.img_url, Options.getRoundOptions(0));
		Log.i(TAG, "图片地址  huod==   " + orderInfo.getActivityIconUrl());
		// if (orderInfo.getOrderPayStatus().equals("1")) {
		// mHolder.orderPayStatus.setText("未出票");
		// } else if (orderInfo.getOrderPayStatus().equals("2")) {
		// mHolder.orderPayStatus.setText("取消预定");
		// } else if (orderInfo.getOrderPayStatus().equals("3")) {
		// mHolder.orderPayStatus.setText("已出票");
		// } else if (orderInfo.getOrderPayStatus().equals("4")) {
		// mHolder.orderPayStatus.setText("已验票");
		// } else if (orderInfo.getOrderPayStatus().equals("5")) {
		// mHolder.orderPayStatus.setText("已出票");
		// }

		mHolder.orderCode.setText(handleText(orderInfo.getOrderValidateCode()));
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(orderInfo.getActivityQrcodeUrl(),
						mHolder.orderQrCode, Options.getRoundOptions(10));
		mHolder.orderNumber.setText(orderInfo.getOrderNumber());
		mHolder.orderTime.setText(orderInfo.getOrderTime());
		mHolder.activityOrderId.setText(orderInfo.getOrderNumber());

		if (orderInfo.getIsSeatOnline()
				&& orderInfo.getActivitySeats().length() > 0
				&& mContext.now_histroy == 2) {
			mHolder.seatlayout.setVisibility(View.GONE);
			MyNewSeatAdapter seat = new MyNewSeatAdapter(mContext, list, arg0);
			mHolder.orderSeat.setAdapter(seat);
			seat.notifyDataSetChanged();
			if (orderInfo.getoSummary().length > 0
					&& !orderInfo.getOrderPayStatus().equals("5")) {
				mHolder.orderSeat
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								orderInfo.setoSummary(Utils.sizeBoo(orderInfo
										.getActivitySeats()));
								orderInfo.getoSummary()[arg2] = true;
								MyOrderActivity.getInstance().goToDialog(
										orderInfo);
							}
						});
			}
		} else {
			mHolder.seatlayout.setVisibility(View.GONE);
		}

		if (orderInfo.getOrderPayStatus().equals("1")
				&& !orderInfo.getOrderVotes().equals("0")) {
			mHolder.orderSendOut.setVisibility(View.GONE);
			mHolder.itemView.setVisibility(View.VISIBLE);
			// mHolder.orderCancel
			// .setBackgroundResource(R.drawable.shape_item_order_but);

			mHolder.orderSendOut
					.setBackgroundResource(R.drawable.shape_order_give);
			mHolder.orderSendOut.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyOrderActivity.getInstance().goToDialogQq(mHolder.view,
							orderInfo);
				}
			});
			/**
			 * 3.5.2 取消
			 */
			// mHolder.orderCancel.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// if (mContext.now_histroy == 1) {
			// for (int i = 0; i < orderInfo.getoSummary().length; i++) {
			// orderInfo.getoSummary()[i] = true;
			// }
			// MyOrderActivity.getInstance().goToDialog(orderInfo);
			// notifyDataSetChanged();
			// } else {
			//
			// }
			//
			// }
			// });
		} else {
			mHolder.orderCancel.setText(getActivityState(orderInfo
					.getOrderPayStatus()));
		}
		if (now_histroy == 1) {
			// 这个是待参加的活动
			mHolder.orderCancel.setVisibility(View.VISIBLE);
			mHolder.orderCancel.setText("待使用");
			mHolder.orderCancel.setBackgroundColor(Color.WHITE);
			mHolder.orderCancel.setTextColor(mContext.getResources().getColor(
					R.color.text_color_26));
			mHolder.orderCancel.setGravity(Gravity.RIGHT
					| Gravity.CENTER_VERTICAL);
			mHolder.orderCancel.setPadding(0, 0, 20, 0);
		} else if (now_histroy == 2) {
			// 1-预定成功 2-取消预定 3-已出票 4-已验票 5.已失效
			// 这个是历史的活动
			if (orderInfo.getOrderPayStatus().equals("2")) {
				mHolder.orderCancel.setText("已取消");
			} else if (orderInfo.getOrderPayStatus().equals("5")) {
				mHolder.orderCancel.setText("已失效");
			} else if (orderInfo.getOrderPayStatus().equals("3")) {
				mHolder.orderCancel.setText("已出票");
			} else if (orderInfo.getOrderPayStatus().equals("4")) {
				mHolder.orderCancel.setText("已验票");
			} else if (orderInfo.getOrderPayStatus().equals("1")) {
				mHolder.orderCancel.setText("已使用");
			}
			mHolder.orderCancel.setVisibility(View.VISIBLE);
			mHolder.orderCancel.setGravity(Gravity.RIGHT
					| Gravity.CENTER_VERTICAL);
			mHolder.orderCancel.setPadding(0, 0, 20, 0);
			mHolder.orderCancel.setBackgroundColor(Color.WHITE);
			mHolder.orderCancel.setTextColor(mContext.getResources().getColor(
					R.color.text_color_26));
			// mHolder.orderCancel.setText(getActivityState(orderInfo
			// .getOrderPayStatus()));
		}
	}

	/**
	 * 设置活动室item 场馆
	 * */
	private void setRoomView(final MyActivityRoomInfo orderInfo,
			ViewHolder mHolder) {
		if (null == orderInfo) {
			return;
		}
		mHolder.orderRoomField.setVisibility(View.GONE);
		mHolder.orderRoomGroup.setVisibility(View.GONE);
		mHolder.linearRoom.setVisibility(View.VISIBLE);
		// mHolder.orderRoomTime.setText(MyApplication.getStringToDate(Long
		// .valueOf(orderInfo.getOrderTime() + "000")));
		mHolder.orderRoomField.setText("活动室：" + orderInfo.getRoomName());
		mHolder.orderRoomTime.setText(MyApplication.getStrNewTime(orderInfo
				.getOrderTime()));
		mHolder.orderTitle.setText(orderInfo.getRoomName());
		mHolder.orderRoomAdd.setText(orderInfo.getVenueName());

		// String week = "";
		// String hour_last = "";
		// String hour = "";
		// int hour_num = 0;
		// try {
		// week = MyApplication.formatFractionalPart(MyApplication
		// .dayForWeek(orderInfo.getRoomTime().split(" ")[0]));
		// hour_last = orderInfo.getRoomTime().split(" ")[2].split("-")[1]
		// .split(":")[0];
		// hour = orderInfo.getRoomTime().split(" ")[2].split("-")[0]
		// .split(":")[0];
		// hour_num = Integer.valueOf(hour_last) - Integer.valueOf(hour);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// mHolder.order_room_num.setText(orderInfo.getRoomTime().split(" ")[0]
		// + "    周" + week);
		// mHolder.order_room_hour.setText(orderInfo.getRoomTime().split(" ")[2]
		// + "    " + hour_num + "小时");
		mHolder.order_room_num.setText(orderInfo.getRoomTime().split(" ")[0]
				+ "    " + orderInfo.getRoomTime().split(" ")[1]);
		mHolder.order_room_hour.setText(orderInfo.getRoomTime().split(" ")[2]
				+ "    " + orderInfo.getRoomTime().split(" ")[3]);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlSmall(orderInfo.getRoomPicUrl()),
						mHolder.img_url, Options.getRoundOptions(0));
Log.i(TAG, "  场馆  "+orderInfo.getRoomPicUrl());
		// mHolder.order_room_num.setText(orderInfo.getRoomTime().split(" ")[0]);
		// mHolder.order_room_hour.setText(orderInfo.getRoomTime().split(" ")[1]);

		mHolder.orderRoomGroup.setText("团体：" + orderInfo.getTuserTeamName());
		mHolder.orderCode.setText(handleText(orderInfo.getValidCode()));
		mHolder.activityOrderId.setText(orderInfo.getRoomOrderNo());
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(orderInfo.getRoomQrcodeUrl(),
						mHolder.orderQrCode, Options.getRoundOptions(10));
		MyApplication.displayFromDrawable(R.drawable.myorder_venue_icon,
				mHolder.activity_venue_img);
		mHolder.orderNumber.setText(orderInfo.getRoomOrderNo());
		mHolder.orderTime.setText(orderInfo.getRoomOrderCreateTime());
		// mHolder.total_price.setText(orderInfo.get)
		if (orderInfo.getOrderStatus().equals("1")) {
			// mHolder.orderCancel
			// .setBackgroundResource(R.drawable.shape_item_order_but);

			// mHolder.orderCancel.setText(getVenueState(orderInfo
			// .getOrderStatus()));

			/**
			 * 3.5.2 取消
			 */
			// mHolder.orderCancel.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// MyOrderActivity.getInstance().goToDialogRoom(orderInfo);
			// }
			// });
		} else {
			// mHolder.orderCancel.setText(getVenueState(orderInfo
			// .getOrderStatus()));
		}
		if (now_histroy == 1) {
			// 这个是待参加的活动
			mHolder.orderCancel.setVisibility(View.VISIBLE);
			mHolder.orderCancel.setText("待使用");
			mHolder.orderCancel.setBackgroundColor(Color.WHITE);
			mHolder.orderCancel.setTextColor(mContext.getResources().getColor(
					R.color.text_color_26));
			mHolder.orderCancel.setGravity(Gravity.RIGHT
					| Gravity.CENTER_VERTICAL);
			mHolder.orderCancel.setPadding(0, 0, 20, 0);
		} else if (now_histroy == 2) {
			// 这个是历史的活动
			// 0.待审核, 1.已预订,2.已取消,3.已验票 4.已删除 5.已出票 6.已失效 )
			// 1-预定成功 2-取消预定 3-预定失败 4-已出票 5-已验票 6.已失效
			if (orderInfo.getOrderStatus().equals("6")) {
				mHolder.orderCancel.setText("已过期");
			} else if (orderInfo.getOrderStatus().equals("2")) {
				// 这里还要判断
				if (orderInfo.getCheckStatus() == 2) {
					mHolder.orderCancel.setText("审核未通过");
				} else {
					mHolder.orderCancel.setText("已取消");
				}

			} else if (orderInfo.getOrderStatus().equals("4")) {
				mHolder.orderCancel.setText("待使用");
			} else if (orderInfo.getOrderStatus().equals("1")) {
				mHolder.orderCancel.setText("待使用");
			} else if (orderInfo.getOrderStatus().equals("5")) {
				mHolder.orderCancel.setText("已使用");
			} else if (orderInfo.getOrderStatus().equals("3")) {
				mHolder.orderCancel.setText("已使用");
			}
			mHolder.orderCancel.setGravity(Gravity.RIGHT
					| Gravity.CENTER_VERTICAL);
			mHolder.orderCancel.setPadding(0, 0, 20, 0);
			mHolder.orderCancel.setVisibility(View.VISIBLE);
			mHolder.orderCancel.setBackgroundColor(Color.WHITE);
			mHolder.orderCancel.setTextColor(mContext.getResources().getColor(
					R.color.text_color_26));
			// mHolder.orderCancel.setText(getActivityState(orderInfo
			// .getOrderPayStatus()));
		}

	}

	public String handleText(String str) {
		String len = "";
		int i = 0;

		while (i + 4 < str.length()) {
			len += str.substring(i, i + 4) + " ";
			i += 4;
		}
		len += str.substring(i, str.length());

		return len;
	}

	// 活动
	public String getActivityState(String state) {
		String stat = "";
		if (null == state) {
			return "";
		}
		int s = Integer.valueOf(state);
		switch (s) {
		case 1:
			stat = "取 消 订 单";
			break;
		case 2:
			stat = "已取消";
			break;
		case 3:
			stat = "已 出 票";
			break;
		case 4:
			stat = "已 验 票";
			break;
		case 5:
			stat = "已 失 效";
			break;

		default:
			stat = "已 失 效";
			break;
		}

		return stat;
	}

	// 场馆
	public String getVenueState(String state) {
		String stat = "";
		if (null == state || state.length() == 0) {
			return "";
		}
		int s = Integer.valueOf(state);
		switch (s) {
		case 1:
			stat = "取 消 订 单";
			break;
		case 2:
			stat = "已 取 消";
			break;
		case 3:
			stat = "预 定 失 败";
			break;
		case 4:
			stat = "已 出 票";
			break;
		case 5:
			stat = "已 验 票";
			break;
		case 6:
			stat = "已 失 效";
			break;

		default:
			break;
		}

		return stat;
	}

	private OnItemClickListener onGridview = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

		}
	};

	class ViewHolder {
		TextView activityOrderId;
		TextView orderPayStatus;
		TextView order_room_num;
		TextView total_price;
		View view;
		TextView order_room_hour;
		TextView orderTitle;
		View orderLinear;
		TextView orderRoomField;
		TextView orderRoomAdd;
		TextView orderRoomTime;
		TextView orderRoomNumber;
		TextView orderRoomGroup;
		TextView orderCode;
		ImageView orderQrCode;
		ScrollViewGridView orderSeat;
		TextView orderNumber;
		TextView orderTime;
		View linearActivity;
		View linearRoom;
		RelativeLayout seatlayout;
		Button orderCancel;
		Button orderSendOut;
		View itemView;
		ImageView activity_venue_img;
		ImageView img_url;
	}
}
