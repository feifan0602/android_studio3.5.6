package com.sun3d.culturalShanghai.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.activity.BannerWebView;
import com.sun3d.culturalShanghai.activity.FeedbackActivity;
import com.sun3d.culturalShanghai.activity.ImageOriginalActivity;
import com.sun3d.culturalShanghai.activity.MainFragmentActivity;
import com.sun3d.culturalShanghai.activity.MyCodeActivity;
import com.sun3d.culturalShanghai.activity.MyCollectActivity;
import com.sun3d.culturalShanghai.activity.MyCommentActivity;
import com.sun3d.culturalShanghai.activity.MyMessageActivity;
import com.sun3d.culturalShanghai.activity.MyOrderActivity;
import com.sun3d.culturalShanghai.activity.MyRoomStutasActivity;
import com.sun3d.culturalShanghai.activity.UserCenterActivity;
import com.sun3d.culturalShanghai.activity.UserDialogActivity;
import com.sun3d.culturalShanghai.adapter.PersonalRelevanceAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.MyUserInfo;
import com.sun3d.culturalShanghai.object.PersonalInfo;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.view.BadgeView;
import com.sun3d.culturalShanghai.view.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun3d.culturalShanghai.http.HttpUrlList.ADDRESS_IP;

public class PersonalCenterFragmet extends Fragment implements OnClickListener,
        OnItemClickListener, LoadingHandler.RefreshListenter {
    private String TAG = "PersonalCenterFragmet";
    private Context mContext;
    private ImageView mHeadIcon;
    private LinearLayout mLinearLayout;
    private TextView mAdmin;
    private TextView titleInfo;// 标题信息数量
    BadgeView MybadgeVenue;
    BadgeView MybadgeMessage;
    private TextView mName;
    private UserInfor mUserInfor;
    private ListView personalContent;
    public final static int UPDATE_NICK_CODE = 102;// 用户中心修改昵称返回
    private int windowWidth = 0;
    private View view;
    private ImageView right_iv;
    private TextView middle_tv, left_tv;
    private ImageView message_iv, setting_iv;
    private CircleImageView mPersonal;
    private ImageView login_iv;
    private TextView top_tv, address_tv;
    private TextView my_order, my_collection, my_comment;
    private List<MyUserInfo> MyUserList;
    private LoadingHandler mLoadingHandler;
    private RelativeLayout loadingLayout;
    private List<PersonalInfo> mList;
    private LinearLayout linear_layout;
    private ImageView mImageView;
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_personal_center,
                null);
        windowWidth = getActivity().getWindowManager().getDefaultDisplay()
                .getWidth();
        mContext = getActivity();
        initView(view);
        // initData(view);
        setData(view);
        mList = new ArrayList<PersonalInfo>();
        if (!MyApplication.UserIsLogin) {
            // 没有登录
            init(view);
        } else {
            mLoadingHandler.startLoading();
            getUserInfo();
        }

        // TODO Auto-generated method stub
        return view;
    }

    private void init(View view2) {
        mList.clear();
        if (MyApplication.OPENORCLOSEBOTTOM) {
            mList.add(new PersonalInfo("我 的 积 分", R.drawable.wff_icon_huodong, -1,
                    "",0)); // 我的订单
            mImageView.setVisibility(View.INVISIBLE);
            linear_layout.setVisibility(View.VISIBLE);
        } else {
//            mList.add(new PersonalInfo()); // 我的积分
            mImageView.setVisibility(View.VISIBLE);
            linear_layout.setVisibility(View.GONE);
        }

        mList.add(new PersonalInfo("我 的 评 论", R.drawable.wff_icon_pinglun, 1,
                "",1));

        mList.add(new PersonalInfo("实 名 认 证", R.drawable.wff_icon_shoucang, -1,
                "",2));

        mList.add(new PersonalInfo("资 质 认 证", R.drawable.wff_icon_pinglun, -1,
                "",3));
        mList.add(new PersonalInfo("我 的 评 论", R.drawable.wff_icon_pinglun, 1,
                "",4));
        mList.add(new PersonalInfo("设 置", R.drawable.wff_icon_help, -1, "",5));
        mList.add(new PersonalInfo("帮 助 与 反 馈", R.drawable.wff_icon_help, -1,
                "",6));
        mList.add(new PersonalInfo("关 于 文 化 云", R.drawable.wff_icon_wenhuayun,
                -1, "",7));
        if (!MyApplication.UserIsLogin) {
            address_tv.setVisibility(View.GONE);
        } else {
            address_tv.setVisibility(View.VISIBLE);
            String province = MyUserList.get(0).getUserProvince().split(",")[1];
            String userArea = MyUserList.get(0).getUserArea().split(",")[1];
            if (MyUserList.get(0).getUserSex() == 2) {
                address_tv.setText(province.substring(0, province.length() - 1)
                        + " " + userArea.substring(0, province.length() - 1)
                        + " " + "女");
            } else {
                address_tv.setText(province.substring(0, province.length() - 1)
                        + " " + userArea.substring(0, province.length() - 1)
                        + " " + "男");
            }

        }

        PersonalRelevanceAdapter pra = new PersonalRelevanceAdapter(
                getActivity(), mList);
        personalContent.setAdapter(pra);
        pra.notifyDataSetChanged();
        personalContent.setOnItemClickListener(this);
    }

    /**
     * 初始化
     */
    @SuppressLint("NewApi")
    private void initView(View view) {
        mImageView= (ImageView) view.findViewById(R.id.back_iv);
        mImageView.setOnClickListener(this);
        linear_layout = (LinearLayout) view.findViewById(R.id.linear_layout);
        loadingLayout = (RelativeLayout) view.findViewById(R.id.loading);
        mLoadingHandler = new LoadingHandler(loadingLayout);
        login_iv = (ImageView) view.findViewById(R.id.login_iv);
        top_tv = (TextView) view.findViewById(R.id.top_tv);
        top_tv.setTypeface(MyApplication.GetTypeFace());
        address_tv = (TextView) view.findViewById(R.id.address_tv);
        address_tv.setTypeface(MyApplication.GetTypeFace());
        mPersonal = (CircleImageView) view.findViewById(R.id.title_left);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.main);
        message_iv = (ImageView) view.findViewById(R.id.message);
        message_iv.setOnClickListener(this);
        setting_iv = (ImageView) view.findViewById(R.id.setting);
        setting_iv.setOnClickListener(this);
        if (!MyApplication.UserIsLogin) {
            message_iv.setVisibility(View.INVISIBLE);
            setting_iv.setVisibility(View.INVISIBLE);
        } else {
            message_iv.setVisibility(View.VISIBLE);
            setting_iv.setVisibility(View.INVISIBLE);
        }
        left_tv = (TextView) view.findViewById(R.id.left_tv);
        middle_tv = (TextView) view.findViewById(R.id.middle_tv);
        right_iv = (ImageView) view.findViewById(R.id.right_iv);
        middle_tv.setText("我的");
        right_iv.setImageResource(R.drawable.sh_icon_my_info);
        left_tv.setText(" ");
        right_iv.setOnClickListener(this);
        login_iv.setOnClickListener(this);
        view.findViewById(R.id.personal_title_left).setOnClickListener(this);
        view.findViewById(R.id.title_info).setOnClickListener(this);
        titleInfo = (TextView) view.findViewById(R.id.title_number);
        mHeadIcon = (ImageView) view.findViewById(R.id.personal_head);
        mName = (TextView) view.findViewById(R.id.personal_name);
        mAdmin = (TextView) view.findViewById(R.id.personal_admin);
        my_order = (TextView) view.findViewById(R.id.my_order);
        my_collection = (TextView) view.findViewById(R.id.my_collection);
        my_comment = (TextView) view.findViewById(R.id.my_comment);

        my_order.setTypeface(MyApplication.GetTypeFace());
        my_collection.setTypeface(MyApplication.GetTypeFace());
        my_comment.setTypeface(MyApplication.GetTypeFace());
        mName.setTypeface(MyApplication.GetTypeFace());
        mAdmin.setTypeface(MyApplication.GetTypeFace());

        my_order.setOnClickListener(this);
        my_collection.setOnClickListener(this);
        my_comment.setOnClickListener(this);
        personalContent = (ListView) view.findViewById(R.id.personal_content);

    }

    /**
     * 数据初始化
     */
    public void initData(View view) {
        // 获取登录时候的数据
        MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
        // findViewById(R.id.personal_my_event).setOnClickListener(this);
        mList.clear();
        if (MyApplication.OPENORCLOSEBOTTOM) {
            mList.add(new PersonalInfo("我 的 积 分", R.drawable.wff_icon_huodong, -1,
                    MyUserList.get(0).getUserIntegral() + " 积分",0)); // 我的积分
            mImageView.setVisibility(View.INVISIBLE);
            linear_layout.setVisibility(View.VISIBLE);
        } else {
//            mList.add(new PersonalInfo()); // 我的积分
            mImageView.setVisibility(View.VISIBLE);
            linear_layout.setVisibility(View.GONE);
        }
        mList.add(new PersonalInfo("我 的 评 论", R.drawable.wff_icon_pinglun, 1,
                "",1));

        if (MyUserList.get(0).getUserType() == 2) {
            mList.add(new PersonalInfo("实 名 认 证", R.drawable.wff_icon_shoucang,
                    -1, "已认证",2));
        } else if (MyUserList.get(0).getUserType() == 3) {
            mList.add(new PersonalInfo("实 名 认 证", R.drawable.wff_icon_shoucang,
                    -1, "待认证",2));
        } else if (MyUserList.get(0).getUserType() == 4) {
            mList.add(new PersonalInfo("实 名 认 证", R.drawable.wff_icon_shoucang,
                    -1, "认证未通过",2));

        } else {
            mList.add(new PersonalInfo("实 名 认 证", R.drawable.wff_icon_shoucang,
                    -1, "未认证",2));
        }

        if (MyUserList.get(0).getTeamUserSize() > 0) {
            mList.add(new PersonalInfo("资 质 认 证", R.drawable.wff_icon_pinglun,
                    -1, "已认证",3));
        } else {
            mList.add(new PersonalInfo("资 质 认 证", R.drawable.wff_icon_pinglun,
                    -1, "未认证",3));
        }
        mList.add(new PersonalInfo("我 的 评 论", R.drawable.wff_icon_pinglun, 1,
                "",4));

        mList.add(new PersonalInfo("设 置", R.drawable.wff_icon_help, -1, "",5)); // 意见反馈
        mList.add(new PersonalInfo("帮 助 与 反 馈", R.drawable.wff_icon_help, -1,
                "",6)); // 意见反馈
        mList.add(new PersonalInfo("关 于 文 化 云", R.drawable.wff_icon_wenhuayun,
                -1, "",7));
        if (!MyApplication.UserIsLogin) {
            address_tv.setVisibility(View.GONE);
        } else {
            address_tv.setVisibility(View.VISIBLE);
            String province = "";
            String userArea = "";
            if (MyUserList.get(0).getUserProvince() != ""
                    && MyUserList.get(0).getUserProvince().length() != 0) {
                province = MyUserList.get(0).getUserProvince().split(",")[1];
            } else {
                province = "上海市";
            }
            if (MyUserList.get(0).getUserArea() != ""
                    && MyUserList.get(0).getUserArea().length() != 0) {
                userArea = MyUserList.get(0).getUserArea().split(",")[1];
            }

            if (MyUserList.get(0).getUserSex() == 2) {
                address_tv.setText(province.replaceAll("市", "") + " "
                        + userArea.replaceAll("区", "") + " " + "女");
            } else {
                address_tv.setText(province.replaceAll("市", "") + " "
                        + userArea.replaceAll("区", "") + " " + "男");
            }

        }
        PersonalRelevanceAdapter pra = new PersonalRelevanceAdapter(
                getActivity(), mList);
        personalContent.setAdapter(pra);
        pra.notifyDataSetChanged();
        personalContent.setOnItemClickListener(this);
        mLoadingHandler.stopLoading();
    }

    /**
     * 设置数据
     */
    private void setData(View view) {
        mUserInfor = MyApplication.loginUserInfor;

        String mUrl = mUserInfor.getUserHeadImgUrl();
        mPersonal.setTag(mUrl == null ? "" : mUrl);
        mPersonal.setOnClickListener(this);

        if (mUserInfor.getUserNickName() != null) {

            mName.setText(mUserInfor.getUserNickName());
        }

        if (MyApplication.loginUserInfor.getUserType().equals("2")) {
            mAdmin.setText("团体管理员");
        } else {
            mAdmin.setText(" ");
        }
        mPersonal.setBackgroundResource(R.color.transparent);
        if (!MyApplication.UserIsLogin) {
            // 没登陆的状态
            top_tv.setVisibility(View.VISIBLE);
            login_iv.setVisibility(View.VISIBLE);
            mPersonal.setVisibility(View.GONE);
            login_iv.setBackgroundResource(R.drawable.wff_icon_login);
        } else {
            login_iv.setVisibility(View.GONE);
            top_tv.setVisibility(View.GONE);
            mPersonal.setVisibility(View.VISIBLE);
            // 获取用户头像
            if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
                MyApplication
                        .getInstance()
                        .getImageLoader()
                        .displayImage(
                                MyApplication.loginUserInfor
                                        .getUserHeadImgUrl(),
                                mPersonal);
            } else {
                MyApplication
                        .getInstance()
                        .getImageLoader()
                        .displayImage(
                                MyApplication.loginUserInfor
                                        .getUserHeadImgUrl(),
                                mPersonal);

            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("ceshi", "回掉了000requestCode===   " + requestCode + "  resultCode==  " + resultCode);
        switch (resultCode) {
            case AppConfigUtil.PERSONAL_RESULT_USER_CODE:
                if (MyApplication.loginUserInfor == null) {
                    break;
                }

                if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
                    MyApplication
                            .getInstance()
                            .getImageLoader()
                            .displayImage(
                                    MyApplication.loginUserInfor
                                            .getUserHeadImgUrl(),
                                    mPersonal);
                } else {
                    MyApplication
                            .getInstance()
                            .getImageLoader()
                            .displayImage(
                                    MyApplication.loginUserInfor
                                            .getUserHeadImgUrl(),
                                    mPersonal);

                }
                break;
            case AppConfigUtil.LOADING_LOGIN_BACK:
                if (MyApplication.loginUserInfor == null) {
                    Log.i("ceshi", "回掉了1111   null  null");
                    break;
                }
                Log.i("ceshi", "回掉了1111   ");
                if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
                    MyApplication
                            .getInstance()
                            .getImageLoader()
                            .displayImage(
                                    MyApplication.loginUserInfor
                                            .getUserHeadImgUrl(),
                                    mPersonal);
                } else {
                    MyApplication
                            .getInstance()
                            .getImageLoader()
                            .displayImage(
                                    MyApplication.loginUserInfor
                                            .getUserHeadImgUrl(),
                                    mPersonal);

                }
                break;
            case AppConfigUtil.PERSONAL_RESULT_VENUE_CODE:
                // MybadgeVenue.setVisibility(View.GONE);
                break;
            case AppConfigUtil.PERSONAL_RESULT_MESSGE_CODE:
                // MybadgeMessage.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_iv:
                MyApplication.change_fragment = 0;
                MainFragmentActivity.getIntance().setTabSelection(0);
                break;
            case R.id.personal_title_left:
                getActivity().finish();
                break;
            case R.id.login_iv:// 用户中心
                // 这是未登陆状态
                if (MyApplication.loginUserInfor.getUserHeadImgUrl().length() <= 0) {
                    if (!MyApplication.UserIsLogin) {
                        intent = new Intent(mContext, UserDialogActivity.class);
                        intent.putExtra(
                                DialogTypeUtil.DialogType,
                                DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                        startActivity(intent);
                    } else {
                        // intent = new Intent(mContext,
                        // PersonalCenterActivity.class);
                    }

                    // if (!MyApplication.UserIsLogin) {
                    // intent = new Intent(mContext, Activity_Login.class);
                    // intent.putExtra(
                    // DialogTypeUtil.DialogType,
                    // DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    // } else {
                    // intent = new Intent(mContext, PersonalCenterActivity.class);
                    // }
                    // startActivity(intent);
                    return;
                } else {
                    intent = new Intent();
                    intent.setClass(getActivity(), ImageOriginalActivity.class);
                    intent.putExtra("select_imgs",
                            MyApplication.loginUserInfor.getUserHeadImgUrl());
                    intent.putExtra("id", 0);
                    startActivity(intent);
                }

                break;
            case R.id.title_info:// 我的信息
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    // intent = new Intent(mContext, MyMessageActivity.class);
                    // startActivityForResult(intent,
                    // AppConfigUtil.USER_REQUEST_CODE);
                    intent = new Intent(mContext, MyRoomStutasActivity.class);
                    startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
                }

                break;
            case R.id.right_iv:

                intent = new Intent(mContext, MyMessageActivity.class);
                startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
                break;
            case R.id.message:
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, MyMessageActivity.class);
                    // intent = new Intent(mContext, MyRoomStutasActivity.class);
                    // intent = new Intent(mContext, TestWebView.class);
                    startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
                }
                break;
            case R.id.setting:

                break;
            case R.id.my_order:
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, MyOrderActivity.class);
                    intent.putExtra("type", "activity");
                    MyApplication.room_activity = 0;
                    startActivity(intent);
                }
                break;
            case R.id.my_collection:
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, MyCollectActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.my_comment:
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, MyCommentActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(mContext);
        if (!MyApplication.UserIsLogin) {
            // 没登陆的状态
            NoLoginClear();
        } else {
            LoginStutas();
        }

        if (mName != null) {
            mName.setText(MyApplication.loginUserInfor.getUserNickName());
        }
        if (mAdmin != null) {
            if (MyApplication.loginUserInfor.getUserType().equals("2")) {
                mAdmin.setText("团体会员");
            } else {
                mAdmin.setText(" ");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        PersonalInfo info= (PersonalInfo) arg0.getItemAtPosition(arg2);
        int i=info.getFlag();
        Intent intent = null;
        switch (i) {

            case 0:// 我的积分
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, MyCodeActivity.class);
                    intent.putExtra("type", "activity");
                    startActivity(intent);
                }
                break;
            case 2:// 实名认证
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, BannerWebView.class);
                    intent.putExtra("url", ADDRESS_IP
                            + "/wechatUser/auth.do?type=app&userId="
                            + MyApplication.loginUserInfor.getUserId());
                    intent.putExtra("auth", "auth");
                    startActivity(intent);
                }

                break;
            case 3: // 资质认证
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, BannerWebView.class);
                    intent.putExtra("url", ADDRESS_IP
                            + "/wechatRoom/authTeamUser.do?type=app&userId="
                            + MyApplication.loginUserInfor.getUserId());
                    intent.putExtra("auth", "authTeamUser");
                    startActivity(intent);
                }

                break;
            // case 2:// 我的消息
            // intent = new Intent(mContext, MyMessageActivity.class);
            // startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
            // break;
            // case 3:// 系统设置
            // intent = new Intent(mContext, SettingActivity.class);
            // startActivity(intent);
            // break;
            // case 2:// 个人设置
            // intent = new Intent(mContext, UserCenterActivity.class);
            // startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
            // break;
            case 5: // 设置
                if (!MyApplication.UserIsLogin) {
                    intent = new Intent(mContext, UserDialogActivity.class);
                    intent.putExtra(
                            DialogTypeUtil.DialogType,
                            DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                    startActivity(intent);
                } else {
                    intent = new Intent(mContext, UserCenterActivity.class);
                    startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
                }
                break;
            case 6: // 反馈
                // if (!MyApplication.UserIsLogin) {
                // intent = new Intent(mContext, UserDialogActivity.class);
                // intent.putExtra(
                // DialogTypeUtil.DialogType,
                // DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                // startActivity(intent);
                // } else {
                intent = new Intent(mContext, FeedbackActivity.class);
                startActivity(intent);
                // }

                break;
            case 7:// 关于文化云
                // if (!MyApplication.UserIsLogin) {
                // intent = new Intent(mContext, UserDialogActivity.class);
                // intent.putExtra(
                // DialogTypeUtil.DialogType,
                // DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
                // startActivity(intent);
                // } else {
                intent = new Intent(mContext, BannerWebView.class);
                intent.putExtra("url", ADDRESS_IP
                        + "/wechatUser/preCulture.do?type=app");
                startActivity(intent);
                // }
                break;
            case 9: // 平台入驻

                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    initData(view);
                    MyApplication.MyloginUserInfor = MyUserList.get(0);
                    break;
                case 2:
                    mLoadingHandler.isNotContent();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void getUserInfo() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(HttpUrlList.HTTP_USER_ID,
                MyApplication.loginUserInfor.getUserId());
        MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.GET_USERINFO,
                params, new HttpRequestCallback() {

                    @Override
                    public void onPostExecute(int statusCode, String resultStr) {
                        // TODO Auto-generated method stub
                        Log.i(TAG, "看看结果==   " + resultStr);
                        if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
                            MyUserList = JsonUtil.getMyUserInfoList(resultStr);
                            if (MyUserList.size() != 0) {
                                handler.sendEmptyMessage(1);
                            } else {
                                handler.sendEmptyMessage(2);
                            }
                        }
                    }
                });
    }

    public void NoLoginClear() {
        login_iv.setVisibility(View.VISIBLE);
        top_tv.setVisibility(View.VISIBLE);
        mPersonal.setVisibility(View.GONE);
        message_iv.setVisibility(View.INVISIBLE);
        setting_iv.setVisibility(View.INVISIBLE);
        address_tv.setVisibility(View.GONE);
        login_iv.setBackgroundResource(R.drawable.wff_icon_login);
        init(view);
    }

    public void LoginStutas() {
        message_iv.setVisibility(View.VISIBLE);
        setting_iv.setVisibility(View.INVISIBLE);
        address_tv.setVisibility(View.VISIBLE);
        login_iv.setVisibility(View.GONE);
        top_tv.setVisibility(View.GONE);
        mPersonal.setVisibility(View.VISIBLE);
        if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
            MyApplication
                    .getInstance()
                    .getImageLoader()
                    .displayImage(
                            MyApplication.loginUserInfor.getUserHeadImgUrl(),
                            mPersonal, Options.getLoginOptions());
        } else {
            MyApplication
                    .getInstance()
                    .getImageLoader()
                    .displayImage(
                            MyApplication.loginUserInfor.getUserHeadImgUrl(),
                            mPersonal, Options.getLoginOptions());

        }
        getUserInfo();
    }

    @Override
    public void onLoadingRefresh() {
        getUserInfo();
    }
}
