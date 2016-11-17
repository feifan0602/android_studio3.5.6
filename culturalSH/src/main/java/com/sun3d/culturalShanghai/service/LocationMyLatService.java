package com.sun3d.culturalShanghai.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.GaoDeLocationUtil;
import com.sun3d.culturalShanghai.fragment.HomeFragment;


/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class LocationMyLatService extends Service  {
    private String location_str;
    private boolean isAddressChange = true;
    private boolean isFirstRun = true;
    private boolean isHava = true;
    private String NowAddress = "";
    private String TAG="LocationMyLatService";
    private HomeFragment.location_address mLocationAddress_interface;

    @Override
    public void onCreate() {
        init();
        getLat();
        super.onCreate();
    }

    private void init() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        NowAddress = sharedPreferences.getString("nowAddress", "");

        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
    }


    private void getLat() {
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
                location_str = location.getCity();
                //这里对两次的地点进行对比

                if (isFirstRun) {
                    //第一次进入定位成功
                    if (isHava) {
                        //定位地点有分公司
                        MyApplication.saveNowAddress(LocationMyLatService.this, location_str);
                        mLocationAddress_interface.LocationStatus(1,location_str);
                    } else {
                        //定位地点没有分公司
                        mLocationAddress_interface.LocationStatus(2,location_str);
                    }

                } else {
                    //第二次定位成功
                    if (isAddressChange) {
                        //地址没变
                        mLocationAddress_interface.LocationStatus(3,location_str);

                    } else {
                        mLocationAddress_interface.LocationStatus(4,location_str);
                        //地址变了
                    }

                }


                Log.i(TAG, "onLocationSuccess: " + location.getCity());
            }

            @Override
            public void onLocationFailure(String error) {
                mLocationAddress_interface.LocationStatus(5,location_str);

                Log.i(TAG, "onLocationFailure: " + error);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
