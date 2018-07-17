/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.email.service;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 邮件服务器配置
 *
 * @Date in Nov 19, 2012,1:14:03 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IEmailInfoServ extends IBaseService {

    /**
     * 设置取消默认邮件服务器
     * @Date in Nov 19, 2012,7:18:45 PM
     * @param object
     * @throws BaseException
     */
    public void updateIsdefaultOE(Object object) throws BaseException;
}
