package com.sun3d.culturalShanghai.http;

/**
 * HTTPurl请求列表
 *
 * @author yangyoutao
 */
public class HttpUrlList {
    /**
     * 正式环境IP:http://www.wenhuayun.cn
     */

    // private static  String IP = "http://192.168.41.170:8080/";// 3.3
    // weiwei
    // private static  String IP = "http://192.168.5.116:8080/";// 3.3 开发环境
    // private static  String IP = "http://www.why.cn";// 3.2 开发环境
    // private static  String IP = "http://192.168.41.147:8080";// 3.2 开发环境
    // public static  String IP = "http://pre.wenhuayun.cn";// 3.5 测试环境
    // public static  String IP = "http://eme.wenhuayun.cn";// 3.5 测试环境
    // public static  String IP = "http://192.168.5.51";// 3.5 测试环境
    // public static  String IP = "http://gen.wenhuayun.cn";// 3.5 测试环境
    // private static  String IP = "http://192.168.5.116:8080";
    // private static  String IP = "http://wx.wenhuayun.cn";// 3.5 测试环境


    //public static  String IP = "http://eme.wenhuayun.cn";// 正式环境
    //public static  String TEST_IP = "http://eme.whycm.sh.cn:10019";

//    public static String IP = "http://m.wenhuayun.cn";
//    public static String TEST_IP = "http://m.whycm.sh.cn:10019";

    public static String IP = "http://m.wenhuayun.cn";
    public static String TEST_IP = "http://m.whycm.sh.cn:10019";


    // public static  String IP = "http://eme.wenhuayun.cn";// 正式环境
    // public static  String IP="http://eme.whycm.sh.cn:10019";

    // private static  String IP = "http://192.168.41.198:8080";//
    // 洋哥的IP测试环境
    // private static  String IP = "http://demo.wenhuayun.cn"; // 准正式环境
    // public static  String ipString = "测试环境";
    // public static  String ipString = "准生产环境";
    // public static  String ipString = "生产环境";
    public static String HTTP_LON = "Lon";// 经度
    public static String HTTP_LAT = "Lat";// 纬度
    public static String HTTP_TOKEN_KEY = "token";// 纬度
    public static String HTTP_TOKEN_VALUE = "anonymous";// 默认，匿名用户.
    // 公共参数
    public static String HTTP_USER_ID = "userId";// 用户id
    public static String HTTP_PAGE_NUM = "pageNum";
    public static String HTTP_PAGE_INDEX = "pageIndex";
    public static int HTTP_NUM_10 = 10;
    public static int HTTP_NUM = 20;
    public static int HTTP_OK = 200;
    public static String NEW_IP = "";
    public static String NEW_TEST_IP = "";



    public static class WebUrl {
        public static String NEWURL = IP + "/frontNews/detail.do";
        public static String VOTEURL = IP + "/frontVote/detail.do";
        public static String WEB_AOUNT = IP + "/serviceProtocol.html";
        // public static  String WEB_HELP = IP
        // + "/STATIC/html/mobileHelp.html";
        public static String WEB_HELP = IP + "/wechat/help.do?type=app";
        public static String WEB_URL = "wenhua";
    }

    /**
     * 用户url
     */
    public static class UserUrl {

