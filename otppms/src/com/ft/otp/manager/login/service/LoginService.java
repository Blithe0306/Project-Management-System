/**
 *Administrator
 */
package com.ft.otp.manager.login.service;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员业务处理服务
 *
 * @Date in Apr 1, 2013,5:13:37 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);
    // 管理员服务接口
    private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");

    /**
     * 更新管理员认证错误临时和永久锁定的次数
     * 
     * @Date in Jan 29, 2013,2:42:07 PM
     * @param adminUser
     * @return
     */
    public void updatePwdErrCnt(AdminUser adminUser) {
        //增加用户认证错误次数或锁定用户
        int tempLoginErrCnt = adminUser.getTemploginerrcnt();
        int longLoginErrCnt = adminUser.getLongloginerrcnt();

        int confTempLockErrCnt = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_ERROR_RETRY_TEMP));
        int confLongLockErrCnt = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_ERROR_RETRY_LONG));

        if (tempLoginErrCnt < confTempLockErrCnt) {
            tempLoginErrCnt += 1;
        }
        if (longLoginErrCnt < confLongLockErrCnt) {
            longLoginErrCnt += 1;
        }
        //设置锁定状态和锁定时间
        if (confTempLockErrCnt == tempLoginErrCnt || longLoginErrCnt == confLongLockErrCnt) {
            if (confTempLockErrCnt == tempLoginErrCnt) {
                adminUser.setLocked(NumConstant.common_number_1);
            }
            if (longLoginErrCnt == confLongLockErrCnt) {
                adminUser.setLocked(NumConstant.common_number_2);
            }
            adminUser.setLoginlocktime(StrTool.timeSecond());
        }

        //设置用户口令认证错误次数(临时、永久)
        adminUser.setTemploginerrcnt(tempLoginErrCnt);
        adminUser.setLongloginerrcnt(longLoginErrCnt);

        //如果配置中等于临界值 说明不需要锁定
        if (confTempLockErrCnt != NumConstant.TEMP_LONG_LOCKED_NUM_65535
                || confLongLockErrCnt != NumConstant.TEMP_LONG_LOCKED_NUM_65535) {
            adminUser.setIsRecordLog(StrConstant.common_number_0);
            updateAdminUser(adminUser);
        }
    }

    /**
     * 获取用户临时锁定错误重试剩余次数
     * 
     * @Date in Feb 1, 2013,8:46:01 PM
     * @param tempCnt
     * @return
     */
    public int getRetryCnt(int tempCnt, int longCnt) {
        int confTempLockErrCnt = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_ERROR_RETRY_TEMP));
        int confLongLockErrCnt = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_ERROR_RETRY_LONG));

        //已临时锁定
        if (tempCnt >= confTempLockErrCnt) {
            //已永久锁定
            if (longCnt >= confLongLockErrCnt) {
                return NumConstant.common_number_na_1;
            }
            return NumConstant.common_number_0;
        }

        return confTempLockErrCnt - tempCnt;
    }

    /**
     * 认证成功后，如果用户临时或永久锁定次数大于0，清0
     * 
     * @Date in Feb 1, 2013,8:30:29 PM
     * @param adminUser
     * @return
     */
    public void unLockedUser(AdminUser adminUser) {
        int tempLoginErrCnt = adminUser.getTemploginerrcnt();
        int longLoginErrCnt = adminUser.getLongloginerrcnt();
        if (tempLoginErrCnt > 0 || longLoginErrCnt > 0) {
            adminUser.setTemploginerrcnt(0);
            adminUser.setLongloginerrcnt(0);
            adminUser.setIsRecordLog(StrConstant.common_number_0);
            updateAdminUser(adminUser);
        }
    }

    /**
     * 如果用户为临时锁定状态，已经达到临时锁定解锁周期，进行自动解锁
     * 
     * @Date in Jan 30, 2013,1:53:26 PM
     * @return
     */
    public int ifUnLockUser(AdminUser adminUser) {
        int currentSecond = StrTool.timeSecond();
        int lockTime = adminUser.getLoginlocktime(); //锁定时间
        int unLockCycle = StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,
                ConfConstant.LOGIN_LOCK_EXPIRE));// 表里面存的是 秒，页面上配的是分钟
        unLockCycle = unLockCycle + lockTime;

        //如果用户为临时锁定状态，已经达到临时锁定解锁周期，进行自动解锁
        if (unLockCycle < currentSecond) {
            adminUser.setLocked(0);
            adminUser.setTemploginerrcnt(0);
            adminUser.setLoginlocktime(0);
            if (updateAdminUser(adminUser)) {
                return NumConstant.common_number_0;
            }
        }

        return NumConstant.common_number_na_1;
    }

    /**
     * 更新管理员状态
     * 
     * @Date in Apr 1, 2013,5:43:27 PM
     * @param adminUser
     * @return
     */
    private boolean updateAdminUser(AdminUser adminUser) {
        try {
            adminUserServ.updateLocked(adminUser);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

}
