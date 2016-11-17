package com.sun3d.culturalShanghai.activity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.adapter.HomeDetail_ContentAdapter;
import com.sun3d.culturalShanghai.object.HomeDataInfo;
import com.sun3d.culturalShanghai.object.HomeDetail_ContentInfor;
import com.sun3d.culturalShanghai.object.HomeImgInfo;
import com.sun3d.culturalShanghai.view.ScrollViewListView;

import java.util.ArrayList;
import java.util.List;

public class HomeDetail_Content {
	private LinearLayout content;
	private Context mContext;
	private Activity mActivity;
	private ScrollViewListView homedetail_list;
	private ArrayList<HomeDetail_ContentInfor> list_Info;
	private ArrayList<HomeDetail_ContentInfor> list;
	private TextView mTitle_tv;
	private List<HomeImgInfo> mImgList;
	private List<HomeDataInfo> mDataList;
	private String TAG = "HomeDetail_Content";

	public HomeDetail_Content(Context context, Activity activity) {
		super();
		this.mContext = context;
		this.mActivity = activity;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.homedetail_content, null);
		initView();
	}

	public void setData(List<HomeImgInfo> mImgList,
			List<HomeDetail_ContentInfor> mDataList) {
		this.mImgList = mImgList;
		list.clear();
		list_Info.clear();
		for (int i = 0; i < mImgList.size(); i++) {
			if (mImgList.get(i).getAdvertType().equals("D")) {
				HomeDetail_ContentInfor info = new HomeDetail_ContentInfor();
				info.setAdvertPostion(mImgList.get(i).getAdvertPostion());
				info.setAdvertSort(mImgList.get(i).getAdvertSort());
				info.setCreateTime(mImgList.get(i).getCreateTime());
				info.setUpdateTime(mImgList.get(i).getUpdateTime());
				info.setAdvertLink(mImgList.get(i).getAdvertLink());
				info.setAdvertState(mImgList.get(i).getAdvertState());
				info.setAdvertLinkType(mImgList.get(i).getAdvertLinkType());
				info.setAdvertType(mImgList.get(i).getAdvertType());
				info.setAdvertImgUrl(mImgList.get(i).getAdvertImgUrl());
				info.setAdvertTitle(mImgList.get(i).getAdvertTitle());
				info.setAdvertId(mImgList.get(i).getAdvertId());
				info.setAdvertUrl(mImgList.get(i).getAdvertUrl());
				info.setCreateBy(mImgList.get(i).getCreateBy());
				info.setUpdateBy(mImgList.get(i).getUpdateBy());
				list_Info.add(info);
			}
		}
		int pos = 0;
		for (int i = 0; i < mDataList.size(); i++) {
			if (i % 3 == 0 && i != 0 && pos < list_Info.size()) {
				list.add(list_Info.get(pos));
				pos++;
			}
			list.add(mDataList.get(i));
		}
		HomeDetail_ContentAdapter ara = new HomeDetail_ContentAdapter(
				mActivity, list, list_Info.size());
		homedetail_list.setAdapter(ara);



		// homedetail_list.setOnTouchListener(myTouch);
		// MyApplication.setListViewHomeHeight(homedetail_list);
		// homedetail_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		// homedetail_list.setSelection(list_Info.size()-1);
		// homedetail_list.setEnabled(false);
	}

	private void initView() {
		list = new ArrayList<HomeDetail_ContentInfor>();
		mImgList = new ArrayList<HomeImgInfo>();
		mDataList = new ArrayList<HomeDataInfo>();
		homedetail_list = (ScrollViewListView) content
				.findViewById(R.id.homedetail_list);
		mTitle_tv = (TextView) content.findViewById(R.id.tv);
		mTitle_tv.setTypeface(MyApplication.GetTypeFace());
		list_Info = new ArrayList<HomeDetail_ContentInfor>();
	}

	public LinearLayout getContent() {
		return content;

	}
}
