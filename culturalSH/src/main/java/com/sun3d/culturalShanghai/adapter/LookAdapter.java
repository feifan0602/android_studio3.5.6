package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.object.LookInfo;

public class LookAdapter extends BaseAdapter {
	private Context mContext;
	private List<LookInfo> list;

	public LookAdapter(Context mContext, List<LookInfo> list) {
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

	public void setList(List<LookInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_look_item, null);
			mHolder.mNumber = (TextView) arg1.findViewById(R.id.look_number);
			mHolder.mContent = (TextView) arg1.findViewById(R.id.look_content);
			mHolder.mNum = (TextView) arg1.findViewById(R.id.look_num);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		LookInfo lookInfo = list.get(arg0);
		mHolder.mNumber.setText((arg0 + 1) + "");
		mHolder.mContent.setText(lookInfo.getTagName());
		mHolder.mNum.setText(lookInfo.getActivityNums()+"");
		
		if (arg0 < 3) {
			mHolder.mNumber.setTextColor(mContext.getResources().getColor(R.color.red_color));
		} else {
			mHolder.mNumber.setTextColor(mContext.getResources().getColor(R.color.weather_text_color));
		}

		return arg1;
	}

	class ViewHolder {
		private TextView mNumber;
		private TextView mContent;
		private TextView mNum;

	}
}
