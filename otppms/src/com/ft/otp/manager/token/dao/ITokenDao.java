/**
 *Administrator
 */
package com.ft.otp.manager.token.dao;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;

/**
 * 令牌操作DAO接口
 *
 * @Date in Apr 18, 2011,11:19:14 AM
 *
 * @author ZJY
 */
public interface ITokenDao extends IBaseDao {

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
     * @param obList
     * @return
     * @throws Exception
     */
    int tokenImport(List<Object> obList) throws BaseException;

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
     * 令牌分配
     * 迁移令牌的组织机构
     * 
     * @Date in Jul 16, 2013,11:11:27 AM
     * @param object
     * @return
     * @throws BaseException
     */
    int updateTokenOrg(Object object) throws BaseException;

    /**
     * 特殊条件计算
     * 要求 orgunitId 不参与匹配
     * BC byContion
     * */
    public int countBC(Object object) throws BaseException;
    
    /**
     * 特殊条件计算
     * BC byContion
     * */
    public int countCT(Object object) throws BaseException;

    /**
     * 查找一组数据
     * 要求 orgunitId 不参与匹配
     * 
     * @Date in Apr 2, 2011,4:51:56 PM
     * @return
     * @throws BaseException
     */
    public List<?> queryBC(Object object, PageArgument pageArg) throws BaseException;
    
    /**
     * 查找一组数据
     * 要求 orgunitId 不参与匹配
     * 
     * @return
     * @throws BaseException
     */
    public List<?> queryCT(Object object, PageArgument pageArg) throws BaseException;

    /**
     * 根据令牌号查询令牌的信息（含PUBKEY）
     * 
     */
    Object findObj(Object object) throws BaseException;
    
    /**
     * 根据条件查询令牌号
     * @param object
     * @return
     * @throws BaseException
     */
    public List<?> findToken(Object object) throws BaseException;
    
    /**
     * 锁定/解锁
     * @param keys
     * @param locked
     * @return
     * @throws BaseException
     */
    boolean tokenLocked(Set<?> keys, int locked, int locktime) throws BaseException;

}