package com.sun3d.culturalShanghai;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alipay.euler.andfix.patch.PatchManager;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.FileSizeUtil;
import com.sun3d.culturalShanghai.Util.Installation;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WeatherDataUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.CalenderActivity;
import com.sun3d.culturalShanghai.activity.SearchListActivity;
import com.sun3d.culturalShanghai.activity.SearchTagListActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.ActivityListRoomInfo;
import com.sun3d.culturalShanghai.object.EventAddressInfo;
import com.sun3d.culturalShanghai.object.HttpResponseText;
import com.sun3d.culturalShanghai.object.MyUserInfo;
import com.sun3d.culturalShanghai.object.SearchListInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.object.WindowInfo;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.ScrollViewListView;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

import static com.sun3d.culturalShanghai.service.DownloadAPKService.context;

public class MyApplication extends Application {
    public static boolean activity_bool=false;
    public static boolean space_bool=false;
    public static boolean organizations_bool=false;
    public static boolean my_bool=false;
    public static String Text_Url = "http://new-img2.ol-img.com/985x695/116/31/lirqOsEqHWnuE.jpg";
    public static String Text_Big_Url = "http://pic16.nipic.com/20110831/8027526_145847893000_2" +
            ".jpg";
    private static Context mContext;
    public static String bookid;
    public static String curDate;
    public static String shareLink = "";
    public static String openPeriod;
    public static String version;
    public static String Img_Path = "";
    public static boolean OPENORCLOSEBOTTOM = true;
    public static int activityispast;
    /**
     * 0 我的订单 1 从预订好的活动室
     */
    public static int room_activity = 2;
    private static int windowHeight;
    private static int windowWidth;
    private static MyApplication instance;
    public static String SearchTagTitle;
    private List<Activity> activitys;
    private static SimpleDateFormat sdf;
    private Handler mReserveHandler;
    private Handler mRoomHandler;
    private Handler mMainHandler;
    private Handler mUserHandler;
    private Handler mLableChoose;
    private Handler mMainFragment;
    private Handler venueListHandler;
    private Handler groupListHandler;
    private Handler notInvoluntaryHandler;
    public static Typeface Total_font;
    // 加载动画图片
    public static RelativeLayout gf1_rl = null;
    private Handler MyBindHandler;
    /**
     * 活动室的判断 选中项
     */
    public static ArrayList<ActivityListRoomInfo> roomList = new ArrayList<ActivityListRoomInfo>();
    public static ArrayList<ActivityListRoomInfo> roomList_new = new
            ArrayList<ActivityListRoomInfo>();
    public static Map<String, Boolean> map = new HashMap<String, Boolean>();
    private Handler activityHandler;
    /**
     * true 表示正常登陆 false 表示第三方登录
     */
    public static boolean login_status;
    /**
     * 来判断是否能预定，预定的状态
     */
    public static int total_availableCount = -1;
    /**
     * 当前的票数
     */
    public static int currentCount = 0;
    /**
     * 秒杀的倒计时间
     */
    public static int spike_Time = -1;
    public static String spike_endTime = "";
    public static String spike_endTimeHour = "";
    public static String spike_event_id = "";
    public static String spike_eventTime = "";
    public static final int HANDLER_USER_CODE = 101;// 拍照返回通知
    /**
     * 来判断 待审核 中的认证状态
     */
    public static int MyUserType = 0;
    public static UserInfor loginUserInfor = new UserInfor();
    public static MyUserInfo MyloginUserInfor = new MyUserInfo();
    public static EventAddressInfo eventAddressInfor = new EventAddressInfo();
    private SearchListInfo searchListInfo;
    private static ImageLoader mImageLoader;
    public static Boolean UserIsLogin = false;
    public static int UserVIPType = 2;
    public static String Third_OpenId;
    public static String Third_LoginType;
    public static Typeface typeFace;
    private WindowInfo venueWindowList;
    private WindowInfo groupWindowList;
    private WindowInfo notInvoluntaryList;
    public static ArrayList<HashMap<String, String>> activity_list;
    public static ArrayList<HashMap<String, String>> venue_list;
    /**
     * 来判断是否返回到指定的 fragment
     */
    public static int change_fragment = 0;
    public static String StartTime = "";
    public static String EndTime = "";
    ;
    public static int pageTotal = 0;
    /**
     * android的当前版本号
     */
    public static int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    /**
     * 日历显示的行数 1, 表示去除第一行 2， 表示去除最后一行
     */
    public static int line_data = 0;
    /**
     * 日历显示的个数
     */
    public static int num_data = 42;
    /**
     * 日历显示的高度
     */
    public static int height_num = 1;
    /**
     * 日历显示的布局
     */
    public static int height_test = 100;
    /**
     * Venue popuwindow 的Y 的位置
     */
    public static int venue_tv_height;
    /**
     * 这里是四个状态 改变场馆的数据
     */
    public static String venueArea;
    public static String Location_latitude = 31.280842 + "";
    public static String Location_longitude = 121.433594 + "";
    public static String venueType_classication;
    public static String venueType_classication_Name;
    public static String venueMood;
    public static String venueDicName;
    public static String venueMoodName;
    public static String venueIsReserve;
    public static String venueIsReserve_Name;

