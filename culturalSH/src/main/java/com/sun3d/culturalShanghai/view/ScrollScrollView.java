package com.sun3d.culturalShanghai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.widget.ScrollView;
import android.widget.Scroller;

public class ScrollScrollView extends ScrollView {
	private String TAG = "ScrollScrollView";
	private static final int ANIMATION_SCREEN_SET_DURATION_MILLIS = 500;
	// What fraction (1/x) of the screen the user must swipe to indicate a page
	// change
	private static final int FRACTION_OF_SCREEN_WIDTH_FOR_SWIPE = 4;
	private static final int INVALID_SCREEN = -1;
	/*
	 * Velocity of a swipe (in density-independent pixels per second) to force a
	 * swipe to the next/previous screen. Adjusted into
	 * mDensityAdjustedSnapVelocity on init.
	 */
	private static final int SNAP_VELOCITY_DIP_PER_SECOND = 600;
	// Argument to getVelocity for units to give pixels per second (1 = pixels
	// per millisecond).
	private static final int VELOCITY_UNIT_PIXELS_PER_SECOND = 1000;

	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_HORIZONTAL_SCROLLING = 1;
	private static final int TOUCH_STATE_VERTICAL_SCROLLING = -1;
	private int mCurrentScreen;
	private int mDensityAdjustedSnapVelocity;
	private boolean mFirstLayout = true;
	private int mLastMotionX;
	private int mLastMotionY;
	// private OnScreenSwitchListener mOnScreenSwitchListener;
	private int mMaximumVelocity;
	private int mNextScreen = INVALID_SCREEN;
	private Scroller mScroller;
	private int mTouchSlop;
	private int mTouchState = TOUCH_STATE_REST;
	private VelocityTracker mVelocityTracker;
	private int mLastSeenLayoutWidth = -1;

	public ScrollScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ScrollScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		// TODO Auto-generated method stub
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (monScrolledListenter != null) {
			this.monScrolledListenter.onScrolled(scrollX, scrollY, clampedX,
					clampedY);
		}

	}

	private OnScrolledListenter monScrolledListenter;

	public void setOnScrolledListenter(OnScrolledListenter onScrolledListenter) {
		this.monScrolledListenter = onScrolledListenter;
	}

	public interface OnScrolledListenter {
		public void onScrolled(int scrollX, int scrollY, boolean clampedX,
				boolean clampedY);

	}
}
