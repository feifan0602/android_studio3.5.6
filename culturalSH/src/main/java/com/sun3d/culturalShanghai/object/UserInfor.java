package com.sun3d.culturalShanghai.object;

public class UserInfor {
	private int loginType = 0;
	private String userCardNo = "";
	private String registerCode = "";
	private String token = "";
	private String userAddress = "";
	private String userRemark = "";
	private int registerCount;
	private int commentStatus;

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public String getUserCardNo() {
		return userCardNo;
	}

	public void setUserCardNo(String userCardNo) {
		this.userCardNo = userCardNo;
	}

	public String getRegisterCode() {
		return registerCode;
	}

	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public int getRegisterCount() {
		return registerCount;
	}

	public void setRegisterCount(int registerCount) {
		this.registerCount = registerCount;
	}

	public int getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(int commentStatus) {
		this.commentStatus = commentStatus;
	}

	private String userId = "";
	private Integer userAge = 0;
	private String userArea = "";
	private String userBirth = "";
	private String userCity = "";
	private String userEmail = "";
	private String userIsDisable = "";
	private String userHeadImgUrl = "";
	private String userMobileNo = "";
	private String userName = "";
	private String userNickName = "";
	private String userProvince = "";
	private String userPwd = "";
	private String userQq = "";
	private String userSex = "";
	private String userType = "";
	private String createTime = "";
	private String userGroupPictrueUrl = "";
	private String userTelephone = "";
	private String registerOrigin = "";// 注册来源，1为文化云平台，2为QQ 3为新浪微博
										// 4为微信(更改为记录登录的方式)
	private String userIsLogin = "";

	public String getUserTelephone() {
		return userTelephone;
	}

	public void setUserTelephone(String userTelephone) {
		this.userTelephone = userTelephone;
	}

	public String getRegisterOrigin() {
		return registerOrigin;
	}

	public void setRegisterOrigin(String registerOrigin) {
		this.registerOrigin = registerOrigin;
	}

	public String getUserIsLogin() {
		return userIsLogin;
	}

	public void setUserIsLogin(String userIsLogin) {
		this.userIsLogin = userIsLogin;
	}

	public String getUserGroupPictrueUrl() {
		return userGroupPictrueUrl;
	}

	public void setUserGroupPictrueUrl(String userGroupPictrueUrl) {
		this.userGroupPictrueUrl = userGroupPictrueUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public String getUserArea() {
		return userArea;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	public String getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(String userBirth) {
		this.userBirth = userBirth;
	}

	public String getUserCity() {
		return userCity;
	}

	public void setUserCity(String userCity) {
		this.userCity = userCity;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserIsDisable() {
		return userIsDisable;
	}

	public void setUserIsDisable(String userIsDisable) {
		this.userIsDisable = userIsDisable;
	}

	public String getUserHeadImgUrl() {
		return userHeadImgUrl;
	}

	public void setUserHeadImgUrl(String userHeadImgUrl) {
		this.userHeadImgUrl = userHeadImgUrl;
	}

	public String getUserMobileNo() {
		return userMobileNo;
	}

	public void setUserMobileNo(String userMobileNo) {
		this.userMobileNo = userMobileNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserProvince() {
		return userProvince;
	}

	public void setUserProvince(String userProvince) {
		this.userProvince = userProvince;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserQq() {
		return userQq;
	}

	public void setUserQq(String userQq) {
		this.userQq = userQq;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "UserInfor [userId=" + userId + ", userAge=" + userAge
				+ ", userArea=" + userArea + ", userBirth=" + userBirth
				+ ", userCity=" + userCity + ", userEmail=" + userEmail
				+ ", userIsDisable=" + userIsDisable + ", userHeadImgUrl="
				+ userHeadImgUrl + ", userMobileNo=" + userMobileNo
				+ ", userName=" + userName + ", userNickName=" + userNickName
				+ ", userProvince=" + userProvince + ", userPwd=" + userPwd
				+ ", userQq=" + userQq + ", userSex=" + userSex + ", userType="
				+ userType + ", createTime=" + createTime
				+ ", userGroupPictrueUrl=" + userGroupPictrueUrl
				+ ", userTelephone=" + userTelephone + ", registerOrigin="
				+ registerOrigin + ", userIsLogin=" + userIsLogin + "]";
	}

}
