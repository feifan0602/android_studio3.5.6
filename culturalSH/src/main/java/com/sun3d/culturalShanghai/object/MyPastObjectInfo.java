package com.sun3d.culturalShanghai.object;

/**
 * 
 * @author liningkang 公共对象，我的以往活动，我的以往预约，我的收藏
 */
public class MyPastObjectInfo {
	private String id;
	private String title;
	private String location;
	private String time;
	private String orderNumber;
	private String orderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public MyPastObjectInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return "MyPastObjectInfo [id=" + id + ", title=" + title + ", location=" + location
				+ ", time=" + time + ", orderNumber=" + orderNumber + "]";
	}

}
