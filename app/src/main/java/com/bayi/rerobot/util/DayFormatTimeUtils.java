package com.bayi.rerobot.util;

import java.util.Formatter;
import java.util.Locale;

public class DayFormatTimeUtils {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private int mYear;

    public DayFormatTimeUtils(int... year) {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        if (year.length > 0) {
            mYear = year[0];
        }
    }

    /**
     * 根据一年中的第几天获取具体时间（在lineChart的X轴坐标中使用）
     *
     * @param year 初始时间年份
     * @param days 初始时间在一年中的第几天开始算起并累加
     */
    public String getStringDate(int year, int days) {
        if (hasLeapYear(year)) {
            return getDate(year, days, 366);
        } else {
            return getDate(year, days, 365);
        }
    }

    private String getDate(int year, int days, int sumDay) {
        if (days > sumDay) {
            if (days - sumDay > sumDay) {
                return getStringDate(year + 1, days - sumDay);
            } else {
                return setDate(year + 1, days - sumDay);
            }
        } else {
            return setDate(year, days);
        }
    }

    private String setDate(int year, int days) {
        mFormatBuilder.setLength(0);
        int month = 0, day = 0;
        for (int i = 1; i < 13; i++) {
            boolean hasLeapYear = hasLeapYear(year);
            if (days <= getSumDay(hasLeapYear, i)) {
                month = i;
                day = getMonthAndDay(hasLeapYear, i) - (getSumDay(hasLeapYear(year), i) - days);
                break;
            }
        }
        if (mYear > 0 && year == mYear) {
            return mFormatter.format("%d-%d", month, day).toString();
        }
        return mFormatter.format("%d-%d-%d", year, month, day).toString();
    }

    private int getSumDay(boolean hasLeapYear, int month) {
        switch (month) {
            case 1:
                return 31;
            case 2:
                return hasLeapYear ? 60 : 59;
            case 3:
                return hasLeapYear ? 91 : 90;
            case 4:
                return hasLeapYear ? 121 : 120;
            case 5:
                return hasLeapYear ? 152 : 151;
            case 6:
                return hasLeapYear ? 182 : 181;
            case 7:
                return hasLeapYear ? 213 : 212;
            case 8:
                return hasLeapYear ? 244 : 243;
            case 9:
                return hasLeapYear ? 274 : 273;
            case 10:
                return hasLeapYear ? 305 : 304;
            case 11:
                return hasLeapYear ? 335 : 334;
            case 12:
                return hasLeapYear ? 366 : 365;
            default:
                return 0;
        }
    }

    public static int getMonthAndDay(boolean hasLeapYear, int month) {
        switch (month) {
            case 1:
                return 31;
            case 2:
                return hasLeapYear ? 29 : 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
            default:
                return 0;
        }
    }

    public static boolean hasLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

}
