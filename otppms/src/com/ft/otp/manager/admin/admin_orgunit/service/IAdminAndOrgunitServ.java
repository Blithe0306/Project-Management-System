package com.ft.otp.manager.admin.admin_orgunit.service;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;

/**
 * 管理员-组织机构关系DAO接口类功能说明
 *
 * @Date in January 24, 2013,10:00:00 AM
 *
 * @author BYL
 */
public interface IAdminAndOrgunitServ extends IBaseService {
    
    /***
     * 根据管理员id 查询 管理员组织机构关系
     * @param object
     */
    List<?> queryAdminAndOrgunitByAdminId(Object object) throws BaseException;
    
    /***
     * 根据组织机构id 查询 管理员组织机构关系
     * @param object
     */
    List<?> queryAdminAndOrgunitByOrgunitId(Object object) throws BaseException;
    
    /***
     * 根据域id 查询 管理员组织机构关系
     * @param object
     */
    List<?> queryAdminAndOrgunitByDomainId(Object object) throws BaseException;
    
    /***
     * 添加 管理员组织机构关系
     * @param list
     */
    void addObjs(List<?> list) throws BaseException;

}
