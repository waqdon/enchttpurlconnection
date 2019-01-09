package com.qdwang.lib.net.des;

/**
 * author: create by qdwang
 * date: 2018/8/28 11:47
 * described：
 */
public interface IEnciphter {
    /**
     * 加密
     * @param var1
     * @return
     * @throws Exception
     */
    String encrypt(String var1) throws Exception;

    /**
     * 解密
     * @param var1
     * @return
     * @throws Exception
     */
    String decrypt(String var1) throws Exception;
}
