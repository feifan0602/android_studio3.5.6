package com.sun3d.culturalShanghai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class TopScrollView extends ScrollView {

	public TopScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		// TODO Auto-generated method stub
		
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}

	
}
