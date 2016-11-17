package com.sun3d.culturalShanghai.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.object.HttpRequestModel;

import android.util.Log;

/**
 * Http 请求线程
 *
 * @author yangyoutao
 *
 */
public class MyHttpRequest
{
	private static String TAG = "MyHttpRequest";
	private static HttpGet httpGet;

	private static boolean needCacheDefaultValue = false;

	private static void initHttpGET(String url, Map<String, String> params)
	{
		httpGet = null;
		if (null == url)
		{
			Log.i(TAG, "url is null!");
			return;
		}
		if (params == null)
		{
			httpGet = new HttpGet(url);
			Log.i(TAG, url);
		} else
		{
			params.put(HttpUrlList.HTTP_TOKEN_KEY, HttpUrlList.HTTP_TOKEN_VALUE);
			Log.i(TAG, getParams(url, params));
			try
			{
				httpGet = new HttpGet(getParams(url, params));
			} catch (Exception e)
			{
				e.printStackTrace();
				Log.d(TAG, e.toString());
				ToastUtil.showToast("不能输入非法符号。");
			}
		}
	}

	/**
	 * url服务器地址 ，params发送参数列表，mCallback回调接口
	 *
	 * @param url
	 * @param params
	 * @param mCallback
	 */
	public static void onHttpPostParams(String url, Map<String, String> params, HttpRequestCallback mCallback,
			boolean isNeedCache)
	{
		if (mCallback == null)
		{
			Log.i(TAG, " mCallback is null!");
			return;
		}
		onStartHttpPostParams(url, params, mCallback, isNeedCache);

	}

	@SuppressWarnings("unused")
	public static void onHttpPostParams(String url, Map<String, String> params, HttpRequestCallback mCallback)
	{
		if (mCallback == null)
		{
			Log.i(TAG, " mCallback is null!");
			return;
		}
		onStartHttpPostParams(url, params, mCallback);

	}

	@SuppressWarnings("unused")
	public static void onHttpPostParamsNoLat(String url, Map<String, String> params, HttpRequestCallback mCallback)
	{
		if (mCallback == null)
		{
			Log.i(TAG, " mCallback is null!");
			return;
		}
		onStartHttpPostParamsNoLat(url, params, mCallback);

	}

	/**
	 * url服务器地址 ，params发送参数列表，mCallback回调接口
	 *
	 * @param url
	 * @param params
	 * @param mCallback
	 */
	public static void onStartHttpGET(String url, Map<String, String> params, HttpRequestCallback mCallback)
	{
		onStartHttpGET(url, params, mCallback, needCacheDefaultValue);
	}

	@SuppressWarnings("unused")
	public static void onStartHttpGET(String url, Map<String, String> params, HttpRequestCallback mCallback,
			boolean isNeedCache)
	{
		if (mCallback == null)
		{
			Log.i(TAG, " mCallback is null!");
			return;
		}
		initHttpGET(url, params);
		if (null != httpGet)
		{
			httpGet.addHeader("Accept-Encoding", "gzip");
			new HttpTask(HttpCode.HTTP_RequestType_Get, httpGet, mCallback, isNeedCache, url).execute();
		}
	}

	/**
	 * url服务器地址 ，jSONObject发送参数对象，mCallback回调接口
	 *
	 * @param url
	 * @param jSONObject
	 * @param mCallback
	 */
	public static void onStartHttpPostJSON(String url, JSONObject jSONObject, HttpRequestCallback mCallback)
	{
		onStartHttpPostJSON(url, jSONObject, mCallback, needCacheDefaultValue);
	}

	public static void onStartHttpPostJSON(String url, JSONObject jSONObject, HttpRequestCallback mCallback,
			boolean isNeedCache)
	{
		HttpPost httpost;
		String url_full = url;
		if (null == url || null == mCallback)
		{
			Log.i(TAG, "url or mCallback is null!");
			return;
		}

		httpost = new HttpPost(url);
		Log.i(TAG, url);
		if (jSONObject != null)
		{
			Log.d(TAG, jSONObject.toString());
			try
			{
				httpost.setEntity(new StringEntity(jSONObject.toString(), "utf-8"));
			} catch (UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.i(TAG, e.toString());
			}
		}
		httpost.addHeader("Accept-Encoding", "gzip");
		new HttpTask(HttpCode.HTTP_RequestType_JSONPost, httpost, mCallback, url, jSONObject.toString(), isNeedCache, url_full + jSONObject.toString()).execute();
		// new HttpTask(HttpCode.HTTP_RequestType_Json, httpost,
		// mCallback).execute();
	}

	/**
	 * url服务器地址 ，Map发送参数对象，mCallback回调接口
	 *
	 * @param url
	 * @param Map
	 * @param mCallback
	 */
	public static void onStartHttpPostParamsNoLat(String url, Map<String, String> params, HttpRequestCallback mCallback)
	{
		onStartHttpPostParamsNoLat(url, params, mCallback, needCacheDefaultValue);

	}

