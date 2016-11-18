package com.sun3d.culturalShanghai.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
    private TextView left_tv, middle_tv;
    public ImageView right_iv;
    private String TAG = "HomeFragment";
    private JSONObject json;
    private JSONObject json_data;
    private List<HomeImgInfo> mImgList;
    private List<HomeImgInfo> mImgHomeIndex;
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

    private TextView address_Nolocation_tv, address_location_tv;
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
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBind = (LocationMyLatService.MyBind) service;
            mMyBind.startService();
            mLocationMyLatService=mMyBind.getService();
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
        getImgData();
        getData();
        return mView;

    }

    public void setMainFragment(MainFragmentActivity mcf) {
        this.mfc = mcf;
    }


    private void getAddressData() {

        handler.sendEmptyMessage(2);
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
                    break;
                //地址的接口
                case 2:
                    for (int i = 0; i < 20; i++) {
                        EventInfo info = new EventInfo();
                        info.setActivityArea("地址");
                        info.setActivityIsWant(true);
                        ArrayList<EventInfo> list = new ArrayList<EventInfo>();
                        for (int j = 0; j < 2; j++) {
                            EventInfo info1 = new EventInfo();
                            info1.setActivityArea("哈哈哈");
                            list.add(info1);
                        }
                        info.setEventInfosList(list);
                        mPopGvList.add(info);
                        mPopList.add(info);
                    }
                    mHomePopGridAdapter.setData(mPopGvList);
                    mHomePopListAdapter.setData(mPopList);
                    mCityItemGv.setAdapter(mHomePopGridAdapter);
                    mListview.setAdapter(mHomePopListAdapter);
                    mCityItemGv.setOnItemClickListener(myGvOnClick);
                    mListview.setOnItemClickListener(myOnclick);
                    MyApplication.setGridViewHeight(mCityItemGv, 4, 12);
                    MyApplication.setListViewHeight(mListview, 1);
                    break;
                //获取listview 的数据
                case 3:
                    mHomeDetail_Content.setData(mImgList, mList);
                    StopLoading();

                    scrroll_view.onRefreshComplete();
                    break;
                case 4:
                    location_lat(mLocationMyLatService);
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
        left_tv.setTypeface(MyApplication.GetTypeFace());
        left_tv.setOnClickListener(this);
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
                getActivity(), scrroll_view, this);
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
        mHomePopGridAdapter = new HomePopGridAdapter(getActivity(), mPopGvList);
        mHomePopListAdapter = new HomePopListAdapter(getActivity(), mPopList);
        //开启定位
        iLocService = new Intent();
        iLocService.setClass(getActivity(), LocationMyLatService.class);
        getActivity().bindService(iLocService, mServiceConnection, BIND_AUTO_CREATE);
        updateAPK = new Intent();
        updateAPK.setClass(getActivity(), DownNewApkService.class);
        getActivity().bindService(updateAPK, downApkServiceConnection, BIND_AUTO_CREATE);
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
        if(status==0){
            handler.sendEmptyMessage(4);
            return;
        }
        location_str = "";
        Log.i(TAG, "LocationStatus: " + status);
        switch (status) {
            case 1:
                mHomeDetail_TopLayout.total_banner_ll.setVisibility(View.VISIBLE);
                mHomeDetail_TopLayout.startTask();
                break;
            case 2:
                address_location_tv.setText("你所在的城市尚未开通");
                popupWindowNoLat.showAsDropDown(middle_tv);
                break;
            case 3:
                mHomeDetail_TopLayout.total_banner_ll.setVisibility(View.VISIBLE);
                mHomeDetail_TopLayout.startTask();
                break;
            case 4:
                popupWindowChangeCity.showAsDropDown(middle_tv);
                break;
            case 5:
                address_location_tv.setText("当前无法定位你的位置");
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
            ToastUtil.showToast("点击了  item");
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
            mListview.setLayoutParams(params);
            mHomePopListAdapter.notifyDataSetChanged();

        }
    };

    private void StartLoading() {
        scrroll_view.setVisibility(View.GONE);
        mLoadingHandler.startLoading();
    }

    private void StopLoading() {
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
                changeToChina();
//                openPopWindow();
                break;
            case R.id.left_btn:
                //进入选择地图
                openPopWindow();
                break;
            case R.id.right_btn:
                //进入中国
                popupWindowNoLat.dismiss();
                //改变接口 改变布局
                changeToChina();
                break;
            case R.id.left_change_btn:
                //不切换
                popupWindowChangeCity.dismiss();
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
            default:
                break;
        }

    }

    public void changeToChina() {
        mHomeDetail_TopLayout.total_banner_china_ll.setVisibility(View.VISIBLE);
        MainFragmentActivity.getIntance().closeBottom();
    }

    public void changeToCity() {

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
            getData();
        } else {
            scrroll_view.onRefreshComplete();
            scrroll_view.setVisibility(View.GONE);
            mLoadingHandler.isNotNetConnection();
        }
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
    }


    public void setLocationInterface(Location_address la) {
        this.mLocationAddress = la;
    }

    public int call() {
        return this.mLocationAddress.LocationStatus();
    }


}
