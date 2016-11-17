package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ValidateUtil;
import com.sun3d.culturalShanghai.adapter.EventPopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.RoomEventWindowAdapter;
import com.sun3d.culturalShanghai.adapter.RoomReserveGridAdapter;
import com.sun3d.culturalShanghai.calender.CalendarActivity;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.RoomBookHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;
import com.sun3d.culturalShanghai.object.EventPopuwindowInfor;
import com.sun3d.culturalShanghai.object.RoomDetailTimeSlotInfor;
import com.sun3d.culturalShanghai.object.RoomEventInfo;
import com.sun3d.culturalShanghai.object.TeamUserInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;
import com.sun3d.culturalShanghai.view.wheelview.ScreenInfo;
import com.sun3d.culturalShanghai.view.wheelview.WheelMain;
import com.umeng.analytics.MobclickAgent;

public class EventRoomActivity extends CalendarActivity {
	private static EventRoomActivity mEventRoomActivity;
	private Context mContext;
	private RelativeLayout mTitle;
	private RelativeLayout mCalender;
	private RelativeLayout mGroup;
	private ScrollView mSrcollView;
	private boolean isCalender = true;
	private boolean isTime = true;
	private boolean isGroup = true;
	private TextView mDate;
	private TextView mtime;
	private TextView roomName;
	private TextView venue_name;
	private TextView venue_address;
	private TextView resere_group;
	private EditText resereAdmin;
	private EditText reserePhone;
	private ScrollViewGridView mScrollViewGridView;
	private RoomReserveGridAdapter mRoomReserveGridAdapter;
	private List<String> timelist = new ArrayList<String>();;
	private int oldindex = -1;
	private String[] strDate;
	private String[] strGroup;
	private List<TeamUserInfo> teamList;
	private String roomID;
	private String roomNamestr;
	private String venueNamestr;
	private String venueAddressstr;
	private LoadingHandler mLoadingHandler;
	private String teamStr = "没有团体选择！";
	private Map<String, TimeInfo> mapList = new HashMap<String, TimeInfo>();
	private String information;
	private int initindex = -1;
	private String timeChooseShow = "选择时间段";
	private TextView reserve_price_next;
	private final String mPageName = "EventRoomActivity";
	private Button select_bt;
	private View mView;
	private PopupWindow pw;
	private EditText reserve_user_person;
	private EditText reserve_user_et;
	private List<RoomEventInfo> mList;
	private ImageView title_img;
	private TextView reserve_phone_text, reserve_text, textView1,
			reserve_time_tv, reserve_price_tv, reserve_user_tv,
			reserve_name_text1, reserve_use_text;

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

	/**
	 * 时间字段
	 */
	public class TimeInfo {
		public String[] timeStatuss;// 0.已过期 1.未过期
		public String[] timeBookIds;// 时间段的ID
		public String[] times;// 时间段

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_room);
		mContext = this;
		mEventRoomActivity = this;
		MyApplication.getInstance().addActivitys(this);
		MyApplication.getInstance().setmRoomHandler(mHandler);

