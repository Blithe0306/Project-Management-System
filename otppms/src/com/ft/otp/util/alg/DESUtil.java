package com.ft.otp.util.alg;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 3DES 算法
 *
 * @Date in Apr 18, 2011,10:20:28 AM
 *
 * @author TBM
 */
public class DESUtil {

    private static Logger logger = Logger.getLogger(DESUtil.class);
    static Cipher cipher = null;
    static KeyGenerator kg = null;
    static SecretKey key = null;

    static {
        try {
            kg = KeyGenerator.getInstance("DESede");
            key = kg.generateKey();
            cipher = Cipher.getInstance("DESede");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String encrypt(String original) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] data = original.getBytes();
        byte[] result = null;
        result = cipher.doFinal(data);

        return new String(result, "8859_1");
    }

    public static String decrypt(String result) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] original = null;
        original = cipher.doFinal(result.getBytes("8859_1"));

        return new String(original);
    }

    public static String encryptByKey(String original, byte[] key) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");//key
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] data = original.getBytes();
        byte[] result = null;

        result = cipher.doFinal(data);

        return new String(result, "8859_1");
    }

    public static String decryptByKey(String result, byte[] key) throws Exception {
        if (!StrTool.strNotNull(result)) {
            return "";
        }

        SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");//key
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] original = null;
        byte[] resultb = result.getBytes("8859_1");
        original = cipher.doFinal(resultb);

        return new String(original);
    }

    public static String decryptByteByKey(byte[] result, byte[] key) throws Exception {
        // Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
        // Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

        byte[] sk = getFromBytes(key, 0, 7, 8);
        byte[] iv = getFromBytes(key, 8, 15, 8);

        //key
        SecretKeySpec keySpec = new SecretKeySpec(sk, "DES");
        //iv
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] original = null;
        original = cipher.doFinal(result);

        return new String(original);
    }

    private static byte[] getFromBytes(byte[] bs, int s, int e, int l) {
        if (bs == null || bs.length < 1) {
            return null;
        }
        byte[] b1 = new byte[l];
        try {
            int p = 0;
            for (int i = s; i <= e; i++) {
                b1[p] = bs[i];
                p++;
            }
        } catch (Exception ex) {
        }
        return b1;
    }

    public static byte[] DES_Decrypt(byte[] key, byte[] iv, byte[] bDesData, int offset, int len) {
        try {
            Cipher des = Cipher.getInstance("DES/CBC/NoPadding");

            SecretKeySpec keySpec = new SecretKeySpec(key, "DES");//key
            IvParameterSpec ivSpec = new IvParameterSpec(iv);//iv

            des.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            return des.doFinal(bDesData, offset, len);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * 3DES解密
     * @Date in Nov 1, 2013,3:31:15 PM
     * @param fiiltype
     * @param key
     * @param data
     * @return
     */
    public static String DES3Decrypt(String fiiltype ,byte[] key, String data) {
        String output = null;
        try {
            BASE64Decoder dec = new BASE64Decoder();
            byte[] ciphertext = dec.decodeBuffer(data);

            byte[] ivs = new byte[8];
            for (int j = 0; j < ivs.length; j++) {
                ivs[j] = ciphertext[j];
            }

            byte[] dataBt = new byte[ciphertext.length - 8];
            int nm = 0;
            for (int k = 8; k < ciphertext.length; k++) {
                dataBt[nm] = ciphertext[k];
                nm++;
            }
            
            String algorithm = "DESede/CBC/" + fiiltype;
            Cipher cipher = Cipher.getInstance(algorithm);
            DESedeKeySpec dks = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);
            IvParameterSpec ips = new IvParameterSpec(ivs);

            cipher.init(Cipher.DECRYPT_MODE, securekey, ips);

            byte[] decryptedText = cipher.doFinal(dataBt);

            output = AlgConvert.bytes2HexString(decryptedText);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    public static void main(String[] args) throws Exception {
        String test = "admin";
        byte[] b = "yoxixinchungedeyongsheng".getBytes();
        String result = encryptByKey(test, b);
        System.out.println(result);
        String dd = AlgConvert.stringToHex(result);
        System.out.println(dd);

        String original = decryptByKey(result, b);
        System.out.println(original);

    }

}