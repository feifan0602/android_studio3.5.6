package com.sun3d.culturalShanghai.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.object.EventInfo;

public class SearchListAdapter extends BaseAdapter {
	private Context mContext;
	private List<EventInfo> list;
	private ViewHolder_Main vh_main = null;
	private String TAG = "SearchListAdapter";
	/**
	 * 0为活动 1为场馆
	 */
	private int mType;
	private static final int TYPE_COUNT = 2;// item类型的总数
	private static final int TYPE_VENUE = 1;// 场馆
	private static final int TYPE_ACTIVITY = 0;// 活动
	private int currentType;// 当前item类型
	private ViewHolder_Venue vh_venue = null;
	private View Activity_view, Venue_view;

	public SearchListAdapter(Context mContext, List<EventInfo> list,
			int type) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.list = list;
		this.mType = type;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (mType == 0) {
			return TYPE_ACTIVITY;
		} else {
			return TYPE_VENUE;
		}

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

	public void setList(List<EventInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		currentType = getItemViewType(pos);
		if (currentType == TYPE_ACTIVITY) {
			convertView = addMainActivity(pos, convertView);
		} else if (currentType == TYPE_VENUE) {
			convertView = addMainVenue(pos, convertView);
		}
		return convertView;
	}

	private View addMainVenue(int pos, View convertView) {
		if (convertView == null) {
			vh_venue = new ViewHolder_Venue();
			Venue_view = View.inflate(mContext,
					R.layout.searchlist_item_adapter, null);
			vh_venue.name_tv = (TextView) Venue_view.findViewById(R.id.name_tv);
			vh_venue.middle_tv = (TextView) Venue_view
					.findViewById(R.id.middle_tv);
			vh_venue.address_tv = (TextView) Venue_view
					.findViewById(R.id.address_tv);
			vh_venue.activity_tv = (TextView) Venue_view
					.findViewById(R.id.activity_num_tv);
			vh_venue.room_tv = (TextView) Venue_view
					.findViewById(R.id.room_num_tv);
			vh_venue.space_bg_iv = (ImageView) Venue_view
					.findViewById(R.id.space_bg);
			vh_venue.middle_tv.setTypeface(MyApplication.GetTypeFace());
			vh_venue.name_tv.setTypeface(MyApplication.GetTypeFace());
			vh_venue.address_tv.setTypeface(MyApplication.GetTypeFace());
			vh_venue.activity_tv.setTypeface(MyApplication.GetTypeFace());
			vh_venue.room_tv.setTypeface(MyApplication.GetTypeFace());
			vh_venue.activity_tv.getBackground().setAlpha(200);
			vh_venue.room_tv.getBackground().setAlpha(200);
			vh_venue.center_tv = (TextView) Venue_view
					.findViewById(R.id.middle_tv);
			Venue_view.setTag(vh_venue);
			convertView = Venue_view;
		} else {
			vh_venue = (ViewHolder_Venue) convertView.getTag();
		}
		if (list.size() != 0 && list.get(pos) != null) {
			vh_venue.name_tv.setText(list.get(pos).getVenueName());
			vh_venue.address_tv.setText(list.get(pos).getVenueAddress());
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlMiddle(list.get(pos)
									.getVenueIconUrl()), vh_venue.space_bg_iv,
							Options.getRoundOptions(0));
			Log.i(TAG,
					"看看图片 链接 ==  "
							+ TextUtil.getUrlMiddle(list.get(pos)
									.getVenueIconUrl()));
			// 这里判断 活动室数 和活动数
			if (list.get(pos).getActCount() == 0
					&& list.get(pos).getRoomCount() == 0) {
				vh_venue.activity_tv.setVisibility(View.INVISIBLE);
				vh_venue.room_tv.setVisibility(View.INVISIBLE);
				vh_venue.middle_tv.setVisibility(View.INVISIBLE);
				// main_vh.middle_tv
				// .setBackgroundResource(R.drawable.space_item_bg);
				// main_vh.middle_tv.setText(MyApplication.getYellowTextColor(list
				// .get(pos).getRoomCount() + "个活动室"));
			} else if (list.get(pos).getRoomCount() == 0) {
				vh_venue.activity_tv.setVisibility(View.GONE);
				vh_venue.room_tv.setVisibility(View.GONE);
				vh_venue.middle_tv.setVisibility(View.VISIBLE);
				vh_venue.middle_tv
						.setBackgroundResource(R.drawable.space_item_bg);
				vh_venue.middle_tv.setText(MyApplication
						.getYellowTextColor(list.get(pos).getActCount()
								+ "个在线活动"));
			} else if (list.get(pos).getActCount() == 0) {
				vh_venue.activity_tv.setVisibility(View.GONE);
				vh_venue.room_tv.setVisibility(View.GONE);
				vh_venue.middle_tv.setVisibility(View.VISIBLE);
				vh_venue.middle_tv
						.setBackgroundResource(R.drawable.space_item_bg);
				vh_venue.middle_tv.setText(MyApplication
						.getYellowTextColor(list.get(pos).getRoomCount()
								+ "个活动室"));
			} else {
				vh_venue.activity_tv.setVisibility(View.VISIBLE);
				vh_venue.room_tv.setVisibility(View.VISIBLE);
				vh_venue.middle_tv.setVisibility(View.INVISIBLE);
				vh_venue.middle_tv.setBackground(null);
				vh_venue.activity_tv.setText(MyApplication
						.getYellowTextColor(list.get(pos).getActCount()
								+ "个在线活动"));
				vh_venue.room_tv.setText(MyApplication.getYellowTextColor(list
						.get(pos).getRoomCount() + "个活动室"));
				vh_venue.middle_tv.setText("");

			}
		}
		return convertView;
	}

	private View addMainActivity(int pos, View convertView) {
		if (convertView == null) {
			vh_main = new ViewHolder_Main();
			Activity_view = View.inflate(mContext,
					R.layout.searchlistadapter_item, null);
			vh_main.ll = (LinearLayout) Activity_view.findViewById(R.id.ll);
			vh_main.img = (ImageView) Activity_view.findViewById(R.id.img);
			vh_main.top_left_img = (ImageView) Activity_view
					.findViewById(R.id.top_left_img);
			vh_main.buttom_right_tv = (TextView) Activity_view
					.findViewById(R.id.buttom_right);
			vh_main.name_tv = (TextView) Activity_view
					.findViewById(R.id.name_tv);
			vh_main.address_tv = (TextView) Activity_view
					.findViewById(R.id.address_tv);
			vh_main.item_subject = (TextView) Activity_view
					.findViewById(R.id.item_subject);
			vh_main.item_subject1 = (TextView) Activity_view
					.findViewById(R.id.item_subject1);
			vh_main.item_subject2 = (TextView) Activity_view
					.findViewById(R.id.item_subject2);
			vh_main.buttom_right_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.name_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.address_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject1.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject2.setTypeface(MyApplication.GetTypeFace());
			Activity_view.setTag(vh_main);
			convertView = Activity_view;
		} else {
			vh_main = (ViewHolder_Main) convertView.getTag();
		}
		if (list.size() != 0) {

			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							TextUtil.getUrlMiddle(list.get(pos)
									.getActivityIconUrl()), vh_main.img,
							Options.getRoundOptions(0));
			if (list.get(pos).getActivityPrice().equals("0")) {
				vh_main.buttom_right_tv.setText("免费");
			} else {
				if (list.get(pos).getActivityPrice().equals("null")) {
					vh_main.buttom_right_tv.setText("免费");
				} else {
					if (list.get(pos).getPriceType() == 0) {
						// XX元起
						vh_main.buttom_right_tv.setText(list.get(pos)
								.getActivityPrice() + "元起");
					} else {
						// XX元/人
						vh_main.buttom_right_tv.setText(list.get(pos)
								.getActivityPrice() + "元/人");
					}
				}

			}

			if (list.get(pos).getActivityEndTime() != null
					&& !list.get(pos).getActivityStartTime()
							.equals(list.get(pos).getActivityEndTime())) {
				String start = list.get(pos).getActivityStartTime()
						.replaceAll("-", ".").substring(5, 10);
				String end = list.get(pos).getActivityEndTime()
						.replaceAll("-", ".").substring(5, 10);
				if (!list.get(pos).getActivityLocationName().equals("")) {
					vh_main.address_tv.setText(start + "-" + end + "|"
							+ list.get(pos).getActivityLocationName());
				} else {
					vh_main.address_tv.setText(start + "-" + end + "|"
							+ list.get(pos).getActivityAddress());
				}

			} else {
				String start = list.get(pos).getActivityStartTime()
						.replaceAll("-", ".").substring(5, 10);
				if (!list.get(pos).getActivityLocationName().equals("")) {
					vh_main.address_tv.setText(start + "|"
							+ list.get(pos).getActivityLocationName());
				} else {
					vh_main.address_tv.setText(start + "|"
							+ list.get(pos).getActivityAddress());
				}

			}
			vh_main.name_tv.setText(list.get(pos).getActivityName());
			if (list.get(pos).getActivityIsReservation().equals("2")) {
				vh_main.top_left_img.setVisibility(View.VISIBLE);
				if (list.get(pos).getSpikeType() == 1) {
					vh_main.top_left_img.setImageResource(R.drawable.icon_miao);
				} else {
					vh_main.top_left_img.setImageResource(R.drawable.icon_ding);
				}
			} else {
				vh_main.top_left_img.setVisibility(View.GONE);
			}
			Log.i(TAG, "tagneme ==  " + list.get(pos).getTagName());
			if (list.get(pos).getTagName() != null
					&& !list.get(pos).getTagName().equals("")
					&& !list.get(pos).getTagName().equals("null")) {
				vh_main.item_subject.setVisibility(View.VISIBLE);
				vh_main.item_subject.setText(list.get(pos).getTagName());
			} else {
				vh_main.item_subject.setVisibility(View.GONE);
			}
			if (list.get(pos).getTagNameList() == null
					|| list.get(pos).getTagNameList().size() == 0) {
				vh_main.item_subject1.setVisibility(View.GONE);
				vh_main.item_subject2.setVisibility(View.GONE);
			} else {
				if (list.get(pos).getTagNameList().size() == 1) {
					vh_main.item_subject1.setVisibility(View.VISIBLE);
					vh_main.item_subject2.setVisibility(View.GONE);
					vh_main.item_subject1.setText(list.get(pos)
							.getTagNameList().get(0));
				} else if (list.get(pos).getTagNameList().size() > 1) {
					vh_main.item_subject1.setVisibility(View.VISIBLE);
					vh_main.item_subject2.setVisibility(View.VISIBLE);
					vh_main.item_subject1.setText(list.get(pos)
							.getTagNameList().get(0));
					vh_main.item_subject2.setText(list.get(pos)
							.getTagNameList().get(1));
				} else {
					vh_main.item_subject1.setVisibility(View.GONE);
					vh_main.item_subject2.setVisibility(View.GONE);
				}
			}

		}
		return convertView;
	}

	class ViewHolder_Main {
		ImageView img;
		ImageView top_left_img;
		TextView buttom_right_tv;
		TextView name_tv;
		TextView address_tv;
		TextView item_subject;
		TextView item_subject1;
		TextView item_subject2;
		LinearLayout ll;

	}

	class ViewHolder_Venue {
		TextView middle_tv;
		TextView name_tv;
		TextView address_tv;
		TextView activity_tv;
		TextView room_tv;
		ImageView space_bg_iv;
		TextView center_tv;
	}
}
