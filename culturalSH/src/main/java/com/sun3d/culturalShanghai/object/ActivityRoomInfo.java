package com.sun3d.culturalShanghai.object;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityRoomInfo implements Serializable {
	private String roomId;
	private String roomName;
	private String roomArea;
	private String roomCapacity;
	private String roomPicUrl;
	private String roomInformation;
	private String roomConsultTel;
	private String roomFacility;
	private String roomNo;
	private int roomFee;
	private int roomIsFree;
	private String roomTel;
	private ArrayList<String> tagNameList;
	public String getRoomTel() {
		return roomTel;
	}

	public ArrayList<String> getTagNameList() {
		return tagNameList;
	}

	public void setTagNameList(ArrayList<String> tagNameList) {
		this.tagNameList = tagNameList;
	}

	public void setRoomTel(String roomTel) {
		this.roomTel = roomTel;
	}

	public int getRoomIsFree() {
		return roomIsFree;
	}

	public void setRoomIsFree(int roomIsFree) {
		this.roomIsFree = roomIsFree;
	}

	private String orderNum;
	private String venueName;
	private String venueAddress;
	private String roomTag;// 活动室标签
	private Integer SysNo;// 系统编号，0表示来着上海文化云，其他数值代表其他系统则都不可以预定
	private String memo;// 活动室备注
	private Boolean venueIsReserve = false;

	public Boolean getVenueIsReserve() {
		return venueIsReserve;
	}

	public void setVenueIsReserve(Boolean venueIsReserve) {
		this.venueIsReserve = venueIsReserve;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getSysNo() {
		return SysNo;
	}

	public void setSysNo(Integer sysNo) {
		SysNo = sysNo;
	}

	public String getRoomTag() {
		return roomTag;
	}

	public void setRoomTag(String roomTag) {
		this.roomTag = roomTag;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getVenueAddress() {
		return venueAddress;
	}

	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}

	public int getRoomFee() {
		return roomFee;
	}

	public void setRoomFee(int roomFee) {
		this.roomFee = roomFee;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getRoomPicUrl() {
		return roomPicUrl;
	}

	public void setRoomPicUrl(String roomPicUrl) {
		this.roomPicUrl = roomPicUrl;
	}

	public String getRoomInformation() {
		return roomInformation;
	}

	public void setRoomInformation(String roomInformation) {
		this.roomInformation = roomInformation;
	}

	public String getRoomConsultTel() {
		return roomConsultTel;
	}

	public void setRoomConsultTel(String roomConsultTel) {
		this.roomConsultTel = roomConsultTel;
	}

	public String getRoomFacility() {
		return roomFacility;
	}

	public void setRoomFacility(String roomFacility) {
		this.roomFacility = roomFacility;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomArea() {
		return roomArea;
	}

	public void setRoomArea(String roomArea) {
		this.roomArea = roomArea;
	}

	public String getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(String roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	@Override
	public String toString() {
		return "ActivityRoomInfo [roomId=" + roomId + ", roomName=" + roomName
				+ ", roomArea=" + roomArea + ", roomCapacity=" + roomCapacity
				+ ", roomPicUrl=" + roomPicUrl + ", roomInformation="
				+ roomInformation + ", roomConsultTel=" + roomConsultTel
				+ ", roomFacility=" + roomFacility + ", roomNo=" + roomNo
				+ ", roomFee=" + roomFee + ", orderNum=" + orderNum
				+ ", venueName=" + venueName + ", venueAddress=" + venueAddress
				+ ", roomTag=" + roomTag + ", SysNo=" + SysNo + ", memo="
				+ memo + ", venueIsReserve=" + venueIsReserve + "]";
	}

}
