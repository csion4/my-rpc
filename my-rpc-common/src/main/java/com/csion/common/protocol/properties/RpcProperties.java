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

    private Integer so_backlog = 128;
    private Boolean so_keepalive = true;
    private Boolean tcp_nodelay = true;
    private Integer connect_timeout_millis = 5000;
}
