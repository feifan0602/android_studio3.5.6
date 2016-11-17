package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 我的场馆
 * 
 * @author liningkang
 * 
 */
public class MyVenueInfo implements Serializable {
	private int index;
	private String tuserTeamName;
	private String validCode;
	private String roomName;
	private String roomId;
	private String roomOrderId;
	private String commentCount;
	private String venueName;
	private String roomRicUrl;
	private String roomFee;
	private String roomOrderNo;
	private String curDate;
	private String roomOrderCreateTime;
	private String roomsCount;
	private String venueAddress;
	private String openPeriod;
	private String roomQrcodeUrl;

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

	public String getTuserTeamName() {
		return tuserTeamName;
	}

	public void setTuserTeamName(String tuserTeamName) {
		this.tuserTeamName = tuserTeamName;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomOrderId() {
		return roomOrderId;
	}

	public void setRoomOrderId(String roomOrderId) {
		this.roomOrderId = roomOrderId;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getRoomRicUrl() {
		return roomRicUrl;
	}

	public void setRoomRicUrl(String roomRicUrl) {
		this.roomRicUrl = roomRicUrl;
	}

	public String getRoomFee() {
		return roomFee;
	}

	public void setRoomFee(String roomFee) {
		this.roomFee = roomFee;
	}

	public String getRoomOrderNo() {
		return roomOrderNo;
	}

	public void setRoomOrderNo(String roomOrderNo) {
		this.roomOrderNo = roomOrderNo;
	}

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}

	public String getRoomOrderCreateTime() {
		return roomOrderCreateTime;
	}

	public void setRoomOrderCreateTime(String roomOrderCreateTime) {
		this.roomOrderCreateTime = roomOrderCreateTime;
	}

	public String getRoomsCount() {
		return roomsCount;
	}

	public void setRoomsCount(String roomsCount) {
		this.roomsCount = roomsCount;
	}

	public String getVenueAddress() {
		return venueAddress;
	}

	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}

	public String getOpenPeriod() {
		return openPeriod;
	}

	public void setOpenPeriod(String openPeriod) {
		this.openPeriod = openPeriod;
	}

	@Override
	public String toString() {
		return "MyVenueInfo [tuserTeamName=" + tuserTeamName + ", validCode=" + validCode
				+ ", roomName=" + roomName + ", roomId=" + roomId + ", roomOrderId=" + roomOrderId
				+ ", commentCount=" + commentCount + ", venueName=" + venueName + ", roomRicUrl="
				+ roomRicUrl + ", roomFee=" + roomFee + ", roomOrderNo=" + roomOrderNo
				+ ", curDate=" + curDate + ", roomOrderCreateTime=" + roomOrderCreateTime
				+ ", roomsCount=" + roomsCount + ", venueAddress=" + venueAddress + ", openPeriod="
				+ openPeriod + "]";
	}

}
