package com.csion.common.protocol;

import com.csion.common.protocol.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by csion on 2022/2/15 10:37.
 * 统一编码handler
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {
    private Class<?> aClass;
    private Serializer serializer;

    /**
     * 指定使用的序列化方式
     * @param serializer
     */
    public RpcEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 将待发送的RpcRequest或RpcResponse进行编码，序列化，注意先写了一个Int长度的数据总长度；可用于拆包，在节码是需要留意；
     * @param channelHandlerContext
     * @param obj
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object obj, ByteBuf byteBuf) throws Exception {
        System.out.println("encode..." + obj);
        byte[] bytes = serializer.doSerializer(obj);
        byteBuf.writeInt(bytes.length); // 这里可用于拆包，可以使用LengthFieldBasedFrameDecoder
        byteBuf.writeBytes(bytes);
    }
}
