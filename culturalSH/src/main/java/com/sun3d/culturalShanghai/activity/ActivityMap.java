package com.sun3d.culturalShanghai.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.adapter.WeekItemAdapter;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.windows.WeekPopupWindow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是高德地图 附近的活动 Created by wangmingming on 2016/1/27.
 */
public class ActivityMap extends Activity implements View.OnClickListener,
		AdapterView.OnItemClickListener, LoadingHandler.RefreshListenter,
		AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener,
		AMap.InfoWindowAdapter, AMap.OnMapClickListener, ImageLoadingListener {
	private Context mContext;
	private ImageButton title_left;
	private TextView title_right;
	private EventInfo eventInfo;
	private List<EventInfo> EventlistItem;
	private MapView mMapView; // 小地图，大地图
	private RelativeLayout titleLayout;
	private AMap aMap;
	private TextView title;
	private MarkerOptions markerOption;
	// private GaoDeMapPoiSeahUtil mGaoDeMapPoiSeahUtil;
	private int ListType = 1;// 1为活动，2为展馆。
	private final String mPageName = "ActivityMap";
	private int mPage = 0;
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
	private ImageButton title_right_btn;
	private List<UserBehaviorInfo> mflagList;
	private LatLng ll;
	private double lat;
	private double lon;
	private RelativeLayout mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		initView();
		if (mMapView != null) {
			mMapView.onCreate(savedInstanceState);
		}

		mLoadingDialog = new LoadingDialog(mContext);
		initMap();
		EventlistItem = new ArrayList<EventInfo>();
		mflagList = new ArrayList<UserBehaviorInfo>();
		mflagList.clear();
		getTypeData();
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

	public StringBuffer removedouhao(StringBuffer stringBuffer) {

		if (stringBuffer.length() > 0) {
			stringBuffer = stringBuffer.replace(stringBuffer.length() - 1,
					stringBuffer.length(), "");
		}
		return stringBuffer;
	}

	/**
	 * 设置活动数据处理
	 */
	private void setActivityListData(int page) {
		Map<String, String> mParams = new HashMap<String, String>();
		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				|| AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")) {
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		}

		mParams.put("appType", "1");// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
		mParams.put("activityTypeId", getTag(WeekItemAdapter.indexSelect)
				.toString());
		/**
		 * 這裡是要變化的日期
		 */
		mParams.put("everyDate", "2016-04-26");
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, 100 + "");
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
		EventlistItem.clear();
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyEvent.WFF_ACTIVITYLISTURL_MAP, mParams,
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
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
	}

	/**
	 * 初始化地图
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			setUpMap();
		}
		// 隐藏放大缩小按钮
		UiSettings uiSettings = aMap.getUiSettings();
		//uiSettings.setZoomControlsEnabled(false);
		//uiSettings.setMyLocationButtonEnabled(false);
		// mGaoDeMapPoiSeahUtil = new GaoDeMapPoiSeahUtil(mContext, aMap);

	}

	/**
	 * 地图设置
	 */
	private void setUpMap() {
		if (AppConfigUtil.LocalLocation.Location_latitude != ""
				&& AppConfigUtil.LocalLocation.Location_longitude != "") {
			lat = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_latitude);
			lon = Double
					.parseDouble(AppConfigUtil.LocalLocation.Location_longitude);
			getLocal(lat, lon);
		} else {
			lat = 12.0;
			lon = 12.0;
			getLocal(lat, lon);
		}
		//
		// aMap.setLocationSource(new MyLocationSource(this, aMap));// 设置定位监听
		//aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
		// 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
		// 显示标记
		markerOption = new MarkerOptions();
		markerOption.position(ll);
		markerOption.draggable(true);
		markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.location_marker)));
		// 将Marker设置为贴地显示，可以双指下拉看效果
		markerOption.setFlat(true);
		aMap.addMarker(markerOption);
	}

	private void getLocal(double lat, double lon) {
		ll = new LatLng(lat, lon);
		// 初始化当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
		aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
	}

	/**
	 * 初始化信息
	 */
	@SuppressWarnings("ResourceType")
	@SuppressLint("CutPasteId")
	private void initView() {
		// 标题栏
		titleLayout = (RelativeLayout) findViewById(R.id.nearby_title);
		title_left = (ImageButton) titleLayout.findViewById(R.id.title_left);
		title_right_btn = (ImageButton) titleLayout
				.findViewById(R.id.title_right);
		title_right_btn.setVisibility(View.GONE);
		title_right = (TextView) titleLayout.findViewById(R.id.title_send);
//		title_right.setText("筛选");
//		title_right.setTextColor(Color.WHITE);
//		title_right.setTextSize(17);
		drawable = getResources()
				.getDrawable(R.drawable.sh_activity_title_pull);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
//		title_right.setCompoundDrawables(null, null, drawable, null);
		title = (TextView) titleLayout.findViewById(R.id.title_content);
		tv_map_location = (TextView) findViewById(R.id.map_location);
		tv_map_location.setVisibility(View.VISIBLE);
		mItem = (RelativeLayout) findViewById(R.id.dialog_activity_map);
		mItemImg = (ImageView) findViewById(R.id.dialog_activity_map_img);
		mItemTitle = (TextView) findViewById(R.id.dialog_activity_map_title);
		mItemAddress = (TextView) findViewById(R.id.dialog_activity_map_address);
		mItemData = (TextView) findViewById(R.id.dialog_activity_map_data);
		mItemDistance=(TextView) findViewById(R.id.mItemDistance);
		mItemTicket = (TextView) findViewById(R.id.dialog_activity_map_ticket);
		mItemPrice = (TextView) findViewById(R.id.mItemPrice);
		map_book = (TextView) findViewById(R.id.map_book);

		mShowAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.dialog_bottom_up);
		mHiddenAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.dialog_bottom_down);
		title.setVisibility(View.VISIBLE);
		title.setText("附近活动");
		// title_right.setImageResource(R.drawable.sh_icon_search_nearby);
		// title_right.setOnClickListener(this);
