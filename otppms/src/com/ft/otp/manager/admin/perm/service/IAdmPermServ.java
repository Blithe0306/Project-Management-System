/**
 *Administrator
 */
package com.ft.otp.manager.admin.perm.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 管理员权限服务接口
 *
 * @Date in Jun 29, 2011,5:01:30 PM
 *
 * @author TBM
 */
public interface IAdmPermServ extends IBaseService {

    /**
     * 根据角色查询Perm
     * @Date in Apr 16, 2013,5:48:06 PM
     * @param object
     * @param pageArg
     * @return
     * @throws BaseException
     */
    List<?> queryRolePerm(Object object, PageArgument pageArg) throws BaseException;
}
