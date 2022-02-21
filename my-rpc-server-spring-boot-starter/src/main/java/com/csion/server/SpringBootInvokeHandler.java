package com.csion.server;

import com.csion.common.protocol.RpcRequest;
import com.csion.server.invoke.InvokeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by csion on 2022/2/21 10:15.
 * 解析请求，从spring context中获取对应的bean调用
 */
@Slf4j
public class SpringBootInvokeHandler implements InvokeHandler, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object invoke(RpcRequest req) {
        try {
            Class<?> aClass = Class.forName(req.getClassName());
            Object bean = applicationContext.getBean(aClass);
            Method method = aClass.getMethod(req.getMethodName(), req.getParameterTypes());
            return method.invoke(bean, req.getParameters());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            log.error("方法调用异常", e);
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
