package com.sun3d.culturalShanghai.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil;
import com.sun3d.culturalShanghai.fragment.HomeFragment;

import org.json.JSONObject;


/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class LocationMyLatService extends Service implements HomeFragment.Location_address {
    private String location_str;
    private boolean isAddressChange = true;
    private boolean isFirstRun = true;
    private boolean isHava = true;
    private String NowAddress = "";
    private String TAG = "LocationMyLatService";
    private MyBind bind = new MyBind();
    private int status_int = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void init() {
        Log.i(TAG, "init: ");
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        NowAddress = sharedPreferences.getString("nowAddress", "");

        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }


    private int getLat() {
        Log.i(TAG, "getLat: ");
        GaoDeLocationUtil mGaoDeLocationUtil = new GaoDeLocationUtil(this);
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
                //这里对两次的地点进行对比
                Log.i(TAG, "onLocationSuccess: " + location.getCity() + "isFirstRun  ==" +
                        isFirstRun + "  isAddressChange  " + isAddressChange + "  isHava  " +
                        isHava + "  code==  " + location.getAdCode());
                //这是第一次进来的时候判断 是否有分公司
                for (int i = 0; i < HomeFragment.sAddressList.size() && HomeFragment.sAddressList
                        .size() != 0; i++) {
                    for (int j = 0; j < HomeFragment.sAddressList.get(i).getCityList().length();
                         j++) {
                        JSONObject jo = HomeFragment.sAddressList.get(i).getCityList()
                                .optJSONObject(j);
                        String city = jo.optString("cityName", "");
                        if (city.equals(location.getCity())) {
                            isHava = false;
                        }
                    }
                }

                location_str = location.getCity();
                HomeFragment.cityCode = location.getAdCode();
                if (isFirstRun) {
                    //第一次进入定位成功
                    if (!isHava) {
                        //定位地点有分公司
                        MyApplication.saveNowAddress(LocationMyLatService.this, location_str);
                        status_int = 1;
//                        mLocationAddress_interface.LocationStatus(1, location_str);
                    } else {
                        //定位地点没有分公司
                        status_int = 2;
//                        mLocationAddress_interface.LocationStatus(2, location_str);
                    }

                } else {
                    //第二次定位成功  这是记录第一次定位的地址
                    String fristAddress = MyApplication.getNowAddress(LocationMyLatService.this);
                    if (fristAddress.equals(location.getCity())) {
                        isAddressChange = true;
                    } else {
                        isAddressChange = false;
                    }
                    if (isAddressChange) {
                        //地址没变
                        status_int = 3;
                        MyApplication.saveNowAddress(LocationMyLatService.this, location_str);
//                        mLocationAddress_interface.LocationStatus(3, location_str);
                    } else {
                        //地址变了
                        status_int = 4;

//                        mLocationAddress_interface.LocationStatus(4, location_str);

                    }

                }
                handler.sendEmptyMessage(1);

            }

            @Override
            public void onLocationFailure(String error) {
                Log.i(TAG, "onLocationFailure: " + error);
                status_int = 5;
                handler.sendEmptyMessage(1);
//                mLocationAddress_interface.LocationStatus(5, location_str);

            }
        });
        return status_int;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    @Override
    public int LocationStatus() {
        int i = status_int;
        Log.i(TAG, "LocationStatus: " + status_int);
        return i;
    }

    @Override
    public String LocationAddress() {
        String address = location_str;
        Log.i(TAG, "LocationAddress: " + address);
        return address;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LocationStatus();
                    LocationAddress();
                    break;
                case 2:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class MyBind extends Binder {
        public void startService() {
            init();
        }

        public LocationMyLatService getService() {
            MyThread run = new MyThread();
            run.execute("");
            return LocationMyLatService.this;
        }
    }

    class MyThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            getLat();
            return null;
        }
    }

}
