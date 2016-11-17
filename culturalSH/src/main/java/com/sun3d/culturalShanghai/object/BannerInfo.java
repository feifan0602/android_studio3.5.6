package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

public class BannerInfo implements Serializable {
	// 排序id
	private String advertPosSort;
	// 图片url
	private String advertPicUrl;
	// 广告Id
	private String advertId;
	// 广告连接地址
	private String advertConnectUrl;

	private String advertTitle;
	//分享内容
	private String advertContent;
	
	private String advertName;
	public String getAdvertName() {
		return advertName;
	}

	public void setAdvertName(String advertName) {
		this.advertName = advertName;
	}

	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}

	public String getAdvImgUrl() {
		return advImgUrl;
	}

	public void setAdvImgUrl(String advImgUrl) {
		this.advImgUrl = advImgUrl;
	}

	public int getAdvLink() {
		return advLink;
	}

	public void setAdvLink(int advLink) {
		this.advLink = advLink;
	}

	private String advUrl;
	private String advImgUrl;
	private int advLink;

	public String getAdvertContent() {
		return advertContent;
	}

	public void setAdvertContent(String advertContent) {
		this.advertContent = advertContent;
	}

	public String getAdvertTitle() {
		return advertTitle;
	}

	public void setAdvertTitle(String advertTitle) {
		this.advertTitle = advertTitle;
	}

	private Integer adverType;// 1 活动 2.场馆 3.团体

	public Integer getAdverType() {
		return adverType;
	}

	public void setAdverType(Integer adverType) {
		this.adverType = adverType;
	}

	public String getAdvertPosSort() {
		return advertPosSort;
	}

	public void setAdvertPosSort(String advertPosSort) {
		this.advertPosSort = advertPosSort;
	}

	public String getAdvertPicUrl() {
		return advertPicUrl;
	}

	public void setAdvertPicUrl(String advertPicUrl) {
		this.advertPicUrl = advertPicUrl;
	}

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
	}

	public String getAdvertConnectUrl() {
		return advertConnectUrl;
	}

	public void setAdvertConnectUrl(String advertConnectUrl) {
		this.advertConnectUrl = advertConnectUrl;
	}

	@Override
	public String toString() {
		return "BannerInfo [advertPosSort=" + advertPosSort + ", advertPicUrl="
				+ advertPicUrl + ", advertId=" + advertId
				+ ", advertConnectUrl=" + advertConnectUrl + ", advertTitle="
				+ advertTitle + ", advertContent=" + advertContent
				+ ", adverType=" + adverType + "]";
	}


}
