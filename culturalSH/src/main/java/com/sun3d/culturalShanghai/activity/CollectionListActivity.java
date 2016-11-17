package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.adapter.CollectionGridAdapter;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.CollectionInfor;
import com.sun3d.culturalShanghai.windows.CollectionListWindows;
import com.sun3d.culturalShanghai.windows.CollectionListWindows.ListOnItemClickListener;
import com.sun3d.culturalShanghai.windows.CollectionListWindows.PopupWindowShowLisener;
import com.umeng.analytics.MobclickAgent;
/**
 * 藏品展示 
 * @author wenff
 *
 */
public class CollectionListActivity extends Activity implements OnClickListener,
		PopupWindowShowLisener, ListOnItemClickListener {
	private String TAG = "CollectionListActivity";
	private TextView chooseTime;
	private TextView chooseType;
	private GridView collectGridview;
	private Context mContext;
	private RelativeLayout chooseTimeLayout;
	private RelativeLayout chooseTypeLayout;
	private CollectionListWindows mCollectionListWindows;
	private LinearLayout mtopLayout;
	private Boolean isCheckType = true;
	private String venueId = "123";
	private String venueName = "123";
	private List<String> CollectionTimelist;
	private List<String> CollectionTypelist;
	private List<CollectionInfor> Collectionlist;
	private CollectionGridAdapter mCollectionGridAdapter;
	private LoadingHandler mLoadingHandler;

	private final String mPageName = "CollectionListActivity";

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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_list);
		mContext = this;
		MyApplication.getInstance().addActivitys(this);
		venueId = this.getIntent().getExtras().getString("Id");
		venueName = this.getIntent().getExtras().getString("title");
		init();
	}

	private void init() {
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		CollectionTimelist = new ArrayList<String>();
		CollectionTypelist = new ArrayList<String>();
		Collectionlist = new ArrayList<CollectionInfor>();
		setTitle();
		mCollectionListWindows = new CollectionListWindows(mContext);
		mCollectionListWindows.setListOnItemClickListener(this);
		mtopLayout = (LinearLayout) findViewById(R.id.collect_top_layout);
		chooseTime = (TextView) findViewById(R.id.collect_time);
		chooseType = (TextView) findViewById(R.id.collect_type);
		chooseTimeLayout = (RelativeLayout) findViewById(R.id.collect_time_layout);
		chooseTypeLayout = (RelativeLayout) findViewById(R.id.collect_type_layout);
		collectGridview = (GridView) findViewById(R.id.collect_gridview);
		mCollectionGridAdapter = new CollectionGridAdapter(mContext, Collectionlist);
		collectGridview.setAdapter(mCollectionGridAdapter);

		chooseTimeLayout.setOnClickListener(this);
		chooseTypeLayout.setOnClickListener(this);
		mCollectionListWindows.setonPopupWindowShowLisener(this);
		collectGridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent itent = new Intent(mContext, CollcetionDetailsActivity.class);
				itent.putExtra("Id", Collectionlist.get(arg2).getId());
				startActivity(itent);
			}
		});
		addData();
	}

	private void addData() {
		for (int i = 0; i < 10; i++) {
			Collectionlist.add(new CollectionInfor());
		}
		addServerData(0);
	}

	private void addServerData(int page) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("venueId", venueId);
		params.put("pageIndex", String.valueOf(page));
		params.put("pageNum", HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(HttpUrlList.Collection.COLLECTION_ALL_GETURL, params,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							Collectionlist.clear();
							JsonUtil.getCollectionInfo(CollectionTimelist, CollectionTypelist,
									Collectionlist, resultStr);
							mCollectionGridAdapter.setList(Collectionlist);
							mCollectionGridAdapter.notifyDataSetChanged();
							mLoadingHandler.stopLoading();
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText("藏品展示");
		title.setVisibility(View.VISIBLE);
	}

	/**
	 * 改变文本颜色
	 */
	private void setTextViewIcon(TextView TV) {
		if (TV.getCurrentTextColor() == getResources().getColor(R.color.app_text_color)) {
			TV.setTextColor(getResources().getColor(R.color.orange_color));
			addImgToTextView(TV, R.drawable.sh_icon_collect_down_red);
		} else {
			TV.setTextColor(getResources().getColor(R.color.app_text_color));
			addImgToTextView(TV, R.drawable.sh_icon_collect_up_gray);
		}
	}

	/**
	 * 选择文本
	 */
	private void selectTextView(TextView TV) {
		if (chooseTime == TV) {
			setTextViewIcon(chooseTime);
			chooseType.setTextColor(getResources().getColor(R.color.app_text_color));
			addImgToTextView(chooseType, R.drawable.sh_icon_collect_up_gray);
		} else {
			setTextViewIcon(chooseType);
			chooseTime.setTextColor(getResources().getColor(R.color.app_text_color));
			addImgToTextView(chooseTime, R.drawable.sh_icon_collect_up_gray);
		}

	}

	private void addImgToTextView(TextView TV, int imgid) {
		Drawable drawable = getResources().getDrawable(imgid);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
		TV.setCompoundDrawables(null, null, drawable, null);// 画在右边
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection_list, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.collect_time_layout:
			selectTextView(chooseTime);
			isCheckType = false;
			mCollectionListWindows.showList(mtopLayout, CollectionListWindows.CollectionTime,
					CollectionTimelist);
			break;
		case R.id.collect_type_layout:
			selectTextView(chooseType);
			isCheckType = true;
			mCollectionListWindows.showList(mtopLayout, CollectionListWindows.CollectionType,
					CollectionTypelist);
			break;
		default:
			finish();
			break;
		}

	}

	@Override
	public void isShow() {
		// TODO Auto-generated method stub
		if (isCheckType) {
			addImgToTextView(chooseType, R.drawable.sh_icon_collect_down_red);
		} else {
			addImgToTextView(chooseTime, R.drawable.sh_icon_collect_down_red);
		}

	}

	@Override
	public void isDismiss() {
		// TODO Auto-generated method stub
		if (isCheckType) {
			addImgToTextView(chooseType, R.drawable.sh_icon_collect_up_gray);
			chooseType.setTextColor(getResources().getColor(R.color.app_text_color));
		} else {
			addImgToTextView(chooseTime, R.drawable.sh_icon_collect_up_gray);
			chooseTime.setTextColor(getResources().getColor(R.color.app_text_color));
		}
	}

	@Override
	public void onItemClick(String resultName, int type) {
		// TODO Auto-generated method stub
		switch (type) {
		case CollectionListWindows.CollectionTime:
			filterCollectionList(0, "antiqueDynasty", resultName,
					HttpUrlList.Collection.COLLECTION_FILTER_GETURL);
			mLoadingHandler.startLoading();
			break;
		case CollectionListWindows.CollectionType:
			filterCollectionList(0, "antiqueTypeName", resultName,
					HttpUrlList.Collection.COLLECTION_FILTER_GETURL_NAME);
			mLoadingHandler.startLoading();
			break;
		default:
			break;
		}

	}

	private void filterCollectionList(int page, String nameKey, String name, String url) {
		Map<String, String> params = new HashMap<String, String>();
		if (!name.equals("查看全部")) {
			params.put(nameKey, name);
		}
		params.put("venueId", venueId);
		params.put("pageIndex", String.valueOf(page));
		params.put("pageNum", HttpUrlList.HTTP_NUM + "");
		MyHttpRequest.onHttpPostParams(url, params, new HttpRequestCallback() {

			@Override
			public void onPostExecute(int statusCode, String resultStr) {
				// TODO Auto-generated method stub
				Log.d(TAG, resultStr);
				if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
					Collectionlist.clear();
					JsonUtil.getCollectionInfo(null, null, Collectionlist, resultStr);
					mCollectionGridAdapter.setList(Collectionlist);
					mCollectionGridAdapter.notifyDataSetChanged();
					mLoadingHandler.stopLoading();
				} else {
					mLoadingHandler.showErrorText(resultStr);
				}
			}
		});
	}

}
