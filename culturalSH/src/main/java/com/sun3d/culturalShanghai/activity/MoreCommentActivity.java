package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.CommetnListAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.CommentInfor;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;
import com.umeng.analytics.MobclickAgent;

public class MoreCommentActivity extends Activity implements OnClickListener {
	private String TAG = "MoreCommentActivity";
	private PullToRefreshListView mListView;
	private String titlestr = "更多点评";
	private CommetnListAdapter mCommetnListAdapter;
	private List<CommentInfor> list;
	private Context mContext;
	private String moldId;
	private String type = "2";
	private LoadingHandler mLoadingHandler;
	private int mPage = 0;
	private TextView mText;
	private RelativeLayout mTitle;

	private final String mPageName = "MoreCommentActivity";
	private boolean isResh = true;

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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_comment);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		moldId = this.getIntent().getExtras().getString("Id");
		type = this.getIntent().getExtras().getString("type");
		init();
	}

	private void init() {
		mPage = 0;
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		mText = (TextView) findViewById(R.id.venue_comment_text);
		mText.setVisibility(View.GONE);
		mListView = (PullToRefreshListView) findViewById(R.id.comment_listview);
		list = new ArrayList<CommentInfor>();
		if ("2".equals(type)) {
			mCommetnListAdapter = new CommetnListAdapter(mContext, list, false);
		} else {
			mCommetnListAdapter = new CommetnListAdapter(mContext, list, true);
		}
		mListView.setAdapter(mCommetnListAdapter);
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
		setTitle();
		onResh();
	}

	private void onResh() {
		isResh = true;
		mPage = 0;
		addData(mPage);
	}

	private void onAddmoreData() {
		isResh = false;
		mPage = HttpUrlList.HTTP_NUM + mPage;
		addData(mPage);
	}

	private void addData(final int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("moldId", moldId);
		params.put("type", type);
		params.put("pageIndex", String.valueOf(page));
		params.put("pageNum", HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Comment.User_CommentList, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				Log.d(TAG, resultStr);
				mListView.onRefreshComplete();
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					List<CommentInfor> nowlist = JsonUtil.getCommentInforFromString(resultStr);
					mText.setVisibility(View.GONE);
					if (nowlist != null && nowlist.size() > 0) {
						if (isResh = true) {
							isResh = false;
							list.clear();
						}
						list.addAll(nowlist);
						mCommetnListAdapter.setList(list);
						LoadingTextShowPopWindow.showLoadingText(mContext, mTitle, "");
					} else {
						LoadingTextShowPopWindow.showLoadingText(mContext, mTitle, "加载结束");
						if (page == 0) {
							mText.setVisibility(View.VISIBLE);
						}
					}
					mLoadingHandler.stopLoading();
				} else {
					mLoadingHandler.showErrorText(resultStr);
				}
			}
		});
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText("查看点评");
		title.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.more_comment, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.list_foot_text:
			mPage = HttpUrlList.HTTP_NUM + mPage;
			addData(mPage);
			break;

		default:
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoadingTextShowPopWindow.dismissPop();
	}
}
