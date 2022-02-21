package com.csion.server.config;

import com.csion.common.protocol.properties.RpcProperties;
import com.csion.server.SpringBootInvokeHandler;
import com.csion.server.invoke.InvokeHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by csion on 2022/2/21 10:20.
 */
@Configuration
public class ServerBeanConfiguration {
    /**
     * 加载客户端启动配置
     * @return
     */
    @Bean(name = "serverRpcProperties")
    @ConfigurationProperties(prefix ="myrpc.server")
    public RpcProperties loadRpcProperties(){
        return new RpcProperties();
    }

    @Bean
    public InvokeHandler loadInvokeHandler() {
        return new SpringBootInvokeHandler();
    }
}