        public static String User_thirdlogin = IP
                + "/login/appOpenUser.do";
        public static String User_fasrlogin = IP + "/login/doLogin.do";
        public static String User_UploadUserIcon = IP
                + "/appUser/uploadAppFiles.do";
        public static String User_setUserInfo = IP
                + "/appUser/editTerminalUser.do";
        public static String User_getphonecodeInfo = IP
                + "/appUser/appSendCode.do";
        public static String User_bindphoneInfo = IP
                + "/appUser/appValidateCode.do";
        public static String User_setuserpasswordInfo = IP
                + "/appUser/appValidatePwd.do";
        public static String USER_SETEMAILINFO = IP
                + "/appUser/editTerminalUserByMail.do";
        public static String GET_USERINFO = IP
                + "/appUser/queryTerminalUserById.do";
        public static String FORGETPW_GETCODE = IP
                + "/appUser/editTerminalUserByMobile.do";
        public static String FINDPW_FIXPW = IP
                + "/appUser/editTerminalUserByPwd.do";
        public static String REGISTER_PHONE_CODE = IP
                + "/login/userCode.do";
        public static String REGISTER_PHONE = "userMobileNo";
        public static String REGISTER_URL = IP + "/login/doRegister.do";
        public static String REGISTER_NAME = "userName";
        public static String REGISTER_SEX = "userSex";
        public static String REGISTER_BIRTH = "userBirthStr";
        public static String REGISTER_PW = "userPwd";
        public static String REGISTER_CODE = "code";
        public static String USER_UPLOADGROUPPIC = IP
                + "/appUser/uploadAppFiles.do";
        public static String USER_THIRD_BIND = IP
                + "/appUser/BindingAccount.do";
    }

    /**
     * 评论
     */
    public static class Comment {
        /**
         * 某一类评论
         */
        public static String User_CommentList = IP
                + "/appActivity/activityAppComment.do";

        public static String User_CommitComment = IP
                + "/appActivity/addComment.do";
    }

    /**
     * 活动
     */
    public static class EventUrl {
        // 主界面20条活动数据
        public static String HTTP_MAIN_LIST_URL = IP
                + "/appActivity/activityAppIndex.do";// 主界面20条活动数据
        /**
         * 3.5 活动详情接口
         */
        public static String WFF_HTTP_EVENT_URL = IP
                + "/appActivity/cmsActivityAppDetail.do";// 活动详情
        // public static  String HTTP_EVENT_URL = IP
        // + "/appActivity/activityAppDetail.do";// 活动详情
        public static String MAIN_EVENT_SIX_URL = IP
                + "/appActivity/appActivityTag.do";// 活动六个标签
        public static String ACTIVITY_ONLINE_SEAT = IP
                + "/appActivity/appActivityBook.do";// 活动在线选座
        public static String ACTIVITY_ID = "activityId";// 活动Id
        public static String LOOK_URL = IP
                + "/appActivity/appActivityTagList.do";// 活动类型
        public static String ADDTYPETAG = IP + "/appTag/addUserTags.do";// 活动类型
        public static String WANTTO_URL = IP
                + "/appActivity/appAddActivityUserWantgo.do";// 我想参加
        public static String WFF_APPADDVENUEUSERWANTGO = IP
                + "/appVenue/appAddVenueUserWantgo.do";// 点赞场馆
        public static String WFF_APPDELETEVENUEUSERWANTGO = IP
                + "/appVenue/appDeleteVenueUserWantgo.do";// 取消点赞场馆
        public static String WANTNOT_URL = IP
                + "/appActivity/deleteActivityUserWantgo.do";// 我不想参加
        public static String WANTTO_USERS_URL = IP
                + "/appActivity/appActivityUserWantgoList.do";// 获取报名用户
        public static String WFF_APPVUNUEUSERWANTGOLIST = IP
                + "/appVenue/appVenueUserWantgoList.do";// 获取报名用户

        /**
         * 获取活动浏览数
         */
        public static String WFF_APPCMSACTIVITYBROWSECOUNT = IP
                + "/appActivity/appCmsActivityBrowseCount.do";// 获取报名用户
        /**
         * 获取场馆浏览数
         */
        public static String WFF_APPCMSVENUEBROWSECOUNT = IP
                + "/appVenue/appCmsVenueBrowseCount.do";// 获取场馆浏览数

        public static String RANDACTIVITY_URL = IP
                + "/appActivity/appRandActivityList.do";// 随机推送的数据
        public static String RANDAUSERSCAN_URL = IP
                + "/appActivity/addRandActivity.do";// 随机推送的数据
        public static String ELECTRONICTICKET_URL = IP
                + "/appUserActivity/electronicTicket.do";// 生成电子票

