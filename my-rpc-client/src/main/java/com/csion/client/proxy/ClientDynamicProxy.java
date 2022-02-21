package com.csion.client.proxy;

import com.csion.client.netty.NettyClient;
import com.csion.common.protocol.RpcRequest;
import com.csion.common.protocol.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by csion on 2022/2/11 16:37.
 * rpc动态代理处理
 */
public class ClientDynamicProxy<T> implements InvocationHandler {
    private Class<T> aClass;
    private NettyClient nettyClient;

    public ClientDynamicProxy(Class<T> aClass, NettyClient nettyClient) {
        this.nettyClient = nettyClient;
        this.aClass = aClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Arrays.asList(aClass.getDeclaredMethods()).contains(method)) {
            // 封装请求格式
            RpcRequest request = createRequest(method, args);

            // 发起请求获取响应
            RpcResponse resp = nettyClient.send(request);
            if (resp.getThrowable() != null) {
                throw new RuntimeException("rpc接口调用失败", resp.getThrowable());
            }
            return resp.getResult();
        }
        return method.invoke(this, args);
    }

    private RpcRequest createRequest(Method method, Object[] args) {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        return request;
    }
}
