package com.jlx.tinyrpc.remoting;


import com.jlx.tinyrpc.remoting.netty.ResponseFuture;

public interface InvokeCallback {
    void operationComplete(final ResponseFuture responseFuture);
}
