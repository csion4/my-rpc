package com.csion.client.netty.handler;

import com.csion.common.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by csion on 2022/2/15 15:24.
 * 客户端对请求响应数据的处理，通过维护一个map集合存储所有的响应对象
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {   // 这里使用双向handler

    // 维护一个Map集合用于存储请求响应对应信息
    private final Map<String, RpcResponse> map = new ConcurrentHashMap<>(); // 这里最简单的设计是直接在map中存储response对象，这样在获取响应时直接轮询获取对应的requestId的key是否存在
    // private final Map<String, MyFuture> map = new ConcurrentHashMap<>(); // 这种则是通过Future来实现通知机制，保证数据写入完成后通知数据读取端

    /**
     * 将获取到的响应对象根据requestId进行区分保存到map中
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse){
            map.put(((RpcResponse) msg).getRequestId(), ((RpcResponse) msg));
        }
        super.channelRead(ctx, msg);
    }

    public RpcResponse getResponse(String requestId) {
        RpcResponse resp;
        // 压测发现，无锁轮询的方式对于客户端在大并发场景下会超级占用cpu，所以可以换成通知机制await/notify()
        // 无锁轮询
        do {
            resp = map.remove(requestId);
//            try {
//                System.out.println(Thread.currentThread().getName() + "************");
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } while (resp == null);

        return resp;
    }
}
