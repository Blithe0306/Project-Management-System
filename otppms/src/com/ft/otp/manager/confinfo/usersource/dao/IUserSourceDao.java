/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.usersource.dao;

import java.sql.Connection;
import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;

/**
 * 用户配置来源接口说明
 * 
 * @Date in Jun 7, 2011,11:12:19 AM
 * 
 * @author YYF
 */
public interface IUserSourceDao extends IBaseDao {

    public Connection getDbConn(UserSourceInfo databaseInfo) throws BaseException;

    public Connection getDominoConn(UserSourceInfo dominoInfo) throws BaseException;

    void initLdap(LdapInfo ldapInfo, OTPLdap ldap) throws BaseException;

    public boolean testDbConn(UserSourceInfo userSourceInfo) throws BaseException;

    public boolean testLdapConn(LdapInfo ldapInfo) throws BaseException;

    public String getAllTableName(UserSourceInfo databaseInfo) throws BaseException;

    public String queryFieldsByTableName(UserSourceInfo databaseInfo) throws BaseException;

    public List<?> queryUSAttrs(UserSourceInfo userSourceInfo) throws BaseException;

    public void addUSAttr(UserSourceInfo userSourceInfo) throws BaseException;

    public void delUSAttr(UserSourceInfo userSourceInfo) throws BaseException;

}