        public static String ACTIVITYDETAILVOTE = IP
                + "/userActivityVote/userVote.do";
        public static String ACTIVITYSHOWINDEX = IP
                + "/appMc/mcShowIndex.do";
        public static String ACTIVITYCOUNT = IP
                + "/appActivity/appWillStartActivityCount.do";
        public static String ACTIVITYWILL = IP
                + "/appActivity/appAddWillStart.do";
        public static String ACTIVITYVOTELIST = IP
                + "/userActivityVote/queryActivityVote.do";
    }

    /**
     * 活动条件筛选
     */
    public static class SearchUrl {

        // 3.5活动接口
        /**
         * 活动搜索接口
         */
        public static String HTTP_APPHOTACTIVITY = IP
                + "/appHot/getActivity.do";

        public static String URL_GET_SK_MATCH = TEST_IP
                + "/activity/automatedName";

        /**
         * 根据条件搜索活动
         */
        public static String WFF_APPCMSACTIVITYLISTBYCONDITION = IP
                + "/appActivity/appCmsActivityListByCondition.do";

        public static String HTTP_PUPO_SEARCH_URL = IP
                + "/appTag/appActivityTagByType.do";
        public static String HTTP_SEARCH_URL = IP
                + "/appActivity/appActivityIndex.do";
        // public static  String HTTP_EVENT_LIST_URL = IP
        // + "/appActivity/queryActivityListByCondition.do";
        public static String HTTP_SEARCH_START_TIME = "activityStartTime";
        public static String HTTP_SEARCH_END_TIME = "activityEndTime";
        public static String HTTP_SEARCH_TYPE = "activityType";
        public static String HTTP_SEARCH_MOOD = "activityMood";
        public static String HTTP_SEARCH_CROWD = "activityCrowd";
        public static String HTTP_SEARCH_PRICE = "activityPrice";
        public static String HTTP_SEARCH_KEYWORD = "activityName";
        public static String HTTP_SEARCH_TABTYPE = "appType";
        public static String AREACODE = "activityArea";
        public static String HTTP_TIMETYPE = "timeType";
        public static String HTTP_ACTIVITY_THEME = "activityTheme";

    }

    /**
     * 主界面标签
     */
    public static class Label {
        public static String APP_TYPE = "appType";
        public static String TAG_ID = "tagId";
        public static String DICT_CODE = "dictCode";
        public static String HTTP_INDEXTAGS = IP
                + "/tag/appActivityTags.do";
    }

    /**
     * 场馆
     */
    public static class Venue {
        public static String WFF_CMSVUNUEAPPDETAIL = IP
                + "/appVenue/cmsVenueAppDetail.do";

        public static String VENUE_DETAILURL = IP
                + "/appVenue/venueAppDetail.do";
        public static String CANCEL_URL = IP
                + "/appUserVenue/removeAppRoomOrder.do";
        public static String CANCEL_ROOMORDER_ID = "roomOrderId";
        // public static  String VENUE_LIST = IP +
        // "/venue/appVenueIndex.do";
        public static String VENUE_LIST = IP
                + "/appVenue/venueAppIndex.do";
        /**
         * 根据条件筛选场馆
         */
        public static String WFF_APPCMSVENUELIST = IP
                + "/appVenue/appCmsVenueList.do";
        /**
         * 場館的分類選項
         */
        public static String WFF_APPVENUETAGBYTYPE = IP
                + "/appTag/appVenueTagByType.do";
        /**
         * 区域的选项 venueMood
         */
        public static String WFF_GETALLAREA = IP
                + "/appActivity/getAllArea.do";
        /**
         * 根据不同条件选择场馆
         */
        public static String WFF_APPVENUELIST = IP
                + "/appVenue/appVenueList.do";

        public static String URL_GET_VENUE_NUMOFROOMS = IP
                + "/appVenue/appVenueCountInfo.do";

