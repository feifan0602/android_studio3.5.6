package com.sun3d.culturalShanghai.basic.service;

import android.util.Log;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DataService
{
	public static String TAG = "DataService";

	public interface ReceiveCallBack
	{
		public void dataReceviceSuccessCallBack(Object data);

		public void dataReceviceFailedCallBack(String feedback);
	}

	public static void getActivityByNear(String tagId, final ReceiveCallBack callback)
	{// 附近活动
		Map<String,String> mParams = new HashMap<String,String>();
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		mParams.put("tagId", tagId);
		
		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.WFF_APPNEARACTIVITYLIST, mParams, new HttpRequestCallback()
		{
			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				Log.i(TAG, "地图的 信息==  " + resultStr+"  地图的 状态==  "+statusCode);

				if (HttpCode.HTTP_Request_Success_CODE == statusCode)
				{

					try
					{
						callback.dataReceviceSuccessCallBack(JsonUtil.getEventList(resultStr));

					} catch (JSONException e)
					{

						e.printStackTrace();
						callback.dataReceviceFailedCallBack(resultStr);
					}
				} else
				{
					callback.dataReceviceFailedCallBack(resultStr);
				}
			}
		});
	}

	public static void getAdvertismentInActivityHome(String tagId, final ReceiveCallBack callback)
	{// 广告数据

		Map<String, String> pic_mParams = new HashMap<String, String>();
		if (tagId == null || tagId.isEmpty())
		{
			pic_mParams.put("tagId", "0");
		} else
		{
			pic_mParams.put("tagId", tagId);
		}

		MyHttpRequest.onHttpPostParams(HttpUrlList.MyEvent.WFF_APPADVERTRECOMENDLIST, pic_mParams, new HttpRequestCallback()
		{
			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{

				if (HttpCode.HTTP_Request_Success_CODE == statusCode)
				{

					Log.i(TAG, "广告位 信息==  " + resultStr);
					try
					{
						callback.dataReceviceSuccessCallBack(JsonUtil.getBanner(resultStr));

					} catch (JSONException e)
					{
						e.printStackTrace();
						callback.dataReceviceFailedCallBack("请求失败。请先选择相应标签。");
					}

				} else
				{
					callback.dataReceviceFailedCallBack(resultStr);
				}

			}
		});
	}

	public static void getActivityType(final ReceiveCallBack callback)
	{// 活动类别
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		Log.i(TAG, "getActivityType: "+HttpUrlList.EventUrl.LOOK_URL);
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params, new HttpRequestCallback()
		{

			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				if (statusCode == HttpCode.HTTP_Request_Success_CODE)
				{
					callback.dataReceviceSuccessCallBack(JsonUtil.getTypeDataList(resultStr));

				} else
				{
					callback.dataReceviceFailedCallBack(resultStr);
				}
			}
		},false);
	}

	public static void getRecommendActivity(String tagId, String venueMood, String venueArea, int sortType,String activityIsFree, String activityIsReservation,int pageNum,boolean isRefresh,final ReceiveCallBack callback)
	{//活动列表

		String urlString;
		Map<String, String> mParams = new HashMap<String, String>();
		mParams.put(HttpUrlList.HTTP_PAGE_NUM, HttpUrlList.HTTP_NUM + "");
		mParams.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
		mParams.put(HttpUrlList.HTTP_PAGE_INDEX, String.valueOf(pageNum));
		mParams.put("tagId", tagId);
		if ("".equals(tagId))
		{
			// 第一次取数据的时候
			if (venueMood == null && venueArea == null && sortType <= 0 && activityIsFree == null && activityIsReservation == null)
			{
				urlString = HttpUrlList.MyEvent.WFF_APPRECOMMENDACTIVITYLIST;
				
			} else
			{

				urlString = HttpUrlList.MyEvent.WFF_APPFILTERACTIVITYLIST;
	
			}
		} else
		{
			// 刷新的时候 这个是横向的listview 的点击事件
			urlString = HttpUrlList.MyEvent.WFF_APPTOPACTIVITYLIST;
			mParams.put("activityType", tagId);
		}

		if (UtilsService.StringIsNotEmpty(venueMood))
		{// 商圈
			mParams.put("activityLocation", venueMood);

		} else
		{
			mParams.put("activityLocation", "");
		}

		if (UtilsService.StringIsNotEmpty(venueArea))
		{// 区域
			mParams.put("activityArea", venueArea);
		} else
		{
			mParams.put("activityArea", "");
		}
		if (sortType > 0)
		{
			mParams.put("sortType", sortType + "");

		} else
		{
			mParams.put("sortType", "");
		}
		if (UtilsService.StringIsNotEmpty(activityIsFree))
		{
			mParams.put("activityIsFree", activityIsFree);
		} else
		{
			mParams.put("activityIsFree", "");
		}

		if (UtilsService.StringIsNotEmpty(activityIsReservation))
		{
			mParams.put("activityIsReservation", activityIsReservation);
		} else
		{
			mParams.put("activityIsReservation", "");
		}

		if (AppConfigUtil.LocalLocation.Location_latitude.equals("0.0") || AppConfigUtil.LocalLocation.Location_longitude.equals("0.0") || AppConfigUtil.LocalLocation.Location_latitude.equals("") || AppConfigUtil.LocalLocation.Location_longitude.equals(""))
		{
			mParams.put(HttpUrlList.HTTP_LAT, MyApplication.Location_latitude);
			mParams.put(HttpUrlList.HTTP_LON, MyApplication.Location_longitude);
		} else
		{
			mParams.put(HttpUrlList.HTTP_LAT, AppConfigUtil.LocalLocation.Location_latitude + "");
			mParams.put(HttpUrlList.HTTP_LON, AppConfigUtil.LocalLocation.Location_longitude + "");
		}
		MyApplication.total_num(urlString, "appRecommendActivityList", "");

		MyHttpRequest.onHttpPostParams(urlString, mParams, new HttpRequestCallback()
		{
			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				// TODO Auto-generated method stub
				if (HttpCode.HTTP_Request_Success_CODE == statusCode)
				{
					try
					{
						callback.dataReceviceSuccessCallBack(JsonUtil.getEventList(resultStr));
						
					} catch (JSONException e)
					{
						e.printStackTrace();
						callback.dataReceviceFailedCallBack(e.toString());
					}
				} else
				{
					callback.dataReceviceFailedCallBack(resultStr);
					
				}
				
			}
		});
	}
	
	public static void getCityArea(final ReceiveCallBack callback)
	{//获取区域
		
		Map<String, String> mParams = new HashMap<String, String>();
		String area = SharedPreManager.getAllArea();
		if (area != null&&MyApplication.activity_bool==false)
		{
			callback.dataReceviceSuccessCallBack(area);
			return;
		}
		MyHttpRequest.onStartHttpGET(HttpUrlList.Venue.WFF_GETALLAREA, mParams, new HttpRequestCallback()
		{

			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				if (HttpCode.HTTP_Request_Success_CODE == statusCode)
				{
					try
					{
						
						JSONObject json = new JSONObject(resultStr);
						
						String status = json.optString("status");
						if (status.equals("200"))
						{
							if (resultStr.length() > 100)
							{
								callback.dataReceviceSuccessCallBack(resultStr);
							}
							
						}
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}

		},false);
	}
	

}
