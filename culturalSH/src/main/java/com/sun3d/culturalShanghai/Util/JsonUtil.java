package com.sun3d.culturalShanghai.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sun3d.culturalShanghai.activity.ActivityRoomDateilsActivity;
import com.sun3d.culturalShanghai.activity.NewOnlinSeatActivity;
import com.sun3d.culturalShanghai.handler.CodeInfo;
import com.sun3d.culturalShanghai.handler.RoomBookHandler.RoomBookInfo;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.APPVersionInfo;
import com.sun3d.culturalShanghai.object.ActivityOrderDetailInfo;
import com.sun3d.culturalShanghai.object.ActivityOtherInfo;
import com.sun3d.culturalShanghai.object.ActivityRandInfo;
import com.sun3d.culturalShanghai.object.ActivityRoomInfo;
import com.sun3d.culturalShanghai.object.BannerInfo;
import com.sun3d.culturalShanghai.object.CollectionInfor;
import com.sun3d.culturalShanghai.object.CommentInfor;
import com.sun3d.culturalShanghai.object.EventInfo;
import com.sun3d.culturalShanghai.object.GroupDeatilInfo;
import com.sun3d.culturalShanghai.object.HomeDetail_ContentInfor;
import com.sun3d.culturalShanghai.object.HomeImgInfo;
import com.sun3d.culturalShanghai.object.IndexTagsInfo;
import com.sun3d.culturalShanghai.object.KillInfo;
import com.sun3d.culturalShanghai.object.LookInfo;
import com.sun3d.culturalShanghai.object.MainGridInfo;
import com.sun3d.culturalShanghai.object.MessageInfo;
import com.sun3d.culturalShanghai.object.MoreCodeInfo;
import com.sun3d.culturalShanghai.object.MyActivityBookInfo;
import com.sun3d.culturalShanghai.object.MyActivityRoomInfo;
import com.sun3d.culturalShanghai.object.MyCollectInfo;
import com.sun3d.culturalShanghai.object.MyGroupPeopleInfor;
import com.sun3d.culturalShanghai.object.MyPastObjectInfo;
import com.sun3d.culturalShanghai.object.MyUserInfo;
import com.sun3d.culturalShanghai.object.MyVenueInfo;
import com.sun3d.culturalShanghai.object.NewInfo;
import com.sun3d.culturalShanghai.object.NotInvoluntaryInfo;
import com.sun3d.culturalShanghai.object.RoomDetailTimeSlotInfor;
import com.sun3d.culturalShanghai.object.RoomEventInfo;
import com.sun3d.culturalShanghai.object.SearchHotInfo;
import com.sun3d.culturalShanghai.object.SearchInfo;
import com.sun3d.culturalShanghai.object.SearchListInfo;
import com.sun3d.culturalShanghai.object.SpaceInfo;
import com.sun3d.culturalShanghai.object.TeamUserInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.object.UserPersionSInfo;
import com.sun3d.culturalShanghai.object.UserVoteInfo;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.object.VoteInfo;
import com.sun3d.culturalShanghai.object.WeiXinInfo;
import com.sun3d.culturalShanghai.object.WindowInfo;
import com.sun3d.culturalShanghai.seat.CH_seatInfo;
import com.sun3d.culturalShanghai.video.VideoInfo;

/**
 * Json 解析类
 * 
 * @author yangyoutao
 */

public class JsonUtil {
	private static String TAG = "JsonUtil";
	public static String JsonMSG = "请求失败";
	public static Integer status = -1;
	public static String count;// 活动筛选数

