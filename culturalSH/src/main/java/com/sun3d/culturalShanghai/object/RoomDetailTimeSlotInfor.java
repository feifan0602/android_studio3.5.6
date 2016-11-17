package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 活动室详情可预订时间段
 * 
 * @author yangyoutao
 * 
 */
@SuppressWarnings("serial")
public class RoomDetailTimeSlotInfor implements Serializable {
	private String date;
	private String timeslot;
	private String status;
	private String bookId;
	private String bookStatus;

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}

	@Override
	public String toString() {
		return "RoomDetailTimeSlotInfor [date=" + date + ", timeslot="
				+ timeslot + ", status=" + status + ", bookId=" + bookId + "]";
	}

}
