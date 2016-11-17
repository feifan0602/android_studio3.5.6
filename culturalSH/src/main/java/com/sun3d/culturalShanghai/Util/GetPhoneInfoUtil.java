package com.sun3d.culturalShanghai.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 获取手机信息类
 * <p/>
 * 获取的信息有限，后期需要其它信息
 * 请自己添加，并写上备注
 */
public class GetPhoneInfoUtil {

    public final String info = "no";

    /**
     * 手机品牌
     */
    public static String getModel() {
        Log.i("ztp_model", android.os.Build.MODEL);
        return android.os.Build.MODEL;
    }

    /**
     * 手机厂商
     */
    public static String getManufacturers() {
        Log.i("ztp_MANUFACTURER", android.os.Build.MANUFACTURER);
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 手机版本
     */
    public static String getVersion() {
        Log.i("ztp_VERSION", android.os.Build.VERSION.RELEASE);
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 手机SDK版本
     */
    public static String getSDKVersion() {
        Log.i("ztp_SDK", android.os.Build.VERSION.SDK);
        return android.os.Build.VERSION.SDK;
    }


    /**
     * 手机SDK版本
     */
    public static String getIMEI(Context mContext) {
        String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    /**
     * 手机CPU信息
     * 1-cpu型号  //2-cpu频率
     */
    @SuppressWarnings("unused")
    private static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("text", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }

}
