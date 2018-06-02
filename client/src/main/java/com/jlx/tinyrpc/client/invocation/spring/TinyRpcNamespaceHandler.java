package com.jlx.tinyrpc.client.invocation.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author liqiang.dong
 * @date 2018/5/31 16:23
 */
public class TinyRpcNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("annotation-driven", new TinyRpcBeanDefinitionParser());
    }

}
