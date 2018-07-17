/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.sms.service;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 短信网关配置
 *
 * @Date in Nov 21, 2012,3:47:42 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface ISmsInfoServ extends IBaseService {

    /**
     * 启用、禁用
     */
    void updateEnabled(Object object) throws BaseException;
}