        public static String VENUE_LIST_SCREEN = IP
                + "/appVenue/appVenueListIndex.do";
        public static String AREACODE = "venueArea";
        public static String APPTYPE = "appType";
        public static String VENUETYPE = "venueType";
        public static String VENUECROWD = "venueCrowd";
        public static String VENUENAME = "venueName";
        public static String VENUEISRESERVE = "venueIsReserve";
    }

    public static class HomeFragment {

        /**
         * 广告的链接
         */
        public static String PAGEADVERTRECOMMEND = TEST_IP
                + "/advertRecommend/pageAdvertRecommend";
        /**
         * 推荐活动接口 3.5.4
         */
        public static String RECOMMENDACTIVITY = TEST_IP
                + "/activity/recommendActivity";

        // public static  String PAGEADVERTRECOMMEND = IP
        // + "/advertRecommend/pageAdvertRecommend";
    }

    public static class SearchList {
        public static String SEARCHACTIVITY = TEST_IP
                + "/activity/searchActivity";
        public static String SEARCHVENUE = TEST_IP + "/venue/searchVenue";
    }

    /**
     * 活动室列表
     */
    public static class ActivityRoomUrl {
        public static String VENUE_ID = "venueId";
        public static String ROOMID = "roomId";
        /**
         * 根据展馆id查询相关活动室信息
         */
        public static String ROOM_LIST_URL = IP
                + "/appVenue/activityAppRoom.do";// venueAppRooms
        public static String ROOMDETAIL_LIST_URL = IP
                + "/appRoom/roomAppDetail.do";
        public static String ROOM_TEAMLIST = IP
                + "/appRoom/roomTeamUser.do";
        public static String ROOM_BOOK = IP + "/appRoom/roomBook.do";

        public static String ROOM_BOOK_TRUE = IP
                + "/appRoom/roomOrderConfirm.do";
    }

    /**
     * 藏品
     */
    public static class Collection {
        public static String COLLECTION_ALL_GETURL = IP
                + "/appAntique/antiqueAppIndex.do";
        public static String COLLECTION_TIME = "dynasty";
        public static String COLLECTION_TYPE = "antique";
        public static String COLLECTION_FILTER_GETURL = IP
                + "/appAntique/screenAppAntiqueDynasty.do";
        public static String COLLECTION_FILTER_GETURL_NAME = IP
                + "/appAntique/screenAppAntiqueTypeName.do";
        public static String COLLECTION_DETAIL_GETURL = IP
                + "/appAntique/antiqueAppDetail.do";
    }

    /**
     * 我的活动
     */
    public static class MyEvent {
        // 3.5接口更新
        /**
         * 这个是横向listview选择后的接口,刷新listview
         */
        public static String WFF_APPTOPACTIVITYLIST = IP
                + "/appActivity/appTopActivityList.do";
        /**
         * app发送用户实名认证验证码
         */
        public static String WFF_SENDAUTHCODE = IP
                + "/appUser/sendAuthCode.do";
        /**
         * 这是首页的广告栏接口
         */
        public static String WFF_APPADVERTRECOMENDLIST = IP
                + "/appActivity/appAdvertRecommendList.do";
        /**
         * 这个是APP首页的活动的接口
         */
        public static String WFF_ACTIVITYRECOMMENTDURL = IP
                + "/appActivity/appRecommendActivity.do";
        /**
         * 这是查询推荐活动的接口 默认是推荐的接口
         */
        public static String WFF_APPRECOMMENDACTIVITYLIST = IP
                + "/appActivity/appRecommendActivityList.do";
        /**
         * 这是查询推荐活动的接口 3.5.2（有筛选条件 的时候选择这个接口）
         */
        public static String WFF_APPFILTERACTIVITYLIST = IP
                + "/appActivity/appFilterActivityList.do";

        /**
         * 这是用户活动订单详情 3.5.2
         */
        public static String WFF_USERACTIVITYORDERDEATIL = IP
                + "/appUserOrder/userActivityOrderDetail.do";
        /**
         * app活动场次列表
         */
        public static String WFF_APPACTIVITYEVENTLIST = IP
                + "/appActivity/appActivityEventList.do";

