package com.sun3d.culturalShanghai.object;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 * @author liningkang 当前活动
 */
public class MyActivityBookInfo implements Serializable {
	private String activitySalesOnline;
	private int index;
	private String roomTime;
	private String tuserTeamName;
	private String validCode;
	public String getTuserId() {
		return tuserId;
	}

	public void setTuserId(String tuserId) {
		this.tuserId = tuserId;
	}

	private int orderStatus;
	private String tuserIsDisplay;
	private String tuserId;
	public String getTuserIsDisplay() {
		return tuserIsDisplay;
	}

	public void setTuserIsDisplay(String tuserIsDisplay) {
		this.tuserIsDisplay = tuserIsDisplay;
	}

	public String getActivitySalesOnline() {
		return activitySalesOnline;
	}

	public void setActivitySalesOnline(String activitySalesOnline) {
		this.activitySalesOnline = activitySalesOnline;
	}

	private String price;
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRoomTime() {
		return roomTime;
	}

	public void setRoomTime(String roomTime) {
		this.roomTime = roomTime;
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

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}


	public String getRoomPicUrl() {
		return roomPicUrl;
	}

	public void setRoomPicUrl(String roomPicUrl) {
		this.roomPicUrl = roomPicUrl;
	}

	public String getRoomOrderNo() {
		return roomOrderNo;
	}

	public void setRoomOrderNo(String roomOrderNo) {
		this.roomOrderNo = roomOrderNo;
	}

	public String getRoomQrcodeUrl() {
		return roomQrcodeUrl;
	}

	public void setRoomQrcodeUrl(String roomQrcodeUrl) {
		this.roomQrcodeUrl = roomQrcodeUrl;
	}

	public String getVenueAddress() {
		return venueAddress;
	}

	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}

	private String roomName;
	private String roomId;
	private String roomOrderId;
	private String venueName;
	private String roomPicUrl;
	private String roomOrderNo;
	private String roomQrcodeUrl;
	private String venueAddress;
	private String orderValidateCode;
	private String activityId;
	private String orderNumber;
	private String orderTime;
	private String activityIconUrl;
	private String activityName;
	private String activityEventDateTime;
	private String activityOrderId;
	private Integer orderPrice;
	private String activityAddress;
	private String orderVotes;
	private String commentCount;
	private String activityQrcodeUrl;
	private String orderSummary;
	private String orderPayStatus;
	// 在线选座时取消订单的参数
	private String orderLine;
	// 在线选座（所有座位）
	private String activitySeats;
	private String activityIsReservation;
	// 用来存储在线选座的座位状态
	private Boolean[] oSummary;
	private Boolean isSeatOnline = false;

	public String getOrderPayStatus() {
		return orderPayStatus;
	}

	public void setOrderPayStatus(String orderPayStatus) {
		this.orderPayStatus = orderPayStatus;
	}

	public String getActivityIsReservation() {
		return activityIsReservation;
	}

	public void setActivityIsReservation(String activityIsReservation) {
		this.activityIsReservation = activityIsReservation;
	}

	public Boolean getIsSeatOnline() {
		return isSeatOnline;
	}

	public void setIsSeatOnline(Boolean isSeatOnline) {
		this.isSeatOnline = isSeatOnline;
	}

	public String getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(String orderLine) {
		this.orderLine = orderLine;
	}

	public Boolean[] getoSummary() {
		return oSummary;
	}

	public void setoSummary(Boolean[] oSummary) {
		this.oSummary = oSummary;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getActivitySeats() {
		return activitySeats;
	}

	public void setActivitySeats(String activitySeats) {
		this.activitySeats = activitySeats;
	}

	public String getOrderSummary() {
		return orderSummary;
	}

	public void setOrderSummary(String orderSummary) {
		this.orderSummary = orderSummary;
	}

	public String getActivityQrcodeUrl() {
		return activityQrcodeUrl;
	}

	public void setActivityQrcodeUrl(String activityQrcodeUrl) {
		this.activityQrcodeUrl = activityQrcodeUrl;
	}

	public String getOrderValidateCode() {
		return orderValidateCode;
	}

	public void setOrderValidateCode(String orderValidateCode) {
		this.orderValidateCode = orderValidateCode;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getActivityIconUrl() {
		return activityIconUrl;
	}

	public void setActivityIconUrl(String activityIconUrl) {
		this.activityIconUrl = activityIconUrl;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityEventDateTime() {
		return activityEventDateTime;
	}

	public void setActivityEventDateTime(String activityEventDateTime) {
		this.activityEventDateTime = activityEventDateTime;
	}

	public String getActivityOrderId() {
		return activityOrderId;
	}

	public void setActivityOrderId(String activityOrderId) {
		this.activityOrderId = activityOrderId;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getActivityAddress() {
		return activityAddress;
	}

	public void setActivityAddress(String activityAddress) {
		this.activityAddress = activityAddress;
	}

	public String getOrderVotes() {
		return orderVotes;
	}

	public void setOrderVotes(String orderVotes) {
		this.orderVotes = orderVotes;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	@Override
	public String toString() {
		return "MyActivityBookInfo [index=" + index + ", orderValidateCode="
				+ orderValidateCode + ", activityId=" + activityId
				+ ", orderNumber=" + orderNumber + ", orderTime=" + orderTime
				+ ", activityIconUrl=" + activityIconUrl + ", activityName="
				+ activityName + ", activityEventDateTime="
				+ activityEventDateTime + ", activityOrderId="
				+ activityOrderId + ", orderPrice=" + orderPrice
				+ ", activityAddress=" + activityAddress + ", orderVotes="
				+ orderVotes + ", commentCount=" + commentCount
				+ ", activityQrcodeUrl=" + activityQrcodeUrl
				+ ", orderSummary=" + orderSummary + ", orderPayStatus="
				+ orderPayStatus + ", orderLine=" + orderLine
				+ ", activitySeats=" + activitySeats
				+ ", activityIsReservation=" + activityIsReservation
				+ ", oSummary=" + Arrays.toString(oSummary) + ", isSeatOnline="
				+ isSeatOnline + "]";
	}


}
