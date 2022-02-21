package com.csion.common.protocol.serialize;

import com.alibaba.fastjson.JSON;

/**
 * Created by csion on 2022/2/15 10:42.
 * 使用json进行序列化
 */
public class JSONSerializer implements Serializer {


    /**
     * 序列化，将对象转化为二进制
     * @param obj
     * @return
     */
    public byte[] doSerializer(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    /**
     * 反序列化，将二进制转化为对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T deSerializer(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }
}
