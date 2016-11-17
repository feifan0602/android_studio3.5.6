package com.sun3d.culturalShanghai.windows;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;

public class ImageViewSaveWindows {
	public final static int click_QQ = 1;
	public final static int click_weixin = 2;
	public final static int click_savephone = 3;

	/**
	 * 保存图片的弹出框
	 * 
	 * @param context
	 * @param Mainview
	 * @param text
	 */
	public static void showLoadingText(Context context, View Mainview) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layoutFaceType = layoutInflater.inflate(R.layout.saveimg_layout, null);
		final PopupWindow LoadingTextpop = new PopupWindow(layoutFaceType, Mainview.getWidth(),
				Mainview.getHeight());
		layoutFaceType.findViewById(R.id.save_qq).setOnClickListener(new OnClickListener() {
			// 分享给QQ好友
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mSaveImgListener != null) {
					mSaveImgListener.onclick(click_QQ);
				}
				if (LoadingTextpop.isShowing()) {
					LoadingTextpop.dismiss();
				}
			}
		});
		layoutFaceType.findViewById(R.id.save_wexin).setOnClickListener(new OnClickListener() {
			// 分享给微信好友
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mSaveImgListener != null) {
					mSaveImgListener.onclick(click_weixin);
				}
				if (LoadingTextpop.isShowing()) {
					LoadingTextpop.dismiss();
				}
			}
		});
		layoutFaceType.findViewById(R.id.save_phone).setOnClickListener(new OnClickListener() {
			// 分享给保存到手机
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mSaveImgListener != null) {
					mSaveImgListener.onclick(click_savephone);
				}
				if (LoadingTextpop.isShowing()) {
					LoadingTextpop.dismiss();
				}
			}
		});
		layoutFaceType.findViewById(R.id.save_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (LoadingTextpop.isShowing()) {
					LoadingTextpop.dismiss();
				}
			}
		});

		LoadingTextpop.setAnimationStyle(R.style.Saveimg_textAnimation);
		// 设置SelectPicPopupWindow弹出窗体可点击
		LoadingTextpop.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		LoadingTextpop.setBackgroundDrawable(dw);
		LoadingTextpop.update();
		LoadingTextpop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		LoadingTextpop.showAtLocation(Mainview, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	private static SaveImgListener mSaveImgListener;

	public static void setSaveImgListener(SaveImgListener saveImgListener) {
		mSaveImgListener = saveImgListener;
	}

	public static interface SaveImgListener {
		public void onclick(int type);
	}
}