    public static String sortType;
    public static String sortType_Name;

    public static String COLLECT_ACTION_FLAG_ACTIVITY = "collect_action_flag_Activity";
    public static String COLLECT_ACTION_FLAG_VUNUE = "collect_action_flag_Vunue";
    public static String COLLECT_ACTION_OBJECT = "collect_action_object";
    public static String LOGIN_ACTION_REFRESH = "LOGIN_Success";
    public static Boolean isReserverTimeDate = false;
    public static String BindPhoneShow = "请先前往个人中心>点击用户头像>用户资料中绑定手机才能预订。";
    public static int stystemTitleHeight = 0;
    private Map<String, Integer> weatherMap;
    private Map<String, Integer> weatherMainMap;
    public Boolean isOverGone = false;
    private String activityType = "1";
    private String venueType = "1";
    private List<UserBehaviorInfo> selectTypeList = new ArrayList<UserBehaviorInfo>();
    private int position = 0;
    public static boolean isTopFrist = false;
    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
            "十亿", "百亿", "千亿", "万亿"};
    static char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '日', '八', '九'};
    public static boolean isFromMyLove = false;
    /**
     * 1 是区域 2 是分类 3 是排序 4 是状态
     */
    public static int type_num;
    public static String VERSION_NAME = "";
    public static PatchManager mPatchManager;
    private static final String TAG = "MyApplication";
    /**
     * apatch文件
     */
    private static final String APATCH_PATH = "/new.apatch";
    public static String TEXTFONTTYPEPATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/YuanTi.TTF";
    public static String FONTSIZI;
    public static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    public static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    public static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z'};

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = this;
        MobclickAgent.openActivityDurationTrack(false); // 禁止默认的Activity页面统计方式。

