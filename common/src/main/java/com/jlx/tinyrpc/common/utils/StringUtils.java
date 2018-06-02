package com.jlx.tinyrpc.common.utils;

/**
 * @author liqiang.dong
 * @date 2018/5/30 13:51
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean notBlank(String str) {
        return str != null && !"".equals(str.trim());
    }


}
