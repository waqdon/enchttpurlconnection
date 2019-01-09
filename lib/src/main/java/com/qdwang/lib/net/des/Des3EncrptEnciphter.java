package com.qdwang.lib.net.des;

/**
 * author: create by qdwang
 * date: 2018/8/28 11:47
 * described：
 */
public class Des3EncrptEnciphter implements IEnciphter {
    static Des3EncrptEnciphter enciphter;
    static final String format = "%1$s?request=%2$s&sign=%3$s";
    private static final String key = "a5bba17123564b039ae99766d9b06d6a997c9613c527462d8607fd861ba08aa0";
    private static final String type = "9b9123a29c45";

    int appType = 0;

    private Des3EncrptEnciphter() {
    }

    public static Des3EncrptEnciphter getInstance() {
        if (enciphter == null) {
            Class var0 = Des3EncrptEnciphter.class;
            synchronized(Des3EncrptEnciphter.class) {
                if (enciphter == null) {
                    enciphter = new Des3EncrptEnciphter();
                }
            }
        }

        return enciphter;
    }

    public String encrypt(String json) throws Exception {
        String request = DES3Encrpt.encrypt(json);
        /*if (0 == appType) {
            request = DES3Encrpt.encryptThreeDESECB(json);
        } else if (1 == appType) {
            request = DES3Encrpt.encrypt(json);
        }*/

        return request;
    }

    public String decrypt(String str) throws Exception {
        String result = DES3Encrpt.decrypt(str);
        /*if (0 == appType) {
            result = DES3Encrpt.decryptThreeDESECB(str);
        } else if (1 == appType) {
            result = DES3Encrpt.decrypt(str);
        }*/

        return result;
    }
}
