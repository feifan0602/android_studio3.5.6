package com.sun3d.culturalShanghai.object;

public class SeatInfo {
	private int RowNumber;
	private Boolean isSeat;// 是否是座位
	private Integer seatRow;// 座位行
	private Integer seatColumn;// 座位列
	private Integer seatStatus;// 座位所属状态 1-正常2-待修 3-不存在

	public Boolean getIsSeat() {
		return isSeat;
	}

	public int getRowNumber() {
		return RowNumber;
	}

	public void setRowNumber(int rowNumber) {
		RowNumber = rowNumber;
	}

	public void setIsSeat(Boolean isSeat) {
		this.isSeat = isSeat;
	}

	public Integer getSeatRow() {
		return seatRow;
	}

	public void setSeatRow(Integer seatRow) {
		this.seatRow = seatRow;
	}

	public Integer getSeatColumn() {
		return seatColumn;
	}

	public void setSeatColumn(Integer seatColumn) {
		this.seatColumn = seatColumn;
	}

	public Integer getSeatStatus() {
		return seatStatus;
	}

	public void setSeatStatus(Integer seatStatus) {
		this.seatStatus = seatStatus;
	}

	@Override
	public String toString() {
		return "SeatInfo [RowNumber=" + RowNumber + ", isSeat=" + isSeat
				+ ", seatRow=" + seatRow + ", seatColumn=" + seatColumn
				+ ", seatStatus=" + seatStatus + "]";
	}


}
