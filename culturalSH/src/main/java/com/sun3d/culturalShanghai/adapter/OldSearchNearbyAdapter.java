package com.sun3d.culturalShanghai.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.CollectUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.callback.CollectCallback;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;

public class OldSearchNearbyAdapter extends BaseAdapter {
	private Map<Integer, View> m = new HashMap<Integer, View>();
	private Context context;
	private List<EventInfo> listItem;
	private List<VenueDetailInfor> VenuelistItem;
	private int type = 1;

	public OldSearchNearbyAdapter(Context context, List<EventInfo> listItem,
			List<VenueDetailInfor> mVenuelistItem) {
		this.context = context;
		this.listItem = listItem;
		this.VenuelistItem = mVenuelistItem;
		if (listItem != null) {
			type = 1;
		} else {
			type = 2;
		}
	}

	public void setEventInfoList(List<EventInfo> listItem) {
		this.listItem = listItem;
		this.notifyDataSetChanged();
	}

	public void setVenueDetailInforList(List<VenueDetailInfor> mVenuelistItem) {
		this.VenuelistItem = mVenuelistItem;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (type == 1) {
			return listItem.size();
		} else {
			return VenuelistItem.size();
		}

	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (type == 1) {
			return listItem.get(arg0);
		} else {
			return VenuelistItem.get(arg0);
		}
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int item, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ViewHolder vh = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.search_nearby_listview_item, null);
			vh = new ViewHolder();
			vh.mId = (TextView) convertView.findViewById(R.id.nearby_listView_tv);
			vh.mName = (TextView) convertView.findViewById(R.id.nearby_listview_title);
			vh.mPlace = (TextView) convertView.findViewById(R.id.nearby_listview_Place);
			vh.mSpaceing = (TextView) convertView.findViewById(R.id.nearby_spaceing);
			vh.mcontent = (RelativeLayout) convertView.findViewById(R.id.nearby_content);
			vh.mSelected = (ImageView) convertView.findViewById(R.id.nearby_listView_iv);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (this.listItem != null) {
			final EventInfo eventInfo = listItem.get(item);
			if (!eventInfo.getIsGroup()) {
				vh.mSpaceing.setVisibility(View.GONE);
				vh.mcontent.setVisibility(View.VISIBLE);
				vh.mId.setText(String.valueOf(eventInfo.getTagId()));
				vh.mName.setText(eventInfo.getEventName());
				vh.mPlace.setText(eventInfo.getEventAddress());
				// 初始化收藏按钮，1表示收藏，0表示未收藏
				if (eventInfo.getEventIsCollect().equals("0")) {
					vh.mSelected.setImageResource(R.drawable.sh_icon_collect_befor);
					vh.mSelected.setTag("false");
				} else {
					vh.mSelected.setImageResource(R.drawable.sh_icon_collect_after);
					vh.mSelected.setTag("true");
				}
				final ViewHolder mHolder = vh;
				vh.mSelected.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						setBg(mHolder.mSelected, eventInfo);
					}
				});
			} else {
				vh.mSpaceing.setVisibility(View.VISIBLE);
				vh.mcontent.setVisibility(View.GONE);
				vh.mSpaceing.setText(eventInfo.getDistance() + "Km");
			}
		} else if (this.VenuelistItem != null) {
			final VenueDetailInfor venueInfo = VenuelistItem.get(item);
			if (!venueInfo.getIsGroup()) {
				vh.mSpaceing.setVisibility(View.GONE);
				vh.mcontent.setVisibility(View.VISIBLE);
				vh.mId.setText(String.valueOf(venueInfo.getTagId()));
				vh.mName.setText(venueInfo.getVenueName());
				vh.mPlace.setText(venueInfo.getVenueAddress());
				// 初始化收藏按钮，1表示收藏，0表示未收藏
				if (!venueInfo.getVenueIsCollect()) {
					vh.mSelected.setImageResource(R.drawable.sh_icon_collect_befor);
					vh.mSelected.setTag("false");
				} else {
					vh.mSelected.setImageResource(R.drawable.sh_icon_collect_after);
					vh.mSelected.setTag("true");
				}
				final ViewHolder mHolder = vh;
				vh.mSelected.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						setVenueBg(mHolder.mSelected, venueInfo);
					}
				});
			} else {
				vh.mSpaceing.setVisibility(View.VISIBLE);
				vh.mcontent.setVisibility(View.GONE);
				vh.mSpaceing.setText(venueInfo.getDistance() + "Km");
			}
		}
		return convertView;

	}

	private void setBg(final ImageView mImageButton, final EventInfo eventInfo) {
		if (mImageButton.getTag().equals("false")) {// 点击收藏
			CollectUtil.addActivity(context, eventInfo.getEventId(), new CollectCallback() {
				@Override
				public void onPostExecute(boolean isOK) {
					// TODO Auto-generated method stub
					if (isOK) {
						ToastUtil.showToast("收藏成功");
						eventInfo.setEventIsCollect("1");
						mImageButton.setImageResource(R.drawable.sh_icon_collect_after);
						mImageButton.setTag("true");
						Intent intent = new Intent(); // Itent就是我们要发送的内容
						intent.putExtra(MyApplication.COLLECT_ACTION_OBJECT, eventInfo);
						intent.setAction(MyApplication.COLLECT_ACTION_FLAG_ACTIVITY); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
						context.sendBroadcast(intent); // 发送广播
					} else {
						ToastUtil.showToast("收藏失败");
					}
				}
			});

		} else {// 点击取消收藏
			CollectUtil.cancelActivity(context, eventInfo.getEventId(), new CollectCallback() {
				@Override
				public void onPostExecute(boolean isOK) {
					// TODO Auto-generated method stub
					if (isOK) {
						ToastUtil.showToast("取消成功");
						mImageButton.setImageResource(R.drawable.sh_icon_collect_befor);
						mImageButton.setTag("false");
						eventInfo.setEventIsCollect("0");
						Intent intent = new Intent(); // Itent就是我们要发送的内容
						intent.putExtra(MyApplication.COLLECT_ACTION_OBJECT, eventInfo);
						intent.setAction(MyApplication.COLLECT_ACTION_FLAG_ACTIVITY); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
						context.sendBroadcast(intent); // 发送广播
					} else {
						ToastUtil.showToast("操作失败");
					}
				}
			});
		}
	}

	private void setVenueBg(final ImageView mImageButton, final VenueDetailInfor venueInfo) {
		if (mImageButton.getTag().equals("false")) {// 点击收藏
			CollectUtil.addVenue(context, venueInfo.getVenueId(), new CollectCallback() {

				@Override
				public void onPostExecute(boolean isOK) {
					// TODO Auto-generated method stub
					if (isOK) {
						ToastUtil.showToast("收藏成功");

						mImageButton.setImageResource(R.drawable.sh_icon_collect_after);
						venueInfo.setVenueIsCollect(true);
						mImageButton.setTag("true");
						Intent intent = new Intent(); // Itent就是我们要发送的内容
						intent.putExtra(MyApplication.COLLECT_ACTION_OBJECT, venueInfo);
						intent.setAction(MyApplication.COLLECT_ACTION_FLAG_VUNUE); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
						context.sendBroadcast(intent); // 发送广播
					} else {
						ToastUtil.showToast("收藏失败");
					}
				}
			});

		} else {// 点击取消收藏
			CollectUtil.cancelVenue(context, venueInfo.getVenueId(), new CollectCallback() {
				@Override
				public void onPostExecute(boolean isOK) {
					// TODO Auto-generated method stub
					if (isOK) {
						mImageButton.setImageResource(R.drawable.sh_icon_collect_befor);
						ToastUtil.showToast("取消成功");
						mImageButton.setTag("false");
						venueInfo.setVenueIsCollect(false);
						Intent intent = new Intent(); // Itent就是我们要发送的内容
						intent.putExtra(MyApplication.COLLECT_ACTION_OBJECT, venueInfo);
						intent.setAction(MyApplication.COLLECT_ACTION_FLAG_VUNUE); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
						context.sendBroadcast(intent); // 发送广播
					} else {
						ToastUtil.showToast("操作失败");
					}
				}
			});
		}
	}

	private class ViewHolder {
		private TextView mId;
		private TextView mName;
		private TextView mPlace;
		private ImageView mSelected;
		private TextView mSpaceing;
		private RelativeLayout mcontent;
	}

}
