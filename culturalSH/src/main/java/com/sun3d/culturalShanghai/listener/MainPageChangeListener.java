package com.sun3d.culturalShanghai.listener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class MainPageChangeListener implements OnPageChangeListener {
	
	private ViewPager viewpager;
	private Context context;
	
	public MainPageChangeListener(Context context, ViewPager viewpager){
		this.viewpager = viewpager;
		this.context = context;

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		Log.i("ztp-", arg0 + "");
		if(0 == viewpager.getCurrentItem()){
			Log.i("ztp-zt", viewpager.getAdapter().getCount() + "");
			viewpager.setCurrentItem(viewpager.getAdapter().getCount() - 1);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
	}

}
