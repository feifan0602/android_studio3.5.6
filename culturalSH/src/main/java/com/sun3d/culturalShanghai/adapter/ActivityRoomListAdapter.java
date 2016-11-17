package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityRoomDateilsActivity;
import com.sun3d.culturalShanghai.handler.LabelHandler;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;

public class ActivityRoomListAdapter extends BaseAdapter {

	private Context context;
	private List<ActivityRoomInfo> listItem;

	public ActivityRoomListAdapter(Context context,
			List<ActivityRoomInfo> listItem) {
		this.context = context;
		this.listItem = listItem;
	}

	public void setData(List<ActivityRoomInfo> listItem) {
		this.listItem = listItem;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItem.size();
	}

	@Override
	public ActivityRoomInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItem.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public void setList(List<ActivityRoomInfo> listItem) {
		this.listItem = listItem;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(context,
					R.layout.activityroomlist_layout, null);
			vh = new ViewHolder();
			vh.img = (ImageView) convertView
					.findViewById(R.id.activityroom_img);
			vh.mTitle = (TextView) convertView
					.findViewById(R.id.activityroom_name);
			vh.mArea = (TextView) convertView
					.findViewById(R.id.activityroom_area);
			vh.mIsfree = (TextView) convertView
					.findViewById(R.id.activityroom_isfree);
			LinearLayout lli = (LinearLayout) convertView
					.findViewById(R.id.label);
			vh.reserve = (TextView) convertView
					.findViewById(R.id.activityroom_reserve);
			vh.mTitle.setTypeface(MyApplication.GetTypeFace());
			vh.mArea.setTypeface(MyApplication.GetTypeFace());
			vh.mIsfree.setTypeface(MyApplication.GetTypeFace());
			vh.reserve.setTypeface(MyApplication.GetTypeFace());
			vh.mLabelHandler = new LabelHandler(context, lli, 1, true);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(listItem.get(arg0).getRoomName())) {
			if (listItem.get(arg0).getSysNo() == 0) {// 文化云平台
				if (listItem.get(arg0).getVenueIsReserve()) {
					vh.reserve.setVisibility(View.VISIBLE);
				} else {
					vh.reserve.setVisibility(View.GONE);
				}
			} else {
				vh.reserve.setVisibility(View.GONE);
			}
			vh.mTitle.setText(listItem.get(arg0).getRoomName());// +
			vh.mArea.setText("面积:" + listItem.get(arg0).getRoomArea() + "平方,"
					+ "容纳:" + listItem.get(arg0).getRoomCapacity() + "人");
			if (listItem.get(arg0).getRoomIsFree() == 2) {
				vh.mIsfree.setText("收费");
			} else {
				vh.mIsfree.setText("免费");
			}

			// vh.mLabelHandler.setLabelsData(listItem.get(arg0).getRoomTag());
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlSmall(listItem.get(arg0)
									.getRoomPicUrl()), vh.img,
							Options.getListOptions());
			vh.mLabelHandler.getScrollViewGridView().setOnTouchListener(
					new OnTouchListener() {
						private float index_X, index_Y;

						@Override
						public boolean onTouch(View view, MotionEvent arg1) {
							// TODO Auto-generated method stub

							switch (arg1.getAction()) {
							case MotionEvent.ACTION_DOWN:
								index_X = arg1.getRawX();
								index_Y = arg1.getRawY();
								break;
							case MotionEvent.ACTION_UP:
								if (Math.abs(arg1.getRawY() - index_Y) < 20) {
									if (Math.abs(arg1.getRawX() - index_X) < 20) {
										Intent intent = new Intent(
												context,
												ActivityRoomDateilsActivity.class);
										intent.putExtra("Id", listItem
												.get(arg0).getRoomId());
										context.startActivity(intent);
									}
								}
								break;

							default:
								break;
							}
							return false;
						}
					});
		}

		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView mTitle;
		TextView mArea;
		TextView mIsfree;
		TextView reserve;
		LabelHandler mLabelHandler;
	}

}
