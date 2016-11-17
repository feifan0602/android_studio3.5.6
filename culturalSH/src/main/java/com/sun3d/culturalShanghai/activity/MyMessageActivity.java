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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.MyMessageAdapter;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.MessageInfo;
import com.umeng.analytics.MobclickAgent;

public class MyMessageActivity extends Activity implements OnClickListener,OnItemClickListener{
	private Context mContext;
	private RelativeLayout mTitle;
	private SwipeMenuListView mListView;
	private MyMessageAdapter mAdapter;
	private List<MessageInfo> list;
	private LoadingHandler mLoadingHandler;
	private LoadingDialog mLoadingDialog;

	private final String mPageName = "MyMessageActivity";

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_message);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
	}

	private void initView() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setTypeface(MyApplication.GetTypeFace());
		title.setVisibility(View.VISIBLE);
		title.setText(mContext.getResources().getString(R.string.message_title));
		mListView = (SwipeMenuListView) findViewById(R.id.listView);

		mListView.setOnItemClickListener(this);
	}

	private void initData() {
		list = new ArrayList<MessageInfo>();
		mAdapter = new MyMessageAdapter(mContext, list);
		mListView.setAdapter(mAdapter);
		addmenu();
		getData();
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.Message.MY_MESSAGE_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						list = JsonUtil.getMessageList(resultStr);
						if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
							if (list.size() == 0) {
								mLoadingHandler.showErrorText("您没有新的消息！");
							} else {
								mAdapter.setList(list);
								mAdapter.notifyDataSetChanged();
								mLoadingHandler.stopLoading();
							}

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
	private void deleteMessage(final int position) {
		mLoadingDialog = new LoadingDialog(mContext);
		mLoadingDialog.startDialog("请稍等");
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMessageId", list.get(position).getUserMessageId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.Message.MY_DELETEMESSAGE_URL, params,
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_left:
			onReturn();
			break;

		default:
			break;
		}
	}

	/**
	 * 返回
	 */
	private void onReturn() {
		Intent intent = new Intent(mContext, PersonalCenterActivity.class);
		setResult(AppConfigUtil.PERSONAL_RESULT_MESSGE_CODE, intent);
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			onReturn();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void addmenu() {
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
				// set item background
				deleteItem.setBackground(new ColorDrawable(getResources().getColor(
						R.color.common_red)));
				// set item width
				deleteItem.setWidth(DensityUtil.dip2px(mContext, 90));
				// set item title
				deleteItem.setTitle("删除");
				// set item title fontsize
				deleteItem.setTitleSize(16);
				// set item title font color
				deleteItem.setTitleColor(mContext.getResources().getColor(R.color.white_color));
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
					deleteMessage(position);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,MyMessageDetailActivity.class);
		intent.putExtra("title",list.get(position).getMessageType());
		intent.putExtra("message",list.get(position).getMessageContent());
		startActivity(intent);
	}
}
