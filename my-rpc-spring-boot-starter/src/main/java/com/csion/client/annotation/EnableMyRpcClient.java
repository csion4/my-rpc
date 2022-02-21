package com.csion.client.annotation;

import com.csion.client.MyRpcClientAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by csion on 2022/2/16 14:23.
 * 注解开启 my rpc client
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MyRpcClientAutoConfiguration.class})
public @interface EnableMyRpcClient {
}
