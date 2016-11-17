package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 我的订单（数据整合类）
 * @author zhoutanping
 * */
public class MyOrderInfo implements Serializable {
	
	private MyActivityBookInfo bookInfo;
	private MyActivityRoomInfo roomInfo;
	//活动是0，活动室为1
	private String activityOrRoom;
	
	public MyActivityBookInfo getBookInfo() {
		return bookInfo;
	}
	public void setBookInfo(MyActivityBookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}
	public MyActivityRoomInfo getRoomInfo() {
		return roomInfo;
	}
	public void setRoomInfo(MyActivityRoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}
	public String getActivityOrRoom() {
		return activityOrRoom;
	}
	public void setActivityOrRoom(String activityOrRoom) {
		this.activityOrRoom = activityOrRoom;
	}
	@Override
	public String toString() {
		return "MyOrderInfo [bookInfo=" + bookInfo + ", roomInfo=" + roomInfo
				+ ", activityOrRoom=" + activityOrRoom + "]";
	}
	
}
