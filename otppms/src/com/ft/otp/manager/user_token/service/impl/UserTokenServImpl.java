/**
 *Administrator
 */
package com.ft.otp.manager.user_token.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user_token.dao.IUserTokenDao;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户令牌业务逻辑实现类
 *
 * @Date in Apr 21, 2011,6:37:51 PM
 *
 * @author TBM
 */
public class UserTokenServImpl implements IUserTokenServ {

    private IUserTokenDao userTokenDao = null;

    /**
     * @return the userTokenDao
     */
    public IUserTokenDao getUserTokenDao() {
        return userTokenDao;
    }

    /**
     * @param userTokenDao the userTokenDao to set
     */
    public void setUserTokenDao(IUserTokenDao userTokenDao) {
        this.userTokenDao = userTokenDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        userTokenDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return userTokenDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        userTokenDao.delObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return userTokenDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        return userTokenDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.service.IUserTokenServ#addUsrTkn(java.util.List)
     */
    public void addUsrTkn(List<?> utList) throws BaseException {
        userTokenDao.addUsrTkn(utList);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.service.IUserTokenServ#query(com.ft.otp.base.form.BaseQueryForm)
     */
    public List<?> batchQueryUT(List<?> usrList, List<?> tknList, int uOradmMark) throws BaseException {
        List<String> idsArr = null;
        List<Integer> domainIdsArr = null;

        int num = 0;
        if (StrTool.listNotNull(usrList)) {
            UserInfo userInfo = null;
            AdminUser adminUser = null;
            idsArr = new ArrayList<String>();
            domainIdsArr = new ArrayList<Integer>();
            for (int i = 0; i < usrList.size(); i++) {
                if (uOradmMark == NumConstant.common_number_0) {
                    //用户集合
                    userInfo = (UserInfo) usrList.get(i);
                    Integer domainId = new Integer(userInfo.getDomainId());
                    idsArr.add(userInfo.getUserId());
                    domainIdsArr.add(domainId);
                } else {
                    //管理员集合
                    adminUser = (AdminUser) usrList.get(i);
                    idsArr.add(adminUser.getAdminid());
                    domainIdsArr = null;
                }
            }
        } else if (StrTool.listNotNull(tknList)) {
            TokenInfo tokenInfo = null;
            idsArr = new ArrayList<String>();
            for (int k = 0; k < tknList.size(); k++) {
                tokenInfo = (TokenInfo) tknList.get(k);
                String token = tokenInfo.getToken();
                idsArr.add(token);
            }
            num = 1;
        }

        UserToken userToken = getUserToken(idsArr, domainIdsArr, num);
        return userTokenDao.query(userToken);
    }

    private UserToken getUserToken(List<String> idStr, List<Integer> domainIdsArr, int num) {
        UserToken userToken = new UserToken();
        if (num == 0) {
            userToken.setUserIds(idStr);
            userToken.setDomainIds(domainIdsArr);
        } else if (num == 1) {
            userToken.setTokenIds(idStr);
            userToken.setDomainIds(domainIdsArr);
        }
        return userToken;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.service.IUserTokenServ#queryJoinTkn(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryJoinTkn(Object object, PageArgument pageArg) throws BaseException {
        return userTokenDao.queryJoinTkn(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.service.IUserTokenServ#batchUnBindUT(java.util.List, java.util.List, java.util.List)
     */
    public void batchUnBindUT(List<Object> utList) throws BaseException {
        userTokenDao.batchUnBindUT(utList);
    }

    /*
     * 根据用户账号查询多用户绑定的令牌
     */
    public List<?> queryMulUserToken(Object object) throws BaseException {
        return userTokenDao.queryMulUserToken(object);
    }
    
    /*
     * 根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
     */
    public List<?> queryTokenOth(Object object) throws BaseException {
        return userTokenDao.queryTokenOth(object);
    }
    
    /*
     * 根据用户令牌号与用户ID查出除此用户下其它用户绑定的同一令牌
     */
    public List<?> queryToken(Object object) throws BaseException {
        return userTokenDao.queryToken(object);
    }

    /*
     * 删除指定用户令牌关系
     */
    public void delObjs(Object object) throws BaseException {
        userTokenDao.delObjs(object);
    }
    
    /*
     * 删除指定用户外绑定同一令牌的令牌关系
     */
    public void delObjUs(Object object) throws BaseException {
        userTokenDao.delObjUs(object);
    }
    
    /*
     * 查出用户与令牌所有的绑定数据 
     *  2013-5-3
     */
    public List<?> selObjs(Object object) throws BaseException {
        return userTokenDao.selObjs(object);
    }

    /*
     * 根据用户ID查出此用户所有的绑定令牌号 
     *  2013-5-13
     */
    public List<?> selTokens(Object object) throws BaseException {
        return userTokenDao.selTokens(object);
    }
    
    /*
     * 用户与令牌绑定关系，查询条件用户ID与域ID，用于解绑操作
     * 2013-8-6
     */
    public List<?> selUserTokens(Object object) throws BaseException {
        return userTokenDao.selUserTokens(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user_token.service.IUserTokenServ#selectAdminTokens(java.lang.Object)
     */
    public List<?> selectAdminTokens(Object object) throws BaseException {
        return userTokenDao.selectAdminTokens(object);
    }
}
