package com.sun3d.culturalShanghai.object;

public class Wff_BannerList {
	int advertSort;
	String advertUrl;
	String advertImgUrl;

	public int getAdvertSort() {
		return advertSort;
	}

	public void setAdvertSort(int advertSort) {
		this.advertSort = advertSort;
	}

	public String getAdvertUrl() {
		return advertUrl;
	}

	public void setAdvertUrl(String advertUrl) {
		this.advertUrl = advertUrl;
	}

	public String getAdvertImgUrl() {
		return advertImgUrl;
	}

	public void setAdvertImgUrl(String advertImgUrl) {
		this.advertImgUrl = advertImgUrl;
	}

	public Wff_BannerList(int advertSort, String advertUrl, String advertImgUrl) {
		super();
		this.advertSort = advertSort;
		this.advertUrl = advertUrl;
		this.advertImgUrl = advertImgUrl;
	}

}
