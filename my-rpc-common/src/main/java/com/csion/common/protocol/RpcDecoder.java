package com.csion.common.protocol;

import com.csion.common.protocol.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by csion on 2022/2/15 14:47.
 * 统一解码handler
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Serializer serializer;
    private Class<?> aClass;

    public RpcDecoder(Serializer serializer, Class<?> aClass) { // 指定序列化方式，指定响应对象类型也就是反序列化类型
        this.serializer = serializer;
        this.aClass = aClass;
    }

    /**
     * 解码操作
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode.....");
        // 通过对byteBuf的一些列操作读出来拆包后的报文
        if (byteBuf.readableBytes() < 4) { // 这里校验长度，因为编码时写入了4字节的int，所以如果小于这个长度则说明数据为读取完成，可以直接跳过
            return;
        }

        byteBuf.markReaderIndex(); // 标记一下当前读取位置，可用于回退读取点

        int i = byteBuf.readInt(); // 获取报文总长度
        if (byteBuf.readableBytes() < i) {  // 如果buf中可读取的长度小于报文总长度，则说明报文未全部接受，重置buf的reader index
            byteBuf.resetReaderIndex();     // 重置buf的reader index
            return;
        }

        byte[] data = new byte[i];
        byteBuf.readBytes(data);    // 读取整个报文长度的数据

        // 反序列化
        Object o = serializer.deSerializer(data, aClass);
        list.add(o);       // 将对象流转到下一个handler
    }
}
