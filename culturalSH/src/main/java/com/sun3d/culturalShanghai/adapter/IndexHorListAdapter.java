package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;

public class IndexHorListAdapter extends BaseAdapter {
	private Context mContext;
	private List<UserBehaviorInfo> mList;

	public IndexHorListAdapter(Context mContext, List<UserBehaviorInfo> mList) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setIsShow(int index) {
		mList.get(index).setShowVal(false);
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_index_hor_list_item,
					null);
			mHolder.mTabName = (RadioButton) arg1.findViewById(R.id.tab_name);
			mHolder.mTabName.setTypeface(MyApplication.GetTypeFace());
			mHolder.mTabIv = (ImageView) arg1.findViewById(R.id.tab_iv);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		UserBehaviorInfo info = mList.get(arg0);
		mHolder.mTabName.setText(info.getTagName());
		if (info.isIndex()) {
			// 选中时候
			mHolder.mTabName.setChecked(true);
			mHolder.mTabName.setTextColor(Color.parseColor("#5E6D98"));
			mHolder.mTabName.setTextSize(17f);
		} else {
			// 未选中时候
			mHolder.mTabName.setChecked(false);
			mHolder.mTabName.setTextColor(Color.parseColor("#494D5B"));
			mHolder.mTabName.setTextSize(16f);
		}
		if (MyApplication.loginUserInfor != null
				&& MyApplication.loginUserInfor.getUserId() != null
				&& !"".equals(MyApplication.loginUserInfor.getUserId())) {
			if (info.isShowVal()) {
				mHolder.mTabIv.setVisibility(View.GONE);
			} else {
				mHolder.mTabIv.setVisibility(View.GONE);
			}
			if (arg0 == 0) {
				mHolder.mTabIv.setVisibility(View.GONE);
			}
		} else {
			mHolder.mTabIv.setVisibility(View.GONE);
		}
		return arg1;
	}

	private class ViewHolder {
		private RadioButton mTabName;
		private ImageView mTabIv;
	}
}
