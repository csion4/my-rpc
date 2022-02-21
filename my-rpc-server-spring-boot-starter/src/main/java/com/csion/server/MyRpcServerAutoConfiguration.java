package com.csion.server;

import com.csion.common.protocol.properties.RpcProperties;
import com.csion.server.invoke.InvokeHandler;
import com.csion.server.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Created by csion on 2022/2/21 10:35.
 */
@Slf4j
public class MyRpcServerAutoConfiguration {
    private NettyServer nettyServer;

    @Resource
    InvokeHandler springBootInvokeHandler;
    @Resource
    RpcProperties serverRpcProperties;

    /**
     * 开启rpc
     */
    @PostConstruct
    public void startRpcServer() {
        nettyServer = new NettyServer(serverRpcProperties, springBootInvokeHandler);
        nettyServer.start();
    }

    @PreDestroy
    public void stopRpcServer() {
        try {
            nettyServer.close();
        } catch (InterruptedException e) {
            log.error("my rpc close err", e);
        }
    }
}
