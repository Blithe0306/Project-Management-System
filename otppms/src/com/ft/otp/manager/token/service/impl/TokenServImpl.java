/**
 *Administrator
 */
package com.ft.otp.manager.token.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.token.dao.ITokenDao;
import com.ft.otp.manager.token.service.ITokenServ;

/**
 * 令牌管理业务接口实现类
 *
 * @Date in Apr 18, 2011,11:23:03 AM
 *
 * @author ZJY
 */
public class TokenServImpl implements ITokenServ {

    private ITokenDao tokenDao = null;

    /**
     * @return the tokenDao
     */
    public ITokenDao getTokenDao() {
        return tokenDao;
    }

    /**
     * @param tokenDao the tokenDao to set
     */
    public void setTokenDao(ITokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public void addObj(Object object) throws BaseException {
        tokenDao.addObj(object);
    }

    public int count(Object object) throws BaseException {
        return tokenDao.count(object);
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(Set<?> keys) throws BaseException {
        tokenDao.delObj(keys);
    }

    public Object find(Object object) throws BaseException {
        return tokenDao.find(object);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return tokenDao.query(object, pageArg);
    }

    public void updateObj(Object object) throws Exception {
        tokenDao.updateObj(object);
    }

    public int importToken(List<Object> tokeniList) throws BaseException {
        return tokenDao.tokenImport(tokeniList);
    }

    public void updateSoftTkn(Object object) throws BaseException {
        tokenDao.updateSoftTkn(object);
    }

    /**
     * 令牌批量分配
     * 批量迁移令牌的组织机构
     */
    public int updateTokenOrg(List<?> tokens) throws BaseException {
        return tokenDao.updateTokenOrg(tokens);
    }

    /**
     * 特殊条件计算
     * 要求 orgunitId 不参与匹配
     * BC byContion
     * */
    public int countBC(Object object) throws BaseException {
        return tokenDao.countBC(object);
    }
    
    /**
     * 特殊条件计算
     * BC byContion
     * */
    public int countCT(Object object) throws BaseException {
        return tokenDao.countCT(object);
    }

    /**
     * 查找一组数据
     * 支持模糊查询
     * @Date in Apr 2, 2011,4:51:56 PM
     * @return
     * @throws BaseException
     */
    public List<?> queryBC(Object object, PageArgument pageArg) throws BaseException {
        return tokenDao.queryBC(object, pageArg);
    }
    
    /**
     * 锁定/解锁
     * @param keys
     * @param lost
     * @return
     * @throws BaseException
     */
    public boolean updateTokenLocked(Set<?> keys, int lost, int locktime) throws BaseException {
        return tokenDao.tokenLocked(keys, lost, locktime);
    }
    
    /**
     * 查找一组数据
     * 支持模糊查询
     * @return
     * @throws BaseException
     */
    public List<?> queryCT(Object object, PageArgument pageArg) throws BaseException {
        return tokenDao.queryCT(object, pageArg);
    }

    /**
     * 令牌分配
     * 迁移令牌的组织机构
     */
    public int updateTokenOrg(Object object) throws BaseException {
        return tokenDao.updateTokenOrg(object);
    }

    /**
     * 根据令牌号查询令牌的信息（含PUBKEY）
     * 
     */
    public Object findObj(Object object) throws BaseException {
        return tokenDao.findObj(object);
    }

    /**
     * 更新一个或一组织令牌的状态(启用/停用、锁定/解锁、挂失/解挂、注销)
     * 令牌更换后，更新令牌状态
     */
    public int updateTokenState(Object object) throws BaseException {
        return tokenDao.updateTokenState(object);
    }

    /**
     * 批量进行令牌的启用/停用、挂失/解挂、锁定/解锁等操作
     */
    public int updateTokenState(List<?> tokens, int operType) throws BaseException {
        return tokenDao.updateTokenState(tokens, operType);
    }
    
    /**
     * 根据条件查询令牌号
     */
	public List<?> findToken(Object object) throws BaseException {
		return tokenDao.findToken(object);
	}

}
