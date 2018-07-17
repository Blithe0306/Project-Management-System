package com.ft.otp.manager.admin.admin_orgunit.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 管理员-组织机构关系DAO接口类功能说明
 *
 * @Date in January 24, 2013,10:00:00 AM
 *
 * @author BYL
 */
public interface IAdminAndOrgunitDao extends IBaseDao {  
    
    /***
     * 查询 管理员组织机构关系
     * @param object
     */
    List<?> queryAdminAndOrgunit(Object object) throws BaseException;
    
    /***
     * 添加 管理员组织机构关系
     * @param list
     */
    void addObjs(List<?> list) throws BaseException;
}
