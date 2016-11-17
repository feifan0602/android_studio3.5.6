package com.sun3d.culturalShanghai.calender;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sun3d.culturalShanghai.MyApplication;
import com.sun3d.culturalShanghai.R;
import com.sun3d.culturalShanghai.activity.EventReserveActivity;

/**
 * 日历gridview中的每一个item显示的textview
 * 
 * @author lmw
 * 
 */
public class CalendarAdapter extends BaseAdapter {
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private Context context;
	/**
	 * 判断一个月全部显示时候的 前面几项数据的灰色显示
	 */
	private boolean frist = true;
	private String[] dayNumber = new String[42]; // 一个gridview中的日期存入此数组中
	private SpecialCalendar sc = null;
	private LunarCalendar lc = null;
	private Resources res = null;
	private Drawable drawable = null;
	private boolean change = true;
	private boolean change_start = false;
	private String currentYear = "";
	private String currentMonth = "";
	private String currentDay = "";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private int currentFlag = 1; // 用于标记当天
	private List<Integer> lastMouth = new ArrayList<Integer>();
	private List<Integer> nextMouth = new ArrayList<Integer>();
	private boolean isRecord = false;

	public int getCurrentFlag() {
		return currentFlag;
	}

	public int getSysMonth() {
		return Integer.parseInt(sys_month);
	}

	private int[] schDateTagFlag = null; // 存储当月所有的日程日期

	private String showYear = ""; // 用于在头部显示的年份
	private String showMonth = ""; // 用于在头部显示的月份
	private String animalsYear = "";
	private String leapMonth = ""; // 闰哪一个月
	private String cyclical = ""; // 天干地支
	// 系统当前时间
	private String sysDate = "";
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";

	private String sys_year_now = "";
	private String sys_month_now = "";
	private String sys_day_now = "";

	public CalendarAdapter() {
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year_now = sysDate.split("-")[0];
		sys_month_now = sysDate.split("-")[1];
		sys_day_now = sysDate.split("-")[2];
		sys_year = MyApplication.StartTime.split("-")[0];
		sys_month = MyApplication.StartTime.split("-")[1];
		sys_day = MyApplication.StartTime.split("-")[2];

	}

	public CalendarAdapter(Context context, Resources rs, int jumpMonth,
			int jumpYear, int year_c, int month_c, int day_c) {
		this();

		this.context = context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		this.res = rs;

		int stepYear = year_c + jumpYear;
		int stepMonth = month_c + jumpMonth;

		if (stepMonth > 0) { // 往下一个月滑动
			if (stepMonth % 12 == 0) {
				stepYear = year_c + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = year_c + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else { // 往上一个月滑动
			stepYear = year_c - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {

			}
		}

		currentYear = String.valueOf(stepYear);
		// 得到当前的年份
		currentMonth = String.valueOf(stepMonth); // 得到本月
													// （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
		currentDay = String.valueOf(day_c); // 得到当前日期是哪天

		getCalendar(Integer.parseInt(currentYear),
				Integer.parseInt(currentMonth));

	}

	public CalendarAdapter(Context context, Resources rs, int year, int month,
			int day) {
		this();
		this.context = context;
		sc = new SpecialCalendar();
		lc = new LunarCalendar();
		this.res = rs;
		currentYear = String.valueOf(year);
		// 得到跳转到的年份
		currentMonth = String.valueOf(month); // 得到跳转到的月份
		currentDay = String.valueOf(day); // 得到跳转到的天
		getCalendar(Integer.parseInt(currentYear),
				Integer.parseInt(currentMonth));

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_calendar_item, null);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.tvtext);
		String d = dayNumber[position].split("\\.")[0];
		Log.d("date:" + position, showYear + ",," + showMonth + ",," + d);
		// 添加阴历
		// String dv = dayNumber[position].split("\\.")[1];
		// SpannableString sp = new SpannableString(d + "\n" + dv);
		SpannableString sp = new SpannableString(d);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
				d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f), 0, d.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// if (dv != null || dv != "") {
		// sp.setSpan(new RelativeSizeSpan(0.75f), d.length() + 1,
		// dayNumber[position].length(),
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// }
		textView.setText(sp);
		textView.setTextColor(Color.WHITE);

