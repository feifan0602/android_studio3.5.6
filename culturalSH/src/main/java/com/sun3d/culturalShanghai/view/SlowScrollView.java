package com.sun3d.culturalShanghai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 
 * @author yangyoutao
 * 
 */
public class SlowScrollView extends ScrollView {
	private String TAG = "SlowScrollView";
	private GestureDetector gestureDete;

	public SlowScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SlowScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDete = new GestureDetector(context, new YScrollDetector());
	}

	public SlowScrollView(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Boolean i1 = super.onInterceptTouchEvent(ev);
		Boolean i2 = gestureDete.onTouchEvent(ev);
		Boolean i3 = i1 && i2;
		Log.d(TAG, "i1:" + i1 + "--i2:" + i2 + "--i3:" + i3);
		return i3;
	}

	class YScrollDetector extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// 如果我们滚动更接近水平方向,返回false,让子视图来处理它
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}

}
