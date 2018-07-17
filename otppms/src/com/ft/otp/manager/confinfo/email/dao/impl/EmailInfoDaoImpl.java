/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.email.dao.impl;

import java.util.List;
import java.util.Set;

import com.ft.otp.base.dao.namespace.EmailNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.email.dao.IEmailInfoDao;
import com.ft.otp.manager.confinfo.email.entity.EmailInfo;

/**
 * 类、接口等说明
 *
 * @Date in Nov 19, 2012,1:21:38 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class EmailInfoDaoImpl extends BaseSqlMapDAO implements IEmailInfoDao {

    protected String getNameSpace() {
        return EmailNSpace.EMAIL_INFO;
    }
    
    private EmailInfo getEmailInfo(Object object) {
        EmailInfo account = (EmailInfo) object;
        if (null == account) {
            account = new EmailInfo();
        }
        return account;
    }
    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.config.dao.IConfigInfoDao#queryConfInfo(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> queryConfInfo(Object object, PageArgument pageArg) throws BaseException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.config.dao.IConfigInfoDao#updateConf(java.util.List)
     */
    public void updateConf(List<Object> list) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        EmailInfo emailInfo = getEmailInfo(object);
        insert(EmailNSpace.ADD_EMAIL_INFO, emailInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        return 0;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        EmailInfo emailInfo = getEmailInfo(object);
        delete(EmailNSpace.DEL_EMAIL_INFO, emailInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(Set<?> set) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        EmailInfo emailInfo = getEmailInfo(object);
        return queryForObject(EmailNSpace.FIND_EMAIL_INFO, emailInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        EmailInfo emailInfo = getEmailInfo(object);
        emailInfo.setStartRow(pageArg.getStartRow());
        emailInfo.setPageSize(pageArg.getPageSize());
        List<?> list = queryForList(EmailNSpace.SELECT_EMAIL_INFO, emailInfo);
        return list;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        EmailInfo emailInfo = getEmailInfo(object);
        update(EmailNSpace.UPDATE_EMAIL_INFO, emailInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.email.dao.IEmailInfoDao#updateIsdefaultOE(java.lang.Object)
     */
    public void updateIsdefaultOE(Object object) throws BaseException {
        EmailInfo emailInfo = getEmailInfo(object);
        update(EmailNSpace.CANCEL_EMAIL_DEFAULT, emailInfo);
        emailInfo.setIsdefault(1);
        update(EmailNSpace.SET_EMAIL_DEFAULT, emailInfo);
    }

}
