package com.csion.common.protocol.properties;

import lombok.Data;

/**
 * Created by csion on 2022/2/17 19:22.
 * 启动参数
 */
@Data
public class RpcProperties {
    private String server;
    private Integer port;
}
