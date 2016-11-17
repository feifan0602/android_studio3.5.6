package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ValidateUtil;
import com.sun3d.culturalShanghai.adapter.RoomReserveGridAdapter;
import com.sun3d.culturalShanghai.calender.CalendarActivity;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.MyTextView;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;
import com.sun3d.culturalShanghai.view.wheelview.ScreenInfo;
import com.sun3d.culturalShanghai.view.wheelview.WheelMain;
import com.sun3d.culturalShanghai.view.wheelview.WheelMain.OnWheelViewNumberChangedListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布活动
 * 
 * @author wenff
 * 
 */
public class EventReserveActivity extends CalendarActivity {
	private RelativeLayout reserve_integral;
	public static final int ONLINESEAT = 101;
	private RelativeLayout reserve_idcard_rl;
	private String code;
	private Context mContext;
	private RelativeLayout mTitle;
	private LinearLayout mCalender;
	private ScrollViewGridView mTimerGridview, mSeatGridview;
	private ScrollView mSrcollView;
	private boolean isCalender = true;
	private boolean isTime = true;
	private TextView mName;
	private TextView mAddress;
	private TextView mDate;
	private TextView mTime;
	private TextView mMoney;
	private TextView vote_num;
	private TextView add;
	private TextView reduce_tv;
	private TextView mEtNumbar;
	private EditText mEtPhone;
	private int mNumbar = 1;
	private String totalTimes = ""; // 接口时间
	private String seat = ""; // 接口座位
	private String showseat = ""; // 显示座位
	private EventInfo mEventInfo;
	private LoadingDialog mLoadingDialog;
	private String loadingText = "加载中";
	private RelativeLayout mReserveLayout;
	private TextView mReserve_seat;
	public static int[] startTime;
	public static int[] endTime;
	private RoomReserveGridAdapter mRoomReserveGridAdapter;
	private List<String> timelist = new ArrayList<String>();;
	private int oldindex = -1;
	private String chooseDate = "选择日期";
	private String chooseTime = "选择时间段";
	private String chooseNum = "选择人数";
	private Map<String, Times> dateMapList = new HashMap<String, Times>();
	private final String mPageName = "EventReserveActivity";
	private boolean isShowNumber = true;
	private Integer[] intList = null;
	private int index = -1;
	private String Strat_time, End_time;
	private int V_num = 1;
	private TextView reserve_integral_num;
	private Button send_code;
	private TextView reserve_total_money;
	private EditText reserve_code;
	private EditText reserve_idcard;
	private ImageView title_img;
	private EditText mUserName_et;
	private MyTextView limit_buys;
	private int ticknum;
	private RelativeLayout Low_integral;
	private TextView Low_integral_tv, Low_integral_num;
	private String sendcode;
	private TextView reserve_name, reserve_date_tv, reserve_time_tv,
			reserve_select_tv, reserve, integral, reserve_phone_text,
			reserve_code_text, reserve_name_text1, reserve_idcard_text,
			reserve_name_text2, ticket_tv;
	private ImageView right_arrow1, right_arrow;
	private RelativeLayout reserve_time_rl;
	private LinearLayout ticket_ll;

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

	public class Times {
		public List<String> timeStatuss;// 0.已过期 1.未过期
		public List<String> times;// 时间段

		@Override
		public String toString() {
			int i = 0;
			String str = "";
			for (String time : times) {
				str = str + time + "," + timeStatuss.get(i) + "-";
				i += 1;
			}
			return "Times [" + str + "]";
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_reserve);
		mContext = this;
		mLoadingDialog = new LoadingDialog(mContext);
		MyApplication.getInstance().addActivitys(this);
		MyApplication.getInstance().setmReserveHandler(mHandler);
		mEventInfo = (EventInfo) this.getIntent().getExtras()
				.getSerializable("EventInfo");
		if (null == mEventInfo) {
			finish();
		}

		initView();

	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// TODO Auto-generated method stub
		ticket_tv = (TextView) findViewById(R.id.ticket_tv);
		ticket_tv.setTypeface(MyApplication.GetTypeFace());
		ticket_ll = (LinearLayout) findViewById(R.id.ticket_ll);
		Low_integral = (RelativeLayout) findViewById(R.id.Low_integral);
		Low_integral_tv = (TextView) findViewById(R.id.Low_integral_tv);
		Low_integral_tv.setTypeface(MyApplication.GetTypeFace());
		Low_integral_num = (TextView) findViewById(R.id.Low_integral_num);
		right_arrow = (ImageView) findViewById(R.id.right_arrow);
		right_arrow1 = (ImageView) findViewById(R.id.right_arrow1);
		reserve_time_rl = (RelativeLayout) findViewById(R.id.reserve_time);
		reserve_time_rl.setOnClickListener(this);
		reserve_name = (TextView) findViewById(R.id.reserve_name);
		reserve_date_tv = (TextView) findViewById(R.id.reserve_date_tv);
		reserve_time_tv = (TextView) findViewById(R.id.reserve_time_tv);
		reserve_select_tv = (TextView) findViewById(R.id.reserve_select_tv);
		reserve = (TextView) findViewById(R.id.reserve);
		integral = (TextView) findViewById(R.id.integral);
		reserve_phone_text = (TextView) findViewById(R.id.reserve_phone_text);
		reserve_code_text = (TextView) findViewById(R.id.reserve_code_text);
		reserve_name_text1 = (TextView) findViewById(R.id.reserve_name_text);
		reserve_name_text2 = (TextView) findViewById(R.id.reserve_name_text1);
		reserve_idcard_text = (TextView) findViewById(R.id.reserve_idcard_text);
		reserve_name.setTypeface(MyApplication.GetTypeFace());
		reserve_date_tv.setTypeface(MyApplication.GetTypeFace());
		reserve_time_tv.setTypeface(MyApplication.GetTypeFace());
		reserve_select_tv.setTypeface(MyApplication.GetTypeFace());
		reserve.setTypeface(MyApplication.GetTypeFace());
		integral.setTypeface(MyApplication.GetTypeFace());
		reserve_phone_text.setTypeface(MyApplication.GetTypeFace());
		reserve_code_text.setTypeface(MyApplication.GetTypeFace());
		reserve_name_text1.setTypeface(MyApplication.GetTypeFace());
		reserve_name_text2.setTypeface(MyApplication.GetTypeFace());
		reserve_idcard_text.setTypeface(MyApplication.GetTypeFace());

