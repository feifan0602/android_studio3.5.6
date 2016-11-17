package com.sun3d.culturalShanghai.windows;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;

public class LoadingTextShowPopWindow {
	private static PopupWindow LoadingTextpop;
	private static TextView text_TV;
	private static int delayMillis = 2000;

	public static void showLoadingText(Context context, View topview, String text) {
		if (LoadingTextpop == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layoutFaceType = layoutInflater.inflate(R.layout.loading_showtextlayout, null);
			text_TV = (TextView) layoutFaceType.findViewById(R.id.textview_loading);
			LoadingTextpop = new PopupWindow(layoutFaceType, topview.getWidth(),
					DensityUtil.dip2px(context, 35));
		} else {
			if (LoadingTextpop.isShowing()) {
				LoadingTextpop.dismiss();
			}
		}
		if (text_TV != null && text != null & text.length() > 0) {
			text_TV.setText(text);
		} else {
			text_TV.setText("数据刷新完成");
		}
		LoadingTextpop.setAnimationStyle(R.style.Loading_textAnimation);
		LoadingTextpop.update();
		LoadingTextpop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
//		LoadingTextpop.showAsDropDown(topview, 0, 0);
		myHandler.sendEmptyMessageDelayed(DISMISS, delayMillis);
	}

	public static void dismissPop() {
		if (LoadingTextpop != null && LoadingTextpop.isShowing()) {
			LoadingTextpop.dismiss();
		}
	}

	private static final int DISMISS = 1;
	private static Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case DISMISS:
				if (LoadingTextpop != null && LoadingTextpop.isShowing()) {
					LoadingTextpop.dismiss();
				}
				break;

			default:
				break;
			}
		}

	};
}