//         JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
//         JPushInterface.init(this); // 初始化 JPush
        try {
            PackageInfo mPackageInfo = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);
            VERSION_NAME = mPackageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        JPushInterface.init(this);
        initChange();
        init();
        getIp();
    }

    public static void getIp() {
        String ip = getNowIp(mContext);
        String testip = getNowTestIp(mContext);
        Log.i(TAG, "getIp: " + ip + "  test ip" + testip);
        HttpUrlList.setIpAndTestIp(mContext, ip, testip);
    }

    private void initChange() {
        mPatchManager = new PatchManager(this);
        Log.i(TAG, "initChange: " + VERSION_NAME);
        mPatchManager.init(VERSION_NAME); // 版本号

        // 加载 apatch
        mPatchManager.loadPatch();

        //apatch文件的目录
        String patchFileString = Environment.getExternalStorageDirectory().getAbsolutePath() +
                APATCH_PATH;
        File apatchPath = new File(patchFileString);

        if (apatchPath.exists()) {
//            ToastUtil.showToast("补丁文件存在");
            try {
                //添加apatch文件
                mPatchManager.addPatch(patchFileString);
            } catch (IOException e) {
//                ToastUtil.showToast("打补丁出错了");
                e.printStackTrace();
            }
        } else {
//            ToastUtil.showToast("补丁文件不存在");
        }


    }

    public MyApplication() {
        activitys = new LinkedList<Activity>();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        int versioncode = 0;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        versioncode = info.versionCode;
        Log.d("getVersionCode", "versioncode:" + versioncode);
        return versioncode;

    }

    public static ArrayList<HashMap<String, String>> getInstenceActivityList() {
        if (activity_list == null) {
            activity_list = new ArrayList<HashMap<String, String>>();
        } else {
            return activity_list;
        }
        if (activity_list.size() > 8) {
            activity_list.clear();
        }
        return activity_list;

    }

    public static ArrayList<HashMap<String, String>> getInstenceVenueList() {
        if (venue_list == null) {
            venue_list = new ArrayList<HashMap<String, String>>();
        } else {
            return venue_list;
        }
        if (venue_list.size() > 8) {
            venue_list.clear();
        }
        return venue_list;

    }

    private void init() {
        windowHeight = WindowsUtil.getwindowsHight(mContext);
        windowWidth = WindowsUtil.getWindowsWidth(mContext);
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(Options.getImageConfig(mContext));
        // 监听崩溃的事件
        CrashHandler crashHandler = CrashHandler.getInstance();//
        // 报错监听类注册crashHandler
        crashHandler.init(this);// 报错监听类初始化
        // PatchManager mPatchManager = new PatchManager(this);
        // mPatchManager.init("3.5.4");
        // mPatchManager.loadPatch();
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    public void addActivitys(Activity activity) {
        if (activitys != null) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            activitys.add(activity);
        }
    }

    public static boolean isFromMyLove() {
        return isFromMyLove;
    }

    public static void setIsFromMyLove(boolean isFromMyLove) {
        MyApplication.isFromMyLove = isFromMyLove;
    }

    public Handler getMyBindHandler() {
        return MyBindHandler;
    }

    public void setMyBindHandler(Handler myBindHandler) {
        MyBindHandler = myBindHandler;
    }

    public List<UserBehaviorInfo> getSelectTypeList() {
        return selectTypeList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setSelectTypeList(List<UserBehaviorInfo> selectTypeList) {
        this.selectTypeList = selectTypeList;
    }

    public Map<String, Integer> getWeatherMap() {
        if (weatherMap == null) {
            WeatherDataUtil weatherDataUtil = new WeatherDataUtil();
            weatherMap = weatherDataUtil.setWeather();
        }
        return weatherMap;
    }

    public Map<String, Integer> getWeatherMainMap() {
        if (weatherMainMap == null) {
            WeatherDataUtil weatherDataUtil = new WeatherDataUtil();
            weatherMainMap = weatherDataUtil.setMainWeather();
        }
        return weatherMainMap;
    }

    /**
     * 退出APP
     */
    public void exit() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 退出先前所有的Activity 但是不退出应用
     */
    public void exitActivity() {
        if (activitys != null && activitys.size() > 0) {
            for (Activity activity : activitys) {
                activity.finish();
            }
        }
    }

    /**
     * 退出activity的层数
     */
    public void exitLayer(int number) {
        if (activitys != null && number <= activitys.size()) {
            for (int i = number - 1; i >= (activitys.size() - number); i--) {
                activitys.get(i).finish();
                activitys.remove(i);
            }
        }
    }

    public void setImageView(Context context, String url, ImageView img) {
        Glide.with(context).load(url).into(img);
    }

    public ImageLoader getImageLoader() {
        return ImageLoader.getInstance();
    }

    /**
     * 从内存卡中异步加载本地图片
     *
     * @param uri
     * @param imageView
     */
    public static void displayFromSDCard(String uri, ImageView imageView) {
        // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
        mImageLoader.displayImage("file://" + uri, imageView);
    }

    /**
     * 从assets文件夹中异步加载图片
     *
     * @param imageName 图片名称，带后缀的，例如：1.png
     * @param imageView
     */
    public static void dispalyFromAssets(String imageName, ImageView imageView) {
        // String imageUri = "assets://image.png"; // from assets
        mImageLoader.displayImage("assets://" + imageName, imageView);
    }

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    public static void displayFromDrawable(int imageId, ImageView imageView) {
        // String imageUri = "drawable://" + R.drawable.image; // from drawables
        // (only images, non-9patch)
        mImageLoader.displayImage("drawable://" + imageId, imageView);
    }

    /**
     * 从内容提提供者中抓取图片
     */
    public static void displayFromContent(String uri, ImageView imageView) {
        // String imageUri = "content://media/external/audio/albumart/13"; //
        // from content provider
        mImageLoader.displayImage("content://" + uri, imageView);
    }

    public SearchListInfo getSearchListInfo() {
        return searchListInfo;
    }

    public void setSearchListInfo(SearchListInfo searchListInfo) {
        this.searchListInfo = searchListInfo;
    }

    public static Context getContext() {
        return mContext;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public Handler getmReserveHandler() {
        return mReserveHandler;
    }

    public void setmReserveHandler(Handler mReserveHandler) {
        this.mReserveHandler = mReserveHandler;
    }

    public Handler getmRoomHandler() {
        return mRoomHandler;
    }

    public void setmRoomHandler(Handler mRoomHandler) {
        this.mRoomHandler = mRoomHandler;
    }

    public Handler getmLableChoose() {
        return mLableChoose;
    }

    public void setmLableChoose(Handler mLableChoose) {
        this.mLableChoose = mLableChoose;
    }

    public Handler getmMainHandler() {
        return mMainHandler;
    }

    public void setmMainHandler(Handler mMainHandler) {
        this.mMainHandler = mMainHandler;
    }

    public Handler getmUserHandler() {
        return mUserHandler;
    }

    public void setmUserHandler(Handler mUserHandler) {
        this.mUserHandler = mUserHandler;
    }

    public Handler getmMainFragment() {
        return mMainFragment;
    }

    public Handler getActivityHandler() {
        return activityHandler;
    }

    public void setActivityHandler(Handler activityHandler) {
        this.activityHandler = activityHandler;
    }

    public void setmMainFragment(Handler mMainFragment) {
        this.mMainFragment = mMainFragment;
    }

    public WindowInfo getVenueWindowList() {
        return venueWindowList;
    }

    public void setVenueWindowList(WindowInfo venueWindowList) {
        this.venueWindowList = venueWindowList;
    }

    public WindowInfo getGroupWindowList() {
        return groupWindowList;
    }

    public void setGroupWindowList(WindowInfo groupWindowList) {
        this.groupWindowList = groupWindowList;
    }

    public WindowInfo getNotInvoluntaryList() {
        return notInvoluntaryList;
    }

    public void setNotInvoluntaryList(WindowInfo notInvoluntaryList) {
        this.notInvoluntaryList = notInvoluntaryList;
    }

    public Handler getVenueListHandler() {
        return venueListHandler;
    }

    public void setVenueListHandler(Handler venueListHandler) {
        this.venueListHandler = venueListHandler;
    }

    public Handler getGroupListHandler() {
        return groupListHandler;
    }

    public void setGroupListHandler(Handler groupListHandler) {
        this.groupListHandler = groupListHandler;
    }

    public Handler getNotInvoluntaryHandler() {
        return notInvoluntaryHandler;
    }

    public void setNotInvoluntaryHandler(Handler notInvoluntaryHandler) {
        this.notInvoluntaryHandler = notInvoluntaryHandler;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    /**
     * 这里可以替换为detectAll(); 就包括了磁盘读写和网络I/O;
     * 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log;
     */

    @SuppressLint({"NewApi", "NewApi"})
    public void initNetwork() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
                .penaltyLog() // 打印logcat
                .penaltyDeath().build());
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeight(GridView gridview, int num, int vertical_height) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        if (num < listAdapter.getCount()) {
            totalHeight = (totalHeight / num) + (listAdapter.getCount() / num) * vertical_height;
        } else {
            totalHeight = (totalHeight / listAdapter.getCount() - 1) + vertical_height *
                    listAdapter.getCount();
        }

        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridview) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight = totalHeight / 4;
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        // + (gridview.getHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeightBasedOnChildrenTwo(GridView gridview) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight = totalHeight / 2;
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        // + (gridview.getHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeightTwo(GridView gridview) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        if (listAdapter.getCount() >= 3) {
            totalHeight = (int) ((totalHeight / 2) * 1.1);
        } else if (listAdapter.getCount() >= 1 && listAdapter.getCount() < 3) {
            totalHeight = (int) ((totalHeight / 2) * 2.3);
        }

        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        // + (gridview.getHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeightBasedOnChildrenTest(GridView gridview) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight = (int) ((totalHeight / 2) * 0.75);
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        // + (gridview.getHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeightBasedOnChildrenCalender(
            GridView gridview) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight = totalHeight / 6;
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        // + (gridview.getHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 GridView 的高度
     *
     * @param gridview
     */
    public static void setGridViewHeightBasedOnChildrenCalendertwo(
            GridView gridview) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = gridview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, gridview);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        totalHeight = (int) ((totalHeight / 5) * 0.85);
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.height = totalHeight;
        // + (gridview.getHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        gridview.setLayoutParams(params);
    }

    /**
     * 动态获取 ListView 的高度
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView, int divider_height) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listAdapter.getCount() * divider_height);
        listView.setLayoutParams(params);
    }

    /**
     * 动态获取 ListView 的高度
     *
     * @param listView
     */
    public static void setListViewHeightPop(ListView listView, int divider_height) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listAdapter.getCount() * divider_height);
        Log.i(TAG, "setListViewHeightPop: " + params.height);
        if (params.height < 1300) {
            params.height = 1300;
        }
        listView.setLayoutParams(params);
    }

    /**
     * 动态获取 ListView 的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = (int) (totalHeight * 1.1);
        // * (listAdapter.getCount() + 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 动态获取 Home listview 的高度
     *
     * @param listView
     */
    public static void setListViewHomeHeight(ScrollViewListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        Log.i("MyApplication", "数据 ==  " + totalHeight + listView.getDividerHeight()
                * (listAdapter.getCount() + 4));
        params.height = totalHeight + listView.getDividerHeight()
                * (listAdapter.getCount() + 4);

        listView.setLayoutParams(params);
    }

    /**
     * 动态获取 ListView 的高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren_Kill(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int) (totalHeight * 1.2);
        // + (listView.getDividerHeight() * (listAdapter.getCount() + 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 动态获取 ListView 的高度
     *
     * @param listView
     */
    public static void setHorizontalListViewBasedOnChildren(
            HorizontalListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int num = 0;
        int totalHeight = 0;
        int max_height = 0;
        for (int i = 0; i < 100; i++) {
            if (listAdapter.getCount() < 4 * i) {
                num = i;
                break;
            }
        }

        Log.i("ceshi", "num  ==  " + num + "  listAdapter.getCount()=="
                + listAdapter.getCount());
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // int desiredWidth=
            // View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
            // View.MeasureSpec.AT_MOST);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight = listItem.getMeasuredHeight(); // 统计所有子项的总高度
            if (max_height < totalHeight) {
                max_height = totalHeight;
            }

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        Log.i("ceshi", "totalHeight  ==  " + max_height);
        params.height = (int) (max_height * 1.5);
        // + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static SpannableStringBuilder getTextColor(String text) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < text.length(); i++) {
            if (matcherReg(String.valueOf(text.charAt(i)))) {
                list.add(i);
            }

        }
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i) + ", ");
            // style.setSpan(new
            // BackgroundColorSpan(Color.RED),list.get(i),list.get(i)+1,Spannable
            // .SPAN_EXCLUSIVE_INCLUSIVE);
            // //设置指定位置textview的背景颜色
            style.setSpan(new ForegroundColorSpan(mContext.getResources()
                    .getColor(R.color.text_color_ffb)), list.get(i), list
                    .get(i) + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
        }
        return style;
    }

    public static SpannableStringBuilder getYellowTextColor(String text) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < text.length(); i++) {
            if (matcherReg(String.valueOf(text.charAt(i)))) {
                list.add(i);
            }

        }
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i) + ", ");
            // style.setSpan(new
            // BackgroundColorSpan(Color.RED),list.get(i),list.get(i)+1,Spannable
            // .SPAN_EXCLUSIVE_INCLUSIVE);
            // //设置指定位置textview的背景颜色
            style.setSpan(new ForegroundColorSpan(mContext.getResources()
                    .getColor(R.color.text_color_ffd1)), list.get(i), list
                    .get(i) + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
        }
        return style;
    }

    public static SpannableStringBuilder getRedTextColor(String text) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < text.length(); i++) {
            if (matcherReg(String.valueOf(text.charAt(i)))) {
                list.add(i);
            }

        }
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i) + ", ");
            // style.setSpan(new
            // BackgroundColorSpan(Color.RED),list.get(i),list.get(i)+1,Spannable
            // .SPAN_EXCLUSIVE_INCLUSIVE);
            // //设置指定位置textview的背景颜色
            style.setSpan(new ForegroundColorSpan(mContext.getResources()
                            .getColor(R.color.text_color_c0)), list.get(i),
                    list.get(i) + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); // 设置指定位置文字的颜色
        }
        return style;
    }

    private static boolean matcherReg(CharSequence c) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(c.toString());
        if (m.matches()) {
            return false;
        }
        return true;
    }

    public static void setFontsSize() {
        MyApplication.FONTSIZI = "23.19MB";
    }

    public static boolean downFontbol() {
        File f = new File(MyApplication.TEXTFONTTYPEPATH);
        String size = FileSizeUtil.getAutoFileOrFilesSize(MyApplication.TEXTFONTTYPEPATH);
        setFontsSize();
        if (!f.exists() || !size.equals(MyApplication.FONTSIZI)) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean changeFontType() {
        File f = new File(MyApplication.TEXTFONTTYPEPATH);
        String size = FileSizeUtil.getAutoFileOrFilesSize(MyApplication.TEXTFONTTYPEPATH);
        setFontsSize();
        if (f.exists() && size.equals(MyApplication.FONTSIZI)) {
            return true;
        } else {
            return false;
        }
    }

    public static Typeface GetTypeFace() {
        if (typeFace == null) {
            String familyName = "宋体";
            File f = new File(TEXTFONTTYPEPATH);
            if (changeFontType()) {
                Log.i(TAG, "GetTypeFace: 存在");
                typeFace = Typeface.createFromFile(f);
            } else {
                Log.i(TAG, "GetTypeFace: 不存在");
                typeFace = Typeface.create(familyName, Typeface.BOLD);
            }
            return typeFace;
        } else {
            Log.i(TAG, "GetTypeFace: Notnull");
            return typeFace;
        }

    }

    public static String getStringToDate(long time) {
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
        Date d = new Date(time);
        return sdf.format(d);
    }

    /**
     * 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    public static String getDigit(String text) {
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(text);
        String result = m.replaceAll("");
        return result;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t * 1000);
        long mill = (long) Math.ceil(time / 1000);// 秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    // 将字符串转为时间戳

    public static String getTime(String user_time) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date d;
        try {

            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return re_time;
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;

    }

    // 将时间戳转为字符串
    public static String getStrNewTime(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;

    }

    // 将时间戳转为字符串
    public static String get_NewTime(String cc_time) {
        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;

    }

    // 版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    // 版本号
    public static int getVersionCode_wff(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 统计数据
     */
    public static void total_num(String localurl, String stype, String id) {
        Map<String, String> mParams_total = new HashMap<String, String>();
        if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0")
                || AppConfigUtil.LocalLocation.Location_longitude.equals("0.0")) {
            mParams_total.put(HttpUrlList.HTTP_LAT,
                    MyApplication.Location_latitude);
            mParams_total.put(HttpUrlList.HTTP_LON,
                    MyApplication.Location_longitude);
        } else {
            mParams_total.put(HttpUrlList.HTTP_LAT,
                    AppConfigUtil.LocalLocation.Location_latitude + "");
            mParams_total.put(HttpUrlList.HTTP_LON,
                    AppConfigUtil.LocalLocation.Location_longitude + "");
        }
        mParams_total.put("GUID", Installation.id(mContext));
        mParams_total.put("platform", "android");
        mParams_total.put("ostype", "android");
        // android 版本
        mParams_total.put("skey1", id);
        // android 版本
        mParams_total.put("skey3", MyApplication.currentapiVersion + "");
        // app 版本
        mParams_total.put("skey4", MyApplication.getVersionName(mContext) + "");
        mParams_total.put("localurl", localurl);
        mParams_total.put("stype", stype);

        String params = "";
        Iterator<Map.Entry<String, String>> it = mParams_total.entrySet()
                .iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            params += entry.getKey() + "=" + entry.getValue() + "&";
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
        }
        params = params.substring(0, params.length() - 1);
        sendPost("http://www.wenhuayun.cn/stat/stat.jsp", params);
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

    public static int dayForWeek(String pTime) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();

        c.setTime(format.parse(pTime));

        int dayForWeek = 0;

        if (c.get(Calendar.DAY_OF_WEEK) == 1) {

            dayForWeek = 7;

        } else {

            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        }

        return dayForWeek;

    }

    public static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }

    public static String getSystemTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    /**
     * 去除空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 3.5.4异步请求接口
     */

    /**
     * 3.5.4版本 请求服务器接口
     */
    public static HttpResponseText callUrlHttpPost(String strUrl, String jsonStr) {
        HttpResponseText text = new HttpResponseText();
        HttpURLConnection conn = null;
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("version", MyApplication.version);
            conn.setRequestProperty("platform", "android");
            // conn.setRequestProperty("userId", "Android");
            // conn.setRequestProperty("userLon", "Android");
            // conn.setRequestProperty("userLat", "Android");
            conn.connect();

            String contentData = jsonStr != null ? jsonStr.toString() : "{}";
            conn.getOutputStream().write(contentData.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();

            // 封装response对象
            int httpCode = conn.getResponseCode();
            text.setHttpCode(httpCode);
            result = sb.toString();
            text.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            text.setData(e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return text;
    }

    public static void selectImg(Context context, int type, String activityInfo) {
        Intent intent;
        Bundle bundle = new Bundle();
        Log.i("myapplication", "type   ===  " + type + "  activityInfo==  "
                + activityInfo);
        switch (type) {
            // 活动列表
            case 0:
                intent = new Intent(context, SearchListActivity.class);
                bundle.putString("KeyWord", activityInfo);
                bundle.putString("ActivityOrVenue", "activity");
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
            // 活动详情
            case 1:
                intent = new Intent(context, ActivityDetailActivity.class);
                intent.putExtra("eventId", activityInfo);
                context.startActivity(intent);
                break;
            // 场馆列表
            case 2:
                intent = new Intent(context, SearchListActivity.class);
                bundle.putString("KeyWord", activityInfo);
                bundle.putString("ActivityOrVenue", "venue");
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
            // 场馆详情
            case 3:
                intent = new Intent(context, VenueDetailActivity.class);
                intent.putExtra("venueId", activityInfo);
                context.startActivity(intent);
                break;
            // 日历
            case 4:
                intent = new Intent(context, CalenderActivity.class);
                context.startActivity(intent);
                break;
            // 带搜索的活动
            case 5:
                intent = new Intent(context, SearchTagListActivity.class);
                bundle.putString("TagId", activityInfo);
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
        }
    }

    public static void selectWeb(Context con, String url) {
        Intent intent = new Intent();
        intent.setClass(con, BannerWebView.class);
        intent.putExtra("url", url);
        con.startActivity(intent);
    }

    public static void saveNowAddress(Context context, String location_str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nowAddress", location_str);
        editor.commit();
    }

    public static String getNowAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("share", MODE_PRIVATE);
        String address = sharedPreferences.getString("nowAddress", "");
        return address;
    }

    public static void saveNowIp(Context context, String ip) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", ip);
        editor.commit();
    }

    public static String getNowIp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ip", MODE_PRIVATE);
        String ip = sharedPreferences.getString("ip", "");
        return ip;
    }

    public static void saveNowTestIp(Context context, String ip) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("testip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("testip", ip);
        editor.commit();
    }

    public static String getNowTestIp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("testip", MODE_PRIVATE);
        String testip = sharedPreferences.getString("testip", "");
        return testip;
    }

    public static void clearCache() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("testip", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences sharedPreferences1 = context.getSharedPreferences("ip", MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor.clear();
        editor1.clear();
        editor2.clear();
        editor.commit();
        editor1.commit();
        editor2.commit();
    }

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {

            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
            }
        }
        return buffer.toString();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }
}
