package com.sun3d.culturalShanghai.activity;

import com.mob.tools.gui.BitmapProcessor.ImageReq;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zhoutanping 
 * 
 * 初始页面的轮播图(3.3版本做的页面)
 * */
public class HomeGearActivity extends Activity {

	private Context mCotnext;
	private ViewPager mViewPager;
	private HomeGrearAdapter mHomeGrear;
	private int mHomeList[] = { R.drawable.sh_icon_home1,
			R.drawable.sh_icon_home2, R.drawable.sh_icon_home3,
			R.drawable.sh_icon_home4, R.drawable.sh_icon_home5};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_gear);
		mCotnext = this;
		MyApplication.getInstance().addActivitys(this);

		initView();

	}

	private void initView() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		mHomeGrear = new HomeGrearAdapter();
		mViewPager.setAdapter(mHomeGrear);

	}

	class HomeGrearAdapter extends PagerAdapter {

		public HomeGrearAdapter() {

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mHomeList.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View mView = null;
			mView = container.inflate(mCotnext, R.layout.item_home_gear, null);
			((ImageView) mView.findViewById(R.id.home_img)).setBackgroundResource(mHomeList[position]);
			TextView mText = (TextView) mView.findViewById(R.id.home_text);
			if(position == 4)
			mText.setVisibility(View.VISIBLE);
			mText.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mCotnext, MainFragmentActivity.class);
					startActivity(intent);
				}
			});;
			
			container.addView(mView);
			
			return mView;
		}
	}

}
