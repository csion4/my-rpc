package com.csion.server.invoke;

import com.csion.common.protocol.RpcRequest;

/**
 * Created by csion on 2022/2/17 14:45.
 * 未实现
 */
public class DefaultInvokeHandler implements InvokeHandler {
    @Override
    public Object invoke(RpcRequest req) {
        return req.getClassName() + req.getMethodName();
    }
}