		setTitle();
		limit_buys = (MyTextView) findViewById(R.id.limit_buys);
		reserve_code = (EditText) findViewById(R.id.reserve_code);
		mUserName_et = (EditText) findViewById(R.id.reserve_name_et);
		mUserName_et.setTypeface(MyApplication.GetTypeFace());
		reserve_idcard_rl = (RelativeLayout) findViewById(R.id.reserve_idcard_rl);
		title_img = (ImageView) findViewById(R.id.title_img);
		reserve_idcard = (EditText) findViewById(R.id.reserve_idcard);
		reserve_total_money = (TextView) findViewById(R.id.reserve_total_money);
		reserve_total_money.setTypeface(MyApplication.GetTypeFace());
		send_code = (Button) findViewById(R.id.send_code);
		send_code.setOnClickListener(this);
		send_code.setTypeface(MyApplication.GetTypeFace());
		reserve_integral_num = (TextView) findViewById(R.id.reserve_integral_num);
		mCalender = (LinearLayout) findViewById(R.id.reserve_calender);
		mTimerGridview = (ScrollViewGridView) findViewById(R.id.room_time);
		mSeatGridview = (ScrollViewGridView) findViewById(R.id.room_seat);
		mSrcollView = (ScrollView) findViewById(R.id.scrollView);
		mDate = (TextView) findViewById(R.id.reserve_date_text);
		mTime = (TextView) findViewById(R.id.reserve_time_text);
		mName = (TextView) findViewById(R.id.reserve_name);
		mMoney = (TextView) findViewById(R.id.reserve_money_num);
		vote_num = (TextView) findViewById(R.id.vote_num);
		reserve_integral = (RelativeLayout) findViewById(R.id.reserve_integral);
		mDate.setTypeface(MyApplication.GetTypeFace());
		mTime.setTypeface(MyApplication.GetTypeFace());
		mName.setTypeface(MyApplication.GetTypeFace());
		mMoney.setTypeface(MyApplication.GetTypeFace());
		vote_num.setTypeface(MyApplication.GetTypeFace());
		reserve_integral_num.setTypeface(MyApplication.GetTypeFace());

		if (mEventInfo.getCostCredit() == null
				|| mEventInfo.getCostCredit().length() == 0) {
			reserve_integral_num.setText("0");
			reserve_integral.setVisibility(View.GONE);
		} else {
			if (mEventInfo.getActivitySalesOnline().equals("N")) {
				// 自由入座
				reserve_integral_num.setText("0");
				reserve_integral.setVisibility(View.VISIBLE);
			} else {
				// 在线预订
				reserve_integral_num.setText("0");
				reserve_integral.setVisibility(View.VISIBLE);
			}
			reserve_integral_num.setText(mEventInfo.getCostCredit());
		}
		if (mEventInfo.getIdentityCard() == 0) {
			// 不需要身份证
			reserve_idcard_rl.setVisibility(View.GONE);
		} else {
			reserve_idcard_rl.setVisibility(View.VISIBLE);
		}
		reduce_tv = (TextView) findViewById(R.id.reduce_tv);
		add = (TextView) findViewById(R.id.add);
		if (mEventInfo.getActivitySalesOnline().equals("N")) {
			reduce_tv.setOnClickListener(this);
			add.setOnClickListener(this);
			vote_num.setText("1");
		} else {
			vote_num.setText("0");
			reduce_tv.setOnClickListener(this);
			add.setOnClickListener(this);
		}

		mAddress = (TextView) findViewById(R.id.reserve_location);
		mEtNumbar = (TextView) findViewById(R.id.reserve_numbar);
		mEtPhone = (EditText) findViewById(R.id.reserve_phone);
		mReserveLayout = (RelativeLayout) findViewById(R.id.reserve_select);
		mReserveLayout.setOnClickListener(this);
		mReserve_seat = (TextView) findViewById(R.id.reserve_seat);
		mAddress.setTypeface(MyApplication.GetTypeFace());
		mEtNumbar.setTypeface(MyApplication.GetTypeFace());
		mEtPhone.setTypeface(MyApplication.GetTypeFace());
		mReserve_seat.setTypeface(MyApplication.GetTypeFace());

		wheelView = (RelativeLayout) findViewById(R.id.wheel_number);
		findViewById(R.id.event_reserve).setOnClickListener(this);
		findViewById(R.id.reserve_explain).setOnClickListener(this);
		// oldFixTimesData();

		if (null != mEventInfo.getActivityNotice()
				&& mEventInfo.getActivityNotice().length() < 1) {
			findViewById(R.id.reserve_explain).setVisibility(View.GONE);
		}

		// setNumbar();
		fixTimesData();
		setData();
	}

