/**
 *Administrator
 */
package com.ft.otp.manager.admin.admin_perm.service;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.manager.admin.admin_perm.entity.AdminPermCode;

/**
 * 管理员常用功能业务 接口
 *
 * @Date in May 14, 2013,2:21:00 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public interface IAdminPermServ extends IBaseService {

    /**
     * 查询表中是否存在此管理员的此常用权限码
     * 
     * @Date in May 14, 2013,6:40:57 PM
     * @param adminPermCode
     * @return
     * @throws BaseException
     */
    public boolean isOftenUsed(AdminPermCode adminPermCode) throws BaseException;
}