	public static void onStartHttpPostParamsNoLat(String url, Map<String, String> params, HttpRequestCallback mCallback,
			boolean isNeedCache)
	{
		HttpPost httpost;
		if (null == url || null == mCallback)
		{
			Log.i(TAG, "url or mCallback is null!");
			return;
		}
		List<NameValuePair> paramsList = null;
		String Params = "";
		if (params != null)
		{

			paramsList = new ArrayList<NameValuePair>();
			for (String str : params.keySet())
			{
				if (Params.length() == 0)
				{
					Params += "?" + str + "=" + params.get(str).toString();
				} else
				{
					Params += "&" + str + "=" + params.get(str).toString();
				}
				paramsList.add(new BasicNameValuePair(str, params.get(str).toString()));
			}
			Log.d(TAG + "Params", url + Params);
		}

		httpost = new HttpPost(url);
		try
		{
			if (paramsList != null && paramsList.size() > 0)
			{
				/* 发出HTTP request */
				httpost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, e.toString());
		}
		httpost.addHeader("Accept-Encoding", "gzip");
		new HttpTask(HttpCode.HTTP_RequestType_Post, httpost, mCallback, isNeedCache, url + Params).execute();
		// new HttpTask(HttpCode.HTTP_RequestType_Post, httpost,
		// mCallback).execute();
	}

	/**
	 * url服务器地址 ，Map发送参数对象，mCallback回调接口
	 *
	 * @param url
	 * @param Map
	 * @param mCallback
	 */
	public static void onStartHttpPostParams(String url, Map<String, String> params, HttpRequestCallback mCallback)
	{
		onStartHttpPostParams(url, params, mCallback, needCacheDefaultValue);
	}

	public static void onStartHttpPostParams(String url, Map<String, String> params, HttpRequestCallback mCallback,
			boolean isNeedCache)
	{
		HttpPost httpost;
		if (null == url || null == mCallback)
		{
			Log.i(TAG, "url or mCallback is null!");
			return;
		}
		List<NameValuePair> paramsList = null;
		String Params_str = "";
		if (params != null)
		{

			paramsList = new ArrayList<NameValuePair>();
			for (String str : params.keySet())
			{
				if (Params_str.length() == 0)
				{
					Params_str += "?" + str + "=" + params.get(str).toString();
				} else
				{
					Params_str += "&" + str + "=" + params.get(str).toString();
				}

				paramsList.add(new BasicNameValuePair(str, params.get(str).toString()));
			}
			Log.d(TAG + "Params", url + Params_str);
		}

		httpost = new HttpPost(url);
		try
		{
			if (paramsList != null && paramsList.size() > 0)
			{
				/* 发出HTTP request */
				httpost.setEntity(new UrlEncodedFormEntity(paramsList, HTTP.UTF_8));
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, e.toString());
		}

		httpost.addHeader("Accept-Encoding", "gzip");
		httpost.addHeader("platform", "android");
		// httpost.addHeader("userId", "android");
		// httpost.addHeader("userLon", "android");
		// httpost.addHeader("userLat", "android");
		httpost.addHeader("version", MyApplication.version);
		new HttpTask(HttpCode.HTTP_RequestType_Post, httpost, mCallback, isNeedCache, url + Params_str).execute();
	}

	/**
	 * 拼接get多个参数
	 *
	 * @param path
	 * @param params
	 * @return
	 */
	public static String getParams(String path, Map<String, String> params)
	{
		StringBuffer sb = new StringBuffer();
		Log.i(TAG, sb.toString());
		sb.append(path);
		sb.append("?");
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			sb.append(entry.getKey());
			sb.append("=");
			String str = entry.getValue().replaceAll("\n", "");
			String str1 = str.replaceAll(" ", "");
			sb.append(str1);
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		Log.i(TAG, sb.toString());
		return sb.toString();
	}

	/**
	 * 3.5.4
	 *
	 * @param url
	 * @param params
	 * @param mCallback
	 */
	public static void onHttpPostJsonStr(String url, Map<String, String> params, HttpRequestCallback mCallback)
	{
		HttpPost httpost;
		String json;
		if (null == url || null == mCallback)
		{
			Log.i(TAG, "url or mCallback is null!");
			return;
		}

		httpost = new HttpPost(url);
		Log.i(TAG, url);
		if (params == null)
		{
			json = "";
		} else
		{
			json = MyHttpRequest.hashMapToJson(params);
		}

		HttpRequestModel hrm = new HttpRequestModel();
		hrm.setUrl(url);
		hrm.setParam(json);

		// new HttpTask(HttpCode.HTTP_RequestType_Json, hrm,
		// mCallback).execute();
	}

	public static String hashMapToJson(Map<String, String> params)
	{
		String string = "{";
		for (Iterator it = params.entrySet().iterator(); it.hasNext();)
		{
			Entry e = (Entry) it.next();
			string += "\"" + e.getKey() + "\":";
			string += "\"" + e.getValue() + "\",";
		}
		string = string.substring(0, string.lastIndexOf(","));
		string += "}";
		return string;
	}

	public static void main(String[] args)
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("keyword", "你好");

		// MyHttpRequest.onHttpPostParams(HttpUrlList.SearchUrl.URL_GET_SK_MATCH,
		// params, new HttpRequestCallback()
		MyHttpRequest.onHttpPostJsonStr(HttpUrlList.SearchUrl.URL_GET_SK_MATCH, params, new HttpRequestCallback()
		{

			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{

				Log.i(TAG, "看看返回的数据==   " + resultStr);
				// TODO Auto-generated method stub
				if (statusCode == HttpCode.HTTP_Request_Success_CODE)
				{
					JSONObject json;
					try
					{
						json = new JSONObject(resultStr);
						JSONObject data = json.optJSONObject("data");
						JSONArray ary = data.optJSONArray("data");
						System.out.println("--");

					} catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}

}
