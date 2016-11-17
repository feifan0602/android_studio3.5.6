package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.sun3d.culturalShanghai.adapter.MyCollectAdapter;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.MyCollectInfo;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

public class MyCollectActivity extends Activity implements OnClickListener,
		RefreshListenter  {
	private RelativeLayout loadingLayout;
	private LoadingHandler mLoadingHandler;
	private SwipeMenuListView mListView;
	private List<MyCollectInfo> list, list_vuneu;
	private MyCollectAdapter mAdapter, vuneu_adapter;
	private Context mContext;
	private LoadingDialog mLoadingDialog;
	private EditText mSearch;
	private TextView tip;
	private ImageView left_iv;
	private TextView middle_tv;
	private LinearLayout activity_ll, venue_ll;
	private ImageView activity_view, venue_view;
	public int select_activity_venue = 0;
	private TextView collection_null;
	private int index = 0;
	private TextView activity_tv, venue_tv;
	/**
	 * 1 表示活动 2 表示场馆
	 */
	private int activity_venue = 1;

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MyCollectActivity"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MyCollectActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collect);
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		// mLoadingHandler = new LoadingHandler(loadingLayout);
		// mLoadingHandler.setOnRefreshListenter(this);
		// mLoadingHandler.startLoading();
		init();
		initData();
		getData();
	}

	private void getVenueData() {
		Map<String, String> mActivityParams = new HashMap<String, String>();
		mActivityParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mActivityParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM
				+ "");
		mActivityParams.put(HttpUrlList.HTTP_PAGE_INDEX, index + "");
		// mActivityParams.put("activityName", mSearch.getText().toString());
		// mActivityParams.put("venueName", mSearch.getText().toString());
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Collect.WFF_USERAPPCOLLECTVEN, mActivityParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个收藏场馆的" + resultStr);
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							list_vuneu.clear();
							list_vuneu = JsonUtil
									.getCollectListVenue(resultStr);
							if (list_vuneu.size() == 0) {
								mLoadingHandler.stopLoading();
								collection_null.setVisibility(View.VISIBLE);
								collection_null.setText("您还没有收藏活动或场馆，赶快去收藏吧！");
								mListView.setVisibility(View.GONE);
								return;
							} else {
								collection_null.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
							}
							// mAdapter.setList(list);
							// mAdapter.notifyDataSetChanged();
							mLoadingHandler.stopLoading();
							if (list.size() == 0) {
								tip.setVisibility(View.GONE);
							} else {
								tip.setVisibility(View.GONE);
							}
							handler.sendEmptyMessage(2);
						} else {
							Log.i("ceshi", "看看数据111");
							mLoadingHandler.stopLoading();
							handler.sendEmptyMessage(3);
						}

					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoadingTextShowPopWindow.dismissPop();
	}

	private void init() {
		activity_ll = (LinearLayout) findViewById(R.id.activity_ll);
		venue_ll = (LinearLayout) findViewById(R.id.venue_ll);
		activity_ll.setOnClickListener(this);
		venue_ll.setOnClickListener(this);
		activity_view = (ImageView) findViewById(R.id.activity_view);
		venue_view = (ImageView) findViewById(R.id.venue_view);
		collection_null = (TextView) findViewById(R.id.collection_null);
		activity_tv = (TextView) findViewById(R.id.activity_tv);
		venue_tv = (TextView) findViewById(R.id.venue_tv);
		activity_tv.setTypeface(MyApplication.GetTypeFace());
		venue_tv.setTypeface(MyApplication.GetTypeFace());
		// findViewById(R.id.title_left).setOnClickListener(this);
		// findViewById(R.id.title_right).setVisibility(View.GONE);
		// TextView mTitle = (TextView) findViewById(R.id.title_content);
		tip = (TextView) findViewById(R.id.tip);
		mSearch = (EditText) findViewById(R.id.collect_search);
		mSearch.setOnKeyListener(onKey);
		// mTitle.setVisibility(View.VISIBLE);
		// mTitle.setText("我的收藏");
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("我的收藏");
		mListView = (SwipeMenuListView) findViewById(R.id.listView);
//		mListView.set;
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
	}

	private void initData() {
		list = new ArrayList<MyCollectInfo>();
		list_vuneu = new ArrayList<MyCollectInfo>();
		addmenu();
		// mLoadingHandler.stopLoading();

	}

	private void getData() {
		Map<String, String> mActivityParams = new HashMap<String, String>();
		mActivityParams.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		mActivityParams.put(HttpUrlList.HTTP_PAGE_NUM, "20");
		mActivityParams.put(HttpUrlList.HTTP_PAGE_INDEX, "0");
		mActivityParams.put("activityName", mSearch.getText().toString());
		mActivityParams.put("venueName", mSearch.getText().toString());
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.Collect.WFF_USERAPPCOLLECTACT, mActivityParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个收藏活动的" + resultStr);
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							list.clear();
							list = JsonUtil.getCollectList(resultStr);
							if (list.size() == 0) {
								mLoadingHandler.stopLoading();
								mListView.setVisibility(View.GONE);
								collection_null.setVisibility(View.VISIBLE);
								return;
							} else {
								collection_null.setVisibility(View.GONE);
								mListView.setVisibility(View.VISIBLE);
							}
							// mAdapter.setList(list);
							// mAdapter.notifyDataSetChanged();
							mLoadingHandler.stopLoading();
							if (list.size() == 0) {
								tip.setVisibility(View.GONE);
							} else {
								tip.setVisibility(View.GONE);
							}
							handler.sendEmptyMessage(1);
						} else {
							mLoadingHandler.stopLoading();
							handler.sendEmptyMessage(3);
						}

					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mAdapter = new MyCollectAdapter(MyCollectActivity.this, list);
				mListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						if (select_activity_venue == 0) {
							Intent intent = new Intent(mContext,
									ActivityDetailActivity.class);
							intent.putExtra("eventId", list.get(arg2)
									.getActivityId());
							startActivity(intent);
						} else {
							Intent intent = new Intent(mContext,
									VenueDetailActivity.class);
							intent.putExtra("venueId", list.get(arg2)
									.getVenueId());
							startActivity(intent);
						}
					}
				});
				break;
			case 2:
				vuneu_adapter = new MyCollectAdapter(MyCollectActivity.this,
						list_vuneu);
				mListView.setAdapter(vuneu_adapter);
				vuneu_adapter.notifyDataSetChanged();
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						if (select_activity_venue == 0) {
							Intent intent = new Intent(mContext,
									ActivityDetailActivity.class);
							intent.putExtra("eventId", list.get(arg2)
									.getActivityId());
							startActivity(intent);
						} else {
							Intent intent = new Intent(mContext,
									VenueDetailActivity.class);
							intent.putExtra("venueId", list_vuneu.get(arg2)
									.getActivityId());
							startActivity(intent);
						}
					}
				});
				break;
			case 3:
				collection_null.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};

	private void onSearchCollect() {
		if (list != null) {
			list.clear();
		}
		getData();
	}

	/**
	 * 监听回车事件
	 */
	OnKeyListener onKey = new OnKeyListener() {
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
					if (TextUtils.isEmpty(mSearch.getText())) {
						ToastUtil.showToast("请输入关键字");
					} else {
						onSearchCollect();
					}

				}
				return true;
			}
			return false;
		}
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.left_iv:
			finish();
			break;
		case R.id.activity_ll:
			clearll();
			select_activity_venue = 0;
			activity_view.setImageResource(R.color.text_color_5e);
			activity_tv.setTextColor(getResources().getColor(
					R.color.text_color_5e));
			mListView.setVisibility(View.GONE);
			mLoadingHandler.startLoading();
			getData();
			break;
		case R.id.venue_ll:
			clearll();
			select_activity_venue = 1;
			venue_view.setImageResource(R.color.text_color_5e);
			venue_tv.setTextColor(getResources()
					.getColor(R.color.text_color_5e));
			mListView.setVisibility(View.GONE);
			mLoadingHandler.startLoading();
			getVenueData();
			break;
		default:
			break;
		}
	}

	private void clearll() {
		activity_view.setImageResource(R.color.text_color_df);
		venue_view.setImageResource(R.color.text_color_df);
		activity_tv
				.setTextColor(getResources().getColor(R.color.text_color_49));
		venue_tv.setTextColor(getResources().getColor(R.color.text_color_49));
	}

	private void addmenu() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
				// set item background
				deleteItem.setBackground(new ColorDrawable(getResources()
						.getColor(R.color.common_red)));
				// set item width
				deleteItem.setWidth(DensityUtil.dip2px(mContext, 90));
				// set item title
				deleteItem.setTitle("删除");
				// set item title fontsize
				deleteItem.setTitleSize(16);
				// set item title font color
				deleteItem.setTitleColor(mContext.getResources().getColor(
						R.color.white_color));
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		mListView.setMenuCreator(creator);
		// step 2. listener item click event
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {

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

	/**
	 * 删除收藏
	 * 
	 * @param position
	 */
	private void cancelCollect(final int position) {

		// /userCollect/appDelCollectVenue.do
		mLoadingDialog = new LoadingDialog(mContext);
		mLoadingDialog.startDialog("请稍等");
		if (select_activity_venue == 1) {
			CollectUtil.cancelVenue(mContext, list_vuneu.get(position)
					.getActivityId(), new CollectCallback() {

				@Override
				public void onPostExecute(boolean isOK) {
					mLoadingDialog.cancelDialog();
					if (isOK) {
						// list.get(position).setEventIsCollect("0");
						// sendmyBroadcast(list.get(position));

						list_vuneu.remove(position);
						vuneu_adapter.notifyDataSetChanged();

						ToastUtil.showToast("删除成功");

					} else {
						ToastUtil.showToast("操作失败");
					}
				}
			});

		} else {

			if (list.get(position).getCollectType() == 0) {
				CollectUtil.cancelActivity(mContext, list.get(position)
						.getActivityId(), new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOK) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (isOK) {
							// list.get(position).setEventIsCollect("0");
							// sendmyBroadcast(list.get(position));

							list.remove(position);
							mAdapter.notifyDataSetChanged();

							ToastUtil.showToast("删除成功");

						} else {
							ToastUtil.showToast("操作失败");
						}
					}
				});
			} else if (list.get(position).getCollectType() == 1) {
				CollectUtil.cancelVenue(mContext, list.get(position)
						.getVenueId(), new CollectCallback() {

					@Override
					public void onPostExecute(boolean isOK) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (isOK) {
							// list.get(position).setEventIsCollect("0");
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
		}

	}

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub

	}
}
