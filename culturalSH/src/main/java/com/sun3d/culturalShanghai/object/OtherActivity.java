package com.sun3d.culturalShanghai.object;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.activity.ActivityDetailActivity;
import com.sun3d.culturalShanghai.adapter.OtherListAdapter;
import com.sun3d.culturalShanghai.adapter.SearchListAdapter;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class OtherActivity extends Activity implements OnClickListener {
	private GridView gridview;
	private String id, ActivityId;
	private String TAG = "OtherActivity";
	private List<ActivityOtherInfo> list;
	private OtherListAdapter adapter;
	private ImageView left_iv;
	private TextView middle_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_activity);
		init();
		getBundle();
		getData();
	}

	private void getData() {
		JSONObject json = new JSONObject();
		try {
			json.put("associationId", id);
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
						Log.i(TAG, "看看返回的数据===  " + statusCode + "  result==  "
								+ resultStr);
						if (statusCode == 1) {
							try {
								list = JsonUtil.getActivityOther(resultStr,
										ActivityId);
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
				middle_tv.setText("精彩回顾");
				Log.i(TAG, "请求到了 数据");
				adapter.setList(list);
				break;
			case 2:
				Log.i(TAG, "失败了吗");
				break;
			default:
				break;
			}
		};
	};

	private void getBundle() {
		id = getIntent().getExtras().getString("ID");
		ActivityId = getIntent().getExtras().getString("ActivityId");
		Log.i(TAG, "id" + id);

	}

	private void init() {
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		left_iv = (ImageView) findViewById(R.id.left_iv);
		gridview = (GridView) findViewById(R.id.gridview);
		list = new ArrayList<ActivityOtherInfo>();
		adapter = new OtherListAdapter(this, list, 0);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(myItemClickListener);
		left_iv.setOnClickListener(this);
	}

	OnItemClickListener myItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View arg1, int arg2,
				long arg3) {
			ActivityOtherInfo info = (ActivityOtherInfo) parent
					.getItemAtPosition(arg2);
			Intent i = new Intent();
			i.setClass(OtherActivity.this, ActivityDetailActivity.class);
			i.putExtra("eventId", info.getActivityId());
			OtherActivity.this.startActivity(i);

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_iv:
			finish();
			break;

		default:
			break;
		}

	}

}
