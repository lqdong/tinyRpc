package com.jlx.tinyrpc.remoting;

import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;

public interface RPCHook {
    void doBeforeRequest(final String remoteAddr, final RemotingCommand request);

    void doAfterResponse(final String remoteAddr, final RemotingCommand request,
                         final RemotingCommand response);
}
