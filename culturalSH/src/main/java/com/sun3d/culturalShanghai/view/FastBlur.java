package com.sun3d.culturalShanghai.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.GetPhoneInfoUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;

/**
 * 图片进行高斯模糊处理
 * 
 * @author yangyoutao
 * 
 */
public class FastBlur {
	public static Bitmap FastBlurBitmap;
	private static String TAG = "FastBlur";

	/**
	 * 添加图片
	 * 
	 * @param mContext
	 */
	private static void addBgPictrue(Context mContext) {
		FastBlurBitmap = FastBlur.fastBlurPic(FastBlur.getScreen((Activity) mContext));
	}

	/**
	 * 获取当前屏幕截屏，不包括标题栏
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap getScreen(Activity activity) {
		if (FastBlurBitmap != null && !FastBlurBitmap.isRecycled()) {
			FastBlurBitmap.recycle();
			FastBlurBitmap = null;
		}
		// 获取windows中最顶层的view
		View view = activity.getWindow().getDecorView();
		view.buildDrawingCache();
		// 获取状态栏高度
		Rect rect = new Rect();
		view.getWindowVisibleDisplayFrame(rect);
		int statusBarHeights = rect.top;
		// 允许当前窗口保存缓存信息
		view.setDrawingCacheEnabled(true);
		// 去掉状态栏
		// 获取屏幕宽和高
		if (view != null && view.getDrawingCache() != null) {
			try {
				if (GetPhoneInfoUtil.getManufacturers().equals("Meizu")) {
					FastBlurBitmap = Bitmap.createBitmap(
							view.getDrawingCache(),
							0,
							statusBarHeights,
							MyApplication.getWindowWidth(),
							MyApplication.getWindowHeight() - statusBarHeights
									- WindowsUtil.getDownMenuHeight(activity));
				} else {
					FastBlurBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0,
							statusBarHeights, MyApplication.getWindowWidth(),
							MyApplication.getWindowHeight() - statusBarHeights);
				}
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
		}
		// 销毁缓存信息
		view.destroyDrawingCache();

		return FastBlurBitmap;
	}

	/**
	 * 为linearlayout添加模糊背景
	 * 
	 * @param mContext
	 * @param linelayout
	 */

	public static void setLinearLayoutBG(Context mContext, LinearLayout linelayout) {
		if (linelayout == null) {
			return;
		}
		new MyTask(mContext, linelayout).execute();
	}

	/**
	 * 为Imageview添加模糊背景
	 * 
	 * @param mContext
	 * @param mImageView
	 */
	public static void setImageViewBG(Context mContext, ImageView mImageView) {
		if (mImageView == null) {
			return;
		}
		new MyTask(mContext, mImageView).execute();
	}

	/**
	 * 背景添加异步线程
	 * 
	 * @author yangyoutao
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	static class MyTask extends AsyncTask<String, Integer, Bitmap> {
		private LinearLayout mlinelayout;
		private ImageView mImageView;
		private Context mContext;

		public MyTask(Context mContext, LinearLayout linelayout) {
			this.mlinelayout = linelayout;
			this.mImageView = null;
			this.mContext = mContext;
		}

		public MyTask(Context mContext, ImageView mimageView) {
			this.mlinelayout = null;
			this.mImageView = mimageView;
			this.mContext = mContext;
		}

		@Override
		protected Bitmap doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return fastBlurPic(FastBlurBitmap);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Bitmap result) {
			if (null != this.mlinelayout && result != null) {
				if (Build.VERSION.SDK_INT < 16) {
					this.mlinelayout.setBackgroundDrawable(new BitmapDrawable(mContext
							.getResources(), result));
				} else {
					this.mlinelayout.setBackground(new BitmapDrawable(mContext.getResources(),
							result));
				}
			} else if (null != this.mImageView && result != null) {
				this.mImageView
						.setImageDrawable(new BitmapDrawable(mContext.getResources(), result));
			}

		}
	}

	/**
	 * 进行高斯模糊处理
	 * 
	 * @param bkg
	 * @return
	 */
	public static Bitmap fastBlurPic(Bitmap bkg) {
		if (null == bkg) {
			return null;
		}
		float scaleFactor = 12;
		float radius = 2;

		Bitmap overlay = Bitmap.createBitmap((int) (MyApplication.getWindowWidth() / scaleFactor),
				(int) (MyApplication.getWindowHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);
		overlay = doBlur(overlay, (int) radius, true);
		return overlay;

	}

	/**
	 * 高斯模糊处理算法
	 * 
	 * @param sentBitmap
	 * @param radius
	 * @param canReuseInBitmap
	 * @return
	 */
	private static Bitmap doBlur(Bitmap bitmap, int radius, boolean canReuseInBitmap) {
		// Bitmap bitmap;
		// if (canReuseInBitmap) {
		// bitmap = sentBitmap;
		// } else {
		// bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		// }

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		bitmap.setPixels(pix, 0, w, 0, 0, w, h);

		return (bitmap);
	}

	class FastBlurTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

		}
	}
}
