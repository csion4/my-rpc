package com.csion.server.netty.handler;

import com.csion.common.protocol.RpcRequest;
import com.csion.common.protocol.RpcResponse;
import com.csion.server.invoke.InvokeHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by csion on 2022/2/11 17:31.
 * 服务端处理器，应该需要支持非spring体系（反射）和spring体系（spring context）的两套处理方式，实现方法的调用
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private InvokeHandler invokeHandler;

    public ServerHandler() {
    }

    public ServerHandler(InvokeHandler invokeHandler) {
        this.invokeHandler = invokeHandler;
    }

    /**
     * 解析请求方法中的调用信息，本地调用后向ctx中写入响应信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcRequest){
            RpcResponse rpcResponse = new RpcResponse();
            RpcRequest req = (RpcRequest) msg;
            try {
                rpcResponse.setRequestId(req.getRequestId());

                Object resp = invokeHandler.invoke(req);
                rpcResponse.setResult(resp);
            }catch (Throwable throwable) {
                rpcResponse.setThrowable(throwable);
                log.error("rpc接口调用异常", throwable);
            }
            ctx.writeAndFlush(rpcResponse);
        }
    }

    /**
     * 这里目的是为了能够实现对方法的调用；
     * 因为客户端是没有具体的方法实现的，所以传过来的类和方法都是接口类和接口中的方法，需要服务端匹配对应的实现类并完成调用
     * spring实现：因为spring容器自己维护了泛型方式的接口和实现类的关系，可以直接通过接口的class信息context.getBean(I.class);获取到实现类，如果有多个bean spring会报错
     * 反射方式：如果脱离spring，则需要服务端注册服务时就明确注册的服务方法和其接口的管理关系，这样就可以根据接口信息在注册中心中反向获取到方法实例对象再通过反射调用
     * @param req
     */
    private Object invoke(RpcRequest req) {
        return req.getClassName();
    }
}
