package com.qdwang.lib.utils;

import java.security.MessageDigest;

/**
 * 创建时间：2017/11/29
 * 编写人：qdwang
 * 功能描述：md5
 */

public class MD5Utils {
    public static String md5Upper(String request) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5Utils");
            return byte2hexUpper(md.digest(request.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String md5Upper(byte[] request) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5Utils");
            return byte2hexUpper(md.digest(request));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * md5加密
     * @param request 需要加密内容
     * @return
     */
    public static String md5Lower(String request) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5Utils");
            return byte2hexLower(md.digest(request.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String md5TwiceUpper(String md5dec) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5Utils");
            return byte2hexUpper(md.digest(md.digest(md5dec.getBytes("utf-8"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2hexUpper(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static String byte2hexLower(byte[] b) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString();
    }
}
