/**
 *Administrator
 */
package com.ft.otp.manager.token.service;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.common.page.PageArgument;

/**
 * 令牌service接口类功能说明
 *
 * @Date in Apr 18, 2011,11:21:50 AM
 *
 * @author ZJY
 */
public interface ITokenServ extends IBaseService {

    /**
     * 批量进行令牌的启用/停用、挂失/解挂、锁定/解锁等操作
     * 
     * @Date in Jul 15, 2013,10:55:08 AM
     * @param tokens
     * @param operType
     * @return
     * @throws BaseException
     */
    int updateTokenState(List<?> tokens, int operType) throws BaseException;

    /**
     * 更新一个或一组织令牌的状态(启用/停用、锁定/解锁、挂失/解挂、注销)
     * 令牌更换后，更新令牌状态
     * 
     * @Date in Jul 15, 2013,4:23:34 PM
     * @param object
     * @throws BaseException
     */
    int updateTokenState(Object object) throws BaseException;

    /**
     * 令牌导入
     * @Date in Apr 24, 2011,10:56:33 AM
     * @param tokeniList
     * @throws Exception
     */
    int importToken(List<Object> tokeniList) throws BaseException;

    /**
     * 软件令牌分发
     */
    void updateSoftTkn(Object object) throws BaseException;

    /**
     * 令牌批量分配
     * 批量迁移令牌的组织机构
     * 
     * @Date in Jul 16, 2013,10:52:17 AM
     * @param tokens
     * @return
     * @throws BaseException
     */
    int updateTokenOrg(List<?> tokens) throws BaseException;

    /**
     * 特殊条件计算
     * 要求 orgunitId 不参与匹配
     * BC byContion
     * */
    int countBC(Object object) throws BaseException;
    
    /**
     * 特殊条件计算
     * BC byContion
     * */
    int countCT(Object object) throws BaseException;

    /**
     * 查找一组数据
     * 支持模糊查询
     * 
     * @Date in Apr 2, 2011,4:51:56 PM
     * @return
     * @throws BaseException
     */
    List<?> queryBC(Object object, PageArgument pageArg) throws BaseException;
    
    /**
     * 查找一组数据
     * 支持模糊查询
     * 
     * @return
     * @throws BaseException
     */
    List<?> queryCT(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 令牌分配
     * 迁移令牌的组织机构
     * 
     * @Date in Jul 16, 2013,11:17:13 AM
     * @param object
     * @return
     * @throws BaseException
     */
    int updateTokenOrg(Object object) throws BaseException;

    /**
     * 根据令牌号查询令牌的信息（含PUBKEY）
     * 
     * @Date in Jul 16, 2013,11:18:00 AM
     * @param object
     * @return
     * @throws BaseException
     */
    Object findObj(Object object) throws BaseException;
    
    /**
     * 根据条件查询令牌号
     * @param object
     * @return
     * @throws BaseException
     */
    List<?> findToken(Object object) throws BaseException;
    
    /**
     * 锁定/解锁
     * @param keys
     * @param lost
     * @return
     * @throws BaseException
     */
    public boolean updateTokenLocked(Set<?> keys, int lost, int locktime) throws BaseException;

}
