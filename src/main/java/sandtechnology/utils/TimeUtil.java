package sandtechnology.utils;

import java.time.Instant;
import java.time.ZoneId;

/**
 * 处理时间相关的工具类
 *
 * @author sandtechnology
 * @since 2020/10/09
 */
public class TimeUtil {
    private static final ZoneId zoneId = ZoneId.of("UTC+8");
    private static final String[] format = {"天", "时", "分", "秒", "毫秒"};

    private TimeUtil() {
    }

    public static long offsetFromNowSec(long sec) {
        return sec - nowSec();
    }

    public static long nowSec() {
        return Instant.now().atZone(zoneId).toEpochSecond();
    }

    public static String getFormattedTimeSec(long sec) {
        return getFormattedTimeMS(sec * 1000);
    }

    public static String getFormattedTimeMS(long ms) {
        //秒
        long sec = ms / 1000;
        //毫秒
        long millsec = ms - sec * 1000;
        //分钟
        long minutes = 0;
        //小时
        long hours = 0;
        //天
        long day = 0;
        if (sec >= 60) {
            minutes = sec / 60;
            sec -= minutes * 60;
            if (minutes >= 60) {
                hours = minutes / 60;
                minutes -= hours * 60;
                if (hours >= 24) {
                    day = hours / 24;
                    hours -= day * 24;
                }
            }

        }
        long[] data = {day, hours, minutes, sec, millsec};
        StringBuilder formattedStrBuilder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (data[i] != 0) {
                formattedStrBuilder.append(data[i]).append(format[i]);
            }
        }
        return formattedStrBuilder.toString();
    }

}
