package com.sun3d.culturalShanghai.object;

public class CollectionInfor {
	private String id;
	private String collectionName;
	private String collectionTime;
	private String collectionImgUrl;
	private String collectionVeune;
	private String collectionSpec;
	private String collectionInfor;
	private String collectionMP3url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(String collectionTime) {
		this.collectionTime = collectionTime;
	}

	public String getCollectionImgUrl() {
		return collectionImgUrl;
	}

	public void setCollectionImgUrl(String collectionImgUrl) {
		this.collectionImgUrl = collectionImgUrl;
	}

	public String getCollectionVeune() {
		return collectionVeune;
	}

	public void setCollectionVeune(String collectionVeune) {
		this.collectionVeune = collectionVeune;
	}

	public String getCollectionSpec() {
		return collectionSpec;
	}

	public void setCollectionSpec(String collectionSpec) {
		this.collectionSpec = collectionSpec;
	}

	public String getCollectionInfor() {
		return collectionInfor;
	}

	public void setCollectionInfor(String collectionInfor) {
		this.collectionInfor = collectionInfor;
	}

	public String getCollectionMP3url() {
		return collectionMP3url;
	}

	public void setCollectionMP3url(String collectionMP3url) {
		this.collectionMP3url = collectionMP3url;
	}

	@Override
	public String toString() {
		return "CollectionInfor [id=" + id + ", collectionName=" + collectionName
				+ ", collectionTime=" + collectionTime + ", collectionImgUrl=" + collectionImgUrl
				+ ", collectionVeune=" + collectionVeune + ", collectionSpec=" + collectionSpec
				+ ", collectionInfor=" + collectionInfor + ", collectionMP3url=" + collectionMP3url
				+ "]";
	}

}
