package com.sun3d.culturalShanghai.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sun3d.culturalShanghai.video.VideoInfo;

@SuppressWarnings("serial")
public class VenueDetailInfor implements Serializable {
	/**
	 * 用于SearchNearbyActivity进行分组
	 */
	private Boolean isGroup = false;
	/**
	 * 第一个商圈
	 */
	private String dictName;
	/**
	 * 第二个 标签
	 */
	private String tagName;
	/**
	 * subList
	 */
	private ArrayList<String> tagNameList;
	private String venuePriceNotice;

	public String getVenuePriceNotice() {
		return venuePriceNotice;
	}

	public void setVenuePriceNotice(String venuePriceNotice) {
		this.venuePriceNotice = venuePriceNotice;
	}

	public ArrayList<String> getTagNameList() {
		return tagNameList;
	}

	public void setTagNameList(ArrayList<String> tagNameList) {
		this.tagNameList = tagNameList;
	}

	private String venueIsFree;
	private String activitySubject;
	private String venueEndTime;
	private int numOfRooms;
	private int numofActivity;
	private int tagId = 1;
	private String venueAddress;
	private String venueIconUrl;
	private Boolean venueHasAntique;
	private Boolean venueHasRoom;
	private String venueLat;
	private String venueLon;
	private String venueName;
	private String venueOpenTime;
	private String venuEndTime;
	private String venueWeek;
	private String venueId;
	private String venuePrice;
	private Boolean venueIsCollect;
	private Integer collectNum;
	private String venueMobile;
	private String venueMemo;
	private String distance;
	private String venuePersonName;
	private String venueComment;
	private float venueRating;
	private String venueHasMetro;
	private String venueHasBus;
	private String roomNamesList;
	private String roomIconUrlList;
	private String openNotice;
	private String shareUrl;
	private String venueVoiceUrl;// 音频地址
	private Boolean venueIsReserve;// 是否可预订
	private String remarkUserImgUrl;
	private List<VideoInfo> videoPalyList;// 场馆视频
	private String remarkUserSex;

	public String getVenueEndTime() {
		return venueEndTime;
	}

	public int getNumOfRooms() {
		return numOfRooms;
	}

	public void setNumOfRooms(int numOfRooms) {
		this.numOfRooms = numOfRooms;
	}

	public int getNumofActivity() {
		return numofActivity;
	}

	public void setNumofActivity(int numofActivity) {
		this.numofActivity = numofActivity;
	}

	public void setVenueEndTime(String venueEndTime) {
		this.venueEndTime = venueEndTime;
	}

	private int venueIsWant;

	public int getVenueIsWant() {
		return venueIsWant;
	}

	public void setVenueIsWant(int venueIsWant) {
		this.venueIsWant = venueIsWant;
	}

	public String getActivitySubject() {
		return activitySubject;
	}

	public void setActivitySubject(String activitySubject) {
		this.activitySubject = activitySubject;
	}

	public String getVenueIsFree() {
		return venueIsFree;
	}

