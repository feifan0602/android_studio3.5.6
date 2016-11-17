package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.manager.DeviceManager;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.HomeImgInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;

public class HomeDetail_Index implements OnClickListener {
	private LinearLayout content;
	private Context mContext;
	private Activity mActivity;
	private ImageView topLeft_img, topRight_img, buttomFrist_img,
			buttomSecond_img, buttomThree_img, buttomFour_img;
	private HomeImgInfo topLeft_info, topRight_info, buttomFrist_info,
			buttomSecond_info, buttomThree_info, buttomFour_info;
	private List<HomeImgInfo> mList;
	private String TAG = "HomeDetail_Index";

	public HomeDetail_Index(Context context, Activity activity) {
		super();
		this.mContext = context;
		this.mActivity = activity;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.homedetail_index, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(DeviceManager.getResolutionWidth() * 0.6));
		content.setLayoutParams(params);
		initView();
	}

	public void setData(List<HomeImgInfo> mImgList) {
		this.mList = mImgList;
		for (int i = 0; i < mList.size(); i++) {
			Log.i(TAG, "看看数据图片==  " + mList.get(i).getAdvertSort()
					+ "  url ==  " + mList.get(i).getAdvertImgUrl());
			if (mList.get(i).getAdvertType().equals("A")) {
				switch (mList.get(i).getAdvertSort()) {
				case 2:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_750_440(mList.get(i)
											.getAdvertImgUrl()), topLeft_img);
					topLeft_info = mList.get(i);
					break;
				case 3:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_750_440(mList.get(i)
											.getAdvertImgUrl()), topRight_img);
					topRight_info = mList.get(i);
					break;
				case 4:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_374_430(mList.get(i)
											.getAdvertImgUrl()),
									buttomFrist_img);
					buttomFrist_info = mList.get(i);
					break;
				case 5:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_374_430(mList.get(i)
											.getAdvertImgUrl()),
									buttomSecond_img);
					buttomSecond_info = mList.get(i);
					break;
				case 6:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_374_430(mList.get(i)
											.getAdvertImgUrl()),
									buttomThree_img);
					buttomThree_info = mList.get(i);
					break;
				case 7:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(
									TextUtil.getUrlSmall_374_430(mList.get(i)
											.getAdvertImgUrl()), buttomFour_img);
					buttomFour_info = mList.get(i);
					break;
				default:
					break;
				}
			}
		}

	}

	private void initView() {
		mList = new ArrayList<HomeImgInfo>();
		topLeft_img = (ImageView) content.findViewById(R.id.topLeft_img);
		
		topRight_img = (ImageView) content.findViewById(R.id.topRight_img);
		buttomFrist_img = (ImageView) content
				.findViewById(R.id.buttomFrist_img);
		buttomSecond_img = (ImageView) content
				.findViewById(R.id.buttomSecond_img);
		buttomThree_img = (ImageView) content
				.findViewById(R.id.buttomThree_img);
		buttomFour_img = (ImageView) content.findViewById(R.id.buttomFour_img);
		topLeft_img.setOnClickListener(this);
		topRight_img.setOnClickListener(this);
		buttomFrist_img.setOnClickListener(this);
		buttomSecond_img.setOnClickListener(this);
		buttomThree_img.setOnClickListener(this);
		buttomFour_img.setOnClickListener(this);
	}

	public LinearLayout getContent() {
		return content;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topLeft_img:
			if (topLeft_info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,
						topLeft_info.getAdvertLinkType(),
						topLeft_info.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext, topLeft_info.getAdvertUrl());
			}
			break;
		case R.id.topRight_img:
			if (topRight_info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,
						topRight_info.getAdvertLinkType(),
						topRight_info.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext, topRight_info.getAdvertUrl());
			}
			break;
		case R.id.buttomFrist_img:
			if (buttomFrist_info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,
						buttomFrist_info.getAdvertLinkType(),
						buttomFrist_info.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext,
						buttomFrist_info.getAdvertUrl());
			}

			break;
		case R.id.buttomSecond_img:
			if (buttomSecond_info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,
						buttomSecond_info.getAdvertLinkType(),
						buttomSecond_info.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext,
						buttomSecond_info.getAdvertUrl());
			}

			break;
		case R.id.buttomThree_img:
			if (buttomThree_info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,
						buttomThree_info.getAdvertLinkType(),
						buttomThree_info.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext,
						buttomThree_info.getAdvertUrl());
			}
			break;
		case R.id.buttomFour_img:
			if (buttomFour_info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,
						buttomFour_info.getAdvertLinkType(),
						buttomFour_info.getAdvertUrl());
			} else {
				MyApplication.selectWeb(mContext,
						buttomFour_info.getAdvertUrl());
			}
			break;

		default:
			break;
		}

	}

}
