/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.dao;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 用户业务数据接口提供类
 *
 * @Date in Apr 20, 2011,10:58:10 AM
 *
 * @author TBM
 */
public interface IUserInfoDao extends IBaseDao {

    /**
     * 批量导入用户
     * @Date in May 12, 2011,5:41:24 PM
     * @param usrList
     * @throws BaseException
     */
    void batchimportUser(List<?> usrList) throws BaseException;

    /**
     * 批量更新用户
     * @Date in May 12, 2011,5:40:13 PM
     * @param usrList
     * @throws BaseException
     */
    void batchUpdateUser(List<?> usrList) throws BaseException;

    /**
     * 导出用户查询用户、令牌、用户组数据
     * @Date in May 14, 2011,2:06:09 PM
     * @param object
     * @return
     * @throws BaseException
     */
    List<?> queryUIUTUG(Object object) throws BaseException;
    
    /**
     * 导出用户查询用户、令牌、用户组数据,不含令牌
     * @Date in Jun 14, 2013,5:45:09 PM
     * @param object
     * @return
     * @throws BaseException
     */
    List<?> queryUTUG(Object object) throws BaseException;

    /**
     * 锁定/解锁用户
     * @Date in Jun 24, 2011,4:08:25 PM
     * @param object
     * @param locked
     * @throws BaseException
     */
    void updateUserLost(Object object, int locked) throws BaseException;

    /**
     * 设置静态密码
     */
    void updateStaticPass(Object object) throws BaseException;

    /**
     * 根据用户查询用户邮箱
     */
    List<?> selectUserEmail(Object object) throws BaseException;
    
    /* 
     * 禁用启用用户
     */
    public void updateUserEnabled(Object object, int enabled) throws BaseException; 
    
    /* 
     * 变更用户组织机构
     */
    public void updateUserOrgunit(Object object) throws BaseException;
    
    /* 
     * 组织机构范围内批量变更用户组织机构
     */
    public void updateUserOrgunits(Object object) throws BaseException;
    
    /**
     * 批量添加返回客户端Radius配置
     * @Date in May 8, 2013,4:06:41 PM
     * @param usrList
     * @throws BaseException
     */
    void batchSetRadId(List<?> usrList) throws BaseException;
    
    /**
     * 批量设置后端认证
     * @param usrList
     * @throws BaseException
     */
    void batchSetBackendId(List<?> usrList) throws BaseException;
    
    /**
     * 批量设置本地认证模式
     * @param usrList
     * @throws BaseException
     */
    void batchSetLocalauth(List<?> usrList) throws BaseException;
    
    /**
     * 数据统计
     * 支持模糊查询
     * @param queryForm
     * @return
     * @throws BaseException
     */
    int countUser(Object object) throws BaseException;
    
    /**
     * 数据统计
     * 支持模糊查询
     * @param queryForm
     * @return
     * @throws BaseException
     */
    int countBind(Object object) throws BaseException;
    
    /**
     * 查找一组数据
     * 支持模糊查询
     * @return
     * @throws BaseException
     */
    List<?> queryUser(Object object, PageArgument pageArg) throws BaseException;
    
    /**
     * 查找一组数据
     * 支持模糊查询
     * @return
     * @throws BaseException
     */
    List<?> queryUser(Object object) throws BaseException;
    
    /**
     * 用户绑定令牌
     * 支持模糊查询
     * @return
     * @throws BaseException
     */
    List<?> queryBind(Object object, PageArgument pageArg) throws BaseException;
    
    /**
     * 用户锁定/解锁
     * @param keys
     * @param locked
     * @return
     * @throws BaseException
     */
    boolean userLocked(Set<?> keys, int locked) throws BaseException;
    
    /**
     * 用户禁用/启用
     * @param keys
     * @param abled
     * @return
     * @throws BaseException
     */
    boolean userAbled(Set<?> keys, int enabled) throws BaseException;
    
    /**
     * 查找一组数据
     * @return
     * @throws BaseException
     */
    List<?> selectUser(Object object) throws BaseException;
    
    /**
     * 根据USERID数组查询用户数据，用于短信分发
     * @return
     * @throws BaseException
     */
    List<?> selectUserToSms(Object object) throws BaseException;
    
    /**
     * 根据Radius，ID查询
     * @author LXH
     * @date Aug 29, 2014 10:18:15 AM
     * @param object
     * @return
     * @throws BaseException
     */
    List<?> selectUserToRad(Object object) throws BaseException;
    
    /**
     * 更新Ldap用户来源，选择删除本地用户使用
     * @param usrList
     * @throws BaseException
     * @return void
     * @throws
     */
    void batchDelUser(List<?> usrList) throws BaseException;
}
