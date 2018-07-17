/**
 *Administrator
 */
package com.ft.otp.util.alg;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

import com.ft.otp.util.alg.pbkdf2.PBKDF2;
import com.ft.otp.util.alg.pbkdf2.PBKDF2Engine;
import com.ft.otp.util.alg.pbkdf2.PBKDF2Parameters;
import com.ft.otp.util.tool.StrTool;

/**
 * AES算法工具类
 *
 * @Date in Oct 24, 2012,4:42:24 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class AESUtil {

    private static Logger log = Logger.getLogger(AESUtil.class);
    // 算法/模式/补码方式
    private static String algorithm = "AES/CBC/";

    private static int PBKDF2COUNTER = 1000;
    private static final int AESKEYLENGTH = 16;

    /**
     * 解密令牌种子密钥数据的方法
     * 
     * 返回16进制字符串的格式
     * 
     * @Date in May 3, 2013,11:38:32 AM
     * @param data
     * @param sKey
     * @param fillType
     * @return
     */
    public static String pubkeyDecrypt(byte[] data, byte[] sKey, String fillType) {
        byte[] decPubkey = aesDecrypt(data, sKey, fillType);
        if (!StrTool.byteNotNull(decPubkey)) {
            return "";
        }

        return AlgConvert.bytes2HexString(decPubkey);
    }

    /**
     * 解密数据库连接密码等AES加密数据的方法
     * 
     * 返回明文字符串的格式
     * 
     * @Date in May 3, 2013,11:40:22 AM
     * @param data
     * @param sKey
     * @param fillType
     * @return
     */
    public static String dataDecrypt(byte[] data, byte[] sKey, String fillType) {
        byte[] decPubkey = aesDecrypt(data, sKey, fillType);
        if (!StrTool.byteNotNull(decPubkey)) {
            return "";
        }

        return new String(decPubkey);
    }

    /**
     * AES 解密密文数据通用方法
     * 
     * @Date in May 14, 2010,4:58:34 PM
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    private static byte[] aesDecrypt(byte[] data, byte[] sKey, String fillType) {
        try {
            byte[] ivs = new byte[16];
            for (int j = 0; j < ivs.length; j++) {
                ivs[j] = data[j];
            }

            byte[] dataBt = new byte[data.length - 16];
            int nm = 0;
            for (int k = 16; k < data.length; k++) {
                dataBt[nm] = data[k];
                nm++;
            }

            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance(algorithm + fillType);
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(dataBt);

            return original;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * AES128加密算法 
     * 
     * 加密数据库连接密码
     * 
     * @Date in May 14, 2010,6:18:18 PM
     * @param password
     * @param key
     * @param fillType
     * @return
     * @throws Exception
     */
    public static byte[] aes128Encrypt(String password, byte[] key, String fillType) throws Exception {
        byte[] ivs = randomIv(128, 16);

        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance(algorithm + fillType);
        IvParameterSpec iv = new IvParameterSpec(ivs);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] bData = password.getBytes();
        byte[] encrypted = cipher.doFinal(bData);

        byte[] encsrt = new byte[ivs.length + encrypted.length];
        for (int i = 0, j = 0; i < encsrt.length; i++) {
            if (i >= ivs.length) {
                encsrt[i] = encrypted[j];
                j++;
            } else {
                encsrt[i] = ivs[i];
            }
        }

        return encsrt;
    }

    /**
     * 随机生成加密向量 
     * 
     * AES128 16个字节
     * AES192 16个字节
     * AES256 16个字节
     * @return
     */
    public static byte[] randomIv(int median, int byteLen) throws Exception {
        byte[] ivs = new byte[byteLen];
        SecretKey key = null;
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        kgen.init(random);
        key = kgen.generateKey();
        ivs = key.getEncoded();

        return ivs;
    }

    /**  
     * 生成签名数据  
     *   
     * @param data 待加密的数据  
     * @param key  加密使用的key  
     * @return BASE64Encoder字符串   
     * @throws InvalidKeyException  
     * @throws NoSuchAlgorithmException  
     */
    public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);

        return Base64.encodeBytes(rawHmac);
    }

    /**
     * 3DES
     * 方法说明
     * @Date in Oct 25, 2012,10:09:15 AM
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    public static String DES3Descrypty(byte[] key, byte[] data) throws Exception {
        byte[] ivs = new byte[8];
        for (int j = 0; j < ivs.length; j++) {
            ivs[j] = data[j];
        }

        Cipher des = Cipher.getInstance("DESede/CBC/PKCS5Padding");

        SecretKeySpec keySpec = new SecretKeySpec(key, "DESede");// key
        IvParameterSpec ivSpec = new IvParameterSpec(ivs);// iv

        des.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] dataBt = new byte[data.length - 8];
        int nm = 0;
        for (int k = 8; k < data.length; k++) {
            dataBt[nm] = data[k];
            nm++;
        }

        byte[] decData = des.doFinal(dataBt);

        return AlgConvert.bytes2HexString(decData);
    }

    /**
     * DES算法
     * 
     * @Date in Feb 28, 2013,2:06:38 PM
     * @param key
     * @param iv
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] DESEncrypt(byte[] key, byte[] iv, byte[] data) throws Exception {
        //Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        //key
        SecretKeySpec keySpec = new SecretKeySpec(key, "DES");
        //iv
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] original = cipher.doFinal(data);

        return original;
    }

    /**
     * PBE解密
     * 
     * @Date in Oct 25, 2012,1:49:11 PM
     * @param key
     * @param data
     * @param filType
     * @return
     */
    public static byte[] PBEDescrypty(byte[] key, byte[] data, String filType) {
        // 解密种子
        byte deData[] = null;
        try {
            byte[] iv = new byte[16];
            byte[] enData = new byte[data.length - 16];

            System.arraycopy(data, 0, iv, 0, 16);
            System.arraycopy(data, 16, enData, 0, enData.length);

            SecretKeySpec skeySpec = new SecretKeySpec(key, "DES");
            try {
                Security.addProvider(new BouncyCastleProvider());
            } catch (Exception e) {
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/" + filType);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);

            deData = cipher.doFinal(enData);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return deData;
    }

    /**
     * 方法名称: getKey  PEB解密中调用
     * 说   明: 获取PBKDF2处理后的密钥
     * 参   数: 
     * @param   key         密钥
     * @param   data        数据
     * @param   icnt        迭代次数
     * @param   ilen        返回的key的长度
     * 返   回: PBKDF2 生成的密钥
     */
    public static byte[] getKey(byte[] key, byte[] data, int icnt, int ilen) {
        PBKDF2Parameters params = new PBKDF2Parameters("HMacSHA1", null, data, icnt);

        PBKDF2 pbkdf2 = new PBKDF2Engine(params);

        return pbkdf2.deriveKey(key, ilen);
    }

    /**
     * 加载私钥
     * 
     * @Date in Oct 25, 2012,2:26:16 PM
     * @param priKeyFile
     * @throws Exception
     */
    public static PrivateKey loadPriKey(File priKeyFile, String pwd) throws Exception {
        PrivateKey priKey = null;
        FileInputStream fis = new FileInputStream(priKeyFile);
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(fis, pwd.toCharArray());
        String keyAlias = null;
        Enumeration alias = ks.aliases();
        while (alias.hasMoreElements()) {
            String ka = (String) alias.nextElement();
            if (ks.isKeyEntry(ka)) {
                priKey = ((PrivateKey) ks.getKey(ka, pwd.toCharArray()));
                keyAlias = ka;
            }
        }
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException ex) {
                //注意：此处必须fis.close();要不然删除不掉.p12文件，照成下次上传时有问题
            }
        }

        return priKey;
    }

    /**     
     * 非对称解密RSA     
     * 用私钥解密  
     * 
     * @param data     
     * @param key     
     * @return     
     * @throws Exception     
     */
    public static String decryptByPrivateKey(byte[] data, byte[] privKey) throws Exception {
        // 取得私钥     
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密     
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return AlgConvert.bytes2HexString(cipher.doFinal(data));
    }

    /**
     * 随机生成加密密钥 
     * 
     * @Date in May 2, 2013,1:26:55 PM
     * @param keyLen 密钥的长度 单位：字节
     * @return
     */
    public static byte[] genRandomEncKey(int keyLen) {
        byte[] keys = new byte[keyLen];
        try {
            SecureRandom random = new SecureRandom();
            random.nextBytes(keys);
        } catch (Exception ex) {
        }

        return keys;
    }
    
    /**
     * PBE 解密，返回解密后种子文件
     * @Date in Nov 1, 2013,3:12:21 PM
     * @param fiiltype
     * @param sSrc
     * @param sKey
     * @return
     */
    public static String AESDecrypt(String fiiltype, String sSrc, byte[] sKey) {
        try {
            BASE64Decoder dec = new BASE64Decoder();
            byte[] ciphertext = dec.decodeBuffer(sSrc);
            byte[] ivs = new byte[16];
            for (int j = 0; j < ivs.length; j++) {
                ivs[j] = ciphertext[j];
            }

            byte[] dataBt = new byte[ciphertext.length - 16];
            int nm = 0;
            for (int k = 16; k < ciphertext.length; k++) {
                dataBt[nm] = ciphertext[k];
                nm++;
            }

            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/" + fiiltype);
            IvParameterSpec iv = new IvParameterSpec(ivs);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(dataBt);
            return AlgConvert.bytes2HexString(original);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex);
            return null;
        }
    }
}
