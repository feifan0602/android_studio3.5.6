package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 我的订单， 我的活动室
 * */
public class MyActivityRoomInfo implements Serializable {
	
	private int index;
	
	private String roomTime;
	private String venueAddress;
	private String roomOrderCreateTime;
	private String roomName;
	private String tuserTeamName;
	private String roomOrderId;
	private String roomPicUrl;
	private String validCode;
	private String roomId;
	private String venueName;
	private String roomOrderNo;
	private String orderStatus;
	private String roomQrcodeUrl;
	private String orderTime;
	private int checkStatus;
	
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getRoomQrcodeUrl() {
		return roomQrcodeUrl;
	}
	public void setRoomQrcodeUrl(String roomQrcodeUrl) {
		this.roomQrcodeUrl = roomQrcodeUrl;
	}
	public String getRoomTime() {
		return roomTime;
	}
	public void setRoomTime(String roomTime) {
		this.roomTime = roomTime;
	}
	public String getVenueAddress() {
		return venueAddress;
	}
	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}
	public String getRoomOrderCreateTime() {
		return roomOrderCreateTime;
	}
	public void setRoomOrderCreateTime(String roomOrderCreateTime) {
		this.roomOrderCreateTime = roomOrderCreateTime;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getTuserTeamName() {
		return tuserTeamName;
	}
	public void setTuserTeamName(String tuserTeamName) {
		this.tuserTeamName = tuserTeamName;
	}
	public String getRoomOrderId() {
		return roomOrderId;
	}
	public void setRoomOrderId(String roomOrderId) {
		this.roomOrderId = roomOrderId;
	}
	public String getRoomPicUrl() {
		return roomPicUrl;
	}
	public void setRoomPicUrl(String roomRicUrl) {
		this.roomPicUrl = roomRicUrl;
	}
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public String getRoomOrderNo() {
		return roomOrderNo;
	}
	public void setRoomOrderNo(String roomOrderNo) {
		this.roomOrderNo = roomOrderNo;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	@Override
	public String toString() {
		return "MyActivityRoomInfo [index=" + index + ", roomTime=" + roomTime
				+ ", venueAddress=" + venueAddress + ", roomOrderCreateTime="
				+ roomOrderCreateTime + ", roomName=" + roomName
				+ ", tuserTeamName=" + tuserTeamName + ", roomOrderId="
				+ roomOrderId + ", roomRicUrl=" + roomPicUrl + ", validCode="
				+ validCode + ", roomId=" + roomId + ", venueName=" + venueName
				+ ", roomOrderNo=" + roomOrderNo + ", orderStatus="
				+ orderStatus + ", roomQrcodeUrl=" + roomQrcodeUrl + "]";
	}
	
}
