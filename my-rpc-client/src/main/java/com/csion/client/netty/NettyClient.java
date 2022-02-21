package com.csion.client.netty;

import com.csion.client.netty.handler.ClientHandler;
import com.csion.common.protocol.RpcDecoder;
import com.csion.common.protocol.RpcEncoder;
import com.csion.common.protocol.RpcRequest;
import com.csion.common.protocol.RpcResponse;
import com.csion.common.protocol.properties.RpcProperties;
import com.csion.common.protocol.serialize.JSONSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by csion on 2022/2/11 17:19.
 * 基于netty的网络传输
 */
@Slf4j
public class NettyClient {
    private RpcProperties properties;
    private Channel channel;
    private ClientHandler clientHandler;
    private NioEventLoopGroup worker;

    public NettyClient(RpcProperties properties){
        this.properties = properties;
    }

    /**
     * 建立连接
     */
    public void connect(){
        worker = new NioEventLoopGroup();
        clientHandler = new ClientHandler();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)   // 设置socket参数SO_KEEPALIVE用于主动探测连接活性，默认探测间隔2h
                .option(ChannelOption.TCP_NODELAY, true)    // 设置为true禁止Nagle算法，使得不会合并数据帧发送，减少发送延迟
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) // Netty参数，连接超时毫秒数，默认值30000毫秒即30秒。
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast(new RpcEncoder(new JSONSerializer())); // 编码处理器（outHandler）

                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4)); // 这里使用netty提供的一个拆包handler，在编码时首先写入了四个字节int长度的整个报文长度标识
                        pipeline.addLast(new RpcDecoder(new JSONSerializer(), RpcResponse.class));  // 解码反序列化
                        pipeline.addLast(clientHandler);    // 读取响应数据基于requestId进行封装缓存

                    }
                });

        ChannelFuture f = null;
        try {
            f = bootstrap.connect(properties.getServer(), properties.getPort()).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("服务端连接成功");
                } else {
                    log.error("服务端连接失败", future.cause()); // todo：这里可以增加失败重试机制
                }
            }).sync();      // 这里在启动时需要添加sync()来保证阻塞式的连接成功
        } catch (InterruptedException e) {
            throw new RuntimeException("服务端连接异常", e);
        }
        channel = f.channel();
        log.info("客户端获取channel...");
    }

//    private ChannelFuture connect0(Bootstrap bootstrap) {
//        try {
//            return bootstrap.connect(properties.getServer(), properties.getPort()).addListener(future -> {
//                if (future.isSuccess()) {
//                    log.info("服务端连接成功");
//                } else {
//                    bootstrap.config().group().schedule(() -> connect0(bootstrap), 5, TimeUnit.SECONDS);
//                    // log.error("服务端连接失败", future.cause()); // todo：这里可以增加失败重试机制
//                }
//            }).sync();      // 这里在启动时需要添加sync()来保证阻塞式的连接成功
//        } catch (InterruptedException e) {
//            throw new RuntimeException("服务端连接异常", e);
//        }
//    }

    /**
     * 发送信息
     * @param request
     */
    public RpcResponse send(final RpcRequest request) {
        try {
            log.info("发送请求：" + request.getClassName() + " -- " +  request.getMethodName());
            // **会阻塞**
            channel.writeAndFlush(request).await(); // 这里是客户端，采用同步的方式等待服务端结果，所以调用了await()方法阻塞了
        } catch (InterruptedException e) {
            log.error("数据发送异常", e);
        }
        // 等到await被唤醒后，从响应中获取响应结果
        log.info("请求发送完成：" + request.getClassName());
        return clientHandler.getResponse(request.getRequestId());
    }

    /**
     * todo：这里的关闭好用吗，在连接异常情况下如何关闭的？？
     */
    public void close() {
        worker.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }

//    public static void main(String[] args) {
//        NettyClient c = new NettyClient(new RpcProperties());
//        c.connect();
//
//        ITest test = NettyProxyFactory.create(ITest.class, c);
//         String s = test.test1();
//        System.out.println(test.toString());
//
//    }
}
