package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.object.ActivityOtherInfo;

public class Activity_Other_Adapter extends BaseAdapter {
	private Context mContext;
	private List<ActivityOtherInfo> list;
	private String TAG = "Activity_Other_Adapter";

	public Activity_Other_Adapter(Context mContext, List<ActivityOtherInfo> list) {
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

	public void setList(List<ActivityOtherInfo> list) {
		this.list = list;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			mHolder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_activity_other_item,
					null);
			mHolder.img = (ImageView) arg1.findViewById(R.id.img);
			mHolder.tv = (TextView) arg1.findViewById(R.id.tv);
			mHolder.top_left_tv = (TextView) arg1
					.findViewById(R.id.top_left_tv);
			mHolder.tv.setTypeface(MyApplication.GetTypeFace());
			mHolder.top_left_tv.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		ActivityOtherInfo lookInfo = list.get(arg0);
		LayoutParams para;
		para = (LayoutParams) mHolder.img.getLayoutParams();
		// 设置
		para.width = MyApplication.getWindowWidth() / 2;
		para.height = (para.width * 2) / 3;
		mHolder.img.setLayoutParams(para);

		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlSmall(MyApplication.Img_Path
								+ lookInfo.getActivityIconUrl()), mHolder.img);
		mHolder.tv.setText(lookInfo.getActivityName());
		if (!lookInfo.getTagName().equals("")
				&& !lookInfo.getTagName().equals("null")) {
			mHolder.top_left_tv.setText(lookInfo.getTagName());
		} else {
			mHolder.top_left_tv.setVisibility(View.GONE);
		}

		return arg1;
	}

	class ViewHolder {
		TextView top_left_tv;
		TextView tv;
		ImageView img;
	}
}
