package com.sun3d.culturalShanghai.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.basic.service.CacheService;
import com.sun3d.culturalShanghai.basic.service.DBService;
import com.sun3d.culturalShanghai.object.HttpResponseText;

import android.test.AndroidTestCase;
import android.util.Log;

public class Test extends AndroidTestCase
{

	public static void CompressedFiles_Gzip(String folderPath, String targzipFilePath, String targzipFileName)
	{
		File srcPath = new File(folderPath);
		int length = srcPath.listFiles().length;
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		File[] files = srcPath.listFiles();
//		try
//		{
			// 建立压缩文件输出流
//			FileOutputStream fout = new FileOutputStream(targzipFilePath);
//			// 建立tar压缩输出流
//			TarOutputStream tout = new TarOutputStream(fout);
//			for (int i = 0; i < length; i++)
//			{
//				String filename = srcPath.getPath() + File.separator + files[i].getName();
//				// 打开需压缩文件作为文件输入流
//				FileInputStream fin = new FileInputStream(filename); // filename是文件全路径
//				TarEntry tarEn = new TarEntry(files[i]); // 此处必须使用new
//															// TarEntry(File
//															// file);
//				tarEn.setName(files[i].getName()); // 此处需重置名称，默认是带全路径的，否则打包后会带全路径
//				tout.putNextEntry(tarEn);
//				int num;
//				while ((num = fin.read(buf)) != -1)
//				{
//					tout.write(buf, 0, num);
//				}
//				tout.closeEntry();
//				fin.close();
//			}
//			tout.close();
//			fout.close();
//
//			// 建立压缩文件输出流
//			FileOutputStream gzFile = new FileOutputStream(targzipFilePath + ".gz");
//			// 建立gzip压缩输出流
//			GZIPOutputStream gzout = new GZIPOutputStream(gzFile);
//			// 打开需压缩文件作为文件输入流
//			FileInputStream tarin = new FileInputStream(targzipFilePath); // targzipFilePath是文件全路径
//			int len;
//			while ((len = tarin.read(buf)) != -1)
//			{
//				gzout.write(buf, 0, len);
//			}
//			gzout.close();
//			gzFile.close();
//			tarin.close();
//		} catch (FileNotFoundException e)
//		{
//			System.out.println(e);
//		} catch (IOException e)
//		{
//			System.out.println(e);
//		}

	}

	public void ttt()
	{
		// String synctype = request.getParameter("type");
		// if (synctype == null || synctype.length() == 0 ||
		// synctype.equals("db"))
		// {
		// File f = new File("/ddmap/dbbak/");
		//
		// if (f.isDirectory())
		// {
		// File[] files = root.listFiles();
		// for (File file : files)
		// {
		// if (file.isDirectory())
		// {
		// getFiles(file.getAbsolutePath());
		// filelist.add(file.getAbsolutePath());
		// System.out.println("显示" + filePath + "下所有子目录及其文件" +
		// file.getAbsolutePath());
		// } else
		// {
		// System.out.println("显示" + filePath + "下所有子目录" +
		// file.getAbsolutePath());
		// }
		// }
		// }
		// }
	}

	public void testCache()
	{
		String url = "http://google.com?sdfljk=1000";
		String content = "this is cache test";

		List<Map<String, String>> list1 = DBService.getInstance().query("select count(*) as cou from localcache", null);
		CacheService.getInstance().addCache(url, content);

		// String content = CacheUtil.getInstance().getCacheByTime(url);

		System.out.println("test");
	}

	public void testDB()
	{
		// String sql = "create table if not exists localcache(id integer
		// primary key autoincrement,rid integer,url varchar(500),content
		// text,crdate TIMESTAMP default (datetime('now', 'localtime')),status
		// integer)";
		DBService dbutil = DBService.getInstance();
		// dbutil.exec(sql);
		String sql = "insert into localcache(url,content) values('http://google.com','contentcontent')";
		dbutil.exec(sql);
		dbutil.query("select * from localcache", null);

	}

	public void test()
	{
		String url = "http://m.wenhuayun.cn/activity/searchActivity";
		String json = "{\"keyword\":\"中华艺术宫\"}";
		HttpResponseText text = MyApplication.callUrlHttpPost(url, json);

		JSONObject jo;
		try
		{
			jo = new JSONObject(text.getData());

			JSONArray arr = jo.getJSONArray("data");
			for (int i = 0; i < arr.length(); i++)
			{
				String ii = arr.optString(i);
				System.out.println(i);
			}

		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.i("-test-", text.getData());
	}
}
