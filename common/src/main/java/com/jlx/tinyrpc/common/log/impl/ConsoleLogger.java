package com.jlx.tinyrpc.common.log.impl;

import com.jlx.tinyrpc.common.log.LogConfig;
import com.jlx.tinyrpc.common.log.LogLevel;
import com.jlx.tinyrpc.common.utils.DateTimeUtils;
import com.jlx.tinyrpc.common.utils.StringUtils;

import java.io.PrintStream;
import java.util.Date;

/**
 * 控制台输出
 * @author liqiang.dong
 * @date 2018/5/30 13:59
 */
public class ConsoleLogger extends AbstractLogger {

    public ConsoleLogger() {
    }

    public ConsoleLogger(LogConfig logConfig) {
        super(logConfig);
    }

    public void debug(String log) {
        if(isEnableDebug()) {
            println(System.out, LogLevel.DEBUG.name(), log);
        }
    }

    public void debug(String format, Object... args) {
        if(isEnableDebug()) {
            println(System.out, LogLevel.DEBUG.name(), format(format, args));
        }
    }

    public void info(String log) {
        if(isEnableInfo()) {
            println(System.out, LogLevel.INFO.name(), log);
        }
    }

    public void info(String format, Object... args) {
        if(isEnableInfo()) {
            println(System.out, LogLevel.INFO.name(), format(format, args));
        }
    }

    public void warn(String log) {
        if(isEnableWarn()) {
            println(System.err, LogLevel.WARN.name(), log);
        }
    }

    public void warn(String format, Object... args) {
        if(isEnableWarn()) {
            println(System.err, LogLevel.WARN.name(), format(format, args));
        }
    }

    public void error(String log) {
        if(isEnableError()) {
            println(System.err, LogLevel.ERROR.name(), log);
        }
    }

    public void error(String format, Object... args) {
        if(isEnableError()) {
            println(System.err, LogLevel.ERROR.name(), format(format, args));
        }
        Object lastArg = null;
        if(args != null && args.length > 0 && (lastArg = args[args.length - 1]) instanceof Throwable) {
            ((Throwable) lastArg).printStackTrace();
        }
    }

    private void println(PrintStream out, String logLevel, String log) {
        if(out != null && StringUtils.notBlank(log)) {
            StringBuilder builder = new StringBuilder();
            builder.append("[")
                    .append(DateTimeUtils.format(new Date(), DateTimeUtils.yyyyMMddHHmmssSSS))
                    .append("] [")
                    .append(logLevel)
                    .append("] ")
                    .append(log);
            out.println(builder.toString());
        }
    }

}
