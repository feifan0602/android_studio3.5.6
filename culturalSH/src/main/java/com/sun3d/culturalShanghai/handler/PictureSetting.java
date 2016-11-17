package com.sun3d.culturalShanghai.handler;

import java.io.File;
import java.io.IOException;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;

import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.PicImgCompressUtil;

/**
 * 图片设置 调用类
 * 
 * @author yangyoutao
 * 
 */
public class PictureSetting {
	private int crop = 180;

	/**
	 * 拍照
	 * 
	 * @return Intent
	 */
	public Intent getPhone(File sdcardTempFile) {
		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (FolderUtil.isMediaMounted()) {
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
		}
		return intentFromCapture;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param Intent
	 */
	public Intent startPhotoZoom(Uri uri) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", crop);
		intent.putExtra("outputY", crop);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 从相册获取
	 * 
	 * @return Intent
	 */
	public Intent getPhoto() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		return intentFromGallery;
	}

	/**
	 * Try to return the absolute file path from the given Uri
	 * 
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * 获得图片
	 * 
	 * @param data
	 * @param sdcardTempFile
	 * @return
	 */
	private String nowTime;

	public Boolean setImageToView(Intent data, File sdcardTempFilepath) {
		Boolean isHavePic = false;
		if (data == null) {
			return isHavePic;
		}
		Bundle extras = data.getExtras();

		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			try {
				if (photo != null) {
					FolderUtil.saveBitmap(photo, sdcardTempFilepath.getAbsolutePath().toString());
					isHavePic = true;
				} else {
					isHavePic = false;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isHavePic = false;
			}
		} else {
			Log.i("tag", "bundle is null");
			isHavePic = false;
		}
		return isHavePic;
	}

	/**
	 * 相册图片
	 * 
	 * @param sdcardTempFilepath
	 */
	public void setImageBitmap(String uripath, File sdcardTempFilepath) {

		Bitmap photo = PicImgCompressUtil.get480Bitmap(uripath);
		try {
			FolderUtil.saveBitmap(photo, sdcardTempFilepath.getAbsolutePath().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 拍照图片
	 * 
	 * @param sdcardTempFilepath
	 */
	public Boolean setImageCamerBitmap(File sdcardTempFilepath) {
		Bitmap photo = PicImgCompressUtil.get480Bitmap(sdcardTempFilepath.getPath());
		if (photo == null) {
			Log.i("tag", "photo is null");
			return false;
		} else {
			Log.i("tag", "photo not null");

			try {
				FolderUtil.saveBitmap(photo, sdcardTempFilepath.getAbsolutePath().toString());
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
}
