package com.jlx.tinyrpc.common.log.impl;

import com.jlx.tinyrpc.common.log.LogConfig;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.utils.StringUtils;

/**
 * @author liqiang.dong
 * @date 2018/5/30 14:00
 */
public abstract class AbstractLogger implements Logger {

    private LogConfig logConfig;

    protected AbstractLogger() {
    }

    protected AbstractLogger(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    protected LogConfig getLogConfig() {
        return logConfig;
    }

    protected void setLogConfig(LogConfig logConfig) {
        this.logConfig = logConfig;
    }

    public boolean isEnableDebug() {
        return logConfig != null && LogConfig.LOG_LEVEL_DEBUG >= logConfig.getLogLevel();
    }

    public boolean isEnableInfo() {
        return logConfig != null && LogConfig.LOG_LEVEL_INFO >= logConfig.getLogLevel();
    }

    public boolean isEnableWarn() {
        return logConfig != null && LogConfig.LOG_LEVEL_WARN >= logConfig.getLogLevel();
    }

    public boolean isEnableError() {
        return logConfig != null && LogConfig.LOG_LEVEL_ERROR >= logConfig.getLogLevel();
    }

    protected String format(String format, Object... args) {
        if(StringUtils.isBlank(format)) {
            return "";
        }
        if(args == null) {
            return format;
        }
        String[] arrays = format.split("\\{\\s*\\}");
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for(String str: arrays) {
            builder.append(str);
            if(index < args.length) {
                builder.append(args[index++]);
            }
        }
        return builder.toString();
    }

}
