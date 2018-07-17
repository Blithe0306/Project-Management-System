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
import com.ft.otp.manager.confinfo.radius.dao.IRadProfileDao;
import com.ft.otp.manager.confinfo.radius.entity.RadProfileInfo;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * RADIUS配置实现
 * 
 * @Date in Oct 29, 2012,4:40:22 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadProfileDaoImpl extends BaseSqlMapDAO implements IRadProfileDao {

	protected String getNameSpace() {
		return RadiusNSpace.OTP_RADIUS_PROFILE;
	}

	private RadProfileInfo getRadProfile(Object object) {
		RadProfileInfo radfile = (RadProfileInfo) object;
		if (null == radfile) {
			radfile = new RadProfileInfo();
		}
		return radfile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
	 */
	public void addObj(Object object) throws BaseException {
		RadProfileInfo radfile = getRadProfile(object);
		insert(RadiusNSpace.OTP_INSERT_RADIUS_PROFILE, radfile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#count(java.lang.Object)
	 */
	public int count(Object object) throws BaseException {
		RadProfileInfo radfile = getRadProfile(object);
		return (Integer) queryForObject(RadiusNSpace.OTP_COUNT_RADIUS_PROFILE,
				radfile);
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
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				int batch = 0;
				executor.startBatch();
				RadProfileInfo radProfileInfo = new RadProfileInfo();
				Iterator<?> iter = set.iterator();
				while (iter.hasNext()) {
					String profileId = (String) iter.next();
					radProfileInfo.setProfileId(StrTool.parseInt(profileId));
					delete(RadiusNSpace.OTP_DEL_RADIUS_PROFILE, radProfileInfo);
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
		RadProfileInfo radfile = getRadProfile(object);
		return queryForObject(RadiusNSpace.OTP_FIND_RADIUS_PROFILE, radfile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#query(java.lang.Object,
	 *      com.ft.otp.common.page.PageArgument)
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		RadProfileInfo radfile = getRadProfile(object);
		radfile.setPageSize(pageArg.getPageSize());
		radfile.setStartRow(pageArg.getStartRow());
		return queryForList(RadiusNSpace.OTP_SELECT_RADIUS_PROFILE, radfile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
	 */
	public void updateObj(Object object) throws BaseException {
		RadProfileInfo radfile = getRadProfile(object);
		update(RadiusNSpace.OTP_UPDATE_RADIUS_PROFILE, radfile);
	}

}
