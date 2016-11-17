package com.sun3d.culturalShanghai.basic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBService
{
	private static DBService instance;
	private static SQLiteDatabase db;
	private static String TAG = "DBUtils";

	public static  DBService getInstance()
	{
		if (instance == null)
		{
			instance = new DBService();
		}
		return instance;
	}

	public DBService()
	{
		db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.sun3d.culturalShanghai/databases/fishbird.db",  null);
	}

	public boolean exec(String sql)
	{
		synchronized (db)
		{
			try
			{
				db.execSQL(sql);

			} catch (Exception e)
			{
				Log.i(TAG, e.toString());
				return false;
			}
			
		}
		return true;
	}


	public List<Map<String, String>> query(String sql, String[] args)
	{
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Cursor c = null;
		try
		{
			c = db.rawQuery(sql, args);
			while (c.moveToNext())
			{
				String[] columns = c.getColumnNames();
				HashMap<String,String> map = new HashMap<String,String>();
				for(int i=0;i<columns.length;i++)
				{
					map.put(columns[i],	c.getString(c.getColumnIndex(columns[i])));
				}
				list.add(map);
			}
			
		} catch (Exception e)
		{
			Log.i(TAG, e.toString());
		}
		finally 
		{
			if (c != null)
			{
				c.close();
			}
		}

		return list;
	}

}
