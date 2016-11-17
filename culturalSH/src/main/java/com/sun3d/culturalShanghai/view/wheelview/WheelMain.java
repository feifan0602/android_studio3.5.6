package com.sun3d.culturalShanghai.view.wheelview;

import java.util.Arrays;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;

public class WheelMain {

	private View view;
	private WheelMychoseTimeView wv_String;
	private WheelTimeView wv_year;
	private WheelTimeView wv_month;
	private WheelTimeView wv_day;
	private WheelMychoseTimeView wv_hours;
	private WheelMychoseTimeView wv_mins;
	public int screenheight;
	private static int START_YEAR = 1950, END_YEAR = 2016;
	private String tag = "";
	private ScrollView mscrollView;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(View view, ScrollView scrollView) {
		super();
		this.mscrollView = scrollView;
		this.view = view;
		setView(view);
	}

	public WheelMain(View view) {
		super();
		this.view = view;
		setView(view);
	}

	public void isShowView(boolean isShow) {
		if (isShow) {
			this.view.setVisibility(View.VISIBLE);
		} else {
			this.view.setVisibility(View.GONE);
		}
	}

	public void initStringPicker(final String[] strlist, final TextView tv) {
		wv_String = (WheelMychoseTimeView) view.findViewById(R.id.string_wheel);
		wv_String.setAdapter(new ArrayWheelAdapter<String>(strlist));// 设置"年"的显示数据
		wv_String.setCyclic(false);// 可循环滚动
		wv_String.setCurrentItem(0);
		wv_String.addPrentView(mscrollView);
		OnWheelChangedListener wheelListener_String = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub
				tv.setText(strlist[newValue]);

			}

		};
		wv_String.addChangingListener(wheelListener_String);
		// wv_hours.addChangingListener(wheelListener_hour);
		// wv_mins.addChangingListener(wheelListener_min);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		textSize = (screenheight / 100) * 4;
		wv_String.TEXT_SIZE = textSize;

	}

	public void initStringPicker(final String[] strlist, final TextView tv,
			final TextView time, final String timeShow) {
		wv_String = (WheelMychoseTimeView) view.findViewById(R.id.string_wheel);
		wv_String.setAdapter(new ArrayWheelAdapter<String>(strlist));// 设置"年"的显示数据
		wv_String.setCyclic(false);// 可循环滚动
		wv_String.setCurrentItem(0);
		wv_String.addPrentView(mscrollView);
		OnWheelChangedListener wheelListener_String = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub
				tv.setText(strlist[newValue]);
				time.setText(timeShow);
			}

		};
		wv_String.addChangingListener(wheelListener_String);
		// wv_hours.addChangingListener(wheelListener_hour);
		// wv_mins.addChangingListener(wheelListener_min);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		textSize = (screenheight / 100) * 4;
		wv_String.TEXT_SIZE = textSize;

	}

	public void initStringPicker(final Integer[] intList,
			final ScrollView mSrcollView, final TextView mNumber) {
		wv_String = (WheelMychoseTimeView) view.findViewById(R.id.string_wheel);
		wv_String.setAdapter(new ArrayWheelAdapter<Integer>(intList));
		OnWheelChangedListener wheelListener_String = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub
				// time.setText(timeShow);
				mNumber.setText("" + intList[newValue]);
				mOnWheelViewNumberChangedListener.onChange(intList[newValue]);
			}

		};
		wv_String.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				mSrcollView.requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
		wv_String.addChangingListener(wheelListener_String);
		wv_String.TEXT_SIZE = 80;
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day) {
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);

		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelTimeView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(true);// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelTimeView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(01, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);

		// 日
		wv_day = (WheelTimeView) view.findViewById(R.id.day);
		wv_day.setCyclic(true);
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
			else
				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
		}
		wv_day.setLabel("日");
		wv_day.setCurrentItem(day - 1);

		// wv_hours = (WheelTimeView) view.findViewById(R.id.hour);
		// wv_hours.setAdapter(new NumericWheelAdapter(00, 23));
		// wv_hours.setCyclic(true);
		// wv_hours.setLabel("时");
		// wv_hours.setCurrentItem(hour);
		//
		// wv_mins = (WheelTimeView) view.findViewById(R.id.mini);
		// wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		// wv_mins.setCyclic(true);
		// wv_mins.setLabel("分");
		// wv_mins.setCurrentItem(min);

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
				int year = wv_year.getCurrentItem() + START_YEAR;
				// show.setText(tag + year + "-" + (wv_month.getCurrentItem() +
				// 1) + "-" + (wv_day.getCurrentItem() + 1) + "  " +
				// (wv_hours.getCurrentItem())
				// + ":" + (wv_mins.getCurrentItem()));
				if (mOnWheelViewChangedListener != null) {
					mOnWheelViewChangedListener.onChange(year,
							wv_month.getCurrentItem() + 1,
							wv_day.getCurrentItem() + 1, 0, 0);
				}
			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub

			}

		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
				int year = wv_year.getCurrentItem() + START_YEAR;
				// show.setText(tag + year + "-" + (wv_month.getCurrentItem() +
				// 1) + "-" + (wv_day.getCurrentItem() + 1) + "  " +
				// (wv_hours.getCurrentItem())
				// + ":" + (wv_mins.getCurrentItem()));
				if (mOnWheelViewChangedListener != null) {
					mOnWheelViewChangedListener.onChange(year,
							wv_month.getCurrentItem() + 1,
							wv_day.getCurrentItem() + 1, 0, 0);
				}
			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub

			}

		};

		// 添加"天"监听
		OnWheelChangedListener wheelListener_day = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

				int year = wv_year.getCurrentItem() + START_YEAR;
				// show.setText(tag + year + "-" + (wv_month.getCurrentItem() +
				// 1) + "-" + (wv_day.getCurrentItem() + 1) + "  " +
				// (wv_hours.getCurrentItem())
				// + ":" + (wv_mins.getCurrentItem()));
				if (mOnWheelViewChangedListener != null) {
					mOnWheelViewChangedListener.onChange(year,
							wv_month.getCurrentItem() + 1,
							wv_day.getCurrentItem() + 1, 0, 0);
				}
			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub

			}

		};

		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);
		wv_day.addChangingListener(wheelListener_day);
		// wv_hours.addChangingListener(wheelListener_hour);
		// wv_mins.addChangingListener(wheelListener_min);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		textSize = (screenheight / 100) * 4;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		// wv_hours.TEXT_SIZE = textSize;
		// wv_mins.TEXT_SIZE = textSize;
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(final TextView mTime, int hour, int min) {

		wv_hours = (WheelMychoseTimeView) view.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setLabel("：");
		wv_hours.setCurrentItem(hour);
		wv_hours.addPrentView(mscrollView);
		wv_mins = (WheelMychoseTimeView) view.findViewById(R.id.minice);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
		wv_mins.setCyclic(true);
		wv_mins.setLabel("");
		wv_mins.setCurrentItem(min);
		wv_mins.addPrentView(mscrollView);
		// 添加"时"监听
		OnWheelChangedListener wheelListener_hour = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub
				int hour = wv_hours.getCurrentItem();
				String hourstr = null;
				if (hour < 10) {
					hourstr = "0" + hour;
				} else {
					hourstr = String.valueOf(hour);
				}
				int mins = wv_mins.getCurrentItem();
				String minsstr = null;
				if (mins < 10) {
					minsstr = "0" + mins;
				} else {
					minsstr = String.valueOf(mins);
				}
				if (mOnWheelViewChangedListener != null) {
					mOnWheelViewChangedListener
							.onChange(0, 0, 0, wv_hours.getCurrentItem(),
									wv_mins.getCurrentItem());
				}
				mTime.setText(hourstr + "：" + minsstr);
			}
		};

		// 添加"分"监听
		OnWheelChangedListener wheelListener_min = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelTimeView wheelTimeView, int oldValue,
					int newValue) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChanged(WheelMychoseTimeView wheelTimeView,
					int oldValue, int newValue) {
				// TODO Auto-generated method stub

				int hour = wv_hours.getCurrentItem();
				String hourstr = null;
				if (hour < 10) {
					hourstr = "0" + hour;
				} else {
					hourstr = String.valueOf(hour);
				}
				int mins = wv_mins.getCurrentItem();
				String minsstr = null;
				if (mins < 10) {
					minsstr = "0" + mins;
				} else {
					minsstr = String.valueOf(mins);
				}
				if (mOnWheelViewChangedListener != null) {
					mOnWheelViewChangedListener
							.onChange(0, 0, 0, wv_hours.getCurrentItem(),
									wv_mins.getCurrentItem());
				}
				mTime.setText(hourstr + "：" + minsstr);
			}
		};

		wv_hours.addChangingListener(wheelListener_hour);
		wv_mins.addChangingListener(wheelListener_min);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		textSize = (screenheight / 100) * 4;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;
	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
				.append((wv_month.getCurrentItem() + 1)).append("-")
				.append((wv_day.getCurrentItem() + 1));
		return sb.toString();
	}

	private OnWheelViewChangedListener mOnWheelViewChangedListener;
	private OnWheelViewNumberChangedListener mOnWheelViewNumberChangedListener;

	public void setOnWheelViewChangedListener(
			OnWheelViewChangedListener onWheelViewChangedListener) {
		mOnWheelViewChangedListener = onWheelViewChangedListener;
	}

	public void setmOnWheelViewNumberChangedListener(
			OnWheelViewNumberChangedListener mOnWheelViewNumberChangedListener) {
		this.mOnWheelViewNumberChangedListener = mOnWheelViewNumberChangedListener;
	}

	public interface OnWheelViewChangedListener {
		public void onChange(int year, int mouth, int day, int hour, int min);
	}

	public interface OnWheelViewNumberChangedListener {
		public void onChange(int number);
	}
}
