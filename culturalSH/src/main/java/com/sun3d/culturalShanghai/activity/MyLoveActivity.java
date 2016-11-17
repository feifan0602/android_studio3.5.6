package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonHelperUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.adapter.FirstLoadAdapter;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.UserBehaviorInfo;
import com.sun3d.culturalShanghai.object.UserBehaviorType;
import com.sun3d.culturalShanghai.windows.LoadingTextShowPopWindow;

/**
 * 我喜欢的活动
 * 
 * @author wenff
 * 
 */
public class MyLoveActivity extends Activity implements OnClickListener {
	private GridView mGridView;
	public static List<UserBehaviorInfo> mList;
	private FirstLoadAdapter mAdapter;
	public static List<UserBehaviorInfo> mSelectList;
	private LoadingDialog mLoadingDialog;
	private boolean isAllSelect = false;
	private View myView;
	private boolean change_select = true;
	private Button love_ok_bt;
	private String TAG = "MyLoveActivity";
	// 保存标签信息
	private List<String> list_tagName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_love);
		MyApplication.getInstance().addActivitys(this);
		initView();
		initData();
		myhandler.sendEmptyMessageDelayed(1, 1000);

	}

	private Handler myhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			getData();
		}

	};
	private TextView tvSelect;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLoadingDialog.cancelDialog();
		LoadingTextShowPopWindow.dismissPop();
	}

	private void initView() {
		mLoadingDialog = new LoadingDialog(this);
		mGridView = (GridView) findViewById(R.id.love_gridview);
		findViewById(R.id.love_return).setOnClickListener(this);
		love_ok_bt = (Button) findViewById(R.id.love_ok);
		love_ok_bt.setTypeface(MyApplication.GetTypeFace());
		love_ok_bt.setOnClickListener(this);
		tvSelect = (TextView) findViewById(R.id.love_select);
		tvSelect.setTypeface(MyApplication.GetTypeFace());
		tvSelect.setOnClickListener(this);
		mList = new ArrayList<UserBehaviorInfo>();
		mSelectList = new ArrayList<UserBehaviorInfo>();
		mAdapter = new FirstLoadAdapter(this, mList, false);
		mGridView.setAdapter(mAdapter);
		mLoadingDialog.startDialog("请稍候");
		list_tagName = SharedPreManager.readTypeInfo();
		if (list_tagName != null) {
			Log.i(TAG, " List_Info   " + list_tagName.size());
		} else {
			Log.i(TAG, "没有保存成功");
		}
	}

	private void initData() {
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				UserBehaviorInfo info = mList.get(arg2);
				myView = arg1;
				if (info.isSelect()) {
					// 这是取消选择
					info.setSelect(false);
					mSelectList.remove(info);
					TextView tv = (TextView) arg1.findViewById(R.id.first_age);
					tv.setBackgroundResource(R.drawable.home_myborder);
					tv.setTextColor(getResources().getColor(
							R.color.text_color_72));
					arg1.findViewById(R.id.first_select).setVisibility(
							View.GONE);
				} else {
					// 这是确认选择
					info.setSelect(true);
					mSelectList.add(info);
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

	public void getData() {

		Map<String, String> params = new HashMap<String, String>();
		params.put(HttpUrlList.HTTP_USER_ID,
				MyApplication.loginUserInfor.getUserId());
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.LOOK_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							mList.addAll(JsonUtil.getTypeDataList(resultStr));
							changeData();
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

	private void changeData() {
		for (int i = 0; i < mList.size(); i++) {
			if (MyApplication.loginUserInfor == null
					|| MyApplication.loginUserInfor.getUserId() == null
					|| "".equals(MyApplication.loginUserInfor.getUserId())) {
				if (list_tagName == null || list_tagName.size() == 0) {
					if ("亲子演出培训DIY展览".indexOf(mList.get(i).getTagName()) != -1) {
						mList.get(i).setSelect(true);
					} else {
						mList.get(i).setSelect(false);
					}
				} else {
					mList.get(i).setSelect(false);
					for (int j = 0; j < list_tagName.size(); j++) {
						if (mList.get(i).getTagName()
								.equals(list_tagName.get(j))) {
							mList.get(i).setSelect(true);
						}
					}

				}

			}
			if (mList.get(i).isSelect()) {
				mSelectList.add(mList.get(i));
			}
		}
		mAdapter.notifyDataSetChanged();

	}

	private void addLoveData() {
		if (mSelectList.size() == 0) {
			ToastUtil.showToast("请至少选择一个主题标签");
			return;
		}
		if (!MyApplication.UserIsLogin) {
			// 这里为了 保存没有登陆时的标签的信息 设置 本地保存
			MyApplication.getInstance().setIsFromMyLove(true);
			MyApplication.getInstance().setSelectTypeList(mSelectList);
			MyApplication.getInstance().setPosition(0);
			JSONArray ja = new JSONArray();
			for (int i = 0; i < mSelectList.size(); i++) {
				JSONObject json = new JSONObject();
				try {
					json.put(i + "", mSelectList.get(i).getTagName());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ja.put(json);
			}
			SharedPreManager.clearTypeInfo();
			SharedPreManager.saveActivityType(ja.toString());
			startActivity(new Intent(MyLoveActivity.this,
					MainFragmentActivity.class));
			finish();
			return;
		}
		MyApplication.getInstance().setIsFromMyLove(false);
		String tagId = "";
		for (int i = 0; i < mSelectList.size(); i++) {
			tagId = tagId + mSelectList.get(i).getTagId() + ",";
		}
		tagId = tagId.substring(0, tagId.length() - 1);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("userSelectTag", tagId);
		mLoadingDialog.startDialog("请稍候");
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ADDTYPETAG, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							MyApplication.getInstance().setPosition(0);
							startActivity(new Intent(MyLoveActivity.this,
									MainFragmentActivity.class));
							finish();
						} else {
							ToastUtil.showToast("添加失败");
						}

					}
				});
	}

	/**
	 * 全部选中的按钮
	 */

	private void onAllSelect() {
		// if (!isAllSelect) {
		// onSelectView();
		mSelectList.clear();
		for (int i = 0; i < mList.size(); i++) {
			mList.get(i).setSelect(true);
		}
		mSelectList.addAll(mList);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
		// tvSelect.setText("取消");
		// }
		// isAllSelect = true;
		// else {
		// // mAdapter.isAllSelect(false, mSelectList.size());
		// mSelectList.clear();
		// tvSelect.setText("全选");
		// }
		// mAdapter.notifyDataSetChanged();
	}

	/**
	 * 全部选中的按钮
	 */

	private void offAllSelect() {
		// if (!isAllSelect) {
		// onSelectView();
		mSelectList.clear();
		for (int i = 0; i < mList.size(); i++) {
			mList.get(i).setSelect(false);
		}
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();

		// tvSelect.setText("取消");
		// }
		// isAllSelect = true;
		// else {
		// // mAdapter.isAllSelect(false, mSelectList.size());
		// mSelectList.clear();
		// tvSelect.setText("全选");
		// }
		// mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.love_return:
			finish();
			break;
		case R.id.love_ok:
			addLoveData();
			break;
		case R.id.love_select:
			if (change_select) {
				change_select = false;
				tvSelect.setText("取消全选");
				onAllSelect();
			} else {
				change_select = true;
				tvSelect.setText("全选");
				offAllSelect();
			}

			break;
		default:
			break;
		}
	}
}
