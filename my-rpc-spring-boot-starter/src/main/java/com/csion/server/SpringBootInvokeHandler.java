package com.csion.server;

import com.csion.common.protocol.RpcRequest;
import com.csion.server.invoke.InvokeHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by csion on 2022/2/17 14:51.
 * 解析请求，从spring context中获取对应的bean调用
 */
@Component
public class SpringBootInvokeHandler implements InvokeHandler, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public Object invoke(RpcRequest req) {
        try {
            Class<?> aClass = Class.forName(req.getClassName());
            Object bean = applicationContext.getBean(aClass);
            Method method = aClass.getMethod(req.getMethodName(), req.getParameterTypes());
            return method.invoke(bean, req.getParameters());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
