package com.jlx.tinyrpc.remoting;

public interface RemotingService {
    void start();

    void shutdown();

    void registerRPCHook(RPCHook rpcHook);
}
