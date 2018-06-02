package com.jlx.tinyrpc.client;

import com.jlx.tinyrpc.common.constant.LoggerName;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;
import com.jlx.tinyrpc.remoting.netty.NettyClientConfig;
import com.jlx.tinyrpc.remoting.netty.NettyServerConfig;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author liqiang.dong
 * @date 2018/5/31 11:20
 */
public class ClientStartup {

    private static final Logger log = LoggerFactory.getLogger(LoggerName.CLIENT_LOGGER_NAME);

    private static AtomicBoolean init = new AtomicBoolean(false);

    /**
     * 启动服务
     * @param registryAddr 注册中心地址
     * @param serverPort 服务监听端口
     */
    public static void start(String registryAddr, int serverPort) {
        if(init.compareAndSet(false, true)) {
            try {

                final NettyClientConfig nettyClientConfig = new NettyClientConfig();
                final NettyServerConfig nettyServerConfig = new NettyServerConfig();
                if(serverPort > 0) {
                    nettyServerConfig.setListenPort(serverPort);
                }

                final ClientController controller = new ClientController(nettyClientConfig, nettyServerConfig, registryAddr);

                boolean initResult = controller.initialize();
                if (!initResult) {
                    controller.shutdown();
                    System.exit(-1);
                }

                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        controller.shutdown();
                    }
                });

                controller.start();

                String tip = "The Client boot success.";

                System.out.printf(tip + "%n");

            } catch (Throwable e) {
                log.error("client start exception", e);
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
