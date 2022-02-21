package com.csion.server.netty;

import com.csion.common.protocol.RpcDecoder;
import com.csion.common.protocol.RpcEncoder;
import com.csion.common.protocol.RpcRequest;
import com.csion.common.protocol.properties.RpcProperties;
import com.csion.common.protocol.serialize.JSONSerializer;
import com.csion.server.invoke.DefaultInvokeHandler;
import com.csion.server.invoke.InvokeHandler;
import com.csion.server.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PreDestroy;

/**
 * Created by csion on 2022/2/11 17:28.
 */
@Slf4j
public class NettyServer {
    private RpcProperties rpcProperties;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private InvokeHandler invokeHandler;

    public NettyServer(RpcProperties rpcProperties, InvokeHandler invokeHandler) {
        this.rpcProperties = rpcProperties;
        this.invokeHandler = invokeHandler;
    }

    /**
     * 开启监听
     */
    public void start() {
        // 创建两个工作线程组
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        // 开启服务端启动类 ServerBootstrap  注意：与client端不同这个使用ServerBootstrap，client端使用Bootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)  // 注意：与client端不同这个使用NioServerSocketChannel，client端使用NioSocketChannel
                .option(ChannelOption.SO_BACKLOG, 1024) // 服务端的SO_BACKLOG可以加大一点，默认128
//                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() { // 注意：与client端不同这个使用childHandler因为只需要指定workerGroup中线程的处理

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcEncoder(new JSONSerializer())); // 写出编码  注意：outHandler需要放到最后一个inHandler之前，否则无法在inHandler中的ctx.writeAndFlush(rpcResponse);调用到该outHandler


                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4)); // 先拆包
                        pipeline.addLast(new RpcDecoder(new JSONSerializer(), RpcRequest.class));   // 解码，反序列化
                        // 服务端业务逻辑处理器 ServerHandler
                        pipeline.addLast(new ServerHandler(invokeHandler));  // 处理rpc服务调用，结果写回
                    }
                });
        bootstrap.bind(rpcProperties.getPort()).addListener(future -> {    // 服务端绑定端口启动netty服务
            if (future.isSuccess()){
                log.info("netty服务启动成功，绑定端口[{}]", rpcProperties.getPort());
            } else {
                log.error("netty服务启动失败", future.cause());
            }
        });
    }

    @PreDestroy
    public void close() throws InterruptedException {
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync(); // 这里为了保证缓存中的socket任务全部处理完，需要使用sync方法
        log.info("Netty服务停止...");
    }

    public static void main(String[] args) {
        RpcProperties p = new RpcProperties();
        p.setPort(9001);
        NettyServer nettyServer = new NettyServer(p, new DefaultInvokeHandler());
        nettyServer.start();
    }
}
