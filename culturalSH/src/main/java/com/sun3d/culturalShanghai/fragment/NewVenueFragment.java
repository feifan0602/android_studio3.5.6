package com.sun3d.culturalShanghai.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.adapter.MyNewVenueAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyVenueInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;

public class NewVenueFragment extends Fragment {
	private PullToRefreshListView mListView;
	private List<MyVenueInfo> list;
	private MyNewVenueAdapter mAdapter;
	private Map<String, String> mParams;
	private LoadingHandler mLoadingHandler;
	private static NewVenueFragment mNewVenueFragment;
	private LoadingDialog mLoadingDialog;
	private Boolean isResh = true;
	private int num = 0;

	public static NewVenueFragment getInstance() {
		return mNewVenueFragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mNewVenueFragment = this;
		mLoadingDialog = new LoadingDialog(getActivity());
		View view = inflater.inflate(R.layout.fragment_new_event, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.listView);
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				// TODO Auto-generated method stub
				container.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				onResh();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				onAddmoreData();
			}

		});
		RelativeLayout loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		return view;
	}

	/**
	 * 刷新
	 */
	private void onResh() {
		isResh = true;
		num = 0;
		getData(num);
	}

	/**
	 * 加载更多
	 */
	private void onAddmoreData() {
		isResh = false;
		num = HttpUrlList.HTTP_NUM + num;
		getData(num);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<MyVenueInfo>();
		mAdapter = new MyNewVenueAdapter(getActivity(), list);
		mListView.setAdapter(mAdapter);
		isResh = true;
		num = 0;
		getData(num);
	}

	/**
	 * 去取消
	 * 
	 * @param info
	 */
	public void goToDialog(MyVenueInfo info) {
		Intent intent = new Intent(getActivity(), MessageDialog.class);
		FastBlur.getScreen(getActivity());
		intent.putExtra(DialogTypeUtil.DialogContent, info);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_VENUE);
		startActivityForResult(intent, HttpCode.BACK);
	}

	/**
	 * 获取数据
	 */
	private void getData(int num) {
		mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, num + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyVenue.NEW_VENUE_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mListView.onRefreshComplete();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							List<MyVenueInfo> nowlist = JsonUtil.getMyVenueList(resultStr);
							if (nowlist.size() > 0) {
								if (isResh) {
									isResh = false;
									list.clear();
								}
								list.addAll(nowlist);
								mAdapter.setList(list);
								mAdapter.notifyDataSetChanged();
//								LoadingTextShowPopWindow.showLoadingText(getActivity(),
//										MyOrderActivity.topView, "加载完成");
							} else {
//								LoadingTextShowPopWindow.showLoadingText(getActivity(),
//										MyOrderActivity.topView, "加载完成");
							}
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 取消场馆
	 */
	private void onCancel(final MyVenueInfo info) {
		mLoadingDialog.startDialog("请稍候");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.Venue.CANCEL_ROOMORDER_ID, info.getRoomOrderId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.CANCEL_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								ToastUtil.showToast("取消成功");
								mAdapter.list.remove(info.getIndex());
								mAdapter.notifyDataSetChanged();
//								PastVenueFragment.getInstance().addData();
							} else {
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else {
							ToastUtil.showToast(resultStr);
						}

						mLoadingDialog.cancelDialog();
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case HttpCode.BACK:
			MyVenueInfo info = (MyVenueInfo) data
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			if (info != null) {
				onCancel(info);
			}

			break;

		default:
			break;
		}
	}

}
