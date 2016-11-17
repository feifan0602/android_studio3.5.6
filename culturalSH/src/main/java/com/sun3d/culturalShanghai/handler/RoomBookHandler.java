package com.sun3d.culturalShanghai.handler;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.EventRoomActivity;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 活动室预定
 * 
 * @author yangyoutao
 * 
 */
public class RoomBookHandler {
	public static class RoomBookInfo {
		public static String reserve_user_et;// 预订用途
		public static String reserve_user_person;// 使用人
		public static String roomId;// 活动室id（必选）
		public static String venueName;// 展馆名称
		public static String venueAddress;// 展馆地址
		public static String roomName;// 活动室名称
		public static String orderRoomDate;// 活动室预定日期
		public static String openPeriod;// 活动室开放时间段
		public static String orderName;// 预定人姓名
		public static String orderMobileNum;// 预定电话号码
		public static String teamUserId;// 团体名称
		public static String teamUserName;// 团体用户id
		public static String orderNo;// 订单编号
		public static String bookId;// 活动室预定ID
		public static String tuserId = "";// 活动室预定ID
		public static String tuserName;
		public static String cmsRoomOrderId;
		public static int userType;// 实名认证
		public static int tuserIsDisplay;// 资质认证
	}

	private static LoadingDialog mLoadingDialog;
	private static String loadingText = "加载中";

	/**
	 * 活动室预定
	 * 
	 * @param mcontext
	 * @param roomId
	 * @param venueName
	 * @param venueAddress
	 * @param roomName
	 * @param orderRoomDate
	 * @param openPeriod
	 * @param orderName
	 * @param orderMobileNum
	 * @param teamUserId
	 * @param teamUserName
	 */
	public static void onRoomBook(final Context mcontext) {
		mLoadingDialog = new LoadingDialog(mcontext);
		Intent intent = new Intent(mcontext, MessageDialog.class);
		FastBlur.getScreen((Activity) mcontext);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.MessageDialog.MSGTYPE_TRUE_SCHEDULE);
		mcontext.startActivity(intent);
		// getBookId(mcontext);
	}

	/**
	 * 活动室预订
	 * 
	 * @param mcontext
	 */
	private static void getBookId(final Context mcontext) {
		mLoadingDialog.startDialog(loadingText);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("roomId", RoomBookInfo.roomId);
		params.put("venueName", RoomBookInfo.venueName);
		params.put("venueAddress", RoomBookInfo.venueAddress);
		params.put("roomName", RoomBookInfo.roomName);
		params.put("orderRoomDate", RoomBookInfo.orderRoomDate);
		params.put("openPeriod", RoomBookInfo.openPeriod);
		params.put("orderName", RoomBookInfo.orderName);
		params.put("orderMobileNum", RoomBookInfo.orderMobileNum);
		params.put("teamUserId", RoomBookInfo.teamUserId);
		params.put("teamUserName", RoomBookInfo.teamUserName);
		MyHttpRequest.onHttpPostParams(HttpUrlList.ActivityRoomUrl.ROOM_BOOK,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								JsonUtil.getRoomBookInfo(resultStr);
								Intent intent = new Intent(mcontext,
										MessageDialog.class);
								FastBlur.getScreen((Activity) mcontext);
								intent.putExtra(
										DialogTypeUtil.DialogType,
										DialogTypeUtil.MessageDialog.MSGTYPE_TRUE_SCHEDULE);
								mcontext.startActivity(intent);
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else {

							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 活动室预定确认
	 * 
	 * @param mcontext
	 */
	public static void onTrueBook() {
		mLoadingDialog.startDialog(loadingText);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("bookId", RoomBookInfo.bookId);
		params.put("orderName", RoomBookInfo.orderName);
		params.put("orderTel", RoomBookInfo.orderMobileNum);

		//
		params.put("tuserId", RoomBookInfo.tuserId);
		params.put("tuserName", RoomBookHandler.RoomBookInfo.reserve_user_person);
		params.put("purpose", RoomBookInfo.reserve_user_et);

		// params.put("teamUserId", RoomBookInfo.teamUserId);
		// params.put("orderNo", RoomBookInfo.orderNo);
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.ActivityRoomUrl.ROOM_BOOK_TRUE, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub

						mLoadingDialog.cancelDialog();
						Log.d("statusCode", statusCode + "确认订单的详情" + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								try {
									JSONObject jo = new JSONObject(resultStr);
									JSONObject json_ob = jo
											.getJSONObject("data");
									RoomBookInfo.cmsRoomOrderId = json_ob
											.getString("cmsRoomOrderId");
									RoomBookInfo.userType = json_ob
											.getInt("userType");
									RoomBookInfo.tuserIsDisplay = json_ob
											.getInt("tuserIsDisplay");
									EventRoomActivity.getIntance().gotoNext();
								} catch (Exception e) {
									e.printStackTrace();
								}

							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

}
