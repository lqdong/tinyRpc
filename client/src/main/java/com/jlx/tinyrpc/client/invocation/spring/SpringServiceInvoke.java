package com.jlx.tinyrpc.client.invocation.spring;

import com.jlx.tinyrpc.client.ClientStartup;
import com.jlx.tinyrpc.client.GlobalConfig;
import com.jlx.tinyrpc.client.annotation.RemotingService;
import com.jlx.tinyrpc.client.invocation.ServiceInvoke;
import com.jlx.tinyrpc.client.invocation.ServiceInvokeFactory;
import com.jlx.tinyrpc.client.registry.RegistryFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author liqiang.dong
 * @date 2018/5/31 15:12
 */
public class SpringServiceInvoke implements ServiceInvoke, ApplicationContextAware, InitializingBean {

    private String registry;

    private int listenPort;

    private int timeout;

    //beanName <-> bean
    private static Map<String, Object> services = new ConcurrentHashMap<String, Object>(16);

    //interface <-> bean
    private static Map<String, List<Object>> interfaceObject = new ConcurrentHashMap<String, List<Object>>(16);

    private static Map<String, Method> methodMap = new ConcurrentHashMap<String, Method>(64);

    private AtomicBoolean init = new AtomicBoolean(false);

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.services.putAll(applicationContext.getBeansWithAnnotation(RemotingService.class));
    }

    public void afterPropertiesSet() throws Exception {
        GlobalConfig.setTimeout(timeout);
        //启动netty服务
        ClientStartup.start(registry, listenPort);
        ServiceInvokeFactory.registServiceInvoke("spring", this);
        if(init.compareAndSet(false, true)) {
            //第一次调用时初始化
            for(Object obj: services.values()) {
                //暂时只获取直接接口
                Class<?>[] interfaces = obj.getClass().getInterfaces();
                for(Class<?> inf: interfaces) {
                    String infName = inf.getCanonicalName();
                    if(!interfaceObject.containsKey(infName)) {
                        interfaceObject.put(infName, new ArrayList<Object>());
                    }
                    interfaceObject.get(infName).add(obj);
                    //注解
                    RemotingService anno = obj.getClass().getAnnotation(RemotingService.class);
                    //到注册中心注册
                    RegistryFactory.registry(anno.group(), inf);
                }
                Method[] methods = obj.getClass().getMethods();
                for(Method m: methods) {
                    if(Object.class.getCanonicalName().equals(m.getClass().getCanonicalName())) {
                        continue;
                    }
                    //类全限定名-方法名-方法各个参数类型名
                    StringBuilder builder = new StringBuilder(obj.getClass().getCanonicalName());
                    builder.append("-").append(m.getName());
                    if(m.getParameterCount() > 0) {
                        for(Class<?> paraType: m.getParameterTypes()) {
                            builder.append("-").append(paraType.getCanonicalName());
                        }
                    }
                    methodMap.put(builder.toString(), m);
                }
            }
        }
    }

    /**
     * 调用服务
     * @param service
     * @param method
     * @param paraTypes
     * @param args
     * @return
     * @throws Exception
     */
    public Object invoke(String service, String method, String[] paraTypes, Object[] args) throws Exception {
        List<Object> beans = interfaceObject.get(service);
        if(beans == null || beans.isEmpty()) {
            throw new NoSuchBeanDefinitionException("no found bean for " + service);
        }
        //通过接口获取到实例bean
        Object aimSerive = beans.get(0);
        //请求方法唯一签名：类全限定名-方法名-方法各个参数类型名
        StringBuilder methodSign = new StringBuilder(aimSerive.getClass().getCanonicalName());
        methodSign.append("-")
                .append(method);
        if(paraTypes != null && paraTypes.length > 0) {
            for(String paraType: paraTypes) {
                methodSign.append("-").append(paraType);
            }
        }
        //要调用的方法
        Method aimMethod = methodMap.get(methodSign.toString());
        if(aimMethod == null) {
            throw new NoSuchMethodException("no found method " + method + " for bean " + aimSerive.getClass().getCanonicalName());
        }
        return aimMethod.invoke(aimSerive, args);
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
