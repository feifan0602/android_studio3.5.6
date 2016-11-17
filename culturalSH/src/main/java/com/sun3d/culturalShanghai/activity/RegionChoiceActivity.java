package com.sun3d.culturalShanghai.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ErrorStatusUtil;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.dialog.DialogTypeUtil;
import com.sun3d.culturalShanghai.dialog.LoadingDialog;
import com.sun3d.culturalShanghai.handler.UserHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.manager.SharedPreManager;
import com.sun3d.culturalShanghai.object.SearchInfo;
import com.sun3d.culturalShanghai.object.SearchListInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegionChoiceActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private ListView regionList;
	private SearchListInfo searchListInfo;
	private Context mContext;
	private String loadingText = "请求中";
	private LoadingDialog mLoadingDialog;
	private int index = 0;
	private AddresAdapter addres;
	private String region;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_region_choice);
		MyApplication.getInstance().addActivitys(this);
		searchListInfo = MyApplication.getInstance().getSearchListInfo();
		mLoadingDialog = new LoadingDialog(mContext);
		mContext = this;
		initView();
		initData();

	}

	private void initData() {
		// TODO Auto-generated method stub
		region = getIntent().getExtras().getString(DialogTypeUtil.DialogType);
		if (null == searchListInfo || searchListInfo.getAddresList().size() < 1) {
			getData();
			return;
		} else {
			List<SearchInfo> listInfo = searchListInfo.getAddresList();
			for (int i = 0; i < listInfo.size(); i++) {
				searchListInfo.getAddresList().get(i).setAddSeleced(false);
				if (region.indexOf(listInfo.get(i).getTagName()) != -1) {
					index = i;
					searchListInfo.getAddresList().get(i).setAddSeleced(true);
				}
			}
			addres = new AddresAdapter();
			regionList.setAdapter(addres);
			regionList.setOnItemClickListener(this);
		}
		if (null != region && region.length() > 0) {
		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		RelativeLayout aountTitle = (RelativeLayout) findViewById(R.id.title);
		aountTitle.findViewById(R.id.title_left).setOnClickListener(this);
		aountTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView mTitle = (TextView) aountTitle
				.findViewById(R.id.title_content);
		mTitle.setText("地区");

		regionList = (ListView) findViewById(R.id.region_list);

	}

	/**
	 * 获取标签数据
	 */
	private void getData() {
		// 没有获取到标签图
		MyApplication.loginUserInfor = SharedPreManager.readUserInfor();
		Map<String, String> params = new HashMap<String, String>();
		MyHttpRequest.onHttpPostParams(
				HttpUrlList.SearchUrl.HTTP_PUPO_SEARCH_URL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.d("statusCode", statusCode + "这个是地区的数据" + resultStr);
						SearchListInfo info = new SearchListInfo();
						info = JsonUtil.getRegionSearchListInfo(resultStr);
						MyApplication.getInstance().setSearchListInfo(info);
						searchListInfo = MyApplication.getInstance()
								.getSearchListInfo();

						handler.sendEmptyMessage(1);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				List<SearchInfo> listInfo = searchListInfo.getAddresList();
				for (int i = 0; i < listInfo.size(); i++) {
					searchListInfo.getAddresList().get(i).setAddSeleced(false);
					if (region.indexOf(listInfo.get(i).getTagName()) != -1) {
						index = i;
						searchListInfo.getAddresList().get(i)
								.setAddSeleced(true);
					}
				}
				addres = new AddresAdapter();
				regionList.setAdapter(addres);
				regionList.setOnItemClickListener(RegionChoiceActivity.this);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.title_left:
			finish();
			break;

		default:
			break;
		}
	}

	class AddresAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchListInfo.getAddresList().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			// TODO Auto-generated method stub
			SearchInfo searchInfo = searchListInfo.getAddresList().get(arg0);
			view = view.inflate(mContext, R.layout.item_region_choice, null);
			TextView addName = (TextView) view.findViewById(R.id.add_name);
			addName.setTypeface(MyApplication.GetTypeFace());
			ImageView addImg = (ImageView) view.findViewById(R.id.add_img);
			addName.setText(searchInfo.getTagName());
			if (searchInfo.isAddSeleced()) {
				addImg.setVisibility(View.VISIBLE);
			}
			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		arg1.findViewById(R.id.add_img).setVisibility(View.VISIBLE);
		if (index != -1) {
			searchListInfo.getAddresList().get(arg2).setAddSeleced(true);
			searchListInfo.getAddresList().get(index).setAddSeleced(false);
			addres.notifyDataSetChanged();
		}

		final String region = searchListInfo.getAddresList().get(arg2)
				.getTagId()
				+ "," + searchListInfo.getAddresList().get(arg2).getTagName();
		final String showRegion = "上海市"
				+ searchListInfo.getAddresList().get(arg2).getTagName();
		Log.d("region", region);
		mLoadingDialog.startDialog(loadingText);
		UserHandler.setUserNameSexBirth(null, -1, null, region,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						mLoadingDialog.cancelDialog();
						if (statusCode == HttpCode.HTTP_Request_Success_CODE) {
							if (HttpCode.serverCode.DATA_Success_CODE == JsonUtil
									.getJsonStatus(resultStr)) {
								ToastUtil.showToast("修改成功");
								Intent intent = new Intent();
								intent.putExtra(DialogTypeUtil.DialogType,
										showRegion);
								Log.d("region1", region);
								setResult(
										DialogTypeUtil.UserDialogType.USER_EDIT_REGION,
										intent);
								finish();
							} else {
								ErrorStatusUtil.seachServerStatus(
										JsonUtil.status, JsonUtil.JsonMSG);
							}
						} else {
							ToastUtil.showToast(resultStr);
						}
					}
				});
	}

}
