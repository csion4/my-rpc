package com.csion.client.annotation;

import java.lang.annotation.*;

/**
 * Created by csion on 2022/2/16 14:23.
 * 客户端扫描需要调用的接口服务
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRpcService {
}
