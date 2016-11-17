package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.SelectThemeActivity;
import com.sun3d.culturalShanghai.adapter.IndexHorListAdapter;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.fragment.NearbyFragment;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.view.HorizontalListView;

/**
 * 这个就是 横向滑动的listview
 * 
 * @author wenff
 * 
 */
public class ActivityWffTabHandler {
	private HorizontalListView mHorListView;
	public IndexHorListAdapter mHorAdapter;
	public static List<UserBehaviorInfo> mHorList;

	public ActivityWffTabHandler(
			Context mContext,
			final View view,
			final com.sun3d.culturalShanghai.handler.ActivityTabHandler.TabCallback tabCallback) {
		// TODO Auto-generated constructor stub
		mHorListView = (HorizontalListView) view
				.findViewById(R.id.index_hor_listview);
		mHorList = new ArrayList<UserBehaviorInfo>();
		mHorList.add(new UserBehaviorInfo("", "全部"));
		// if (ActivityFragment.userBehaviorInfos != null) {
		// mHorList.addAll(ActivityFragment.userBehaviorInfos);
		// } else {
		if (MyApplication.getInstance().getSelectTypeList() != null) {
			mHorList.addAll(MyApplication.getInstance().getSelectTypeList());
		}
		// }
		if (mHorList.size() != 0) {
			if (MyApplication.getInstance().getPosition() == 0) {
				mHorList.get(0).setIndex(true);
				MyApplication.getInstance().setPosition(0);
			}
		} else {
			Intent intent = new Intent(mContext, SelectThemeActivity.class);
			mContext.startActivity(intent);
		}
		for (int i = 0; i < mHorList.size(); i++) {

		}
		mHorAdapter = new IndexHorListAdapter(mContext, mHorList);
		mHorListView.setAdapter(mHorAdapter);
		mHorListView.setOnItemClickListener(new OnItemClickListener() {
			// int position = MyApplication.getInstance().getPosition();

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// mHorAdapter.setIsShow(arg2);
				for (int i = 0; i < mHorList.size(); i++) {
					// 这里判断哪个被选中
					if (arg2 == i) {
						mHorList.get(arg2).setIndex(true);
					} else {
						mHorList.get(i).setIndex(false);
					}
				}
				MyApplication.getInstance().setPosition(arg2);
				mHorAdapter.notifyDataSetChanged();
				if (tabCallback != null) {
					tabCallback.setTab(mHorList.get(arg2));
				}
				/**
				 * 再次请求服务器刷新数据 横向的listview 的点击来刷新页面
				 */
				onStartWill(mHorList.get(arg2).getTagId());
				if (MyApplication.loginUserInfor != null
						&& MyApplication.loginUserInfor.getUserId() != null
						&& !"".equals(MyApplication.loginUserInfor.getUserId())) {
				}
				// if (arg2 == 0) {
				// ActivityFragment.setCountTip(arg2,
				// mHorList.get(arg2).getTagId());
				// } else {
				// ActivityFragment.setCountTip(arg2 - 1,
				// mHorList.get(arg2).getTagId());
				// }

			}

		});

		mHorListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				((ViewGroup) view).requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}


	private void onStartWill(String tagId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("tagId", tagId);
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYWILL,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						try {
							JSONObject jsonObject = new JSONObject(resultStr
									.toString());
							String data = jsonObject.get("status").toString();
							if ("1".equals(data)) {
							} else {
							}
						} catch (Exception e) {

						}
					}
				});
	}

	public void setPosition() {
		mHorList.get(MyApplication.getInstance().getPosition()).setIndex(true);
		mHorAdapter.notifyDataSetChanged();
	}

	public interface TabCallback {
		public void setTab(UserBehaviorInfo info);
	}
}
