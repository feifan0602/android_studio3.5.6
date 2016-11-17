package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;

public class PersionListViewAdapter extends BaseAdapter implements OnClickListener {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<UserPersionSInfo> TagsList;

	public PersionListViewAdapter(Context context, List<UserPersionSInfo> TagsList) {
		this.mContext = context;
		this.TagsList = TagsList;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setList(List<UserPersionSInfo> TagsList) {
		this.TagsList = TagsList;
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return TagsList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.user_persions_layout, null);
			holder.mImage = (ImageView) convertView.findViewById(R.id.personal_head);
			holder.mName = (TextView) convertView.findViewById(R.id.personal_name);
			holder.mInfor = (TextView) convertView.findViewById(R.id.personal_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UserPersionSInfo mUserPersionSInfo = TagsList.get(position);
		String str = mUserPersionSInfo.getSex() + "  " + mUserPersionSInfo.getBithryDay();
		holder.mInfor.setText(str);
		holder.mName.setText(mUserPersionSInfo.getName());
		if ("男".equals(mUserPersionSInfo.getSex())) {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(mUserPersionSInfo.getHeadUrl(), holder.mImage,
							Options.getRoundOptions(R.drawable.sh_user_sex_man, 10));
		} else {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(mUserPersionSInfo.getHeadUrl(), holder.mImage,
							Options.getRoundOptions(R.drawable.sh_user_sex_woman, 10));
		}
		holder.mImage.setTag(mUserPersionSInfo.getHeadUrl());
		holder.mImage.setOnClickListener(this);
		AbsListView.LayoutParams newlayoutparams = new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mContext, 50));
		convertView.setLayoutParams(newlayoutparams);
		return convertView;
	}

	private static class ViewHolder {
		private TextView mName;
		private ImageView mImage;
		private TextView mInfor;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getTag().toString().length() <= 0){
			ToastUtil.showToast("默认头像无法放大显示！");
			return ;
		}
		Intent intent = new Intent();
		intent.setClass(mContext, ImageOriginalActivity.class);
		intent.putExtra("select_imgs", view.getTag().toString());
		intent.putExtra("id", 0);
		mContext.startActivity(intent);
	}

}