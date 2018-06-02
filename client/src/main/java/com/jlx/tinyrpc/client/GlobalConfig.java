package com.jlx.tinyrpc.client;

/**
 * 全局配置
 * @author liqiang.dong
 * @date 2018/6/1 10:04
 */
public class GlobalConfig {

    //服务调用超时时间
    private static int timeout;

    public static int getTimeout() {
        return timeout;
    }

    public static void setTimeout(int timeout) {
        GlobalConfig.timeout = timeout;
    }

}
