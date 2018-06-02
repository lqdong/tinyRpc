package com.jlx.tinyrpc.client.test;

import com.jlx.tinyrpc.client.annotation.RemotingService;

/**
 * @author liqiang.dong
 * @date 2018/5/30 16:11
 */
public class AnnotationTest {

    @RemotingService(group = "loggin")
    private LogginService logginService;

    public boolean isLogin(String user) {
        return logginService.isLogin(user);
    }

    public static void main(String[] args) {
        AnnotationTest test = new AnnotationTest();
        String user = "donglq";
        boolean isLoggin = test.isLogin(user);
        System.out.println(user + " isLoggin: " + isLoggin);
    }

}
