package com.jlx.tinyrpc.client.proxy;

import com.alibaba.fastjson.JSON;
import com.jlx.tinyrpc.client.GlobalConfig;
import com.jlx.tinyrpc.common.constant.LoggerName;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;
import com.jlx.tinyrpc.common.protocol.RequestCode;
import com.jlx.tinyrpc.common.protocol.ResponseCode;
import com.jlx.tinyrpc.common.utils.StringUtils;
import com.jlx.tinyrpc.remoting.RemotingClient;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.GetKVConfigRequestHeader;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.GetKVConfigResponseHeader;
import com.jlx.tinyrpc.remoting.ext.protocol.header.service.RemotingServiceRequestBody;
import com.jlx.tinyrpc.remoting.ext.protocol.header.service.RemotingServiceResponseBody;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author liqiang.dong
 * @date 2018/5/30 16:50
 */
public class RemotingServiceInvocationHandler implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(LoggerName.CLIENT_LOGGER_NAME);

    //注册中心地址
    private List<String> registryList;

    //远程调用端
    private RemotingClient remotingClient;

    //服务组
    private String group;

    //要调用的服务的接口类对象
    private Class<?> service;

    public RemotingServiceInvocationHandler(List<String> registryList, RemotingClient remotingClient, String group, Class<?> service) {
        this.registryList = registryList;
        this.remotingClient = remotingClient;
        this.group = group;
        this.service = service;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        GetKVConfigRequestHeader getKVConfigRequestHeader = new GetKVConfigRequestHeader();
        getKVConfigRequestHeader.setNamespace(group);
        getKVConfigRequestHeader.setKey(service.getCanonicalName());
        RemotingCommand registryRequest = RemotingCommand.createRequestCommand(RequestCode.GET_KV_CONFIG, getKVConfigRequestHeader);
        RemotingCommand registryResponse = remotingClient.invokeSync(registryList.get(0), registryRequest, GlobalConfig.getTimeout());
        if(registryResponse != null && registryResponse.getCode() == ResponseCode.SUCCESS) {
            GetKVConfigResponseHeader getKVConfigResponseHeader = (GetKVConfigResponseHeader) registryResponse.decodeCommandCustomHeader(GetKVConfigResponseHeader.class);
            if(getKVConfigResponseHeader == null || StringUtils.isBlank(getKVConfigResponseHeader.getValue())) {
                log.warn("no service [{}] can be found", service.getCanonicalName());
                return null;
            } else {
                //服务调用地址
                String serviceAddr = getKVConfigResponseHeader.getValue();
                String[] paraTypes = null;
                if(method.getParameterCount() > 0) {
                    paraTypes = new String[method.getParameterCount()];
                    int index = 0;
                    for(Class<?> paraType: method.getParameterTypes()) {
                        paraTypes[index++] = paraType.getCanonicalName();
                    }
                }
                RemotingServiceRequestBody serviceRequestBody = new RemotingServiceRequestBody(service.getCanonicalName(), method.getName(), paraTypes, args);
                RemotingCommand serviceRequest = RemotingCommand.createRequestCommand(0, null);
                serviceRequest.setBody(JSON.toJSONBytes(serviceRequestBody));
                RemotingCommand serviceResponse = remotingClient.invokeSync(serviceAddr, serviceRequest, GlobalConfig.getTimeout());
                if(serviceResponse != null && serviceResponse.getCode() == ResponseCode.SUCCESS) {
                    byte[] body = serviceResponse.getBody();
                    if(body == null || body.length == 0) {
                        return null;
                    }
                    try {
                        RemotingServiceResponseBody serviceResponseBody = JSON.parseObject(body, RemotingServiceResponseBody.class);
                        if(serviceResponseBody.getResult() != null) {
                            return JSON.parseObject(JSON.toJSONString(serviceResponseBody.getResult()), method.getReturnType());
                        }
                        if(serviceResponseBody.getThrowable() != null) {
                            throw serviceResponseBody.getThrowable();
                        }
                        return null;
                    } catch (Exception e) {
                        log.error("parse service [{}] response exception", service.getCanonicalName(), e);
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } else {
            log.warn("detect service [{}] from registry [{}] fail", service.getCanonicalName(), registryList.get(0));
            return null;
        }
    }

}