	/**
	 * 时间段处理
	 */
	private void fixTimesData() {
		String[] strdate = mEventInfo.getActivityEventimes().split(",");
		String[] strstatus = mEventInfo.getStatus().split(",");
		Log.i("ceshi", "数据 查看添加" + mEventInfo.getActivityEventimes()
				+ "数据 查看 ==" + mEventInfo.getStatus());

		// 2016-7-5 08:00-09:00,数据 查看 ==1,

		if (null != strdate) {
			int i = 0;
			for (String date : strdate) {
				String[] strtime = date.split(" ");
				Log.i("ceshi", "数据===  " + strtime[0] + "  ");
				if (dateMapList.containsKey(strtime[0])) {
					Log.i("ceshi", "数据111===  " + strtime[0]);
					Times times = dateMapList.get(strtime[0]);
					times.times.add(strtime[1]);
					times.timeStatuss.add(strstatus[i]);
				} else {
					if (mEventInfo.getActivityStartTime().equals(
							mEventInfo.getEventEndTime())) {
						Times times = new Times();
						times.times = new ArrayList<String>();
						times.timeStatuss = new ArrayList<String>();
						// Log.i("ceshi", "strtime[1]==   "+strtime[1]);
						// /**
						// * 鬼后台 不知道 我们是把秒杀的字段 改成 2016-7-7 不是秒杀的字段 改成
						// 2016-07-07
						// */
						// int mouth =
						// Integer.valueOf(strtime[1].split("-")[1]);
						// int day =
						// Integer.valueOf(strtime[1].split("-")[2]);
						// String mouth_str;
						// String day_str;
						// if (mouth < 10) {
						// mouth_str = strtime[1].split("-")[1].replaceAll(
						// "0", "");
						// } else {
						// mouth_str = strtime[1].split("-")[1];
						// }
						// if (day < 10) {
						// day_str =
						// strtime[1].split("-")[2].replaceAll("0",
						// "");
						// } else {
						// day_str = strtime[1].split("-")[2];
						// }
						// String key_time = strtime[1].split("-")[0] + "-"
						// + mouth_str + "-" + day_str;

						times.times.add(strtime[1]);
						times.timeStatuss.add(strstatus[i]);

						dateMapList.put(strtime[0], times);
					} else {
						Times times = new Times();
						times.times = new ArrayList<String>();
						times.timeStatuss = new ArrayList<String>();

						times.times.add(strtime[1]);
						times.timeStatuss.add(strstatus[i]);
						dateMapList.put(strtime[0], times);
					}

				}
				i += 1;
			}
		}
	}

	/**
	 * 时间段处理(废弃)
	 */
	private void oldFixTimesData() {
		String[] str = mEventInfo.getTimeQuantum().trim().split(",");
		if (str != null && str.length > 0
				&& mEventInfo.getTimeQuantum().trim().length() > 0) {
			for (String s : str) {
				timelist.add(s);
			}
		} else {
			timelist.add("没有时间选择");
		}
	}

	/**
	 * 设置数据
	 */
	private void setData() {
		if (!mEventInfo.getTicketNumber().equals("")) {
			ticknum = Integer.valueOf(mEventInfo.getTicketCount());
			limit_buys.setText("此活动同一ID限购" + mEventInfo.getTicketNumber()
					+ "次,每次限购" + mEventInfo.getTicketCount() + "张");
		} else {
			ticknum = 5;
			limit_buys.setText("此活动同一ID限购5张票");
		}

		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlSmall(mEventInfo
								.getEventDetailsIconUrl()), title_img);
		// 总价要如何设置
		// reserve_total_money.setText(mEventInfo.get)
		mName.setText(mEventInfo.getEventName());
		mAddress.setText(mEventInfo.getEventAddress());
		if (mEventInfo.getEventPrice().equals("0")
				| mEventInfo.getEventPrice().length() == 0) {
			mMoney.setText("免费");
		} else {
			if (ValidateUtil.isNumeric(mEventInfo.getEventPrice())) {
				mMoney.setText(mEventInfo.getEventPrice() + "元");
			} else {
				mMoney.setText("收费");
			}
		}
		startTimeString = mEventInfo.getActivityStartTime();
		String[] start1 = mEventInfo.getActivityStartTime().split("-");
		String statttime = "";
		if (null != start1 && start1.length > 2) {
			statttime = start1[0] + "年" + start1[1] + "月" + start1[2] + "日";
			startTime = new int[] { Integer.parseInt(start1[0]),
					Integer.parseInt(start1[1]), Integer.parseInt(start1[2]) };
		}
		endTimeString = mEventInfo.getActivityStartTime();
		String[] end1 = mEventInfo.getEventEndTime().split("-");
		String eddtime = "";
		if (null != end1 && end1.length > 2) {
			eddtime = end1[1] + "月" + end1[2] + "日";
			endTime = new int[] { Integer.parseInt(end1[0]),
					Integer.parseInt(end1[1]), Integer.parseInt(end1[2]) };
		}

