package com.jlx.tinyrpc.common.log;

import com.jlx.tinyrpc.common.log.impl.ConsoleLogger;

/**
 * 日志工厂
 * @author liqiang.dong
 * @date 2018/5/30 14:19
 */
public class LoggerFactory {

    private static Logger defaultLogger = new ConsoleLogger(new LogConfig(LogConfig.LOG_LEVEL_DEBUG));

    public static Logger getLogger(String name) {

        return defaultLogger;
    }

    public static Logger getLogger(Class<?> clazz) {

        return defaultLogger;
    }

}