//		title_right.setVisibility(View.VISIBLE);
		mMapView = (MapView) findViewById(R.id.activity_amap);
		title_left.setOnClickListener(this);
//		title_right.setOnClickListener(this);
		tv_map_location.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		super.onStart();

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int version = Integer.valueOf(android.os.Build.VERSION.SDK);
		Intent intent = null;
		switch (view.getId()) {
		case R.id.title_left:
			finish();
			mflagList.clear();
			break;
		case R.id.title_send:
			if (mItem.isShown()) {
				mItem.startAnimation(mHiddenAnimation);
			}
			mItem.setVisibility(View.GONE);
			mWeekPopupWindow.showWeekPopupWindow(mContext, titleLayout,
					title_right, drawable, mflagList, new WeekPopupCallback() {
						@Override
						public void referShing() {
							setActivityListData(mPage);
						}
					});
			break;
		case R.id.nearby_map_controls: // 放大
			intent = new Intent(mContext, BigMapViewActivity.class);
			intent.putExtra(AppConfigUtil.INTENT_TYPE, "1");
			intent.putExtra("titleContent", title.getText().toString());
			startActivity(intent);
			if (version > 5) {
				overridePendingTransition(R.anim.getinto_animation,
						R.anim.exit_animation);
			}
			break;
		case R.id.map_location:
			getLocal(lat, lon);
			break;
		default:
			break;
		}
	}

	/**
	 * 条目单击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg2 - 1 > -1) {
			if (ListType == 1) {
				EventInfo eventInfo = EventlistItem.get(arg2 - 1);
				Intent intent = new Intent(mContext, EventDetailsActivity.class);
				intent.putExtra("eventId", eventInfo.getEventId());
				startActivity(intent);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
		if (mMapView != null)
			mMapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
		if (mMapView != null)
			mMapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (mMapView != null)
			mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMapView != null)
			mMapView.onDestroy();
		if (aMap == null) {
			aMap.clear();
		}
		WeekItemAdapter.init(false);
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

		if (listItem != null) {
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

	// 提取图像Alpha位图
	public static Bitmap getAlphaBitmap(Bitmap mBitmap, int mColor) {
		// BitmapDrawable mBitmapDrawable = (BitmapDrawable)
		// mContext.getResources().getDrawable(R.drawable.enemy_infantry_ninja);
		// Bitmap mBitmap = mBitmapDrawable.getBitmap();

		// BitmapDrawable的getIntrinsicWidth（）方法，Bitmap的getWidth（）方法
		// 注意这两个方法的区别
		// Bitmap mAlphaBitmap =
		// Bitmap.createBitmap(mBitmapDrawable.getIntrinsicWidth(),
		// mBitmapDrawable.getIntrinsicHeight(), Config.ARGB_8888);
		Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Bitmap.Config.ARGB_8888);

		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();

		mPaint.setColor(mColor);
		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = mBitmap.extractAlpha();
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);

		return mAlphaBitmap;
	}

	Drawable bitmap2Drawable(Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	public int getFontHeight(float fontSize) {
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

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		initData();
	}

	@Override
	public void onLoadingStarted(String s, View view) {

	}

	@Override
	public void onLoadingFailed(String s, View view, FailReason failReason) {

	}

	@Override
	public void onLoadingComplete(String s, View view, Bitmap bitmap) {

	}

	@Override
	public void onLoadingCancelled(String s, View view) {

	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

	}

	@Override
	public void onMapClick(LatLng latLng) {

		if (mItem.isShown()) {
			mItem.startAnimation(mHiddenAnimation);
		}
		mItem.setVisibility(View.GONE);

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		addMyActivity(marker);
		return true;
	}

	/**
	 * 最新活动自定义消息框
	 * 
	 * @param marker
	 */
	private void addMyActivity(Marker marker) {
		final EventInfo eif = (EventInfo) marker.getObject();
		getLocal(Double.valueOf(eif.getEventLat()),
				Double.valueOf(eif.getEventLon()));
		if (!mItem.isShown()) {
			mItem.startAnimation(mShowAnimation);
		}
		mItem.setVisibility(View.VISIBLE);
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlMiddle(eif.getEventIconUrl()),
						mItemImg, this);
		mItemTitle.setText(eif.getEventName());
		mItemAddress.setText(eif.getActivitySite());
		if (eif.getActivityStartTime()==null||eif.getActivityEventimes()==null) {
			String start_time=eif.getActivityStartTime().replaceAll("-", ".");
			start_time=start_time.substring(5, 10);
			mItemData.setText(start_time);
		}else {
			String start_time=eif.getActivityStartTime().replaceAll("-", ".");
			start_time=start_time.substring(5, 10);
			String end_time=eif.getActivityEventimes().replaceAll("-", ".");
			end_time=end_time.substring(5, 10);
			mItemData.setText(start_time+"-"+end_time);
		}
		if (eif.getActivityPrice().equals("0")) {
			mItemPrice.setText("免费");
		}else {
			mItemPrice.setText(eif.getActivityPrice()+"元");
		}
		mItemDistance.setText(eif.getDistance()+"m");
		if (eif.getActivityAbleCount() != null
				&& eif.getActivityAbleCount().length() > 0) {
			if (eif.getActivityAbleCount().equals("0")) {
				mItemTicket.setVisibility(View.GONE);
				map_book.setVisibility(View.GONE);
			} else {
				mItemTicket.setVisibility(View.VISIBLE);
				mItemTicket.setText("剩余" + eif.getActivityAbleCount() + "张票");
				map_book.setVisibility(View.VISIBLE);
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
												ActivityMap.this,
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

	public interface WeekPopupCallback extends
			ThisWeekActivity.WeekPopupCallback {
	}
}