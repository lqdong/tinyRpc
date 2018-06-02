package com.jlx.tinyrpc.registry.processor;

import com.jlx.tinyrpc.common.constant.LoggerName;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;
import com.jlx.tinyrpc.common.protocol.RequestCode;
import com.jlx.tinyrpc.common.protocol.ResponseCode;
import com.jlx.tinyrpc.registry.RegistryController;
import com.jlx.tinyrpc.remoting.common.RemotingHelper;
import com.jlx.tinyrpc.remoting.exception.RemotingCommandException;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.DeleteKVConfigRequestHeader;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.GetKVConfigRequestHeader;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.GetKVConfigResponseHeader;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.PutKVConfigRequestHeader;
import com.jlx.tinyrpc.remoting.netty.NettyRequestProcessor;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

/**
 * 默认的请求处理器
 */
public class DefaultRequestProcessor implements NettyRequestProcessor {
    private static final Logger log = LoggerFactory.getLogger(LoggerName.REGISTRY_LOGGER_NAME);

    protected final RegistryController registryController;

    public DefaultRequestProcessor(RegistryController registryController) {
        this.registryController = registryController;
    }

    /**
     * 处理注册中心相关请求
     * @param ctx
     * @param request
     * @return
     * @throws RemotingCommandException
     */
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {
            log.debug("receive request, {} {} {}",
                request.getCode(),
                RemotingHelper.parseChannelRemoteAddr(ctx.channel()),
                request);
        switch (request.getCode()) {
            case RequestCode.PUT_KV_CONFIG:
                return this.putKVConfig(ctx, request);
            case RequestCode.GET_KV_CONFIG:
                return this.getKVConfig(ctx, request);
            case RequestCode.DELETE_KV_CONFIG:
                return this.deleteKVConfig(ctx, request);
            default:
                break;
        }
        return null;
    }

    public boolean rejectRequest() {
        return false;
    }

    /**
     * 添加配置
     * @param ctx
     * @param request
     * @return
     * @throws RemotingCommandException
     */
    public RemotingCommand putKVConfig(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {
        final RemotingCommand response = RemotingCommand.createResponseCommand(null);
        final PutKVConfigRequestHeader requestHeader =
            (PutKVConfigRequestHeader) request.decodeCommandCustomHeader(PutKVConfigRequestHeader.class);

        this.registryController.getKvConfigManager().putKVConfig(
            requestHeader.getNamespace(),
            requestHeader.getKey(),
            requestHeader.getValue()
        );

        response.setCode(ResponseCode.SUCCESS);
        response.setRemark(null);
        return response;
    }

    /**
     * 获取配置
     * @param ctx
     * @param request
     * @return
     * @throws RemotingCommandException
     */
    public RemotingCommand getKVConfig(ChannelHandlerContext ctx, RemotingCommand request) throws RemotingCommandException {
        final RemotingCommand response = RemotingCommand.createResponseCommand(GetKVConfigResponseHeader.class);
        final GetKVConfigResponseHeader responseHeader = (GetKVConfigResponseHeader) response.readCustomHeader();
        final GetKVConfigRequestHeader requestHeader =
            (GetKVConfigRequestHeader) request.decodeCommandCustomHeader(GetKVConfigRequestHeader.class);

        String value = this.registryController.getKvConfigManager().getKVConfig(
            requestHeader.getNamespace(),
            requestHeader.getKey()
        );

        if (value != null) {
            responseHeader.setValue(value);
            response.setCode(ResponseCode.SUCCESS);
            response.setRemark(null);
            return response;
        }

        response.setCode(ResponseCode.QUERY_NOT_FOUND);
        response.setRemark("No config item, Namespace: " + requestHeader.getNamespace() + " Key: " + requestHeader.getKey());
        return response;
    }

    /**
     * 删除配置
     * @param ctx
     * @param request
     * @return
     * @throws RemotingCommandException
     */
    public RemotingCommand deleteKVConfig(ChannelHandlerContext ctx,
        RemotingCommand request) throws RemotingCommandException {
        final RemotingCommand response = RemotingCommand.createResponseCommand(null);
        final DeleteKVConfigRequestHeader requestHeader =
            (DeleteKVConfigRequestHeader) request.decodeCommandCustomHeader(DeleteKVConfigRequestHeader.class);

        this.registryController.getKvConfigManager().deleteKVConfig(
            requestHeader.getNamespace(),
            requestHeader.getKey()
        );

        response.setCode(ResponseCode.SUCCESS);
        response.setRemark(null);
        return response;
    }

}
