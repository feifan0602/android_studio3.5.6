package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

public class NewInfo implements Serializable {
	private String newId;
	private String newsReportUser;
	private String newReportTime;
	private String newsVideoUrl;
	private String newType;
	private String newsImgUrl;
	private String newsDesc;
	private String newsTitle;

	public String getNewId() {
		return newId;
	}

	public void setNewId(String newId) {
		this.newId = newId;
	}

	public String getNewsReportUser() {
		return newsReportUser;
	}

	public void setNewsReportUser(String newsReportUser) {
		this.newsReportUser = newsReportUser;
	}

	public String getNewReportTime() {
		return newReportTime;
	}

	public void setNewReportTime(String newReportTime) {
		this.newReportTime = newReportTime;
	}

	public String getNewsVideoUrl() {
		return newsVideoUrl;
	}

	public void setNewsVideoUrl(String newsVideoUrl) {
		this.newsVideoUrl = newsVideoUrl;
	}

	public String getNewType() {
		return newType;
	}

	public void setNewType(String newType) {
		this.newType = newType;
	}

	public String getNewsImgUrl() {
		return newsImgUrl;
	}

	public void setNewsImgUrl(String newsImgUrl) {
		this.newsImgUrl = newsImgUrl;
	}

	public String getNewsDesc() {
		return newsDesc;
	}

	public void setNewsDesc(String newsDesc) {
		this.newsDesc = newsDesc;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

}
