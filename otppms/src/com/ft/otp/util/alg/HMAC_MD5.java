package com.ft.otp.util.alg;

import java.io.IOException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * HMAC_MD5算法
 *
 * @Date in Apr 12, 2011,11:52:34 AM
 *
 * @author ZJY
 */
public class HMAC_MD5 {
    private static final String KEY_MAC = "HmacMD5";
    private static final String ISO8859 = "ISO-8859-1";

    /**
     * HMACMD5 encode
     * 
     * @param data
     * @param key
     * @return String
     * @throws Exception
     */
    public static String encodeHMAC(String source, String key) throws Exception {
        Mac hmac_md5 = Mac.getInstance(KEY_MAC);
        hmac_md5.init(new SecretKeySpec(key.getBytes(ISO8859), KEY_MAC));
        byte[] b = hmac_md5.doFinal(source.getBytes(ISO8859));
        return getHexStr(b);
    }

    /**
     * HMACMD5 encode
     * 
     * @param data
     * @param key
     * @return byte
     * @throws Exception
     */
    public static byte[] encodeHMACByte(String source, String key)
            throws Exception {
        // byte key
        byte[] bkey = { (byte) 0x4c, (byte) 0x1a, (byte) 0x53, (byte) 0xf4,
                (byte) 0x6e, (byte) 0xba, (byte) 0xec, (byte) 0xa9,
                (byte) 0xf3, (byte) 0xb7, (byte) 0xc6, (byte) 0xd5,
                (byte) 0x59, (byte) 0x3e, (byte) 0x10, (byte) 0x7c };

        // 方式一
        // SecretKey keyStr = new SecretKeySpec(bkey, KEY_MAC);
        //
        // // Create a MAC object using HMAC-MD5 and initialize with key
        // Mac mac = Mac.getInstance(keyStr.getAlgorithm());
        // mac.init(keyStr);
        //      
        // // Encode the string into bytes using utf-8 and digest it
        // byte[] bdata = source.getBytes();
        // byte[] digest = mac.doFinal(bdata);

        // return digest;

        // 方式二
        Mac hmac_md5 = Mac.getInstance(KEY_MAC);
        hmac_md5.init(new SecretKeySpec(bkey, KEY_MAC));
        byte[] bdate = hmac_md5.doFinal(source.getBytes(ISO8859));
        return bdate;
    }

    /**
     * byte[] -> String
     * 
     * @param b
     * @return String
     */
    public static String getHexStr(byte[] b) {
        String tempstr = "";
        String str = "";
        for (int i = 0; i < b.length; i++) {
            tempstr = Integer.toHexString(b[i] & 0xFF);
            if (tempstr.length() == 1) {
                str += "0" + tempstr;
            } else {
                str += tempstr;
            }
            if ((i + 1) % 16 == 0) {
                str += "\n";
            }
        }
        return str;
    }

    public static String encode(byte[] b) {
        if (b == null)
            return null;

        return new BASE64Encoder().encode(b);
    }

    public static byte[] decode(String str) {
        if (str == null)
            return null;

        try {
            return new BASE64Decoder().decodeBuffer(str);
        } catch (IOException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String keyStr = "4c1a53f46ebaeca9f3b7c6d5593e107c";
        String source = "192.168.16.337a/AJi+3FaL0quWC8r5fGg==";

        // String key = encodeHMAC(source, keyStr);
        byte[] b = encodeHMACByte(source, keyStr);
    }
}
