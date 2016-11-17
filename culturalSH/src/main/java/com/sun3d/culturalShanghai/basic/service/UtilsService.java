package com.sun3d.culturalShanghai.basic.service;

public class UtilsService
{
	public static boolean StringIsEmpty(String str)
	{
		if(str == null  ||  str.isEmpty())
		{
			return true;
		}
		return false;
	}
	
	
	public static boolean StringIsNotEmpty(String str)
	{
		return !StringIsEmpty(str);
	}
	
	
}
