package com.csion.client;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by csion on 2022/2/16 19:50.
 * 客户端扫描的接口服务缓存
 */
public class ScanInterfacesCache {
    private static Set<String> interfaces = new HashSet<>();

    public static void addInterface(String name) {
        interfaces.add(name);
    }

    public static Set<String> getInterfaces() {
        return interfaces;
    }

    public static void cleanCache() {
        interfaces = null;
    }
}
