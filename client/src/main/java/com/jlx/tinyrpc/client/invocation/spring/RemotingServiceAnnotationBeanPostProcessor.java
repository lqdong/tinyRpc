package com.jlx.tinyrpc.client.invocation.spring;

import com.jlx.tinyrpc.client.annotation.RemotingService;
import com.jlx.tinyrpc.client.proxy.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;

/**
 * 处理有RemotingService注解的属性
 * @author liqiang.dong
 * @date 2018/6/1 10:57
 */
public class RemotingServiceAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanNameAware, BeanFactoryAware {

    private String beanName;

    private BeanFactory beanFactory;

    public void setBeanName(String name) {
        this.beanName = name;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        parseFields(bean, bean.getClass().getDeclaredFields());
        return bean;
    }

    private void parseFields(final Object bean, final Field[] fields) throws BeansException {
        for (final Field field : fields) {
            RemotingService annotation = AnnotationUtils.getAnnotation(field, RemotingService.class);
            if (annotation == null || !field.getType().isInterface()) {
                continue;
            }
            String group = annotation.group();
            Object proxyObject = ProxyFactory.newProxy(this.getClass().getClassLoader(), field.getType(), group);
            setField(field, bean, proxyObject);
        }

    }

    private void setField(Field field, Object bean, Object param) throws BeansException {
        try {
            field.setAccessible(true);
            field.set(bean, param);
        } catch (Exception e) {
            throw new BeanCreationException("inject remoting service[{}] to bean[{}] exception", field.getName(), bean.getClass().getCanonicalName(), e);
        }
    }

}
