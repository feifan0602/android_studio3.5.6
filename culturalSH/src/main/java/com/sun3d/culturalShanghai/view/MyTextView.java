package com.sun3d.culturalShanghai.view;

import com.sun3d.culturalShanghai.MyApplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		AssetManager assmag = context.getAssets();
	
		if (MyApplication.Total_font == null) {
			MyApplication.Total_font = Typeface.createFromAsset(assmag,
					"fonts/STYuanti-SC-Light.TTF");
		}else {
			
		}
		setTypeface(MyApplication.Total_font);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	public MyTextView(Context context) {
		super(context);
		init(context);
		// TODO Auto-generated constructor stub
	}

}
