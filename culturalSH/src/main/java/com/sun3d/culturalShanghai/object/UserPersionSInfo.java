package com.sun3d.culturalShanghai.object;

public class UserPersionSInfo {
	public static String totalNum = "0";
	private String Name;
	private String headUrl;
	private String persionId;
	private String bithryDay;
	private String sex;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getPersionId() {
		return persionId;
	}

	public void setPersionId(String persionId) {
		this.persionId = persionId;
	}

	public String getBithryDay() {
		return bithryDay;
	}

	public void setBithryDay(String bithryDay) {
		this.bithryDay = bithryDay;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

}
