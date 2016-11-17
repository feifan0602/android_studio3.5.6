package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.adapter.HomeDetail_HorizonListAdapter;
import com.sun3d.culturalShanghai.adapter.HomeDetail_TopLayoutAdapter;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.HomeDetail_HorizonListInfor;
import com.sun3d.culturalShanghai.object.HomeDetail_TopLayoutInfor;
import com.sun3d.culturalShanghai.object.HomeImgInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.ScrollScrollView;

public class HomeDetail_HorizontalListView {
	private LinearLayout content;
	private Context mContext;
	private Activity mActivity;
	private HorizontalListView horizon_Listview;
	private ArrayList<HomeDetail_HorizonListInfor> list_Info;
	private PullToRefreshScrollView Scroll_view;
	private int lastX, lastY;
	private String TAG = "HomeDetail_HorizontalListView";
	private TextView tv;
	private List<HomeImgInfo> mList;

	public HomeDetail_HorizontalListView(Context context, Activity activity,
			PullToRefreshScrollView ssv) {
		super();
		this.mContext = context;
		this.mActivity = activity;
		this.Scroll_view = ssv;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.homedetail_horizontallistview, null);
		initView();
	}

	public void setData(List<HomeImgInfo> mImgList) {
		this.mList = mImgList;
		horizon_Listview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				content.getParent().requestDisallowInterceptTouchEvent(true);
				int x = (int) event.getRawX();
				int y = (int) event.getRawY();

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = x;
					lastY = y;
					break;
				case MotionEvent.ACTION_MOVE:
					int deltaY = y - lastY;
					int deltaX = x - lastX;
					if (Math.abs(deltaX) > Math.abs(deltaY)) {
						Scroll_view.getParent()
								.requestDisallowInterceptTouchEvent(false);
					} else {
						Scroll_view.getParent()
								.requestDisallowInterceptTouchEvent(true);
					}
				default:
					break;
				}
				return false;
			}
		});

		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getAdvertType().equals("C")) {
				HomeDetail_HorizonListInfor info = new HomeDetail_HorizonListInfor();
				info.setAdvertPostion(mList.get(i).getAdvertPostion());
				info.setAdvertSort(mList.get(i).getAdvertSort());
				info.setCreateTime(mList.get(i).getCreateTime());
				info.setUpdateTime(mList.get(i).getUpdateTime());
				info.setAdvertLink(mList.get(i).getAdvertLink());
				info.setAdvertState(mList.get(i).getAdvertState());
				info.setAdvertLinkType(mList.get(i).getAdvertLinkType());
				info.setAdvertType(mList.get(i).getAdvertType());
				info.setAdvertImgUrl(mList.get(i).getAdvertImgUrl());
				info.setAdvertTitle(mList.get(i).getAdvertTitle());
				info.setAdvertId(mList.get(i).getAdvertId());
				info.setAdvertUrl(mList.get(i).getAdvertUrl());
				info.setCreateBy(mList.get(i).getCreateBy());
				info.setUpdateBy(mList.get(i).getUpdateBy());
				list_Info.add(info);
			}

		}

		HomeDetail_HorizonListAdapter ara = new HomeDetail_HorizonListAdapter(
				mActivity, list_Info);
		horizon_Listview.setAdapter(ara);
		horizon_Listview.setOnItemClickListener(myItem);
	}

	public LinearLayout getContent() {
		return content;

	}

	private void initView() {
		mList = new ArrayList<HomeImgInfo>();
		horizon_Listview = (HorizontalListView) content
				.findViewById(R.id.horizon_listview);
		tv = (TextView) content.findViewById(R.id.tv);
		tv.setTypeface(MyApplication.GetTypeFace());
		list_Info = new ArrayList<HomeDetail_HorizonListInfor>();

	}

	OnItemClickListener myItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int position,
				long arg3) {
			//Toast.makeText(mActivity, "看看位置pos" + position, 1000).show();
			HomeDetail_HorizonListInfor info = (HomeDetail_HorizonListInfor) parent.getItemAtPosition(position);
			if (info.getAdvertLink() == 0) {
				MyApplication.selectImg(mContext,info.getAdvertLinkType(), info.getAdvertUrl());
			} else {
				
				MyApplication.selectWeb(mContext,info.getAdvertUrl());
			}
		}
	};

}
