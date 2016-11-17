package com.sun3d.culturalShanghai.listener;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.sun3d.culturalShanghai.animation.ContainerAnimation;
import com.sun3d.culturalShanghai.animation.ContainerAnimation.AnimationLayoutListener;
import com.sun3d.culturalShanghai.view.FocusChangeLinearLayout;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ContainerAnimationListener implements OnTouchListener, AnimationLayoutListener {
	private String TAG = "ContainerAnimationListener";
	private LinearLayout mtopLayout;
	private int disstance = 20;// 滑动距离
	private int lastY;
	private int defultsite;
	private LinearLayout main_middle_layout;
	private ContainerAnimation mContainerAnimation;
	private Boolean isShow = true;
	private FocusChangeLinearLayout mdownayout;

	public ContainerAnimationListener(LinearLayout parentlayout, LinearLayout mtopLayout, LinearLayout main_middle_layout, FocusChangeLinearLayout mdownayout) {
		mContainerAnimation = new ContainerAnimation();
		mContainerAnimation.alphaTranslationYAnimation(parentlayout);
		mContainerAnimation.alphaTranslationYAnimation(mdownayout);
		this.mtopLayout = mtopLayout;
		mtopLayout.measure(0, 0);
		this.defultsite = (int) mtopLayout.getMeasuredHeight() + 420;
		this.main_middle_layout = main_middle_layout;
		this.mdownayout = mdownayout;
		this.mdownayout.setIsTonuchTransfer(true);
		this.mdownayout.setOnTouchListener(this);

		mContainerAnimation.setAnimationLayoutListenerAdapter(this);

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG, "defultsite:" + defultsite);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = (int) event.getRawY();

			break;
		case MotionEvent.ACTION_MOVE:
			int dy = (int) event.getRawY() - lastY;
			LayoutParams lp = (LayoutParams) mtopLayout.getLayoutParams();
			lp.height += dy;
			Log.d(TAG, "dy:" + dy + "---lp.height:" + lp.height + "----di:" + (defultsite - disstance));
			if ((dy < 0 && Math.abs(dy) > disstance)) {
				isShow = true;
				this.mdownayout.setIsTonuchTransfer(false);
				mtopLayout.setVisibility(View.GONE);

			} else if (dy > 0 && Math.abs(dy) > disstance) {
				this.mdownayout.setIsTonuchTransfer(true);
				lp.height = defultsite;
				isShow = false;
				mtopLayout.setVisibility(View.VISIBLE);
				mtopLayout.setLayoutParams(lp);
			} else {
				if (dy < 0 && lp.height < (defultsite - disstance)) {
					this.mdownayout.setIsTonuchTransfer(false);
					isShow = true;
					mtopLayout.setVisibility(View.GONE);

				} else if (dy > 0 && lp.height > (defultsite + disstance)) {
					
					lp.height = defultsite;
					this.mdownayout.setIsTonuchTransfer(true);
					isShow = false;
					mtopLayout.setVisibility(View.VISIBLE);
					mtopLayout.setLayoutParams(lp);
				}
			}
			lastY = (int) event.getRawY();
			break;
		case MotionEvent.ACTION_UP:

			break;
		}
		return true;

	}

	@Override
	public void onAnimationEnd(Animator anim) {
		// TODO Auto-generated method stub
		if (isShow) {
			main_middle_layout.setVisibility(View.VISIBLE);
		} else {
			main_middle_layout.setVisibility(View.GONE);
		}
	}
}
