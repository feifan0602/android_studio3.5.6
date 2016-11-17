package com.sun3d.culturalShanghai.basic.service;

import android.util.Log;

import java.util.List;
import java.util.Map;

public class CacheService
{
	private static CacheService instance;
	private final int CACHE_TIME = 30*60; 
	private static String TAG = "CacheUtil";
	
	public static CacheService getInstance()
	{
		synchronized (instance)
		{
			if (instance == null) {
				instance = new CacheService();
				String sql = "create table if not exists localcache(id integer primary key autoincrement,rid integer,url varchar(500),content text,crdate TIMESTAMP default (datetime('now', 'localtime')),status integer)";
				DBService.getInstance().exec(sql);
			}
		}
		return instance;
		
	}
	
	
	public void addCache(String url, String content)
	{
		url = url.replaceAll("'", "''");
		this.removeCache(url);
		String sql = "insert into localcache(url,content) values('"+url+"','"+content+"')";
		DBService.getInstance().exec(sql);
		Log.i(TAG,"addcache "+url+"\n"+content);
	}
	
	public void removeCache(String url)
	{
		url = url.replaceAll("'", "''");
		String newu = url;
		try
		{
			int fi = url.indexOf('?');
			if(fi <= 0)
				fi = url.indexOf('&');
			if(fi > 0);
			newu = url.substring(0, fi);
			
		} catch (Exception  e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sql = "delete from localcache where url like '"+newu+"%' ";
		DBService.getInstance().exec(sql);
		Log.i(TAG,"removeCache "+url);
	}

	public String getCache(String url)
	{
		url = url.replaceAll("'", "''");
		String sql = "select  content from localcache where url=? order by id desc  limit 1"; 
		String[] params = { url };		
		List<Map<String,String>>  list =  DBService.getInstance().query(sql, params);
		if(list == null || list.size() == 0)
		{
			Log.i(TAG,"getCache "+url+" is null");
			return null;
		}
		else
		{
			String res = list.get(0).get("content");
			Log.i(TAG,"getCache "+url+"\n"+res);
			return res;
		}
		
	}
	
	public String getCacheByTime(String url)
	{
		url = url.replaceAll("'", "''");
		String sql = "select  content from localcache where url=? and (strftime('%s','now','localtime')-strftime('%s',crdate)) < ?   order by id desc  limit 1"; 
		String[] params = { url ,CACHE_TIME+""};		
		List<Map<String,String>>  list =  DBService.getInstance().query(sql, params);
		if(list == null || list.size() == 0)
		{
			Log.i(TAG,"getCacheByTime "+url+" is null");
			return null;
		}
		else
		{
			String res = list.get(0).get("content");
			Log.i(TAG,"getCacheByTime "+url+"\n"+res);
			return res;
			 
		}
	}
	
	
	public void cleanCache()
	{
		DBService.getInstance().exec("delete  from localcache");
	}
	
	
//-(NSArray *)getCacheByTime:(NSString *)url
//{
//    url = [url stringByReplacingOccurrencesOfString:@"'" withString:@"''"];
//    NSString * sql = [NSString stringWithFormat:@"select content from localcache where url='%@' and (strftime('%%s','now','localtime')-strftime('%%s',crdate)) < %d   order by id desc  limit 1",url,CACHE_HALFTIME];
//    NSArray * ary =  [[DBServices shareInstance] query:sql];
//    return ary;
//}
//
 
	
	
	
}
