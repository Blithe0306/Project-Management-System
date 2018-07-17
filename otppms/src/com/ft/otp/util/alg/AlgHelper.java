package com.ft.otp.util.alg;

import com.ft.otp.util.tool.StrTool;

public class AlgHelper {
    /**
     * 高字节序数据转成short
     */
    public static short bigEndianToShort(byte[] bs, int off) {
        int n = bs[off] << 8;
        n |= (bs[++off] & 0xff);
        return (short) n;
    }

    /**
     * short转换成高字节序数据
     */
    public static void shortToBigEndian(short n, byte[] bs, int off) {
        bs[off] = (byte) (n >>> 8);
        bs[++off] = (byte) (n);
    }

    /**
     * 高字节序数据转成整形
     */
    public static int bigEndianToInt(byte[] bs, int off) {
        int n = bs[off] << 24;
        n |= (bs[++off] & 0xff) << 16;
        n |= (bs[++off] & 0xff) << 8;
        n |= (bs[++off] & 0xff);
        return n;
    }

    /**
     * 整形转换成高字节序数据
     */
    public static void intToBigEndian(int n, byte[] bs, int off) {
        bs[off] = (byte) (n >>> 24);
        bs[++off] = (byte) (n >>> 16);
        bs[++off] = (byte) (n >>> 8);
        bs[++off] = (byte) (n);
    }

    /**
     * 高字节序数据转成long型
     */
    public static long bigEndianToLong(byte[] bs, int off) {
        int hi = bigEndianToInt(bs, off);
        int lo = bigEndianToInt(bs, off + 4);
        return ((long) (hi & 0xffffffffL) << 32) | (long) (lo & 0xffffffffL);
    }

    /**
     * long型数据转成高字节序
     */
    public static void longToBigEndian(long n, byte[] bs, int off) {
        intToBigEndian((int) (n >>> 32), bs, off);
        intToBigEndian((int) (n & 0xffffffffL), bs, off + 4);
    }

    /**
     * 十六进制转二进制码表
     */
    static byte[] hex2bin_table = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x00 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x10 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x20 */
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0, 0, 0, /* 0x30 */
    0000, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0000, /* 0x40 */
    0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x50 */
    0000, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0000, /* 0x60 */
    0000, 0000, 0000, 0000, 0000, 0000, 0000, 0000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x70 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x80 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0x90 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0xa0 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0xb0 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0xc0 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0xd0 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, /* 0xe0 */
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 /* 0xf0 */
    };

    /**
     * 将十六进制字符串转换成二进制字节数组
     * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
     */
    public static byte[] hexStringToBytes(String strHexs) {
        if (strHexs == null || strHexs.length() % 2 != 0) {
            return null;
        }

        int len = strHexs.length() / 2;
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = (byte) (hex2bin_table[strHexs.charAt(i * 2)] << 4 | hex2bin_table[strHexs.charAt(i * 2 + 1)]);
        }

        return ret;
    }

    /**
     * 单字节的byte数据转换成用字符串表示的16进制数
     */
    public static String byteToHex(byte b) {
        StringBuffer result = new StringBuffer();
        result.append("0123456789ABCDEF".charAt(0xf & b >> 4));
        result.append("0123456789ABCDEF".charAt(b & 0xf));
        return result.toString();
    }

    /**
     * byte数组转换成16进制表示的字符串
     */
    public static String bytesToHexs(byte[] bs) {
        if (bs == null) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            result.append(byteToHex(bs[i]));
        }
        return result.toString();
    }

    /**
     * byte[] -> 十六进制 String 串
     * 
     * @Date in Jan 28, 2013,11:49:39 AM
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        String ret = "";

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            ret += hex.toUpperCase();
        }

        return ret;
    }

    /**
     * Byte[] -> 16进制String 指定长度
     * @Date in Apr 18, 2011,10:38:26 AM
     * @param b
     * @return
     */
    public static String bytes2HexStringEx(byte[] b, int len) {
        String ret = "";

        for (int i = 0; i < len; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 16进制String -> String
     * @Date in Apr 18, 2011,10:38:54 AM
     * @param hex
     * @return
     */
    public static String hexToString(String hex) {
        if (hex == null)
            return "";

        int length = hex.length() & -2;

        StringBuffer result = new StringBuffer(length / 2);
        hexToString(hex, result);

        return result.toString();
    }

    public static void hexToString(String hex, StringBuffer out) {
        if (hex == null)
            return;

        int length = hex.length() & -2;

        for (int pos = 0; pos < length; pos += 2) {
            int this_char = 0;
            try {
                this_char = Integer.parseInt(hex.substring(pos, pos + 2), 16);
            } catch (NumberFormatException ex) {
            }
            out.append((char) this_char);
        }
    }

    /**
     * String -> 16进制String
     * @Date in Apr 18, 2011,10:41:45 AM
     * @param str
     * @return
     */
    public static final String stringToHex(String str) {
        if (!StrTool.strNotNull(str))
            return "";

        int length = str.length();

        StringBuffer result = new StringBuffer(length * 2);
        stringToHex(str, result);

        return result.toString().toUpperCase();
    }

    public static final void stringToHex(String str, StringBuffer out) {
        if (str == null)
            return;

        int length = str.length();

        for (int pos = 0; pos < length; pos++) {
            int this_char = (int) str.charAt(pos);

            for (int digit = 0; digit < 2; digit++) {
                int this_digit = this_char & 0xf0;
                this_char = this_char << 4;
                this_digit = (this_digit >> 4);

                if (this_digit >= 10) {
                    out.append((char) (this_digit + 87));
                } else {
                    out.append((char) (this_digit + 48));
                }
            }
        }
    }

    /**
     * byte[] -> int
     * 
     * @Date in Jan 28, 2013,11:48:14 AM
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        int num = 0;
        for (int i = 0; i < bytes.length; i++) {
            num += (bytes[i] & 0xFF) << (8 * (3 - i));
        }

        return num;
    }

    /**
     * int -> byte[]
     * 
     * @Date in Jan 28, 2013,11:47:58 AM
     * @param x
     * @return
     */
    public static byte[] intToByte(int x) {
        byte[] res = new byte[4];

        for (int i = 0; i < 4; i++) {
            res[i] = (byte) (x >> 24);
            x <<= 8;
        }

        return res;
    }

    /**
     * byte数组比较，是否相等
     * 
     * @Date in Jan 28, 2013,11:46:47 AM
     * @param bs1
     * @param bs2
     * @return
     */
    public static boolean bytesCompare(byte[] bs1, byte[] bs2) {
        if (bs1 == null || bs2 == null || bs1.length != bs2.length) {
            return false;
        }

        for (int i = 0; i < bs1.length; i++) {
            if (bs1[i] != bs2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * 数组copy
     * 
     * @Date in Dec 13, 2011,10:16:58 AM
     * @param src
     * @param start
     * @param ch
     * @throws Exception
     */
    public static void orgByteArray(byte[] src, int start, byte[] ch) throws Exception {
        for (int i = 0; i < ch.length; i++) {
            src[start] = ch[i];
            start = start + 1;
        }
    }

}