        /**
         * app转发添加积分接口(3.5.2接口)
         */
        public static String WFF_FORWARDINGINTEGRAL = IP
                + "/appUser/forwardingIntegral.do";

        // public static  String WFF_APPACTIVITYEVENTLIST = IP
        // + "/appActivity/appActivitySpikeList.do";

        /**
         * 这是用户活动室详情 3.5.2
         */
        public static String WFF_USERROOMORDERDEATIL = IP
                + "/appUserOrder/userRoomOrderDetail.do";

        /**
         * 这是根據不同的条件显示活动列表
         */
        public static String WFF_APPCMSACTIVITYLISTBYCONDITION = IP
                + "/appActivity/appCmsActivityListByCondition.do";

        /**
         * 这个是APP附近活动的接口
         */
        public static String WFF_APPNEARACTIVITYLIST = IP
                + "/appActivity/appNearActivityList.do";
        /**
         * 每天的活动数
         */
        public static String WFF_APPEVERYDATEACTIVITYCOUNT = IP
                + "/appActivity/appEveryDateActivityCount.do";
        // http://m.wenhuayun.cn+ "/appActivity/appEveryDateActivityCount.do";

        /**
         * 某天的活动列表
         */
        public static String WFF_ACTIVITYLISTURL_MAP = IP
                + "/appActivity/appEveryDateActivityList.do";
        /**
         * 3.5.2 日历接口改变
         */
        public static String WFF_APPACTIVITYCALENDARLIST = IP
                + "/appActivity/appActivityCalendarList.do";

        // public static  String ACTIVITYLISTURL_MAP = IP
        // + "/appActivity/appActivityIndex.do";
        public static String ACTIVITYLISTURL = IP
                + "/appActivity/appTopActivity.do";

        public static String INDEXLISTURL = IP
                + "/appActivity/appActivityListIndex.do";
        public static String MY_PASTEVENT_URL = IP
                + "/appUserActivity/userAppOldActivity.do";
        public static String MY_NEWEVENT_URL = IP
                + "/appUserActivity/userAppNowActivity.do";
        /**
         * 根据展馆id查询相关活动
         */
        public static String ACTIVITY_LIST_URL = IP
                + "/appVenue/venueAppCmsActivity.do";
        public static String ACTIVITY_RESERVE_URL = IP
                + "/appActivity/appActivityOrder.do";
        public static String CANCEL_EVENT_URL = IP
                + "/appUserActivity/removeAppActivity.do";
        public static String CANCEL_EVENT_ID = "activityOrderId";
        public static String CANCEL_EVENT_SEAT = "orderSeat";
        public static String DELLETE_ACTIVITY_URL = IP
                + "/appUserActivity/deleteAppUserActivityHistory.do";
    }

    /**
     * 主界面banner
     *
     * @author liningkang
     */
    public static class Banner {
        public static String CALENDER_BANNER_URL = IP
                + "/appActivity/queryCalendarAdvert.do";
        public static String MAIN_BANNER_URL = IP
                + "/appActivity/appActivityBanner.do";
        // 这个是引导页
        public static String WFF_GETMOBILEIMAGE = IP
                + "/appUser/getMobileImage.do";
        // 这个是版本号
        public static String WFF_CHECKAPPVERSIONNO = IP
                + "/appUser/checkAppVersionNo.do";

        public static String BANNER_NUM = "banNum";
    }

    public static class ActivityDetail {
        public static String ASSOCIATIONACTIVITY = TEST_IP
                + "/association/associationActivity";
        public static String PATH = TEST_IP + "/staticServer/path";

    }

