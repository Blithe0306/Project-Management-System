package com.ft.otp.util.alg;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Encoder;

import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;

/**
 * 证书(cer、pem、p12、jdk等)相关的密钥处理操作公共类
 *
 * @Date in Aug 10, 2012,11:24:01 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class CertKeyUtil {

    private static Logger logger = Logger.getLogger(CertKeyUtil.class);

    /**
     * 得到AES加密的编码格式
     * 
     * @Date in Jul 24, 2012,11:05:55 AM
     * @param password
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] genEncodeFormat(String password) throws IOException, NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        kgen.init(128, secureRandom);
        SecretKey secretKey = kgen.generateKey();

        return secretKey.getEncoded();
    }

    /**
     * AES加密  
     * @param content 需要加密的内容  
     * @param encodeFormat 密钥编码   
     * @return
     * @Date:2011-9-16 下午03:32:18
     * @Author:lukaifu  
     */
    public static byte[] encrypt(String content, byte[] encodeFormat) {
        try {
            SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);

            return result; //加密
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 解密  
     * @param content  待解密内容  
     * @param encodeFormat 密钥编码 
     * @return
     * @Date:2011-9-16 下午03:32:18
     * @Author:lukaifu  
     */
    public static byte[] decrypt(byte[] content, byte[] encodeFormat) {
        try {
            SecretKeySpec key = new SecretKeySpec(encodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 读取证书文件中的数据
     * 
     * @Date in Aug 9, 2012,1:59:02 PM
     * @param upFile
     * @return
     */
    public static String getFileContent(File upFile, String fileSubStr) {
        try {
            InputStream inputStream = new FileInputStream(upFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            Object localObject1;
            if (fileSubStr == null || fileSubStr.endsWith(Constant.FILE_BIN) || fileSubStr.endsWith(Constant.FILE_P12)) {
                long len = upFile.length();
                byte[] cerbyte = new byte[(int) len];
                int r = inputStream.read(cerbyte);
                if (r != len) {
                    return "";
                }

                inputStream.close();
                return new BASE64Encoder().encode(cerbyte);
            } else if ((localObject1 = new CertKeyUtil().readObject(br, upFile)) != null) {
                return (String) localObject1;
            }

            return (String) localObject1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public Object readObject(BufferedReader reader, File file) throws IOException {
        String str;
        do {
            if ((str = reader.readLine()) == null)
                break;
            if (str.indexOf("-----BEGIN CERTIFICATE") != -1)
                return readCertificate("-----END CERTIFICATE", reader);
            if (str.indexOf("-----BEGIN RSA PRIVATE KEY") != -1)
                return readCertificate("-----BEGIN RSA PRIVATE KEY", reader);
            if (str.indexOf("-") == -1) {
                InputStream inputStream = new FileInputStream(file);
                long len = file.length();
                byte[] cerbyte = new byte[(int) len];
                int r = inputStream.read(cerbyte);
                if (r != len) {
                    return "";
                }

                inputStream.close();
                return new BASE64Encoder().encode(cerbyte);
            }
        } while (str.indexOf("-----END RSA PRIVATE KEY") == -1);

        return str;
    }

    /**
     * 获取证书内容数据
     * 
     * @Date in Sep 12, 2012,3:42:28 PM
     * @param paramString
     * @param reader
     * @return
     * @throws IOException
     */
    private Object readCertificate(String paramString, BufferedReader reader) throws IOException {
        StringBuffer localStringBuffer = new StringBuffer();
        String str;
        while ((str = reader.readLine()) != null) {
            if (str.indexOf(paramString) != -1)
                break;
            localStringBuffer.append(str.trim());
        }

        return localStringBuffer.toString();
    }

    /**
     * Fortinet加密时做MD5值
     * 
     * @Date in Aug 9, 2012,1:34:46 PM
     * @param originalKey
     * @return
     */
    public static byte[] getKey(String originalKey) {
        try {
            // 第一次md5结果
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(originalKey.getBytes());
            byte[] key1 = md5.digest();

            // 第二次md5结果
            md5.reset();
            md5.update(key1);
            md5.update(originalKey.getBytes());
            byte[] key2 = md5.digest();

            // 真正的密钥：第一次md5的结果+第二次md5结果
            byte[] key = new byte[key1.length + key2.length];
            System.arraycopy(key1, 0, key, 0, key1.length);
            System.arraycopy(key2, 0, key, key1.length, key2.length);

            return key;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 种子原始明文做HMAC_SHA1后（HMAC的密钥同加密密钥），再做BASE64编码
     * 方法说明
     * @Date in Dec 28, 2011,1:46:20 PM
     * @param aesKey
     * @return
     */
    public static String getDigest(byte[] seed) {
        // 得到Sha1(key)
        Digest sha1 = new SHA1Digest();
        byte[] digest = new byte[sha1.getDigestSize()];
        sha1.update(seed, 0, seed.length);
        sha1.doFinal(digest, 0);

        return new BASE64Encoder().encode(digest);
    }

    /**
     * 获取CER证书公钥数据
     * 
     * @Date in Aug 9, 2012,1:35:35 PM
     * @param data
     * @return
     */
    public static byte[] getCerPublicKey(byte[] data) {
        byte keydata[] = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert1 = (X509Certificate) cf.generateCertificate(inputStream);
            PublicKey publicKey = cert1.getPublicKey();
            keydata = publicKey.getEncoded();
        } catch (Exception e) {
            return null;
        }

        return keydata;
    }

    /**
     * 读取.PEM文件公钥或私钥
     * 
     * @Date in Aug 9, 2012,1:35:59 PM
     * @param bdata
     * @param type
     * @return
     */
    public static Object getPemKey(byte bdata[], int type) {
        byte[] cerbyte = null;
        try {
            // 获取公私钥数组
            if (type == NumConstant.common_number_1 || type == NumConstant.common_number_4) {//公钥
                PublicKey publicKey = readCertificate(bdata);
                cerbyte = publicKey.getEncoded();
            } else if (type == NumConstant.common_number_2 || type == NumConstant.common_number_5) {//私钥
                PrivateKey privateKey = readKeyPair("RSA", bdata);
                cerbyte = privateKey.getEncoded();
            }

        } catch (Exception e) {
            return false;
        }

        return cerbyte;
    }

    /**
     * 读取PEM格式公钥
     * 
     * @Date in Aug 10, 2012,11:22:57 AM
     * @param data
     * @return
     * @throws IOException
     */
    public static PublicKey readCertificate(byte data[]) throws IOException {
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(data);
        try {
            CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509",
                    new BouncyCastleProvider());
            X509Certificate certificate = (X509Certificate) localCertificateFactory
                    .generateCertificate(localByteArrayInputStream);

            return certificate.getPublicKey();
        } catch (Exception localException) {
            throw new IOException("problem parsing cert: " + localException.toString());
        }
    }

    /**
     * 读取PEM格式私钥
     * 
     * @Date in Aug 10, 2012,11:22:38 AM
     * @param paramString1
     * @param arrayOfByte
     * @return
     * @throws Exception
     */
    public static PrivateKey readKeyPair(String paramString1, byte[] arrayOfByte) throws Exception {
        Object localObject1;
        Object localObject2;
        Object localObject3;
        Object localObject4;
        Object localObject5;
        Object localObject6;

        localObject3 = new ByteArrayInputStream(arrayOfByte);
        localObject4 = new ASN1InputStream((InputStream) localObject3);
        localObject5 = (ASN1Sequence) ((ASN1InputStream) localObject4).readObject();
        DERInteger localDERInteger1;
        DERInteger localDERInteger2;
        DERInteger localDERInteger3;
        DERInteger localDERInteger4;
        DERInteger localDERInteger5;
        if (paramString1.equals("RSA")) {
            localObject6 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(0);
            localDERInteger1 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(1);
            localDERInteger2 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(2);
            localDERInteger3 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(3);
            localDERInteger4 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(4);
            localDERInteger5 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(5);
            DERInteger localDERInteger6 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(6);
            DERInteger localDERInteger7 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(7);
            DERInteger localDERInteger8 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(8);
            localObject1 = new RSAPublicKeySpec(localDERInteger1.getValue(), localDERInteger2.getValue());
            localObject2 = new RSAPrivateCrtKeySpec(localDERInteger1.getValue(), localDERInteger2.getValue(),
                    localDERInteger3.getValue(), localDERInteger4.getValue(), localDERInteger5.getValue(),
                    localDERInteger6.getValue(), localDERInteger7.getValue(), localDERInteger8.getValue());
        } else {
            localObject6 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(0);
            localDERInteger1 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(1);
            localDERInteger2 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(2);
            localDERInteger3 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(3);
            localDERInteger4 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(4);
            localDERInteger5 = (DERInteger) ((ASN1Sequence) localObject5).getObjectAt(5);
            localObject2 = new DSAPrivateKeySpec(localDERInteger5.getValue(), localDERInteger1.getValue(),
                    localDERInteger2.getValue(), localDERInteger3.getValue());
            localObject1 = new DSAPublicKeySpec(localDERInteger4.getValue(), localDERInteger1.getValue(),
                    localDERInteger2.getValue(), localDERInteger3.getValue());
        }
        localObject6 = KeyFactory.getInstance(paramString1, new BouncyCastleProvider());
        KeyPair keyPair = ((KeyPair) (KeyPair) (KeyPair) (KeyPair) (KeyPair) (KeyPair) new KeyPair(
                ((KeyFactory) localObject6).generatePublic((KeySpec) localObject1), ((KeyFactory) localObject6)
                        .generatePrivate((KeySpec) localObject2)));

        return keyPair.getPrivate();
    }

    /**
     * 获取p12私钥
     * 
     * @Date in Aug 9, 2012,1:36:29 PM
     * @param data
     * @param password
     * @return
     */
    public static Object getP12PrivKey(byte[] data, String password) {
        try {
            InputStream inputStream = new ByteArrayInputStream(data);
            PrivateKey privateKey = null;
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(inputStream, password.toCharArray());
            Enumeration<?> alias = ks.aliases();
            while (alias.hasMoreElements()) {
                String ka = (String) alias.nextElement();
                if (ks.isKeyEntry(ka)) {
                    privateKey = ((PrivateKey) ks.getKey(ka, password.toCharArray()));
                }
            }

            return privateKey.getEncoded();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取JKS私钥
     * 
     * @Date in Aug 9, 2012,1:36:37 PM
     * @param data
     * @param password
     * @return
     */
    public static Object getJksPrivKey(byte data[], String password) {
        PrivateKey priKey = null;
        try {
            InputStream fis = new ByteArrayInputStream(data);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(fis, password.toCharArray());
            String keyAlias = null;
            Enumeration alias = ks.aliases();
            while (alias.hasMoreElements()) {
                String ka = (String) alias.nextElement();
                if (ks.isKeyEntry(ka)) {
                    priKey = ((PrivateKey) ks.getKey(ka, password.toCharArray()));
                    keyAlias = ka;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return priKey.getEncoded();
    }

    public static void main(String[] args) throws IOException {
        String pubkey = getFileContent(new File("d:\\feitian.cer"), "fs");
        char data[] = pubkey.toCharArray();
        String pubkeyStr = "";
        for (int i = 0; i < data.length; i++) {
            if ((i + 1) % 76 == 0) {
                pubkeyStr += data[i] + "\r\n";
            } else {
                pubkeyStr += data[i];
            }
        }
        //byte priv[] = getFileContent(upFile, fileSubStr);
        System.out.println(pubkey);
    }

}
