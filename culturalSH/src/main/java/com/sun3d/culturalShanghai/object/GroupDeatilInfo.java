package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GroupDeatilInfo implements Serializable {
	private String GroupImgUrl;
	private String GroupName;
	private String GroupType;
	private String GroupArea;
	private String GroupMaxPople;
	private String GroupDetail;
	private String GroupAdmin;
	private String GroupId;
	private Integer collectNum = 0;
	private Boolean isCollect;
	private Boolean isUserJion;
	private int applyCheckState;// 1-审核中 2-未通过 3-已通过 4-已退出
	private String applyId;
	private String checkCount;
	private String tuserMobileNo;

	public String getTuserMobileNo() {
		return tuserMobileNo;
	}

	public void setTuserMobileNo(String tuserMobileNo) {
		this.tuserMobileNo = tuserMobileNo;
	}

	public String getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(String checkCount) {
		this.checkCount = checkCount;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public int getApplyCheckState() {
		return applyCheckState;
	}

	public void setApplyCheckState(int applyCheckState) {
		this.applyCheckState = applyCheckState;
	}

	public String getGroupAdmin() {
		return GroupAdmin;
	}

	public void setGroupAdmin(String groupAdmin) {
		GroupAdmin = groupAdmin;
	}

	public Boolean getIsUserJion() {
		return isUserJion;
	}

	public void setIsUserJion(Boolean isUserJion) {
		this.isUserJion = isUserJion;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Boolean getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Boolean isCollect) {
		this.isCollect = isCollect;
	}

	public String getGroupImgUrl() {
		return GroupImgUrl;
	}

	public void setGroupImgUrl(String groupImgUrl) {
		GroupImgUrl = groupImgUrl;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setGroupName(String groupName) {
		GroupName = groupName;
	}

	public String getGroupType() {
		return GroupType;
	}

	public void setGroupType(String groupType) {
		GroupType = groupType;
	}

	public String getGroupArea() {
		return GroupArea;
	}

	public void setGroupArea(String groupArea) {
		GroupArea = groupArea;
	}

	public String getGroupMaxPople() {
		return GroupMaxPople;
	}

	public void setGroupMaxPople(String groupMaxPople) {
		GroupMaxPople = groupMaxPople;
	}

	public String getGroupDetail() {
		return GroupDetail;
	}

	public void setGroupDetail(String groupDetail) {
		GroupDetail = groupDetail;
	}

	public String getGroupId() {
		return GroupId;
	}

	public void setGroupId(String groupId) {
		GroupId = groupId;
	}

}
