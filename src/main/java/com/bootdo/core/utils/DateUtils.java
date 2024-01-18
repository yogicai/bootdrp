package com.bootdo.core.utils;

import cn.hutool.core.util.StrUtil;
import com.bootdo.core.consts.Constant;
import com.google.common.collect.Lists;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

/**
 * 日期处理
 *
 * @author L
 */
public class DateUtils {
    public final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final static DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public final static DateTimeFormatter ISO_SECOND_YMDHMS_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public final static DateTimeFormatter DATE_TIME_Y_FORMAT = DateTimeFormatter.ofPattern("yyyy");
    public final static DateTimeFormatter DATE_TIME_YM_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    public final static DateTimeFormatter DATE_TIME_YMD_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");


    public static String currentDate() {
        return LocalDate.now().toString();
    }

    public static Date nowDate() {
        return new Date();
    }

    public static String format(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }

    public static String format(Date date, DateTimeFormatter pattern) {
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(pattern) : null;
    }

    /**
     * 计算距离现在多久，非精确
     */
    public static String getTimeBefore(Date date) {
        Duration duration = Duration.between(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now());
        String r = "";
        if (duration.toDays() > 0) {
            r += duration.toDays() + "天";
        } else if (duration.toHours() > 0) {
            r += duration.toHours() + "小时";
        } else if (duration.toMinutes() > 0) {
            r += duration.toMinutes() + "分";
        } else if (duration.getSeconds() > 0) {
            r += duration.getSeconds() + "秒";
        }
        r += "前";
        return r;
    }

    public static String convertDateToStrISO(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(ISO_SECOND_YMDHMS_FORMAT);
    }


    public static List<String> getDaySerial(String type) {
        LocalDate begin = LocalDate.parse(getDayStartStr(type), DATE_TIME_FORMAT);
        LocalDate end = LocalDate.now().plusDays(1);
        List<String> serialList = Lists.newArrayList();
        if (Constant.Q_DAY.equals(type)) {
            while (begin.isBefore(end)) {
                serialList.add(begin.getDayOfMonth() + "号");
                begin = begin.plusDays(1);
            }
        } else if (Constant.Q_MONTH.equals(type)) {
            while (begin.isBefore(end)) {
                serialList.add(begin.getMonthValue() + "月");
                begin = begin.plusMonths(1);
            }
        } else if (Constant.Q_YEAR.equals(type)) {
            while (begin.isBefore(end)) {
                serialList.add(begin.getYear() + "年");
                begin = begin.plusYears(1);
            }
        }
        return serialList;
    }

    public static List<String> getDayTimeSerial(String type) {
        LocalDate begin = LocalDate.parse(getDayStartStr(type), DATE_TIME_FORMAT);
        LocalDate end = LocalDate.now().plusDays(1);
        List<String> serialList = Lists.newArrayList();
        if (Constant.Q_DAY.equals(type)) {
            while (begin.isBefore(end)) {
                serialList.add(begin.format(DATE_TIME_YMD_FORMAT));
                begin = begin.plusDays(1);
            }
        } else if (Constant.Q_MONTH.equals(type)) {
            while (begin.isBefore(end)) {
                serialList.add(begin.format(DATE_TIME_YM_FORMAT));
                begin = begin.plusMonths(1);
            }
        } else if (Constant.Q_YEAR.equals(type)) {
            while (begin.isBefore(end)) {
                serialList.add(begin.format(DATE_TIME_Y_FORMAT));
                begin = begin.plusYears(1);
            }
        }
        return serialList;
    }

    public static String getDayStartStr(String type) {
        if (Constant.Q_DAY.equals(type)) {
            return LocalDateTime.now().minusDays(30).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        } else if (Constant.Q_MONTH.equals(type)) {
            return LocalDateTime.now().minusMonths(12).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        } else if (Constant.Q_YEAR.equals(type)) {
            return LocalDateTime.now().minusYears(5).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        }
        return LocalDateTime.now().minusDays(30).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
    }

    public static String getStartStr(int i) {
        return LocalDateTime.now().plusDays(i).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
    }

    public static String getStartStr(String type) {
        String value = "";
        if (Constant.Q_DAY.equals(type)) {
            value = LocalDateTime.now().with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        } else if (Constant.Q_WEEK.equals(type)) {
            value = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        } else if (Constant.Q_MONTH.equals(type)) {
            value = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        } else if (Constant.Q_YEAR.equals(type)) {
            value = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN).format(DATE_TIME_FORMAT);
        }
        return value;
    }

    public static String getDayBegin(String dateStr) {
        if (StrUtil.isEmpty(dateStr)) {
            return "";
        }
        return LocalDate.parse(dateStr, DATE_FORMAT).atStartOfDay().format(DATE_TIME_FORMAT);
    }

    public static String getDayEnd(String dateStr) {
        if (StrUtil.isEmpty(dateStr)) {
            return "";
        }
        return LocalDate.parse(dateStr, DATE_FORMAT).atStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).format(DATE_TIME_FORMAT);
    }

    public static String getMonthBegin(String dateStr) {
        return getMonthBegin(dateStr, DATE_FORMAT);
    }

    public static String getMonthBegin(String dateStr, DateTimeFormatter formatter) {
        if (StrUtil.isEmpty(dateStr)) {
            return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().format(DATE_TIME_FORMAT);
        }
        return LocalDate.parse(dateStr, formatter).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay().format(DATE_TIME_FORMAT);
    }

    public static String getMonthEnd(String dateStr) {
        return getMonthEnd(dateStr, DATE_FORMAT);
    }

    public static String getMonthEnd(String dateStr, DateTimeFormatter formatter) {
        if (StrUtil.isEmpty(dateStr)) {
            return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59).format(DATE_TIME_FORMAT);
        }
        return LocalDate.parse(dateStr, formatter).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59).format(DATE_TIME_FORMAT);
    }

    public static String getYearBegin() {
        return getYearBegin("");
    }

    public static String getYearBegin(String dateStr) {
        if (StrUtil.isEmpty(dateStr)) {
            return LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay().format(DATE_TIME_FORMAT);
        }
        return LocalDate.parse(dateStr, DATE_FORMAT).with(TemporalAdjusters.firstDayOfYear()).atStartOfDay().format(DATE_TIME_FORMAT);
    }

    public static String getYearEnd() {
        return getYearEnd("");
    }

    public static String getYearEnd(String dateStr) {
        if (StrUtil.isEmpty(dateStr)) {
            return LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59).format(DATE_TIME_FORMAT);
        }
        return LocalDate.parse(dateStr, DATE_FORMAT).with(TemporalAdjusters.lastDayOfYear()).atTime(23, 59, 59).format(DATE_TIME_FORMAT);
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().minusDays(30).with(LocalTime.MIN).format(DATE_TIME_FORMAT));
        System.out.println(getMonthBegin("2018-07-01", DateUtils.DATE_FORMAT));
    }
}
