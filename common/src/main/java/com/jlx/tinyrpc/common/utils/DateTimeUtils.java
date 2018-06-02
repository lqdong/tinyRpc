package com.jlx.tinyrpc.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liqiang.dong
 * @date 2018/5/30 14:36
 */
public class DateTimeUtils {

    public static final SimpleDateFormat yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String format(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

}
