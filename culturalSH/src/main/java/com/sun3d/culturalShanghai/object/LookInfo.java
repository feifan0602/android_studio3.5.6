package com.sun3d.culturalShanghai.object;

import java.util.List;

public class LookInfo {
	private int number;
	private int status;
	private List<LookInfo> data;
	private int activityNums;
	private String tagName;
	private String tagId;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<LookInfo> getData() {
		return data;
	}

	public void setData(List<LookInfo> data) {
		this.data = data;
	}


	public int getActivityNums() {
		return activityNums;
	}

	public void setActivityNums(int activityNums) {
		this.activityNums = activityNums;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

}
