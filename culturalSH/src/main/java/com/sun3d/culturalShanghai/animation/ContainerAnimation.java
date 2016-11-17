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
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 容器动画类
 * 
 * @author yangyoutao
 * 
 */
@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ContainerAnimation {
	private LayoutTransition mTransitioner;
	private ContainerAnimation mMyAlphaAnimation;
	public int animaTime = 500;

	public ContainerAnimation() {
		mMyAlphaAnimation = this;
	}

	/**
	 * 容器内部显示（淡入，下滑出去），隐藏（淡出，上滑出去），动画
	 * 
	 * @param container父容器
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void alphaTranslationYAnimation(LinearLayout container) {

		mTransitioner = new LayoutTransition();
		mTransitioner.setDuration(animaTime);
		container.setLayoutTransition(mTransitioner);
		// Adding定义动画
		PropertyValuesHolder pvhXadd = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
		PropertyValuesHolder pvhYadd = PropertyValuesHolder.ofFloat("translationY", -200, 1f);
		// 动画：APPEARING
		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofPropertyValuesHolder(this, pvhXadd, pvhYadd);
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(1);
				if (mAnimationLayoutListenerAdapter != null) {
					mAnimationLayoutListenerAdapter.onAnimationEnd(anim);
				}
			}
		});
		// Removing 定义动画
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 1f, -200);
		// 动画：DISAPPEARING
		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofPropertyValuesHolder(mMyAlphaAnimation, pvhX,
				pvhY);
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);

		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(0);
				if (mAnimationLayoutListenerAdapter != null) {
					mAnimationLayoutListenerAdapter.onAnimationEnd(anim);
				}
			}
		});

	}

	/**
	 * 容器内部显示（下滑），隐藏（上滑），动画
	 * 
	 * @param container父容器
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void translationYAnimation(LinearLayout container, int duration) {

		mTransitioner = new LayoutTransition();
		mTransitioner.setDuration(duration);
		container.setLayoutTransition(mTransitioner);
		// Removing 定义动画
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 1f, -1f);
		// 动画：DISAPPEARING
		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofPropertyValuesHolder(mMyAlphaAnimation, pvhX,
				pvhY);
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(0);
				if (mAnimationLayoutListenerAdapter != null) {
					mAnimationLayoutListenerAdapter.onAnimationEnd(anim);
				}
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void alphaTranslationYAnimation(ListView container) {

		mTransitioner = new LayoutTransition();
		mTransitioner.setDuration(animaTime);
		container.setLayoutTransition(mTransitioner);
		// Adding定义动画
		PropertyValuesHolder pvhXadd = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
		PropertyValuesHolder pvhYadd = PropertyValuesHolder.ofFloat("translationY", -200, 1f);
		// 动画：APPEARING
		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofPropertyValuesHolder(this, pvhXadd, pvhYadd);
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(1);
				if (mAnimationLayoutListenerAdapter != null) {
					mAnimationLayoutListenerAdapter.onAnimationEnd(anim);
				}
			}
		});
		// Removing 定义动画
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 1f, -200);
		// 动画：DISAPPEARING
		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofPropertyValuesHolder(mMyAlphaAnimation, pvhX,
				pvhY);
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);

		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setAlpha(0);
				if (mAnimationLayoutListenerAdapter != null) {
					mAnimationLayoutListenerAdapter.onAnimationEnd(anim);
				}
			}
		});

	}

	private AnimationLayoutListener mAnimationLayoutListenerAdapter;

	public void setAnimationLayoutListenerAdapter(
			AnimationLayoutListener mAnimationLayoutListenerAdapter) {
		this.mAnimationLayoutListenerAdapter = mAnimationLayoutListenerAdapter;
	}

	public interface AnimationLayoutListener {
		public void onAnimationEnd(Animator anim);
	}
}
