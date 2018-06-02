package com.jlx.tinyrpc.remoting;


import com.jlx.tinyrpc.remoting.exception.RemotingCommandException;

public interface CommandCustomHeader {
    void checkFields() throws RemotingCommandException;
}
