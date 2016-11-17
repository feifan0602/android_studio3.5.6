package com.sun3d.culturalShanghai.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.view.MyTextView;

public class ActivityDetail_Host implements OnClickListener {
	private LinearLayout content;
	private Context mContext;
	private Activity activity;
	private TextView mActivityHost;
	private TextView mActivityOrganizer;
	private TextView mActivityCoorganizer;;
	private EventInfo eventInfo;
	private LinearLayout host_ll;
	private TextView activityPerformed;
	private TextView activitySpeaker;
	private TextView activityHost_tv;
	private LinearLayout activity_performed_layout;
	private TextView activityHost_tv1, activitySpeaker1, activityPerformed1,
			mActivityHost1, mActivityOrganizer1, mActivityCoorganizer1;
	private TextView subItem, subItem1, subItem2;
	private String TAG = "ActivityDetail_Host";

	public LinearLayout getContent() {
		return content;
	}

	public ActivityDetail_Host(Context context, Activity activity) {
		this.mContext = context;
		this.activity = activity;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_hostlayout, null);
		initView();
	}

	private void initView() {
		subItem = (TextView) content.findViewById(R.id.subItem);
		subItem1 = (TextView) content.findViewById(R.id.subItem1);
		subItem2 = (TextView) content.findViewById(R.id.subItem2);
		subItem.setTypeface(MyApplication.GetTypeFace());
		subItem1.setTypeface(MyApplication.GetTypeFace());
		subItem2.setTypeface(MyApplication.GetTypeFace());
		host_ll = (LinearLayout) content.findViewById(R.id.host_ll);
		activityHost_tv = (TextView) content.findViewById(R.id.activityHost_tv);
		mActivityHost = (TextView) content.findViewById(R.id.activityHost);
		mActivityOrganizer = (TextView) content
				.findViewById(R.id.activityOrganizer);
		mActivityCoorganizer = (TextView) content
				.findViewById(R.id.activityCoorganizer);
		activityPerformed = (TextView) content
				.findViewById(R.id.activityPerformed);
		activity_performed_layout = (LinearLayout) content
				.findViewById(R.id.activity_performed_layout);
		activitySpeaker = (TextView) content.findViewById(R.id.activitySpeaker);
		mActivityHost.setTypeface(MyApplication.GetTypeFace());
		mActivityOrganizer.setTypeface(MyApplication.GetTypeFace());
		mActivityCoorganizer.setTypeface(MyApplication.GetTypeFace());
		activityPerformed.setTypeface(MyApplication.GetTypeFace());
		activitySpeaker.setTypeface(MyApplication.GetTypeFace());

		mActivityHost1 = (TextView) content.findViewById(R.id.activityHost1);
		mActivityOrganizer1 = (TextView) content
				.findViewById(R.id.activityOrganizer1);
		mActivityCoorganizer1 = (TextView) content
				.findViewById(R.id.activityCoorganizer1);
		activityPerformed1 = (TextView) content
				.findViewById(R.id.activityPerformed1);
		activitySpeaker1 = (TextView) content
				.findViewById(R.id.activitySpeaker1);
		mActivityHost1.setTypeface(MyApplication.GetTypeFace());
		activitySpeaker1.setTypeface(MyApplication.GetTypeFace());
		activityPerformed1.setTypeface(MyApplication.GetTypeFace());
		mActivityHost1.setTypeface(MyApplication.GetTypeFace());
		mActivityOrganizer1.setTypeface(MyApplication.GetTypeFace());
		mActivityCoorganizer1.setTypeface(MyApplication.GetTypeFace());
	}

	public void setHostData(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
		if (eventInfo.getActivityPerformed().toString() != null
				&& !eventInfo.getActivityPerformed().toString().equals("")) {
			host_ll.setVisibility(View.VISIBLE);
			activity_performed_layout.setOnClickListener(this);
			activityPerformed.setText(eventInfo.getActivityPerformed()
					.toString());
			if(eventInfo.getAssnId() == null || eventInfo.getAssnId().length() == 0)
			{
				activityPerformed.setTextColor(mActivityCoorganizer.getTextColors());
				content.findViewById(R.id.arrowimg).setVisibility(View.GONE);
				
			}
			
		} else {
			host_ll.setVisibility(View.GONE);
			activityPerformed.setVisibility(View.GONE);
			activityPerformed1.setVisibility(View.GONE);
			activity_performed_layout.setVisibility(View.GONE);
		}
		if (eventInfo.getActivitySpeaker().toString() != null
				&& !eventInfo.getActivitySpeaker().toString().equals("")) {
			host_ll.setVisibility(View.VISIBLE);
			activitySpeaker.setText(eventInfo.getActivitySpeaker().toString());
		} else {
			activitySpeaker.setVisibility(View.GONE);
			activitySpeaker1.setVisibility(View.GONE);
		}
		if (eventInfo.getActivityHost().toString() != null
				&& !eventInfo.getActivityHost().toString().equals("")) {
			host_ll.setVisibility(View.VISIBLE);
			mActivityHost.setText(eventInfo.getActivityHost().toString());
		} else {
			mActivityHost.setVisibility(View.GONE);
			mActivityHost1.setVisibility(View.GONE);
		}
		if (eventInfo.getActivityOrganizer().toString() != null
				&& !eventInfo.getActivityOrganizer().toString().equals("")) {
			mActivityOrganizer.setText(eventInfo.getActivityOrganizer()
					.toString());
		} else {
			mActivityOrganizer.setVisibility(View.GONE);
			mActivityOrganizer1.setVisibility(View.GONE);
		}
		if (eventInfo.getActivityCoorganizer().toString() != null
				&& !eventInfo.getActivityCoorganizer().toString().equals("")) {
			mActivityCoorganizer.setText(eventInfo.getActivityCoorganizer()
					.toString());
		} else {
			mActivityCoorganizer.setVisibility(View.GONE);
			mActivityCoorganizer1.setVisibility(View.GONE);
		}
		if (eventInfo.getAssnSub() != null
				&& eventInfo.getAssnSub().size() != 0) {
			switch (eventInfo.getAssnSub().size()) {
			case 1:
				subItem.setVisibility(View.VISIBLE);
				subItem.setText(eventInfo.getAssnSub().get(0));
				break;
			case 2:
				subItem.setVisibility(View.VISIBLE);
				subItem1.setVisibility(View.VISIBLE);
				subItem.setText(eventInfo.getAssnSub().get(0));
				subItem1.setText(eventInfo.getAssnSub().get(1));
				break;
			case 3:
				subItem.setVisibility(View.VISIBLE);
				subItem1.setVisibility(View.VISIBLE);
				subItem2.setVisibility(View.VISIBLE);
				subItem.setText(eventInfo.getAssnSub().get(0));
				subItem1.setText(eventInfo.getAssnSub().get(1));
				subItem2.setText(eventInfo.getAssnSub().get(2));

				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_performed_layout:
			if(eventInfo.getAssnId() != null && eventInfo.getAssnId().length() > 0)
			{
				Intent i = new Intent();
				i.setClass(activity, BannerWebView.class);
				Bundle bundle = new Bundle();
				Log.i(TAG, "ID==   " + eventInfo.getAssnId());
				bundle.putString("url",
						HttpUrlList.IP + "/wechatAssn/toAssnDetail.do?assnId="
								+ eventInfo.getAssnId());
				i.putExtras(bundle);
				activity.startActivity(i);
			}
			break;

		default:
			break;
		}

	}
}
