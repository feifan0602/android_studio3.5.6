package com.sun3d.culturalShanghai.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.GsonUtils;
import com.sun3d.culturalShanghai.Util.RepairBugUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WeakHandler;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.fragment.CalenderFragment;
import com.sun3d.culturalShanghai.fragment.HomeFragment;
import com.sun3d.culturalShanghai.fragment.NearbyFragment;
import com.sun3d.culturalShanghai.fragment.OrganizationsFragment;
import com.sun3d.culturalShanghai.fragment.PersonalCenterFragmet;
import com.sun3d.culturalShanghai.fragment.SpaceFragment;
import com.sun3d.culturalShanghai.fragment.VenueFragment;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.PatchBean;
import com.sun3d.culturalShanghai.service.DownloadTextFont;
import com.sun3d.culturalShanghai.view.CircleImageView;
import com.sun3d.culturalShanghai.windows.EventWindows;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.sun3d.culturalShanghai.windows.VenueWindows;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainFragmentActivity extends FragmentActivity implements
        OnClickListener {
    private Context mContext;
    private RelativeLayout mTitle;
    private boolean mToExit = false;
    public static final int HANDLER_TAB_CODE = 201;// 通知切换fragment
    public static int mTabType = 0;
    private static MainFragmentActivity mMainFragmentActivity;
    private TextView mActivity, mVeune;
    private View activity, veune;
    private FragmentTransaction transaction;
    private ImageView bottom_home_iv, bottom_nearby_iv, bottom_calender_iv,
            bottom_venue_iv, bottom_my_iv, bottom_activity_iv,
            bottom_community_iv;
    private TextView bottom_home_tv, bottom_nearby_tv, bottom_calender_tv,
            bottom_venue_tv, bottom_my_tv, bottom_activity_tv,
            bottom_community_tv;
    private LinearLayout bottom_home_ll, bottom_nearby_ll, bottom_calender_ll,
            bottom_venue_ll, bottom_my_ll, bottom_activity_ll,
            bottom_community_ll;
    private OrganizationsFragment organizations_view;// 社团
    private SpaceFragment space_view;
    private HomeFragment home_view;
    private ActivityFragment Activity_view;
    private VenueFragment Venue_view;
    private CalenderFragment Calender_view;
    private NearbyFragment Nearby_view;
    private PersonalCenterFragmet My_view;
    private int position;
    private SharedPreferences sharedPreferences;
    public static final int MSG_WHAT_DOWNLOAD = 0x111;
    public static boolean isForeground = false;
    private String TAG = "MainFragmentActivity";
    private static final String APATCH_PATH = "new.apatch";
    private LinearLayout frame_bottom;
    private boolean isFirstRun;

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;
        MobclickAgent.onPause(mContext);
        // JPushInterface.onPause(mContext);
    }

    public static MainFragmentActivity getIntance() {
        return mMainFragmentActivity;

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
        RepairBugUtil.getInstance().release();
        LoadingTextShowPopWindow.dismissPop();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isForeground = true;
        MobclickAgent.onResume(mContext);
        // JPushInterface.onResume(mContext);
        MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
        mHandler.sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
        /**
         * 返回时候的状态
         */
        switch (MyApplication.change_fragment) {
            case 0:
                setTabSelection(0);
                break;
            case 1:
                setTabSelection(1);
                break;
            case 2:
                setTabSelection(2);
                break;
            case 3:
                setTabSelection(3);
                break;
            case 4:
                setTabSelection(4);
                break;

            default:
                break;
        }
    }

    public void setOnLogin_Status(OnLogin_Status onLogin_Status) {
        this.mOnLogin_Status = onLogin_Status;
    }

    private OnLogin_Status mOnLogin_Status;

    public interface OnLogin_Status {
        public void onLoginSuccess();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        MyApplication.getInstance().initNetwork();
        super.onCreate(arg0);
        setContentView(R.layout.activity_fragment_main);
        sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        MyApplication.getInstance().addActivitys(this);
        sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        //这里是字体的下载 如果没有这个文件 就进行下载
        if (MyApplication.downFontbol()){
            Log.i(TAG,"文件不在了");
            Intent iService = new Intent();
            iService.setClass(this, DownloadTextFont.class);
            iService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(iService);
        }
        MyApplication.getInstance().setmMainFragment(mHandler);
        mContext = this;
        mMainFragmentActivity = this;
        registerMessageReceiver();
        initView();
        initData();

    }


    public void openBottom() {
        MyApplication.OPENORCLOSEBOTTOM = true;
        home_view.right_iv.setImageResource(R.drawable.sh_activity_search_icon);
        frame_bottom.setVisibility(View.VISIBLE);
    }

    public void closeBottom() {
        MyApplication.OPENORCLOSEBOTTOM = false;
        home_view.right_iv.setImageResource(R.drawable.tomy);
        frame_bottom.setVisibility(View.GONE);
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.sun3d.culturalShanghai" +
            ".MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        Log.i(TAG, "registerMessageReceiver: ");
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getAction());
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String message = intent.getStringExtra(KEY_MESSAGE);
                Message msg = new Message();
                msg.what = MSG_WHAT_DOWNLOAD;
                msg.obj = message;
                mHandler_receive.sendMessage(msg);
            }
        }
    }

    private WeakHandler mHandler_receive = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == MSG_WHAT_DOWNLOAD) {
                String message = (String) msg.obj;
                if (TextUtils.isEmpty(message)) return false;
                try {
                    Log.i(TAG, "mHandler_receive: " + msg.obj + "  what==  " + msg.what);
                    PatchBean bean = GsonUtils.getInstance().parse(PatchBean.class, message);
                    downloadPatch(bean.app_v, bean.path_v, bean.url);
//                    RepairBugUtil.getInstance().comparePath(MainFragmentActivity.this, bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });

    private void downloadPatch(String app_v, String path_v, final String url) {


        if (!MyApplication.VERSION_NAME.equals(app_v)) {
            return;
        }
        ToastUtil.showToast("开始下载");
        new Thread() {
            @Override
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    int fileSize = Integer.parseInt(entity.getContentLength() + "");
                    int downloadSize = 0;
                    InputStream is = entity.getContent();
                    FileOutputStream fos = null;
                    if (is != null) {
                        File file = new File(Environment.getExternalStorageDirectory()
                                .getAbsolutePath(), APATCH_PATH);
                        if (file.exists()) {
                            if (file.isFile()) {
                                file.delete();
                            }
                        }
                        file.createNewFile();

                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            downloadSize += ch;
                        }

                        fos.flush();
                        if (fos != null) {
                            if (fileSize == downloadSize) {
                                sendMsg(0);
                            }
                            fos.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler update_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        //设置进度条最大值
                       ToastUtil.showToast("补丁下载完成");
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        update_handler.sendMessage(msg);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        getstystemSize();

    }

    /**
     * 这个是什么？
     */
    private void getstystemSize() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        MyApplication.stystemTitleHeight = statusBarHeight;
    }

    private void initView() {
        frame_bottom = (LinearLayout) findViewById(R.id.frame_bottom);
        bottom_home_iv = (ImageView) findViewById(R.id.bottom_home_iv);
        bottom_nearby_iv = (ImageView) findViewById(R.id.bottom_nearby_iv);
        bottom_calender_iv = (ImageView) findViewById(R.id.bottom_calender_iv);
        bottom_venue_iv = (ImageView) findViewById(R.id.bottom_venue_iv);
        bottom_my_iv = (ImageView) findViewById(R.id.bottom_my_iv);
        bottom_activity_iv = (ImageView) findViewById(R.id.bottom_activity_iv);
        bottom_community_iv = (ImageView) findViewById(R.id.bottom_community_iv);

        bottom_home_tv = (TextView) findViewById(R.id.bottom_home_tv);
        bottom_nearby_tv = (TextView) findViewById(R.id.bottom_nearby_tv);
        bottom_calender_tv = (TextView) findViewById(R.id.bottom_calender_tv);
        bottom_venue_tv = (TextView) findViewById(R.id.bottom_venue_tv);
        bottom_my_tv = (TextView) findViewById(R.id.bottom_my_tv);
        bottom_activity_tv = (TextView) findViewById(R.id.bottom_activity_tv);
        bottom_community_tv = (TextView) findViewById(R.id.bottom_community_tv);

        bottom_home_tv.setTypeface(MyApplication.GetTypeFace());
        bottom_nearby_tv.setTypeface(MyApplication.GetTypeFace());
        bottom_calender_tv.setTypeface(MyApplication.GetTypeFace());
        bottom_venue_tv.setTypeface(MyApplication.GetTypeFace());
        bottom_my_tv.setTypeface(MyApplication.GetTypeFace());
        bottom_activity_tv.setTypeface(MyApplication.GetTypeFace());
        bottom_community_tv.setTypeface(MyApplication.GetTypeFace());

        bottom_home_ll = (LinearLayout) findViewById(R.id.bottom_home_ll);
        bottom_nearby_ll = (LinearLayout) findViewById(R.id.bottom_nearby_ll);
        bottom_calender_ll = (LinearLayout) findViewById(R.id.bottom_calender_ll);
        bottom_venue_ll = (LinearLayout) findViewById(R.id.bottom_venue_ll);
        bottom_my_ll = (LinearLayout) findViewById(R.id.bottom_my_ll);
        bottom_activity_ll = (LinearLayout) findViewById(R.id.bottom_activity_ll);
        bottom_community_ll = (LinearLayout) findViewById(R.id.bottom_community_ll);

        bottom_activity_ll.setOnClickListener(this);
        bottom_home_ll.setOnClickListener(this);
        bottom_nearby_ll.setOnClickListener(this);
        bottom_calender_ll.setOnClickListener(this);
        bottom_venue_ll.setOnClickListener(this);
        bottom_my_ll.setOnClickListener(this);
        bottom_community_ll.setOnClickListener(this);
        mTitle = (RelativeLayout) findViewById(R.id.title);
        mPersonal = (CircleImageView) mTitle.findViewById(R.id.title_left);
        findViewById(R.id.title_left_layout).setOnClickListener(this);
        mRight = (ImageButton) findViewById(R.id.title_right);
        // mRight.setOnClickListener(this);
        mActivity = (TextView) findViewById(R.id.index_title_activity);
        mVeune = (TextView) findViewById(R.id.index_title_venue);
        activity = (View) findViewById(R.id.index_title_line1);
        veune = (View) findViewById(R.id.index_title_line2);
        mActivity.setOnClickListener(this);
        mVeune.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mTabType = 0;
        setTitle();
        mFragmentManager = getSupportFragmentManager();
        mActivityFragment = (ActivityFragment) mFragmentManager
                .findFragmentById(R.id.main_activity_fragment);
        mVenueFragment = (VenueFragment) mFragmentManager
                .findFragmentById(R.id.main_venue_fragment);
        switchFragment(mActivityFragment);
        setHeadImg();
        if (MyApplication.change_fragment == 0) {
            setTabSelection(0);
        }

    }

    /**
     * 显示哪个fragment
     *
     * @param fragment
     */
    private void switchFragment(Fragment fragment) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mActivityFragment);
        mTransaction.hide(mVenueFragment);
        mTransaction.show(fragment);
        mTransaction.commit();

    }

    /**
     * 设置标题 隐藏不需要的fragment 将字体透明度变暗变明
     */
    private void setTitle() {
        switch (mTabType) {
            case 0:
                mActivity.setAlpha(1);
                mVeune.setAlpha((float) 0.6);
                activity.setVisibility(View.VISIBLE);
                veune.setVisibility(View.GONE);
                break;
            case 1:
                mActivity.setAlpha((float) 0.6);
                mVeune.setAlpha(1);
                activity.setVisibility(View.GONE);
                veune.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        // if (!ButtonUtil.isDelayClick()) {
        // return;
        // }
        Intent intent = null;
        switch (arg0.getId()) {
            /**
             * 左上角的图标 登陆了 则进入到个人中心 未登录则到达登陆页面
             */
            case R.id.title_left_layout:
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                } else {
                    intent = new Intent(mContext, PersonalCenterActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.index_title_activity:// 活动
                mTabType = 0;
                setTitle();
                switchFragment(mActivityFragment);
                break;
            case R.id.index_title_venue:// 场馆
                mTabType = 1;
                setTitle();
                switchFragment(mVenueFragment);
                break;
            case R.id.title_right:
                if (mTabType == 1) {// 场馆
                    VenueWindows.getInstance(mContext).show(mTitle, true, "场馆");
                    // Intent intentMap = new Intent(mContext,
                    // SearchNearbyActivity.class);
                    // intentMap.putExtra("ListType", "2");
                    // startActivity(intentMap);
                } else {
                    EventWindows.getInstance(mContext).showSearch(mTitle, true);
                }

                break;
            case R.id.bottom_home_ll:
                MyApplication.change_fragment = 0;
                setTabSelection(0);
                break;
            case R.id.bottom_activity_ll:
                MyApplication.change_fragment = 1;
                setTabSelection(1);
                break;
            // case R.id.bottom_nearby_ll:
            // setTabSelection(1);
            // break;
            // case R.id.bottom_calender_ll:
            // setTabSelection(2);
            // break;
            case R.id.bottom_venue_ll:
                MyApplication.change_fragment = 2;
                setTabSelection(2);
                break;
            case R.id.bottom_community_ll:
                MyApplication.change_fragment = 3;
                setTabSelection(3);
                break;
            case R.id.bottom_my_ll:
                MyApplication.change_fragment = 4;
                setTabSelection(4);
                break;

            default:
                break;
        }
    }

    /**
     * 设置左上角头部的iamgeview
     */
    private void setHeadImg() {
        if (null == MyApplication.loginUserInfor.getUserHeadImgUrl()
                || MyApplication.loginUserInfor.getUserHeadImgUrl().equals("")) {
            mPersonal.setBackgroundResource(R.drawable.sh_icon_personal_n);
        } else {
            mPersonal.setBackgroundResource(R.color.transparent);
            MyApplication
                    .getInstance()
                    .getImageLoader()
                    .displayImage(
                            MyApplication.loginUserInfor.getUserHeadImgUrl(),
                            mPersonal);
        }
    }

    private Handler mHandler = new Handler() {
        @SuppressLint("NewApi")
        public void handleMessage(android.os.Message msg) {
            // TODO Auto-generated method stub
            // 更新用户头像
            switch (msg.what) {
                case MyApplication.HANDLER_USER_CODE:
                    setHeadImg();
                    break;
                case HANDLER_TAB_CODE:
                    mTabType = msg.arg1;
                    setTitle();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 圆形的imageview
     */
    private CircleImageView mPersonal;
    private ImageButton mRight;
    private FragmentManager mFragmentManager;
    private ActivityFragment mActivityFragment;
    private VenueFragment mVenueFragment;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            startExit();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 启动退出
     */
    private void startExit() {
        Timer tExit = null;
        // 处理二次点击退出
        if (mToExit == false) {
            mToExit = true;
            ToastUtil.showToast(getString(R.string.app_exit));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    mToExit = false;
                }
            }, 2500);
        } else {
            // 销毁所有的activity
            MyApplication.getInstance().exit();
            finish();
        }
    }

    public void setTabSelection(int i) {

        clearSelection();
        transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                position = 0;
                bottom_home_iv.setBackgroundResource(R.drawable.btn_shouye_on);
                if (home_view == null) {
                    home_view = new HomeFragment();
                    transaction.add(R.id.content, home_view);
                } else {
                    transaction.show(home_view);
                }
                home_view.setMainFragment(this);
                break;
            // 原先的首页 现在变成了 活动
            case 1:
                position = 1;
                bottom_activity_iv.setBackgroundResource(R.drawable.btn_huodong_on);
                if (Activity_view == null) {
                    Activity_view = new ActivityFragment();
                    transaction.add(R.id.content, Activity_view);
                } else {
                    transaction.show(Activity_view);
                }
                break;
            // case 1:
            // position = 1;
            // MyApplication.change_ho_listviw = 1;
            // bottom_nearby_iv.setBackgroundResource(R.drawable.nearby_press);
            // if (Nearby_view == null) {
            // Nearby_view = new NearbyFragment();
            // transaction.add(R.id.content, Nearby_view);
            // } else {
            // transaction.show(Nearby_view);
            // }
            // break;
            // case 2:
            // position = 2;
            // MyApplication.change_ho_listviw = 0;
            // bottom_calender_iv.setBackgroundResource(R.drawable.calender_press);
            // if (Calender_view == null) {
            // Calender_view = new CalenderFragment();
            // transaction.add(R.id.content, Calender_view);
            // } else {
            // transaction.show(Calender_view);
            // }
            // break;
            // 原先的场所 变成了 现在的空间
            case 2:
                position = 2;
                bottom_venue_iv.setBackgroundResource(R.drawable.btn_kongjian_on);
                if (space_view == null) {
                    space_view = new SpaceFragment();
                    transaction.add(R.id.content, space_view);
                } else {
                    transaction.show(space_view);
                }
                break;
            // 这里是社团
            case 3:
                position = 3;
                bottom_community_iv
                        .setBackgroundResource(R.drawable.btn_shetuan_on);
                if (organizations_view == null) {
                    organizations_view = new OrganizationsFragment();
                    transaction.add(R.id.content, organizations_view);
                } else {
                    transaction.show(organizations_view);
                }

                // if (Venue_view == null) {
                // Venue_view = new VenueFragment();
                // transaction.add(R.id.content, Venue_view);
                // } else {
                // transaction.show(Venue_view);
                // }
                break;
            case 4:
                position = 4;
                bottom_my_iv.setBackgroundResource(R.drawable.btn_wo_on);
                if (My_view == null) {
                    My_view = new PersonalCenterFragmet();
                    transaction.add(R.id.content, My_view);
                } else {
                    transaction.show(My_view);
                }
                break;
        }

        transaction.commit();

    }

    private void clearSelection() {
        if (Activity_view != null && Activity_view.pw != null
                && Activity_view.pw.isShowing()) {
            Activity_view.pw.dismiss();
            Activity_view.sieve_ll.setVisibility(View.GONE);
            Activity_view.mListAdapter.setSieveVisible(0, "");
        }
        // if (Calender_view != null && Calender_view.pw != null
        // && Calender_view.pw.isShowing()) {
        // Calender_view.pw.dismiss();
        // Calender_view.sieve_ll.setVisibility(View.VISIBLE);
        // }
        // if (Nearby_view != null && Nearby_view.pw != null
        // && Nearby_view.pw.isShowing()) {
        // Nearby_view.pw.dismiss();
        // Nearby_view.nearby_ll.setVisibility(View.VISIBLE);
        // }
        bottom_home_iv.setBackgroundResource(R.drawable.btn_shouye);
        bottom_activity_iv.setBackgroundResource(R.drawable.btn_huodong);
        bottom_community_iv.setBackgroundResource(R.drawable.btn_shetuan);
        bottom_venue_iv.setBackgroundResource(R.drawable.btn_kongjian);
        bottom_my_iv.setBackgroundResource(R.drawable.btn_wo);

        bottom_nearby_iv.setBackgroundResource(R.drawable.nearby);
        bottom_calender_iv.setBackgroundResource(R.drawable.calender);

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (Activity_view != null) {
            transaction.hide(Activity_view);
        }
        if (home_view != null) {
            transaction.hide(home_view);
        }
        if (space_view != null) {
            transaction.hide(space_view);
        }
        if (organizations_view != null) {
            transaction.hide(organizations_view);
        }
        if (Calender_view != null) {
            transaction.hide(Calender_view);
        }

        if (Nearby_view != null) {
            transaction.hide(Nearby_view);
        }
        if (My_view != null) {
            transaction.hide(My_view);
        }
        if (Venue_view != null) {
            transaction.hide(Venue_view);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt("position");
        setTabSelection(position);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // 记录当前的position
        outState.putInt("position", position);
    }

    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainFragmentActivity
            .MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
