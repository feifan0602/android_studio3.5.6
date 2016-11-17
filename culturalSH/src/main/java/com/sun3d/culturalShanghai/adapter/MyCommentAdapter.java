package com.sun3d.culturalShanghai.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.PicImgCompressUtil;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.MyCommentActivity;
import com.sun3d.culturalShanghai.image.ImageLoader;
import com.sun3d.culturalShanghai.image.ImageLoader.Type;
import com.sun3d.culturalShanghai.object.CommentImgeInfo;
import com.sun3d.culturalShanghai.object.MyCollectInfo;
import com.sun3d.culturalShanghai.object.MyCommentBean;

public class MyCommentAdapter extends BaseAdapter {
	private MyCommentActivity mContext;
	private ArrayList<MyCommentBean> list;
	private ArrayList<String> imglist = new ArrayList<String>();

	public MyCommentAdapter(MyCommentActivity mContext,
			ArrayList<MyCommentBean> list) {
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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder mHolder = null;
		if (arg1 == null) {
			arg1 = View.inflate(mContext, R.layout.activity_my_comment_adapter,
					null);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) arg1.findViewById(R.id.name);
			mHolder.address_ll = (LinearLayout) arg1
					.findViewById(R.id.address_ll);
			mHolder.address = (TextView) arg1.findViewById(R.id.address);
			mHolder.time = (TextView) arg1.findViewById(R.id.time);
			mHolder.comment = (TextView) arg1.findViewById(R.id.comment);
			mHolder.address.setTypeface(MyApplication.GetTypeFace());
			mHolder.time.setTypeface(MyApplication.GetTypeFace());
			mHolder.name.setTypeface(MyApplication.GetTypeFace());
			mHolder.comment.setTypeface(MyApplication.GetTypeFace());
			mHolder.gridview = (GridView) arg1.findViewById(R.id.gridview);
			mHolder.gridview.setNumColumns(3);
			arg1.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) arg1.getTag();
		}
		if (list.get(arg0).getActivityName() == "") {
			mHolder.name.setVisibility(View.GONE);
		} else {
			mHolder.name.setVisibility(View.VISIBLE);
		}
		if (mContext.venue_num == 1) {
			// 这个是活动
			mHolder.address_ll.setVisibility(View.VISIBLE);
			mHolder.address.setVisibility(View.VISIBLE);
			mHolder.name.setText(list.get(arg0).getActivityName());
		} else {
			// 这个是场所
			mHolder.address_ll.setVisibility(View.GONE);
			mHolder.address.setVisibility(View.GONE);
			mHolder.name.setVisibility(View.VISIBLE);
			mHolder.name.setText(list.get(arg0).getVenueName());
		}

		// if (list.get(arg0).getVenueName() == "") {
		// mHolder.address.setVisibility(View.GONE);
		// } else {
		// mHolder.address.setVisibility(View.VISIBLE);
		// }
		if (list.get(arg0).getCommentTime() == "") {
			mHolder.time.setVisibility(View.GONE);
		} else {
			mHolder.time.setVisibility(View.VISIBLE);
		}
		if (list.get(arg0).getCommentRemark() == "") {
			mHolder.comment.setVisibility(View.GONE);
		} else {
			mHolder.comment.setVisibility(View.VISIBLE);
		}
		String[] img = list.get(arg0).getCommentImgUrl().split(",");
		if (imglist.size() != 0) {
			imglist.clear();
		}
		for (int i = 0; i < img.length; i++) {
			imglist.add(img[i]);
		}
		if (imglist.size() == 0) {
			mHolder.gridview.setVisibility(View.GONE);
		} else {
			GridViewAdapter ga = new GridViewAdapter(imglist);
			mHolder.gridview.setAdapter(ga);
			MyApplication.setGridViewHeightBasedOnChildrenTwo(mHolder.gridview);
		}

		mHolder.address.setText(list.get(arg0).getVenueName());
		mHolder.time.setText(list.get(arg0).getCommentTime());
		mHolder.comment.setText(list.get(arg0).getCommentRemark());
		// MyApplication.getInstance().setImageView(
		// MyApplication.getContext(),
		// TextUtil.getUrlMiddle(list.get(arg0).getCommentImgUrl()),
		// mHolder.img);
		return arg1;
	}

	class ViewHolder {
		TextView name;
		TextView address;
		TextView time;
		TextView comment;
		GridView gridview;
		LinearLayout address_ll;
	}

	class GridViewAdapter extends BaseAdapter {
		private ArrayList<String> imglist;

		public GridViewAdapter(ArrayList<String> list) {
			this.imglist = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imglist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.comment_imgitem, null);
			ImageView img = (ImageView) arg1.findViewById(R.id.imageView1);
			String img_url = imglist.get(arg0);
			MyApplication.getInstance().getImageLoader()
					.displayImage(TextUtil.getUrlSmall_150_150(img_url), img);
			return arg1;
		}
	}
}
