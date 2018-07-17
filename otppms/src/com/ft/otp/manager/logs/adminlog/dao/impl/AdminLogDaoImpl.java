/**
 *Administrator
 */
package com.ft.otp.manager.logs.adminlog.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.logs.adminlog.dao.IAdminLogDao;
import com.ft.otp.manager.logs.adminlog.entity.AdminLogInfo;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.base.dao.namespace.LogsNSpace;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 管理员日志实现类-DAO说明
 *
 * @Date in May 3, 2011,1:17:36 PM
 *
 * @author ZJY
 */
public class AdminLogDaoImpl extends BaseSqlMapDAO implements IAdminLogDao {

    protected String getNameSpace() {
        return LogsNSpace.ADMIN_LOG_NS;
    }

    public AdminLogInfo getAdminLogInfo(Object object) {
        AdminLogInfo adminLogInfo = (AdminLogInfo) object;
        if (null == adminLogInfo) {
            adminLogInfo = new AdminLogInfo();
        }
        return adminLogInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
     */
    public void addObj(Object object) throws BaseException {
        final AdminLogInfo logInfo = (AdminLogInfo) object;
        final List<String> descList = logInfo.getDescList();
        if (StrTool.listNotNull(descList)) {
            getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
                public Object doInSqlMapClient(SqlMapExecutor executor)
                        throws SQLException {
                    int batch = 0;

                    executor.startBatch();
                    for (int i = 0; i < descList.size(); i++) {
                        String usrTknStr = descList.get(i);
                        logInfo.setDescp(usrTknStr);
                        insert(LogsNSpace.ADMIN_LOG_INSERT_AL, logInfo);

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
        } else {
            insert(LogsNSpace.ADMIN_LOG_INSERT_AL, logInfo);
        }
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
     */
    public int count(Object object) throws BaseException {
        AdminLogInfo adminLogInfo = getAdminLogInfo(object);
        return (Integer) queryForObject(LogsNSpace.ADMIN_LOG_COUNT_AL,
                adminLogInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
     */
    public void delObj(Object object) throws BaseException {
        AdminLogInfo adminLogInfo = getAdminLogInfo(object);
        delete(LogsNSpace.ADMIN_LOG_DELETE, adminLogInfo);
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
        AdminLogInfo adminLogInfo = getAdminLogInfo(object);
        return queryForObject(LogsNSpace.ADMIN_LOG_FIND_AL, adminLogInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm, com.ft.otp.common.page.PageArgument)
     */
    public List<?> query(Object object, PageArgument pageArg)
            throws BaseException {
        AdminLogInfo adminLogInfo = getAdminLogInfo(object);
        adminLogInfo.setPageSize(pageArg.getPageSize());
        adminLogInfo.setStartRow(pageArg.getStartRow());
        return queryForList(LogsNSpace.ADMIN_LOG_SELECT_AL, adminLogInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
     */
    public void updateObj(Object object) throws BaseException {
    }

}
