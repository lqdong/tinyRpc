package com.jlx.tinyrpc.registry.kvconfig;


import com.jlx.tinyrpc.common.constant.LoggerName;
import com.jlx.tinyrpc.common.log.Logger;
import com.jlx.tinyrpc.common.log.LoggerFactory;
import com.jlx.tinyrpc.common.utils.MixAll;
import com.jlx.tinyrpc.registry.RegistryController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * K-V配置管理类
 */
public class KVConfigManager {

    private static final Logger log = LoggerFactory.getLogger(LoggerName.REGISTRY_LOGGER_NAME);

    private final RegistryController registryController;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final HashMap<String/* Namespace */, HashMap<String/* Key */, String/* Value */>> configTable =
        new HashMap<String, HashMap<String, String>>();

    public KVConfigManager(RegistryController registryController) {
        this.registryController = registryController;
    }

    /**
     * 加载配置
     */
    public void load() {
        String content = null;
        try {
            content = MixAll.file2String(this.registryController.getRegistryConfig().getKvConfigPath());
        } catch (IOException e) {
            log.warn("Load KV config table exception", e);
        }
        if (content != null) {
            KVConfigSerializeWrapper kvConfigSerializeWrapper =
                KVConfigSerializeWrapper.fromJson(content, KVConfigSerializeWrapper.class);
            if (null != kvConfigSerializeWrapper) {
                this.configTable.putAll(kvConfigSerializeWrapper.getConfigTable());
                log.info("load KV config table OK");
            }
        }
    }

    /**
     * 将配置信息转化为JSON串，持久化到磁盘文件中
     */
    public void persist() {
        try {
            this.lock.readLock().lockInterruptibly();
            try {
                KVConfigSerializeWrapper kvConfigSerializeWrapper = new KVConfigSerializeWrapper();
                kvConfigSerializeWrapper.setConfigTable(this.configTable);

                String content = kvConfigSerializeWrapper.toJson();

                if (null != content) {
                    MixAll.string2File(content, this.registryController.getRegistryConfig().getKvConfigPath());
                }
            } catch (IOException e) {
                log.error("persist kvconfig Exception, "
                        + this.registryController.getRegistryConfig().getKvConfigPath(), e);
            } finally {
                this.lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            log.error("persist InterruptedException", e);
        }

    }

    public void putKVConfig(final String namespace, final String key, final String value) {
        try {
            this.lock.writeLock().lockInterruptibly();
            try {
                HashMap<String, String> kvTable = this.configTable.get(namespace);
                if (null == kvTable) {
                    kvTable = new HashMap<String, String>();
                    this.configTable.put(namespace, kvTable);
                    log.info("putKVConfig create new Namespace {}", namespace);
                }

                final String prev = kvTable.put(key, value);
                if (null != prev) {
                    log.info("putKVConfig update config item, Namespace: {} Key: {} Value: {}",
                        namespace, key, value);
                } else {
                    log.info("putKVConfig create new config item, Namespace: {} Key: {} Value: {}",
                        namespace, key, value);
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            log.error("putKVConfig InterruptedException", e);
        }

        this.persist();
    }

    public void deleteKVConfig(final String namespace, final String key) {
        try {
            this.lock.writeLock().lockInterruptibly();
            try {
                HashMap<String, String> kvTable = this.configTable.get(namespace);
                if (null != kvTable) {
                    String value = kvTable.remove(key);
                    log.info("deleteKVConfig delete a config item, Namespace: {} Key: {} Value: {}",
                        namespace, key, value);
                }
            } finally {
                this.lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            log.error("deleteKVConfig InterruptedException", e);
        }

        this.persist();
    }

    public String getKVConfig(final String namespace, final String key) {
        try {
            this.lock.readLock().lockInterruptibly();
            try {
                HashMap<String, String> kvTable = this.configTable.get(namespace);
                if (null != kvTable) {
                    return kvTable.get(key);
                }
            } finally {
                this.lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            log.error("getKVConfig InterruptedException", e);
        }

        return null;
    }

    public void printAllPeriodically() {
        try {
            this.lock.readLock().lockInterruptibly();
            try {
                log.info("--------------------------------------------------------");
                log.info("configTable SIZE: {}", this.configTable.size());
                Iterator<Entry<String, HashMap<String, String>>> it = this.configTable.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, HashMap<String, String>> next = it.next();
                    Iterator<Entry<String, String>> itSub = next.getValue().entrySet().iterator();
                    while (itSub.hasNext()) {
                        Entry<String, String> nextSub = itSub.next();
                        log.info("configTable NS: {} Key: {} Value: {}", next.getKey(), nextSub.getKey(), nextSub.getValue());
                    }
                }
            } finally {
                this.lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            log.error("printAllPeriodically InterruptedException", e);
        }
    }
}
