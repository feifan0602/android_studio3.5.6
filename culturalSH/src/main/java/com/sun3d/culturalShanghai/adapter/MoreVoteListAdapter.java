package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.activity.MoreVoteListActivity;
import com.sun3d.culturalShanghai.activity.VoteDetailActivity;
import com.sun3d.culturalShanghai.object.VoteInfo;

public class MoreVoteListAdapter extends BaseAdapter {
	private Context mContext;
	private List<VoteInfo> list;

	public MoreVoteListAdapter(Context mContext, List<VoteInfo> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
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

	public void setList(List<VoteInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_vote_list, null);
			mHolder.mTitle = (TextView) arg1.findViewById(R.id.vote_title);
			mHolder.mUrl = (ImageView) arg1.findViewById(R.id.vote_url);
			mHolder.mLayout = (RelativeLayout) arg1.findViewById(R.id.vote_layout);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		final VoteInfo info = list.get(arg0);
		mHolder.mTitle.setText(info.getVoteTitel());
		MyApplication.getInstance().getImageLoader().displayImage(info.getVoteCoverImgUrl(), mHolder.mUrl, Options.getListOptions());
		mHolder.mLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, VoteDetailActivity.class);
				intent.putExtra("activityId", info.getVoteAddress());
				mContext.startActivity(intent);
			}
		});
		return arg1;
	}

	private class ViewHolder {
		private TextView mTitle;
		private ImageView mUrl;
		private RelativeLayout mLayout;
	}
}
