/**
 *Administrator
 */
package com.ft.otp.util.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ft.otp.common.NumConstant;

/**
 * 时间、日期格式化工具类
 *
 * @Date in May 11, 2011,2:21:48 PM
 *
 * @author TBM
 */
public class DateTool {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SimpleDateFormat dateDayFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static SimpleDateFormat dateZFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**  
     * Date -> String
     * @param DATE
     * @return
     */
    public static String dateToStr(Date date) {
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * long -> Date -> String
     * @Date in Jun 2, 2011,2:06:30 PM
     * @param dateL
     * @return
     */
    public static String dateToStr(long dateL, boolean milli) {
        try {
            if (milli) {
                dateL = dateL * 1000;
            }
            Date date = new Date(dateL);
            return dateFormat.format(date);
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * long -> Date -> String--年/月/日
     * @Date in Jun 2, 2011,2:06:30 PM
     * @param dateL
     * @return
     */
    public static String dateConvertStr(long dateL) {
        try {
            dateL = dateL * 1000;
            Date date = new Date(dateL);
            return dateDayFormat.format(date);
        } catch (Exception e) {
        }

        return "";
    }

    /**
     * String -> Date
     * param string
     */
    public static Date strToDate(String dateStr) {
        try {
            return dateDayFormat.parse(dateStr);
        } catch (ParseException ex) {
        }

        return null;
    }
    
    /**
     * String -> Date 包括时 分 秒
     * param string
     */
    public static Date stringToDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException ex) {
        }

        return null;
    }

    /**
     * String->Date 
     */
    public static Date strZToDate(String dateZStr) {
        try {
            return dateZFormat.parse(dateZStr);
        } catch (ParseException ex) {
        }

        return null;
    }

    /**
     * String -> Date
     * param string
     */
    public static Date strToDate(String dateStr, int mark) {
        try {
            if (mark == NumConstant.common_number_1) {
                dateStr += " 00:00:00";
            } else if (mark == NumConstant.common_number_2) {
                dateStr += " 23:59:59";
            }
            return dateFormat.parse(dateStr);
        } catch (ParseException ex) {
        }

        return null;
    }

    /**
     * String -> Date
     * param string
     */
    public static Date strToDateFull(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException ex) {
        }

        return null;
    }

    /**
     * Date -> long
     * @Date in Jun 8, 2011,7:10:12 PM
     * @param date
     * @return
     */
    public static long dateToLong(Date date) {
        try {
            return date.getTime();
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * Date -> int 10位秒
     * @Date in Jun 8, 2011,7:10:12 PM
     * @param date
     * @return
     */
    public static int dateToInt(Date date) {
        try {
            long timeL = date.getTime() / 1000;
            int time = StrTool.longToInt(timeL);
            return time;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * int->UTC时间戳秒
     * @Date in Jun 24, 2011,4:11:19 PM
     * @param day
     * @return
     */
    public static long dayToLong(long day) {
        long time = 0;
        time = day * 24 * 3600 + System.currentTimeMillis() / 1000;
        return time;
    }

    /**
     * 获得当前的UTC时间戳秒
     * @Date in Jun 24, 2013,4:11:19 PM
     * @return
     */
    public static long currentUTC() {
        long time = 0;
        time = System.currentTimeMillis() / 1000;
        return time;
    }

    /**
     * 计算当前时间与某个时间相差天数
     * @Date in Jun 24, 2011,4:11:25 PM
     * @param datetime
     * @return
     * @throws ParseException
     */
    public static long getDay(long datetime) throws ParseException {
        String str = dateToStr(datetime, true);
        long DAY = 24L * 60L * 60L * 1000L;
        Date d1 = dateDayFormat.parse(str);
        Date d2 = new Date();
        long day = (d1.getTime() - d2.getTime()) / DAY + 1;
        return day;
    }

    /**
     *计算当前时间往后推移时间
     * @Date in Dec 15, 2011,11:45:24 AM
     * @param secondtime(秒)
     * @return String 
     */
    public static String getfutureDateStr(int secondtime) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, secondtime);
        String futureDate = dateFormat.format(calendar.getTime());
        return futureDate;
    }

    /**
     * 根据传入的年月获取该年月的 第一天的时间
     * 方法说明
     * @Date in Aug 21, 2012,11:38:40 AM
     * @param year
     * @param month
     * @return
     */
    public static Date getYearMonthBeginTime(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 按照给定的格式获取当天日期
     * format 
     * (true  2012-07-18)
     * (false 20120718)
     * @Date in Dec 12, 2011,6:19:14 PM
     * @param format
     * @return
     */
    public static String getCurDate(String format) {
        Calendar d = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(d.getTime());
    }
    
    /**
     * 获取当前时间推后三天时间
     * @Date in May 4, 2013,4:06:24 PM
     * @return
     */
    public static String getCurTimeLastThreeDay() {
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_YEAR, curr.get(Calendar.DAY_OF_YEAR) + 3);
        Date date = curr.getTime();
        return dateToStr(date);
    }

}
