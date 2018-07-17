/**
 *Administrator
 */
package com.ft.otp.util.tool;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.util.alg.AESUtil;
import com.ft.otp.util.alg.AlgConvert;
import com.ft.otp.util.alg.Base64;
import com.ft.otp.util.alg.DESUtil;
import com.ft.otp.util.alg.dist.RC4Util;
import com.ft.otp.util.conf.ConfDataFormat;

/**
 * 对管理员密码加密、解密处理类、接口等说明
 *
 * @Date in Feb 16, 2012,10:38:58 AM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class PwdEncTool {

    /**
     * 密码加密公共方法
     * 
     * @Date in Apr 5, 2013,10:26:55 AM
     * @param passwd
     * @return
     */
    public static String encPwd(String passwd) {
        if (passwd == null || passwd.equals("")) {//密码为空，取默认的密码 此处是null或"" 不能trim后""
            passwd = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER, ConfConstant.DEFAULT_USER_PWD);
        }
        return PwdEncTool.encPwdStr(passwd);
    }

    /**
     * 管理员、用户密码加密
     * 
     * @Date in Feb 16, 2012,11:12:10 AM
     * @return
     */
    private static String encPwdStr(String pwdStr) {
        String encStr = "";
        if (pwdStr != null && !pwdStr.equals("")) {
            try {
                encStr = RC4Util.runRC4(pwdStr);
                encStr = AlgConvert.stringToHex(encStr);
            } catch (Exception ex) {
                encStr = pwdStr;
            }
        }

        return encStr;
    }

    /**
     * 对数字型的密码进行解密，比如：A77F70CD，解密为:1111
     * 方法说明
     * @Date in Feb 16, 2012,11:12:20 AM
     * @param pwdStr
     * @return
     * @throws BaseException
     */
    public static String descryPwdStr(String pwdStr) {
        String newpwdStr = "";
        if (StrTool.strNotNull(pwdStr)) {
            newpwdStr = AlgConvert.hexToString(pwdStr);
            try {
                newpwdStr = RC4Util.runRC4(newpwdStr);
            } catch (Exception ex) {
                newpwdStr = pwdStr;
            }
        }

        return newpwdStr;
    }

    /**
     * 加密帐户密码
     * @Date in Jul 15, 2011,5:21:52 PM
     * @param pwdStr
     * @return
     */
    public static String desEncPwd(String pwdStr) {
        String encStr = "";
        if (StrTool.strNotNull(pwdStr)) {
            byte[] keyByte = Constant.DES_KEY.getBytes();
            String result = null;
            try {
                result = DESUtil.encryptByKey(pwdStr, keyByte);
                encStr = AlgConvert.stringToHex(result);
            } catch (Exception ex) {
                encStr = pwdStr;
            }
        }
        return encStr;
    }

    /**
     * 解密管理员帐号密码
     * @Date in Jul 16, 2011,6:14:31 PM
     * @param pwdStr
     * @return
     */
    public static String desDecPwd(String pwdStr) {
        String decStr = "";
        if (StrTool.strNotNull(pwdStr)) {
            byte[] keyByte = Constant.DES_KEY.getBytes();
            String result = null;
            try {
                result = AlgConvert.hexToString(pwdStr);
                decStr = DESUtil.decryptByKey(result, keyByte);
            } catch (Exception ex) {
                decStr = pwdStr;
            }
        }
        return decStr;
    }

    /**
     * 数据库连接密码加密方法
     * 
     * @Date in May 3, 2013,11:09:20 AM
     * @param password
     * @return
     */
    public static String encDbPasswd(String password, byte[] key) {
        byte[] bPwd = null;
        try {
            bPwd = AESUtil.aes128Encrypt(password, key, StrConstant.FILTYPE_PKCS5PADDING);
        } catch (Exception ex) {
            return null;
        }

        return Base64.encodeBytes(bPwd);
    }

    /**
     * 数据库连接密码解密方法
     * 
     * @Date in May 3, 2013,11:20:24 AM
     * @param password
     * @return
     */
    public static String decDbPasswd(String password, byte[] key) {
        try {
            byte[] bPwd = Base64.decode(password);
            password = AESUtil.dataDecrypt(bPwd, key, StrConstant.FILTYPE_PKCS5PADDING);
        } catch (Exception ex) {
            return null;
        }

        return password;
    }

    public static void main(String[] args) {
        String password = "123456";
        password = encDbPasswd(password, null);
        System.out.println(password);
        System.out.println(decDbPasswd(password, null));
    }

}
