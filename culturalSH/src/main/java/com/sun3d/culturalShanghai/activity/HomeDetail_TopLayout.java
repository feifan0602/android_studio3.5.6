package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.HomeDetail_TopLayoutAdapter;
import com.sun3d.culturalShanghai.object.HomeDetail_TopLayoutInfor;
import com.sun3d.culturalShanghai.object.HomeImgInfo;
import com.sun3d.culturalShanghai.view.PageControlView;
import com.sun3d.culturalShanghai.view.ScrollLayout;
import com.sun3d.culturalShanghai.view.ScrollLayout.OnScreenChangeListenerDataLoad;
import com.sun3d.culturalShanghai.view.SlideShowView;

import java.util.ArrayList;
import java.util.List;

public class HomeDetail_TopLayout {
    private LinearLayout content;
    private Context mContext;
    private Activity mActivity;
    private ScrollLayout horizon_Listview;
    private ArrayList<HomeDetail_TopLayoutInfor> list_Info;
    private PageControlView pageControl;
    private DataLoading dataLoad;
    private static final float APP_PAGE_SIZE = 4.0f;
    private int lastX, lastY;
    private String TAG = "HomeDetail_TopLayout";
    private PullToRefreshScrollView Scroll_view;
    private List<HomeImgInfo> mList;
    private List<HomeDetail_TopLayoutInfor> topImgInfoList;

    private SlideShowView mSlideShowView;


    public HomeDetail_TopLayout(Context context, Activity activity,
                                PullToRefreshScrollView ssv) {
        super();
        this.mContext = context;
        this.mActivity = activity;
        this.Scroll_view = ssv;

    }



    public void setData(List<HomeImgInfo> mImgList) {
        content = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.homedetail_toplayout, null);
        initView();
        this.mList = mImgList;
        list_Info.clear();
        topImgInfoList.clear();
        horizon_Listview.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getAdvertType().equals("B")) {
                HomeDetail_TopLayoutInfor info = new HomeDetail_TopLayoutInfor();
                info.setAdvertPostion(mList.get(i).getAdvertPostion());
                info.setAdvertSort(mList.get(i).getAdvertSort());
                info.setCreateTime(mList.get(i).getCreateTime());
                info.setUpdateTime(mList.get(i).getUpdateTime());
                info.setAdvertLink(mList.get(i).getAdvertLink());
                info.setAdvertState(mList.get(i).getAdvertState());
                info.setAdvertLinkType(mList.get(i).getAdvertLinkType());
                info.setAdvertType(mList.get(i).getAdvertType());
                info.setAdvertImgUrl(mList.get(i).getAdvertImgUrl());
                info.setAdvertTitle(mList.get(i).getAdvertTitle());
                info.setAdvertId(mList.get(i).getAdvertId());
                info.setAdvertUrl(mList.get(i).getAdvertUrl());
                info.setCreateBy(mList.get(i).getCreateBy());
                info.setUpdateBy(mList.get(i).getUpdateBy());
                list_Info.add(info);
            }
            if (mList.get(i).getAdvertType().equals("A")) {
                if (mList.get(i).getAdvertSort() == 1
                        || mList.get(i).getAdvertSort() == 8
                        || mList.get(i).getAdvertSort() == 9
                        || mList.get(i).getAdvertSort() == 10) {
                    HomeDetail_TopLayoutInfor info = new HomeDetail_TopLayoutInfor();
                    info.setAdvertPostion(mList.get(i).getAdvertPostion());
                    info.setAdvertSort(mList.get(i).getAdvertSort());
                    info.setCreateTime(mList.get(i).getCreateTime());
                    info.setUpdateTime(mList.get(i).getUpdateTime());
                    info.setAdvertLink(mList.get(i).getAdvertLink());
                    info.setAdvertState(mList.get(i).getAdvertState());
                    info.setAdvertLinkType(mList.get(i).getAdvertLinkType());
                    info.setAdvertType(mList.get(i).getAdvertType());
                    info.setAdvertImgUrl(mList.get(i).getAdvertImgUrl());
                    info.setAdvertTitle(mList.get(i).getAdvertTitle());
                    info.setAdvertId(mList.get(i).getAdvertId());
                    info.setAdvertUrl(mList.get(i).getAdvertUrl());
                    info.setCreateBy(mList.get(i).getCreateBy());
                    info.setUpdateBy(mList.get(i).getUpdateBy());
                    topImgInfoList.add(info);
                }
            }
        }
        Log.i(TAG, "topImgInfoList  ==  " + topImgInfoList.size() + "  listinfo==  " + list_Info
                .size());
        mSlideShowView.setActivity(mActivity);
        mSlideShowView.setImageUris(topImgInfoList);
