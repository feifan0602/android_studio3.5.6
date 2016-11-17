package com.sun3d.culturalShanghai.http;

import android.os.AsyncTask;
import android.util.Log;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.Util.NetWorkUtil;
import com.sun3d.culturalShanghai.basic.service.CacheService;
import com.sun3d.culturalShanghai.object.HttpResponseText;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * 请求线程
 *
 * @author yangyoutao
 *
 */
public class HttpTask extends AsyncTask<String, Integer, String>
{
	private String TAG = "HttpTask";
	private Object httpRequest;
	private HttpRequestCallback mCallback;
	private int code;
	private int type;
	private String url;
	private String jsonStr;
	private String url_full = "";
	private boolean isNeedCache = false;

	public HttpTask(int mtype, Object mhttpRequest, HttpRequestCallback mCallback, boolean isNeedCache,String url_full)
	{
		this.type = mtype;
		this.mCallback = mCallback;
		this.httpRequest = mhttpRequest;
		this.isNeedCache = isNeedCache;
		this.url_full = url_full;
		Log.d(TAG, mhttpRequest.toString());
	}

	public HttpTask(int mtype, Object mhttpRequest, HttpRequestCallback mCallback, String url, String jsonStr,boolean isNeedCache,String url_full)
	{
		this.type = mtype;
		this.mCallback = mCallback;
		this.httpRequest = mhttpRequest;
		this.url = url;
		this.url_full = url_full;
		this.jsonStr = jsonStr;
		this.isNeedCache = isNeedCache;
	}

	@Override
	protected String doInBackground(String... arg0)
	{
		code = HttpCode.HTTP_Request_Success_CODE;
		isNeedCache = false;
		if (isNeedCache)
		{
			String requestContent = CacheService.getInstance().getCacheByTime(url_full);
			if (requestContent != null && requestContent.length() > 0)
			{
				Log.i(TAG, "from cache " + url_full + "\n" + requestContent);
				return requestContent;
			}
		}

		// TODO Auto-generated method stub
		if (!NetWorkUtil.isConnected())
		{
			code = HttpCode.HTTP_Request_Failure_CODE;
			return "网络异常，请检查网络";
		}

		HttpResponse httpResponse = null;
		String result = null;
		try
		{
			switch (this.type)
			{
			case HttpCode.HTTP_RequestType_Get:
				Log.d(TAG, "Get");
				HttpGet httpget = (HttpGet) httpRequest;
				httpResponse = HttpClientHelper.getHttpClient().execute(httpget);
				result = onStart(httpResponse);
				break;
			case HttpCode.HTTP_RequestType_Post:
				Log.d(TAG, "Post");
				HttpPost httpost = (HttpPost) httpRequest;
				httpResponse = HttpClientHelper.getHttpClient().execute(httpost);
				result = onStart(httpResponse);
				break;
			// case HttpCode.HTTP_RequestType_Json:
			// Log.d(TAG, "Json");
			// HttpRequestModel hrm = (HttpRequestModel)httpRequest;
			// HttpResponseText request =
			// MyApplication.callUrlHttpPost(hrm.getUrl(), hrm.getParam());
			// if(request != null && request.getData() != null)
			// {
			// result = request.getData();
			// code = HttpCode.HTTP_Request_Success_CODE;
			// break;
			// }
			case HttpCode.HTTP_RequestType_JSONPost:
				Log.d(TAG, "JSON");
				// result = MyApplication.callUrlHttpPost(url,
				// jsonStr).getData();
				HttpResponseText request = MyApplication.callUrlHttpPost(url, jsonStr);
				if (request != null && request.getData() != null)
				{
					result = request.getData();
					Log.i(TAG, "from net " + url_full + "\n" + result);
					if (result != null)
					{
						//CacheService.getInstance().addCache(url_full, result);
					}
					code = HttpCode.HTTP_Request_Success_CODE;
					break;
				}

			default:
				result = "TYPE is error!!";
				code = HttpCode.HTTP_Request_Failure_CODE;
				break;
			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "连接响应超时";
			Log.d(TAG, e.toString());
			code = HttpCode.HTTP_Request_Failure_CODE;
		}
		return result;
	}

	/**
	 * 数据显示
	 *
	 * @param httpResponse
	 * @return
	 */
	private String onStart(HttpResponse httpResponse)
	{
		String result;
		String result1 = HttpGetGzip.getJsonStringFromGZIP(httpResponse);
		Log.d(TAG, result1);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		{
			result = result1;
			code = HttpCode.HTTP_Request_Success_CODE;
			Log.i(TAG, "from net " + url_full + "\n" + result1);
			if (isNeedCache &&  result1 != null && result1.length() > 10)
			{
				//CacheService.getInstance().addCache(url_full, result1);
			}
		} else
		{
			// result =
			// HttpStatusUtil.httpStatus(httpResponse.getStatusLine().getStatusCode());
			result = "服务器响应错误";
			code = HttpCode.HTTP_Request_Failure_CODE;
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result)
	{
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mCallback.onPostExecute(code, result);
	}
}
