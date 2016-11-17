package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.GetPhoneInfoUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WhatDateUtil;
import com.sun3d.culturalShanghai.adapter.WeekActivityListAdapter;
import com.sun3d.culturalShanghai.adapter.WeekItemAdapter;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.BannerInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.sun3d.culturalShanghai.windows.WeekPopupWindow;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 近期的活动 
 * @author wenff
 *
 */
public class ThisWeekActivity extends Activity implements OnClickListener, WeekActivityListAdapter.WeekLisenner {
    private final String mPageName = "ThisWeekActivity";
    private Context mContext;
    private PullToRefreshListView mListView;
    private int mPage = 0;
    TextView right;
    Drawable drawable;
    private RelativeLayout mTitle;
    private LoadingHandler mLoadingHandler;
    private Map<String, String> mParams;
    private View Topview;
    private WeekActivityListAdapter mWeekActivityListAdapter;
    private LinearLayout topLanel;
    private TextView topLaBel;
    private ImageView topImg;
    private WeekPopupWindow mWeekPopupWindow = new WeekPopupWindow();
    private List<UserBehaviorInfo> mflagList;
    public static String today="";
    private Boolean isRefresh = false;
    private Boolean isListViewRefresh = true;
    private String activityType = "2";// 活动类型 1->距离 2.即将开始 3.热门 4.所有活动（必选）
    private List<EventInfo> mList;
    private WeekActivityListAdapter mListAdapter;
    private Boolean isFirstResh = true;
    private String activityTime = "";
    public static List<BannerInfo> listBannerInfo =new ArrayList<BannerInfo>();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API. See
     * https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        LoadingTextShowPopWindow.dismissPop();
        WeekItemAdapter.init(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_week);
        mContext = this;
        today = TextUtil.Time2Format(TextUtil.getToDay());
        MyApplication.getInstance().addActivitys(this);
        isFirstResh = true;
        isRefresh = false;
        init();
        initListData();
        getListData(mPage);
        setCountTip("");
        startWill();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
//dian
    private void startWill(){
        Map<String,String> params=new HashMap<String,String>();
        params.put("userId",MyApplication.loginUserInfor.getUserId());
        MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYWILL, params, new HttpRequestCallback() {
            @Override
            public void onPostExecute(int statusCode, String resultStr) {

            }
        });
    }


    public static void setCountTip(String tagId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("versionNo", GetPhoneInfoUtil.getIMEI(MyApplication.getContext()));
        params.put("tagId", tagId);
        params.put("userId", MyApplication.loginUserInfor.getUserId());
        MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYCOUNT, params, new HttpRequestCallback() {

            @Override
            public void onPostExecute(int statusCode, String resultStr) {
                // TODO Auto-generated method stub
                if (statusCode == HttpCode.HTTP_Request_Success_CODE) {

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
                    if (!isFirstResh) {
                        // LoadingTextShowPopWindow.showLoadingText(ThisWeekActivity.this,
                        // Topview, "数据刷新完成");
                    }
                    isFirstResh = false;
                    break;
//                case 3:
//                    listBannerInfo = new ArrayList<BannerInfo>();
//                    listBannerInfo = (List<BannerInfo>) msg.obj;
//                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 设置标题
     */
    private void setTitle() {
        mTitle = (RelativeLayout) findViewById(R.id.title);
        mTitle.findViewById(R.id.title_left).setOnClickListener(this);
        mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
        TextView title = (TextView) mTitle.findViewById(R.id.title_content);
        right = (TextView) mTitle.findViewById(R.id.title_send);
        title.setText("近期活动");
        right.setText("筛选");
        right.setTextSize(17);
        right.setTextColor(getResources().getColor(R.color.white_color));
        drawable = getResources().getDrawable(R.drawable.sh_activity_title_pull);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        right.setCompoundDrawables(null, null, drawable, null);
        title.setVisibility(View.VISIBLE);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {
        setTitle();
        RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
        mLoadingHandler = new LoadingHandler(loadingLayout);
        mLoadingHandler.startLoading();
        mLoadingHandler.stopLoading();
        Topview = (View) findViewById(R.id.activity_page_line);
        topLanel = (LinearLayout) findViewById(R.id.tabtop);
        topLaBel = (TextView) topLanel.findViewById(R.id.toptv);
        topLaBel.setText(today);
        topImg = (ImageView) findViewById(R.id.guang_gao);
        mListView = (PullToRefreshListView) findViewById(R.id.exhibition_listview);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                if (arg2 > 1) {

                    Intent intent = new Intent(mContext, ActivityDetailActivity.class);
                    // Intent intent = new Intent(mContext,
                    // EventDetailsActivity.class);
                    intent.putExtra("eventId", mList.get(arg2).getEventId());
                    mContext.startActivity(intent);

                }

            }
        });
        mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.d("PullToRefreshListView", "onPullDownToRefresh");
                onResh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                Log.d("PullToRefreshListView", "onPullUpToRefresh");
                if (isListViewRefresh) {
                    onResh();
                    isListViewRefresh = false;
                } else {
                    onAddmoreData();
                }

            }

        });
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                if (arg1 > 0) {
                    topLanel.setVisibility(View.VISIBLE);
                } else {
                    topLanel.setVisibility(View.GONE);
                }
            }
        });
        initListView();
    }

    /**
     * 初始化listview的数据
     */
    private void initListData() {
        mPage = 0;
        isRefresh = true;
        mList = new ArrayList<EventInfo>();
        mListAdapter = new WeekActivityListAdapter(ThisWeekActivity.this, mList,listBannerInfo);
        mListAdapter.setWeekLisenner(this);
        mListView.setAdapter(mListAdapter);
        // getTypeTag();
    }

    private void onAddmoreData() {
        mPage = HttpUrlList.HTTP_NUM + mPage;
        getListData(mPage);
    }

    private void onResh() {
        isRefresh = true;
        mPage = 0;
        getListData(mPage);
    }

    public StringBuffer getTag(HashMap<Integer, Boolean> map) {
        StringBuffer tagAll = new StringBuffer();
        StringBuffer tag = new StringBuffer();
        if (map.size() == 0) {
            return removedouhao(tagAll);
        } else {
            for (int i = 0; i < map.size(); i++) {
                if (map.get(i)) {
                    if (i == 0) {
                        return removedouhao(tagAll);
                    } else {
                        tag.append(mflagList.get(i - 1).getTagId());
                        tag.append(",");
                    }
                }
            }
        }
        Log.d("tagtagtag", tag.toString() + "====");

        return removedouhao(tag);
    }

    public StringBuffer removedouhao(StringBuffer stringBuffer) {

        if (stringBuffer.length() > 0) {
            stringBuffer = stringBuffer.replace(stringBuffer.length() - 1, stringBuffer.length(), "");
        }
        return stringBuffer;
    }

    /**
     * 静态数据
     */
    private void addflag() {
        mflagList = new ArrayList<UserBehaviorInfo>();
        mflagList.clear();
        mflagList.addAll(MyApplication.getInstance().getSelectTypeList());
        for (int i = 0; i < MyApplication.getInstance().getSelectTypeList().size(); i++) {
            mflagList.get(i).setSelect(false);
        }
    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "3");
        MyHttpRequest.onHttpPostParams(HttpUrlList.Banner.MAIN_BANNER_URL, params, new HttpRequestCallback() {

            @Override
            public void onPostExecute(int statusCode, String resultStr) {
                // TODO Auto-generated method stub
                if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
                    listBannerInfo = JsonUtil.getBannerInfoList(resultStr);
                }else {

                }

            }
        });
    }

    /**
     * 获取20条活动的数据
     */
    private void getListData(int page) {
        getData();
        mParams = new HashMap<String, String>();
//        MyApplication.getInstance().setActivityType(activityType);
        mParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
        if (activityTime.equals("")) {
            activityTime = TextUtil.getToDay();
        }
        mParams.put("activityTime", activityTime);
        mParams.put("appType", activityType);
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
        mParams.put("activityTypeId", getTag(WeekItemAdapter.indexSelect).toString());
        mParams.put(HttpUrlList.HTTP_PAGE_NUM, 20 + "");
        // 活动类型标签id（可选）用逗号分隔，没有就传空字符串
        Log.d("page", "---page:" + page);
        mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(page));
        MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.WFF_ACTIVITYLISTURL_MAP, mParams, new HttpRequestCallback() {
            @Override
            public void onPostExecute(int statusCode, String resultStr) {
                // TODO Auto-generated method stub
                Log.d("statusCode", statusCode + "---" + resultStr);
                if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
                    try {
                        List<EventInfo> mAddList = new ArrayList<EventInfo>();
                        mList.clear();
                        mAddList = JsonUtil.getEventList(resultStr);
                        if (mAddList.size() > 0) {
                            if (isRefresh) {
                                isRefresh = false;
                                EventInfo nullingo = new EventInfo();
                                mAddList.add(0, nullingo);
                                mAddList.add(1, nullingo);
                            }
                            mList.addAll(mAddList);
                            mListAdapter.setList(mList,listBannerInfo);
                        } else {
                            Log.d("wewewew", "---" + mAddList.size() + "-====" + activityType);
                            LoadingTextShowPopWindow.showLoadingText(ThisWeekActivity.this, Topview, "数据为空");
                            isFirstResh = true;
                        }
                        mLoadingHandler.stopLoading();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        mLoadingHandler.showErrorText(e.toString());
                    }
                } else {
                    mLoadingHandler.showErrorText(resultStr);
                    ToastUtil.showToast("数据请求失败:" + resultStr);
                }
                handler.sendEmptyMessageDelayed(REFRESH, 200);
            }
        });
    }

    /**
     * 初始化listview的数据
     */
    private void initListView() {
        mPage = 0;
        mList = new ArrayList<EventInfo>();
        for (int i = 0; i < 15; i++) {
            mList.add(new EventInfo());
        }
        mWeekActivityListAdapter = new WeekActivityListAdapter(mContext, mList,listBannerInfo);
        mListView.setAdapter(mWeekActivityListAdapter);
        addflag();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.this_week, menu);
        return true;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.title_left:
                finish();
                mflagList.clear();
                break;
            case R.id.title_send:
                mWeekPopupWindow.showWeekPopupWindow(mContext, mTitle, right, drawable, mflagList, new WeekPopupCallback() {
                    @Override
                    public void referShing() {
                        // TODO Auto-generated method stub
                        onResh();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onWeekClick(String date) {
        // ToastUtil.showToast(date);
        activityTime = date;
        onResh();
        topLaBel.setText(TextUtil.Time2Format(date));
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public interface WeekPopupCallback {
        public void referShing();
    }
}
