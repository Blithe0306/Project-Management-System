/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.sms.dao;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 短信网关配置
 *
 * @Date in Nov 21, 2012,3:48:13 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface ISmsInfoDao extends IBaseDao {

    /**
     * 启用、禁用
     */
    void updateEnabled(Object object) throws BaseException;
}
