package com.sun3d.culturalShanghai.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.Util.WindowsUtil;
import com.sun3d.culturalShanghai.adapter.VenuePopuGridViewWindowAdapter;
import com.sun3d.culturalShanghai.adapter.VenuePopuListViewWindowAdapter;
import com.sun3d.culturalShanghai.adapter.VenuePopuWindowAdapter;
import com.sun3d.culturalShanghai.adapter.VenuePopuWindowAreaAdapter;
import com.sun3d.culturalShanghai.fragment.VenueFragment;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.object.VenueDetailInfor;
import com.sun3d.culturalShanghai.object.Wff_VenuePopuwindow;

/**
 * 这是场所上头的popwindow
 * 
 * @author wenff
 * 
 */
public class VenuePopuwindow {
	private PopupWindow popup;
	private View itemView;
	private PopupWindow popup_area;
	private View mView;
	private View mView_area;
	private ListView region_listview, status_listview, area_listview,
			sort_listview;
	private GridView classification_gridview;
	private ArrayList<Wff_VenuePopuwindow> list;
	private ArrayList<Wff_VenuePopuwindow> list_area;
	private ArrayList<Wff_VenuePopuwindow> list_area_z = new ArrayList<Wff_VenuePopuwindow>();
	private ArrayList<Wff_VenuePopuwindow> list_sort;
	private ArrayList<Wff_VenuePopuwindow> list_status;
	private VenuePopuWindowAdapter vpwa;
	private VenuePopuGridViewWindowAdapter vpgva;
	private LinearLayout classition_ll;
	private TextView classition_all;
	private Wff_VenuePopuwindow wvpw;
	private int mBgColor;
	private View view;

	public int getmBgColor() {
		return mBgColor;
	}

	public void setmBgColor(int mBgColor) {
		this.mBgColor = mBgColor;
	}

	private Context mContext;
	/**
	 * 条件数据
	 */
	private static VenuePopuwindow searchWindows;
	private VenueFragment vf;

	public static VenuePopuwindow getInstance(Context mContext, VenueFragment vf) {
		if (searchWindows == null) {
			searchWindows = new VenuePopuwindow(mContext, vf);

		}
		return searchWindows;
	}

