package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.MoreVoteListActivity;
import com.sun3d.culturalShanghai.activity.VoteDetailActivity;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.UserVoteInfo;

/**
 * 活动投票
 * 
 * @author yangyoutao
 * 
 */
public class ActivityDetail_Vote {
	private LinearLayout content;
	private Context mContext;
	private ListView mListView;
	private List<UserVoteInfo> list;
	private MyAdapter mAdapter;
	private String activityId;
	private ImageView mMoreVote;
	private RelativeLayout mLayout;

	public LinearLayout getContent() {
		return content;
	}

	public ActivityDetail_Vote(Context context, String activityId) {
		this.mContext = context;
		this.activityId = activityId;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_detati_votelayout, null);
		mListView = (ListView) content.findViewById(R.id.list_view);
		mMoreVote = (ImageView) content.findViewById(R.id.vote_more);
		mLayout = (RelativeLayout) content.findViewById(R.id.vote_layout);
		initData();
		getData();
	}

	private void initData() {
		list = new ArrayList<UserVoteInfo>();
		mAdapter = new MyAdapter(list);
		mListView.setAdapter(mAdapter);
		mMoreVote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MoreVoteListActivity.class);
				intent.putExtra("activityId", activityId);
				mContext.startActivity(intent);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MoreVoteListActivity.class);
				intent.putExtra("activityId", activityId);
				mContext.startActivity(intent);
			}
		});
	}

	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("activityId", activityId);
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYDETAILVOTE,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil.getUserVoteList(resultStr);
							setData();
						} else {
//							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	private void setData() {
		mAdapter.setList(list);
		mAdapter.notifyDataSetChanged();
		if (list != null && list.size() > 0) {
			if (list.get(0).getVoteNum() > 0) {
				mLayout.setVisibility(View.VISIBLE);
				mMoreVote.setVisibility(View.VISIBLE);
			} else {
				mLayout.setVisibility(View.GONE);
				mMoreVote.setVisibility(View.GONE);
			}
		} else {
			mLayout.setVisibility(View.GONE);
		}
	}

	private class MyAdapter extends BaseAdapter {
		private List<UserVoteInfo> list;

		public MyAdapter(List<UserVoteInfo> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public void setList(List<UserVoteInfo> list) {
			this.list = list;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = View.inflate(mContext,
					R.layout.adapter_activity_detail_vote, null);
			ImageView vote_show_img = (ImageView) view
					.findViewById(R.id.vote_show_img);
			TextView vote_num = (TextView) view.findViewById(R.id.vote_num);
			TextView textView4 = (TextView) view.findViewById(R.id.textView4);
			UserVoteInfo info = list.get(arg0);
			String coverImgUrl = info.getVoteCoverImgUrl();
			String voteCoverImgUrl = coverImgUrl.substring(0,
					coverImgUrl.length() - 4)
					+ "_300_300"
					+ coverImgUrl.substring(coverImgUrl.length() - 4);
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(voteCoverImgUrl, vote_show_img,
							Options.getListOptions());
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlSmall(info.getVoteCoverImgUrl()),
							vote_show_img, Options.getListOptions());
			vote_num.setText(info.getVoteCount());
			textView4.setText(info.getVoteContent());
			view.findViewById(R.id.textView1_ll).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(mContext,
									VoteDetailActivity.class);
							if (list.get(0) != null) {
								intent.putExtra("activityId", list.get(0)
										.getVoteAddress());
								mContext.startActivity(intent);
							}
						}
					});
			return view;
		}

	}
}
