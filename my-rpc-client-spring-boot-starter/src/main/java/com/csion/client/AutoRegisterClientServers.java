package com.csion.client;

import com.csion.client.netty.NettyClient;
import com.csion.client.proxy.NettyProxyFactory;
import com.csion.common.protocol.properties.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * Created by csion on 2022/2/21 9:36.
 * 自动注册代理server
 */
@Slf4j
public class AutoRegisterClientServers implements BeanFactoryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private NettyClient nettyClient;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        nettyClient = new NettyClient(loadRpcProperties());
        nettyClient.connect();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        for (String i : ScanInterfacesCache.getInterfaces()) {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(i);
                beanFactory.registerSingleton(aClass.getSimpleName(), NettyProxyFactory.create(aClass, nettyClient));
            } catch (ClassNotFoundException e) {
                log.error("接口[" + i + "]beanFactory构建异常", e);
            }
        }
        ScanInterfacesCache.cleanCache();
    }

    public void close() {
        nettyClient.close();
    }


    private RpcProperties loadRpcProperties() {
        RpcProperties properties = new RpcProperties();
        Yaml yaml = new Yaml();
        Map load = yaml.load(AutoRegisterClientServers.class.getResourceAsStream("/application.yml"));
        Object myrpc = load.get("myrpc");
        if (myrpc instanceof Map) {
            Object client = ((Map) myrpc).get("client");
            if (client instanceof Map) {
                properties.setServer((String) ((Map) client).get("server"));
                properties.setPort((Integer) ((Map) client).get("port"));
            }
        }
        return properties;
    }
}
