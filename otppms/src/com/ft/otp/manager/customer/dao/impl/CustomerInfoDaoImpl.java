package com.ft.otp.manager.customer.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.CustomerNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.customer.dao.ICustomerInfoDao;
import com.ft.otp.manager.customer.entity.CustomerInfo;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 客户业务数据接口实现类
 */
public class CustomerInfoDaoImpl extends BaseSqlMapDAO implements ICustomerInfoDao {

	protected String getNameSpace() {
		return CustomerNSpace.CUST_INFO_NS;
	}

	private CustomerInfo getCustomerInfo(Object object) {
		CustomerInfo custInfo = (CustomerInfo) object;
		if (null == custInfo) {
			custInfo = new CustomerInfo();
		}
		return custInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
	 */
	public void addObj(Object object) throws BaseException {
		CustomerInfo custInfo = (CustomerInfo) object;
		insert(CustomerNSpace.CUST_INFO_ADD_UI, custInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
	 */
	public int count(Object object) throws BaseException {
		CustomerInfo custInfo = getCustomerInfo(object);
		return (Integer) queryForObject(CustomerNSpace.CUST_INFO_COT_UI, custInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
	 */
	public void delObj(Object object) throws BaseException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
	 */
	public void delObj(final Set<?> set) throws BaseException {
		// 批量删除用户
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				CustomerInfo custInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = set.iterator();
				while (iter.hasNext()) {
					String customerId = (String) iter.next();
					custInfo = new CustomerInfo();
					custInfo.setId(Integer.parseInt(customerId));

					// 删除客户信息
					delete(CustomerNSpace.CUST_INFO_DEL_UI, custInfo);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
	 */
	public Object find(Object object) throws BaseException {
		CustomerInfo custInfo = (CustomerInfo) object;
		return queryForObject(CustomerNSpace.CUST_INFO_FIND_UI, custInfo);
	}
	public Object findObj(Object object) throws BaseException {
        CustomerInfo custInfo = (CustomerInfo) object;
        return queryForObject(CustomerNSpace.CUST_INFO_SEL_CUST_EQUAL_UI, custInfo);
    }
	
	public Object findMaxCustid(Object object) throws BaseException {
        CustomerInfo custInfo = (CustomerInfo) object;
        return queryForObject(CustomerNSpace.cUST_INFO_SEL_MAX_CUSTID, custInfo);
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm,
	 *      com.ft.otp.common.page.PageArgument)
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		CustomerInfo custInfo = getCustomerInfo(object);
		custInfo.setStartRow(pageArg.getStartRow());
		custInfo.setPageSize(pageArg.getPageSize());
//		return queryForList(CustomerNSpace.CUST_INFO_SEL_UI, custInfo);
		return queryForList(CustomerNSpace.CUST_INFO_SEL_CUST_UI, custInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
	 */
	public void updateObj(Object object) throws BaseException {
		CustomerInfo custInfo = (CustomerInfo) object;
		update(CustomerNSpace.CUST_INFO_UPDA_UI, custInfo);
	}

}
