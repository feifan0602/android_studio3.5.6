package com.sun3d.culturalShanghai.windows;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.thirdparty.MyShare;
import com.sun3d.culturalShanghai.thirdparty.ShareName;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class SelectPicPopupWindow extends PopupWindow implements
		OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;

	public SelectPicPopupWindow(final Activity context,
			OnClickListener itemsOnClick,String intent) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.dialog_thirdshare_layout, null);
		mMenuView.findViewById(R.id.share_cancel).setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
		});
		// contentLayout.addView(mMenuView);

		final ShareInfo shareInfo = (ShareInfo) context.getIntent()
				.getSerializableExtra(AppConfigUtil.INTENT_SHAREINFO);
		// 微信好友圈
		mMenuView.findViewById(R.id.third_share_weixin_friend)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// ShareToWX.shareShow(mContext, shareInfo, true);
						MyShare.showShare(ShareName.WechatMoments, context,
								shareInfo);
						dismiss();
					}
				});
		// 微信好友
		mMenuView.findViewById(R.id.third_share_weixin).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MyShare.showShare(ShareName.Wechat, context, shareInfo);
						// ShareToWX.shareShow(mContext, shareInfo, false);
						context.finish();
					}
				});
		// QQ好友
		mMenuView.findViewById(R.id.third_share_qq).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MyShare.showShare(ShareName.QQ, context, shareInfo);
						dismiss();
					}
				});
		// 新浪微博
		mMenuView.findViewById(R.id.third_share_sina).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MyShare.showShare(ShareName.SinaWeibo, context,
								shareInfo);
						dismiss();
					}
				});

		// btn_take_photo = (Button)
		// mMenuView.findViewById(R.id.btn_take_photo);
		// btn_pick_photo = (Button)
		// mMenuView.findViewById(R.id.btn_pick_photo);
		// btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		// //ȡ��ť
		// btn_cancel.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// //��ٵ�����
		// dismiss();
		// }
		// });
		// //���ð�ť����
		// btn_pick_photo.setOnClickListener(itemsOnClick);
		// btn_take_photo.setOnClickListener(itemsOnClick);
		// ����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		// ����SelectPicPopupWindow��������Ŀ�
		this.setWidth(LayoutParams.FILL_PARENT);
		// ����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		// ����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.AnimBottom);
		// ʵ��һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// ����SelectPicPopupWindow��������ı���
		this.setBackgroundDrawable(dw);
		// mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ�����������ٵ�����
		// mMenuView.setOnTouchListener(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// int height = mMenuView.findViewById(R.id.pop_layout).getTop();
		// int y=(int) event.getY();
		// if(event.getAction()==MotionEvent.ACTION_UP){
		// if(y<height){
		// dismiss();
		// }
		// }
		// return true;
		// }
		// });

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
