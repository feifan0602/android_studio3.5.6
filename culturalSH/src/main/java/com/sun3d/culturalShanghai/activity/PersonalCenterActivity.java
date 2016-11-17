package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.PersonalRelevanceAdapter;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.object.PersonalInfo;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.view.BadgeView;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.umeng.analytics.MobclickAgent;

public class PersonalCenterActivity extends Activity implements
		OnClickListener, OnItemClickListener {
	private String TAG = "PersonalCenterActivity";
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

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		windowWidth = getWindowManager().getDefaultDisplay().getWidth();
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
		setData();
	}

	/**
	 * 初始化
	 */
	@SuppressLint("NewApi")
	private void initView() {
		mLinearLayout = (LinearLayout) findViewById(R.id.main);
		findViewById(R.id.personal_title_left).setOnClickListener(this);
		findViewById(R.id.title_info).setOnClickListener(this);
		titleInfo = (TextView) findViewById(R.id.title_number);
		mHeadIcon = (ImageView) findViewById(R.id.personal_head);
		mName = (TextView) findViewById(R.id.personal_name);
		mAdmin = (TextView) findViewById(R.id.personal_admin);
		personalContent = (ListView) findViewById(R.id.personal_content);

	}

	/**
	 * 设置标签View
	 * 
	 * @param bv
	 */
	@SuppressLint("NewApi")
	private void setBadgeViewStyle(BadgeView bv) {
		bv.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		int size = (int) mContext.getResources().getDimension(
				R.dimen.my_messge_size);
		bv.setBadgeMargin(0, 3);
		bv.setWidth(size);
		bv.setHeight(size);
		if (Build.VERSION.SDK_INT < 16) {
			bv.setBackgroundDrawable(mContext.getResources().getDrawable(
					R.drawable.shape_org_round));
		} else {
			bv.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_org_round));
		}
		bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		// findViewById(R.id.personal_my_event).setOnClickListener(this);
		List<PersonalInfo> mList = new ArrayList<PersonalInfo>();
		// 图标
		// mList.add(new PersonalInfo("我 的 喜 欢",
		// R.drawable.select_personal_like, -1)); // 我喜欢
		mList.add(new PersonalInfo("我 的 订 单",
				R.drawable.select_personal_indent, -1,"")); // 我的订单
		mList.add(new PersonalInfo("我 的 收 藏",
				R.drawable.select_personal_collect, -1,"")); // 我的收藏
		// mList.add(new PersonalInfo("我 的 消 息",
		// R.drawable.select_personal_message, 99)); // 我的消息
		// mList.add(new PersonalInfo("系 统 设 置",
		// R.drawable.select_personal_system, -1)); // 系统设置
		mList.add(new PersonalInfo("个 人 设 置",
				R.drawable.select_personal_single, -1,"")); // 个人设置
		mList.add(new PersonalInfo("帮 助 与 反 馈",
				R.drawable.select_personal_system, -1,"")); // 意见反馈
		mList.add(new PersonalInfo("关 于 文 化 云",
				R.drawable.select_personal_idea, -1,""));

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 此处相当于布局文件中的Android:layout_gravity属性
		lp.gravity = Gravity.CENTER;
		int num = MyApplication.getInstance().pageTotal;
		titleInfo.setText(num + "");
		if (num >= 99) {
			titleInfo.setText(99 + "");
		} else if (num == 0) {
			titleInfo.setVisibility(View.INVISIBLE);
			findViewById(R.id.title_number_img).setLayoutParams(lp);
		}

		if (windowWidth == 0)
			windowWidth = getWindowManager().getDefaultDisplay().getWidth();
		PersonalRelevanceAdapter pra = new PersonalRelevanceAdapter(this, mList);
		personalContent.setAdapter(pra);
		personalContent.setOnItemClickListener(this);
	}

	/**
	 * 设置数据
	 */
	private void setData() {
		mUserInfor = MyApplication.loginUserInfor;

		String mUrl = mUserInfor.getUserHeadImgUrl();
		mHeadIcon.setTag(mUrl == null ? "" : mUrl);
		mHeadIcon.setOnClickListener(this);

		if (mUserInfor.getUserNickName() != null) {

			mName.setText(mUserInfor.getUserNickName());
		}

		if (MyApplication.loginUserInfor.getUserType().equals("2")) {
			mAdmin.setText("团体管理员");
		} else {
			mAdmin.setText(" ");
		}
		MyApplication
				.getInstance()
				.getImageLoader()
				.displayImage(
						mUserInfor.getUserHeadImgUrl(),
						mHeadIcon,
						Options.getRoundOptions(R.drawable.sh_user_header_icon,
								10));
		// 获取用户头像
		if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							mUserInfor.getUserHeadImgUrl(),
							mHeadIcon,
							Options.getRoundOptions(
									R.drawable.sh_user_header_icon, 10));
		} else {
			MyApplication
					.getInstance()
					.getImageLoader()
					.displayImage(
							mUserInfor.getUserHeadImgUrl(),
							mHeadIcon,
							Options.getRoundOptions(
									R.drawable.sh_user_header_icon, 10));

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.PERSONAL_RESULT_USER_CODE:
			if (MyApplication.loginUserInfor == null
					|| !"".equals(MyApplication.loginUserInfor.getUserSex())) {
				break;
			}
			if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								mUserInfor.getUserHeadImgUrl(),
								mHeadIcon,
								Options.getRoundOptions(
										R.drawable.sh_user_header_icon, 10));
			} else {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								mUserInfor.getUserHeadImgUrl(),
								mHeadIcon,
								Options.getRoundOptions(
										R.drawable.sh_user_header_icon, 10));

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
		case R.id.personal_title_left:
			finish();
			break;
		case R.id.personal_head:// 用户中心
			if (MyApplication.loginUserInfor.getUserHeadImgUrl().length() <= 0) {
				ToastUtil.showToast("默认头像无法放大显示！");
				return;
			}
			intent = new Intent();
			intent.setClass(this, ImageOriginalActivity.class);
			intent.putExtra("select_imgs",
					MyApplication.loginUserInfor.getUserHeadImgUrl());
			intent.putExtra("id", 0);
			startActivity(intent);
			break;
		case R.id.title_info:// 我的信息
			intent = new Intent(mContext, MyMessageActivity.class);
			startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
		if (mHeadIcon != null) {
			if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								MyApplication.loginUserInfor
										.getUserHeadImgUrl(),
								mHeadIcon,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_man, 10));
			} else {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								MyApplication.loginUserInfor
										.getUserHeadImgUrl(),
								mHeadIcon,
								Options.getRoundOptions(
										R.drawable.sh_user_sex_woman, 10));

			}
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
		Intent intent = null;
		switch (arg2) {

		case 0:// 我的订单
			intent = new Intent(mContext, MyOrderActivity.class);
			intent.putExtra("type", "activity");
			startActivity(intent);
			break;
		case 1:// 我的收藏
			intent = new Intent(mContext, MyCollectActivity.class);
			startActivity(intent);
			break;
		// case 2:// 我的消息
		// intent = new Intent(mContext, MyMessageActivity.class);
		// startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
		// break;
		// case 3:// 系统设置
		// intent = new Intent(mContext, SettingActivity.class);
		// startActivity(intent);
		// break;
		case 2:// 个人设置
			intent = new Intent(mContext, UserCenterActivity.class);
			startActivityForResult(intent, AppConfigUtil.USER_REQUEST_CODE);
			break;
		case 3:// 反馈
			intent = new Intent(mContext, FeedbackActivity.class);
			startActivity(intent);
			break;
		case 4:// 关于文化云
			intent = new Intent(mContext, RelevantWHYInfoActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
