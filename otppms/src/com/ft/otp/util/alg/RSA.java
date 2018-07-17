/**
 *Administrator
 */
package com.ft.otp.util.alg;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

 

/**
 * 类功能说明
 *
 * @Date in May 30, 2011,5:41:48 PM
 *
 * @author ZJY
 */
public class RSA {
    private static final String strModules =    "d9e41abf8240dd047f400dab42f036b3" +
    "a18ffb964fc56ff7d06b3e75a78a3209" + 
    "b13b85e60b5e328ecf8335c6ff3b903f" +
    "c2351ffec710660a0089b2cd4f7477c6" +
    "7c9f5e9c1ef8a81f0ecc61b4c16c3120" +
    "154ea743338d4c3c1d9fd63e5da0319b" +
    "13d04b26cbdb2af204d6c709e17eeb03" +
    "dedb195796235376a0b794c84f4c9ec3";
    private static final String strPublicExponent = "010001";
    
    public static final byte[] bDesKey = new byte[]{0xc4-256,0xa1-256,0x23,0xf4-256,
        0x6f,0xab-256,0xec-256,0xa9-256,
        0x3f,0xb9-256,0x8c-256,0xdd-256,
        0x56,0xde-256,0x10,0xc4-256};

    public static byte[] RSA_Decrypt(byte[] bEncData)
    {
        try 
        {
            BigInteger N = new BigInteger (strModules, 16);
            BigInteger E = new BigInteger (strPublicExponent, 16);
            
            RSAPublicKeySpec bobPubKeySpec = new RSAPublicKeySpec(N, E);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey bobPubKey = keyFactory.generatePublic(bobPubKeySpec);
            
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",new BouncyCastleProvider());   
            cipher.init(Cipher.DECRYPT_MODE, bobPubKey); 
            
            return cipher.doFinal(bEncData);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return null;
    }
    
}
