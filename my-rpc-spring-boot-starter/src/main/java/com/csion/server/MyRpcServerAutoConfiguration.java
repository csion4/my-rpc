package com.csion.server;

import com.csion.common.protocol.properties.RpcProperties;
import com.csion.server.invoke.DefaultInvokeHandler;
import com.csion.server.invoke.InvokeHandler;
import com.csion.server.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * Created by csion on 2022/2/17 15:35.
 */
@Slf4j
public class MyRpcServerAutoConfiguration {
    private NettyServer nettyServer;

    @Resource
    InvokeHandler springBootInvokeHandler;
    @Resource
    RpcProperties serviceRpcProperties; // 这里客户端服务端都复用了这个RpcProperties配置bean，所以需要通过byName来注入bean，否则会报错


    /**
     * 开启rpc
     */
    @PostConstruct
    public void startRpcServer() {
        nettyServer = new NettyServer(serviceRpcProperties, springBootInvokeHandler);
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
