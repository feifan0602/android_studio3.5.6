package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;

import com.sun3d.culturalShanghai.activity.MainFragmentActivity;
import com.sun3d.culturalShanghai.activity.SplashActivity;
import com.sun3d.culturalShanghai.service.DownloadAPKService;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author yangyu ����������ViewPager����������������ݺ�view
 */
public class ViewPagerAdapter extends PagerAdapter {

	// �����б�
	private ArrayList<View> views;
	private SplashActivity mContext;

	public ViewPagerAdapter(ArrayList<View> views, SplashActivity mContext) {
		this.views = views;
		this.mContext = mContext;
	}

	/**
	 * ��õ�ǰ������
	 */
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}

	/**
	 * ��ʼ��positionλ�õĽ���
	 */
	@Override
	public Object instantiateItem(View view, int position) {

		((ViewPager) view).addView(views.get(position), 0);
		Log.i("ceshi", "position==" + position);
		if (position == views.size() - 1) {
			mContext.Cancle_tv.setVisibility(View.VISIBLE);
			mContext.mRoundProgressBar1.setVisibility(View.VISIBLE);
		} else {
			mContext.Cancle_tv.setVisibility(View.VISIBLE);
			mContext.mRoundProgressBar1.setVisibility(View.VISIBLE);
		}
		if (position == views.size() - 1) {

			views.get(position).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mContext.start();
				}
			});
		}
		return views.get(position);
	}

	/**
	 * �ж��Ƿ��ɶ�����ɽ���
	 */
	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return (view == arg1);
	}

	/**
	 * ���positionλ�õĽ���
	 */
	@Override
	public void destroyItem(View view, int position, Object arg2) {
		((ViewPager) view).removeView(views.get(position));
	}
}
