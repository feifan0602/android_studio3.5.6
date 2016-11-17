package com.sun3d.culturalShanghai.activity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.handler.PictureSetting;
import com.sun3d.culturalShanghai.image.ChooseAlbumActivity;
import com.sun3d.culturalShanghai.image.ImageLoader;
import com.sun3d.culturalShanghai.image.ImageLoader.Type;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.umeng.analytics.MobclickAgent;

/**
 * 拍照选择照片
 * 
 * @author wenff
 * 
 */
public class CameraChooseActivity extends Activity implements OnClickListener {
	private final String mPageName = "CameraChooseActivity";
	private Context mContext;
	private PictureSetting pictureSetting;
	private File sdcardTempFile;
	private static final int CAMERA_REQUEST_CODE = 2;
	private String selectImgs = "";
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_choose);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		init();
	}

	private void init() {
		selectImgs = this.getIntent().getStringExtra("select_imgs");
		LinearLayout backgraundlayout = (LinearLayout) findViewById(R.id.backgraundlayout);
		FastBlur.setLinearLayoutBG(mContext, backgraundlayout);
		RelativeLayout editiconLayout = (RelativeLayout) findViewById(R.id.img_camera);
		editiconLayout.findViewById(R.id.usericon_takepic).setOnClickListener(
				this);
		editiconLayout.findViewById(R.id.usericon_choosepic)
				.setOnClickListener(this);
		editiconLayout.findViewById(R.id.usericon_cancel).setOnClickListener(
				this);
	}

	/**
	 * 设置拍照
	 */
	private void setIconCamera() {
		if (pictureSetting == null) {
			pictureSetting = new PictureSetting();
		}
		String nowTime = String.valueOf(System.currentTimeMillis());
		sdcardTempFile = new File(FolderUtil.createImageCacheFile(), nowTime
				+ "iamgecathwhy.jpg");
		Log.d("sdcardTempFile1", sdcardTempFile.getAbsolutePath().toString());
		startActivityForResult(pictureSetting.getPhone(sdcardTempFile),
				CAMERA_REQUEST_CODE);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.camera_choose, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_REQUEST_CODE) {
			if (pictureSetting.setImageCamerBitmap(sdcardTempFile)) {
				Log.d("sdcardTempFile2", sdcardTempFile.getAbsolutePath()
						.toString());
				if (ImageLoader.getInstance() == null) {
					ImageLoader.getInstance(3, Type.LIFO);
				}
				ImageLoader.addImageToLruCache(sdcardTempFile.getAbsolutePath()
						.toString());
				if (selectImgs.length() > 1) {
					selectImgs = selectImgs + ","
							+ sdcardTempFile.getAbsolutePath().toString();
				} else {
					selectImgs = sdcardTempFile.getAbsolutePath().toString();
				}
			}
			Intent backintent = new Intent();
			backintent.putExtra("select_imgs", selectImgs);
			setResult(30, backintent);
			finish();
		}
		if (resultCode == 30) {
			selectImgs = data.getStringExtra("select_imgs");
			Intent backintent = new Intent();
			backintent.putExtra("select_imgs", selectImgs);
			setResult(30, backintent);
			finish();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.usericon_takepic:// 拍照
			setIconCamera();
			break;
		case R.id.usericon_choosepic:// 选择相册
			Intent intent = new Intent();
			intent.setClass(mContext, ChooseAlbumActivity.class);
			intent.putExtra("select_imgs", selectImgs);
			startActivityForResult(intent, 20);
			break;
		default:
			finish();
			break;
		}
	}

}