	public void setVenueIsFree(String venueIsFree) {
		this.venueIsFree = venueIsFree;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getRemarkUserSex() {
		return remarkUserSex;
	}

	public void setRemarkUserSex(String remarkUserSex) {
		this.remarkUserSex = remarkUserSex;
	}

	public List<VideoInfo> getVideoPalyList() {
		return videoPalyList;
	}

	public void setVideoPalyList(List<VideoInfo> videoPalyList) {
		this.videoPalyList = videoPalyList;
	}

	public String getRemarkUserImgUrl() {
		return remarkUserImgUrl;
	}

	public void setRemarkUserImgUrl(String remarkUserImgUrl) {
		this.remarkUserImgUrl = remarkUserImgUrl;
	}

	public Boolean getVenueIsReserve() {
		return venueIsReserve;
	}

	public void setVenueIsReserve(Boolean venueIsReserve) {
		this.venueIsReserve = venueIsReserve;
	}

	public String getVenueVoiceUrl() {
		return venueVoiceUrl;
	}

	public void setVenueVoiceUrl(String venueVoiceUrl) {
		this.venueVoiceUrl = venueVoiceUrl;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getOpenNotice() {
		return openNotice;
	}

	public void setOpenNotice(String openNotice) {
		this.openNotice = openNotice;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getVenuEndTime() {
		return venuEndTime;
	}

	public void setVenuEndTime(String venuEndTime) {
		this.venuEndTime = venuEndTime;
	}

	public String getVenueWeek() {
		return venueWeek;
	}

	public void setVenueWeek(String venueWeek) {
		this.venueWeek = venueWeek;
	}

	public String getVenueMemo() {
		return venueMemo;
	}

	public void setVenueMemo(String venueMemo) {
		this.venueMemo = venueMemo;
	}

	public String getVenueAddress() {
		return venueAddress;
	}

	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}

	public String getVenueIconUrl() {
		return venueIconUrl;
	}

	public void setVenueIconUrl(String venueIconUrl) {
		this.venueIconUrl = venueIconUrl;
	}

	public Boolean getVenueHasAntique() {
		return venueHasAntique;
	}

	public void setVenueHasAntique(Boolean venueHasAntique) {
		this.venueHasAntique = venueHasAntique;
	}

	public Boolean getVenueHasRoom() {
		return venueHasRoom;
	}

	public void setVenueHasRoom(Boolean venueHasRoom) {
		this.venueHasRoom = venueHasRoom;
	}

	public String getVenueLat() {
		return venueLat;
	}

	public void setVenueLat(String venueLat) {
		this.venueLat = venueLat;
	}

	public String getVenueLon() {
		return venueLon;
	}

	public void setVenueLon(String venueLon) {
		this.venueLon = venueLon;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getVenueOpenTime() {
		return venueOpenTime;
	}

	public void setVenueOpenTime(String venueOpenTime) {
		this.venueOpenTime = venueOpenTime;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getVenuePrice() {
		return venuePrice;
	}

	public void setVenuePrice(String venueIsFree) {
		this.venuePrice = venueIsFree;
	}

	public Boolean getVenueIsCollect() {
		return venueIsCollect;
	}

	public void setVenueIsCollect(Boolean venueIsCollect) {
		this.venueIsCollect = venueIsCollect;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public String getVenueMobile() {
		return venueMobile;
	}

	public void setVenueMobile(String venueMobile) {
		this.venueMobile = venueMobile;
	}

	public String getVenuePersonName() {
		return venuePersonName;
	}

	public void setVenuePersonName(String venuePersonName) {
		this.venuePersonName = venuePersonName;
	}

	public String getVenueComment() {
		return venueComment;
	}

	public void setVenueComment(String venueComment) {
		this.venueComment = venueComment;
	}

	public float getVenueRating() {
		return venueRating;
	}

	public void setVenueRating(float venueRating) {
		this.venueRating = venueRating;
	}

	public String getVenueHasMetro() {
		return venueHasMetro;
	}

	public void setVenueHasMetro(String venueHasMetro) {
		this.venueHasMetro = venueHasMetro;
	}

	public String getVenueHasBus() {
		return venueHasBus;
	}

	public void setVenueHasBus(String venueHasBus) {
		this.venueHasBus = venueHasBus;
	}

	public String getRoomNamesList() {
		return roomNamesList;
	}

	public void setRoomNamesList(String roomNamesList) {
		this.roomNamesList = roomNamesList;
	}

	public String getRoomIconUrlList() {
		return roomIconUrlList;
	}

	public void setRoomIconUrlList(String roomIconUrlList) {
		this.roomIconUrlList = roomIconUrlList;
	}

	@Override
	public String toString() {
		return "VenueDetailInfor [venueAddress=" + venueAddress
				+ ", venueIconUrl=" + venueIconUrl + ", venueHasAntique="
				+ venueHasAntique + ", venueHasRoom=" + venueHasRoom
				+ ", venueLat=" + venueLat + ", venueLon=" + venueLon
				+ ", venueName=" + venueName + ", venueOpenTime="
				+ venueOpenTime + ", venueId=" + venueId + ", venueIsFree="
				+ venuePrice + ", venueIsCollect=" + venueIsCollect
				+ ", collectNum=" + collectNum + ", venueMobile=" + venueMobile
				+ ", venueMemo=" + venueMemo + "]";
	}

}
