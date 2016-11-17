package com.sun3d.culturalShanghai.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil;
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
import com.sun3d.culturalShanghai.object.HttpResponseText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

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
    private HttpResponseText httpBackContent;
    private HttpResponseText httpBackDataContent;
    private JSONObject json;
    private JSONObject json_data;
    private List<HomeImgInfo> mImgList;
    private List<HomeImgInfo> mImgHomeIndex;
    private List<HomeDetail_ContentInfor> mDataList;
    private List<HomeDetail_ContentInfor> mList;
    private RelativeLayout loadingLayout;
    private LoadingHandler mLoadingHandler;
    //    private RelativeLayout mPopRelativeLayout;
//    private LoadingHandler mPopLoadingHandler;
    private int pageIndex = 0;
    private int currentListPosition = 0;
    private boolean isRefresh = false;
    private View popupWindow_view, popupWindowNoLat_view, popupWindowChangeCity_view;
    private PopupWindow popupWindow, popupWindowNoLat, popupWindowChangeCity;
    private HomePopGridAdapter mHomePopGridAdapter;
    private HomePopListAdapter mHomePopListAdapter;
    private List<EventInfo> mPopList;
    private List<EventInfo> mPopGvList;
    private int oldPostion = -1;
    private String location_str;
    private TextView address_Nolocation_tv, address_location_tv;
    private Button mLeftNoLat_btn, mRightNolat_btn;
    private Button mLeftChange_btn, mRightChange_btn;
    private SharedPreferences sharedPreferences;
    private boolean isFirstRun = true;
    private boolean isHava = true;
    private boolean isAddressChange = true;
    private boolean fristStartLat = true;
    private MainFragmentActivity mfc;
    private String NowAddress="";
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
    public void setMainFragment(MainFragmentActivity mcf){
        this.mfc=mcf;
    }
    private void getLat() {
        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }


        GaoDeLocationUtil mGaoDeLocationUtil = new GaoDeLocationUtil(mContext);
        mGaoDeLocationUtil.startLocation();
        mGaoDeLocationUtil.setOnLocationListener(new GaoDeLocationUtil
                .OnLocationListener() {

            @Override
            public void onLocationSuccess(AMapLocation location) {
                /*定位的情况
                * 第一种：第一次进入APP 定位成功 且定位的地点有我们分公司  提示5秒鐘消失的小彈出框
                * 第二种：第一次进入APP 定位成功 但是定位地点没有我们分公司  提示你所在的城市暂时未开通
                * 第三种：第一次进入APP 定位失败 进入定位失败 提示定位失败   选择地址和全中国
                * 第四种：第二次进入APP 定位成功 且当前位置和现在的位置一致   不弹出弹框
                * 第五种：第二次进入APP 定位成功 但是当前位置产生变化   提示切换城市
                * 第六种：第二次进入APP 定位失败 默认进入前一次的地点
                *
                * */
                location_str = location.getCity();
                //这里对两次的地点进行对比

                if (isFirstRun) {
                    //第一次进入定位成功
                    if (isHava) {
                        //定位地点有分公司
                        MyApplication.saveNowAddress(getActivity(),location_str);
                        mHomeDetail_TopLayout.total_banner_ll.setVisibility(View.VISIBLE);
                        mHomeDetail_TopLayout.startTask();
                    } else {
                        //定位地点没有分公司
                        address_location_tv.setText("你所在的城市尚未开通");
                        popupWindowNoLat.showAsDropDown(middle_tv);


                    }

                } else {
                    //第二次定位成功
                    if (isAddressChange) {
                        //地址没变
                        mHomeDetail_TopLayout.total_banner_ll.setVisibility(View.VISIBLE);
                        mHomeDetail_TopLayout.startTask();
                    } else {
                        popupWindowChangeCity.showAsDropDown(middle_tv);
                        //地址变了
                    }

                }
                StopLoading();


                Log.i(TAG, "onLocationSuccess: " + location.getCity());
            }

            @Override
            public void onLocationFailure(String error) {
                address_location_tv.setText("当前无法定位你的位置");
                popupWindowNoLat.showAsDropDown(middle_tv);
                StopLoading();
                Log.i(TAG, "onLocationFailure: " + error);
            }
        });
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

        Log.i(TAG, "getData: " + HttpUrlList.IP + "  IP==  " + HttpUrlList.HomeFragment
                .RECOMMENDACTIVITY + "  new  ip" + HttpUrlList.NEW_IP);
        MyHttpRequest.onStartHttpPostJSON(
                HttpUrlList.HomeFragment.RECOMMENDACTIVITY, json_data,
                new HttpRequestCallback() {
                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        isRefresh = false;
                        Log.i(TAG, "Data   statusCode  ==  " + statusCode
                                + "  resultStr==  " + resultStr);
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
                        Log.i(TAG, "状态==  " + statusCode + " resultStr== "
                                + resultStr);
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
                    if (fristStartLat) {
                        fristStartLat = false;
                        getLat();
                    }else{
                        StopLoading();
                    }

                    scrroll_view.onRefreshComplete();
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
        sharedPreferences = getActivity().getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        NowAddress=sharedPreferences.getString("nowAddress", "");
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
        StartLoading();
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
}
