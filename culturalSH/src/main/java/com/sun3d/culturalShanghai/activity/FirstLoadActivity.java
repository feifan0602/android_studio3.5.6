package com.sun3d.culturalShanghai.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ErrorStatusUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.FirstLoadAdapter;
import com.sun3d.culturalShanghai.animation.FixedSpeedScroller;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

public class FirstLoadActivity extends Activity implements
		OnPageChangeListener, OnClickListener {
	private ViewPager mViewPager;
	private List<View> mList;
	private List<ImageView> dotList;
	private GridView mAgeGridView;
	private List<UserBehaviorInfo> ageList;
	private FirstLoadAdapter mAgeAdapter;
	private GridView mTypeGridView;
	private List<UserBehaviorInfo> typeList;
	private List<UserBehaviorInfo> selectTypeList;
	private FirstLoadAdapter mTypeAdapter;
	private RelativeLayout mLayout;
	private LoadingDialog mLoadingDialog;
	private LinearLayout dotLayout;
	private ImageView mIvManSelect;
	private ImageView mIvWoManSelect;
	private int mAge = -1;
	private int mSex = 0;
	private TextView mTvMan, mTvWoman;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_load);
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
		onLogin();
		getTypeData();
	}

	/**
	 * 用户登录
	 */
	private void onLogin() {
		if (!MyApplication.UserIsLogin) {
			Intent intent = new Intent(this, UserDialogActivity.class);
			intent.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN);
			FastBlur.getScreen(this);
			startActivityForResult(intent, AppConfigUtil.LOADING_LOGIN_BACK);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.LOADING_LOGIN_BACK:
			if (MyApplication.loginUserInfor.getUserIsLogin().equals("1")) {
				Intent intent = new Intent(FirstLoadActivity.this,
						MainFragmentActivity.class);
				startActivity(intent);
				finish();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化数据
	 */
	private void initView() {
		mLoadingDialog = new LoadingDialog(this);
		mLayout = (RelativeLayout) findViewById(R.id.first_layout);
		mViewPager = (ViewPager) findViewById(R.id.load_viewpager);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		mList = new ArrayList<View>();
		dotList = new ArrayList<ImageView>();
		addViewList();
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(new MyPagerAdapter());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		try {// 设置时间
			Field field = null;
			field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mViewPager.getContext(), new AccelerateInterpolator());
			field.set(mViewPager, scroller);
			scroller.setmDuration(500);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 添加内容数据
	 */
	private void addViewList() {

		// View mSexView = View.inflate(this, R.layout.first_load_sex, null);
		View mAgeView = View.inflate(this, R.layout.first_load_age, null);
		View mTypeView = View.inflate(this, R.layout.first_load_type, null);

		// mList.add(mSexView);
		mList.add(mAgeView);
		mList.add(mTypeView);
		dotList.add((ImageView) findViewById(R.id.v_dot1));
		dotList.add((ImageView) findViewById(R.id.v_dot2));
		// dotList.add((ImageView) findViewById(R.id.v_dot3));
		mTvMan = (TextView) mAgeView.findViewById(R.id.first_sex_man);
		mIvManSelect = (ImageView) mAgeView
				.findViewById(R.id.first_sex_man_select);
		mIvWoManSelect = (ImageView) mAgeView
				.findViewById(R.id.first_sex_woman_select);
		mTvWoman = (TextView) mAgeView.findViewById(R.id.first_sex_woman);
		Button mStart = (Button) mTypeView.findViewById(R.id.first_start);
		mTvWoman.setOnClickListener(this);
		mTvMan.setOnClickListener(this);
		mStart.setOnClickListener(this);
		LinearLayout mSex = (LinearLayout) mAgeView
				.findViewById(R.id.first_sex);
		mAgeGridView = (GridView) mAgeView
				.findViewById(R.id.first_age_gridview);
		mTypeGridView = (GridView) mTypeView
				.findViewById(R.id.first_type_gridview);
		mAgeGridView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mViewPager.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mTypeGridView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mViewPager.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mSex.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mViewPager.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mStart.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mViewPager.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mTvWoman.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mViewPager.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		mTvMan.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mViewPager.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		ageList = new ArrayList<UserBehaviorInfo>();
		mAgeAdapter = new FirstLoadAdapter(this, ageList, true);
		mAgeGridView.setAdapter(mAgeAdapter);
		typeList = new ArrayList<UserBehaviorInfo>();
		selectTypeList = new ArrayList<UserBehaviorInfo>();
		mTypeAdapter = new FirstLoadAdapter(this, typeList, false);
		mTypeGridView.setAdapter(mTypeAdapter);
		addData();
		mAgeGridView.setOnItemClickListener(new OnItemClickListener() {
			int position = -1;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (position >= 0) {
					mAgeAdapter.isSelect(position, false);
				}
				mAgeAdapter.isSelect(arg2, true);
				position = arg2;
				mAge = ageList.get(arg2).getAge();
				onSendSelect(mSex, ageList.get(arg2).getAge());
				// updateAge(ageList.get(arg2).getAge());
				// mViewPager.setCurrentItem(2);
				// dotLayout.setVisibility(View.GONE);
			}
		});
		mTypeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UserBehaviorInfo info = typeList.get(arg2);
				if (info.isSelect()) {
					info.setSelect(false);
					selectTypeList.remove(info);
					arg1.findViewById(R.id.first_select).setVisibility(
							View.GONE);
				} else {
					arg1.findViewById(R.id.first_select).setVisibility(
							View.VISIBLE);
					selectTypeList.add(info);
					info.setSelect(true);
				}
			}
		});
	}

	/**
	 * 添加数据
	 */
	private void addData() {
		ageList.add(new UserBehaviorInfo(50, R.drawable.sh_first_age_five_bg));
		ageList.add(new UserBehaviorInfo(60, R.drawable.sh_first_age_six_bg));
		ageList.add(new UserBehaviorInfo(70, R.drawable.sh_first_age_seven_bg));
		ageList.add(new UserBehaviorInfo(80, R.drawable.sh_first_age_eight_bg));
		ageList.add(new UserBehaviorInfo(90, R.drawable.sh_first_age_nine_bg));
		ageList.add(new UserBehaviorInfo(00, R.drawable.sh_first_age_zero_bg));
		mAgeAdapter.notifyDataSetChanged();
	}

	/**
	 * 添加用户喜欢的类型
	 */
	private void addUserTag() {
		String tagId = "";
		for (int i = 0; i < selectTypeList.size(); i++) {
			tagId = tagId + selectTypeList.get(i).getTagId() + ",";
		}
		tagId = tagId.substring(0, tagId.length() - 1);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("userSelectTag", tagId);
		mLoadingDialog.startDialog("请稍等");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ADDTYPETAG, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						MyApplication.getInstance().setSelectTypeList(
								selectTypeList);
						SharedPreManager.saveMain(
								AppConfigUtil.PRE_FILE_NAME_KEY, true);
						startActivity(new Intent(FirstLoadActivity.this,
								MainFragmentActivity.class));
						finish();
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoadingDialog.cancelDialog();
	}

	/**
	 * 获取类型数据
	 */
	private void getTypeData() {
		Map<String, String> params = new HashMap<String, String>();
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							List<UserBehaviorInfo> mList = JsonUtil
									.getTypeDataList(resultStr);
							if (mList != null) {
								typeList.addAll(mList);
								mTypeAdapter.notifyDataSetChanged();
							}
						}
					}
				});
	}

	/**
	 * 选择年龄
	 */
	private void updateAge(int age) {
		String ageString = "";
		switch (age) {
		case 50:
			ageString = "1950-01-01";
			break;
		case 60:
			ageString = "1960-01-01";
			break;
		case 70:
			ageString = "1970-01-01";
			break;
		case 80:
			ageString = "1980-01-01";
			break;
		case 90:
			ageString = "1990-01-01";
			break;
		case 00:
			ageString = "2000-01-01";
			break;
		default:
			ageString = "1980-01-01";
			break;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("userBirthStr", ageString);
		params.put("userSex", "0");
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_setUserInfo,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
								.getJsonStatus(resultStr)) {
							// ToastUtil.showToast("选择成功");
							dotLayout.setVisibility(View.GONE);
							mViewPager.setCurrentItem(1);
						} else {
							ErrorStatusUtil.seachServerStatus(JsonUtil.status,
									JsonUtil.JsonMSG);
						}
					}
				});
	}

	// 提交选择
	private void onSendSelect(int type, final int age) {
		if (type == 0 || age < 0) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("userSex", String.valueOf(type));
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.User_setUserInfo,
				params, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub

						// TODO Auto-generated method stub
						if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
								.getJsonStatus(resultStr)) {
							// ToastUtil.showToast("选择成功");
							updateAge(age);
						} else {
							ErrorStatusUtil.seachServerStatus(JsonUtil.status,
									JsonUtil.JsonMSG);
						}

					}
				});
	}

	/**
	 * 选择性别
	 * 
	 * @param type
	 */
	private void updateSex(int type) {
		if (!MyApplication.getInstance().UserIsLogin) {
			ToastUtil.showToast("请先登录您的帐号");
			onLogin();
			return;
		}
		if (type == 1) {
			mIvManSelect.setVisibility(View.VISIBLE);
			mIvWoManSelect.setVisibility(View.GONE);
		} else {
			mIvManSelect.setVisibility(View.GONE);
			mIvWoManSelect.setVisibility(View.VISIBLE);
		}
		// mViewPager.setCurrentItem(1);
		mSex = type;
		onSendSelect(type, mAge);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.first_sex_man:
			updateSex(1);
			break;
		case R.id.first_sex_woman:
			updateSex(2);
			break;
		case R.id.first_start:
			onStartActivity();
			break;

		default:
			break;
		}
	}

	@Override
	public void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
