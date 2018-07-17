package com.ft.otp.manager.project.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.ProjectResultNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.project.dao.IProjectResultDao;
import com.ft.otp.manager.project.entity.ProjectResult;
import com.ibatis.sqlmap.client.SqlMapExecutor;

public class ProjectResultDaoImpl extends BaseSqlMapDAO implements IProjectResultDao {

	protected String getNameSpace() {
		return ProjectResultNSpace.PROJECT_RESULT_NS;
	}

	private ProjectResult getProjectResultInfo(Object object) {
		ProjectResult proBeanResult = (ProjectResult) object;
		if (null == proBeanResult) {
			proBeanResult = new ProjectResult();
		}
		return proBeanResult;
	}

	@Override
	public void addObj(Object object) throws BaseException {
		ProjectResult proBeanResult = (ProjectResult) object;
		insert(ProjectResultNSpace.PROJECT_RESULT_ADD, proBeanResult);
	}

	public int count(Object object) throws BaseException {
		ProjectResult proBeanResult = (ProjectResult) object;
		return (Integer) queryForObject(ProjectResultNSpace.PROJECT_RESULT_COUNT, proBeanResult);
	}

	public void delObj(Object object) throws BaseException {
		ProjectResult proBeanResult = (ProjectResult) object;
		// 删除项目总结
		delete(ProjectResultNSpace.PROJECT_RESULT_DEL, proBeanResult);
	}

	public void delObj(final Set<?> set) throws BaseException {
		// 批量删除用户
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				ProjectResult proBean = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = set.iterator();
				while (iter.hasNext()) {
					String userId = (String) iter.next();
					proBean = new ProjectResult();
					proBean.setId(Integer.parseInt(userId));

					// 删除项目总结
					delete(ProjectResultNSpace.PROJECT_RESULT_DEL, proBean);
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
		ProjectResult proBean = (ProjectResult) object;
		return queryForObject(ProjectResultNSpace.PROJECT_RESULT_FIND, proBean);
	}

	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		ProjectResult proBean = getProjectResultInfo(object);
		proBean.setStartRow(pageArg.getStartRow());
		proBean.setPageSize(pageArg.getPageSize());
		return queryForList(ProjectResultNSpace.PROJECT_RESULT_QUERY, proBean);
	}

	public void updateObj(Object object) throws Exception {
		ProjectResult proBean = (ProjectResult) object;
		update(ProjectResultNSpace.PROJECT_RESULT_UPDA, proBean);
	}
}
