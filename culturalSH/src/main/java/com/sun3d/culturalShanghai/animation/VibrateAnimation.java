package com.sun3d.culturalShanghai.animation;

import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class VibrateAnimation {
	/**
	 * 控件抖动动画
	 * 
	 * @param view
	 */
	public static void startVibrateAnimation(View view) {

		final TranslateAnimation anim = new TranslateAnimation(view.getWidth(),
				view.getWidth() + 10, view.getHeight(), view.getHeight());
		// 利用 CycleInterpolator 参数 为float 的数 表示 抖动的次数，而抖动的快慢是由 duration 和
		// CycleInterpolator 的参数的大小 联合确定的
		anim.setInterpolator(new CycleInterpolator(2f));
		anim.setDuration(300);
		view.startAnimation(anim);
	}

}
