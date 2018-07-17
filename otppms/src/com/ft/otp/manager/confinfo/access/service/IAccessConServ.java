/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.access.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 访问控制策略service接口
 *
 * @Date in Dec 27, 2012,5:38:38 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public interface IAccessConServ extends IBaseService {

    /**
     * 查询访问IP段list列表
     * @Date in Jan 4, 2013,10:19:09 AM
     * @param object
     * @return
     * @throws BaseException
     */
    public List<?> queryIPDList(Object object, PageArgument pageArg) throws BaseException;
}
