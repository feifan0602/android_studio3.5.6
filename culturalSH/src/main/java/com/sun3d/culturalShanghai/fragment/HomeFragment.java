package com.sun3d.culturalShanghai.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.NetWorkUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.HomeDetail_Content;
import com.sun3d.culturalShanghai.activity.HomeDetail_HorizontalListView;
import com.sun3d.culturalShanghai.activity.HomeDetail_Index;
import com.sun3d.culturalShanghai.activity.HomeDetail_TopLayout;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity;
import com.sun3d.culturalShanghai.activity.SearchActivity;
import com.sun3d.culturalShanghai.adapter.HomePopGridAdapter;
import com.sun3d.culturalShanghai.adapter.HomePopListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.HomeDetail_ContentInfor;
import com.sun3d.culturalShanghai.object.HomeImgInfo;
import com.sun3d.culturalShanghai.service.DownNewApkService;
import com.sun3d.culturalShanghai.service.LocationMyLatService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 这个是首页的
 *
 * @author liningkang
 */
public class HomeFragment extends Fragment implements OnClickListener,
        OnRefreshListener2<ScrollView>, RefreshListenter {
    @Bind(R.id.address_tv)
    TextView mAddressTv;
    @Bind(R.id.city_item_gv)
    GridView mCityItemGv;
    @Bind(R.id.listview)
    ListView mListview;
    @Bind(R.id.gps_tv)
    TextView mGpsTv;
    @Bind(R.id.select_tv)
    TextView mSelectTv;
    @Bind(R.id.all_address)
    TextView mAllAddress;
    private View mView;
    private PullToRefreshScrollView scrroll_view;
    private LinearLayout content_layout;
    private Context mContext;
    private HomeDetail_TopLayout mHomeDetail_TopLayout; // 顶部的图片 和橫向滑動的Listview
    private HomeDetail_Index mHomeDetail_Index; // 日历 和场馆等
    private HomeDetail_HorizontalListView mHomeDetail_HorizontalListView;// 为你推荐
    private HomeDetail_Content mHomeDetail_Content;// 猜你喜歡
    public TextView left_tv, middle_tv;
    public ImageView right_iv;
    private String TAG = "HomeFragment";
    private JSONObject json;
    private JSONObject json_data;
    private List<HomeImgInfo> mImgList;
    private List<HomeImgInfo> mImgHomeIndex;
    private List<EventInfo> mAddressSelect;
    private List<HomeDetail_ContentInfor> mDataList;
    private List<HomeDetail_ContentInfor> mList;
    private RelativeLayout loadingLayout;
    private LoadingHandler mLoadingHandler;
    private int pageIndex = 0;
    private boolean isRefresh = false;
    private View popupWindow_view, popupWindowNoLat_view, popupWindowChangeCity_view;
    private PopupWindow popupWindow, popupWindowNoLat, popupWindowChangeCity;
    private HomePopGridAdapter mHomePopGridAdapter;
    private HomePopListAdapter mHomePopListAdapter;
    private List<EventInfo> mPopList;
    private List<EventInfo> mPopGvList;
    private int oldPostion = -1;
    private int isFristRun = 0;
    private TextView address_Nolocation_tv, address_location_tv;
    private String newIp;
    private Button mLeftNoLat_btn, mRightNolat_btn;
    private Button mLeftChange_btn, mRightChange_btn;
    private MainFragmentActivity mfc;
    private String location_str;
    private Location_address mLocationAddress;
    private LocationMyLatService mLocationMyLatService;
    private Intent iLocService;
    private Intent updateAPK;
    private LocationMyLatService.MyBind mMyBind;
    private DownNewApkService.MyDownApkBind mDownApkBind;
    private String PreviousAddress = "";
    public static String cityCode = "";
    private TextView tv, tv1, tv2, tv3, tv4;
    private TextView top_banner_address_tv;
    private TextView closetime_tv;
    private Button change_btn;
    private TextView top_banner_address_china_tv;
    private Button change_china_btn;
    //全国的接口  要全部从 getImgData中获取猜你喜欢的数据
    private boolean all_city_bol = false;
    //全国的接口  改变猜我喜欢的布局
    private boolean all_city_ada_bol = true;
    public LinearLayout total_banner_ll, total_banner_china_ll;
    public Timer timer;
    public static List<EventInfo> sAddressList = new ArrayList<EventInfo>();
    private int i = 5;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBind = (LocationMyLatService.MyBind) service;
            mMyBind.startService();
            mLocationMyLatService = mMyBind.getService();
            location_lat(mLocationMyLatService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private ServiceConnection downApkServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownApkBind = (DownNewApkService.MyDownApkBind) service;
            mDownApkBind.startService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_fragment, null);
        mContext = getActivity();
        init(mView);
        getAddressData();
        return mView;

    }

    public void setMainFragment(MainFragmentActivity mcf) {
        this.mfc = mcf;
    }


    private void getAddressData() {
        JSONObject json = new JSONObject();
        MyHttpRequest.onStartHttpPostJSON(
                HttpUrlList.HomeFragment.CITYLIST, json,
                new HttpRequestCallback() {
                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        // TODO Auto-generated method stub
                        if (HttpCode.HTTP_Request_OK == statusCode) {
                            try {
                                mAddressSelect = JsonUtil.getHomeAddressPop(resultStr);
                                if (mAddressSelect.size() > 0) {
                                    handler.sendEmptyMessage(2);
                                } else {
                                    handler.sendEmptyMessage(405);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            handler.sendEmptyMessage(404);
                        }
                    }

                });
    }

    /**
     * 获取listview 的数据
     */
    private void getData() {
        json_data = new JSONObject();
        try {
            json_data.put("userId", MyApplication.loginUserInfor.getUserId());
            json_data.put("resultIndex", pageIndex * 5);
            json_data.put("resultSize", 5);
            if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
                    || AppConfigUtil.LocalLocation.Location_longitude
                    .equals("0.0")
                    || AppConfigUtil.LocalLocation.Location_latitude.equals("")
                    || AppConfigUtil.LocalLocation.Location_longitude
                    .equals("")) {
                json_data.put("lat", MyApplication.Location_latitude);
                json_data.put("lon", MyApplication.Location_longitude);
            } else {
                json_data.put("lat",
                        AppConfigUtil.LocalLocation.Location_latitude + "");
                json_data.put("lon",
                        AppConfigUtil.LocalLocation.Location_longitude + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyHttpRequest.onStartHttpPostJSON(
                HttpUrlList.HomeFragment.RECOMMENDACTIVITY, json_data,
                new HttpRequestCallback() {
                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        isRefresh = false;
                        if (HttpCode.HTTP_Request_OK == statusCode) {
                            Log.i(TAG, "getData:111 " + HttpUrlList.HomeFragment
                                    .RECOMMENDACTIVITY + "  resultStr  ==  " + resultStr);
                            try {
                                mDataList = JsonUtil.getHomeDataList(resultStr);
                                if (mDataList.size() > 0) {
                                    mList.addAll(mDataList);
                                    handler.sendEmptyMessage(3);
                                } else {
                                    handler.sendEmptyMessage(405);

                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            handler.sendEmptyMessage(404);
                        }

                    }

                });
    }

    /**
     * 获取img的数据
     */
    private void getImgData() {
        json = new JSONObject();
        try {
            json.put("advertPostion", 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyHttpRequest.onStartHttpPostJSON(
                HttpUrlList.HomeFragment.PAGEADVERTRECOMMEND, json,
                new HttpRequestCallback() {

                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        // TODO Auto-generated method stub
                        if (HttpCode.HTTP_Request_OK == statusCode) {
                            try {
                                mImgList = JsonUtil.getHomeImgList(resultStr);
                                if (mImgList.size() > 0) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(405);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        } else {
                            handler.sendEmptyMessage(404);
                        }
                    }

                });

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mHomeDetail_TopLayout.setData(mImgList);
                    mHomeDetail_Index.setData(mImgList);
                    mHomeDetail_HorizontalListView.setData(mImgList);
                    content_layout.removeAllViews();
                    content_layout.addView(mHomeDetail_TopLayout.getContent());
                    mImgHomeIndex.clear();
                    for (int j = 0; j < mImgList.size(); j++) {
                        if (mImgList.get(j).getAdvertType().equals("A")) {
                            if (mImgList.get(j).getAdvertSort() != 1) {
                                mImgHomeIndex.add(mImgList.get(j));
                            }
                        }
                    }
                    if (mImgHomeIndex.size() >= 6) {
                        content_layout.addView(mHomeDetail_Index.getContent());
                    }
                    content_layout.addView(mHomeDetail_HorizontalListView
                            .getContent());
                    content_layout.addView(mHomeDetail_Content.getContent());
                    all_city_ada_bol = true;
                    if (all_city_bol) {
                        Log.i(TAG, "handleMessage: all_city");
                        all_city_ada_bol = false;
                        mList.clear();
                        all_city_bol = false;
                        for (int j = 0; j < mImgList.size(); j++) {
                            if (mImgList.get(j).getAdvertType().equals("E")) {
                                HomeDetail_ContentInfor info = new HomeDetail_ContentInfor();
                                info.setAdvertPostion(mImgList.get(j).getAdvertPostion());
                                info.setAdvertSort(mImgList.get(j).getAdvertSort());
                                info.setCreateTime(mImgList.get(j).getCreateTime());
                                info.setUpdateTime(mImgList.get(j).getUpdateTime());
                                info.setAdvertLink(mImgList.get(j).getAdvertLink());
                                info.setAdvertState(mImgList.get(j).getAdvertState());
                                info.setAdvertLinkType(mImgList.get(j).getAdvertLinkType());
                                info.setAdvertType(mImgList.get(j).getAdvertType());
                                info.setAdvertImgUrl(mImgList.get(j).getAdvertImgUrl());
                                info.setAdvertTitle(mImgList.get(j).getAdvertTitle());
                                info.setAdvertId(mImgList.get(j).getAdvertId());
                                info.setAdvertUrl(mImgList.get(j).getAdvertUrl());
                                info.setCreateBy(mImgList.get(j).getCreateBy());
                                info.setUpdateBy(mImgList.get(j).getUpdateBy());
//                                if (mImgList.get(j).getAdvertSort() != 1) {
//                                    mImgHomeIndex.add(mImgList.get(j));
//                                }
                                mList.add(info);
                            }
                        }
                        handler.sendEmptyMessage(3);
                    }
                    break;
                //地址的接口
                case 2:
                    mPopGvList.clear();
                    mPopList.clear();
                    EventInfo all_city = new EventInfo();
                    all_city.setCityCode(0);
                    all_city.setCityName("中国");
                    mPopGvList.add(all_city);
                    for (int i = 0; i < mAddressSelect.size(); i++) {
                        EventInfo info = mAddressSelect.get(i);
                        info.setActivityIsWant(true);
                        if (mAddressSelect.get(i).getIsQuickSearch() == 1) {
                            mPopGvList.add(info);
                        }
                        mPopList.add(info);
                    }
                    sAddressList = mAddressSelect;
                    iLocService = new Intent();
                    iLocService.setClass(getActivity(), LocationMyLatService.class);
                    getActivity().bindService(iLocService, mServiceConnection,
                            BIND_AUTO_CREATE);
                    mHomePopGridAdapter.setData(mPopGvList);
                    mHomePopListAdapter.setData(mPopList);
                    mCityItemGv.setAdapter(mHomePopGridAdapter);
                    mListview.setAdapter(mHomePopListAdapter);
                    mCityItemGv.setOnItemClickListener(myGvOnClick);
                    mListview.setOnItemClickListener(myOnclick);
                    MyApplication.setGridViewHeight(mCityItemGv, 4, 20);
                    MyApplication.setListViewHeightPop(mListview, 1);
                    break;
                //获取listview 的数据
                case 3:
                    mHomeDetail_Content.setData(mImgList, mList, all_city_ada_bol);
                    if (isFristRun == 0) {
                        updateAPK = new Intent();
                        updateAPK.setClass(getActivity(), DownNewApkService.class);
                        getActivity().bindService(updateAPK, downApkServiceConnection,
                                BIND_AUTO_CREATE);
                    }
                    StopLoading();
                    isFristRun = 1;
                    scrroll_view.onRefreshComplete();

                    break;
                case 4:
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    location_lat(mLocationMyLatService);
                    break;
                case 5:
                    int num = i--;
                    closetime_tv.setText(num + "");
                    if (num <= 0) {
                        timer.cancel();
                        task.cancel();
                        total_banner_ll.setVisibility(View.GONE);
                    }
                    break;
                case 6:
                    all_city_bol = true;
                    getImgData();
                    break;
                case 7:
                    onResh();
                    break;
                //错误的请求
                case 404:
                    mLoadingHandler.isNotContent();
                    break;
                //没有数据
                case 405:
                    mLoadingHandler.isNotContent();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void init(View view) {
        Log.i(TAG, "init: ");
        PreviousAddress = MyApplication.getNowAddress(mContext);
        mImgHomeIndex = new ArrayList<HomeImgInfo>();
        loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
        mLoadingHandler = new LoadingHandler(loadingLayout);

        mLoadingHandler.setOnRefreshListenter(this);
        scrroll_view = (PullToRefreshScrollView) view
                .findViewById(R.id.scr_view);
        content_layout = (LinearLayout) view.findViewById(R.id.lin_layout);
        mList = new ArrayList<HomeDetail_ContentInfor>();
        left_tv = (TextView) view.findViewById(R.id.left_tv);
        middle_tv = (TextView) view.findViewById(R.id.middle_tv);
        right_iv = (ImageView) view.findViewById(R.id.right_iv);
        Drawable nav_up = getResources().getDrawable(R.drawable.xiasanjiao);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        left_tv.setCompoundDrawables(null, null, nav_up, null);
        left_tv.setCompoundDrawablePadding(0);
        left_tv.setTypeface(MyApplication.GetTypeFace());
        left_tv.setOnClickListener(this);
        String city_str = MyApplication.getNowAddress(mContext);
        if (!city_str.isEmpty()) {
            left_tv.setText(city_str);
        } else {
            left_tv.setVisibility(View.INVISIBLE);
        }
        middle_tv.setTypeface(MyApplication.GetTypeFace());
        middle_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                location_lat();
                ToastUtil.showToast("我修改好了 哈哈哈哈哈哈哈哈哈哈");
            }
        });
        right_iv.setOnClickListener(this);
        mHomeDetail_TopLayout = new HomeDetail_TopLayout(mContext,
                getActivity(), scrroll_view);
        mHomeDetail_Index = new HomeDetail_Index(mContext, getActivity());
        mHomeDetail_HorizontalListView = new HomeDetail_HorizontalListView(
                mContext, getActivity(), scrroll_view);
        mHomeDetail_Content = new HomeDetail_Content(mContext, getActivity());
        // 上拉、下拉设定
        scrroll_view.setOnRefreshListener(this);
        popupWindow_view = LayoutInflater.from(getActivity()).inflate(R.layout
                .activity_popupwindow_left, null);
        popupWindowNoLat_view = LayoutInflater.from(getActivity()).inflate(R.layout
                .activity_popupwindownolat, null);
        popupWindowChangeCity_view = LayoutInflater.from(getActivity()).inflate(R.layout
                .activity_popupwindowchangecity, null);
        address_Nolocation_tv = (TextView) popupWindowNoLat_view.findViewById(R.id.address_tv);
        mLeftNoLat_btn = (Button) popupWindowNoLat_view.findViewById(R.id.left_btn);
        mRightNolat_btn = (Button) popupWindowNoLat_view.findViewById(R.id.right_btn);
        mLeftChange_btn = (Button) popupWindowChangeCity_view.findViewById(R.id.left_change_btn);
        mRightChange_btn = (Button) popupWindowChangeCity_view.findViewById(R.id.right_change_btn);
        address_location_tv = (TextView) popupWindowChangeCity_view.findViewById(R.id.address_tv);

        ButterKnife.bind(this, popupWindow_view);
        mLeftNoLat_btn.setTypeface(MyApplication.GetTypeFace());
        mRightNolat_btn.setTypeface(MyApplication.GetTypeFace());
        mLeftChange_btn.setTypeface(MyApplication.GetTypeFace());
        mRightChange_btn.setTypeface(MyApplication.GetTypeFace());
        address_location_tv.setTypeface(MyApplication.GetTypeFace());
        mLeftNoLat_btn.setOnClickListener(this);
        mRightNolat_btn.setOnClickListener(this);
        mLeftChange_btn.setOnClickListener(this);
        mRightChange_btn.setOnClickListener(this);

        mGpsTv.setTypeface(MyApplication.GetTypeFace());
        mAllAddress.setTypeface(MyApplication.GetTypeFace());
        mAddressTv.setTypeface(MyApplication.GetTypeFace());
        mSelectTv.setTypeface(MyApplication.GetTypeFace());
        mAddressTv.setOnClickListener(this);
        popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowNoLat = new PopupWindow(popupWindowNoLat_view, LinearLayout.LayoutParams
                .MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindowChangeCity = new PopupWindow(popupWindowChangeCity_view, LinearLayout
                .LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopList = new ArrayList<EventInfo>();
        mPopGvList = new ArrayList<EventInfo>();
        mHomePopGridAdapter = new HomePopGridAdapter(getActivity(), mPopGvList, this);
        mHomePopListAdapter = new HomePopListAdapter(getActivity(), mPopList, this);


        tv = (TextView) view.findViewById(R.id.tv);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        top_banner_address_tv = (TextView) view.findViewById(R.id.top_banner_address_tv);
        closetime_tv = (TextView) view.findViewById(R.id.closetime);
        change_btn = (Button) view.findViewById(R.id.change_btn);
        total_banner_ll = (LinearLayout) view.findViewById(R.id.total_banner_ll);
        total_banner_china_ll = (LinearLayout) view.findViewById(R.id.total_banner_china_ll);
        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);
        top_banner_address_china_tv = (TextView) view.findViewById(R.id
                .top_banner_address_china_tv);
        change_china_btn = (Button) view.findViewById(R.id.change_china_btn);


        tv.setTypeface(MyApplication.GetTypeFace());
        tv1.setTypeface(MyApplication.GetTypeFace());
        tv2.setTypeface(MyApplication.GetTypeFace());
        tv3.setTypeface(MyApplication.GetTypeFace());
        tv4.setTypeface(MyApplication.GetTypeFace());
        top_banner_address_china_tv.setTypeface(MyApplication.GetTypeFace());
        change_china_btn.setTypeface(MyApplication.GetTypeFace());
        top_banner_address_tv.setTypeface(MyApplication.GetTypeFace());
        closetime_tv.setTypeface(MyApplication.GetTypeFace());
        change_btn.setTypeface(MyApplication.GetTypeFace());
        change_btn.setOnClickListener(this);
        change_china_btn.setOnClickListener(this);
        timer = new Timer();
        if (MyApplication.getNowAddress(mContext).equals("中国")){
            MainFragmentActivity.getIntance().closeBottom();
        }
        //开启定位
        StartLoading();
    }

    /**
     * 回调定位的状态
     * 1 第一次定位  有分公司
     * 2 第一次定位  没有分公司
     * 3 第二次定位  没有切换地址
     * 4 第二次定位  位置变化
     * 5 定位失败
     */
    private void location_lat(Location_address interface_address) {
        HomeFragment.this.setLocationInterface(interface_address);
        int status = HomeFragment.this.call();
        String locationAddress = HomeFragment.this.callAddress();
        if (status == 0) {
            handler.sendEmptyMessage(4);
            return;
        }
        location_str = locationAddress;
        StopLoading();
        Log.i(TAG, "LocationStatus: " + status);
        switch (status) {
            case 1:
                //第一次进入  有分公司
                total_banner_ll.setVisibility(View.VISIBLE);
                startTask();
                left_tv.setVisibility(View.VISIBLE);
                left_tv.setText(locationAddress);
                int code = Integer.parseInt(cityCode);
                getNewIP(code);
                break;
            case 2:
                //第一次进入  无分公司
                address_Nolocation_tv.setText("你所在的城市尚未开通");
                mLeftNoLat_btn.setText("选择城市");
                mRightNolat_btn.setText("进入中国版");
                popupWindowNoLat.showAsDropDown(middle_tv);

                break;
            case 3:
                int code_new = Integer.parseInt(cityCode);
                getNewIP(code_new);
                //第二次进入  地址不变
//                address_location_tv.setText("你所在的城市尚未开通");
//                popupWindowNoLat.showAsDropDown(middle_tv);
//                total_banner_ll.setVisibility(View.VISIBLE);
//                startTask();
                break;
            case 4:
                //第二次进入  地址变了
                popupWindowChangeCity.showAsDropDown(middle_tv);
                break;
            case 5:
                //无法定位当前位置
                address_Nolocation_tv.setText("当前无法定位你的位置");
                mLeftNoLat_btn.setText("选择城市");
                mRightNolat_btn.setText("进入中国版");
                popupWindowNoLat.showAsDropDown(middle_tv);
                break;

        }
    }

    AdapterView.OnItemClickListener myGvOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };


    AdapterView.OnItemClickListener myOnclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EventInfo info = mPopList.get(position);
            if (oldPostion != position) {
                oldPostion = position;
                info.setActivityIsWant(false);
            } else {
                oldPostion = -1;
                info.setActivityIsWant(true);

            }
            int totalHeight = 0;
            for (int i = 0; i < mHomePopListAdapter.getCount(); i++) {
                View viewItem = mHomePopListAdapter.getView(i, null, mListview);
                //这个很重要，那个展开的item的measureHeight比其他的大
                viewItem.measure(0, 0);
                totalHeight += viewItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = mListview.getLayoutParams();
            params.height = totalHeight
                    + (mListview.getDividerHeight() * (mListview.getCount() - 1));
            if (params.height < 1300) {
                params.height = 1300;
            }
            mListview.setLayoutParams(params);
            mHomePopListAdapter.notifyDataSetChanged();

        }
    };

    private void StartLoading() {
        scrroll_view.setVisibility(View.GONE);
        mLoadingHandler.startLoading();
    }

    public void StopLoading() {
        scrroll_view.setVisibility(View.VISIBLE);
        mLoadingHandler.stopLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_iv:
                if (MyApplication.OPENORCLOSEBOTTOM) {
                    Intent i = new Intent();
                    i.setClass(mContext, SearchActivity.class);
                    mContext.startActivity(i);
                } else {
                    //这里进入我的页面  中国版的时候
                    MyApplication.change_fragment = 4;
                    mfc.setTabSelection(4);
                }
                break;
            case R.id.left_tv:
                openPopWindow();
                break;
            case R.id.left_btn:
                //进入选择地图
                popupWindowNoLat.dismiss();
                left_tv.setVisibility(View.VISIBLE);
                left_tv.setText("选择");
                openPopWindow();
                break;
            case R.id.right_btn:
                //进入中国
                popupWindowNoLat.dismiss();
                //改变接口 改变布局
                changeToChina();
                break;
            case R.id.left_change_btn:
                //不切换  如果前一次记录是在中国  进入  handler 7  如果是其他城市  是  6
                popupWindowChangeCity.dismiss();
                Log.i(TAG, "onClick: "+PreviousAddress);
                if (PreviousAddress.equals("中国")) {
                    Log.i(TAG, "onClick: 666");
                    handler.sendEmptyMessage(6);
                } else {
                    Log.i(TAG, "onClick: 777");
                    handler.sendEmptyMessage(7);
                }

                break;
            case R.id.right_change_btn:
                //切换
                popupWindowChangeCity.dismiss();
                //改变接口 改变布局
                changeToCity();
                break;
            case R.id.address_tv:
                popupWindow.dismiss();
                break;
            case R.id.change_btn:
                //小的弹出框
                openPopWindow();
                break;
            case R.id.change_china_btn:
                //小的弹出框
                openPopWindow();
                break;
            default:
                break;
        }

    }

    public void changeToChina() {
        total_banner_china_ll.setVisibility(View.VISIBLE);
        left_tv.setVisibility(View.VISIBLE);
        left_tv.setText("中国");
//        left_tv.setCompoundDrawables(null, null, null, null);
        MyApplication.saveNowAddress(mContext, "中国");
        MainFragmentActivity.getIntance().closeBottom();
        Log.i("MainFragmentActivity", "changeToChina: ");
        HttpUrlList.setIpAndTestIp(mContext, "http://emechina.wenhuayun.cn", "http://emechinam" +
                ".wenhuayun.cn:10019");
//        http://emechinam.wenhuayun.cn:10019/advertRecommend/pageAdvertRecommend
        getNewIP(0);
    }

    public void changeToCity() {
        int code = Integer.parseInt(cityCode);
        left_tv.setVisibility(View.VISIBLE);
        left_tv.setText(location_str);
        MyApplication.saveNowAddress(mContext, location_str);
        getNewIP(code);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        scrroll_view.setFocusable(true);
        scrroll_view.setFocusableInTouchMode(true);
        scrroll_view.requestFocus();
        // scrroll_view.requestFocusFromTouch();

    }

    /**
     * 刷新
     */
    private void onResh() {
        // TODO Auto-generated method stub
        if (NetWorkUtil.isConnected()) {
            isRefresh = true;
            mList.clear();
            StartLoading();
            pageIndex = 0;
            getImgData();
            getData();
        } else {
            scrroll_view.onRefreshComplete();
            scrroll_view.setVisibility(View.GONE);
            mLoadingHandler.isNotNetConnection();
        }
    }

    public void getNewIP(int cityCode) {
        if (cityCode == 0) {
            //这个表示全国  直接刷新数据
            handler.sendEmptyMessage(6);
            return;
        }
        total_banner_china_ll.setVisibility(View.GONE);
        MainFragmentActivity.getIntance().openBottom();
        Log.i(TAG, "getNewIP-CityCode: " + cityCode);
        JSONObject jo = new JSONObject();
        try {
            jo.put("cityCode", cityCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyHttpRequest.onStartHttpPostJSON(
                HttpUrlList.HomeFragment.CITYPAGEINFO, jo,
                new HttpRequestCallback() {

                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        // TODO Auto-generated method stub
                        if (HttpCode.HTTP_Request_OK == statusCode) {
                            try {
                                JSONObject json = new JSONObject(resultStr);
                                JSONObject jo = json.optJSONObject("data");
                                String test_ip = jo.optString("platformPath", "");
                                String ip = jo.optString("pagePath", "");
                                ip = ip.substring(0, ip.length() - 1);
                                test_ip = test_ip.substring(0, test_ip.length() - 1);
                                Log.i(TAG, "onPostExecute: " + ip + "  test_ip  " + test_ip);
                                HttpUrlList.setIpAndTestIp(mContext, ip, test_ip);
                                handler.sendEmptyMessage(7);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showToast("数据请求失败，请重新选择地址");
                        }
                    }

                });
    }

    /**
     * 加载更多
     */
    private void addMore() {
        // TODO Auto-generated method stub
        // pageIndex++;
        // getData();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        onResh();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        // addMore();
    }

    @Override
    public void onLoadingRefresh() {
        onResh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unbindService(mServiceConnection);
        getActivity().unbindService(downApkServiceConnection);
    }

    public void openPopWindow() {
        if (!popupWindow.isShowing()) {
            popupWindow.setFocusable(false);
            popupWindow.showAsDropDown(middle_tv);
            mAddressTv.setText(location_str);
        } else {
            popupWindow.dismiss();
        }
    }

    public interface Location_address {
        public int LocationStatus();

        public String LocationAddress();
    }

    public void setLocationInterface(Location_address la) {
        this.mLocationAddress = la;
    }

    public int call() {
        return this.mLocationAddress.LocationStatus();
    }

    public String callAddress() {
        return this.mLocationAddress.LocationAddress();
    }

    public void startTask() {
        timer.schedule(task, 0, 10000);//
    }

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 5;
            handler.sendMessage(message);
        }
    };
}
