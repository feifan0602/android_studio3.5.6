package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ActivityConditionInfo implements Serializable {
	private String activityStartTime = "";// 活动开始时间（可选）
	private String activityEndTime = "";// 活动结束时间（可选）
	private String activityType = "";// 活动主题标签id（可选）
	private String activityMood = "";// 活动心情标签id（可选）
	private String activityCrowd = "";// 活动人群标签id（可选）
	private String activityKeyWord = "";// 搜索活动（可选）
	private String areaCode = "";
	private String timeType = "";
	private String activityTheme="";
	private String tabType = "1";
	/**
	 * 以下暂时不用
	 */
	private String activityPrice = "";// 活动费用（可选） 1.代表0 2.代表0-100 3.代表100-200
										// 4.代表200以上

	public String getActivityStartTime() {
		return activityStartTime;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public String getActivityTheme() {
		return activityTheme;
	}

	public void setActivityTheme(String activityTheme) {
		this.activityTheme = activityTheme;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityMood() {
		return activityMood;
	}

	public void setActivityMood(String activityMood) {
		this.activityMood = activityMood;
	}

	public String getActivityCrowd() {
		return activityCrowd;
	}

	public void setActivityCrowd(String activityCrowd) {
		this.activityCrowd = activityCrowd;
	}

	public String getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(String activityPrice) {
		this.activityPrice = activityPrice;
	}

	public String getActivityKeyWord() {
		return activityKeyWord;
	}

	public void setActivityKeyWord(String activityKeyWord) {
		this.activityKeyWord = activityKeyWord;
	}

	@Override
	public String toString() {
		return "ActivityConditionInfo [activityStartTime=" + activityStartTime
				+ ", activityEndTime=" + activityEndTime + ", activityType=" + activityType
				+ ", activityMood=" + activityMood + ", activityCrowd=" + activityCrowd
				+ ", activityKeyWord=" + activityKeyWord + ", areaCode=" + areaCode + ", timeType="
				+ timeType + ", activityTheme=" + activityTheme + ", tabType=" + tabType
				+ ", activityPrice=" + activityPrice + "]";
	}

	public void clear() {
		setActivityCrowd("");
		setActivityEndTime("");
		setActivityKeyWord("");
		setActivityMood("");
		setActivityPrice("");
		setActivityStartTime("");
		setActivityType("");
		setActivityTheme("");
		setAreaCode("");
		setTabType("1");
		setTimeType("");
	}

}
