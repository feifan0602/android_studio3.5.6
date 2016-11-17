package com.sun3d.culturalShanghai.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sun3d.culturalShanghai.R;

/**
 * 文字处理
 * 
 * @author yangyoutao
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TextUtil {
	private static String TimeFormatString = "yyyy-MM-dd";
	private static String TimeFormaTIMEString = "HH:mm";
	private static String TimeFormatAllString = "yyyy-MM-dd HH:mm";
	private static String TimeForma24TIMEString = "mm:ss";

	/**
	 * 计算微博内容的长度 1个汉字 == 两个英文字母所占的长度 标点符号区分英文和中文
	 * 
	 * @param c
	 *            所要统计的字符序列
	 * @return 返回字符序列计算的长度
	 */
	public static long calculateWeiboLength(CharSequence c) {

		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int temp = (int) c.charAt(i);
			if (temp > 0 && temp < 127) {
				// len += 0.5;
				len++;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	/**
	 * 地址处理,去除地址编号
	 * 
	 * @param result
	 * @return
	 */
	public static String getAddresHandler(String result) {
		if (TextUtils.isEmpty(result)) {
			return "";
		}
		String[] str = result.split(",");
		if (str != null && str.length > 1) {
			return str[1];
		} else {
			return "";
		}

	}
	
	/**
	 * 地址处理
	 * @author zhoutanping
	 * @param result
	 * @return
	 */
	public static String getAddresHandle(String result) {
		if (TextUtils.isEmpty(result) && null == result && result.length() < 1) {
			return "";
		}
		String[] str = result.split(":");
		return str[str.length - 1];
	}

	/**
	 * 地址处理，区分地址与编号
	 */
	public static String getAddresText(String result, int type) {
		if (TextUtils.isEmpty(result)) {
			return "";
		}
		String[] str = result.split(":");
		return str[type];
	}

	/**
	 * 时间处理 年-月-日
	 * 
	 * @param date
	 * @return
	 */
	public static String TimeFormat(long date) {
		date = date * 1000;
		SimpleDateFormat format = new SimpleDateFormat(TimeFormatString);
		format.format(date);
		return format.format(date);
	}

	/**
	 * 时间处理 年-月-日 时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String TimeFormatAll(long date) {
		date = date * 1000;
		SimpleDateFormat format = new SimpleDateFormat(TimeFormatAllString);
		format.format(date);
		return format.format(date);
	}

	/**
	 * 时间处理 时:分
	 * 
	 * @param date
	 * @return
	 */
	public static String TimeFormatTime(long date) {
		date = date * 1000;
		SimpleDateFormat format = new SimpleDateFormat(TimeFormaTIMEString);
		format.format(date);
		return format.format(date);
	}

	/**
	 * 活动详情和场馆详情评论的文字处理
	 * 
	 * @param tv
	 */

	public static void setTextViewText(TextView tv, String admin) {
		SpannableStringBuilder builder = new SpannableStringBuilder(tv.getText().toString());
		ForegroundColorSpan namecolor = new ForegroundColorSpan(R.color.dialog_defult_text_color);
		ForegroundColorSpan commentcolor = new ForegroundColorSpan(R.color.event_text_color);

		builder.setSpan(namecolor, 0, 0, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		builder.setSpan(commentcolor, admin.length() + 1, tv.getText().toString().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tv.setText(builder);
	}
	/**
	 * 拼接图片地址 _750_500
	 */
	public static String getUrl(String url) {
		return url;
	}
	/**
	 * 拼接图片地址 _750_500
	 */
	public static String getUrlMiddle(String url) {
		if ("".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_750_500.");
		sb.append(lastChar);
		return sb.toString();
	}

	/**
	 * 拼接图片地址 _300_300
	 */
	public static String getUrlSmall(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_300_300.");
		sb.append(lastChar);
		return sb.toString();
	}
//	 @"_750_250"//大的轮播图
//	 #define kImageSize_SmallAdv @"_748_310"//小的轮播图

	/**
	 * 拼接图片地址 _750_150
	 */
	public static String getUrlSmall_750_150(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_750_150.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _750_250  大图
	 */
	public static String getUrlSmall_750_250(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_750_250.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _750_310  小图
	 */
	public static String getUrlSmall_748_310(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_748_310.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _750_400
	 */
	public static String getUrlSmall_750_400(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_750_400.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _150_150
	 */
	public static String getUrlSmall_150_150(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_150_150.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _150_100
	 */
	public static String getUrlSmall_150_100(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_150_100.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _140_120
	 */
	public static String getUrlSmall_140_120(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_140_120.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _300_190
	 */
	public static String getUrlSmall_300_190(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_300_190.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _375_220
	 */
	public static String getUrlSmall_375_220(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_375_220.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _750_310
	 */
	public static String getUrlSmall_750_310(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_750_310.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _750_440
	 */
	public static String getUrlSmall_750_440(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_750_440.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _187_215
	 */
	public static String getUrlSmall_187_215(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_187_215.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 拼接图片地址 _374_430
	 */
	public static String getUrlSmall_374_430(String url) {
		if (url == null || "".equals(url)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf(".")));
		String lastChar = url.substring(url.lastIndexOf(".") + 1, url.length());
		sb.append("_374_430.");
		sb.append(lastChar);
		return sb.toString();
	}
	/**
	 * 字符全角化,把所有的半角全部转化为全角，和汉字一样。
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 时间段处理 活动详情和列表
	 * 
	 * @param Str
	 * @return
	 */
	public static String getTime(String Str) {
		String timeStr = "";
		String[] times = Str.split(",");
		if (times != null) {
			for (String str : times) {
				String[] times_new = str.split("-");
				if (times_new != null && times_new.length > 1 && times_new[0].equals(times_new[1])) {
					timeStr += times_new[0] + ",";
				} else {
					timeStr += str + ",";
				}

			}
		}
		if (timeStr != null) {
			timeStr = timeStr.substring(0, timeStr.length() - 1);
		}
		return timeStr;
	}


	/**
	 * 时间处理 2016-01-5-->2016年01月5日
	 *
	 * @param
	 * @return
	 */
	public static String Time2Format(String strStart) {
		String[] start1 = strStart.split("-");
		String statttime = "";
		if (null != start1 && start1.length > 2) {
			statttime = start1[0] + "年" + start1[1] + "月" + start1[2]+"日";
		}
		return statttime;
	}

	public static String getToDay() {
		String data;
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int DAY = cal.get(Calendar.DAY_OF_MONTH);
		int MONTH = cal.get(Calendar.MONTH) + 1;
		String month = "";
		String day = "";
		if (MONTH < 10) {
			month = "0" + MONTH;
		} else {
			month = MONTH + "";
		}
		if (DAY < 10) {
			day = "0" + DAY;
		} else {
			day = DAY + "";
		}
		int year = cal.get(Calendar.YEAR);
		data = year + "-" + month + "-" + day;
		return data.toString();
	}

	/**
	 * 时间日期处理,活动详情和列表
	 */
	public static String getDate(String strStart, String strend) {
		String[] start1 = strStart.split("-");
		String statttime = "";
		if (null != start1 && start1.length > 2) {
			statttime = start1[0] + "年" + start1[1] + "月" + start1[2]+"日";
		}
		String[] end1 = strend.split("-");
		String eddtime = "";
		if (null != end1 && end1.length > 2) {
			eddtime = end1[0] + "年" + end1[1] + "月" + end1[2]+"日";
		}
		String date = "";
		if (statttime.equals(eddtime)) {
			date = eddtime;
		} else {
			if (strend.length() > 0) {
				date = statttime + "至" + eddtime;
			} else {
				date = eddtime;
			}

		}
		return date;
	}

	/**
	 * 
	 * @param time
	 * @return 将yyyy-hh-dd转为hh/dd
	 */
	public static String getDate(String time) {
		String newTime = "";
		if ("".equals(time)) {
			return "12/12";
		}
		String[] times = time.split("-");
		if (times.length >= 3) {
			newTime = times[1] + "/" + times[2];
		}
		return newTime;
	}

	/**
	 * 日期装换
	 */
	public static String getDateBriahty(String Briathy) {
		String DateStr = "";
		String[] brithday = Briathy.split("-");
		int year = 1980;
		if (brithday != null && brithday.length > 2) {
			year = Integer.parseInt(brithday[0]);
		}
		if (year < 2000) {
			switch (year % 1900 / 10) {
			case 5:
				DateStr = "50后";
				break;
			case 6:
				DateStr = "60后";
				break;
			case 7:
				DateStr = "70后";
				break;
			case 8:
				DateStr = "80后";
				break;
			case 9:
				DateStr = "90后";
				break;
			}
		} else {
			switch (year % 2000 / 10) {
			case 0:
				DateStr = "00后";
				break;
			case 1:
				DateStr = "10后";
				break;
			}
		}
		return DateStr;
	}

	/**
	 * 返回控件的宽度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewWidth(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		int readlyheight = view.getMeasuredHeight();
		int readlywidth = view.getMeasuredWidth();
		return readlywidth;
	}

	/**
	 * 返回控件的高度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewHeight(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(width, height);
		int readlyheight = view.getMeasuredHeight();
		int readlywidth = view.getMeasuredWidth();
		return readlyheight;
	}

	/**
	 * 计算文件播放的总时长
	 */
	public static String getDurationTime(long Duration) {
		SimpleDateFormat format = new SimpleDateFormat(TimeForma24TIMEString);
		Date date = new Date(Duration);
		return format.format(date);

	}
}
