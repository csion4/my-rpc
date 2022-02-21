package com.csion.client;

import com.csion.client.netty.NettyClient;
import com.csion.client.proxy.NettyProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;

/**
 * Created by csion on 2022/2/21 9:36.
 * 自动注册代理server
 */
@Slf4j
public class AutoRegisterClientServers implements InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Resource
    private NettyClient nettyClient;

    @Override
    public void afterPropertiesSet() throws Exception {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
