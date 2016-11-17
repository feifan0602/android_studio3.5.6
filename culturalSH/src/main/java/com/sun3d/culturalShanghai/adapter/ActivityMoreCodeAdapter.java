package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.handler.CodeInfo;
import com.sun3d.culturalShanghai.object.KillInfo;
import com.sun3d.culturalShanghai.object.MessageInfo;
import com.sun3d.culturalShanghai.object.MoreCodeInfo;

public class ActivityMoreCodeAdapter extends BaseAdapter {
	private Context mContext;
	private List<MoreCodeInfo> list;

	public ActivityMoreCodeAdapter(Context mContext, List<MoreCodeInfo> list) {
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

	public void setList(List<MoreCodeInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.activity_more_code_item,
					null);
			mHolder = new ViewHolder();
			mHolder.m_Name = (TextView) arg1.findViewById(R.id.mName);
			mHolder.m_Description = (TextView) arg1
					.findViewById(R.id.mDescription);
			mHolder.m_IntegralChange = (TextView) arg1
					.findViewById(R.id.mIntegralChange);
			mHolder.m_Date = (TextView) arg1.findViewById(R.id.mDate);
			mHolder.m_Name.setTypeface(MyApplication.GetTypeFace());
			mHolder.m_Description.setTypeface(MyApplication.GetTypeFace());
			mHolder.m_IntegralChange.setTypeface(MyApplication.GetTypeFace());
			mHolder.m_Date.setTypeface(MyApplication.GetTypeFace());

			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		mHolder.m_Name.setText(list.get(arg0).getName());
		mHolder.m_Description.setText(list.get(arg0).getDescription());
		String Sign;
		if (list.get(arg0).getChangeType() == 1) {
			// 扣除
			Sign = "-";
			mHolder.m_IntegralChange.setTextColor(mContext.getResources()
					.getColor(R.color.text_color_000));
		} else {
			Sign = "+";
			mHolder.m_IntegralChange.setTextColor(mContext.getResources()
					.getColor(R.color.text_color_d581));
		}
		mHolder.m_IntegralChange.setText(Sign
				+ list.get(arg0).getIntegralChange() + "");
		mHolder.m_Date.setText(list.get(arg0).getDate());
		return arg1;
	}

	class ViewHolder {
		TextView m_Name, m_Description, m_IntegralChange, m_Date;
	}
}
