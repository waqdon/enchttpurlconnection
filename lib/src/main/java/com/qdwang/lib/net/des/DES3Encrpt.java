package com.qdwang.lib.net.des;

import android.text.TextUtils;
import android.util.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * author: create by qdwang
 * date: 2018/8/28 11:48
 * described：
 */
public class DES3Encrpt {
    private static final String key = "oiekr@#8$8CAB4D1BB@#$%^&*()_+(*&$5537*";
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String DEFAULT_ENCRYPT_KEY = "2En!HE%SYQc#W-B3";
    private static final String IV_STRING = "16-Bytes--String";

    public DES3Encrpt() {
    }

    public static String decryptThreeDESECB(String src) throws Exception {
        String result = null;
        src = src.trim();

        try {
            new DESBase64();
            byte[] decryptStr = DESBase64.decode(src);
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec("oiekr@#8$8CAB4D1BB@#$%^&*()_+(*&$5537*".getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(2, key, sr);
            byte[] decryResult = cipher.doFinal(decryptStr);
            result = new String(decryResult, "UTF-8");
            return result;
        } catch (Exception var9) {
            throw new RuntimeException("解密错误，错误信息：", var9);
        }
    }

    public static String encryptThreeDESECB(String content) {
        String result = null;
        try {
            SecureRandom sr = new SecureRandom();
            DESKeySpec deskey = new DESKeySpec("oiekr@#8$8CAB4D1BB@#$%^&*()_+(*&$5537*".getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(deskey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(1, key, sr);
            new DESBase64();
            result = DESBase64.encode(cipher.doFinal(content.getBytes("UTF-8")));
            return result;
        } catch (Exception var7) {
            throw new RuntimeException("加密错误，错误信息：", var7);
        }
    }

    public static String encryptThreeDESECB(String src, String key1) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec("oiekr@#8$8CAB4D1BB@#$%^&*()_+(*&$5537*".getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(1, securekey);
        byte[] b = cipher.doFinal(src.getBytes("utf-8"));
        return (new String(Base64.encode(b, 0))).replaceAll("\r", "").replaceAll("\n", "");
    }

    public static String decryptThreeDESECB(String src, String key1) throws Exception {
        byte[] bytesrc = Base64.decode(src, 0);
        DESedeKeySpec dks = new DESedeKeySpec(TextUtils.isEmpty(key1) ? "oiekr@#8$8CAB4D1BB@#$%^&*()_+(*&$5537*".getBytes("UTF-8") : key1.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(2, securekey);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    public static String encrypt(String content) {
        return encrypt(content, "2K!AI@EK;%SYQc#W");
//        return encrypt(content, "2En!HE%SYQc#W-B3");
    }

    public static String decrypt(String content) {
        return decrypt(content, "2K!AI@EK;%SYQc#W");
//        return decrypt(content, "2En!HE%SYQc#W-B3");
    }

    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] byteContent = content.getBytes("utf-8");
            byte[] enCodeFormat = password.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = "16-Bytes--String".getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            cipher.init(1, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(byteContent);
            return (new BASE64Encoder()).encode(encryptedBytes);
        } catch (Exception var9) {
//            LogUtils.e("AESCryptUtil#encrypt catch error,msg-{},", var9.getLocalizedMessage(), new Object[]{var9});
            return null;
        }
    }

    public static String decrypt(String content, String password) {
        try {
            byte[] enCodeFormat = password.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = "16-Bytes--String".getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal((new BASE64Decoder()).decodeBuffer(content));
            return new String(result, "utf-8");
        } catch (Exception var8) {
//            LogUtils.e("AESCryptUtil#decrypt catch error,msg-{},", var8.getLocalizedMessage(), new Object[]{var8});
            return null;
        }
    }
}
