package com.sun3d.culturalShanghai.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载服务类，用于下载APK，并用通知栏通知
 * 
 * @author yangyoutao
 * 
 */
public class DownloadAPKService extends Service {

	private static NotificationManager nm;
	private static Notification notification;
	private static boolean cancelUpdate = false;
	private static MyHandler myHandler;
	private static ExecutorService executorService = Executors
			.newFixedThreadPool(5);
	public static Map<Integer, Integer> download = new HashMap<Integer, Integer>();
	public static Context context;
	public static DownloadAPKService mDownloadAPKService;
	/**
	 * APK保存本地的文件夹名字
	 */
	private static String LocatAPKFilePath = "wenhuayun";
	/**
	 * 保存在本地APK的名字
	 */
	private static String LocatAPK = "wenhuayun" + ".apk";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		myHandler = new MyHandler(Looper.myLooper(), DownloadAPKService.this);
		mDownloadAPKService = this;
		context = this;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 下载初始化，通知栏设置
	 * 
	 * @param url
	 * @param notificationId
	 * @param name
	 */
	public static void downNewFile(final String url, final int notificationId,
			final String name) {
		if (download.containsKey(notificationId))
			return;
		notification = new Notification();
		notification.icon = R.drawable.sh_icon_logo;
		notification.tickerText = name;
		notification.when = System.currentTimeMillis();
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.icon = R.drawable.sh_icon_logo;
		notification.flags = Notification.FLAG_NO_CLEAR
				| Notification.FLAG_ONGOING_EVENT;
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				notificationId,
				new Intent(context, MainFragmentActivity.class), 0);
		// 通知自定义视图
		notification.contentView = new RemoteViews(
				mDownloadAPKService.getPackageName(),
				R.layout.mynotification_progressbar);
		notification.contentView.setProgressBar(R.id.progressBar1, 100, 0,
				false);
		notification.contentView.setTextViewText(R.id.tv_1, name);
		notification.contentIntent = contentIntent;// 这个pengdingIntent很重要，必须要设置
		download.put(notificationId, 0);

		nm.notify(notificationId, notification);

		downFile(url, notificationId, name);
	}

	/**
	 * 下载文件，并通知通知栏更新下载进度
	 * 
	 * @param url
	 * @param notificationId
	 * @param name
	 */
	private static void downFile(final String url, final int notificationId,
			final String name) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File tempFile = null;
				try {
					HttpClient client = new DefaultHttpClient();

					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					if (is != null) {
						File rootFile = new File(Environment
								.getExternalStorageDirectory(), "/"
								+ LocatAPKFilePath);
						if (!rootFile.exists() && !rootFile.isDirectory())
							rootFile.mkdir();

						tempFile = new File(Environment
								.getExternalStorageDirectory(),
								LocatAPKFilePath + "/" + LocatAPK);
						if (tempFile.exists())
							tempFile.delete();
						tempFile.createNewFile();

						BufferedInputStream bis = new BufferedInputStream(is);

						FileOutputStream fos = new FileOutputStream(tempFile);

						BufferedOutputStream bos = new BufferedOutputStream(fos);

						int read;
						long count = 0;
						int precent = 0;
						byte[] buffer = new byte[1024];
						while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
							bos.write(buffer, 0, read);
							count += read;
							precent = (int) (((double) count / length) * 100);

							if (precent - download.get(notificationId) >= 1) {
								download.put(notificationId, precent);
								Message message = myHandler.obtainMessage(3,
										precent);
								Bundle bundle = new Bundle();
								bundle.putString("name", name);
								message.setData(bundle);
								message.arg1 = notificationId;
								myHandler.sendMessage(message);
							}
						}
						bos.flush();
						bos.close();
						fos.flush();
						fos.close();
						is.close();
						bis.close();
					}

					if (!cancelUpdate) {
						Message message = myHandler.obtainMessage(2, tempFile);
						message.arg1 = notificationId;
						Bundle bundle = new Bundle();
						bundle.putString("name", name);
						message.setData(bundle);
						myHandler.sendMessage(message);
					} else {
						tempFile.delete();
					}
				} catch (ClientProtocolException e) {
					if (tempFile.exists())
						tempFile.delete();
					Message message = myHandler.obtainMessage(4, name
							+ "  下载失败!");
					message.arg1 = notificationId;
					myHandler.sendMessage(message);
				} catch (IOException e) {
					if (tempFile.exists())
						tempFile.delete();
					Message message = myHandler.obtainMessage(4, name
							+ "  下载失败!");
					message.arg1 = notificationId;
					myHandler.sendMessage(message);
				} catch (Exception e) {
					if (tempFile.exists())
						tempFile.delete();
					Message message = myHandler.obtainMessage(4, name
							+ "  下载失败!," + e.getMessage());
					message.arg1 = notificationId;
					myHandler.sendMessage(message);
				}
			}
		});
	}

	/**
	 * 下载完成后 安装APK
	 * 
	 * @param file
	 * @param context
	 */
	private void Instanll(File file, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 通知UI更新的Handler
	 */
	class MyHandler extends Handler {
		private Context context;

		public MyHandler(Looper looper, Context c) {
			super(looper);
			this.context = c;
		}

		@Override
		public void handleMessage(Message msg) {
			PendingIntent contentIntent = null;
			super.handleMessage(msg);
			if (msg != null) {
				switch (msg.what) {
				case 0:
					Toast.makeText(context, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					download.remove(msg.arg1);
					break;
				case 1:

					break;
				case 2:
					contentIntent = PendingIntent.getActivity(
							DownloadAPKService.this, msg.arg1, new Intent(
									DownloadAPKService.this,
									MainFragmentActivity.class), 0);
					// notification.setLatestEventInfo(DownloadAPKService.this,
					// msg.getData().getString("name") + "        下载完成!",
					// "100%", contentIntent);
					notification.defaults = Notification.DEFAULT_SOUND;// 设置铃声
					notification.contentIntent = contentIntent;
					notification.contentView.setTextViewText(R.id.tv_downInfo,
							"        下载完成!");
					notification.contentView.setProgressBar(R.id.progressBar1,
							100, 100, false);
					nm.notify(msg.arg1, notification);
					download.remove(msg.arg1);
					nm.cancel(msg.arg1);
					Instanll((File) msg.obj, context);
					break;
				case 3:
					contentIntent = PendingIntent.getActivity(
							DownloadAPKService.this, msg.arg1, new Intent(
									DownloadAPKService.this,
									MainFragmentActivity.class), 0);
					notification.contentIntent = contentIntent;
					notification.contentView.setTextViewText(R.id.tv_downInfo,
							"下载中" + download.get(msg.arg1) + "%");
					notification.contentView.setProgressBar(R.id.progressBar1,
							100, download.get(msg.arg1), false);
					nm.notify(msg.arg1, notification);
					break;
				case 4:
					Toast.makeText(context, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					contentIntent = PendingIntent.getActivity(
							DownloadAPKService.this, msg.arg1, new Intent(
									DownloadAPKService.this,
									MainFragmentActivity.class), 0);
					notification.contentIntent = contentIntent;
					notification.contentView.setTextViewText(R.id.tv_downInfo,
							"下载失败！" + download.get(msg.arg1) + "%");
					notification.contentView.setProgressBar(R.id.progressBar1,
							100, download.get(msg.arg1), false);
					nm.notify(msg.arg1, notification);
					download.remove(msg.arg1);
					nm.cancel(msg.arg1);
					break;
				}
			}
		}
	}
}
