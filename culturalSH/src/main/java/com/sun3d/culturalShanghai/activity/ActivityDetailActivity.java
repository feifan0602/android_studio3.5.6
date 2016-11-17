package com.sun3d.culturalShanghai.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.CollectUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Activity;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Comment;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Detail;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Header;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Host;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Kills;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Special;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Video;
import com.sun3d.culturalShanghai.handler.ActivityDetail_Vote;
import com.sun3d.culturalShanghai.handler.ActivityDetail_webview;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.DeviceManager;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.view.CustomDigitalClock;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.ScrollScrollView;
import com.sun3d.culturalShanghai.view.ScrollScrollView.OnScrolledListenter;
import com.sun3d.culturalShanghai.windows.SelectPicPopupWindow;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 活动详情页面 根据服务器返回的数据来添加到LinearLayout里面去
 * 
 * @author yangyoutao
 */
public class ActivityDetailActivity extends Activity implements
		OnClickListener, OnScrolledListenter {
	private String TAG = "ActivityDetailActivity";
	private final String mPageName = "ActivityDetailActivity";
	private Context mContext;
	private EventInfo eventInfo;
	private String eventInfoId;
	/**
	 * 判断是否有活动单位 true 表示有 false 表示无
	 */
	public boolean Host_bool = false;
	public LinearLayout activity_Layout_ll;
	private ActivityDetail_Header mActivityDetail_Header;// 活动头部
	private ActivityDetail_Host mActivityHost;// 3.5 活动单位
	private ActivityDetail_Detail mActivityDetail_Detail;// 活动详情
	private ActivityDetail_Video mActivityDetail_Video;// 活动视频
	private ActivityDetail_webview mActivity_webView;// 网页 温馨提示
	private ActivityDetail_Vote mActivityDetail_Vote;// 活动投票
	private ActivityDetail_Special mActivityDetail_Special;// 实况直播
	private ActivityDetail_Kills mActivityDetail_Kill;// 秒杀活动
	private ActivityDetail_Comment mActivityDetail_Comment;// 活动评论
	private ActivityDetail_Activity mActivityDetail_Activity;// 其他的活动
	private LinearLayout MainContentLayout;
	private LoadingHandler mLoadingHandler;
	private LinearLayout mReservation;
	public final int ONLINESEAT = 111;
	private TextView activity_comment_tv;
	private String activityType = "2";// 活动评论type
	private ImageView activity_comment_zan_iv;
	private ImageView share;
	private ImageView collect;
	private boolean isCollect;
	private SelectPicPopupWindow menuWindow;
	private ScrollScrollView refreshscrollView;
	private TextView profile;
	private boolean isWant = false;
	private ImageView mTopView;
	private CustomDigitalClock timeClock;
	private TextView condition_tv, condition_tv1;
	private ImageButton OnResh_ib;
	private LinearLayout condition_ll;
	private LinearLayout Kill_ll;
	public static int total_availableCount;
	private RelativeLayout mTitle;
	private TextView activity_left_tv, activity_right_tv;
	public ImageView right_line_img, left_line_img, movBarImageView;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
		if (mActivityDetail_Video != null) {
			mActivityDetail_Video.onPause();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mActivityDetail_Video != null) {
			mActivityDetail_Video.onDestroy();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mActivityDetail_Video != null
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& mActivityDetail_Video.getVideoConfiguration() == Configuration.ORIENTATION_LANDSCAPE) {// 全屏的时候监听
			return mActivityDetail_Video.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mActivityDetail_Video != null) {
			mActivityDetail_Video.onStop();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		if (mActivityDetail_Video != null) {
			mActivityDetail_Video.onResume();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_detail);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		init();
	}

	@Override
	public void onScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {

		// TODO Auto-generated method stub
		int head_height;
		if (eventInfo.getSpikeType() == 1) {
			head_height = mActivityDetail_Header.getContent().getHeight()
					+ mActivityDetail_Kill.getContent().getHeight();
			mActivityDetail_Detail.setHeadHeight(head_height);
		} else {
			head_height = mActivityDetail_Header.getContent().getHeight();
		}

		int detail_height = mActivityDetail_Detail.getContent().getHeight();
		int wenxintishi_height = MyApplication.getInstance().getWindowHeight() / 6;
		int total_height = wenxintishi_height + detail_height;
		if (scrollY > head_height) {
			if (Host_bool) {
				activity_Layout_ll.setVisibility(View.VISIBLE);
				mTitle.setVisibility(View.VISIBLE);
			}
		} else {
			activity_Layout_ll.setVisibility(View.GONE);
		}
		if (Host_bool) {
			mActivityDetail_Detail.setLayout(Host_bool);
			if (scrollY > total_height) {
				clearLineColor();
				right_line_img.setBackgroundResource(R.color.text_color_72);
				mActivityDetail_Detail.right_line_img
						.setBackgroundResource(R.color.text_color_72);
			} else {
				clearLineColor();
				left_line_img.setBackgroundResource(R.color.text_color_72);
				mActivityDetail_Detail.left_line_img
						.setBackgroundResource(R.color.text_color_72);
			}
		} else {
			mActivityDetail_Detail.setLayout(Host_bool);
		}
	}

	/***
	 * 初始化呗，还能干啥。
	 */
	private void init() {
		eventInfoId = this.getIntent().getStringExtra("eventId");
		activity_left_tv = (TextView) findViewById(R.id.activity_detail_tv);
		activity_right_tv = (TextView) findViewById(R.id.activity_tv);
		activity_left_tv.setTypeface(MyApplication.GetTypeFace());
		activity_right_tv.setTypeface(MyApplication.GetTypeFace());
		activity_left_tv.setOnClickListener(this);
		activity_right_tv.setOnClickListener(this);
		left_line_img = (ImageView) findViewById(R.id.left_line_img);
		right_line_img = (ImageView) findViewById(R.id.right_line_img);
		Kill_ll = (LinearLayout) findViewById(R.id.Kill_ll);
		timeClock = (CustomDigitalClock) findViewById(R.id.venue_clock);
		timeClock.setActivity(this);
		OnResh_ib = (ImageButton) findViewById(R.id.OnResh_ib);
		OnResh_ib.setOnClickListener(this);
		activity_Layout_ll = (LinearLayout) findViewById(R.id.activity_ll);
		condition_ll = (LinearLayout) findViewById(R.id.condition_ll);
		condition_tv = (TextView) findViewById(R.id.condition_tv);
		condition_tv1 = (TextView) findViewById(R.id.condition_tv1);
		condition_tv.setTypeface(MyApplication.GetTypeFace());
		condition_tv1.setTypeface(MyApplication.GetTypeFace());
		refreshscrollView = (ScrollScrollView) findViewById(R.id.refreshscrollView);
		refreshscrollView.setOnScrolledListenter(this);
		mTopView = (ImageView) findViewById(R.id.venue_top);
		mTopView.setOnClickListener(this);
		((ScrollScrollView) refreshscrollView).setOnScrolledListenter(this);

		refreshscrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_MOVE:
					int scrollY = view.getScrollY();
					int height = view.getHeight();
					// if ((height + scrollY) > WindowsUtil
					// .getwindowsHight(mContext)) {
					// mTopView.setVisibility(View.VISIBLE);
					// } else {
					// mTopView.setVisibility(View.GONE);
					// }
					break;
				default:
					break;
				}
				return false;
			}
		});
		profile = (TextView) findViewById(R.id.venue_profile);
		profile.setTypeface(MyApplication.GetTypeFace());
		share = (ImageView) findViewById(R.id.share);
		collect = (ImageView) findViewById(R.id.collect);
		activity_comment_tv = (TextView) findViewById(R.id.activity_comment_tv);
		activity_comment_tv.setTypeface(MyApplication.GetTypeFace());
		activity_comment_zan_iv = (ImageView) findViewById(R.id.activity_comment_zan_iv);

		MainContentLayout = (LinearLayout) findViewById(R.id.contentlayout);

		mReservation = (LinearLayout) findViewById(R.id.event_reserve);
		mActivityHost = new ActivityDetail_Host(mContext, this);
		mActivity_webView = new ActivityDetail_webview(mContext, this);
		mActivityDetail_Header = new ActivityDetail_Header(mContext, this);

		mActivityDetail_Detail = new ActivityDetail_Detail(mContext,
				refreshscrollView, this);

		mActivityDetail_Video = new ActivityDetail_Video(mContext);
		// mActivityDetail_Video.addVideoData();
		mActivityDetail_Vote = new ActivityDetail_Vote(mContext, eventInfoId);
		mActivityDetail_Special = new ActivityDetail_Special(mContext,
				eventInfoId);
		mActivityDetail_Comment = new ActivityDetail_Comment(this, mContext,
				eventInfoId);
		mActivityDetail_Activity = new ActivityDetail_Activity(this,
				refreshscrollView, this);
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mReservation.setOnClickListener(this);
		activity_comment_tv.setOnClickListener(this);
		activity_comment_zan_iv.setOnClickListener(this);
		share.setOnClickListener(this);
		collect.setOnClickListener(this);

		movBarImageView = (ImageView) findViewById(R.id.line_img_bottom);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) movBarImageView
				.getLayoutParams();
		params.width = DeviceManager.getResolutionWidth() / 2 - 50;
		params.leftMargin = 25;
		movBarImageView.setLayoutParams(params);

		params = (RelativeLayout.LayoutParams) mActivityDetail_Detail.line_img_bottom
				.getLayoutParams();
		params.width = DeviceManager.getResolutionWidth() / 2 - 50;
		params.leftMargin = 25;
		mActivityDetail_Detail.line_img_bottom.setLayoutParams(params);

		setTitle();
		addViews();
		getData();

	}

	private void addViews() {
		MainContentLayout.addView(mActivityDetail_Header.getContent());
		MainContentLayout.setFocusable(true);
		MainContentLayout.setFocusableInTouchMode(true);
		MainContentLayout.requestFocus();
		MainContentLayout.addView(mActivityDetail_Video.getContent());
		// MainContentLayout.addView(mActivityHost.getContent());
		// MainContentLayout.addView(mActivityDetail_Comment.getContent());

	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		ImageButton mTitleRight = (ImageButton) mTitle
				.findViewById(R.id.title_right);
		mTitleRight.setOnClickListener(this);
		mTitleRight.setImageResource(R.drawable.sh_icon_title_share);
		TextView mTitle_tv = (TextView) mTitle.findViewById(R.id.title_content);
		mTitle_tv.setText("活动详情");
		mTitle_tv.setTypeface(MyApplication.GetTypeFace());
		mTitle_tv.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		mLoadingHandler.startLoading();
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.EventUrl.ACTIVITY_ID, eventInfoId);
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyApplication.total_num(HttpUrlList.EventUrl.WFF_HTTP_EVENT_URL,
				"cmsActivityAppDetail", eventInfoId);
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WFF_HTTP_EVENT_URL,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
									.getJsonStatus(resultStr)) {
								eventInfo = JsonUtil.getEventInfo(resultStr);
								MyApplication.activityispast = eventInfo
										.getActivityIsPast();
								if (eventInfo.getEventIsCollect().equals("1")) {
									collect.setImageResource(R.drawable.sh_icon_collect_after);
								}
								if (eventInfo.isWant()) {
									isWant = eventInfo.isWant();
									activity_comment_zan_iv
											.setImageResource(R.drawable.wff_zan_after);
								}
								if (eventInfo.getVideoPalyList() != null
										&& eventInfo.getVideoPalyList().size() > 0) {
									mActivityDetail_Video
											.addVideoData(eventInfo
													.getVideoPalyList());
									// mActivityDetail_Video.initPlatyUrlList(eventInfo.getVideoPalyList());
								} else {
									mActivityDetail_Video.getContent()
											.setVisibility(View.GONE);
								}
								setData();
							} else {
								mLoadingHandler.showErrorText(JsonUtil.JsonMSG);
							}

						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
						mLoadingHandler.stopLoading();
					}
				});
	}

	private void setData() {
		mActivityDetail_Header.setHeaderData(eventInfo);
		mActivityDetail_Activity.setData(eventInfo);
		if (eventInfo != null) {
			if (!eventInfo.getDeductionCredit().equals("")
					&& !eventInfo.getDeductionCredit().equals("0")) {
				condition_tv1.setVisibility(View.VISIBLE);
				condition_tv1.setText(MyApplication
						.getRedTextColor("此活动热门,预订后未到场将会被扣除"
								+ eventInfo.getDeductionCredit() + "积分"));
			} else {
				condition_tv1.setVisibility(View.GONE);
			}

			if (!eventInfo.getLowestCredit().equals("")
					&& !eventInfo.getCostCredit().equals("")
					&& !eventInfo.getLowestCredit().equals("0")
					&& !eventInfo.getCostCredit().equals("0")) {
				condition_tv.setVisibility(View.VISIBLE);
				condition_tv.setText(MyApplication.getRedTextColor("预订需要达到"
						+ eventInfo.getLowestCredit() + "积分" + ",且每张需抵扣"
						+ eventInfo.getCostCredit() + "积分"));

			} else if (!eventInfo.getLowestCredit().equals("")
					&& eventInfo.getCostCredit().equals("")
					&& !eventInfo.getLowestCredit().equals("0")
					&& eventInfo.getCostCredit().equals("0")) {
				condition_tv.setVisibility(View.VISIBLE);
				condition_tv.setText(MyApplication.getRedTextColor("预订需要达到"
						+ eventInfo.getLowestCredit() + "积分"));
				// condition_tv1.setText(MyApplication
				// .getRedTextColor("此活动热门,预订后未到场将会被扣除"
				// + eventInfo.getCostCredit() + "积分"));
			} else if (eventInfo.getLowestCredit().equals("")
					&& !eventInfo.getCostCredit().equals("")
					&& eventInfo.getLowestCredit().equals("0")
					&& !eventInfo.getCostCredit().equals("0")) {
				condition_tv.setVisibility(View.VISIBLE);
				condition_tv.setText(MyApplication.getRedTextColor("每张需抵扣"
						+ eventInfo.getCostCredit() + "积分"));

			} else {
				condition_tv.setText("");
				condition_tv.setVisibility(View.GONE);
				condition_ll.setVisibility(View.GONE);
			}

		}

		// 0：非秒杀 1:秒杀
		if (eventInfo.getSpikeType() == 1) {
			mActivityDetail_Kill = new ActivityDetail_Kills(this, mContext,
					eventInfoId);
			mActivityDetail_Kill.setdate(eventInfo);
			MainContentLayout.addView(mActivityDetail_Kill.getContent());
		} else {
			String profile_num = eventInfo.getActivityIsReservation();
			String status = eventInfo.getStatus();
			String activityAbleCount = eventInfo.getActivityAbleCount();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			str = str.replaceAll("-", "");
			long now_time = Long.valueOf(str);
			String over_time = eventInfo.getEventEndTime();
			over_time = over_time.replaceAll("-", "");
			String[] arrays = over_time.split(" ");
			Log.i(TAG, "看看详情数据 ==  " + arrays.length);
			if (arrays.length > 1) {
				over_time = over_time.split(" ")[0];
			}
			long end_time = Long.valueOf(over_time);
			String time1 = MyApplication.getStrTime(str);
			if (profile_num.equals("2")) {
				// 可以预约
				if (now_time > end_time) {
					// 余票数
					profile.setText("已结束");
					profile.setOnClickListener(null);
					profile.setBackgroundColor(getResources().getColor(
							R.color.text_color_cc));

				} else if (status != null) {
					String[] status_arr = status.split(",");
					for (int i = 0; i < status_arr.length; i++) {
						if (status_arr[i].equals("1")) {
							profile.setText("立即预约");
							profile.setFocusable(true);
							profile.setBackgroundColor(getResources().getColor(
									R.color.text_color_72));
							profile.setOnClickListener(this);
							break;
						} else {
							profile.setText("已订完");
							profile.setOnClickListener(null);
							profile.setBackgroundColor(getResources().getColor(
									R.color.text_color_cc));
						}

					}
				} else if (activityAbleCount.equals("0")) {
					profile.setText("已订完");
					profile.setOnClickListener(null);
					profile.setBackgroundColor(getResources().getColor(
							R.color.text_color_cc));
				}
			} else {
				// 直接前往
				profile.setText("直接前往");
				profile.setOnClickListener(null);
				profile.setBackgroundColor(getResources().getColor(
						R.color.text_color_cc));
			}
		}

		// String str = eventInfo.getEventContent();
		if (eventInfo.getActivityMemo() == null
				|| eventInfo.getActivityMemo() == ""
				|| eventInfo.getActivityMemo().equals("")) {

		} else {
			MainContentLayout.addView(mActivityDetail_Detail.getContent());
			mActivityDetail_Detail.setDetailsData(eventInfo.getActivityMemo(),
					true);

		}
		MainContentLayout.addView(mActivity_webView.getContent());
		mActivity_webView.setWebData(eventInfo);
		if (!"".equals(eventInfo.getActivityPerformed())
				|| !"".equals(eventInfo.getActivitySpeaker())
				|| !"".equals(eventInfo.getActivityHost())
				|| !"".equals(eventInfo.getActivityOrganizer())
				|| !"".equals(eventInfo.getActivityCoorganizer())) {
			// 这里表示有活动单位
			Host_bool = true;
			MainContentLayout.addView(mActivityHost.getContent());
			mActivityHost.setHostData(eventInfo);
			mActivityDetail_Detail.setLayout(Host_bool);
		} else {

			Host_bool = false;
			mActivityDetail_Detail.setLayout(Host_bool);
		}
		if (eventInfo.getAssnId() != null
				&& eventInfo.getAssnId().length() != 0) {
			MainContentLayout.addView(mActivityDetail_Activity.getContent());
		}

		MainContentLayout.addView(mActivityDetail_Comment.getContent());
		if (eventInfo.getSpikeType() == 1) {

		} else {
			mActivityDetail_Detail.setHeadHeight(mActivityDetail_Header
					.getContent().getHeight());
		}
		mActivityDetail_Detail.setWebHeight(mActivity_webView.getContent()
				.getHeight());

		// if (eventInfo.getActivityFunName() != null
		// && !"".equals(eventInfo.getActivityFunName())) {

		// if (eventInfo.getActivityFunName().indexOf("我要投票") != -1) {
		// MainContentLayout.addView(mActivityDetail_Vote.getContent());
		// }
		// if (eventInfo.getActivityFunName().indexOf("实况直击") != -1) {
		// MainContentLayout.addView(mActivityDetail_Special.getContent());
		// }

		// if (eventInfo.getActivityFunName().indexOf("活动点评") != -1) {
		// MainContentLayout.addView(mActivityDetail_Comment.getContent());
		// mActivityDetail_Comment.isWant(eventInfo.isWant());
		// }
		// }
		// MainContentLayout.addView(mActivityDetail_Comment.getContent());
		// 预约

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_detail, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case HttpCode.ServerCode_Comment.CODE_BACK:
			mActivityDetail_Comment.getCommtentData();
			break;
		case ONLINESEAT:
			String info = data.getExtras().getString("CountsInfo");
			Log.i("https", info);
			if (null != info) {
				eventInfo.setEventCounts(info);
			}
			break;
		case AppConfigUtil.LOADING_LOGIN_BACK:

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {

		// TODO Auto-generated method stub
		if (!ButtonUtil.isDelayClick()) {
			return;
		}
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.venue_top:
			finish();
			break;
		/**
		 * 这是评论
		 */
		case R.id.activity_comment_tv:
			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			if (MyApplication.UserIsLogin) {
				intent = new Intent(mContext, WriteCommentActivity.class);
				intent.putExtra("Id", eventInfoId);
				intent.putExtra("type", activityType);
				this.startActivityForResult(intent,
						HttpCode.ServerCode_Comment.CODE_BACK);
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}
			break;
		/**
		 * 这是点赞
		 */
		case R.id.title_right:

			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			intent = new Intent(mContext, UserDialogActivity.class);
			FastBlur.getScreen((Activity) mContext);
			ShareInfo info = new ShareInfo();
			info.setTitle(eventInfo.getEventName());
			info.setImageUrl(eventInfo.getEventDetailsIconUrl());
			info.setContentUrl(eventInfo.getShareUrl());
			info.setContent(ViewUtil.getTextFromHtml(eventInfo
					.getActivityMemo().toString()));
			// info.setContent("ee");
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		/**
		 * 这个是预约活动的
		 */
		case R.id.event_reserve:
			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			if (MyApplication.UserIsLogin) {
				intent = new Intent(mContext, EventReserveActivity.class);
				intent.putExtra("EventInfo", eventInfo);

				Log.i("tag3", eventInfo.toString());
				startActivityForResult(intent, ONLINESEAT);
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				startActivity(intent);
			}
			break;
		/**
		 * 这是点赞
		 */
		case R.id.activity_comment_zan_iv:
			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			if (MyApplication.UserIsLogin) {
				if (isWant) {
					quxiaoJoin();
				} else {
					goToJoin();
				}
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}
			break;
		/**
		 * 这是点击预约 3.5版本
		 */
		case R.id.venue_profile:
			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			if (MyApplication.UserIsLogin) {
				Log.i(TAG, "登陆了==  " + eventInfo.getIntegralStatus());
				// 0：可以预定；1：未达最低积分；2：未达抵扣积分
				if (eventInfo.getIntegralStatus().equals("0")) {
					// intent = new Intent(mContext,
					// EventReserveActivity.class);
					Log.i(TAG, eventInfo.toString());
					String url = HttpUrlList.IP
							+ "/wechatActivity/preActivityOrder.do?"
							+ "activityId=" + eventInfoId;

					// HttpUrlList.IP
					// + "/wechatActivity/preActivityOrder.do"
					// + "?activityId=" + eventInfoId;
					Log.i(TAG, "看看数据 ==  " + url);
					MyApplication.selectWeb(mContext, url);

					// intent.putExtra("EventInfo", eventInfo);
					// MyApplication.StartTime =
					// eventInfo.getActivityStartTime();
					// MyApplication.EndTime = eventInfo.getEventEndTime();
					// startActivityForResult(intent, ONLINESEAT);
				} else if (eventInfo.getIntegralStatus().equals("1")) {
					ToastUtil.showToast("您的积分未达到最低要求！");
				} else if (eventInfo.getIntegralStatus().equals("2")) {
					ToastUtil.showToast("您的积分不够抵扣！");
				} else {
					String url = HttpUrlList.IP
							+ "/wechatActivity/preActivityOrder.do?"
							+ "activityId=" + eventInfoId;
					Log.i(TAG, "看看数据 ==  " + url);
					MyApplication.selectWeb(mContext, url);
				}

			} else {
				Log.i(TAG, "没有登录==  ");
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				startActivity(intent);
			}
			break;
		/**
		 * 这是分享
		 */
		case R.id.share:
			// menuWindow = new
			// SelectPicPopupWindow(ActivityDetailActivity.this,
			// itemsOnClick, AppConfigUtil.INTENT_SHAREINFO);
			// menuWindow.showAtLocation(
			// ActivityDetailActivity.this.findViewById(R.id.share),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			intent = new Intent(mContext, UserDialogActivity.class);
			FastBlur.getScreen((Activity) mContext);
			ShareInfo info1 = new ShareInfo();
			info1.setTitle(eventInfo.getEventName());
			info1.setImageUrl(eventInfo.getEventDetailsIconUrl());
			MyApplication.shareLink = eventInfo.getShareUrl();
			info1.setContentUrl(eventInfo.getShareUrl());
			info1.setContent(ViewUtil.getTextFromHtml(eventInfo
					.getActivityMemo().toString()));
			// info.setContent("ee");
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info1);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		case R.id.collect:
			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			if (MyApplication.UserIsLogin) {
				onCollect();
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}
			break;
		case R.id.OnResh_ib:
			if (MyApplication.MyloginUserInfor.getUserIsDisable() == 2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			/**
			 * 这里要刷新状态
			 */
			// getData();
			RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(1000);
			rotateAnimation.setAnimationListener(new MyAnimationListener());
			OnResh_ib.startAnimation(rotateAnimation);
			break;
		// 活动详情
		case R.id.activity_detail_tv:
			// clearLineColor();
			// left_line_img.setBackgroundResource(R.color.text_color_72);
			// mActivityDetail_Detail.left_line_img
			// .setBackgroundResource(R.color.text_color_72);

			// left_line_img.setVisibility(View.VISIBLE);
			// right_line_img.setVisibility(View.GONE);
			//
			activity_Layout_ll.setVisibility(View.GONE);
			moveBottomImageLine(R.id.activity_detail_tv);
			if (eventInfo.getSpikeType() == 1) {
				refreshscrollView.scrollTo(0, mActivityDetail_Header
						.getContent().getHeight()
						+ mActivityDetail_Kill.getContent().getHeight());
			} else {
				refreshscrollView.scrollTo(0, mActivityDetail_Header
						.getContent().getHeight());
			}

			break;
		// 活动单位
		case R.id.activity_tv:
			// clearLineColor();
			// right_line_img.setBackgroundResource(R.color.text_color_72);
			// mActivityDetail_Detail.right_line_img
			// .setBackgroundResource(R.color.text_color_72);
			// left_line_img.setVisibility(View.GONE);
			// right_line_img.setVisibility(View.VISIBLE);
			activity_Layout_ll.setVisibility(View.VISIBLE);
			moveBottomImageLine(R.id.activity_tv);
			if (eventInfo.getSpikeType() == 1) {
				refreshscrollView.scrollTo(0, mActivityDetail_Header
						.getContent().getHeight()
						+ mActivityDetail_Detail.getContent().getHeight()
						+ mActivity_webView.getContent().getHeight()
						- 50
						+ mActivityDetail_Kill.getContent().getHeight());
				Log.i(TAG, "秒杀的 高度=== "
						+ mActivityDetail_Kill.getContent().getHeight());
			} else {
				refreshscrollView.scrollTo(0, mActivityDetail_Header
						.getContent().getHeight()
						+ mActivityDetail_Detail.getContent().getHeight()
						+ mActivity_webView.getContent().getHeight() - 50);
			}

			break;
		}
	}

	private void moveBottomImageLine(int currentSelectViewId) {// 先根据marginLeft判断是否已经是始终状态
		movBarImageView.clearAnimation();
		Log.i("fishbird", "step 1");
		final boolean isMoveToLeft;
		final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) movBarImageView
				.getLayoutParams();

		int left = params.leftMargin;
		int to = DeviceManager.getResolutionWidth() / 2;
		if (params.leftMargin < (DeviceManager.getResolutionWidth() / 2)) {
			if (currentSelectViewId == R.id.activity_detail_tv) {// 已经是选中状态
				return;
			}

			isMoveToLeft = false;
		} else {
			if (currentSelectViewId != R.id.activity_detail_tv) {// 已经是选中状态
				return;
			}
			isMoveToLeft = true;
		}

		if (isMoveToLeft) {
			params.leftMargin = 25;
		} else {
			params.leftMargin = DeviceManager.getResolutionWidth() / 2 + 25;
		}
		movBarImageView.setLayoutParams(params);
		// Log.i("fishbird", "step 2");
		// int offset = (DeviceManager.getResolutionWidth() / 2)
		// * (isMoveToLeft ? -1 : 1);
		// TranslateAnimation animation1 = new TranslateAnimation(0, offset, 0,
		// 0);
		// Log.i(TAG, offset+"----");
		// animation1.setDuration(500);
		// animation1.setAnimationListener(new AnimationListener() {
		//
		//
		// @Override
		// public void onAnimationStart(Animation animation)
		// {
		// // TODO Auto-generated method stub
		// Log.i("fishbird", "start");
		// }
		//
		//
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// if (isMoveToLeft) {
		// params.leftMargin = 25;
		// } else {
		// params.leftMargin = DeviceManager.getResolutionWidth() / 2 + 25;
		// }
		// movBarImageView.setLayoutParams(params);
		//
		// Log.i("fishbird", "end");
		//
		//
		// }
		// });
		// movBarImageView.startAnimation(animation1);
	}

	private void clearLineColor() {
		mActivityDetail_Detail.left_line_img
				.setBackgroundResource(R.color.text_color_cc);
		mActivityDetail_Detail.right_line_img
				.setBackgroundResource(R.color.text_color_cc);
		left_line_img.setBackgroundResource(R.color.text_color_cc);
		right_line_img.setBackgroundResource(R.color.text_color_cc);
	}

	private class MyAnimationListener implements AnimationListener {

		public void onAnimationEnd(Animation animation) {

			onResh();

			// TODO Auto-generated method stub
		}

		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		}

		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		}

	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				break;
			case R.id.btn_pick_photo:
				break;
			default:
				break;
			}

		}

	};

	/**
	 * 收藏
	 */
	private void onCollect() {

		if (isCollect) {
			cancelCollect();
		} else {
			addCollect();
		}

	}

	/**
	 * 添加收藏
	 */
	private void addCollect() {
		CollectUtil.addActivity(mContext, eventInfo.getEventId(),
				new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOk) {
						// TODO Auto-generated method stub
						if (isOk) {
							EventListActivity.setListCollction(1);
							ToastUtil.showToast("收藏成功");
							collect.setImageResource(R.drawable.sh_icon_collect_after);
							eventInfo.setEventIsCollect("1");
							isCollect = !isCollect;
						} else {
							ToastUtil.showToast("收藏失败");
						}
					}
				});
	}

	/**
	 * 取消收藏
	 */
	private void cancelCollect() {
		CollectUtil.cancelActivity(mContext, eventInfo.getEventId(),
				new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOk) {
						// TODO Auto-generated method stub
						if (isOk) {
							EventListActivity.setListCollction(0);
							ToastUtil.showToast("取消收藏");
							collect.setImageResource(R.drawable.wff_collection);
							eventInfo.setEventIsCollect("0");
							isCollect = !isCollect;
						} else {
							ToastUtil.showToast("操作失败");
						}
					}
				});
	}

	public void setHeaderData(EventInfo eventInfo) {
		this.eventInfo = eventInfo;

		// 1表示已经收藏，0表示未收藏
		if (eventInfo.getEventIsCollect() != null
				&& "1".equals(eventInfo.getEventIsCollect())) {
			collect.setImageResource(R.drawable.sh_icon_collect_after);
			isCollect = true;
		} else {
			collect.setImageResource(R.drawable.sh_icon_collect_befor);
			isCollect = false;
		}
	}

	private void quxiaoJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventInfoId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		//
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTNOT_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == 0) {
								activity_comment_zan_iv
										.setImageResource(R.drawable.wff_zan);
								// mIsWant.setText("点赞");
								isWant = !isWant;
								// if (listUsers != null) {
								// listUsers.clear();
								// }
								getJoinUsers();
							}
						}
					}
				});
	}

	/**
	 * 我想报名
	 */
	private void goToJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventInfoId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTTO_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							// if (code ==
							// HttpCode.serverCode.DATA_Success_CODE) {
							ToastUtil.showToast("点赞成功");
							// mIsWant.setText("取消");
							activity_comment_zan_iv
									.setImageResource(R.drawable.wff_zan_after);
							isWant = !isWant;
							// if (listUsers != null) {
							// listUsers.clear();
							// }
							getJoinUsers();
							// } else {
							// quxiaoJoin();
							// }

						} else {
							ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	protected void getJoinUsers() {
		mActivityDetail_Comment.getUserData();
	}

	public void setStatus() {
		Log.i("ceshi", "倒计时开始  " + MyApplication.total_availableCount + "  "
				+ MyApplication.currentCount + "  " + MyApplication.spike_Time);
		/**
		 * 3.5.2秒杀进入
		 */
		// 0：非秒杀 1:秒杀
		if (MyApplication.activityispast == 1) {
			profile.setText("已结束");
			// OnResh_ib.setVisibility(View.VISIBLE);
			// OnResh_ib.setOnClickListener(this);
			profile.setOnClickListener(null);
			profile.setBackgroundColor(getResources().getColor(
					R.color.text_color_cc));
		} else {
			// 票数的总数
			if (MyApplication.total_availableCount < 0) {
				// 所有的倒计时订票 余票
				// 没票了
				profile.setText("已订完");
				profile.setOnClickListener(null);
				profile.setBackgroundColor(getResources().getColor(
						R.color.text_color_cc));
			} else {
				if (MyApplication.currentCount > 0) {
					// 秒杀的票数并没有弄完
					timeClock.setVisibility(View.GONE);
					profile.setVisibility(View.VISIBLE);
					Kill_ll.setVisibility(View.VISIBLE);
					profile.setText("秒杀");
					profile.setOnClickListener(this);
					profile.setTextColor(getResources().getColor(
							R.color.text_color_ff));
					profile.setBackgroundColor(getResources().getColor(
							R.color.text_color_c0));
				} else {

					if (MyApplication.spike_Time > 0
							&& MyApplication.spike_Time < 86400) {
						// 开始倒计时了
						profile.setVisibility(View.GONE);
						Kill_ll.setVisibility(View.GONE);
						timeClock.setVisibility(View.VISIBLE);
						Log.i("ceshi",
								"时间戳00===  " + System.currentTimeMillis()
										+ MyApplication.spike_Time * 1000);
						timeClock.setEndTime(System.currentTimeMillis()
								+ MyApplication.spike_Time * 1000);
						timeClock.start();

					} else {
						profile.setText("未开始");
						// OnResh_ib.setVisibility(View.VISIBLE);
						// OnResh_ib.setOnClickListener(this);
						profile.setOnClickListener(null);
						profile.setBackgroundColor(getResources().getColor(
								R.color.text_color_cc));

					}
				}
			}
		}
	}

	public void onResh() {
		// Intent intent2 = new Intent(ActivityDetailActivity.this,
		// ActivityDetailActivity.class);
		// intent2.putExtra("eventId", eventInfoId);
		// MyApplication.currentCount = -1;
		// MyApplication.spike_Time = -1;
		// MyApplication.total_availableCount = -1;
		// ActivityDetailActivity.this.startActivity(intent2);
		// finish();
		mActivityDetail_Kill.initdate();
	};

	public void onResh60() {
		Intent intent2 = new Intent(ActivityDetailActivity.this,
				ActivityDetailActivity.class);
		intent2.putExtra("eventId", eventInfoId);
		ActivityDetailActivity.this.startActivity(intent2);
		finish();
	};

}
