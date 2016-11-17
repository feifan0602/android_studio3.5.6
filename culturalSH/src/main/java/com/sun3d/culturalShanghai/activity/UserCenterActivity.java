package com.sun3d.culturalShanghai.activity;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.Options;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.LoadingHandler.RefreshListenter;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.view.CircleImageView;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.umeng.analytics.MobclickAgent;

/**
 * 个人设置里面
 * 
 * @author wenff
 * 
 */
public class UserCenterActivity extends Activity implements OnClickListener,
		RefreshListenter {
	private Context mContext;
	private TextView nickName_tv, sex_tv, email_tv;
	private UserInfor mUserInfor;
	private LoadingDialog mLoadingDialog;
	private final String mPageName = "UserCenterActivity";
	private LoadingHandler mLoadingHandler;
	private TextView userCenterSize;
	private TextView userItemRegion;
	private TextView userItemAccount;
	private SharedPreferences sharedPreferences;

	private File user_cache_path;
	private File image_cache_path = new File(FolderUtil.createImageCacheFile());
	private File cache_path = new File(FolderUtil.createCacheFile());
	private int mTime = 1000;
	private ImageView right_iv, left_iv;
	private TextView middle_tv;
	private CircleImageView mHeadIcon;
	private TextView user_item_right_text;
	private TextView left_tv, left_tv1, left_tv2, left_tv3, left_tv4, left_tv5,
			left_tv6, left_tv7, right_tv0;
	private String TAG="UserCenterActivity";
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(mPageName);
		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		mContext = this;
		sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
		MyApplication.getInstance().addActivitys(this);
		MyApplication.getInstance().setmUserHandler(mHandler);
		mLoadingDialog = new LoadingDialog(mContext);
		initView();
		getData();
	}

	/**
	 * 初始化
	 */
	private void initView() {
		user_cache_path = StorageUtils.getOwnCacheDirectory(mContext,
				FolderUtil.createUserFile());
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.setOnRefreshListenter(this);
		mLoadingHandler.startLoading();
		setTitle();
		right_tv0 = (TextView) findViewById(R.id.right_tv0);
		left_tv = (TextView) findViewById(R.id.left_tv0);
		left_tv1 = (TextView) findViewById(R.id.left_tv1);
		left_tv2 = (TextView) findViewById(R.id.left_tv2);
		left_tv3 = (TextView) findViewById(R.id.left_tv3);
		left_tv4 = (TextView) findViewById(R.id.left_tv4);
		left_tv5 = (TextView) findViewById(R.id.left_tv5);
		left_tv6 = (TextView) findViewById(R.id.left_tv6);
		left_tv7 = (TextView) findViewById(R.id.left_tv7);
		left_tv.setTypeface(MyApplication.GetTypeFace());
		left_tv1.setTypeface(MyApplication.GetTypeFace());
		left_tv2.setTypeface(MyApplication.GetTypeFace());
		left_tv3.setTypeface(MyApplication.GetTypeFace());
		left_tv4.setTypeface(MyApplication.GetTypeFace());
		left_tv5.setTypeface(MyApplication.GetTypeFace());
		left_tv6.setTypeface(MyApplication.GetTypeFace());
		left_tv7.setTypeface(MyApplication.GetTypeFace());
		right_tv0.setTypeface(MyApplication.GetTypeFace());
		userCenterSize = (TextView) findViewById(R.id.user_center_size);
		mHeadIcon = (CircleImageView) findViewById(R.id.user_head_icon);
		nickName_tv = (TextView) findViewById(R.id.user_item_name);
		sex_tv = (TextView) findViewById(R.id.user_item_sex);
		email_tv = (TextView) findViewById(R.id.user_item_bind_email);
		userItemRegion = (TextView) findViewById(R.id.user_item_region);
		userItemAccount = (TextView) findViewById(R.id.user_item_account);
		userCenterSize.setTypeface(MyApplication.GetTypeFace());
		nickName_tv.setTypeface(MyApplication.GetTypeFace());
		sex_tv.setTypeface(MyApplication.GetTypeFace());
		email_tv.setTypeface(MyApplication.GetTypeFace());
		userItemRegion.setTypeface(MyApplication.GetTypeFace());
		userItemAccount.setTypeface(MyApplication.GetTypeFace());

		left_iv = (ImageView) findViewById(R.id.left_iv);
		middle_tv = (TextView) findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		left_iv.setImageResource(R.drawable.sh_icon_title_return);
		middle_tv.setText("个人设置");
		left_iv.setOnClickListener(this);
		findViewById(R.id.user_region).setOnClickListener(this);
		findViewById(R.id.user_bind_email).setOnClickListener(this);
		findViewById(R.id.user_return_login).setOnClickListener(this);
		findViewById(R.id.user_eliminate).setOnClickListener(this);
		findViewById(R.id.user_nick).setOnClickListener(this);
		findViewById(R.id.user_head_icon_layout).setOnClickListener(this);
		findViewById(R.id.user_nick).setOnClickListener(this);
		findViewById(R.id.user_sex).setOnClickListener(this);
		user_item_right_text = (TextView) findViewById(R.id.user_item_right_text);
		user_item_right_text.setTypeface(MyApplication.GetTypeFace());
		if (MyApplication.login_status) {
			findViewById(R.id.user_update_pw).setOnClickListener(this);
		} else {
			user_item_right_text.setText("不支持修改密码");
		}

		mUserInfor = MyApplication.loginUserInfor;
		// mLoadingDialog.startDialog("加载中");

		try {

			setCacheSize(getTotalCacheSize(mContext));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取用户信息
	 */
	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.GET_USERINFO,
				params, new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mUserInfor = JsonUtil.getUserInforFromString(resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							if (JsonUtil.status == HttpCode.serverCode.DATA_Success_CODE) {
								MyApplication.loginUserInfor = mUserInfor;
								initData();
								saveUserInfo(mUserInfor);
								mLoadingHandler.stopLoading();
							} else {
								// mLoadingDialog.cancelDialog();
								mLoadingHandler.showErrorText(JsonUtil.JsonMSG);
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 保存用户信息
	 * 
	 * @param userinfo
	 */
	private void saveUserInfo(UserInfor userinfo) {
		if (userinfo == null) {
			return;
		}
		String str = JsonHelperUtil.toJSON(userinfo).toString();
		SharedPreManager.saveUserInfor(str);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		if (mUserInfor != null) {
			if ("1".equals(MyApplication.loginUserInfor.getUserSex())) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(mUserInfor.getUserHeadImgUrl(),
								mHeadIcon, Options.getLoginOptions());
			} else {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(mUserInfor.getUserHeadImgUrl(),
								mHeadIcon, Options.getLoginOptions());

			}
			mHeadIcon.setTag(mUserInfor.getUserHeadImgUrl());
			mHeadIcon.setOnClickListener(this);
			if (mUserInfor.getUserNickName() != null) {
				nickName_tv.setText(mUserInfor.getUserNickName());

			}

			if ("1".equals(mUserInfor.getUserSex())) {
				sex_tv.setText("男");
			} else if ("2".equals(mUserInfor.getUserSex())) {
				sex_tv.setText("女");
			} else {
				sex_tv.setText("未知");
			}
			if (mUserInfor.getUserTelephone() != null
					&& !mUserInfor.getUserTelephone().equals("")) {
				findViewById(R.id.user_bind_email).setVisibility(View.VISIBLE);
				email_tv.setText(mUserInfor.getUserTelephone());
			}
			if (mUserInfor.getUserMobileNo() != null
					&& !TextUtils.isEmpty(mUserInfor.getUserMobileNo()
							.toString())) {
				email_tv.setText(mUserInfor.getUserMobileNo());
			}
			Log.i("http", mUserInfor.toString());
			if (mUserInfor.getRegisterOrigin().equals("1")) {
				Drawable drawable = getResources().getDrawable(
						R.drawable.sh_icon_null);
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界
				email_tv.setCompoundDrawables(null, null, drawable, null);
				findViewById(R.id.user_bind_email).setEnabled(false);
			}
			if (null != mUserInfor.getUserArea()
					&& mUserInfor.getUserArea().length() > 0) {
				String city = null == mUserInfor.getUserCity() ? ""
						: mUserInfor.getUserCity();
				String area = mUserInfor.getUserArea();
				userItemRegion.setText(getCityData(city) + getCityData(area));
			}
			if (null != mUserInfor.getUserName()
					&& !mUserInfor.getUserName().equals("")) {
				userItemAccount.setText(mUserInfor.getUserName());
			}

		} else {
			ToastUtil.showToast("数据请求失败。");
		}
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		// RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		// mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		// mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		// TextView mContext = (TextView)
		// mTitle.findViewById(R.id.title_content);
		// mContext.setVisibility(View.VISIBLE);
		// mContext.setText(getString(R.string.user_title_text_z));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AppConfigUtil.RESULT_LOCAT_CODE:// 拍照返回
			MyApplication.getInstance().getImageLoader()
					.displayImage(mUserInfor.getUserHeadImgUrl(), mHeadIcon);
			// 通知主界面更换头像
			// MyApplication.getmMainHandler().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
			MyApplication.getInstance().getmMainFragment()
					.sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_LINKNAME:
			// Log.i("ceshi", "回掉了==  ");
			String strLINKNAME = data.getExtras().getString(
					DialogTypeUtil.DialogType);
			nickName_tv.setText(strLINKNAME);
			MyApplication.loginUserInfor.setUserNickName(strLINKNAME);
			saveUserInfo(MyApplication.loginUserInfor);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_REGION:
			String region = data.getExtras().getString(
					DialogTypeUtil.DialogType);
			Log.d("region2", region);
			userItemRegion.setText(region);
			MyApplication.loginUserInfor.setUserCity(region);
			saveUserInfo(MyApplication.loginUserInfor);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_SEX:
			String strSEX = data.getExtras().getString(
					DialogTypeUtil.DialogType);
			sex_tv.setText(strSEX);
			int resDrawable;
			if ("男".equals(strSEX)) {
				MyApplication.loginUserInfor.setUserSex("1");
				resDrawable = R.drawable.sh_user_sex_man;
			} else {
				MyApplication.loginUserInfor.setUserSex("2");
				resDrawable = R.drawable.sh_user_sex_woman;
			}
			MyApplication.getInstance().getImageLoader()
					.displayImage(mUserInfor.getUserHeadImgUrl(), mHeadIcon);
			saveUserInfo(MyApplication.loginUserInfor);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_BIRTHDAY:
			String strBIRTHDAY = data.getExtras().getString(
					DialogTypeUtil.DialogType);
			// birthday_tv.setText(strBIRTHDAY);
			MyApplication.loginUserInfor.setUserBirth(strBIRTHDAY);
			saveUserInfo(MyApplication.loginUserInfor);
			break;
		case DialogTypeUtil.UserDialogType.USER_BINDING_PHONE:// 绑定手机返回
			String strEMAIL = data.getExtras().getString(
					DialogTypeUtil.DialogType);
			email_tv.setText(strEMAIL);
			Drawable drawable = getResources().getDrawable(
					R.drawable.sh_icon_null);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界
			// email_tv.setCompoundDrawables(null, null, drawable, null);
			// findViewById(R.id.user_bind_email).setEnabled(false);
			MyApplication.loginUserInfor.setUserTelephone(strEMAIL);
			saveUserInfo(MyApplication.loginUserInfor);
			break;
		case DialogTypeUtil.UserDialogType.USER_RETURN:// 退出登录
			userExit();
			break;

		default:
			break;
		}
	}

	/**
	 * 返回键监听
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			onReturn();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 返回
	 */
	private void onReturn() {
		Intent mRetrue = new Intent(mContext, UserCenterActivity.class);
		setResult(AppConfigUtil.PERSONAL_RESULT_USER_CODE, mRetrue);
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent userdialog = new Intent(mContext, UserDialogActivity.class);
		switch (view.getId()) {
		case R.id.user_head_icon:
			if (view.getTag().toString().length() <= 0) {
				ToastUtil.showToast("默认头像无法放大显示！");
				return;
			}
			userdialog.setClass(this, ImageOriginalActivity.class);
			userdialog.putExtra("select_imgs", view.getTag().toString());
			userdialog.putExtra("id", 0);
			break;
		case R.id.left_iv:
			onReturn();
			break;
		case R.id.title_left:
			onReturn();
			break;
		case R.id.user_head_icon_layout:// 更换头像
			FastBlur.getScreen((Activity) mContext);
			userdialog.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.UserDialogType.USER_EDIT_USERICON);
			startActivityForResult(userdialog, AppConfigUtil.USER_REQUEST_CODE);
			break;
		case R.id.user_nick:
			FastBlur.getScreen((Activity) mContext);
			userdialog.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.UserDialogType.USER_EDIT_LINKNAME);
			userdialog.putExtra(DialogTypeUtil.DialogContent, nickName_tv
					.getText().toString());
			startActivityForResult(userdialog,
					DialogTypeUtil.UserDialogType.USER_EDIT_LINKNAME);
			// startActivity(userdialog);
			break;
		case R.id.user_sex:
			FastBlur.getScreen((Activity) mContext);
			userdialog.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.UserDialogType.USER_EDIT_SEX);
			userdialog.putExtra(DialogTypeUtil.DialogContent, sex_tv.getText()
					.toString());
			startActivityForResult(userdialog,
					DialogTypeUtil.UserDialogType.USER_EDIT_SEX);
			break;
		// case R.id.user_birthday: //修改日期
		// FastBlur.getScreen((Activity) mContext);
		// userdialog.putExtra(DialogTypeUtil.DialogType,
		// DialogTypeUtil.UserDialogType.USER_EDIT_BIRTHDAY);
		// userdialog.putExtra(DialogTypeUtil.DialogContent, birthday_tv
		// .getText().toString());
		// startActivityForResult(userdialog,
		// DialogTypeUtil.UserDialogType.USER_EDIT_BIRTHDAY);
		// break;
		case R.id.user_bind_email:// 手机号码
			FastBlur.getScreen((Activity) mContext);

			userdialog.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.UserDialogType.USER_BINDING_PHONE);
			userdialog.putExtra(DialogTypeUtil.DialogContent, email_tv
					.getText().toString());
			startActivityForResult(userdialog,
					DialogTypeUtil.UserDialogType.USER_BINDING_PHONE);

			break;
		case R.id.user_region:// 地区
			userdialog = new Intent(this, RegionChoiceActivity.class);
			userdialog.putExtra(DialogTypeUtil.DialogType, userItemRegion
					.getText().toString());
			startActivityForResult(userdialog,
					DialogTypeUtil.UserDialogType.USER_EDIT_REGION);
			break;
		case R.id.user_update_pw:// 密码修改
			FastBlur.getScreen((Activity) mContext);
			userdialog.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.UserDialogType.USER_EDIT_PASSWORD);
			startActivityForResult(userdialog,
					DialogTypeUtil.UserDialogType.USER_EDIT_PASSWORD);
			break;
		// case R.id.user_bind_third: //绑定第三方
		// userdialog = new Intent(mContext, ThirdBindActivity.class);
		// startActivityForResult(userdialog, Intent.FILL_IN_ACTION);
		// break;
		case R.id.user_eliminate: // 清除缓存
			clearAllCache(mContext);
			break;
		case R.id.user_return_login: // 退出登录
			FastBlur.getScreen((Activity) mContext);
			userdialog.putExtra(DialogTypeUtil.DialogType,
					DialogTypeUtil.UserDialogType.USER_RETURN);
			startActivityForResult(userdialog,
					DialogTypeUtil.UserDialogType.USER_RETURN);
			break;
		default:
			break;
		}
	}

	/**
	 * 更新头像更新
	 */
	private Handler mHandler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			// 更新用户头像
			if (msg.what == MyApplication.HANDLER_USER_CODE) {
				MyApplication
						.getInstance()
						.getImageLoader()
						.displayImage(
								MyApplication.loginUserInfor
										.getUserHeadImgUrl(),
								mHeadIcon);
			}
		}
	};

	@Override
	public void onLoadingRefresh() {
		// TODO Auto-generated method stub
		mLoadingHandler.startLoading();
		getData();
	}

	/**
	 * 清理缓存
	 * 
	 * @param context
	 */
	@SuppressWarnings("static-access")
	private void clearAllCache(final Context context) {
		mLoadingDialog.startDialog("清理中");
		try {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					deleteDir(context.getCacheDir());
					if (Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						deleteDir(context.getExternalCacheDir());
					}
					deleteDir(user_cache_path);
					deleteDir(image_cache_path);
					deleteDir(cache_path);
					cleanInternalCache();
					try {
						setCacheSize(getTotalCacheSize(mContext));
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					mLoadingDialog.cancelDialog();
				}
			}, mTime);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param dir
	 * @return
	 */
	private boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * 删除文件
	 * 
	 * @param directory
	 */
	private void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/**
	 * 清除缓存
	 * 
	 * @param context
	 */
	private void cleanInternalCache() {
		deleteFilesByDirectory(user_cache_path);
	}

	/**
	 * 获取缓存大小
	 */
	public String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		cacheSize += getFolderSize(user_cache_path);
		cacheSize += getFolderSize(image_cache_path);
		cacheSize += getFolderSize(cache_path);
		return getFormatSize(cacheSize);
	}

	/**
	 * 获取文件大小
	 * 
	 * @param file
	 * @return
	 */

	public static long getFolderSize(File file) {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	private String getCityData(String area) {
		area = area.indexOf(",") == -1 ? area : area.substring(
				area.indexOf(",") + 1, area.length());
		return area;
	}

	/**
	 * 设置缓存大小
	 * 
	 * @param size
	 */
	private void setCacheSize(String size) {
		userCenterSize.setText(size);
	}

	WebViewClient wvc = new WebViewClient() {
		public void onPageFinished(WebView view, String url) {
			Log.i(TAG, "回调了");
			view.loadUrl("javascript:window.localStorage.clear()");
			view.loadUrl("javascript:alert(window.localStorage.length);");
		}
	};

	/**
	 * 退出帐号
	 */
	private void userExit() {
		WebView wv = new WebView(mContext);
		wv.setWebViewClient(wvc);
		wv.loadUrl("<!DOCTYPE html><html><head><title></title></head><body><p>Just to clear the localStorage when change account.</p></body></html>");
		Editor editor = sharedPreferences.edit();
		// 重新进入时需要重选主题
		editor.putBoolean("isFirstRun", true);
		editor.commit();
		SharedPreManager.clearUserInfo();
		// Intent intent = new Intent(mContext, SplashActivity.class);
		// startActivity(intent);
		finish();
	}
}
