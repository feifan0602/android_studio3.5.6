package com.sun3d.culturalShanghai.object;

public class RoomDateInfo {
	String time;
	String stutas;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public RoomDateInfo(String time, String stutas) {
		super();
		this.time = time;
		this.stutas = stutas;
	}

	public String getStutas() {
		return stutas;
	}

	public void setStutas(String stutas) {
		this.stutas = stutas;
	}

}
