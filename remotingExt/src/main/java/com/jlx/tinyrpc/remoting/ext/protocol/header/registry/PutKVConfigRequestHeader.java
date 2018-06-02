package com.jlx.tinyrpc.remoting.ext.protocol.header.registry;


import com.jlx.tinyrpc.remoting.CommandCustomHeader;
import com.jlx.tinyrpc.remoting.annotation.CFNotNull;
import com.jlx.tinyrpc.remoting.exception.RemotingCommandException;

public class PutKVConfigRequestHeader implements CommandCustomHeader {
    @CFNotNull
    private String namespace;
    @CFNotNull
    private String key;
    @CFNotNull
    private String value;

    public void checkFields() throws RemotingCommandException {
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PutKVConfigRequestHeader{" +
                "namespace='" + namespace + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
