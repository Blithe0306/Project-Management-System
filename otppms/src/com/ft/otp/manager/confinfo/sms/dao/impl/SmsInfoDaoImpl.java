/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.sms.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.SmsNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.sms.dao.ISmsInfoDao;
import com.ft.otp.manager.confinfo.sms.entity.SmsInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 短信网关配置
 *
 * @Date in Nov 21, 2012,3:52:01 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class SmsInfoDaoImpl extends BaseSqlMapDAO implements ISmsInfoDao {

    protected String getNameSpace() {
        return SmsNSpace.SMS_INFO;
    }
    
    private SmsInfo getSmsInfo(Object object) {
        SmsInfo account = (SmsInfo) object;
        if (null == account) {
            account = new SmsInfo();
        }
        return account;
    }
    
    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        SmsInfo smsInfo = getSmsInfo(object);
        insert(SmsNSpace.ADD_SMS_INFO, smsInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
     */
    public int count(Object object) throws BaseException {
        SmsInfo smsInfo = getSmsInfo(object);
        return (Integer) queryForObject(SmsNSpace.COUNT_SMS_INFO, smsInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
     */
    public void delObj(final Set<?> set) throws BaseException {
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {
                int batch = 0;
                executor.startBatch();
                SmsInfo smsInfo = new SmsInfo();
                Iterator<?> iter = set.iterator();
                while (iter.hasNext()) {
                    String smsid = (String) iter.next();
                    smsInfo.setId(Integer.parseInt(smsid));
                    delete(SmsNSpace.DELETE_SMS_INFO, smsInfo);
                    batch++;
                    if (batch == NumConstant.batchCount) {
                        executor.executeBatch();
                        batch = 0;
                    }
                }
                executor.executeBatch();
                return null;
            }
        });
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
     */
    public Object find(Object object) throws BaseException {
        SmsInfo smsInfo = getSmsInfo(object);
        return queryForObject(SmsNSpace.FIND_SMS_INFO, smsInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        SmsInfo smsInfo = getSmsInfo(object);
        smsInfo.setPageSize(pageArg.getPageSize());
        smsInfo.setStartRow(pageArg.getStartRow());
        return queryForList(SmsNSpace.SELECT_SMS_INFO, smsInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
        SmsInfo smsInfo = getSmsInfo(object);
        update(SmsNSpace.UPDATE_SMS_INFO, smsInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.confinfo.sms.dao.ISmsInfoDao#updateEnabled(java.lang.Object)
     */
    public void updateEnabled(Object object) throws BaseException {
        SmsInfo smsInfo = getSmsInfo(object);
        update(SmsNSpace.ENABLED_SMS_INFO, smsInfo);
    }

}
