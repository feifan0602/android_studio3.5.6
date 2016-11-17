package com.sun3d.culturalShanghai.wff.calender;

import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * <p>
 * 自定义日历GridView
 * </p>
 * @author mengjiankang
 * @date 2013-8-31
 ********************************************************************** 
 * @modified <修改人姓名> @date <日期>
 * @comment
 * 
 ********************************************************************** 
 */

public class CalendarGridView extends GridView implements
		android.widget.AdapterView.OnItemLongClickListener {

	private Context mContext;
	public CalendarGridView(Context context) {
		super(context);
		this.mContext = context;
		initGridView();
	}

	public CalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initGridView();
	}

	public CalendarGridView(Context context,AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initGridView();
	}

	/**
	 * 初始化GirdView
	 * 
	 * @param <参数名称>
	 *            <参数类型> <参数说明>
	 * @return <返回值类型>
	 * @throws <异常>
	 */
	public void initGridView() {
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

}