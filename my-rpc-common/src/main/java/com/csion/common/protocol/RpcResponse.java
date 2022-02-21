package com.csion.common.protocol;

import lombok.Data;

/**
 * Created by csion on 2022/2/15 15:03.
 * 定义rpc响应格式
 */
@Data
public class RpcResponse {
    /**
     * 请求对象的ID
     */
    private String requestId;

    /**
     * 返回的结果
     */
    private Object result;

    /**
     * 异常信息
     */
    private Throwable throwable;

}
