package com.sun3d.culturalShanghai.object;

import java.util.List;

public class WindowInfo {
	private String tabType;
	/**
	 * 类型，类别
	 */
	private List<SearchInfo> typeList;
	/**
	 * 人群
	 */
	private List<SearchInfo> crowdList;
	/**
	 * 位置
	 */
	private List<SearchInfo> adressList;
	/**
	 * 地点
	 */
	private List<SearchInfo> locationList;
	/**
	 * 属性
	 */
	private List<SearchInfo> natureList;
	/**
	 * 体系
	 */
	private List<SearchInfo> systemList;
	/**
	 * 年代
	 */
	private List<SearchInfo> timeList;

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	
	
	public List<SearchInfo> getSystemList() {
		return systemList;
	}

	public void setSystemList(List<SearchInfo> systemList) {
		this.systemList = systemList;
	}

	public List<SearchInfo> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<SearchInfo> locationList) {
		this.locationList = locationList;
	}

	public List<SearchInfo> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<SearchInfo> timeList) {
		this.timeList = timeList;
	}

	public List<SearchInfo> getNatureList() {
		return natureList;
	}

	public void setNatureList(List<SearchInfo> natureList) {
		this.natureList = natureList;
	}

	public List<SearchInfo> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<SearchInfo> typeList) {
		this.typeList = typeList;
	}

	public List<SearchInfo> getCrowdList() {
		return crowdList;
	}

	public void setCrowdList(List<SearchInfo> crowdList) {
		this.crowdList = crowdList;
	}

	public List<SearchInfo> getAdressList() {
		return adressList;
	}

	public void setAdressList(List<SearchInfo> adressList) {
		this.adressList = adressList;
	}

	@Override
	public String toString() {
		return "WindowInfo [tabType=" + tabType + ", typeList=" + typeList + ", crowdList=" + crowdList + ", adressList=" + adressList + ", locationList="
				+ locationList + ", natureList=" + natureList + "]";
	}


}
