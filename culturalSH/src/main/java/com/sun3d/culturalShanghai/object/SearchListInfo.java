package com.sun3d.culturalShanghai.object;

import java.util.List;

public class SearchListInfo {
	// 心情
	private List<SearchInfo> moodList;
	// 人群
	private List<SearchInfo> personalList;
	// 主题
	private List<SearchInfo> themeList;
	// 类型
	private List<SearchInfo> typeList;
	//地区
	private List<SearchInfo> addresList;
	
	private List<SearchInfo> timeList;
	
	/**
	 * 这是区域
	 */
	private List<SearchInfo> classication_List;
	/**
	 * 这是分类
	 */
	private List<SearchInfo> hotword_List;
	private String startTime;
	private String endTime;
	private String money;

	

	public List<SearchInfo> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<SearchInfo> typeList) {
		this.typeList = typeList;
	}

	public List<SearchInfo> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<SearchInfo> timeList) {
		this.timeList = timeList;
	}

	public List<SearchInfo> getAddresList() {
		return addresList;
	}

	public void setAddresList(List<SearchInfo> addresList) {
		this.addresList = addresList;
	}

	public List<SearchInfo> getMoodList() {
		return moodList;
	}

	public void setMoodList(List<SearchInfo> moodList) {
		this.moodList = moodList;
	}

	public List<SearchInfo> getPersonalList() {
		return personalList;
	}

	public void setPersonalList(List<SearchInfo> personalList) {
		this.personalList = personalList;
	}

	public List<SearchInfo> getThemeList() {
		return themeList;
	}

	public void setThemeList(List<SearchInfo> themeList) {
		this.themeList = themeList;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

}
