package com.jlx.tinyrpc.client.test;

import com.jlx.tinyrpc.client.proxy.ProxyFactory;

/**
 * @author liqiang.dong
 * @date 2018/5/30 16:38
 */
public class ProxyTest {

    public static void main(String[] args) {
        LogginService logginService = ProxyFactory.newProxy(ProxyTest.class.getClassLoader(), LogginService.class, "loggin");
        boolean isLoggin = logginService.isLogin("donglq");
        System.out.println("isLoggin: " + isLoggin);
    }

}
