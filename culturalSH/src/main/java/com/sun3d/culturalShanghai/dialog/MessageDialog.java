package com.sun3d.culturalShanghai.dialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ScreenshotUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.Utils;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.activity.MyRoomStutasActivity;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.handler.RoomBookHandler;
import com.sun3d.culturalShanghai.handler.VersionUpdate;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;
import com.sun3d.culturalShanghai.object.MyOrderInfo;
import com.sun3d.culturalShanghai.object.MyVenueInfo;
import com.sun3d.culturalShanghai.thirdparty.MyShare;
import com.sun3d.culturalShanghai.thirdparty.ShareName;
import com.sun3d.culturalShanghai.view.FastBlur;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MessageDialog extends Activity implements
		android.view.View.OnClickListener {
	private String TAG = "MessageDialog";
	private int type = DialogTypeUtil.MessageDialog.MSGTYPE_SHOW_TEXT;
	private Context mContext;
	private String content = " ";
	private RelativeLayout contetnt;

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.mContext = this;
		if (getIntent() != null && getIntent().getExtras() != null) {
			this.type = getIntent().getExtras().getInt(
					DialogTypeUtil.DialogType);
			this.content = getIntent().getExtras().getString(
					DialogTypeUtil.DialogContent);
		}
		this.setContentView(R.layout.activity_messagedialog_layout);
		ImageView mainView = (ImageView) findViewById(R.id.main);
		contetnt = (RelativeLayout) findViewById(R.id.content_layout);
		super.onCreate(savedInstanceState);
		switch (type) {
		case DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE:// 拨打电话
			addTellPhoneView(content);
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_SHOW_TEXT:// 显示文本信息
			addShowText(content);
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_SCHEDULE_ACTIVITY:// 活动预定
			addScheduleActivity();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_SCHEDULE_VENUE:// 场馆预定
			addScheduleVenue();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_ACTIVITY:// 活动取消
			cancelScheduleActivity();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_CHECK_ORDER_ACTIVITY:// 待审核的场馆取消
			cancelScheduleCheckVenue(false);
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_VENUE:// 场馆取消
			// cancelScheduleVenue(false);
			cancelScheduleRoom(false);
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_CHECK_ROOM:// 场馆取消
			// cancelScheduleVenue(false);
			cancelCheckRoom(false);
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_TRUE_SCHEDULE:// 确认预定
			trueScheduleVenue();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_VERSION_UPDATER:// 版本更新
			versionupdaterActivity();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_GROUP_ADD:// 申请加入团体
			addGroupAdd();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_NOGROUP_SHOW:// 非团体会员点评场馆提示消息
			cancelScheduleVenue(true);
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_ELECTRONIC_TICKET:// 电子发票
			// electronicTicketVenue();
			break;
		case DialogTypeUtil.MessageDialog.MSGTYPE_ELECTRONIC_SHARE:// 订单中的分享
			orderShare();
			break;
		default:
			break;
		}

		FastBlur.setImageViewBG(mContext, mainView);
		mainView.setOnClickListener(this);

	}

	private void cancelCheckRoom(boolean isdisable) {
		final MyActivityBookInfo mInfo = (MyActivityBookInfo) this.getIntent()
				.getSerializableExtra(DialogTypeUtil.DialogContent);
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_copy_layout, null);
		Button commit = (Button) mainView.findViewById(R.id.schedule_commit);
		commit.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_z_ok));
		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(DialogTypeUtil.DialogContent, mInfo);
				setResult(HttpCode.BACK_CHCEK_ROOM, intent);
				finish();
			}
		});

		Button cancel = (Button) mainView.findViewById(R.id.schedule_cancel);
		cancel.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_z));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView title = (TextView) mainView.findViewById(R.id.schedule_title);
		title.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_activity));

		TextView content = (TextView) mainView
				.findViewById(R.id.schedule_content);
		String orderId = disposeInfo(mInfo);
		content.setText(orderId);
		contetnt.addView(mainView);
	}

	/**
	 * @author zhoutanping 订单分享
	 * */
	private void orderShare() {
		final MyActivityBookInfo mInfo = (MyActivityBookInfo) this.getIntent()
				.getSerializableExtra(DialogTypeUtil.DialogContent);
		contetnt.removeAllViewsInLayout();
		View view = (View) getLayoutInflater().inflate(
				R.layout.dialog_order_share, null);
		TextView orderInfo = (TextView) view
				.findViewById(R.id.dialog_order_info);
		view.findViewById(R.id.do_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		view.findViewById(R.id.dialog_order_qq).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra(DialogTypeUtil.DialogContent,
								mInfo.getOrderValidateCode() + "QQ");
						setResult(HttpCode.BACK_QQ, intent);
						finish();
					}
				});
		view.findViewById(R.id.dialog_order_wx).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra(DialogTypeUtil.DialogContent,
								mInfo.getOrderValidateCode() + "WX");
						setResult(HttpCode.BACK_QQ, intent);
						finish();
					}
				});
		String mTime = Utils.getStrTime(mInfo.getOrderTime());
		String info = "您将把" + mTime + "的“" + mInfo.getActivityName()
				+ "”活动票赠送给你的朋友， 请选择以下的任一种方式进行赠送：";

		orderInfo.setText(info);
		contetnt.addView(view);
	}

	/**
	 * 获取电子票信息
	 */
	private void getElectronicTicket(final String electronicTicketId,
			final String ActivitySalesOnline) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityOrderId", electronicTicketId);
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.EventUrl.ELECTRONICTICKET_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							MyActivityBookInfo info = JsonUtil
									.getElectronicTicketInfo(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.status) {
								electronicTicketVenue(info, electronicTicketId,
										ActivitySalesOnline);
							} else {
								ToastUtil.showToast("获取订单信息失败");
							}
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 切割座位信息
	 * 
	 * @param seats
	 */
	private String getSeatsString(String seats) {
		String seatsString = "";
		String[] mStrings = seats.split(",");
		for (int i = 0; i < mStrings.length; i++) {
			String[] mSeats = mStrings[i].split("_");
			String mString = "";
			if (mSeats.length >= 2) {
				mString = mSeats[0] + "排" + mSeats[1] + "座";
			}
			seatsString = seatsString + mString + "；";
		}
		seatsString = seatsString.substring(0, seatsString.length() - 1);
		return seatsString;
	}

	/**
	 * 电子发票
	 */
	private void electronicTicketVenue(MyActivityBookInfo info,
			final String electronicTicketId, final String ActivitySalesOnline) {
		contetnt.removeAllViewsInLayout();
		final String imgUrl = "/" + electronicTicketId + ".png";
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_electronic_ticke, null);
		final LinearLayout mContent = (LinearLayout) mainView
				.findViewById(R.id.electronic_content);
		final TextView sharetext = (TextView) mainView
				.findViewById(R.id.share_text);
		final ImageView mQqShare = (ImageView) mainView
				.findViewById(R.id.qq_share);
		final ImageView mWeixinShare = (ImageView) mainView
				.findViewById(R.id.weixin_share);
		final TextView ShowSeattext = (TextView) mainView
				.findViewById(R.id.activity_steas_show);
		TextView mName = (TextView) mainView.findViewById(R.id.activity_name);
		TextView mAdress = (TextView) mainView
				.findViewById(R.id.activity_adress);
		TextView mTime = (TextView) mainView.findViewById(R.id.activity_time);
		TextView mSteas = (TextView) mainView.findViewById(R.id.activity_steas);
		ImageView mUrl = (ImageView) mainView.findViewById(R.id.activity_url);
		mName.setText(info.getActivityName());
		mAdress.setText("地点: " + info.getActivityAddress());
		mTime.setText("场次: " + info.getActivityEventDateTime());
		if (ActivitySalesOnline.equals("N")) {// 自由入座
			ShowSeattext.setText("票数: ");
			mSteas.setText(info.getActivitySeats().substring(
					info.getActivitySeats().length() - 1));
		} else {// 在线选座
			mSteas.setText(getSeatsString(info.getActivitySeats()));
		}

		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						info.getActivityQrcodeUrl(),
						mUrl,
						Options.getListOptions(R.drawable.sh_icon_error_loading));
		isGone(sharetext, mQqShare, mWeixinShare, true);
		// whiteBg.setVisibility(View.GONE);
		mQqShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isGone(sharetext, mQqShare, mWeixinShare, false);
				ScreenshotUtil.getData(mContent, imgUrl, new CollectCallback() {
					@Override
					public void onPostExecute(boolean isOK) {
						// TODO Auto-generated method stub
						if (isOK) {
							MyShare.showShare(ShareName.QQ, mContext,
									FolderUtil.createImageCacheFile() + imgUrl);
							TicketUIObject TUI = new TicketUIObject();
							TUI.isShow = true;
							TUI.mQqShare = mQqShare;
							TUI.mWeixinShare = mWeixinShare;
							TUI.sharetext = sharetext;
							Message msg = new Message();
							msg.what = NOTIFYUI;
							msg.obj = TUI;
							myViewShowHanderl.sendMessage(msg);
						} else {
							ToastUtil.showToast("分享失败");
						}
					}
				});

			}
		});
		mWeixinShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isGone(sharetext, mQqShare, mWeixinShare, false);
				ScreenshotUtil.getData(mContent, imgUrl, new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOK) {
						// TODO Auto-generated method stub
						if (isOK) {
							MyShare.showShare(ShareName.Wechat, mContext,
									FolderUtil.createImageCacheFile() + imgUrl);
							TicketUIObject TUI = new TicketUIObject();
							TUI.isShow = true;
							TUI.mQqShare = mQqShare;
							TUI.mWeixinShare = mWeixinShare;
							TUI.sharetext = sharetext;
							Message msg = new Message();
							msg.what = NOTIFYUI;
							msg.obj = TUI;
							myViewShowHanderl.sendMessage(msg);
						} else {
							ToastUtil.showToast("分享失败");
						}
					}
				});

			}
		});
		contetnt.addView(mainView);
	}

	/**
	 * UI更新的对象
	 * 
	 * @author yangyoutao
	 * 
	 */
	private class TicketUIObject {
		public TextView sharetext;
		public ImageView mQqShare;
		public ImageView mWeixinShare;
		public boolean isShow;

	}

	/**
	 * UI更新的handler
	 */
	private final int NOTIFYUI = 101;
	Handler myViewShowHanderl = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case NOTIFYUI:
				TicketUIObject TUI = (TicketUIObject) msg.obj;
				isGone(TUI.sharetext, TUI.mQqShare, TUI.mWeixinShare,
						TUI.isShow);
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 截屏隐藏
	 * 
	 * @param sharetext
	 * @param mQqShare
	 * @param mWeixinShare
	 */
	private void isGone(TextView sharetext, ImageView mQqShare,
			ImageView mWeixinShare, boolean isShow) {
		if (isShow) {
			sharetext.setVisibility(View.VISIBLE);
			mQqShare.setVisibility(View.VISIBLE);
			mWeixinShare.setVisibility(View.VISIBLE);
		} else {
			sharetext.setVisibility(View.GONE);
			mQqShare.setVisibility(View.GONE);
			mWeixinShare.setVisibility(View.GONE);
		}
	}

	/**
	 * 加入团体成功
	 */
	private void addGroupAdd() {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_addgroup_layout, null);
		contetnt.addView(mainView);
	}

	/**
	 * 拨打电话
	 * 
	 * @param num
	 */
	private void addTellPhoneView(String num) {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_tellphone_layout, null);
		TextView content = (TextView) mainView.findViewById(R.id.phone_num);
		content.setText(num);
		mainView.findViewById(R.id.phone_cancel).setOnClickListener(this);
		mainView.findViewById(R.id.phone_ok).setOnClickListener(this);
		contetnt.addView(mainView);
	}

	/**
	 * 显示文本内容
	 * 
	 * @param text
	 */
	private void addShowText(String text) {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_showtext_layout, null);
		TextView content = (TextView) mainView.findViewById(R.id.show_text);
		content.setText(text);
		contetnt.addView(mainView);
	}

	/**
	 * 预约活动
	 */
	private void addScheduleActivity() {
		contetnt.removeAllViewsInLayout();
		final String mName = getIntent().getExtras().getString("mName");
		final String myTitle = getIntent().getExtras().getString("mTitle");
		final String mTime = getIntent().getExtras().getString("mTime");
		final String mNumber = getIntent().getExtras().getString("mNumber");
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_layout, null);
		TextView mTitle = (TextView) mainView.findViewById(R.id.schedule_title);
		TextView mContent = (TextView) mainView
				.findViewById(R.id.schedule_content);
		Button mCancel = (Button) mainView.findViewById(R.id.schedule_commit);
		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// getElectronicTicket(electronicTicketId, ActivitySalesOnline);
				// 生成电子票
				finish();
			}
		});
		mCancel.setText("我 知 道 了");
		String name = "";
		if (null == mName && mName.equals(""))
			name = "您好！";
		else
			name = mName + "，您好！";
		mTitle.setText(name);

		// String content = "您已成功预订" + handleString(mTime) + "“" + myTitle
		// + "”活动，短信已发至您" + mNumber + "手机，请查收。";
		String content = "您已成功预订" + mTime + "“" + myTitle + "”活动，短信已发至您"
				+ mNumber + "手机，请查收。";
		mContent.setText(content);
		Button backactivity = (Button) mainView
				.findViewById(R.id.schedule_cancel);
		backactivity.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_activity_text));
		backactivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MyOrderActivity.class);
				MyApplication.room_activity = 1;
				startActivity(intent);
				finish();
			}
		});

		contetnt.addView(mainView);
	}

	private String handleString(String time) {
		String info = "";
		if (null == time && time.equals("")) {
			return "";
		}
		time = time.replaceFirst("-", "年").replaceFirst("-", "月");
		info = time.substring(0, time.indexOf("月") + 3);
		time = time.substring(info.length(), time.length());
		time = info + "日" + time;

		return time.replace(" ", "");
	}

	/**
	 * 取消预约活动
	 */
	private void cancelScheduleActivity() {
		final MyActivityBookInfo mInfo = (MyActivityBookInfo) this.getIntent()
				.getSerializableExtra(DialogTypeUtil.DialogContent);
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_copy_layout, null);
		Button commit = (Button) mainView.findViewById(R.id.schedule_commit);
		commit.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_z_ok));
		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra(DialogTypeUtil.DialogContent, mInfo);
				setResult(HttpCode.BACK, intent);
				finish();
			}
		});

		Button cancel = (Button) mainView.findViewById(R.id.schedule_cancel);
		cancel.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_z));
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView title = (TextView) mainView.findViewById(R.id.schedule_title);
		title.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_activity));

		TextView content = (TextView) mainView
				.findViewById(R.id.schedule_content);
		String orderId = disposeInfo(mInfo);
		content.setText(orderId);
		contetnt.addView(mainView);

	}

	/**
	 * 取消活动订单，或取消订单中的个别座位
	 * */
	private String disposeInfo(MyActivityBookInfo mInfo) {
		Log.i(TAG, "查看  id== " + mInfo.toString());
		String orderId = "您正在取消编号为" + mInfo.getOrderNumber()
				+ "的预约，取消后您需要重新下单。";

		if (mInfo.getIsSeatOnline()) {
			boolean boo = true;
			String seat = "";
			String[] seats = Utils.initSeats(mInfo.getActivitySeats());
			Log.i("tag", seats.length + ":" + mInfo.getoSummary().length);
			for (int j = 0; j < mInfo.getoSummary().length; j++) {
				if (mInfo.getoSummary()[j]) {
					seat += seats[j] + " ";
				} else {
					boo = false;
				}
			}

			if (!boo && seat.length() > 1) // 为true表示取消所有座位
				orderId = "您确定取消该订单的" + seat.substring(0, seat.length() - 1)
						+ "吗？";
		}

		return orderId;
	}

	/**
	 * 版本更新提示
	 */
	private void versionupdaterActivity() {

		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_layout, null);
		Button cancel = (Button) mainView.findViewById(R.id.schedule_commit);
		cancel.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel));
		cancel.setText("放弃更新");
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Button commit = (Button) mainView.findViewById(R.id.schedule_cancel);
		commit.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_ok));
		commit.setText("确定更新");
		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				VersionUpdate.updater();
				finish();
			}
		});
		TextView title = (TextView) mainView.findViewById(R.id.schedule_title);
		title.setText("新版本描述");

		TextView content = (TextView) mainView
				.findViewById(R.id.schedule_content);
		content.setText(this.content);
		contetnt.addView(mainView);

	}

	/**
	 * 预约场馆
	 */
	private void addScheduleVenue() {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_layout, null);
		mainView.findViewById(R.id.schedule_commit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stubMyVenueActivity
						Intent intent = new Intent(mContext,
								MyRoomStutasActivity.class);
						intent.putExtra("type", "venue");
						startActivity(intent);
						setResult(AppConfigUtil.LOADING_LOGIN_BACK);
						finish();
					}
				});
		Button backactivity = (Button) mainView
				.findViewById(R.id.schedule_cancel);
		backactivity.setText(mContext.getResources().getText(
				R.string.dialog_schedule_cancel_vevuebtn_text));
		backactivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				setResult(AppConfigUtil.LOADING_LOGIN_BACK);
				finish();
			}
		});
		contetnt.addView(mainView);
	}

	/**
	 * 取消预约活动
	 */
	private void cancelScheduleVenue(Boolean isdisable) {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_layout, null);
		Button cancel = (Button) mainView.findViewById(R.id.schedule_commit);
		Button commit = (Button) mainView.findViewById(R.id.schedule_cancel);
		TextView title = (TextView) mainView.findViewById(R.id.schedule_title);
		TextView content = (TextView) mainView
				.findViewById(R.id.schedule_content);
		if (isdisable) {
			title.setText(mContext.getResources().getText(
					R.string.tream_no_titleshow));
			commit.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			content.setText(mContext.getResources().getText(
					R.string.tream_no_contentshow));
		} else {
			final MyVenueInfo mInfo = (MyVenueInfo) this.getIntent()
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			title.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel_vevue));
			commit.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel_ok));
			commit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra(DialogTypeUtil.DialogContent, mInfo);
					setResult(HttpCode.BACK, intent);
					finish();
				}
			});
			cancel.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel));
			cancel.setOnClickListener(this);
			String orderid = "您正在取消编号为" + mInfo.getRoomOrderNo()
					+ "的预约，取消后您需要重新下单。";
			content.setText(orderid);
		}
		contetnt.addView(mainView);
	}

	private void cancelScheduleCheckVenue(Boolean isdisable) {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_layout, null);
		Button cancel = (Button) mainView.findViewById(R.id.schedule_commit);
		Button commit = (Button) mainView.findViewById(R.id.schedule_cancel);
		TextView title = (TextView) mainView.findViewById(R.id.schedule_title);
		TextView content = (TextView) mainView
				.findViewById(R.id.schedule_content);
		if (isdisable) {
			title.setText(mContext.getResources().getText(
					R.string.tream_no_titleshow));
			commit.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			content.setText(mContext.getResources().getText(
					R.string.tream_no_contentshow));
		} else {
			final MyActivityBookInfo mInfo = (MyActivityBookInfo) this
					.getIntent().getSerializableExtra(
							DialogTypeUtil.DialogContent);
			title.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel_vevue));
			commit.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel_ok));
			commit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra(DialogTypeUtil.DialogContent, mInfo);
					setResult(HttpCode.BACK_CHCEK_ROOM, intent);
					finish();
				}
			});
			cancel.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel));
			cancel.setOnClickListener(this);
			String orderid = "您正在取消编号为" + mInfo.getRoomOrderNo()
					+ "的预约，取消后您需要重新下单。";
			content.setText(orderid);
		}
		contetnt.addView(mainView);

	}

	/**
	 * 取消预约活动室
	 * 
	 * @author zhoutanping
	 */
	private void cancelScheduleRoom(Boolean isdisable) {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_schedule_layout, null);
		Button cancel = (Button) mainView.findViewById(R.id.schedule_commit);
		Button commit = (Button) mainView.findViewById(R.id.schedule_cancel);
		TextView title = (TextView) mainView.findViewById(R.id.schedule_title);
		TextView content = (TextView) mainView
				.findViewById(R.id.schedule_content);
		if (isdisable) {
			title.setText(mContext.getResources().getText(
					R.string.tream_no_titleshow));
			commit.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			content.setText(mContext.getResources().getText(
					R.string.tream_no_contentshow));
		} else {
			final MyActivityRoomInfo mInfo = (MyActivityRoomInfo) this
					.getIntent().getSerializableExtra(
							DialogTypeUtil.DialogContent);
			title.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel_vevue));
			commit.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel_ok));
			commit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra(DialogTypeUtil.DialogContent, mInfo);
					setResult(HttpCode.BACK_ROOM, intent);
					finish();
				}
			});
			cancel.setText(mContext.getResources().getText(
					R.string.dialog_schedule_cancel));
			cancel.setOnClickListener(this);
			String orderid = "您正在取消编号为" + mInfo.getRoomOrderNo()
					+ "的预约，取消后您需要重新下单。";
			content.setText(orderid);
		}
		contetnt.addView(mainView);
	}

	/**
	 * 确认预约
	 */
	private void trueScheduleVenue() {
		contetnt.removeAllViewsInLayout();
		View mainView = (View) getLayoutInflater().inflate(
				R.layout.dialog_true_schedule, null);
		mainView.findViewById(R.id.trueschedule_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		mainView.findViewById(R.id.trueschedule_commit).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						finish();
						RoomBookHandler.onTrueBook();

					}
				});
		TextView venue = (TextView) mainView
				.findViewById(R.id.trueschedule_venue);
		TextView address = (TextView) mainView
				.findViewById(R.id.trueschedule_address);
		TextView activityRoom = (TextView) mainView
				.findViewById(R.id.trueschedule_activity);
		TextView time = (TextView) mainView
				.findViewById(R.id.trueschedule_time);
		TextView scheduleName = (TextView) mainView
				.findViewById(R.id.trueschedule_schedulename);
		TextView phone = (TextView) mainView
				.findViewById(R.id.trueschedule_phone);
		TextView group = (TextView) mainView
				.findViewById(R.id.trueschedule_group);
		venue.setText(RoomBookHandler.RoomBookInfo.venueName);
		address.setText(RoomBookHandler.RoomBookInfo.venueAddress);
		activityRoom.setText(RoomBookHandler.RoomBookInfo.roomName);
		time.setText(RoomBookHandler.RoomBookInfo.orderRoomDate + " "
				+ RoomBookHandler.RoomBookInfo.openPeriod);
		scheduleName.setText(RoomBookHandler.RoomBookInfo.orderName);
		phone.setText(RoomBookHandler.RoomBookInfo.orderMobileNum);
		group.setText(RoomBookHandler.RoomBookInfo.teamUserName);
		contetnt.addView(mainView);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.phone_cancel:
			finish();
			break;
		case R.id.phone_ok:
			Intent intent = new Intent(Intent.ACTION_CALL);
			Uri data = Uri.parse("tel:" + content);
			intent.setData(data);
			startActivity(intent);
			finish();
			break;
		case R.id.schedule_cancel:
			finish();
			break;
		case R.id.schedule_commit:
			finish();
			break;
		default:
			// finish();
			break;
		}

	}

}
