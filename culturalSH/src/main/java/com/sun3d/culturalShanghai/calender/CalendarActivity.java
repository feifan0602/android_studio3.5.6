package com.sun3d.culturalShanghai.calender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.Util.ToastUtil;
import com.sun3d.culturalShanghai.activity.EventReserveActivity;

/**
 * 不爽了，谁来接手自己看着办
 * 
 * @author yangyoutao
 * 
 */
public class CalendarActivity extends Activity implements OnGestureListener,
		OnClickListener {
	private static List<DayClass> selectDay;
	private GestureDetector gestureDetector;
	private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
	private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)
	private CalendarAdapter calV;
	private int year_c = 0;
	private int month_c = 0;
	private int day_c = 0;
	public static String currentDate = "";
	private static Context mContext;
	private GridView mGridView;
	private TextView mTopTime;
	private LinearLayout mCalender;
	private ScrollView mScrollView;
	protected String currDate;
	protected final int HANDLER_SUPER = 101;
	protected final int EVENT_ROOM = 102;
	protected final int EVENT_RESERVE = 103;
	private int type;
	private int SpikeType;
	public static int nowMonth;
	public static int nowYear;
	public static String clickDateStrng = null;

	private static class DayClass {

		public int id;
		/**
		 * 阳历
		 */
		private String scheduleDay;
		/**
		 * 星期几
		 */
		private String week;
		/**
		 * 阴历
		 */
		private String LunarDay;

		private int day;

		private int month;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

		currentDate = sdf.format(date); // 当期日期
		if (MyApplication.StartTime != "") {
			year_c = Integer.parseInt(MyApplication.StartTime.split("-")[0]);
			month_c = Integer.parseInt(MyApplication.StartTime.split("-")[1]);
			day_c = Integer.parseInt(MyApplication.StartTime.split("-")[2]);
		} else {
			year_c = Integer.parseInt(currentDate.split("-")[0]);
			month_c = Integer.parseInt(currentDate.split("-")[1]);
			day_c = Integer.parseInt(currentDate.split("-")[2]);

		}

		nowMonth = month_c;
		nowYear = year_c;
	}

	/**
	 * 日历操作
	 */
	protected void onCalendar(LinearLayout calender, Context context,
			ScrollView scrollView, int mType, int[] start, int[] end,
			int SpikeType1) {
		initView();
		SpikeType = SpikeType1;
		selectDay = new ArrayList<DayClass>();
		type = mType;
		mScrollView = scrollView;
		mContext = context;
		mCalender = calender;
		mCalender.findViewById(R.id.calender_title).setVisibility(View.GONE);
		mGridView = (GridView) mCalender.findViewById(R.id.calender_gridview);
		mTopTime = (TextView) mCalender.findViewById(R.id.calendar_time);

		gestureDetector = new GestureDetector(mContext, this);
		calV = new CalendarAdapter(mContext, mContext.getResources(),
				jumpMonth, jumpYear, year_c, month_c, day_c);
		addGridView();
		mGridView.setAdapter(calV);
		addTextToTopTextView(mTopTime);
	}

	public static void setinitselectDay(int clickDateID, String date) {
		if (date != null) {
			String[] day = date.split("-");
			if (day != null && day.length > 0) {
				DayClass mDayClass = new DayClass();
				mDayClass.id = clickDateID;
				if (Integer.parseInt(day[1]) < 10) {
					day[1].replaceAll("0", "");
				}
				mDayClass.day = Integer.parseInt(day[2]);
				mDayClass.month = Integer.parseInt(day[1]);
				mDayClass.scheduleDay = date;
				selectDay.add(mDayClass);
			}
		}
	}

	private TextView mShow;

	protected void setText(TextView mdata) {
		mShow = mdata;
	}

	private void addGridView() {
		mGridView = (GridView) mCalender.findViewById(R.id.calender_gridview);

		mGridView.setOnTouchListener(new OnTouchListener() {
			// 将gridview中的触摸事件回传给gestureDetector
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				mScrollView.requestDisallowInterceptTouchEvent(true);
				return gestureDetector.onTouchEvent(event);
			}
		});

		mGridView.setOnItemClickListener(new OnItemClickListener() {
			// gridView中的每一个item的点击事件

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
				int showMonth = Integer.parseInt(calV.getShowMonth());
				// if (position < calV.getCurrentFlag() || showMonth <
				// calV.getSysMonth()) {
				// ToastUtil.showToast("不能选择过去的日期");
				// return;
				// }
				DayClass mDayClass = new DayClass();
				int startPosition = calV.getStartPositon();
				int endPosition = calV.getEndPosition();
				if (startPosition <= position + 7
						&& position <= endPosition - 7) {
					String scheduleDay = calV.getDateByClickItem(position)
							.split("\\.")[0]; // 这一天的阳历
					String scheduleLunarDay = calV.getDateByClickItem(position)
							.split("\\.")[1];
					// 这一天的阴历
					String scheduleYear = calV.getShowYear();
					String scheduleMonth = calV.getShowMonth();
					Boolean isADD = true;
					List<DayClass> ddc = new ArrayList<DayClass>();
					for (DayClass dc : selectDay) {
						if (dc.id == position) {
							arg0.getChildAt(dc.id).setBackgroundColor(
									mContext.getResources().getColor(
											R.color.activity_bg_color));
							ddc.add(dc);
							isADD = false;
						}
					}
					if (isADD) {
						int month = Integer.parseInt(scheduleMonth);

						if (month < 10) {
							if (SpikeType == 1) {
								// 秒杀
								scheduleMonth = "0" + scheduleMonth;
							} else {
								// 非秒杀
								scheduleMonth = "0" + scheduleMonth;
							}

						}
						int day = Integer.parseInt(scheduleDay);
						if (day < 10) {
							if (SpikeType == 1) {
								scheduleDay = "0" + scheduleDay;
							} else {
								scheduleDay = "0" + scheduleDay;
							}

						}
						if (!isOnclick(Integer.parseInt(scheduleYear), month,
								day) || position < calV.getCurrentFlag()) {
							ToastUtil.showToast("不在选择日期之内，请重新选择。");
							return;
						}
						if (selectDay.size() > 0) {
							for (DayClass dc : selectDay) {
								arg0.getChildAt(dc.id).setBackgroundColor(
										mContext.getResources().getColor(
												R.color.activity_bg_color));
							}
							selectDay.clear();
						}
						String daystr = scheduleYear + "-" + scheduleMonth
								+ "-" + scheduleDay;
						mDayClass.id = position;
						mDayClass.day = Integer.parseInt(scheduleDay);
						mDayClass.month = Integer.parseInt(scheduleMonth);
						mDayClass.scheduleDay = daystr;
						selectDay.add(mDayClass);
						// arg1.setBackgroundResource(R.drawable.shape_orger_oval);
						// arg1.setBackgroundColor(mCalender.getResources().getColor(R.color.orange_color));

						Message msg = new Message();
						msg.what = HANDLER_SUPER;
						if (EVENT_ROOM == type) {
							MyApplication.getInstance().getmRoomHandler()
									.sendMessage(msg);
						} else if (EVENT_RESERVE == type) {
							MyApplication.getInstance().getmReserveHandler()
									.sendMessage(msg);
						}
						currDate = selectDay.get(0).scheduleDay;
						if (mShow != null) {
							mShow.setText(currDate);
						}

					} else {
						selectDay.removeAll(ddc);
					}
				}
			}

		});

	}

	private Boolean isOnclick(int year, int mouth, int day) {
		Boolean isonclick = false;
		// 这是判断年是否相等
		if (EventReserveActivity.startTime[0] == EventReserveActivity.endTime[0]) {
			if (EventReserveActivity.startTime[0] == year) {
				// 这是判断月是否相等
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
				String sysDate = "";
				String sys_day = "";
				String sys_month = "";
				sysDate = sdf.format(date); // 当期日期
				sys_month = sysDate.split("-")[1];
				sys_day = sysDate.split("-")[2];
				if (EventReserveActivity.startTime[1] == mouth
						&& EventReserveActivity.startTime[1] <= Integer
								.valueOf(sys_month)) {

					if (EventReserveActivity.startTime[2] <= day
							&& day >= Integer.valueOf(sys_day)) {
						isonclick = true;
					}
				} else if (EventReserveActivity.startTime[1] == mouth
						&& EventReserveActivity.startTime[1] > Integer
								.valueOf(sys_month)) {
					if (EventReserveActivity.startTime[2] <= day) {
						isonclick = true;
					}
				}
			}
			if (EventReserveActivity.endTime[0] == year) {
				if (EventReserveActivity.endTime[1] == mouth) {
					if (EventReserveActivity.endTime[2] < day) {
						isonclick = false;
					} else {
						if (EventReserveActivity.endTime[1] != EventReserveActivity.startTime[1]) {
							isonclick = true;
						}

					}
				}

			}
			if (EventReserveActivity.endTime[0] == year) {
				if (EventReserveActivity.endTime[1] > mouth
						&& mouth > EventReserveActivity.startTime[1]) {
					isonclick = true;
				}
			}
		} else {
			if (EventReserveActivity.endTime[0] == year) {
				if (EventReserveActivity.endTime[1] < mouth) {
					isonclick = false;
				} else if (EventReserveActivity.endTime[1] == mouth) {
					if (EventReserveActivity.endTime[2] < day) {
						isonclick = false;
					} else {
						isonclick = true;
					}
				} else {
					isonclick = true;
				}
			} else {
				if (EventReserveActivity.startTime[0] == year) {
					if (EventReserveActivity.startTime[1] == mouth) {
						if (EventReserveActivity.startTime[2] <= day) {
							// isonclick = true;
						}
					}
				}
			}
		}
		return isonclick;

	}

	/**
	 * 添加头部的年份 闰哪月等信息
	 * 
	 * @param view
	 */
	public void addTextToTopTextView(TextView view) {
		StringBuffer textDate = new StringBuffer();
		textDate.append(calV.getShowYear()).append("年")
				.append(calV.getShowMonth()).append("月").append("\t");
		view.setText(textDate);
		view.setTypeface(Typeface.DEFAULT_BOLD);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
		if (e1.getX() - e2.getX() > 30) {
			// 像左滑动
			addGridView(); // 添加一个gridView
			jumpMonth++; // 下一个月
			calV = new CalendarAdapter(mContext, mContext.getResources(),
					jumpMonth, jumpYear, year_c, month_c, day_c);
			mGridView.setAdapter(calV);
			addTextToTopTextView(mTopTime);
			gvFlag++;
			return true;
		} else if (e1.getX() - e2.getX() < -30) {
			// 向右滑动
			addGridView(); // 添加一个gridView
			jumpMonth--; // 上一个月

			calV = new CalendarAdapter(mContext, mContext.getResources(),
					jumpMonth, jumpYear, year_c, month_c, day_c);
			mGridView.setAdapter(calV);
			gvFlag++;
			addTextToTopTextView(mTopTime);
			return true;
		}
		return false;

	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
