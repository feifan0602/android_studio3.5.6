package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.ActivityRoomAdapter;
import com.sun3d.culturalShanghai.adapter.ActivityRoomDeviceAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LabelHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;
import com.sun3d.culturalShanghai.object.RoomDateInfo;
import com.sun3d.culturalShanghai.object.RoomDetailTimeSlotInfor;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.pulltozoomscrollview.PullToZoomScrollViewEx;
import com.umeng.analytics.MobclickAgent;

/**
 * 活动室详情
 * 
 * @author wenff
 * 
 */
public class ActivityRoomDateilsActivity extends Activity implements
		OnClickListener {
	private String TAG = "ActivityRoomDateilsActivity";
	private Context mContext;
	private PullToZoomScrollViewEx scrollView;
	private View contentView;
	private View reserve_view;;
	private String roomId;
	private ActivityRoomInfo mActivityRoomInfo;
	private TextView RoomName;
	private TextView RoomPhone;
	private TextView RoomTime;
	private TextView RoomMoney;
	private TextView RoomArea;
	private TextView RoomCapacity;
	private ImageView zoomView;
	public static List<RoomDetailTimeSlotInfor> timelist;
	private LoadingHandler mLoadingHandler;
	private TextView Equipmentm;// 设备

	public RelativeLayout activityroom_reserve;
	private LabelHandler mLabelHandler;
	private final String mPageName = "ActivityRoomDateilsActivity";
	private TextView memo;
	private RelativeLayout mMemoLayout;
	private ImageView left_iv;
	private TextView middle_tv;
	private GridView activityroom_device_gv;
	private ActivityRoomDeviceAdapter arda;
	private TextView tag_name, tag_name1, tag_name2, tag_name3;
	private HorizontalListView horizontal_lv;
	// private TextView text_frist, text_second, text_three, text_four;
	private ImageView front_iv, next_iv;
	private View itemView;
	private int mBgColor;
	private int arrow_bool = 0;
	public TextView null_tv;
	private TextView room_schedules, sure_tv;
	private TextView activityroom_consult_show, activityroom_area_show,
			activityroom_pople_show, activityroom_memo_show,
			activityroom_device_show;
	private LinearLayout activityroom_device_ll;

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
		setContentView(R.layout.activity_activity_room_dateils);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		roomId = this.getIntent().getExtras().getString("Id");
		init();
		getData();
	}

	private void init() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		if (timelist != null) {
			timelist.clear();
		}

		MyApplication.roomList_new.clear();
		MyApplication.roomList.clear();
		MyApplication.bookid = "";
		MyApplication.curDate = "";
		MyApplication.openPeriod = "";

		room_schedules = (TextView) findViewById(R.id.room_schedules);
		sure_tv = (TextView) findViewById(R.id.sure_tv);
		room_schedules.setTypeface(MyApplication.GetTypeFace());
		sure_tv.setTypeface(MyApplication.GetTypeFace());
		null_tv = (TextView) findViewById(R.id.null_tv);
		null_tv.setTypeface(MyApplication.GetTypeFace());
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());

		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
		scrollView.setFocusable(true);
		scrollView.setFocusableInTouchMode(true);
		zoomView = (ImageView) LayoutInflater.from(this).inflate(
				R.layout.scrollview_profile_zoom_view, null, false);
		contentView = LayoutInflater.from(this).inflate(
				R.layout.activityroomdateil_scrollcontentlayout, null, false);
		reserve_view = LayoutInflater.from(this).inflate(
				R.layout.activity_roomdateil_reserve_layout, null, false);
		View headView = LayoutInflater.from(this).inflate(
				R.layout.event_detail_collectlayout, null, false);
		activityroom_device_ll = (LinearLayout) contentView
				.findViewById(R.id.activityroom_ll);
		headView.findViewById(R.id.event_collect).setVisibility(View.GONE);

		activityroom_consult_show = (TextView) contentView
				.findViewById(R.id.activityroom_consult_show);
		activityroom_area_show = (TextView) contentView
				.findViewById(R.id.activityroom_area_show);
		activityroom_pople_show = (TextView) contentView
				.findViewById(R.id.activityroom_pople_show);
		activityroom_memo_show = (TextView) contentView
				.findViewById(R.id.activityroom_memo_show);
		activityroom_device_show = (TextView) contentView
				.findViewById(R.id.activityroom_device_show);
		activityroom_consult_show.setTypeface(MyApplication.GetTypeFace());
		activityroom_area_show.setTypeface(MyApplication.GetTypeFace());
		activityroom_pople_show.setTypeface(MyApplication.GetTypeFace());
		activityroom_memo_show.setTypeface(MyApplication.GetTypeFace());
		activityroom_device_show.setTypeface(MyApplication.GetTypeFace());

		tag_name = (TextView) headView.findViewById(R.id.tag_name);
		tag_name1 = (TextView) headView.findViewById(R.id.tag_name1);
		tag_name2 = (TextView) headView.findViewById(R.id.tag_name2);
		tag_name3 = (TextView) headView.findViewById(R.id.tag_name3);
		// text_frist = (TextView) findViewById(R.id.text_frist);
		// text_second = (TextView) findViewById(R.id.text_second);
		// text_three = (TextView) findViewById(R.id.text_three);
		// text_four = (TextView) findViewById(R.id.text_four);
		tag_name.setTypeface(MyApplication.GetTypeFace());
		tag_name1.setTypeface(MyApplication.GetTypeFace());
		tag_name2.setTypeface(MyApplication.GetTypeFace());
		tag_name3.setTypeface(MyApplication.GetTypeFace());

		front_iv = (ImageView) findViewById(R.id.front_iv);
		next_iv = (ImageView) findViewById(R.id.next_iv);
		front_iv.setOnClickListener(this);
		next_iv.setOnClickListener(this);
		// text_frist.setTypeface(MyApplication.GetTypeFace());
		// text_second.setTypeface(MyApplication.GetTypeFace());
		// text_three.setTypeface(MyApplication.GetTypeFace());
		// text_four.setTypeface(MyApplication.GetTypeFace());
		scrollView.setZoomView(zoomView);
		scrollView.setHeaderView(headView);
		scrollView.setScrollContentView(contentView);
		// scrollView.setScrollContentView(reserve_view);
		contentView.findViewById(R.id.activityroom_consult_phone)
				.setOnClickListener(this);
		RoomPhone = (TextView) contentView
				.findViewById(R.id.activityroom_consult);
		RoomName = (TextView) contentView.findViewById(R.id.activityroom_name);
		RoomTime = (TextView) contentView.findViewById(R.id.activityroom_time);
		RoomMoney = (TextView) contentView
				.findViewById(R.id.activityroom_money);
		RoomArea = (TextView) contentView.findViewById(R.id.activityroom_area);
		RoomCapacity = (TextView) contentView
				.findViewById(R.id.activityroom_pople);
		RoomPhone.setTypeface(MyApplication.GetTypeFace());
		RoomName.setTypeface(MyApplication.GetTypeFace());
		RoomTime.setTypeface(MyApplication.GetTypeFace());
		RoomMoney.setTypeface(MyApplication.GetTypeFace());
		RoomArea.setTypeface(MyApplication.GetTypeFace());
		RoomCapacity.setTypeface(MyApplication.GetTypeFace());

		activityroom_device_gv = (GridView) contentView
				.findViewById(R.id.activityroom_device_gv);
		horizontal_lv = (HorizontalListView) findViewById(R.id.gridview);
		Equipmentm = (TextView) contentView
				.findViewById(R.id.activityroom_device);
		Equipmentm.setText("");
		memo = (TextView) contentView.findViewById(R.id.activityroom_memo);
		mMemoLayout = (RelativeLayout) contentView
				.findViewById(R.id.activityroom_memo_layout);
		activityroom_reserve = (RelativeLayout) findViewById(R.id.activityroom_reserve);
		activityroom_reserve.setOnClickListener(this);
		contentView.findViewById(R.id.room_phone).setOnClickListener(this);
		LinearLayout label = (LinearLayout) contentView
				.findViewById(R.id.activityroom_label_layout);
		mLabelHandler = new LabelHandler(mContext, label, 3, false);
		Equipmentm.setTypeface(MyApplication.GetTypeFace());
		memo.setTypeface(MyApplication.GetTypeFace());

	}

	/**
	 * 添加设备的数据
	 * 
	 * @param str
	 */
	private void addEquipmentm(String[] str) {
		String html = "";
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			html = html + "<img src='" + R.drawable.sh_icon_ditch + "'/>"
					+ str[i] + "&#160;&#160;";
			list.add(str[i]);
		}

		activityroom_device_ll.setVisibility(View.VISIBLE);
		arda = new ActivityRoomDeviceAdapter(list, this);
		activityroom_device_gv.setAdapter(arda);
		MyApplication.setGridViewHeightBasedOnChildren(activityroom_device_gv);
		Equipmentm.append(getTextImg(html));
	}

	/**
	 * 获取详情数据
	 */
	private void getData() {
		Map<String, String> mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.ActivityRoomUrl.ROOMID, roomId);
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.ActivityRoomUrl.ROOMDETAIL_LIST_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						timelist = new ArrayList<RoomDetailTimeSlotInfor>();

						Log.d("statusCode", statusCode + "这个是详情数据---"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							mActivityRoomInfo = JsonUtil
									.getActivityRoomDetailInfo(resultStr);
							setData();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 设置数据
	 */
	private void setData() {
		// roomTagName
		if (!mActivityRoomInfo.getRoomTag().equals("")) {
			tag_name.setVisibility(View.VISIBLE);
			Log.i(TAG, "  看看 tagname==  "+mActivityRoomInfo.getRoomTag());
			tag_name.setText(mActivityRoomInfo.getRoomTag());
		}
		if (mActivityRoomInfo.getTagNameList().size() != 0) {
			if (mActivityRoomInfo.getTagNameList().size() == 1) {
				tag_name1.setVisibility(View.VISIBLE);
				tag_name1.setText(mActivityRoomInfo.getTagNameList().get(0));
			}
			if (mActivityRoomInfo.getTagNameList().size() == 2) {
				tag_name1.setVisibility(View.VISIBLE);
				tag_name1.setText(mActivityRoomInfo.getTagNameList().get(0));
				tag_name2.setVisibility(View.VISIBLE);
				tag_name2.setText(mActivityRoomInfo.getTagNameList().get(1));
			}
		}

		// if (mActivityRoomInfo.getRoomTag() != null
		// && mActivityRoomInfo.getRoomTag() != "") {
		// tag_name.setVisibility(View.VISIBLE);
		// // 这里来判断 展示情况 3.5.3 需要将数据 分开
		// String[] RoomTags = mActivityRoomInfo.getRoomTag().split(",");
		// switch (RoomTags.length) {
		// case 1:
		// tag_name.setText(RoomTags[0]);
		// break;
		// case 2:
		// tag_name1.setVisibility(View.VISIBLE);
		// tag_name.setText(RoomTags[0]);
		// tag_name1.setText(RoomTags[1]);
		// break;
		// case 3:
		// tag_name1.setVisibility(View.VISIBLE);
		// tag_name2.setVisibility(View.VISIBLE);
		// tag_name.setText(RoomTags[0]);
		// tag_name1.setText(RoomTags[1]);
		// tag_name2.setText(RoomTags[2]);
		// break;
		// }

		// }
		if (null != mActivityRoomInfo) {
			if (mActivityRoomInfo.getSysNo() == 0) {
				// 这里判断 3.5.2 不论是谁都能预订活动室活动
				if (timelist != null && timelist.size() > 0) {
					null_tv.setVisibility(View.GONE);
					activityroom_reserve.setVisibility(View.VISIBLE);
				} else {
					null_tv.setVisibility(View.VISIBLE);
					activityroom_reserve.setVisibility(View.GONE);
				}
			} else {
				activityroom_reserve.setVisibility(View.VISIBLE);
			}
			middle_tv.setText(mActivityRoomInfo.getRoomName());
			RoomName.setText(mActivityRoomInfo.getRoomName());
			RoomPhone.setText(mActivityRoomInfo.getRoomConsultTel());
			if (mActivityRoomInfo.getRoomFee() <= 0) {
				RoomMoney.setText("免费");
				RoomMoney.setCompoundDrawables(null, null, null, null);
			} else {
				RoomMoney
						.setText(String.valueOf(mActivityRoomInfo.getRoomFee()));
			}

			if (mActivityRoomInfo.getMemo() != null
					&& mActivityRoomInfo.getMemo().length() > 0) {
				mMemoLayout.setVisibility(View.VISIBLE);
				memo.setText(mActivityRoomInfo.getMemo());
			} else {
				mMemoLayout.setVisibility(View.GONE);
			}
			RoomArea.setText(mActivityRoomInfo.getRoomArea() + "平方");//
			RoomCapacity.setText(mActivityRoomInfo.getRoomCapacity() + "人");
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlMiddle(mActivityRoomInfo
									.getRoomPicUrl()), zoomView,
							Options.getListOptions());
			mLabelHandler.setLabelsData(mActivityRoomInfo.getRoomTag());
			String[] str = mActivityRoomInfo.getRoomFacility().split(",");
			if (str != null && str.length > 0
					&& mActivityRoomInfo.getRoomFacility().trim().length() > 0) {
				addEquipmentm(str);
			} else {
				activityroom_device_ll.setVisibility(View.GONE);
				Equipmentm.append("无");
			}
			mLoadingHandler.stopLoading();
		} else {
			mLoadingHandler.showErrorText("加载失败！");
		}
		scrollView.scrollBy(0, 0);
		ActivityRoomAdapter ara = new ActivityRoomAdapter(this, timelist);
		horizontal_lv.setAdapter(ara);
		MyApplication.setHorizontalListViewBasedOnChildren(horizontal_lv);
		Log.i("ceshi", "看看size===  " + MyApplication.roomList_new.size()
				+ "  timelist== " + timelist.size());
		if (timelist.size() <= 4) {
			front_iv.setVisibility(View.INVISIBLE);
			next_iv.setVisibility(View.INVISIBLE);
		} else if (timelist.size() > 4) {
			next_iv.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_room_dateils, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		// 上一个
		case R.id.front_iv:
			if (arrow_bool >= 1) {
				arrow_bool--;
				if (arrow_bool == 0) {
					front_iv.setVisibility(View.INVISIBLE);
					next_iv.setVisibility(View.VISIBLE);
					horizontal_lv.setFrontSelection(0);
				}
				horizontal_lv.setFrontSelection(arrow_bool);
			}
			// for (int i = 0; i < timelist.size(); i++) {
			//
			// }
			// ActivityRoomAdapter ara = new ActivityRoomAdapter(mContext,
			// timelist);
			// horizontal_lv.setAdapter(ara);

			break;
		// 下一个
		case R.id.next_iv:
			Log.i("ceshi",
					" num ==  " + arrow_bool + " size  ==  " + timelist.size());
			arrow_bool++;
			if (arrow_bool * 4 < timelist.size()) {

				horizontal_lv.setNextSelection(arrow_bool);

			} else {
				next_iv.setVisibility(View.INVISIBLE);
			}

			front_iv.setVisibility(View.VISIBLE);
			break;
		case R.id.activityroom_consult_phone:
			FastBlur.getScreen((Activity) mContext);
			intent = new Intent(mContext, MessageDialog.class);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			intent.putExtra(DialogTypeUtil.DialogContent, RoomPhone.getText()
					.toString());
			startActivity(intent);
			break;
		case R.id.activityroom_reserve:
			if (MyApplication.UserIsLogin) {
				// if (MyApplication.loginUserInfor.getUserType().equals("2"))
				// {// 如果用户是团体管理员
				if (timelist.size() > 0) {
					// if (null !=
					// MyApplication.loginUserInfor.getUserMobileNo()
					// && MyApplication.loginUserInfor.getUserMobileNo()
					// .toString().length() > 0) {// 没有手机号
					if (MyApplication.bookid.equals("")) {
						ToastUtil.showToast("请选择日期");
					} else {
						if (MyApplication.bookid == "") {
							ToastUtil.showToast("请选择一个时间段");
						} else {
							intent = new Intent(mContext,
									EventRoomActivity.class);
							intent.putExtra("roomname",
									mActivityRoomInfo.getRoomName());
							intent.putExtra("venuename",
									mActivityRoomInfo.getVenueName());
							intent.putExtra("address",
									mActivityRoomInfo.getVenueAddress());
							intent.putExtra("Id", mActivityRoomInfo.getRoomId());
							intent.putExtra("information",
									mActivityRoomInfo.getRoomInformation());
							startActivityForResult(intent,
									AppConfigUtil.LOADING_LOGIN_BACK);
						}

					}

					// } else {
					// ToastUtil.showToast(MyApplication.BindPhoneShow);
					// }

				} else {
					ToastUtil.showToast("活动室没有可预订的时间。");
				}
				// } else {
				// intent = new Intent(mContext, MessageDialog.class);
				// FastBlur.getScreen((Activity) mContext);
				// intent.putExtra(DialogTypeUtil.DialogType,
				// DialogTypeUtil.MessageDialog.MSGTYPE_NOGROUP_SHOW);
				// startActivity(intent);
				// }
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				startActivity(intent);
			}

			break;
		case R.id.title_right:
			intent = new Intent(mContext, UserDialogActivity.class);
			FastBlur.getScreen((Activity) mContext);
			ShareInfo info = new ShareInfo();
			info.setTitle(mActivityRoomInfo.getRoomName());
			info.setImageUrl(mActivityRoomInfo.getRoomPicUrl());
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		case R.id.room_phone:
			intent = new Intent(mContext, MessageDialog.class);
			FastBlur.getScreen((Activity) mContext);
			intent.putExtra(DialogTypeUtil.DialogContent, RoomPhone.getText());
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			startActivity(intent);
			break;
		default:
			finish();
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.LOADING_LOGIN_BACK:
			setResult(AppConfigUtil.LOADING_LOGIN_BACK);
			// finish();

			break;

		default:
			break;
		}
	}

	/**
	 * 添加设备图片
	 * 
	 * @param html
	 * @return
	 */
	private CharSequence getTextImg(String html) {
		ImageGetter imgGetter = new ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				int id = Integer.parseInt(source);
				Drawable d = getResources().getDrawable(id);
				d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
				return d;
			}
		};
		return Html.fromHtml(html, imgGetter, null);

	}

	// /**
	// * 改变listitem的背景色
	// *
	// * @param view
	// */
	// private void itemBackChanged(View view) {
	// if (itemView == null) {
	// itemView = view;
	// }
	// mBgColor = R.color.text_color_003b;
	// itemView.setBackgroundResource(mBgColor);
	// mBgColor = R.color.text_color_26;
	// // 将上次点击的listitem的背景色设置成透明
	// view.setBackgroundResource(mBgColor);
	// // 设置当前点击的listitem的背景色
	// itemView = view;
	//
	// }
}
