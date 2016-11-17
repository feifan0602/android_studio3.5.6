package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.MyCollectActivity;
import com.sun3d.culturalShanghai.activity.MyCommentActivity;
import com.sun3d.culturalShanghai.object.MyCollectInfo;

public class MyCollectAdapter extends BaseAdapter {
	private MyCollectActivity mContext;
	private List<MyCollectInfo> list;

	public MyCollectAdapter(MyCollectActivity mContext, List<MyCollectInfo> list) {
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

	public void setList(List<MyCollectInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.fragment_collect_item, null);
			mHolder = new ViewHolder();
			mHolder.mTitle = (TextView) arg1.findViewById(R.id.collect_title);
			mHolder.mLocation = (TextView) arg1
					.findViewById(R.id.collect_locaiton);
			mHolder.mDate = (TextView) arg1.findViewById(R.id.collect_data);
			mHolder.mTitle.setTypeface(MyApplication.GetTypeFace());
			mHolder.mLocation.setTypeface(MyApplication.GetTypeFace());
			mHolder.mDate.setTypeface(MyApplication.GetTypeFace());
			mHolder.mIv = (ImageView) arg1.findViewById(R.id.collect_iv);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}

		MyCollectInfo collectgInfo = list.get(arg0);

		// if () {
		mHolder.mLocation.setVisibility(View.VISIBLE);
		mHolder.mTitle.setText(collectgInfo.getActivityName());
		if (mContext.select_activity_venue == 0) {
			if (collectgInfo.getVenueName() == "") {
				mHolder.mLocation.setVisibility(View.GONE);
			} else {
				mHolder.mLocation.setVisibility(View.VISIBLE);
				mHolder.mLocation.setText(collectgInfo.getVenueName());
			}
		} else {
			if (collectgInfo.getActivityAddress() == "") {
				mHolder.mLocation.setVisibility(View.GONE);
			} else {
				mHolder.mLocation.setVisibility(View.VISIBLE);
				mHolder.mLocation.setText(collectgInfo.getActivityAddress());
			}
		}

		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(collectgInfo.getActivityIconUrl(), mHolder.mIv,
						Options.getListOptions());
		mHolder.mDate.setVisibility(View.VISIBLE);
		// if(collectgInfo.getActivityStartTime()==null){
		// mHolder.mDate.setText("：" + collectgInfo.getActivityStartTime() +
		// "至" + collectgInfo.getActivityEndTime());
		// }else {
		// mHolder.mDate.setText("时间：" + collectgInfo.getActivityStartTime()
		// + "至" + collectgInfo.getActivityEndTime());
		// }
		if (collectgInfo.getActivityStartTime() == "") {
			mHolder.mDate.setVisibility(View.INVISIBLE);
		} else {
			String start_time = collectgInfo.getActivityStartTime().replace(
					"-", ".");
			String end_time = collectgInfo.getActivityEndTime().replace("-",
					".");
			start_time = start_time.substring(5, 10);
			if (end_time != null && end_time.length() != 0) {
				end_time = end_time.substring(5, 10);
				mHolder.mDate.setText(start_time + "--" + end_time);
			} else {
				mHolder.mDate.setText(start_time);
			}

		}

		// }
		// else {
		// mHolder.mTitle.setText(collectgInfo.getVenueName());
		// mHolder.mLocation.setVisibility(View.VISIBLE);
		// mHolder.mDate.setVisibility(View.INVISIBLE);
		// MyApplication
		// .getInstance()
		// .getImageLoader()
		// .displayImage(collectgInfo.getVenueIconUrl(), mHolder.mIv,
		// Options.getListOptions());
		// // mHolder.mDate.setText("时间：" + collectgInfo.getActivityStartTime()
		// // + "至" + collectgInfo.getActivityEndTime());
		// if (collectgInfo.getActivityAddress() != null) {
		// mHolder.mLocation.setText(collectgInfo.getActivityAddress());
		// } else {
		// mHolder.mLocation.setVisibility(View.INVISIBLE);
		// }
		//
		// }

		return arg1;
	}

	class ViewHolder {
		private TextView mTitle;
		private TextView mLocation;
		private TextView mDate;
		private ImageView mIv;
	}
}
