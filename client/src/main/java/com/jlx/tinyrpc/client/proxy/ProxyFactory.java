package com.jlx.tinyrpc.client.proxy;

import com.jlx.tinyrpc.common.utils.StringUtils;
import com.jlx.tinyrpc.remoting.RemotingClient;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用端接口代理
 * @author liqiang.dong
 * @date 2018/5/30 16:33
 */
public class ProxyFactory {

    //注册中心地址
    private static List<String> registryList = new ArrayList<String>(4);

    private static RemotingClient remotingClient;

    public ProxyFactory(String registryAddrs, RemotingClient remotingClient) {
        for(String registryAddr: registryAddrs.split(",")) {
            if(StringUtils.notBlank(registryAddr)) {
                registryList.add(registryAddr.trim());
            }
        }
        this.remotingClient = remotingClient;
    }

    public static <T> T newProxy(ClassLoader classLoader, Class<T> clazz, String group) {
        return (T)Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new RemotingServiceInvocationHandler(registryList, remotingClient, group, clazz));
    }

}