    /**
     * 收藏
     *
     * @author liningkang
     */
    public static class Collect {
        // 活动Id
        public static String ACTIVITY_ID = "activityId";
        // 添加活动收藏
        public static String ADD_EVENT_URL = IP
                + "/appUserCollect/appCollectActivity.do";
        // 取消活动收藏
        public static String CANCEL_EVENT_URL = IP
                + "/appUserCollect/appDelCollectActivity.do";
        // 我的收藏活动列表
        public static String ACTIVITY_LIST = IP
                + "/appUserCollect/userAppCollectAct.do";
        // app显示用户收藏活动列表
        public static String WFF_USERAPPCOLLECTACT = IP
                + "/appUserCollect/userAppCollectAct.do";
        // app显示用户收藏场馆列表
        public static String WFF_USERAPPCOLLECTVEN = IP
                + "/appUserCollect/userAppCollectVen.do";
        // app显示用户评论活动列表
        public static String WFF_APPACTIVITYCOMMENTLIST = IP
                + "/appActivity/appActivityCommentList.do";
        // app显示用户评论场馆列表
        public static String WFF_APPVENUECOMMENTLIST = IP
                + "/appVenue/appVenueCommentList.do";
        public static String COLLECT_LIST = IP
                + "/appUserCollect/queryAppUserCollect.do";
        // 场馆Id
        public static String VENUE_ID = "venueId";
        // 添加场馆收藏
        public static String ADD_VENUE_URL = IP
                + "/appUserCollect/appCollectVenue.do";
        // 取消场馆收藏
        public static String CANCEL_VENUE_URL = IP
                + "/appUserCollect/appDelCollectVenue.do";
        // 我的收藏场馆列表
        public static String VENUE_LIST_URL = IP
                + "/appUserCollect/userAppCollectVen.do";
        // 添加团体收藏
        public static String ADD_GROUP_URL = IP
                + "/appUserCollect/appCollectTeam.do";
        // 取消团体收藏
        public static String CANCEL_GROUP_URL = IP
                + "/appUserCollect/appDelCollectTeam.do";
        // 我的收藏团体列表
        public static String GROUP_LIST_URL = IP
                + "/appUserCollect/userAppCollectTeam.do";
    }

    /**
     * 我的订单（包括活动与场馆订单）
     */
    public static class MyMessage {
        /**
         * app显示活动及场馆进行中的订单列表
         */
        public static String WFF_USERORDERS = IP
                + "/appUserOrder/appUserOrder.do";
        /**
         * app显示待审核的订单列表
         */
        public static String WFF_APPUSERCHECKORDER = IP
                + "/appUserOrder/appUserCheckOrder.do";

        /**
         * app显示活动及场馆进行中的历史订单列表
         */
        public static String WFF_APPUSERHISTORYORDER = IP
                + "/appUserOrder/appUserHistoryOrder.do";
        public static String ORDER_MESSAGE_URL = IP
                + "/appUserOrder/userOrders.do";

        public static String ORDER_VALIDATE_CODE = "orderValidateCode";
        public static String ORDER_NUMBER = "orderNumber";
        public static String ACTIVITY_NAME = "activityName";
        public static String VENUE_NAME = "venueName";

    }

    /**
     * 我的消息
     */
    public static class Message {
        public static String MY_MESSAGE_URL = IP
                + "/appUserMessage/userAppMessage.do";
        public static String MY_DELETEMESSAGE_URL = IP
                + "/appUserMessage/delAppMessage.do";
    }

    /**
     * 我的積分
     */
    public static class Code {
        public static String MY_CODE_URL = IP
                + "/appUser/appUserIntegralDetail.do";
        public static String MY_MORE_CODE_URL = IP
                + "/appUser/appUserIntegralDetailList.do";
        // /appUserOrder/appUserIntegralDetailList.do
        // /appUserOrder/appUserIntegralDetail.do
    }

    /**
     * 反馈信息
     */
    public static class Feedback {
        public static String FEEDBACK_URL = IP
                + "/appUser/appFeedInformation.do";
        public static String FEEDBACK_TYPE_URL = IP
                + "/appTag/appFeedBackTagByType.do";
        public static String CONTENT = "feedContent";
    }

    /**
     * 我的场馆
     */
    public static class MyVenue {
        public static String NEW_VENUE_URL = IP
                + "/appUserVenue/userAppNowVenue.do";
        public static String PAST_VENUE_URL = IP
                + "/appUserVenue/userAppOldVenue.do";
        public static String DELETE_VENUE_URL = IP
                + "/appUserVenue/deleteAppRoomOrder.do";
    }