//		mSlideShowView.startPlay();
        // 这是第一个 horizonlistview
        // int onepageNo = topImgInfoList.size();
        // one_horizon_Listview.setOnTouchListener(new View.OnTouchListener() {
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // content.getParent().requestDisallowInterceptTouchEvent(true);
        // int x = (int) event.getRawX();
        // int y = (int) event.getRawY();
        //
        // switch (event.getAction()) {
        // case MotionEvent.ACTION_DOWN:
        // lastX = x;
        // lastY = y;
        // break;
        // case MotionEvent.ACTION_MOVE:
        // int deltaY = y - lastY;
        // int deltaX = x - lastX;
        // if (Math.abs(deltaX) > Math.abs(deltaY)) {
        // Log.i(TAG, "横向 滑动...  ");
        // one_horizon_Listview.getParent()
        // .requestDisallowInterceptTouchEvent(false);
        // } else {
        // Log.i(TAG, "竖向滑动...  ");
        // one_horizon_Listview.getParent()
        // .requestDisallowInterceptTouchEvent(true);
        // }
        // case MotionEvent.ACTION_UP:
        //
        // break;
        // default:
        // break;
        // }
        // return false;
        // }
        // });
        // for (int i = 0; i < onepageNo; i++) {
        // ImageView appPage = new ImageView(mContext);
        // final int pos = i;
        // appPage.setScaleType(ScaleType.CENTER_CROP);
        // MyApplication
        // .getInstance()
        // .getImageLoader()
        // .displayImage(
        // TextUtil.getUrlSmall_750_250(topImgInfoList.get(i)
        // .getAdvertImgUrl()), appPage);
        // appPage.setOnClickListener(new OnClickListener() {
        // @Override
        // public void onClick(View arg0) {
        // HomeDetail_TopLayoutInfor info = topImgInfoList.get(pos);
        // if (info.getAdvertLink() == 0) {
        // MyApplication.selectImg(mContext,
        // info.getAdvertLinkType(), info.getAdvertUrl());
        // } else {
        // MyApplication.selectWeb(mContext, info.getAdvertUrl());
        // }
        // }
        // });
        // one_horizon_Listview.addView(appPage);
        // }
        // 这个是第二个 horizonlistview
        int pageNo = (int) Math.ceil(list_Info.size() / APP_PAGE_SIZE);
        horizon_Listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                content.getParent().requestDisallowInterceptTouchEvent(true);
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaY = y - lastY;
                        int deltaX = x - lastX;
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            Log.i(TAG, "横向 滑动...  ");
                            Scroll_view.getParent()
                                    .requestDisallowInterceptTouchEvent(false);
                        } else {
                            Log.i(TAG, "竖向滑动...  ");
                            Scroll_view.getParent()
                                    .requestDisallowInterceptTouchEvent(true);
                        }
                    default:
                        break;
                }
                return false;
            }
        });
        for (int i = 0; i < pageNo; i++) {
            GridView appPage = new GridView(mContext);
            appPage.setSelector(new ColorDrawable(Color.TRANSPARENT));
            appPage.setHorizontalSpacing(30);
            // get the "i" page data
            appPage.setAdapter(new HomeDetail_TopLayoutAdapter(mActivity,
                    list_Info, i));
            appPage.setNumColumns(4);
            appPage.setOnItemClickListener(listener);
            horizon_Listview.addView(appPage);
        }
        // onePageControl.bindScrollViewGroup(one_horizon_Listview);
        // // 加载分页数据
        // dataLoad.bindScrollViewGroup(one_horizon_Listview);

        pageControl.bindScrollViewGroup(horizon_Listview);
        // 加载分页数据
        dataLoad.bindScrollViewGroup(horizon_Listview);


    }

    public LinearLayout getContent() {
        return content;

    }

    private void initView() {
        topImgInfoList = new ArrayList<HomeDetail_TopLayoutInfor>();
        mList = new ArrayList<HomeImgInfo>();
        dataLoad = new DataLoading();


        mSlideShowView = (SlideShowView) content
                .findViewById(R.id.slideshowView);
        pageControl = (PageControlView) content.findViewById(R.id.pageControl);
        // onePageControl = (PageControlView) content
        // .findViewById(R.id.one_pageControl);
        horizon_Listview = (ScrollLayout) content.findViewById(R.id.Scroll_ll);
        // one_horizon_Listview = (ScrollLayout) content
        // .findViewById(R.id.one_Scroll_ll);
        list_Info = new ArrayList<HomeDetail_TopLayoutInfor>();

    }

    /**
     * gridView 的onItemLick响应事件
     */
    public OnItemClickListener onelistener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            // Toast.makeText(mActivity, "position=  " + position, 1000).show();
            HomeDetail_TopLayoutInfor info = (HomeDetail_TopLayoutInfor) parent
                    .getItemAtPosition(position);
            MyApplication.SearchTagTitle = info.getAdvertTitle();
            if (info.getAdvertLink() == 0) {
                MyApplication.selectImg(mContext, info.getAdvertLinkType(),
                        info.getAdvertUrl());
            } else {
                MyApplication.selectWeb(mContext, info.getAdvertUrl());
            }
        }

    };
    /**
     * gridView 的onItemLick响应事件
     */
    public OnItemClickListener listener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            // Toast.makeText(mActivity, "position=  " + position, 1000).show();
            HomeDetail_TopLayoutInfor info = (HomeDetail_TopLayoutInfor) parent
                    .getItemAtPosition(position);
            MyApplication.SearchTagTitle = info.getAdvertTitle();
            if (info.getAdvertLink() == 0) {
                MyApplication.selectImg(mContext, info.getAdvertLinkType(),
                        info.getAdvertUrl());
            } else {
                MyApplication.selectWeb(mContext, info.getAdvertUrl());
            }
        }

    };

    protected void onDestroy() {
        // TODO Auto-generated method stub
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    // 分页数据
    class DataLoading {
        private int count;

        public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {
            this.count = scrollViewGroup.getChildCount();
            scrollViewGroup
                    .setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {
                        public void onScreenChange(int currentIndex) {
                            // TODO Auto-generated method stub
                            generatePageControl(currentIndex);
                        }
                    });
        }

        private void generatePageControl(int currentIndex) {
            // 如果到最后一页，就加载16条记录
            // if (count == currentIndex + 1) {
            // MyThread m = new MyThread();
            // new Thread(m).start();
            // }
        }
    }


}
