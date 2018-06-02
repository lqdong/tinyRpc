package com.jlx.tinyrpc.client.registry;

import com.jlx.tinyrpc.client.GlobalConfig;
import com.jlx.tinyrpc.common.constant.LoggerName;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;
import com.jlx.tinyrpc.common.protocol.RequestCode;
import com.jlx.tinyrpc.common.protocol.ResponseCode;
import com.jlx.tinyrpc.common.utils.IPUtils;
import com.jlx.tinyrpc.common.utils.StringUtils;
import com.jlx.tinyrpc.remoting.RemotingClient;
import com.jlx.tinyrpc.remoting.exception.RemotingConnectException;
import com.jlx.tinyrpc.remoting.exception.RemotingSendRequestException;
import com.jlx.tinyrpc.remoting.exception.RemotingTimeoutException;
import com.jlx.tinyrpc.remoting.ext.protocol.header.registry.PutKVConfigRequestHeader;
import com.jlx.tinyrpc.remoting.protocol.RemotingCommand;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务端注册服务
 * @author liqiang.dong
 * @date 2018/5/30 18:12
 */
public class RegistryFactory {

    private static final Logger log = LoggerFactory.getLogger(LoggerName.CLIENT_LOGGER_NAME);

    private static List<String> registryList = new ArrayList<String>(4);

    //协议调用端，用来注册服务
    private static RemotingClient remotingClient;

    //服务端监听端口
    private static int port = -1;

    public RegistryFactory(String registryAddrs, RemotingClient remotingClient, int port) {
        for(String registryAddr: registryAddrs.split(",")) {
            if(StringUtils.notBlank(registryAddr)) {
                registryList.add(registryAddr.trim());
            }
        }
        this.remotingClient = remotingClient;
        this.port = port;
    }

    /**
     * 同步注册服务
     * @param group
     * @param clazz
     * @return
     * @throws UnknownHostException
     * @throws InterruptedException
     * @throws RemotingTimeoutException
     * @throws RemotingSendRequestException
     * @throws RemotingConnectException
     */
    public static boolean registry(String group, Class<?> clazz) {
        try {
            PutKVConfigRequestHeader putKVConfigRequestHeader = new PutKVConfigRequestHeader();
            putKVConfigRequestHeader.setNamespace(group);
            putKVConfigRequestHeader.setKey(clazz.getCanonicalName());
            StringBuilder builder = new StringBuilder(IPUtils.getLocalHostIp());
            builder.append(":").append(port);
            putKVConfigRequestHeader.setValue(builder.toString());
            log.info("start registing service: {}", putKVConfigRequestHeader.toString());
            RemotingCommand remotingCommand = RemotingCommand.createRequestCommand(RequestCode.PUT_KV_CONFIG, putKVConfigRequestHeader);
            RemotingCommand response = remotingClient.invokeSync(registryList.get(0), remotingCommand, GlobalConfig.getTimeout());
            boolean success = response != null && response.getCode() == ResponseCode.SUCCESS;
            log.info("registing service {} {}", clazz.getCanonicalName(), success);
            return success;
        } catch (Exception e) {
            log.error("registing service {} exception", clazz.getCanonicalName(), e);
            return false;
        }
    }

}
