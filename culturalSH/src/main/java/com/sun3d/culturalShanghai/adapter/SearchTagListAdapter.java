package com.sun3d.culturalShanghai.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSONArray;
import com.amap.api.mapcore.util.co;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ButtonUtil;
import com.sun3d.culturalShanghai.Util.DensityUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.ActivityMap;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.EventListActivity;
import com.sun3d.culturalShanghai.activity.MyLoveActivity;
import com.sun3d.culturalShanghai.activity.ThisWeekActivity;
import com.sun3d.culturalShanghai.activity.VenueDetailActivity;
import com.sun3d.culturalShanghai.activity.VenueListActivity;
import com.sun3d.culturalShanghai.animation.ContainerAnimation;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler;
import com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback;
import com.sun3d.culturalShanghai.object.ActivityConditionInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.ScreenInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.Wff_BannerInfo;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;
import com.sun3d.culturalShanghai.view.HorizontalListView;
import com.sun3d.culturalShanghai.view.SlideShowView;

@SuppressLint("UseSparseArrays")
public class SearchTagListAdapter extends BaseAdapter {
	private Context mContext;
	private List<EventInfo> mList;
	private ViewHolder_Main vh_main = null;
	private int mType;

	public SearchTagListAdapter(Context mContext, List<EventInfo> list, int type) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.mList = list;
		this.mType = type;
	}

	@Override
	public int getCount() {
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

	public void setList(List<EventInfo> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			vh_main = new ViewHolder_Main();
			convertView = View.inflate(mContext,
					R.layout.search_tag_adapter_item, null);
			// vh_main.sieve_ll = (LinearLayout)
			// convertView.findViewById(R.id.Sieve);
			vh_main.ll = (LinearLayout) convertView.findViewById(R.id.ll);
			vh_main.distance_tv = (TextView) convertView
					.findViewById(R.id.distance_tv);
			vh_main.img = (ImageView) convertView.findViewById(R.id.img);
			vh_main.top_left_img = (ImageView) convertView
					.findViewById(R.id.top_left_img);
			vh_main.buttom_right_tv = (TextView) convertView
					.findViewById(R.id.buttom_right);
			vh_main.name_tv = (TextView) convertView.findViewById(R.id.name_tv);
			vh_main.address_tv = (TextView) convertView
					.findViewById(R.id.address_tv);
			vh_main.item_subject = (TextView) convertView
					.findViewById(R.id.item_subject);
			vh_main.item_subject1 = (TextView) convertView
					.findViewById(R.id.item_subject1);
			vh_main.item_subject2 = (TextView) convertView
					.findViewById(R.id.item_subject2);

			vh_main.distance_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.buttom_right_tv.setTypeface(MyApplication.GetTypeFace());

			vh_main.name_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.address_tv.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject1.setTypeface(MyApplication.GetTypeFace());
			vh_main.item_subject2.setTypeface(MyApplication.GetTypeFace());
			convertView.setTag(vh_main);
		} else {
			vh_main = (ViewHolder_Main) convertView.getTag();
		}
		final EventInfo eventInfo = mList.get(arg0);
		vh_main.name_tv.setText(eventInfo.getEventName());
		if (eventInfo.getEventEndTime() != null
				&& !eventInfo.getActivityStartTime().equals(
						eventInfo.getEventEndTime())) {
			String start = eventInfo.getActivityStartTime()
					.replaceAll("-", ".").substring(5, 10);
			String end = eventInfo.getEventEndTime().replaceAll("-", ".")
					.substring(5, 10);
			if (!eventInfo.getActivityLocationName().equals("")) {
				vh_main.address_tv.setText(start + "-" + end + " | "
						+ eventInfo.getActivityLocationName());
			} else {
				vh_main.address_tv.setText(start + "-" + end + " | "
						+ eventInfo.getActivityAddress());
			}

		} else {
			String start = eventInfo.getActivityStartTime()
					.replaceAll("-", ".").substring(5, 10);
			if (!eventInfo.getActivityLocationName().equals("")) {
				vh_main.address_tv.setText(start + " | "
						+ eventInfo.getActivityLocationName());
			} else {
				vh_main.address_tv.setText(start + " | "
						+ eventInfo.getActivityAddress());
			}

		}

		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						TextUtil.getUrlMiddle(eventInfo.getEventIconUrl()),
						vh_main.img, Options.getRoundOptions(0));
		vh_main.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,
						ActivityDetailActivity.class);
				intent.putExtra("eventId", eventInfo.getEventId());
				MyApplication.currentCount = -1;
				MyApplication.spike_Time = -1;
				MyApplication.total_availableCount = -1;
				mContext.startActivity(intent);
			}
		});

		if (eventInfo.getActivityIsReservation().equals("2")) {
			vh_main.top_left_img.setVisibility(View.VISIBLE);
			if (eventInfo.getSpikeType() == 1) {
				vh_main.top_left_img.setImageResource(R.drawable.icon_miao);
			} else {
				vh_main.top_left_img.setImageResource(R.drawable.icon_ding);
			}
		} else {
			vh_main.top_left_img.setVisibility(View.GONE);
		}
		if (eventInfo.getActivityIsFree() == 1) {
			vh_main.buttom_right_tv.setText("免费");
		} else {
			if (eventInfo.getActivityPrice().equals("null")) {
				vh_main.buttom_right_tv.setText("免费");
			} else {
				if (eventInfo.getPriceType() == 0) {
					vh_main.buttom_right_tv.setText(eventInfo
							.getActivityPrice() + "元/起");
				} else {
					vh_main.buttom_right_tv.setText(eventInfo
							.getActivityPrice() + "元/人");
				}
			}

		}
		if (eventInfo.getTagName() != null && eventInfo.getTagName() != "") {
			vh_main.item_subject.setVisibility(View.VISIBLE);
			vh_main.item_subject.setText(eventInfo.getTagName());
		} else {
			vh_main.item_subject.setVisibility(View.GONE);
		}
		if (eventInfo.getTagNameList().size() == 1) {
			vh_main.item_subject1.setVisibility(View.VISIBLE);
			vh_main.item_subject2.setVisibility(View.GONE);
			vh_main.item_subject1.setText(eventInfo.getTagNameList().get(0));
		} else if (eventInfo.getTagNameList().size() > 1) {
			vh_main.item_subject1.setVisibility(View.VISIBLE);
			vh_main.item_subject2.setVisibility(View.VISIBLE);
			vh_main.item_subject1.setText(eventInfo.getTagNameList().get(0));
			vh_main.item_subject2.setText(eventInfo.getTagNameList().get(1));
		} else {
			vh_main.item_subject1.setVisibility(View.GONE);
			vh_main.item_subject2.setVisibility(View.GONE);
		}
		if (mType != 5) {
			vh_main.distance_tv.setVisibility(View.GONE);
		} else {
			vh_main.distance_tv.setVisibility(View.VISIBLE);
			vh_main.distance_tv.setText(eventInfo.getDistance_str());
		}
		return convertView;
	}

	public void setType(int type) {
		this.mType = type;
	}

	class ViewHolder_Main {
		TextView distance_tv;

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
}
