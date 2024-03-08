package com.framework.xxljob.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


/**
 * @author fan.junwei
 * @since 2022-06-06 15:14
 */
public class CronUtils {
    /**
     * 仅一次
     *
     * @param dateStr 2022-03-31 09:51:05
     */
    public static String onlyOnce(final String dateStr) {
        LocalDateTime time = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return time.format(DateTimeFormatter.ofPattern("ss mm HH dd MM ? yyyy"));
    }

    /**
     * 每天
     *
     * @param timeStr 09:51:05
     */
    public static String everyDay(final String timeStr) {
        LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time.format(DateTimeFormatter.ofPattern("ss mm HH * * ?"));
    }

    /**
     * 每周
     *
     * @param timeStr 09:51:05
     * @param week    1代表周日 7代表周六 可选值：1、2、3、4、5、6、7、1-5、1-7
     */
    public static String everyWeek(final String timeStr, final String week) {
        LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time.format(DateTimeFormatter.ofPattern("ss mm HH ? * " + week));
    }

    /**
     * 每月
     *
     * @param dateStr 2022-03-31 09:51:05
     */
    public static String everyMonth(String dateStr) {
        LocalDateTime time = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return time.format(DateTimeFormatter.ofPattern("ss mm HH dd * ?"));
    }

    /**
     * 每年
     *
     * @param dateStr 2022-03-31 09:51:05
     */
    public static String everyYear(final String dateStr) {
        LocalDateTime time = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return time.format(DateTimeFormatter.ofPattern("ss mm HH dd MM *"));
    }
}
