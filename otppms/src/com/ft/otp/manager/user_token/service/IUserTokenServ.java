/**
 *Administrator
 */
package com.ft.otp.manager.user_token.service;

import java.util.List;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 用户令牌业务逻辑接口提供类
 *
 * @Date in Apr 21, 2011,6:37:18 PM
 *
 * @author TBM
 */
public interface IUserTokenServ extends IBaseService {

    /**
     * 添加用户令牌对应关系
     * 
     * @Date in Apr 24, 2011,7:05:31 PM
     */
    void addUsrTkn(List<?> utList) throws BaseException;

    /**
     * 用户或令牌批量查询，使用IN()
     * @Date in Apr 26, 2011,5:29:57 PM
     * @param usrList
     *        用户或是是管理员集合
     * @param tknList
     *        令牌集合
     * @param uOrAdmMark
     *        第一个参数是用户还是管理员集合 标识 0 用户；1管理员
     * @throws BaseException
     */
    List<?> batchQueryUT(List<?> usrList, List<?> tknList, int uOrAdmMark) throws BaseException;

    /**
     * 根据用户名查询对应令牌号及其主要信息
     * @Date in May 3, 2011,3:11:44 PM
     * @param object
     * @param pageArg
     * @return
     * @throws BaseException
     */
    List<?> queryJoinTkn(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 批量解除用户、令牌的绑定关系
     * @Date in May 16, 2011,5:40:39 PM
     * @param utList 用户令牌列表
     * @throws BaseException
     */
    void batchUnBindUT(List<Object> utList) throws BaseException;

    /*
     * 根据用户账号查询多用户绑定的令牌
     */
    public List<?> queryMulUserToken(Object object) throws BaseException;
    
    /*
     * 根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
     */
    public List<?> queryTokenOth(Object object) throws BaseException;
    
    /*
     * 根据批量用户与域ID查出绑定的令牌号
     */
    public List<?> queryToken(Object object) throws BaseException;

    /*
     * 删除指定用户令牌关系
     * 小批量 几十 条记录
     */
    public void delObjs(Object object) throws BaseException;
    
    /*
     * 删除指定用户外绑定同一令牌的令牌关系
     */
    public void delObjUs(Object object) throws BaseException;
    
    /*
     * 查出用户与令牌所有的绑定数据 
     *  2013-5-3
     */
    public List<?> selObjs(Object object) throws BaseException;
    
    /*
     * 根据用户ID查出此用户所有的绑定令牌号 
     *  2013-5-13
     */
    public List<?> selTokens(Object object) throws BaseException;
    
    /*
     * 用户与令牌绑定关系，查询条件用户ID与域ID，用于解绑操作
     * 2013-8-6
     */
    public List<?> selUserTokens(Object object) throws BaseException;
    
    /*
     * 根据管理员与令牌所有的绑定数据
     */
    public List<?> selectAdminTokens(Object object) throws BaseException;

}
