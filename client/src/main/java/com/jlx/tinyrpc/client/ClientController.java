package com.jlx.tinyrpc.client;

import com.jlx.tinyrpc.client.processor.ServerDefaultRequestProcessor;
import com.jlx.tinyrpc.client.proxy.ProxyFactory;
import com.jlx.tinyrpc.client.registry.RegistryFactory;
import com.jlx.tinyrpc.remoting.RemotingClient;
import com.jlx.tinyrpc.remoting.RemotingServer;
import com.jlx.tinyrpc.remoting.netty.NettyClientConfig;
import com.jlx.tinyrpc.remoting.netty.NettyRemotingClient;
import com.jlx.tinyrpc.remoting.netty.NettyRemotingServer;
import com.jlx.tinyrpc.remoting.netty.NettyServerConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liqiang.dong
 * @date 2018/5/30 18:16
 */
public class ClientController {

    //注册中心地址
    private String registryAddr = "";

    //远程服务端配置
    private NettyServerConfig nettyServerConfig;

    //远程服务端
    private RemotingServer remotingServer;

    //远程调用端配置
    private NettyClientConfig nettyClientConfig;

    //远程调用端
    private RemotingClient remotingClient;

    //执行请求任务的线程池
    private ExecutorService remotingExecutor;

    //注册服务的工厂类
    private RegistryFactory registryFactory;

    //代理工厂类
    private ProxyFactory proxyFactory;

    public ClientController(NettyClientConfig nettyClientConfig, NettyServerConfig nettyServerConfig, String registryAddr) {
        this.nettyClientConfig = nettyClientConfig;
        this.nettyServerConfig = nettyServerConfig;
        this.registryAddr = registryAddr;
    }

    public boolean initialize() {

        this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);

        this.remotingClient = new NettyRemotingClient(this.nettyClientConfig);

        this.remotingExecutor = Executors.newFixedThreadPool(nettyServerConfig.getServerWorkerThreads());

        this.registerProcessor();

        this.registryFactory = new RegistryFactory(registryAddr, remotingClient, nettyServerConfig.getListenPort());

        this.proxyFactory = new ProxyFactory(registryAddr, remotingClient);

        return true;
    }

    /**
     * 注册请求处理器
     */
    private void registerProcessor() {
        this.remotingServer.registerDefaultProcessor(new ServerDefaultRequestProcessor(), this.remotingExecutor);
    }

    /**
     * 启动
     * @throws Exception
     */
    public void start() throws Exception {
        this.remotingServer.start();
        this.remotingClient.start();
    }

    /**
     * 关闭
     */
    public void shutdown() {
        this.remotingClient.shutdown();
        this.remotingServer.shutdown();
        this.remotingExecutor.shutdown();
    }

}
