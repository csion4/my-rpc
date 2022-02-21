package com.csion.client.config;

import com.csion.client.AutoRegisterClientServers;
import com.csion.client.netty.NettyClient;
import com.csion.common.protocol.properties.RpcProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by csion on 2022/2/17 19:37.
 */
@Configuration
public class ClientBeanConfiguration {

    /**
     * 加载客户端启动配置
     * @return
     */
    @Bean(name = "clientRpcProperties")
    @ConfigurationProperties(prefix ="myrpc.client")
    public RpcProperties loadRpcProperties(){
        return new RpcProperties();
    }

    /**
     * 加载客户端nettyClient
     * @param clientRpcProperties
     * @return
     */
    @Bean(initMethod = "connect", destroyMethod = "close")
    @Resource
    public NettyClient loadNettyClient(RpcProperties clientRpcProperties) {
        return new NettyClient(clientRpcProperties);
    }

    /**
     * 加载代理server
     * @return
     */
    @Bean
    public AutoRegisterClientServers autoRegisterClientServers() {
        return new AutoRegisterClientServers();
    }
}
