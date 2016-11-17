package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 主界面6个标签
 * 
 * @author liningkang
 * 
 */
public class MainGridInfo implements Serializable{
	private boolean activityNearByExist;
	private String tagImageUrl;
	private String tagId;
	private String dictCode;
	private boolean activityIsExist;
	private String tagName;

	public boolean isActivityNearByExist() {
		return activityNearByExist;
	}

	public void setActivityNearByExist(boolean activityNearByExist) {
		this.activityNearByExist = activityNearByExist;
	}

	public String getTagImageUrl() {
		return tagImageUrl;
	}

	public void setTagImageUrl(String tagImageUrl) {
		this.tagImageUrl = tagImageUrl;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public boolean isActivityIsExist() {
		return activityIsExist;
	}

	public void setActivityIsExist(boolean activityIsExist) {
		this.activityIsExist = activityIsExist;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		return "MainGridInfo [activityNearByExist=" + activityNearByExist + ", tagImageUrl=" + tagImageUrl + ", tagId=" + tagId + ", dictCode=" + dictCode
				+ ", activityIsExist=" + activityIsExist + ", tagName=" + tagName + "]";
	}

}
