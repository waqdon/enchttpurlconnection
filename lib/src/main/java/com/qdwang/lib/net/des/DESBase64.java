package com.qdwang.lib.net.des;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * author: create by qdwang
 * date: 2018/8/28 11:52
 * described：
 */
public class DESBase64 {
    static final char[] charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public DESBase64() {
    }

    public static String encode(byte[] data) {
        return encode(data, 0, data.length, (StringBuffer)null).toString();
    }

    public static String utf8_decode(byte[] bytes, int start, int len) {
        StringBuffer s = new StringBuffer();
        int i = start;

        while(i < start + len) {
            byte b = bytes[i++];
            if (b >> 7 == 0) {
                s.append((char)b);
            } else if (b >> 5 == -2) {
                s.append((char)((b & 31) << 6 | bytes[i++] & 63));
            } else if (b >> 4 == -2) {
                s.append((char)((b & 15) << 12 | (bytes[i++] & 63) << 6 | bytes[i++] & 63));
            }
        }

        return new String(s);
    }

    public static StringBuffer encode(byte[] data, int start, int len, StringBuffer buf) {
        if (buf == null) {
            buf = new StringBuffer(data.length * 3 / 2);
        }

        int end = len - 3;

        int i;
        int d;
        int d1;
        int d2;
        for(i = start; i <= end; i += 3) {
            if (data[i] < 0) {
                d1 = data[i] + 256;
            } else {
                d1 = data[i];
            }

            if (data[i + 1] < 0) {
                d2 = data[i + 1] + 256;
            } else {
                d2 = data[i + 1];
            }

            int d3;
            if (data[i + 2] < 0) {
                d3 = data[i + 2] + 256;
            } else {
                d3 = data[i + 2];
            }

            d = (d1 & 255) << 16 | (d2 & 255) << 8 | d3 & 255;
            buf.append(charTab[d >> 18 & 63]);
            buf.append(charTab[d >> 12 & 63]);
            buf.append(charTab[d >> 6 & 63]);
            buf.append(charTab[d & 63]);
        }

        if (i == start + len - 2) {
            if (data[i] < 0) {
                d1 = data[i] + 256;
            } else {
                d1 = data[i];
            }

            if (data[i + 1] < 0) {
                d2 = data[i + 1] + 256;
            } else {
                d2 = data[i + 1];
            }

            d = (d1 & 255) << 16 | (d2 & 255) << 8;
            buf.append(charTab[d >> 18 & 63]);
            buf.append(charTab[d >> 12 & 63]);
            buf.append(charTab[d >> 6 & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            if (data[i] < 0) {
                d1 = data[i] + 256;
            } else {
                d1 = data[i];
            }

            d = (d1 & 255) << 16;
            buf.append(charTab[d >> 18 & 63]);
            buf.append(charTab[d >> 12 & 63]);
            buf.append("==");
        }

        return buf;
    }

    static int decode(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 65;
        } else if (c >= 'a' && c <= 'z') {
            return c - 97 + 26;
        } else if (c >= '0' && c <= '9') {
            return c - 48 + 26 + 26;
        } else {
            switch(c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
        }
    }

    public static byte[] decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        decode(s, bos);
        return bos.toByteArray();
    }

    public static void decode(String s, ByteArrayOutputStream bos) {
        int i = 0;
        int len = s.length();

        while(true) {
            while(i < len && s.charAt(i) <= ' ') {
                ++i;
            }

            if (i == len) {
                break;
            }

            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6) + decode(s.charAt(i + 3));
            bos.write(tri >> 16 & 255);
            if (s.charAt(i + 2) == '=') {
                break;
            }

            bos.write(tri >> 8 & 255);
            if (s.charAt(i + 3) == '=') {
                break;
            }

            bos.write(tri & 255);
            i += 4;
        }

    }

    public static boolean needBase64(String s) {
        byte[] buff = null;
        if (s.length() > 2 && s.substring(0, 2).equals("=?")) {
            return true;
        } else {
            try {
                buff = s.getBytes("UTF8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }

            for(int i = 0; i < buff.length; ++i) {
                if (buff[i] < 32 || buff[i] > 127 || buff[i] == 60 || buff[i] == 62 || buff[i] == 38 || buff[i] == 39 || buff[i] == 34) {
                    return true;
                }
            }

            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        byte[] a = "�̽���".getBytes();
        String sEncode = encode(a);
        System.out.println("sEncode:" + sEncode);
        byte[] str = decode(sEncode);
        System.out.println("decode:" + new String(str));
        byte[] b = sEncode.getBytes();
        System.out.println("b:" + new String(b));
        byte[] b3 = decode("54ix");
        System.out.println(new String(b3, "utf-8"));
        System.out.println(new String(b3));
    }
}
