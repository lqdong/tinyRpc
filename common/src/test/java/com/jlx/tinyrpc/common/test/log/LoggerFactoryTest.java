package com.jlx.tinyrpc.common.test.log;

import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;

import java.util.Date;

/**
 * @author liqiang.dong
 * @date 2018/5/30 14:24
 */
public class LoggerFactoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(LoggerFactoryTest.class);

    public static void main(String[] args) {
        LOG.warn("{} 这是一个测试日志 { } 哈哈", new Date(), "test", "haah", "ewgqe");
    }

}
