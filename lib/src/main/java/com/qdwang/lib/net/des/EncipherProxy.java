package com.qdwang.lib.net.des;

/**
 * author: create by qdwang
 * date: 2018/8/28 11:47
 * described：
 */
public class EncipherProxy {
    static final String format = "%1$s?request=%2$s&sign=%3$s";
    static IEnciphter enciphter = Des3EncrptEnciphter.getInstance();

    public EncipherProxy() {
    }

    public static void init(IEnciphter e) {
        enciphter = e;
    }

    public static String encrypt(String json) throws Exception {
        return enciphter == null ? json : enciphter.encrypt(json);
    }

    public static String decrypt(String str) throws Exception {
        return enciphter == null ? str : enciphter.decrypt(str);
    }
}
