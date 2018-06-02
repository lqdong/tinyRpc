package com.jlx.tinyrpc.client.invocation;

/**
 * @author liqiang.dong
 * @date 2018/5/31 15:00
 */
public interface ServiceInvoke {

    Object invoke(String service, String method, String[] paraTypes, Object[] args) throws Exception;

}
