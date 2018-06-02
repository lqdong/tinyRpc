package com.jlx.tinyrpc.remoting.test.netty;

import com.jlx.tinyrpc.remoting.netty.NettyRemotingServer;
import com.jlx.tinyrpc.remoting.netty.NettyRequestProcessor;
import com.jlx.tinyrpc.remoting.netty.NettyServerConfig;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executors;

/**
 * @author liqiang.dong
 * @date 2018/5/30 15:39
 */
public class NettyRemotingServerTest {

    public static void main(String[] args) {
        NettyServerConfig serverConfig = new NettyServerConfig();
        NettyRemotingServer server = new NettyRemotingServer(serverConfig);
        server.registerDefaultProcessor(new NettyRequestProcessor() {
            public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
                System.out.println("receive: " + request);
                return request;
            }

            public boolean rejectRequest() {
                return false;
            }
        }, Executors.newCachedThreadPool());
        server.start();
    }

}
