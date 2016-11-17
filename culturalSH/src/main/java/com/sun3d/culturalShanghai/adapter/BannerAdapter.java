package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ViewPager适配器
 * 
 * @author Rabbit_Lee
 * 
 */
public class BannerAdapter extends PagerAdapter {

	// 数据源
	private List<ImageView> mList;

	public BannerAdapter(List<ImageView> list) {
		this.mList = list;
	}

	@Override
	public int getCount() {
		// 取超大的数，实现无线循环效果
		if (this.mList.size() > 1) {
			return Integer.MAX_VALUE;
		} else {
			return this.mList.size();
		}

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Log.d("position", "position:" + position % mList.size());
		try {
			if (mList.get(position % mList.size()).getParent() == null) {
				container.addView(mList.get(position % mList.size()));
			} else {
				((ViewGroup) mList.get(position % mList.size()).getParent()).removeView(mList
						.get(position % mList.size()));
				container.addView(mList.get(position % mList.size()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// container.addView(mList.get(position % mList.size()));
		return mList.get(position % mList.size());
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView(mList.get(position % mList.size()));
	}

}