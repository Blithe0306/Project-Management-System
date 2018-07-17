/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.service.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.user.userinfo.dao.IUserInfoDao;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;

/**
 * 用户业务逻辑实现类
 *
 * @Date in Apr 20, 2011,10:56:11 AM
 *
 * @author TBM
 */
public class UserInfoServImpl implements IUserInfoServ {

    private IUserInfoDao userInfoDao = null;

    /**
     * @return the userInfoDao
     */
    public IUserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    /**
     * @param userInfoDao the userInfoDao to set
     */
    public void setUserInfoDao(IUserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        userInfoDao.addObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        return userInfoDao.count(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#delObj(java.util.Set)
     */
    public void delObj(Set<?> keys) throws BaseException {
        userInfoDao.delObj(keys);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        return userInfoDao.find(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        return userInfoDao.query(object, pageArg);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.service.IBaseService#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws Exception {
        userInfoDao.updateObj(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#batchUpdateUser(java.util.List)
     */
    public void batchUpdateUser(List<?> usrList) throws BaseException {
        userInfoDao.batchUpdateUser(usrList);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#batchimportUser(java.util.List)
     */
    public void batchimportUser(List<?> usrList) throws BaseException {
        userInfoDao.batchimportUser(usrList);
    }
    
    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#batchImportUs(java.util.List)
     */
    public void batchImportUs(List<?> usrList) throws BaseException {
        userInfoDao.batchimportUser(usrList);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#queryUIUTUG(com.ft.otp.manager.user.userinfo.entity.UserInfo)
     */
    public List<?> queryUIUTUG(Object object) throws BaseException {
        return userInfoDao.queryUIUTUG(object);
    }
    
    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#queryUIUTUG(com.ft.otp.manager.user.userinfo.entity.UserInfo)
     */
    public List<?> queryUTUG(Object object) throws BaseException {
        return userInfoDao.queryUTUG(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#updateUserLost(java.lang.Object, int)
     */
    public void updateUserLost(Object object, int locked) throws BaseException {
        userInfoDao.updateUserLost(object, locked);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#setStaticPass(java.lang.Object)
     */
    public void updateStaticPass(Object object) throws BaseException {
        userInfoDao.updateStaticPass(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#selectUserEmail(java.lang.Object)
     */
    public List<?> selectUserEmail(Object object) throws BaseException {
        return userInfoDao.selectUserEmail(object);
    }
    
    /* 
     * 禁用启用用户
     */
    public void updateUserEnabled(Object object, int enabled) throws BaseException {
    	 userInfoDao.updateUserEnabled(object, enabled);
    }
    
    /* 
     * 变更用户组织机构
     */
    public void updateUserOrgunit(Object object) throws BaseException{
    	userInfoDao.updateUserOrgunit(object);
    }
    
    /* 
     * 组织机构范围内批量变更用户组织机构
     */
    public void updateUserOrgunits(Object object) throws BaseException{
    	userInfoDao.updateUserOrgunits(object);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.user.userinfo.service.IUserInfoServ#batchSetRadId(java.util.List)
     */
    public void batchSetRadId(List<?> usrList) throws BaseException {
        userInfoDao.batchSetRadId(usrList);
    }
    
    /*
     * 批量设置后端认证
     */
    public void batchSetBackendId(List<?> usrList) throws BaseException {
        userInfoDao.batchSetBackendId(usrList);
    }
    
    /*
     * 批量设置本地认证模式
     */
    public void batchSetLocalauth(List<?> usrList) throws BaseException {
        userInfoDao.batchSetLocalauth(usrList);
    }
    
    /**
     * 机构变更，用户数据总数
     */
	public int countUser(Object object) throws BaseException {
        return userInfoDao.countUser(object);
    }
	
	/**
     * 用户绑定令牌，用户数据总数
     */
	public int countBind(Object object) throws BaseException {
        return userInfoDao.countBind(object);
    }

	public List<?> queryUser(Object object, PageArgument pageArg)
			throws BaseException {
		 return userInfoDao.queryUser(object, pageArg);
	}
	
	public List<?> queryUser(Object object)
		throws BaseException {
		return userInfoDao.queryUser(object);
	}
	
	public List<?> queryBind(Object object, PageArgument pageArg)
		throws BaseException {
		return userInfoDao.queryBind(object, pageArg);
	}
	
	/**
	 * 用户锁定/解锁
	 */
	public boolean updateUserLocked(Set<?> keys, int locked)
			throws BaseException {
		return userInfoDao.userLocked(keys, locked);
	}
	
	/**
	 * 用户启用/禁用
	 */
	public boolean updateUserAbled(Set<?> keys, int enabled)
			throws BaseException {
		return userInfoDao.userAbled(keys, enabled);
	}
	
	public List<?> selectUser(Object object)
		throws BaseException {
	 return userInfoDao.selectUser(object);
	}
	
	public List<?> selectUserToSms(Object object)throws BaseException {
		return userInfoDao.selectUserToSms(object);
	}
	
	/**
	 * 根据Radius，ID查询
	 */
	public List<?> selectUserToRad(Object object)
			throws BaseException {
		return userInfoDao.selectUserToRad(object);
	}
	
    public void batchDelUser(List<?> usrList) throws BaseException {
        userInfoDao.batchDelUser(usrList);
    }
}