		initView();
		getRoomListData();
		getTeamData();
		addData();
	}

	private void getRoomListData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("roomId", roomID);
		params.put("bookId", MyApplication.bookid);
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.ActivityRoomUrl.ROOM_BOOK,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是活动室的数据00000===="
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							mList = JsonUtil.getRoomEvent(resultStr);
							handler.sendEmptyMessage(1);

						}
					}
				});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mList.size() != 0) {
					reserve_price_next.setText(mList.get(0).getPrice());
					Log.i("ceshi", "mList==  "
							+ mList.get(0).getOrderName().length()
							+ "  ordername==  " + mList.get(0).getOrderName());
					if (mList.get(0).getOrderName() == ""
							|| mList.get(0).getOrderName().equals("null")) {
						resereAdmin.setText("");
					} else {
						resereAdmin.setText(mList.get(0).getOrderName());
					}

					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(mList.get(0).getRoomPicUrl(),
									title_img);
					if (mList.get(0).getTuserId() == null) {
						select_bt.setVisibility(View.INVISIBLE);
					}
				} else {
					reserve_price_next.setText("数据空了吗");
				}

				break;

			default:
				break;
			}
		};
	};

	/**
	 * 对象
	 * 
	 * @return
	 */
	public static EventRoomActivity getIntance() {
		return mEventRoomActivity;
	}

	/**
	 * 控件初始化
	 */
	private void initView() {
		// TODO Auto-generated method stub
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		reserve_phone_text = (TextView) findViewById(R.id.reserve_phone_text);
		textView1 = (TextView) findViewById(R.id.textView1);
		reserve_time_tv = (TextView) findViewById(R.id.reserve_time_tv);
		reserve_price_tv = (TextView) findViewById(R.id.reserve_price_tv);
		reserve_user_tv = (TextView) findViewById(R.id.reserve_user_tv);
		reserve_name_text1 = (TextView) findViewById(R.id.reserve_name_text1);
		reserve_use_text = (TextView) findViewById(R.id.reserve_use_text);
		reserve_phone_text.setTypeface(MyApplication.GetTypeFace());
		textView1.setTypeface(MyApplication.GetTypeFace());
		reserve_time_tv.setTypeface(MyApplication.GetTypeFace());
		reserve_price_tv.setTypeface(MyApplication.GetTypeFace());
		reserve_user_tv.setTypeface(MyApplication.GetTypeFace());
		reserve_name_text1.setTypeface(MyApplication.GetTypeFace());
		reserve_use_text.setTypeface(MyApplication.GetTypeFace());
		title_img = (ImageView) findViewById(R.id.title_img);
		select_bt = (Button) findViewById(R.id.select);
		select_bt.setTypeface(MyApplication.GetTypeFace());
		select_bt.setOnClickListener(this);
		reserve_price_next = (TextView) findViewById(R.id.reserve_price_next);
		reserve_price_next.setTypeface(MyApplication.GetTypeFace());
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		roomID = this.getIntent().getStringExtra("Id");
		roomNamestr = this.getIntent().getStringExtra("roomname");
		venueNamestr = this.getIntent().getStringExtra("venuename");
		venueAddressstr = this.getIntent().getStringExtra("address");
		information = this.getIntent().getStringExtra("information");
		setTitle();
		roomName = (TextView) findViewById(R.id.reserve_roomname);
		venue_name = (TextView) findViewById(R.id.reserve_name);
		venue_address = (TextView) findViewById(R.id.reserve_location);
		resereAdmin = (EditText) findViewById(R.id.reserve_room_name);
		reserePhone = (EditText) findViewById(R.id.reserve_phone);
		reserve_user_person = (EditText) findViewById(R.id.reserve_user_person);
		reserve_user_et = (EditText) findViewById(R.id.reserve_user_et);
		reserve_user_et.setTypeface(MyApplication.GetTypeFace());
		reserve_user_person.setTypeface(MyApplication.GetTypeFace());
		roomName.setTypeface(MyApplication.GetTypeFace());
		venue_name.setTypeface(MyApplication.GetTypeFace());
		venue_address.setTypeface(MyApplication.GetTypeFace());
		resereAdmin.setTypeface(MyApplication.GetTypeFace());
		reserePhone.setTypeface(MyApplication.GetTypeFace());

		mScrollViewGridView = (ScrollViewGridView) findViewById(R.id.room_time);
		findViewById(R.id.reserve_date).setOnClickListener(this);
		findViewById(R.id.event_reserve).setOnClickListener(this);
		mCalender = (RelativeLayout) findViewById(R.id.reserve_calender);
		mGroup = (RelativeLayout) findViewById(R.id.reserve_group);
		mSrcollView = (ScrollView) findViewById(R.id.scrollView);
		mDate = (TextView) findViewById(R.id.reserve_date_text);
		mtime = (TextView) findViewById(R.id.reserve_time_next);
		resere_group = (TextView) findViewById(R.id.reserve_room_admin);
		mDate.setTypeface(MyApplication.GetTypeFace());
		mtime.setTypeface(MyApplication.GetTypeFace());
		resere_group.setTypeface(MyApplication.GetTypeFace());

		mtime.setOnClickListener(this);
		mRoomReserveGridAdapter = new RoomReserveGridAdapter(mContext,
				timelist, null);
		mRoomReserveGridAdapter.setinitIndex(initindex);
		mScrollViewGridView.setAdapter(mRoomReserveGridAdapter);
		mScrollViewGridView.setSelector(R.drawable.shape_orge_rect);
		mScrollViewGridView.setPrentView(mSrcollView);
		mScrollViewGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (mapList.get(mDate.getText().toString()) != null
						&& mapList.get(mDate.getText().toString()).timeStatuss[arg2]
								.equals("1")) {
					initindex = arg2;
					TextView v = (TextView) arg0.getChildAt(arg2);
					v.setBackgroundResource(R.drawable.shape_orge_rect);
					v.setTextColor(getResources().getColor(R.color.white_color));
					mRoomReserveGridAdapter.setinitIndex(arg2);

					mtime.setText(v.getText().toString());
					if (oldindex != -1) {
						TextView oldview = (TextView) arg0.getChildAt(oldindex);
						oldview.setBackgroundResource(R.drawable.shape_white_rect);
						oldview.setTextColor(getResources().getColor(
								R.color.dialog_content_color));
					}
					oldindex = arg2;
				} else {
					ToastUtil.showToast("时间段已过期");
				}
			}
		});
		findViewById(R.id.reserve_explain).setOnClickListener(this);
	}

	/**
	 * 添加数据
	 */
	private void addData() {
		reserePhone.setText(MyApplication.loginUserInfor.getUserMobileNo());
		// resereAdmin.setText("");
		roomName.setText(roomNamestr);
		venue_name.setText(venueNamestr);
		venue_address.setText(venueAddressstr);
		addDate();
	}

	/**
	 * 日期添加
	 */
	public void addDate() {
		if (ActivityRoomDateilsActivity.timelist != null) {
			strDate = new String[ActivityRoomDateilsActivity.timelist.size()];
			int i = 0;
			for (RoomDetailTimeSlotInfor rdts : ActivityRoomDateilsActivity.timelist) {
				strDate[i] = rdts.getDate();
				TimeInfo newTimeInfo = new TimeInfo();
				newTimeInfo.times = rdts.getTimeslot().split(",");
				newTimeInfo.timeStatuss = rdts.getStatus().split(",");
				newTimeInfo.timeBookIds = rdts.getBookId().split(",");
				mapList.put(rdts.getDate(), newTimeInfo);
				i += 1;
			}
		}

		if (!MyApplication.curDate.equals("")) {
			mDate.setText(MyApplication.curDate);
		} else {
			if (strDate[0] != null) {
				mDate.setText(strDate[0]);
			}
		}
		if (!MyApplication.openPeriod.equals("")) {
			mtime.setText(MyApplication.openPeriod);
		} else {
			mtime.setText(timeChooseShow);
		}

		for (String rdts : mapList.get(strDate[0]).times) {
			timelist.add(rdts);
		}
		mRoomReserveGridAdapter.setData(timelist,
				mapList.get(mDate.getText().toString()).timeStatuss);
	}

	/**
	 * 团队信息初始化
	 */
	private void setInitGroup() {
		Drawable mDrawable = null;
		if (isGroup) {
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_pull);
			mGroup.setVisibility(View.VISIBLE);
			setGroup();
		} else {
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_next);
			mGroup.setVisibility(View.GONE);
		}
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());
		// resere_group.setCompoundDrawables(null, null, mDrawable, null);
		isGroup = !isGroup;
	}

	/**
	 * 设置时间段
	 * 
	 * @param nowday
	 */
	public void setTimesolf(String nowday) {
		Drawable mDrawable = null;
		if (isTime) {
			initindex = -1;
			isCalender = !isCalender;
			Drawable mDrawable1 = getResources().getDrawable(
					R.drawable.sh_icon_next);
			mCalender.setVisibility(View.GONE);
			mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(),
					mDrawable1.getMinimumHeight());
			// mDate.setCompoundDrawables(null, null, mDrawable1, null);
			mScrollViewGridView.setVisibility(View.VISIBLE);
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_pull);
		} else {
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_next);
			mScrollViewGridView.setVisibility(View.GONE);
		}
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());
		// mtime.setCompoundDrawables(null, null, mDrawable, null);
		isTime = !isTime;
		timelist.clear();
		for (String rdts : mapList.get(nowday).times) {
			timelist.add(rdts);
		}
		mRoomReserveGridAdapter.setinitIndex(initindex);
		mRoomReserveGridAdapter.setData(timelist,
				mapList.get(mDate.getText().toString()).timeStatuss);

	}

	/**
	 * 更新日期
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == HANDLER_SUPER) {
				mDate.setText(currDate);
			}
		};
	};

	/**
	 * 设置标题
	 */
	private void setTitle() {
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setTypeface(MyApplication.GetTypeFace());
		title.setVisibility(View.VISIBLE);
		title.setText("活动室预订");
	}

	/**
	 * 设置日期
	 */
	private void setDate() {

		Drawable mDrawable = null;
		if (isCalender) {
			isTime = !isTime;
			Drawable mDrawable1 = getResources().getDrawable(
					R.drawable.sh_icon_next);
			mScrollViewGridView.setVisibility(View.GONE);
			mDrawable1.setBounds(0, 0, mDrawable1.getMinimumWidth(),
					mDrawable1.getMinimumHeight());
			// mtime.setCompoundDrawables(null, null, mDrawable1, null);

			mCalender.setVisibility(View.VISIBLE);
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_pull);
			setTime();
		} else {
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_next);
			mCalender.setVisibility(View.GONE);
		}
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());
		// mDate.setCompoundDrawables(null, null, mDrawable, null);
		isCalender = !isCalender;
	}

	/**
	 * 设置时间
	 */
	private void setTime() {
		ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
		WheelMain wheelMain = new WheelMain(mCalender, mSrcollView);
		wheelMain.screenheight = screenInfo.getWidth();
		wheelMain.initStringPicker(strDate, mDate, mtime, timeChooseShow);
	}

	/**
	 * 设置团队信息
	 */
	private void setGroup() {
		ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
		WheelMain wheelMain = new WheelMain(mGroup, mSrcollView);
		wheelMain.screenheight = screenInfo.getWidth();
		wheelMain.initStringPicker(strGroup, resere_group);
	}

	/**
	 * 返回团队信息
	 */
	private void getTeamData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.ActivityRoomUrl.ROOM_TEAMLIST, params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							teamList = JsonUtil
									.getAtivityRoomTeamList(resultStr);
							setTeatmData();
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 设置团队信息
	 */
	private void setTeatmData() {
		if (null != teamList && teamList.size() > 0) {
			strGroup = new String[teamList.size()];
			int i = 0;
			for (TeamUserInfo team : teamList) {
				strGroup[i] = team.getTeamName();
				i += 1;
			}
			resere_group.setText(strGroup[0]);
			resere_group.setOnClickListener(this);
		} else {
			// resere_group.setCompoundDrawables(null, null, null, null);
			resere_group.setText(teamStr);
			resere_group.setTextColor(getResources().getColor(
					R.color.event_text_color));
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.select:
			if (mView == null) {
				mView = View
						.inflate(this, R.layout.event_room_popuwindow, null);
			}

			ListView listView = (ListView) mView.findViewById(R.id.listView);
			RoomEventWindowAdapter hpwa = new RoomEventWindowAdapter(mList,
					this);
			listView.setAdapter(hpwa);
			int height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.6);
			if (pw == null) {
				pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 2,
						height);
				pw.setFocusable(false);
				pw.setOutsideTouchable(true);

			}
			int[] location = new int[2];
			select_bt.getLocationOnScreen(location);
			if (pw.isShowing()) {
				pw.dismiss();
				select_bt.setText("选择");
			} else {
				pw.showAtLocation(select_bt, Gravity.NO_GRAVITY, location[0]
						- pw.getWidth() - 10, location[1]);
				select_bt.setText("取消");
			}
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View arg1,
						int arg2, long arg3) {
					RoomEventInfo rei = (RoomEventInfo) parent
							.getItemAtPosition(arg2);
					if (pw.isShowing()) {
						pw.dismiss();
						reserve_user_person.setText(rei.getTuserName());
						RoomBookHandler.RoomBookInfo.tuserId = rei.getTuserId();
						select_bt.setText("选择");
					}

				}
			});
			break;
		case R.id.title_left:
			super.finish();
			finish();
			break;
		case R.id.reserve_date:
			// setDate();
			break;
		case R.id.reserve_room_admin:
			setInitGroup();
			break;
		case R.id.reserve_time_next:
			// setTimesolf(mDate.getText().toString());
			break;
		case R.id.reserve_explain:
			// if (information == null || information.length() <= 0) {
			// return;
			// }
			Intent intenttext = new Intent(mContext, ExplainTextActivity.class);
			intenttext.putExtra("type", ExplainTextActivity.venue_explain);
			intenttext.putExtra("content", "");
			// intenttext.putExtra("content", information);
			startActivity(intenttext);
			break;
		case R.id.event_reserve:
			if (resereAdmin.getText().toString().length() > 0) {
				StringBuffer sb = new StringBuffer();
				if (!ValidateUtil.isCellphone(reserePhone.getText().toString(),
						sb)) {
					ToastUtil.showToast(sb.toString());
					return;
				}
				Log.i("ceshi", reserve_user_et.getText().toString());
				if (reserve_user_et.getText().toString().length() == 0
						|| reserve_user_et.getText().equals("")) {
					ToastUtil.showToast("请填写预订用途");
					return;
				}
				if (reserve_user_person.getText().toString().length() == 0
						|| reserve_user_person.getText().equals("")) {

				}
				// if (resere_group.getText().toString().trim().length() > 0
				// && !resere_group.getText().toString().equals(teamStr)) {
				if (!mtime.getText().toString().equals(timeChooseShow)) {
					fomatData();
					RoomBookHandler.onRoomBook(mContext);
				} else {
					ToastUtil.showToast("请先选择预定的时间段");
				}
				// } else {
				// ToastUtil.showToast("您还没有管理的团队哦，不能订票。");
				// }
			} else {
				ToastUtil.showToast("预订人不能为空!");
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 预定数据整理
	 */
	private void fomatData() {
		String teamUserId = "";
		if (null != teamList && teamList.size() > 0) {
			for (TeamUserInfo mTeamUserInfo : teamList) {
				if (resere_group.getText().toString()
						.equals(mTeamUserInfo.getTeamName())) {
					teamUserId = mTeamUserInfo.getTeamId();
				}
			}
		}
		for (int i = 0; i < mapList.get(mDate.getText().toString()).times.length; i++) {
			if (mtime.getText().toString()
					.equals(mapList.get(mDate.getText().toString()).times[i])) {

				RoomBookHandler.RoomBookInfo.bookId = mapList.get(mDate
						.getText().toString()).timeBookIds[i];
				Log.d("bookId", RoomBookHandler.RoomBookInfo.bookId);
			}

		}
		if (RoomBookHandler.RoomBookInfo.bookId == null
				|| RoomBookHandler.RoomBookInfo.bookId.length() <= 0) {
			ToastUtil.showToast("bookId获取失败");

			return;

		}
		RoomBookHandler.RoomBookInfo.reserve_user_et = reserve_user_et
				.getText().toString();
		RoomBookHandler.RoomBookInfo.reserve_user_person = reserve_user_person
				.getText().toString();
		RoomBookHandler.RoomBookInfo.openPeriod = mtime.getText().toString();
		RoomBookHandler.RoomBookInfo.orderMobileNum = reserePhone.getText()
				.toString();
		RoomBookHandler.RoomBookInfo.orderName = resereAdmin.getText()
				.toString();
		RoomBookHandler.RoomBookInfo.orderRoomDate = mDate.getText().toString();
		RoomBookHandler.RoomBookInfo.roomId = roomID;
		RoomBookHandler.RoomBookInfo.roomName = roomNamestr;
		RoomBookHandler.RoomBookInfo.teamUserId = teamUserId;
		RoomBookHandler.RoomBookInfo.teamUserName = resere_group.getText()
				.toString();
		RoomBookHandler.RoomBookInfo.venueAddress = venueAddressstr;
		RoomBookHandler.RoomBookInfo.venueName = venueNamestr;

	}

	/**
	 * 跳转下一个界面
	 */
	public void gotoNext() {
		Intent intent = new Intent(mContext, MyRoomStutasActivity.class);
		intent.putExtra("type", "venue");
		startActivity(intent);
		setResult(AppConfigUtil.LOADING_LOGIN_BACK);
		finish();
		// Intent intent = new Intent(mContext, MessageDialog.class);
		// intent.putExtra(DialogTypeUtil.DialogType,
		// DialogTypeUtil.MessageDialog.MSGTYPE_SCHEDULE_VENUE);
		// startActivityForResult(intent, AppConfigUtil.LOADING_LOGIN_BACK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.LOADING_LOGIN_BACK:
			setResult(AppConfigUtil.LOADING_LOGIN_BACK);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
