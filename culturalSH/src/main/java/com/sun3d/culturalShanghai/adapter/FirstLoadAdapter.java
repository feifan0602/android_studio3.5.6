package com.sun3d.culturalShanghai.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;

public class FirstLoadAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserBehaviorInfo> list;
	private boolean isShowText;
	private Map<Integer, ViewHolder> mMap = new HashMap<Integer, ViewHolder>();
	private boolean isAllSelect = false;

	public FirstLoadAdapter(Context mContext, List<UserBehaviorInfo> list,
			boolean isShowText) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.isShowText = isShowText;
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

	public void setList(List<UserBehaviorInfo> list) {
		this.list = list;
	}

	public void isSelect(int position, boolean isSelect) {
		if (isSelect) {
			Log.i("ceshi", "选中了");
			mMap.get(position).mTvAge
					.setBackgroundResource(R.drawable.home_myborder_press);
			mMap.get(position).mTvAge.setTextColor(Color.WHITE);
			mMap.get(position).mIvSelect.setVisibility(View.GONE);
		} else {
			Log.i("ceshi", "没选中了");
			mMap.get(position).mTvAge
					.setBackgroundResource(R.drawable.home_myborder);
			mMap.get(position).mTvAge.setTextColor(mContext.getResources()
					.getColor(R.color.text_color_7c));
			mMap.get(position).mIvSelect.setVisibility(View.GONE);
		}
		if (position == 0) {
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		UserBehaviorInfo info = list.get(arg0);
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_first_load_item,
					null);
			mHolder.mTvAge = (TextView) arg1.findViewById(R.id.first_age);
			mHolder.mTvAge.setTypeface(MyApplication.GetTypeFace());
			mHolder.mIvSelect = (ImageView) arg1
					.findViewById(R.id.first_select);
			mHolder.mIvType = (ImageView) arg1.findViewById(R.id.first_type);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mMap.put(arg0, mHolder);
		if (isAllSelect) {
			// mei选中

			mHolder.mTvAge.setBackgroundResource(R.drawable.home_myborder);
			mHolder.mTvAge.setTextColor(Color.WHITE);

			mHolder.mIvSelect.setVisibility(View.GONE);
		} else {
			// 选中
			mHolder.mTvAge
					.setBackgroundResource(R.drawable.home_myborder_press);
			mHolder.mTvAge.setTextColor(mContext.getResources().getColor(
					R.color.text_color_7c));
			mHolder.mIvSelect.setVisibility(View.GONE);
		}

		if (isShowText) {// 年龄
			mHolder.mIvType.setVisibility(View.GONE);
			mHolder.mTvAge.setVisibility(View.VISIBLE);
			if (info.getAge() == 0) {
				mHolder.mTvAge.setText(info.getAge() + "0后");
			} else {
				mHolder.mTvAge.setText(info.getAge() + "后");
			}
			mHolder.mTvAge.setBackgroundResource(info.getTvBackground());
		} else {// 类型
			mHolder.mTvAge.setVisibility(View.VISIBLE);
			mHolder.mIvType.setVisibility(View.GONE);
			// 这里判断是否选中
			mHolder.mTvAge.setText(info.getTagName());
			if (info.isSelect()) {
				// 选中
				mHolder.mTvAge
						.setBackgroundResource(R.drawable.home_myborder_press);
				mHolder.mTvAge.setTextColor(Color.WHITE);
				mHolder.mIvSelect.setVisibility(View.GONE);
			} else {
				// 没选中
				mHolder.mTvAge.setBackgroundResource(R.drawable.home_myborder);
				mHolder.mTvAge.setTextColor(mContext.getResources().getColor(
						R.color.text_color_7c));
				mHolder.mIvSelect.setVisibility(View.GONE);
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int mWidth = (MyApplication.getWindowWidth() - 100) / 4;
			// params.width = mWidth;
			params.height = mWidth;
			mHolder.mIvType.setLayoutParams(params);
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(info.getTagImageUrl(), mHolder.mIvType,
							Options.getListOptions());

		}
		return arg1;
	}

	private class ViewHolder {
		private TextView mTvAge;
		private ImageView mIvSelect;
		private ImageView mIvType;
	}
}
