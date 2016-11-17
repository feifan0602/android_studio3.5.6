package com.sun3d.culturalShanghai.handler;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.TextUtil;
import com.sun3d.culturalShanghai.Util.WhatDateUtil;
import com.sun3d.culturalShanghai.Util.WhatDateUtil.WeekDay;
import com.sun3d.culturalShanghai.adapter.WeekActivityListAdapter;

import java.util.List;

/**
 * 日期控件
 * 
 * @author wenff
 * 
 */
public class WeekDateHandler implements OnClickListener {
	private Context mContext;
	private LinearLayout prentLayout;
	private List<WeekDay> WeekDayList;
	private TextView mouth_Tv;
	private TextView week1_Tv, week2_Tv, week3_Tv, week4_Tv, week5_Tv,
			week6_Tv, week7_Tv;
	private Button day1_bt, day2_bt, day3_bt, day4_bt, day5_bt, day6_bt,
			day7_bt;
	private Button oldClick = null;
	private TextView tv;
	private WeekActivityListAdapter.WeekLisenner mWeekLisenner;

	public WeekDateHandler(Context context, LinearLayout weekdatelayot,
			TextView mtv, WeekActivityListAdapter.WeekLisenner weekLisenner) {
		this.mContext = context;
		this.prentLayout = weekdatelayot;
		this.WeekDayList = WhatDateUtil.getDateToWeek();
		this.tv = mtv;
		this.mWeekLisenner = weekLisenner;
		initView();
	}

	/**
	 * 控件初始化
	 */
	private void initView() {
		/**
		 * 月
		 */
		mouth_Tv = (TextView) this.prentLayout.findViewById(R.id.mouth);
		/**
		 * 周
		 */
		week1_Tv = (TextView) this.prentLayout.findViewById(R.id.week1);
		week2_Tv = (TextView) this.prentLayout.findViewById(R.id.week2);
		week3_Tv = (TextView) this.prentLayout.findViewById(R.id.week3);
		week4_Tv = (TextView) this.prentLayout.findViewById(R.id.week4);
		week5_Tv = (TextView) this.prentLayout.findViewById(R.id.week5);
		week6_Tv = (TextView) this.prentLayout.findViewById(R.id.week6);
		week7_Tv = (TextView) this.prentLayout.findViewById(R.id.week7);
		/**
		 * 日
		 */
		day1_bt = (Button) this.prentLayout.findViewById(R.id.day1);
		day2_bt = (Button) this.prentLayout.findViewById(R.id.day2);
		day3_bt = (Button) this.prentLayout.findViewById(R.id.day3);
		day4_bt = (Button) this.prentLayout.findViewById(R.id.day4);
		day5_bt = (Button) this.prentLayout.findViewById(R.id.day5);
		day6_bt = (Button) this.prentLayout.findViewById(R.id.day6);
		day7_bt = (Button) this.prentLayout.findViewById(R.id.day7);
		setlisener();
		addDateData();
	}

	/**
	 * 数据初始化以及添加
	 */
	private void addDateData() {
		if (WeekDayList != null && WeekDayList.size() > 0) {
			switch (WeekDayList.get(0).mouthDate) {
			case 0:
				mouth_Tv.setText("一月");
				break;
			case 1:
				mouth_Tv.setText("一月");
				break;
			case 2:
				mouth_Tv.setText("二月");
				break;
			case 3:
				mouth_Tv.setText("三月");
				break;
			case 4:
				mouth_Tv.setText("四月");
				break;
			case 5:
				mouth_Tv.setText("五月");
				break;
			case 6:
				mouth_Tv.setText("六月");
				break;
			case 7:
				mouth_Tv.setText("七月");
				break;
			case 8:
				mouth_Tv.setText("八月");
				break;
			case 9:
				mouth_Tv.setText("九月");
				break;
			case 10:
				mouth_Tv.setText("十月");
				break;
			case 11:
				mouth_Tv.setText("十一月");
				break;
			case 12:
				mouth_Tv.setText("十二月");
				break;
			default:
				break;
			}
			for (int i = 0; i < WeekDayList.size(); i++) {
				WeekDay wd = WeekDayList.get(i);
				String strDay = "";
				if (wd.day < 10) {
					strDay = "0" + wd.day;
				} else {
					strDay = wd.day + "";
				}
				// if (DAY < 10) {
				// day = "0" + day;
				// } else {
				// day = day + "";
				// }
				switch (i) {
				case 0:
					week1_Tv.setText("周" + wd.weekDate);
					day1_bt.setText("今天");
					setButtonBg(day1_bt);
					oldClick = day1_bt;
					break;
				case 1:
					week2_Tv.setText("周" + wd.weekDate);
					day2_bt.setText("明天");
					break;
				case 2:
					week3_Tv.setText("周" + wd.weekDate);
					day3_bt.setText(strDay);
					break;
				case 3:
					week4_Tv.setText("周" + wd.weekDate);
					day4_bt.setText(strDay);
					break;
				case 4:
					week5_Tv.setText("周" + wd.weekDate);
					day5_bt.setText(strDay);
					break;
				case 5:
					week6_Tv.setText("周" + wd.weekDate);
					day6_bt.setText(strDay);
					break;
				case 6:
					week7_Tv.setText("周" + wd.weekDate);
					day7_bt.setText(strDay);
					break;
				default:
					break;
				}

			}
		}

	}

