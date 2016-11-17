package com.sun3d.culturalShanghai.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class FocusChangeLinearLayout extends LinearLayout {
	private Boolean isTonuchTransfer = false;

	public FocusChangeLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FocusChangeLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public FocusChangeLinearLayout(Context context, AttributeSet attrs, int systle) {
		super(context, attrs, systle);
		// TODO Auto-generated constructor stub
	}

	public Boolean getIsTonuchTransfer() {
		return isTonuchTransfer;
	}

	public void setIsTonuchTransfer(Boolean isTonuchTransfer) {
		this.isTonuchTransfer = isTonuchTransfer;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return isTonuchTransfer;
	}



}
