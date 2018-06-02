package com.jlx.tinyrpc.registry;


import com.jlx.tinyrpc.registry.kvconfig.KVConfigManager;
import com.jlx.tinyrpc.registry.processor.DefaultRequestProcessor;
import com.jlx.tinyrpc.remoting.RemotingServer;
import com.jlx.tinyrpc.remoting.netty.NettyRemotingServer;
import com.jlx.tinyrpc.remoting.netty.NettyServerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegistryController {

    private final RegistryConfig registryConfig;

    private final NettyServerConfig nettyServerConfig;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private final KVConfigManager kvConfigManager;

    private RemotingServer remotingServer;

    private ExecutorService remotingExecutor;

    public RegistryController(RegistryConfig registryConfig, NettyServerConfig nettyServerConfig) {
        this.registryConfig = registryConfig;
        this.nettyServerConfig = nettyServerConfig;
        this.kvConfigManager = new KVConfigManager(this);
    }

    /**
     * 初始化
     * @return
     */
    public boolean initialize() {

        this.kvConfigManager.load();

        this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);

        this.remotingExecutor =
            Executors.newFixedThreadPool(nettyServerConfig.getServerWorkerThreads());

        this.registerProcessor();

        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            public void run() {
                RegistryController.this.kvConfigManager.printAllPeriodically();
            }
        }, 1, 10, TimeUnit.MINUTES);

        return true;
    }

    /**
     * 注册请求处理器
     */
    private void registerProcessor() {
        this.remotingServer.registerDefaultProcessor(new DefaultRequestProcessor(this), this.remotingExecutor);
    }

    /**
     * 启动
     * @throws Exception
     */
    public void start() throws Exception {
        this.remotingServer.start();
    }

    public void shutdown() {
        this.remotingServer.shutdown();
        this.remotingExecutor.shutdown();
        this.scheduledExecutorService.shutdown();
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public NettyServerConfig getNettyServerConfig() {
        return nettyServerConfig;
    }

    public KVConfigManager getKvConfigManager() {
        return kvConfigManager;
    }

    public RemotingServer getRemotingServer() {
        return remotingServer;
    }

    public void setRemotingServer(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }

}
