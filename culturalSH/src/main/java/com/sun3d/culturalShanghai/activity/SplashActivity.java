package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.RepairBugUtil;
import com.sun3d.culturalShanghai.adapter.ViewPagerAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.view.RoundProgressBar;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;


/**
 * 导航页面
 *
 * @author wenff
 */
public class SplashActivity extends Activity implements OnClickListener,
        OnPageChangeListener {
    private String TAG = "SplashActivity";
    private Context mContext;
    private Intent intent = null;
    private ImageView start_up_img;
    private boolean isFirstRun = true;
    private boolean isBack = true;
    private SharedPreferences sharedPreferences;
    // 定义ViewPager对象
    private ViewPager viewPager;

    // 定义ViewPager适配器
    private ViewPagerAdapter vpAdapter;

    // 定义一个ArrayList来存放View
    private ArrayList<View> views;

    // 引导图片资源
    private static final int[] pics = {R.drawable.guide1, R.drawable.guide2,
            R.drawable.guide3};

    // 底部小点的图片
    private ImageView[] points;

    // 记录当前选中位置
    private int currentIndex;
    private ImageView start_img;
    private String url;
    public RoundProgressBar mRoundProgressBar1;
    private int progress = 0;
    public TextView Cancle_tv;
    private ImageView start_img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MyApplication.version = MyApplication.getVersionName(this);
        start_img1 = (ImageView) findViewById(R.id.start_img1);
        Cancle_tv = (TextView) findViewById(R.id.Cancle_tv);
        Cancle_tv.setOnClickListener(this);
        start_up_img = (ImageView) findViewById(R.id.start_up_img);
        start_img = (ImageView) findViewById(R.id.start_img);
        mRoundProgressBar1 = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
        sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        MyApplication.getInstance().initNetwork();
        MyApplication.getInstance().addActivitys(this);
        mContext = this;
        getPath();
//        Editor editor = sharedPreferences.edit();
        if (isFirstRun) {
//            Log.d("debug", "第一次运行");
//            editor.putBoolean("isFirstRun", false);
//            editor.commit();
            initView();
            initData();
        } else {
            new Thread(run).start();
            getSplashImage();
        }


    }

    public void start() {
        intent = new Intent(SplashActivity.this,
                MainFragmentActivity.class);
        startActivity(intent);
        finish();
    }

    private void getPath() {
        JSONObject json = new JSONObject();
        // Log.i(TAG, "请求的参数  ==  " + _param.toString());
        MyHttpRequest.onStartHttpPostJSON(HttpUrlList.ActivityDetail.PATH,
                json, new HttpRequestCallback() {
                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        // TODO Auto-generated method stub
                        Log.i(TAG, "看看返回的数据===  " + statusCode + "  result==  "
                                + resultStr);
                        if (statusCode == 1) {
                            try {
                                JSONObject json = new JSONObject(resultStr);
                                String data = json.optString("data", "");
                                if (!data.equals("")) {
                                    MyApplication.Img_Path = data;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化ArrayList对象
        views = new ArrayList<View>();
        start_img1.setVisibility(View.GONE);
        // 实例化ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setVisibility(View.VISIBLE);
        // start_img.setVisibility(View.VISIBLE);
        // 实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views, this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ScaleType.FIT_XY);
            MyApplication.displayFromDrawable(pics[i], iv);
            views.add(iv);
        }
        // 设置数据
        viewPager.setAdapter(vpAdapter);
        // 设置监听
        viewPager.setOnPageChangeListener(this);

        // 初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);
        linearLayout.setVisibility(View.VISIBLE);
        points = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            // 默认都设为灰色
            points[i].setEnabled(true);
            // 给每个小点设置监听
            points[i].setOnClickListener(this);
            // 设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        // 设置当面默认的位置
        currentIndex = 0;
        // 设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
    }

    /**
     * 当滑动状态改变时调用
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    /**
     * 当当前页面被滑动时调用
     */

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    /**
     * 当新的页面被选中时调用
     */

    @Override
    public void onPageSelected(int position) {
        // 设置底部小点选中状态
        setCurDot(position);
    }

    /**
     * 通过点击事件来切换当前的页面
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Cancle_tv:
                isBack = false;
                intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                int position = (Integer) v.getTag();
                setCurView(position);
                setCurDot(position);
                break;
        }

    }

    /**
     * 设置当前页面的位置
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        points[positon].setEnabled(false);
        points[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    private final String mPageName = "SplashActivity";

    /**
     * 初始换分享SDK
     */
    private void initShareSDK() {
        ShareSDK.initSDK(this);
        ShareSDK.setConnTimeout(20000);
        ShareSDK.setReadTimeout(20000);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        RepairBugUtil.getInstance().release();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initShareSDK();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.splash, menu);
        setMenuBackground();
        return true;
    }

    // 关键代码为重写Layout.Factory.onCreateView()方法自定义布局
    protected void setMenuBackground() {
        getLayoutInflater().setFactory(
                new android.view.LayoutInflater.Factory() {
                    /**
                     * name - Tag name to be inflated.<br/>
                     * context - The context the view is being created in.<br/>
                     * attrs - Inflation attributes as specified in XML file.<br/>
                     */
                    public View onCreateView(String name, Context context,
                                             AttributeSet attrs) {
                        // 指定自定义inflate的对象
                        if (name.equalsIgnoreCase("com.android.internal.view.menu" +
                                ".IconMenuItemView")) {
                            try {
                                LayoutInflater f = getLayoutInflater();
                                final View view = f.createView(name, null,
                                        attrs);
                                view.setBackgroundResource(R.drawable.location_marker);
                                return view;
                            } catch (InflateException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                });
    }


    private void getSplashImage() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("height", "1787");
        params.put("width", "1242");

        MyHttpRequest.onHttpPostParams(HttpUrlList.Banner.WFF_GETMOBILEIMAGE,
                params, new HttpRequestCallback() {

                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        // TODO Auto-generated method stub
                        if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
                            try {
                                JSONObject jo = new JSONObject(resultStr);
                                url = jo.getString("data");
                                handler2.sendEmptyMessage(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    Handler handler2 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "url==  " + url);
                    // 图片资源
                    String bit_url = url;
                    // 得到可用的图片
                    Bitmap bitmap = getHttpBitmap(bit_url);
                    // 显示
                    start_img.setImageBitmap(bitmap);

                    // MyApplication.getInstance().getImageLoader()
                    // .displayImage(url, start_img);

                    break;
                default:
                    break;
            }
        }

    };


    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    private Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL
                    .openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    private boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;

    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            while (progress <= 100) {
                progress += 2;
                try {
                    mRoundProgressBar1.setProgress(progress);
                    if (isBack == false) {
                        break;
                    }
                    if (progress == 98) {
                        intent = new Intent(SplashActivity.this,
                                MainFragmentActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

}