		Drawable mDrawable = getResources()
				.getDrawable(R.drawable.sh_icon_next);
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());

		Drawable drawableNull = getResources().getDrawable(
				R.drawable.sh_icon_null);
		drawableNull.setBounds(0, 0, drawableNull.getMinimumWidth(),
				drawableNull.getMinimumHeight());

		String date = statttime + "-" + eddtime;
		String time = mEventInfo.getActivityStartTime() + "至"
				+ mEventInfo.getEventEndTime();
		String datestr = null;
		if (!mEventInfo.getActivityStartTime().equals(
				mEventInfo.getEventEndTime())) {
			// 不是單天
			if (mEventInfo.getSingleEvent() == 1) {
				// 单场次
				Log.i("ceshi", "单场次=== ");
				mDate.setText(mEventInfo.getActivityStartTime() + "至"
						+ mEventInfo.getEventEndTime());
				mTime.setOnClickListener(null);
				reserve_time_rl.setOnClickListener(null);
				right_arrow.setVisibility(View.INVISIBLE);
				right_arrow1.setVisibility(View.INVISIBLE);
				datestr = mEventInfo.getEventEndTime();

			} else {
				// 多场次
				if (mEventInfo.getSpikeType() == 1) {
					// 这是秒杀 需要写死 时间点 多场次 多场次 秒杀
					Log.i("ceshi", "多场次=== " + MyApplication.spike_endTime);
					datestr = MyApplication.spike_endTime;
					mDate.setText(MyApplication.spike_endTime);
					mTime.setText(MyApplication.spike_endTimeHour);

					// right_arrow.setVisibility(View.INVISIBLE);
					findViewById(R.id.reserve_date).setOnClickListener(this);
				} else {
					// 这不是秒杀 不需要写死时间点
					Log.i("ceshi", "不是藐视00000");
					mDate.setText(chooseDate);
					findViewById(R.id.reserve_date).setOnClickListener(this);
					mTime.setOnClickListener(this);
				}

			}

			// mDate.setCompoundDrawables(null, null, mDrawable, null);

		} else {
			// 单天
			// 结束时间和开始时间不一样
			if (mEventInfo.getSpikeType() == 1) {
				// 这是秒杀 需要写死 时间点
				mDate.setText(MyApplication.spike_endTime);
				right_arrow.setVisibility(View.INVISIBLE);
				findViewById(R.id.reserve_date).setOnClickListener(null);
			} else {
				// 这不是秒杀 不需要写死时间点
				Log.i("ceshi", "不是藐视");
				mDate.setText(chooseDate);
				findViewById(R.id.reserve_date).setOnClickListener(this);
				mTime.setOnClickListener(this);
			}

		}
		Log.i("ceshi",
				"看看数据==" + datestr + "  dateMapList = "
						+ dateMapList.get(datestr));
		if (datestr != null && dateMapList.get(datestr) != null
				&& dateMapList.get(datestr).times != null
				&& dateMapList.get(datestr).times.size() <= 1) {
			mTime.setText(dateMapList.get(datestr).times.get(0));
			// mTime.setCompoundDrawables(null, null, drawableNull, null);
		} else {
			if (mEventInfo.getSingleEvent() == 1) {
				Log.i("ceshi", "这里到了吗");
				if (mEventInfo.getActivityStartTime().equals(
						mEventInfo.getEventEndTime())) {
					mDate.setText(mEventInfo.getEventEndTime());

				} else {
					mDate.setText(mEventInfo.getActivityStartTime() + "至"
							+ mEventInfo.getEventEndTime());
				}

				mTime.setText(mEventInfo.getTimeQuantum());
				mDate.setOnClickListener(null);
				// mTime.setCompoundDrawables(null, null, mDrawable, null);
				mTime.setOnClickListener(null);
				reserve_time_rl.setOnClickListener(null);
				right_arrow1.setVisibility(View.INVISIBLE);
				right_arrow.setVisibility(View.INVISIBLE);

			} else {
				// 单场次 秒杀 可以选择时间
				// 这里是当天的时间 来判断一下 变成2016-7-10
				int mouth = Integer.valueOf(mEventInfo.getActivityStartTime()
						.split("-")[1]);
				String mouth_str = null;
				int day = Integer.valueOf(mEventInfo.getActivityStartTime()
						.split("-")[2]);
				String day_str = null;
				if (mouth < 10) {
					// mouth_str =
					// mEventInfo.getActivityStartTime().split("-")[1]
					// .replaceAll("0", "");
					mouth_str = mEventInfo.getActivityStartTime().split("-")[1];
				} else {
					mouth_str = mEventInfo.getActivityStartTime().split("-")[1];
				}
				if (day < 10) {
					// day_str = mEventInfo.getActivityStartTime().split("-")[2]
					// .replaceAll("0", "");
					day_str = mEventInfo.getActivityStartTime().split("-")[2];
				} else {
					day_str = mEventInfo.getActivityStartTime().split("-")[2];
				}
				datestr = mEventInfo.getActivityStartTime().split("-")[0] + "-"
						+ mouth_str + "-" + day_str;
				// datestr = mEventInfo.getActivityStartTime().split("-")[1];
				if (mEventInfo.getSpikeType() == 1) {
					// 秒杀
					// mDate.setText(datestr);
				} else {
					mDate.setText(datestr);

				}

				// mDate.setCompoundDrawables(null, null, drawableNull, null);

			}

		}

		if (MyApplication.loginUserInfor.getUserMobileNo().length() > 0) {
			mEtPhone.setText(MyApplication.loginUserInfor.getUserMobileNo());
		} else {
			mEtPhone.setText(MyApplication.loginUserInfor.getUserTelephone());
		}
		if (MyApplication.loginUserInfor != null) {
			mUserName_et.setText(MyApplication.loginUserInfor.getUserName());
		}
		if (mEtPhone.getText().toString().length() < 5) {
			getData();
		}
		Log.i("http", MyApplication.loginUserInfor.toString());
		if (mEventInfo.getActivityIsReservation().equals("2")) {// 可预订
			mReserveLayout.setVisibility(View.VISIBLE);

			if (mEventInfo.getActivitySalesOnline().equals("N")) {// 不可再线选座，就是自由入座
				mReserveLayout.setVisibility(View.GONE);
				mReserve_seat.setText("自由入座");
				// mReserve_seat.setCompoundDrawables(null, null, drawableNull,
				// null);
				mEtNumbar.setText(chooseNum);
				mEtNumbar.setOnClickListener(this);
			} else {// 在线选座
				mNumbar = 1;
				// setNumbar();
				isShowNumber = false;
				mReserve_seat.setText("在线选座");
				mReserve_seat.setOnClickListener(this);
				// mEtNumbar.setCompoundDrawables(null, null, drawableNull,
				// null);
			}
		} else {// 不可预订
			mReserveLayout.setVisibility(View.GONE);
		}
		if (!mEventInfo.getCostCredit().equals("")
				|| mEventInfo.getCostCredit().equals("0")) {
			Low_integral_num.setText(mEventInfo.getLowestCredit());
		} else {
			Low_integral.setVisibility(View.GONE);
			Low_integral_num.setVisibility(View.GONE);
			Low_integral_tv.setVisibility(View.GONE);
		}
		if (!mEventInfo.getActivityNotice().equals("")) {
			ticket_ll.setVisibility(View.VISIBLE);
			ticket_ll.setOnClickListener(this);
			ticket_tv.setVisibility(View.VISIBLE);
		} else {
			ticket_ll.setVisibility(View.GONE);
			ticket_tv.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取用户信息
	 */
	private void getData() {
		mLoadingDialog.startDialog(loadingText);
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.GET_USERINFO,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						UserInfor mUserInfor = JsonUtil
								.getUserInforFromString(resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.loginUserInfor
										.getUserMobileNo().length() > 0) {
									mEtPhone.setText(mUserInfor
											.getUserMobileNo());
								} else {
									mEtPhone.setText(mUserInfor
											.getUserTelephone());
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

	/**
	 * 更新日期
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == HANDLER_SUPER) {
				if (mDate != null) {
					// mCalender.setVisibility(View.GONE);
					mDate.setText(currDate);
					mTime.setText(chooseTime);
					mEtNumbar.setText(chooseNum);
					setDate();
					isChange = false;
					initSeat("");
					// setNumbar();
					initTimer();
					Drawable drawable = null;
					if (timeStateAll()) {
						drawable = getResources().getDrawable(
								R.drawable.sh_icon_next);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						// mTime.setCompoundDrawables(null, null, drawable,
						// null);
						mTime.setClickable(true);
						mEtNumbar.setClickable(true);
					} else {
						drawable = getResources().getDrawable(
								R.drawable.sh_icon_null);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						mTime.setText("此时间段已无票");
						wheelView.setVisibility(View.GONE);
						mTime.setClickable(false);
						mEtNumbar.setClickable(false);
						// mTime.setCompoundDrawables(null, null, drawable,
						// null);
					}
				}

			}
		};
	};

	/**
	 * 2.5接口预定（废弃）
	 */
	private void sendReserveBook() {
		mLoadingDialog.startDialog(loadingText);
		Map<String, String> mParams = new HashMap<String, String>();
		mParams.put("userId", MyApplication.loginUserInfor.getUserId());
		mParams.put("activityId", mEventInfo.getEventId());
		mParams.put("bookCount", mEtNumbar.getText().toString());
		mParams.put("orderMobileNum", mEtPhone.getText().toString());
		int num = Integer.parseInt(mEtNumbar.getText().toString());
		// int sum_price = mEventInfo.getEventPrice() * num;
		mParams.put("orderPrice", "0");

		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyEvent.ACTIVITY_RESERVE_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								Intent intent = new Intent(mContext,
										MessageDialog.class);
								FastBlur.getScreen((Activity) mContext);
								intent.putExtra(
										DialogTypeUtil.DialogType,
										DialogTypeUtil.MessageDialog.MSGTYPE_SCHEDULE_ACTIVITY);
								startActivity(intent);
								finish();
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
	 * 设置标题
	 */
	private void setTitle() {
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setTypeface(MyApplication.GetTypeFace());
		title.setVisibility(View.VISIBLE);
		title.setText(mContext.getString(R.string.reserve_title));

	}

	private boolean isFrist = true;
	private boolean isChange = true;
	private RelativeLayout wheelView;
	public static String startTimeString;
	public static String endTimeString;
	private int intLenght = 0;

	/**
	 * 设置日期
	 */
	private void setDate() {
		if (isTime) {
			isTime = false;
		}
		if (isFrist) {
			isFrist = false;
		} else {
			// setTime();
		}

		MyApplication.isReserverTimeDate = true;
		Drawable mDrawable = null;

		if (isCalender) {
			mCalender.setVisibility(View.VISIBLE);
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_pull);
			if (!mDate.getText().toString().equals(chooseDate)) {
				Log.d("mDate", mDate.getText().toString());
				CalendarActivity.clickDateStrng = mDate.getText().toString();
			} else {
				CalendarActivity.clickDateStrng = null;
			}
			// 3.5.2 判断是否是秒杀 秒杀 2016-7-11 费秒杀 2016-07-11
			onCalendar(mCalender, mContext, mSrcollView, EVENT_RESERVE,
					startTime, endTime, mEventInfo.getSpikeType());
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
	 * 设置人数
	 */
	private void setNumbar() {
		String times = mDate.getText().toString() + mTime.getText().toString();
		index = -1;
		String[] eventTimes = mEventInfo.getActivityEventimes()
				.replace(" ", "").split(",");
		for (String str : eventTimes) {
			++index;
			if (times.replace(" ", "").equals(str)) {
				break;
			}
		}
		Log.i("tag00", index + "\n" + mEventInfo.getActivityEventimes() + "\n"
				+ times + "\n" + mEventInfo.getEventCounts());

		ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
		WheelMain mWheelMain = new WheelMain(wheelView);
		mWheelMain.screenheight = screenInfo.getWidth();
		if (intList != null)
			intList = null;
		if (null != mEventInfo.getEventCounts() && index != -1) {
			String[] content = mEventInfo.getEventCounts().split(",");
			try {
				int num = Integer.parseInt(content[index]);
				if (num > 5) {
					num = 5;
				}
				intList = new Integer[num];
				for (int i = 0; i < num; i++) {

					intList[i] = i + 1;
				}
			} catch (Exception e) {
				intList = new Integer[] {};
			}
		} else {
			Log.i("tag00", "" + index);
			intList = new Integer[] {};
		}
		intLenght = intList.length;
		Log.i("tag001", "" + intList.length);
		if (intList.length > 0) {
			mWheelMain.initStringPicker(intList, mSrcollView, mEtNumbar);
			mWheelMain
					.setmOnWheelViewNumberChangedListener(new OnWheelViewNumberChangedListener() {

						@Override
						public void onChange(int number) {
							// TODO Auto-generated method stub
							mNumbar = number;
							mEtNumbar.setText("" + mNumbar);
						}
					});
		} else {
			wheelView.setVisibility(View.GONE);
		}
		if (ValidateUtil.isNumeric(mEventInfo.getEventPrice())) {
			BigDecimal bd = new BigDecimal(mEventInfo.getEventPrice());
			// mMoney.setText(bd.intValue() * mNumbar + "元");
			if (bd.intValue() > 0) {
				mMoney.setText("收费");
			} else {
				mMoney.setText("免费");
			}
		} else {
			mMoney.setText("收费");
		}

	}

	/**
	 * 设置时间
	 */
	private void setTime() {
		timelist.clear();
		Log.d("String", mDate.getText().toString());

		if (dateMapList.containsKey(mDate.getText().toString())) {
			int i = 0;
			for (String str : dateMapList.get(mDate.getText().toString()).times) {
				timelist.add(str);
				Log.d("String", i + "-" + str);
				i += 1;
			}
			if (timelist.size() > 0) {
				mTime.setClickable(true);
			} else {
				mTime.setClickable(false);
			}
		} else {
			ToastUtil.showToast("请先" + chooseDate);
			return;
		}
		Drawable mDrawable = null;
		if (isTime) {
			mTimerGridview.setVisibility(View.VISIBLE);
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_pull);
			initTimer();

		} else {
			mDrawable = getResources().getDrawable(R.drawable.sh_icon_next);
			mTimerGridview.setVisibility(View.GONE);
		}
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());
		// mTime.setCompoundDrawables(null, null, mDrawable, null);
		isTime = !isTime;
		if (timelist.size() > 0
				&& mTime.getText().toString().indexOf("-") == -1 & isChange) {
			String timeContent = timelist.get(0);
			mTime.setText(timeContent);
		}
		isChange = true;
	}

	private String[] getTimeStatus() {
		Log.d("String2", mDate.getText().toString());
		if (dateMapList.containsKey(mDate.getText().toString())) {
			String[] status = new String[dateMapList.get(mDate.getText()
					.toString()).timeStatuss.size()];
			int i = 0;
			for (String sta : dateMapList.get(mDate.getText().toString()).timeStatuss) {
				status[i] = sta;
				Log.d("String", i + "-" + status[i]);
				i += 1;
			}
			return status;
		} else {
			return null;
		}
	}

	/**
	 * 时间控件初始化
	 */
	private void initTimer() {
		mRoomReserveGridAdapter = new RoomReserveGridAdapter(mContext,
				timelist, getTimeStatus());
		mRoomReserveGridAdapter.setinitIndex(oldindex);
		mTimerGridview.setAdapter(mRoomReserveGridAdapter);
		mTimerGridview.setSelector(R.drawable.shape_orge_rect);
		mTimerGridview.setPrentView(mSrcollView);
		// mRoomReserveGridAdapter.setinitIndex(0);
		mTimerGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (getTimeStatus()[arg2].equals("1")) {
					TextView v = (TextView) arg0.getChildAt(arg2);
					v.setBackgroundResource(R.drawable.shape_orge_rect);
					v.setTextColor(getResources().getColor(R.color.white_color));
					mTime.setText(v.getText().toString());
					if (oldindex != -1) {
						TextView oldview = (TextView) arg0.getChildAt(oldindex);
						oldview.setBackgroundResource(R.drawable.shape_white_rect);
						oldview.setTextColor(getResources().getColor(
								R.color.dialog_content_color));
					}
					oldindex = arg2;
					setNumbar();
					initSeat("");
				} else {
					ToastUtil.showToast("该时间段不可预订");
				}
			}
		});
	}

	/**
	 * 座位数据初始化
	 * 
	 * @param seat
	 */
	private void initSeat(String seat) {
		Log.i("tag", "seat :" + seat);
		if (seat != null & seat.length() > 0) {

			mNumbar = seat.split(",").length;
			// setNumbar();

			int CostCredit = 0;
			if (!mEventInfo.getCostCredit().equals("")) {
				CostCredit = Integer.valueOf(mEventInfo.getCostCredit());
			}
			reserve_integral_num.setText(mNumbar * CostCredit + "");
			vote_num.setText(mNumbar + "");
			mSeatGridview.setVisibility(View.VISIBLE);
			mRoomReserveGridAdapter = new RoomReserveGridAdapter(mContext,
					setSeat(seat), null);
			mRoomReserveGridAdapter.setinitIndex(-1);
			mSeatGridview.setAdapter(mRoomReserveGridAdapter);
			mSeatGridview.setSelector(R.drawable.shape_orge_rect);
			mSeatGridview.setPrentView(mSrcollView);
			mEtNumbar.setText(mNumbar + "");
		} else {
			mSeatGridview.setVisibility(View.GONE);
			totalTimes = "";
			Log.i("tag", "initSeat : 1");
			mEtNumbar.setText("1");
			mNumbar = 1;
			this.seat = "";
		}
	}

	/**
	 * 设置座位数据
	 * 
	 * @param seat
	 * @return
	 */
	private List<String> setSeat(String seat) {
		List<String> list = new ArrayList<String>();
		String[] sea = seat.split(",");
		for (int i = 0; i < sea.length; i++) {
			list.add(sea[i].replace("_", "排") + "座");
		}
		return list;
	}

	/**
	 * 提交订单
	 * */

	public void setOrder() {
		mLoadingDialog.startDialog(loadingText);
		Map<String, String> params = new HashMap<String, String>();

		if (mEventInfo.getActivitySalesOnline().equals("N")) {// 自由入座
			// params.put("bookCount", "" + mEtNumbar.getText().toString());
			params.put("bookCount", "" + vote_num.getText().toString());
		} else {
			params.put("bookCount", "" + this.seat.split(",").length);
			params.put("seatIds", this.seat);
			params.put("seatValues", this.showseat);
		}
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("orderPrice", "0");
		params.put("activityId", mEventInfo.getEventId());
		params.put("orderMobileNum", mEtPhone.getText().toString());
		if (mEventInfo.getSpikeType() == 1) {
			// 秒杀 ID
			// 这里还要判断是否是多场次 多场次的 id 并不是最小的id
			if (mEventInfo.getSingleEvent() != 1) {
				params.put("activityEventIds", arrangementEventlds());
			} else {
				params.put("activityEventIds", MyApplication.spike_event_id);
			}

		} else {
			params.put("activityEventIds", arrangementEventlds());
		}
		Log.i("ceshi", "kankan  ===  " + totalTimes);
		params.put("activityEventimes", totalTimes);

		// 0：不需要 1：需要
		// params.put("times", times);
		if (mEventInfo.getIdentityCard() == 0) {
			params.put("orderIdentityCard", "");
		} else {
			// 身份证号码
			params.put("orderIdentityCard", reserve_idcard.getText().toString());
		}

		// 姓名
		params.put("orderName", mName.getText().toString());
		// 积分
		params.put("costTotalCredit", mEventInfo.getCostCredit());

		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyEvent.ACTIVITY_RESERVE_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								String electronicTicketId = JsonUtil
										.getElectronicTicket(resultStr);
								Intent intent = new Intent(mContext,
										MessageDialog.class);
								FastBlur.getScreen((Activity) mContext);
								intent.putExtra(
										DialogTypeUtil.DialogType,
										DialogTypeUtil.MessageDialog.MSGTYPE_SCHEDULE_ACTIVITY);
								intent.putExtra("mName",
										MyApplication.loginUserInfor
												.getUserNickName());
								// intent.putExtra("mTime", totalTimes);
								intent.putExtra("mTime", mDate.getText()
										.toString()
										+ "  "
										+ mTime.getText().toString());

								intent.putExtra("mTitle", mName.getText()
										.toString());
								intent.putExtra("mNumber", mEtPhone.getText()
										.toString());
								// intent.putExtra("electronicTicketId",
								// electronicTicketId);
								// intent.putExtra("ActivitySalesOnline",
								// mEventInfo.getActivitySalesOnline());
								startActivity(intent);
								Intent intent2 = new Intent();
								if (index != -1) {
									intent2.putExtra("CountsInfo",
											number(index));
								} else {
									intent2.putExtra("CountsInfo", "");
								}

								setResult(111, intent2);
								finish();
							} else {
								if (JsonUtil.JsonMSG.equals("此活动需要秒杀，请关注“文化云”公众号进行订票")) {
									ToastUtil.showToast("该场次秒杀还未开始，敬请期待！");
								}else {
									ToastUtil.showToast(JsonUtil.JsonMSG);
								}
							}
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	private String number(int index) {
		String i = "";
		String[] info = mEventInfo.getEventCounts().split(",");
		int len = Integer.parseInt(mEtNumbar.getText().toString());
		info[index] = Integer.parseInt(info[index]) - len + "";
		for (String in : info) {
			i += in + ",";
		}
		Log.i("https", i);
		return i;
		// mEventInfo.setEventCounts(i);
	}

	/**
	 * 活动ID数据处理
	 * 
	 * @return
	 */
	public String arrangementEventlds() {
		String id = "";
		String[] screenings = mEventInfo.getActivityEventIds().split(",");
		String[] time = mEventInfo.getActivityEventimes().split(",");

		for (int i = 0; i < time.length; i++) {
			if (totalTimes.equals(time[i].toString())) {
				id = screenings[i].toString();
			}
		}

		return id;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int CostCredit = 0;
		if (!mEventInfo.getCostCredit().equals("")) {
			CostCredit = Integer.valueOf(mEventInfo.getCostCredit());
		}
		switch (arg0.getId()) {
		case R.id.ticket_ll:
			Intent intent1 = new Intent();
			intent1.setClass(EventReserveActivity.this,
					TicketContentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("content", mEventInfo.getActivityNotice()
					.toString());
			intent1.putExtras(bundle);
			EventReserveActivity.this.startActivity(intent1);

			break;
		case R.id.title_left:
			super.finish();
			finish();
			break;
		case R.id.send_code:
			// 发送验证码
			getCode();
			break;
		case R.id.reserve_date:
			isChange = false;
			setDate();
			break;
		case R.id.reduce_tv:

			if (mEventInfo.getActivitySalesOnline().equals("N")) {
				if (V_num <= 0) {
					V_num = 0;
				} else {
					V_num--;
				}
				reserve_integral_num.setText((V_num * CostCredit) + "");
				vote_num.setText(V_num + "");
			}

			break;
		case R.id.add:
			if (mEventInfo.getActivitySalesOnline().equals("N")) {
				if (V_num >= ticknum) {
					V_num = ticknum;
				} else {
					V_num++;
				}
				reserve_integral_num.setText((V_num * CostCredit) + "");
				vote_num.setText(V_num + "");
			}
			break;
		case R.id.event_reserve:
			if (submitSelection()) {
				// arrangementTime();
				if (mEventInfo.getIdentityCard() == 0) {

				} else {
					if (reserve_idcard.getText().toString().equals("")
							|| reserve_idcard.getText() == null
							|| reserve_idcard.getText().length() == 0) {
						ToastUtil.showToast("请输入身份证号码");
						return;
					}
				}

				if (reserve_code.getText().toString().equals(sendcode)) {
					// 单场次

					setOrder();
				} else {
					ToastUtil.showToast("验证码输入错误");
				}

			}
			// sendReserveBook();
			break;
		case R.id.reserve_time:
			Log.i("String1", "时间");
			setTime();
			break;
		case R.id.reserve_explain:
			// if (mEventInfo.getActivityInformation() == null
			// || mEventInfo.getActivityInformation().length() <= 0) {
			// ToastUtil.showToast("目前没有说明");
			// return;
			// }
			Intent intenttext = new Intent(mContext, ExplainTextActivity.class);
			intenttext.putExtra("type", ExplainTextActivity.event_explain);
			intenttext.putExtra("content", mEventInfo.getActivityNotice()
					.toString());
			// intenttext.putExtra("content",
			// mEventInfo.getActivityInformation());
			startActivity(intenttext);
			break;
		case R.id.reserve_select:

			if (mDate.getText().toString().equals(chooseDate)
					|| mTime.getText().toString().equals(chooseTime)
					|| timeState()) {
				ToastUtil.showToast("请选择可预订的日期与时间段");
				return;
			}
			if (mEventInfo.getSingleEvent() == 1) {
				totalTimes = mEventInfo.getEventEndTime() + " "
						+ mTime.getText().toString();

			} else {
				totalTimes = mDate.getText().toString() + " "
						+ mTime.getText().toString();
			}

			// Intent intent = new Intent(mContext, OnlinSeatActivity.class);
			Intent intent = new Intent(mContext, NewOnlinSeatActivity.class);
			intent.putExtra("activityId", mEventInfo.getEventId());
			intent.putExtra("Seat", this.seat);
			intent.putExtra("showSeat", this.showseat);
			intent.putExtra("activityEventimes", totalTimes);
			Log.i("TAG_g", mEventInfo.getEventId() + "---" + totalTimes + "---"
					+ this.seat);
			startActivityForResult(intent, ONLINESEAT);
			break;
		case R.id.reserve_numbar:
			setNumbar();
			Drawable mDrawable = null;

			if (mTime.getText().toString().indexOf("-") == -1) {
				ToastUtil.showToast("请选择日期与时间段");
				return;
			}

			Log.i("tag001", "intLenght : " + intLenght + "");

			if (isShowNumber && intLenght > 0) {
				mDrawable = getResources().getDrawable(R.drawable.sh_icon_pull);
				wheelView.setVisibility(View.VISIBLE);
			} else {
				mDrawable = getResources().getDrawable(R.drawable.sh_icon_next);
				wheelView.setVisibility(View.GONE);

			}
			mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
					mDrawable.getMinimumHeight());
			// mEtNumbar.setCompoundDrawables(null, null, mDrawable, null);
			isShowNumber = !isShowNumber;
			break;
		default:
			break;
		}
	}

	private void getCode() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put("userMobileNo", mEtPhone.getText().toString());

		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.WFF_SENDAUTHCODE,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是获取验证码" + resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								JSONObject jo = new JSONObject(resultStr);
								code = jo.optString("data1", "");
