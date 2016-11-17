package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.adapter.ActivityKillAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.KillInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityDetail_Kills {
	private LinearLayout content;
	private Context mContext;
	private String eventInfoId;
	private ListView listview;
	private ActivityKillAdapter aka;
	private ActivityDetailActivity activity;
	private TextView condition;
	private List<KillInfo> list;
	private List<KillInfo> mList;
	private LinearLayout kill_ll;
	private EventInfo eventInfo;

	public ActivityDetail_Kills(ActivityDetailActivity activity,
			Context mContext, String eventInfoId) {
		super();
		this.activity = activity;
		this.mContext = mContext;
		this.eventInfoId = eventInfoId;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_killlayout, null);
		listview = (ListView) content.findViewById(R.id.listview);
		condition = (TextView) content.findViewById(R.id.condition);
		condition.setTypeface(MyApplication.GetTypeFace());
		kill_ll = (LinearLayout) content.findViewById(R.id.kill_ll);
		mList = new ArrayList<KillInfo>();
		list = new ArrayList<KillInfo>();
		initdate();
	}

	public void setdate(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}

	public void initdate() {
		/**
		 * 这个是要传入到服务器的参数
		 */
		list.clear();
		mList.clear();
		Map<String, String> mParams = new HashMap<String, String>();

		mParams.put("activityId", eventInfoId);
		String urlString = "";
		// 第一次取数据的时候
		urlString = HttpUrlList.MyEvent.WFF_APPACTIVITYEVENTLIST;
		MyHttpRequest.onHttpPostParams(urlString, mParams,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "-秒杀开始  --"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list = JsonUtil.getDetailKillList(resultStr);
							handler.sendEmptyMessage(1);
						}
					}
				});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (eventInfo != null) {
					if (!eventInfo.getLowestCredit().equals("")
							&& !eventInfo.getCostCredit().equals("")
							&& !eventInfo.getLowestCredit().equals("0")
							&& !eventInfo.getCostCredit().equals("0")) {
						condition.setText(MyApplication
								.getRedTextColor("预订需要达到"
										+ eventInfo.getLowestCredit() + "积分"
										+ ",且每张需抵扣" + eventInfo.getCostCredit()
										+ "积分"));
					} else if (!eventInfo.getLowestCredit().equals("")
							&& eventInfo.getCostCredit().equals("")
							&& !eventInfo.getLowestCredit().equals("0")
							) {
						condition.setText(MyApplication
								.getRedTextColor("预订需要达到"
										+ eventInfo.getLowestCredit() + "积分"));
					} else if (eventInfo.getLowestCredit().equals("")
							&& !eventInfo.getCostCredit().equals("")
							&& !eventInfo.getCostCredit().equals("0")) {
						condition.setText(MyApplication.getRedTextColor("每张需抵扣"
								+ eventInfo.getCostCredit() + "积分"));
					} else {
						condition.setText("");
					}

				}

				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getSpikeType() == 1) {
						mList.add(list.get(i));
					}

				}
				if (mList.size() == 0) {
					kill_ll.setVisibility(View.GONE);
				} else {
					aka = new ActivityKillAdapter(getmContext(), mList);
					listview.setAdapter(aka);
					MyApplication
							.setListViewHeightBasedOnChildren_Kill(listview);
				}
				activity.setStatus();
				break;

			default:
				break;
			}
		};
	};

	public String getEventInfoId() {
		return eventInfoId;
	}

	public void setEventInfoId(String eventInfoId) {
		this.eventInfoId = eventInfoId;
	}

	public LinearLayout getContent() {
		return content;
	}

	public void setContent(LinearLayout content) {
		this.content = content;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

}
