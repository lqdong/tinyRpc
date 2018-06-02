package com.jlx.tinyrpc.client.invocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liqiang.dong
 * @date 2018/5/31 16:08
 */
public class ServiceInvokeFactory {

    private static final Map<String, ServiceInvoke> invokers = new ConcurrentHashMap<String, ServiceInvoke>(4);

    public static void registServiceInvoke(String name, ServiceInvoke serviceInvoke) {
        invokers.put(name, serviceInvoke);
    }

    public static ServiceInvoke getServiceInvoke(String name) {
        return invokers.get(name);
    }

}
