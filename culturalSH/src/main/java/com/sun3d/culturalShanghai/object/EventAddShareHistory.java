package com.sun3d.culturalShanghai.object;

import java.util.ArrayList;
import java.util.HashMap;

public class EventAddShareHistory {
	private ArrayList<HashMap<String, String>> list;

	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}

	public void setList(ArrayList<HashMap<String, String>> list) {
		this.list = list;
	}

	public EventAddShareHistory(ArrayList<SearchListViewInfo> list) {
		super();
		if(list == null)
		{
			return;
		}
		
		this.list = new ArrayList<HashMap<String, String>>();
		for(SearchListViewInfo info : list)
		{
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tv", info.getAddress_Str());
			map.put("type", info.getType()+"");
			this.list.add(map);
		}
		
		
	}

	public EventAddShareHistory() {
		super();
	}
	
}
