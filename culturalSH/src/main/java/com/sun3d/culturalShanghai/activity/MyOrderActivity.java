package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ScreenshotUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.Utils;
import com.sun3d.culturalShanghai.adapter.MyOrderAdapter;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.dialog.MessageDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;

import android.view.View.OnKeyListener;

import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;
import com.sun3d.culturalShanghai.object.MyOrderInfo;
import com.sun3d.culturalShanghai.object.MyUserInfo;
import com.sun3d.culturalShanghai.thirdparty.MyShare;
import com.sun3d.culturalShanghai.thirdparty.ShareName;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的订单(活动与活动室)
 * 
 * @author zhoutanping
 * */
public class MyOrderActivity extends Activity implements OnClickListener,
		RefreshListenter {
	private Boolean addmore_bool;
	private PullToRefreshListView mListView;
	private List<MyOrderInfo> orderList;
	private List<MyOrderInfo> mList;
	private LoadingHandler mLoadingHandler;
	public MyOrderAdapter mAdapter;
	private LoadingDialog mLoadingDialog;
	private EditText orderEdit;
	private static MyOrderActivity mMyOrderActivity;
	private int index = 0;
	private int indexNum = 5;
	private View layoutView;
	private boolean isLoad = false;
	private RelativeLayout loadingLayout;
	private ImageView left_iv;
	private TextView middle_tv;
	private LinearLayout being_ll, history_ll, join_ll;
	private ImageView being_iv, history_iv, join_iv, history_top_iv,
			being_top_iv, join_top_iv;
	private List<MyUserInfo> MyUserList;
	private TextView being_tv, history_tv, join_tv;
	/**
	 * 1 表示现在 2表示历史 3待审核
	 */
	public int now_histroy = 3;
	private TextView collection_null;

	public static MyOrderActivity getInstance() {
		return mMyOrderActivity;

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainFragmentActivity"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainFragmentActivity");
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_my_order);
		MyApplication.getInstance().addActivitys(this);
		mMyOrderActivity = this;
		init();
		initData();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoadingTextShowPopWindow.dismissPop();
	}

	private void init() {
		orderList = new ArrayList<MyOrderInfo>();
		mList = new ArrayList<MyOrderInfo>();
		collection_null = (TextView) findViewById(R.id.collection_null);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("我的订单");
		being_ll = (LinearLayout) findViewById(R.id.being_ll);
		history_ll = (LinearLayout) findViewById(R.id.history_ll);
		join_ll = (LinearLayout) findViewById(R.id.join_ll);
		join_ll.setOnClickListener(this);
		being_ll.setOnClickListener(this);
		history_ll.setOnClickListener(this);
		join_iv = (ImageView) findViewById(R.id.join_iv);
		being_iv = (ImageView) findViewById(R.id.being_iv);
		history_iv = (ImageView) findViewById(R.id.history_iv);
		history_top_iv = (ImageView) findViewById(R.id.history_top_iv);
		being_top_iv = (ImageView) findViewById(R.id.being_top_iv);
		join_top_iv = (ImageView) findViewById(R.id.join_top_iv);
		join_tv = (TextView) findViewById(R.id.join_tv);
		being_tv = (TextView) findViewById(R.id.being_tv);
		history_tv = (TextView) findViewById(R.id.history_tv);
		being_tv.setTypeface(MyApplication.GetTypeFace());
		history_tv.setTypeface(MyApplication.GetTypeFace());
		join_tv.setTypeface(MyApplication.GetTypeFace());
		// findViewById(R.id.title_left).setOnClickListener(this);
		// findViewById(R.id.title_right).setVisibility(View.GONE);
		// TextView mTitle = (TextView) findViewById(R.id.title_content);
		// mTitle.setVisibility(View.VISIBLE);
		// mTitle.setText("我的订单");
		mListView = (PullToRefreshListView) findViewById(R.id.listView);
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingDialog = new LoadingDialog(this);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		mListView.setOnItemClickListener(new MyItemClick());
		orderEdit = (EditText) findViewById(R.id.order_edit);
		orderEdit.setOnKeyListener(onKeySearch);
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullDownToRefresh");
				isLoad = true;
				onResh(orderEdit.getText().toString());

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Log.d("PullToRefreshListView", "onPullUpToRefresh");
				isLoad = false;
				// 这里是上拉加载更多
				onAddmoreData("");
				// onResh(orderEdit.getText().toString());

			}

		});
	}

	private void initData() {
		mAdapter = null;
		mAdapter = new MyOrderAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		// mLoadingHandler.startLoading();
		isLoad = true;
		onResh(null);
	}

	public void onResh(String content) {
		index = 0;
		mList.clear();
		addmore_bool = false;
		if (MyApplication.room_activity == 1) {
			clearll();
			join_iv.setVisibility(View.VISIBLE);
			join_top_iv.setBackgroundResource(R.drawable.join_press);
			join_tv.setTextColor(getResources().getColor(R.color.text_color_5e));
			now_histroy = 1;
			mListView.setVisibility(View.GONE);
			mLoadingHandler.startLoading();
			getOrderInfo(content);
			MyApplication.room_activity = 2;
		} else if (MyApplication.room_activity == 0) {
			clearll();
			being_iv.setVisibility(View.VISIBLE);
			being_top_iv.setBackgroundResource(R.drawable.join_press);
			being_tv.setTextColor(getResources()
					.getColor(R.color.text_color_5e));
			if (now_histroy == 1) {
				getOrderInfo(content);
			} else if (now_histroy == 2) {
				getOrderInfoHistory(content);
			} else if (now_histroy == 3) {
				getUserInfo();
			}
			MyApplication.room_activity = 2;
		} else {
			if (now_histroy == 1) {
				getOrderInfo(content);
			} else if (now_histroy == 2) {
				getOrderInfoHistory(content);
			} else if (now_histroy == 3) {
				getUserInfo();
			}
		}
		mAdapter.setStatus(now_histroy);

	}

	private void getUserCheckOrder() {

		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, index + "");
		params.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyMessage.WFF_APPUSERCHECKORDER, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是待审核" + resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<MyActivityBookInfo> book = JsonUtil
									.getMyBeingEventList(resultStr);
							List<MyActivityRoomInfo> room = JsonUtil
									.getMyNewRoomList(resultStr);

							getMyOrderList(room, book);
							if (null == orderList || orderList.size() == 0
									&& addmore_bool == false) {
								// loadingLayout.setVisibility(View.VISIBLE);
								// mLoadingHandler.showErrorText("暂无数据！");
								mLoadingHandler.stopLoading();
								collection_null.setVisibility(View.VISIBLE);
								collection_null
										.setText("你还没参加过任何活动，点击查看首页精彩活动！");
								collection_null
										.setOnClickListener(MyOrderActivity.this);
								mListView.setVisibility(View.GONE);
								return;
							} else {
								mList.addAll(orderList);
								collection_null.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								mAdapter.setData(mList);
							}
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
							ToastUtil.showToast("数据请求失败:" + resultStr);
						}
						mLoadingDialog.cancelDialog();
						isLoad = false;
						mListView.onRefreshComplete();
					}
				});
	}

	private void getOrderInfo(String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, index + "");
		params.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");

		MyHttpRequest.onHttpPostParams(HttpUrlList.MyMessage.WFF_USERORDERS,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是订单的详情" + resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<MyActivityBookInfo> book = JsonUtil
									.getMyNewEventList(resultStr);
							List<MyActivityRoomInfo> room = JsonUtil
									.getMyNewRoomList(resultStr);

							getMyOrderList(room, book);
							if (null == orderList || orderList.size() == 0
									&& addmore_bool == false) {
								// loadingLayout.setVisibility(View.VISIBLE);
								// mLoadingHandler.showErrorText("暂无数据！");
								mLoadingHandler.stopLoading();
								collection_null.setVisibility(View.VISIBLE);
								collection_null
										.setText("你还没参加过任何活动，点击查看首页精彩活动！");
								collection_null
										.setOnClickListener(MyOrderActivity.this);
								mListView.setVisibility(View.GONE);
								return;
							} else {
								mList.addAll(orderList);
								collection_null.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								mAdapter.setData(mList);
							}
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
							ToastUtil.showToast("数据请求失败:" + resultStr);
						}
						mLoadingDialog.cancelDialog();
						isLoad = false;
						mListView.onRefreshComplete();
					}
				});
	}

	private void onAddmoreData(String content) {
		index = HttpUrlList.HTTP_NUM + index;
		addmore_bool = true;
		if (now_histroy == 1) {
			getOrderInfo(content);
		} else if (now_histroy == 2) {
			getOrderInfoHistory(content);
		} else if (now_histroy == 3) {
			getUserInfo();

		}
	}

	private void getUserInfo() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.GET_USERINFO,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.i("ceshi", "看看结果==   " + resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							MyUserList = JsonUtil.getMyUserInfoList(resultStr);
							handler.sendEmptyMessage(1);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				MyApplication.MyUserType = MyUserList.get(0).getUserType();
				getUserCheckOrder();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			setResult(102);

			break;
		case R.id.collection_null:
			finish();
			MyApplication.getInstance().exitActivity();
			startActivity(new Intent(MyOrderActivity.this,MainFragmentActivity.class));
			MyApplication.change_fragment = 0;
			break;
		case R.id.left_iv:
			finish();
			MyApplication.getInstance().exitActivity();
			startActivity(new Intent(MyOrderActivity.this,MainFragmentActivity.class));
			MyApplication.change_fragment = 4;
			break;
		case R.id.being_ll:
			clearll();
			being_iv.setVisibility(View.VISIBLE);
			being_top_iv.setBackgroundResource(R.drawable.examine_press);
			being_tv.setTextColor(getResources()
					.getColor(R.color.text_color_5e));
			now_histroy = 3;
			mListView.setVisibility(View.GONE);
			mLoadingHandler.startLoading();
			onResh(null);
			break;
		case R.id.join_ll:
			clearll();
			join_iv.setVisibility(View.VISIBLE);
			join_top_iv.setBackgroundResource(R.drawable.join_press);
			join_tv.setTextColor(getResources().getColor(R.color.text_color_5e));
			now_histroy = 1;
			mListView.setVisibility(View.GONE);
			mLoadingHandler.startLoading();
			onResh(null);
			break;
		case R.id.history_ll:
			clearll();
			history_iv.setVisibility(View.VISIBLE);
			history_top_iv.setBackgroundResource(R.drawable.history_press);
			history_tv.setTextColor(getResources().getColor(
					R.color.text_color_5e));
			now_histroy = 2;
			mListView.setVisibility(View.GONE);
			mLoadingHandler.startLoading();
			onResh(null);
			break;
		default:
			break;
		}
	}

	private void clearll() {
		being_iv.setVisibility(View.GONE);
		history_iv.setVisibility(View.GONE);
		join_iv.setVisibility(View.GONE);
		history_top_iv.setBackgroundResource(R.drawable.history);
		being_top_iv.setBackgroundResource(R.drawable.examine);
		join_top_iv.setBackgroundResource(R.drawable.join);
		being_tv.setTextColor(getResources().getColor(R.color.text_color_49));
		history_tv.setTextColor(getResources().getColor(R.color.text_color_49));
		join_tv.setTextColor(getResources().getColor(R.color.text_color_49));
	}

	/**
	 * 搜索
	 * */
	OnKeyListener onKeySearch = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub

			if (keyCode == KeyEvent.KEYCODE_ENTER
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),
							0);
					if (TextUtils.isEmpty(orderEdit.getText())) {
						ToastUtil.showToast("请先输入搜索条件!");
					} else {
						isLoad = true;
						mLoadingDialog.startDialog("请稍候");
						onResh(orderEdit.getText().toString());
					}
				}
				return true;
			}
			return false;
		}
	};

	private void getOrderInfoHistory(String content) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.HTTP_PAGE_INDEX, index + "");
		params.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.MyMessage.WFF_APPUSERHISTORYORDER, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是订单历史的详情"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							List<MyActivityBookInfo> book = JsonUtil
									.getMyNewEventList(resultStr);
							List<MyActivityRoomInfo> room = JsonUtil
									.getMyNewRoomList(resultStr);

							getMyOrderList(room, book);
							if (null == orderList || orderList.size() == 0
									&& addmore_bool == false) {
								// loadingLayout.setVisibility(View.VISIBLE);
								// mLoadingHandler.showErrorText("暂无数据！");
								collection_null.setVisibility(View.VISIBLE);
								collection_null
										.setText("你还没参加过任何活动，点击查看附近精彩活动！");
								collection_null
										.setOnClickListener(MyOrderActivity.this);
								mListView.setVisibility(View.GONE);
								mLoadingHandler.stopLoading();
								return;
							} else {
								mList.addAll(orderList);
								collection_null.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
								mAdapter.setData(mList);
								mLoadingHandler.stopLoading();
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
							ToastUtil.showToast("数据请求失败:" + resultStr);

						}
						Log.i("tag2", "asdf");
						mLoadingDialog.cancelDialog();
						isLoad = false;
						mListView.onRefreshComplete();
					}
				});

	}

	/**
	 * 整合活动与活动室数据
	 * */
	public void getMyOrderList(List<MyActivityRoomInfo> roomList,
			List<MyActivityBookInfo> bookList) {
		orderList.clear();
		int leng = 0;
		if (roomList.size() > bookList.size()) {
			leng = roomList.size();
		} else {
			leng = bookList.size();
		}

		for (int i = 0; i < leng; i++) {
			if (roomList.size() > i) {
				MyOrderInfo order = new MyOrderInfo();
				order.setRoomInfo(roomList.get(i));
				order.setActivityOrRoom("1");
				orderList.add(order);
			}
			if (bookList.size() > i) {
				if (bookList.get(i).getRoomId() != null) {
					// 這個是審核的
					MyOrderInfo order = new MyOrderInfo();
					order.setActivityOrRoom("3");
					order.setBookInfo(bookList.get(i));
					orderList.add(order);
				} else {
					MyOrderInfo order = new MyOrderInfo();
					order.setActivityOrRoom("0");
					order.setBookInfo(bookList.get(i));
					orderList.add(order);
				}

			}

		}
	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		mLoadingHandler.startLoading();
		onResh(null);
	}

	/**
	 * 去取消活动室
	 * 
	 * @param info
	 */
	public void goToDialogRoom(MyActivityRoomInfo info) {
		Intent intent = new Intent(this, MessageDialog.class);
		FastBlur.getScreen(this);
		intent.putExtra(DialogTypeUtil.DialogContent, info);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_VENUE);
		startActivityForResult(intent, HttpCode.BACK_ROOM);
	}

	/**
	 * 取消活动室
	 */
	private void onCancelRoom(final MyActivityRoomInfo info) {
		mLoadingDialog.startDialog("请稍候");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
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
								orderList.get(info.getIndex()).getRoomInfo()
										.setOrderStatus("2");
								mAdapter.setData(orderList);
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
	 * 取消活动 2645 宋
	 */
	public void onCancel(final MyActivityBookInfo info) {
		mLoadingDialog.startDialog("请稍等");
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		params.put(HttpUrlList.MyEvent.CANCEL_EVENT_ID,
				info.getActivityOrderId());
		params.put(HttpUrlList.MyEvent.CANCEL_EVENT_SEAT, setSeat(info));
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.CANCEL_EVENT_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							int code = JsonUtil.getJsonStatus(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == code) {
								ToastUtil.showToast("活动已取消");
								orderList.get(info.getIndex()).getBookInfo()
										.setOrderPayStatus("2");
								orderList.get(info.getIndex()).getBookInfo()
										.setOrderVotes("0");
								if (info.getIsSeatOnline()) {// 在线选座
									disposeDate(info);
								}
								// mAdapter.list = orderList;
								mAdapter.setData(orderList);
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
	 * 在线选座（取消成功的状态下），处理adapter数据
	 * */
	private void disposeDate(MyActivityBookInfo info) {
		String seatAll = "", seatArrays = "";
		String[] seats = info.getActivitySeats().split(",");
		String[] seatArray = info.getOrderLine().split(",");
		Boolean[] oSummary = info.getoSummary();
		for (int i = 0; i < oSummary.length; i++) {
			Log.i("tag1", oSummary[i] + "");
			if (!oSummary[i]) {
				seatAll += seats[i] + ",";
				seatArrays += seatArray[i] + ",";
			}
		}
		Log.i("tag", seatAll + "：" + seatArrays);
		orderList.get(info.getIndex()).getBookInfo().setOrderLine(seatArrays);
		orderList.get(info.getIndex()).getBookInfo().setActivitySeats(seatAll);
		if (seatAll.length() > 0) {
			orderList.get(info.getIndex()).getBookInfo()
					.setOrderVotes(seatAll.split(",").length + "");
			orderList.get(info.getIndex()).getBookInfo().setOrderPayStatus("1");
			orderList.get(info.getIndex()).getBookInfo()
					.setoSummary(Utils.sizeBoo(seatArrays));
			return;
		}
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
	 * 跳转页面去 取消页面
	 * 
	 * @param info
	 */
	public void goToDialog(MyActivityBookInfo info) {
		Intent intent = new Intent(this, MessageDialog.class);
		FastBlur.getScreen(this);
		intent.putExtra(DialogTypeUtil.DialogContent, info);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.MessageDialog.MSGTYPE_CANCEL_ACTIVITY);
		startActivityForResult(intent, HttpCode.BACK);
	}

	/**
	 * 跳转页面去 发送给QQ或微信好友
	 */
	public void goToDialogQq(View view, MyActivityBookInfo orderInfo) {
		layoutView = view;
		Intent intent = new Intent(this, MessageDialog.class);
		FastBlur.getScreen(this);
		intent.putExtra(DialogTypeUtil.DialogContent, orderInfo);
		intent.putExtra(DialogTypeUtil.DialogType,
				DialogTypeUtil.MessageDialog.MSGTYPE_ELECTRONIC_SHARE);
		startActivityForResult(intent, HttpCode.BACK_QQ);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case HttpCode.BACK:
			MyActivityBookInfo info = (MyActivityBookInfo) data
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			if (info != null && info.getIsSeatOnline()) {
				onCancel(info);
			} else {
				for (int i = 0; i < info.getoSummary().length; i++) {
					info.getoSummary()[i] = false;
				}
				mAdapter.list.get(info.getIndex()).setBookInfo(info);
				onCancel(info);
			}
			break;
		case HttpCode.BACK_ROOM:
			MyActivityRoomInfo infoRoom = (MyActivityRoomInfo) data
					.getSerializableExtra(DialogTypeUtil.DialogContent);
			if (infoRoom != null) {
				onCancelRoom(infoRoom);
			}

			break;
		case HttpCode.BACK_MYORDER:
			onResh(null);
			break;
		case HttpCode.BACK_QQ:
			final String orderInfo = data
					.getStringExtra(DialogTypeUtil.DialogContent);
			final String imgUrl = "/" + orderInfo + ".png";
			ScreenshotUtil.getData(layoutView, imgUrl, new CollectCallback() {
				@Override
				public void onPostExecute(boolean isOK) {
					// TODO Auto-generated method stub
					if (isOK) {
						if ("QQ".equals(orderInfo.substring(
								orderInfo.length() - 2, orderInfo.length()))) {
							MyShare.showShare(ShareName.QQ,
									MyOrderActivity.this,
									FolderUtil.createImageCacheFile() + imgUrl);
						} else if ("WX".equals(orderInfo.substring(
								orderInfo.length() - 2, orderInfo.length()))) {
							MyShare.showShare(ShareName.Wechat,
									MyOrderActivity.this,
									FolderUtil.createImageCacheFile() + imgUrl);
						}
					} else {
						ToastUtil.showToast("分享失败");
					}
				}
			});

			break;
		case 102:
			Log.i("ceshi", "结束了");
			finish();
			break;
		default:
			break;
		}
	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int arg2,
				long arg3) {
			Intent intent = new Intent();
			MyOrderInfo orderInfo = (MyOrderInfo) parent
					.getItemAtPosition(arg2);
			intent.setClass(MyOrderActivity.this, ActivityOrderDetail.class);
			Bundle bundle = new Bundle();
			if (orderInfo.getActivityOrRoom().equals("0")) {
				// 活动
				bundle.putString("activityOrderId", orderInfo.getBookInfo()
						.getActivityOrderId());
				bundle.putString("ActivityOrRoom",
						orderInfo.getActivityOrRoom());
				bundle.putInt("now_histroy", now_histroy);
			} else if (orderInfo.getActivityOrRoom().equals("1")) {
				// 场馆活动室
				bundle.putString("activityOrderId", orderInfo.getRoomInfo()
						.getRoomOrderId());
				bundle.putString("ActivityOrRoom",
						orderInfo.getActivityOrRoom());
				bundle.putInt("now_histroy", now_histroy);
			} else if (orderInfo.getActivityOrRoom().equals("3")) {
				// 审核中的
				bundle.putString("activityOrderId", orderInfo.getBookInfo()
						.getRoomOrderId());
				bundle.putString("ActivityOrRoom",
						orderInfo.getActivityOrRoom());
				bundle.putInt("now_histroy", now_histroy);
			}
			intent.putExtras(bundle);
			intent.putExtra("orderInfo", orderInfo);
			startActivityForResult(intent, HttpCode.BACK_MYORDER);
		}
	};
}
