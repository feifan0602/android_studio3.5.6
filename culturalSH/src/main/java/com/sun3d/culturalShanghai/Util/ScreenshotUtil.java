package com.sun3d.culturalShanghai.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.view.View;

import com.sun3d.culturalShanghai.callback.CollectCallback;

public class ScreenshotUtil {
	/**
	 * 根据控件保存图片
	 * 
	 * @param view
	 * @param callback
	 */
	public static void getData(View view, String imgUrl, final CollectCallback callback) {
		final Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
		view.draw(new Canvas(bmp));
		String dirString = FolderUtil.createImageCacheFile();
		File dir = new File(dirString);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		final String photoUrl = dirString + imgUrl;// 换成自己的图片保存路径
		final File file = new File(photoUrl);
		new Thread() {
			@Override
			public void run() {
				try {
					boolean bitMapOk = bmp.compress(CompressFormat.PNG, 100, new FileOutputStream(file));
					callback.onPostExecute(bitMapOk);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
}
