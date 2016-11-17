package com.sun3d.culturalShanghai.view;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ScaleViewPager extends ViewPager {

	private enum State {
		IDLE, GOING_LEFT, GOING_RIGHT
	}

	private State mState;
	private float mTrans;
	private float mScale;
	/**
	 * 最大的缩小比例
	 */
	private static final float SCALE_MAX = 0.8f;
	private static final float ZOOM_MAX = 0.8f;
	private static final float ROT_MAX = 8.0f;
	private static final String TAG = "MyJazzyViewPager";

	private static final boolean API_11;
	static {
		API_11 = Build.VERSION.SDK_INT >= 11;
	}
	/**
	 * 保存position与对于的View
	 */
	private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();
	/**
	 * 滑动时左边的元素
	 */
	private View mLeft;
	/**
	 * 滑动时右边的元素
	 */
	private View mRight;

	public ScaleViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		// 滑动特别小的距离时，我们认为没有动，可有可无的判断
		float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

		// 获取左边的View
		mLeft = findViewFromObject(position);
		// 获取右边的View
		mRight = findViewFromObject(position + 1);

		// 添加切换动画效果
		animateZoom(mLeft, mRight, effectOffset, true);
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
	}

	public void setObjectForPosition(View view, int position) {
		mChildrenViews.put(position, view);
	}

	/**
	 * 通过过位置获得对应的View
	 * 
	 * @param position
	 * @return
	 */
	public View findViewFromObject(int position) {
		return mChildrenViews.get(position);
	}

	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}

	/**
	 * 相对缩放的位置
	 * */
	private void animateZoom(View left, View right, float positionOffset, boolean in) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				// 左边缩放比例
				mScale = in ? ZOOM_MAX + (1 - ZOOM_MAX) * (1 - positionOffset) : 1 + ZOOM_MAX
						- ZOOM_MAX * (1 - positionOffset);
				// 左边-宽和高的位置
				ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.8f);
				ViewHelper.setPivotY(left, left.getMeasuredHeight() * 0.5f);
				ViewHelper.setScaleX(left, mScale);
				ViewHelper.setScaleY(left, mScale);
			}
			if (right != null) {
				manageLayer(right, true);
				// 右边缩放比例
				mScale = in ? ZOOM_MAX + (1 - ZOOM_MAX) * positionOffset : 1 + ZOOM_MAX - ZOOM_MAX
						* positionOffset;
				// 右边-宽和高的位置
				ViewHelper.setPivotX(right, right.getMeasuredWidth() * 0.2f);
				ViewHelper.setPivotY(right, right.getMeasuredHeight() * 0.5f);
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void manageLayer(View v, boolean enableHardware) {
		if (!API_11)
			return;
		int layerType = enableHardware ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
		if (layerType != v.getLayerType())
			v.setLayerType(layerType, null);
	}
}