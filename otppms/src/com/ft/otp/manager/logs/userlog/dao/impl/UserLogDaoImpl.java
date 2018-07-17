/**
 *Administrator
 */
package com.ft.otp.manager.logs.userlog.dao.impl;

import java.util.List;
import java.util.Set;
import com.ft.otp.base.dao.namespace.LogsNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.logs.userlog.dao.IUserLogDao;
import com.ft.otp.manager.logs.userlog.entity.UserLogInfo;

/**
 * 用户日志实现类DAO功能说明
 *
 * @Date in May 4, 2011,4:48:11 PM
 *
 * @author ZJY
 */
public class UserLogDaoImpl extends BaseSqlMapDAO implements IUserLogDao {

    protected String getNameSpace() {
        return LogsNSpace.USER_LOG_NS;
    }

    public UserLogInfo getUserLogInfo(Object object) {
        UserLogInfo userLogInfo = (UserLogInfo) object;
        if (null == userLogInfo) {
            userLogInfo = new UserLogInfo();
        }
        return userLogInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        UserLogInfo userLogInfo = getUserLogInfo(object);
        return (Integer) queryForObject(LogsNSpace.USER_LOG_COUNT_UL,
                userLogInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        UserLogInfo userLogInfo = getUserLogInfo(object);
        delete(LogsNSpace.USER_LOG_DELETE, userLogInfo);
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
        UserLogInfo userLogInfo = getUserLogInfo(object);
        return queryForObject(LogsNSpace.USER_LOG_FIND_UL, userLogInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        UserLogInfo userLogInfo = getUserLogInfo(object);
        userLogInfo.setPageSize(pageArg.getPageSize());
        userLogInfo.setStartRow(pageArg.getStartRow());
        return queryForList(LogsNSpace.USER_LOG_SELECT_UL, userLogInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

}
