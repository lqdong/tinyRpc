package com.jlx.tinyrpc.common.log;

/**
 * @author liqiang.dong
 * @date 2018/5/30 13:53
 */
public interface Logger {

    void debug(String log);

    void debug(String format, Object... args);

    void info(String log);

    void info(String format, Object... args);

    void warn(String log);

    void warn(String format, Object... args);

    void error(String log);

    void error(String format, Object... args);

}
