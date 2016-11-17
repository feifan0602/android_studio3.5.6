package com.sun3d.culturalShanghai.object;

import java.util.List;

import com.sun3d.culturalShanghai.video.VideoInfo;

public class SpaceInfo {
	int advertPostion;
	int advertSort;
	int createTime;
	int updateTime;
	int advertLink;
	int advertState;
	int advertLinkType;
	String advertType;
	String advertImgUrl;
	String advertTitle;
	String advertId;
	String advertUrl;
	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public int getActCount() {
		return actCount;
	}

	public void setActCount(int actCount) {
		this.actCount = actCount;
	}

	String createBy;
	String updateBy;
	boolean index;
	int roomCount;
	int actCount;
	/**
	 * 用于SearchNearbyActivity进行分组
	 */
	private Boolean isGroup = false;
	private String dictName;
	private String tagName;
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
	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
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

	public String getVenueIsFree() {
		return venueIsFree;
	}

	public void setVenueIsFree(String venueIsFree) {
		this.venueIsFree = venueIsFree;
	}

	public String getActivitySubject() {
		return activitySubject;
	}

	public void setActivitySubject(String activitySubject) {
		this.activitySubject = activitySubject;
	}

	public String getVenueEndTime() {
		return venueEndTime;
	}

	public void setVenueEndTime(String venueEndTime) {
		this.venueEndTime = venueEndTime;
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

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
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

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getVenuePrice() {
		return venuePrice;
	}

	public void setVenuePrice(String venuePrice) {
		this.venuePrice = venuePrice;
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

	public String getVenueMemo() {
		return venueMemo;
	}

	public void setVenueMemo(String venueMemo) {
		this.venueMemo = venueMemo;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
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

	public String getOpenNotice() {
		return openNotice;
	}

	public void setOpenNotice(String openNotice) {
		this.openNotice = openNotice;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getVenueVoiceUrl() {
		return venueVoiceUrl;
	}

	public void setVenueVoiceUrl(String venueVoiceUrl) {
		this.venueVoiceUrl = venueVoiceUrl;
	}

	public Boolean getVenueIsReserve() {
		return venueIsReserve;
	}

	public void setVenueIsReserve(Boolean venueIsReserve) {
		this.venueIsReserve = venueIsReserve;
	}

	public String getRemarkUserImgUrl() {
		return remarkUserImgUrl;
	}

	public void setRemarkUserImgUrl(String remarkUserImgUrl) {
		this.remarkUserImgUrl = remarkUserImgUrl;
	}

	public List<VideoInfo> getVideoPalyList() {
		return videoPalyList;
	}

	public void setVideoPalyList(List<VideoInfo> videoPalyList) {
		this.videoPalyList = videoPalyList;
	}

	public String getRemarkUserSex() {
		return remarkUserSex;
	}

	public void setRemarkUserSex(String remarkUserSex) {
		this.remarkUserSex = remarkUserSex;
	}

	public boolean isIndex() {
		return index;
	}

	public void setIndex(boolean index) {
		this.index = index;
	}

	public int getAdvertPostion() {
		return advertPostion;
	}

	public void setAdvertPostion(int advertPostion) {
		this.advertPostion = advertPostion;
	}

	public int getAdvertSort() {
		return advertSort;
	}

	public void setAdvertSort(int advertSort) {
		this.advertSort = advertSort;
	}

	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}

	public int getAdvertLink() {
		return advertLink;
	}

	public void setAdvertLink(int advertLink) {
		this.advertLink = advertLink;
	}

	public int getAdvertState() {
		return advertState;
	}

	public void setAdvertState(int advertState) {
		this.advertState = advertState;
	}

	public int getAdvertLinkType() {
		return advertLinkType;
	}

	public void setAdvertLinkType(int advertLinkType) {
		this.advertLinkType = advertLinkType;
	}

	public String getAdvertType() {
		return advertType;
	}

	public void setAdvertType(String advertType) {
		this.advertType = advertType;
	}

	public String getAdvertImgUrl() {
		return advertImgUrl;
	}

	public void setAdvertImgUrl(String advertImgUrl) {
		this.advertImgUrl = advertImgUrl;
	}

	public String getAdvertTitle() {
		return advertTitle;
	}

	public void setAdvertTitle(String advertTitle) {
		this.advertTitle = advertTitle;
	}

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
	}

	public String getAdvertUrl() {
		return advertUrl;
	}

	public void setAdvertUrl(String advertUrl) {
		this.advertUrl = advertUrl;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

}