		if (schDateTagFlag != null && schDateTagFlag.length > 0) {
			for (int i = 0; i < schDateTagFlag.length; i++) {
				if (schDateTagFlag[i] == position) {
					// 设置日程标记背景
					textView.setBackgroundResource(R.drawable.mark);
				}
			}
		}

		if (MyApplication.isReserverTimeDate) {
			if ("1".equals(d)) {
				isRecord = true;
			}
			setStartAndEnd(position, d, textView);
		} else {
			if (position < currentFlag || lastMouth.indexOf(position) >= 0) {
				// textView.setTextColor(0xffd2d2d2);
				textView.setTextColor(context.getResources().getColor(
						R.color.white_color));
			} else if (position == currentFlag) {
				textView.setTextColor(context.getResources().getColor(
						R.color.white_color));
			}
		}
		String scheduleMonth = "";
		int month = Integer.parseInt(showMonth);
		if (month < 10) {
			scheduleMonth = "0" + showMonth;
		} else {
			scheduleMonth = showMonth;
		}
		String scheduleDay = "";
		int day = Integer.parseInt(d);
		if (day < 10) {
			scheduleDay = "0" + d;
		} else {
			scheduleDay = d;
		}
		String today = sys_year + "-" + scheduleMonth + "-" + scheduleDay;
		if (CalendarActivity.clickDateStrng != null
				&& today.equals(CalendarActivity.clickDateStrng)
				&& lastMouth.indexOf(position) == -1 && position >= currentFlag) {
			// convertView.setBackgroundColor(context.getResources().getColor(R.color.ac));
			CalendarActivity.setinitselectDay(position, today);
		}
		Log.d("DATA:", CalendarActivity.clickDateStrng + "---" + today);
		setnextmouth(position, textView, convertView);
		return convertView;
	}

	/**
	 * @param index
	 * @param day
	 * @param tv
	 */
	private void setStartAndEnd(int index, String day, TextView tv) {
		Log.i("date", day + ":" + index);
		tv.setTextColor(0xffd2d2d2);
		tv.setBackgroundColor(Color.TRANSPARENT);
		// 新的判断 StartTimeNum 为活动开始的时间 NowTime 为现在的时间 EndTime 为活动结束的时间
		// ShowTime为点击现在的
		String start_mouth = "";
		String start_day = "";
		String end_mouth = "";
		String end_day = "";
		String show_mouth = "";
		String show_day = "";
		if (EventReserveActivity.startTime[1] < 10) {
			start_mouth = "0"
					+ String.valueOf(EventReserveActivity.startTime[1]);
		} else {
			start_mouth = String.valueOf(EventReserveActivity.startTime[1]);
		}
		if (EventReserveActivity.startTime[2] < 10) {
			start_day = "0" + String.valueOf(EventReserveActivity.startTime[2]);

		} else {
			start_day = String.valueOf(EventReserveActivity.startTime[2]);
		}
		int StartTimeNum = Integer.valueOf(EventReserveActivity.startTime[0]
				+ "" + start_mouth + "" + start_day);

		int NowTime = Integer.valueOf(sys_year_now + "" + sys_month_now + ""
				+ sys_day_now);

		if (EventReserveActivity.endTime[1] < 10) {
			end_mouth = "0" + String.valueOf(EventReserveActivity.endTime[1]);
		} else {
			end_mouth = String.valueOf(EventReserveActivity.endTime[1]);
		}
		if (EventReserveActivity.endTime[2] < 10) {
			end_day = "0" + String.valueOf(EventReserveActivity.endTime[2]);

		} else {
			end_day = String.valueOf(EventReserveActivity.endTime[2]);
		}

		int EndTimeNum = Integer.valueOf(EventReserveActivity.endTime[0] + ""
				+ end_mouth + "" + end_day);
		if (Integer.valueOf(showMonth) < 10) {
			show_mouth = "0" + showMonth;
		} else {
			show_mouth = showMonth;
		}
		if (Integer.valueOf(day) < 10) {
			show_day = "0" + day;

		} else {
			show_day = day;
		}
		int ShowTime = Integer.valueOf(showYear + "" + show_mouth + ""
				+ show_day);
		// 开始的时间大于现在的时间
		if (ShowTime >= NowTime && ShowTime <= EndTimeNum
				&& ShowTime >= StartTimeNum && lastMouth.indexOf(index) == -1
				&& index >= currentFlag) {
			tv.setTextColor(context.getResources()
					.getColor(R.color.white_color));
			tv.setBackgroundResource(R.drawable.shape_orger_oval);
		}
		// if (EventReserveActivity.startTime[0] ==
		// EventReserveActivity.endTime[0]) {
		// if (EventReserveActivity.startTime[0] == Integer.parseInt(showYear))
		// {
		// // 这个是开始的月份的参数
		// // 如果开始的时间大于 现在的时间
		// if (EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)
		// && EventReserveActivity.startTime[1] <= Integer
		// .parseInt(sys_month_now)) {
		// // 跨日期 day显示为 1
		// change_start = true;
		// if (EventReserveActivity.startTime[2] <= Integer
		// .parseInt(day)
		// && lastMouth.indexOf(index) == -1
		// && index >= currentFlag
		// && Integer.parseInt(day) >= Integer
		// .parseInt(sys_day_now)) {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		//
		// } else {
		//
		// }
		// } else
		// // 开始的时间和结束的时间是同一个月的
		// if (EventReserveActivity.startTime[1] > Integer
		// .parseInt(sys_month_now)
		// && EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)
		// && EventReserveActivity.startTime[2] <= Integer
		// .parseInt(day)
		// && EventReserveActivity.endTime[1] >= Integer
		// .parseInt(showMonth)
		// && lastMouth.indexOf(index) == -1
		// && index >= currentFlag
		// && EventReserveActivity.endTime[2] >= Integer
		// .parseInt(day)) {
		//
		// change_start = true;
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// } else if (EventReserveActivity.startTime[1] > Integer
		// .parseInt(sys_month_now)
		// && EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)
		// && EventReserveActivity.endTime[1] == Integer
		// .parseInt(showMonth)
		// && Integer.parseInt(day) >= EventReserveActivity.startTime[2]
		// && Integer.parseInt(day) <= EventReserveActivity.endTime[2]) {
		// change_start = true;
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// } else if (EventReserveActivity.startTime[1] > Integer
		// .parseInt(sys_month_now)
		// && EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)
		// && EventReserveActivity.endTime[1] == Integer
		// .parseInt(showMonth)
		// && Integer.parseInt(day) < EventReserveActivity.startTime[2]) {
		// change_start = true;
		// tv.setTextColor(0xffd2d2d2);
		// } else if (EventReserveActivity.startTime[1] > Integer
		// .parseInt(sys_month_now)
		// && EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)
		// && EventReserveActivity.endTime[1] != Integer
		// .parseInt(showMonth)
		// && Integer.parseInt(day) > EventReserveActivity.startTime[2]
		// && lastMouth.indexOf(index) == -1) {
		// change_start = true;
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// }
		// }
		//
		// if (EventReserveActivity.endTime[0] == Integer.parseInt(showYear)
		// && change_start == false) {
		// // 这个是结束时间和当天滑动的月份的对比
		// if (EventReserveActivity.endTime[1] == Integer
		// .parseInt(showMonth)) {
		// // 这是是结束时间大于遍历的时间
		//
		// if (EventReserveActivity.endTime[2] >= Integer
		// .parseInt(day)
		// && EventReserveActivity.startTime[2] <= Integer
		// .parseInt(day)
		// && EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)) {
		// if (change_start) {
		// tv.setTextColor(0xffd2d2d2);
		// } else {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// change = false;
		// }
		//
		// } else if (EventReserveActivity.endTime[2] >= Integer
		// .parseInt(day)
		// && EventReserveActivity.endTime[1] == Integer
		// .parseInt(showMonth)
		// && lastMouth.indexOf(index) == -1) {
		// if (change_start) {
		// tv.setTextColor(0xffd2d2d2);
		// } else {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// change = false;
		// }
		// } else {
		// // 这个是开始的时间日 小于遍历的时间日
		// if (EventReserveActivity.startTime[2] <= Integer
		// .parseInt(day) || isRecord) {
		// Date date = new Date(System.currentTimeMillis());
		// SimpleDateFormat sdf = new SimpleDateFormat(
		// "yyyy-MM-dd");
		// String currentTime = sdf.format(date);
		// String[] currentData = MyApplication.StartTime
		// .split("-");
		// if (currentData.length > 2
		// && currentData[2] != null
		// && !"".equals(currentData[2]) && change
		// && change_start) {
		// Log.i("date6", day + ":" + index);
		// // 这个是判断 只是当月的 还是不只是当月的
		// if (Integer.parseInt(showMonth) == EventReserveActivity.endTime[1]) {
		// if (Integer.parseInt(day) >= Integer
		// .parseInt(currentData[2])
		// && isRecord
		// && EventReserveActivity.endTime[2] >= Integer
		// .parseInt(day)) {
		// tv.setTextColor(context.getResources()
		// .getColor(R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// } else {
		// tv.setTextColor(0xffd2d2d2);
		// tv.setBackgroundColor(Color.TRANSPARENT);
		// }
		// } else {
		// if (Integer.parseInt(day) >= Integer
		// .parseInt(currentData[2])
		// && isRecord) {
		// tv.setTextColor(context.getResources()
		// .getColor(R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// } else {
		// tv.setTextColor(0xffd2d2d2);
		// tv.setBackgroundColor(Color.TRANSPARENT);
		// }
		// }
		// // &&EventReserveActivity.endTime[2]>=Integer.parseInt(day)
		//
		// }
		//
		// } else {
		//
		// }
		// }
		// } else if (EventReserveActivity.endTime[1] > Integer
		// .parseInt(showMonth)
		// && change_start == false
		// && Integer.parseInt(showMonth) > EventReserveActivity.startTime[1]) {
		//
		// int day_num = Integer.valueOf(day);
		//
		// if (day_num == 1) {
		// Log.i("ceshi", "看看显示的日期 == " + day);
		// frist = false;
		// }
		// if (day_num < 32 && frist) {
		// Log.i("ceshi", "看看显示的日期000 == " + day);
		// tv.setTextColor(0xffd2d2d2);
		// tv.setBackgroundColor(Color.TRANSPARENT);
		// } else {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// }
		//
		// }
		//
		// }
		// } else {
		// Log.i("ceshi", "看看显示的日期00000 == " + day);
		// tv.setBackgroundColor(Color.TRANSPARENT);
		// // 这个是判断 结束那年和当前年一样
		// if (EventReserveActivity.endTime[0] == Integer.parseInt(showYear)) {
		// //这是是最后一年的月
		// if (EventReserveActivity.endTime[1] < Integer
		// .parseInt(showMonth)) {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// } else
		//
		// if (EventReserveActivity.endTime[1] == Integer
		// .parseInt(showMonth) && index >= currentFlag) {
		// if (EventReserveActivity.endTime[2] < Integer.parseInt(day)) {
		// tv.setTextColor(0xffd2d2d2);
		// } else {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// tv.setBackgroundResource(R.drawable.shape_orger_oval);
		// }
		// } else {
		// if (lastMouth.indexOf(index) == -1 && index >= currentFlag) {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.light_blue));
		// }
		//
		// }
		// } else {
		// if (EventReserveActivity.startTime[0] == Integer
		// .parseInt(showYear)) {
		// if (EventReserveActivity.startTime[1] == Integer
		// .parseInt(showMonth)) {
		// if (EventReserveActivity.startTime[2] <= Integer
		// .parseInt(day)
		// && lastMouth.indexOf(index) == -1
		// && index >= currentFlag) {
		// tv.setTextColor(context.getResources().getColor(
		// R.color.white_color));
		// //
		// tv.setTextColor(context.getResources().getColor(R.color.light_blue));
		// }
		// }
		// }
		// }
		// }

	}

	private void setnextmouth(int dex, TextView tv, View convertView) {
		for (int i : nextMouth) {
			if (dex == i) {
				tv.setTextColor(0xffd2d2d2);
				tv.setBackgroundColor(Color.TRANSPARENT);
				convertView.setBackgroundColor(context.getResources().getColor(
						R.color.activity_bg_color));
			}

		}
	}

	// 得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1); // 上一个月的总天数
		Log.d("DAY", isLeapyear + " ======  " + daysOfMonth
				+ "  ============  " + dayOfWeek + "  =========   "
				+ lastDaysOfMonth);
		getweek(year, month);
	}

	// 将一个月中的每一天的值添加入数组dayNuber中
	private void getweek(int year, int month) {
		int j = 1;
		int flag = 0;
		String lunarDay = "";
		// 得到当前月的所有日程日期(这些日期需要标记)
		lastMouth.clear();
		nextMouth.clear();
		for (int i = 0; i < dayNumber.length; i++) {
			// 周一
			// if(i<7){
			// dayNumber[i]=week[i]+"."+" ";
			// }
			if (i < dayOfWeek) { // 前一个月
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				lunarDay = lc.getLunarDate(year, month - 1, temp + i, false);
				dayNumber[i] = (temp + i) + "." + lunarDay;
				lastMouth.add(i);
			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期
				lunarDay = lc.getLunarDate(year, month, i - dayOfWeek + 1,
						false);
				dayNumber[i] = i - dayOfWeek + 1 + "." + lunarDay;
				// 对于当前月才去标记当前日期
				String date_string = year + "" + month + "" + day;
				// sys_month=sys_month
				String start_string = sys_year + ""
						+ sys_month.replaceAll("0", "") + ""
						+ sys_day.replaceFirst("0", "");
				int date_int = Integer.valueOf(date_string);
				int start_int = Integer.valueOf(start_string);
				if (sys_year.equals(String.valueOf(year))
						&& sys_month.equals(String.valueOf(month))
						&& sys_day.equals(day)) {
					// 标记当前日期
					currentFlag = i;
				}
				this.showYear = String.valueOf(year);
				this.showMonth = String.valueOf(month);
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
				setAnimalsYear(lc.animalsYear(year));
				setLeapMonth(lc.leapMonth == 0 ? "" : String
						.valueOf(lc.leapMonth));
				setCyclical(lc.cyclical(year));
			} else { // 下一个月
				nextMouth.add(i);
				lunarDay = lc.getLunarDate(year, month + 1, j, false);
				dayNumber[i] = j + "." + lunarDay;
				j++;
			}
		}

		String abc = "";
		for (int i = 0; i < dayNumber.length; i++) {
			abc = abc + dayNumber[i] + ":";
		}
		Log.d("DAYNUMBER", abc);

	}

	public void matchScheduleDate(int year, int month, int day) {

	}

	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public String getDateByClickItem(int position) {
		return dayNumber[position];
	}

	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek + 7;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth + 7) - 1;
	}

	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}

	public String getAnimalsYear() {
		return animalsYear;
	}

	public void setAnimalsYear(String animalsYear) {
		this.animalsYear = animalsYear;
	}

	public String getLeapMonth() {
		return leapMonth;
	}

	public void setLeapMonth(String leapMonth) {
		this.leapMonth = leapMonth;
	}

	public String getCyclical() {
		return cyclical;
	}

	public void setCyclical(String cyclical) {
		this.cyclical = cyclical;
	}
}
