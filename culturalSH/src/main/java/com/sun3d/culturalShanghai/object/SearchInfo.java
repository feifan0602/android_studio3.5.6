package com.sun3d.culturalShanghai.object;

public class SearchInfo {
	private String tagId;
	private String tagName;
	private String tagImageUrl;
	private String areaId;
	private boolean isSeleced;
	private boolean isAddSeleced;
	
	


	public String getAreaId() {
		return areaId;
	}


	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}


	public SearchInfo() {
	}

	
	public boolean isAddSeleced() {
		return isAddSeleced;
	}


	public void setAddSeleced(boolean isAddSeleced) {
		this.isAddSeleced = isAddSeleced;
	}


	public SearchInfo(String tagId, String tagName) {
		this.tagId = tagId;
		this.tagName = tagName;
	}

	public SearchInfo(String tagName) {
		this.tagName = tagName;
	}

	public boolean isSeleced() {
		return isSeleced;
	}

	public void setSeleced(boolean isSeleced) {
		this.isSeleced = isSeleced;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagImageUrl() {
		return tagImageUrl;
	}

	public void setTagImageUrl(String tagImageUrl) {
		this.tagImageUrl = tagImageUrl;
	}


	@Override
	public String toString() {
		return "SearchInfo [tagId=" + tagId + ", tagName=" + tagName
				+ ", tagImageUrl=" + tagImageUrl + ", isSeleced=" + isSeleced
				+ ", isAddSeleced=" + isAddSeleced + "]";
	}



}
