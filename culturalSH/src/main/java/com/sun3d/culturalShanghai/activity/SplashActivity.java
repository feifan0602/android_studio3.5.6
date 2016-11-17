package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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
    // private UserInfor mUserInfor;
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
    private String url, update_url;
    public RoundProgressBar mRoundProgressBar1;
    private int progress = 0;
    private ProgressDialog pd;
    private String UPDATE_SERVERAPK = "CulturalSH.apk";
    public TextView Cancle_tv;
    private ImageView start_img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // final Animation alpha = AnimationUtils.loadAnimation(this,
        // R.anim.anim_alpha);
        MyApplication.version = MyApplication.getVersionName(this);
        start_img1 = (ImageView) findViewById(R.id.start_img1);
        Cancle_tv = (TextView) findViewById(R.id.Cancle_tv);
        Cancle_tv.setOnClickListener(this);
        start_up_img = (ImageView) findViewById(R.id.start_up_img);
        start_img = (ImageView) findViewById(R.id.start_img);
        mRoundProgressBar1 = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
        // start_up_img.startAnimation(alpha);
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
            getSplashVersion();
//            Log.d("debug", "不是第一次运行");
        }


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
        // 设置语音模块播报
    }

    /**
     * 获取筛选条件
     */
    // private void getData() {
    // getMyLocation();
    // MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
    // Map<String, String> params = new HashMap<String, String>();
    // // 获取活动筛选条件
    // MyHttpRequest.onHttpPostParams(
    // HttpUrlList.SearchUrl.HTTP_APPHOTACTIVITY, params,
    // new HttpRequestCallback() {
    //
    // @Override
    // public void onPostExecute(int statusCode, String resultStr) {
    // // TODO Auto-generated method stub
    // List<SearchInfo> timeList = new ArrayList<SearchInfo>();
    // timeList.add(new SearchInfo("1", "今天"));
    // timeList.add(new SearchInfo("2", "明天"));
    // timeList.add(new SearchInfo("3", "本周末"));
    // timeList.add(new SearchInfo("4", "本周"));
    // timeList.add(new SearchInfo("", "自定义"));
    // timeList.add(new SearchInfo("", "全部"));
    // SearchListInfo info = new SearchListInfo();
    // info = JsonUtil.getSearchListInfo(resultStr);
    // info.setTimeList(timeList);
    // MyApplication.getInstance().setSearchListInfo(info);
    // }
    // });
    //
    // // 获取场馆筛选条件
    // params.put("appType", "3");
    // MyHttpRequest.onHttpPostParams(HttpUrlList.Window.VENUE_URL, params,
    // new HttpRequestCallback() {
    //
    // @Override
    // public void onPostExecute(int statusCode, String resultStr) {
    // // TODO Auto-generated method stub
    // WindowInfo info = new WindowInfo();
    // info = JsonUtil.getWindowList(resultStr, 1);
    // MyApplication.getInstance().setVenueWindowList(info);
    // }
    // });
    // }

    /**
     * 获取我的个人位置
     */
    // private void getMyLocation() {
    // GaoDeLocationUtil.startLocation(mContext);
    // GaoDeLocationUtil.setLocationCycleType(false);
    // GaoDeLocationUtil.setOnLocationListener(new OnLocationListener() {
    //
    // @Override
    // public void onLocationSuccess(AMapLocation location) {
    // // TODO Auto-generated method stub
    // AppConfigUtil.LocalLocation.Location_latitude = String
    // .valueOf(location.getLatitude());
    // AppConfigUtil.LocalLocation.Location_longitude = String
    // .valueOf(location.getLongitude());
    //
    // }
    //
    // @Override
    // public void onLocationFailure(String error) {
    // // TODO Auto-generated method stub
    // ToastUtil.showToast(error);
    // }
    // });
    // }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            intent = new Intent(SplashActivity.this, MainFragmentActivity.class);
            startActivity(intent);
            finish();
        }
    };

    /**
     * 开始页面
     */
    public void start() {
        // ToastUtil.showToast(HttpUrlList.ipString);
        // startService(new Intent(SplashActivity.this,
        // DownloadAPKService.class));
        handler.postDelayed(run, 1000);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        RepairBugUtil.getInstance().release();
        super.onDestroy();
        handler.removeCallbacks(run);

    }

    /**
     * 获取用户信息(用于强制登录)
     */
    // private void getUserData() {
    // Map<String, String> params = new HashMap<String, String>();
    // params.put(HttpUrlList.HTTP_USER_ID,
    // MyApplication.loginUserInfor.getUserId());
    // MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.GET_USERINFO,
    // params, new HttpRequestCallback() {
    // @Override
    // public void onPostExecute(int statusCode, String resultStr) {
    // // TODO Auto-generated method stub
    // UserInfor mUserInfor = JsonUtil
    // .getUserInforFromString(resultStr);
    // intent = new Intent(SplashActivity.this,
    // SelectThemeActivity.class);
    // if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
    // if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
    // MyApplication.loginUserInfor = mUserInfor;
    // saveUserInfo(mUserInfor);
    // if (mUserInfor.getUserIsLogin() != null) {
    // if (mUserInfor.getUserIsLogin().equals("1")) {
    // intent = new Intent(
    // SplashActivity.this,
    // MainFragmentActivity.class);
    // }
    // }
    // } else {
    // ToastUtil.showToast("用户信息认证失败");
    // }
    // } else {
    // ToastUtil.showToast("网络连接异常");
    //
    // }
    // startActivity(intent);
    // finish();
    // }
    // });
    // }

    /**
     * 保存用户信息
     *
     * @param userinfo
     */
    // private void saveUserInfo(UserInfor userinfo) {
    // if (userinfo == null) {
    // return;
    // }
    // String str = JsonHelperUtil.toJSON(userinfo).toString();
    // SharedPreManager.saveUserInfor(str);
    // }
    @Override
    protected void onResume() {
        super.onResume();
        // getData();

        initShareSDK();
        // start();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(mContext);

    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(mContext);

    }

    /**
     * @param :请求接口
     * @param httpArg :参数
     * @return 返回结果
     */
    // public static String request(String httpUrl, String httpArg) {
    // BufferedReader reader = null;
    // String result = null;
    // StringBuffer sbf = new StringBuffer();
    // httpUrl = httpUrl + "?" + httpArg;
    // try {
    // URL url = new URL(httpUrl);
    // HttpURLConnection connection = (HttpURLConnection) url
    // .openConnection();
    // connection.setRequestMethod("GET");
    // // 填入apikey到HTTP header
    // connection.setRequestProperty("apikey",
    // "1edc8bf286f25eeea7e3db3fb5dcfb19");
    // connection.connect();
    // InputStream is = connection.getInputStream();
    // reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    // String strRead = null;
    // while ((strRead = reader.readLine()) != null) {
    // sbf.append(strRead);
    // sbf.append("\r\n");
    // }
    // reader.close();
    // result = sbf.toString();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return result;
    // }
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

    private void getSplashVersion() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileType", "2");
        params.put("versionNo", "v" + MyApplication.getVersionName(mContext));

        MyHttpRequest.onHttpPostParams(
                HttpUrlList.Banner.WFF_CHECKAPPVERSIONNO, params,
                new HttpRequestCallback() {

                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        Log.d(TAG, statusCode + "这个是第二次来到应用 这个是版本" + resultStr);

                        // TODO Auto-generated method stub
                        if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
                            /**
                             * 暂时取消这个自动更新的程序
                             */
                            try {
                                JSONObject jo = new JSONObject(resultStr);
                                if (jo.optInt("status") == 200) {
                                    // // 这里是不要更新的
                                    handler2.sendEmptyMessage(3);
                                } else {
                                    update_url = jo.getString("data1");
                                    Log.i(TAG, "  看看下载  地址===  " + update_url);
                                    handler2.sendEmptyMessage(2);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
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
                    new Thread(new Runnable() {

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
                    }).start();
                    break;
                // 更新APP
                case 2:
                    /**
                     * 更新APP 现在还没有用
                     */
                    doNewVersionUpdate();
                    // getSplashImage();
                    // 这不是我写的
                    // startService(new Intent(SplashActivity.this,
                    // DownloadAPKService.class));
                    break;
                case 3:
                    getSplashImage();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 更新版本
     */
    public void doNewVersionUpdate() {
        int verCode = MyApplication.getVersionCode_wff(mContext);
        String verName = MyApplication.getVersionName(mContext);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本：");
        sb.append(verName);
        sb.append("\n现有新版本：是否更新");
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        pd = new ProgressDialog(SplashActivity.this);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setTitle("正在下载");
                        pd.setMessage("请稍后。。。");
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        Log.i(TAG, "更新的 地址==  " + update_url);
                        downFile("");
                    }
                })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                getSplashImage();
                                // finish();
                            }
                        }).create();
        // 显示更新框
        dialog.show();
    }

    /**
     * 下载apk
     */
    public void downFile(final String url) {
        pd.show();
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                // 这里暂时写死了更新的
                String downloadUrl =
                        // "http://www.wenhuayun.cn/appdownload/index.html";
                        // "http://apk.hiapk.com/appdown/com.sun3d
                        // .culturalShanghai?em=5&p=android&f_name=文化云";
                        "http://www.wenhuayun.cn/app/andriod/CulturalSH.apk";

                HttpGet get = new HttpGet(downloadUrl);

                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    Log.i(TAG, "apk  length== " + length + "  url==  " + url);
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        isFolderExists(Environment
                                .getExternalStorageDirectory()
                                + UPDATE_SERVERAPK);
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                UPDATE_SERVERAPK);

                        fileOutputStream = new FileOutputStream(file);
                        byte[] b = new byte[64];
                        int charb = -1;
                        int count = 0;
                        while ((charb = is.read(b)) != -1) {
                            fileOutputStream.write(b, 0, charb);
                            count += charb;
                        }
                    }

                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            pd.cancel();
            update();
        }
    };

    /**
     * 下载完成，通过handler将下载对话框取消
     */
    public void down() {
        new Thread() {
            public void run() {
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        }.start();
    }

    /**
     * 安装应用
     */
    public void update() {
        // Intent intent = new Intent();
        // intent.setAction("android.intent.action.VIEW");
        // Uri content_url = Uri.parse(update_url);
        // intent.setData(content_url);
        // startActivity(intent);
        Uri uri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), UPDATE_SERVERAPK));
        Log.i(TAG, "update  ==  " + uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), UPDATE_SERVERAPK)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

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


}