	/**
	 * 判断请求服务器是否成功
	 * 
	 * @param result
	 * @return
	 */
	public static int getJsonStatus(String result) {
		JsonMSG = "连接响应超时，请重试。";
		int Mystatus = -1;
		try {
			JSONObject json = new JSONObject(result);
			JsonMSG = json.optString("data", "连接响应超时，请重试。");
			status = Mystatus = json.optInt("status", -1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Mystatus;
	}

	public static int getForgetPWcode(String result) {
		JsonMSG = "服务器返回数据异常。";
		int Mystatus = -1;
		try {
			JSONObject json = new JSONObject(result);
			JsonMSG = json.optString("data", "服务器返回数据异常。");
			status = Mystatus = json.optInt("status", -1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Mystatus;
	}

	/**
	 * 用户信息解析
	 * 
	 * @param result
	 * @return UserInfor
	 */
	public static UserInfor getUserInforFromString(String result) {
		UserInfor mUserInfor = null;
		try {
			JSONObject data = new JSONObject(result);
			status = data.optInt("status");
			JSONArray array = data.optJSONArray("data");
			if (array != null) {
				JSONObject User = array.optJSONObject(0);
				if (null != User) {
					mUserInfor = new UserInfor();
					mUserInfor.setLoginType(User.optInt("loginType"));
					mUserInfor.setRegisterCount(User.optInt("registerCount"));
					mUserInfor.setCommentStatus(User.optInt("commentStatus"));
					mUserInfor.setUserCardNo(User.optString("userCardNo"));
					mUserInfor.setRegisterCode(User.optString("registerCode"));
					mUserInfor.setToken(User.optString("token"));
					mUserInfor.setUserAddress(User.optString("userAddress"));
					mUserInfor.setUserRemark(User.optString("userRemark"));

					mUserInfor.setUserAge(User.optInt("userAge"));
					mUserInfor.setUserArea(TextUtil.getAddresHandle(User
							.optString("userArea")));
					mUserInfor.setUserBirth(TextUtil.TimeFormat(User
							.optLong("userBirth")));
					mUserInfor.setUserCity(TextUtil.getAddresHandler(User
							.optString("userCity")));
					mUserInfor.setUserEmail(User.optString("userEmail"));
					mUserInfor.setUserHeadImgUrl(User
							.optString("userHeadImgUrl"));
					mUserInfor.setUserId(User.optString("userId"));
					mUserInfor
							.setUserIsDisable(User.optString("userIsDisable"));
					mUserInfor.setUserMobileNo(User.optString("userMobileNo"));
					mUserInfor.setUserName(User.optString("userName"));
					mUserInfor.setUserNickName(User.optString("userName"));// 这两个字段相同
					mUserInfor.setUserProvince(TextUtil.getAddresHandler(User
							.optString("userProvince")));
					mUserInfor.setUserPwd(User.optString("userPwd"));
					mUserInfor.setUserQq(User.optString("userQq"));
					mUserInfor.setUserSex(User.optInt("userSex") + "");
					mUserInfor.setUserIsLogin(User.optString("userIsLogin"));
					mUserInfor.setUserType(User.optString("userType"));
					mUserInfor.setCreateTime(TextUtil.TimeFormat(User
							.optLong("createTime")));
					mUserInfor
							.setUserTelephone(User.optString("userTelephone"));
					mUserInfor.setRegisterOrigin(User
							.optString("registerOrigin"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		return mUserInfor;

	}

	/**
	 * 获取评论列表
	 * 
	 * @param result
	 * @return
	 */
	public static List<CommentInfor> getCommentInforFromString(String result) {
		List<CommentInfor> list = new ArrayList<CommentInfor>();
		try {
			JSONObject data = new JSONObject(result);
			status = data.optInt("status");
			JSONArray comment = data.optJSONArray("data");
			if (null != comment) {
				for (int i = 0; i < comment.length(); i++) {
					JSONObject item = comment.optJSONObject(i);
					CommentInfor mCommentInfor = new CommentInfor();
					mCommentInfor.setCommentId(item.optString("commentId"));
					mCommentInfor.setCommentRemark(item
							.optString("commentRemark"));
					mCommentInfor.setCommentRkName(item
							.optString("commentUserNickName"));
					mCommentInfor.setCommentTime(item.optString("commentTime"));
					mCommentInfor.setCommentImgUrl(item
							.optString("commentImgUrl"));
					mCommentInfor.setUserHeadImgUrl(item
							.optString("userHeadImgUrl"));
					mCommentInfor.setUserHeadImgUrl(item
							.optString("userHeadImgUrl"));
					mCommentInfor.setCommentUserSex(item
							.optString("commentUserSex"));
					String rating = item.optString("commentStar");
					if (!"".equals(rating)) {
						mCommentInfor.setCommentStar(Float.parseFloat(rating));
					} else {
						mCommentInfor.setCommentStar(5.0f);
					}
					list.add(mCommentInfor);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.toString());
		}
		return list;
	}

	public static List<HomeImgInfo> getHomeImgList(String json_str)
			throws JSONException {
		List<HomeImgInfo> list = new ArrayList<HomeImgInfo>();
		JSONObject json = new JSONObject(json_str);
		JSONArray data_ja = json.getJSONArray("data");
		for (int i = 0; i < data_ja.length(); i++) {
			JSONObject jo = data_ja.getJSONObject(i);
			HomeImgInfo homeimg = new HomeImgInfo();
			homeimg.setAdvertPostion(jo.optInt("advertPosition", 0));
			homeimg.setAdvertSort(jo.optInt("advertSort", 0));
			homeimg.setCreateTime(jo.optInt("createTime", 0));
			homeimg.setUpdateTime(jo.optInt("updateTime", 0));
			homeimg.setAdvertLink(jo.optInt("advertLink", 0));
			homeimg.setAdvertState(jo.optInt("advertState", 0));
			homeimg.setAdvertLinkType(jo.optInt("advertLinkType", 0));
			homeimg.setAdvertType(jo.optString("advertType", ""));
			homeimg.setAdvertImgUrl(jo.optString("advertImgUrl", ""));
			homeimg.setAdvertTitle(jo.optString("advertTitle", ""));
			homeimg.setAdvertId(jo.optString("advertId", ""));
			homeimg.setAdvertUrl(jo.optString("advertUrl", ""));
			homeimg.setCreateBy(jo.optString("createBy", ""));
			homeimg.setUpdateBy(jo.optString("updateBy", ""));
			list.add(homeimg);

		}
		return list;
	}

	public static List<SpaceInfo> getSpaceList(String json_str)
			throws JSONException {
		List<SpaceInfo> list = new ArrayList<SpaceInfo>();
		JSONObject json = new JSONObject(json_str);
		JSONArray data_ja = json.getJSONArray("data");
		for (int i = 0; i < data_ja.length(); i++) {
			JSONObject jo = data_ja.getJSONObject(i);
			SpaceInfo info = new SpaceInfo();
			info.setAdvertPostion(jo.optInt("advertPosition", 0));
			info.setAdvertSort(jo.optInt("advertSort", 0));
			info.setCreateTime(jo.optInt("createTime", 0));
			info.setUpdateTime(jo.optInt("updateTime", 0));
			info.setAdvertLink(jo.optInt("advertLink", 0));
			info.setAdvertState(jo.optInt("advertState", 0));
			info.setAdvertLinkType(jo.optInt("advertLinkType", 0));
			info.setAdvertType(jo.optString("advertType", ""));
			info.setAdvertImgUrl(jo.optString("advertImgUrl", ""));
			info.setAdvertTitle(jo.optString("advertTitle", ""));
			info.setAdvertId(jo.optString("advertId", ""));
			info.setAdvertUrl(jo.optString("advertUrl", ""));
			info.setCreateBy(jo.optString("createBy", ""));
			info.setUpdateBy(jo.optString("updateBy", ""));
			list.add(info);

		}
		return list;
	}

	public static List<SearchHotInfo> getSearchHotList(String json_str)
			throws JSONException {
		List<SearchHotInfo> list = new ArrayList<SearchHotInfo>();
		JSONObject json = new JSONObject(json_str);
		JSONArray data_ja = json.getJSONArray("data");
		for (int i = 0; i < data_ja.length(); i++) {
			JSONObject jo = data_ja.getJSONObject(i);
			SearchHotInfo hotInfo = new SearchHotInfo();
			hotInfo.setAdvertId(jo.optString("advertId", ""));
			hotInfo.setAdvertImgUrl(jo.optString("advertImgUrl", ""));
			hotInfo.setAdvertType(jo.optString("advertType", ""));
			hotInfo.setAdvertUrl(jo.optString("advertUrl", ""));
			hotInfo.setCreateBy(jo.optString("createBy", ""));
			hotInfo.setUpdateBy(jo.optString("updateBy", ""));
			hotInfo.setAdvertTitle(jo.optString("advertTitle", ""));
			hotInfo.setAdvertPostion(jo.optInt("advertPostion", 0));
			hotInfo.setAdvertSort(jo.optInt("advertSort", 0));
			hotInfo.setCreateTime(jo.optInt("createTime", 0));
			hotInfo.setUpdateTime(jo.optInt("updateTime", 0));
			hotInfo.setAdvertLink(jo.optInt("advertLink", 0));
			hotInfo.setAdvertLinkType(jo.optInt("advertLinkType", 0));
			hotInfo.setAdvertState(jo.optInt("advertState", 0));
			list.add(hotInfo);

		}
		return list;
	}

	public static List<ActivityOtherInfo> getActivityOther(String json_str,
			String id) throws JSONException {
		List<ActivityOtherInfo> list = new ArrayList<ActivityOtherInfo>();
		JSONObject jSon = new JSONObject(json_str);
		status = jSon.optInt("status");
		count = jSon.optString("pageTotal");
		JSONArray array = jSon.optJSONArray("data");
		if (array != null && !"".equals(array)) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				ActivityOtherInfo eventInfo = new ActivityOtherInfo();
				eventInfo.setActivityLat(object.optInt("activityLat"));
				eventInfo.setSpikeType(object.optInt("spikeType"));
				eventInfo.setPriceType(object.optInt("priceType"));
				eventInfo.setActivityIsCollect(object
						.optInt("activityIsCollect"));
				eventInfo.setSysId(object.optString("sysId"));
				eventInfo.setActivityAddress(object
						.optString("activityAddress"));

				eventInfo.setActivityId(object.optString("activityId"));
				eventInfo.setActivityIconUrl(object
						.optString("activityIconUrl"));
				eventInfo.setSysNo(object.optString("sysNo"));
				eventInfo.setActivityName(object.optString("activityName"));
				eventInfo.setActivityUpdateTime(object
						.optString("activityUpdateTime"));
				eventInfo.setActivityArea(object.optString("activityArea"));
				eventInfo.setActivityEndTime(object
						.optString("activityEndTime"));

				eventInfo.setDistance(object.optString("distance"));
				eventInfo.setActivityLocationName(object
						.optString("activityLocationName"));
				eventInfo.setEventAddress(object.optString("activityAddress"));
				eventInfo.setEventIconUrl(object.optString("activityIconUrl"));
				eventInfo.setEventId(object.optString("activityId"));
				eventInfo.setActivityIsHot(object.optInt("activityIsHot"));
				eventInfo.setTagName(object.optString("tagName"));
				eventInfo.setEventIsCollect(object
						.optString("activityIsCollect"));
				eventInfo.setActivityIsFree(object.optInt("activityIsFree"));
				eventInfo.setEventPrice(object.optString("activityPrice"));
				eventInfo.setEventName(object.optString("activityName"));
				eventInfo.setEventLat(object.optString("activityLat"));
				eventInfo.setEventLon(object.optString("activityLon"));
				eventInfo.setActivityStartTime(object
						.optString("activityStartTime"));
				eventInfo.setActivityAbleCount(object
						.optString("activityAbleCount"));
				eventInfo.setEventEndTime(object.optString("activityEndTime"));
				eventInfo.setActivityJoinMethod(object
						.optString("activityJoinMethod"));
				eventInfo.setActivityIsReservation(object
						.optString("activityIsReservation"));
				eventInfo.setActivityRecommend(object
						.optString("activityRecommend"));
				eventInfo.setActivityTagName(object
						.optString("activityTagName"));
				eventInfo.setActivityFunName(object
						.optString("activityFunName"));
				eventInfo.setTagColor(object.optString("tagColor"));
				eventInfo.setTagInitial(object.optString("tagInitial"));
				eventInfo.setActivitySite(object.optString("activitySite"));
				eventInfo.setActivitySubject(object
						.optString("activitySubject"));
				eventInfo.setActivityNotice(object.optString("activityNotice"));
				eventInfo.setActivityPast(object.optString("activityPast"));
				JSONArray ja = object.optJSONArray("subList");
				ArrayList<String> list_str = new ArrayList<String>();
				for (int j = 0; j < ja.length(); j++) {
					JSONObject jo = ja.getJSONObject(j);
					String tagname = jo.optString("tagName", "");
					if (!tagname.equals("")) {
						list_str.add(tagname);
					}
				}
				eventInfo.setTagNameList(list_str);
				Log.i(TAG, "查看 id  " + object.optString("activityId"));
				if (object.optString("activityId").equals(id)) {

				} else {
					list.add(eventInfo);
				}

			}
		}
		return list;
	}

	public static List<HomeDetail_ContentInfor> getHomeDataList(String json_str)
			throws JSONException {
		List<HomeDetail_ContentInfor> list = new ArrayList<HomeDetail_ContentInfor>();
		JSONObject json = new JSONObject(json_str);
		JSONArray data_ja = json.getJSONArray("data");
		for (int i = 0; i < data_ja.length(); i++) {
			JSONObject jo = data_ja.getJSONObject(i);
			HomeDetail_ContentInfor homedata = new HomeDetail_ContentInfor();
			homedata.setTagName(jo.optString("tagName", ""));
			homedata.setActivityIsFree(jo.optInt("activityIsFree", 0));
			homedata.setActivityAddress(jo.optString("activityAddress", ""));
			homedata.setSysId(jo.optString("sysId", ""));
			homedata.setActivityId(jo.optString("activityId", ""));
			homedata.setActivityLocationName(jo.optString(
					"activityLocationName", ""));
			homedata.setActivitySite(jo.optString("activitySite", ""));
			homedata.setActivityArea(jo.optString("activityArea", ""));
			homedata.setActivityRecommend(jo.optString("activityRecommend", ""));
			homedata.setActivityIconUrl(jo.optString("activityIconUrl", ""));
			homedata.setActivitySubject(jo.optString("activitySubject", ""));
			homedata.setActivityName(jo.optString("activityName", ""));
			homedata.setActivityPrice(jo.optString("activityPrice", ""));
			homedata.setActivityEndTime(jo.optString("activityEndTime", ""));
			homedata.setActivityStartTime(jo.optString("activityStartTime", ""));
			homedata.setActivityLocation(jo.optString("activityLocation", ""));
			homedata.setSysNo(jo.optString("sysNo", ""));
			homedata.setSpikeType(jo.optInt("spikeType", 0));
			homedata.setAvailableCount(jo.optInt("availableCount", 0));
			homedata.setActivityIsReservation(jo.optInt(
					"activityIsReservation", 0));
			homedata.setActivityIsHot(jo.optInt("activityIsHot", 0));
			homedata.setPriceType(jo.optInt("priceType", 0));
			homedata.setActivityLon(jo.optInt("activityLon", 0));
			homedata.setActivityLat(jo.optInt("activityLat", 0));
			homedata.setDistance(jo.optInt("distance", 0));
			JSONArray ja = jo.optJSONArray("subList");
			ArrayList<String> list_str = new ArrayList<String>();
			for (int j = 0; j < ja.length(); j++) {
				JSONObject jo1 = ja.getJSONObject(j);
				String tagname = jo1.optString("tagName", "");
				if (!tagname.equals("")) {
					list_str.add(tagname);
				}
			}
			homedata.setTagNameList(list_str);
			list.add(homedata);

		}
		return list;
	}

	/**
	 * 活动和场馆的信息 都在这里获取
	 * 
	 * @param json_str
	 * @return
	 * @throws JSONException
	 */
	public static List<EventInfo> getSearchActivityVenueList(
			String json_str) throws JSONException {
		List<EventInfo> list = new ArrayList<EventInfo>();
		JSONObject json = new JSONObject(json_str);
		JSONArray data_ja = json.getJSONArray("data");
		for (int i = 0; i < data_ja.length(); i++) {
			JSONObject jo = data_ja.getJSONObject(i);
			EventInfo searchOb = new EventInfo();
			searchOb.setVenueIconUrl(jo.optString("venueIconUrl", ""));
			searchOb.setVenueAddress(jo.optString("venueAddress", ""));
			searchOb.setVenueName(jo.optString("venueName", ""));
			searchOb.setVenueId(jo.optString("venueId", ""));
			searchOb.setActivityAbleCount(jo.optString("activityAbleCount", "0"));
			searchOb.setSysNo(jo.optString("sysNo", "0"));
			searchOb.setActivityAddress(jo.optString("activityAddress", ""));
			searchOb.setActivitySite(jo.optString("activitySite", ""));
			searchOb.setActivityPrice(jo.optString("activityPrice", ""));
			searchOb.setTagId(jo.optInt("tagId", 0));
			searchOb.setActivityLocationName(jo.optString(
					"activityLocationName", ""));
			searchOb.setActivitySubject(jo.optString("activitySubject", ""));
			searchOb.setActivityArea(jo.optString("activityArea", ""));
			searchOb.setActivityLat(jo.optInt("activityLat", 0));
			searchOb.setActivityId(jo.optString("activityId", ""));
			searchOb.setDistance(jo.optString("distance", ""));
			searchOb.setSysId(jo.optString("sysId", ""));
			searchOb.setActivityIsFree(jo.optInt("activityIsFree", 0));
			searchOb.setActivityEndTime(jo.optString("activityEndTime", ""));
			searchOb.setActivityIconUrl(jo.optString("activityIconUrl", ""));
			if (jo.optString("tagName", "").equals("null")) {
				searchOb.setTagName("");
			} else {
				searchOb.setTagName(jo.optString("tagName", ""));
			}
			searchOb.setActivityLon(jo.optInt("activityLon", 0));
			searchOb.setActivityUpdateTime(jo.optString("activityUpdateTime",
					""));
			searchOb.setActivityIsReservation(jo.optString(
					"activityIsReservation", ""));
			searchOb.setActivityName(jo.optString("activityName", ""));
			searchOb.setActivityStartTime(jo.optString("activityStartTime", ""));
			searchOb.setActivityIsHot(jo.optInt("activityIsHot", 0));
			searchOb.setActivityRecommend(jo.optString("activityRecommend", ""));
			searchOb.setPriceType(jo.optInt("priceType", 0));
			searchOb.setSpikeType(jo.optInt("spikeType", 0));
			JSONArray ja = jo.optJSONArray("subList");
			ArrayList<String> list_str = new ArrayList<String>();
			for (int j = 0; j < ja.length(); j++) {
				JSONObject jo1 = ja.optJSONObject(j);
				String tagname = jo1.optString("tagName", "");
				list_str.add(tagname);
			}
			searchOb.setTagNameList(list_str);
			list.add(searchOb);
		}
		return list;
	}

	/**
	 * 活动列表解析
	 * 
	 * @throws JSONException
	 */
	public static List<EventInfo> getEventList(String json)
			throws JSONException {
		List<EventInfo> list = new ArrayList<EventInfo>();
		JSONObject jSon = new JSONObject(json);
		status = jSon.optInt("status");
		count = jSon.optString("pageTotal");
		JSONArray array = jSon.optJSONArray("data");
		if (array != null && !"".equals(array)) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				EventInfo eventInfo = new EventInfo();
				eventInfo.setCount(jSon.optString("pageTotal",""));
				eventInfo.setActivityLat(object.optInt("activityLat"));
				eventInfo.setSpikeType(object.optInt("spikeType"));
				eventInfo.setPriceType(object.optInt("priceType"));
				eventInfo.setActivityIsCollect(object
						.optInt("activityIsCollect"));
				eventInfo.setSysId(object.optString("sysId"));
				eventInfo.setActivityAddress(object
						.optString("activityAddress"));
				eventInfo.setActivityId(object.optString("activityId"));
				eventInfo.setActivityIconUrl(object
						.optString("activityIconUrl"));
				eventInfo.setSysNo(object.optString("sysNo"));
				eventInfo.setActivityName(object.optString("activityName"));
				eventInfo.setActivityUpdateTime(object
						.optString("activityUpdateTime"));
				eventInfo.setActivityArea(object.optString("activityArea"));
				eventInfo.setActivityEndTime(object
						.optString("activityEndTime"));

				eventInfo.setDistance(object.optString("distance"));
				if (!object.optString("distance", "").equals("")) {
					String distance_str = "--";
					double distance = Double.valueOf(object.optString(
							"distance", ""));
					if (distance < 0) {
						distance = 0;
					}
					distance = distance * 1000;
					if (distance < 1000) {
						distance_str = String.format("%.0fM", distance);
					} else if (distance < 20000) {
						distance_str = String.format("%.1fKM", distance / 1000);
					} else {
						distance_str = String.format("%.0fKM", distance / 1000);
					}
					eventInfo.setDistance_str(distance_str);
				} else {
					eventInfo.setDistance_str("");
				}

				eventInfo.setActivityLocationName(object
						.optString("activityLocationName"));
				eventInfo.setEventAddress(object.optString("activityAddress"));
				eventInfo.setEventIconUrl(object.optString("activityIconUrl"));
				eventInfo.setEventId(object.optString("activityId"));
				eventInfo.setActivityIsHot(object.optInt("activityIsHot"));
				eventInfo.setTagName(object.optString("tagName"));
				eventInfo.setEventIsCollect(object
						.optString("activityIsCollect"));
				eventInfo.setActivityIsFree(object.optInt("activityIsFree"));
				eventInfo.setEventPrice(object.optString("activityPrice"));
				eventInfo.setEventName(object.optString("activityName"));
				eventInfo.setEventLat(object.optString("activityLat"));
				eventInfo.setEventLon(object.optString("activityLon"));
				eventInfo.setActivityStartTime(object
						.optString("activityStartTime"));
				eventInfo.setActivityAbleCount(object
						.optString("activityAbleCount"));
				eventInfo.setEventEndTime(object.optString("activityEndTime"));
				eventInfo.setActivityJoinMethod(object
						.optString("activityJoinMethod"));
				eventInfo.setActivityIsReservation(object
						.optString("activityIsReservation"));
				eventInfo.setActivityRecommend(object
						.optString("activityRecommend"));
				eventInfo.setActivityTagName(object
						.optString("activityTagName"));
				eventInfo.setActivityFunName(object
						.optString("activityFunName"));
				eventInfo.setTagColor(object.optString("tagColor"));
				eventInfo.setTagInitial(object.optString("tagInitial"));
				eventInfo.setActivitySite(object.optString("activitySite"));
				eventInfo.setActivitySubject(object
						.optString("activitySubject"));
				eventInfo.setActivityNotice(object.optString("activityNotice"));
				eventInfo.setActivityPast(object.optString("activityPast"));
				JSONArray ja = object.optJSONArray("subList");
				ArrayList<String> list_str = new ArrayList<String>();
				for (int j = 0; j < ja.length(); j++) {
					JSONObject jo = ja.getJSONObject(j);
					String tagname = jo.optString("tagName", "");
					if (!tagname.equals("")) {
						list_str.add(tagname);
					}
				}
				eventInfo.setTagNameList(list_str);
				// if (object.optInt("activityPast") == 0) {
				list.add(eventInfo);
				// }
			}
		}
		return list;
	}

	/**
	 * 日历的场数
	 * 
	 * @throws JSONException
	 */
	public static ArrayList<String> getEveryDataList(String json)
			throws JSONException {
		ArrayList<String> list = new ArrayList<String>();
		JSONObject jSon = new JSONObject(json);
		status = jSon.optInt("status");
		JSONObject json_ob = jSon.optJSONObject("data");
		String[] str_arr = json_ob.toString().split(",");
		for (int i = 0; i < str_arr.length; i++) {
			list.add(str_arr[i]);
		}
		return list;
	}

	/**
	 * 首页的三个广告位
	 * 
	 * @throws JSONException
	 */
	public static List<EventInfo> getBanner(String json) throws JSONException {
		List<EventInfo> list = new ArrayList<EventInfo>();
		JSONObject jSon = new JSONObject(json);
		status = jSon.optInt("status");
		count = jSon.optString("pageTotal");
		JSONArray array = jSon.optJSONArray("data");
		if (array != null && !"".equals(array)) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				EventInfo bannerInfo = new EventInfo();

				String adv = object.getString("isContainActivtiyAdv");
				if (adv == null || adv.isEmpty()) {
					adv = "1";
				}
				bannerInfo.setIsContainActivtiyAdv(Integer.parseInt(adv));
				bannerInfo.setAdvBannerFIsLink(object
						.getInt("advBannerFIsLink"));
				String LinkType = object.getString("advBannerFLinkType");
				if (LinkType == null || LinkType.isEmpty()) {
					LinkType = "1";
				}
				bannerInfo.setAdvBannerFLinkType(Integer.parseInt(LinkType));
				bannerInfo.setAdvBannerFUrl(object.getString("advBannerFUrl"));
				bannerInfo.setAdvBannerFImgUrl(object
						.getString("advBannerFImgUrl"));

				bannerInfo.setAdvBannerSIsLink(object.optInt(
						"advBannerSIsLink", 1));
				bannerInfo.setAdvBannerSLinkType(object.optInt(
						"advBannerSLinkType", 1));
				bannerInfo.setAdvBannerSUrl(object.getString("advBannerSUrl"));
				bannerInfo.setAdvBannerSImgUrl(object
						.getString("advBannerSImgUrl"));

				bannerInfo.setAdvBannerLLinkType(object.optInt(
						"advBannerLLinkType", 1));
				bannerInfo.setAdvBannerLUrl(object.getString("advBannerLUrl"));
				bannerInfo.setAdvBannerLImgUrl(object
						.getString("advBannerLImgUrl"));
				bannerInfo.setAdvBannerLIsLink(object.optInt(
						"advBannerLIsLink", 1));
				bannerInfo.setList(object.getJSONArray("dataList"));
				// if (object.optInt("activityPast") == 0) {
				list.add(bannerInfo);
				// }
			}
		}
		return list;
	}

	/**
	 * 首页活动标签6个解析
	 * 
	 * @throws JSONException
	 */
	public static List<MainGridInfo> getMainEventSixList(String json)
			throws JSONException {
		List<MainGridInfo> list = new ArrayList<MainGridInfo>();
		JSONObject jSon = new JSONObject(json);
		status = jSon.optInt("status");
		JSONArray array = jSon.optJSONArray("data");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			if (object != null) {
				MainGridInfo eventInfo = new MainGridInfo();
				if (object.optString("activityIsExist").equals("0")) {
					eventInfo.setActivityIsExist(false);
				} else {
					eventInfo.setActivityIsExist(true);
				}
				if (object.optString("activityNearByExist").equals("0")) {
					eventInfo.setActivityNearByExist(false);
				} else {
					eventInfo.setActivityNearByExist(true);
				}
				eventInfo.setDictCode(object.optString("dictCode"));
				eventInfo.setTagId(object.optString("tagId"));
				eventInfo.setTagImageUrl(object.optString("tagImageUrl"));
				eventInfo.setTagName(object.optString("tagName"));
				list.add(eventInfo);
			}
		}
		return list;
	}

	/**
	 * 获取活动详情
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static EventInfo getEventInfo(String json) {
		EventInfo eventInfo = null;
		try {
			eventInfo = new EventInfo();
			JSONObject jSon = new JSONObject(json);
			status = jSon.optInt("status");
			if (status != 0) {
				return null;
			}
			JSONArray array = jSon.optJSONArray("data");
			if (array != null) {
				JSONObject object = array.optJSONObject(0);
				if (null != object) {
					eventInfo.setAssnId(object.optString("assnId", ""));
					eventInfo.setAssociationId(object.optString(
							"associationId", ""));
					eventInfo.setPriceType(object.optInt("priceType"));
					eventInfo.setIdentityCard(object.optInt("identityCard"));
					eventInfo.setSpikeType(object.optInt("spikeType"));
					eventInfo.setSingleEvent(object.optInt("singleEvent"));
					eventInfo
							.setActivityIsPast(object.optInt("activityIsPast"));
					eventInfo.setDeductionCredit(object
							.optString("deductionCredit"));
					eventInfo.setTicketSettings(object
							.optString("ticketSettings"));
					eventInfo.setIntegralStatus(object
							.optString("integralStatus"));
					eventInfo.setPriceDescribe(object
							.optString("priceDescribe"));
					eventInfo.setSpikeDifferences(object
							.optString("spikeDifferences"));
					eventInfo.setEventPrices(object.optString("eventPrices"));
					eventInfo.setTicketNumber(object.optString("ticketNumber"));
					eventInfo.setLowestCredit(object.optString("lowestCredit"));
					eventInfo.setActivityTel(object.optString("activityTel"));
					eventInfo.setTicketCount(object.optString("ticketCount"));
					eventInfo.setActivityTime(object.optString("activityTime"));
					eventInfo.setCostCredit(object.optString("costCredit"));
					eventInfo.setActivityPerformed(object
							.optString("activityPerformed"));
					eventInfo.setActivitySpeaker(object
							.optString("activitySpeaker"));
					eventInfo.setEventAddress(object
							.optString("activityAddress"));
					eventInfo.setActivityTips(object.optString("activityTips"));
					eventInfo.setEventId(object.optString("activityId"));
					eventInfo.setActivityHost(object.optString("activityHost"));
					eventInfo.setActivityOrganizer(object
							.optString("activityOrganizer"));
					eventInfo.setActivityCoorganizer(object
							.optString("activityCoorganizer"));
					eventInfo
							.setActivityIsFree(object.optInt("activityIsFree"));
					eventInfo.setEventIsCollect(object
							.optString("activityIsCollect"));
					eventInfo.setEventPrice(object.optString("activityPrice"));
					eventInfo.setEventName(object.optString("activityName"));
					eventInfo.setEventLat(object.optString("activityLat"));
					eventInfo.setEventLon(object.optString("activityLon"));
					eventInfo.setActivityMemo(object.optString("activityMemo"));
					eventInfo.setEventTel(object.optString("activityTel"));
					eventInfo.setEventDetailsIconUrl(object
							.optString("activityIconUrl"));
					eventInfo.setEventEndTime(object
							.optString("activityEndTime"));
					eventInfo.setVenueId(object.optString("venueId"));
					eventInfo.setVenueName(object.optString("venusName"));
					eventInfo.setEventTicketNum(object
							.optString("activityTicketNum"));
					eventInfo.setCollectNum(object.optString("collectNum"));
					eventInfo.setActivityIsReservation(object
							.optString("activityIsReservation"));
					eventInfo.setActivitySalesOnline(object
							.optString("activitySalesOnline"));
					eventInfo.setActivityInformation(object
							.optString("activityInformation"));
					eventInfo.setActivityAbleCount(object
							.optInt("activityAbleCount") + "");
					eventInfo.setActivityDateNums(object
							.optString("activityDateNums"));
					eventInfo.setActivityTimeDes(object
							.optString("activityTimeDes"));
					eventInfo.setTimeQuantum(object.optString("timeQuantum"));
					eventInfo.setActivityStartTime(object
							.optString("activityStartTime"));
					eventInfo.setActivityTimeDes(object
							.optString("activityTimeDes"));
					eventInfo.setActivityEventimes(object
							.optString("activityEventimes"));
					eventInfo.setActivityEventIds(object
							.optString("activityEventIds"));
					eventInfo.setStatus(object.optString("status"));
					eventInfo.setShareUrl(object.optString("shareUrl"));
					eventInfo.setActivityFunName(object
							.optString("activityFunName"));
					eventInfo.setTagColor(object.optString("tagColor"));
					eventInfo.setTagInitial(object.optString("tagInitial"));
					eventInfo.setActivitySite(object.optString("activitySite"));
					eventInfo.setActivityNotice(object
							.optString("activityNotice"));
					eventInfo.setActivityPast(object.optString("activityPast"));
					eventInfo.setEventCounts(object.optString("eventCounts"));
					eventInfo.setEventContent(object.optString("eventContent"));
					if (object.optJSONArray("assnSub") != null) {
						JSONArray ja = object.optJSONArray("assnSub");
						ArrayList<String> list = new ArrayList<String>();
						for (int i = 0; i < ja.length(); i++) {
							list.add(ja.getString(i));
						}
						eventInfo.setAssnSub(list);
					} else {

					}
					if (object.optInt("activityIsWant") == 0) {
						eventInfo.setWant(false);
					} else if (object.optInt("activityIsWant") == 1) {
						eventInfo.setWant(true);
					}
					if (object.optInt("activityIsWant") == 1) {
						eventInfo.setActivityIsWant(true);
					} else {
						eventInfo.setActivityIsWant(false);
					}
				}
			}
			JSONArray arrayTag = jSon.optJSONArray("data1");
			if (arrayTag != null) {
				for (int i = 0; i < arrayTag.length(); i++) {
					JSONObject object = arrayTag.optJSONObject(i);
					String tagName = object.optString("tagName", "");
					ArrayList<String> subList = new ArrayList<String>();
					JSONArray ja = object.optJSONArray("subList");
					for (int j = 0; j < ja.length(); j++) {
						JSONObject jo = ja.getJSONObject(j);
						subList.add(jo.optString("tagName", ""));
					}
					eventInfo.setTagName(tagName);
					eventInfo.setTagNameList(subList);
				}

			}
			JSONArray arrayVideo = jSon.optJSONArray("data2");
			if (arrayVideo != null) {
				List<VideoInfo> listVideo = new ArrayList<VideoInfo>();
				for (int i = 0; i < arrayVideo.length(); i++) {
					JSONObject object = arrayVideo.optJSONObject(i);
					VideoInfo vi = new VideoInfo(i,
							object.optString("videoTitle"),
							object.optString("videoImgUrl"),
							object.optString("videoLink"), object.optString(
									"videoCreateTime", ""));
					listVideo.add(vi);
				}
				eventInfo.setVideoPalyList(listVideo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventInfo;
	}

	/**
	 * 场馆详情
	 * 
	 * @param result
	 * @return
	 */
	public static VenueDetailInfor getVenueDetailInfor(String result) {
		VenueDetailInfor mVenueDetailInfor = null;
		try {
			mVenueDetailInfor = new VenueDetailInfor();
			JSONObject jSon = new JSONObject(result);
			status = jSon.optInt("status");
			if (status != 0) {
				return null;
			}
			JSONArray array = jSon.optJSONArray("data");
			if (array != null) {
				JSONObject object = array.optJSONObject(0);
				if (object != null) {
					mVenueDetailInfor.setVenuePriceNotice(object.optString(
							"venuePriceNotice", ""));
					mVenueDetailInfor.setDictName(object.optString("dictName"));
					mVenueDetailInfor.setTagName(object.optString("tagName"));
					mVenueDetailInfor.setVenueEndTime(object
							.optString("venueEndTime"));
					mVenueDetailInfor.setVenueIsFree(object
							.optString("venueIsFree"));
					mVenueDetailInfor.setVenueIsWant(object
							.optInt("venueIsWant"));
					// mVenueDetailInfor.setOpenNotice(object
					// .optString("openNotice"));

					mVenueDetailInfor
							.setCollectNum(object.optInt("collectNum"));
					mVenueDetailInfor.setVenueAddress(object
							.optString("venueAddress"));
					if (object.optString("venueHasAntique").equals("2")) {
						mVenueDetailInfor.setVenueHasAntique(true);
					} else {
						mVenueDetailInfor.setVenueHasAntique(false);
					}
					if (object.optString("venueHasRoom").equals("2")) {
						mVenueDetailInfor.setVenueHasRoom(true);
					} else {
						mVenueDetailInfor.setVenueHasRoom(false);
					}
					mVenueDetailInfor.setVenueIconUrl(object
							.optString("venueIconUrl"));
					mVenueDetailInfor.setVenueId(object.optString("venueId"));
					if (object.optString("venueIsCollect").equals("1")) {
						mVenueDetailInfor.setVenueIsCollect(true);
					} else {
						mVenueDetailInfor.setVenueIsCollect(false);
					}

					// mVenueDetailInfor.setVenuePrice(MyApplication.getContext().getResources().getString(R.string.string_money)
					// + object.optString("venuePrice"));
					mVenueDetailInfor.setVenuePrice(object
							.optString("venuePrice"));

					mVenueDetailInfor.setVenueLat(object.optString("venueLat"));
					mVenueDetailInfor.setVenueLon(object.optString("venueLon"));
					mVenueDetailInfor.setVenueMobile(object
							.optString("venueMobile"));
					mVenueDetailInfor.setVenueName(object
							.optString("venueName"));
					mVenueDetailInfor.setVenueOpenTime(object
							.optString("venueOpenTime"));
					mVenueDetailInfor.setVenuEndTime(TextUtil
							.TimeFormatTime(object.optLong("venuEndTime")));
					mVenueDetailInfor.setVenueWeek(object
							.optString("venueWeek"));
					mVenueDetailInfor.setVenueMemo(object
							.optString("venueMemo"));
					mVenueDetailInfor.setVenueRating(Float.parseFloat(object
							.optString("venueStars")));
					mVenueDetailInfor.setVenuePersonName(object
							.optString("remarkName"));
					mVenueDetailInfor.setVenueComment(object
							.optString("commentRemark"));
					mVenueDetailInfor.setVenueHasBus(object
							.optString("venueHasBus"));
					mVenueDetailInfor.setVenueHasMetro(object
							.optString("venueHasMetro"));
					mVenueDetailInfor.setRoomNamesList(object
							.optString("venueName")
							+ ","
							+ object.optString("roomNames"));
					mVenueDetailInfor.setRoomIconUrlList(object
							.optString("venueIconUrl")
							+ ","
							+ object.optString("roomIconUrl"));
					mVenueDetailInfor.setOpenNotice(object
							.optString("openNotice"));
					mVenueDetailInfor.setShareUrl(object.optString("shareUrl"));
					mVenueDetailInfor.setVenueVoiceUrl(object
							.optString("venueVoiceUrl"));
					mVenueDetailInfor.setRemarkUserSex(object
							.optString("remarkUserSex"));
					JSONArray ja = object.optJSONArray("subList");
					ArrayList<String> list_str = new ArrayList<String>();
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = ja.optJSONObject(i);
						String tagname = jo.optString("tagName", "");
						list_str.add(tagname);
					}
					mVenueDetailInfor.setTagNameList(list_str);
					if (object.optString("venueIsReserve").equals("2")) {
						mVenueDetailInfor.setVenueIsReserve(true);
					} else {
						mVenueDetailInfor.setVenueIsReserve(false);
					}

				}
			}
			JSONArray arrayVideo = jSon.optJSONArray("data1");
			if (arrayVideo != null) {
				List<VideoInfo> listVideo = new ArrayList<VideoInfo>();
				for (int i = 0; i < arrayVideo.length(); i++) {
					JSONObject object = arrayVideo.optJSONObject(i);
					VideoInfo vi = new VideoInfo(i,
							object.optString("videoTitle"),
							object.optString("videoImgUrl"),
							object.optString("videoLink"),
							object.optString("videoCreateTime"));
					listVideo.add(vi);
				}
				mVenueDetailInfor.setVideoPalyList(listVideo);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mVenueDetailInfor = null;
		}

		return mVenueDetailInfor;
	}

	/**
	 * 活动室列表
	 */
	public static List<ActivityRoomInfo> getRoomList(String json) {
		List<ActivityRoomInfo> list = new ArrayList<ActivityRoomInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					if (null != item) {
						ActivityRoomInfo info = new ActivityRoomInfo();
						info.setRoomId(item.optString("roomId"));
						info.setRoomIsFree(item.optInt("roomIsFree"));
						info.setRoomArea(item.optString("roomArea"));
						info.setRoomCapacity(item.optString("roomCapacity"));
						info.setRoomName(item.optString("roomName"));
						info.setRoomNo(item.optString("roomNo"));
						info.setOrderNum(item.optString("orderNum"));
						info.setRoomPicUrl(item.optString("roomPicUrl"));
						info.setRoomTag(item.optString("roomTagName"));
						info.setSysNo(item.optInt("sysNo"));
						if (!item.optString("roomIsReserve").equals("0")) {
							info.setVenueIsReserve(true);
						} else {
							info.setVenueIsReserve(false);
						}
						list.add(info);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据展馆查询相关活动
	 */
	public static List<EventInfo> getAtivityList(String json) {
		List<EventInfo> list = new ArrayList<EventInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					if (null != item) {
						EventInfo info = new EventInfo();
						info.setEventId(item.optString("activityId"));
						info.setEventName(item.optString("activityName"));
						info.setEventPrice(item.optString("activityPrice"));
						info.setEventEndTime(item.optString("activityEndTime"));
						info.setActivityStartTime(item
								.optString("activityStartTime"));

						list.add(info);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * popwindos泡泡窗口的条件标签
	 */
	public static SearchListInfo getSearchListInfo(String json) {
		SearchListInfo info = new SearchListInfo();
		try {
			JSONObject object = new JSONObject(json);
			// 心情 3.5变成了地区
			JSONArray mood = object.optJSONArray("data");
			List<SearchInfo> moodList = new ArrayList<SearchInfo>();
			if (mood != null) {
				for (int i = 0; i < mood.length(); i++) {
					JSONObject item = mood.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo
							.setTagId(item.optString("areaCode").split(":")[0]);
					searchInfo
							.setTagName(item.optString("areaCode").split(":")[1]);
					searchInfo.setTagImageUrl(item.optString("tagImageUrl"));
					moodList.add(searchInfo);
				}
				info.setMoodList(moodList);
			}
			// 类型 3.5变成了分类
			JSONArray type = object.optJSONArray("data1");
			List<SearchInfo> typeList = new ArrayList<SearchInfo>();
			if (type != null) {
				for (int i = 0; i < type.length(); i++) {
					JSONObject item = type.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));
					searchInfo.setTagName(item.optString("tagName"));
					searchInfo.setTagImageUrl(item.optString("tagImageUrl"));
					typeList.add(searchInfo);
				}
				info.setTypeList(typeList);
			}
			// 人群 3.5变成了热门话题
			JSONArray personal = object.optJSONArray("data2");
			List<SearchInfo> personalList = new ArrayList<SearchInfo>();
			if (personal != null) {
				for (int i = 0; i < personal.length(); i++) {
					JSONObject item = personal.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));

					searchInfo.setTagName(item.optString("hotKeywords"));
					searchInfo.setTagImageUrl(item.optString("tagImageUrl"));
					personalList.add(searchInfo);
				}
				info.setPersonalList(personalList);
			}
			JSONArray addres = object.optJSONArray("data3");
			List<SearchInfo> addresList = new ArrayList<SearchInfo>();
			if (addres != null) {
				for (int i = 0; i < addres.length(); i++) {
					JSONObject item = addres.optJSONObject(i);
					SearchInfo adressInfo = new SearchInfo();
					adressInfo.setTagId(TextUtil.getAddresText(
							item.optString("areaCode"), 0));
					adressInfo.setTagName(TextUtil.getAddresText(
							item.optString("areaCode"), 1));
					addresList.add(adressInfo);
				}
				info.setAddresList(addresList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * popwindos泡泡窗口的条件标签
	 */
	public static SearchListInfo getRegionSearchListInfo(String json) {
		SearchListInfo info = new SearchListInfo();
		try {
			JSONObject object = new JSONObject(json);
			// 心情 3.5变成了地区
			JSONArray mood = object.optJSONArray("data");
			List<SearchInfo> moodList = new ArrayList<SearchInfo>();
			if (mood != null) {
				for (int i = 0; i < mood.length(); i++) {
					JSONObject item = mood.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));
					searchInfo.setTagName(item.optString("tagName"));
					moodList.add(searchInfo);
				}
				info.setMoodList(moodList);
			}
			// 类型 3.5变成了分类
			JSONArray type = object.optJSONArray("data1");
			List<SearchInfo> typeList = new ArrayList<SearchInfo>();
			if (type != null) {
				for (int i = 0; i < type.length(); i++) {
					JSONObject item = type.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));
					searchInfo.setTagName(item.optString("tagName"));
					searchInfo.setTagImageUrl(item.optString("tagImageUrl"));
					typeList.add(searchInfo);
				}
				info.setTypeList(typeList);
			}
			// 人群 3.5变成了热门话题
			JSONArray personal = object.optJSONArray("data2");
			List<SearchInfo> personalList = new ArrayList<SearchInfo>();
			if (personal != null) {
				for (int i = 0; i < personal.length(); i++) {
					JSONObject item = personal.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));

					searchInfo.setTagName(item.optString("tagName"));
					personalList.add(searchInfo);
				}
				info.setPersonalList(personalList);
			}
			JSONArray addres = object.optJSONArray("data3");
			List<SearchInfo> addresList = new ArrayList<SearchInfo>();
			if (addres != null) {
				for (int i = 0; i < addres.length(); i++) {
					JSONObject item = addres.optJSONObject(i);
					SearchInfo adressInfo = new SearchInfo();
					adressInfo.setTagId(TextUtil.getAddresText(
							item.optString("areaCode"), 0));
					adressInfo.setTagName(TextUtil.getAddresText(
							item.optString("areaCode"), 1));
					addresList.add(adressInfo);
				}
				info.setAddresList(addresList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 获取所有藏品和藏品选择条件
	 */
	public static void getCollectionInfo(List<String> Timelist,
			List<String> Typelist, List<CollectionInfor> list, String resultjson) {
		try {
			JSONObject jSon = new JSONObject(resultjson);
			status = jSon.optInt("status");
			/**
			 * 添加藏品列表
			 */
			JSONArray collectionarray = jSon.optJSONArray("data");
			if (collectionarray != null) {
				for (int i = 0; i < collectionarray.length(); i++) {
					JSONObject object = collectionarray.optJSONObject(i);
					if (object != null) {
						CollectionInfor mCollectionInfor = new CollectionInfor();
						mCollectionInfor.setCollectionImgUrl(object
								.optString("antiqueImgUrl"));
						mCollectionInfor.setId(object.optString("antiqueId"));
						mCollectionInfor.setCollectionName(object
								.optString("antiqueName"));
						mCollectionInfor.setCollectionTime(object
								.optString("antiqueTime"));
						list.add(mCollectionInfor);
					}
				}
			}
			/**
			 * 添加朝代
			 */
			JSONArray timearray = jSon.optJSONArray("data2");
			if (null != timearray) {
				for (int i = 0; i < timearray.length(); i++) {
					JSONObject object = timearray.optJSONObject(i);
					if (object != null) {
						Timelist.add(object
								.optString(HttpUrlList.Collection.COLLECTION_TIME));
					}
				}

			}
			/**
			 * 添加类别
			 */
			JSONArray Typearray = jSon.optJSONArray("data1");
			if (null != Typearray) {
				for (int i = 0; i < Typearray.length(); i++) {
					JSONObject object = Typearray.optJSONObject(i);
					if (object != null) {
						Typelist.add(object
								.optString(HttpUrlList.Collection.COLLECTION_TYPE));
					}
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 藏品详情
	 */
	public static CollectionInfor getCollectionDetailInfo(String result) {
		CollectionInfor mCollectionInfor = new CollectionInfor();
		try {
			JSONObject jSon = new JSONObject(result);
			status = jSon.optInt("status");
			JSONArray array = jSon.optJSONArray("data");
			if (array != null) {
				JSONObject object = array.optJSONObject(0);
				if (object != null) {
					mCollectionInfor.setCollectionImgUrl(object
							.optString("antiqueImgUrl"));
					mCollectionInfor.setCollectionInfor(object
							.optString("antiqueRemark"));
					mCollectionInfor.setCollectionMP3url(object
							.optString("antiqueVoiceUrl"));
					mCollectionInfor.setCollectionName(object
							.optString("antiqueName"));
					mCollectionInfor.setCollectionSpec(object
							.optString("antiqueSpectfication"));
					mCollectionInfor.setCollectionVeune(object
							.optString("venueName"));
					mCollectionInfor.setCollectionTime(object
							.optString("antiqueTime"));
					mCollectionInfor.setId(object.optString("antiqueId"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mCollectionInfor;

	}

	/**
	 * 我的订单，活动订单与场馆订单以及以往订单
	 */
	public static List<Object> getMyOrderEventList(String json) {
		List<Object> myList = new ArrayList<Object>();
		ActivityRoomInfo mActivityRoomInfo = null;
		try {
			JSONObject object = new JSONObject(json);
			JSONArray data1 = object.optJSONArray("data");
			if (null != data1) {
				JSONObject data = data1.optJSONObject(0);
				if (null != data) {
					mActivityRoomInfo = new ActivityRoomInfo();
					mActivityRoomInfo.setRoomId(data.optString("roomId"));
					mActivityRoomInfo.setRoomCapacity(data
							.optString("roomCapacity"));
					mActivityRoomInfo.setRoomName(data.optString("roomName"));
					mActivityRoomInfo.setRoomFacility(data
							.optString("facility"));
					mActivityRoomInfo.setRoomPicUrl(data
							.optString("roomPicUrl"));
					mActivityRoomInfo.setRoomInformation(data
							.optString("roomInformation"));
					mActivityRoomInfo.setRoomConsultTel(data
							.optString("roomConsultTel"));
					mActivityRoomInfo.setRoomFee(data.optInt("roomFee"));
					mActivityRoomInfo.setRoomArea(data.optString("roomArea"));
					mActivityRoomInfo.setVenueAddress(data
							.optString("venueAddress"));
					mActivityRoomInfo.setVenueName(data.optString("venueName"));
					mActivityRoomInfo.setRoomTag(data.optString("roomTagName"));
					mActivityRoomInfo.setSysNo(data.optInt("sysNo", 0));
					mActivityRoomInfo.setMemo(data.optString("roomRemark"));
				}
			}
			JSONArray data2 = object.optJSONArray("data1");
			if (null != data2) {
				for (int i = 0; i < data2.length(); i++) {
					JSONObject data = data2.optJSONObject(i);
					if (null != data) {
						RoomDetailTimeSlotInfor mRoomDetailTimeSlotInfor = new RoomDetailTimeSlotInfor();
						mRoomDetailTimeSlotInfor.setDate(data
								.optString("curDate"));
						mRoomDetailTimeSlotInfor.setTimeslot(data
								.optString("openPeriod"));
						mRoomDetailTimeSlotInfor.setBookId(data
								.optString("bookId"));
						mRoomDetailTimeSlotInfor.setStatus(data
								.optString("status"));
						ActivityRoomDateilsActivity.timelist
								.add(mRoomDetailTimeSlotInfor);
					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return myList;
	}

	/**
	 * 我的订单，活动室
	 */
	public static List<MyActivityRoomInfo> getMyNewRoomList(String json) {
		List<MyActivityRoomInfo> list = new ArrayList<MyActivityRoomInfo>();
		try {

			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data1");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyActivityRoomInfo info = new MyActivityRoomInfo();
					JSONObject item = array.optJSONObject(i);
					info.setCheckStatus(item.optInt("checkStatus"));
					info.setVenueAddress(item.optString("venueAddress"));
					info.setRoomTime(item.optString("roomTime"));
					info.setRoomOrderCreateTime(item
							.optString("roomOrderCreateTime"));
					info.setRoomName(item.optString("roomName"));
					info.setOrderTime(item.optString("orderTime"));
					info.setTuserTeamName(item.optString("tuserTeamName"));
					info.setRoomOrderId(item.optString("roomOrderId"));
					info.setRoomPicUrl(item.optString("roomPicUrl"));
					info.setValidCode(item.optString("validCode"));
					info.setRoomId(item.optString("roomId"));
					info.setVenueName(item.optString("venueName"));
					info.setRoomOrderNo(item.optString("roomOrderNo"));
					info.setOrderStatus(item.optString("orderStatus"));
					info.setRoomQrcodeUrl(item.optString("roomQrcodeUrl"));
					list.add(info);

				}
			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * app活动场次列表 秒杀
	 */
	public static List<KillInfo> getDetailKillList(String json) {
		List<KillInfo> list = new ArrayList<KillInfo>();
		try {

			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					KillInfo info = new KillInfo();
					JSONObject item = array.optJSONObject(i);
					info.setEventId(item.optString("eventId"));
					info.setEventEndDate(item.optString("eventEndDate"));
					info.setEventTime(item.optString("eventTime"));
					info.setOrderPrice(item.optString("orderPrice"));
					info.setSpikeTime(item.optString("spikeTime"));
					info.setEventDate(item.optString("eventDate"));
					info.setAvailableCount(item.optInt("availableCount"));
					info.setSpikeDifference(item.optInt("spikeDifference"));
					info.setSingleEvent(item.optInt("singleEvent"));
					info.setSpikeType(item.optInt("spikeType"));
					list.add(info);
				}

			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的订单，活动室
	 */
	public static List<ActivityOrderDetailInfo> getActivityOrderDetailList(
			String json) {
		List<ActivityOrderDetailInfo> list = new ArrayList<ActivityOrderDetailInfo>();
		try {

			JSONObject object = new JSONObject(json);
			JSONObject jo = object.optJSONObject("data");
			if (jo != null) {
				ActivityOrderDetailInfo info = new ActivityOrderDetailInfo();
				info.setActivitySite(jo.optString("activitySite"));
				info.setTuserIsDisplay(jo.optString("tuserIsDisplay"));
				info.setBookStatus(jo.optInt("bookStatus"));
				info.setUserType(jo.optInt("userType"));
				info.setActivityIconUrl(jo.optString("activityIconUrl"));
				info.setCostTotalCredit(jo.optInt("costTotalCredit"));
				info.setVenueLat(jo.optInt("venueLat"));
				info.setVenueLon(jo.optInt("venueLon"));
				info.setStr_UserId(jo.optString("tuserId"));
				info.setTuserId(jo.optInt("tuserId"));
				info.setBookStauts(jo.optInt("bookStauts"));
				info.setRoomId(jo.optString("roomId"));
				info.setVenueIconUrl(jo.optString("venueIconUrl"));
				info.setDate(jo.optString("date"));
				info.setPrice(jo.optString("price"));
				info.setCheckStatus(jo.optString("checkStatus"));
				info.setRoomOrderId(jo.optString("roomOrderId"));
				info.setRoomName(jo.optString("roomName"));
				info.setRoomPicUrl(jo.optString("roomPicUrl"));
				info.setOpenPeriod(jo.optString("openPeriod"));
				info.setOrderTel(jo.optString("orderTel"));
				info.setRoomQrcodeUrl(jo.optString("roomQrcodeUrl"));
				info.setTuserName(jo.optString("tuserName"));
				info.setVenueId(jo.optString("venueId"));
				info.setActivityIsReservation(jo
						.optInt("activityIsReservation"));
				info.setOrderVotes(jo.optInt("orderVotes"));
				info.setOrderTime(jo.optInt("orderTime"));
				info.setOrderPayStatus(jo.optInt("orderPayStatus"));
				info.setOrderPayTime(jo.optString("orderPayTime"));
				info.setVenueAddress(jo.optString("venueAddress"));
				info.setOrderName(jo.optString("orderName"));
				info.setActivityQrcodeUrl(jo.optString("activityQrcodeUrl"));
				info.setOrderNumber(jo.optString("orderNumber"));
				info.setOrderLine(jo.optString("orderLine"));
				info.setActivityName(jo.optString("activityName"));
				info.setActivityAddress(jo.optString("activityAddress"));
				info.setOrderPhoneNo(jo.optString("orderPhoneNo"));
				info.setActivityEventDateTime(jo
						.optString("activityEventDateTime"));
				info.setVenueName(jo.optString("venueName"));
				info.setOrderValidateCode(jo.optString("orderValidateCode"));
				info.setActivityId(jo.optString("activityId"));
				info.setActivitySeats(jo.optString("activitySeats"));
				info.setActivitySalesOnline(jo.optString("activitySalesOnline"));
				info.setActivityOrderId(jo.optString("activityOrderId"));
				list.add(info);

			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的活动,以往活动
	 */
	public static List<MyPastObjectInfo> getMyPastEventList(String json) {
		List<MyPastObjectInfo> list = new ArrayList<MyPastObjectInfo>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyPastObjectInfo info = new MyPastObjectInfo();
					JSONObject item = array.optJSONObject(i);
					info.setId(item.optString("activityId"));
					info.setTitle(item.optString("activityName"));
					info.setLocation(item.optString("activityAddress"));
					info.setOrderNumber(item.optString("orderNumber"));
					info.setOrderId(item.optString("activityOrderId"));
					list.add(info);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的活动，当前活动
	 */
	public static List<MyActivityBookInfo> getMyNewEventList(String json) {
		List<MyActivityBookInfo> list = new ArrayList<MyActivityBookInfo>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyActivityBookInfo info = new MyActivityBookInfo();
					JSONObject item = array.optJSONObject(i);
					info.setCommentCount(item.optString("activityCommentNum"));
					info.setActivitySalesOnline(item
							.optString("activitySalesOnline"));
					info.setActivityAddress(item.optString("activityAddress"));
					info.setActivityIconUrl(item.optString("activityIconUrl"));
					info.setActivityId(item.optString("activityId"));
					info.setActivityName(item.optString("activityName"));
					info.setActivityOrderId(item.optString("activityOrderId"));
					info.setActivityEventDateTime(item
							.optString("activityEventDateTime"));
					info.setOrderNumber(item.optString("orderNumber"));
					info.setPrice(item.optString("orderPrice"));
					info.setOrderPrice(item.optInt("orderPrice"));
					info.setOrderTime(item.optString("orderTime"));
					info.setOrderValidateCode(item
							.optString("orderValidateCode"));
					info.setOrderVotes(item.optString("orderVotes"));
					info.setActivityQrcodeUrl(item
							.optString("activityQrcodeUrl"));
					info.setActivitySeats(item.optString("activitySeats"));
					info.setOrderLine(item.optString("orderLine"));
					info.setOrderSummary(item.optString("orderSummary"));
					info.setActivityIsReservation(item
							.optString("activityIsReservation"));
					info.setOrderPayStatus(item.optString("orderPayStatus"));
					if (item.optString("activitySalesOnline").equals("Y")) {
						info.setIsSeatOnline(true);
					} else {
						info.setIsSeatOnline(false);
					}
					// 初始化我的活动，在线选座全部设置为不选中
					info.setoSummary(Utils.sizeBoo(item.optString("orderLine")));
					list.add(info);

				}
			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的信息
	 */
	public static List<MyUserInfo> getMyUserInfoList(String json) {
		List<MyUserInfo> list = new ArrayList<MyUserInfo>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyUserInfo info = new MyUserInfo();
					JSONObject item = array.optJSONObject(i);
					info.setUserIsDisable(item.optInt("userIsDisable"));
					info.setUserSex(item.optInt("userSex"));
					info.setUserIntegral(item.optInt("userIntegral"));
					info.setUserBirth(item.optInt("userBirth"));
					info.setCommentStatus(item.optInt("commentStatus"));
					info.setRegisterCount(item.optInt("registerCount"));
					info.setUserType(item.optInt("userType"));
					info.setTeamUserSize(item.optInt("teamUserSize"));
					info.setUserIsLogin(item.optInt("userIsLogin"));
					info.setRegisterOrigin(item.optInt("registerOrigin"));
					info.setUserProvince(item.optString("userProvince"));
					info.setUserHeadImgUrl(item.optString("userHeadImgUrl"));
					info.setUserTelephone(item.optString("userTelephone"));
					info.setUserPwd(item.optString("userPwd"));
					info.setUserMobileNo(item.optString("userMobileNo"));
					info.setUserAge(item.optString("userAge"));
					info.setUserCardNo(item.optString("userCardNo"));
					info.setUserEmail(item.optString("userEmail"));
					info.setUserNickName(item.optString("userNickName"));
					info.setRegisterCode(item.optString("registerCode"));
					info.setUserQq(item.optString("userQq"));
					info.setUserName(item.optString("userName"));
					info.setUserId(item.optString("userId"));
					info.setUserArea(item.optString("userArea"));
					info.setUserAddress(item.optString("userAddress"));
					info.setUserRemark(item.optString("userRemark"));

					info.setUserCity(item.optString("userCity"));

					list.add(info);

				}
			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的活动室详情
	 */
	public static List<RoomEventInfo> getRoomEvent(String json) {
		List<RoomEventInfo> list = new ArrayList<RoomEventInfo>();
		try {
			JSONObject ob = new JSONObject(json);
			JSONObject object = ob.optJSONObject("data");
			JSONArray array = object.optJSONArray("teamList");
			int num;
			if (array.length() == 0 || array == null) {
				Log.i("ceshi", "array 空掉了==  ");
				num = 1;
				for (int i = 0; i < num; i++) {
					JSONObject item = array.optJSONObject(i);
					RoomEventInfo info = new RoomEventInfo();
					info.setDate(object.optString("date"));
					info.setRoomPicUrl(object.optString("roomPicUrl"));
					info.setPrice(object.optString("price"));
					info.setCmsVenueName(object.optString("cmsVenueName"));
					info.setOrderTel(object.optString("orderTel"));
					info.setRoomName(object.optString("roomName"));
					info.setOpenPeriod(object.optString("openPeriod"));
					info.setCmsRoomBookId(object.optString("cmsRoomBookId"));
					info.setOrderName(object.optString("orderName"));
					info.setVenueLat(object.optInt("venueLat"));
					info.setVenueLon(object.optInt("venueLon"));
					if (item != null) {
						info.setTuserId(item.optString("tuserId"));
						info.setTuserName(item.optString("tuserName"));
					}

					list.add(info);

				}
			} else {
				num = array.length();
				Log.i("ceshi", "array 没有空==  ");
				for (int i = 0; i < num; i++) {
					JSONObject item = array.optJSONObject(i);
					RoomEventInfo info = new RoomEventInfo();
					info.setDate(object.optString("date"));
					info.setRoomPicUrl(object.optString("roomPicUrl"));
					info.setPrice(object.optString("price"));
					info.setCmsVenueName(object.optString("cmsVenueName"));
					info.setOrderTel(object.optString("orderTel"));
					info.setRoomName(object.optString("roomName"));
					info.setOpenPeriod(object.optString("openPeriod"));
					info.setCmsRoomBookId(object.optString("cmsRoomBookId"));
					info.setOrderName(object.optString("orderName"));
					info.setVenueLat(object.optInt("venueLat"));
					info.setVenueLon(object.optInt("venueLon"));
					info.setTuserId(item.optString("tuserId"));
					info.setTuserName(item.optString("tuserName"));
					list.add(info);

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的活动待审核
	 */
	public static List<MyActivityBookInfo> getMyBeingEventList(String json) {
		List<MyActivityBookInfo> list = new ArrayList<MyActivityBookInfo>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data");

			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyActivityBookInfo info = new MyActivityBookInfo();
					JSONObject item = array.optJSONObject(i);
					info.setTuserIsDisplay(item.optString("tuserIsDisplay"));
					info.setRoomTime(item.optString("roomTime"));
					info.setPrice(item.optString("price"));
					info.setTuserId(item.optString("tuserId"));
					info.setTuserTeamName(item.optString("tuserTeamName"));
					info.setValidCode(item.optString("validCode"));
					info.setOrderStatus(item.optInt("orderStatus"));
					info.setRoomName(item.optString("roomName"));
					info.setRoomId(item.optString("roomId"));
					info.setRoomOrderId(item.optString("roomOrderId"));
					info.setVenueName(item.optString("venueName"));
					info.setOrderTime(item.optString("orderTime"));
					info.setRoomOrderNo(item.optString("roomOrderNo"));
					info.setRoomQrcodeUrl(item.optString("roomQrcodeUrl"));
					info.setVenueAddress(item.optString("venueAddress"));
					info.setVenueAddress(item.optString("venueAddress"));
					info.setRoomPicUrl(item.optString("roomPicUrl"));
					list.add(info);

				}
			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的30天积分列表
	 */
	public static List<CodeInfo> getMyCode(String json) {
		List<CodeInfo> list = new ArrayList<CodeInfo>();
		try {
			JSONObject object = new JSONObject(json);
			JSONObject jo = object.getJSONObject("data");
			JSONArray array = jo.optJSONArray("userIntegralDetails");

			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					CodeInfo info = new CodeInfo();
					JSONObject item = array.optJSONObject(i);
					info.setIntegralNow(jo.optInt("integralNow", 0));
					info.setDate(item.optString("date"));
					info.setName(item.optString("name"));
					info.setDescription(item.optString("description"));
					info.setChangeType(item.optInt("changeType"));
					info.setIntegralChange(item.optInt("integralChange"));
					list.add(info);

				}
			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的30天积分列表
	 */
	public static List<MoreCodeInfo> getMyMoreCode(String json) {
		List<MoreCodeInfo> list = new ArrayList<MoreCodeInfo>();
		try {
			JSONObject object = new JSONObject(json);
			JSONObject jo = object.getJSONObject("data");
			JSONArray array = jo.optJSONArray("userIntegralDetails");

			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MoreCodeInfo info = new MoreCodeInfo();
					JSONObject item = array.optJSONObject(i);
					info.setIntegralNow(jo.optInt("integralNow", 0));
					info.setDate(item.optString("date"));
					info.setName(item.optString("name"));
					info.setDescription(item.optString("description"));
					info.setChangeType(item.optInt("changeType"));
					info.setIntegralChange(item.optInt("integralChange"));
					list.add(info);

				}
			} else {
				JsonMSG = object.optString("data");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 用户注册返回userId
	 */
	public static String getUserId(String json) {
		String userId = "";
		try {
			JSONObject object = new JSONObject(json);
			userId = object.optString("userId");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userId;
	}

	/**
	 * 获取活动收藏列表
	 */
	public static List<EventInfo> getCollectActivityList(String json) {
		List<EventInfo> list = new ArrayList<EventInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.optJSONObject(i);
				EventInfo info = new EventInfo();
				info.setEventId(item.optString("activityId"));
				info.setEventAddress(item.optString("activityAddress"));
				info.setEventName(item.optString("activityName"));
				list.add(info);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取场馆收藏列表
	 */
	public static List<VenueDetailInfor> getCollectVenueList(String json) {
		List<VenueDetailInfor> list = new ArrayList<VenueDetailInfor>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.optJSONObject(i);
				VenueDetailInfor info = new VenueDetailInfor();
				info.setVenueId(item.optString("venueId"));
				info.setVenueAddress(item.optString("venueAddress"));
				info.setVenueName(item.optString("venueName"));
				list.add(info);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 活动室详情
	 */
	public static ActivityRoomInfo getActivityRoomDetailInfo(String json) {
		ActivityRoomInfo mActivityRoomInfo = null;
		try {

			JSONObject object = new JSONObject(json);
			JSONArray data1 = object.optJSONArray("data");
			if (null != data1) {
				JSONObject data = data1.optJSONObject(0);
				if (null != data) {
					mActivityRoomInfo = new ActivityRoomInfo();
					mActivityRoomInfo.setRoomId(data.optString("roomId"));
					mActivityRoomInfo.setRoomTel(data.optString("roomTel"));
					mActivityRoomInfo.setRoomCapacity(data
							.optString("roomCapacity"));
					mActivityRoomInfo.setRoomName(data.optString("roomName"));
					mActivityRoomInfo.setRoomFacility(data
							.optString("facility"));
					mActivityRoomInfo.setRoomPicUrl(data
							.optString("roomPicUrl"));
					mActivityRoomInfo.setRoomInformation(data
							.optString("roomInformation"));
					mActivityRoomInfo.setRoomConsultTel(data
							.optString("roomConsultTel"));
					mActivityRoomInfo.setRoomFee(data.optInt("roomFee"));
					mActivityRoomInfo.setRoomArea(data.optString("roomArea"));
					mActivityRoomInfo.setVenueAddress(data
							.optString("venueAddress"));
					mActivityRoomInfo.setVenueName(data.optString("venueName"));
					mActivityRoomInfo.setRoomTag(data.optString("roomTagName"));
					mActivityRoomInfo.setSysNo(data.optInt("sysNo", 0));
					mActivityRoomInfo.setMemo(data.optString("roomRemark"));
					JSONArray ja = data.optJSONArray("subList");
					ArrayList<String> list_str = new ArrayList<String>();
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = ja.optJSONObject(i);
						String tagname = jo.optString("tagName", "");
						list_str.add(tagname);
					}
					mActivityRoomInfo.setTagNameList(list_str);
				}
			}
			JSONArray data2 = object.optJSONArray("data1");
			if (null != data2) {
				for (int i = 0; i < data2.length(); i++) {
					JSONObject data = data2.optJSONObject(i);
					if (null != data) {
						RoomDetailTimeSlotInfor mRoomDetailTimeSlotInfor = new RoomDetailTimeSlotInfor();
						mRoomDetailTimeSlotInfor.setDate(data
								.optString("curDate"));
						mRoomDetailTimeSlotInfor.setBookStatus(data
								.optString("bookStatus"));
						mRoomDetailTimeSlotInfor.setTimeslot(data
								.optString("openPeriod"));
						mRoomDetailTimeSlotInfor.setBookId(data
								.optString("bookId"));
						mRoomDetailTimeSlotInfor.setStatus(data
								.optString("status"));
						if (!data.optString("bookStatus").equals("")) {
							ActivityRoomDateilsActivity.timelist
									.add(mRoomDetailTimeSlotInfor);
						}

					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mActivityRoomInfo;
	}

	/**
	 * 获取活动室团体
	 */
	public static List<TeamUserInfo> getAtivityRoomTeamList(String json) {
		List<TeamUserInfo> list = new ArrayList<TeamUserInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					if (null != item) {
						TeamUserInfo info = new TeamUserInfo();
						info.setTeamId(item.optString("TUserId"));
						info.setTeamName(item.optString("teamUserName"));
						list.add(info);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 主界面banner
	 */
	public static List<BannerInfo> getBannerInfoList(String json) {
		List<BannerInfo> list = new ArrayList<BannerInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				BannerInfo info = new BannerInfo();
				JSONObject item = array.optJSONObject(i);
				info.setAdvertConnectUrl(item.optString("advertConnectUrlId"));
				info.setAdvertId(item.optString("advertId"));
				info.setAdvertPicUrl(item.optString("advertPicUrl"));
				info.setAdvertPosSort(item.optString("advertPosSort"));
				info.setAdverType(item.optInt("adverType"));
				info.setAdvertTitle(item.optString("advertTitle"));
				info.setAdvertContent(item.optString("advertContent"));
				list.add(info);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 日历一张广告
	 */
	public static List<BannerInfo> getCalenderBannerInfo(String json) {
		List<BannerInfo> list = new ArrayList<BannerInfo>();
		BannerInfo info = new BannerInfo();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONObject jo = object.optJSONObject("data");
			info.setAdvertName(jo.optString("advertName"));
			info.setAdvUrl(jo.optString("advUrl"));
			info.setAdvImgUrl(jo.optString("advImgUrl"));
			info.setAdvLink(jo.optInt("advLink"));
			list.add(info);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的消息
	 */
	public static List<MessageInfo> getMessageList(String json) {
		List<MessageInfo> list = new ArrayList<MessageInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				MessageInfo info = new MessageInfo();
				JSONObject item = array.optJSONObject(i);
				info.setMessageContent(item.optString("messageContent"));
				info.setMessageType(item.optString("messageType"));
				info.setUserMessageId(item.optString("userMessageId"));
				list.add(info);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的场馆
	 */
	public static List<MyVenueInfo> getMyVenueList(String json) {
		List<MyVenueInfo> list = new ArrayList<MyVenueInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyVenueInfo info = new MyVenueInfo();
					JSONObject item = array.optJSONObject(i);
					info.setCommentCount(item.optString("commentCount"));
					info.setCurDate(TextUtil.TimeFormat(item.getLong("curDate")));
					info.setOpenPeriod(item.optString("openPeriod"));
					if (item.optString("roomFee").equals("0")
							|| item.optString("roomFee").equals("")) {
						info.setRoomFee("免费");
					} else {
						info.setRoomFee(item.optString("roomFee"));
					}
					info.setRoomId(item.optString("roomId"));
					info.setRoomOrderCreateTime(TextUtil.TimeFormat(item
							.getLong("roomOrderCreateTime")));
					info.setRoomOrderNo(item.optString("roomOrderNo"));
					info.setRoomRicUrl(item.optString("roomRicUrl"));
					info.setRoomsCount(item.optString("roomsCount"));
					info.setRoomName(item.optString("roomName"));
					info.setRoomOrderId(item.optString("roomOrderId"));
					info.setValidCode(item.optString("validCode"));
					info.setVenueAddress(item.optString("venueAddress"));
					info.setVenueName(item.optString("venueName"));
					info.setTuserTeamName(item.optString("tuserTeamName"));
					info.setRoomQrcodeUrl(item.optString("roomQrcodeUrl"));
					list.add(info);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的场馆，以往预约
	 */
	public static List<MyVenueInfo> getVenuePastList(String json) {
		List<MyVenueInfo> list = new ArrayList<MyVenueInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					MyVenueInfo info = new MyVenueInfo();
					info.setRoomId(item.optString("roomId"));
					info.setVenueName(item.optString("venueName"));
					info.setVenueAddress(item.optString("venueAddress"));
					info.setRoomOrderNo(item.optString("roomOrderNo"));
					info.setRoomOrderId(item.optString("roomOrderId"));
					info.setRoomOrderCreateTime(item
							.optString("roomOrderCreateTime"));
					list.add(info);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 活动室订单详情解析
	 */
	public static void getRoomBookInfo(String json) {
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				JSONObject item = array.optJSONObject(0);
				if (item != null) {
					RoomBookInfo.orderNo = item.optString("orderNo");
					RoomBookInfo.bookId = item.optString("bookId");
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 版本更新检测
	 */
	public static APPVersionInfo getVersionInfo(String json) {
		APPVersionInfo mAPPVersionInfo = new APPVersionInfo();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.optJSONObject(i);
				if (item != null) {
					mAPPVersionInfo.setExternalVnumber(item
							.optString("externalVnumber"));
					mAPPVersionInfo.setServiceVersionNumber(item
							.optInt("buildNumber"));
					mAPPVersionInfo.setUpdateDescr(item
							.optString("updateDescr"));
					mAPPVersionInfo.setUpdateUrl(item.optString("updateUrl"));
					mAPPVersionInfo.setVersionCreateTime(TextUtil
							.TimeFormat(item.optLong("versionCreateTime")));
					mAPPVersionInfo.setVersionCreateUser(item
							.optString("versionCreateUser"));
					mAPPVersionInfo.setVersionUpdateTime(TextUtil
							.TimeFormat(item.optLong("versionUpdateTime")));
					mAPPVersionInfo.setVersionUpdateUser(item
							.optString("versionCreateUser"));
					if (item.optString("versionUpdateStatus").equals("1")) {
						mAPPVersionInfo.setIsForcedUpdate(true);
					} else {
						mAPPVersionInfo.setIsForcedUpdate(false);
					}

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return mAPPVersionInfo;
	}

	/**
	 * 新的在线选座解析
	 */
	public static List<CH_seatInfo> getNewOnlineSeatInfoList(String json) {
		List<CH_seatInfo> list = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				JSONObject data = array.optJSONObject(0);
				if (data != null) {
					NewOnlinSeatActivity.ticketCount = data
							.optInt("ticketCount");
					JSONArray seatListarray = data.optJSONArray("seatList");
					if (seatListarray != null) {
						list = new ArrayList<CH_seatInfo>();
						for (int i = 0; i < seatListarray.length(); i++) {
							CH_seatInfo info = new CH_seatInfo();
							JSONObject item = seatListarray.optJSONObject(i);
							info.setColumn(item.optInt("seatColumn"));
							info.setRaw(item.optInt("seatRow"));
							info.setStatus(item.optInt("seatStatus"));
							info.setShow_column(item.optInt("seatVal", 0));
							list.add(info);
						}
					}
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 筛选条件标签 type 1表示场馆，2表示团体
	 */
	public static WindowInfo getWindowList(String json, int type) {
		WindowInfo info = new WindowInfo();
		try {
			JSONObject object = new JSONObject(json);
			// data 是区域
			JSONArray data = object.optJSONArray("data");
			List<SearchInfo> list = new ArrayList<SearchInfo>();
			if (data != null) {
				for (int i = 0; i < data.length(); i++) {
					JSONObject item = data.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					if (item.optString("areaCode") != ""
							&& item.optString("areaCode") != null) {
						searchInfo.setTagId(item.optString("areaCode").split(
								":")[0]);
						searchInfo.setTagName(item.optString("areaCode").split(
								":")[1]);
						list.add(searchInfo);
					}

				}
				// if (list.size() < 4) {
				// int size = 4 - list.size() % 4;
				// for (int i = 0; i < size; i++) {
				// list.add(new SearchInfo("", ""));
				// }
				// }
			}
			// data2 是热门活动
			JSONArray data2 = object.optJSONArray("data2");
			List<SearchInfo> list2 = new ArrayList<SearchInfo>();
			if (data2 != null) {
				for (int i = 0; i < data2.length(); i++) {
					JSONObject item = data2.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));
					searchInfo.setTagName(item.optString("hotKeywords"));
					list2.add(searchInfo);
				}
				// if (list2.size() < 4) {
				// int size = 4 - list2.size() % 4;
				// for (int i = 0; i < size; i++) {
				// list2.add(new SearchInfo("", ""));
				// }
				// }
			}
			JSONArray data3 = object.optJSONArray("data3");
			List<SearchInfo> list3 = new ArrayList<SearchInfo>();
			// SearchInfo search = new SearchInfo();
			// search.setTagId("");
			// search.setTagName("上海市");
			// search.setSeleced(true);
			// list3.add(search);
			if (data3 != null) {
				for (int i = 0; i < data3.length(); i++) {
					JSONObject item = data3.optJSONObject(i);
					SearchInfo adressInfo = new SearchInfo();
					adressInfo.setTagId(TextUtil.getAddresText(
							item.optString("areaCode"), 0));
					adressInfo.setTagName(TextUtil.getAddresText(
							item.optString("areaCode"), 1));
					list3.add(adressInfo);
				}
			}
			// 位置
			// if (type == 1) {// 场馆
			// info.setTypeList(list);
			// info.setCrowdList(list2);
			// info.setAdressList(list3);
			// } else if (type == 2) {// 团体

			// data1 是热门分类
			JSONArray data1 = object.optJSONArray("data1");
			List<SearchInfo> list1 = new ArrayList<SearchInfo>();
			if (data1 != null) {
				for (int i = 0; i < data1.length(); i++) {
					JSONObject item = data1.optJSONObject(i);
					SearchInfo searchInfo = new SearchInfo();
					searchInfo.setTagId(item.optString("tagId"));
					searchInfo.setTagName(item.optString("tagName"));
					list1.add(searchInfo);
				}
				// if (list1.size() < 4) {
				// int size = 4 - list1.size() % 4;
				// for (int i = 0; i < size; i++) {
				// list1.add(new SearchInfo("", ""));
				// }
				// }
				// }

			}
			info.setNatureList(list); // 热门区域地区
			info.setAdressList(list1); // 热门分类
			info.setCrowdList(list2); // 热门活动
			info.setLocationList(list3);
			// else if (type == 3) {
			// JSONArray data1 = object.optJSONArray("data1");
			// List<SearchInfo> list1 = new ArrayList<SearchInfo>();
			// if (data1 != null) {
			// for (int i = 0; i < data1.length(); i++) {
			// JSONObject item = data1.optJSONObject(i);
			// SearchInfo searchInfo = new SearchInfo();
			// searchInfo.setTagId(item.optString("tagId"));
			// searchInfo.setTagName(item.optString("tagName"));
			// list1.add(searchInfo);
			// }
			// if (list1.size() < 4) {
			// int size = 4 - list1.size() % 4;
			// for (int i = 0; i < size; i++) {
			// list1.add(new SearchInfo("", ""));
			// }
			// }
			// }
			// info.setSystemList(list);
			// info.setTimeList(list1);
			// info.setTypeList(list2);
			// info.setLocationList(list3);
			// }

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 展馆列表解析
	 */
	public static List<SpaceInfo> getSpaceListInfo(String json) {
		List<SpaceInfo> list = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			count = object.optString("pageTotal");
			JSONArray array = object.optJSONArray("data");
			// "venueAddress": "山海关路344号",
			// "venueIconUrl":
			// "http://img1.ctwenhuayun.cn/admin/47/201507/Img/Img41fca131be0e4c5195ec28c20bd3641c.jpg",
			// "venueIsReserve": 1,
			// "venueId": "da68ba6f0df24970b9d06bf75ad019fe",
			// "activityName": "",
			// "venueName": "上海自然博物馆新馆"

			if (array != null) {
				list = new ArrayList<SpaceInfo>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.optJSONObject(i);
					SpaceInfo vt = new SpaceInfo();
					vt.setVenueAddress(data.optString("venueAddress"));
					vt.setVenueIconUrl(data.optString("venueIconUrl"));
					vt.setDistance(data.optString("distance", "0"));
					vt.setVenueLat(data.optString("venueLat"));
					vt.setVenueLon(data.optString("venueLon"));
					vt.setVenueId(data.optString("venueId"));
					vt.setVenueName(data.optString("venueName"));
					vt.setVenuePersonName(data.optString("remarkName"));
					vt.setVenueComment(data.optString("commentRemark"));
					vt.setVenueHasBus(data.optString("venueHasBus"));
					vt.setVenueHasMetro(data.optString("venueHasMetro"));
					vt.setActivitySubject(data.optString("activityName"));
					// float venueRating = Float.parseFloat(data
					// .optString("venueStars"));
					// vt.setVenueRating(venueRating);
					if (data.optInt("venueIsCollect") == 1) {
						vt.setVenueIsCollect(true);
					} else {
						vt.setVenueIsCollect(false);
					}
					vt.setCollectNum(data.optInt("collectNum"));
					if (data.optString("venueIsReserve").equals("2")) {
						vt.setVenueIsReserve(true);
					} else {
						vt.setVenueIsReserve(false);
					}
					vt.setRemarkUserImgUrl(data.optString("remarkUserImgUrl"));
					list.add(vt);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 展馆列表解析
	 */
	public static List<VenueDetailInfor> getVenueListInfoList(String json) {
		List<VenueDetailInfor> list = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			count = object.optString("pageTotal");
			JSONArray array = object.optJSONArray("data");
			// "venueAddress": "山海关路344号",
			// "venueIconUrl":
			// "http://img1.ctwenhuayun.cn/admin/47/201507/Img/Img41fca131be0e4c5195ec28c20bd3641c.jpg",
			// "venueIsReserve": 1,
			// "venueId": "da68ba6f0df24970b9d06bf75ad019fe",
			// "activityName": "",
			// "venueName": "上海自然博物馆新馆"

			if (array != null) {
				list = new ArrayList<VenueDetailInfor>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.optJSONObject(i);
					VenueDetailInfor vt = new VenueDetailInfor();
					vt.setVenueAddress(data.optString("venueAddress"));
					vt.setVenueIconUrl(data.optString("venueIconUrl"));
					vt.setDistance(data.optString("distance", "0"));
					vt.setVenueLat(data.optString("venueLat"));
					vt.setVenueLon(data.optString("venueLon"));
					vt.setVenueId(data.optString("venueId"));
					vt.setVenueName(data.optString("venueName"));
					vt.setVenuePersonName(data.optString("remarkName"));
					vt.setVenueComment(data.optString("commentRemark"));
					vt.setVenueHasBus(data.optString("venueHasBus"));
					vt.setVenueHasMetro(data.optString("venueHasMetro"));
					vt.setActivitySubject(data.optString("activityName"));
					// float venueRating = Float.parseFloat(data
					// .optString("venueStars"));
					// vt.setVenueRating(venueRating);
					if (data.optInt("venueIsCollect") == 1) {
						vt.setVenueIsCollect(true);
					} else {
						vt.setVenueIsCollect(false);
					}
					vt.setCollectNum(data.optInt("collectNum"));
					if (data.optString("venueIsReserve").equals("2")) {
						vt.setVenueIsReserve(true);
					} else {
						vt.setVenueIsReserve(false);
					}
					vt.setRemarkUserImgUrl(data.optString("remarkUserImgUrl"));
					list.add(vt);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 展馆列表解析
	 */
	public static List<VenueDetailInfor> getVenueListInfoListwff(String json) {
		List<VenueDetailInfor> list = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			// "venueAddress": "山海关路344号",
			// "venueIconUrl":
			// "http://img1.ctwenhuayun.cn/admin/47/201507/Img/Img41fca131be0e4c5195ec28c20bd3641c.jpg",
			// "venueIsReserve": 1,
			// "venueId": "da68ba6f0df24970b9d06bf75ad019fe",
			// "activityName": "",
			// "venueName": "上海自然博物馆新馆"

			if (array != null) {
				list = new ArrayList<VenueDetailInfor>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.optJSONObject(i);
					VenueDetailInfor vt = new VenueDetailInfor();
					vt.setVenueAddress(data.optString("venueAddress"));
					vt.setVenueIconUrl(data.optString("venueIconUrl"));
					vt.setVenueLon(data.optString("venueLon"));
					vt.setVenueId(data.optString("venueId"));
					vt.setVenueName(data.optString("venueName"));
					vt.setVenuePersonName(data.optString("remarkName"));
					vt.setVenueComment(data.optString("commentRemark"));
					vt.setVenueHasBus(data.optString("venueHasBus"));
					vt.setVenueHasMetro(data.optString("venueHasMetro"));
					float venueRating = Float.parseFloat(data
							.optString("venueStars"));
					vt.setVenueRating(venueRating);
					if (data.optInt("venueIsCollect") == 1) {
						vt.setVenueIsCollect(true);
					} else {
						vt.setVenueIsCollect(false);
					}
					vt.setCollectNum(data.optInt("collectNum"));
					if (data.optString("venueIsReserve").equals("2")) {
						vt.setVenueIsReserve(true);
					} else {
						vt.setVenueIsReserve(false);
					}
					vt.setRemarkUserImgUrl(data.optString("remarkUserImgUrl"));
					list.add(vt);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 团体列表解析
	 */
	public static List<GroupDeatilInfo> getGroupListInfoList(String json) {
		List<GroupDeatilInfo> list = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			count = object.optString("pageTotal");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				list = new ArrayList<GroupDeatilInfo>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.optJSONObject(i);
					GroupDeatilInfo gdi = new GroupDeatilInfo();
					gdi.setGroupId(data.optString("tuserId"));
					gdi.setGroupName(data.optString("tuserName"));
					gdi.setGroupImgUrl(data.optString("tuserPicture"));
					gdi.setCollectNum(data.optInt("collectNum"));
					gdi.setApplyCheckState(data.optInt("applyCheckState"));
					gdi.setApplyId(data.optString("applyId"));
					gdi.setGroupMaxPople(data.optString("tuserLimit"));
					gdi.setCheckCount(data.optString("checkCount"));
					list.add(gdi);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 团体详情解析
	 */
	public static GroupDeatilInfo getGroupDetailInfoList(String json) {
		GroupDeatilInfo groupdetail = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				groupdetail = new GroupDeatilInfo();
				JSONObject data = array.optJSONObject(0);
				groupdetail.setGroupId(data.optString("tuserId"));
				groupdetail.setGroupName(data.optString("tuserName"));
				groupdetail.setGroupImgUrl(data.optString("tuserPicture"));
				groupdetail.setCollectNum(data.optInt("collectNum"));
				groupdetail.setGroupArea(data.optString("tuserArea"));
				groupdetail.setGroupDetail(data.optString("tuserTeamRemark"));
				groupdetail.setGroupType(data.optString("dictName"));
				groupdetail.setGroupMaxPople(data.optString("tuserLimit"));
				groupdetail.setGroupAdmin(data.optString("userNickName"));
				if (data.optInt("applyJoinCount") == 0) {
					groupdetail.setIsUserJion(false);
				} else {
					groupdetail.setIsUserJion(true);
				}
				if (data.optInt("teamUserIsCollect") == 1) {
					groupdetail.setIsCollect(true);
				} else {
					groupdetail.setIsCollect(false);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return groupdetail;
	}

	/**
	 * 获取团体收藏列表
	 */
	public static List<GroupDeatilInfo> getCollectGroupList(String json) {
		List<GroupDeatilInfo> list = new ArrayList<GroupDeatilInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.optJSONObject(i);
				GroupDeatilInfo info = new GroupDeatilInfo();
				info.setGroupId(item.optString("tuserId"));
				info.setGroupArea(item.optString("tuserAddress"));
				info.setGroupName(item.optString("tuserName"));
				list.add(info);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 团体管理
	 */
	public static List<MyGroupPeopleInfor> getGroupManage(String json) {
		List<MyGroupPeopleInfor> list = new ArrayList<MyGroupPeopleInfor>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray array = object.optJSONArray("data");
			status = object.optInt("status");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyGroupPeopleInfor info = new MyGroupPeopleInfor();
					JSONObject item = array.optJSONObject(i);
					info.setPeopleId(item.optString("applyId"));
					info.setPeopleName(item.optString("userNickName"));
					info.setPeopleimgUrl(item.optString("userPicture"));
					info.setPeoplePhone(item.getString("userMobileNo"));
					info.setApplyIsState(item.optString("applyIsState"));
					list.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 团体消息
	 */
	public static List<MyGroupPeopleInfor> getGroupMessage(String json) {
		List<MyGroupPeopleInfor> list = new ArrayList<MyGroupPeopleInfor>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					MyGroupPeopleInfor info = new MyGroupPeopleInfor();
					JSONObject item = array.optJSONObject(i);
					info.setPeopleId(item.optString("applyId"));
					info.setPeopleApplyInfo(item.optString("applyReason"));
					info.setPeopleimgUrl(item.optString("userHeadImgUrl"));
					info.setPeopleName(item.optString("userNickName"));
					info.setPeopleTime(TextUtil.TimeFormat(item
							.optLong("applyTime")));
					info.setPeoplePhone(item.optString("userMobileNo"));
					info.setTuserLimit(item.optInt("tuserLimit"));
					list.add(info);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 非遗列表解析
	 */
	public static List<NotInvoluntaryInfo> getNotInvoluntaryInfoListInfo(
			String json) {
		List<NotInvoluntaryInfo> list = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			count = object.optString("pageTotal");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				list = new ArrayList<NotInvoluntaryInfo>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.optJSONObject(i);
					NotInvoluntaryInfo gdi = new NotInvoluntaryInfo();
					gdi.setAcultureImgUrl(data.optString("cultureImgUrl"));
					gdi.setCollectNum(data.optString("collectNum"));
					gdi.setCultureArea(data.optString("cultureArea"));
					gdi.setCultureDes(data.optString("cultureDes"));
					gdi.setCultureName(data.optString("cultureName"));
					gdi.setDictName(data.optString("dictName"));
					gdi.setCultureId(data.optString("cultureId"));
					if (data.optInt("cultuleIsCollect") == 1) {
						gdi.setCultuleIsCollect(true);
					} else {
						gdi.setCultuleIsCollect(false);
					}
					list.add(gdi);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 非遗详情解析
	 */
	public static NotInvoluntaryInfo getNotInvoluntaryInfoInfo(String json) {
		NotInvoluntaryInfo detail = null;
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			count = object.optString("pageTotal");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				detail = new NotInvoluntaryInfo();
				JSONObject data = array.optJSONObject(0);
				detail.setAcultureImgUrl(data.optString("cultureImgUrl"));
				detail.setCollectNum(data.optString("collectNum"));
				detail.setCultureArea(data.optString("cultureArea"));
				detail.setCultureDes(data.optString("cultureDes"));
				detail.setCultureName(data.optString("cultureName"));
				detail.setDictName(data.optString("dictName"));
				detail.setCultureId(data.optString("cultureId"));
				if (data.optInt("cultuleIsCollect") == 1) {
					detail.setCultuleIsCollect(true);
				} else {
					detail.setCultuleIsCollect(false);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return detail;
	}

	public static void getIndexTags(String json,
			List<IndexTagsInfo> activityTagsList,
			List<IndexTagsInfo> venueTagsList) {
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data1");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject data = array.optJSONObject(i);
					IndexTagsInfo ITI = new IndexTagsInfo();
					ITI.setTagId(data.optString("tagId"));
					ITI.setTagImageUrl(data.optString("tagImageUrl"));
					ITI.setTagName(data.optString("tagName"));
					activityTagsList.add(ITI);
				}
			}
			JSONArray array2 = object.optJSONArray("data2");
			if (array2 != null) {
				for (int i = 0; i < array2.length(); i++) {
					JSONObject data = array2.optJSONObject(i);
					IndexTagsInfo ITI = new IndexTagsInfo();
					ITI.setTagId(data.optString("tagId"));
					ITI.setTagImageUrl(data.optString("tagImageUrl"));
					ITI.setTagName(data.optString("tagName"));
					venueTagsList.add(ITI);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取意见反馈的TYPE
	 */
	public static List<Map<String, String>> getFeedBackTypeList(String json) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					if (null != item) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("item", item.optString("tagName"));
						map.put("id", item.optString("tagId"));
						list.add(map);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// 3.1数据

	/**
	 * 获取随便看看
	 */
	public static List<LookInfo> getLookList(String json) {
		List<LookInfo> list = new ArrayList<LookInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					LookInfo info = new LookInfo();
					info.setActivityNums(item.optInt("activityNums"));
					info.setTagId(item.optString("tagId"));
					info.setTagName(item.optString("tagName"));
					list.add(info);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 首页列表
	 * 
	 * @throws JSONException
	 */
	public static Map<String, List<EventInfo>> getIndexList(String json)
			throws JSONException {
		Map<String, List<EventInfo>> mMap = new HashMap<String, List<EventInfo>>();
		List<EventInfo> list = new ArrayList<EventInfo>();
		JSONObject jSon = new JSONObject(json);
		status = jSon.optInt("status");
		JSONArray array = jSon.optJSONArray("data");
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			EventInfo eventInfo = new EventInfo();
			eventInfo.setDistance(object.optString("distance"));
			eventInfo.setEventAddress(object.optString("activityAddress"));
			eventInfo.setEventIconUrl(object.optString("activityIconUrl"));
			eventInfo.setEventId(object.optString("activityId"));
			eventInfo.setEventIsCollect(object.optString("activityIsCollect"));
			eventInfo.setEventPrice(object.optString("activityPrice"));
			eventInfo.setEventName(object.optString("activityName"));
			eventInfo.setEventLat(object.optString("activityLat"));
			eventInfo.setEventLon(object.optString("activityLon"));
			eventInfo.setActivityType(object.optString("activityType"));
			eventInfo.setActivityStartTime(object
					.optString("activityStartTime"));
			eventInfo.setActivityAbleCount(object
					.optString("activityAbleCount"));
			eventInfo.setEventEndTime(object.optString("activityEndTime"));
			eventInfo.setActivityJoinMethod(object
					.optString("activityJoinMethod"));
			eventInfo.setActivityIsReservation(object
					.optString("activityIsReservation"));
			eventInfo.setActivityRecommend(object
					.optString("activityRecommend"));
			list.add(eventInfo);
		}
		JSONArray array1 = jSon.optJSONArray("data1");
		List<EventInfo> list1 = new ArrayList<EventInfo>();
		for (int i = 0; i < array1.length(); i++) {
			JSONObject object = array1.getJSONObject(i);
			EventInfo eventInfo = new EventInfo();
			eventInfo.setDistance(object.optString("distance"));
			eventInfo.setEventAddress(object.optString("activityAddress"));
			eventInfo.setEventIconUrl(object.optString("activityIconUrl"));
			eventInfo.setEventId(object.optString("activityId"));
			eventInfo.setEventIsCollect(object.optString("activityIsCollect"));
			eventInfo.setEventPrice(object.optString("activityPrice"));
			eventInfo.setEventName(object.optString("activityName"));
			eventInfo.setEventLat(object.optString("activityLat"));
			eventInfo.setEventLon(object.optString("activityLon"));
			eventInfo.setActivityType(object.optString("activityType"));
			eventInfo.setActivityStartTime(object
					.optString("activityStartTime"));
			eventInfo.setActivityAbleCount(object
					.optString("activityAbleCount"));
			eventInfo.setEventEndTime(object.optString("activityEndTime"));
			eventInfo.setActivityJoinMethod(object
					.optString("activityJoinMethod"));
			eventInfo.setActivityIsReservation(object
					.optString("activityIsReservation"));
			eventInfo.setActivityRecommend(object
					.optString("activityRecommend"));
			list1.add(eventInfo);
		}
		JSONArray array2 = jSon.optJSONArray("data2");
		List<EventInfo> list2 = new ArrayList<EventInfo>();
		for (int i = 0; i < array2.length(); i++) {
			JSONObject object = array2.getJSONObject(i);
			EventInfo eventInfo = new EventInfo();
			eventInfo.setDistance(object.optString("distance"));
			eventInfo.setEventAddress(object.optString("activityAddress"));
			eventInfo.setEventIconUrl(object.optString("activityIconUrl"));
			eventInfo.setEventId(object.optString("activityId"));
			eventInfo.setEventIsCollect(object.optString("activityIsCollect"));
			eventInfo.setEventPrice(object.optString("activityPrice"));
			eventInfo.setEventName(object.optString("activityName"));
			eventInfo.setEventLat(object.optString("activityLat"));
			eventInfo.setEventLon(object.optString("activityLon"));
			eventInfo.setActivityType(object.optString("activityType"));
			eventInfo.setActivityStartTime(object
					.optString("activityStartTime"));
			eventInfo.setActivityAbleCount(object
					.optString("activityAbleCount"));
			eventInfo.setEventEndTime(object.optString("activityEndTime"));
			eventInfo.setActivityJoinMethod(object
					.optString("activityJoinMethod"));
			eventInfo.setActivityIsReservation(object
					.optString("activityIsReservation"));
			eventInfo.setActivityRecommend(object
					.optString("activityRecommend"));
			list2.add(eventInfo);
		}
		mMap.put("data", list);
		mMap.put("data1", list1);
		mMap.put("data2", list2);
		return mMap;
	}

	/**
	 * 获取加入用户列表
	 */
	public static List<UserPersionSInfo> getJoinUserList(String json) {
		List<UserPersionSInfo> list = new ArrayList<UserPersionSInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			UserPersionSInfo.totalNum = object.optString("pageTotal");
			if (null != array) {
				int length;
				if (array.length() >= 10) {
					length = 10;
				} else {
					length = array.length();
				}
				for (int i = 0; i < length; i++) {
					JSONObject item = array.optJSONObject(i);
					UserPersionSInfo info = new UserPersionSInfo();
					info.setBithryDay(TextUtil.getDateBriahty(item
							.optString("userBirth")));
					info.setHeadUrl(item.optString("userHeadImgUrl"));
					info.setName(item.optString("userName"));

					if (item.optInt("userSex") == 1) {
						info.setSex("男");
					} else {
						info.setSex("女");
					}
					list.add(info);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取随机推送的三个活动
	 */
	public static List<ActivityRandInfo> getActivityRandList(String json) {
		List<ActivityRandInfo> list = new ArrayList<ActivityRandInfo>();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (null != array) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					if (null != item) {
						ActivityRandInfo info = new ActivityRandInfo();
						info.setActivityIconUrl(item
								.optString("activityIconUrl"));
						info.setActivityId(item.optString("activityId"));
						info.setActivityName(item.optString("activityName"));
						list.add(info);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取电子票id
	 */
	public static String getElectronicTicket(String json) {
		String electronicTicket = "";
		try {
			JSONObject object = new JSONObject(json);
			electronicTicket = object.optString("data");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return electronicTicket;
	}

	/**
	 * 获取电子票信息
	 */
	public static MyActivityBookInfo getElectronicTicketInfo(String json) {
		MyActivityBookInfo info = new MyActivityBookInfo();
		try {
			JSONObject object = new JSONObject(json);
			status = object.optInt("status");
			JSONArray array = object.optJSONArray("data");
			if (array != null) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.optJSONObject(i);
					info.setActivityQrcodeUrl(obj
							.optString("activityQrcodeUrl"));
					info.setActivityId(obj.optString("activityId"));
					info.setActivityEventDateTime(obj
							.optString("activityEventDateTime"));
					info.setOrderVotes(obj.optString("orderVotes"));
					info.setActivityAddress(obj.optString("activityAddress"));
					info.setActivityName(obj.optString("activityName"));
					info.setActivitySeats(obj.optString("activitySeats"));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 我的活动 标签
	 */
	public static List<UserBehaviorInfo> getTypeDataList(String json) {
		List<UserBehaviorInfo> mList = null;
		try {
			JSONObject object = new JSONObject(json);
			// 主题
			JSONArray mood = object.optJSONArray("data");
			mList = new ArrayList<UserBehaviorInfo>();
			if (mood != null) {
				for (int i = 0; i < mood.length(); i++) {
					JSONObject item = mood.optJSONObject(i);
					UserBehaviorInfo info = new UserBehaviorInfo();
					info.setTagId(item.optString("tagId"));
					info.setTagName(item.optString("tagName"));
					info.setTagImageUrl(item.optString("tagImageUrl"));
					info.setActivityTheme(item.optString("activityTheme", ""));
					int status = item.optInt("status");
					if (status == 1) {
						info.setSelect(true);
					} else if (status == 2) {
						info.setSelect(false);
					}
					mList.add(info);
				}
			}
			// 类型
			JSONArray type = object.optJSONArray("data1");
			if (type != null) {
				for (int i = 0; i < type.length(); i++) {
					JSONObject item = type.optJSONObject(i);
					UserBehaviorInfo info = new UserBehaviorInfo();
					info.setTagId(item.optString("tagId"));
					info.setTagName(item.optString("tagName"));
					info.setTagImageUrl(item.optString("tagImageUrl"));
					info.setActivityType(item.optString("activityType", ""));
					int status = item.optInt("status");
					if (status == 1) {
						info.setSelect(true);
					} else if (status == 2) {
						info.setSelect(false);
					}
					mList.add(info);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mList;
	}

	/**
	 * 第三方微信登录信息解析
	 */
	public static WeiXinInfo getWeiXinInfo(String json) {
		WeiXinInfo info = new WeiXinInfo();
		try {
			JSONObject objectdata = new JSONObject(json);
			info.setOpenid(objectdata.optString("openid"));
			info.setUnionid(objectdata.optString("unionid"));
			info.setExpiresIn(objectdata.optString("expiresIn"));
			info.setIcon(objectdata.optString("icon"));
			info.setNickname(objectdata.optString("nickname"));
			info.setToken(objectdata.optString("token"));
			info.setWeibo(objectdata.optString("weibo"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * 我的收藏场馆
	 * 
	 * @param json
	 * @return
	 */
	public static List<MyCollectInfo> getCollectListVenue(String json) {
		List<MyCollectInfo> list = new ArrayList<MyCollectInfo>();
		try {
			JSONObject objectdata = new JSONObject(json);
			JSONArray activityList = objectdata.optJSONArray("data");
			if (activityList != null) {
				for (int i = 0; i < activityList.length(); i++) {
					JSONObject object = activityList.optJSONObject(i);
					MyCollectInfo info = new MyCollectInfo();
					info.setCollectType(0);
					info.setActivityAddress(object.optString("venueAddress"));
					info.setActivityEndTime(object.optString("activityEndTime"));
					info.setActivityIconUrl(object.optString("venueIconUrl"));
					info.setActivityId(object.optString("venueId"));
					info.setActivityName(object.optString("venueName"));
					info.setActivityStartTime(object
							.optString("activityStartTime"));
					list.add(info);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 我的收藏
	 * 
	 * @param json
	 * @return
	 */
	public static List<MyCollectInfo> getCollectList(String json) {
		List<MyCollectInfo> list = new ArrayList<MyCollectInfo>();
		try {
			JSONObject objectdata = new JSONObject(json);
			JSONArray venueList = objectdata.optJSONArray("data1");
			if (venueList != null) {
				for (int i = 0; i < venueList.length(); i++) {
					JSONObject object = venueList.optJSONObject(i);
					MyCollectInfo info = new MyCollectInfo();
					info.setCollectType(1);
					info.setVenueAddress(object.optString("venueAddress"));
					info.setVenueIconUrl(object.optString("venueIconUrl"));
					info.setVenueId(object.optString("venueId"));
					info.setVenueName(object.optString("venueName"));
					list.add(info);
				}
			}
			JSONArray activityList = objectdata.optJSONArray("data");
			if (activityList != null) {
				for (int i = 0; i < activityList.length(); i++) {
					JSONObject object = activityList.optJSONObject(i);
					MyCollectInfo info = new MyCollectInfo();
					info.setCollectType(0);
					info.setActivityAddress(object.optString("activitySite"));
					info.setActivityEndTime(object.optString("activityEndTime"));
					info.setActivityIconUrl(object.optString("activityIconUrl"));
					info.setActivityId(object.optString("activityId"));
					info.setActivityName(object.optString("activityName"));
					info.setActivityStartTime(object
							.optString("activityStartTime"));
					info.setVenueName(object.optString("venueName"));
					info.setVenueAddress(object.optString("venueAddress"));
					info.setActivityAddress(object.optString("activityAddress"));
					list.add(info);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public static List<UserVoteInfo> getUserVoteList(String json) {
		List<UserVoteInfo> list = new ArrayList<UserVoteInfo>();
		try {
			JSONObject objectdata = new JSONObject(json);
			JSONArray voteList = objectdata.optJSONArray("data");
			if (voteList != null) {
				for (int i = 0; i < voteList.length(); i++) {
					JSONObject object = voteList.optJSONObject(i);
					UserVoteInfo info = new UserVoteInfo();
					info.setVoteContent(object.optString("voteContent"));
					info.setVoteCount(object.optInt("voteCount") + "");
					info.setVoteCoverImgUrl(object.optString("voteCoverImgUrl"));
					info.setVoteAddress(object.optString("voteAddress"));
					info.setVoteNum(objectdata.optInt("data1"));
					list.add(info);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	public static List<NewInfo> getNewList(String json) {
		List<NewInfo> list = new ArrayList<NewInfo>();
		try {
			JSONObject objectdata = new JSONObject(json);
			JSONArray newList = objectdata.optJSONArray("data");
			try {
				if (newList != null) {
					for (int i = 0; i < newList.length(); i++) {
						JSONObject object = newList.optJSONObject(i);
						NewInfo info = new NewInfo();
						info.setNewId(object.optString("newId"));
						info.setNewReportTime(object.optString("newReportTime"));
						info.setNewsDesc(object.optString("newsDesc"));
						info.setNewsImgUrl(object.optString("newsImgUrl"));
						info.setNewsReportUser(object
								.optString("newsReportUser"));
						info.setNewsTitle(object.optString("newsTitle"));
						info.setNewsVideoUrl(object.optString("newsVideoUrl"));
						info.setNewType(object.optString("newType"));
						list.add(info);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

	public static int getCount(String json) {
		int count = 0;
		try {
			JSONObject data = new JSONObject(json);
			count = data.optInt("data");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}

	public static List<VoteInfo> getVoteList(String json) {
		List<VoteInfo> list = new ArrayList<VoteInfo>();
		try {
			JSONObject objectdata = new JSONObject(json);
			JSONArray voteList = objectdata.optJSONArray("data");
			if (voteList != null) {
				for (int i = 0; i < voteList.length(); i++) {
					JSONObject object = voteList.optJSONObject(i);
					VoteInfo voteInfo = new VoteInfo();
					voteInfo.setVoteAddress(object.optString("voteAddress"));
					voteInfo.setVoteCoverImgUrl(object
							.optString("voteCoverImgUrl"));
					voteInfo.setVoteTitel(object.optString("voteTitel"));
					list.add(voteInfo);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
}
