/**
 *Administrator
 */
package com.ft.otp.manager.lic.decode;

import org.apache.log4j.Logger;

import com.ft.otp.common.LicenseConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.manager.lic.entity.LicInfo;
import com.ft.otp.manager.lic.entity.License;

/**
 * 授权有效性验证
 *
 * @Date in Mar 29, 2013,5:58:11 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LicenseVerify {

    private static Logger logger = Logger.getLogger(LicenseVerify.class);
    //认证服务器
    private static IServerServ serverServ = (IServerServ) AppContextMgr.getObject("serverServ");

    /**
     * 验证授权是否在有效期内
     * 
     * @Date in Feb 20, 2013,2:44:19 PM
     * @param license
     * @return
     */
    public static boolean verifyLicValidity(License license) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (license.getStartTime() < currentTime && license.getExpireTime() > currentTime) {
            return true;
        }

        return false;
    }

    /**
     * 验证授权类型和授权状态是否正常
     * 
     * @Date in Feb 20, 2013,2:44:12 PM
     * @param licInfo
     * @return
     */
    public static boolean verifyTypeAndState(LicInfo licInfo) {
        int licType = licInfo.getLictype();
        int licState = licInfo.getLicstate();
        if (licState == NumConstant.common_number_0) {
            return false;
        }

        int servCount = 0;
        try {
            servCount = serverServ.count(new ServerInfo());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        if (licType == LicenseConstant.LICENSE_TYPE_EVALUATION) {
            if (servCount <= NumConstant.common_number_1) {
                return true;
            }
        } else if (licType == LicenseConstant.LICENSE_TYPE_ENTERPRISE) {
            if (servCount <= NumConstant.common_number_2) {
                return true;
            }
        } else if (licType == LicenseConstant.LICENSE_TYPE_PREMIUM) {
            if (servCount <= Integer.MAX_VALUE) {
                return true;
            }
        }

        return false;
    }

}
