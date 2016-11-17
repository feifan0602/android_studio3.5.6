package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 用户选择类型
 * 
 * @author liningkang
 * 
 */
public class UserBehaviorInfo implements Serializable {
	private String sex;
	private int tvBackground;
	private int age;
	private boolean isSelect;
	private String tagId;
	private String tagImageUrl;
	private String tagName;
	private String activityType = "";
	private String activityTheme = "";
	private boolean isIndex;
	private boolean isShowVal = false;

	public UserBehaviorInfo() {
		// TODO Auto-generated constructor stub
	}

	public UserBehaviorInfo(String tagId, String tagName) {
		// TODO Auto-generated constructor stub
		this.tagId = tagId;
		this.tagName = tagName;
	}

	public boolean isShowVal() {
		return isShowVal;
	}

	public void setShowVal(boolean isShowVal) {
		this.isShowVal = isShowVal;
	}

	public UserBehaviorInfo(int tvBg) {
		// TODO Auto-generated constructor stub
		this.tvBackground = tvBg;
	}

	public boolean isIndex() {
		return isIndex;
	}

	public void setIndex(boolean isIndex) {
		this.isIndex = isIndex;
	}

	public UserBehaviorInfo(int age, int tvBg) {
		// TODO Auto-generated constructor stub
		this.age = age;
		this.tvBackground = tvBg;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagImageUrl() {
		return tagImageUrl;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityTheme() {
		return activityTheme;
	}

	public void setActivityTheme(String activityTheme) {
		this.activityTheme = activityTheme;
	}

	public void setTagImageUrl(String tagImageUrl) {
		this.tagImageUrl = tagImageUrl;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getTvBackground() {
		return tvBackground;
	}

	public void setTvBackground(int tvBackground) {
		this.tvBackground = tvBackground;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "UserBehaviorInfo [sex=" + sex + ", tvBackground=" + tvBackground + ", age=" + age + ", isSelect=" + isSelect + ", tagId=" + tagId
				+ ", tagImageUrl=" + tagImageUrl + ", tagName=" + tagName + ", activityType=" + activityType + ", activityTheme=" + activityTheme + "]";
	}

}
