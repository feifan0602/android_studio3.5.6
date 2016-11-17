package com.sun3d.culturalShanghai.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.CollectUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.adapter.ActivityRoomListAdapter;
import com.sun3d.culturalShanghai.adapter.BannerAdapter;
import com.sun3d.culturalShanghai.adapter.CollectionGridAdapter;
import com.sun3d.culturalShanghai.adapter.CommetnListAdapter;
import com.sun3d.culturalShanghai.adapter.RelatedActivityAdapter;
import com.sun3d.culturalShanghai.animation.DepthPageTransformer;
import com.sun3d.culturalShanghai.animation.FixedSpeedScroller;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.handler.VenueDetailMusicHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;
import com.sun3d.culturalShanghai.object.CollectionInfor;
import com.sun3d.culturalShanghai.object.CommentInfor;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.video.MyVideoView;
import com.sun3d.culturalShanghai.video.VideoInfo;
import com.sun3d.culturalShanghai.view.CustomDigitalClock;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.ScrollScrollView;
import com.sun3d.culturalShanghai.view.ScrollViewGridView;
import com.sun3d.culturalShanghai.view.ScrollScrollView.OnScrolledListenter;
import com.sun3d.culturalShanghai.view.ScrollViewListView;
import com.sun3d.culturalShanghai.windows.SelectPicPopupWindow;
import com.umeng.analytics.MobclickAgent;

/**
 * 场馆详情的页面 注： 此页面由多个listview组成 根据 展馆的ID 来获取数据 查看哪些数据存在 然后呈现出来
 * 
 * @author wenff
 * 
 */
