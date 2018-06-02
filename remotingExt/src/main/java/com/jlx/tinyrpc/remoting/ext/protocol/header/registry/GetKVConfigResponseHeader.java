package com.jlx.tinyrpc.remoting.ext.protocol.header.registry;


import com.jlx.tinyrpc.remoting.CommandCustomHeader;
import com.jlx.tinyrpc.remoting.annotation.CFNullable;
import com.jlx.tinyrpc.remoting.exception.RemotingCommandException;

public class GetKVConfigResponseHeader implements CommandCustomHeader {
    @CFNullable
    private String value;

    public void checkFields() throws RemotingCommandException {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
