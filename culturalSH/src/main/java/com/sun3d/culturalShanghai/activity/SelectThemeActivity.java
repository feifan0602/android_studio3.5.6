package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.AppConfigUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.FirstLoadAdapter;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.view.FastBlur;

/**
 * 选择你喜欢的服务
 * 
 * @author wenff
 * 
 */
public class SelectThemeActivity extends Activity {
	private List<UserBehaviorInfo> typeList, selectTypeList;
	private FirstLoadAdapter mTypeAdapter;
	private GridView mTypeGridView;
	private LoadingDialog mLoadingDialog;
	private boolean isAllSelect = false;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_load_type);
		MyApplication.getInstance().addActivitys(this);
		sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
		initView();
		initData();
		// onLogin();
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
				Editor editor = sharedPreferences.edit();
				editor.putBoolean("isFirstRun", false);
				editor.commit();
				Intent intent = new Intent(SelectThemeActivity.this,
						MainFragmentActivity.class);
				startActivity(intent);
				finish();
			}
			break;

		default:
			break;
		}
	}

	private void initView() {
		findViewById(R.id.first_start).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						onStartActivity();
					}
				});
		mTypeGridView = (GridView) findViewById(R.id.first_type_gridview);
		mLoadingDialog = new LoadingDialog(this);
		findViewById(R.id.love_select).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						onAllSelect();
					}
				});
	}

	private void onAllSelect() {
		// if (!isAllSelect) {
		// onSelectView();
		selectTypeList.clear();
		for (int i = 0; i < typeList.size(); i++) {
			typeList.get(i).setSelect(true);
		}
		selectTypeList.addAll(typeList);
		mTypeAdapter.setList(typeList);
		mTypeAdapter.notifyDataSetChanged();
		// tvSelect.setText("取消");
		// }
		// isAllSelect = true;
	}

	private void initData() {
		typeList = new ArrayList<UserBehaviorInfo>();
		selectTypeList = new ArrayList<UserBehaviorInfo>();
		mTypeAdapter = new FirstLoadAdapter(this, typeList, false);
		mTypeGridView.setAdapter(mTypeAdapter);
		mTypeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UserBehaviorInfo info = typeList.get(arg2);
				if (info.isSelect()) {
					info.setSelect(false);
					selectTypeList.remove(info);
					TextView tv = (TextView) arg1.findViewById(R.id.first_age);
					tv.setBackgroundResource(R.drawable.home_myborder);
					tv.setTextColor(getResources().getColor(
							R.color.text_color_72));
					arg1.findViewById(R.id.first_select).setVisibility(
							View.GONE);

				} else {
					selectTypeList.add(info);
					info.setSelect(true);
					TextView tv = (TextView) arg1.findViewById(R.id.first_age);
					tv.setBackgroundResource(R.drawable.home_myborder_press);
					tv.setTextColor(getResources().getColor(
							R.color.text_color_ff));
					arg1.findViewById(R.id.first_select).setVisibility(
							View.GONE);
				}
			}
		});
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
						Editor editor = sharedPreferences.edit();
						editor.putBoolean("isFirstRun", false);
						editor.commit();
						// 跳转到首页
						MyApplication.change_fragment = 0;
						finish();
					}
				});
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
			ToastUtil.showToast("请至少选择一个主题标签");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoadingDialog.cancelDialog();
	}
}
