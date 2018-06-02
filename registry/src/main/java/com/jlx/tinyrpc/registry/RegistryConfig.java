package com.jlx.tinyrpc.registry;

import java.io.File;

/**
 * 注册中心配置
 */
public class RegistryConfig {

    private String kvConfigPath = System.getProperty("user.home") + File.separator + "registry" + File.separator + "kvConfig.json";

    public String getKvConfigPath() {
        return kvConfigPath;
    }

    public void setKvConfigPath(String kvConfigPath) {
        this.kvConfigPath = kvConfigPath;
    }

}
