/**
 *Administrator
 */
package com.ft.otp.manager.notice.customer.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import com.ft.otp.base.dao.namespace.CustomerUserNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.notice.customer.dao.ICustomerUserDao;
import com.ft.otp.manager.notice.customer.entity.CustomerUser;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 派单监视通知DAO实现
 *
 * @Date in May 24, 2012,2:23:30 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class CustomerUserDaoImpl extends BaseSqlMapDAO implements ICustomerUserDao {

    protected String getNameSpace() {
        return CustomerUserNSpace.OTP_CUSTOMER_USER_NS;
    }

    private CustomerUser getCustomerUser(Object object) {
        CustomerUser customerUser = (CustomerUser) object;
        if (null == customerUser) {
            customerUser = new CustomerUser();
        }

        return customerUser;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        final CustomerUser customerUser = getCustomerUser(object);
        insert(CustomerUserNSpace.INSERT_CUSTOMER_USER, customerUser);
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
        delete(CustomerUserNSpace.DELETE_CUSTOMER_USER, getCustomerUser(object));
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
        return queryForObject(CustomerUserNSpace.FIND_CUSTOMER_USER, getCustomerUser(object));
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        CustomerUser customerUser = getCustomerUser(object);
        customerUser.setPageSize(pageArg.getPageSize());
        customerUser.setStartRow(pageArg.getStartRow());

        return queryForList(CustomerUserNSpace.QUERY_CUSTOMER_USER, customerUser);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    	CustomerUser customerUser = getCustomerUser(object);
        update(CustomerUserNSpace.UPDATE_CUSTOMER_USER, customerUser);
    }

}
