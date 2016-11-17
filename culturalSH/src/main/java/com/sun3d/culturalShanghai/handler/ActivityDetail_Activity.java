package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.activity.SearchListActivity;
import com.sun3d.culturalShanghai.adapter.Activity_Other_Adapter;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.ActivityOtherInfo;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.HttpResponseText;
import com.sun3d.culturalShanghai.object.OtherActivity;
import com.sun3d.culturalShanghai.view.ScrollScrollView;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityDetail_Activity implements OnClickListener {
	private Context mContext;
	private ScrollScrollView scroll_view;
	private LinearLayout content;
	private GridView gridview;
	private Activity_Other_Adapter adapter;
	private List<ActivityOtherInfo> list;
	private List<ActivityOtherInfo> mList;
	private TextView activity_num_tv;
	private TextView title_tv;
	private EventInfo eventInfo;
	private HttpResponseText httpBackContent;
	private ActivityDetailActivity activity;
	private String TAG = "ActivityDetail_Activity";
	private ImageView arrow_right;
	private LinearLayout activity_detail_ll;

	public ActivityDetail_Activity(Context context, ScrollScrollView scroll,
			ActivityDetailActivity act) {
		this.scroll_view = scroll;
		this.mContext = context;
		this.activity = act;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(
				R.layout.activity_detail, null);
		initView();

	}

	private void getData() {
		// HashMap<String, String> _param = new HashMap<String, String>();
		// _param.put("associationId", eventInfo.getAssnId());
		JSONObject json = new JSONObject();
		try {
			json.put("associationId", eventInfo.getAssnId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Log.i(TAG, "请求的参数  ==  " + _param.toString());
		MyHttpRequest.onStartHttpPostJSON(
				HttpUrlList.ActivityDetail.ASSOCIATIONACTIVITY, json,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == 1) {
							try {

								list = JsonUtil.getActivityOther(resultStr,
										eventInfo.getEventId());
								handler.sendEmptyMessage(1);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							handler.sendEmptyMessage(2);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String num = "共" + list.size() + "个活动";
				int size_num = 0;
				if (list.size() >= 4) {
					size_num = 4;
				} else {
					size_num = list.size();
				}
				for (int i = 0; i < size_num; i++) {
					mList.add(list.get(i));
				}
				if (list.size() > 4) {
					activity_num_tv.setVisibility(View.VISIBLE);
					arrow_right.setVisibility(View.VISIBLE);
					activity_num_tv.setText(MyApplication.getRedTextColor(num));
				} else {
					activity_num_tv.setVisibility(View.INVISIBLE);
					arrow_right.setVisibility(View.INVISIBLE);
				}

				adapter = new Activity_Other_Adapter(mContext, mList);
				gridview.setAdapter(adapter);
				gridview.setOnItemClickListener(MyOnItem);
				MyApplication.setGridViewHeightTwo(gridview);
				activity_detail_ll.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		};
	};

	public void setData(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
		getData();
	}

	private void initView() {
		activity_detail_ll = (LinearLayout) content
				.findViewById(R.id.activity_detail_ll);
		arrow_right = (ImageView) content.findViewById(R.id.arrow_right);
		activity_num_tv = (TextView) content.findViewById(R.id.activity_num);
		title_tv = (TextView) content.findViewById(R.id.title);
		activity_num_tv.setTypeface(MyApplication.GetTypeFace());
		activity_num_tv.setOnClickListener(this);
		title_tv.setTypeface(MyApplication.GetTypeFace());
		gridview = (GridView) content.findViewById(R.id.gridview);
		list = new ArrayList<ActivityOtherInfo>();
		mList = new ArrayList<ActivityOtherInfo>();

	}

	public LinearLayout getContent() {
		return content;
	}

	OnItemClickListener MyOnItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos,
				long arg3) {
			ActivityOtherInfo info = (ActivityOtherInfo) parent
					.getItemAtPosition(pos);
			Intent i = new Intent();
			i.setClass(mContext, ActivityDetailActivity.class);
			i.putExtra("eventId", info.getActivityId());
			mContext.startActivity(i);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_num:
			Intent i = new Intent();
			i.putExtra("ID", eventInfo.getAssnId());
			i.putExtra("ActivityId", eventInfo.getEventId());
			i.setClass(mContext, OtherActivity.class);
			mContext.startActivity(i);
			break;

		default:
			break;
		}

	}
}