public class VenueDetailActivity extends FragmentActivity implements
		OnClickListener, RefreshListenter, OnScrolledListenter {
	/**
	 * 3.5 点赞 评论
	 */
	private LinearLayout content;
	private ImageView fenge_comment;
	private ScrollViewGridView mLikeGridView;
	private ScrollViewListView mListView;
	private GridViewAdapter mGridViewAdapter;
	private Activity activity;
	private String eventInfoId;
	private String activityType = "2";// 活动评论type
	private List<CommentInfor> myCommentList;
	private CommetnListAdapter mCommentAdapter;
	private List<UserPersionSInfo> listUsers;
	private LinearLayout mCommentLayout;
	private TextView tvMoreComment;
	private boolean isWant;
	private TextView mIsWant;
	private TextView comment_num;
	private TextView zanguo_tv;
	private TextView Collecton_left;
	private Context mContext;
	/**
	 * 这是上面切换的viewpager
	 */
	private ViewPager mViewPager;
	// 线程标志
	private boolean isStop = false;
	// private LinearLayout venue_addcomment;
	private String venueType = "1";// 活动评论type
	private String venueId;
	private VenueDetailInfor venueInfo;
	private ImageView mCollect;
	private boolean isChoice = false;
	// private TextView mVenueLoveHiddenIv;
	private String[] roomUrls;
	private String[] roomNames;
	private TextView mRoomName;
	private BannerListener bannerListener;
	private List<ImageView> mlist;
	private LoadingHandler mLoadingHandler;
	private ScrollScrollView scrollview;
	private final String mPageName = "VenueDetailActivity";
	private VenueDetailMusicHandler mVenueDetailMusicHandler;
	private WebView mVenueProfileDetails;
	private TextView venue_more_activity;
	private RelativeLayout beizhu_rl;
	private TextView beizhu_right;
	private ImageButton detailMore;
	/**
	 * 活动室listview
	 */
	private ListView mRoomListView;
	private ListView mActivityListView;
	private TextView nameContent;
	private TextView nameContentName;
	private View nameCommentLine;
	private TextView venueName;
	private RatingBar mRating;
	private ImageView venue_item_bus;
	private ImageView venue_item_metro;
	private TextView phone;
	private TextView time;
	private TextView memo;
	private RelativeLayout memoLayout;
	private TextView address;
	private TextView value;
	private RelativeLayout collect_activity;
	private LinearLayout mVenueEvent;
	private RelativeLayout mVenueRoom;
	private TextView moreRoom;
	private ImageView reserve;
	private LinearLayout mContentLayout;
	private List<ActivityRoomInfo> mRoomList;
	private ActivityRoomListAdapter mRoomAdapter;
	private List<EventInfo> mEventList;
	private RelatedActivityAdapter mEventAdapter;
	private ScrollViewListView mCommentListView;
	private List<CommentInfor> commentList;
	private CommetnListAdapter mCommetnListAdapter;
	private ImageView mTopView;
	private CustomDigitalClock venue_clock;
	private int pageNum = 5;
	private int page = 0;
	private View footView;
	private TextView mCommentNull;
	private boolean isUp = true;
	private MyVideoView mMyVideoView;
	private ImageView back_iv;
	private ImageView share;
	private TextView venue_profile;
	private TextView activity_comment_tv;
	private SelectPicPopupWindow menuWindow;
	private ScrollView refreshscrollView;
	private TextView tagname, dictname, subItem, subItem1;
	private RelativeLayout zanguo_rl;
	private RelativeLayout Collection_rl;
	private GridView Collect_gridview;
	private TextView Collection_num;
	private CollectionGridAdapter mCollectionGridAdapter;
	private List<CollectionInfor> Collectionlist;
	private List<String> CollectionTimelist;
	private List<String> CollectionTypelist;
	private ImageView fenge_zan, fenge_collection, fenge_room, fenge_detail,
			fenge_detail_event, fenge_muisc;
	private boolean isRemoveFooter;
	/**
	 * 播放信息列表
	 */
	private List<VideoInfo> videoPalyList = new ArrayList<VideoInfo>();
	/**
	 * 视频父控件，不需要的时候隐藏就好
	 */
	private LinearLayout videoLinearLayout;
	private TextView price, price_last;
	private ImageView activity_comment_zan_iv;
	private String TAG = "VenueDetailActivity";

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		mMyVideoView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
		mMyVideoView.onPause();
		if (mVenueDetailMusicHandler != null) {
			mVenueDetailMusicHandler.pause();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_venue_details);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		initView();
		initComment();
		initTitle();
		getData();
		onStartViewPager();

	}

	private void initComment() {
		content = (LinearLayout) findViewById(R.id.venue_comment_activity);
		fenge_comment = (ImageView) findViewById(R.id.fenge_comment);
		zanguo_rl = (RelativeLayout) findViewById(R.id.zanguo_rl);
		comment_num = (TextView) content.findViewById(R.id.comment_num);
		comment_num.setTypeface(MyApplication.GetTypeFace());
		mLikeGridView = (ScrollViewGridView) content
				.findViewById(R.id.like_gridview);
		mListView = (ScrollViewListView) content
				.findViewById(R.id.comment_listview);
		mCommentLayout = (LinearLayout) content
				.findViewById(R.id.activity_comment_edit);
		// content.findViewById(R.id.add).setOnClickListener(this);
		mIsWant = (TextView) content.findViewById(R.id.activity_comment_zan_tv);
		// mIsWant.setOnClickListener(this);
		// content.findViewById(R.id.activity_comment_tv).setOnClickListener(this);
		zanguo_tv = (TextView) content.findViewById(R.id.zanguo_tv);
		zanguo_tv.setTypeface(MyApplication.GetTypeFace());
		fenge_zan = (ImageView) findViewById(R.id.fenge_zan);
		fenge_collection = (ImageView) findViewById(R.id.fenge_collection);
		fenge_room = (ImageView) findViewById(R.id.fenge_room);
		fenge_detail = (ImageView) findViewById(R.id.fenge_detail);
		fenge_muisc = (ImageView) findViewById(R.id.fenge_muisc);
		fenge_detail_event = (ImageView) findViewById(R.id.fenge_detail_event);
		tvMoreComment = (TextView) content.findViewById(R.id.more_comment);
		// tvMoreComment.setOnClickListener(this);
		myCommentList = new ArrayList<CommentInfor>();
		listUsers = new ArrayList<UserPersionSInfo>();
		mGridViewAdapter = new GridViewAdapter(listUsers);
		mCommentAdapter = new CommetnListAdapter(mContext, myCommentList, false);

		mLikeGridView.setAdapter(mGridViewAdapter);
		mListView.setAdapter(mCommentAdapter);
		mCommentLayout.setVisibility(View.VISIBLE);
		getJoinUsers();
		getBrowseCount();
		getCommtentList();
		getCollectionlist();
	}

	private void getCollectionlist() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("venueId", venueId);
		params.put("pageIndex", String.valueOf(page));
		params.put("pageNum", HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Collection.COLLECTION_ALL_GETURL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							Collectionlist.clear();
							JsonUtil.getCollectionInfo(CollectionTimelist,
									CollectionTypelist, Collectionlist,
									resultStr);
							if (Collectionlist.size() != 0) {
								Collection_rl.setVisibility(View.VISIBLE);

								fenge_collection.setVisibility(View.VISIBLE);
								Collecton_left.setVisibility(View.VISIBLE);
								Collection_num.setVisibility(View.VISIBLE);
								Collect_gridview.setVisibility(View.VISIBLE);
								String text = "共" + Collectionlist.size()
										+ "个藏品";
								Collection_num.setText(MyApplication
										.getTextColor(text));
								mCollectionGridAdapter.setList(Collectionlist);
								mCollectionGridAdapter.notifyDataSetChanged();
							}
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("NewApi")
	private void initView() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		venue_clock = (CustomDigitalClock) findViewById(R.id.venue_clock);
		venue_clock.setVisibility(View.GONE);
		Collectionlist = new ArrayList<CollectionInfor>();
		CollectionTimelist = new ArrayList<String>();
		CollectionTypelist = new ArrayList<String>();
		Collection_rl = (RelativeLayout) findViewById(R.id.Collection_rl);
		Collection_rl.setOnClickListener(this);// 更多藏品
		Collect_gridview = (GridView) findViewById(R.id.Collect_gridview);
		Collection_num = (TextView) findViewById(R.id.Collection_num);
		Collection_num.setTypeface(MyApplication.GetTypeFace());
		Collecton_left = (TextView) findViewById(R.id.Collecton_left);
		Collecton_left.setTypeface(MyApplication.GetTypeFace());
		mCollectionGridAdapter = new CollectionGridAdapter(mContext,
				Collectionlist);
		Collect_gridview.setAdapter(mCollectionGridAdapter);
		Collect_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent itent = new Intent(mContext,
						CollcetionDetailsActivity.class);
				itent.putExtra("Id", Collectionlist.get(arg2).getId());
				startActivity(itent);
			}
		});
		//
		refreshscrollView = (ScrollView) findViewById(R.id.scrollview);
		beizhu_rl = (RelativeLayout) findViewById(R.id.beizhu_rl);
		beizhu_right = (TextView) findViewById(R.id.beizhu_right);
		activity_comment_zan_iv = (ImageView) findViewById(R.id.activity_comment_zan_iv);
		activity_comment_zan_iv.setOnClickListener(this);
		subItem = (TextView) findViewById(R.id.subItem);
		subItem1 = (TextView) findViewById(R.id.subItem1);
		tagname = (TextView) findViewById(R.id.tagname);
		dictname = (TextView) findViewById(R.id.dictname);
		price = (TextView) findViewById(R.id.price);
		price_last = (TextView) findViewById(R.id.price_last);
		beizhu_right.setTypeface(MyApplication.GetTypeFace());
		tagname.setTypeface(MyApplication.GetTypeFace());
		dictname.setTypeface(MyApplication.GetTypeFace());
		price.setTypeface(MyApplication.GetTypeFace());
		price_last.setTypeface(MyApplication.GetTypeFace());

		mLoadingHandler = new LoadingHandler(loadingLayout);
		venue_more_activity = (TextView) findViewById(R.id.venue_more_activity);
		venue_more_activity.setOnClickListener(this);
		activity_comment_tv = (TextView) findViewById(R.id.activity_comment_tv);
		activity_comment_tv.setOnClickListener(this);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		venueId = getIntent().getExtras().getString("venueId");
		venue_profile = (TextView) findViewById(R.id.venue_profile);
		venue_profile.setVisibility(View.VISIBLE);
		share = (ImageView) findViewById(R.id.share);
		share.setOnClickListener(this);
		venue_profile.setOnClickListener(this);
		scrollview = (ScrollScrollView) findViewById(R.id.scrollview);
		scrollview.setOnScrolledListenter(this);
		// venue_addcomment = (LinearLayout) findViewById(R.id.venue_comment);
		// venue_addcomment.setOnClickListener(this);
		// venue_addcomment.setVisibility(View.GONE);
		back_iv = (ImageView) findViewById(R.id.back_iv);
		back_iv.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setFocusable(true);
		mViewPager.setFocusableInTouchMode(true);
		mViewPager.requestFocus();
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				// TODO Auto-generated method stub
				scrollview.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mRoomName = (TextView) findViewById(R.id.venue_room_name);
		mCollect = (ImageView) findViewById(R.id.collect);
		mTopView = (ImageView) findViewById(R.id.venue_top);
		mTopView.setOnClickListener(this);
		mCollect.setOnClickListener(this);
		scrollview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_MOVE:
					int scrollY = view.getScrollY();
					int height = view.getHeight();
					if ((height + scrollY) > WindowsUtil
							.getwindowsHight(mContext)) {
						mTopView.setVisibility(View.VISIBLE);
					} else {
						mTopView.setVisibility(View.GONE);
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		initDetailView();
	}

	/**
	 * 初始化场馆详情的控件
	 */
	private void initDetailView() {
		LinearLayout musicLyayout = (LinearLayout) findViewById(R.id.venue_muisc_layout);
		mVenueDetailMusicHandler = new VenueDetailMusicHandler(musicLyayout);
		mVenueProfileDetails = (WebView) findViewById(R.id.venue_profile_details);
		detailMore = (ImageButton) findViewById(R.id.venue_profile_details_more);
		ViewUtil.setWebViewSettings(mVenueProfileDetails, mContext);
		onWindowFocusChanged(true);
		mRoomListView = (ListView) findViewById(R.id.venue_room_list);
		mActivityListView = (ListView) findViewById(R.id.venue_activity_list);
		mRoomListView.setFocusable(false);
		mActivityListView.setFocusable(false);
		mVenueProfileDetails.setFocusable(false);
		nameContent = (TextView) findViewById(R.id.venue_content);
		nameContentName = (TextView) findViewById(R.id.venue_content_name);
		nameContent.setTypeface(MyApplication.GetTypeFace());
		nameContentName.setTypeface(MyApplication.GetTypeFace());

		nameCommentLine = findViewById(R.id.venue_comment_lien);
		venueName = (TextView) findViewById(R.id.venue_name);
		mRating = (RatingBar) findViewById(R.id.venue_ratingbar);
		venue_item_bus = (ImageView) findViewById(R.id.venue_item_bus);
		venue_item_metro = (ImageView) findViewById(R.id.venue_item_metro);
		phone = (TextView) findViewById(R.id.venue_profile_telephone_text);
		time = (TextView) findViewById(R.id.venue_profile_data_text);
		memo = (TextView) findViewById(R.id.venue_profile_data_text_show);
		memoLayout = (RelativeLayout) findViewById(R.id.venue_profile_timemakert);
		address = (TextView) findViewById(R.id.venue_profile_activity_text);
		value = (TextView) findViewById(R.id.event_text_money);
		collect_activity = (RelativeLayout) findViewById(R.id.collect_activity);
		mVenueEvent = (LinearLayout) findViewById(R.id.venue_details_event);
		mVenueRoom = (RelativeLayout) findViewById(R.id.venue_detail_room);
		moreRoom = (TextView) findViewById(R.id.venue_more_room);
		reserve = (ImageView) findViewById(R.id.venue_item_reserve);
		mContentLayout = (LinearLayout) findViewById(R.id.venue_content_layout);

		LinearLayout commentLayout = (LinearLayout) findViewById(R.id.venue_dateils_commentlayout);
		mCommentListView = (ScrollViewListView) commentLayout
				.findViewById(R.id.comment_listview);
		mCommentNull = (TextView) commentLayout
				.findViewById(R.id.dateils_comment_null);
		commentLayout.findViewById(R.id.commit_comment)
				.setOnClickListener(this);
		videoLinearLayout = (LinearLayout) findViewById(R.id.activity_videolayout);
		LinearLayout viodelayout = (LinearLayout) findViewById(R.id.activity_video);
		mMyVideoView = (MyVideoView) viodelayout.findViewById(R.id.whyvideo);
		inittype();
		// addVideoData();
		initCommentData();
		initDetailData();
	}

	private void inittype() {

		activity_comment_tv.setTypeface(MyApplication.GetTypeFace());
		venue_more_activity.setTypeface(MyApplication.GetTypeFace());
		mRoomName.setTypeface(MyApplication.GetTypeFace());
		mCommentNull.setTypeface(MyApplication.GetTypeFace());
		moreRoom.setTypeface(MyApplication.GetTypeFace());
		venueName.setTypeface(MyApplication.GetTypeFace());
		phone.setTypeface(MyApplication.GetTypeFace());
		time.setTypeface(MyApplication.GetTypeFace());
		memo.setTypeface(MyApplication.GetTypeFace());
		address.setTypeface(MyApplication.GetTypeFace());
		value.setTypeface(MyApplication.GetTypeFace());
	}

	private void addVideoData() {// 静态数据
		String TEST_PLAY_URL = "http://v.iask.com/v_play_ipad.php?vid=138152839";
		String TEST_PLAY_URL2 = "http://v.iask.com/v_play_ipad.php?vid=138116139";
		String TEST_PLAY_TITLE = "<GTA5>闪电侠席卷圣洛城";
		String TEST_PLAY_TITLE2 = "DOTA2-TI5国际邀请赛";
		String urlimg = "http://d.hiphotos.baidu.com/image/pic/item/a2cc7cd98d1001e9b230cf71ba0e7bec54e79744.jpg";
		String urling2 = "http://imgsrc.baidu.com/forum/w%3D580/sign=4b286c1b4fed2e73fce98624b700a16d/b5cdbede9c82d158b1edda2e850a19d8bd3e4202.jpg";
	}

	private void initCommentData() {
		mCommentListView.setFocusable(false);
		commentList = new ArrayList<CommentInfor>();
		mCommetnListAdapter = new CommetnListAdapter(mContext, commentList,
				true);
		footView = View.inflate(mContext, R.layout.list_foot, null);
		mCommentListView.addFooterView(footView);
		mCommentListView.setAdapter(mCommetnListAdapter);
		footView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				page = page + pageNum;
				getCommentData(page);
			}
		});

	}

	/*
	 * 设置数据
	 */

	public void setCommentData(List<CommentInfor> mList) {
		if (mList.size() > 0) {
			commentList.addAll(mList);
			mCommetnListAdapter.setList(commentList);
			if (mList.size() < pageNum) {
				mCommentListView.removeFooterView(footView);
				isRemoveFooter = true;
			} else if (isRemoveFooter) {
				isRemoveFooter = false;
				mCommentListView.addFooterView(footView);
			}
		} else {
			isRemoveFooter = true;
			mCommentListView.removeFooterView(footView);
		}
		if (commentList.size() > 0) {
			mCommentNull.setVisibility(View.GONE);
		} else {
			mCommentNull.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 某一类评论
	 * 
	 * @param page
	 */
	private void getCommentData(int page) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("moldId", venueId);
		params.put("type", "1");
		params.put("pageIndex", String.valueOf(page));
		params.put("pageNum", pageNum + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommentList,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<CommentInfor> myCommentList = JsonUtil
									.getCommentInforFromString(resultStr);
							setCommentData(myCommentList);
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});

	}

	/**
	 * 初始化场馆详情数据
	 */
	private void initDetailData() {
		findViewById(R.id.venue_profile_telephone).setOnClickListener(this);// 咨询
		findViewById(R.id.venue_profile_parking).setOnClickListener(this);// 更多车位
		findViewById(R.id.collect_activity).setOnClickListener(this);// 更多藏品
		findViewById(R.id.venue_profile_activity).setOnClickListener(this);// 场馆位置
		venueId = this.getIntent().getExtras().getString("venueId");
		moreRoom.setOnClickListener(this);
		detailMore.setOnClickListener(this);
		// 活动室列表
		mRoomList = new ArrayList<ActivityRoomInfo>();
		mRoomAdapter = new ActivityRoomListAdapter(mContext, mRoomList);
		mRoomListView.setAdapter(mRoomAdapter);
		// 活动列表
		mEventList = new ArrayList<EventInfo>();
		mEventAdapter = new RelatedActivityAdapter(mContext, mEventList, false);
		mActivityListView.setAdapter(mEventAdapter);

		mRoomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						ActivityRoomDateilsActivity.class);
				intent.putExtra("Id", mRoomList.get(arg2).getRoomId());
				startActivity(intent);
			}
		});
		mActivityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, EventDetailsActivity.class);
				intent.putExtra("eventId", mEventList.get(arg2).getEventId());
				startActivity(intent);
			}
		});
	}

	// if (v.getActivityTags().size() == 1) {
	// name.setText(eventInfo.getActivityTags().get(0));
	// } else if (eventInfo.getActivityTags().size() == 2) {
	// name1.setVisibility(View.VISIBLE);
	// name.setText(eventInfo.getActivityTags().get(0));
	// name1.setText(eventInfo.getActivityTags().get(1));
	// } else if (eventInfo.getActivityTags().size() == 3) {
	// name1.setVisibility(View.VISIBLE);
	// name2.setVisibility(View.VISIBLE);
	// name.setText(eventInfo.getActivityTags().get(0));
	// name1.setText(eventInfo.getActivityTags().get(1));
	// name2.setText(eventInfo.getActivityTags().get(2));
	// }
	/**
	 * 获取场馆数据
	 */
	private void getVenueInfo() {
		mVenueDetailMusicHandler.setMusicUrl(venueInfo.getVenueVoiceUrl());
		setVenueData();
		getRoomList();
		getActivityList();
		getCommentData(0);// 获取评论
	}

	/**
	 * 获取活动室列表
	 */
	private void getRoomList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, venueInfo.getVenueId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		params.put(HttpUrlList.HTTP_PAGE_NUM, "300");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.ActivityRoomUrl.ROOM_LIST_URL, params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是活动室的数据"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<ActivityRoomInfo> mnewRoomList = JsonUtil
									.getRoomList(resultStr);
							Log.i(TAG, "size  ==  "+mnewRoomList.size());
							if (mnewRoomList.size() > 0) {
								mRoomList.clear();
								fenge_room.setVisibility(View.VISIBLE);
								if (mnewRoomList.size() < 4) {
									mRoomList = mnewRoomList;
									moreRoom.setVisibility(View.GONE);
								} else {
									for (int i = 0; i < 3; i++) {
										mRoomList.add(mnewRoomList.get(i));
									}
									moreRoom.setVisibility(View.VISIBLE);
								}
								if (mnewRoomList.size() <= 3) {
									moreRoom.setVisibility(View.INVISIBLE);
								} else {
									moreRoom.setVisibility(View.VISIBLE);
								}
								String text = "共 " + mnewRoomList.size()
										+ " 个 活 动 室";
								moreRoom.setText(MyApplication
										.getTextColor(text));
								mRoomAdapter.setList(mRoomList);
								mRoomAdapter.notifyDataSetChanged();
							} else {
								mVenueRoom.setVisibility(View.GONE);
							}
						}
					}
				});
	}

	/**
	 * 获取活动列表
	 */
	private void getActivityList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, venueInfo.getVenueId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		params.put(HttpUrlList.HTTP_PAGE_NUM, "10");
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.ACTIVITY_LIST_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是场馆活动室的数据"
								+ resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								mEventList = JsonUtil.getEventList(resultStr);
								fenge_detail_event.setVisibility(View.VISIBLE);
								
								if (mEventList.size() <= 3) {
									venue_more_activity
											.setVisibility(View.GONE);
								} else {
									venue_more_activity
											.setVisibility(View.VISIBLE);
									String text = "共 " + mEventList.get(0).getCount()
											+ " 个 活 动";
									venue_more_activity.setText(MyApplication
											.getTextColor(text));
								}

								mEventAdapter.setList(mEventList);
								mEventAdapter.notifyDataSetChanged();
								if (mEventList.size() == 0) {
									fenge_detail_event.setVisibility(View.GONE);
									mVenueEvent.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}
					}
				});
	}

	/**
	 * 设置场馆详情数据 根据获取的数据来显示
	 */
	private void setVenueData() {
		venueName.setText(venueInfo.getVenueName());
		phone.setText(venueInfo.getVenueMobile());
		time.setText(venueInfo.getVenueOpenTime() + "至"
				+ venueInfo.getVenuEndTime());
		StringBuffer Venuetime = new StringBuffer();
		Log.i("ceshi", "opentime==  " + venueInfo.getVenueOpenTime() + "  end ");
		if (venueInfo.getVenueOpenTime().equals(venueInfo.getVenueEndTime())) {
			Venuetime.append(venueInfo.getVenueOpenTime() + "\n");
		} else {
			Venuetime.append(venueInfo.getVenueOpenTime() + "-"
					+ venueInfo.getVenueEndTime() + "\n");
		}

		if (venueInfo.getOpenNotice() != null
				&& venueInfo.getOpenNotice().trim().length() > 0) {
			/**
			 * 这里是我注释的 3.5
			 */
			Venuetime.append(venueInfo.getOpenNotice());
		} else {
			memoLayout.setVisibility(View.VISIBLE);
		}
		String memo_text;
		// if (!venueInfo.getOpenNotice().toString().equals("")) {
		// memo_text = Venuetime.toString() + " "
		// + venueInfo.getOpenNotice().toString();
		// } else {
		// memo_text =Venuetime.toString() ;
		// }

		// Log.i("ceshi", "看看数据===  " + memo_text);
		memo.setText(Venuetime.toString());

		value.setText(venueInfo.getVenuePrice());
		// g
		address.setText(venueInfo.getVenueAddress());
		mRating.setRating(venueInfo.getVenueRating());

		if (venueInfo.getVenueComment() != null
				&& venueInfo.getVenueComment().trim().length() > 0) {
			nameContentName.setText(venueInfo.getVenuePersonName() + " : ");
			nameContent.setText(venueInfo.getVenueComment());
			mContentLayout.setVisibility(View.GONE);
			nameCommentLine.setVisibility(View.GONE);
		} else {
			mContentLayout.setVisibility(View.GONE);
			nameCommentLine.setVisibility(View.GONE);
		}

		/**
		 * 这是原先的收藏的地方 通过下面的代码进行显示是否
		 */
		// if (venueInfo.getVenueHasAntique()) {
		// collect_activity.setVisibility(View.VISIBLE);
		// } else {
		// collect_activity.setVisibility(View.GONE);
		// }
		/**
		 * 3.5的修改 来显示 藏品
		 */
		// if (venueInfo.getVenueHasAntique()) {
		// Collection_rl.setVisibility(View.VISIBLE);
		// Collecton_left.setVisibility(View.VISIBLE);
		// Collection_num.setVisibility(View.VISIBLE);
		// Collect_gridview.setVisibility(View.VISIBLE);
		// } else {
		// Collection_rl.setVisibility(View.GONE);
		// Collecton_left.setVisibility(View.GONE);
		// Collection_num.setVisibility(View.GONE);
		// Collect_gridview.setVisibility(View.GONE);
		// }
		if (venueInfo.getVenueHasMetro().equals("1")) {
			venue_item_metro.setVisibility(View.GONE);
		} else {
			venue_item_metro.setVisibility(View.VISIBLE);
		}
		if (venueInfo.getVenueHasBus().equals("1")) {
			venue_item_bus.setVisibility(View.GONE);
		} else {
			venue_item_bus.setVisibility(View.VISIBLE);
		}

		if (venueInfo.getVenueMemo().length() < 190) {
			detailMore.setVisibility(View.GONE);
		}
		mVenueProfileDetails.loadDataWithBaseURL(null, ViewUtil.initContent(
				ViewUtil.subString(venueInfo.getVenueMemo(), true, detailMore),
				this.mContext), AppConfigUtil.APP_MIMETYPE,
				AppConfigUtil.APP_ENCODING, null);
		// detailMore.setOnClickListener(this);
		// detailMore.setVisibility(View.GONE);

	}

	/**
	 * 设置标题
	 */
	private void initTitle() {
		// RelativeLayout mTitleLayout = (RelativeLayout)
		// findViewById(R.id.title);
		// ImageButton mRight = (ImageButton) mTitleLayout
		// .findViewById(R.id.title_right);
		// mRight.setImageDrawable(getResources().getDrawable(
		// R.drawable.sh_icon_title_share));
		// mRight.setOnClickListener(this);
		// TextView mTitle_tv = (TextView) mTitleLayout
		// .findViewById(R.id.title_content);
		// mTitle_tv.setText("场馆详情");
		// mTitle_tv.setVisibility(View.VISIBLE);
		// mTitleLayout.findViewById(R.id.title_left).setOnClickListener(this);
	}

	/**
	 * 设置viewpager数据
	 */
	private void setViewPager() {
		mViewPager.removeAllViews();
		mViewPager.removeAllViewsInLayout();
		mlist = new ArrayList<ImageView>();
		if (roomUrls.length == 1) {
			mViewPager.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		for (int i = 0; i < roomUrls.length; i++) {
			// 设置广告图
			ImageView imageView = new ImageView(mContext);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(TextUtil.getUrlMiddle(roomUrls[i]),
							imageView, Options.getListOptions());
			Log.d("imageView", "i" + i);
			mlist.add(imageView);
		}

		BannerAdapter mAdapter = new BannerAdapter(mlist);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());

		try {// 设置时间
			Field field = null;
			field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mViewPager.getContext(), new AccelerateInterpolator());
			field.set(mViewPager, scroller);
			scroller.setmDuration(500);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 根据展馆ID 来获取数据
	 */
	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put("venueId", venueId);

		MyApplication.total_num(HttpUrlList.Venue.WFF_CMSVUNUEAPPDETAIL,
				"cmsVenueAppDetail", venueId);
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.WFF_CMSVUNUEAPPDETAIL,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是场馆详情的数据"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							venueInfo = JsonUtil.getVenueDetailInfor(resultStr);
							if (venueInfo != null) {
								setData();
								if (venueInfo.getVenueIsCollect()) {
									mCollect.setImageResource(R.drawable.sh_icon_collect_after);
								}
								if (venueInfo.getVenueIsWant() == 0) {

								} else {
									activity_comment_zan_iv
											.setImageResource(R.drawable.wff_zan_after);
								}
								if (venueInfo.getVideoPalyList() != null
										&& venueInfo.getVideoPalyList().size() > 0) {
									mMyVideoView.initPlatyUrlList(venueInfo
											.getVideoPalyList());
								} else {
									videoLinearLayout.setVisibility(View.GONE);
								}
								mLoadingHandler.stopLoading();
							} else {
								mLoadingHandler.showErrorText("返回数据错误");
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
		if (!venueInfo.getTagName().equals("")) {
			tagname.setVisibility(View.VISIBLE);
			tagname.setText(venueInfo.getTagName().toString());
		}
		if (!venueInfo.getDictName().equals("")) {
			dictname.setVisibility(View.VISIBLE);
			dictname.setText(venueInfo.getDictName().toString());
		}
		if (venueInfo.getTagNameList().size() != 0) {
			subItem.setVisibility(View.VISIBLE);
			subItem.setText(venueInfo.getTagNameList().get(0));
		}
		if (!venueInfo.getVenueIsFree().equals("2")) {
			price_last.setVisibility(View.GONE);
			price.setText("免费");
		} else {
			price_last.setVisibility(View.GONE);
			price.setText("收费");
		}
		if (!venueInfo.getVenuePrice().equals("0")
				&& venueInfo.getVenuePrice() != null
				&& !venueInfo.getVenuePrice().equals("")) {
			beizhu_rl.setVisibility(View.VISIBLE);
			if (!venueInfo.getVenuePriceNotice().equals("")) {
				beizhu_right.setText(venueInfo.getVenuePrice() + "元/人" + "\n"
						+ venueInfo.getVenuePriceNotice());
			} else {
				beizhu_right.setText(venueInfo.getVenuePrice() + "元/人");
			}

		} else {
			beizhu_rl.setVisibility(View.GONE);
			beizhu_right.setVisibility(View.GONE);
		}
		if (venueInfo == null) {
			return;
		}
		if (venueInfo.getVenueIsReserve()) {
			reserve.setVisibility(View.VISIBLE);
		} else {
			reserve.setVisibility(View.GONE);
		}
		mRoomName.setText(venueInfo.getVenueName());
		Log.d("mRoomName:",
				venueInfo.getRoomNamesList() + "--"
						+ venueInfo.getRoomIconUrlList());
		roomNames = venueInfo.getRoomNamesList().split(",");
		roomUrls = venueInfo.getRoomIconUrlList().split(",");
		setViewPager();
		bannerListener = new BannerListener();
		mViewPager.setOnPageChangeListener(bannerListener);
		if (venueInfo.getVenueIsCollect()) {
			mCollect.setImageResource(R.drawable.sh_icon_collect_after);
			isChoice = true;
		} else {
			mCollect.setImageResource(R.drawable.sh_icon_collect_befor);
			isChoice = false;
		}
		getVenueInfo();
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
	 * 收藏
	 */
	private void onCollect() {
		if (isChoice) {
			cancelCollect();
		} else {
			addCollect();
		}

	}

	/**
	 * 添加收藏
	 */
	private void addCollect() {
		CollectUtil.addVenue(mContext, venueId, new CollectCallback() {

			@Override
			public void onPostExecute(boolean isOK) {
				// TODO Auto-generated method stub
				if (isOK) {
					mCollect.setImageResource(R.drawable.sh_icon_collect_after);
					ToastUtil.showToast("收藏成功");
					venueInfo.setVenueIsCollect(true);
					isChoice = !isChoice;
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
		CollectUtil.cancelVenue(mContext, venueId, new CollectCallback() {

			@Override
			public void onPostExecute(boolean isOK) {
				// TODO Auto-generated method stub
				if (isOK) {
					mCollect.setImageResource(R.drawable.sh_icon_collect_befor);
					ToastUtil.showToast("取消收藏");
					venueInfo.setVenueIsCollect(false);
					isChoice = !isChoice;
				} else {
					ToastUtil.showToast("操作失败");
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		if (!ButtonUtil.isDelayClick()) {
			return;
		}
		switch (arg0.getId()) {
		// 这是分享
		// case R.id.title_right:
		// if (venueInfo == null) {
		// return;
		// }
		// Intent intent = new Intent(mContext, UserDialogActivity.class);
		// FastBlur.getScreen((Activity) mContext);
		// ShareInfo info = new ShareInfo();
		// info.setTitle(venueInfo.getVenueName());
		// info.setImageUrl(venueInfo.getVenueIconUrl());
		// info.setContentUrl(venueInfo.getShareUrl());
		// info.setContent(ViewUtil.getTextFromHtml(venueInfo.getVenueMemo()
		// .toString()));
		// intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
		// intent.putExtra(DialogTypeUtil.DialogType,
		// DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
		// startActivity(intent);
		// break;
		case R.id.share:

			// menuWindow = new SelectPicPopupWindow(VenueDetailActivity.this,
			// itemsOnClick, AppConfigUtil.INTENT_SHAREINFO);
			// menuWindow.showAtLocation(
			// VenueDetailActivity.this.findViewById(R.id.share),
			// Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			if (venueInfo == null) {
				return;
			}
			intent = new Intent(mContext, UserDialogActivity.class);
			FastBlur.getScreen((Activity) mContext);
			ShareInfo info = new ShareInfo();
			info.setTitle(venueInfo.getVenueName());
			info.setImageUrl(venueInfo.getVenueIconUrl());
			MyApplication.shareLink = venueInfo.getShareUrl();
			info.setContentUrl(venueInfo.getShareUrl());
			info.setContent(ViewUtil.getTextFromHtml(venueInfo.getVenueMemo()
					.toString()));
			intent.putExtra(AppConfigUtil.INTENT_SHAREINFO, info);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE);
			startActivity(intent);
			break;
		/**
		 * 全部活动列表
		 */
		case R.id.venue_more_activity:
			intent = new Intent(mContext, VenueMoreActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("VenueId", venueInfo.getVenueId());
			intent.putExtras(bundle);
			mContext.startActivity(intent);
			break;
		case R.id.activity_comment_zan_iv:
			if (MyApplication.MyloginUserInfor.getUserIsDisable()==2) {
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
		// 收藏
		case R.id.collect:
			if (MyApplication.MyloginUserInfor.getUserIsDisable()==2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			onCollect();
			break;
		case R.id.venue_top:
			finish();
			// mTopView.setVisibility(View.GONE);
			// scrollview.smoothScrollTo(0, 0);
			break;
		case R.id.title_left:
			finish();
			break;
		case R.id.back_iv:
			finish();
			break;
		// case R.id.commit_comment:
		// if (MyApplication.UserIsLogin) {
		// intent = new Intent(mContext, WriteCommentActivity.class);
		// intent.putExtra("Id", venueId);
		// intent.putExtra("type", venueType);
		// startActivityForResult(intent, HttpCode.BACK);
		//
		// } else {
		// intent = new Intent(mContext, UserDialogActivity.class);
		// FastBlur.getScreen((Activity) mContext);
		// intent.putExtra(
		// DialogTypeUtil.DialogType,
		// DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
		// startActivity(intent);
		// }
		//
		// break;
		case R.id.venue_more_room:
			intent = new Intent(mContext, ActivityRoomActivity.class);
			intent.putExtra("venueId", venueId);
			intent.putExtra("venueName", venueInfo.getVenueName());
			startActivity(intent);
			break;
		/**
		 * 3.5 评论
		 */
		case R.id.activity_comment_tv:
			if (MyApplication.MyloginUserInfor.getUserIsDisable()==2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			if (MyApplication.UserIsLogin) {
				intent = new Intent(mContext, WriteCommentActivity.class);
				/**
				 * 这里是场所的评论 需要传ID ID 还未知 不传会报空
				 */
				intent.putExtra("Id", venueInfo.getVenueId());
				intent.putExtra("type", "1");
				this.startActivityForResult(intent, HttpCode.BACK);
			} else {
				FastBlur.getScreen((Activity) mContext);
				intent = new Intent(mContext, UserDialogActivity.class);
				intent.putExtra(
						DialogTypeUtil.DialogType,
						DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
				mContext.startActivity(intent);
			}

			break;
		// 3.5 收藏
		case R.id.venue_collect:
			if (MyApplication.MyloginUserInfor.getUserIsDisable()==2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			onCollect();
			break;
		// case R.id.venue_profile_details_more:
		// if (isUp) {
		// LinearLayout.LayoutParams lParams = (LayoutParams)
		// mVenueProfileDetails
		// .getLayoutParams();
		// lParams.height = lParams.MATCH_PARENT;
		// mVenueProfileDetails.setLayoutParams(lParams);
		// detailMore.setImageResource(R.drawable.arrow_up);
		// } else {
		// LinearLayout.LayoutParams lParams = (LayoutParams)
		// mVenueProfileDetails
		// .getLayoutParams();
		// lParams.height = 200;
		// mVenueProfileDetails.setLayoutParams(lParams);
		// detailMore.setImageResource(R.drawable.arrow_down);
		// }
		// isUp = !isUp;
		// // if (isUp) {// 缩
		// // mVenueProfileDetails.loadDataWithBaseURL(null, ViewUtil
		// // .initContent(ViewUtil.subString(
		// // venueInfo.getVenueMemo(), false, detailMore),
		// // this.mContext), AppConfigUtil.APP_MIMETYPE,
		// // AppConfigUtil.APP_ENCODING, null);
		// // detailMore
		// // .setImageResource(R.drawable.sh_icon_venue_profile_down);
		// // } else {// 放
		// // mVenueProfileDetails.loadDataWithBaseURL(null, ViewUtil
		// // .initContent(ViewUtil.subString(
		// // venueInfo.getVenueMemo(), true, detailMore),
		// // this.mContext), AppConfigUtil.APP_MIMETYPE,
		// // AppConfigUtil.APP_ENCODING, null);
		// // detailMore
		// // .setImageResource(R.drawable.sh_icon_venue_profile_up);
		// // }
		// // isUp = !isUp;
		// break;
		case R.id.venue_profile:
			if (MyApplication.MyloginUserInfor.getUserIsDisable()==2) {
				ToastUtil.showToast("你的账号已经冻结");
				return;
			}
			intent = new Intent(this.mContext, MessageDialog.class);
			FastBlur.getScreen((Activity) mContext);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			intent.putExtra(DialogTypeUtil.DialogContent,
					venueInfo.getVenueMobile());
			startActivity(intent);
			break;
		case R.id.venue_profile_telephone:// 3.5 咨询
			intent = new Intent(this.mContext, MessageDialog.class);
			FastBlur.getScreen((Activity) mContext);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			intent.putExtra(DialogTypeUtil.DialogContent,
					venueInfo.getVenueMobile());
			startActivity(intent);
			break;
		case R.id.venue_profile_parking:// 更多车位
			if (venueInfo.getVenueLat() != null
					&& venueInfo.getVenueLon() != null
					&& venueInfo.getVenueLat().length() > 0
					&& venueInfo.getVenueLon().length() > 0) {
				intent = new Intent(mContext, NearbyParkingActivity.class);
				intent.putExtra("titleContent", venueInfo.getVenueName());
				intent.putExtra("lat", venueInfo.getVenueLat());
				intent.putExtra("lon", venueInfo.getVenueLon());
				startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;
		case R.id.Collection_rl:// 更多藏品
			intent = new Intent(mContext, CollectionListActivity.class);
			intent.putExtra("Id", venueId);
			intent.putExtra("title", venueInfo.getVenueName());
			startActivity(intent);
			break;
		case R.id.venue_profile_activity:// 场馆地址
			if (venueInfo.getVenueLat() != null
					&& venueInfo.getVenueLat().length() > 0
					&& !venueInfo.getVenueLat().equals("0")
					&& venueInfo.getVenueLon() != null
					&& venueInfo.getVenueLon().length() > 0
					&& !venueInfo.getVenueLon().equals("0")) {
				intent = new Intent(mContext, BigMapViewActivity.class);
				intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
				intent.putExtra("titleContent", venueInfo.getVenueName());
				// 传入场馆的位置
				intent.putExtra("lat", venueInfo.getVenueLat());
				intent.putExtra("lon", venueInfo.getVenueLon());
				intent.putExtra("address", venueInfo.getVenueAddress());
				startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;

		default:
			break;
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

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg1) {
		case HttpCode.BACK:// 通知评论列表重新获取数据
			if (commentList != null) {
				commentList.clear();
			}
			page = 0;
			getCommtentList();
			break;
		default:
			break;
		}
	}

	// 实现VierPager监听器接口
	class BannerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			int newPosition = position % roomNames.length;
			mRoomName.setText(roomNames[newPosition]);
		}

	}

	@Override
	protected void onDestroy() {
		// 关闭定时器
		isStop = true;
		super.onDestroy();
		mMyVideoView.onDestroy();
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mMyVideoView.onStop();
		if (mVenueDetailMusicHandler != null) {
			mVenueDetailMusicHandler.stopMusic();
		}
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
			mTopView.setVisibility(View.GONE);
		}
		Log.d("onScrolled", "scrollX:" + scrollX + "--scrollY:" + scrollY
				+ "---clampedX:" + clampedX + "---clampedY:" + clampedY);
	}

	/**
	 * 3.5 添加的点赞评论数的代码
	 */
	public void isWant(boolean isWant) {
		this.isWant = isWant;
		if (isWant) {
			mIsWant.setText("取消");
		} else {
			mIsWant.setText("点赞");
		}
	}

	public void getCommtentData() {
		if (myCommentList != null) {
			myCommentList.clear();
		}
		getCommtentList();
	}

	/**
	 * 获取评论数据
	 */
	private void getCommtentList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("moldId", venueId);
		params.put("type", "1");
		params.put("pageIndex", "0");
		params.put("pageNum", "30");

		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommentList,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							myCommentList = JsonUtil
									.getCommentInforFromString(resultStr);
							if (myCommentList.size() > 0) {
								mCommentAdapter.setList(myCommentList);
								mCommentAdapter.notifyDataSetChanged();
								if (myCommentList.size() == 10) {
									tvMoreComment.setVisibility(View.GONE);
								}
								comment_num.setVisibility(View.VISIBLE);
								fenge_comment.setVisibility(View.VISIBLE);
								String text = "共" + myCommentList.size()
										+ " 条 评 论";
								comment_num.setText(MyApplication
										.getTextColor(text));

							} else {
								comment_num.setVisibility(View.GONE);
								tvMoreComment.setVisibility(View.GONE);
							}

						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	/**
	 * 浏览数
	 */

	private void getBrowseCount() {
		//
		Map<String, String> params = new HashMap<String, String>();
		params.put("venueId", venueId);
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.EventUrl.WFF_APPCMSVENUEBROWSECOUNT, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							try {
								JSONObject json = new JSONObject(resultStr);
								int data = json.getInt("data");
								if (listUsers.size() != 0 && listUsers != null) {
									zanguo_rl.setVisibility(View.VISIBLE);
									fenge_zan.setVisibility(View.VISIBLE);
									zanguo_tv.setVisibility(View.VISIBLE);
									String text = null;
									if (data > 50) {
										text = "共 " + UserPersionSInfo.totalNum
												+ " 人 赞 过 " + "," + data
												+ " 人 浏 览 过";
									} else {
										text = "共 " + UserPersionSInfo.totalNum
												+ " 人 赞 过 ";
									}
									zanguo_tv.setText(MyApplication
											.getTextColor(text));
								} else {
									fenge_zan.setVisibility(View.GONE);
									zanguo_rl.setVisibility(View.GONE);
									zanguo_tv.setVisibility(View.GONE);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {
							zanguo_rl.setVisibility(View.GONE);
							fenge_zan.setVisibility(View.GONE);
							zanguo_tv.setVisibility(View.GONE);
							ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	/**
	 * 获取报名用户 也就是点赞数
	 */
	private void getJoinUsers() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("venueId", venueId);
		params.put("pageIndex", "0");
		params.put("pageNum", "30");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.EventUrl.WFF_APPVUNUEUSERWANTGOLIST, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							listUsers = JsonUtil.getJoinUserList(resultStr);
							mGridViewAdapter.setUserList(listUsers);
							mGridViewAdapter.notifyDataSetChanged();
						} else {
							ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	/**
	 * 我想报名
	 */
	private void goToJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("venueId", venueId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.EventUrl.WFF_APPADDVENUEUSERWANTGO, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							// if (code ==
							// HttpCode.serverCode.DATA_Success_CODE) {
							ToastUtil.showToast("点赞成功");
							activity_comment_zan_iv
									.setImageResource(R.drawable.wff_zan_after);
							isWant = !isWant;

							getUserData();
							// } else {
							// ToastUtil.showToast("已赞");
							// }

						} else {
							ToastUtil.showToast(resultStr);
						}

					}

				});
	}

	protected void getUserData() {
		if (listUsers != null) {
			listUsers.clear();
		}
		getJoinUsers();
		getBrowseCount();
	}

	private void quxiaoJoin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("venueId", venueId);
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		//
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.EventUrl.WFF_APPDELETEVENUEUSERWANTGO, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == 0) {
								activity_comment_zan_iv
										.setImageResource(R.drawable.wff_zan);
								isWant = !isWant;
								getUserData();
							}
						}
					}
				});
	}

	class GridViewAdapter extends BaseAdapter {
		private List<UserPersionSInfo> listUsers;

		public GridViewAdapter(List<UserPersionSInfo> listUsers) {
			// TODO Auto-generated constructor stub
			this.listUsers = listUsers;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.listUsers.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return this.listUsers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public void setUserList(List<UserPersionSInfo> listUsers) {
			this.listUsers = listUsers;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder mHolder = null;
			if (arg1 == null) {
				mHolder = new ViewHolder();
				arg1 = View.inflate(mContext, R.layout.likeitemlayout, null);
				mHolder.show = (TextView) arg1.findViewById(R.id.text);
				mHolder.img_url = (ImageView) arg1.findViewById(R.id.img);
				arg1.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) arg1.getTag();
			}
			UserPersionSInfo info = this.listUsers.get(arg0);
			if ("女".equals(info.getSex())) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								info.getHeadUrl(),
								mHolder.img_url,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_woman, 5));
			} else {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								info.getHeadUrl(),
								mHolder.img_url,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_man, 5));
			}
			if (arg0 == 23) {
				mHolder.show.setVisibility(View.VISIBLE);
				mHolder.img_url.setVisibility(View.GONE);
			} else {
				mHolder.show.setVisibility(View.GONE);
				mHolder.img_url.setVisibility(View.VISIBLE);
			}

			return arg1;
		}

		class ViewHolder {
			private TextView show;
			private ImageView img_url;

		}
	}
}
