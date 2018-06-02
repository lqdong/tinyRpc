package com.jlx.tinyrpc.client.invocation.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author liqiang.dong
 * @date 2018/5/31 16:26
 */
public class TinyRpcBeanDefinitionParser implements BeanDefinitionParser {

    public BeanDefinition parse(Element element, ParserContext parserContext) {

        //服务端类注解处理
        RootBeanDefinition serverAnnotation = new RootBeanDefinition(SpringServiceInvoke.class);
        serverAnnotation.setLazyInit(false);
        //注册中心地址
        String registry = element.getAttribute("registry");
        String listenPort = element.getAttribute("listenPort");
        String timeout = element.getAttribute("timeout");
        serverAnnotation.getPropertyValues()
                .add("registry", registry)
                .add("listenPort", Integer.valueOf(listenPort))
                .add("timeout", Integer.valueOf(timeout));
        parserContext.getRegistry().registerBeanDefinition(getBeanName(parserContext, SpringServiceInvoke.class.getName()), serverAnnotation);

        //客户端属性注解处理
        RootBeanDefinition clientAnnotation = new RootBeanDefinition(RemotingServiceAnnotationBeanPostProcessor.class);
        clientAnnotation.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        String clientId = getBeanName(parserContext, RemotingServiceAnnotationBeanPostProcessor.class.getName());
        parserContext.getRegistry().registerBeanDefinition(clientId, clientAnnotation);
        parserContext.registerComponent(new BeanComponentDefinition(new BeanDefinitionHolder(clientAnnotation, clientId)));
        return serverAnnotation;
    }

    private String getBeanName(ParserContext parserContext, String baseName) {
        String id = baseName;
        int counter = 2;
        while(parserContext.getRegistry().containsBeanDefinition(id)) {
            id = baseName + (counter++);
        }
        return id;
    }

}
