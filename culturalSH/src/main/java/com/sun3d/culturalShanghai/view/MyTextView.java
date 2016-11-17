package com.sun3d.culturalShanghai.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;

import java.io.File;

import static com.sun3d.culturalShanghai.MyApplication.TEXTFONTTYPEPATH;
import static com.sun3d.culturalShanghai.MyApplication.changeFontType;
import static com.sun3d.culturalShanghai.MyApplication.typeFace;

public class MyTextView extends TextView {

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		if (typeFace == null) {
			String familyName = "宋体";
			File f = new File(TEXTFONTTYPEPATH);
			if(changeFontType()){
				typeFace = Typeface.createFromFile(f);
			}else{
				typeFace =Typeface.create(familyName,Typeface.BOLD);
			}
		}
		MyApplication.Total_font = typeFace;
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
