package wtssg.xdly.common.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static Calendar calendar = Calendar.getInstance();

    /**
     * 获得当前的星期
     * @param date
     * @return
     */
    public static int getWeek(Date date) {
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0) {
            week = 7;
        }
        return week;
    }

    /**
     * 得到时间差（单位：分钟）
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMinute(Date startDate, Date endDate) {
        long start = startDate.getTime();
        long end = endDate.getTime();
        return  (int) ((end - start) * (1000 * 60));
    }

    /**
     * 得到指定的时间
     * @param hour
     * @param minute
     * @return
     */
    public static Date getDate(int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
}
