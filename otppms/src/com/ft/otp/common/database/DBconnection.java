/**
 *Administrator
 */
package com.ft.otp.common.database;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 测试数据库连接的公共类
 *
 * @Date in Jun 2, 2011,3:16:26 PM
 *
 * @author TBM
 */
public class DBconnection {

    private static Logger logger = Logger.getLogger(DBconnection.class);

    // 管理员服务接口
    private static IAdminUserServ adminUserServ = null;
    // 认证服务器
    private static IServerServ serverServ;

    /**
     * 数据库连接测试方法
     * 
     * @Date in Jun 7, 2011,2:29:27 PM
     * @param driver
     * @param url
     * @param user
     * @param pass
     * @return
     */
    public static int testDBConn(String driver, String url, String user, String pass, boolean mark) {
        Connection conn = null;
        Statement stemt = null;
        int result = -1; // -1 连接失败 0 连接成功 1 连接成功，但执行语句失败（数据库表未创建）
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
            if (null != conn) {
                if (!mark) { // 初始化
                    result = 0;
                } else {
                    result = 1; // 连接成功，但尚未执行查询语句    
                }
                
            } else {
                return result;
            }
            
            if (mark) {
                stemt = conn.createStatement();
                String testSql = "select * from otppms_configinfo where confname = '" + ConfConstant.CORE_TOTP_AUTH_WND
                        + "'";
                stemt.executeQuery(testSql);
                result = 0;
            }
        } catch (Exception e) {
            logger.error("Database Connection Test Failed!", e);
        } finally {
            try {
                if (null != stemt) {
                    stemt.close();
                }
                if (null != conn) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

        return result;
    }

    /**
     * 得到数据库连接对象
     * 
     * @param driver
     * @param url
     * @param user
     * @param pass
     * @return
     */
    public static Connection getConnection(String driver, String url, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return conn;
    }

    /**
     * 执行指定的SQL语句
     * 
     * @Date in Sep 7, 2013,2:23:46 PM
     * @param driver
     * @param url
     * @param user
     * @param pass
     * @param sql
     * @return
     */
    public static boolean executeSql(String driver, String url, String user, String pass, String sql) {
        Connection connection = getConnection(driver, url, user, pass);
        if (null == connection) {
            return false;
        }

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);

            return true;
        } catch (Exception e) {
            logger.error("executeSql Fail:", e);
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }

