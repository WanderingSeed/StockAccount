package com.morgan.stockaccount.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 提供日期相关的实用方法（如格式化等）。
 * 
 * @author Morgan.Ji
 */
public class DateUtils {
    
    /**
     * {@value}
     */
    public static final String TIME_ONLY_FORMAT = "HH:mm";

    /**
	 * {@value}
	 */
    public static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    /**
	 * {@value}
	 */
    public static final String COMPLETE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 按照format输出date的字符串，format格式如：{@link #DATE_ONLY_FORMAT}。
     * 
     * @param date
     *            要格式化的日期
     * @param format
     *            格式
     * @return
     */
    public static String dateToString(Date date, String format) {
        String timeString = null;
        if (date == null) {
            return "";
        }
        try {
            DateFormat formatDate = new SimpleDateFormat(format, Locale.CHINESE);
            timeString = formatDate.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeString;
    }
}
