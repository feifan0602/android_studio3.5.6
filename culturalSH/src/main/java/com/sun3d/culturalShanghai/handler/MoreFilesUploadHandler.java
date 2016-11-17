package com.sun3d.culturalShanghai.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.PicImgCompressUtil;
import com.sun3d.culturalShanghai.Util.UploadImgUtil;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.image.ImageLoader;
import com.sun3d.culturalShanghai.image.ImageLoader.Type;

public class MoreFilesUploadHandler {
	private static String imageCacheName = "filesmoresimage.jpg";

	/**
	 * 本地图片处理
	 * 
	 * @param path
	 */
	public static void uploadFile(String path, String modeType) {
		String newpath = FolderUtil.createImageCacheFile() + "/" + imageCacheName;
		if (ImageLoader.getInstance() == null) {
			ImageLoader.getInstance(3, Type.LIFO);

		}
		Bitmap Bit = ImageLoader.getBitmapFromLruCache(path + AppConfigUtil.Local.ImageTag);
		if (Bit == null) {
			Bit = PicImgCompressUtil.get480Bitmap(path);
			ImageLoader.addImageToLruCache(path, AppConfigUtil.Local.ImageTag);
		}
		if (Bit != null) {
			try {
				FolderUtil.saveBitmap(Bit, newpath);
				upload(newpath, modeType);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 新文件上传
	 * 
	 * @param path
	 */
	private static void upload(String path, String modeType) {
		Map<String, String> param = new HashMap<String, String>();
		param.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		param.put("uploadType", "1");// 多文件
		param.put("modelType", modeType);// 2 是个人头像 3是评论与用户反馈
		UploadImgUtil.getInstance().uploadFile(path, AppConfigUtil.UploadImage.KEY, HttpUrlList.File.UPLOADIMG, param);
	}

}
