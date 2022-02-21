package com.csion.server.annotation;

import java.lang.annotation.*;

/**
 * Created by csion on 2022/2/21 10:23.
 * 注解开启 my rpc server
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface EnableMyRpcServer {
}
