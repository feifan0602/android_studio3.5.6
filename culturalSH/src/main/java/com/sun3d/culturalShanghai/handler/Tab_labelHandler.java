package com.sun3d.culturalShanghai.handler;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sun3d.culturalShanghai.R;

public class Tab_labelHandler {
	private Context mContext;
	private int type;// 1为活动，2为场馆
	private Button nebaryBtn, startBtn, hotBtn;
	private ImageView nebaryImg, startImg, hotImg;

	public Tab_labelHandler(Context mContext, LinearLayout contancalLayout, int type) {
		this.mContext = mContext;
		this.type = type;
		LinearLayout startLayout = (LinearLayout) contancalLayout
				.findViewById(R.id.tab_start_layout);
		nebaryBtn = (Button) contancalLayout.findViewById(R.id.tab_nebaty_btn);
		startBtn = (Button) contancalLayout.findViewById(R.id.tab_start_btn);
		hotBtn = (Button) contancalLayout.findViewById(R.id.tab_hot_btn);
		nebaryImg = (ImageView) contancalLayout.findViewById(R.id.tab_nebaty_img);
		startImg = (ImageView) contancalLayout.findViewById(R.id.tab_start_img);
		hotImg = (ImageView) contancalLayout.findViewById(R.id.tab_hot_img);
		if (type == 2) {
			startLayout.setVisibility(View.GONE);
			nebaryBtn.setText("附近场馆");
			hotBtn.setText("热门场馆");
		}
		setTabHot();

	}

	public void setTabNebaty() {
		nebaryBtn.setTextColor(mContext.getResources().getColor(R.color.red_color));
		nebaryImg.setVisibility(View.VISIBLE);
		startBtn.setTextColor(mContext.getResources().getColor(R.color.loading_text_color));
		hotBtn.setTextColor(mContext.getResources().getColor(R.color.loading_text_color));
		startImg.setVisibility(View.GONE);
		hotImg.setVisibility(View.GONE);
	}

	public void setTabStart() {
		startBtn.setTextColor(mContext.getResources().getColor(R.color.red_color));
		startImg.setVisibility(View.VISIBLE);
		nebaryBtn.setTextColor(mContext.getResources().getColor(R.color.loading_text_color));
		hotBtn.setTextColor(mContext.getResources().getColor(R.color.loading_text_color));
		nebaryImg.setVisibility(View.GONE);
		hotImg.setVisibility(View.GONE);
	}

	public void setTabHot() {
		hotBtn.setTextColor(mContext.getResources().getColor(R.color.red_color));
		hotImg.setVisibility(View.VISIBLE);
		startBtn.setTextColor(mContext.getResources().getColor(R.color.loading_text_color));
		nebaryBtn.setTextColor(mContext.getResources().getColor(R.color.loading_text_color));
		startImg.setVisibility(View.GONE);
		nebaryImg.setVisibility(View.GONE);
	}

}
