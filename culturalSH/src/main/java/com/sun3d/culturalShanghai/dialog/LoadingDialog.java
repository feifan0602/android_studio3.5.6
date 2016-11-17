package com.sun3d.culturalShanghai.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;

public class LoadingDialog {
	private Dialog mLoadingDialog;
	private Context mContext;
	private TextView showText;
	private String contentstr;
	private int delayMillis = 400;

	public LoadingDialog(Context mContext) {
		this.mContext = mContext;
	}

	public void startDialog(String text) {
		if (null == mContext) {
			return;
		}

		mLoadingDialog = new Dialog(mContext, R.style.Loadingdialog);
		mLoadingDialog.setContentView(R.layout.loading_progress);

		showText = (TextView) mLoadingDialog.findViewById(R.id.loading_text);
		if (text != null && text.trim().length() > 0) {
			contentstr = text;
			myHander.sendEmptyMessage(Next_1);
			showText.setText(text);
		} else {
			showText.setText("");
		}
		mLoadingDialog.show();
	}

	private final static int Next_1 = 1;
	private final static int Next_2 = 2;
	private final static int Next_3 = 3;
	Handler myHander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case Next_1:
				showText.setText(contentstr + ".");
				myHander.sendEmptyMessageDelayed(Next_2, delayMillis);
				break;
			case Next_2:
				showText.setText(contentstr + "..");
				myHander.sendEmptyMessageDelayed(Next_3, delayMillis);
				break;
			case Next_3:
				showText.setText(contentstr + "...");
				myHander.sendEmptyMessageDelayed(Next_1, delayMillis);
				break;

			default:
				break;
			}
		}

	};

	public void cancelDialog() {
		myHander.removeMessages(Next_1);
		myHander.removeMessages(Next_2);
		myHander.removeMessages(Next_3);
		if (this.mLoadingDialog != null && this.mLoadingDialog.isShowing()) {
			this.mLoadingDialog.cancel();
		}

	}

	public Dialog getLoadingDialog() {
		return this.mLoadingDialog;

	}
}
