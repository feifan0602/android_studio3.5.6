package com.sun3d.culturalShanghai.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.CollectUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ValidateUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.adapter.BannerAdapter;
import com.sun3d.culturalShanghai.adapter.CommetnListAdapter;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.Activity_JoinHandler;
import com.sun3d.culturalShanghai.handler.LabelHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.CommentInfor;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;
import com.sun3d.culturalShanghai.video.MyVideoView;
import com.sun3d.culturalShanghai.video.VideoInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.ScrollScrollView;
import com.sun3d.culturalShanghai.view.ScrollScrollView.OnScrolledListenter;
import com.sun3d.culturalShanghai.view.ScrollViewListView;
import com.umeng.analytics.MobclickAgent;

/**
 * 有播放的详情
 * 
 * @author wenff
 * 
 */
public class EventDetailsActivity extends Activity implements OnClickListener,
		RefreshListenter, OnScrolledListenter {
	private Context mContext;
	private ImageView mCollect;
	private boolean isCollect;
	// private TextView mCollectBig;
	private EventInfo eventInfo;
	private TextView mName;
	private TextView mLoaction;
	private TextView mPhone;
	private TextView mTime;
	private TextView mmoney;
	private String eventId = null;
	private WebView detailShow;
	private String activityType = "2";// 活动评论type
	private ImageView mmoney_iv;
	private ImageButton detailMore;
	private LoadingHandler mLoadingHandler;
	private RelativeLayout mEventReserve;
	private Boolean isUp = false;
	private ScrollViewListView commentListView;
	private List<CommentInfor> commentList;
	private CommetnListAdapter mCommentAdapter;
	private TextView mDataNum;
	private TextView mAbleCount;
	private TextView mTimeDes;
	private TextView mTimeQuantum;
	private int webHeight = 0;
	private ImageButton mCommentNext;
	private TextView mCommentNull;
	private LinearLayout mAbleLayout;
	private View mAbleLine;
	private LinearLayout mRemarkLayout;
	private View mRemarkLine;
	private Activity_JoinHandler mActivity_JoinHandler;
	private ScrollScrollView mScrollView;
	private ViewPager mViewPager;
	private ArrayList<ImageView> mlist;
	// 线程标志
	private boolean isStop = false;
	private final String mPageName = "CollectionListActivity";
	private TextView wantto_total;
	private ImageView mIvTop;
	private int index = 0;
	private int pageNum = 5;
	private View footView;
	private LabelHandler mLabelHandler;
	private MyVideoView mMyVideoView;
	private boolean isRemoveFooter;
	/**
	 * 播放信息列表
	 */
	private List<VideoInfo> videoPalyList = new ArrayList<VideoInfo>();
	/**
	 * 视频父控件，不需要的时候隐藏就好
	 */
	private LinearLayout videoLinearLayout;
	private TextView date_num_show;

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
		mMyVideoView.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_dateils);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
		getData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		setTitle();
		mScrollView = (ScrollScrollView) findViewById(R.id.scrollview);
		mScrollView.setOnScrolledListenter(this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mCollect = (ImageView) findViewById(R.id.event_collect);
		mName = (TextView) findViewById(R.id.event_name);
		mLoaction = (TextView) findViewById(R.id.venue_item_location);
		mPhone = (TextView) findViewById(R.id.venue_item_phone);
		mTime = (TextView) findViewById(R.id.venue_time_time);
		mRemarkLayout = (LinearLayout) findViewById(R.id.event_detail_remark);
		mRemarkLine = findViewById(R.id.event_detail_remark_line);
		mTimeQuantum = (TextView) findViewById(R.id.venue_time_quantum);
		mmoney = (TextView) findViewById(R.id.event_item_money);
		mmoney_iv = (ImageView) findViewById(R.id.event_item_money_iv);
		detailShow = (WebView) findViewById(R.id.event_dateils);
		detailMore = (ImageButton) findViewById(R.id.event_dateils_more);
		mCommentNext = (ImageButton) findViewById(R.id.event_dateils_comment_next);
		mAbleLayout = (LinearLayout) findViewById(R.id.event_details_able);
		mAbleLine = findViewById(R.id.event_details_able_line);
		mCommentNext.setOnClickListener(this);
		findViewById(R.id.event_parking).setOnClickListener(this);
		mDataNum = (TextView) findViewById(R.id.evenut_data_num);
		date_num_show = (TextView) findViewById(R.id.evenut_data_num_show);
		mAbleCount = (TextView) findViewById(R.id.event_able_count);
		mTimeDes = (TextView) findViewById(R.id.event_time_des);
		mEventReserve = (RelativeLayout) findViewById(R.id.event_reserve);

		ViewUtil.setWebViewSettings(detailShow, mContext);
		isUp = false;
		mCommentNull = (TextView) findViewById(R.id.dateils_comment_null);
		mIvTop = (ImageView) findViewById(R.id.event_top);
		videoLinearLayout = (LinearLayout) findViewById(R.id.activity_videolayout);
		LinearLayout viodelayout = (LinearLayout) findViewById(R.id.activity_video);
		mMyVideoView = (MyVideoView) viodelayout.findViewById(R.id.whyvideo);
		LinearLayout labellayout = (LinearLayout) findViewById(R.id.event_label);
		mLabelHandler = new LabelHandler(mContext, labellayout, 3, false);
		// addVideoData();
	}

	private void addVideoData() {// 静态数据
		String TEST_PLAY_URL = "http://v.iask.com/v_play_ipad.php?vid=138152839";
		String TEST_PLAY_URL2 = "http://v.iask.com/v_play_ipad.php?vid=138116139";
		String TEST_PLAY_TITLE = "(GTA5)闪电侠席卷圣洛城";
		String TEST_PLAY_TITLE2 = "DOTA2-TI5国际邀请赛";
		String urlimg = "http://d.hiphotos.baidu.com/image/pic/item/a2cc7cd98d1001e9b230cf71ba0e7bec54e79744.jpg";
		String urling2 = "http://imgsrc.baidu.com/forum/w%3D580/sign=4b286c1b4fed2e73fce98624b700a16d/b5cdbede9c82d158b1edda2e850a19d8bd3e4202.jpg";
		mMyVideoView.initPlatyUrlList(videoPalyList);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		initcommtent();
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				// TODO Auto-generated method stub
				mScrollView.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_MOVE:
					int scrollY = view.getScrollY();
					int height = view.getHeight();
					if ((height + scrollY) > WindowsUtil
							.getwindowsHight(mContext)) {
						mIvTop.setVisibility(View.VISIBLE);
					} else {
						mIvTop.setVisibility(View.GONE);
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		mCollect.setOnClickListener(this);
		mEventReserve.setOnClickListener(this);
		mIvTop.setOnClickListener(this);
		findViewById(R.id.venue_location).setOnClickListener(this);
		findViewById(R.id.venue_phone).setOnClickListener(this);
		findViewById(R.id.event_imagelayout_nums).setOnClickListener(this);

		TextView imgs = (TextView) findViewById(R.id.event_image_nums);
		imgs.setText("5");
		imgs.setOnClickListener(this);
		if (this.getIntent() != null) {
			eventId = this.getIntent().getStringExtra("eventId");
			LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.activity_want_to);
			wantto_total = (TextView) mLinearLayout.findViewById(R.id.join_num);
			mActivity_JoinHandler = new Activity_JoinHandler(mContext,
					mLinearLayout, eventId);
		}
		if (eventId == null) {
			mLoadingHandler.showErrorText("数据加载失败！");
			return;
		}
	}

	/**
	 * 获取报名用户
	 */
	private void getJoinUsers() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", eventId);
		params.put("pageIndex", "0");
		params.put("pageNum", "6");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WANTTO_USERS_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							mActivity_JoinHandler.setUserListDate(JsonUtil
									.getJoinUserList(resultStr));
							wantto_total.setText(UserPersionSInfo.totalNum);
						} else {
							ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	/**
	 * 设置viewpager数据
	 */
	private void setViewPager() {
		mViewPager.removeAllViews();
		mViewPager.removeAllViewsInLayout();
		mlist = new ArrayList<ImageView>();
		String detailStr = eventInfo.getEventDetailsIconUrl();

		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		for (int i = 0; i < 1; i++) {
			// 设置广告图
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(TextUtil.getUrlMiddle(detailStr), imageView,
							Options.getListOptions());
			mlist.add(imageView);
		}

		BannerAdapter mAdapter = new BannerAdapter(mlist);
		mViewPager.setAdapter(mAdapter);

		// mViewPager.setPageTransformer(true, new DepthPageTransformer());
		// try {// 设置时间
		// Field field = null;
		// field = ViewPager.class.getDeclaredField("mScroller");
		// field.setAccessible(true);
		// FixedSpeedScroller scroller = new
		// FixedSpeedScroller(mViewPager.getContext(),
		// new AccelerateInterpolator());
		// field.set(mViewPager, scroller);
		// scroller.setmDuration(500);
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
	}

	/**
	 * 播放viewpager
	 */
	private void onStartViewPager() {
		// 开启新线程，2秒一次更新Banner
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!isStop) {
					SystemClock.sleep(2500);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (mlist != null && mlist.size() > 1) {
								mViewPager.setCurrentItem(mViewPager
										.getCurrentItem() + 1);
							}
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 获取评论数据
	 */
	private void getCommtentList(int index) {
		if (eventInfo == null) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("moldId", eventInfo.getEventId());
		params.put("type", activityType);
		params.put("pageIndex", index + "");
		params.put("pageNum", pageNum + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommentList,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<CommentInfor> myCommentList = JsonUtil
									.getCommentInforFromString(resultStr);
							if (myCommentList.size() > 0) {
								commentList.addAll(myCommentList);
								if (myCommentList.size() < pageNum) {
									commentListView.removeFooterView(footView);
									isRemoveFooter = true;
								} else if (isRemoveFooter) {
									isRemoveFooter = false;
									commentListView.addFooterView(footView);
								}
							} else {
								isRemoveFooter = true;
								commentListView.removeFooterView(footView);
							}
							if (commentList.size() > 0) {
								mCommentNull.setVisibility(View.GONE);
							} else {
								mCommentNull.setVisibility(View.VISIBLE);
							}
							setData();
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 初始化评论数据
	 */
	private void initcommtent() {
		LinearLayout comtentlayout = (LinearLayout) findViewById(R.id.event_dateils_commentlayout);
		commentListView = (ScrollViewListView) comtentlayout
				.findViewById(R.id.comment_listview);
		footView = View.inflate(mContext, R.layout.list_foot, null);
		commentListView.addFooterView(footView);
		commentList = new ArrayList<CommentInfor>();
		mCommentAdapter = new CommetnListAdapter(mContext, commentList, false);
		commentListView.setAdapter(mCommentAdapter);
		comtentlayout.findViewById(R.id.commit_comment)
				.setOnClickListener(this);
		footView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				index = index + pageNum;
				getCommtentList(index);
			}
		});
	}

	/**
	 * 设置评论数据
	 */
	private void setcomment() {
		mCommentAdapter.setList(commentList);
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		Log.d("http", eventId);
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.EventUrl.ACTIVITY_ID, eventId);

		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WFF_HTTP_EVENT_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
									.getJsonStatus(resultStr)) {
								eventInfo = JsonUtil.getEventInfo(resultStr);
								if (eventInfo.getVideoPalyList() != null
										&& eventInfo.getVideoPalyList().size() > 0) {
									mMyVideoView.initPlatyUrlList(eventInfo
											.getVideoPalyList());
								} else {
									videoLinearLayout.setVisibility(View.GONE);
								}

								getCommtentList(index);
								getJoinUsers();
							} else {
								mLoadingHandler.showErrorText(JsonUtil.JsonMSG);
							}

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
		// 1表示已经收藏，0表示未收藏
		Log.i("tag", eventInfo.toString());
		if (eventInfo.getEventIsCollect() != null
				&& "1".equals(eventInfo.getEventIsCollect())) {
			mCollect.setImageResource(R.drawable.sh_icon_collect_after);
			isCollect = true;
		} else {
			mCollect.setImageResource(R.drawable.sh_icon_collect_befor);
			isCollect = false;
		}
		mName.setText(eventInfo.getEventName());
		mLoaction.setText(eventInfo.getEventAddress());
		mPhone.setText(eventInfo.getEventTel());
		int date = Integer.parseInt(eventInfo.getActivityDateNums());
		if (date > 0) {
			mDataNum.setText(eventInfo.getActivityDateNums());
		} else if (date == 0) {
			mDataNum.setText("进行中");
			date_num_show.setVisibility(View.GONE);
		} else {
			mDataNum.setText("已结束");
			mDataNum.setTextColor(0xff8d8d8d);
			date_num_show.setVisibility(View.GONE);
		}

		if ("1".equals(eventInfo.getActivityIsReservation())) {
			mAbleLayout.setVisibility(View.GONE);
			mAbleLine.setVisibility(View.GONE);
		} else {
			mAbleLayout.setVisibility(View.VISIBLE);
			mAbleLine.setVisibility(View.VISIBLE);
			mAbleCount.setText(eventInfo.getActivityAbleCount());
		}
		if ("".equals(eventInfo.getActivityTimeDes())
				|| eventInfo.getActivityTimeDes() == null) {
			mRemarkLayout.setVisibility(View.GONE);
			mRemarkLine.setVisibility(View.GONE);
		} else {
			mRemarkLine.setVisibility(View.VISIBLE);
			mRemarkLayout.setVisibility(View.VISIBLE);
			mTimeDes.setText(eventInfo.getActivityTimeDes());
		}
		if (eventInfo.getActivityStartTime() != null) {
			mTime.setText(TextUtil.getDate(eventInfo.getActivityStartTime(),
					eventInfo.getEventEndTime()));
		}

		if (eventInfo.getTimeQuantum() != null
				&& eventInfo.getTimeQuantum().length() > 0) {
			mTimeQuantum.setText(TextUtil.getTime(eventInfo.getTimeQuantum()));

		} else {
			mTimeQuantum.setText("没有时间");
		}
		if (eventInfo.getEventContent() != null) {
			detailShow.loadDataWithBaseURL(null, ViewUtil.initContent(ViewUtil
					.subString(eventInfo.getEventContent(), true, detailMore),
					mContext), AppConfigUtil.APP_MIMETYPE,
					AppConfigUtil.APP_ENCODING, null);
		}
		// detailMore.setOnClickListener(this);
		detailMore.setVisibility(View.GONE);
		if (eventInfo.getActivityIsReservation().equals("2")
				&& !eventInfo.getActivityAbleCount().equals("0")) {
			// mmoney.setText("免费");
			// mmoney_iv.setVisibility(View.GONE);
			mmoney.setText("需要事先预订");

		} else {
			mmoney.setText("直接前往，无需预订");
			// if (ValidateUtil.isNumeric(eventInfo.getEventPrice())) {
			// mmoney_iv.setVisibility(View.VISIBLE);
			// } else {
			// mmoney_iv.setVisibility(View.GONE);
			// }
			// mmoney.setText(eventInfo.getEventPrice());
		}
		if (eventInfo.getActivityIsReservation().equals("2")) {
			if ("0".equals(eventInfo.getActivityAbleCount())) {
				mEventReserve.setVisibility(View.GONE);
			} else {
				mEventReserve.setVisibility(View.VISIBLE);
			}
		} else {
			mEventReserve.setVisibility(View.GONE);
		}
		if (eventInfo.getActivityIsWant()) {
			mActivity_JoinHandler.setIsJoin(true);
			mActivity_JoinHandler.setWantToGray();
		} else {
			mActivity_JoinHandler.setIsJoin(false);
			mActivity_JoinHandler.setWantToRed();
		}
		mLabelHandler.setLabelsData(eventInfo.getActivityTags());
		setcomment();
		setViewPager();
		mLoadingHandler.stopLoading();
	}

	/**
	 * webview显示部分与全部 boo:true为截取内容（内容小于英文120（中文60）字符将显示部分），false为全部显示
	 */
	public String calculate(String content, boolean boo) {
		Log.i("newHtmlContents", content);
		content = content.replaceAll("<a([^>]{0,})>", "").trim();// 去除所有的a标签
		// content = content.replaceAll("<p([^>]{0,})>", "").trim();// 去除所有的p标签
		int startString = 120; // 英文取120字，中文取一半
		for (int i = 0; i < content.length(); i++) {
			if (isChineseChar(content.charAt(i))) {

				startString = (startString / 2) + i;
				startString = startString > content.length() ? content.length()
						: startString;
				break;
			}
		}
		if (startString >= content.length()) {
			detailMore.setVisibility(View.GONE);
		}

		if (content.length() > startString && boo) {
			content = content.substring(0, startString) + "...";
		}
		return content;
	}

	/**
	 * 判断是否是中文字符
	 * 
	 * @param @param c
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 * @Title: isChineseChar
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 */
	public static boolean isChineseChar(char c) {
		try {
			// 如果字节数大于1，是汉字
			return String.valueOf(c).getBytes("utf-8").length > 1;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

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
		CollectUtil.addActivity(mContext, eventId, new CollectCallback() {

			@Override
			public void onPostExecute(boolean isOk) {
				// TODO Auto-generated method stub
				if (isOk) {
					EventListActivity.setListCollction(1);
					ToastUtil.showToast("收藏成功");
					mCollect.setImageResource(R.drawable.sh_icon_collect_after);
					eventInfo.setEventIsCollect("1");
					isCollect = !isCollect;
				} else {
					ToastUtil.showToast("收藏失败");
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		// if (detailMore != null && detailMore.getVisibility() != View.GONE) {
		// detailMore.setVisibility(View.VISIBLE);
		// }
		MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
		mMyVideoView.onResume();
	}

	/**
	 * 取消收藏
	 */
	private void cancelCollect() {
		CollectUtil.cancelActivity(mContext, eventId, new CollectCallback() {

			@Override
			public void onPostExecute(boolean isOk) {
				// TODO Auto-generated method stub
				if (isOk) {
					EventListActivity.setListCollction(0);
					ToastUtil.showToast("取消收藏");
					mCollect.setImageResource(R.drawable.sh_icon_collect_befor);
					eventInfo.setEventIsCollect("0");
					isCollect = !isCollect;
				} else {
					ToastUtil.showToast("操作失败");
				}
			}
		});
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		ImageButton mTitleRight = (ImageButton) mTitle
				.findViewById(R.id.title_right);
		mTitleRight.setOnClickListener(this);
		mTitleRight.setImageResource(R.drawable.sh_icon_title_share);
		TextView mTitle_tv = (TextView) mTitle.findViewById(R.id.title_content);
		mTitle_tv.setText("活动详情");
		mTitle_tv.setVisibility(View.VISIBLE);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (!ButtonUtil.isDelayClick()) {
			return;
		}
		Intent intent = null;
		switch (view.getId()) {
		case R.id.event_top:
			mIvTop.setVisibility(View.GONE);
			mScrollView.smoothScrollTo(0, 0);
			break;
		case R.id.title_left:
			finish();
			break;
		case R.id.event_collect:
			onCollect();
			break;
		case R.id.event_dateils_comment_next:
			intent = new Intent(mContext, MoreCommentActivity.class);
			intent.putExtra("Id", eventInfo.getEventId());
			intent.putExtra("type", activityType);
			startActivity(intent);
			break;
		case R.id.commit_comment:
			if (MyApplication.UserIsLogin) {
				intent = new Intent(mContext, WriteCommentActivity.class);
				intent.putExtra("Id", eventInfo.getEventId());
				intent.putExtra("type", activityType);
				startActivityForResult(intent,
						HttpCode.ServerCode_Comment.CODE_BACK);
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				startActivity(intent);
			}

			break;
		case R.id.event_reserve:
			if (MyApplication.UserIsLogin) {
				// if (null != MyApplication.loginUserInfor.getUserMobileNo()
				// &&
				// MyApplication.loginUserInfor.getUserMobileNo().toString().length()
				// > 0) {
				intent = new Intent(mContext, EventReserveActivity.class);
				intent.putExtra("EventInfo", eventInfo);
				startActivity(intent);
				// } else {
				// ToastUtil.showToast(MyApplication.BindPhoneShow);
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
			info.setTitle(eventInfo.getEventName());
			info.setImageUrl(eventInfo.getEventDetailsIconUrl());
			info.setContentUrl(eventInfo.getShareUrl());
			info.setContent(ViewUtil.getTextFromHtml(eventInfo
					.getEventContent().toString()));

			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		case R.id.venue_phone:
			intent = new Intent(mContext, MessageDialog.class);
			intent.putExtra(DialogTypeUtil.DialogContent, mPhone.getText());
			FastBlur.getScreen((Activity) mContext);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			startActivity(intent);

			break;
		case R.id.event_dateils_more:
			if (isUp) {// 缩
				detailShow.loadDataWithBaseURL(null, ViewUtil.initContent(
						ViewUtil.subString(eventInfo.getEventContent(), false,
								detailMore), mContext),
						AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING,
						null);
				detailMore
						.setImageResource(R.drawable.sh_icon_venue_profile_down);
				detailShow.invalidate();
			} else {// 放
				detailShow.loadDataWithBaseURL(null, ViewUtil.initContent(
						ViewUtil.subString(eventInfo.getEventContent(), true,
								detailMore), mContext),
						AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING,
						null);
				detailMore
						.setImageResource(R.drawable.sh_icon_venue_profile_up);
				detailShow.invalidate();

			}
			isUp = !isUp;
			break;
		case R.id.venue_location:
			if (eventInfo.getEventLat() != null
					&& eventInfo.getEventLat().length() > 0
					&& !eventInfo.getEventLat().equals("0")
					&& eventInfo.getEventLon() != null
					&& eventInfo.getEventLon().length() > 0
					&& !eventInfo.getEventLon().equals("0")) {

				intent = new Intent(mContext, BigMapViewActivity.class);
				intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
				intent.putExtra("titleContent", eventInfo.getEventName());
				// 传入场馆的位置
				intent.putExtra("lat", eventInfo.getEventLat());
				intent.putExtra("lon", eventInfo.getEventLon());
				intent.putExtra("address", eventInfo.getEventAddress());
				startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;
		// 附近车位
		case R.id.event_parking:
			if (eventInfo.getEventLat() != null
					&& eventInfo.getEventLat().length() > 0
					&& eventInfo.getEventLon() != null
					&& eventInfo.getEventLon().length() > 0) {
				intent = new Intent(mContext, NearbyParkingActivity.class);
				intent.putExtra("titleContent", eventInfo.getEventName());
				intent.putExtra("lat", eventInfo.getEventLat());
				intent.putExtra("lon", eventInfo.getEventLon());
				startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case HttpCode.ServerCode_Comment.CODE_BACK:
			if (commentList != null) {
				commentList.clear();
			}
			index = 0;
			getCommtentList(index);
			break;

		default:
			break;
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			// LayoutParams lp = detailShow.getLayoutParams();
			// lp.height = AppConfigUtil.Page.PageDefaHight;
			// detailShow.setLayoutParams(lp);
		}
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		initView();
		initData();
		getData();
	}

	@Override
	protected void onDestroy() {
		// 关闭定时器
		isStop = true;
		super.onDestroy();
		mMyVideoView.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mMyVideoView.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mMyVideoView.getVideoConfiguration() == Configuration.ORIENTATION_LANDSCAPE) {// 全屏的时候监听
			return mMyVideoView.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		// TODO Auto-generated method stub
		if (scrollY < 10) {
			mIvTop.setVisibility(View.GONE);
		}
		Log.d("onScrolled", "scrollX:" + scrollX + "--scrollY:" + scrollY
				+ "---clampedX:" + clampedX + "---clampedY:" + clampedY);
	}
}
