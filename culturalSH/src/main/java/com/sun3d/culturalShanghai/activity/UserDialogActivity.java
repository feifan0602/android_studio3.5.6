package com.sun3d.culturalShanghai.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.ErrorStatusUtil;
import com.sun3d.culturalShanghai.Util.FolderUtil;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.MD5Util;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.UploadImgUtil;
import com.sun3d.culturalShanghai.Util.UploadImgUtil.OnUploadProcessListener;
import com.sun3d.culturalShanghai.Util.ValidateUtil;
import com.sun3d.culturalShanghai.animation.PageChangeAnimation;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.PictureSetting;
import com.sun3d.culturalShanghai.handler.UserHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.ShareInfo;
import com.sun3d.culturalShanghai.object.UserInfor;
import com.sun3d.culturalShanghai.thirdparty.GetTokenPage;
import com.sun3d.culturalShanghai.thirdparty.MyShare;
import com.sun3d.culturalShanghai.thirdparty.ShareName;
import com.sun3d.culturalShanghai.view.FastBlur;
import com.sun3d.culturalShanghai.view.wheelview.ScreenInfo;
import com.sun3d.culturalShanghai.view.wheelview.WheelMain;
import com.sun3d.culturalShanghai.view.wheelview.WheelMain.OnWheelViewChangedListener;
import com.umeng.analytics.MobclickAgent;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class UserDialogActivity extends Activity implements OnClickListener, OnUploadProcessListener
{
	private int type;
	private String TAG = "UserDialogActivity";
	private LinearLayout contentLayout;
	private Context mContext;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private File sdcardTempFile;
	private PictureSetting pictureSetting = null;
	private Boolean IsloginGo = false;
	private Boolean IsThFastloginGo = false;
	private Boolean isLoadingGo = true;
	private LoadingDialog mLoadingDialog;
	private String loadingText = "请求中";
	private PageChangeAnimation mPageChangeAnimation;
	private List<View> addList;
	private Boolean isUserPictrue = true;
	private String teamUserId;
	private Boolean isUnload = true;
	public static int isClose = 0;

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(mContext);
		switch (isClose)
		{
		case 1:
			ToastUtil.showToast("分享成功");
			finish();
			break;
		case 2:
			ToastUtil.showToast("分享失败");
			break;
		case 3:
			ToastUtil.showToast("分享取消");
			break;

		default:
			break;
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 3.5.2 取消掉这段代码 不知道有什么用 但是 会引起程序 异常奔溃
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectDiskReads().detectDiskWrites().detectNetwork()
		// .penaltyLog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
		// .penaltyLog().penaltyDeath().build());
		MyApplication.getInstance().addActivitys(this);
		mContext = this;
		type = getIntent().getExtras().getInt(DialogTypeUtil.DialogType);
		setContentView(R.layout.activity_userdialog_layout);
		mLoadingDialog = new LoadingDialog(mContext);
		init();
	}

	/**
	 * 初始换
	 */
	private void init()
	{
		LinearLayout main = (LinearLayout) findViewById(R.id.main);
		contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		FastBlur.setLinearLayoutBG(mContext, main);
		mPageChangeAnimation = new PageChangeAnimation();
		// mPageChangeAnimation.alphaTranslationXAnimation(contentLayout);
		addList = new ArrayList<View>();
		switch (type)
		{
		case DialogTypeUtil.UserDialogType.USER_FAST_LOGIN:// 快速登录
			addFastLogin(false);
			break;
		// case DialogTypeUtil.UserDialogType.USER_LOGIN:// 登录
		// isLoadingGo = true;
		// addLogin(false);
		// break;
		case DialogTypeUtil.UserDialogType.USER_REGUSTERED:// 用户注册
			addRegister(false);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_BIRTHDAY:// 编辑生日
			addEditBrithday();
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_LINKNAME:// 编辑昵称
			addEditLinkName();
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_REGION:// 编辑地区
			addEditLinkRegion();
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_PASSWORD:// 修改密码
			addEditPassWord(false);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_USERICON:// 修改用户头像
			isUserPictrue = true;
			isUnload = true;
			addEditIcon();
			break;
		case DialogTypeUtil.UserDialogType.USER_FIND_PASSWORD:// 寻找密码
			addFindPassWord(false);
			break;
		case DialogTypeUtil.UserDialogType.USER_FORGOT_PASSWORD:// 忘记密码
			addForGotPassword(false);
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_SEX:// 编辑性别
			addEditSex();
			break;
		case DialogTypeUtil.UserDialogType.USER_BINDING_PHONE:// 绑定手机
			addBindPhone();
			break;
		case DialogTypeUtil.UserDialogType.USER_RETURN:// 退出登录
			MyApplication.getInstance().setIsFromMyLove(false);
			userReturnLogin();
			// addBindPhone();
			break;
		case DialogTypeUtil.UserDialogType.USER_BINDING_EMAIL:// 绑定邮箱
			addBindEmail();
			break;
		case DialogTypeUtil.UserDialogType.USER_PERSONALITY_SETTING:// 个性设置
			addPersonalitySetting(false);
			break;
		case DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_FAST_LOGIN:// 第三方快速登录
			isLoadingGo = true;
			// addThirdFastLogin(false);
			addFastLogin(false);
			break;
		case DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_PAY:// 第三方支付
			break;
		case DialogTypeUtil.ThirdPartyDialogType.THIRDPARTY_SHARE:// 第三方分享
			addThirdShare();
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_GROUPPICTRUE:// 修改团体图片
			isUserPictrue = false;
			isUnload = true;
			teamUserId = this.getIntent().getStringExtra("teamUserId");
			addEditIcon();
			break;
		case DialogTypeUtil.UserDialogType.USER_EDIT_COMMENTPICTRUE:// 编辑评论图片
			isUnload = false;
			addEditIcon();
			break;
		default:
			break;
		}

	}

	/**
	 * 退出登录
	 */
	private void userReturnLogin()
	{
		// TODO Auto-generated method stub
		contentLayout.removeAllViewsInLayout();
		RelativeLayout tshareLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_return_login_layout, null);
		tshareLayout.findViewById(R.id.user_return).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				setResult(DialogTypeUtil.UserDialogType.USER_RETURN);
				finish();
			}
		});
		tshareLayout.findViewById(R.id.user_return_cancel).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});

		contentLayout.addView(tshareLayout);

	}

	/**
	 * 第三方分享
	 */
	private void addThirdShare()
	{

		contentLayout.removeAllViewsInLayout();
		LinearLayout tshareLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_thirdshare_layout, null);
		tshareLayout.findViewById(R.id.share_cancel).setOnClickListener(this);
		contentLayout.addView(tshareLayout);

		final ShareInfo shareInfo = (ShareInfo) getIntent().getSerializableExtra(AppConfigUtil.INTENT_SHAREINFO);
		// 微信好友圈
		tshareLayout.findViewById(R.id.third_share_weixin_friend).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				// ShareToWX.shareShow(mContext, shareInfo, true);
				MyShare.showShare(ShareName.WechatMoments, mContext, shareInfo);
				finish();
			}
		});
		// 微信好友
		tshareLayout.findViewById(R.id.third_share_weixin).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				MyShare.showShare(ShareName.Wechat, mContext, shareInfo);
				// ShareToWX.shareShow(mContext, shareInfo, false);
				finish();
			}
		});
		// QQ好友
		tshareLayout.findViewById(R.id.third_share_qq).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				MyShare.showShare(ShareName.QQ, mContext, shareInfo);
				finish();
			}
		});
		// 新浪微博
		tshareLayout.findViewById(R.id.third_share_sina).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				MyShare.showShare(ShareName.SinaWeibo, mContext, shareInfo);
				finish();
			}
		});

	}

	/**
	 * 第三方快速登录
	 */
	private void addThirdFastLogin(Boolean isback)
	{
		IsThFastloginGo = true;
		LinearLayout tfastloginLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_login, null);
		addList.add(tfastloginLayout);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(tfastloginLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(tfastloginLayout, contentLayout.getChildAt(0));
		}

		tfastloginLayout.findViewById(R.id.fastlogin_register).setOnClickListener(this);
		tfastloginLayout.findViewById(R.id.third_fastlogin_return).setOnClickListener(this);
		// tfastloginLayout.findViewById(R.id.third_fastlogin_account).setOnClickListener(this);
		tfastloginLayout.findViewById(R.id.third_fastlogin_qq).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.startDialog("登录中");
				GetTokenPage.GetQQToken(mContext, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							Log.i("ceshi ", "第三方statusCode  ==  " + statusCode + "  resultStr  ==  " + resultStr);
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");
								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								finish();
							} else
							{
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							ToastUtil.showToast(resultStr);
						}
					}

				});
			}
		});
		tfastloginLayout.findViewById(R.id.third_fastlogin_sina).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.startDialog("登录中");
				GetTokenPage.GetSinaWeiboToken(mContext, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");
								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								finish();
							} else
							{
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							ToastUtil.showToast(resultStr);
						}
					}

				});
			}
		});
		tfastloginLayout.findViewById(R.id.third_fastlogin_weixin).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.startDialog("登录中");
				GetTokenPage.GetWechatToken(mContext, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");

								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								finish();
							} else
							{
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							ToastUtil.showToast(resultStr);
						}
					}

				});
			}
		});
		contentLayout.addView(tfastloginLayout);
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	/**
	 * 修改密码
	 */
	private void addEditPassWord(Boolean isback)
	{
		LinearLayout EditpeLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_editpw_layout, null);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(EditpeLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(EditpeLayout, contentLayout.getChildAt(0));
		}

		final EditText oldinput = (EditText) EditpeLayout.findViewById(R.id.editpw_input_oldpw);
		final EditText newinput1 = (EditText) EditpeLayout.findViewById(R.id.editpw_input_newpw);
		final EditText newinput2 = (EditText) EditpeLayout.findViewById(R.id.editpw_input_truenewpw);
		EditpeLayout.findViewById(R.id.editpw_return).setOnClickListener(this);
		EditpeLayout.findViewById(R.id.editpw_commit).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				String oldstr = oldinput.getText().toString();
				String newstr = newinput1.getText().toString();
				String newstrtrue = newinput2.getText().toString();
				StringBuffer sb = new StringBuffer();
				if (!ValidateUtil.findpwVaildate(oldstr, newstr, newstrtrue, sb))
				{
					ToastUtil.showToast(sb.toString());
					return;
				}
				mLoadingDialog.startDialog(loadingText);
				UserHandler.setUserPassWord(oldstr, newstrtrue, new HttpRequestCallback()
				{
					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							if (JsonUtil.getJsonStatus(resultStr) == HttpCode.serverCode.DATA_Success_CODE)
							{
								ToastUtil.showToast("修改成功！");
								addFastLogin(false);
							} else
							{
								ToastUtil.showToast("" + JsonUtil.JsonMSG);
							}
						} else
						{
							ToastUtil.showToast(resultStr);
						}
					}
				});
			}
		});
		contentLayout.addView(EditpeLayout);
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	/**
	 * 绑定邮箱
	 */
	private void addBindEmail()
	{
		contentLayout.removeAllViewsInLayout();
		LinearLayout bindmailLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_bindemail_layout, null);
		final EditText emailinput = (EditText) bindmailLayout.findViewById(R.id.bindemail_input_phone);
		bindmailLayout.findViewById(R.id.bindemail_return).setOnClickListener(this);
		bindmailLayout.findViewById(R.id.bindemail_commit).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				final String emailStr = emailinput.getText().toString();
				if (emailStr.trim().length() == 0)
				{
					ToastUtil.showToast(mContext.getResources().getString(R.string.user_null_email));
					return;
				}
				if (!ValidateUtil.isEmail(emailStr))
				{
					ToastUtil.showToast("邮箱格式有误!请重新输入");
					return;
				}
				mLoadingDialog.startDialog(loadingText);
				UserHandler.setUserEmail(emailStr, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						int code = JsonUtil.getJsonStatus(resultStr);
						if (code == HttpCode.serverCode.DATA_Success_CODE)
						{
							ToastUtil.showToast("修改成功！");
							Intent intent = new Intent();
							intent.putExtra(DialogTypeUtil.DialogType, emailStr);
							setResult(DialogTypeUtil.UserDialogType.USER_BINDING_EMAIL, intent);
							finish();
						} else
						{
							ToastUtil.showToast(JsonUtil.JsonMSG);
						}
					}
				});
			}
		});
		contentLayout.addView(bindmailLayout);

	}

	/**
	 * 绑定手机
	 */
	private void addBindPhone()
	{
		contentLayout.removeAllViewsInLayout();
		String phone = getIntent().getExtras().getString(DialogTypeUtil.DialogContent);
		LinearLayout bindphoneLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_bindphone_layout, null);
		bindphoneLayout.findViewById(R.id.bindphone_return).setOnClickListener(this);

		final EditText minput = (EditText) bindphoneLayout.findViewById(R.id.bindphone_input_phone);
		final EditText mcodeinput = (EditText) bindphoneLayout.findViewById(R.id.bindphone_input_verificationcode);
		final Button mGetCode = (Button) bindphoneLayout.findViewById(R.id.bindphone_getcode);
		mGetCode.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				String phone = minput.getText().toString();
				if (!ValidateUtil.isCellphone(phone, new StringBuffer()))
				{
					return;
				}
				if (phone.length() > 0)
				{
					mLoadingDialog.startDialog(loadingText);
					UserHandler.sendPhoneCode(phone, new HttpRequestCallback()
					{
						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
							{
								ToastUtil.showToast("验证码发送成功！");
								TimeCount timeCount = new TimeCount(60000, 1000, mGetCode);
								if (mGetCode.isClickable())
								{
									timeCount.start();
								}
							} else
							{
								if (HttpCode.ServerCode_Phone.ServerCode_RepeatPhone == JsonUtil.status)
								{
									ToastUtil.showToast("该手机号已经绑定，不能重复绑定！");
								} else
								{
									// ErrorStatusUtil.seachServerStatus(JsonUtil.status,
									// resultStr);
									ToastUtil.showToast(JsonUtil.JsonMSG);
								}
							}
						}
					});
				} else
				{
					ToastUtil.showToast("请输入手机号码。");
				}
			}
		});

		if (null != phone && phone.length() > 0)
		{
			minput.setText(phone);
		}
		bindphoneLayout.findViewById(R.id.bindphone_commit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				final String phone = minput.getText().toString();
				if (!ValidateUtil.isCellphone(phone, new StringBuffer()))
				{
					return;
				}
				String code = mcodeinput.getText().toString();
				if (code.length() > 0 && phone.length() > 0)
				{
					mLoadingDialog.startDialog(loadingText);
					UserHandler.bindPhone(phone, code, new HttpRequestCallback()
					{
						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (statusCode == HttpCode.HTTP_Request_Success_CODE)
							{
								if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
								{
									ToastUtil.showToast("手机绑定成功！");
									Intent intent = new Intent();
									intent.putExtra(DialogTypeUtil.DialogType, phone);
									setResult(DialogTypeUtil.UserDialogType.USER_BINDING_PHONE, intent);
									finish();
								} else
								{
									ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
								}
							} else
							{
								ToastUtil.showToast(resultStr);
							}
						}
					});
				}
			}
		});

		bindphoneLayout.findViewById(R.id.bindphone_commit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				final String phone = minput.getText().toString();
				if (!ValidateUtil.isCellphone(phone, new StringBuffer()))
				{
					ToastUtil.showToast("请输入合法手机号");
					return;
				}
				String code = mcodeinput.getText().toString();
				if (phone.length() > 0)
				{
					mLoadingDialog.startDialog(loadingText);
					UserHandler.bindPhone(phone, code, new HttpRequestCallback()
					{
						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (statusCode == HttpCode.HTTP_Request_Success_CODE)
							{

								if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
								{
									ToastUtil.showToast("手机绑定成功！");
									Intent intent = new Intent();
									intent.putExtra(DialogTypeUtil.DialogType, phone);
									setResult(DialogTypeUtil.UserDialogType.USER_BINDING_PHONE, intent);
									finish();
								} else
								{
									ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
								}
							} else
							{
								ToastUtil.showToast(resultStr);
							}
						}
					});
				} else
				{
					ToastUtil.showToast("不能为空！");
				}
			}
		});

		contentLayout.addView(bindphoneLayout);
	}

	/**
	 * 编辑昵称
	 */
	private void addEditLinkName()
	{
		contentLayout.removeAllViewsInLayout();
		LinearLayout editlinknameLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_editlinkname_layout, null);
		editlinknameLayout.findViewById(R.id.linkname_return).setOnClickListener(this);
		final EditText ipunt = (EditText) editlinknameLayout.findViewById(R.id.linkname_input_newpw);
		String Name = this.getIntent().getStringExtra(DialogTypeUtil.DialogContent);
		ipunt.setText(Name);
		editlinknameLayout.findViewById(R.id.linkname_commit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				final String name = ipunt.getText().toString();
				if (!ValidateUtil.isIllegal(name))
				{
					ToastUtil.showToast(getResources().getString(R.string.user_null_name));
					return;
				}
				if (name.length() > 0)
				{
					mLoadingDialog.startDialog(loadingText);
					UserHandler.setUserNameSexBirth(name, -1, null, null, new HttpRequestCallback()
					{
						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (statusCode == HttpCode.HTTP_Request_Success_CODE)
							{
								if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
								{
									ToastUtil.showToast("修改成功");
									Intent intent = new Intent();
									intent.putExtra(DialogTypeUtil.DialogType, name);
									setResult(DialogTypeUtil.UserDialogType.USER_EDIT_LINKNAME, intent);
									finish();
								} else
								{
									ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
								}
							} else
							{
								ToastUtil.showToast(resultStr);
							}
						}
					});
				} else
				{
					ToastUtil.showToast("请输入修改昵称!");
				}
			}
		});

		contentLayout.addView(editlinknameLayout);
	}

	/**
	 * 编辑地区
	 */
	private void addEditLinkRegion()
	{
		contentLayout.removeAllViewsInLayout();
		LinearLayout editlinknameLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_editlinkname_layout, null);
		editlinknameLayout.findViewById(R.id.linkname_return).setOnClickListener(this);
		final EditText ipunt = (EditText) editlinknameLayout.findViewById(R.id.linkname_input_newpw);
		((TextView) editlinknameLayout.findViewById(R.id.edit_name)).setText("编辑地区");
		ipunt.setHint("请输入20字以内的地区");
		String mRegion = this.getIntent().getStringExtra(DialogTypeUtil.DialogContent);
		if (null != mRegion && mRegion.length() > 0)
		{
			ipunt.setText(mRegion);
		}
		editlinknameLayout.findViewById(R.id.linkname_commit).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				final String region = ipunt.getText().toString();
				if (!ValidateUtil.isIllegal(region))
				{
					ToastUtil.showToast(getResources().getString(R.string.user_null_name));
					return;
				}
				if (region.length() > 0)
				{
					mLoadingDialog.startDialog(loadingText);
					UserHandler.setUserNameSexBirth(null, -1, null, region, new HttpRequestCallback()
					{
						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (statusCode == HttpCode.HTTP_Request_Success_CODE)
							{
								if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
								{
									ToastUtil.showToast("修改成功");
									Intent intent = new Intent();
									intent.putExtra(DialogTypeUtil.DialogType, region);
									setResult(DialogTypeUtil.UserDialogType.USER_EDIT_REGION, intent);
									finish();
								} else
								{
									ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
								}
							} else
							{
								ToastUtil.showToast(resultStr);
							}
						}
					});
				} else
				{
					ToastUtil.showToast("请输入修改地区!");
				}
			}
		});

		contentLayout.addView(editlinknameLayout);
	}

	/**
	 * 编辑用户头像
	 */
	private void addEditIcon()
	{
		contentLayout.removeAllViewsInLayout();
		RelativeLayout editiconLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_edit_usericon_layout, null);
		editiconLayout.findViewById(R.id.usericon_takepic).setOnClickListener(this);
		editiconLayout.findViewById(R.id.usericon_choosepic).setOnClickListener(this);
		editiconLayout.findViewById(R.id.usericon_cancel).setOnClickListener(this);
		contentLayout.addView(editiconLayout);
	}

	/**
	 * 注册界面
	 */
	private void addRegister(Boolean isback)
	{
		contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
		LinearLayout registerLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_register_layout, null);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(registerLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(registerLayout, contentLayout.getChildAt(0));
		}

		registerLayout.findViewById(R.id.register_return).setOnClickListener(this);
		contentLayout.addView(registerLayout);
		// final EditText nick = (EditText)
		// registerLayout.findViewById(R.id.register_input_newpw);
		final EditText phone = (EditText) registerLayout.findViewById(R.id.register_input_phone);
		final EditText nickname = (EditText) registerLayout.findViewById(R.id.register_input_nickname);
		final EditText pw = (EditText) registerLayout.findViewById(R.id.register_input_truenewpw);
		final EditText code = (EditText) registerLayout.findViewById(R.id.register_input_verificationcode);
		final Button mGetCodeText = (Button) registerLayout.findViewById(R.id.register_getcode);

		// 获取验证码
		mGetCodeText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				String phoneString = phone.getText().toString();
				getRegisterCode(phoneString, mGetCodeText);
			}
		});
		// 注册
		registerLayout.findViewById(R.id.register_commit).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				String nickStr = nickname.getText().toString();
				String pwStr = pw.getText().toString();
				String phoneStr = phone.getText().toString();
				String codeStr = code.getText().toString();
				onRegister(nickStr, pwStr, phoneStr, codeStr);
			}
		});
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	/**
	 * 注册获取验证码
	 */
	private void getRegisterCode(String phoneString, final Button mGetCodeText)
	{
		StringBuffer sb = new StringBuffer();
		if (!ValidateUtil.isCellphone(phoneString, sb))
		{
			ToastUtil.showToast(sb.toString());
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.UserUrl.REGISTER_PHONE, phoneString);
		mLoadingDialog.startDialog(loadingText);
		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.REGISTER_PHONE_CODE, params, new HttpRequestCallback()
		{
			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.cancelDialog();
				if (statusCode == HttpCode.HTTP_Request_Success_CODE)
				{
					int code = JsonUtil.getJsonStatus(resultStr);

					if (code == HttpCode.serverCode.DATA_Success_CODE)
					{
						TimeCount timeCount = new TimeCount(60000, 1000, mGetCodeText);
						if (mGetCodeText.isClickable())
						{
							timeCount.start();
						}
						ToastUtil.showToast("发送成功");
					} else
					{
						ToastUtil.showToast(JsonUtil.JsonMSG);
					}
				} else
				{
					ToastUtil.showToast(resultStr);
				}
			}
		});
	}

	/**
	 * 注册
	 */
	private void onRegister(final String nick, final String pw,final String phone, String code)
	{
		contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
		StringBuffer sb = new StringBuffer();
		if (!ValidateUtil.registerVaildate(nick, pw, phone, code, sb))
		{
			ToastUtil.showToast(sb.toString());
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.UserUrl.REGISTER_NAME, nick);
		params.put(HttpUrlList.UserUrl.REGISTER_PW, MD5Util.toMd5(pw));
		params.put(HttpUrlList.UserUrl.REGISTER_PHONE, phone);
		params.put(HttpUrlList.UserUrl.REGISTER_CODE, code);
		// params.put("userBirthStr", "1980-01-01");
		// params.put("userSex", "1");
		mLoadingDialog.startDialog(loadingText);

		MyHttpRequest.onHttpPostParams(HttpUrlList.UserUrl.REGISTER_URL, params, new HttpRequestCallback()
		{
			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.cancelDialog();
				if (statusCode == HttpCode.HTTP_Request_Success_CODE)
				{
					int code = JsonUtil.getJsonStatus(resultStr);
					if (code == HttpCode.serverCode.DATA_Success_CODE)
					{
						if (MyApplication.loginUserInfor == null)
						{
							MyApplication.loginUserInfor = new UserInfor();
						}
						MyApplication.loginUserInfor.setUserId(JsonUtil.getUserId(resultStr));
						ToastUtil.showToast("注册成功");
						MyApplication.loginUserInfor = new UserInfor();
						// Intent intent = new Intent(mContext,
						// MyLoveActivity.class);
						// mContext.startActivity(intent);
						// modify by fishbird at 16-09-05
						// 注册成功后，直接登录，跳转返回

						UserHandler.fastLogin(phone, pw, new HttpRequestCallback()
						{

							@Override
							public void onPostExecute(int statusCode, String resultStr)
							{
								// TODO Auto-generated method stub

								
								Log.i("ceshi", "看看登陆返回的数据 resultStr==  " + resultStr);
								if (statusCode == HttpCode.HTTP_Request_Success_CODE)
								{
									int code = JsonUtil.getJsonStatus(resultStr);
									if (code == HttpCode.serverCode.DATA_Success_CODE)
									{
										UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
										mUserInfor.setRegisterOrigin("1");
										MyApplication.loginUserInfor = mUserInfor;
										if (MyApplication.getInstance().getmMainFragment() != null)
										{
											MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
										}
										if (MyApplication.getInstance().getActivityHandler() != null)
										{
											Message msg = new Message();
											msg.arg1 = 1002;
											MyApplication.getInstance().getActivityHandler().sendMessage(msg);
										}

										sendmyBroadcast();
										String str = JsonHelperUtil.toJSON(mUserInfor).toString();
										// 保存登录后的信息到 Shareperfence
										SharedPreManager.saveUserInfor(str);
										//ToastUtil.showToast("登录成功.");
										MyApplication.login_status = true;
										setResult(AppConfigUtil.LOADING_LOGIN_BACK);
										//返回用户首页
										Intent intent = new Intent(mContext, MainFragmentActivity.class);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
									} 
								}
								Log.d(TAG, "结果：" + resultStr);
							}
						});

						

						/**
						 * 登录界面
						 */
						// addFastLogin(false);
						// 个性化设置
						// addPersonalitySetting(false);
					} else if (code == 10101)
					{
						ToastUtil.showToast("用户名缺失");
					} else if (code == 10102)
					{
						ToastUtil.showToast("密码缺失");
					} else if (code == 13104)
					{
						ToastUtil.showToast("手机号码缺失");
					} else if (code == 10105)
					{
						ToastUtil.showToast("昵称已存在");
					} else if (code == 13120)
					{
						ToastUtil.showToast("验证码错误");
					} else if (code == 14101)
					{
						ToastUtil.showToast("短信发送失败");
					} else if (code == 13105)
					{
						ToastUtil.showToast("手机已注册");
					} else
					{
						ToastUtil.showToast("注册失败");
					}

				} else
				{
					ToastUtil.showToast(resultStr);
				}

			}
		});

	}

	/**
	 * 登录
	 */
	private void addLogin(Boolean isback)
	{
		IsloginGo = true;
		IsThFastloginGo = false;
		LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_login_layout, null);
		addList.add(loginLayout);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(loginLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(loginLayout, contentLayout.getChildAt(0));
		}

		loginLayout.findViewById(R.id.login_return).setOnClickListener(this);
		loginLayout.findViewById(R.id.login_register).setOnClickListener(this);
		loginLayout.findViewById(R.id.login_login).setOnClickListener(this);
		contentLayout.addView(loginLayout);
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	/**
	 * 快速登录
	 */
	private void addFastLogin(Boolean isback)
	{
		contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
		IsloginGo = false;
		LinearLayout fastloginLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_login, null);
		TextView middle_tv = (TextView) fastloginLayout.findViewById(R.id.middle_tv);
		middle_tv.setTypeface(MyApplication.GetTypeFace());
		middle_tv.setText("登录");
		TextView left_tv = (TextView) fastloginLayout.findViewById(R.id.left_tv);
		TextView left_tv1 = (TextView) fastloginLayout.findViewById(R.id.left_tv1);
		TextView left_tv2 = (TextView) fastloginLayout.findViewById(R.id.left_tv2);
		left_tv.setTypeface(MyApplication.GetTypeFace());
		left_tv1.setTypeface(MyApplication.GetTypeFace());
		left_tv2.setTypeface(MyApplication.GetTypeFace());
		addList.add(fastloginLayout);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(fastloginLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(fastloginLayout, contentLayout.getChildAt(0));
		}

		ImageView third_fastlogin_return = (ImageView) fastloginLayout.findViewById(R.id.third_fastlogin_return);
		TextView fastlogin_forgotpw = (TextView) fastloginLayout.findViewById(R.id.fastlogin_forgotpw);
		TextView fastlogin_register = (TextView) fastloginLayout.findViewById(R.id.fastlogin_register);
		fastlogin_forgotpw.setTypeface(MyApplication.GetTypeFace());
		fastlogin_register.setTypeface(MyApplication.GetTypeFace());
		third_fastlogin_return.setOnClickListener(this);
		fastlogin_forgotpw.setOnClickListener(this);
		fastlogin_register.setOnClickListener(this);
		final EditText inputaccount = (EditText) fastloginLayout.findViewById(R.id.fastlogin_input_account);
		final EditText inputpassword = (EditText) fastloginLayout.findViewById(R.id.fastlogin_input_password);
		inputaccount.setTypeface(MyApplication.GetTypeFace());
		inputpassword.setTypeface(MyApplication.GetTypeFace());
		contentLayout.addView(fastloginLayout);
		Button fastlogin_login = (Button) fastloginLayout.findViewById(R.id.fastlogin_login);
		fastlogin_login.setTypeface(MyApplication.GetTypeFace());
		fastlogin_login.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				StringBuffer sb = new StringBuffer();
				if (!ValidateUtil.loginVaildate(inputaccount.getText().toString(), inputpassword.getText().toString(), sb))
				{
					ToastUtil.showToast(sb.toString());
					return;
				}
				mLoadingDialog.startDialog(loadingText);
				UserHandler.fastLogin(inputaccount.getText().toString().trim(), inputpassword.getText().toString().trim(), new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub

						mLoadingDialog.cancelDialog();
						Log.i("ceshi", "看看登陆返回的数据 resultStr==  " + resultStr);
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								mUserInfor.setRegisterOrigin("1");
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}

								sendmyBroadcast();
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								// 保存登录后的信息到 Shareperfence
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");
								MyApplication.login_status = true;
								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
								finish();

							} else
							{
								contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
								ToastUtil.showToast(JsonUtil.JsonMSG);

							}

						} else
						{
							contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
							ToastUtil.showToast(resultStr);

						}
						// 还原登录的背景色

						Log.d(TAG, "结果：" + resultStr);
					}
				});

			}
		});
		fastloginLayout.findViewById(R.id.third_fastlogin_qq).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.startDialog("登录中");
				GetTokenPage.GetQQToken(mContext, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						Log.i("ceshi ", "statusCode  ==  " + statusCode + "  resultStr  ==  " + resultStr);
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");
								MyApplication.login_status = false;
								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
								finish();
							} else
							{
								contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
							ToastUtil.showToast(resultStr);
						}

					}

				});
			}
		});
		fastloginLayout.findViewById(R.id.third_fastlogin_sina).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.startDialog("登录中");
				GetTokenPage.GetSinaWeiboToken(mContext, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");
								MyApplication.login_status = false;
								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
								finish();
							} else
							{
								contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
								Log.i(TAG, "返回的 信息  ==  "+JsonUtil.JsonMSG);
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
							ToastUtil.showToast(resultStr);
						}
					}

				});
			}
		});
		fastloginLayout.findViewById(R.id.third_fastlogin_weixin).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View view)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.startDialog("登录中");
				GetTokenPage.GetWechatToken(mContext, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE)
						{
							int code = JsonUtil.getJsonStatus(resultStr);
							if (code == HttpCode.serverCode.DATA_Success_CODE)
							{
								UserInfor mUserInfor = JsonUtil.getUserInforFromString(resultStr);
								MyApplication.loginUserInfor = mUserInfor;
								if (MyApplication.getInstance().getmMainFragment() != null)
								{
									MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
								}
								if (MyApplication.getInstance().getActivityHandler() != null)
								{
									Message msg = new Message();
									msg.arg1 = 1002;
									MyApplication.getInstance().getActivityHandler().sendMessage(msg);
								}
								String str = JsonHelperUtil.toJSON(mUserInfor).toString();
								SharedPreManager.saveUserInfor(str);
								ToastUtil.showToast("登录成功.");
								MyApplication.login_status = false;
								setResult(AppConfigUtil.LOADING_LOGIN_BACK);
								contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
								finish();
							} else
							{
								contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							contentLayout.setBackgroundColor(getResources().getColor(R.color.text_color_ff));
							ToastUtil.showToast(resultStr);
						}
					}

				});
			}
		});
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	private void sendmyBroadcast()
	{
		Intent intent = new Intent(); // Itent就是我们要发送的内容
		intent.setAction(MyApplication.LOGIN_ACTION_REFRESH); // 设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
		sendBroadcast(intent); // 发送广播
	}

	private String forgetPwCode;
	private String forgetPwPhone;

	/**
	 * 忘记密码
	 */
	private void addForGotPassword(Boolean isback)
	{
		contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
		forgetPwCode = null;
		LinearLayout forgotpwLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_forgotpw_layout, null);
		addList.add(forgotpwLayout);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(forgotpwLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(forgotpwLayout, contentLayout.getChildAt(0));
		}

		final EditText inputphone = (EditText) forgotpwLayout.findViewById(R.id.forgotpw_input_account);
		final EditText inputcode = (EditText) forgotpwLayout.findViewById(R.id.forgotpw_input_verificationcode);
		final Button mGetCode = (Button) forgotpwLayout.findViewById(R.id.forgotpw_getcode);
		mGetCode.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				forgetPwPhone = inputphone.getText().toString();
				StringBuffer sb = new StringBuffer();
				if (!ValidateUtil.isCellphone(forgetPwPhone, sb))
				{
					ToastUtil.showToast(sb.toString());
					return;
				}
				mLoadingDialog.startDialog(loadingText);
				UserHandler.getForgetPwgetCode(forgetPwPhone, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode)
						{
							int status = JsonUtil.getForgetPWcode(resultStr);
							if (HttpCode.serverCode.DATA_Success_CODE == status)
							{
								forgetPwCode = JsonUtil.JsonMSG;
								TimeCount timeCount = new TimeCount(60000, 1000, mGetCode);
								if (mGetCode.isClickable())
								{
									timeCount.start();
								}
								ToastUtil.showToast("短信发送成功！");
							} else
							{
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}
						} else
						{
							ToastUtil.showToast(resultStr);
						}

					}
				});
			}
		});

		forgotpwLayout.findViewById(R.id.forgotpw_return).setOnClickListener(this);
		forgotpwLayout.findViewById(R.id.forgotpw_next).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
				if (forgetPwCode == null)
				{
					ToastUtil.showToast("请先获取验证码");
					return;
				}
				final String inputCode = inputcode.getText().toString();
				final String inputphonestr = inputphone.getText().toString();
				if (inputCode.trim().length() > 0 && inputphonestr.trim().length() > 0)
				{
					if (forgetPwPhone.equals(inputphonestr))
					{
						if (forgetPwCode != null && forgetPwCode.equals(inputCode))
						{
							addFindPassWord(false);
						} else
						{
							ToastUtil.showToast("验证码错误！");
						}
					} else
					{
						ToastUtil.showToast("手机号码更换，请重新获取验证");
					}
				} else
				{
					ToastUtil.showToast("手机号/验证码不能为空！");
				}

			}
		});
		contentLayout.addView(forgotpwLayout);
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	/**
	 * 找回密码
	 */
	private void addFindPassWord(Boolean isback)
	{
		contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
		LinearLayout findpwLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_findpw_layout, null);
		addList.add(findpwLayout);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(findpwLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(findpwLayout, contentLayout.getChildAt(0));
		}

		final EditText inputnewpw = (EditText) findpwLayout.findViewById(R.id.findtpw_input_newpw);
		final EditText inputtruenewpw = (EditText) findpwLayout.findViewById(R.id.findpw_input_truenewpw);

		findpwLayout.findViewById(R.id.findpw_return).setOnClickListener(this);
		findpwLayout.findViewById(R.id.findpw_commit).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				String oldpw = inputnewpw.getText().toString();
				String newpw = inputtruenewpw.getText().toString();
				StringBuffer sb = new StringBuffer();
				if (!ValidateUtil.findpwVaildate(oldpw, newpw, sb))
				{
					ToastUtil.showToast(sb.toString());
					return;
				}
				mLoadingDialog.startDialog(loadingText);
				UserHandler.findPw(forgetPwPhone, newpw, forgetPwCode, new HttpRequestCallback()
				{

					@Override
					public void onPostExecute(int statusCode, String resultStr)
					{
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (HttpCode.HTTP_Request_Success_CODE == statusCode)
						{
							int status = JsonUtil.getJsonStatus(resultStr);
							if (status == HttpCode.serverCode.DATA_Success_CODE)
							{
								ToastUtil.showToast("密码重置成功！");
								addFastLogin(false);
							} else
							{
								ToastUtil.showToast(JsonUtil.JsonMSG);
							}

						} else
						{
							ToastUtil.showToast(resultStr);
						}

					}
				});

			}

		});
		contentLayout.addView(findpwLayout);
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	private String chooseBrithday = "";;

	/**
	 * 编辑生日
	 */
	private void addEditBrithday()
	{
		contentLayout.removeAllViewsInLayout();
		final LinearLayout brithdayLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_edit_birthdaylayout, null);
		brithdayLayout.findViewById(R.id.brithday_return).setOnClickListener(this);
		View timeView = (View) brithdayLayout.findViewById(R.id.brithday_timeview);
		Calendar calendar = Calendar.getInstance();
		ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);
		WheelMain wheelMain = new WheelMain(timeView);
		wheelMain.screenheight = screenInfo.getWidth();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String[] brithday = this.getIntent().getStringExtra(DialogTypeUtil.DialogContent).split("-");
		if (brithday != null && brithday.length > 2)
		{
			year = Integer.parseInt(brithday[0]);
			month = Integer.parseInt(brithday[1]) - 1;
			day = Integer.parseInt(brithday[2]);
		}
		wheelMain.initDateTimePicker(year, month, day);
		chooseBrithday = year + "-" + month + "-" + day;
		setBrithdayRonud(year, brithdayLayout);
		wheelMain.setOnWheelViewChangedListener(new OnWheelViewChangedListener()
		{

			@Override
			public void onChange(int year, int mouth, int day, int hour, int min)
			{
				// TODO Auto-generated method stub
				chooseBrithday = year + "-" + mouth + "-" + day;
				setBrithdayRonud(year, brithdayLayout);
			}
		});
		brithdayLayout.findViewById(R.id.brithday_commit).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				setToServerBrithday(chooseBrithday);
			}
		});
		contentLayout.addView(brithdayLayout);
	}

	private void setBrithdayRonud(int year, LinearLayout brithdayLayout)
	{
		if (year < 2000)
		{
			switch (year % 1900 / 10)
			{
			case 5:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_1);
				break;
			case 6:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_2);
				break;
			case 7:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_3);
				break;
			case 8:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_4);
				break;
			case 9:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_5);
				break;
			}
		} else
		{
			switch (year % 2000 / 10)
			{
			case 0:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_6);
				break;
			case 1:
				setCheckedButtonBg(brithdayLayout, R.id.brithday_7);
				break;
			}
		}
	}

	private void setToServerBrithday(final String day)
	{
		mLoadingDialog.startDialog(loadingText);
		UserHandler.setUserNameSexBirth(null, -1, day, null, new HttpRequestCallback()
		{

			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				// TODO Auto-generated method stub
				mLoadingDialog.cancelDialog();
				int code = JsonUtil.getJsonStatus(resultStr);
				if (HttpCode.serverCode.DATA_Success_CODE == code)
				{
					ToastUtil.showToast("修改成功");
					Intent intent = new Intent();
					intent.putExtra(DialogTypeUtil.DialogType, day);
					setResult(DialogTypeUtil.UserDialogType.USER_EDIT_BIRTHDAY, intent);
					finish();
				} else
				{
					ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
				}
			}
		});
	}

	/**
	 * 点击radiobutton改变背景,选择年纪
	 * 
	 * @param layout
	 * @param id
	 */
	private void setCheckedButtonBg(LinearLayout layout, int id)
	{
		RadioButton checkButton = (RadioButton) layout.findViewById(id);
		checkButton.setChecked(true);
	}

	private int sexStr;
	private String personStr;

	/**
	 * 添加个性设置
	 */
	private void addPersonalitySetting(Boolean isback)
	{
		sexStr = 3;
		personStr = "";
		final LinearLayout personalityLayout = (LinearLayout) View.inflate(mContext, R.layout.dialog_personalitysetting_layout, null);
		addList.add(personalityLayout);
		if (isback)
		{
			mPageChangeAnimation.setanimoBack(personalityLayout, contentLayout.getChildAt(0));
		} else
		{
			mPageChangeAnimation.setanimoinorout(personalityLayout, contentLayout.getChildAt(0));
		}

		personalityLayout.findViewById(R.id.personality_return).setOnClickListener(this);
		RadioGroup gp = (RadioGroup) personalityLayout.findViewById(R.id.person_radiogroup);
		gp.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1)
			{
				// TODO Auto-generated method stub
				switch (arg1)
				{
				case R.id.personrbtn_1:
					personStr = "1950-01-01";
					break;
				case R.id.personrbtn_2:
					personStr = "1960-01-01";
					break;
				case R.id.personrbtn_3:
					personStr = "1970-01-01";
					break;
				case R.id.personrbtn_4:
					personStr = "1980-01-01";
					break;
				case R.id.personrbtn_5:
					personStr = "1990-01-01";
					break;
				case R.id.personrbtn_6:
					personStr = "2000-01-01";
					break;
				case R.id.personrbtn_7:
					personStr = "2010-01-01";
					break;
				default:
					break;
				}
			}
		});

		RadioGroup gpSex = (RadioGroup) personalityLayout.findViewById(R.id.person_radiogroup_sex);
		gpSex.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup radiogroup, int i)
			{
				// TODO Auto-generated method stub
				switch (i)
				{
				case R.id.personrbtn_man:
					sexStr = 1;
					onUpdate();
					break;
				case R.id.personrbtn_woman:
					sexStr = 2;
					onUpdate();
					break;

				default:
					break;
				}
			}
		});
		contentLayout.addView(personalityLayout);
		if (contentLayout.getChildCount() > 1)
		{
			contentLayout.removeViewAt(0);
		}
	}

	/**
	 * 修改个性化设置
	 */

	private void onUpdate()
	{
		if (personStr.equals(""))
		{
			return;
		}
		UserHandler.setUserNameSexBirth(null, sexStr, personStr, null, new HttpRequestCallback()
		{

			@Override
			public void onPostExecute(int statusCode, String resultStr)
			{
				// TODO Auto-generated method stub
				// 跳转到登录
				addFastLogin(false);
			}
		});
	}

	/**
	 * 编辑性别
	 */
	private void addEditSex()
	{
		contentLayout.removeAllViewsInLayout();
		final LinearLayout SexLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_editsex_layout, null);
		SexLayout.findViewById(R.id.sex_return).setOnClickListener(this);
		RadioGroup gpSex = (RadioGroup) SexLayout.findViewById(R.id.sex_radiogroup_sex);
		String sex = this.getIntent().getStringExtra(DialogTypeUtil.DialogContent);
		if (sex.trim().equals("男"))
		{
			RadioButton man = (RadioButton) gpSex.findViewById(R.id.sex_man);
			man.setChecked(true);
		} else
		{
			RadioButton woman = (RadioButton) gpSex.findViewById(R.id.sex_woman);
			woman.setChecked(true);
		}
		gpSex.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(RadioGroup radiogroup, int i)
			{
				// TODO Auto-generated method stub
				switch (radiogroup.getCheckedRadioButtonId())
				{
				case R.id.sex_man:// 男 int为 1
					mLoadingDialog.startDialog(loadingText);
					UserHandler.setUserNameSexBirth(null, 1, null, null, new HttpRequestCallback()
					{

						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
							{
								ToastUtil.showToast("修改成功");
								Intent intent = new Intent();
								intent.putExtra(DialogTypeUtil.DialogType, "男");
								setResult(DialogTypeUtil.UserDialogType.USER_EDIT_SEX, intent);
								finish();
							} else
							{
								ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
							}

						}
					});
					break;
				case R.id.sex_woman:// 女 int为 2
					mLoadingDialog.startDialog(loadingText);
					UserHandler.setUserNameSexBirth(null, 2, null, null, new HttpRequestCallback()
					{

						@Override
						public void onPostExecute(int statusCode, String resultStr)
						{
							// TODO Auto-generated method stub
							mLoadingDialog.cancelDialog();
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil.getJsonStatus(resultStr))
							{
								ToastUtil.showToast("修改成功");
								Intent intent = new Intent();
								intent.putExtra(DialogTypeUtil.DialogType, "女");
								setResult(DialogTypeUtil.UserDialogType.USER_EDIT_SEX, intent);
								finish();
							} else
							{
								ErrorStatusUtil.seachServerStatus(JsonUtil.status, JsonUtil.JsonMSG);
							}

						}
					});
					break;
				default:
					break;
				}
			}
		});
		contentLayout.addView(SexLayout);
	}

	private int Image_type = -1;

	/**
	 * 设置头像
	 */
	private void setIcon(int type)
	{
		Image_type = type;
		if (pictureSetting == null)
		{
			pictureSetting = new PictureSetting();
		}
		String nowTime = String.valueOf(System.currentTimeMillis());
		sdcardTempFile = new File(FolderUtil.createImageCacheFile(), nowTime + AppConfigUtil.User.USER_ICON);
		if (isUnload)
		{//
			if (type == 0)
			{
				startActivityForResult(pictureSetting.getPhone(sdcardTempFile), CAMERA_REQUEST_CODE);
			} else
			{
				startActivityForResult(pictureSetting.getPhoto(), IMAGE_REQUEST_CODE);
			}
		} else
		{
			if (type == 0)
			{
				startActivityForResult(pictureSetting.getPhone(sdcardTempFile), RESULT_REQUEST_CODE);
			} else
			{
				startActivityForResult(pictureSetting.getPhoto(), RESULT_REQUEST_CODE);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_dialog, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode)
		{
		case IMAGE_REQUEST_CODE:
			if (intent != null && intent.getData() != null)
			{
				Log.d("data", "1");
				startActivityForResult(pictureSetting.startPhotoZoom(intent.getData()), RESULT_REQUEST_CODE);
			} else
			{
				finish();
			}
			break;
		case CAMERA_REQUEST_CODE:
			if (FolderUtil.isMediaMounted())
			{
				Intent intentpic = pictureSetting.startPhotoZoom(Uri.fromFile(sdcardTempFile));
				if (intentpic.getData() != null)
				{
					startActivityForResult(intentpic, RESULT_REQUEST_CODE);
				} else
				{
					finish();
				}
			} else
			{
				finish();
				ToastUtil.showToast("未找到存储卡，无法存储照片！");
			}

			break;
		case RESULT_REQUEST_CODE:

			// 得到图片路径返回
			Log.d("path", "RESULT_REQUEST_CODE");
			// 上传图片
			String Filepath = sdcardTempFile.getPath();
			Log.d("path0", Filepath);
			if (isUnload)
			{// 头像
				if (Filepath != null && Filepath.length() > 0)
				{
					Log.d("path0", "isUnload" + Filepath);
					Boolean isHavepic = pictureSetting.setImageToView(intent, sdcardTempFile);
					if (isHavepic)
					{
						uploadImg();
					}

				}

			} else
			{// 评论
				if (Image_type == 0)
				{
					pictureSetting.setImageCamerBitmap(sdcardTempFile);
				} else
				{
					if (intent != null && intent.getData() != null)
					{
						Uri uri = intent.getData();
						pictureSetting.setImageBitmap(PictureSetting.getRealFilePath(mContext, uri), sdcardTempFile);
					}
				}
				Log.d("data", "c");
				Log.d("path0", Filepath);
				Intent mRetrue = new Intent();
				mRetrue.putExtra("path", Filepath);
				setResult(AppConfigUtil.RESULT_LOCAT_CODE, mRetrue);
			}

			finish();

			break;
		}
	}

	/**
	 * 上传图片
	 */
	private void uploadImg()
	{
		Map<String, String> param = new HashMap<String, String>();
		UploadImgUtil.getInstance().setOnUploadProcessListener(this);
		if (isUserPictrue)
		{
			param.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			param.put("uploadType", "2");// 用户头像
			param.put("modelType", "2");// 用户头像
			Log.d(TAG, "sdcardTempFile.getPath()" + sdcardTempFile.getPath());
			UploadImgUtil.getInstance().uploadFile(sdcardTempFile.getPath(), AppConfigUtil.UploadImage.KEY, HttpUrlList.UserUrl.User_UploadUserIcon, param);
		} else
		{
			param.put(HttpUrlList.HTTP_USER_ID, MyApplication.loginUserInfor.getUserId());
			param.put("teamUserId", teamUserId);
			param.put("uploadType", "3");// 团体图片
			Log.d(TAG, "sdcardTempFile.getPath()" + sdcardTempFile.getPath());
			UploadImgUtil.getInstance().uploadFile(sdcardTempFile.getPath(), AppConfigUtil.UploadImage.KEY, HttpUrlList.UserUrl.USER_UPLOADGROUPPIC, param);
		}

	}

	@Override
	public void onClick(View view)
	{
		// TODO Auto-generated method stub
		switch (view.getId())
		{
		case R.id.login_register:
			isLoadingGo = false;
			addRegister(false);
			break;
		case R.id.login_login:
			isLoadingGo = false;
			addThirdFastLogin(false);
			break;
		case R.id.fastlogin_forgotpw:
			addForGotPassword(false);
			break;
		case R.id.forgotpw_return:
			addFastLogin(true);
			break;
		case R.id.findpw_return:
			addForGotPassword(true);
			break;

		// case R.id.fastlogin_return:
		// if (IsThFastloginGo) {
		// addThirdFastLogin(true);
		// } else {
		// addLogin(true);
		// }
		// break;
		case R.id.register_return:
			if (isLoadingGo)
			{
				finish();
			} else
			{
				if (IsloginGo)
				{
					// addLogin(true);
					addThirdFastLogin(true);
				} else
				{
					addFastLogin(true);
				}
			}
			break;
		case R.id.usericon_takepic:// 拍照

			setIcon(0);
			break;
		case R.id.third_fastlogin_creat_account:// 创建账号
			isLoadingGo = false;
			IsloginGo = true;
			addRegister(false);
			break;
		case R.id.usericon_choosepic:// 相册
			setIcon(1);
			break;
		case R.id.third_fastlogin_account:// 登录
			addFastLogin(false);
			break;
		case R.id.third_fastlogin_return:// 返回
			// if (isLoadingGo) {
			contentLayout.setBackgroundColor(getResources().getColor(R.color.untransparent));
			finish();
			// } else {
			// addLogin(true);
			// }
			break;
		case R.id.fastlogin_register:// 注册
			isLoadingGo = false;
			addRegister(false);
			break;
		default:
			finish();
			break;
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message)
	{
		// TODO Auto-generated method stub
		// 上传头像完成

		Looper.prepare();
		Log.d(TAG, message);
		int code = JsonUtil.getJsonStatus(message);
		if (code == HttpCode.serverCode.DATA_Success_CODE)
		{
			if (isUserPictrue)
			{
				ToastUtil.showToast("头像上传成功！");
				// 更新用户头像并保存
				MyApplication.loginUserInfor.setUserHeadImgUrl(JsonUtil.JsonMSG);
				String str = JsonHelperUtil.toJSON(MyApplication.loginUserInfor).toString();
				SharedPreManager.saveUserInfor(str);
				// 通知更新头像
				MyApplication.getInstance().getmUserHandler().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
				MyApplication.getInstance().getmMainFragment().sendEmptyMessage(MyApplication.HANDLER_USER_CODE);
			} else
			{
				ToastUtil.showToast("团体图片更新成功！");
				Log.d(TAG, JsonUtil.JsonMSG);
				MyApplication.loginUserInfor.setUserGroupPictrueUrl(JsonUtil.JsonMSG);
			}
		} else
		{
			ToastUtil.showToast(JsonUtil.JsonMSG);
		}
		Looper.loop();

	}

	@Override
	public void onUploadProcess(int uploadSize)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initUpload(int fileSize)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * 计时
	 * 
	 * @author lingningkang
	 * 
	 */
	class TimeCount extends CountDownTimer
	{
		private Button mText;

		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);
		}

		public TimeCount(long millisInFuture, long countDownInterval, Button mText)
		{
			super(millisInFuture, countDownInterval);
			this.mText = mText;
		}

		public void onFinish()
		{
			mText.setText("重新发送");
			mText.setClickable(true);
		}

		public void onTick(long millisUntilFinished)
		{
			mText.setClickable(false);
			mText.setText(millisUntilFinished / 1000 + "秒");
		}
	}
}
