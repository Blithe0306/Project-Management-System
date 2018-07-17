/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.usersource.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ft.otp.base.dao.namespace.UserSourceNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.database.DBconnection;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.ibatisext.BaseSqlMapDAO;
import com.ft.otp.manager.confinfo.usersource.dao.IUserSourceDao;
import com.ft.otp.manager.confinfo.usersource.dao.LdapInfo;
import com.ft.otp.manager.confinfo.usersource.dao.OTPLdap;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户配置来源接口实现
 * 
 * @Date in Jun 7, 2011,11:13:49 AM
 * 
 * @author YYF
 */
public class UserSourceDaoImpl extends BaseSqlMapDAO implements IUserSourceDao {

    protected String getNameSpace() {
        return UserSourceNSpace.USERSOURCE_INFO_NS;
    }

    private UserSourceInfo getUserSourceInfo(Object object) {
        UserSourceInfo userSourceInfo = (UserSourceInfo) object;
        if (userSourceInfo == null) {
            userSourceInfo = new UserSourceInfo();
        }
        return userSourceInfo;
    }

    public void addObj(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = getUserSourceInfo(object);
        insert(UserSourceNSpace.INSERT_USERSOURCE_INFO, userSourceInfo);
    }

    public int count(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = getUserSourceInfo(object);
        return (Integer) queryForObject(UserSourceNSpace.COUNT_USERSOURCE_INFO, userSourceInfo);
    }

    public void delObj(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = getUserSourceInfo(object);
        delete(UserSourceNSpace.DEL_USERSOURCE_INFO, userSourceInfo);
    }

    public void addUSAttr(UserSourceInfo userSourceInfo) throws BaseException {
        insert(UserSourceNSpace.INSERT_USERSOURCE_ATTR_INFO, userSourceInfo);
    }

    public void delObj(final Set<?> set) throws BaseException {

    }

    public Object find(Object object) throws BaseException {
        UserSourceInfo userSourceInfo = getUserSourceInfo(object);
        return queryForObject(UserSourceNSpace.FIND_USERSOURCE_INFO, userSourceInfo);

    }

    public List<?> query(Object object, PageArgument pageArg) throws BaseException {
        UserSourceInfo userSourceInfo = getUserSourceInfo(object);
        return queryForList(UserSourceNSpace.QUERY_USERSOURCE_INFO, userSourceInfo, pageArg.getStartRow(), pageArg
                .getPageSize());

    }

    public void updateObj(final Object object) throws BaseException {
        UserSourceInfo userSourceInfo = getUserSourceInfo(object);
        //修改用户来源主数据
        update(UserSourceNSpace.UPDATE_USERSOURCE_INFO, userSourceInfo);
    }

    public void delUSAttr(UserSourceInfo userSourceInfo) throws BaseException {
        delete(UserSourceNSpace.DEL_USERSOURCE_ATTR_INFO, userSourceInfo);
    }

    /**
     * 测试数据库连接
     */
    public boolean testDbConn(UserSourceInfo userSourceInfo) throws BaseException {
        boolean result = false;
        Connection conn = null;
        try {
            conn = getDbConn(userSourceInfo);
            if (null != conn) {
                result = true;
            }
        } catch (Exception e) {
            return result;
        } finally {
            DBconnection.closeConnection(conn);
        }

        return result;
    }

    //数据库
    public Connection getDbConn(UserSourceInfo databaseInfo) {
        int type = databaseInfo.getDbtype();
        String ip = databaseInfo.getServeraddr();
        String port = StrTool.intToString(databaseInfo.getPort());
        String dbname = databaseInfo.getDbname();
        String user = databaseInfo.getUsername();
        String password = databaseInfo.getPwd();

        String driver = "";
        String url = "";
        if (type == NumConstant.common_number_1) { //oracle(1) 
            driver = "oracle.jdbc.driver.OracleDriver";
            url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbname;
        } else if (type == NumConstant.common_number_3) {//sqlserver
            driver = "net.sourceforge.jtds.jdbc.Driver";
            url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + dbname;
        } else if (type == NumConstant.common_number_0) {//mysql
            driver = "com.mysql.jdbc.Driver";
            url = "jdbc:mysql://" + ip + ":" + port + "/" + dbname;
        } else if (type == NumConstant.common_number_2) {//postgresql
            driver = "org.postgresql.Driver";
            url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbname;
        }

        return DBconnection.getConnection(driver, url, user, password);
    }

