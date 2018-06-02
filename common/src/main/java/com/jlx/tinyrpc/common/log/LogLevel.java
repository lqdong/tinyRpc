package com.jlx.tinyrpc.common.log;

/**
 * @author liqiang.dong
 * @date 2018/5/30 15:59
 */
public enum LogLevel {

    DEBUG(1), INFO(2), WARN(3), ERROR(4), NONE(9);

    public int code;

    LogLevel(int code) {
        this.code = code;
    }

    public static LogLevel valueOf(int value) {
        for(LogLevel logLevel: LogLevel.values()) {
            if(logLevel.code == value) {
                return logLevel;
            }
        }
        return null;
    }

}
