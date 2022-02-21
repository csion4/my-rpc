package com.csion.server.invoke;

import com.csion.common.protocol.RpcRequest;

/**
 * Created by csion on 2022/2/17 14:39.
 * 方法调用
 */
public interface InvokeHandler {
    Object invoke(RpcRequest req);
}
