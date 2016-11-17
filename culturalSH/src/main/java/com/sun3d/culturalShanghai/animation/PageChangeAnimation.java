package com.sun3d.culturalShanghai.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;

public class PageChangeAnimation {
	private LayoutTransition mTransitioner;
	private ContainerAnimation mMyAlphaAnimation;
	public int animaTime = 200;

	/**
	 * 容器内部显示（淡入，下滑出去），隐藏（淡出，上滑出去），动画
	 * 
	 * @param container父容器
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void alphaTranslationXAnimation(LinearLayout container) {
		mTransitioner = new LayoutTransition();
		mTransitioner.setDuration(animaTime);
		container.setLayoutTransition(mTransitioner);
		// Adding定义动画
		PropertyValuesHolder pvhXadd = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
		PropertyValuesHolder pvhYadd = PropertyValuesHolder.ofFloat("translationX", 600, 1f);
		// 动画：APPEARING
		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofPropertyValuesHolder(this, pvhXadd, pvhYadd);
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(1);
			}
		});
		// Removing 定义动画
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationX", 1f, -600);
		// 动画：DISAPPEARING
		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY);
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);

		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(0);
			}
		});

	}

	public void setanimo(LinearLayout container) {
		AnimationSet set = new AnimationSet(true);

		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(50);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(100);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
		container.setLayoutAnimation(controller);
	}

	public void setanimoinorout(LinearLayout inlayout, View outlayout) {
		Animation slide_in = AnimationUtils.loadAnimation(MyApplication.getContext(),
				R.anim.layout_adimin);
		slide_in.setDuration(animaTime);
		inlayout.startAnimation(slide_in);
		if (outlayout != null) {
			Animation slide_out = AnimationUtils.loadAnimation(MyApplication.getContext(),
					R.anim.layout_adimout);
			slide_out.setDuration(animaTime);
			outlayout.startAnimation(slide_out);
		}

	}

	public void setanimoBack(View inlayout, View outlayout) {
		if (inlayout != null) {
			Animation slide_in = AnimationUtils.loadAnimation(MyApplication.getContext(),
					R.anim.layout_adimin_back);
			slide_in.setDuration(animaTime);
			inlayout.startAnimation(slide_in);
		}

		if (outlayout != null) {
			Animation slide_out = AnimationUtils.loadAnimation(MyApplication.getContext(),
					R.anim.layout_adimout_back);
			slide_out.setDuration(animaTime);
			outlayout.startAnimation(slide_out);
		}

	}
}