        return false;
    }

    /**
     * 创建数据库表及初始化表数据
     * @Date in Sep 28, 2011,11:51:50 AM
     * @param prop
     * @param path
     * @return
     */
    public static boolean importDBTable(String driver, String url, String user, String password, String path) {
        if (!StrTool.strNotNull(url)) {
            return false;
        }
        if (!StrTool.strNotNull(driver)) {
            return false;
        }

        Connection conn = null;
        Statement stmt = null;
        String sql = "";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, user, password);

            List<String> sqlList = loadSql(path);
            for (int i = 0; i < sqlList.size(); i++) {
                sql = sqlList.get(i);
                if (i == sqlList.size() - 1) {
                    sql = sql.replace(";", "");
                }
                stmt = conn.createStatement();
                // stmt.addBatch(sql);
                // stmt.executeBatch();
                stmt.executeUpdate(sql);
                stmt.close();
            }
            return true;
        } catch (Exception e) {
            if (path.indexOf("upgrade") != -1) {
                logger.error("DB Upgrade Fail:", e);
            } else {
                logger.error("Create Table Fail:", e);
            }
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }

        return false;
    }
    
    /**
     * 检查是否需要升级表结构
     * 4.2恢复4.0或4.0.1的数据
     * @author LXH
     * @date Dec 2, 2014 1:52:29 PM
     * @param driver
     * @param url
     * @param user
     * @param password
     * @return
     */
    public static boolean checkTableSql(String driver, String url, String user, String password){
    	boolean flag = true;
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement preStmt;
        String sql = "";
        try {
        	Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, user, password);
            
        	sql = "select * from otppms_attrinfo"; // radius属性表
        	preStmt = conn.prepareStatement(sql);
            ResultSet rs = preStmt.executeQuery();
            ResultSetMetaData data = rs.getMetaData();
            
            // 查询radius属性表是否含有vendorid字段，如果有不需要升级表结构
            for (int i = 1; i <= data.getColumnCount(); i++) {
            	String columnName = data.getColumnName(i); // 表列名
            	if(StrTool.strEquals(columnName.toLowerCase(), "vendorid")){
            		flag = false;
            		break;
            	}
            }
        } catch (Exception e) {
        	logger.error("Select Table Fail:", e);
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        return flag;
    }

    /**
     * 加载数据表文件，组织SQL脚本语句
     * 
     * @Date in Apr 7, 2013,9:03:25 PM
     * @param sqlFile
     * @return
     */
    private static List<String> loadSql(String sqlFile) {
        List<String> sqlList = new ArrayList<String>();
        try {
            InputStream sqlFileIn = new FileInputStream(sqlFile);
            StringBuffer sqlSb = new StringBuffer();
            byte[] buff = new byte[1024];
            int byteRead = 0;
            while ((byteRead = sqlFileIn.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead));
            }
            sqlFileIn.close();
            // Windows 下换行是 \r\n, Linux 下是 \n
            String[] sqlArr = sqlSb.toString().split("(;\\s*\\r\\n)|(;\\s*\\n)");
            sqlList = Arrays.asList(sqlArr);
        } catch (Exception ex) {
            logger.error("Load sql js file failed!", ex);
            return null;
        }

        return sqlList;
    }

    /**
     * 创建数据库
     * 
     * @Date in Mar 29, 2012,3:05:00 PM
     * @param driver
     * @param url
     * @param user
     * @param password
     * @param dbname
     * @return
     */
    public static boolean createDatabase(String driver, String url, String user, String password, String dbname) {
        if (!StrTool.strNotNull(url)) {
            return false;
        }
        if (!StrTool.strNotNull(driver)) {
            return false;
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, user, password);
            String sql = "CREATE DATABASE IF NOT EXISTS " + dbname;
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);

            return true;
        } catch (Exception e) {
            logger.error("Create DataBase Fail:", e);
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {

            }
        }

        return false;
    }

    /**
     * 关闭数据库连接
     * 
     * @Date in Mar 27, 2012,2:55:04 PM
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        try {
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {

        }
    }

    /**
     * 查询是否已经存在管理员，如果存在就不需要进行管理员的创建了
     * 
     * @Date in Apr 7, 2013,4:16:30 PM
     * @return
     */
    public static boolean ifCreateAdminUser() {
        try {
            adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");

            int count = adminUserServ.count(new AdminUser());
            if (count > 0) {
                return true;
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

    /**
     * 查询数据库表中的认证服务器IP，进行IP设置页是否显示的判断
     * 
     * 如果为一个IP，127.0.0.1，并且本地存在认证服务器的安装目录，则显示IP设置页；
     * 如果为多个IP，或本地不存在认证服务器的安装目录，则不显示IP设置页。
     * 
     * @Date in Apr 7, 2013,4:16:30 PM
     * @return
     */
    public static boolean ifLocalServerIp() {
        try {
            serverServ = (IServerServ) AppContextMgr.getObject("serverServ");

            ServerInfo serverInfo = new ServerInfo();
            List<?> list = serverServ.query(serverInfo, new PageArgument());
            if (!StrTool.listNotNull(list)) {
                return false;
            }
            int size = list.size();
            String servIp = "";
            if (size == NumConstant.common_number_1) {
                serverInfo = (ServerInfo) list.get(0);
                servIp = serverInfo.getHostipaddr();
                if (StrTool.strEquals(servIp, "127.0.0.1") || StrTool.strEquals(servIp, "localhost")) {//为本地IP
                    return true;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return false;
    }

}
