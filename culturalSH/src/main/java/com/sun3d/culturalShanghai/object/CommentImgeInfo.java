package com.sun3d.culturalShanghai.object;

public class CommentImgeInfo {
	private int id;
	private String localhostPath;
	private String serverUrl;
	private Boolean isImagePic = true;

	public Boolean getIsImagePic() {
		return isImagePic;
	}

	public void setIsImagePic(Boolean isImagePic) {
		this.isImagePic = isImagePic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocalhostPath() {
		return localhostPath;
	}

	public void setLocalhostPath(String localhostPath) {
		this.localhostPath = localhostPath;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

}
