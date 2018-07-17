package com.ft.otp.manager.confinfo.radius.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import com.ft.otp.base.dao.namespace.RadiusNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.radius.dao.IRadAttrDao;
import com.ft.otp.manager.confinfo.radius.entity.RadAttrInfo;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * RADIUS配置属性管理
 * 
 * @Date in Oct 30, 2012,4:10:18 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadAttrDaoImpl extends BaseSqlMapDAO implements IRadAttrDao {

	protected String getNameSpace() {
		return RadiusNSpace.OTP_RADIUS_Attr;
	}

	private RadAttrInfo getRadAttr(Object object) {
		RadAttrInfo attrInfo = (RadAttrInfo) object;
		if (null == attrInfo) {
			attrInfo = new RadAttrInfo();
		}
		return attrInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
	 */
	public void addObj(Object object) throws BaseException {
		RadAttrInfo attrInfo = getRadAttr(object);
		insert(RadiusNSpace.OTP_ADD_RADIUS_ATTR, attrInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
	 */
	public int count(Object object) throws BaseException {
		RadAttrInfo attrInfo = getRadAttr(object);
		return (Integer) queryForObject(RadiusNSpace.OTP_COUNT_RADIUS_ATTR,
				attrInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#delObj(java.lang.Object)
	 */
	public void delObj(Object object) throws BaseException {
		RadAttrInfo attrInfo = getRadAttr(object);
		delete(RadiusNSpace.OTP_DELETE_RADIUS_ATTR, attrInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#delObj(java.util.Set)
	 */
	public void delObj(final Set<?> set) throws BaseException {
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				int batch = 0;
				executor.startBatch();
				RadAttrInfo attrInfo = new RadAttrInfo();
				Iterator<?> iter = set.iterator();
				while (iter.hasNext()) {
					String idStr = (String) iter.next();
					String[] strArr = idStr.split(":");
					attrInfo.setAttrId(strArr[0]);
					attrInfo.setProfileId(StrTool.parseInt(strArr[1]));
					attrInfo.setVendorid(StrTool.parseInt(strArr[2]));
					attrInfo.setFlag(1);
					delete(RadiusNSpace.OTP_DELETE_RADIUS_ATTR, attrInfo);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#find(java.lang.Object)
	 */
	public Object find(Object object) throws BaseException {
		RadAttrInfo attrInfo = getRadAttr(object);
		return queryForObject(RadiusNSpace.OTP_FIND_RADIUS_ATTR, attrInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object,
	 *      com.ft.otp.common.page.PageArgument)
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		RadAttrInfo attrInfo = getRadAttr(object);
		attrInfo.setPageSize(pageArg.getPageSize());
		attrInfo.setStartRow(pageArg.getStartRow());
		return queryForList(RadiusNSpace.OTP_SELECT_RADIUS_ATTR, attrInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
	 */
	public void updateObj(Object object) throws BaseException {
		RadAttrInfo attrInfo = getRadAttr(object);
		update(RadiusNSpace.OTP_UPDATE_RADIUS_ATTR, attrInfo);
	}

}
