package com.csion.common.protocol.serialize;

import com.alibaba.fastjson.JSON;

/**
 * Created by csion on 2022/2/15 10:50.
 *  序列化，通过泛型对多个序列化的实现
 */
public interface Serializer {
    /**
     * 序列化，将对象转化为二进制
     * @param obj
     * @return
     */
    byte[] doSerializer(Object obj);

    /**
     * 反序列化，将二进制转化为对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deSerializer(byte[] bytes, Class<T> clazz);
}
