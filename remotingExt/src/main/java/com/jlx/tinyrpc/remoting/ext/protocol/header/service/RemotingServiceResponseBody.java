package com.jlx.tinyrpc.remoting.ext.protocol.header.service;

/**
 * @author liqiang.dong
 * @date 2018/5/31 11:45
 */
public class RemotingServiceResponseBody {

    //异常
    private Throwable throwable;

    //返回结果
    private Object result;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
