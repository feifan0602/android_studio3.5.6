package com.sun3d.culturalShanghai.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;

public class SpacingTextView extends TextView {
	private String content;
	private int width;
	private Paint paint;
	private int xPadding;
	private int yPadding;
	private int textHeight;
	private int xPaddingMin;
	int count;
	// 记录每个字的二维数组
	int[][] position;

	public SpacingTextView(Context context) {
		super(context);
		init();
	}

	public SpacingTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SpacingTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		content = this.getText().toString();
		paint = new Paint();
		paint.setTextSize(this.getTextSize());
		paint.setColor(this.getTextColors().getDefaultColor());
		paint.setAntiAlias(true);
		Paint.FontMetrics fm = paint.getFontMetrics();// 得到系统默认字体属性
		textHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);
		// TODO 设置字间距
		xPadding = dip2px(this.getContext(), 4f);
		// TODO 设置行间距
		yPadding = dip2px(this.getContext(), 5f);
		// TODO 设置比较小的字间距（字母和数字应紧凑）
		xPaddingMin = dip2px(this.getContext(), 2f);
	}

	public void setText(String str) {
		// 获得设备的宽
		width = MyApplication.getWindowWidth() - dip2px(this.getContext(), 20f);
		getPositions(str);
		// 重新画控件，将会调用onDraw方法
		this.invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!TextUtils.isEmpty(content)) {
			for (int i = 0; i < count; i++) {
				canvas.drawText(String.valueOf(content.charAt(i)), position[i][0], position[i][1],
						paint);
			}
		}
	}

	public void getPositions(String content) {
		this.content = content;
		char ch;
		// 输入点的 x的坐标
		int x = 0;
		// 当前行数
		int lineNum = 1;
		count = content.length();
		// 初始化字体位置数组
		position = new int[count][2];
		for (int i = 0; i < count; i++) {
			ch = content.charAt(i);
			String str = String.valueOf(ch);

			// 根据画笔获得每一个字符的显示的rect 就是包围框（获得字符宽度）
			Rect rect = new Rect();
			paint.getTextBounds(str, 0, 1, rect);
			int strwidth = rect.width();

			// TODO 对有些标点做些处理
			if (str.equals("《") || str.equals("（") || str.equals("，") || str.equals("。")) {
				strwidth += xPaddingMin * 2;
			}
			// 当前行的宽度
			float textWith = strwidth;
			// 没画字前预判看是否会出界
			x += textWith;
			// TODO 出界就换行(能满足Android下TextView中文换行需求)
			if (x > width) {
				lineNum++;// 真实的行数加一
				x = 0;
			} else {
				// 回到预判前的位置
				x -= textWith;
			}
			// 记录每一个字的位置
			position[i][0] = x;
			position[i][1] = textHeight * lineNum + yPadding * (lineNum - 1);
			// 判断是否是数字还是字母 （数字和字母应该紧凑点）
			// 每次输入完毕 进入下一个输入位置准备就绪
			if (isNumOrLetters(str)) {
				x += textWith + xPaddingMin;
			} else {
				x += textWith + xPadding;
			}
		}
		// 根据所画的内容设置控件的高度
		this.setHeight((textHeight + yPadding) * lineNum);
	}

	// 工具类：判断是否是字母或者数字
	public boolean isNumOrLetters(String str) {
		String regEx = "^[A-Za-z0-9_]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	// 工具类：在代码中使用dp的方法（因为代码中直接用数字表示的是像素）
	public static int dip2px(Context context, float dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}
}