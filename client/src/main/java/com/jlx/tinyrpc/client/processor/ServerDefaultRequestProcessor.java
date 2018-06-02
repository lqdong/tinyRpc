package com.jlx.tinyrpc.client.processor;

import com.alibaba.fastjson.JSON;
import com.jlx.tinyrpc.client.invocation.ServiceInvoke;
import com.jlx.tinyrpc.client.invocation.ServiceInvokeFactory;
import com.jlx.tinyrpc.common.constant.LoggerName;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;
import com.jlx.tinyrpc.common.protocol.ResponseCode;
import com.jlx.tinyrpc.remoting.ext.protocol.header.service.RemotingServiceRequestBody;
import com.jlx.tinyrpc.remoting.ext.protocol.header.service.RemotingServiceResponseBody;
import com.jlx.tinyrpc.remoting.netty.NettyRequestProcessor;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端默认请求处理器
 * @author liqiang.dong
 * @date 2018/5/31 10:02
 */
public class ServerDefaultRequestProcessor implements NettyRequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(LoggerName.CLIENT_LOGGER_NAME);

    /**
     * 处理请求
     * @param ctx
     * @param request
     * @return
     * @throws Exception
     */
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        RemotingServiceResponseBody responseBody = new RemotingServiceResponseBody();
        String service = null;
        String method = null;
        String[] paraTypes = null;
        Object[] args = null;
        try {
            if(request.getBody() != null && request.getBody().length > 0) {
                RemotingServiceRequestBody requestBody = JSON.parseObject(request.getBody(), RemotingServiceRequestBody.class);
                service = requestBody.getService();
                method = requestBody.getMethod();
                paraTypes = requestBody.getParaTypes();
                args = requestBody.getArgs();
            }
            if(service != null && method != null) {
                log.debug("server receive request: {} {}", service, method);
            }
            ServiceInvoke serviceInvoke = ServiceInvokeFactory.getServiceInvoke("spring");
            Object result = serviceInvoke.invoke(service, method, paraTypes, args);
            responseBody.setResult(result);
        } catch (Exception e) {
            log.debug("process request [{} {}] exception", service, method, e);
            responseBody.setThrowable(e);
        }
        RemotingCommand response = RemotingCommand.createResponseCommand(ResponseCode.SUCCESS, "成功");
        response.setBody(JSON.toJSONBytes(responseBody));
        return response;
    }

    public boolean rejectRequest() {
        return false;
    }
}
