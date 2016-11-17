package com.sun3d.culturalShanghai.Util;

import android.graphics.RectF;
import android.widget.ImageView;

/**
 * 图片全部显示位置
 * 
 * @author yangyoutao
 * 
 */
public class PhotoInfoUtil {
	// 内部图片在整个窗口的位置
	public RectF mRect = new RectF();
	public RectF mLocalRect = new RectF();
	public RectF mImgRect = new RectF();
	public RectF mWidgetRect = new RectF();
	public float mScale;
	public ImageView.ScaleType mScaleType;

	public PhotoInfoUtil(RectF rect, RectF local, RectF img, RectF widget, float scale,
			ImageView.ScaleType scaleType) {
		this.mRect.set(rect);
		this.mLocalRect.set(local);
		this.mImgRect.set(img);
		this.mWidgetRect.set(widget);
		this.mScale = scale;
		this.mScaleType = scaleType;
	}
}
