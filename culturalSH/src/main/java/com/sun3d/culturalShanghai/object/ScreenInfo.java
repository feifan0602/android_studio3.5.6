package com.sun3d.culturalShanghai.object;

import java.io.Serializable;

/**
 * 
 * @author liningkang 筛选条件类(场馆，团体，非遗)
 */
public class ScreenInfo implements Serializable {
	private String tabType = "";
	private String type = "";
	private String crowd = "";
	private String adress = "";
	private String location = "";
	private String nature = "";

	private String serach = "";
	private String systemText = "";
	private String time = "";
	private String venueIsReserve = "";

	public String getSystemText() {
		return systemText;
	}

	public void setSystemText(String systemText) {
		this.systemText = systemText;
	}

	public String getTime() {
		return time;
	}

	public String getVenueIsReserve() {
		return venueIsReserve;
	}

	public void setVenueIsReserve(String venueIsReserve) {
		this.venueIsReserve = venueIsReserve;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSerach() {
		return serach;
	}

	public void setSerach(String serach) {
		this.serach = serach;
	}

	public String getTabType() {
		return tabType;
	}

	public void setTabType(String tabType) {
		this.tabType = tabType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCrowd() {
		return crowd;
	}

	public void setCrowd(String crowd) {
		this.crowd = crowd;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	/**
	 * 清除数据
	 */
	public void clear() {
		type = "";
		crowd = "";
		adress = "";
		location = "";
		nature = "";
		serach = "";
		systemText = "";
		time = "";
		venueIsReserve = "";
	}

	@Override
	public String toString() {
		return "ScreenInfo [tabType=" + tabType + ", type=" + type + ", crowd=" + crowd + ", adress=" + adress + ", location=" + location + ", nature="
				+ nature + "]";
	}

}
