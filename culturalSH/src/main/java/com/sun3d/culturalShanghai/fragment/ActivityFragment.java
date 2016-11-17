package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
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
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.ActivityMap;
import com.sun3d.culturalShanghai.activity.EventReserveActivity;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity.OnLogin_Status;
import com.sun3d.culturalShanghai.activity.MyLoveActivity;
import com.sun3d.culturalShanghai.activity.SearchActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.adapter.ActivityHomeListAdapter;
import com.sun3d.culturalShanghai.adapter.HomeCenterPopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopuWindowAreaAdapter;
import com.sun3d.culturalShanghai.basic.service.DataService;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.umeng.analytics.MobclickAgent;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这个是活动的fragment
 *
 * @author liningkang
 */
@SuppressLint("ValidFragment")
public class ActivityFragment extends Fragment
		implements OnClickListener, RefreshListenter, OnLogin_Status, AMap.OnMarkerClickListener,
		AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnMapClickListener, ImageLoadingListener
{
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private List<EventInfo> mAddList;
	private List<EventInfo> mEventList;
	public boolean isAddBanner = true;
	private boolean addmore_bool = true;
	private LinearLayout collection_null;
	private boolean animation_close = false;
	/**
	 * true 表示 title不见了 false 表示title还在
	 */
	private boolean animation_change = true;
	private boolean titleIsAnimating = false;
	private TextView shopping_areas, preface, sieve;
	public PopupWindow pw;
	private boolean scroll_status = true;
	private PullToRefreshListView mListView;
	/**
	 * 这是 listview中的数据
	 */
	private List<EventInfo> mList;
	private RelativeLayout mtitl;
	public ActivityHomeListAdapter mListAdapter;


	/**
	 * 刷新用的参数 0 为第一页
	 */
	private int mPage = 0;
	/**
	 * 这是和场馆一样的接口
	 */
	public ArrayList<Wff_VenuePopuwindow> list_area;
	/**
	 * 智能排序 1-智能排序 2-热门排序 3-最新上线 4-即将结束 5-离我最近
	 */
	public int sortType = -1;
	/**
	 * 1-免费 2-收费
	 */
	public String activityIsFree = null;
	/**
	 * 是否预定 1-不可预定 2-可预定
	 */
	public String activityIsReservation = null;
	public String venueArea = null;
	public String venueDicName = "";
	public String venueMood = null;
	public String venueMoodName = "";
	private HomePopuWindowAdapter hpwa;
	private View itemView;
	private int mBgColor;
	private Wff_VenuePopuwindow wvpw;
	public ArrayList<Wff_VenuePopuwindow> list_area_z;
	private int title_heigth;
	private boolean hasMeasured = false;
	private int frist_item;
	private String TAG = "ActivityFragment";

	public int getmBgColor()
	{
		return mBgColor;
	}

	public void setmBgColor(int mBgColor)
	{
		this.mBgColor = mBgColor;
	}

	public LinearLayout sieve_ll, head_sieve_ll;
	private TextView middle_tv;
	private ImageView left_iv, right_iv;
	public Boolean isRefresh = false;
	private Boolean isListViewRefresh = true;
	private String activityType = "3";// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
	private View Topview;
	private Boolean isFirstResh = true;
	private LinearLayout tab_layout_view;
	private Boolean isBannerRefresh = true;
	private String Activity_typeId = "";
	// cf719729422c497aa92abdd47acdaa56
	private Boolean isAddTabData = true;
	private String tagId = "";
	/**
	 * 这是横向listview的数据
	 */
	public static List<UserBehaviorInfo> selectTypeList;
	private MapView mMapView; // 小地图，大地图
	private AMap aMap;
	private MarkerOptions markerOption;
	private double lat;
	private double lon;
	private LatLng latlng;
	private RelativeLayout titleLayout;
	private RelativeLayout map_rl;
	private TextView tv_map_location;
	private TextView mItemTitle;
	private TextView mItemAddress;
	private TextView mItemData;
	private TextView mItemDistance;
	private TextView mItemPrice;
	private TextView mItemTicket;
	private TextView map_book;
	private RelativeLayout mItem;
	private ImageView mItemImg;
	private Animation mShowAnimation;
	private Animation mHiddenAnimation;
	private Context mContext;
	private Button buttonRefresh;
	Drawable drawable;
	private EventInfo eventInfo;
	private int scrollDirection;// 1:上 0:不变 －1 下
	private List<EventInfo> EventlistItem;
	/**
	 * 这个是来判断 map 和 fragment 的切换
	 */
	private boolean map_fg_change = true;
	private int heigth;
	/**
	 * 1 表示adapter的 0 表示acitvity的
	 */
	private int type_num;
	private ListView listView_right;
	private int frist_run = 0;

	public void onResume()
	{
		super.onResume();
		MobclickAgent.onPageStart("MainFragmentActivity"); // 统计页面
		if (mMapView != null)
			mMapView.onResume();
		titleIsAnimating = false;

	}

	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd("MainFragmentActivity");
		if (mMapView != null)
			mMapView.onPause();

	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (mMapView != null)
			mMapView.onSaveInstanceState(outState);
	}

	private void initMap()
	{
		/**
		 * 初始化地图
		 */
		if (aMap == null)
		{
			aMap = mMapView.getMap();
			setUpMap();
		}
		// 隐藏放大缩小按钮

		// mGaoDeMapPoiSeahUtil = new GaoDeMapPoiSeahUtil(mContext, aMap);

	}

	public int getFontHeight(float fontSize)
	{
		Paint paint = new Paint();
		paint.setTextSize(fontSize);
		Paint.FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	/**
	 * 返回自定义图标
	 *
	 * @param text
	 * @return
	 */
	public View getBitMap(String color, String text, EventInfo eif)
	{

		int h = getFontHeight(16);

		TextView view = new TextView(mContext);
		view.setText(text);
		view.setTextSize(16);

		view.setPadding(h / 2, 0, h / 2, h + h / 2);
		view.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sh_icon_d);
		view.setTextColor(Color.WHITE);
		if (text == null || text == "")
		{
			view.setBackgroundResource(R.drawable.icon_map);
		} else
		{
			String str = text.trim().substring(0, 1);
			if ("亲".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_qin);
			}
			if ("养".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_yang);
			}
			if ("影".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_ying);
			}
			if ("D".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_d);
			}
			if ("演".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_yan);
			}
			if ("充".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_chong);
			}
			if ("友".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_you);
			}
			if ("食".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_shi);
			}
			if ("旅".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_lv);
			}
			if ("赛".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_sai);
			}
			if ("展".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_zhan);
			}
			if ("运".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_yun);
			}
			if ("聚".equals(text))
			{
				view.setBackgroundResource(R.drawable.sh_icon_ju);
			}
		}

		return view;
	}

	/**
	 * 這個是添加標示的方法
	 *
	 * @param listItem
	 */
	public void addActivityMakrt(List<EventInfo> listItem)
	{
		aMap.clear();
		setUpMap();
		int i = 0;
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
		aMap.setOnInfoWindowClickListener(this);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		aMap.setOnMapClickListener(this);
		aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
		if (listItem != null && listItem.size() != 0)
		{
			for (EventInfo eif : listItem)
			{
				try
				{
					double lat = Double.parseDouble(eif.getEventLat());
					double lon = Double.parseDouble(eif.getEventLon());
					latlng = new LatLng(lat, lon);

					if (i <= 100)
					{
						if (Double.valueOf(eif.getDistance()) <= 50)
						{
							aMap.addMarker(new MarkerOptions().position(latlng).title(eif.getEventName() + "," + eif.getEventId()).snippet(eif.getEventAddress()).period(50).icon(BitmapDescriptorFactory.fromView(getBitMap(String.valueOf(eif.getTagColor()), eif.getTagInitial(), eif))).draggable(true)).setObject(eif);
						}
					}
					i++;
				} catch (Exception e)
				{
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
	public void addActivityMakrtReturn(List<EventInfo> listItem)
	{
		aMap.clear();
		int i = 0;
		// aMap.moveCamera(CameraUpdateFactory.zoomTo(13));// 设置地图放大系数
		// aMap.setOnInfoWindowClickListener(this);
		// aMap.setOnMarkerClickListener(this);// 添加点击marker监听事
		// aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		// aMap.setOnMapClickListener(this);
		// aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
		if (listItem != null && listItem.size() != 0)
		{

			for (EventInfo eif : listItem)
			{
				try
				{
					double lat = Double.parseDouble(eif.getEventLat());
					double lon = Double.parseDouble(eif.getEventLon());
					LatLng ll = new LatLng(lat, lon);

					if (i <= 100)
					{
						if (Double.valueOf(eif.getDistance()) <= 50)
						{
							aMap.addMarker(

									new MarkerOptions().position(ll).title(eif.getEventName() + "," + eif.getEventId()).snippet(eif.getEventAddress()).period(50).icon(BitmapDescriptorFactory.fromView(getBitMap(String.valueOf(eif.getTagColor()), eif.getTagInitial(), eif))).draggable(true)).setObject(eif);
						}
					}
					i++;
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

		}

	}

	private void getLocal(double lat, double lon)
	{
		latlng = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	}

	/**
	 * 地图设置
	 */
	private void setUpMap()
	{
		if (AppConfigUtil.LocalLocation.Location_latitude != "" && AppConfigUtil.LocalLocation.Location_longitude != "")
		{
			lat = Double.parseDouble(AppConfigUtil.LocalLocation.Location_latitude);
			lon = Double.parseDouble(AppConfigUtil.LocalLocation.Location_longitude);
			getLocal(lat, lon);
		} else
		{
			lat = Double.valueOf(MyApplication.Location_latitude);
			lon = Double.valueOf(MyApplication.Location_longitude);
			getLocal(lat, lon);
		}
		//
		// aMap.setLocationSource(new MyLocationSource(this, aMap));// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示

		aMap.setMyLocationEnabled(false);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
		// 显示标记
		markerOption = new MarkerOptions();
		markerOption.position(latlng);
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);
		aMap.addMarker(markerOption);
	}

	private void initViewMap(View view)
	{
		// 标题栏
		titleLayout = (RelativeLayout) view.findViewById(R.id.nearby_title);
		drawable = getResources().getDrawable(R.drawable.sh_activity_title_pull);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		map_rl = (RelativeLayout) view.findViewById(R.id.map);
		tv_map_location = (TextView) view.findViewById(R.id.map_location);
		tv_map_location.setVisibility(View.VISIBLE);
		mItem = (RelativeLayout) view.findViewById(R.id.dialog_activity_map);
		mItemImg = (ImageView) view.findViewById(R.id.dialog_activity_map_img);
		mItemTitle = (TextView) view.findViewById(R.id.dialog_activity_map_title);
		mItemAddress = (TextView) view.findViewById(R.id.dialog_activity_map_address);
		mItemData = (TextView) view.findViewById(R.id.dialog_activity_map_data);
		mItemDistance = (TextView) view.findViewById(R.id.mItemDistance);
		mItemTicket = (TextView) view.findViewById(R.id.dialog_activity_map_ticket);
		mItemPrice = (TextView) view.findViewById(R.id.mItemPrice);
		map_book = (TextView) view.findViewById(R.id.map_book);

		mShowAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_bottom_up);
		mHiddenAnimation = AnimationUtils.loadAnimation(mContext, R.anim.dialog_bottom_down);
		mMapView = (MapView) view.findViewById(R.id.activity_amap);
		tv_map_location.setOnClickListener(this);
	}

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (msg.arg1 == 1001)
			{
				UserBehaviorInfo info = (UserBehaviorInfo) msg.obj;
				mActivityTabHandler.setPosition();
				Activity_typeId = info.getTagId();
				onResh(info.getTagId());
			} else
			{
				onLoadingRefresh();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.fragment_event, null);
		mContext = getActivity();
		initViewMap(view);
		if (mMapView != null)
		{
			mMapView.onCreate(savedInstanceState);
		}
		initMap();
		MyApplication.getInstance().setActivityHandler(mHandler);
		EventlistItem = new ArrayList<EventInfo>();
		heigth = (int) (((MyApplication.getWindowHeight()) / 3) * 1.1);
		left_iv = (ImageView) view.findViewById(R.id.left_iv);
		left_iv.setImageResource(R.drawable.icon_maptop);
		left_iv.setOnClickListener(this);
		middle_tv = (TextView) view.findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("文化活动");
		collection_null = (LinearLayout) view.findViewById(R.id.collection_null);
		buttonRefresh = (Button) view.findViewById(R.id.button_refresh);
		buttonRefresh.setOnClickListener(this);
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		Topview = (View) view.findViewById(R.id.activity_page_line);
		mtitl = (RelativeLayout) view.findViewById(R.id.title);
		ViewTreeObserver vto = mtitl.getViewTreeObserver();

		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
		{
			public boolean onPreDraw()
			{
				if (hasMeasured == false)
				{

					title_heigth = mtitl.getMeasuredHeight();
					int width = mtitl.getMeasuredWidth();
					// 获取到宽度和高度后，可用于计算
					hasMeasured = true;

				}
				return true;
			}
		});

		view.findViewById(R.id.activity_map_tv).setOnClickListener(this);
		view.findViewById(R.id.activity_week_tv).setOnClickListener(this);
		view.findViewById(R.id.activity_add_type).setOnClickListener(this);
		right_iv = (ImageView) view.findViewById(R.id.right_iv);
		right_iv.setImageResource(R.drawable.sh_activity_search_icon);
		right_iv.setOnClickListener(this);
		tab_layout_view = (LinearLayout) view.findViewById(R.id.activity_tablayout_activity_layout);
		mListView = (PullToRefreshListView) view.findViewById(R.id.main_list);

		View view_head = LayoutInflater.from(getActivity()).inflate(R.layout.activity_null_top, null);
		head_sieve_ll = (LinearLayout) view_head.findViewById(R.id.top_null_ll);
		mListView.getRefreshableView().addHeaderView(view_head);

		sieve_ll = (LinearLayout) view.findViewById(R.id.Sieve);
		shopping_areas = (TextView) view.findViewById(R.id.shopping_areas);
		preface = (TextView) view.findViewById(R.id.preface);
		sieve = (TextView) view.findViewById(R.id.sieve);
		shopping_areas.setTypeface(MyApplication.GetTypeFace());
		preface.setTypeface(MyApplication.GetTypeFace());
		sieve.setTypeface(MyApplication.GetTypeFace());
		shopping_areas.setOnClickListener(this);
		preface.setOnClickListener(this);
		sieve.setOnClickListener(this);
		mCount = (TextView) view.findViewById(R.id.tab_count);
		mListView.setOnRefreshListener(myRefresh);
		initView();
		initListData();
		initDataArea();
		// getListData(mPage);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	/**
	 * 这里是获取喜欢的有多少项
	 */
	private void getTip()
	{
		String tagId = "";
		for (int i = 0; i < MyApplication.getInstance().getSelectTypeList().size(); i++)
		{
			tagId = MyApplication.getInstance().getSelectTypeList().get(i).getTagId();
		}
	}

	public static List<UserBehaviorInfo> userBehaviorInfos = new ArrayList<UserBehaviorInfo>();

	/**
	 * 这个是遍历出所有的喜欢的项目
	 *
	 * @param selectTypeList
	 */
	private void addTabData(List<UserBehaviorInfo> selectTypeList)
	{

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
		if (!MyApplication.getInstance().isFromMyLove)
		{
			if (MyApplication.getInstance().getSelectTypeList() != null)
			{
				MyApplication.getInstance().getSelectTypeList().clear();
			}
		}

		if (MyApplication.UserIsLogin && MyApplication.getInstance().isFromMyLove)
		{
			if (MyApplication.getInstance().getSelectTypeList() != null)
			{
				MyApplication.getInstance().getSelectTypeList().clear();
			}
		}

		List<UserBehaviorInfo> mList = new ArrayList<UserBehaviorInfo>();
		for (int i = 0; i < selectTypeList.size(); i++)
		{
			if (MyApplication.loginUserInfor == null || MyApplication.loginUserInfor.getUserId() == null || "".equals(MyApplication.loginUserInfor.getUserId()))
			{
				if ("亲子演出培训DIY展览".indexOf(selectTypeList.get(i).getTagName()) != -1)
				{
					mList.add(selectTypeList.get(i));
				}
			} else
			{
				if (selectTypeList.get(i).isSelect())
				{
					mList.add(selectTypeList.get(i));
				}
			}
		}
		List<UserBehaviorInfo> mList1 = new ArrayList<UserBehaviorInfo>();

		if (!MyApplication.UserIsLogin && !MyApplication.getInstance().isFromMyLove)
		{
			mList1.clear();

			for (int h = 0; h < mList.size(); h++)
			{
				if (mList.get(h).getTagName().equals("亲子"))
				{
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++)
			{
				if (mList.get(h).getTagName().equals("演出"))
				{
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++)
			{
				if (mList.get(h).getTagName().equals("培训"))
				{
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++)
			{
				if (mList.get(h).getTagName().equals("DIY"))
				{
					mList1.add(mList.get(h));
					break;
				}
			}
			for (int h = 0; h < mList.size(); h++)
			{
				if (mList.get(h).getTagName().equals("展览"))
				{
					mList1.add(mList.get(h));
					break;
				}
			}
			userBehaviorInfos.clear();
			userBehaviorInfos.addAll(mList1);

			MyApplication.getInstance().setSelectTypeList(mList1);
		} else if (MyApplication.isFromMyLove)
		{
			userBehaviorInfos.clear();
			userBehaviorInfos.addAll(MyLoveActivity.mSelectList);
			MyApplication.getInstance().setSelectTypeList(MyLoveActivity.mSelectList);
		} else
		{
			userBehaviorInfos.clear();
			userBehaviorInfos.addAll(mList);
			MyApplication.getInstance().setSelectTypeList(mList);
		}
		addmore_bool = false;
		if (frist_run == 0)
		{
			if (userBehaviorInfos != null && userBehaviorInfos.size() > 0)
			{
				tagId = userBehaviorInfos.get(0).getTagId();
			}
		}
		addTab();
		getTip();
	}

	private void addTab()
	{
		mActivityTabHandler = new ActivityTabHandler(getActivity(), view, new TabCallback()
		{

			@Override
			public void setTab(UserBehaviorInfo info)
			{

				// TODO Auto-generated method stub
				// setCountTip(info.getTagId());
				Activity_typeId = info.getTagId();
				if (Activity_typeId == "")
				{
					venueMood = null;
					venueArea = null;
					sortType = -1;
					activityIsFree = null;
					activityIsReservation = null;
				}
				addmore_bool = false;
				if (pw != null && pw.isShowing())
				{
					pw.dismiss();
				}
				mListView.getRefreshableView().setSelection(0);
				tagId = Activity_typeId;
				Log.i(TAG, "看看 ID==  " + tagId);
				sieve_ll.setVisibility(View.GONE);
				onResh(tagId);
			}
		}, this);
	}

	public void onResh(String tagId)
	{

		if (isBannerRefresh)
		{
			mListAdapter.setBannerIsRefresh(true);
		}
		isBannerRefresh = !isBannerRefresh;
		isRefresh = true;
		animation_change = true;
		animation_close = false;
		mPage = 0;
		mLoadingHandler.startLoading();
		titleIsAnimating = false;
		setParams(tagId);
	}

	/**
	 * 上拉加载更多 设置mpage 进行更新
	 */
	private void onAddmoreData()
	{
		mPage = HttpUrlList.HTTP_NUM + mPage;
		addmore_bool = true;
		// getAdvertRecommendListData();
		getListData(mPage);
	}

	public ActivityFragment()
	{
		// TODO Auto-generated constructor stub
		super();
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initView()
	{
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
	}

	private void setParams(String tagId)
	{
		this.tagId = tagId;
		isAddBanner = true;
		getAdvertRecommendListData();
		getListData(mPage);
		setActivityListData(mPage);
		mListView.setOnScrollListener(new My_ScrollListener());

	}

	/**
	 * 获取广告的接口
	 *
	 * @param tagId2
	 */
	private void getAdvertRecommendListData()
	{
		MyApplication.getInstance().setActivityType(activityType);
		DataService.getAdvertismentInActivityHome(tagId, new DataService.ReceiveCallBack()
		{

			@Override
			public void dataReceviceSuccessCallBack(Object data)
			{
				mAddList = (List<EventInfo>) data;
				animation_close = true;
				mListAdapter.setBannerList(mAddList);
				mListAdapter.notifyDataSetChanged();
			}

			@Override
			public void dataReceviceFailedCallBack(String feedback)
			{
				mAddList.clear();
				mListAdapter.setBannerList(mAddList);
				mListAdapter.notifyDataSetChanged();

			}
		});
	}

	/**
	 * 获取我喜欢的类型
	 */
	public void getTypeTag()
	{
		if (selectTypeList != null)
		{
			selectTypeList.clear();
		}

		DataService.getActivityType(new DataService.ReceiveCallBack()
		{

			@Override
			public void dataReceviceSuccessCallBack(Object data)
			{
				selectTypeList = (List<UserBehaviorInfo>) data;
				if (selectTypeList != null && selectTypeList.size() > 0)
				{
					addTabData(selectTypeList);
					setParams(tagId);

				} else
				{
					mLoadingHandler.showErrorText("请求失败。请先选择相应标签。");
				}

			}

			@Override
			public void dataReceviceFailedCallBack(String feedback)
			{
				mLoadingHandler.showErrorText(feedback);

			}
		});

	}

	/**
	 * 刷新
	 */
	private final int REFRESH = 1;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what)
			{
			case REFRESH:
				if (mListView != null)
				{
					mListView.onRefreshComplete();
				}
				isFirstResh = false;
				break;
			case 2:
				mListAdapter.setList_area(list_area);
				mListAdapter.setFragment(ActivityFragment.this);
				break;
			case 3:
				mList.clear();
				mListAdapter.setList(mList);
				collection_null.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				Toast.makeText(mContext, "已经全部加载完毕", 1000).show();
				break;
			default:
				break;
			}
		}

	};
	private View view;
	private ActivityTabHandler mActivityTabHandler;
	/**
	 * 近期是否有活动
	 */
	private TextView mCount;

	/**
	 * 获取20条活动的数据
	 */
	public void getListData(int page)
	{
		if (page == 0)
		{
			mLoadingHandler.startLoading();
		}

		MyApplication.getInstance().setActivityType(activityType);
		DataService.getRecommendActivity(tagId, venueMood, venueArea, sortType, activityIsFree, activityIsReservation, page, isRefresh, new DataService.ReceiveCallBack()
		{

			@Override
			public void dataReceviceSuccessCallBack(Object data)
			{

				mEventList = (List<EventInfo>) data;
				Log.i(TAG, "dataReceviceSuccessCallBack: "+mEventList.size());
				if (mEventList.size() > 0)
				{
					collection_null.setVisibility(View.GONE);
					mListView.setVisibility(View.VISIBLE);
					if (isRefresh)
					{
						mList.clear();
						isRefresh = false;
					}
					for (int i = 0; i < mEventList.size(); i++)
					{
						if (mAddList.size() != 0 && isAddBanner && mAddList.get(0).getIsContainActivtiyAdv() == 1 && i == 0)
						{
							mList.add(mAddList.get(0));
						}

						if (i % 4 == 3 && mAddList.size() != 0 && isAddBanner && mAddList.get(0).getList() != null && mAddList.get(0).getList().length() != 0)
						{
							int pos = i % 3;

							if (pos < mAddList.get(0).getList().length() && i < mAddList.get(0).getList().length() * 4)
							{
								mList.add(mAddList.get(0));
							}

						}
						mList.add(mEventList.get(i));
					}
					isAddBanner = false;
					Log.i(TAG, "dataReceviceSuccessCallBack: size=== "+mList.size());
					mListAdapter.setList(mList);
				} else
				{
					if (addmore_bool)
					{
						// handler.sendEmptyMessage(3);
					} else
					{
//						 mLoadingHandler.isNotContent();
						collection_null.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
						sieve_ll.setVisibility(View.VISIBLE);
						isFirstResh = true;
						addmore_bool = false;
					}

				}
				mLoadingHandler.stopLoading();
				handler.sendEmptyMessageDelayed(REFRESH, 200);

			}

			@Override
			public void dataReceviceFailedCallBack(String feedback)
			{
				mLoadingHandler.showErrorText(feedback);
			}
		});

	}

	public List<EventInfo> getMainListData()
	{
		return mList;
	}

	@Override
	public void onClick(View arg0)
	{
		Intent intent = null;
		switch (arg0.getId())
		{
		/**
		 * 近期的活动
		 */
		case R.id.activity_week_tv:
			mListAdapter.activityCount(0);
			mListAdapter.notifyDataSetChanged();
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
			// EventWindows.getInstance(getActivity()).showSearch(mtitl, true);
			Intent i = new Intent();
			i.setClass(getActivity(), SearchActivity.class);
			i.putExtra("type", "activity");
			getActivity().startActivity(i);
			break;
		case R.id.shopping_areas:
			// if (scroll_status) {
			showPopuwindow(1, 0, null);
			// }

			break;
		case R.id.preface:
			// if (scroll_status) {
			showPopuwindow(2, 0, null);
			// }
			break;
		case R.id.sieve:
			// if (scroll_status) {
			showPopuwindow(3, 0, null);
			// }
			break;
		case R.id.tv:
			if (pw.isShowing())
			{
				pw.dismiss();
			}
			if (mAddList.size() != 0 && mAddList.get(0).getIsContainActivtiyAdv() == 1)
			{
				sieve_ll.setVisibility(View.VISIBLE);
			} else
			{

			}
			preface.setText("智能排序");
			sortType = 1;
			getListData(0);
			isRefresh = true;
			break;
		case R.id.left_iv:
			mLoadingHandler.stopLoading();
			if (map_fg_change)
			{
				map_fg_change = false;
				middle_tv.setText("附近");
				left_iv.setImageResource(R.drawable.icon_list);
				map_rl.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				sieve_ll.setVisibility(View.GONE);
			} else
			{
				map_fg_change = true;
				middle_tv.setText("文化活动");
				left_iv.setImageResource(R.drawable.icon_maptop);
				map_rl.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				if (mAddList != null && mAddList.size() != 0 && mAddList.get(0).getIsContainActivtiyAdv() == 1)
				{
					if (frist_item >= 2)
					{
						sieve_ll.setVisibility(View.VISIBLE);
					}
				} else
				{
					if (frist_item >= 1)
					{
						sieve_ll.setVisibility(View.VISIBLE);
					}
				}

			}

			break;
		case R.id.button_refresh:// 刷新按钮
			getListData(0);
			break;
		default:
			break;
		}
		// titleIsAnimating = false;
	}

	/**
	 * 加载开始 数据加载
	 */
	@Override
	public void onLoadingRefresh()
	{
		// TODO Auto-generated method stub
		isRefresh = true;
		mPage = 0;

	}

	/**
	 * 数据加载完成
	 */
	@Override
	public void onLoginSuccess()
	{
		// TODO Auto-generated method stub
		isRefresh = true;
		mPage = 0;
		getListData(mPage);
		mLoadingHandler.startLoading();
	}

	class My_ScrollListener implements OnScrollListener
	{

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{

			if (titleIsAnimating == false)
			{
				if (firstVisibleItem > frist_item)
				{
					startTitleAnimation(1);

				} else if (firstVisibleItem < frist_item)
				{
					startTitleAnimation(0);
				}
			}
			frist_item = firstVisibleItem;

			int detailCellIndex = 0;
			// 这是表示有广告的时候
			if (mAddList != null && mAddList.size() != 0 && mAddList.get(0).getIsContainActivtiyAdv() == 1 && animation_close)
			{
				detailCellIndex = 1;
			}

			if (firstVisibleItem >= (detailCellIndex + 1))
			{
				mListAdapter.filterLayout.setVisibility(View.GONE);
				sieve_ll.setVisibility(View.VISIBLE);
			} else
			{
				if (pw == null || !pw.isShowing())
				{
					mListAdapter.filterLayout.setVisibility(View.VISIBLE);
					sieve_ll.setVisibility(View.GONE);
				}
			}

		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1)
		{
			if (arg1 == SCROLL_STATE_IDLE)
			{
				// 停止滑动
				scroll_status = true;
			} else if (arg1 == SCROLL_STATE_TOUCH_SCROLL)
			{
				if (pw != null && pw.isShowing())
				{
					pw.dismiss();
				}
				// 手指还在上面滑动
				scroll_status = false;
			} else if (arg1 == SCROLL_STATE_FLING)
			{
				if (pw != null && pw.isShowing())
				{
					pw.dismiss();
				}
				setGoneOrVisible();
				// 正在滑动
				scroll_status = false;
			}
		}

	}

	private void setGoneOrVisible()
	{
		if (mAddList.size() != 0 && mAddList.get(0).getIsContainActivtiyAdv() == 1)
		{
			if (frist_item >= 2)
			{
				// sieve_ll.setVisibility(View.VISIBLE);
				// mListAdapter.setSieveGone();
			} else
			{
				// sieve_ll.setVisibility(View.GONE);
				// mListAdapter.setSieveVisible(0, "");
			}
		} else
		{
			if (frist_item >= 1)
			{
				// sieve_ll.setVisibility(View.VISIBLE);
				// mListAdapter.setSieveGone();
			} else
			{
				// sieve_ll.setVisibility(View.GONE);
				// mListAdapter.setSieveVisible(0, "");
			}
		}

	}

	public void showPopuwindow(int num, final int number, LinearLayout Linear_Layout)
	{
		// number == 1 表示 是从 adapter 里面点击的 0 表示activity 点击
		addmore_bool = false;
		View mView;
		ListView listView;
		LinearLayout close_ll;
		TextView tv;
		int height;
		ArrayList<Wff_VenuePopuwindow> list;
		type_num = number;
		if (pw != null)
		{
			pw.dismiss();
		}
		switch (num)
		{
		// 全部的商区
		case 1:
			if (number == 1)
			{
				mListAdapter.filterLayout.setVisibility(View.GONE);
			} else
			{
				sieve_ll.setVisibility(View.GONE);
			}

			mView = View.inflate(getActivity(), R.layout.home_popuwindow_shopping, null);
			ListView listView_left = (ListView) mView.findViewById(R.id.listView_left);
			listView_right = (ListView) mView.findViewById(R.id.listView_right);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			close_ll.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					if (pw.isShowing())
					{
						pw.dismiss();
					}
					if (number == 1)
					{
						mListAdapter.filterLayout.setVisibility(View.VISIBLE);
					} else
					{
						sieve_ll.setVisibility(View.VISIBLE);

					}

				}
			});
			hpwa = new HomePopuWindowAdapter(list_area, getActivity());
			listView_left.setAdapter(hpwa);
			listView_left.setOnItemClickListener(left_item);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 2, MyApplication.getWindowHeight() / 4);
			showFristPopWindow();

			break;
		case 2:
			if (number == 1)
			{
				mListAdapter.filterLayout.setVisibility(View.GONE);
			} else
			{
				sieve_ll.setVisibility(View.GONE);

			}
			mView = View.inflate(getActivity(), R.layout.home_popuwindow_preface, null);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(this);
			tv.setTypeface(MyApplication.GetTypeFace());
			listView = (ListView) mView.findViewById(R.id.listView);
			list = new ArrayList<Wff_VenuePopuwindow>();
			list.add(new Wff_VenuePopuwindow("智能排序"));
			list.add(new Wff_VenuePopuwindow("热门排序"));
			list.add(new Wff_VenuePopuwindow("最新上线"));
			list.add(new Wff_VenuePopuwindow("即将结束"));
			list.add(new Wff_VenuePopuwindow("离我最近"));
			hpwa = new HomePopuWindowAdapter(list, getActivity());
			listView.setAdapter(hpwa);
			height = MyApplication.getWindowHeight() / 3;
			height = (int) (height * 0.9);
			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3, height);
			showSecondPopWindow();
			close_ll.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					if (pw != null)
					{
						pw.dismiss();
						if (number == 1)
						{
							mListAdapter.filterLayout.setVisibility(View.VISIBLE);
						} else
						{
							sieve_ll.setVisibility(View.VISIBLE);
						}
					}
				}
			});
			listView.setOnItemClickListener(second_item);
			break;
		case 3:
			if (number == 1)
			{
				mListAdapter.filterLayout.setVisibility(View.GONE);
			} else
			{
				sieve_ll.setVisibility(View.GONE);
			}
			mView = View.inflate(getActivity(), R.layout.home_popuwindow_sieve, null);
			close_ll = (LinearLayout) mView.findViewById(R.id.close_ll);
			tv = (TextView) mView.findViewById(R.id.tv);
			tv.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					if (pw.isShowing())
					{
						pw.dismiss();
					}
					if (number == 1)
					{
						mListAdapter.filterLayout.setVisibility(View.VISIBLE);
					} else
					{
						if (mAddList.size() != 0 && mAddList.get(0).getIsContainActivtiyAdv() == 1)
						{
							sieve_ll.setVisibility(View.VISIBLE);
						} else
						{

						}
					}
					mListAdapter.setSieveVisible(3, "全部");
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
			HomeCenterPopuWindowAdapter hcpwa = new HomeCenterPopuWindowAdapter(list, getActivity());
			listView.setAdapter(hcpwa);
			height = MyApplication.getWindowHeight() / 4;

			pw = new PopupWindow(mView, MyApplication.getWindowWidth() / 3, height);
			showThreePopWindow();
			close_ll.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					if (pw != null)
					{
						pw.dismiss();
						if (number == 1)
						{
							mListAdapter.filterLayout.setVisibility(View.VISIBLE);
						} else
						{
							sieve_ll.setVisibility(View.VISIBLE);
						}
					}
				}
			});
			listView.setOnItemClickListener(three_item);
			break;

		default:
			break;
		}
	}

	private void showThreePopWindow()
	{
		if (type_num == 1)
		{
			pw.showAsDropDown(mListAdapter.filterLayout, (mListAdapter.filterLayout.getWidth() - pw.getWidth()) / 2, 0);

		} else
		{
			pw.showAsDropDown(sieve_ll, (sieve_ll.getWidth() - pw.getWidth()) / 2, 0);
		}
	}

	private void showSecondPopWindow()
	{
		if (type_num == 1)
		{
			pw.showAsDropDown(mListAdapter.filterLayout, (mListAdapter.filterLayout.getWidth() - pw.getWidth()) / 2, 0);
		} else
		{
			pw.showAsDropDown(sieve_ll, (sieve_ll.getWidth() - pw.getWidth()) / 2, 0);
		}
	}

	private void showFristPopWindow()
	{
		if (type_num == 1)
		{

			pw.showAsDropDown(mListAdapter.filterLayout, (mListAdapter.filterLayout.getWidth() - pw.getWidth()) / 2, 0);

		} else
		{
			// 这个是 activity 本身的选项框

			pw.showAsDropDown(sieve_ll, (sieve_ll.getWidth() - pw.getWidth()) / 2, 0);
		}
	}

	private void initDataArea()
	{

		DataService.getCityArea(new DataService.ReceiveCallBack()
		{

			@Override
			public void dataReceviceSuccessCallBack(Object data)
			{
				String resultStr = (String) data;
				SharedPreManager.saveAllArea(resultStr);
				initAreaList(resultStr);
				handler.sendEmptyMessage(2);

			}

			@Override
			public void dataReceviceFailedCallBack(String feedback)
			{

			}
		});

	}

	private void initAreaList(String data)
	{

		try
		{
			list_area = new ArrayList<Wff_VenuePopuwindow>();
			JSONObject json = new JSONObject(data);
			JSONArray json_arr = json.optJSONArray("data");
			list_area.add(new Wff_VenuePopuwindow("", "全上海", "", new JSONArray()));
			for (int i = 0; i < json_arr.length(); i++)
			{
				JSONObject json_new = json_arr.getJSONObject(i);
				String dictId = json_new.optString("dictId");
				String dictName = json_new.optString("dictName");
				String dictCode = json_new.optString("dictCode");
				JSONArray dictList = json_new.optJSONArray("dictList");
				list_area.add(new Wff_VenuePopuwindow(dictId, dictName, dictCode, dictList));

			}
		} catch (Exception ex)
		{

		}

	}

	/**
	 * 初始化listview的数据
	 */
	private void initListData()
	{
		mPage = 0;
		isRefresh = true;
		mList = new ArrayList<EventInfo>();
		mListAdapter = new ActivityHomeListAdapter(this, getActivity(), mList, true);
		mListAdapter.isShowFootView(true);
		mListAdapter.isActivityMainList(true);
		mListAdapter.addBannerContanView(mListView);
		mListView.setAdapter(mListAdapter);
		frist_run = 0;
		getTypeTag();
	}

	/**
	 * 改变listitem的背景色
	 *
	 * @param view
	 */
	private void itemBackChanged(View view)
	{
		if (itemView == null)
		{
			itemView = view;
		}

		mBgColor = R.color.text_color_003b;
		itemView.setBackgroundResource(mBgColor);
		mBgColor = R.color.text_color_26;
		// 将上次点击的listitem的背景色设置成透明
		view.setBackgroundResource(mBgColor);
		// 设置当前点击的listitem的背景色
		itemView = view;

	}

	public int getScrollY()
	{
		View c = mListView.getRefreshableView().getChildAt(0);
		if (c == null)
		{
			return 0;
		}
		int firstVisiblePosition = mListView.getRefreshableView().getFirstVisiblePosition();
		int top = c.getTop();
		return -top + firstVisiblePosition * c.getHeight();
	}

	@Override
	public void onLoadingCancelled(String arg0, View arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingStarted(String arg0, View arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0)
	{
		if (mItem.isShown())
		{
			mItem.startAnimation(mHiddenAnimation);
		}
		mItem.setVisibility(View.GONE);
	}

	@Override
	public View getInfoContents(Marker arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMarkerClick(Marker arg0)
	{
		// initData();
		addMyActivity(arg0);
		return true;
	}

	/**
	 * 最新活动自定义消息框
	 *
	 * @param marker
	 */
	private void addMyActivity(Marker marker)
	{
		addActivityMakrtReturn(EventlistItem);
		final EventInfo eif = (EventInfo) marker.getObject();
		// 显示标记
		marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_mapon)));
		getLocal(Double.valueOf(eif.getEventLat()), Double.valueOf(eif.getEventLon()));
		if (!mItem.isShown())
		{
			mItem.startAnimation(mShowAnimation);
		}
		double lat = Double.parseDouble(eif.getEventLat());
		double lon = Double.parseDouble(eif.getEventLon());
		LatLng ll1 = new LatLng(lat, lon);
		MarkerOptions markerOption1 = new MarkerOptions();
		markerOption1.position(ll1);
		markerOption1.draggable(true);
		markerOption1.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_mapon)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption1.setFlat(true);
		aMap.addMarker(markerOption1);
		// view.setBackgroundResource(R.drawable.icon_mapon);
		mItem.setVisibility(View.VISIBLE);
		MyApplication.getInstance().getImageLoader().displayImage(TextUtil.getUrlMiddle(eif.getEventIconUrl()), mItemImg, this);
		mItemTitle.setText(eif.getEventName());
		if (eif.getActivityLocationName() != null && !eif.getActivityLocationName().equals(""))
		{
			mItemAddress.setText(eif.getActivityLocationName());
		} else
		{
			mItemAddress.setText(eif.getActivityAddress());
		}
		if (eif.getActivityStartTime() == null || eif.getEventEndTime() == null)
		{
			String start_time = eif.getActivityStartTime().replaceAll("-", ".");
			start_time = start_time.substring(5, 10);
			mItemData.setText(start_time);
		} else
		{
			String start_time = eif.getActivityStartTime().replaceAll("-", ".");
			start_time = start_time.substring(5, 10);
			String end_time = eif.getEventEndTime().replaceAll("-", ".");
			end_time = end_time.substring(5, 10);
			mItemData.setText(start_time + "-" + end_time);
		}
		if (eif.getActivityPrice().equals("0"))
		{
			mItemPrice.setText("免费");
		} else
		{
			mItemPrice.setText(eif.getActivityPrice() + "元");
		}
		Float distance = Float.valueOf(eif.getDistance().substring(0, 3));
		if (distance > 1.0)
		{
			mItemDistance.setText(distance + "KM");
		} else
		{
			mItemDistance.setText(distance * 1000 + "M");
		}

		if (eif.getActivityAbleCount() != null && eif.getActivityAbleCount().length() > 0)
		{
			if (eif.getActivityAbleCount().equals("0"))
			{
				mItemTicket.setVisibility(View.GONE);
				map_book.setVisibility(View.GONE);
			} else
			{
				mItemTicket.setVisibility(View.GONE);
				mItemTicket.setText("剩余" + eif.getActivityAbleCount() + "张票");
				map_book.setVisibility(View.GONE);
				// mItemTicket.setText("余票：" + eif.getActivityAbleCount());
			}
		} else
		{
			mItemTicket.setText("");
			map_book.setVisibility(View.GONE);
		}

		map_book.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put(HttpUrlList.EventUrl.ACTIVITY_ID, eif.getEventId());

				params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
				MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.WFF_HTTP_EVENT_URL, params, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode)
						{
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
							{
								eventInfo = JsonUtil.getEventInfo(resultStr);
								Intent intent = new Intent(getActivity(), EventReserveActivity.class);
								intent.putExtra("EventInfo", eventInfo);
								startActivity(intent);
							} else
							{

							}
						}
					}
				});
			}
		});

		mItem.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (mContext != null && eif.getEventId() != null)
				{
					Intent intent = new Intent(mContext, ActivityDetailActivity.class);
					intent.putExtra("eventId", eif.getEventId());
					mContext.startActivity(intent);
				}
			}
		});
	}

	/**
	 * 0 表示 放大title 1表示 缩小title
	 *
	 * @param animation_type
	 */
	public void startTitleAnimation(final int animation_type)
	{
		int tHeight = mtitl.getMeasuredHeight();
		if ((animation_type == 0 && tHeight == title_heigth) || (animation_type == 1 && tHeight == 0))
		{
			return;
		}
		Log.i(TAG, "--startTitleAnimation");
		ValueAnimator animator_title;
		if (animation_type == 0)
		{
			animator_title = ValueAnimator.ofInt(0, title_heigth);
		} else
		{
			animator_title = ValueAnimator.ofInt(title_heigth, 0);
		}

		animator_title.setDuration(500);
		animator_title.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		{
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator)
			{

				android.widget.LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) mtitl.getLayoutParams();
				int margin_height = (Integer) valueAnimator.getAnimatedValue();
				ll.height = margin_height;
				Log.i("animation", animation_type + "--" + margin_height);
				mtitl.setLayoutParams(ll);
			}
		});
		animator_title.addListener(new Animator.AnimatorListener()
		{
			@Override
			public void onAnimationStart(Animator animator)
			{
				titleIsAnimating = true;
				if (animation_type == 0)
				{
					animation_change = true;
				} else
				{
					animation_change = false;
				}

			}

			@Override
			public void onAnimationEnd(Animator animator)
			{
				titleIsAnimating = false;
			}

			@Override
			public void onAnimationCancel(Animator animator)
			{
				titleIsAnimating = false;
			}

			@Override
			public void onAnimationRepeat(Animator animator)
			{

			}
		});
		animator_title.start();
	}

	OnItemClickListener three_item = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0.getItemAtPosition(arg2);
			if (arg2 == 0)
			{
				activityIsFree = "1";
				activityIsReservation = "";
			} else if (arg2 == 1)
			{
				activityIsFree = "";
				activityIsReservation = "2";
			}
			if (pw != null)
			{
				pw.dismiss();
				sieve.setText(wvp.getDictName());
				if (type_num == 1)
				{
					sieve_ll.setVisibility(View.GONE);

				} else
				{
					sieve_ll.setVisibility(View.VISIBLE);
				}
				mListAdapter.setSieveVisible(3, wvp.getDictName());
			}
			getListData(0);
			isRefresh = true;
		}
	};

	OnItemClickListener second_item = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) parent.getItemAtPosition(position);
			sortType = position + 1;
			mListAdapter.setType(sortType);
			if (pw != null)
			{
				pw.dismiss();
				preface.setText(wvp.getDictName());
				if (type_num == 1)
				{
					sieve_ll.setVisibility(View.GONE);
				} else
				{
					sieve_ll.setVisibility(View.VISIBLE);
				}
				mListAdapter.setSieveVisible(2, wvp.getDictName());
			}
			getListData(0);
			isRefresh = true;

		}
	};
	OnItemClickListener right_item = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0.getItemAtPosition(arg2);
			if (arg2 == 0)
			{
				venueMood = "";
				venueMoodName = wvp.getTagName();
				shopping_areas.setText(wvp.getTagName());

				if (type_num == 1)
				{
					sieve_ll.setVisibility(View.GONE);
				} else
				{
					sieve_ll.setVisibility(View.VISIBLE);
				}
				if (pw != null)
				{
					pw.dismiss();
				}
				mListAdapter.setSieveVisible(1, wvp.getTagName());
				getListData(0);
				isRefresh = true;
				return;
			}
			venueMood = wvp.getTagId();
			venueMoodName = wvp.getTagName();
			shopping_areas.setText(wvp.getTagName());
			if (type_num == 1)
			{
				// sieve_ll.setVisibility(View.GONE);
			} else
			{
				// sieve_ll.setVisibility(View.VISIBLE);
			}
			mListAdapter.setSieveVisible(1, wvp.getTagName());
			getListData(0);
			isRefresh = true;
			if (pw != null)
			{
				pw.dismiss();
			}
		}
	};
	OnItemClickListener left_item = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3)
		{
			if (arg2 == 0)
			{
				venueArea = "";
				venueDicName = "";
				venueMood = "";
				venueMoodName = "";
				shopping_areas.setText("全上海");
				getListData(0);
				isRefresh = true;
				pw.dismiss();
				if (type_num == 1)
				{
					sieve_ll.setVisibility(View.GONE);
				} else
				{
					if (mAddList.size() != 0 && mAddList.get(0).getIsContainActivtiyAdv() == 1)
					{
						sieve_ll.setVisibility(View.VISIBLE);
					} else
					{

					}
				}
				mListAdapter.setSieveVisible(1, "全上海");
				return;
			}
			view = arg1;
			itemBackChanged(arg1);

			wvpw = (Wff_VenuePopuwindow) parent.getItemAtPosition(arg2);
			venueArea = wvpw.getDictCode();
			venueDicName = wvpw.getDictName();
			list_area_z = new ArrayList<Wff_VenuePopuwindow>();
			list_area_z.add(new Wff_VenuePopuwindow("", "全部" + wvpw.getDictName()));
			for (int i = 0; i < wvpw.getDictList().length(); i++)
			{
				try
				{
					JSONObject json = wvpw.getDictList().getJSONObject(i);
					list_area_z.add(new Wff_VenuePopuwindow(json.optString("id"), json.optString("name")));
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}

			HomePopuWindowAreaAdapter vpwaa = new HomePopuWindowAreaAdapter(list_area_z, getActivity());
			listView_right.setAdapter(vpwaa);
			listView_right.setOnItemClickListener(right_item);
		}
	};

	/**
	 * 地图的接口
	 */
	private void setActivityListData(int page)
	{
		EventlistItem.clear();
		DataService.getActivityByNear(tagId, new DataService.ReceiveCallBack()
		{

			@Override
			public void dataReceviceSuccessCallBack(Object data)
			{
				List<EventInfo> list = (List<EventInfo>) data;
				if (list.size() > 0)
				{
					EventlistItem.addAll(list);
					addActivityMakrt(EventlistItem);
				} else
				{
					EventlistItem.clear();
					addActivityMakrt(EventlistItem);
				}

			}

			@Override
			public void dataReceviceFailedCallBack(String feedback)
			{

			}
		});

	}

	OnRefreshListener2 myRefresh = new OnRefreshListener2<ListView>()
	{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			// TODO Auto-generated method stub
			onResh(tagId);
			getTip();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
		{
			// TODO Auto-generated method stub
			onAddmoreData();
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		// TODO Auto-generated method stub
		if (hidden)
		{
			mMapView.setVisibility(View.GONE);
			// warn_mapView.setVisibility(View.GONE);
		} else
		{
			mMapView.setVisibility(View.VISIBLE);
			// warn_mapView.setVisibility(View.VISIBLE);
		}
	}
}
