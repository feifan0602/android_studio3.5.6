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
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.Utils;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.adapter.MyNewEventAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

public class NewEventFragment extends Fragment {
	private PullToRefreshListView mListView;
	private List<MyActivityBookInfo> list;
	private MyNewEventAdapter mAdapter;
	private int num = 0;
	private LoadingHandler mLoadingHandler;
	private LoadingDialog mLoadingDialog;
	private static NewEventFragment mNewEventFragment;
	private Boolean isResh = true;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("NewEventFragment"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("NewEventFragment");
	}

	public static NewEventFragment getInstance() {
		return mNewEventFragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mNewEventFragment = this;
		View view = inflater.inflate(R.layout.fragment_new_event, null);
		// Topview = (View) view.findViewById(R.id.activity_page_line);
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
		mListView.setOnScrollListener(new PauseOnScrollListener(MyApplication.getInstance()
				.getImageLoader(), true, false));
		RelativeLayout loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		mLoadingDialog = new LoadingDialog(getActivity());

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		list = new ArrayList<MyActivityBookInfo>();
		mAdapter = new MyNewEventAdapter(getActivity(), list);
		mListView.setAdapter(mAdapter);
		isResh = true;
		getData(num);
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

	/**
	 * 获取数据
	 * 
	 * @param num
	 */
	private void getData(int num) {
		Map<String, String> mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, num + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.MY_NEWEVENT_URL, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mListView.onRefreshComplete();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							mLoadingHandler.stopLoading();
							List<MyActivityBookInfo> nowlist = JsonUtil
									.getMyNewEventList(resultStr);
							if (nowlist.size() > 0) {
								if (isResh) {
									isResh = false;
									list.clear();
								}
								list.addAll(nowlist);
								mAdapter.setData(list);
								mAdapter.notifyDataSetChanged();
							} else {
//								LoadingTextShowPopWindow.showLoadingText(getActivity(),
//										MyOrderActivity.topView, "加载完成");
							}

						} else {
							mLoadingHandler.showErrorText(resultStr);
						}

					}
				});
	}

	/**
	 * 跳转页面去 取消页面
	 * 
	 * @param info
	 */
	public void goToDialog(MyActivityBookInfo info) {
		Intent intent = new Intent(getActivity(), MessageDialog.class);
		FastBlur.getScreen(getActivity());
		intent.putExtra(DialogTypeUtil.DialogContent, info);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_ACTIVITY);
		startActivityForResult(intent, HttpCode.BACK);
	}

	/**
	 * 取消活动 2645 宋
	 */
	public void onCancel(final MyActivityBookInfo info) {
		mLoadingDialog.startDialog("请稍等");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.MyEvent.CANCEL_EVENT_ID, info.getActivityOrderId());
		params.put(HttpUrlList.MyEvent.CANCEL_EVENT_SEAT, setSeat(info));
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.CANCEL_EVENT_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								ToastUtil.showToast("活动已取消");
								mAdapter.list.remove(info.getIndex());
								if (info.getIsSeatOnline()) {// 在线选座
									disposeDate(info);
								}
								mAdapter.notifyDataSetChanged();
								// if (PastEventFragment.getInstance() != null)
								// {
								// PastEventFragment.getInstance().adddData();
								// }
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

	/**
	 * 需要取消的在线选座（座位）
	 * */
	private String setSeat(final MyActivityBookInfo info) {
		String seat = "";
		if (!info.getIsSeatOnline()) {
			return seat;
		}
		String[] seatArray = info.getOrderLine().split(",");
		if (seatArray != null && seatArray.length > 0) {
			for (int i = 0; i < seatArray.length; i++) {
				if (info.getoSummary()[i]) {
					seat += seatArray[i] + ",";
				}
			}
		}
		return seat;
	}

	/**
	 * 在线选座（取消成功的状态下），处理adapter数据
	 * */
	private void disposeDate(MyActivityBookInfo info) {
		String seatAll = "", seatArrays = "";
		String[] seats = info.getActivitySeats().split(",");
		String[] seatArray = info.getOrderLine().split(",");
		Boolean[] oSummary = info.getoSummary();
		for (int i = 0; i < oSummary.length; i++) {
			if (!oSummary[i]) {
				seatAll += seats[i] + ",";
				seatArrays += seatArray[i] + ",";
			}
		}

		if (seatAll.length() > 0) {
			info.setOrderLine(seatArrays);
			info.setActivitySeats(seatAll);
			info.setOrderVotes(seatAll.split(",").length + "");
			info.setoSummary(Utils.sizeBoo(seatAll));
			mAdapter.list.add(info.getIndex(), info);
			return;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case HttpCode.BACK:
			MyActivityBookInfo info = (MyActivityBookInfo) data
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
