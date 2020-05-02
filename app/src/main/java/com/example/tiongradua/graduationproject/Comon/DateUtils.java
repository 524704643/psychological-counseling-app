package com.example.tiongradua.graduationproject.Comon;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 本人 on 2019/11/20.
 */

public class DateUtils {
    public  Date stringToDate(String strTime, String formatType) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public String getDateToString(Date date) {
        //Date date = new Date(date1);
        SimpleDateFormat format;
        String hintDate = "";
        //先获取年份
        int year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date));
        //获取一年中的第几天
        int day = Integer.valueOf(new SimpleDateFormat("d").format(date));
        //获取当前年份 和 一年中的第几天
        Date currentDate = new Date(System.currentTimeMillis());
        int currentYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(currentDate));
        int currentDay = Integer.valueOf(new SimpleDateFormat("d").format(currentDate));
        //计算 如果是去年的
        if (currentYear - year == 1) {
            //如果当前正好是 1月1日 计算去年有多少天，指定时间是否是一年中的最后一天
            if (currentDay == 1) {
                int yearDay;
                if (year % 400 == 0) {
                    yearDay = 366;//世纪闰年
                } else if (year % 4 == 0 && year % 100 != 0) {
                    yearDay = 366;//普通闰年
                } else {
                    yearDay = 365;//平年
                }
                if (day == yearDay) {
                    hintDate = "昨天";
                }
            }
        } else {
            if (currentDay - day == 2) {
                hintDate = "前天";
            }
            if (currentDay - day == 1) {
                hintDate = "昨天";
            }
            if (currentDay - day == 0) {
                hintDate = "今天";
            }
        }
        if (TextUtils.isEmpty(hintDate)) {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return format.format(date);
        } else {
            format = new SimpleDateFormat("HH:mm");
            return hintDate + " " + format.format(date);
        }

    }

}
