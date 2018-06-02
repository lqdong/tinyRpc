package com.jlx.tinyrpc.registry;

import com.jlx.tinyrpc.remoting.netty.NettyServerConfig;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;

/**
 * 注册中心启动类
 */
public class RegistryStartup {

    public static void main(String[] args) {
        main0(args);
    }

    public static RegistryController main0(String[] args) {
        try {

            final RegistryConfig registryConfig = new RegistryConfig();
            final NettyServerConfig nettyServerConfig = new NettyServerConfig();
            nettyServerConfig.setListenPort(9876);

            final RegistryController controller = new RegistryController(registryConfig, nettyServerConfig);

            boolean initResult = controller.initialize();
            if (!initResult) {
                controller.shutdown();
                System.exit(-3);
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    controller.shutdown();
                }
            });

            controller.start();

            String tip = "The Registry Server boot success. serializeType=" + RemotingCommand.getSerializeTypeConfigInThisServer();

            System.out.printf(tip + "%n");

            return controller;
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

}
