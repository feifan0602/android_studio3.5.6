package com.sun3d.culturalShanghai.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.SelectThemeActivity;
import com.sun3d.culturalShanghai.adapter.IndexHorListAdapter;
import com.sun3d.culturalShanghai.basic.service.DataService;
import com.sun3d.culturalShanghai.fragment.ActivityFragment;
import com.sun3d.culturalShanghai.fragment.NearbyFragment;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.view.HorizontalListView;

/**
 * 这个就是 横向滑动的listview
 * 
 * @author wenff
 * 
 */
public class ActivityTabHandler 
{
	private HorizontalListView mHorListView;
	public IndexHorListAdapter mHorAdapter;
	public static List<UserBehaviorInfo> mHorList;
	private String TAG = "ActivityTabHandler";

	public ActivityTabHandler(Context mContext, final View view, final TabCallback callback, final ActivityFragment af)
	{
	
		// TODO Auto-generated constructor stub
		
		mHorListView = (HorizontalListView) view.findViewById(R.id.index_hor_listview);
		mHorList = new ArrayList<UserBehaviorInfo>();

		if (MyApplication.getInstance().getSelectTypeList() != null)
		{//本地保存的标签
			Log.i(TAG, " 看看 空了吗？  " + MyApplication.getInstance().getSelectTypeList().size());
			mHorList.addAll(MyApplication.getInstance().getSelectTypeList());
		}
		// if(list_tagName.size() != 0 && !MyApplication.UserIsLogin)
		else
		{
			final List<String> list_tagName = SharedPreManager.readTypeInfo();
			
			DataService.getActivityType(new DataService.ReceiveCallBack()
			{

				@Override
				public void dataReceviceSuccessCallBack(Object data)
				{
					List<UserBehaviorInfo> mList = (List<UserBehaviorInfo>) data;
					mHorList.clear();
					if (mList != null)
					{
						if (list_tagName != null && list_tagName.size() != 0)
						{
							for (int i = 0; i < mList.size(); i++)
							{
								for (int j = 0; j < list_tagName.size(); j++)
								{
									if (mList.get(i).getTagName().equals(list_tagName.get(j)))
									{
										Log.i(TAG, "添加成功了");
										mHorList.add(mList.get(i));
									}
								}
							}

						} else
						{
							mHorList.addAll(mList);
						}
						mHorAdapter.notifyDataSetChanged();
						MyApplication.getInstance().setSelectTypeList(mList);

					}

				}

				@Override
				public void dataReceviceFailedCallBack(String feedback)
				{
					// TODO Auto-generated method stub

				}
			});
		}

		if (mHorList.size() != 0)
		{
			Log.i(TAG, " 看看是否为0？  ");
			if (MyApplication.getInstance().getPosition() == 0)
			{
				mHorList.get(0).setIndex(true);
				MyApplication.getInstance().setPosition(0);
			}
		}
		mHorAdapter = new IndexHorListAdapter(mContext, mHorList);
		mHorListView.setAdapter(mHorAdapter);
		mHorListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
				// mHorAdapter.setIsShow(arg2);
				for (int i = 0; i < mHorList.size(); i++)
				{
					// 这里判断哪个被选中
					if (arg2 == i)
					{
						mHorList.get(arg2).setIndex(true);
					} else
					{
						mHorList.get(i).setIndex(false);
					}
				}
				MyApplication.getInstance().setPosition(arg2);
				mHorAdapter.notifyDataSetChanged();
				if (callback != null)
				{
					callback.setTab(mHorList.get(arg2));
				}
				/**
				 * 再次请求服务器刷新数据 横向的listview 的点击来刷新页面
				 */
				// onStartWill(mHorList.get(arg2).getTagId());
				if (MyApplication.loginUserInfor != null && MyApplication.loginUserInfor.getUserId() != null && !"".equals(MyApplication.loginUserInfor.getUserId()))
				{
				}

			}

		});

		mHorListView.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				// TODO Auto-generated method stub
				((ViewGroup) view).requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	private void onStartWill(String tagId)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("tagId", tagId);
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITYWILL, params, new HttpRequestCallback()
		{
			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				try
				{
					JSONObject jsonObject = new JSONObject(resultStr.toString());
					String data = jsonObject.get("status").toString();
					if ("1".equals(data))
					{
					} else
					{
					}
				} catch (Exception e)
				{

				}
			}
		});
	}

	public void setPosition()
	{
		mHorList.get(MyApplication.getInstance().getPosition()).setIndex(true);
		mHorAdapter.notifyDataSetChanged();
	}

	public interface TabCallback
	{
		public void setTab(UserBehaviorInfo info);
	}
}
