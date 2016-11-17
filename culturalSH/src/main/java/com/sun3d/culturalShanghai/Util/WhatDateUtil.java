package com.sun3d.culturalShanghai.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WhatDateUtil {
    private final String TAG = "WhatDateUtil";

    /**
     * 日期
     */
    public static class WeekDay {
        public String date;
        public String weekDate;
        public int mouthDate;
        public int day;
    }

    /**
     * 根据当前日期的时间戳，返回当前是周几
     *
     * @param longtime
     * @return
     */
    public static String getDayOfWeek(long longtime) {
        String weekstr = "";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(longtime));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                System.out.println("星期日");
                weekstr = "日";
                break;
            case 2:
                System.out.println("星期一");
                weekstr = "一";
                break;
            case 3:
                System.out.println("星期二");
                weekstr = "二";
                break;
            case 4:
                System.out.println("星期三");
                weekstr = "三";
                break;
            case 5:
                System.out.println("星期四");
                weekstr = "四";
                break;
            case 6:
                System.out.println("星期五");
                weekstr = "五";
                break;
            case 7:
                System.out.println("星期六");
                weekstr = "六";
                break;

        }
        return weekstr;
    }

    /**
     * 返回一周日期
     *
     * @return
     */
    public static List<WeekDay> getDateToWeek() {
        List<WeekDay> WeekDayList = new ArrayList<WeekDay>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            cal.setTime(new Date());
            WeekDay wd = new WeekDay();
            cal.add(Calendar.DAY_OF_MONTH, i);
            wd.day = cal.get(Calendar.DAY_OF_MONTH);
            wd.mouthDate = cal.get(Calendar.MONTH) + 1;
            String month = "";
            String day = "";
            if (wd.mouthDate<10){
                month = "0"+wd.mouthDate;
            }else {
                month = wd.mouthDate+"";
            }
            if (wd.day<10){
                day = "0"+wd.day;
            }else {
                day = wd.day+"";
            }
            int year = cal.get(Calendar.YEAR);
            wd.weekDate = getDayOfWeek(cal.getTimeInMillis());
            wd.date = year + "-" + month + "-" + day;
            WeekDayList.add(wd);
        }
        return WeekDayList;


    }
}
