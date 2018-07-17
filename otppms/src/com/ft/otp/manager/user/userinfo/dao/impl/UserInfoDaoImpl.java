/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.dao.impl;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.orm.ibatis.SqlMapClientCallback;

import com.ft.otp.base.dao.namespace.UserNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.user.userinfo.dao.IUserInfoDao;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;
import com.ibatis.sqlmap.client.SqlMapExecutor;

/**
 * 用户业务数据接口实现类
 * 
 * @Date in Apr 20, 2011,10:58:31 AM
 * 
 * @author TBM
 */
public class UserInfoDaoImpl extends BaseSqlMapDAO implements IUserInfoDao {

	protected String getNameSpace() {
		return UserNSpace.USER_INFO_NS;
	}

	private UserInfo getUserInfo(Object object) {
		UserInfo userInfo = (UserInfo) object;
		if (null == userInfo) {
			userInfo = new UserInfo();
		}
		return userInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#addObj(java.lang.Object)
	 */
	public void addObj(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		insert(UserNSpace.USER_INFO_ADD_UI, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#count(com.ft.otp.base.form.BaseQueryForm)
	 */
	public int count(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return (Integer) queryForObject(UserNSpace.USER_GROUP_COT_UI, userInfo);
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

				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = set.iterator();
				while (iter.hasNext()) {
					String userIdandDomainId = (String) iter.next();
					String userId = userIdandDomainId.split(":")[0];
					userId = userId.replace(StrConstant.common_char2,
							StrConstant.common_char1);
					int domainId = StrTool.parseInt(userIdandDomainId
							.split(":")[1]);
					userInfo = new UserInfo();
					userInfo.setDomainId(domainId);
					userInfo.setUserId(userId);

					// 解绑用户令牌信息，由于添加用户时域ID不能为空，所以domainId一定不会为空；
					delete(UserNSpace.USER_INFO_DEL_UTI, userInfo);

					// 删除用户信息
					delete(UserNSpace.USER_INFO_DEL_UI, userInfo);
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
		UserInfo userInfo = (UserInfo) object;
		return queryForObject(UserNSpace.USER_INFO_FIND_UI, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#query(com.ft.otp.base.form.BaseQueryForm,
	 *      com.ft.otp.common.page.PageArgument)
	 */
	public List<?> query(Object object, PageArgument pageArg)
			throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		userInfo.setStartRow(pageArg.getStartRow());
		userInfo.setPageSize(pageArg.getPageSize());
		if (DbEnv.getDbtype().equals(DbConstant.DB_TYPE_SYBASE)) {
			return queryForList(UserNSpace.USER_INFO_SEL_UI, userInfo, pageArg
					.getStartRow(), pageArg.getPageSize());
		} else {
			return queryForList(UserNSpace.USER_INFO_SEL_UI, userInfo);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.base.dao.IBaseDao#updateObj(java.lang.Object)
	 */
	public void updateObj(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		update(UserNSpace.USER_INFO_UPDA_UI, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#batchUpdateUser(java.util.List)
	 */
	public void batchUpdateUser(final List<?> usrList) throws BaseException {
		// 批量更新用户
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = usrList.iterator();
				while (iter.hasNext()) {
					userInfo = (UserInfo) iter.next();
					update(UserNSpace.USER_INFO_UPDA_UI, userInfo);

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
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#batchimportUser(java.util.List)
	 */
	public void batchimportUser(final List<?> usrList) throws BaseException {
		// 批量导入(添加)用户
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				String defaultPass = ConfConfig
						.getConfValue(ConfConstant.CONF_TYPE_USER + "_"
								+ ConfConstant.DEFAULT_USER_PWD); // 配置中的默认密码
				String localAuth = ConfDataFormat.getConfValue(
						ConfConstant.CONF_TYPE_USER,
						ConfConstant.DEFAULT_LOCALAUTH);//配置中的默认的本地认证模式
				String backendAuth = ConfDataFormat.getConfValue(
						ConfConstant.CONF_TYPE_USER,
						ConfConstant.DEFAULT_BACKENDAUTH);//配置中的默认的后端认证模式

				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = usrList.iterator();
				while (iter.hasNext()) {
					userInfo = (UserInfo) iter.next();
					if (StrTool.strNotNull(userInfo.getPwd())) {
						userInfo.setPwd(userInfo.getPwd());
					} else {
						userInfo.setPwd(PwdEncTool.encPwd(defaultPass));
					}
					if (StrTool.strNotNull(localAuth)) {
						userInfo.setLocalAuth(Integer.parseInt(localAuth));
					}
					if (StrTool.strNotNull(backendAuth)) {
						userInfo.setBackEndAuth(Integer.parseInt(backendAuth));
					}
					//设置导入时间为创建用户时间
					userInfo.setCreateTime(StrTool.timeSecond());
					
					insert(UserNSpace.USER_INFO_ADD_UI, userInfo);

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
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#queryUIUTUG(java.lang.Object)
	 */
	public List<?> queryUIUTUG(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		return queryForList(UserNSpace.USER_INFO_QUERYUI_TI_UG, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#queryUIUTUG(java.lang.Object)
	 */
	public List<?> queryUTUG(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		return queryForList(UserNSpace.USER_INFO_QUERYUI_UG, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#updateUserLost(java.lang.Object,
	 *      int)
	 */
	public void updateUserLost(Object object, int locked) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		userInfo.setLocked(locked);
		update(UserNSpace.USER_INFO_UPDATE_LOCKED, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#setStaticPass(java.lang.Object)
	 */
	public void updateStaticPass(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		update(UserNSpace.USER_INFO_UPDATE_STATICPASS, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#selectUserEmail(java.lang.Object)
	 */
	public List<?> selectUserEmail(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		return queryForList(UserNSpace.USER_INFO_QUERYUI_EMAIL, userInfo);
	}

	/*
	 * 禁用启用用户
	 */
	public void updateUserEnabled(Object object, int enabled)
			throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		userInfo.setEnabled(enabled);
		update(UserNSpace.USER_INFO_UPDATE_ENABLED, userInfo);
	}

	/*
	 * 变更用户组织机构
	 */
	public void updateUserOrgunit(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		update(UserNSpace.USER_INFO_UPDATE_ORGUNIT, userInfo);
	}

	/*
	 * 变更用户组织机构
	 */
	public void updateUserOrgunits(Object object) throws BaseException {
		UserInfo userInfo = (UserInfo) object;
		update(UserNSpace.USER_INFO_UPDATE_ORGUNITS, userInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ft.otp.manager.user.userinfo.dao.IUserInfoDao#batchSetRadId(java.util.List)
	 */
	public void batchSetRadId(final List<?> usrList) throws BaseException {
		// 批量添加Radius配置
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = usrList.iterator();
				while (iter.hasNext()) {
					userInfo = (UserInfo) iter.next();
					if (NumConstant.common_number_0 == userInfo
							.getRadProfileId()) { // 如果不返回
						userInfo.setRadProfileId(null);
					} else {
						userInfo.setRadProfileId(userInfo.getRadProfileId());
					}
					update(UserNSpace.USER_INFO_UPDATE_RADIUS_ID, userInfo);

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
	 * 批量设置后端认证
	 */
	public void batchSetBackendId(final List<?> usrList) throws BaseException {
		// 批量设置后端认证
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = usrList.iterator();
				while (iter.hasNext()) {
					userInfo = (UserInfo) iter.next();
					update(UserNSpace.USER_INFO_UPDATE_BACKEND_ID, userInfo);
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
	 * 批量设置本地认证模式
	 */
	public void batchSetLocalauth(final List<?> usrList) throws BaseException {
		// 批量设置本地认证模式
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = usrList.iterator();
				while (iter.hasNext()) {
					userInfo = (UserInfo) iter.next();
					update(UserNSpace.USER_INFO_UPDATE_LOCALAUTH_ID, userInfo);
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

	/**
	 * 数据统计 支持模糊查询
	 * 
	 * @param queryForm
	 * @return
	 * @throws BaseException
	 */
	public int countUser(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return (Integer) queryForObject(UserNSpace.USER_GROUP_COT_UC, userInfo);
	}

	/**
	 * 数据统计 支持模糊查询
	 * 
	 * @param queryForm
	 * @return
	 * @throws BaseException
	 */
	public int countBind(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return (Integer) queryForObject(UserNSpace.USER_GROUP_COT_BIND,
				userInfo);
	}

	/**
	 * 查找一组数据 支持模糊查询
	 * 
	 * @return
	 * @throws BaseException
	 */
	public List<?> queryUser(Object object, PageArgument pageArg)
			throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		userInfo.setStartRow(pageArg.getStartRow());
		userInfo.setPageSize(pageArg.getPageSize());
		if (DbEnv.getDbtype().equals(DbConstant.DB_TYPE_SYBASE)) {
			return queryForList(UserNSpace.USER_INFO_SEL_UC, userInfo, pageArg
					.getStartRow(), pageArg.getPageSize());
		} else {
			return queryForList(UserNSpace.USER_INFO_SEL_UC, userInfo);
		}
	}

	/**
	 * 查找一组数据 支持模糊查询
	 * 
	 * @return
	 * @throws BaseException
	 */
	public List<?> queryUser(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return queryForList(UserNSpace.USER_INFO_SEL_UO, userInfo);
	}

	/**
	 * 用户绑定令牌 支持模糊查询
	 * 
	 * @return
	 * @throws BaseException
	 */
	public List<?> queryBind(Object object, PageArgument pageArg)
			throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		userInfo.setStartRow(pageArg.getStartRow());
		userInfo.setPageSize(pageArg.getPageSize());
		if (DbEnv.getDbtype().equals(DbConstant.DB_TYPE_SYBASE)) {
			return queryForList(UserNSpace.USER_INFO_SEL_BIND, userInfo,
					pageArg.getStartRow(), pageArg.getPageSize());
		} else {
			return queryForList(UserNSpace.USER_INFO_SEL_BIND, userInfo);
		}
	}

	/**
	 * 用户锁定/解锁
	 * 
	 * @param keys
	 * @param locked
	 * @return
	 * @throws BaseException
	 */
	public boolean userLocked(final Set<?> keys, final int locked)
			throws BaseException {

		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				UserInfo userInfo = null;
				Iterator<?> iter = (Iterator<?>) keys.iterator();
				int batch = 0;
				boolean bool = true;
				executor.startBatch();
				while (iter.hasNext()) {
					String userIdandDomainId = (String) iter.next();
					String userId = userIdandDomainId.split(":")[0];
					userId = userId.replace(StrConstant.common_char2,
							StrConstant.common_char1);
					int domainId = StrTool.parseInt(userIdandDomainId
							.split(":")[1]);
					userInfo = new UserInfo();
					userInfo.setDomainId(domainId);
					userInfo.setUserId(userId);
					userInfo.setLocked(locked);
					if (locked == NumConstant.common_number_2) {
						userInfo.setLoginLockTime(StrTool.timeSecond());
					} else {
						userInfo.setLoginLockTime(0);// 解锁的话 锁定时间设为0
						userInfo.setTempLoginErrCnt(0);
						userInfo.setLongLoginErrCnt(0);
					}
					int k = update(UserNSpace.USER_INFO_UPDATE_LOCKED, userInfo);
					if (k == -1) {
						bool = false;
					}
					batch++;
					if (batch == NumConstant.batchCount) {
						executor.executeBatch();
						batch = 0;
					}
				}
				executor.executeBatch();
				return bool;
			}
		});
		return true;
	}

	/**
	 * 用户禁用/启用
	 * 
	 * @param keys
	 * @param abled
	 * @return
	 * @throws BaseException
	 */
	public boolean userAbled(final Set<?> keys, final int enabled)
			throws BaseException {

		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {

				UserInfo userInfo = null;
				Iterator<?> iter = (Iterator<?>) keys.iterator();
				int batch = 0;
				boolean bool = true;
				executor.startBatch();
				while (iter.hasNext()) {
					String userIdandDomainId = (String) iter.next();
					String userId = userIdandDomainId.split(":")[0];
					userId = userId.replace(StrConstant.common_char2,
							StrConstant.common_char1);
					int domainId = StrTool.parseInt(userIdandDomainId
							.split(":")[1]);
					userInfo = new UserInfo();
					userInfo.setDomainId(domainId);
					userInfo.setUserId(userId);
					userInfo.setEnabled(enabled);
					int k = update(UserNSpace.USER_INFO_UPDATE_ENABLED,
							userInfo);
					if (k == -1) {
						bool = false;
					}
					batch++;
					if (batch == NumConstant.batchCount) {
						executor.executeBatch();
						batch = 0;
					}
				}
				executor.executeBatch();
				return bool;
			}
		});
		return true;
	}

	/**
	 * 查找一组数据
	 * 
	 * @return
	 * @throws BaseException
	 */
	public List<?> selectUser(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return queryForList(UserNSpace.USER_SEL_US, userInfo);
	}
	
	/**
	 * 根据USERID数组查询用户数据，用于短信分发
	 * @return
	 * @throws BaseException
	 */
	public List<?> selectUserToSms(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return queryForList(UserNSpace.USER_INFO_SEL_USER_SMS, userInfo);
	}

	/**
	 * 根据Radius，ID查询
	 */
	public List<?> selectUserToRad(Object object) throws BaseException {
		UserInfo userInfo = getUserInfo(object);
		return queryForList(UserNSpace.USER_SEL_RAD_US, userInfo);
	}

	public void batchDelUser(final List<?> usrList) throws BaseException {
		// 批量删除用户
		getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
			public Object doInSqlMapClient(SqlMapExecutor executor)
					throws SQLException {
				UserInfo userInfo = null;
				int batch = 0;
				executor.startBatch();
				Iterator<?> iter = usrList.iterator();
				while (iter.hasNext()) {
					userInfo = (UserInfo) iter.next();
					// 解绑用户令牌信息，由于添加用户时域ID不能为空，所以domainId一定不会为空；
					delete(UserNSpace.USER_INFO_DEL_UTI, userInfo);

					// 删除用户信息
					delete(UserNSpace.USER_INFO_DEL_UI, userInfo);
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
}
