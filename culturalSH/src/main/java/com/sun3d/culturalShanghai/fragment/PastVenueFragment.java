package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.MyPastVenueAdapter;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyVenueInfo;

public class PastVenueFragment extends Fragment {
	private SwipeMenuListView mListView;
	private List<MyVenueInfo> list;
	private MyPastVenueAdapter mAdapter;
	private Map<String, String> mParams;
	private LoadingHandler mLoadingHandler;
	private LoadingDialog mLoadingDialog;
	private static PastVenueFragment mPastVenueFragment;

	public static PastVenueFragment getInstance() {
		return mPastVenueFragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mPastVenueFragment = this;
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
		addData();
	}

	public void addData() {
		list = new ArrayList<MyVenueInfo>();
		mAdapter = new MyPastVenueAdapter(getActivity(), list);
		mListView.setAdapter(mAdapter);
		mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, "20");
		addmenu();
		getData(0);
	}

	/**
	 * 获取数据
	 * 
	 * @param num
	 */
	private void getData(int num) {
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, num + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyVenue.PAST_VENUE_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							list = JsonUtil.getVenuePastList(resultStr);
							mAdapter.setList(list);
							mAdapter.notifyDataSetChanged();
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}

					}
				});
	}

	/**
	 * 删除活动室
	 * 
	 * @param position
	 */
	private void deleteHisVenue(final int position) {
		mLoadingDialog = new LoadingDialog(getActivity());
		mLoadingDialog.startDialog("请稍等");
		Map<String, String> params = new HashMap<String, String>();
		params.put("roomOrderId", list.get(position).getRoomOrderId());
		params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyVenue.DELETE_VENUE_URL, params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE) {
								ToastUtil.showToast("删除成功");
								list.remove(position);
								mAdapter.notifyDataSetChanged();
								mListView.invalidate();
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}

						} else {
							ToastUtil.showToast(resultStr);
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
					deleteHisVenue(position);
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
