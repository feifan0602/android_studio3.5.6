package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.NewDetailsActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.activity.VoteDetailActivity;
import com.sun3d.culturalShanghai.adapter.VenueListAdapter;
import com.sun3d.culturalShanghai.adapter.VenueListAdapter.OnMyClickListener;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.handler.Tab_labelHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.view.AutoListView.OnLoadListener;
import com.sun3d.culturalShanghai.view.AutoListView.OnRefreshListener;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.sun3d.culturalShanghai.windows.VenuePopuwindow;
import com.sun3d.culturalShanghai.windows.VenueWindows;

//
/**
 * 场馆
 * 
 * 
 * @author wenff
 * 
 */
@SuppressLint("ValidFragment")
public class VenueFragment extends Fragment implements OnClickListener,
		OnRefreshListener, OnLoadListener, RefreshListenter, OnMyClickListener {
	private LinearLayout venue_ll, head_sieve_ll;
	private RelativeLayout loadingLayout;
	private PullToRefreshListView mListView;
	private static List<VenueDetailInfor> list;
	private VenueListAdapter mAdapter;
	private LoadingHandler mLoadingHandler;
	private int mPage = 0;
	private Boolean isRefresh = false;
	private String venueType = "3";// 活动类型 1->距离 3.热门 4.所有活动（必选）
	private View Topview;
	private Boolean isFirstResh = true;
	private ImageView right_iv;
	private TextView middle_tv, left_tv;
	private ImageView fenge_iv;
	private boolean addmore_bool = true;
	private RelativeLayout mTitle;
	/**
	 * 区域，分类，排序，状态
	 */
	public TextView region, classification, sort, status;
	public boolean region_bool = true;
	public boolean classification_bool = true;
	public boolean sort_bool = true;
	public boolean status_bool = true;
	private TextView collection_null;

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_venue, null);
		collection_null = (TextView) view.findViewById(R.id.collection_null);
		region = (TextView) view.findViewById(R.id.region);
		classification = (TextView) view.findViewById(R.id.classification);
		sort = (TextView) view.findViewById(R.id.sort);
		fenge_iv = (ImageView) view.findViewById(R.id.fenge_iv);
		status = (TextView) view.findViewById(R.id.status);
		right_iv = (ImageView) view.findViewById(R.id.right_iv);
		middle_tv = (TextView) view.findViewById(R.id.middle_tv);
		left_tv = (TextView) view.findViewById(R.id.left_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		region.setTypeface(MyApplication.GetTypeFace());
		classification.setTypeface(MyApplication.GetTypeFace());
		sort.setTypeface(MyApplication.GetTypeFace());
		status.setTypeface(MyApplication.GetTypeFace());
		left_tv.setText(" ");
		middle_tv.setText("场馆");

		mTitle = (RelativeLayout) view.findViewById(R.id.title);
		right_iv.setOnClickListener(this);
		region.setOnClickListener(this);

		classification.setOnClickListener(this);
		sort.setOnClickListener(this);
		status.setOnClickListener(this);
		mListView = (PullToRefreshListView) view.findViewById(R.id.venue_list);

		View view_head = LayoutInflater.from(getActivity()).inflate(
				R.layout.activity_null_top, null);
		head_sieve_ll = (LinearLayout) view_head.findViewById(R.id.top_null_ll);
		mListView.getRefreshableView().addHeaderView(view_head);
		venue_ll = (LinearLayout) view.findViewById(R.id.venue_ll);
		mListView.setOnScrollListener(new My_ScrollListener());
		Topview = (View) view.findViewById(R.id.activity_page_line);
		loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		/**
		 * 上下拉刷新
		 */
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// 下拉刷新
				onResh();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				// 上拉加载更多
				onAddmoreData();
			}

		});
		return view;
	}

	/**
	 * 下拉刷新
	 */
	public void onResh() {
		mPage = 0;
		isRefresh = true;
		addData(mPage);
	}

	/**
	 * 上拉加载更多
	 */
	private void onAddmoreData() {
		mPage = HttpUrlList.HTTP_NUM + mPage;
		addmore_bool = true;
		addData(mPage);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		isRefresh = false;
		isFirstResh = true;
		initData();
	}

	private void initData() {
		isRefresh = true;
		mListView.setOnScrollListener(new PauseOnScrollListener(MyApplication
				.getInstance().getImageLoader(), true, false));
		mPage = 0;
		list = new ArrayList<VenueDetailInfor>();
		mAdapter = new VenueListAdapter(getActivity(), list);
		// 这里是选择上下拉加载的布局 点击事件的回掉
		mAdapter.setOnMyClickListener(this);
		mAdapter.isVenueMainList(true);
		mAdapter.isShowFootView(true);
		mListView.setAdapter(mAdapter);
		addData(mPage);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				/**
				 * 防止多次点击和选择头部和底部的点击事件
				 */
				if (arg2 != 0) {
					arg2 = arg2 - 1;
				}
				if (arg2 < 0 || arg2 >= list.size()) {
					return;
				}
				if (!ButtonUtil.isDelayClick()) {
					return;
				}
				// 传展馆ID 到展馆详情页面
				Intent intent = new Intent(getActivity(),
						VenueDetailActivity.class);
				intent.putExtra("venueId", list.get(arg2).getVenueId());
				startActivity(intent);
			}
		});
	}

	private void addData(int page) {
		MyApplication.getInstance().setVenueType(venueType);
		Map<String, String> mParams = new HashMap<String, String>();
		if (!AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
				&& !AppConfigUtil.LocalLocation.Location_longitude.equals("0")) {
			mParams.put(HttpUrlList.HTTP_LAT,
					AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON,
					AppConfigUtil.LocalLocation.Location_longitude + "");
		} else {
			mParams.put(HttpUrlList.HTTP_LAT, 31.280842 + "");
			mParams.put(HttpUrlList.HTTP_LON, 121.433594 + "");
		}

		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, page + "");
		mParams.put("appType", venueType);
		// 分类
		if (MyApplication.venueType_classication != ""
				&& MyApplication.venueType_classication != null) {
			mParams.put("venueType", MyApplication.venueType_classication);
		}
		// 商圈
		if (MyApplication.venueMood != "" && MyApplication.venueMood != null) {
			mParams.put("venueMood", MyApplication.venueMood);
		}
		// 区域
		if (MyApplication.venueArea != "" && MyApplication.venueArea != null) {
			mParams.put("venueArea", MyApplication.venueArea);
		}
		// 是否可预订 2 可预订 1 全部
		if (MyApplication.venueIsReserve != ""
				&& MyApplication.venueIsReserve != null) {
			mParams.put("venueIsReserve", MyApplication.venueIsReserve);
		}
		// 排序 1 浏览数 2距离
		if (MyApplication.sortType != "" && MyApplication.sortType != null) {
			mParams.put("sortType", MyApplication.sortType);
		}
		if (MyApplication.venueMoodName != ""
				&& MyApplication.venueMoodName != null) {
			region.setText(MyApplication.venueMoodName);
		}
		if (MyApplication.venueType_classication_Name != ""
				&& MyApplication.venueType_classication_Name != null) {
			classification.setText(MyApplication.venueType_classication_Name);
		}
		if (MyApplication.sortType_Name != ""
				&& MyApplication.sortType_Name != null) {
			sort.setText(MyApplication.sortType_Name);
		}
		if (MyApplication.venueIsReserve_Name != ""
				&& MyApplication.venueIsReserve_Name != null) {
			status.setText(MyApplication.venueIsReserve_Name);
		}
		mParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());

		MyApplication.total_num(HttpUrlList.Venue.WFF_APPVENUELIST,
				"appVenueList", "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.WFF_APPVENUELIST,
				mParams, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是场馆的数据" + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {

							mListView.setVisibility(View.VISIBLE);
							collection_null.setVisibility(View.GONE);
							List<VenueDetailInfor> ddlist = new ArrayList<VenueDetailInfor>();
							ddlist = JsonUtil.getVenueListInfoList(resultStr);
							if (ddlist != null) {
								if (ddlist.size() > 0) {
									collection_null.setVisibility(View.GONE);
									mListView.setVisibility(View.VISIBLE);
									if (isRefresh) {
										list.clear();
										isRefresh = false;
										VenueDetailInfor vd = new VenueDetailInfor();
										ddlist.add(0, vd);
									}
									list.addAll(ddlist);
									mAdapter.setList(list);
									setTab_labelHandlerBg();
								} else {
									if (addmore_bool) {

									} else {
										collection_null
												.setVisibility(View.VISIBLE);
										mListView.setVisibility(View.GONE);
										isFirstResh = true;
										addmore_bool = false;
									}
									// LoadingTextShowPopWindow.showLoadingText(getActivity(),
									// Topview, "数据为空");
								}

								mAdapter.notifyDataSetChanged();
							} else {
								mLoadingHandler.showErrorText("数据为空");
							}
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
							ToastUtil.showToast("数据请求失败:" + resultStr);
						}
						handler.sendEmptyMessageDelayed(REFRESH, 200);
					}
				});
	}

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
				if (!isFirstResh) {
					LoadingTextShowPopWindow.showLoadingText(getActivity(),
							Topview, "数据加载完成");
				}
				isFirstResh = false;
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.list_foot_text:
			mPage = HttpUrlList.HTTP_NUM + mPage;
			addData(mPage);
			break;
		case R.id.right_iv:
			VenueWindows.getInstance(getActivity()).show(mTitle, true, "场馆");
			break;
		case R.id.region:
			MyApplication.type_num = 1;
			if (region_bool) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_chooseup);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				region.setCompoundDrawables(null, null, drawable, null);// 画在右边
				int[] location = new int[2];
				fenge_iv.getLocationOnScreen(location);
				int x = location[0];
				int y = location[1];
				MyApplication.venue_tv_height = y;

				VenuePopuwindow.getInstance(getActivity(), this).showSearch(
						arg0);
				region_bool = false;
			} else {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_choosearrow);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				region.setCompoundDrawables(null, null, drawable, null);// 画在右边
				region_bool = true;
			}

			break;
		case R.id.classification:
			MyApplication.type_num = 2;
			if (classification_bool) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_chooseup);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				classification.setCompoundDrawables(null, null, drawable, null);// 画在右边

				VenuePopuwindow.getInstance(getActivity(), this).showSearch(
						arg0);
				classification_bool = false;
			} else {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_choosearrow);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				classification.setCompoundDrawables(null, null, drawable, null);// 画在右边
				classification_bool = true;
			}

			break;
		case R.id.sort:
			MyApplication.type_num = 3;
			if (sort_bool) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_chooseup);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				sort.setCompoundDrawables(null, null, drawable, null);// 画在右边

				VenuePopuwindow.getInstance(getActivity(), this).showSearch(
						arg0);
				sort_bool = false;
			} else {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_choosearrow);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				sort.setCompoundDrawables(null, null, drawable, null);// 画在右边
				sort_bool = true;
			}

			break;
		case R.id.status:
			MyApplication.type_num = 4;
			if (status_bool) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_chooseup);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				status.setCompoundDrawables(null, null, drawable, null);// 画在右边

				VenuePopuwindow.getInstance(getActivity(), this).showSearch(
						arg0);
				status_bool = false;
			} else {
				Drawable drawable = getResources().getDrawable(
						R.drawable.icon_choosearrow);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				status.setCompoundDrawables(null, null, drawable, null);// 画在右边
				status_bool = true;
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		mListView.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mPage = 0;
		isRefresh = true;
		mLoadingHandler.startLoading();
		addData(mPage);
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		mPage = 0;
		isRefresh = true;
		addData(mPage);
		mLoadingHandler.startLoading();
	}

	/**
	 * 标签背景修改
	 */
	private void setTab_labelHandlerBg() {
		if (mTab_labelHandler == null) {
			return;
		}
		if ("1".equals(venueType)) {
			mTab_labelHandler.setTabNebaty();
		} else if ("2".equals(venueType)) {
			mTab_labelHandler.setTabStart();
		} else {
			mTab_labelHandler.setTabHot();
		}

	}

	private Tab_labelHandler mTab_labelHandler;

	@Override
	public void onMyClick(View arg0, Tab_labelHandler mTab_labelHandler) {
		// TODO Auto-generated method stub
		this.mTab_labelHandler = mTab_labelHandler;
		switch (arg0.getId()) {
		case R.id.tab_nebaty_btn:// 附近
			venueType = "1";
			mListView.setRefreshing();
			break;

		case R.id.tab_hot_btn:// 热门
			venueType = "3";
			mListView.setRefreshing();
			break;
		default:
			break;
		}
	}

	class My_ScrollListener implements OnScrollListener {

		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int arg2,
				int arg3) {
			int top2 = head_sieve_ll.getTop();
			int top_margin = 0;
			if (top2 < 0 || firstVisibleItem >= 1) {
				android.widget.FrameLayout.LayoutParams ll = (android.widget.FrameLayout.LayoutParams) venue_ll
						.getLayoutParams();
				ll.setMargins(0, 30, 0, 0);
				venue_ll.setVisibility(View.VISIBLE);
				venue_ll.setLayoutParams(ll);
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}
}