//								reserve_code.setText(code);
								sendcode = code;
							} catch (Exception e) {
								e.printStackTrace();
							}
							TimeCount timeCount = new TimeCount(60000, 1000,
									send_code);
							if (send_code.isClickable()) {
								timeCount.start();
							}
						}
					}
				});

	}

	/**
	 * 选择的内容提交
	 * 
	 * @return
	 */
	private boolean submitSelection() {
		StringBuffer sb = new StringBuffer();
		if (!ValidateUtil.isCellphone(mEtPhone.getText().toString(), sb)) {
			ToastUtil.showToast(sb.toString());
			return false;
		}
		if (mDate.getText().toString().equals(chooseDate)) {
			ToastUtil.showToast("请先" + chooseDate);
			return false;
		}
		if (mTime.getText().toString().equals(chooseTime)) {
			ToastUtil.showToast("请先" + chooseTime);
			return false;
		}
		/**
		 * 不知道 他哪里出问题了 bug 需要处理 从前面传过来的时候 就已经是空的
		 */
		// if (mEtNumbar.getText().toString() == ""
		// || mEtNumbar.getText().toString() == null) {
		// mEtNumbar.setText("1");
		// }
		// if (Integer.parseInt(mEtNumbar.getText().toString()) == 0) {
		// if (mEventInfo.getActivitySalesOnline().equals("N")) {
		// ToastUtil.showToast("请先选择人数！");
		// } else {
		// ToastUtil.showToast("请先选择座位！");
		// }
		// return false;
		// }
		/**
		 * 人数的选择 3.5.2
		 */
		if (vote_num.getText().toString() == ""
				|| vote_num.getText().toString() == null) {
			vote_num.setText("1");
		}
		if (Integer.parseInt(vote_num.getText().toString()) == 0) {
			if (mEventInfo.getActivitySalesOnline().equals("N")) {
				ToastUtil.showToast("请先选择人数！");
			} else {
				ToastUtil.showToast("请先选择座位！");
			}
			return false;
		}

		if (mEventInfo.getSingleEvent() == 1) {
			// 单场次
			if (mEventInfo.getSpikeType() == 1) {
				// 秒杀
				Log.i("ceshi", "秒杀=== ");
				totalTimes = mDate.getText().toString() + " "
						+ mTime.getText().toString();
			} else {
				Log.i("ceshi", "非秒杀=== ");
				totalTimes = mEventInfo.getEventEndTime() + " "
						+ mTime.getText().toString();
			}
			// totalTimes = mEventInfo.getEventEndTime() + " "
			// + mTime.getText().toString();
		} else {
			totalTimes = mDate.getText().toString() + " "
					+ mTime.getText().toString();
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ONLINESEAT:
			String seat = data.getStringExtra("seat");
			this.seat = seat;
			this.showseat = data.getStringExtra("showseat");
			initSeat(this.showseat);
			break;
		default:
			break;
		}
	}

	// 获取日期场次是否过期
	private boolean timeState() {
		boolean mStatu = false;
		int index = timelist.indexOf(mTime.getText().toString());
		if (index != -1) {
			mStatu = getTimeStatus()[index].equals("0");
		}
		return mStatu;
	}

	/**
	 * 该日期内是否有可选场次 注：只有在日期选完之后才可使用
	 * */
	private boolean timeStateAll() {
		boolean boo = false;
		if (getTimeStatus() == null) {
			ToastUtil.showToast("已过期");
		} else {
			for (int i = 0; i < getTimeStatus().length; i++) {
				Log.i("timeStateAll", getTimeStatus()[i]);
				if (getTimeStatus()[i].equals("1")) {
					boo = true;
				}
			}
		}

		return boo;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 计时
	 * 
	 * @author lingningkang
	 * 
	 */
	class TimeCount extends CountDownTimer {
		private Button mText;

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		public TimeCount(long millisInFuture, long countDownInterval,
				Button mText) {
			super(millisInFuture, countDownInterval);
			this.mText = mText;
		}

		public void onFinish() {
			mText.setText("重新发送");
			mText.setClickable(true);
		}

		public void onTick(long millisUntilFinished) {
			mText.setClickable(false);
			mText.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
