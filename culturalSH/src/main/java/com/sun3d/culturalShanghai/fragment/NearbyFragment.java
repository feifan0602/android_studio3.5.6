package com.sun3d.culturalShanghai.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityMap;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity.OnLogin_Status;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.EventReserveActivity;
import com.sun3d.culturalShanghai.activity.MyLoveActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.adapter.ActivityListAdapter;
import com.sun3d.culturalShanghai.adapter.HomeCenterPopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAreaAdapter;
import com.sun3d.culturalShanghai.adapter.IndexHorListAdapter;
import com.sun3d.culturalShanghai.adapter.NearbyListAdapter;
import com.sun3d.culturalShanghai.adapter.WeekItemAdapter;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback;
import com.sun3d.culturalShanghai.handler.ActivityWffTabHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.windows.EventWindows;
import com.sun3d.culturalShanghai.windows.WeekPopupWindow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个是活动的fragment
 * 
 * @author liningkang
 */
@SuppressLint("ValidFragment")
public class NearbyFragment extends Fragment implements OnClickListener,
		RefreshListenter, OnLogin_Status, AMap.OnMarkerClickListener,
		AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter,
		AMap.OnMapClickListener, ImageLoadingListener {
	private boolean map_fg_change = true;
	private RelativeLayout map_rl;
	private Context mContext;
	private EventInfo eventInfo;
	private List<EventInfo> EventlistItem;
	private MapView mMapView; // 小地图，大地图
	private RelativeLayout titleLayout;
	private AMap aMap;
	private MarkerOptions markerOption;
	// private GaoDeMapPoiSeahUtil mGaoDeMapPoiSeahUtil;
	private int ListType = 1;// 1为活动，2为展馆。
	private final String mPageName = "ActivityMap";
	private RelativeLayout mItem;
	private ImageView mItemImg;
	private TextView mItemTitle;
	private TextView mItemAddress;
	private TextView mItemData;
	private TextView mItemDistance;
	private TextView mItemPrice;
	private TextView mItemTicket;
	private TextView map_book;
	private TextView tv_map_location;
	private LoadingDialog mLoadingDialog;
	Drawable drawable;
	private WeekPopupWindow mWeekPopupWindow = new WeekPopupWindow();
	private Animation mShowAnimation;
	private Animation mHiddenAnimation;
	private List<UserBehaviorInfo> mflagList;
	private LatLng latlng;
	private double lat;
	private double lon;
	private RelativeLayout mTitle;
	private boolean addmore_bool = true;
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private PullToRefreshListView mListView;
	private List<EventInfo> mList;
	private RelativeLayout mtitl;
	private ImageView right_iv;
	private TextView left_tv;
	private TextView middle_tv;
	private TextView collection_null;
	private NearbyListAdapter mListAdapter;
	/**
	 * 这个是要传入到服务器的参数
	 */
	private Map<String, String> mParams;
	/**
	 * 刷新用的参数 0 为第一页
	 */
	private int mPage = 0;
	private Boolean isRefresh = false;
	private Boolean isListViewRefresh = true;
	private String activityType = "3";// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
	private View Topview;
	private Boolean isFirstResh = true;
	private LinearLayout tab_layout_view;
	private Boolean isBannerRefresh = true;
	private String Activity_typeId = "cf719729422c497aa92abdd47acdaa56";
	private Boolean isAddTabData = true;
	public LinearLayout nearby_ll;
	/**
	 * 3.5.2 筛选
	 */
	public PopupWindow pw;
	/**
	 * 智能排序 1-智能排序 2-热门排序 3-最新上线 4-即将结束
	 */
	private int sortType = 5;
	/**
	 * 1-免费 2-收费
	 */
	private String activityIsFree = "";
	/**
	 * 是否预定 1-不可预定 2-可预定
	 */
	private String activityIsReservation = "";
	private HomePopuWindowAdapter hpwa;
	private Wff_VenuePopuwindow wvpw;
	private ArrayList<Wff_VenuePopuwindow> list_area_z;
	private View itemView;
	private int mBgColor;
	private TextView preface, sieve;
	private boolean scroll_status = true;
	private String venueArea = "";
	private String venueDicName = "";
	private String venueMood = "";
	private String venueMoodName = "";
	private boolean animation_close = false;
	private boolean animation_change = true;
	private Handler mHnadler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1001) {
				UserBehaviorInfo info = (UserBehaviorInfo) msg.obj;
				mActivityTabHandler.setPosition();
				Activity_typeId = info.getTagId();
				onResh(info.getTagId());
			} else {
				onLoadingRefresh();
			}
		}
	};
	private String tagId;
	public static List<UserBehaviorInfo> selectTypeList;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainFragmentActivity"); // 统计页面
		if (mMapView != null)
			mMapView.onResume();
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainFragmentActivity");
		if (mMapView != null)
			mMapView.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (mMapView != null)
			mMapView.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.nearby_fragment_event, null);
		mContext = getActivity();
		// 这是地图
		initViewMap(view);
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
		}

		mLoadingDialog = new LoadingDialog(mContext);
		initMap();
		EventlistItem = new ArrayList<EventInfo>();
		mflagList = new ArrayList<UserBehaviorInfo>();
		mflagList.clear();
		getTypeData();
		// 这是原先的布局
		MyApplication.getInstance().setActivityHandler(mHnadler);
		nearby_ll = (LinearLayout) view.findViewById(R.id.nearby_ll);
		preface = (TextView) view.findViewById(R.id.preface);
		sieve = (TextView) view.findViewById(R.id.sieve);
		preface.setTypeface(MyApplication.GetTypeFace());
		sieve.setTypeface(MyApplication.GetTypeFace());
		preface.setOnClickListener(this);
		sieve.setOnClickListener(this);

		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		Topview = (View) view.findViewById(R.id.activity_page_line);
		mtitl = (RelativeLayout) view.findViewById(R.id.title);
		view.findViewById(R.id.activity_map_tv).setOnClickListener(this);
		view.findViewById(R.id.activity_week_tv).setOnClickListener(this);
		view.findViewById(R.id.activity_add_type).setOnClickListener(this);
		collection_null = (TextView) view.findViewById(R.id.collection_null);
		left_tv = (TextView) view.findViewById(R.id.left_tv);
		middle_tv = (TextView) view.findViewById(R.id.middle_tv);
		right_iv = (ImageView) view.findViewById(R.id.right_iv);
		right_iv.setImageResource(R.drawable.icon_maptop);
		right_iv.setOnClickListener(this);
		middle_tv.setText("附近");
		left_tv.setText(" ");
		tab_layout_view = (LinearLayout) view
				.findViewById(R.id.activity_tablayout_activity_layout);
		mListView = (PullToRefreshListView) view.findViewById(R.id.main_list);
		mCount = (TextView) view.findViewById(R.id.tab_count);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullDownToRefresh");
				onResh(Activity_typeId);
				getTip();
				getCount();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullUpToRefresh");
				// if (isListViewRefresh) {
				// onResh(Activity_typeId);
				// isListViewRefresh = false;
				// } else {
				onAddmoreData();
				// }
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				if (arg1 == SCROLL_STATE_IDLE) {
					// nearby_ll.setVisibility(View.VISIBLE);
					// 停止滑动
					scroll_status = true;
				} else if (arg1 == SCROLL_STATE_TOUCH_SCROLL) {
					// nearby_ll.setVisibility(View.GONE);
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					nearby_ll.setVisibility(View.VISIBLE);
					// 手指还在上面滑动
					scroll_status = false;
				} else if (arg1 == SCROLL_STATE_FLING) {
					// nearby_ll.setVisibility(View.GONE);
					if (pw != null && pw.isShowing()) {
						pw.dismiss();
					}
					nearby_ll.setVisibility(View.VISIBLE);
					// 正在滑动
					scroll_status = false;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int firstVisibleItem,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				// if (mAddList != null && mAddList.size() != 0
				// && mAddList.get(0).getIsContainActivtiyAdv() == 1
				// && animation_close) {
				// int heigth = (int) (((MyApplication.getWindowHeight()) / 3) *
				// 1.1);
				// if (firstVisibleItem >= 1) {
				// if (animation_change == false) {
				// ValueAnimator animator = ValueAnimator.ofInt(
				// heigth, 30);
				// animator.setDuration(500);
				// animator.addUpdateListener(new
				// ValueAnimator.AnimatorUpdateListener() {
				// @Override
				// public void onAnimationUpdate(
				// ValueAnimator valueAnimator) {
				// android.widget.FrameLayout.LayoutParams ll =
				// (android.widget.FrameLayout.LayoutParams) nearby_ll
				// .getLayoutParams();
				// int margin_height = (Integer) valueAnimator
				// .getAnimatedValue();
				// ll.setMargins(0, margin_height, 0, 0);
				// nearby_ll.setLayoutParams(ll);
				//
				// }
				// });
				// animator.addListener(new Animator.AnimatorListener() {
				// @Override
				// public void onAnimationStart(Animator animator) {
				// animation_change = true;
				// }
				//
				// @Override
				// public void onAnimationEnd(Animator animator) {
				// }
				//
				// @Override
				// public void onAnimationCancel(Animator animator) {
				// }
				//
				// @Override
				// public void onAnimationRepeat(Animator animator) {
				//
				// }
				// });
				// animator.start();
				// }
				// } else if (firstVisibleItem < 1 && animation_change) {
				// ValueAnimator animator = ValueAnimator
				// .ofInt(30, heigth);
				// animator.setDuration(500);
				// animator.addUpdateListener(new
				// ValueAnimator.AnimatorUpdateListener() {
				// @Override
				// public void onAnimationUpdate(
				// ValueAnimator valueAnimator) {
				// android.widget.FrameLayout.LayoutParams ll =
				// (android.widget.FrameLayout.LayoutParams) nearby_ll
				// .getLayoutParams();
				// int margin_height = (Integer) valueAnimator
				// .getAnimatedValue();
				// ll.setMargins(0, margin_height, 0, 0);
				// nearby_ll.setLayoutParams(ll);
				//
				// }
				// });
				// animator.addListener(new Animator.AnimatorListener() {
				// @Override
				// public void onAnimationStart(Animator animator) {
				// animation_change = false;
				// }
				//
				// @Override
				// public void onAnimationEnd(Animator animator) {
				// }
				//
				// @Override
				// public void onAnimationCancel(Animator animator) {
				// }
				//
				// @Override
				// public void onAnimationRepeat(Animator animator) {
				//
				// }
				// });
				// animator.start();
				// // 这里要判断是否有广告的出现
				//
				// // if (firstVisibleItem >= 1) {
				// // IndexHorListAdapter mHorAdapter = new
				// // IndexHorListAdapter(
				// // getActivity(), userBehaviorInfos);
				// // tab_layout_view.setVisibility(View.VISIBLE);
				// // mHorAdapter.notifyDataSetChanged();
				// // } else {
				// // tab_layout_view.setVisibility(View.GONE);
				// // }
				// }
				// } else {
				animation_close = false;
				android.widget.FrameLayout.LayoutParams ll = (android.widget.FrameLayout.LayoutParams) nearby_ll
						.getLayoutParams();
				ll.setMargins(0, 50, 0, 0);
				nearby_ll.setLayoutParams(ll);
				// }
				// if (firstVisibleItem >= 1) {
				// IndexHorListAdapter mHorAdapter = new
				// IndexHorListAdapter(getActivity(), userBehaviorInfos);
				// tab_layout_view.setVisibility(View.VISIBLE);
				// mHorAdapter.notifyDataSetChanged();
				// } else {
				// tab_layout_view.setVisibility(View.GONE);
				// }
			}
		});
		return view;
	}

	/**
	 * 获取类型数据
	 */
	private void getTypeData() {
		mLoadingDialog.startDialog("请稍候");
		Map<String, String> params = new HashMap<String, String>();
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							List<UserBehaviorInfo> mList = JsonUtil
									.getTypeDataList(resultStr);
							if (mList != null) {
								mflagList.addAll(mList);
							} else {
								mflagList.addAll(MyApplication.getInstance()
										.getSelectTypeList());
							}
						} else {
							mflagList.addAll(MyApplication.getInstance()
									.getSelectTypeList());
						}
						initData();
					}
				});
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		// EventlistItem = new ArrayList<EventInfo>();
		// mflagList = new ArrayList<UserBehaviorInfo>();
		// mflagList.clear();
		// mflagList.addAll(MyApplication.getInstance().getSelectTypeList());
		for (int i = 0; i < mflagList.size(); i++) {
			mflagList.get(i).setSelect(false);
		}
		setActivityListData(mPage);
	}

	/**
	 * 设置活动数据处理
	 */
	private void setActivityListData(int page) {
		// Map<String, String> mParams1 = new HashMap<String, String>();
		// if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
		// || AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")) {
		// mParams1.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
		// mParams1.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		// } else {
		// mParams1.put(HttpUrlList.HTTP_LAT,
		// AppConfigUtil.LocalLocation.Location_latitude + "");
		// mParams1.put(HttpUrlList.HTTP_LON,
		// AppConfigUtil.LocalLocation.Location_longitude + "");
		// }
		//
		// mParams1.put("appType", "1");// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		// Log.i("ceshi", "tagid==  "+tagId);
		// if (tagId == null || tagId == "") {
		// tagId = "";
		// }
		// mParams1.put("activityTypeId", tagId);
		// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//
		// // 获取前月的第一天
		// Calendar cal = Calendar.getInstance();// 获取当前日期
		// String today = format.format(cal.getTime());
		// Log.i("ceshi", "today===  " + today);
		// /**
		// * 這裡是要變化的日期
		// */
		// mParams1.put("everyDate", today);
		// mParams1.put(HttpUrlList.HTTP_PAGE_NUM, 100 + "");
		// mParams1.put(HttpUrlList.HTTP_USER_ID,
		// MyApplication.loginUserInfor.getUserId());
		// mParams1.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		EventlistItem.clear();
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyEvent.WFF_APPNEARACTIVITYLIST, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是地图的数据" + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								List<EventInfo> list = JsonUtil
										.getEventList(resultStr);
								if (list.size() > 0) {
									EventlistItem.addAll(list);
									addActivityMakrt(EventlistItem);
								} else {
									EventlistItem.clear();
									addActivityMakrt(EventlistItem);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
	}

	public StringBuffer getTag(HashMap<Integer, Boolean> map) {
		StringBuffer tagAll = new StringBuffer();
		StringBuffer tag = new StringBuffer();
		if (map.size() == 0) {
			for (int j = 0; j < mflagList.size(); j++) {
				tagAll.append(mflagList.get(j).getTagId());
				tagAll.append(",");
			}
			return removedouhao(tagAll);
		} else {
			for (int i = 0; i < map.size(); i++) {
				if (map.get(i)) {
					if (i == 0) {
						for (int j = 0; j < mflagList.size(); j++) {
							tagAll.append(mflagList.get(j).getTagId());
							tagAll.append(",");
						}
						return removedouhao(tagAll);
					} else {
						tag.append(mflagList.get(i - 1).getTagId());
						tag.append(",");
					}
				}
			}
		}
		return removedouhao(tag);
	}

	public StringBuffer removedouhao(StringBuffer stringBuffer) {

		if (stringBuffer.length() > 0) {
			stringBuffer = stringBuffer.replace(stringBuffer.length() - 1,
					stringBuffer.length(), "");
		}
		return stringBuffer;
	}

	/**
	 * 這個是添加標示的方法
	 * 
	 * @param listItem
	 */
	public void addActivityMakrt(List<EventInfo> listItem) {
		aMap.clear();
		setUpMap();
		int i = 0;
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
		aMap.setOnInfoWindowClickListener(this);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnMapClickListener(this);
		aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层

		if (listItem != null && listItem.size() != 0) {
			for (EventInfo eif : listItem) {
				try {
					double lat = Double.parseDouble(eif.getEventLat());
					double lon = Double.parseDouble(eif.getEventLon());
					latlng = new LatLng(lat, lon);

					if (i <= 100) {
						if (Double.valueOf(eif.getDistance()) <= 50) {
							aMap.addMarker(

									new MarkerOptions()
											.position(latlng)
											.title(eif.getEventName() + ","
													+ eif.getEventId())
											.snippet(eif.getEventAddress())
											.period(50)
											.icon(BitmapDescriptorFactory.fromView(getBitMap(
													String.valueOf(eif
															.getTagColor()),
													eif.getTagInitial(), eif)))
											.draggable(true)).setObject(eif);
						}
					}
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 這個是添加標示的方法
	 * 
	 * @param listItem
	 */
	public void addActivityMakrtReturn(List<EventInfo> listItem) {
		aMap.clear();
		int i = 0;
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
		// aMap.setOnInfoWindowClickListener(this);
		// aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
		// aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		// aMap.setOnMapClickListener(this);
		// aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
		if (listItem != null && listItem.size() != 0) {

			for (EventInfo eif : listItem) {
				try {
					double lat = Double.parseDouble(eif.getEventLat());
					double lon = Double.parseDouble(eif.getEventLon());
					LatLng ll = new LatLng(lat, lon);

					if (i <= 100) {
						if (Double.valueOf(eif.getDistance()) <= 50) {
							aMap.addMarker(

									new MarkerOptions()
											.position(ll)
											.title(eif.getEventName() + ","
													+ eif.getEventId())
											.snippet(eif.getEventAddress())
											.period(50)
											.icon(BitmapDescriptorFactory.fromView(getBitMap(
													String.valueOf(eif
															.getTagColor()),
													eif.getTagInitial(), eif)))
											.draggable(true)).setObject(eif);
						}
					}
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * 返回自定义图标
	 * 
	 * @param text
	 * @return
	 */
	public View getBitMap(String color, String text, EventInfo eif) {

		// LayoutInflater inflater = LayoutInflater.from(mContext);
		// View view = inflater.inflate(R.layout.map_tip, null);
		//
		// CustomTitleView view = new CustomTitleView(mContext);
		// view.setWidth(50);
		// view.setHeight(70);
		// view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		// view.setMbg(Color.parseColor(color));
		// view.setTextColor(Color.WHITE);
		// view.setmTitleText(text);
		int h = getFontHeight(16);
		// eif.toString();

		TextView view = new TextView(mContext);
		view.setText(text);
		view.setTextSize(16);
		// view.setHeight(h*4);
		// view.setWidth(h*3);

		view.setPadding(h / 2, 0, h / 2, h + h / 2);
		view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.sh_icon_d);
		// view.setBackground(bitmap2Drawable(getAlphaBitmap(bmp,
		// Color.parseColor(color))));
		// view.setBackgroundResource(new
		// BitmapDrawable(getAlphaBitmap(bmp,Color.parseColor(color))));
		view.setTextColor(Color.WHITE);

		// RelativeLayout view = (RelativeLayout)
		// getLayoutInflater().inflate(R.layout.map_tip, null, false);
		// CustomTitleView pathView = (CustomTitleView)
		// view.findViewById(R.id.pathView);
		// pathView.setMbg(Color.parseColor(color));
		// pathView.setmTitleText(text);
		// pathView.setmTitleTextColor(Color.WHITE);
		if (text == null || text == "") {
			view.setBackgroundResource(R.drawable.icon_map);
		} else {
			String str = text.trim().substring(0, 1);
			if ("亲".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_qin);
			}
			if ("养".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_yang);
			}
			if ("影".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_ying);
			}
			if ("D".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_d);
			}
			if ("演".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_yan);
			}
			if ("充".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_chong);
			}
			if ("友".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_you);
			}
			if ("食".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_shi);
			}
			if ("旅".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_lv);
			}
			if ("赛".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_sai);
			}
			if ("展".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_zhan);
			}
			if ("运".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_yun);
			}
			if ("聚".equals(text)) {
				view.setBackgroundResource(R.drawable.sh_icon_ju);
			}
		}

		return view;
	}

	public int getFontHeight(float fontSize) {
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		Paint.FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	private void initMap() {
		/**
		 * 初始化地图
		 */
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();
		}
		// 隐藏放大缩小按钮
		UiSettings uiSettings = aMap.getUiSettings();
		uiSettings.setZoomControlsEnabled(false);
		// mGaoDeMapPoiSeahUtil = new GaoDeMapPoiSeahUtil(mContext, aMap);

	}

	/**
	 * 地图设置
	 */
	private void setUpMap() {
		Log.i("ceshi", "坐标== "+AppConfigUtil.LocalLocation.Location_latitude+"  "+AppConfigUtil.LocalLocation.Location_longitude);
		if (AppConfigUtil.LocalLocation.Location_latitude != ""
				&& AppConfigUtil.LocalLocation.Location_longitude != "") {
			lat = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_latitude);
			lon = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_longitude);
			getLocal(lat, lon);
		} else {
			lat = Double.valueOf(MyApplication.Location_latitude);
			lon = Double.valueOf(MyApplication.Location_longitude);
			getLocal(lat, lon);
		}
		//
		// aMap.setLocationSource(new MyLocationSource(this, aMap));// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
		// 显示标记
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);
		aMap.addMarker(markerOption);
	}

	private void getLocal(double lat, double lon) {
		latlng = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	}

	private void initViewMap(View view) {
		// 标题栏
		titleLayout = (RelativeLayout) view.findViewById(R.id.nearby_title);
		// title_right.setText("筛选");
		// title_right.setTextColor(Color.WHITE);
		// title_right.setTextSize(17);
		drawable = getResources()
				.getDrawable(R.drawable.sh_activity_title_pull);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		map_rl = (RelativeLayout) view.findViewById(R.id.map);
		// title_right.setCompoundDrawables(null, null, drawable, null);
		tv_map_location = (TextView) view.findViewById(R.id.map_location);
		tv_map_location.setVisibility(View.VISIBLE);
		mItem = (RelativeLayout) view.findViewById(R.id.dialog_activity_map);
		mItemImg = (ImageView) view.findViewById(R.id.dialog_activity_map_img);
		mItemTitle = (TextView) view
				.findViewById(R.id.dialog_activity_map_title);
		mItemAddress = (TextView) view
				.findViewById(R.id.dialog_activity_map_address);
		mItemData = (TextView) view.findViewById(R.id.dialog_activity_map_data);
		mItemDistance = (TextView) view.findViewById(R.id.mItemDistance);
		mItemTicket = (TextView) view
				.findViewById(R.id.dialog_activity_map_ticket);
		mItemPrice = (TextView) view.findViewById(R.id.mItemPrice);
		map_book = (TextView) view.findViewById(R.id.map_book);

		mShowAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.dialog_bottom_up);
		mHiddenAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.dialog_bottom_down);
		// title_right.setImageResource(R.drawable.sh_icon_search_nearby);
		// title_right.setOnClickListener(this);
		// title_right.setVisibility(View.VISIBLE);
		mMapView = (MapView) view.findViewById(R.id.activity_amap);
		// title_right.setOnClickListener(this);
		tv_map_location.setOnClickListener(this);
	}

	/**
	 * 这里是获取喜欢的有多少项
	 */
	private void getTip() {
		String tagId = "";
		for (int i = 0; i < MyApplication.getInstance().getSelectTypeList()
				.size(); i++) {
			tagId = MyApplication.getInstance().getSelectTypeList().get(i)
					.getTagId();
		}
	}


	public static List<UserBehaviorInfo> userBehaviorInfos = new ArrayList<UserBehaviorInfo>();

	/**
	 * 这个是遍历出所有的喜欢的项目
	 * 
	 * @param selectTypeList
	 */
	private void addTabData(List<UserBehaviorInfo> selectTypeList) {

		// if (!MyApplication.UserIsLogin &&
		// !MyApplication.getInstance().isFromMyLove) {
		// if (MyApplication.getInstance().getSelectTypeList() != null) {
		// MyApplication.getInstance().getSelectTypeList().clear();
		// }
		//
		// }
		// if (MyApplication.UserIsLogin) {
		// if (MyApplication.getInstance().getSelectTypeList() != null) {
		// MyApplication.getInstance().getSelectTypeList().clear();
		// }
		// }
		if (!MyApplication.getInstance().isFromMyLove) {
			if (MyApplication.getInstance().getSelectTypeList() != null) {
				MyApplication.getInstance().getSelectTypeList().clear();
			}
		}

		if (MyApplication.UserIsLogin
				&& MyApplication.getInstance().isFromMyLove) {
			if (MyApplication.getInstance().getSelectTypeList() != null) {
				MyApplication.getInstance().getSelectTypeList().clear();
			}
		}

		List<UserBehaviorInfo> mList = new ArrayList<UserBehaviorInfo>();
		for (int i = 0; i < selectTypeList.size(); i++) {
			if (MyApplication.loginUserInfor == null
					|| MyApplication.loginUserInfor.getUserId() == null
					|| "".equals(MyApplication.loginUserInfor.getUserId())) {
				if ("亲子演出培训DIY展览".indexOf(selectTypeList.get(i).getTagName()) != -1) {
					mList.add(selectTypeList.get(i));
				}
			} else {
				if (selectTypeList.get(i).isSelect()) {
					mList.add(selectTypeList.get(i));
				}
			}
		}
		List<UserBehaviorInfo> mList1 = new ArrayList<UserBehaviorInfo>();

		if (!MyApplication.UserIsLogin
				&& !MyApplication.getInstance().isFromMyLove) {
			mList1.clear();

			for (int h = 0; h < mList.size(); h++) {
				if (mList.get(h).getTagName().equals("亲子")) {
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++) {
				if (mList.get(h).getTagName().equals("演出")) {
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++) {
				if (mList.get(h).getTagName().equals("培训")) {
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++) {
				if (mList.get(h).getTagName().equals("DIY")) {
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++) {
				if (mList.get(h).getTagName().equals("展览")) {
					mList1.add(mList.get(h));
					break;
				}
			}
			userBehaviorInfos.clear();
			userBehaviorInfos.addAll(mList1);
			MyApplication.getInstance().setSelectTypeList(mList1);
		} else if (MyApplication.isFromMyLove) {
			userBehaviorInfos.clear();
			userBehaviorInfos.addAll(MyLoveActivity.mSelectList);
			MyApplication.getInstance().setSelectTypeList(
					MyLoveActivity.mSelectList);
		} else {
			userBehaviorInfos.clear();
			userBehaviorInfos.addAll(mList);
			MyApplication.getInstance().setSelectTypeList(mList);
		}
		addTab();
		getTip();
	}

	private void addTab() {
		mActivityTabHandler = new ActivityWffTabHandler(getActivity(), view,
				new TabCallback() {

					@Override
					public void setTab(UserBehaviorInfo info) {
						// TODO Auto-generated method stub
						// setCountTip(info.getTagId());
						Activity_typeId = info.getTagId();
						addmore_bool = false;

						mItem.setVisibility(View.GONE);
						mListView.getRefreshableView().setSelection(0);
						onResh(info.getTagId());
					}
				});
	}

	private void onResh(String tagId) {
		if (isBannerRefresh) {
			mListAdapter.setBannerIsRefresh(true);
		}
		isBannerRefresh = !isBannerRefresh;
		isRefresh = true;
		mPage = 0;
		mLoadingHandler.startLoading();
		setParams(tagId);

		// getTypeTag();
	}

	/**
	 * 上拉加载更多 设置mpage 进行更新
	 */
	private void onAddmoreData() {
		mPage = HttpUrlList.HTTP_NUM + mPage;
		addmore_bool = true;
		getListData(mPage);
		setActivityListData(mPage);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		isFirstResh = true;
		isRefresh = false;
		initView();
		initListData();
	}

	public NearbyFragment() {
		// TODO Auto-generated constructor stub
		super();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMapView != null)
			mMapView.onDestroy();
		if (aMap == null) {
			aMap.clear();
		}
		WeekItemAdapter.init(false);
	}

	private void initView() {

		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
	}

	/**
	 * 初始化listview的数据
	 */
	private void initListData() {
		mPage = 0;
		isRefresh = true;
		mList = new ArrayList<EventInfo>();
		mListAdapter = new NearbyListAdapter(this, getActivity(), mList, true);
		mListAdapter.isShowFootView(true);
		mListAdapter.isActivityMainList(true);
		mListAdapter.addBannerContanView(mListView);
		mListView.setAdapter(mListAdapter);
		getTypeTag();
		getCount();
	}

	/**
	 * 获取近期活动的数量
	 */
	private void getCount() {
		Map<String, String> params = new HashMap<String, String>();
		params.clear();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYCOUNT,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int count = JsonUtil.getCount(resultStr);
							setCount(count);
						}
					}
				});
	}

	/**
	 * 设置近期活动的数量
	 * 
	 * @param count
	 */
	public void setCount(int count) {
		if (count == 0) {
			mCount.setVisibility(View.GONE);
		} else {
			mCount.setVisibility(View.VISIBLE);
			mCount.setText(count + "");
		}
		if (mListAdapter != null) {
			mListAdapter.activityCount(count);
		}
	}

	private void setParams(String tagId) {
		this.tagId = tagId;
		mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mParams.put("tagId", tagId);
		getListData(mPage);
		setActivityListData(mPage);
	}

	/**
	 * 获取我喜欢的类型
	 */
	public void getTypeTag() {
		if (selectTypeList != null) {
			selectTypeList.clear();
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							selectTypeList = JsonUtil
									.getTypeDataList(resultStr);
							if (selectTypeList != null
									&& selectTypeList.size() > 0) {
								// setParams();
								setParams("");
								// if (isAddTabData) {
								addTabData(selectTypeList);
								// isAddTabData = false;
								// }
							} else {
								mLoadingHandler.showErrorText("请求失败。请先选择相应标签。");
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 刷新
	 */
	private final int REFRESH = 1;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH:
				if (mListView != null) {
					mListView.onRefreshComplete();
				}
				isFirstResh = false;
				break;
			default:
				break;
			}
		}

	};
	private View view;
	private ActivityWffTabHandler mActivityTabHandler;
	/**
	 * 近期是否有活动
	 */
	private TextView mCount;

	/**
	 * 获取20条活动的数据
	 */
	private void getListData(int page) {
		MyApplication.getInstance().setActivityType(activityType);
		mLoadingHandler.startLoading();
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		String urlString = "";

		if ("".equals(tagId)) {
			urlString = HttpUrlList.MyEvent.WFF_APPNEARACTIVITYLIST;
		} else {
			// 这个是根据横向的 listview 点击事件来刷新数据的
			urlString = HttpUrlList.MyEvent.WFF_APPNEARACTIVITYLIST;
			mParams.put("activityType", tagId);
		}
		if (sortType != 5) {
			mParams.put("sortType", sortType + "");
		} else {
			mParams.put("sortType", 1 + "");
		}
		if (activityIsFree != "" && activityIsFree != null) {
			mParams.put("activityIsFree", activityIsFree);
		} else {
			mParams.put("activityIsFree", "");
		}
		if (activityIsReservation != "" && activityIsReservation != null) {
			mParams.put("activityIsReservation", activityIsReservation);
		} else {
			mParams.put("activityIsReservation", "");
		}
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_latitude.length() == 0
				|| AppConfigUtil.LocalLocation.Location_latitude.equals("")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("")) {
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}
		Log.i("ceshi", "地址== Loc=== "
				+ AppConfigUtil.LocalLocation.Location_latitude + "  lon  =="
				+ AppConfigUtil.LocalLocation.Location_longitude);
		// 這裡暫時 寫死了
		// mParams.put(HttpUrlList.HTTP_LAT, 31.280842 + "");
		// mParams.put(HttpUrlList.HTTP_LON, 121.433594 + "");
		// mParams.put(HttpUrlList.HTTP_LAT,
		// AppConfigUtil.LocalLocation.Location_latitude + "");
		// mParams.put(HttpUrlList.HTTP_LON,
		// AppConfigUtil.LocalLocation.Location_longitude + "");
		MyHttpRequest.onHttpPostParamsNoLat(urlString, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "---" + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								List<EventInfo> mAddList = new ArrayList<EventInfo>();
								mAddList = JsonUtil.getEventList(resultStr);
								if (mAddList.size() > 0) {
									collection_null.setVisibility(View.GONE);
									mListView.setVisibility(View.VISIBLE);
									nearby_ll.setVisibility(View.VISIBLE);
									if (isRefresh) {
										mList.clear();
										isRefresh = false;
										EventInfo nullingo = new EventInfo();
										mAddList.add(0, nullingo);
										mAddList.add(1, nullingo);
									}
									mList.addAll(mAddList);
									mListAdapter.setList(mList);
								} else {
									if (addmore_bool) {

									} else {
										collection_null
												.setVisibility(View.VISIBLE);
										mListView.setVisibility(View.GONE);
										// nearby_ll.setVisibility(View.GONE);
										isFirstResh = true;
										addmore_bool = false;
									}

								}
								mLoadingHandler.stopLoading();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								mLoadingHandler.showErrorText(e.toString());
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
						handler.sendEmptyMessageDelayed(REFRESH, 200);
					}
				});
	}

	public List<EventInfo> getMainListData() {
		return mList;
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		switch (arg0.getId()) {
		/**
		 * 近期的活动
		 */
		case R.id.activity_week_tv:
			mListAdapter.activityCount(0);
			mListAdapter.notifyDataSetChanged();
			setCount(0);
			intent = new Intent(getActivity(), ThisWeekActivity.class);
			startActivity(intent);
			break;
		/**
		 * 附近的活动
		 */
		case R.id.activity_map_tv:
			intent = new Intent(getActivity(), ActivityMap.class);
			startActivity(intent);
			break;
		/**
		 * 我喜欢的
		 */
		case R.id.activity_add_type:
			intent = new Intent(getActivity(), MyLoveActivity.class);
			startActivity(intent);
			break;
		case R.id.right_iv:
			if (map_fg_change) {
				map_fg_change = false;
				right_iv.setImageResource(R.drawable.icon_list);
				map_rl.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				nearby_ll.setVisibility(View.GONE);
			} else {
				map_fg_change = true;
				right_iv.setImageResource(R.drawable.icon_maptop);
				map_rl.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				nearby_ll.setVisibility(View.VISIBLE);
			}

			// intent = new Intent(getActivity(), ActivityMap.class);
			// startActivity(intent);
			break;
		case R.id.preface:
			if (scroll_status) {
				showPopuwindow(2);
				addmore_bool = false;
			}
			break;
		case R.id.sieve:
			if (scroll_status) {
				showPopuwindow(3);
				addmore_bool = false;
			}
			break;
		case R.id.tv:
			if (pw.isShowing()) {
				pw.dismiss();
			}
			preface.setText("智能排序");
			nearby_ll.setVisibility(View.VISIBLE);
			sortType = 1;
			getListData(0);
			isRefresh = true;
			break;
		default:
			break;
		}
	}

	/**
	 * 加载开始 数据加载
	 */
	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		isRefresh = true;
		mPage = 0;
		getTypeTag();
		// setParams("");
		// getListData(mPage);
		// initData();
		mLoadingHandler.startLoading();
	}

	/**
	 * 数据加载完成
	 */
	@Override
	public void onLoginSuccess() {
		// TODO Auto-generated method stub
		isRefresh = true;
		mPage = 0;
		getListData(mPage);
		setActivityListData(mPage);
		mLoadingHandler.startLoading();
	}

	/**
	 * 广播通知 不知道哪里用到了 进行数据的加载
	 * 
	 * @author wenff
	 * 
	 */
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("onReceive", "onReceive");
			isRefresh = false;
			initView();
			initListData();
		}

	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (mItem.isShown()) {
			mItem.startAnimation(mHiddenAnimation);
		}
		mItem.setVisibility(View.GONE);
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// initData();

		addMyActivity(arg0);
		return true;
	}

	/**
	 * 最新活动自定义消息框
	 * 
	 * @param marker
	 */
	private void addMyActivity(Marker marker) {
		addActivityMakrtReturn(EventlistItem);
		final EventInfo eif = (EventInfo) marker.getObject();
		// 显示标记
		marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.icon_mapon)));
		getLocal(Double.valueOf(eif.getEventLat()),
				Double.valueOf(eif.getEventLon()));
		if (!mItem.isShown()) {
			mItem.startAnimation(mShowAnimation);
		}
		double lat = Double.parseDouble(eif.getEventLat());
		double lon = Double.parseDouble(eif.getEventLon());
		LatLng ll1 = new LatLng(lat, lon);
		MarkerOptions markerOption1 = new MarkerOptions();
		markerOption1.position(ll1);
		markerOption1.draggable(true);
		markerOption1.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.icon_mapon)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption1.setFlat(true);
		aMap.addMarker(markerOption1);
		// view.setBackgroundResource(R.drawable.icon_mapon);
		mItem.setVisibility(View.VISIBLE);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlMiddle(eif.getEventIconUrl()),
						mItemImg, this);
		mItemTitle.setText(eif.getEventName());
		mItemAddress.setText(eif.getActivitySite());
		if (eif.getActivityStartTime() == null || eif.getEventEndTime() == null) {
			String start_time = eif.getActivityStartTime().replaceAll("-", ".");
			start_time = start_time.substring(5, 10);
			mItemData.setText(start_time);
		} else {
			String start_time = eif.getActivityStartTime().replaceAll("-", ".");
			start_time = start_time.substring(5, 10);
			String end_time = eif.getEventEndTime().replaceAll("-", ".");
			end_time = end_time.substring(5, 10);
			mItemData.setText(start_time + "-" + end_time);
		}
		if (eif.getActivityPrice().equals("0")) {
			mItemPrice.setText("免费");
		} else {
			mItemPrice.setText(eif.getActivityPrice() + "元");
		}
		Float distance = Float.valueOf(eif.getDistance().substring(0, 3));
		if (distance > 1.0) {
			mItemDistance.setText(distance + "KM");
		} else {
			mItemDistance.setText(distance * 1000 + "M");
		}

		if (eif.getActivityAbleCount() != null
				&& eif.getActivityAbleCount().length() > 0) {
			if (eif.getActivityAbleCount().equals("0")) {
				mItemTicket.setVisibility(View.GONE);
				map_book.setVisibility(View.GONE);
			} else {
				mItemTicket.setVisibility(View.GONE);
				mItemTicket.setText("剩余" + eif.getActivityAbleCount() + "张票");
				map_book.setVisibility(View.GONE);
				// mItemTicket.setText("余票：" + eif.getActivityAbleCount());
			}
		} else {
			mItemTicket.setText("");
			map_book.setVisibility(View.GONE);
		}

		map_book.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, String> params = new HashMap<String, String>();
				params.put(HttpUrlList.EventUrl.ACTIVITY_ID, eif.getEventId());

				params.put(HttpUrlList.HTTP_USER_ID,
						MyApplication.loginUserInfor.getUserId());
				MyHttpRequest.onHttpPostParams(
						HttpUrlList.EventUrl.WFF_HTTP_EVENT_URL, params,
						new HttpRequestCallback() {

							@Override
							public void onPostExecute(int statusCode,
									String resultStr) {
								// TODO Auto-generated method stub
								if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
									if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
											.getJsonStatus(resultStr)) {
										eventInfo = JsonUtil
												.getEventInfo(resultStr);
										Intent intent = new Intent(
												getActivity(),
												EventReserveActivity.class);
										intent.putExtra("EventInfo", eventInfo);
										startActivity(intent);
									} else {

									}
								}
							}
						});
			}
		});

		mItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mContext != null && eif.getEventId() != null) {
					Intent intent = new Intent(mContext,
							ActivityDetailActivity.class);
					intent.putExtra("eventId", eif.getEventId());
					mContext.startActivity(intent);
				}
			}
		});
	}

	private void showPopuwindow(int num) {
		View mView;
		ListView listView;
		ImageView arrow_up;
		TextView tv;
		int height;
		ArrayList<Wff_VenuePopuwindow> list;
		if (pw != null) {
			pw.dismiss();
		}
		switch (num) {

		case 2:
			nearby_ll.setVisibility(View.GONE);
			mView = View.inflate(getActivity(),
					R.layout.home_popuwindow_preface, null);
			arrow_up = (ImageView) mView.findViewById(R.id.arrow_up);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("热门排序"));
			list.add(new Wff_VenuePopuwindow("最新上线"));
			list.add(new Wff_VenuePopuwindow("即将结束"));
			hpwa = new HomePopuWindowAdapter(list, getActivity());
			listView.setAdapter(hpwa);
			height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.9);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			pw.showAsDropDown(nearby_ll, 50, 0);
			arrow_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null) {
						pw.dismiss();
						nearby_ll.setVisibility(View.VISIBLE);
					}
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
							.getItemAtPosition(arg2);
					if (arg2 == 0) {
						sortType = 2;
					} else if (arg2 == 1) {
						sortType = 3;
					} else if (arg2 == 2) {
						sortType = 4;
					}

					if (pw != null) {
						pw.dismiss();
						preface.setText(wvp.getDictName());
						nearby_ll.setVisibility(View.VISIBLE);
					}
					getListData(0);
					isRefresh = true;

				}
			});
			break;
		case 3:
			nearby_ll.setVisibility(View.GONE);
			mView = View.inflate(getActivity(), R.layout.home_popuwindow_sieve,
					null);
			arrow_up = (ImageView) mView.findViewById(R.id.arrow_up);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (pw.isShowing()) {
						pw.dismiss();
					}
					nearby_ll.setVisibility(View.VISIBLE);
					activityIsFree = "";
					activityIsReservation = "";
					getListData(0);
					isRefresh = true;
					sieve.setText("全部");
				}
			});
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("免费"));
			list.add(new Wff_VenuePopuwindow("在线预订"));
			HomeCenterPopuWindowAdapter hcpwa = new HomeCenterPopuWindowAdapter(
					list, getActivity());
			listView.setAdapter(hcpwa);
			height = MyApplication.getWindowHeight() / 4;

			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3,
					height);
			pw.showAsDropDown(nearby_ll, 50, 0);
			arrow_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (pw != null) {
						pw.dismiss();
						nearby_ll.setVisibility(View.VISIBLE);
					}
				}
			});
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
							.getItemAtPosition(arg2);
					if (arg2 == 0) {
						activityIsFree = "1";
						activityIsReservation = "";
					} else if (arg2 == 1) {
						activityIsFree = "";
						activityIsReservation = "2";
					}
					if (pw != null) {
						pw.dismiss();
						sieve.setText(wvp.getDictName());
						nearby_ll.setVisibility(View.VISIBLE);
					}
					getListData(0);
					isRefresh = true;

				}
			});
			break;

		default:
			break;
		}
	}
}
