package com.sun3d.culturalShanghai.object;

import java.util.ArrayList;
import java.util.HashMap;

public class EventAddressInfo {
	private ArrayList<HashMap<String, String>> list;

	public ArrayList<HashMap<String, String>> getList() {
		return list;
	}

	public void setList(ArrayList<HashMap<String, String>> list) {
		this.list = list;
	}

	public EventAddressInfo(ArrayList<HashMap<String, String>> list) {
		super();
		this.list = list;
	}

	public EventAddressInfo() {
		super();
	}
	
}
