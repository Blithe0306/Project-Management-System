/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.LicenseConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.lic.decode.LicenseDecode;
import com.ft.otp.manager.lic.decode.LicenseVerify;
import com.ft.otp.manager.lic.entity.LicInfo;
import com.ft.otp.manager.lic.entity.License;
import com.ft.otp.manager.lic.service.ILicInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 授权初始化加载
 *
 * @Date in Apr 18, 2011,4:24:51 PM
 *
 * @author TBM
 */
public class LicenseConfig {

    private Logger logger = Logger.getLogger(LicenseConfig.class);
    public final static String LICENCE_ENABLE = "licence_enable";

    private static Map<Object, Object> licMap;
    private static LicenseConfig config = null;
    private static int loadResult = -1;

    //授权信息服务接口
    private ILicInfoServ licInfoServ = null;

    private LicenseConfig() {
        licMap = new HashMap<Object, Object>();
        licInfoServ = (ILicInfoServ) AppContextMgr.getObject("licInfoServ");
        LicInfo licInfo = new LicInfo();
        licInfo.setLicstate(1);

        try {
            licInfo = (LicInfo) licInfoServ.find(licInfo);
            if (null == licInfo) {
                return; //未上传授权
            }

            License license = new License();
            license = LicenseDecode.getLicenseInfo(licInfo.getLicinfo());
            if (null == license) {
                loadResult = NumConstant.common_number_1; //授权解析失败
                return;
            }

            if (!LicenseVerify.verifyLicValidity(license)) {
                loadResult = NumConstant.common_number_2;//授权已经过期
            }

            licMap.put(LicenseConstant.OTP_SERVER_LIC_ISSUER, license.getIssuer());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_OWNER, license.getOwner());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_STARTTIME, license.getStartTime());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_EXPIRETIME, license.getExpireTime());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_TOKENCOUNT, license.getTokenCount());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_LICTYPE, license.getLicType());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_SERVERNODES, license.getServerNodes());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_MOBILETOKENNUM, license.getMobileTokenNum());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_SOFTTOKENNUM, license.getSoftTokenNum());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_CARDTOKENNUM, license.getCardTokenNum());
            licMap.put(LicenseConstant.OTP_SERVER_LIC_SMSTOKENNUM, license.getSmsTokenNum());

            loadResult = 0;
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static LicenseConfig loadLicenceInfo() {
        if (config != null) {
            return config;
        }

        synchronized (LicenseConfig.class) {
            if (config == null) {
                config = new LicenseConfig();
            }
            return config;
        }
    }

    /**
     * 重新加载授权
     * 
     * @Date in Mar 28, 2013,5:26:59 PM
     */
    public static void reloadLicenceInfo() {
        config = new LicenseConfig();
    }

    /**
     * 根据key取得Value
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        if (!StrTool.mapNotNull(licMap)) {
            return "";
        }

        return licMap.get(key);
    }

    /**
     * 加载结果
     * 
     * @Date in Mar 29, 2013,11:28:52 AM
     * @return
     */
    public static int getLoadResult() {
        return loadResult;
    }

    /**
     * 清空Map，重置config
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void clear() {
        if (null != config) {
            licMap.clear();
            loadResult = -1;
            config = null;
        }
    }

    /**
     * 重新加载授权配置
     * 
     * @Date in May 7, 2013,3:19:59 PM
     */
    public static void reLoad() {
        clear();
        loadLicenceInfo();

        //通知服务器加载授权
        SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10001);
    }

}
