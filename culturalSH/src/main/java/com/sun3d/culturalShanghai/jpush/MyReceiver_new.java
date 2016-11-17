package com.sun3d.culturalShanghai.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sun3d.culturalShanghai.Util.LocalPreferencesHelper;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver_new extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private LocalPreferencesHelper mLocalPreferencesHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
           /* Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

        	//打开自定义的Activity
        	Intent i = new Intent(context, TestActivity.class);
        	i.putExtras(bundle);
        	//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);*/

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
/*
	private void processCustomMessage(Context context, Bundle bundle) {
		if (mLocalPreferencesHelper == null) {
			mLocalPreferencesHelper = new LocalPreferencesHelper(context, SPConst.SP_NAME);
		}
		String pathInfo = mLocalPreferencesHelper.getString(SPConst.PATH_INFO);
		final PatchBean localBean = GsonUtils.getInstance().parseIfNull(PatchBean.class , pathInfo);
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		if (TextUtils.isEmpty(message)) return;
		try {
			PatchBean bean = GsonUtils.getInstance().parse(PatchBean.class, message);
			if (BaseApplication.VERSION_NAME.equals(bean.app_v)){	//远程的应用版本跟当前应用的版本比较
				if (localBean == null && !TextUtils.isEmpty(bean.path_v)
						|| localBean.app_v.equals(bean.app_v) &&
						! localBean.path_v.equals(bean.path_v)){	//远程的应用版本跟本地保存的应用版本一样，但补丁不一样，则需要下载重新
					RepairBugUtil.getInstance().downloadAndLoad(context, bean, SPConst.URL_PREFIX + bean.url);
					String json = GsonUtils.getInstance().parse(bean);
					mLocalPreferencesHelper.saveOrUpdate(SPConst.PATH_INFO, json);
				}else {
					mLocalPreferencesHelper.saveOrUpdate(SPConst.IsHavePathDownLoad, false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e(TAG,message);
	}
*/


    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Intent msgIntent = new Intent(MainFragmentActivity.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(MainFragmentActivity.KEY_MESSAGE, message);
        context.sendBroadcast(msgIntent);
    }
}
