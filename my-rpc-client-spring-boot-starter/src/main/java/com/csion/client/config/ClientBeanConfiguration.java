package com.csion.client.config;

import com.csion.client.AutoRegisterClientServers;
import com.csion.client.netty.NettyClient;
import com.csion.common.protocol.properties.RpcProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by csion on 2022/2/21 9:09.
 * 加载my rpc client bean
 */
@Configuration
public class ClientBeanConfiguration {

//    /**
//     * 加载客户端启动配置，貌似行不通
//     * @return
//     */
//    @Bean(name = "clientRpcProperties")
//    @ConfigurationProperties(prefix ="myrpc.client")
//    public RpcProperties loadRpcProperties(){
//        return new RpcProperties();
//    }

//    /**
//     * 加载客户端nettyClient
//     * @return
//     */
//    @Bean(initMethod = "connect", destroyMethod = "close")
//    public NettyClient loadNettyClient() {
//        return new NettyClient();
//    }

    /**
     * 加载代理server
     * @return
     */
    @Bean(destroyMethod = "close")
    public AutoRegisterClientServers  autoRegisterClientServers() {
        return new AutoRegisterClientServers();
    }
}