	public VenuePopuwindow(Context mContext, VenueFragment vf) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.vf = vf;

	}

	/**
	 * 不同的Type 显示不同的布局
	 */
	public void showSearch(View view) {

		initView();
		/**
		 * 场馆分类
		 */
		initData();
		/**
		 * 区域选项
		 */
		initDataArea();
		/**
		 * 离我最近 热门程度
		 */
		initDataSort();
		/**
		 * 可预订 全部
		 */
		initDataStatus();
		popup.showAsDropDown(view);

	}

	private void initDataStatus() {
		list_status = new ArrayList<Wff_VenuePopuwindow>();
		list_status.add(new Wff_VenuePopuwindow("2", "可预订"));
		list_status.add(new Wff_VenuePopuwindow("1", "全部"));
		VenuePopuListViewWindowAdapter vpwa = new VenuePopuListViewWindowAdapter(
				list_status, mContext);
		status_listview.setAdapter(vpwa);
		status_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
						.getItemAtPosition(arg2);
				// clear();
				MyApplication.venueIsReserve = wvp.getTagId();
				MyApplication.venueIsReserve_Name = wvp.getTagName();
				if (popup != null) {
					popup.dismiss();
				}

				vf.onRefresh();

			}
		});
	}

	private void initDataSort() {
		list_sort = new ArrayList<Wff_VenuePopuwindow>();
		list_sort.add(new Wff_VenuePopuwindow("1", "热门程度"));
		list_sort.add(new Wff_VenuePopuwindow("2", "离我最近"));
		VenuePopuListViewWindowAdapter vpwa = new VenuePopuListViewWindowAdapter(
				list_sort, mContext);
		sort_listview.setAdapter(vpwa);
		sort_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
						.getItemAtPosition(arg2);
				// clear();
				MyApplication.sortType = wvp.getTagId();
				MyApplication.sortType_Name = wvp.getTagName();
				if (popup != null) {
					popup.dismiss();
				}

				vf.onRefresh();

			}
		});
	}

	private void initDataArea() {
		Map<String, String> mParams = new HashMap<String, String>();
		MyHttpRequest.onStartHttpGET(HttpUrlList.Venue.WFF_GETALLAREA, mParams,
				new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						Log.d("statusCode", statusCode + "--获取区域数据-"
								+ resultStr);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							try {
								list_area = new ArrayList<Wff_VenuePopuwindow>();
								JSONObject json = new JSONObject(resultStr);
								String status = json.optString("status");
								if (status.equals("200")) {
									JSONArray json_arr = json
											.optJSONArray("data");
									list_area.add(new Wff_VenuePopuwindow("",
											"全上海", "", new JSONArray()));
									for (int i = 0; i < json_arr.length(); i++) {
										JSONObject json_new = json_arr
												.getJSONObject(i);

										String dictId = json_new
												.optString("dictId");
										String dictName = json_new
												.optString("dictName");
										String dictCode = json_new
												.optString("dictCode");
										JSONArray dictList = json_new
												.optJSONArray("dictList");
										list_area.add(new Wff_VenuePopuwindow(
												dictId, dictName, dictCode,
												dictList));

									}
									handler.sendEmptyMessage(2);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void initData() {
		Map<String, String> mParams = new HashMap<String, String>();
		MyHttpRequest.onHttpPostParams(HttpUrlList.Venue.WFF_APPVENUETAGBYTYPE,
				mParams, new HttpRequestCallback() {

					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							// Log.d("statusCode", statusCode + "--获取分类数据-"
							// + resultStr);
							try {
								list = new ArrayList<Wff_VenuePopuwindow>();
								JSONObject json = new JSONObject(resultStr);
								String status = json.optString("status");
								if (status.equals("0")) {
									JSONArray json_arr = json
											.optJSONArray("data");

									for (int i = 0; i < json_arr.length(); i++) {
										JSONObject json_new = json_arr
												.getJSONObject(i);
										String tagId = json_new
												.optString("tagId");
										String tagName = json_new
												.optString("tagName");
										list.add(new Wff_VenuePopuwindow(tagId,
												tagName));

									}
									handler.sendEmptyMessage(1);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						} else {

						}
					}
				});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				vpgva = new VenuePopuGridViewWindowAdapter(list, mContext);
				classification_gridview.setAdapter(vpgva);
				classification_gridview
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
										.getItemAtPosition(arg2);
								// clear();
								MyApplication.venueType_classication = wvp
										.getTagId();
								if (popup != null) {
									popup.dismiss();
								}
								MyApplication.venueType_classication_Name = wvp
										.getTagName();
								vf.onRefresh();
							}
						});
				break;
			case 2:
				vpwa = new VenuePopuWindowAdapter(list_area, mContext);
				region_listview.setAdapter(vpwa);
				if (list_area_z.size() != 0 && list_area_z != null
						&& MyApplication.type_num == 1) {
					// 旁边的listview 的显示
					VenuePopuWindowAreaAdapter vpwaa = new VenuePopuWindowAreaAdapter(
							list_area_z, mContext);
					area_listview.setAdapter(vpwaa);

					int height = (int) (MyApplication.getWindowHeight() * 0.6);
					int width = (int) (MyApplication.getWindowWidth() * 0.6);
					popup_area = new PopupWindow(mView_area, width, height);
					popup_area.setFocusable(true);
					popup_area.setBackgroundDrawable(new BitmapDrawable());
					int[] location = new int[2];

					int width_local = (int) (MyApplication.getWindowWidth() * 0.4);
					popup_area.showAtLocation(area_listview, Gravity.TOP,
							width_local, MyApplication.venue_tv_height);
					area_listview
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
											.getItemAtPosition(arg2);
									if (arg2 == 0) {
										MyApplication.venueMood = "";
										MyApplication.venueMoodName = wvp
												.getTagName();
										if (popup != null) {
											popup.dismiss();
										}
										if (popup_area != null) {
											popup_area.dismiss();
										}
										vf.onRefresh();
										return;
									}

									MyApplication.venueMood = wvp.getTagId();
									MyApplication.venueMoodName = wvp
											.getTagName();
									if (popup != null) {
										popup.dismiss();
									}
									if (popup_area != null) {
										popup_area.dismiss();
									}
									vf.onRefresh();
								}
							});
				}

				region_listview
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View arg1, int arg2, long arg3) {
								// clear();
								// 这里来判断进入
								// if(){}
								if (arg2 == 0) {
									MyApplication.venueArea = "";
									MyApplication.venueMood = "";
									MyApplication.venueMoodName = "全上海";
									if (popup != null) {
										popup.dismiss();
									}
									if (popup_area != null) {
										popup_area.dismiss();
									}
									vf.onRefresh();
									return;
								}
								view = arg1;
								itemBackChanged(arg1);

								wvpw = (Wff_VenuePopuwindow) parent
										.getItemAtPosition(arg2);
								MyApplication.venueArea = wvpw.getDictCode();
								MyApplication.venueDicName = wvpw.getDictName();
								list_area_z = new ArrayList<Wff_VenuePopuwindow>();
								list_area_z.add(new Wff_VenuePopuwindow("",
										"全部" + wvpw.getDictName()));
								for (int i = 0; i < wvpw.getDictList().length(); i++) {
									try {
										JSONObject json = wvpw.getDictList()
												.getJSONObject(i);
										list_area_z.add(new Wff_VenuePopuwindow(
												json.optString("id"), json
														.optString("name")));
									} catch (Exception e) {
										e.printStackTrace();
									}

								}

								VenuePopuWindowAreaAdapter vpwaa = new VenuePopuWindowAreaAdapter(
										list_area_z, mContext);
								area_listview.setAdapter(vpwaa);

								int height = (int) (MyApplication
										.getWindowHeight() * 0.6);
								int width = (int) (MyApplication
										.getWindowWidth() * 0.6);
								popup_area = new PopupWindow(mView_area, width,
										height);
								popup_area.setFocusable(true);
								popup_area
										.setBackgroundDrawable(new BitmapDrawable());
								int[] location = new int[2];

								int width_local = (int) (MyApplication
										.getWindowWidth() * 0.4);
								popup_area.showAtLocation(area_listview,
										Gravity.TOP, width_local,
										MyApplication.venue_tv_height);
								area_listview
										.setOnItemClickListener(new OnItemClickListener() {

											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int arg2,
													long arg3) {
												Wff_VenuePopuwindow wvp = (Wff_VenuePopuwindow) arg0
														.getItemAtPosition(arg2);
												if (arg2 == 0) {
													MyApplication.venueMood = "";
													MyApplication.venueMoodName = wvp
															.getTagName();
													if (popup != null) {
														popup.dismiss();
													}
													if (popup_area != null) {
														popup_area.dismiss();
													}
													vf.onRefresh();
													return;
												}

												MyApplication.venueMood = wvp
														.getTagId();
												MyApplication.venueMoodName = wvp
														.getTagName();
												if (popup != null) {
													popup.dismiss();
												}
												if (popup_area != null) {
													popup_area.dismiss();
												}

												vf.onRefresh();
											}
										});
							}
						});

				break;
			default:
				break;
			}
		};
	};

	/**
	 * 显示搜索界面
	 * 
	 * @param
	 */
	private void initView() {

		mView = View.inflate(mContext, R.layout.windows_venue_popwindow_event,
				null);
		region_listview = (ListView) mView.findViewById(R.id.region_listview);
		mView_area = View.inflate(mContext,
				R.layout.windows_venue_popwindow_event_area, null);
		area_listview = (ListView) mView_area.findViewById(R.id.area_listview);
		classition_ll = (LinearLayout) mView.findViewById(R.id.classition_ll);
		classition_all = (TextView) mView.findViewById(R.id.classition_all);
		classition_all.setTypeface(MyApplication.GetTypeFace());
		classition_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MyApplication.venueType_classication = "";
				MyApplication.venueType_classication_Name = "全部";
				if (popup != null) {
					popup.dismiss();
				}

				vf.onRefresh();

			}
		});
		classification_gridview = (GridView) mView
				.findViewById(R.id.classification_gridview);
		sort_listview = (ListView) mView.findViewById(R.id.sort_listview);
		status_listview = (ListView) mView.findViewById(R.id.status_listview);
		int height = (int) (MyApplication.getWindowHeight() * 0.6);
		int width = (int) (MyApplication.getWindowWidth() * 0.4);
		if (MyApplication.type_num == 1) {
			region_listview.setVisibility(View.VISIBLE);
			area_listview.setVisibility(View.VISIBLE);
			mView_area.setVisibility(View.VISIBLE);
			classification_gridview.setVisibility(View.GONE);
			sort_listview.setVisibility(View.GONE);
			status_listview.setVisibility(View.GONE);
			classition_ll.setVisibility(View.GONE);
			popup = new PopupWindow(mView, width, height);
			popup.setFocusable(true);
			popup.setOutsideTouchable(true);
			popup.setBackgroundDrawable(new BitmapDrawable());
		} else if (MyApplication.type_num == 2) {
			region_listview.setVisibility(View.GONE);
			area_listview.setVisibility(View.GONE);
			mView_area.setVisibility(View.GONE);
			classification_gridview.setVisibility(View.VISIBLE);
			classition_ll.setVisibility(View.VISIBLE);
			sort_listview.setVisibility(View.GONE);
			status_listview.setVisibility(View.GONE);
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT, height);
			popup.setFocusable(true);
			popup.setOutsideTouchable(true);
			popup.setBackgroundDrawable(new BitmapDrawable());
		} else if (MyApplication.type_num == 3) {
			region_listview.setVisibility(View.GONE);
			area_listview.setVisibility(View.GONE);
			mView_area.setVisibility(View.GONE);
			classification_gridview.setVisibility(View.GONE);
			sort_listview.setVisibility(View.VISIBLE);
			status_listview.setVisibility(View.GONE);
			classition_ll.setVisibility(View.GONE);
			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT, height);
			popup.setFocusable(true);
			popup.setOutsideTouchable(true);
			popup.setBackgroundDrawable(new BitmapDrawable());
		} else if (MyApplication.type_num == 4) {
			region_listview.setVisibility(View.GONE);
			area_listview.setVisibility(View.GONE);
			mView_area.setVisibility(View.GONE);
			classification_gridview.setVisibility(View.GONE);
			sort_listview.setVisibility(View.GONE);
			classition_ll.setVisibility(View.GONE);
			status_listview.setVisibility(View.VISIBLE);

			popup = new PopupWindow(mView,
					LinearLayout.LayoutParams.MATCH_PARENT, height);
			popup.setFocusable(true);
			popup.setOutsideTouchable(true);
			popup.setBackgroundDrawable(new BitmapDrawable());
		}
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				Drawable drawable = mContext.getResources().getDrawable(
						R.drawable.icon_choosearrow);

				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight()); // 设置边界

				vf.region.setCompoundDrawables(null, null, drawable, null);// 画在右边
				vf.classification.setCompoundDrawables(null, null, drawable,
						null);// 画在右边
				vf.sort.setCompoundDrawables(null, null, drawable, null);// 画在右边
				vf.status.setCompoundDrawables(null, null, drawable, null);// 画在右边
				vf.region_bool = true;
				vf.classification_bool = true;
				vf.sort_bool = true;
				vf.status_bool = true;
			}
		});
	}

	public void clear() {

		MyApplication.venueType_classication = "";
		MyApplication.venueArea = "";
		MyApplication.venueMood = "";
		MyApplication.venueIsReserve = "";
		MyApplication.sortType = "";
	}

	/**
	 * 改变listitem的背景色
	 * 
	 * @param view
	 */
	private void itemBackChanged(View view) {
		if (itemView == null) {
			itemView = view;
		}
		if (view != null) {
			Log.i("ceshi", "看看color====  " + this.getmBgColor());
		}
		mBgColor = R.color.text_color_f2;
		itemView.setBackgroundResource(mBgColor);
		mBgColor = R.color.text_color_ff;
		// 将上次点击的listitem的背景色设置成透明
		view.setBackgroundResource(mBgColor);
		// 设置当前点击的listitem的背景色
		itemView = view;

	}
}
