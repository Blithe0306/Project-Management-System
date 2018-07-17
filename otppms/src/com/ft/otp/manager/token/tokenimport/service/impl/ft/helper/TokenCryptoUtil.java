package com.ft.otp.manager.token.tokenimport.service.impl.ft.helper;

import java.io.IOException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.alg.Base64;
import com.ft.otp.util.alg.base.checksum.CRC16;
import com.ft.otp.util.alg.base.cipher.RC4Engine;
import com.ft.otp.util.alg.base.digest.IDigest;
import com.ft.otp.util.alg.base.digest.MD5;

/**
 * 令牌种子私有数据加解密工具类
 *
 * @Date in Mar 18, 2013,9:30:18 PM
 *
 * @version v1.0
 *
 * @author KK David
 */
public class TokenCryptoUtil {

    public static byte[] aesKey = null;
    private static String customerid = "";

    private static final String RC4PREFIX = "kk"; // RC4加密方式的前缀（原有产品已有此处理方式，为了兼容，在解密时仍然支持）
    private static final int RC4LENGTH = 42; // RC4加密种子的长度
    private static final String AES128PREFIX = "mm"; // AES128加密方式的前缀

    private static byte[] aesCrypto(byte[] key, byte[] iv, byte[] input, int inputLen, int mode) {
        if (key == null || iv == null || input == null) {
            return null;
        }

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivs = new IvParameterSpec(iv);
            cipher.init(mode, keySpec, ivs);

            if (mode == Cipher.ENCRYPT_MODE) { // 如果是加密则提前把CRC16校验和的两字节空间分配好
                byte[] output = new byte[cipher.getOutputSize(inputLen) + 2];
                cipher.doFinal(input, 0, inputLen, output, 0);
                return output;
            } else {
                return cipher.doFinal(input, 0, inputLen);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static String encryptPubkey(String token, byte[] pubkey) {
        // 加密
        IDigest md5 = new MD5();
        md5.update(token.getBytes());
        byte[] enPubkey = aesCrypto(aesKey, md5.digest(), pubkey, pubkey.length, Cipher.ENCRYPT_MODE);
        if (enPubkey == null) { // 加密失败也存十六进制格式
            return AlgHelper.bytesToHexs(pubkey);
        }
        // 校验和
        CRC16 crc16 = new CRC16();
        short crc = 0;
        crc = crc16.getCRC16(crc, token.getBytes(), 0, token.length());
        crc = crc16.getCRC16(crc, pubkey, 0, pubkey.length);

        AlgHelper.shortToBigEndian(crc, enPubkey, enPubkey.length - 2);

        return AES128PREFIX + Base64.encodeBytes(enPubkey);
    }

    public static byte[] decryptPubkey(String token, String pubkey) {
        if (pubkey.length() == RC4LENGTH && pubkey.substring(0, 2).equals(RC4PREFIX)) { // RC4
            // 先得到密钥
            IDigest md5 = new MD5();
            md5.update(customerid.getBytes());
            byte[] rc4Key = md5.digest();

            // 得到加密后的种子数据
            byte[] data = AlgHelper.hexStringToBytes(pubkey.substring(2));
            byte[] bKey = new byte[data.length];

            RC4Engine rc4 = new RC4Engine();
            rc4.init(rc4Key);
            rc4.processBytes(data, 0, data.length, bKey, 0);

            return bKey;
        } else if (pubkey.substring(0, 2).equals(AES128PREFIX) && pubkey.length() % 4 == 2) { // AES128
            IDigest md5 = new MD5();
            md5.update(token.getBytes());
            byte[] iv = md5.digest();
            byte[] data = null;
            try {
                data = Base64.decode(pubkey.getBytes(), 2, pubkey.length() - 2);
            } catch (IOException ex) {
                return null;
            }

            // 解密
            byte[] bKey = aesCrypto(aesKey, iv, data, data.length - 2, Cipher.DECRYPT_MODE);
            if (bKey == null) {
                return null;
            }

            // 检查校验和
            CRC16 crc16 = new CRC16();
            short crc = 0;
            crc = crc16.getCRC16(crc, token.getBytes(), 0, token.length());
            crc = crc16.getCRC16(crc, bKey, 0, bKey.length);

            short oldcrc = AlgHelper.bigEndianToShort(data, data.length - 2);
            if (crc != oldcrc) {
                return null;
            }

            return bKey;
        } else { // 认为是未加密的十六进制
            return AlgHelper.hexStringToBytes(pubkey);
        }
    }

    public static void init(byte[] key, String strCustomerId, boolean isNeedEncrypt) {
        if (key != null) {
            aesKey = new byte[key.length];
            System.arraycopy(key, 0, aesKey, 0, key.length);
        } else {
            aesKey = null;
        }

        customerid = strCustomerId;
    }

    public static void main(String[] args) {
        aesKey = AlgHelper.hexStringToBytes("465F2677CBC7321249AD6654818B3D5A");
        byte[] pubkey = AlgHelper.hexStringToBytes("69A1BCB1E39F87BA751B97012E259905E84AF9D5");
        String token = "4210077199200";

        String enKey = encryptPubkey(token, pubkey);
        System.out.println(enKey);
        enKey = "mmTCS3K2NRdBUbdZ4n0a25NfKteAU+gOAG1SB1O4yiVagGZw==";

        byte[] deKey = decryptPubkey(token, enKey);
        System.out.println(AlgHelper.bytesToHexs(deKey));
    }

}