	/**
	 * 设置监听
	 */
	private void setlisener() {
		day1_bt.setOnClickListener(this);
		day2_bt.setOnClickListener(this);
		day3_bt.setOnClickListener(this);
		day4_bt.setOnClickListener(this);
		day5_bt.setOnClickListener(this);
		day6_bt.setOnClickListener(this);
		day7_bt.setOnClickListener(this);

	}

	/**
	 * 切换背景
	 * 
	 * @param now
	 */
	private void setButtonBg(Button now) {
		if (now.getText().equals("今天")) {
			day1_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg_one));
		} else {
			if (oldClick != null) {
				now.setBackground(mContext.getResources().getDrawable(
						R.drawable.shape_week_tc_bg));
				now.setTextColor(mContext.getResources().getColor(
						R.color.black_color));
			} else {
				oldClick.setBackgroundColor(Color.WHITE);
				oldClick.setTextColor(mContext.getResources().getColor(
						R.color.app_text_color));
			}
		}
	}

	private void reSetTextBg() {
		day2_bt.setBackgroundColor(Color.WHITE);
		day3_bt.setBackgroundColor(Color.WHITE);
		day4_bt.setBackgroundColor(Color.WHITE);
		day5_bt.setBackgroundColor(Color.WHITE);
		day6_bt.setBackgroundColor(Color.WHITE);
		day7_bt.setBackgroundColor(Color.WHITE);
	}

	String text = "";

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.day1:
			setMouthOrDate(0);
			reSetTextBg();
			day1_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg_one));
			break;
		case R.id.day2:
			// mouth_Tv.setText(WeekDayList.get(1).mouthDate);
			// text = WeekDayList.get(1).date;
			setMouthOrDate(1);
			reSetTextBg();
			day2_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg));
			break;
		case R.id.day3:
			setMouthOrDate(2);
			reSetTextBg();
			day3_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg));
			break;
		case R.id.day4:
			setMouthOrDate(3);
			reSetTextBg();
			day4_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg));
			break;
		case R.id.day5:
			setMouthOrDate(4);
			reSetTextBg();
			day5_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg));
			break;
		case R.id.day6:
			setMouthOrDate(5);
			reSetTextBg();
			day6_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg));
			break;
		case R.id.day7:
			setMouthOrDate(6);
			reSetTextBg();
			day7_bt.setBackground(mContext.getResources().getDrawable(
					R.drawable.shape_week_tc_bg));
			break;
		default:
			break;
		}
		this.tv.setText(TextUtil.Time2Format(text));
		if (this.mWeekLisenner != null) {
			this.mWeekLisenner.onWeekClick(text);
		}
		oldClick = (Button) arg0;
	}

	private void setMouthOrDate(int i) {
		switch (WeekDayList.get(i).mouthDate) {
		case 0:
			mouth_Tv.setText("一月");
			break;
		case 1:
			mouth_Tv.setText("一月");
			break;
		case 2:
			mouth_Tv.setText("二月");
			break;
		case 3:
			mouth_Tv.setText("三月");
			break;
		case 4:
			mouth_Tv.setText("四月");
			break;
		case 5:
			mouth_Tv.setText("五月");
			break;
		case 6:
			mouth_Tv.setText("六月");
			break;
		case 7:
			mouth_Tv.setText("七月");
			break;
		case 8:
			mouth_Tv.setText("八月");
			break;
		case 9:
			mouth_Tv.setText("九月");
			break;
		case 10:
			mouth_Tv.setText("十月");
			break;
		case 11:
			mouth_Tv.setText("十一月");
			break;
		case 12:
			mouth_Tv.setText("十二月");
			break;
		default:
			break;
		}
		text = WeekDayList.get(i).date;
	}

}
