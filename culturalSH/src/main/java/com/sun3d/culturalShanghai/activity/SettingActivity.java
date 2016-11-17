package com.sun3d.culturalShanghai.activity;

import java.io.File;
import java.math.BigDecimal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.VersionUpdate;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity implements OnClickListener {
	private Context mContext;
	private RelativeLayout mTitle;
	private TextView mCache;
	private File user_cache_path;
	private File image_cache_path = new File(FolderUtil.createImageCacheFile());
	private File cache_path = new File(FolderUtil.createCacheFile());
	private LoadingDialog mLoadingDialog;
	private int mTime = 1000;
	private CheckBox SpeakorVioce;
	private AudioManager audioManager;
	private final String mPageName = "SettingActivity";

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		initView();
		initData();
	}

	private void initView() {
		mLoadingDialog = new LoadingDialog(mContext);
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		SpeakorVioce = (CheckBox) findViewById(R.id.speakvioce);
		findViewById(R.id.setting_updter_layout).setOnClickListener(this);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setVisibility(View.VISIBLE);
		title.setText(mContext.getResources().getString(R.string.setting_title));
		findViewById(R.id.setting_exit).setOnClickListener(this);
		findViewById(R.id.setting_comment).setOnClickListener(this);
		findViewById(R.id.setting_clean).setOnClickListener(this);
		findViewById(R.id.setting_product).setOnClickListener(this);
		mCache = (TextView) findViewById(R.id.cache_size);
		SpeakorVioce.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				setSpeakorVioce(!arg1);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		user_cache_path = StorageUtils.getOwnCacheDirectory(mContext, FolderUtil.createUserFile());

		try {
			setCacheSize(getTotalCacheSize(mContext));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取缓存大小
	 */
	public String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		cacheSize += getFolderSize(user_cache_path);
		cacheSize += getFolderSize(image_cache_path);
		cacheSize += getFolderSize(cache_path);
		return getFormatSize(cacheSize);
	}

	/**
	 * 清理缓存
	 * 
	 * @param context
	 */
	@SuppressWarnings("static-access")
	private void clearAllCache(final Context context) {
		mLoadingDialog.startDialog("清理中");
		try {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					deleteDir(context.getCacheDir());
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						deleteDir(context.getExternalCacheDir());
					}
					deleteDir(user_cache_path);
					deleteDir(image_cache_path);
					deleteDir(cache_path);
					cleanInternalCache();
					try {
						setCacheSize(getTotalCacheSize(mContext));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					mLoadingDialog.cancelDialog();
				}
			}, mTime);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param dir
	 * @return
	 */
	private boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 设置缓存大小
	 * 
	 * @param size
	 */
	private void setCacheSize(String size) {
		mCache.setText(size);
	}

	/**
	 * 退出帐号
	 */
	private void userExit() {
		SharedPreManager.clearUserInfo();
		Intent intent = new Intent(mContext, SplashActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 恩赐好评
	 */
	private void onComment() {
		try {
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 清除缓存
	 * 
	 * @param context
	 */
	private void cleanInternalCache() {
		deleteFilesByDirectory(user_cache_path);
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 */
	private void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * 使用听筒或者扬声器模式
	 * 
	 * @param on
	 */
	private void setSpeakorVioce(boolean on) {
		if (on) {// 开启扬声器
			audioManager.setSpeakerphoneOn(true);
			// ToastUtil.showToast("使用扬声器模式");
		} else {// 关闭扬声器
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE,
					AudioManager.ROUTE_ALL);
			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
			// 把声音设定成Earpiece（听筒）出来，设定为正在通话中
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			// ToastUtil.showToast("使用听筒模式");
		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param file
	 * @return
	 */

	public static long getFolderSize(File file) {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.setting_exit:
			userExit();
			break;
		case R.id.setting_comment:
			onComment();
			break;
		case R.id.setting_clean:
			Intent intenttext1 = new Intent(mContext, ThisWeekActivity.class);
			startActivity(intenttext1);
			// clearAllCache(mContext);
			break;

		case R.id.setting_updter_layout:
			VersionUpdate.onVersionUpdate(mContext, true);
			break;
		case R.id.setting_product:
//			Intent intenttext = new Intent(mContext, ExplainTextActivity.class);
//			intenttext.putExtra("type", ExplainTextActivity.product_explain);
//			startActivity(intenttext);
			break;
		default:
			break;
		}
	}
}
