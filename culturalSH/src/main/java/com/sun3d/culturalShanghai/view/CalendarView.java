package com.sun3d.culturalShanghai.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * 日历控件 功能：获得点选的日期区间
 * 
 */
public class CalendarView extends View implements View.OnTouchListener {
	private final static String TAG = "anCalendar";
	private int calendar_height = -1;
	private View calendar_view;
	private int height_num;
	/**
	 * 这个是判断去除第一行 false 標示去除第一行
	 */
	private boolean frist_bool = true;
	/**
	 * 这个是判断去除最后一行 false 標示去除最後一行
	 */
	private boolean last_bool = true;
	private Date selectedStartDate;
	private Date selectedEndDate;
	private Date curDate; // 当前日历显示的月
	private Date today; // 今天的日期文字显示红色
	private Date downDate; // 手指按下状态时临时日期
	private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期
	private int downIndex; // 按下的格子索引
	private Calendar calendar;
	private Surface surface;
	private int[] date = new int[42]; // 日历显示数字
	private int[] date_num = new int[42]; // 显示场数
	private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
	private boolean completed = false; // 为false表示只选择了开始日期，true表示结束日期也选择了
	private boolean isSelectMore = false;

	private int start_index;
	/**
	 * true 由大变小的 false 由小变大
	 */
	public boolean animation_item = true;
	public boolean animation_start = true;;
	private float seven_x, seven_y, pandding_right;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private int x_cricle, y_cricle;
	private int todayIndex;
	private ValueAnimator animator;
	/**
	 * 判断选中的圆圈 是否消失 false 消失 true 不消失
	 */
	private boolean select_cricle;
	private int item_width;
	/**
	 * 还原的date
	 */
	private Date date1;
	private int end_index;
	/**
	 * 今天星期几
	 */
	private int day_num;
	private float Y_seven = 1.3f;
	// 给控件设置监听事件
	private OnItemClickListener_calendar onItemClickListener;
	/**
	 * 移動日期的距離
	 */
	private int margin_top = 15;
	private int margin_top_seven = 60;
	/**
	 * 这是圆心的位置
	 */
	private int circle_num = 5;
	private ArrayList<String> list = new ArrayList<String>();

