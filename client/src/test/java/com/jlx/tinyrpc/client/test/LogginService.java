package com.jlx.tinyrpc.client.test;

import com.jlx.tinyrpc.client.annotation.RemotingService;

/**
 * @author liqiang.dong
 * @date 2018/5/30 16:23
 */
@RemotingService(group = "loggin")
public interface LogginService {

    boolean isLogin(String user);

}
