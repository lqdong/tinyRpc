package com.jlx.tinyrpc.remoting.test.netty;

import com.jlx.tinyrpc.remoting.netty.NettyClientConfig;
import com.jlx.tinyrpc.remoting.netty.NettyRemotingClient;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;

/**
 * @author liqiang.dong
 * @date 2018/5/30 15:43
 */
public class NettyRemotingClientTest {

    public static void main(String[] args) {
        try {
            NettyClientConfig clientConfig = new NettyClientConfig();
            NettyRemotingClient client = new NettyRemotingClient(clientConfig);
            client.start();
            while(true) {
                RemotingCommand command = RemotingCommand.createRequestCommand(0, null);
                client.invokeOneway("127.0.0.1:8888", command, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