    //domain
    public Connection getDominoConn(UserSourceInfo dominoInfo) throws BaseException {
        return null;
    }

    public void initLdap(LdapInfo ldapInfo, OTPLdap ldap) throws BaseException {

    }

    /**
     * LDAP连接测试
     */
    public boolean testLdapConn(LdapInfo ldapInfo) throws BaseException {
        boolean res = false;
        try {
            OTPLdap ldap = new OTPLdap();
            res = ldap.initConn(ldapInfo);
            ldap.unInit();
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            return res;
        }

        return res;
    }

    public List<String> getAccount_attr(LdapInfo ldapInfo) throws BaseException {
        OTPLdap ldap = new OTPLdap();
        initLdap(ldapInfo, ldap);
        List<String> attrList = ldap.searchAccountAttr(ldapInfo.getLdapDn(), ldapInfo.getFilter());
        ldap.unInit();

        return attrList;
    }

    /**
     * 得到所有表名
     * 
     * @param databaseInfo
     * @return
     * @throws BaseException
     */
    public String getAllTableName(UserSourceInfo databaseInfo) throws BaseException {
        JSONArray jsonArray = new JSONArray();
        Connection conn = null;
        String result = "";
        try {
            conn = getDbConn(databaseInfo);
            if (null == conn) {
                result = "connfailed";
            } else {
                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet rest = dbmd.getTables(conn.getCatalog(), null, null, new String[] { "TABLE" });
                // 输出 table_name
                while (rest.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("dbtablename", rest.getString("TABLE_NAME"));
                    jsonArray.add(jsonObject);
                }
                result = jsonArray.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBconnection.closeConnection(conn);
        }

        return result;
    }

    /**
     * 根据表名得到字段信息列表
     */
    public String queryFieldsByTableName(UserSourceInfo databaseInfo) throws BaseException {
        JSONArray jsonArray = new JSONArray();
        String result = "";
        Connection conn = null;
        try {
            conn = getDbConn(databaseInfo);
            if (null == conn) {
                result = "connfailed";
            } else {
                String tableName = databaseInfo.getDbtablename();
                if (StrTool.strNotNull(tableName)) {
                    ResultSet rs = conn.createStatement().executeQuery("select * from " + tableName);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsmd.getColumnName(i);
                        int columnDisplaySize = rsmd.getColumnDisplaySize(i);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("columnName", columnName);
                        jsonObject.put("columnDisplaySize", columnDisplaySize);
                        jsonArray.add(jsonObject);
                    }
                    result = jsonArray.toString();
                }
            }
        } catch (Exception e) {
            return "connfailed";
        } finally {
            DBconnection.closeConnection(conn);
        }

        return result;
    }

    /**
     * 用户属性
     * @Date in Dec 22, 2011,1:19:21 PM
     * @param userInfo
     * @param property
     * @param value
     */
    public static void setUserInfoByProperty(UserInfo userInfo, String property, String value) {
        if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_USERID)) {
            userInfo.setUserId(value);
        } else if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_REALNAME)) {
            userInfo.setRealName(value);
        } else if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_PIN)) {
            userInfo.setPwd(value);
        } else if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_EMAIL)) {
            userInfo.setEmail(value);
        } else if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_ADDRESS)) {
            userInfo.setAddress(value);
        } else if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_TEL)) {
            userInfo.setTel(value);
        } else if (property.equalsIgnoreCase(ConfConstant.USER_SOURCE_CELLPHONE)) {
            userInfo.setCellPhone(value);
        }

    }

    public List<?> queryUSAttrs(UserSourceInfo userSourceInfo) throws BaseException {
        return queryForList(UserSourceNSpace.QUERY_USERSOURCE_ATTR_INFO, userSourceInfo);
    }

}
