package com.jlx.tinyrpc.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author liqiang.dong
 * @date 2018/5/30 18:35
 */
public class IPUtils {

    public static String getLocalHostIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

}
