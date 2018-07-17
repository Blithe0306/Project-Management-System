/**
 *Administrator
 */
package com.ft.otp.util.alg;

import java.io.IOException;

import sun.misc.BASE64Decoder;

import com.ft.otp.util.tool.StrTool;

/**
 * 算法结果转换帮助类
 *
 * @Date in Apr 18, 2011,10:36:52 AM
 *
 * @author TBM
 */
public class AlgConvert {

    /**
     * 打印16进制String
     * @Date in Apr 18, 2011,10:43:32 AM
     * @param hint
     * @param b
     * @return
     */
    public static String printHexString(String hint, byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            return hex.toUpperCase();
        }
        return "";
    }

    /**
     * Byte[] -> 16进制String
     * @Date in Apr 18, 2011,10:38:26 AM
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

    /** 将两位16进制转为一个byte*/
    public static byte[] hexString2Byte(String hexStr) {
        int upbound = hexStr.length() / 2;
        byte[] bArr2 = new byte[upbound];
        for (int k = 0; k < upbound; k++) {
            int i = k * 2;
            String temp = hexStr.substring(i, i + 2);
            bArr2[k] = (byte) Integer.parseInt(temp, 16);
        }
        return bArr2;
    }

    /**将两位16进制转为一个byte*/
    public static byte[] hexToBytes(String hexString) {
        char[] hex = hexString.toCharArray();
        //转rawData长度减半
        int length = hex.length / 2;
        byte[] rawData = new byte[length];
        for (int i = 0; i < length; i++) {
            //先将hex转10进制
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            //将第一个值的二进制位左平移4位,ex: 00001000 => 10000000 (8=>128)
            //然后与第二个值的二进制位做连接 ex: 10000000 | 00001100 => 10001100 (137)
            int value = (high << 4) | low;
            //与FFFFFFFF作补集
            if (value > 127)
                value -= 256;
            //最后转回byte就OK
            rawData[i] = (byte) value;
        }
        return rawData;
    }

    public static int bytesToInt(byte[] bytes) {
        int num = 0;
        num += (bytes[0] << 24);
        num += (bytes[1] << 16);
        num += (bytes[2] << 8);
        num += bytes[3];

        return num;
    }

    public static byte[] htonl(int x) {
        byte[] res = new byte[4];

        for (int i = 0; i < 4; i++) {
            res[i] = (byte) (x >> 24);
            x <<= 8;
        }
        return res;
    }

    public static void orgByteArray(byte[] src, int start, byte[] ch) throws Exception {
        for (int i = 0; i < ch.length; i++) {
            src[start] = ch[i];
            start = start + 1;
        }
    }

    /**
     * 
     * 字节数组转化成16进制
     * 原理：Java中byte用二进制表示占用8位，而我们知道16进制的每个字符需要用4位二进制位来表示（23 + 22 + 21 + 20 = 15），
     *      所以我们就可以把每个byte转换成两个相应的16进制字符，即把byte的高4位和低4位分别转换成相应的16进制字符H和L，
     *      并组合起来得到byte转换到16进制字符串的结果new String(H) + new String(L)。即byte用十六进制表示只占2位。
     * @Date in Dec 20, 2011,9:53:48 AM
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }

        return stringBuilder.toString();
    }
    
    /**
     * Base64 - String
     * @Date in Nov 1, 2013,3:28:55 PM
     * @param desStr
     * @return
     */
    public static String base64DecCode(String desStr) {
        BASE64Decoder dec = new BASE64Decoder();
        byte[] ciphertext = null;
        try {
            ciphertext = dec.decodeBuffer(desStr);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes2HexString(ciphertext);
    }

}
