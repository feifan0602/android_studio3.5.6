package com.sun3d.culturalShanghai.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2016/11/18 0018.
 */

public class DownNewApkService extends Service {
    private ProgressDialog pd;
    private String UPDATE_SERVERAPK = "CulturalSH.apk";
    private String update_url;
    private String TAG = "DownNewApkService";
    private MyDownApkBind mybind = new MyDownApkBind();
    ;

    @Override
    public void onCreate() {

        super.onCreate();
    }


    private void getSplashVersion() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileType", "2");
        params.put("versionNo", "v" + MyApplication.getVersionName(this));

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
                                    handler.sendEmptyMessage(1);
                                } else {
                                    update_url = jo.getString("data1");
                                    Log.i(TAG, "  看看下载  地址===  " + update_url);
                                    handler.sendEmptyMessage(2);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DownNewApkService.this.onDestroy();
                    ToastUtil.showToast("没有最新的版本");
                    break;
                // 更新APP
                case 2:
                    /**
                     * 更新APP
                     */
                    doNewVersionUpdate();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    /**
     * 更新版本
     */
    public void doNewVersionUpdate() {
        int verCode = MyApplication.getVersionCode_wff(this);
        String verName = MyApplication.getVersionName(this);
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
                        downFile("http://www.wenhuayun.cn/app/andriod/CulturalSH.apk");
                    }
                })
                .setNegativeButton("暂不更新",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                                DownNewApkService.this.onDestroy();
                            }
                        }).create();
        dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        // 显示更新框
        dialog.show();
    }

    /**
     * 下载apk
     */
    public void downFile(final String url) {
        DownloadApk download = new DownloadApk();
        download.execute(url);
    }

    private boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (file.isFile()&&file.exists()){
            file.delete();
        }else{
            try {
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return true;

    }


    /**
     * 安装应用
     */
    public void update() {
        Uri uri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), UPDATE_SERVERAPK));
        Log.i(TAG, "update  ==  " + uri.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), UPDATE_SERVERAPK)),
                "application/vnd.android.package-archive");
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mybind;
    }

    public class MyDownApkBind extends Binder {
        public void startService() {
            getSplashVersion();
        }
    }

    class DownloadApk extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute: ");
            pd = new ProgressDialog(DownNewApkService.this);
            pd.setCanceledOnTouchOutside(false);
            pd.setTitle("正在下载");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            Log.i(TAG, "更新的 地址==  " + update_url);
            pd.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection httpURLConnection = null;
            int progress_int = 0;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return 404;
                }
                int fileLength = httpURLConnection.getContentLength();
                input = httpURLConnection.getInputStream();
                isFolderExists(Environment
                        .getExternalStorageDirectory()
                        + UPDATE_SERVERAPK);
                File file = new File(
                        Environment.getExternalStorageDirectory(),
                        UPDATE_SERVERAPK);
                output = new FileOutputStream(file);
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    if (fileLength > 0) {
                        progress_int = ((int) (total * 100) / fileLength);
                        Log.i(TAG, "doInBackground: " +progress_int);
                        publishProgress(progress_int);
                    }
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }

            return progress_int;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(TAG, "onProgressUpdate: " );
            pd.setMessage("请稍后。。。" + values[0] + "%");
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            pd.cancel();
            update();
            super.onPostExecute(integer);
        }
    }
}
