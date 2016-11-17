package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.MoreSpecialActivity;
import com.sun3d.culturalShanghai.activity.NewDetailsActivity;
import com.sun3d.culturalShanghai.adapter.SpecialAdapter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.NewInfo;
import com.sun3d.culturalShanghai.view.ScrollViewListView;

/**
 * 实况直击
 * 
 * @author yangyoutao
 * 
 */
public class ActivityDetail_Special {
	private LinearLayout content;
	private Context mContext;
	private ScrollViewListView mListView;
	private List<NewInfo> list = new ArrayList<NewInfo>();
	private SpecialAdapter mSpecialAdapter;
	private String activityId;
	private LinearLayout mLayout;
	private ImageView tvMore;

	public LinearLayout getContent() {
		return content;
	}

	public ActivityDetail_Special(Context context, String activityId) {
		this.mContext = context;
		this.activityId = activityId;
		content = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.activity_speciallayout, null);
		mLayout = (LinearLayout) content.findViewById(R.id.activity_specialla_layout);
		tvMore = (ImageView) content.findViewById(R.id.more);
		mListView = (ScrollViewListView) content.findViewById(R.id.listview);
		initData();
		getData();
	}

	private void initData() {
		mSpecialAdapter = new SpecialAdapter(mContext, list);
		mListView.setAdapter(mSpecialAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, NewDetailsActivity.class);
				// intent.putExtra("newId", list.get(arg2).getNewId());
				NewInfo newInfo = list.get(arg2);
				intent.putExtra("NewInfo", newInfo);
				mContext.startActivity(intent);
			}
		});
		tvMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, MoreSpecialActivity.class);
				intent.putExtra("activityId", activityId);
				mContext.startActivity(intent);
			}
		});
	}

	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		// params.put("referId", "692e4fd1468c4a7e804218ff208d88b3");
		params.put("referId", activityId);
		params.put("pageIndex", "0");
		params.put("pageNum", "4");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYSHOWINDEX, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					list = JsonUtil.getNewList(resultStr);
					setData();
				} else {
//					ToastUtil.showToast(resultStr);
				}
			}
		});
	}

	private void setData() {
		if (list.size() == 0) {
			mLayout.setVisibility(View.GONE);
		} else {
			if (list.size() > 3) {
				list.remove(list.size() - 1);
				tvMore.setVisibility(View.VISIBLE);
			} else {
				tvMore.setVisibility(View.INVISIBLE);
			}
			mLayout.setVisibility(View.VISIBLE);
		}
		mSpecialAdapter.setList(list);
		mSpecialAdapter.notifyDataSetChanged();
	}

}
