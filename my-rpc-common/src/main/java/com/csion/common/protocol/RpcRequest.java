package com.csion.common.protocol;

import lombok.Data;

/**
 * Created by csion on 2022/2/11 16:54.
 * 定义rpc请求格式
 */
@Data
public class RpcRequest {
    /**
     * 请求对象的ID
     */
    private String requestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 入参
     */
    private Object[] parameters;
}
