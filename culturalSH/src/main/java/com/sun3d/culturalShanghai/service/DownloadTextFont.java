package com.sun3d.culturalShanghai.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;

/**
 * Created by Administrator on 2016/11/15 0015.
 */

public class DownloadTextFont extends Service {
    private String TAG = "DownloadTextFont";
    private ThinDownloadManager mDownloadManager;
    private int THREAD_COUNT = 3;

    @Override
    public void onCreate() {
        downTextFont("http://www.wenfeifan.cn/YuanTi.TTF", "/YuanTi.TTF");
        super.onCreate();
    }

    private void downTextFont(String url, final String path_name) {
        Log.i(TAG, "downTextFont: url==  " + url + "  path_name==  " + path_name);
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + path_name);
        if (file.exists()) {
            file.delete();
        }
        Uri downloadUri = Uri.parse(url);
        Uri destinationUri = Uri.parse(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + path_name);
        DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri)
                .setPriority(DownloadRequest.Priority.HIGH)
                .setDownloadListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(int id) {
                        DownloadTextFont.this.onDestroy();
                        Log.e(TAG, "onDownloadComplete");
                    }

                    @Override
                    public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                        //下载失败的时候，标注标记位，等下次重新打开应用的时候重新下载
                        Log.e(TAG, "onDownloadFailed" + errorMessage);
                        DownloadTextFont.this.onDestroy();

                    }

                    @Override
                    public void onProgress(int id, long totalBytes, int progress) {
                        Log.e(TAG, "progress:" + progress);
                    }
                });
        mDownloadManager = new ThinDownloadManager(THREAD_COUNT);
        mDownloadManager.add(downloadRequest);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
