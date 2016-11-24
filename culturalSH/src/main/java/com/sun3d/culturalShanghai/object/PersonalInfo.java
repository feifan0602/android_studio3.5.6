package com.sun3d.culturalShanghai.object;

public class PersonalInfo {

	String title;
	int icon;
	int info;
	String status;

	public int getFlag() {
		return mFlag;
	}

	public void setFlag(int flag) {
		mFlag = flag;
	}

	int mFlag;
	public PersonalInfo(String title, int icon, int info, String status,int flag) {
		super();
		this.title = title;
		this.icon = icon;
		this.info = info;
		this.status = status;
		this.mFlag=flag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getInfo() {
		return info;
	}

	public void setInfo(int info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "PersonalInfo [title=" + title + ", icon=" + icon + ", info="
				+ info + "]";
	}

}
