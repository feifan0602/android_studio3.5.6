package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;
import com.sun3d.culturalShanghai.object.CommentInfor;
import com.sun3d.culturalShanghai.view.PhotoView;

public class CommetnListAdapter extends BaseAdapter implements OnClickListener {
	private Context context;
	private List<CommentInfor> listItem;
	private boolean isShowRatingbar;
	AlphaAnimation in = new AlphaAnimation(0, 1);

	public CommetnListAdapter(Context context, List<CommentInfor> listItem,
			boolean isShowRatingbar) {
		this.context = context;
		this.listItem = listItem;
		this.isShowRatingbar = isShowRatingbar;
		in.setDuration(300);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	public void setList(List<CommentInfor> listItem) {
		this.listItem = listItem;
		notifyDataSetChanged();
	}

	@Override
	public CommentInfor getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItem.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if (convertView == null) {
			// convertView = View.inflate(context,
			// R.layout.commetnlist_itemlayout, null);
			convertView = View.inflate(context,
					R.layout.adapter_comment_list_item, null);
			vh = new ViewHolder();
			vh.name = (TextView) convertView.findViewById(R.id.name);
			vh.content = (TextView) convertView
					.findViewById(R.id.comment_content);
			vh.name.setTypeface(MyApplication.GetTypeFace());
			vh.content.setTypeface(MyApplication.GetTypeFace());
			vh.icon = (ImageView) convertView.findViewById(R.id.comment_head);
			vh.ratingBar = (RatingBar) convertView
					.findViewById(R.id.comment_ratingbar);

			vh.imgCenter = (PhotoView) convertView
					.findViewById(R.id.comment_imgcenter);
			vh.imgRight = (PhotoView) convertView
					.findViewById(R.id.comment_imgright);
			vh.imgLayout = (LinearLayout) convertView
					.findViewById(R.id.comment_img_layout);
			vh.imgLeft = (PhotoView) convertView
					.findViewById(R.id.comment_imgleft);

			vh.imgCenter1 = (PhotoView) convertView
					.findViewById(R.id.comment_imgcenter1);
			vh.imgRight1 = (PhotoView) convertView
					.findViewById(R.id.comment_imgright1);
			vh.imgLayout1 = (LinearLayout) convertView
					.findViewById(R.id.comment_img_layout1);
			vh.imgLeft1 = (PhotoView) convertView
					.findViewById(R.id.comment_imgleft1);

			vh.imgCenter2 = (PhotoView) convertView
					.findViewById(R.id.comment_imgcenter2);
			vh.imgRight2 = (PhotoView) convertView
					.findViewById(R.id.comment_imgright2);
			vh.imgLayout2 = (LinearLayout) convertView
					.findViewById(R.id.comment_img_layout2);
			vh.imgLeft2 = (PhotoView) convertView
					.findViewById(R.id.comment_imgleft2);
			vh.time = (TextView) convertView.findViewById(R.id.time);
			vh.time.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// vh.name.setText(listItem.get(arg0).getCommentRkName());
		vh.content.setText(listItem.get(arg0).getCommentRemark());
		vh.name.setText(listItem.get(arg0).getCommentRkName());
		vh.time.setText(listItem.get(arg0).getCommentTime());
		// vh.time.setText(listItem.get(arg0).getCommentTime());
		if (listItem.get(arg0).getUserHeadImgUrl().length() > 0) {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							listItem.get(arg0).getUserHeadImgUrl(),
							vh.icon,
							Options.getRoundOptions(R.drawable.sh_user_sex_man,
									10));

		} else {
			if (listItem.get(arg0).getCommentUserSex().length() > 0) {
				if (listItem.get(arg0).getCommentUserSex().trim().equals("2")) {
					vh.icon.setImageResource(R.drawable.sh_user_sex_woman);
				} else {
					vh.icon.setImageResource(R.drawable.sh_user_sex_man);
				}
			} else {
				vh.icon.setImageResource(R.drawable.sh_user_sex_man);
			}
			// MyApplication.getInstance().getImageLoader()
			// .displayImage(listItem.get(arg0).getUserHeadImgUrl(), vh.icon,
			// Options.getRoundOptions(R.drawable.sh_user_sex_woman, 10));
		}
		if (isShowRatingbar) {
			vh.ratingBar.setVisibility(View.GONE);
			vh.ratingBar.setRating(listItem.get(arg0).getCommentStar());
		} else {
			vh.ratingBar.setVisibility(View.GONE);
		}
		vh.imgLeft.disenable();
		vh.imgRight.disenable();
		vh.imgCenter.disenable();
		vh.imgLeft1.disenable();
		vh.imgRight1.disenable();
		vh.imgCenter1.disenable();
		vh.imgLeft2.disenable();
		vh.imgRight2.disenable();
		vh.imgCenter2.disenable();
		vh.imgLeft.setId(0);
		vh.imgCenter.setId(1);
		vh.imgRight.setId(2);
		vh.imgLeft1.setId(3);
		vh.imgCenter1.setId(4);
		vh.imgRight1.setId(5);
		vh.imgLeft2.setId(6);
		vh.imgCenter2.setId(7);
		vh.imgRight2.setId(8);
		vh.icon.setId(0);

		vh.icon.setTag(listItem.get(arg0).getUserHeadImgUrl());
		vh.icon.setOnClickListener(this);
		if (listItem.get(arg0).getCommentImgUrl() != null) {
			String[] imgarray = listItem.get(arg0).getCommentImgUrl()
					.split(",");

			if (imgarray.length <= 1) {
				vh.imgLayout.setVisibility(View.GONE);
				vh.imgLayout1.setVisibility(View.GONE);
				vh.imgLayout2.setVisibility(View.GONE);
			} else if (imgarray.length <= 3 && imgarray.length > 0) {
				vh.imgLayout.setVisibility(View.VISIBLE);
				vh.imgLayout1.setVisibility(View.GONE);
				vh.imgLayout2.setVisibility(View.GONE);
			} else if (imgarray.length <= 6 && imgarray.length >= 3) {
				vh.imgLayout.setVisibility(View.VISIBLE);
				vh.imgLayout1.setVisibility(View.VISIBLE);
				vh.imgLayout2.setVisibility(View.GONE);
			} else if (imgarray.length <= 9 && imgarray.length >= 6) {
				vh.imgLayout.setVisibility(View.VISIBLE);
				vh.imgLayout1.setVisibility(View.VISIBLE);
				vh.imgLayout2.setVisibility(View.VISIBLE);
			}

			if (imgarray != null && imgarray.length > 0) {

				switch (imgarray.length) {

				case 1:
					if (imgarray[0].length() == 0) {
						vh.imgLayout.setVisibility(View.GONE);
						vh.imgLayout1.setVisibility(View.GONE);
						vh.imgLayout2.setVisibility(View.GONE);
						break;
					}
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());

					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setImageBitmap(null);
					vh.imgRight.setImageBitmap(null);

					vh.imgLayout.setVisibility(View.VISIBLE);
					break;
				case 2:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setImageBitmap(null);
					vh.imgLayout.setVisibility(View.VISIBLE);
					break;
				case 3:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					break;
				case 4:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[3], vh.imgLeft1,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setOnClickListener(this);
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					vh.imgLayout1.setVisibility(View.VISIBLE);
					break;
				case 5:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[3], vh.imgLeft1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[4], vh.imgCenter1,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setOnClickListener(this);
					vh.imgCenter1.setOnClickListener(this);
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					vh.imgLayout1.setVisibility(View.VISIBLE);
					break;
				case 6:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[3], vh.imgLeft1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[4], vh.imgCenter1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[5], vh.imgRight1,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setOnClickListener(this);
					vh.imgCenter1.setOnClickListener(this);
					vh.imgRight1.setOnClickListener(this);
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					vh.imgLayout1.setVisibility(View.VISIBLE);
					break;
				case 7:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[3], vh.imgLeft1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[4], vh.imgCenter1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[5], vh.imgRight1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[6], vh.imgLeft2,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft2.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft2.setOnClickListener(this);
					vh.imgLeft1.setOnClickListener(this);
					vh.imgCenter1.setOnClickListener(this);
					vh.imgRight1.setOnClickListener(this);
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					vh.imgLayout1.setVisibility(View.VISIBLE);
					vh.imgLayout2.setVisibility(View.VISIBLE);
					break;
				case 8:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[3], vh.imgLeft1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[4], vh.imgCenter1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[5], vh.imgRight1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[6], vh.imgLeft2,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[7], vh.imgCenter2,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft2.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter2.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter2.setOnClickListener(this);
					vh.imgLeft2.setOnClickListener(this);
					vh.imgLeft1.setOnClickListener(this);
					vh.imgCenter1.setOnClickListener(this);
					vh.imgRight1.setOnClickListener(this);
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					vh.imgLayout1.setVisibility(View.VISIBLE);
					vh.imgLayout2.setVisibility(View.VISIBLE);
					break;
				case 9:
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[0], vh.imgLeft,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[1], vh.imgCenter,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[2], vh.imgRight,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[3], vh.imgLeft1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[4], vh.imgCenter1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[5], vh.imgRight1,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[6], vh.imgLeft2,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[7], vh.imgCenter2,
									Options.getListOptions());
					MyApplication
							.getInstance()
							.getImageLoader()
							.displayImage(imgarray[8], vh.imgRight2,
									Options.getListOptions());
					vh.imgLeft.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight1.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgLeft2.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgCenter2.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight2.setTag(listItem.get(arg0).getCommentImgUrl());
					vh.imgRight2.setOnClickListener(this);
					vh.imgCenter2.setOnClickListener(this);
					vh.imgLeft2.setOnClickListener(this);
					vh.imgLeft1.setOnClickListener(this);
					vh.imgCenter1.setOnClickListener(this);
					vh.imgRight1.setOnClickListener(this);
					vh.imgLeft.setOnClickListener(this);
					vh.imgCenter.setOnClickListener(this);
					vh.imgRight.setOnClickListener(this);
					vh.imgLayout.setVisibility(View.VISIBLE);
					vh.imgLayout1.setVisibility(View.VISIBLE);
					vh.imgLayout2.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			} else {
				Log.d("resultStr", "GONE2");
				vh.imgLayout.setVisibility(View.GONE);
			}
		} else {
			Log.d("resultStr", "GONE1");
			vh.imgLayout.setVisibility(View.GONE);
		}
		return convertView;

	}

	class ViewHolder {
		private TextView name;
		private TextView content;
		private TextView time;
		private RatingBar ratingBar;
		private ImageView icon;
		private PhotoView imgLeft;
		private PhotoView imgCenter;
		private PhotoView imgRight;
		private LinearLayout imgLayout;
		private PhotoView imgLeft1;
		private PhotoView imgCenter1;
		private PhotoView imgRight1;
		private LinearLayout imgLayout1;
		private PhotoView imgLeft2;
		private PhotoView imgCenter2;
		private PhotoView imgRight2;
		private LinearLayout imgLayout2;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getTag().toString().length() <= 0) {
			ToastUtil.showToast("默认头像无法放大显示！");
			return;
		}
		Intent intent = new Intent();
		intent.setClass(context, ImageOriginalActivity.class);
		intent.putExtra("select_imgs", arg0.getTag().toString());
		intent.putExtra("id", arg0.getId());
		context.startActivity(intent);

	}

}
