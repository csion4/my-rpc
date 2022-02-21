package com.csion.client.proxy;

import com.csion.client.netty.NettyClient;

import java.lang.reflect.Proxy;

/**
 * Created by csion on 2022/2/11 16:33.
 * 动态代理
 */
public class NettyProxyFactory {
    public static <T> T create(Class<T> clazz, NettyClient c){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new ClientDynamicProxy<T>(clazz, c));
    }
}
