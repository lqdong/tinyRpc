package com.jlx.tinyrpc.client.test;

import com.jlx.tinyrpc.client.ClientStartup;
import com.jlx.tinyrpc.client.proxy.ProxyFactory;
import com.jlx.tinyrpc.client.registry.RegistryFactory;

/**
 * @author liqiang.dong
 * @date 2018/5/31 9:36
 */
public class RemotingServiceTest {

    public static void main(String[] args) {
        //启动客户端
        ClientStartup.start("127.0.0.1:9876", 8888);
        //注册服务
        RegistryFactory.registry("test", LogginService.class);
        //调用服务
        LogginService logginService = ProxyFactory.newProxy(RemotingServiceTest.class.getClassLoader(), LogginService.class, "test");
        boolean isLoggin = logginService.isLogin("donglq");
        System.out.println("isLoggin: " + isLoggin);
    }

}