    /**
     * 版本更新
     */
    public static class Version {
        public static String APP_VESIONUPDATER_URL = IP
                + "/login/appAndroidVersion.do";
    }

    /**
     * 条件筛选(场馆，团体，非遗)
     */
    public static class Window {
        public static String WFF_VENUE_URL = IP + "/appHot/getVenue.do";
        public static String VENUE_URL = IP
                + "/appTag/appVenueTagByType.do";
        public static String GROUP_URL = IP
                + "/teamUser/appTeamUserTagByType.do";
        public static String NOT_INVOLUANTARY_URL = IP
                + "/culture/appCultureTagByType.do";
    }

    /**
     * 团体
     */
    public static class Group {
        public static String GROUP_ID = "teamUserId";
        public static String GROUP_LIST = IP
                + "/teamUser/teamUserAppIndex.do";
        public static String GROUP_DETAIL = IP
                + "/teamUser/appTeamUserDetail.do";
        public static String GROUP_LIST_SCREEN = IP
                + "/teamUser/appTeamUserListIndex.do";
        public static String TUSERNAME = "tuserName";
        public static String APPTYPE = "appType";// tab
        public static String TUSERCOUNTY = "tuserCounty";// 区域
        public static String TUSERCROWD = "tuserCrowd";// 人群
        public static String TUSERPROPERTY = "tuserProperty";// 属性
        public static String TUSERSITE = "tuserSite";// 地点
        public static String GROUP_ADD = IP
                + "/teamUser/appTeamUserAdd.do";
        public static String GROUP_MYADD = IP
                + "/userTeam/userAppNowTeam.do";
        public static String GROUP_HISADD = IP
                + "/userTeam/userAppHistoryTeam.do";
        // 团体管理
        public static String GROUP_MANAGE = IP
                + "/teamUser/appGroupManager.do";
        public static String TEAMUSERID = "teamUserId";

        // 团体消息
        public static String MESSAGE_URL = IP
                + "/userTeam/userAppGroupAuditing.do";
        public static String GROUP_QUITADD = IP
                + "/userTeam/userAppQuitTeam.do";
        public static String GROUP_MANAGEADD = IP
                + "/userTeam/userAppGroupTeam.do";
        public static String GROUP_TREAMLABEL = IP
                + "/teamUser/appMateTeamUserTag.do";
        // 同意
        public static String MESSAGE_AGREE_URL = IP
                + "/userTeam/userAppApplyJoinTeamPass.do";
        public static String TUSERLIMIT = "tuserLimit";
        public static String APPLYID = "applyId";
        public static String MESSAGE_REPULSE_URL = IP
                + "/userTeam/userAppApplyJoinTeamRefuse.do";
        public static String GROUP_EDITCOMMIT = IP
                + "/userTeam/editTeamUser.do";
    }

    /**
     * 非遗
     */
    public static class NotInvoluntary {
        public static String NOTINVOLUNTARY_LIST_URL = IP
                + "/culture/cultureAppIndex.do";
        public static String NOTINVOLUNTARY_DETAIL_URL = IP
                + "/culture/cultureAppDetail.do";
        public static String NOTINVOLUNTARY_URL = IP
                + "/culture/appCultureListIndex.do";// 筛选列表
        public static String CULTURENAME = "cultureName";// 名称
        public static String APPTYPE = "appType";// tab类型
        public static String CULTUREAREA = "cultureArea";// 区域
        public static String CULTURESYSTEM = "cultureSystem";// 体系
        public static String CULTUREYEARS = "cultureYears";// 年代
        public static String CULTURETYPE = "cultureType";// 类别
        public static String AREACODE = "areaCode";// 地区
    }

    /**
     * 文件相关
     */
    public static class File {
        public static String UPLOADIMG = IP
                + "/appUser/uploadAppFiles.do";
    }
}
