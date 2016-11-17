package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.MoreVoteListAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.VoteInfo;

/**
 * 投票列表
 * 
 * @author liningkang
 * 
 */
public class MoreVoteListActivity extends Activity {
	private PullToRefreshListView mListView;
	private String activityId;
	private List<VoteInfo> mList;
	private MoreVoteListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote_list);
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
		getData();
	}

	private void initView() {
		mListView = (PullToRefreshListView) findViewById(R.id.vote_list);
		RelativeLayout mTitleLayout = (RelativeLayout) findViewById(R.id.title);
		TextView mTitle = (TextView) mTitleLayout.findViewById(R.id.title_content);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText("我要投票");
		mTitleLayout.findViewById(R.id.title_right).setVisibility(View.GONE);
		mTitleLayout.findViewById(R.id.title_left).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initData() {
		activityId = getIntent().getExtras().getString("activityId");
		mList = new ArrayList<VoteInfo>();
		adapter = new MoreVoteListAdapter(this, mList);
		mListView.setAdapter(adapter);
	}

	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", activityId);
        params.put("pageIndex", "0");
		params.put("pageNum", "10");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYVOTELIST, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					List<VoteInfo> list = JsonUtil.getVoteList(resultStr);
					if (list != null && list.size() > 0) {
						mList.addAll(list);
						setData();
					}
				}
			}
		});
	}

	private void setData() {
		adapter.setList(mList);
	}
}
