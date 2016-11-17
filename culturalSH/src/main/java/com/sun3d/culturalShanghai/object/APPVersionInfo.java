package com.sun3d.culturalShanghai.object;

public class APPVersionInfo {
	private String externalVnumber;// 外部版本号
	private int ServiceVersionNumber;// 内部版本号 用户进行APP比对，比对参数配置文件versionCode
	private String updateUrl;// 更新地址
	private String versionUpdateUser;// 安卓版本更新人
	private String versionUpdateTime;// 安卓版本更新时间
	private String versionCreateTime;// 安卓版本创建时间
	private String versionCreateUser;// 安卓版本创建人
	private String updateDescr;// 更新描述
	private Boolean isForcedUpdate = false;// 是否强制更新

	public String getExternalVnumber() {
		return externalVnumber;
	}

	public void setExternalVnumber(String externalVnumber) {
		this.externalVnumber = externalVnumber;
	}

	public int getServiceVersionNumber() {
		return ServiceVersionNumber;
	}

	public void setServiceVersionNumber(int serviceVersionNumber) {
		ServiceVersionNumber = serviceVersionNumber;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getVersionUpdateUser() {
		return versionUpdateUser;
	}

	public void setVersionUpdateUser(String versionUpdateUser) {
		this.versionUpdateUser = versionUpdateUser;
	}

	public String getVersionUpdateTime() {
		return versionUpdateTime;
	}

	public void setVersionUpdateTime(String versionUpdateTime) {
		this.versionUpdateTime = versionUpdateTime;
	}

	public String getVersionCreateTime() {
		return versionCreateTime;
	}

	public void setVersionCreateTime(String versionCreateTime) {
		this.versionCreateTime = versionCreateTime;
	}

	public String getVersionCreateUser() {
		return versionCreateUser;
	}

	public void setVersionCreateUser(String versionCreateUser) {
		this.versionCreateUser = versionCreateUser;
	}

	public String getUpdateDescr() {
		return updateDescr;
	}

	public void setUpdateDescr(String updateDescr) {
		this.updateDescr = updateDescr;
	}

	public Boolean getIsForcedUpdate() {
		return isForcedUpdate;
	}

	public void setIsForcedUpdate(Boolean isForcedUpdate) {
		this.isForcedUpdate = isForcedUpdate;
	}

}