	public CalendarView(Context context) {
		super(context);
		init();
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		date1 = new Date();
		item_width = MyApplication.getWindowWidth() / 7;
		curDate = selectedStartDate = selectedEndDate = today = new Date();
		calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		surface = new Surface();
		surface.density = getResources().getDisplayMetrics().density;
		setBackgroundColor(surface.bgColor);
		setOnTouchListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		x_cricle = getResources().getDisplayMetrics().widthPixels;
		y_cricle = getResources().getDisplayMetrics().heightPixels;
		if (calendar_height >= 0) {
			surface.width = getResources().getDisplayMetrics().widthPixels;
			surface.height = calendar_height;
		} else {

			if (MyApplication.num_data == 7) {
				surface.width = getResources().getDisplayMetrics().widthPixels;
				surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 20);

			} else {
				if (MyApplication.line_data == 1
						|| MyApplication.line_data == 2) {
					surface.width = getResources().getDisplayMetrics().widthPixels;
					surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 6);
				} else {
					surface.width = getResources().getDisplayMetrics().widthPixels;
					surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 5);
				}

			}
		}
		// if (View.MeasureSpec.getMode(widthMeasureSpec) ==
		// View.MeasureSpec.EXACTLY) {
		// surface.width = View.MeasureSpec.getSize(widthMeasureSpec);
		// }
		// if (View.MeasureSpec.getMode(heightMeasureSpec) ==
		// View.MeasureSpec.EXACTLY) {
		// surface.height = View.MeasureSpec.getSize(heightMeasureSpec);
		// }
		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,
				View.MeasureSpec.EXACTLY);
		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,
				View.MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setdata(ArrayList<String> list) {
		this.list = list;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		Log.d(TAG, "[onLayout] changed:"
				+ (changed ? "new size" : "not change") + " left:" + left
				+ " top:" + top + " right:" + right + " bottom:" + bottom);
		if (changed) {
			surface.init();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw");
		// 画框
		canvas.drawPath(surface.boxPath, surface.borderPaint);
		// 年月
		// String monthText = getYearAndmonth();
		// float textWidth = surface.monthPaint.measureText(monthText);
		// canvas.drawText(monthText, (surface.width - textWidth) / 2f,
		// surface.monthHeight * 3 / 4f, surface.monthPaint);
		// 上一月/下一月
		// canvas.drawPath(surface.preMonthBtnPath,
		// surface.monthChangeBtnPaint);
		// canvas.drawPath(surface.nextMonthBtnPath,
		// surface.monthChangeBtnPaint);
		// 星期
		float weekTextY = surface.monthHeight + surface.weekHeight * 3 / 4f;
		// 星期背景
		// surface.cellBgPaint.setColor(surface.textColor);
		// canvas.drawRect(surface.weekHeight, surface.width,
		// surface.weekHeight, surface.width, surface.cellBgPaint);
		if (MyApplication.num_data == 7) {
			weekTextY = weekTextY + y_cricle / 50;
		}
		for (int i = 0; i < surface.weekText.length; i++) {
			float weekTextX = i
					* surface.cellWidth
					+ (surface.cellWidth - surface.weekPaint
							.measureText(surface.weekText[i])) / 2f;
			canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
					surface.weekPaint);
		}

		// 计算日期和场数
		calculateDate();
		// 按下状态，选择状态背景色
		drawDownOrSelectedBg(canvas);
		// write date number
		// today index 这个是今天的下标
		todayIndex = -1;
		calendar.setTime(curDate);
		// 这个是今天星期几 calendar.get(Calendar.MONTH);
		String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
				+ calendar.get(Calendar.MONTH);
		calendar.setTime(today);
		String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
				+ calendar.get(Calendar.MONTH);
		if (curYearAndMonth.equals(todayYearAndMonth)) {
			int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
			todayIndex = curStartIndex + todayNumber - 1;
		}
		if (MyApplication.num_data == 7) {
			int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);
			switch (dayOfweek) {
			case 1:
				System.out.println("星期日");
				day_num = 0;
				break;
			case 2:
				System.out.println("星期一");
				day_num = 1;
				break;
			case 3:
				System.out.println("星期二");
				day_num = 2;
				break;
			case 4:
				System.out.println("星期三");
				day_num = 3;
				break;
			case 5:
				System.out.println("星期四");
				day_num = 4;
				break;
			case 6:
				System.out.println("星期五");
				day_num = 5;
				break;
			case 7:
				System.out.println("星期六");
				day_num = 6;
				break;
			}
			int pos;

			if (todayIndex < 0) {
				// 这个是别个月的7个
				start_index = curStartIndex;
				pos = MyApplication.num_data;
			} else {
				// 这个是本月的 7个
				pos = todayIndex + MyApplication.num_data - day_num;
				start_index = (todayIndex - day_num);
			}

			/**
			 * 日历 日期的计算
			 */
			for (int i = start_index; i < pos; i++) {
				// 今天星期几

				// int color = surface.textColor;
				// if (isLastMonth(i)) {
				// color = surface.borderColor;
				// } else if (isNextMonth(i)) {
				// color = surface.borderColor;
				// }
				// if (todayIndex != -1 && i == todayIndex) {
				// color = surface.todayNumberColor;
				// }
				// drawCellText(canvas, i, date[i] + "", color);

				int color = surface.textColor;
				if (isLastMonth(i)) {
					color = surface.borderColor;
				} else if (isNextMonth(i)) {
					color = surface.borderColor;
				}
				if (todayIndex != -1 && i == todayIndex) {
					color = surface.todayNumberColor;
					drawCellCircle(canvas, i, date[i] + "", color);
					drawCellText(canvas, i, date[i] + "", color);

					color = surface.todayNumber;
					String data = list.get(i - curStartIndex + 1).split(":")[1]
							.replace("}", "");
					for (int j = 0; j < list.size(); j++) {
						String data_left = list.get(j).split(":")[0];
						data_left = data_left.replace("{", "");
						data_left = data_left.substring(9, 11);
						// .replace("0","")

						int data_int = Integer.valueOf(data_left);

						if (date[i] == data_int) {
							drawCellNumText(canvas, i,
									list.get(j).split(":")[1].replace("}", "")
											+ "场", color);
						}
					}

				} else if (i > 0) {
					// color = surface.todayNumber;

					drawCellText(canvas, i, date[i] + "", color);
					/**
					 * 添加场数的 drawText
					 */
					if (i >= curStartIndex) {
						String data = list.get(i - curStartIndex).split(":")[1]
								.replace("}", "");
					} else {
						String data = list.get(i).split(":")[1]
								.replace("}", "");
					}

					for (int j = 0; j < list.size(); j++) {
						String data_left = list.get(j).split(":")[0];
						data_left = data_left.replace("{", "");
						data_left = data_left.replace("}", "");
						data_left = data_left.substring(9, 11);
						// .replace("0","")

						int data_int = Integer.valueOf(data_left);
						if (date[i] == data_int) {
							drawCellNumText(canvas, i,
									list.get(j).split(":")[1].replace("}", "")
											+ "场", color);
						}
					}
					// drawCellNumText(canvas, i, data + "场", color);
				}

			}
		} else {
			// 这里来调整 到底如何显示
			int pos_1;
			int start_index_1;
			// 去除第一排
			if (curStartIndex >= 7) {
				frist_bool = false;
				start_index_1 = 7;
			} else {
				frist_bool = true;
				start_index_1 = 0;
			}
			// 去除最后一排
			if (42 - curEndIndex >= 7) {
				last_bool = false;
				pos_1 = 35;
			} else {
				last_bool = true;
				pos_1 = 42;
			}
			for (int i = start_index_1; i < pos_1; i++) {
				// int color = surface.textColor;
				// if (isLastMonth(i)) {
				// color = surface.borderColor;
				// } else if (isNextMonth(i)) {
				// color = surface.borderColor;
				// }
				// if (todayIndex != -1 && i == todayIndex) {
				// color = surface.todayNumberColor;
				// }
				// drawCellText(canvas, i, date[i] + "", color);

				int color = surface.textColor;
				if (isLastMonth(i)) {
					color = surface.borderColor;
				} else if (isNextMonth(i)) {
					color = surface.borderColor;
				}
				if (todayIndex == -1) {
					if (i < curStartIndex) {
						color = surface.borderColor;
					}
				}
				if (todayIndex != -1 && i == todayIndex) {
					color = surface.todayNumberColor;

					drawCellCircle(canvas, i, date[i] + "", color);
					drawCellText(canvas, i, date[i] + "", color);
					color = surface.todayNumber;

					// if (list == null || i > list.size() - 1) {
					// Log.i("ceshi", "当天日期===  " + list.size());
					// drawCellNumText(canvas, i, 0 + "", color);
					// } else {
					for (int j = 0; j < list.size(); j++) {
						String data_left = list.get(j).split(":")[0];
						data_left = data_left.replace("{", "");
						data_left = data_left.replace("}", "");
						data_left = data_left.substring(9, 11);
						// .replace("0","")

						int data_int = Integer.valueOf(data_left);
						if (date[i] == data_int) {
							drawCellNumText(canvas, i,
									list.get(j).split(":")[1].replace("}", "")
											+ "场", color);
						}
					}
					// drawCellNumText(canvas, i, list.get(i).split(":")[1]
					// + "场", color);
					// }

				} else {
					// color = surface.todayNumber;
					drawCellText(canvas, i, date[i] + "", color);
					int start_num = i - curStartIndex;
					if (list == null || start_num > list.size() - 1) {
						drawCellNumText(canvas, i, 0 + "", color);
					} else {
						// 减去上个月的几天的数据 留下来的就是现在对应数据库的数据
						if (i >= curStartIndex) {
							/**
							 * 添加场数的 drawText 这里要判断开始的哪天的场数
							 */

							for (int j = 0; j < list.size(); j++) {
								String data_left = list.get(j).split(":")[0];
								data_left = data_left.replace("{", "");
								data_left = data_left.replace("}", "");
								data_left = data_left.substring(9, 11);

								int data_int = Integer.valueOf(data_left);
								if (date[i] == data_int) {
									drawCellNumText(canvas, i, list.get(j)
											.split(":")[1].replace("}", "")
											+ "场", color);
								}
							}

							// drawCellNumText(canvas, i, data + "场", color);

						} else {
							drawCellNumText(canvas, i, 0 + "", color);
						}

					}

				}

			}
			if (MyApplication.line_data != 0) {
				LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 6);
				setLayoutParams(layoutParams);
				calendar_height = layoutParams.height;
			} else {
				LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 5);
				setLayoutParams(layoutParams);
				calendar_height = layoutParams.height;
			}

		}

		super.onDraw(canvas);
	}

	private void calculateDate() {
		Calendar c = Calendar.getInstance();
		String[] months = new String[30];

		for (int i = 0; i < 30; i++) {
			months[i] = new SimpleDateFormat("yyyy-MM-dd").format(new Date(c
					.getTimeInMillis()));
			c.add(Calendar.DAY_OF_MONTH, -1);
			// if (i == 0) {
			// lastday = months[i];
			// }
			// if (i == months.length - 1) {
			// firstday = months[i];
			// }
			// Log.i("ceshi", "moth== " + months[i]);
			// if (list.size() != 0 && list != null) {
			// if (list.get(i).split(":")[0].equals(months[i])) {
			// Log.i("ceshi", "moth== " + list.get(i).split(":")[1]);
			// }
			// }

		}

		calendar.setTime(curDate);
		// 这是当天的时间
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Log.d(TAG, "day in week:" + dayInWeek);
		int monthStart = dayInWeek;
		if (monthStart == 1) {
			monthStart = 8;
		}
		monthStart -= 1; // 以日为开头-1，以星期一为开头-2
		curStartIndex = monthStart;
		date[monthStart] = 1;
		// if (MyApplication.num_data == 7) {
		// //这个是判断当前的年和月份
		// int todayIndex;
		// String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
		// + calendar.get(Calendar.MONTH);
		// calendar.setTime(today);
		// String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
		// + calendar.get(Calendar.MONTH);
		// if (curYearAndMonth.equals(todayYearAndMonth)) {
		// int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
		// todayIndex = curStartIndex + todayNumber - 1;
		// }
		// int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		// // 这是要显示的日期
		// for (int i = 1; i < monthDay; i++) {
		// date[monthStart + i] = i + 1;
		// }
		// } else {
		// last month
		if (monthStart > 0) {
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
			// 这是末尾的日期
			for (int i = monthStart - 1; i >= 0; i--) {
				date[i] = dayInmonth;
				dayInmonth--;
			}
			calendar.set(Calendar.DAY_OF_MONTH, date[0]);
		}
		showFirstDate = calendar.getTime();
		// this month
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		// Log.d(TAG, "m:" + calendar.get(Calendar.MONTH) + " d:" +
		// calendar.get(Calendar.DAY_OF_MONTH));
		// monthDay 是表示这个月的天数
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		// 这是要显示的日期
		for (int i = 1; i < monthDay; i++) {
			date[monthStart + i] = i + 1;
		}
		curEndIndex = monthStart + monthDay;
		// next month
		// 这是下个月的多余的日期
		for (int i = monthStart + monthDay; i < 42; i++) {
			date[i] = i - (monthStart + monthDay) + 1;
		}
		if (curEndIndex < 42) {
			// 显示了下一月的
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.DAY_OF_MONTH, date[41]);
		showLastDate = calendar.getTime();
		// }

	}

	// /**
	// *
	// * @param canvas
	// * @param index
	// * @param color
	// */
	// private void drawCellBg(Canvas canvas, int index, int color) {
	// int x = getXByIndex(index);
	// int y = getYByIndex(index);
	// surface.cellBgPaint.setColor(color);
	// float left = surface.cellWidth * (x - 1) + surface.borderWidth;
	// float top = surface.monthHeight + surface.weekHeight + (y - 1)
	// * surface.cellHeight + surface.borderWidth;
	// canvas.drawRect(left, top, left + surface.cellWidth
	// - surface.borderWidth, top + surface.cellHeight
	// - surface.borderWidth, surface.cellBgPaint);
	// }

	private void drawDownOrSelectedBg(Canvas canvas) {
		// down and not up
		if (downDate != null) {
			drawCellBg(canvas, downIndex, surface.cellDownColor);
		}
		// selected bg color
		if (!selectedEndDate.before(showFirstDate)
				&& !selectedStartDate.after(showLastDate)) {
			int[] section = new int[] { -1, -1 };
			calendar.setTime(curDate);
			calendar.add(Calendar.MONTH, -1);
			findSelectedIndex(0, curStartIndex, calendar, section);
			if (section[1] == -1) {
				calendar.setTime(curDate);
				findSelectedIndex(curStartIndex, curEndIndex, calendar, section);
			}
			if (section[1] == -1) {
				calendar.setTime(curDate);
				calendar.add(Calendar.MONTH, 1);
				findSelectedIndex(curEndIndex, 42, calendar, section);
			}
			if (section[0] == -1) {
				section[0] = 0;
			}
			if (section[1] == -1) {
				section[1] = 41;
			}
			for (int i = section[0]; i <= section[1]; i++) {
				drawCellBg(canvas, i, surface.cellSelectedColor);
			}
		}
	}

	private void findSelectedIndex(int startIndex, int endIndex,
			Calendar calendar, int[] section) {
		for (int i = startIndex; i < endIndex; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, date[i]);
			Date temp = calendar.getTime();
			// Log.d(TAG, "temp:" + temp.toLocaleString());
			if (temp.compareTo(selectedStartDate) == 0) {
				section[0] = i;
			}
			if (temp.compareTo(selectedEndDate) == 0) {
				section[1] = i;
				return;
			}
		}
	}

	public Date getSelectedStartDate() {
		return selectedStartDate;
	}

	public Date getSelectedEndDate() {
		return selectedEndDate;
	}

	private boolean isLastMonth(int i) {

		if (i < todayIndex) {
			return true;
		}
		return false;
	}

	private boolean isNextMonth(int i) {
		if (i >= curEndIndex) {
			return true;
		}
		return false;
	}

	private int getXByIndex(int i) {
		return i % 7 + 1; // 1 2 3 4 5 6 7
	}

	private int getYByIndex(int i) {
		return i / 7 + 1; // 1 2 3 4 5 6
	}

	// 获得当前应该显示的年月
	public String getYearAndmonth() {
		calendar.setTime(curDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return year + "-" + month;
	}

	public String return_calendar() {

		calendar.setTime(date1);
		curDate = calendar.getTime();
		invalidate();
		return getYearAndmonth();
	}

	// 上一月
	public String clickLeftMonth() {
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, -1);
		curDate = calendar.getTime();
		invalidate();

		return getYearAndmonth();
	}

	// 下一月
	public String clickRightMonth() {
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		curDate = calendar.getTime();
		invalidate();

		return getYearAndmonth();
	}

	// 设置日历时间
	public void setCalendarData(Date date) {
		calendar.setTime(date);
		invalidate();
	}

	// 获取日历时间
	public void getCalendatData() {
		calendar.getTime();
	}

	// 设置是否多选
	public boolean isSelectMore() {
		return isSelectMore;
	}

	public void setSelectMore(boolean isSelectMore) {
		this.isSelectMore = isSelectMore;
	}

	private void setSelectedDateByCoor(float x, float y) {
		// change month
		// if (y < surface.monthHeight) {
		// // pre month
		// if (x < surface.monthChangeWidth) {
		// calendar.setTime(curDate);
		// calendar.add(Calendar.MONTH, -1);
		// curDate = calendar.getTime();
		// }
		// // next month
		// else if (x > surface.width - surface.monthChangeWidth) {
		// calendar.setTime(curDate);
		// calendar.add(Calendar.MONTH, 1);
		// curDate = calendar.getTime();
		// }
		// }
		// cell click down
		float weekh;
		float cellh;

		if (MyApplication.num_data == 7) {
			// 每一个item的宽度

			seven_y = (int) ((getResources().getDisplayMetrics().heightPixels * 2 / 20) / 2) - 5;
			if (x < item_width) {
				seven_x = item_width / 2;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index]);
				downIndex = start_index;
			} else if (x < item_width * 2 && item_width < x) {
				seven_x = (item_width / 2) + item_width;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index + 1]);
				downIndex = start_index + 1;
			} else if (x < item_width * 3 && item_width * 2 < x) {
				seven_x = (item_width / 2) + item_width * 2;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index + 2]);
				downIndex = start_index + 2;
			} else if (x < item_width * 4 && item_width * 3 < x) {
				seven_x = (item_width / 2) + item_width * 3;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index + 3]);
				downIndex = start_index + 3;
			} else if (x < item_width * 5 && item_width * 4 < x) {
				seven_x = (item_width / 2) + item_width * 4;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index + 4]);
				downIndex = start_index + 4;
			} else if (x < item_width * 6 && item_width * 5 < x) {
				seven_x = (item_width / 2) + item_width * 5;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index + 5]);
				downIndex = start_index + 5;
			} else if (x < item_width * 7 && item_width * 6 < x) {
				seven_x = (item_width / 2) + item_width * 6;
				pandding_right = 3;
				calendar.set(Calendar.DAY_OF_MONTH, date[start_index + 6]);
				downIndex = start_index + 6;

			}
			calendar.setTime(curDate);
			if (isLastMonth(downIndex)) {
				calendar.add(Calendar.MONTH, -1);
			} else if (isNextMonth(downIndex)) {
				calendar.add(Calendar.MONTH, 1);
			}
			downDate = calendar.getTime();
			Log.i("ceshi", "downIndex2222==  " + downIndex + "  downDate==  "
					+ downDate + " date==    " + date[downIndex]);
		} else {
			if (y > surface.monthHeight + surface.weekHeight) {

				// 这是行
				int m = (int) (Math.floor(x / surface.cellWidth) + 1);
				// 这是列
				int n = (int) (Math
						.floor((y - (surface.monthHeight + surface.weekHeight))
								/ Float.valueOf(surface.cellHeight)) + 1);

				downIndex = (n - 1) * 7 + m - 1;
				if (MyApplication.line_data == 1) {
					downIndex = downIndex + 7;
				} else if (MyApplication.line_data == 2) {

				} else {

				}

				Log.d(TAG, "downIndex:" + downIndex);
				calendar.setTime(curDate);
				if (isLastMonth(downIndex)) {
					calendar.add(Calendar.MONTH, -1);
				} else if (isNextMonth(downIndex)) {
					calendar.add(Calendar.MONTH, 1);
				}
				calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);
				downDate = calendar.getTime();
				Log.i("ceshi", "downIndex==  " + downIndex + "  downDate==  "
						+ downDate + " date==    " + date[downIndex]);
			}
		}

		invalidate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setSelectedDateByCoor(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			if (downDate != null) {
				if (isSelectMore) {
					if (!completed) {
						if (downDate.before(selectedStartDate)) {
							selectedEndDate = selectedStartDate;
							selectedStartDate = downDate;
						} else {
							selectedEndDate = downDate;
						}
						completed = true;
						// 响应监听事件
						onItemClickListener.OnItemClick(selectedStartDate,
								selectedEndDate, downDate);
					} else {
						selectedStartDate = selectedEndDate = downDate;
						completed = false;
					}
				} else {
					selectedStartDate = selectedEndDate = downDate;
					// 响应监听事件
					onItemClickListener.OnItemClick(selectedStartDate,
							selectedEndDate, downDate);
				}
				invalidate();
			}

			break;
		}
		return true;
	}

	// 给控件设置监听事件
	public void setOnItemClickListener_calendar(
			OnItemClickListener_calendar onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	// 监听接口
	public interface OnItemClickListener_calendar {
		void OnItemClick(Date selectedStartDate, Date selectedEndDate,
				Date downDate);
	}

	/**
	 * 这是场数的textview
	 * 
	 * @param canvas
	 * @param index
	 * @param text
	 */
	private void drawCellNumText(Canvas canvas, int index, String text,
			int color) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.dateNumPaint.setColor(color);
		float cellY = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight * 3 / 5f;
		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth - surface.datePaint.measureText(text))
				/ 2f;

		if (MyApplication.num_data == 7) {
			if (todayIndex == -1) {
				canvas.drawText(text, cellX, cellY + y_cricle / 15,
						surface.dateNumPaint);
			} else {
				if (MyApplication.line_data == 1) {
					canvas.drawText(text, cellX, cellY + y_cricle / 180,
							surface.dateNumPaint);
				}
				if (MyApplication.line_data == 2) {
					canvas.drawText(text, cellX, cellY + y_cricle / 50,
							surface.dateNumPaint);
				}
				if (MyApplication.line_data == 0) {
					canvas.drawText(text, cellX, cellY + y_cricle / 50,
							surface.dateNumPaint);
				}

			}

		} else {
			if (MyApplication.line_data == 1) {
				canvas.drawText(text, cellX + 5, cellY - y_cricle / 25,
						surface.dateNumPaint);
			}
			if (MyApplication.line_data == 2) {
				canvas.drawText(text, cellX + 5, cellY + y_cricle / 100,
						surface.dateNumPaint);
			}
			if (MyApplication.line_data == 0) {
				canvas.drawText(text, cellX + 5, cellY + y_cricle / 60,
						surface.dateNumPaint);
			}

		}

	}

	/**
	 * data 日期
	 * 
	 * @param canvas
	 * @param index
	 * @param text
	 */
	private void drawCellText(Canvas canvas, int index, String text, int color) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.datePaint.setColor(color);

		float cellY = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight * 3 / 5f;

		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth - surface.datePaint.measureText(text))
				/ 2f;
		if (MyApplication.num_data == 7) {
			// 判断是否是本月
			if (todayIndex == -1) {
				canvas.drawText(text, cellX, cellY + y_cricle / 25,
						surface.datePaint);
			} else {
				if (MyApplication.line_data == 1) {
					canvas.drawText(text, cellX, cellY - y_cricle / 40,
							surface.datePaint);
				}
				if (MyApplication.line_data == 2) {
					canvas.drawText(text, cellX, cellY + y_cricle / 130,
							surface.datePaint);
				}
				if (MyApplication.line_data == 0) {
					canvas.drawText(text, cellX, cellY + y_cricle / 130,
							surface.datePaint);
				}

			}

		} else {
			if (MyApplication.line_data == 1) {
				canvas.drawText(text, cellX, cellY - y_cricle / 15,
						surface.datePaint);
			}
			if (MyApplication.line_data == 2) {
				canvas.drawText(text, cellX, cellY - y_cricle / 100,
						surface.datePaint);
			}
			if (MyApplication.line_data == 0) {
				canvas.drawText(text, cellX, cellY - y_cricle / 85,
						surface.datePaint);
			}

		}

	}

	/**
	 * 这个是选中的圆圈
	 * 
	 * @param canvas
	 * @param index
	 * @param color
	 */
	private void drawCellBg(Canvas canvas, int index, int color) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.cellBgPaint.setColor(color);
		float left = surface.cellWidth * (x - 1) + surface.borderWidth;
		float top = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.borderWidth;
		float circle_x = (left + surface.cellWidth - surface.borderWidth - left) / 2;
		float circle_y = (top + surface.cellHeight - surface.borderWidth - top) / 2;
		float cellY = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight * 3 / 5f;
		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth - surface.datePaint.measureText("20"))
				/ 2f;
		if (MyApplication.num_data == 7) {
			if (select_cricle) {

				if (seven_x == 0.0 || seven_y == 0.0) {
					seven_x = item_width / 2;
					seven_y = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 20) / 2;
				}
				if (MyApplication.line_data == 1) {
					canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle
							/ 34, y_cricle / 75, surface.cellBgPaint);
				}
				if (MyApplication.line_data == 2) {
					canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle
							/ 50, y_cricle / 75, surface.cellBgPaint);
				}
				if (MyApplication.line_data == 0) {
					canvas.drawCircle(cellX + x_cricle / 48, cellY + y_cricle
							/ 400, y_cricle / 75, surface.cellBgPaint);
				}
			} else {
				canvas.drawCircle(0, 0, 0, surface.cellBgPaint);
			}
		} else {
			if (MyApplication.line_data == 1) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 13,
						y_cricle / 75, surface.cellBgPaint);
			}
			if (MyApplication.line_data == 2) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 50,
						y_cricle / 75, surface.cellBgPaint);
			}
			if (MyApplication.line_data == 0) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 50,
						y_cricle / 75, surface.cellBgPaint);
			}

		}

	}

	/**
	 * 这是现在的日期
	 * 
	 * @param canvas
	 * @param index
	 * @param text
	 */
	private void drawCellCircle(Canvas canvas, int index, String text, int color) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.datePaint_circle.setColor(getResources().getColor(
				R.color.text_color_67));
		float left = surface.cellWidth * (x - 1) + surface.borderWidth;
		float top = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.borderWidth;
		float circle_x = (left + surface.cellWidth - surface.borderWidth - left) / 2;
		float circle_y = (top + surface.cellHeight - surface.borderWidth - top) / 2;
		float cellY = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight * 3 / 5f;

		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth - surface.datePaint.measureText("20"))
				/ 2f;
		if (MyApplication.num_data == 7) {
			if (MyApplication.line_data == 1) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 34,
						y_cricle / 75, surface.datePaint_circle);
			}
			if (MyApplication.line_data == 2) {
				canvas.drawCircle(cellX + x_cricle / 48,
						cellY + y_cricle / 400, y_cricle / 75,
						surface.datePaint_circle);
			}
			if (MyApplication.line_data == 0) {
				canvas.drawCircle(cellX + x_cricle / 48,
						cellY + y_cricle / 400, y_cricle / 75,
						surface.datePaint_circle);
			}

		} else {
			if (MyApplication.line_data == 1) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 13,
						y_cricle / 75, surface.datePaint_circle);
			}
			if (MyApplication.line_data == 2) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 50,
						y_cricle / 75, surface.datePaint_circle);
			}
			if (MyApplication.line_data == 0) {
				canvas.drawCircle(cellX + x_cricle / 48, cellY - y_cricle / 50,
						y_cricle / 75, surface.datePaint_circle);
			}

			// canvas.drawCircle(left + circle_x - 5, top + circle_y -
			// margin_top
			// + circle_num - 5, circle_y - 30, surface.datePaint_circle);
		}

	}

	/**
	 * 
	 * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
	 */
	private class Surface {
		public float density;
		public int width; // 整个控件的宽度
		public int height; // 整个控件的高度
		public float monthHeight; // 显示月的高度
		// public float monthChangeWidth; // 上一月、下一月按钮宽度
		public float weekHeight; // 显示星期的高度
		public float cellWidth; // 日期方框宽度
		public float cellHeight; // 日期方框高度
		public float borderWidth;
		private int textColor = getResources().getColor(R.color.text_color_26);
		private int textNumColor = getResources().getColor(
				R.color.text_color_99);
		public int bgColor = Color.parseColor("#FFFFFF");
		// private int textColorUnimportant = Color.parseColor("#666666");
		private int btnColor = Color.parseColor("#666666");
		private int borderColor = Color.parseColor("#CCCCCC");
		public int todayNumberColor = Color.WHITE;
		public int todayNumber = Color.BLACK;
		/**
		 * 点击时候的颜色
		 */
		public int cellDownColor = Color.parseColor("#CCFFFF");
		/**
		 * 点击后的颜色
		 */
		public int cellSelectedColor = getResources().getColor(
				R.color.text_color_26);
		public Paint borderPaint;
		public Paint monthPaint;
		public Paint weekPaint;
		public Paint datePaint_circle;
		public Paint dateNumPaint;

		public Paint datePaint;
		public Paint monthChangeBtnPaint;
		public Paint cellBgPaint;
		public Path boxPath; // 边框路径
		// public Path preMonthBtnPath; // 上一月按钮三角形
		// public Path nextMonthBtnPath; // 下一月按钮三角形
		public String[] weekText = { "日", "一", "二", "三", "四", "五", "六" };

		// public String[] monthText =
		// {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

		public void init() {
			float temp = height / 7f;
			monthHeight = 0;// (float) ((temp + temp * 0.3f) * 0.6);
			// monthChangeWidth = monthHeight * 1.5f;
			weekHeight = (float) ((temp + temp * 0.3f) * 0.7);
			if (MyApplication.line_data != 0) {
				cellHeight = (height - monthHeight - weekHeight) / 5f;
			} else {
				cellHeight = (height - monthHeight - weekHeight) / 6f;
			}

			cellWidth = width / 7f;
			float cellTextSize = cellHeight * 0.5f;
			borderPaint = new Paint();
			borderPaint.setColor(borderColor);
			borderPaint.setStyle(Paint.Style.STROKE);
			borderWidth = (float) (0.5 * density);
			// Log.d(TAG, "borderwidth:" + borderWidth);
			borderWidth = borderWidth < 1 ? 1 : borderWidth;
			borderPaint.setStrokeWidth(borderWidth);
			monthPaint = new Paint();
			monthPaint.setColor(textColor);
			monthPaint.setAntiAlias(true);
			float textSize = cellHeight * 0.4f;
			Log.d(TAG, "text size:" + textSize);
			monthPaint.setTextSize(23);
			monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
			weekPaint = new Paint();
			weekPaint.setColor(textColor);
			weekPaint.setAntiAlias(true);

			float weekTextSize = weekHeight * 0.4f;
			if (MyApplication.num_data == 7) {
				weekTextSize = weekTextSize * 4.0f;
				cellTextSize = cellTextSize * 4.0f;
			}
			weekPaint.setTextSize(y_cricle / 40);
			weekPaint.setTypeface(Typeface.DEFAULT_BOLD);
			datePaint_circle = new Paint();
			datePaint_circle.setAntiAlias(true);
			datePaint_circle.setStyle(Paint.Style.FILL);
			datePaint_circle.setTypeface(MyApplication.GetTypeFace());
			datePaint_circle.setColor(cellSelectedColor);
			dateNumPaint = new Paint();
			dateNumPaint.setColor(textNumColor);
			dateNumPaint.setTypeface(MyApplication.GetTypeFace());
			dateNumPaint.setAntiAlias(true);
			dateNumPaint.setTextSize(y_cricle / 50);
			dateNumPaint.setTypeface(Typeface.DEFAULT_BOLD);
			datePaint = new Paint();
			datePaint.setColor(textColor);
			datePaint.setAntiAlias(true);
			datePaint.setTypeface(MyApplication.GetTypeFace());
			datePaint.setTextSize(y_cricle / 50);
			datePaint.setTypeface(Typeface.DEFAULT_BOLD);
			boxPath = new Path();
			// boxPath.addRect(0, 0, width, height, Direction.CW);
			// boxPath.moveTo(0, monthHeight);
			boxPath.rLineTo(width, 0);
			if (MyApplication.num_data == 7) {
				boxPath.moveTo(0, monthHeight + weekHeight + y_cricle / 40);
				boxPath.rLineTo(width, 0);
			} else {
				boxPath.moveTo(0, monthHeight + weekHeight);
				boxPath.rLineTo(width, 0);
			}

			// for (int i = 1; i < 6; i++) {
			// boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);
			// boxPath.rLineTo(width, 0);
			// boxPath.moveTo(i * cellWidth, monthHeight);
			// boxPath.rLineTo(0, height - monthHeight);
			// }
			// boxPath.moveTo(6 * cellWidth, monthHeight);
			// boxPath.rLineTo(0, height - monthHeight);
			// preMonthBtnPath = new Path();
			// int btnHeight = (int) (monthHeight * 0.6f);
			// preMonthBtnPath.moveTo(monthChangeWidth / 2f, monthHeight / 2f);
			// preMonthBtnPath.rLineTo(btnHeight / 2f, -btnHeight / 2f);
			// preMonthBtnPath.rLineTo(0, btnHeight);
			// preMonthBtnPath.close();
			// nextMonthBtnPath = new Path();
			// nextMonthBtnPath.moveTo(width - monthChangeWidth / 2f,
			// monthHeight / 2f);
			// nextMonthBtnPath.rLineTo(-btnHeight / 2f, -btnHeight / 2f);
			// nextMonthBtnPath.rLineTo(0, btnHeight);
			// nextMonthBtnPath.close();
			monthChangeBtnPaint = new Paint();
			monthChangeBtnPaint.setAntiAlias(true);
			monthChangeBtnPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			monthChangeBtnPaint.setTypeface(MyApplication.GetTypeFace());
			monthChangeBtnPaint.setColor(btnColor);
			cellBgPaint = new Paint();
			cellBgPaint.setAntiAlias(true);
			cellBgPaint.setTypeface(MyApplication.GetTypeFace());
			cellBgPaint.setStyle(Paint.Style.STROKE);
			cellBgPaint.setColor(cellSelectedColor);
		}
	}

	public void change_view() {
		invalidate();

	}

	/**
	 * 由小变大
	 */
	public void startAnimation() {
		if (animation_start) {
			animation_start = false;
		} else {
			return;
		}
		if (MyApplication.line_data != 0) {
			animator = ValueAnimator
					.ofInt((int) (getResources().getDisplayMetrics().heightPixels * 2 / 20),
							(int) (getResources().getDisplayMetrics().heightPixels * 2 / 6));
		} else {
			animator = ValueAnimator
					.ofInt((int) (getResources().getDisplayMetrics().heightPixels * 2 / 20),
							(int) (getResources().getDisplayMetrics().heightPixels * 2 / 5));
		}

		animator.setDuration(100);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// height_num = (Integer) valueAnimator.getAnimatedValue();
				// surface.height = height_num;
				// requestLayout();
				// invalidate();
				LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				calendar_height = layoutParams.height;
				layoutParams.height = (Integer) valueAnimator
						.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});
		animator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				MyApplication.num_data = 42;
				select_cricle = true;
				animation_item = true;
				animation_start = false;
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				animation_item = true;
				animation_start = true;
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		animator.start();

	}

	/**
	 * 由大变小
	 */
	public void startAnimation_seven() {
		if (animation_start) {
			animation_start = false;
		} else {
			return;
		}
		if (MyApplication.line_data == 1 || MyApplication.line_data == 2) {
			animator = ValueAnimator
					.ofInt((int) (getResources().getDisplayMetrics().heightPixels * 2 / 6),
							(int) (getResources().getDisplayMetrics().heightPixels * 2 / 20));
		} else {
			animator = ValueAnimator
					.ofInt((int) (getResources().getDisplayMetrics().heightPixels * 2 / 5),
							(int) (getResources().getDisplayMetrics().heightPixels * 2 / 20));
		}

		animator.setDuration(100);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// height_num = (Integer) valueAnimator.getAnimatedValue();
				// surface.height = height_num;
				// requestLayout();
				// invalidate();
				LayoutParams layoutParams = (LayoutParams) getLayoutParams();
				calendar_height = layoutParams.height;
				layoutParams.height = (Integer) valueAnimator
						.getAnimatedValue();
				setLayoutParams(layoutParams);
			}
		});
		animator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				MyApplication.num_data = 7;
				// 这里要去掉选中的日期
				select_cricle = false;
				animation_item = false;
				animation_start = false;
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				animation_item = false;
				animation_start = true;
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		animator.start();

	}
}
