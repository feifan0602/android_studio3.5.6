package com.sun3d.culturalShanghai.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.ViewUtil;
import com.sun3d.culturalShanghai.activity.ActivityRoomActivity;
import com.sun3d.culturalShanghai.activity.ActivityRoomDateilsActivity;
import com.sun3d.culturalShanghai.activity.BigMapViewActivity;
import com.sun3d.culturalShanghai.activity.CollectionListActivity;
import com.sun3d.culturalShanghai.activity.EventDetailsActivity;
import com.sun3d.culturalShanghai.activity.NearbyParkingActivity;
import com.sun3d.culturalShanghai.adapter.ActivityRoomListAdapter;
import com.sun3d.culturalShanghai.adapter.RelatedActivityAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.VenueDetailMusicHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.view.FastBlur;

public class VenueDetailFragment extends Fragment implements OnClickListener {
	private View view;
	private WebView mVenueProfileDetails;
	private ImageButton detailMore;
	private boolean isUp = false;
	private VenueDetailInfor mVenueDetailInfor;
	private String venueId;
	private ListView mRoomListView;
	private ListView mActivityListView;
	private List<ActivityRoomInfo> mRoomList;
	private ActivityRoomListAdapter mRoomAdapter;
	private RelatedActivityAdapter mEventAdapter;
	private List<EventInfo> mEventList;
	private TextView venueName;
	private ImageView venue_item_bus;
	private ImageView venue_item_metro;
	private TextView phone;
	private TextView time;
	private TextView memo;
	private TextView address;
	private TextView value;
	private RatingBar mRating;
	private RelativeLayout collect_activity;
	private TextView nameContent;
	private LinearLayout mVenueEvent;
	private Context mContext;
	private LinearLayout mVenueRoom;
	private RelativeLayout memoLayout;
	private TextView moreRoom;
	private VenueDetailMusicHandler mVenueDetailMusicHandler;
	private ImageView reserve;
	private View nameCommentLine;
	private TextView nameContentName;
	private LinearLayout mContentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_venue_details, null);

		return view;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		LinearLayout musicLyayout = (LinearLayout) view.findViewById(R.id.venue_muisc_layout);
		mVenueDetailMusicHandler = new VenueDetailMusicHandler(musicLyayout);

		mVenueProfileDetails = (WebView) view.findViewById(R.id.venue_profile_details);
		detailMore = (ImageButton) view.findViewById(R.id.venue_profile_details_more);
		ViewUtil.setWebViewSettings(mVenueProfileDetails, mContext);
		onWindowFocusChanged(true);
		mRoomListView = (ListView) view.findViewById(R.id.venue_room_list);
		mActivityListView = (ListView) view.findViewById(R.id.venue_activity_list);
		mRoomListView.setFocusable(false);
		mActivityListView.setFocusable(false);
		mVenueProfileDetails.setFocusable(false);
		nameContent = (TextView) view.findViewById(R.id.venue_content);
		nameContentName = (TextView) view.findViewById(R.id.venue_content_name);
		nameCommentLine = view.findViewById(R.id.venue_comment_lien);
		venueName = (TextView) view.findViewById(R.id.venue_name);
		mRating = (RatingBar) view.findViewById(R.id.venue_ratingbar);
		venue_item_bus = (ImageView) view.findViewById(R.id.venue_item_bus);
		venue_item_metro = (ImageView) view.findViewById(R.id.venue_item_metro);
		phone = (TextView) view.findViewById(R.id.venue_profile_telephone_text);
		time = (TextView) view.findViewById(R.id.venue_profile_data_text);
		memo = (TextView) view.findViewById(R.id.venue_profile_data_text_show);
		memoLayout = (RelativeLayout) view.findViewById(R.id.venue_profile_timemakert);
		address = (TextView) view.findViewById(R.id.venue_profile_activity_text);
		value = (TextView) view.findViewById(R.id.event_text_money);
		collect_activity = (RelativeLayout) view.findViewById(R.id.collect_activity);
		mVenueEvent = (LinearLayout) view.findViewById(R.id.venue_details_event);
		mVenueRoom = (LinearLayout) view.findViewById(R.id.venue_detail_room);
		moreRoom = (TextView) view.findViewById(R.id.venue_more_room);
		reserve = (ImageView) view.findViewById(R.id.venue_item_reserve);
		mContentLayout = (LinearLayout) view.findViewById(R.id.venue_content_layout);
	}

	private void initData() {
		view.findViewById(R.id.venue_profile_telephone).setOnClickListener(this);// 咨询
		view.findViewById(R.id.venue_profile_parking).setOnClickListener(this);// 更多车位
		view.findViewById(R.id.collect_activity).setOnClickListener(this);// 更多藏品
		view.findViewById(R.id.venue_profile_activity).setOnClickListener(this);// 场馆位置
		venueId = getActivity().getIntent().getExtras().getString("venueId");
		moreRoom.setOnClickListener(this);
		detailMore.setOnClickListener(this);
		// 活动室列表
		mRoomList = new ArrayList<ActivityRoomInfo>();
		mRoomAdapter = new ActivityRoomListAdapter(getActivity(), mRoomList);
		mRoomListView.setAdapter(mRoomAdapter);
		// 活动列表
		mEventList = new ArrayList<EventInfo>();
		mEventAdapter = new RelatedActivityAdapter(getActivity(), mEventList, false);
		mActivityListView.setAdapter(mEventAdapter);

		mRoomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ActivityRoomDateilsActivity.class);
				intent.putExtra("Id", mRoomList.get(arg2).getRoomId());
				startActivity(intent);
			}
		});
		mActivityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
				intent.putExtra("eventId", mEventList.get(arg2).getEventId());
				startActivity(intent);
			}
		});

	}

	/**
	 * 获取场馆数据
	 */
	public void getVenueInfo(VenueDetailInfor mVenueDetailInfor) {
		this.mVenueDetailInfor = mVenueDetailInfor;
		mVenueDetailMusicHandler.setMusicUrl(mVenueDetailInfor.getVenueVoiceUrl());
		setData();
		getRoomList();
		getActivityList();

	}

	/**
	 * 获取活动室列表
	 */
	private void getRoomList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, mVenueDetailInfor.getVenueId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		params.put(HttpUrlList.HTTP_PAGE_NUM, "5");
		MyHttpRequest.onHttpPostParams(HttpUrlList.ActivityRoomUrl.ROOM_LIST_URL, params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<ActivityRoomInfo> mnewRoomList = JsonUtil.getRoomList(resultStr);
							if (mnewRoomList.size() > 0) {
								mRoomList.clear();
								reserve.setVisibility(View.VISIBLE);
								if (mnewRoomList.size() < 4) {
									mRoomList = mnewRoomList;
									moreRoom.setVisibility(View.GONE);
								} else {
									for (int i = 0; i < 3; i++) {
										mRoomList.add(mnewRoomList.get(i));
									}
									moreRoom.setVisibility(View.VISIBLE);
								}
								mRoomAdapter.setList(mRoomList);
								mRoomAdapter.notifyDataSetChanged();
							} else {
								reserve.setVisibility(View.GONE);
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
		params.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, mVenueDetailInfor.getVenueId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		params.put(HttpUrlList.HTTP_PAGE_NUM, "10");
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.ACTIVITY_LIST_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								mEventList = JsonUtil.getEventList(resultStr);
								mEventAdapter.setList(mEventList);
								mEventAdapter.notifyDataSetChanged();
								if (mEventList.size() == 0) {
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
	 * 设置场馆数据
	 */
	private void setData() {
		if (mVenueDetailInfor == null) {
			return;
		}
		venueName.setText(mVenueDetailInfor.getVenueName());
		phone.setText(mVenueDetailInfor.getVenueMobile());
		time.setText(mVenueDetailInfor.getVenueOpenTime() + "至"
				+ mVenueDetailInfor.getVenuEndTime());
		if (mVenueDetailInfor.getOpenNotice() != null
				&& mVenueDetailInfor.getOpenNotice().trim().length() > 0) {
			memo.setText(mVenueDetailInfor.getOpenNotice());
		} else {
			memoLayout.setVisibility(View.GONE);
		}
		value.setText(mVenueDetailInfor.getVenuePrice());

		address.setText(mVenueDetailInfor.getVenueAddress());
		mRating.setRating(mVenueDetailInfor.getVenueRating());

		if (mVenueDetailInfor.getVenueComment() != null
				&& mVenueDetailInfor.getVenueComment().trim().length() > 0) {
			nameContentName.setText(mVenueDetailInfor.getVenuePersonName() + " : ");
			nameContent.setText(mVenueDetailInfor.getVenueComment());
			mContentLayout.setVisibility(View.VISIBLE);
			nameCommentLine.setVisibility(View.VISIBLE);
		} else {
			mContentLayout.setVisibility(View.GONE);
			nameCommentLine.setVisibility(View.GONE);
		}
		if (mVenueDetailInfor.getVenueHasAntique()) {
			collect_activity.setVisibility(View.VISIBLE);
		} else {
			collect_activity.setVisibility(View.GONE);
		}
		if (mVenueDetailInfor.getVenueHasMetro().equals("1")) {
			venue_item_metro.setVisibility(View.GONE);
		} else {
			venue_item_metro.setVisibility(View.VISIBLE);
		}
		if (mVenueDetailInfor.getVenueHasBus().equals("1")) {
			venue_item_bus.setVisibility(View.GONE);
		} else {
			venue_item_bus.setVisibility(View.VISIBLE);
		}

		mVenueProfileDetails.loadDataWithBaseURL(null, ViewUtil.initContent(
				ViewUtil.subString(mVenueDetailInfor.getVenueMemo(), false, detailMore),
				this.mContext), AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING, null);

	}

	/**
	 * webview显示部分与全部 boo:true为截取内容（内容小于100字符将全部显示），false为全部显示
	 * 
	 * */
	public String calculate(String content, boolean boo) {
		Log.i("newHtmlContents", content);
		content = content.replaceAll("<a([^>]{0,})>", "").trim();// 去除所有的a标签
		// content = content.replaceAll("<p([^>]{0,})>", "").trim();// 去除所有的p标签
		int startString = 120; // 英文取120字，中文取一半
		for (int i = 0; i < content.length(); i++) {
			if (isChineseChar(content.charAt(i))) {
				startString = (startString / 2) + i;
				startString = startString > content.length() ? content.length() : startString;
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
	 * @Title: isChineseChar
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param c
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.venue_profile_details_more:
			if (isUp) {// 缩
				mVenueProfileDetails.loadDataWithBaseURL(null, ViewUtil.initContent(
						ViewUtil.subString(mVenueDetailInfor.getVenueMemo(), false, detailMore),
						this.mContext), AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING,
						null);
				detailMore.setImageResource(R.drawable.sh_icon_venue_profile_down);
			} else {// 放
				mVenueProfileDetails.loadDataWithBaseURL(null, ViewUtil.initContent(
						ViewUtil.subString(mVenueDetailInfor.getVenueMemo(), true, detailMore),
						this.mContext), AppConfigUtil.APP_MIMETYPE, AppConfigUtil.APP_ENCODING,
						null);
				detailMore.setImageResource(R.drawable.sh_icon_venue_profile_up);
			}
			isUp = !isUp;
			break;
		case R.id.venue_more_room:
			intent = new Intent(getActivity(), ActivityRoomActivity.class);
			intent.putExtra("venueId", venueId);
			intent.putExtra("venueName", mVenueDetailInfor.getVenueName());
			startActivity(intent);
			break;
		case R.id.venue_profile_telephone:// 咨询
			intent = new Intent(getActivity(), MessageDialog.class);
			FastBlur.getScreen((Activity) getActivity());
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.MessageDialog.MSGTYPE_TELLPHONE);
			intent.putExtra(DialogTypeUtil.DialogContent, mVenueDetailInfor.getVenueMobile());
			startActivity(intent);
			break;
		case R.id.venue_profile_parking:// 更多车位
			if (mVenueDetailInfor.getVenueLat() != null && mVenueDetailInfor.getVenueLon() != null
					&& mVenueDetailInfor.getVenueLat().length() > 0
					&& mVenueDetailInfor.getVenueLon().length() > 0) {
				intent = new Intent(getActivity(), NearbyParkingActivity.class);
				intent.putExtra("titleContent", mVenueDetailInfor.getVenueName());
				intent.putExtra("lat", mVenueDetailInfor.getVenueLat());
				intent.putExtra("lon", mVenueDetailInfor.getVenueLon());
				startActivity(intent);
			} else {
				ToastUtil.showToast("没有相关位置信息");
			}
			break;
		case R.id.collect_activity:// 更多藏品
			intent = new Intent(getActivity(), CollectionListActivity.class);
			intent.putExtra("Id", venueId);
			intent.putExtra("title", mVenueDetailInfor.getVenueName());
			startActivity(intent);
			break;
		case R.id.venue_profile_activity:// 场馆地址
			if (mVenueDetailInfor.getVenueLat() != null
					&& mVenueDetailInfor.getVenueLat().length() > 0
					&& !mVenueDetailInfor.getVenueLat().equals("0")
					&& mVenueDetailInfor.getVenueLon() != null
					&& mVenueDetailInfor.getVenueLon().length() > 0
					&& !mVenueDetailInfor.getVenueLon().equals("0")) {
				intent = new Intent(mContext, BigMapViewActivity.class);
				intent.putExtra(AppConfigUtil.INTENT_TYPE, "2");
				intent.putExtra("titleContent", mVenueDetailInfor.getVenueName());
				// 传入场馆的位置
				intent.putExtra("lat", mVenueDetailInfor.getVenueLat());
				intent.putExtra("lon", mVenueDetailInfor.getVenueLon());
				intent.putExtra("address", mVenueDetailInfor.getVenueAddress());
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
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mVenueDetailMusicHandler != null) {
			mVenueDetailMusicHandler.stopMusic();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mVenueDetailMusicHandler != null) {
			mVenueDetailMusicHandler.pause();
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			// LayoutParams lp = mVenueProfileDetails.getLayoutParams();
			// lp.height = AppConfigUtil.Page.PageDefaHight;
			// mVenueProfileDetails.setLayoutParams(lp);

		}
	}
}
