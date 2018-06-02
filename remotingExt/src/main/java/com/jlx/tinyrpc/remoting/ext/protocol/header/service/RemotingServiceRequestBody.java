package com.jlx.tinyrpc.remoting.ext.protocol.header.service;

/**
 * @author liqiang.dong
 * @date 2018/5/31 11:45
 */
public class RemotingServiceRequestBody {

    //服务接口类全限定名
    private String service;

    //方法名
    private String method;

    //参数类型
    private String[] paraTypes;

    //参数
    private Object[] args;

    public RemotingServiceRequestBody() {
    }

    public RemotingServiceRequestBody(String service, String method, String[] paraTypes, Object[] args) {
        this.service = service;
        this.method = method;
        this.paraTypes = paraTypes;
        this.args = args;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParaTypes() {
        return paraTypes;
    }

    public void setParaTypes(String[] paraTypes) {
        this.paraTypes = paraTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
