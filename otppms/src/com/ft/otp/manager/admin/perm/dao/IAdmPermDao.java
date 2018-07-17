/**
 *Administrator
 */
package com.ft.otp.manager.admin.perm.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 管理员权限DAO接口
 *
 * @Date in Jun 29, 2011,5:03:34 PM
 *
 * @author TBM
 */
public interface IAdmPermDao extends IBaseDao {

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
