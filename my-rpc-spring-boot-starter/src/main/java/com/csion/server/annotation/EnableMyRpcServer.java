package com.csion.server.annotation;

import java.lang.annotation.*;

/**
 * Created by csion on 2022/2/17 14:23.
 * 注解开启 my rpc server
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface EnableMyRpcServer {
}
