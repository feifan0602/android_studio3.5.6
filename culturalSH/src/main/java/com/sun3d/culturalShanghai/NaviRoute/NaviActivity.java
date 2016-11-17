package com.sun3d.culturalShanghai.NaviRoute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.BigMapViewActivity;

public class NaviActivity extends Activity implements AMapNaviViewListener {
	// 导航View
	private AMapNaviView mAmapAMapNaviView;
	// 是否为模拟导航
	private boolean mIsEmulatorNavi = true;
	// 记录有哪个页面跳转而来，处理返回键
	private int mCode = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi);
		MyApplication.getInstance().addActivitys(this);
		Bundle bundle = getIntent().getExtras();
		processBundle(bundle);
		init(savedInstanceState);
	}

	private void processBundle(Bundle bundle) {
		if (bundle != null) {
			mIsEmulatorNavi = bundle.getBoolean(Utils.ISEMULATOR, true);
			mCode = bundle.getInt(Utils.ACTIVITYINDEX);
		}
	}

	/**
	 * 初始化
	 * 
	 * @param savedInstanceState
	 */
	private void init(Bundle savedInstanceState) {
		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
		mAmapAMapNaviView.onCreate(savedInstanceState);
		mAmapAMapNaviView.setAMapNaviViewListener(this);
		TTSController.getInstance(this).startSpeaking();
		if (mIsEmulatorNavi) {
			// 设置模拟速度
			AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
			// 开启模拟导航
			AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
		} else {
			// 开启实时导航
			AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navi, menu);
		return true;
	}

	@Override
	public void onLockMap(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviCancel() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(NaviActivity.this, BigMapViewActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		finish();
	}

	/**
	 * 
	 * 返回键监听事件
	 * */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onNaviMapMode(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviSetting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNaviTurnClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNextRoadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScanViewButtonClick() {
		// TODO Auto-generated method stub

	}

	// ------------------------------生命周期方法---------------------------
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mAmapAMapNaviView.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAmapAMapNaviView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mAmapAMapNaviView.onPause();
		AMapNavi.getInstance(this).stopNavi();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAmapAMapNaviView.onDestroy();
		TTSController.getInstance(this).stopSpeaking();

	}

	@Override
	public boolean onNaviBackClick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onNaviViewLoaded() {
		// TODO Auto-generated method stub
		
	}

}