//		int mWidth = (MyApplication.getWindowWidth() - 100) / 4;
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		params.height = mWidth;
//		params.weight = mWidth;
//		if (mTvMan != null & mTvWoman != null) {
//			mTvMan.setLayoutParams(params);
//			mTvWoman.setLayoutParams(params);
//		}
	}

	/**
	 * 进入主界面
	 */
	private void onStartActivity() {
		if (typeList == null || typeList.size() == 0) {
			getTypeData();
			return;
		}
		if (selectTypeList.size() > 0) {
			addUserTag();
		} else {
			ToastUtil.showToast("请选择分类标签");
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(mList.get(position));
			return mList.get(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		// switch (arg0) {
		// case 0:// 滑动结束，即切换完毕或者加载完毕
		// // 当前为最后一张，此时从右向左滑，则切换到第一张
		// if (mViewPager.getCurrentItem() == mViewPager.getAdapter().getCount()
		// - 1) {
		// if (isAutoPlay) {
		// onStartActivity();
		// }
		// isAutoPlay = true;
		// } else {
		// isAutoPlay = false;
		// }
		// break;
		// }
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		for (int i = 0; i < dotList.size(); i++) {
			if (arg0 == i) {
				dotList.get(i)
						.setImageResource(R.drawable.shape_main_red_round);
			} else {
				dotList.get(i).setImageResource(
						R.drawable.shape_main_white_round);

			}
		}
	}

}
