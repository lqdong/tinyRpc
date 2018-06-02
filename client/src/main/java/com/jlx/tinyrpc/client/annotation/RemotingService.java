package com.jlx.tinyrpc.client.annotation;

import java.lang.annotation.*;

/**
 * 远程服务注解
 * 以 group + 接口名 唯一定位一个服务实现
 * 服务端注解在类上
 * 调用端注解在属性上
 * @author liqiang.dong
 * @date 2018/5/30 16:08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface RemotingService {

    /**
     * 服务组名
     * @return
     */
    String group();

}
