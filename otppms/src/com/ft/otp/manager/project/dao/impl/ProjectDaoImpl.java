/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 23, 2014,1:38:20 PM
 */
package com.ft.otp.manager.project.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.ProjectNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.project.dao.IProjectDao;
import com.ft.otp.manager.project.entity.Project;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 类、接口等说明
 *
 * @Date Dec 23, 2014,1:38:20 PM
 * @version v1.0
 * @author ZWX
 */
public class ProjectDaoImpl extends BaseSqlMapDAO  implements IProjectDao {
    
    protected String getNameSpace() {
        return ProjectNSpace.PROJECT_NS;
    }

    private Project getProjectInfo(Object object) {
        Project proBean = (Project) object;
        if (null == proBean) {
            proBean = new Project();
        }
        return proBean;
    }

    public void addObj(Object object) throws BaseException {
        Project proBean = (Project) object;
        insert(ProjectNSpace.PROJECT_ADD, proBean);
    }

    public int count(Object object) throws BaseException {
        Project proBean = (Project) object;
        return (Integer) queryForObject(ProjectNSpace.PROJECT_COUNT, proBean);
    }

    public void delObj(Object object) throws BaseException {
    }

    public void delObj(final Set<?> set) throws BaseException {
     // 批量删除用户
        getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
            public Object doInSqlMapClient(SqlMapExecutor executor)
                    throws SQLException {

                Project proBean = null;
                int batch = 0;
                executor.startBatch();
                Iterator<?> iter = set.iterator();
                while (iter.hasNext()) {
                    String userId = (String)iter.next();
                    proBean = new Project();
                    proBean.setId(userId);

                    // 删除用户信息
                    delete(ProjectNSpace.PROJECT_DEL, proBean);
                    batch++;
                    
                    // 达到500时，提交
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

    public Object find(Object object) throws BaseException {
        Project proBean = (Project) object;
        return queryForObject(ProjectNSpace.PROJECT_FIND, proBean);
    }
    
    public Object findExceptself(Object object) throws BaseException {
    	Project proBean = (Project) object;
    	return queryForObject(ProjectNSpace.PROJECT_FIND_EXCEPT_SELF, proBean);
    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        Project proBean = getProjectInfo(object);
        proBean.setStartRow(pageArg.getStartRow());
        proBean.setPageSize(pageArg.getPageSize());
        return queryForList(ProjectNSpace.PROJECT_QUERY, proBean);
    }
    public Object selectPowerAdmin()throws BaseException{
    	return queryForObject(ProjectNSpace.PROJECT_SELECT_POWERADMIN, null);
    }

    public void updateObj(Object object) throws Exception {
        Project proBean = (Project) object;
        update(ProjectNSpace.PROJECT_UPDA, proBean);
    }
}
