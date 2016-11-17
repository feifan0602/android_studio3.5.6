package com.sun3d.culturalShanghai.object;

import com.sun3d.culturalShanghai.video.VideoInfo;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EventInfo implements Serializable {

	public ArrayList<EventInfo> getEventInfosList() {
		return mEventInfosList;
	}

	public void setEventInfosList(ArrayList<EventInfo> eventInfosList) {
		mEventInfosList = eventInfosList;
	}

	ArrayList<EventInfo> mEventInfosList;
	/**
	 * 场馆的图片
	 */
	String venueIconUrl;

	public String getVenueIconUrl() {
		return venueIconUrl;
	}

	public void setVenueIconUrl(String venueIconUrl) {
		this.venueIconUrl = venueIconUrl;
	}

	public String getVenueAddress() {
		return venueAddress;
	}

	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}

	public int getActCount() {
		return actCount;
	}

	public void setActCount(int actCount) {
		this.actCount = actCount;
	}

	public int getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(int roomCount) {
		this.roomCount = roomCount;
	}

	public Boolean getGroup() {
		return isGroup;
	}

	public void setGroup(Boolean group) {
		isGroup = group;
	}

	/**
	 * 场馆的地址
	 */
	String venueAddress;
	/**
	 * 在线活动数
	 *
	 * @return
	 */
	int actCount;
	/**
	 * 在线活动室
	 *
	 * @return
	 */
	int roomCount;
	private String distance_str;
	public String getDistance_str() {
		return distance_str;
	}

	public void setDistance_str(String distance_str) {
		this.distance_str = distance_str;
	}

	/**
	 * 社团ID
	 */
	private String associationId;
	private String deductionCredit;
	private ArrayList<String> tagNameList;
	private String assnId;
	private ArrayList<String> assnSub;

	public String getAssnId() {
		return assnId;
	}

	public void setAssnId(String assnId) {
		this.assnId = assnId;
	}

	public ArrayList<String> getAssnSub() {
		return assnSub;
	}

	public void setAssnSub(ArrayList<String> assnSub) {
		this.assnSub = assnSub;
	}

	public ArrayList<String> getTagNameList() {
		return tagNameList;
	}

	public void setTagNameList(ArrayList<String> tagNameList) {
		this.tagNameList = tagNameList;
	}

	public String getAssociationId() {
		return associationId;
	}

	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	public List<ActivitySubInfo> getSubList() {
		return subList;
	}

	public void setSubList(List<ActivitySubInfo> subList) {
		this.subList = subList;
	}

	private String ticketSettings;
	private String integralStatus;
	private String priceDescribe;
	private String spikeDifferences;
	private String eventPrices;
	private String ticketNumber;
	private String lowestCredit;
	private String activityTel;
	private String ticketCount;
	private List<ActivitySubInfo> subList;

	public int getActivityIsPast() {
		return activityIsPast;
	}

	public void setActivityIsPast(int activityIsPast) {
		this.activityIsPast = activityIsPast;
	}

	private String activityTime;
	private String costCredit;
	private int identityCard;
	private int singleEvent;
	private int activityIsPast;

	public String getDeductionCredit() {
		return deductionCredit;
	}

	public void setDeductionCredit(String deductionCredit) {
		this.deductionCredit = deductionCredit;
	}

	public String getTicketSettings() {
		return ticketSettings;
	}

	public void setTicketSettings(String ticketSettings) {
		this.ticketSettings = ticketSettings;
	}

	public String getIntegralStatus() {
		return integralStatus;
	}

	public void setIntegralStatus(String integralStatus) {
		this.integralStatus = integralStatus;
	}

	public String getPriceDescribe() {
		return priceDescribe;
	}

	public void setPriceDescribe(String priceDescribe) {
		this.priceDescribe = priceDescribe;
	}

	public String getSpikeDifferences() {
		return spikeDifferences;
	}

	public void setSpikeDifferences(String spikeDifferences) {
		this.spikeDifferences = spikeDifferences;
	}

	public String getEventPrices() {
		return eventPrices;
	}

	public void setEventPrices(String eventPrices) {
		this.eventPrices = eventPrices;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getLowestCredit() {
		return lowestCredit;
	}

	public void setLowestCredit(String lowestCredit) {
		this.lowestCredit = lowestCredit;
	}

	public String getActivityTel() {
		return activityTel;
	}

	public void setActivityTel(String activityTel) {
		this.activityTel = activityTel;
	}

	public String getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(String ticketCount) {
		this.ticketCount = ticketCount;
	}

	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}

	public String getCostCredit() {
		return costCredit;
	}

	public void setCostCredit(String costCredit) {
		this.costCredit = costCredit;
	}

	public int getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(int identityCard) {
		this.identityCard = identityCard;
	}

	public int getSingleEvent() {
		return singleEvent;
	}

	public void setSingleEvent(int singleEvent) {
		this.singleEvent = singleEvent;
	}

	private int activityLat;
	private int spikeType;
	private int priceType;
	private int activityIsCollect;
	private int activityLon;
	private String sysId;
	private String activityAddress;
	private String activityId;
	private String activityIconUrl;
	private String sysNo;

	public int getActivityLat() {
		return activityLat;
	}

	public void setActivityLat(int activityLat) {
		this.activityLat = activityLat;
	}

	public int getSpikeType() {
		return spikeType;
	}

	public void setSpikeType(int spikeType) {
		this.spikeType = spikeType;
	}

	public int getPriceType() {
		return priceType;
	}

	public void setPriceType(int priceType) {
		this.priceType = priceType;
	}

	public int getActivityIsCollect() {
		return activityIsCollect;
	}

	public void setActivityIsCollect(int activityIsCollect) {
		this.activityIsCollect = activityIsCollect;
	}

	public int getActivityLon() {
		return activityLon;
	}

	public void setActivityLon(int activityLon) {
		this.activityLon = activityLon;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public String getActivityAddress() {
		return activityAddress;
	}

	public void setActivityAddress(String activityAddress) {
		this.activityAddress = activityAddress;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityIconUrl() {
		return activityIconUrl;
	}

	public void setActivityIconUrl(String activityIconUrl) {
		this.activityIconUrl = activityIconUrl;
	}

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityUpdateTime() {
		return activityUpdateTime;
	}

	public void setActivityUpdateTime(String activityUpdateTime) {
		this.activityUpdateTime = activityUpdateTime;
	}

	public String getActivityArea() {
		return activityArea;
	}

	public void setActivityArea(String activityArea) {
		this.activityArea = activityArea;
	}

	public String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	private String activityName;
	private String activityUpdateTime;
	private String activityArea;
	private String activityEndTime;

	/**
	 * 用于SearchNearbyActivity进行分组
	 */
	private Boolean isGroup = false;
	private String tagName;
	private String activityLocationName;
	private String activityPerformed;
	private String activitySpeaker;
	private String activityHost;
	private String activityOrganizer;
	private String activityCoorganizer;
	private String activityTips;
	private int tagId = 1;
	private int activityIsFree;
	private int activityIsHot;
	private String eventId;
	private String distance;
	private String eventIsCollect;
	private String activityPrice;
	private String eventIconUrl;
	private String eventAddress;
	private String eventName;
	private String eventDetailsIconUrl;
	private String eventLat;
	private String eventContent;
	private String collectNum;
	private String eventLon;
	private String eventEndTime;
	private String venueId;
	private String venueName;
	private String eventTicketNum;
	private String eventTel;
	private String eventCounts; // 3.3 新参数，活动场次票数
	private String activityAbleCount = "0";// 剩余票数
	private String activityJoinMethod;// 参与方式
	private String count;
	private String activitySalesOnline;// 是否支持在线选座 Y 是 N否
	private String activityIsReservation;// 是否可预定 1 否 2是
	private String activityInformation;// 活动购买须知
	private String activityStartTime;// 开始时间
	private String activityDateNums;//
	private String activityEventimes;// 活动具体时间（用于在线选座传参数）
	private String activityEventIds;// 活动场次id（用于在线选座传参数）
	private String activityTimeDes;// 活动备注
	private String timeQuantum;// 活动时间段
	private String activityRecommend;// Y最热，N最新
	private String shareUrl;// 分享链接
	private String status;// 活动时间状态 0.已过期 1.未过期
	private Boolean activityIsWant;// 用户是否已经报名参加，true已报名，false未报名
	private List<String> activityTags;// 活动标签
	private String activityType;//
	private String activityTagName;//
	private List<VideoInfo> videoPalyList;// 活动视频
	private String activityFunName;
	private String tagColor;
	private String tagInitial;
	private String activitySubject;
	private String activitySite;
	private String activityNotice;
	private String activityPast;// 是否过期
	private String activityMemo;
	private boolean isWant;
	private int isContainActivtiyAdv;
	private int advBannerFIsLink;
	private int advBannerFLinkType;
	private String advBannerFUrl;
	private String advBannerFImgUrl;
	private int advBannerSIsLink;
	private int advBannerSLinkType;
	private String advBannerSUrl;
	private String advBannerSImgUrl;
	private int advBannerLIsLink;

	public String getTagName() {
		return tagName;
	}

	public String getActivityLocationName() {
		return activityLocationName;
	}

	public void setActivityLocationName(String activityLocationName) {
		this.activityLocationName = activityLocationName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getIsContainActivtiyAdv() {
		return isContainActivtiyAdv;
	}

	public void setIsContainActivtiyAdv(int isContainActivtiyAdv) {
		this.isContainActivtiyAdv = isContainActivtiyAdv;
	}

	public int getAdvBannerFIsLink() {
		return advBannerFIsLink;
	}

	public void setAdvBannerFIsLink(int advBannerFIsLink) {
		this.advBannerFIsLink = advBannerFIsLink;
	}

	public int getAdvBannerFLinkType() {
		return advBannerFLinkType;
	}

	public void setAdvBannerFLinkType(int advBannerFLinkType) {
		this.advBannerFLinkType = advBannerFLinkType;
	}

	public String getAdvBannerFUrl() {
		return advBannerFUrl;
	}

	public void setAdvBannerFUrl(String advBannerFUrl) {
		this.advBannerFUrl = advBannerFUrl;
	}

	public String getAdvBannerFImgUrl() {
		return advBannerFImgUrl;
	}

	public void setAdvBannerFImgUrl(String advBannerFImgUrl) {
		this.advBannerFImgUrl = advBannerFImgUrl;
	}

	public int getAdvBannerSIsLink() {
		return advBannerSIsLink;
	}

	public void setAdvBannerSIsLink(int advBannerSIsLink) {
		this.advBannerSIsLink = advBannerSIsLink;
	}

	public int getAdvBannerSLinkType() {
		return advBannerSLinkType;
	}

	public void setAdvBannerSLinkType(int advBannerSLinkType) {
		this.advBannerSLinkType = advBannerSLinkType;
	}

	public String getAdvBannerSUrl() {
		return advBannerSUrl;
	}

	public void setAdvBannerSUrl(String advBannerSUrl) {
		this.advBannerSUrl = advBannerSUrl;
	}

	public String getAdvBannerSImgUrl() {
		return advBannerSImgUrl;
	}

	public void setAdvBannerSImgUrl(String advBannerSImgUrl) {
		this.advBannerSImgUrl = advBannerSImgUrl;
	}

	public int getAdvBannerLIsLink() {
		return advBannerLIsLink;
	}

	public void setAdvBannerLIsLink(int advBannerLIsLink) {
		this.advBannerLIsLink = advBannerLIsLink;
	}

	public int getAdvBannerLLinkType() {
		return advBannerLLinkType;
	}

	public void setAdvBannerLLinkType(int advBannerLLinkType) {
		this.advBannerLLinkType = advBannerLLinkType;
	}

	public String getAdvBannerLUrl() {
		return advBannerLUrl;
	}

	public void setAdvBannerLUrl(String advBannerLUrl) {
		this.advBannerLUrl = advBannerLUrl;
	}

	public String getAdvBannerLImgUrl() {
		return advBannerLImgUrl;
	}

	public void setAdvBannerLImgUrl(String advBannerLImgUrl) {
		this.advBannerLImgUrl = advBannerLImgUrl;
	}

	public JSONArray getList() {
		return list;
	}

	public void setList(JSONArray list) {
		this.list = list;
	}

	private int advBannerLLinkType;
	private String advBannerLUrl;
	private String advBannerLImgUrl;

	private JSONArray list;

	public String getActivityPerformed() {
		return activityPerformed;
	}

	public void setActivityPerformed(String activityPerformed) {
		this.activityPerformed = activityPerformed;
	}

	public String getActivitySpeaker() {
		return activitySpeaker;
	}

	public void setActivitySpeaker(String activitySpeaker) {
		this.activitySpeaker = activitySpeaker;
	}

	public int getActivityIsHot() {
		return activityIsHot;
	}

	public void setActivityIsHot(int activityIsHot) {
		this.activityIsHot = activityIsHot;
	}

	public String getActivityTips() {
		return activityTips;
	}

	public void setActivityTips(String activityTips) {
		this.activityTips = activityTips;
	}

	public String getActivityMemo() {
		return activityMemo;
	}

	public void setActivityMemo(String activityMemo) {
		this.activityMemo = activityMemo;
	}

	public String getEventCounts() {
		return eventCounts;
	}

	public boolean isWant() {
		return isWant;
	}

	public void setWant(boolean isWant) {
		this.isWant = isWant;
	}

	public void setEventCounts(String eventCounts) {
		this.eventCounts = eventCounts;
	}

	public String getActivityPast() {
		return activityPast;
	}

	public int getActivityIsFree() {
		return activityIsFree;
	}

	public void setActivityIsFree(int activityIsFree) {
		this.activityIsFree = activityIsFree;
	}

	public void setActivityPast(String activityPast) {
		this.activityPast = activityPast;
	}

	public String getActivityNotice() {
		return activityNotice;
	}

	public void setActivityNotice(String activityNotice) {
		this.activityNotice = activityNotice;
	}

	public String getTagColor() {
		return tagColor;
	}

	public void setTagColor(String tagColor) {
		this.tagColor = tagColor;
	}

	public String getTagInitial() {
		return tagInitial;
	}

	public void setTagInitial(String tagInitial) {
		this.tagInitial = tagInitial;
	}

	public String getActivitySite() {
		return activitySite;
	}

	public void setActivitySite(String activitySite) {
		this.activitySite = activitySite;
	}

	public String getActivityFunName() {
		return activityFunName;
	}

	public String getActivitySubject() {
		return activitySubject;
	}

	public void setActivitySubject(String activitySubject) {
		this.activitySubject = activitySubject;
	}

	public void setActivityFunName(String activityFunName) {
		this.activityFunName = activityFunName;
	}

	public List<VideoInfo> getVideoPalyList() {
		return videoPalyList;
	}

	public void setVideoPalyList(List<VideoInfo> videoPalyList) {
		this.videoPalyList = videoPalyList;
	}

	public String getActivityTagName() {
		return activityTagName;
	}

	public void setActivityTagName(String activityTagName) {
		this.activityTagName = activityTagName;
	}

	public Boolean getActivityIsWant() {
		return activityIsWant;
	}

	public void setActivityIsWant(Boolean activityIsWant) {
		this.activityIsWant = activityIsWant;
	}

	public List<String> getActivityTags() {
		return activityTags;
	}

	public void setActivityTags(List<String> activityTags) {
		this.activityTags = activityTags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public String getActivityEventimes() {
		return activityEventimes;
	}

	public void setActivityEventimes(String activityEventimes) {
		this.activityEventimes = activityEventimes;
	}

	public String getActivityEventIds() {
		return activityEventIds;
	}

	public String getActivityRecommend() {
		return activityRecommend;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public void setActivityRecommend(String activityRecommend) {
		this.activityRecommend = activityRecommend;
	}

	public void setActivityEventIds(String activityEventIds) {
		this.activityEventIds = activityEventIds;
	}

	public String getActivityAbleCount() {
		return activityAbleCount;
	}

	public void setActivityAbleCount(String activityAbleCount) {
		this.activityAbleCount = activityAbleCount;
	}

	public String getActivityJoinMethod() {
		return activityJoinMethod;
	}

	public void setActivityJoinMethod(String activityJoinMethod) {
		this.activityJoinMethod = activityJoinMethod;
	}

	public String getActivityInformation() {
		return activityInformation;
	}

	public void setActivityInformation(String activityInformation) {
		this.activityInformation = activityInformation;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public Boolean getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(Boolean isGroup) {
		this.isGroup = isGroup;
	}

	public String getActivityPrice() {
		return activityPrice;
	}

	public void setActivityPrice(String activityPrice) {
		this.activityPrice = activityPrice;
	}

	public String getActivitySalesOnline() {
		return activitySalesOnline;
	}

	public void setActivitySalesOnline(String activitySalesOnline) {
		this.activitySalesOnline = activitySalesOnline;
	}

	public String getActivityIsReservation() {
		return activityIsReservation;
	}

	public void setActivityIsReservation(String activityIsReservation) {
		this.activityIsReservation = activityIsReservation;
	}

	public EventInfo() {
		// TODO Auto-generated constructor stub
	}

	public EventInfo(String eventName, String eventAddress) {
		// TODO Auto-generated constructor stub
		this.eventName = eventName;
		this.eventAddress = eventAddress;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getEventIsCollect() {
		return eventIsCollect;
	}

	public void setEventIsCollect(String eventIsCollect) {
		this.eventIsCollect = eventIsCollect;
	}

	public String getEventPrice() {
		return activityPrice;
	}

	public void setEventPrice(String eventPrice) {
		this.activityPrice = eventPrice;
	}

	public String getEventIconUrl() {
		return eventIconUrl;
	}

	public void setEventIconUrl(String eventIconUrl) {
		this.eventIconUrl = eventIconUrl;
	}

	public String getEventAddress() {
		return eventAddress;
	}

	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDetailsIconUrl() {
		return eventDetailsIconUrl;
	}

	public void setEventDetailsIconUrl(String eventDetailsIconUrl) {
		this.eventDetailsIconUrl = eventDetailsIconUrl;
	}

	public String getEventLat() {
		return eventLat;
	}

	public void setEventLat(String eventLat) {
		this.eventLat = eventLat;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	public String getEventLon() {
		return eventLon;
	}

	public void setEventLon(String eventLon) {
		this.eventLon = eventLon;
	}

	public String getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(String eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getEventTicketNum() {
		return eventTicketNum;
	}

	public void setEventTicketNum(String eventTicketNum) {
		this.eventTicketNum = eventTicketNum;
	}

	public String getEventTel() {
		return eventTel;
	}

	public void setEventTel(String eventTel) {
		this.eventTel = eventTel;
	}

	public String getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getActivityDateNums() {
		return activityDateNums;
	}

	public void setActivityDateNums(String activityDateNums) {
		this.activityDateNums = activityDateNums;
	}

	public String getActivityTimeDes() {
		return activityTimeDes;
	}

	public void setActivityTimeDes(String activityTimeDes) {
		this.activityTimeDes = activityTimeDes;
	}

	public String getTimeQuantum() {
		return timeQuantum;
	}

	public void setTimeQuantum(String timeQuantum) {
		this.timeQuantum = timeQuantum;
	}

	public String getActivityHost() {
		return activityHost;
	}

	public void setActivityHost(String activityHost) {
		this.activityHost = activityHost;
	}

	public String getActivityOrganizer() {
		return activityOrganizer;
	}

	public void setActivityOrganizer(String activityOrganizer) {
		this.activityOrganizer = activityOrganizer;
	}

	public String getActivityCoorganizer() {
		return activityCoorganizer;
	}

	public void setActivityCoorganizer(String activityCoorganizer) {
		this.activityCoorganizer = activityCoorganizer;
	}

	@Override
	public String toString() {
		return "EventInfo [isGroup=" + isGroup + ", tagId=" + tagId
				+ ", eventId=" + eventId + ", distance=" + distance
				+ ", eventIsCollect=" + eventIsCollect + ", activityPrice="
				+ activityPrice + ", eventIconUrl=" + eventIconUrl
				+ ", eventAddress=" + eventAddress + ", eventName=" + eventName
				+ ", eventDetailsIconUrl=" + eventDetailsIconUrl
				+ ", eventLat=" + eventLat + ", eventContent=" + eventContent
				+ ", collectNum=" + collectNum + ", eventLon=" + eventLon
				+ ", eventEndTime=" + eventEndTime + ", venueId=" + venueId
				+ ", venueName=" + venueName + ", eventTicketNum="
				+ eventTicketNum + ", eventTel=" + eventTel + ", eventCounts="
				+ eventCounts + ", activityAbleCount=" + activityAbleCount
				+ ", activityJoinMethod=" + activityJoinMethod + ", count="
				+ count + ", activitySalesOnline=" + activitySalesOnline
				+ ", activityIsReservation=" + activityIsReservation
				+ ", activityInformation=" + activityInformation
				+ ", activityStartTime=" + activityStartTime
				+ ", activityDateNums=" + activityDateNums
				+ ", activityEventimes=" + activityEventimes
				+ ", activityEventIds=" + activityEventIds
				+ ", activityTimeDes=" + activityTimeDes + ", timeQuantum="
				+ timeQuantum + ", activityRecommend=" + activityRecommend
				+ ", shareUrl=" + shareUrl + ", status=" + status
				+ ", activityIsWant=" + activityIsWant + ", activityTags="
				+ activityTags + ", activityType=" + activityType
				+ ", activityTagName=" + activityTagName + ", videoPalyList="
				+ videoPalyList + ", activityFunName=" + activityFunName
				+ ", tagColor=" + tagColor + ", tagInitial=" + tagInitial
				+ ", activitySubject=" + activitySubject + ", activitySite="
				+ activitySite + ", activityNotice=" + activityNotice
				+ ", activityPast=" + activityPast + "]";
	}

}
