package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.RelatedActivityAdapter;
import com.sun3d.culturalShanghai.adapter.RelatedNewActivityAdapter;
import com.sun3d.culturalShanghai.adapter.VenueMoreAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.EventInfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VenueMoreActivity extends Activity implements OnClickListener {
	private PullToRefreshListView main_list;
	private ImageView left_iv;
	private VenueMoreAdapter vma;
	private String VenueId;
	private List<EventInfo> list = new ArrayList<EventInfo>();
	private List<EventInfo> mList = new ArrayList<EventInfo>();
	private RelatedNewActivityAdapter mEventAdapter;
	private TextView middle_tv;
	private int mIndex = 0;
	private String mNum = "10";
	private boolean addMoreBool = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.venuemoreactivity);
		Bundle bundle = getIntent().getExtras();
		VenueId = bundle.getString("VenueId");
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setText("活动列表");
		main_list = (PullToRefreshListView) findViewById(R.id.main_list);
		main_list.setOnRefreshListener(myRefresh);
		left_iv = (ImageView) findViewById(R.id.left_iv);
		left_iv.setOnClickListener(this);
		getActivityList();
		mEventAdapter = new RelatedNewActivityAdapter(this, list, false);
		main_list.setAdapter(mEventAdapter);
	}

	OnRefreshListener2 myRefresh = new OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			onResh();
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			// TODO Auto-generated method stub
			onAddmoreData();
		}

	};

	private void onResh() {
		// TODO Auto-generated method stub
		addMoreBool = true;
		mList.clear();
		list.clear();
		mIndex = 0;
		getActivityList();
	}

	private void onAddmoreData() {
		// TODO Auto-generated method stub
		addMoreBool = false;
		mIndex = HttpUrlList.HTTP_NUM_10 + mIndex;
		getActivityList();
	}

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

	/**
	 * 获取活动列表
	 */
	private void getActivityList() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.ActivityRoomUrl.VENUE_ID, VenueId);
		params.put(HttpUrlList.HTTP_PAGE_INDEX, mIndex + "");
		params.put(HttpUrlList.HTTP_PAGE_NUM, mNum);
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.ACTIVITY_LIST_URL,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "这个是场馆活动室的数据"
								+ resultStr);
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								list = JsonUtil.getEventList(resultStr);
								if (list.size() != 0) {
									mList.addAll(list);
									handler.sendEmptyMessage(1);
								} else {
									handler.sendEmptyMessage(2);
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}

						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mEventAdapter.setList(mList);
				mEventAdapter.notifyDataSetChanged();
				main_list.onRefreshComplete();
				break;
			case 2:
				// 加载更多 没数据 和刚进去没数据
				if (addMoreBool) {

				} else {
					main_list.onRefreshComplete();
					ToastUtil.showToast("已经全部加载完毕");
				}
				break;
			default:
				break;
			}
		}
	};
}
