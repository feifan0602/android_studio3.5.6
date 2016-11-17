package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.CollectUtil;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.adapter.CollectVenueAdapter;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;

public class CollectVenueFragment extends Fragment {
	private SwipeMenuListView mListView;
	private List<VenueDetailInfor> list;
	private CollectVenueAdapter mAdapter;
	private LoadingHandler mLoadingHandler;
	private LoadingDialog mLoadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_list, null);
		mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				// TODO Auto-generated method stub
				container.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		RelativeLayout loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<VenueDetailInfor>();
		mAdapter = new CollectVenueAdapter(getActivity(), list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), VenueDetailActivity.class);
				intent.putExtra("venueId", list.get(arg2).getVenueId());
				startActivity(intent);
			}
		});
		addmenu();
		getActivityData();
	}

	/**
	 * 获取场馆数据
	 */
	private void getActivityData() {
		Map<String, String> mActivityParams = new HashMap<String, String>();
		mActivityParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		mActivityParams.put(HttpUrlList.HTTP_PAGE_NUM, "10");
		mActivityParams.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Collect.VENUE_LIST_URL, mActivityParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						list = JsonUtil.getCollectVenueList(resultStr);
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							mAdapter.setList(list);
							mAdapter.notifyDataSetChanged();
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	private void sendmyBroadcast(VenueDetailInfor mVenueDetailInfor) {
		Intent intent = new Intent(); // Itent就是我们要发送的内容
		intent.putExtra(MyApplication.COLLECT_ACTION_OBJECT, mVenueDetailInfor);
		intent.setAction(MyApplication.COLLECT_ACTION_FLAG_VUNUE); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
		getActivity().sendBroadcast(intent); // 发送广播
	}

	/**
	 * 删除收藏
	 * 
	 * @param position
	 */
	private void cancelCollect(final int position) {
		mLoadingDialog = new LoadingDialog(getActivity());
		mLoadingDialog.startDialog("请稍等");
		CollectUtil.cancelVenue(getActivity(), list.get(position).getVenueId(),
				new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOK) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (isOK) {
							list.get(position).setVenueIsCollect(false);
							// sendmyBroadcast(list.get(position));
							list.remove(position);
							mAdapter.notifyDataSetChanged();
							ToastUtil.showToast("删除成功");

						} else {
							ToastUtil.showToast("操作失败");
						}
					}
				});
	}

	private void addmenu() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
				// set item background
				deleteItem.setBackground(new ColorDrawable(getResources().getColor(
						R.color.common_red)));
				// set item width
				deleteItem.setWidth(DensityUtil.dip2px(getActivity(), 90));
				// set item title
				deleteItem.setTitle("删除");
				// set item title fontsize
				deleteItem.setTitleSize(16);
				// set item title font color
				deleteItem
						.setTitleColor(getActivity().getResources().getColor(R.color.white_color));
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);
		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

				switch (index) {
				case 0:
					// delete
					cancelCollect(position);
					break;

				}
				return false;
			}
		});

		// set SwipeListener
		mListView.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start

			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end

			}
		});
	}
}
