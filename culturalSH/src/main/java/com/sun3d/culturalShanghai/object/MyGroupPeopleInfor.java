package com.sun3d.culturalShanghai.object;

public class MyGroupPeopleInfor {
	private String peopleimgUrl;
	private String peopleName;
	private String peoplePhone;
	private String peopleId;
	private String peopleTime;
	private String peopleApplyInfo;
	private int tuserLimit;
	private String applyIsState;
	

	public String getApplyIsState() {
		return applyIsState;
	}

	public void setApplyIsState(String applyIsState) {
		this.applyIsState = applyIsState;
	}

	public int getTuserLimit() {
		return tuserLimit;
	}

	public void setTuserLimit(int tuserLimit) {
		this.tuserLimit = tuserLimit;
	}

	public String getPeopleTime() {
		return peopleTime;
	}

	public void setPeopleTime(String peopleTime) {
		this.peopleTime = peopleTime;
	}

	public String getPeopleApplyInfo() {
		return peopleApplyInfo;
	}

	public void setPeopleApplyInfo(String peopleApplyInfo) {
		this.peopleApplyInfo = peopleApplyInfo;
	}

	public String getPeopleimgUrl() {
		return peopleimgUrl;
	}

	public void setPeopleimgUrl(String peopleimgUrl) {
		this.peopleimgUrl = peopleimgUrl;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public String getPeoplePhone() {
		return peoplePhone;
	}

	public void setPeoplePhone(String peoplePhone) {
		this.peoplePhone = peoplePhone;
	}

	public String getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}

	@Override
	public String toString() {
		return "MyGroupPeopleInfor [peopleimgUrl=" + peopleimgUrl + ", peopleName=" + peopleName + ", peoplePhone=" + peoplePhone + ", peopleId=" + peopleId
				+ ", peopleTime=" + peopleTime + ", peopleApplyInfo=" + peopleApplyInfo + ", tuserLimit=" + tuserLimit + ", applyIsState=" + applyIsState + "]";
	}

}
