package com.ft.otp.manager.lic.decode;

import java.security.MessageDigest;
import org.apache.log4j.Logger;
import com.ft.otp.manager.lic.entity.License;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.alg.Base64;
import com.ft.otp.util.alg.DESUtil;
import com.ft.otp.util.alg.HMAC_MD5;
import com.ft.otp.util.alg.RSA;

/**
 * 解析FT License授权文件
 *
 * @Date in Feb 19, 2013,4:30:08 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LicenseDecode {
    
    private static Logger logger = Logger.getLogger(LicenseDecode.class);

    /**
     * 解析并取得授权内容具体的属性数据
     * 
     * @Date in Feb 19, 2013,5:39:22 PM
     * @param licInfo
     * @return
     */
    public static License getLicenseInfo(String licInfo) {
        byte[] licData = HMAC_MD5.decode(licInfo);
        if (null == licData) {
            return null;
        }

        return decodeLic(licData, licData.length);
    }

    /**
     * 解析授权内容的操作过程
     * 
     * @Date in Feb 19, 2013,5:38:59 PM
     * @param licData
     * @param length
     * @return
     */
    public static License decodeLic(byte[] licData, int length) {
        License license = null;
        try {
            //签名
            byte[] bSignData = new byte[128];
            //加密的DES随机密钥
            byte[] bEnDesData = new byte[128];
            //加密后的授权信息
            byte[] bDesLicData = new byte[256];
            //bDeDesData和bDeLicData的md5摘要值,用于跟RSA解密后的签名验证
            byte[] digest = null;
            //bDesKey和解密后的DES随机密钥的摘要值,用来解密授权信息
            byte[] bKey = null;
            //授权信息的有效长度
            int len = length - 128 - 128;

            byte[] bIssuer = new byte[32];
            int iIssuerLen = 0;
            byte[] bOwner = new byte[32];
            int iOwnerLen = 0;
            byte[] bDesc = new byte[64];
            int iDescLen = 0;
            byte[] bStart = new byte[4];
            byte[] bExpire = new byte[4];
            byte[] bTokenCount = new byte[4];
            byte[] bLicScale = new byte[4];
            byte[] bSrvNodes = new byte[4];

            //3.2加的字段
            byte[] bmbtkNum = new byte[4]; //手机令牌个数
            byte[] bsftkNum = new byte[4]; //软件令牌
            byte[] bcdtkNum = new byte[4]; //卡片令牌
            byte[] bsmstkNum = new byte[4]; //短信令牌

            if (len <= 0)
                return null;

            for (int i = 0; i < 128; i++) {
                bSignData[i] = licData[i];
                bEnDesData[i] = licData[i + 128];
            }
            for (int i = 0; i < len; i++) {
                bDesLicData[i] = licData[i + 128 + 128];
            }

            //计算加密的DES随机密钥和授权信息的摘要值
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bEnDesData);
            md.update(bDesLicData, 0, len);
            digest = md.digest();

            //RSA解密签名值
            byte[] bUnSignData = RSA.RSA_Decrypt(bSignData);
            //RSA解密失败
            if (null == bUnSignData) {
                return license;
            }

            //比对解密后的签名数据和摘要值是否一致
            if (!AlgHelper.bytesCompare(bUnSignData, digest)) {
                return license;
            }

            //RSA解密DES随机缓冲区
            byte[] bDesData = RSA.RSA_Decrypt(bEnDesData);
            if (null == bDesData) {
                return license;
            }

            // 计算加密的DES随机密钥和授权信息的摘要值
            md.update(RSA.bDesKey);
            md.update(bDesData);
            bKey = md.digest();

            // 准备DES解密的key和iv
            byte[] key = new byte[8];
            byte[] iv = new byte[8];
            for (int i = 0; i < 8; i++) {
                key[i] = bKey[i];
                iv[i] = bKey[i + 8];
            }

            //DES解密
            byte[] bLicData = DESUtil.DES_Decrypt(key, iv, bDesLicData, 0, len);
            if (null == bLicData) {
                return license;
            }

            for (int i = 0; i < 32; i++) {
                if (bLicData[i] == 0) {
                    iIssuerLen = i;
                    break;
                }
                iIssuerLen = i + 1;
                bIssuer[i] = bLicData[i];
            }
            for (int i = 0; i < 32; i++) {
                if (bLicData[i + 32] == 0) {
                    iOwnerLen = i;
                    break;
                }
                iOwnerLen = i + 1;
                bOwner[i] = bLicData[i + 32];
            }
            for (int i = 0; i < 64; i++) {
                if (bLicData[i + 64] == 0) {
                    iDescLen = i;
                    break;
                }
                iDescLen = i + 1;
                bDesc[i] = bLicData[i + 64];
            }
            for (int i = 0; i < 4; i++) {
                bStart[i] = bLicData[i + 128];
                bExpire[i] = bLicData[i + 132];
                //硬件令牌个数
                bTokenCount[i] = bLicData[i + 136];
                if (bLicData.length == 152 || bLicData.length == 168) {
                    bLicScale[i] = bLicData[i + 140];
                    bSrvNodes[i] = bLicData[i + 144];
                }
                if (bLicData.length == 168) {
                    bmbtkNum[i] = bLicData[i + 148]; //手机令牌个数
                    bsftkNum[i] = bLicData[i + 152]; //软件令牌
                    bcdtkNum[i] = bLicData[i + 156]; //卡片令牌
                    bsmstkNum[i] = bLicData[i + 160]; //短信令牌
                }
            }

            license = new License();
            license.setIssuer(new String(bIssuer, 0, iIssuerLen));
            license.setOwner(new String(bOwner, 0, iOwnerLen));
            license.setDescp((new String(bDesc, 0, iDescLen)));

            license.setStartTime(AlgHelper.bytesToInt(bStart));
            license.setExpireTime(AlgHelper.bytesToInt(bExpire));
            license.setTokenCount(AlgHelper.bytesToInt(bTokenCount));
            license.setLicType(AlgHelper.bytesToInt(bLicScale));
            license.setServerNodes(AlgHelper.bytesToInt(bSrvNodes));

            //3.2新扩展
            license.setMobileTokenNum(AlgHelper.bytesToInt(bmbtkNum));
            license.setSoftTokenNum(AlgHelper.bytesToInt(bsftkNum));
            license.setCardTokenNum(AlgHelper.bytesToInt(bcdtkNum));
            license.setSmsTokenNum(AlgHelper.bytesToInt(bsmstkNum));
            license.setLicId(Base64.encodeBytes(digest));
            license.setLicInfo(Base64.encodeBytes(licData));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return license;
    }

}
