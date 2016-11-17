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
import com.sun3d.culturalShanghai.object.CollectionInfor;

public class CollectionGridAdapter extends BaseAdapter {
	private Context mContext;
	private List<CollectionInfor> list;

	public CollectionGridAdapter(Context mContext, List<CollectionInfor> list) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void setList(List<CollectionInfor> list) {
		this.list = list;
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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.collectiongridview_itemlayout, null);
			mHolder = new ViewHolder();
			mHolder.mcollection_img = (ImageView) arg1.findViewById(R.id.colltection_item_img);
			mHolder.mName = (TextView) arg1.findViewById(R.id.colltection_item_name);
			mHolder.mTime = (TextView) arg1.findViewById(R.id.colltection_item_time);
			mHolder.mName.setTypeface(MyApplication.GetTypeFace());
			mHolder.mTime.setTypeface(MyApplication.GetTypeFace());
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		CollectionInfor info = list.get(arg0);
		mHolder.mName.setText(info.getCollectionName());
		// mHolder.mTime.setText(info.getCollectionTime());
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(TextUtil.getUrlSmall(info.getCollectionImgUrl()), mHolder.mcollection_img,
						Options.getListOptions());
		return arg1;
	}

	class ViewHolder {
		ImageView mcollection_img;
		TextView mName;

		TextView mTime;
	}
}
