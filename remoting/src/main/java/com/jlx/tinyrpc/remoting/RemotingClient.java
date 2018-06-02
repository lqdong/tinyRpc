package com.jlx.tinyrpc.remoting;

import com.jlx.tinyrpc.remoting.exception.RemotingConnectException;
import com.jlx.tinyrpc.remoting.exception.RemotingSendRequestException;
import com.jlx.tinyrpc.remoting.exception.RemotingTimeoutException;
import com.jlx.tinyrpc.remoting.exception.RemotingTooMuchRequestException;
import com.jlx.tinyrpc.remoting.netty.NettyRequestProcessor;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface RemotingClient extends RemotingService {

    void updateNameServerAddressList(final List<String> addrs);

    List<String> getNameServerAddressList();

    RemotingCommand invokeSync(final String addr, final RemotingCommand request,
                               final long timeoutMillis) throws InterruptedException, RemotingConnectException,
            RemotingSendRequestException, RemotingTimeoutException;

    void invokeAsync(final String addr, final RemotingCommand request, final long timeoutMillis,
                     final InvokeCallback invokeCallback) throws InterruptedException, RemotingConnectException,
            RemotingTooMuchRequestException, RemotingTimeoutException, RemotingSendRequestException;

    void invokeOneway(final String addr, final RemotingCommand request, final long timeoutMillis)
        throws InterruptedException, RemotingConnectException, RemotingTooMuchRequestException,
        RemotingTimeoutException, RemotingSendRequestException;

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    void setCallbackExecutor(final ExecutorService callbackExecutor);

    boolean isChannelWritable(final String addr);
}
