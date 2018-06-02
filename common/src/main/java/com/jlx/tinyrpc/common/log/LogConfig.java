package com.jlx.tinyrpc.common.log;

/**
 * 日志相关配置
 * @author liqiang.dong
 * @date 2018/5/30 13:56
 */
public class LogConfig {

    public static final int LOG_LEVEL_DEBUG = 1;

    public static final int LOG_LEVEL_INFO = 2;

    public static final int LOG_LEVEL_WARN = 3;

    public static final int LOG_LEVEL_ERROR = 4;

    //不输出日志
    public static final int LOG_LEVEL_NONE = 9;

    private int logLevel = LOG_LEVEL_WARN;

    public LogConfig() {
    }

    public LogConfig(int logLevel) {
        this.logLevel = logLevel;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }
}
