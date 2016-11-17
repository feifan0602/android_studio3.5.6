package com.sun3d.culturalShanghai.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.JsonUtil;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.handler.LoadingHandler;
import com.sun3d.culturalShanghai.handler.NewSeatRowComparator;
import com.sun3d.culturalShanghai.http.HttpCode;
import com.sun3d.culturalShanghai.http.HttpRequestCallback;
import com.sun3d.culturalShanghai.http.HttpUrlList;
import com.sun3d.culturalShanghai.http.MyHttpRequest;
import com.sun3d.culturalShanghai.seat.CH_seatInfo;
import com.sun3d.culturalShanghai.seat.OnNewSeatClickListener;
import com.sun3d.culturalShanghai.seat.SSThumView;
import com.sun3d.culturalShanghai.seat.SeatStatus;
import com.sun3d.culturalShanghai.seat.SeatView;
import com.umeng.analytics.MobclickAgent;

public class NewOnlinSeatActivity extends Activity implements OnClickListener,
		OnNewSeatClickListener {
	private Context mContext;
	private String activityId = "", activityEventimes = "";
	private LoadingHandler mLoadingHandler;
	private TextView selectSeatTV;
	// private String selcetSeatStr = "";
	private String spaceingSeatStr = " ";
	private String RealySeat;
	private String showRealySeat;
	private String titlestr = "在 线 选 座";
	private static NewOnlinSeatActivity mNewOnlinSeatActivity;
	private List<CH_seatInfo> list_seat = new ArrayList<CH_seatInfo>();
	public static List<CH_seatInfo> selectSeatlist = new ArrayList<CH_seatInfo>();
	private SeatView mSSView;
	private SSThumView mSSThumView;
	public static int ticketCount;
	/**
	 * 最多能预定的座位
	 */
	private static int MaxSeatCount = 5;
	private final String mPageName = "NewOnlinSeatActivity";

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
		setContentView(R.layout.activity_new_onlin_seat);
		mContext = this;
		mNewOnlinSeatActivity = this;
		MyApplication.getInstance().addActivitys(this);
		activityEventimes = this.getIntent().getExtras().getString("activityEventimes");
		activityId = this.getIntent().getExtras().getString("activityId");
		RealySeat = this.getIntent().getExtras().getString("Seat");
		showRealySeat = this.getIntent().getExtras().getString("showSeat");
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mSSView = (SeatView) findViewById(R.id.mSSView);
		mSSView.setOnSeatClickListener(this);
		mSSThumView = (SSThumView) findViewById(R.id.ss_ssthumview);
		RelativeLayout loadingLayout = (RelativeLayout) findViewById(R.id.loading);
		mLoadingHandler = new LoadingHandler(loadingLayout);
		mLoadingHandler.startLoading();
		selectSeatTV = (TextView) findViewById(R.id.seat_select_tv);
		findViewById(R.id.onlin_reserve).setOnClickListener(this);
		selectSeatTV.setText("");

		setTitle();
		getData();

	}

	/**
	 * 座位数据初始化
	 */
	private void ininSeatShow() {
		if (RealySeat != null && RealySeat.length() > 2) {
			String[] seatArray = RealySeat.split(",");
			String[] showseatArray = showRealySeat.split(",");
			if (seatArray != null && seatArray.length > 0) {
				int i = 0;
				for (String seat : seatArray) {
					String[] strArray = seat.split("_");
					String[] showstrArray = showseatArray[i].split("_");
					if (strArray != null && strArray.length > 0) {
						CH_seatInfo msSeatInfo = new CH_seatInfo();
						msSeatInfo.setRaw(Integer.parseInt(strArray[0]));
						msSeatInfo.setColumn(Integer.parseInt(strArray[1]));
						msSeatInfo.setStatus(SeatStatus.CHOOSE_OK);
						msSeatInfo.setShow_column(Integer.parseInt(showstrArray[1]));
						selectSeatlist.add(msSeatInfo);
						// selcetSeatStr += msSeatInfo.getRaw() + "排" +
						// msSeatInfo.getColumn() + "座"
						// + spaceingSeatStr;
					}
					i += 1;
				}

			}

		}
		// selectSeatTV.setText(selcetSeatStr);
		showSeat();// 设值
	}

	/**
	 * 初始换座位数据
	 */
	private void iniSeatData() {
		selectSeatlist.clear();
		NewSeatRowComparator comparator = new NewSeatRowComparator();
		Collections.sort(list_seat, comparator);
		ininSeatShow();
		int index = 0;
		for (CH_seatInfo msSeatInfo : list_seat) {
			msSeatInfo.setPosition(index);
			index += 1;
			if (selectSeatlist.size() > 0) {
				for (CH_seatInfo ms : selectSeatlist) {
					if ((ms.getColumn() == msSeatInfo.getColumn())
							&& (ms.getRaw() == msSeatInfo.getRaw())) {
						msSeatInfo.setStatus(SeatStatus.CHOOSE_OK);
					}

				}
			}
		}
		Log.i("ceshi", "看看座位的数据==list_seat==  "+list_seat.size()+" mSSThumView ==  "+mSSThumView+" MaxSeatCount== "+MaxSeatCount);
		mSSView.init(list_seat, mSSThumView, MaxSeatCount);
		mLoadingHandler.stopLoading();

	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		RelativeLayout mTitle = (RelativeLayout) findViewById(R.id.title);
		mTitle.findViewById(R.id.title_left).setOnClickListener(this);
		mTitle.findViewById(R.id.title_right).setVisibility(View.GONE);
		TextView title = (TextView) mTitle.findViewById(R.id.title_content);
		title.setText(titlestr);
		title.setTypeface(MyApplication.GetTypeFace());
		title.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取数据
	 */
	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", MyApplication.loginUserInfor.getUserId());
		params.put("activityId", activityId);
		params.put("activityEventimes", activityEventimes);
		MyHttpRequest.onHttpPostParams(HttpUrlList.EventUrl.ACTIVITY_ONLINE_SEAT, params,
				new HttpRequestCallback() {
					@Override
					public void onPostExecute(int statusCode, String resultStr) {
						// TODO Auto-generated method stub
						Log.i("ceshi", "座位==  "+resultStr+"  statusCode==  "+statusCode);
						if (HttpCode.HTTP_Request_Success_CODE == statusCode) {
							list_seat = JsonUtil.getNewOnlineSeatInfoList(resultStr);
							if (list_seat == null || list_seat.size() == 0) {
								mLoadingHandler.showErrorText("没有座位信息");
							} else {
								iniSeatData();
							}
						} else {
							mLoadingHandler.showErrorText(resultStr);
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_onlin_seat, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.onlin_reserve:
			Intent intent = new Intent();
			intent.putExtra("seat", setSeat());
			intent.putExtra("showseat", setShowSeat());
			this.setIntent(intent);
			this.setResult(101, intent);
			finish();
			break;
		default:
			finish();
			break;
		}

	}

	/**
	 * 提交座位字符整理
	 */
	public String setSeat() {
		String str = "";

		for (int i = 0; i < selectSeatlist.size(); i++) {
			str += selectSeatlist.get(i).getRaw() + "_" + selectSeatlist.get(i).getColumn() + ",";
		}
		if (str.length() > 2) {
			str = str.substring(0, str.length() - 1);
		}
		Log.d("RealySeat", str);
		return str;
	}

	/**
	 * 显示座位字符整理
	 */
	public String setShowSeat() {
		String str = "";

		for (int i = 0; i < selectSeatlist.size(); i++) {
			str += selectSeatlist.get(i).getRaw() + "_" + selectSeatlist.get(i).getShow_column()
					+ ",";
		}
		if (str.length() > 2) {
			str = str.substring(0, str.length() - 1);
		}
		Log.d("RealySeat", str);
		return str;
	}

	/**
	 * 整理场次ID
	 */
	public String arrangeScreeningsID() {
		String id = "";

		return id;
	}

	/**
	 * 点击取消
	 */
	@Override
	public boolean unClick(CH_seatInfo seatInfo) {
		// TODO Auto-generated method stub
		deleteSeat(seatInfo);
		return false;
	}

	/**
	 * 点击选中
	 */
	@Override
	public boolean onClick(CH_seatInfo seatInfo) {
		// TODO Auto-generated method stub
		selectSeatlist.add(seatInfo);
		// selcetSeatStr += seatInfo.getRaw() + "排" + seatInfo.getColumn() + "座"
		// + spaceingSeatStr;
		// selectSeatTV.setText(selcetSeatStr);
		showSeat();// 设值
		return false;
	}

	/**
	 * 显示座位
	 */
	private void showSeat() {
		if (selectSeatlist == null) {
			return;
		}
		String newStr = "";
		for (CH_seatInfo seatInfo : selectSeatlist) {
			newStr += seatInfo.getRaw() + "排" + seatInfo.getShow_column() + "座" + spaceingSeatStr;
		}
		selectSeatTV.setText(newStr);

	}

	/**
	 * 超过最大票数显示弹出框
	 */
	public static void showOverSeatToast() {
		LayoutInflater inflater = mNewOnlinSeatActivity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_maxnumber_layout, null);
		Toast toast = new Toast(mNewOnlinSeatActivity);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(layout);
		toast.show();
	}

	/**
	 * 删除座位
	 * 
	 * @param msSeatInfo
	 */
	private void deleteSeat(CH_seatInfo msSeatInfo) {
		CH_seatInfo deleteinfor = null;
		if (selectSeatlist != null && selectSeatlist.size() > 0) {
			for (CH_seatInfo mst : selectSeatlist) {
				Log.d("selectSeatlist", mst.toString());
				if ((msSeatInfo.getColumn() == mst.getColumn())
						&& (msSeatInfo.getRaw() == mst.getRaw())) {
					deleteinfor = mst;
				}

			}

		}
		if (deleteinfor != null) {
			selectSeatlist.remove(deleteinfor);
			showSeat();// 设值
			// String str = msSeatInfo.getRaw() + "排" + msSeatInfo.getColumn() +
			// "座";
			// String strnew = "";
			// String[] strarry = selcetSeatStr.split(spaceingSeatStr);
			// if (strarry != null && strarry.length > 0) {
			// for (String s : strarry) {
			// if (!str.equals(s)) {
			// strnew += s + spaceingSeatStr;
			// }
			// }
			// selcetSeatStr = strnew;
			// }
			// selectSeatTV.setText(selcetSeatStr);
		}

	}
}
