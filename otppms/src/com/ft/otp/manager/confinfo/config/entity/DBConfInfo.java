/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.entity;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.util.tool.StrTool;

/**
 * 数据库连接池配置实体对象
 * @Date in Nov 20, 2012,10:06:23 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class DBConfInfo extends BaseEntity {

    private String dbtype; //数据库类型
    private String ip; //数据库主机地址
    private String port; //数据库端口
    private String dbname; //数据库名称
    private String username; //数据库用户名
    private String passwd; //数据库密码

    private String viceip; //副IP，Oracle RAC模式，使用两个IP地址
    private String viceport;//端口2
    private String dual; //是否使用RAC模式，针对Oracle数据库

    private String driver; //临时driver
    private String url; //临时url

    private String passEncrypt; //数据库连接密码是否加密

    private int protaldbconf; //是否更新用户门户数据库连接配置
    private int authdbconf; //是否更新认证服务数据库连接配置

    /**
     * 由配置列表得到DBConfInfo对象
     * @Date in Nov 20, 2012,4:48:39 PM
     * @param configList
     * @return
     */
    public static DBConfInfo getDbConfInfo() {
        DBConfInfo dbConfInfo = new DBConfInfo();
        String dbType = DbConstant.DB_TYPE_MYSQL;
        String dbIp = "127.0.0.1";
        String port = "3306";
        String dbName = "otpdb";
        String viceIp = "127.0.0.1";
        String vicePort = "3306";

        String url = ProxoolConfig.getProperties(DbConstant.DRIVER_URL);
        String user = ProxoolConfig.getProperties(DbConstant.USER);
        String racMode = ProxoolConfig.getProperties(DbConstant.ORACLE_RAC_MODE);

        if (StrTool.strNotNull(url)) {
            if (url.startsWith("jdbc:postgresql")) {
                dbType = DbConstant.DB_TYPE_POSTGRESQL;
            } else if (url.startsWith("jdbc:mysql")) {
                dbType = DbConstant.DB_TYPE_MYSQL;
            } else if (url.startsWith("jdbc:oracle")) {
                dbType = DbConstant.DB_TYPE_ORACLE;
            } else if (url.startsWith("jdbc:jtds:sqlserver")) {
                dbType = DbConstant.DB_TYPE_SQLSERVER;
            }

            String urlStr = "";
            String nameStr = "";
            if (StrTool.strEqualsIgnoreCase(dbType, DbConstant.DB_TYPE_ORACLE)) {//Oracle数据库
                if (StrTool.strEqualsIgnoreCase(racMode, StrConstant.common_y)) {
                    String delimiters = "))(ADDRESS=";
                    String host = "HOST=";
                    String port_0 = "PORT=";
                    String port_1 = ")(PORT";
                    String bracket_1 = "))(";
                    String bracket_2 = ")))";
                    String serviceName = "SERVICE_NAME=";

                    String rac1 = url.substring(0, url.indexOf(delimiters));
                    String rac2 = url.substring(url.indexOf(delimiters), url.length());
                    dbIp = rac1.substring(rac1.indexOf(host) + host.length(), rac1.lastIndexOf(port_1));
                    port = rac1.substring(rac1.indexOf(port_0) + port_0.length(), rac1.length());

                    viceIp = rac2.substring(rac2.indexOf(host) + host.length(), rac2.lastIndexOf(port_1));
                    vicePort = rac2.substring(rac2.indexOf(port_0) + port_0.length(), rac2.lastIndexOf(bracket_1));

                    dbName = rac2.substring(rac2.indexOf(serviceName) + serviceName.length(), rac2
                            .lastIndexOf(bracket_2));
                } else {
                    urlStr = url.substring(url.indexOf(":@") + 2, url.lastIndexOf(":"));
                    if (StrTool.strNotNull(urlStr)) {
                        String[] hostArr = urlStr.split(":");
                        dbIp = hostArr[0];
                        port = hostArr[1];
                    }
                    nameStr = url.substring(url.lastIndexOf(":") + 1);
                    if (StrTool.strNotNull(nameStr)) {
                        dbName = nameStr;
                    }
                }
            } else {//其它数据库
                urlStr = url.substring(url.indexOf("//") + 2, url.lastIndexOf("/"));
                if (StrTool.strNotNull(urlStr)) {
                    String[] hostArr = urlStr.split(":");
                    dbIp = hostArr[0];
                    port = hostArr[1];
                }
                nameStr = url.substring(url.lastIndexOf("/") + 1);
                if (StrTool.strNotNull(nameStr)) {
                    dbName = nameStr;
                }
            }
        }

        dbConfInfo.setDbtype(dbType);
        dbConfInfo.setIp(dbIp);
        dbConfInfo.setViceip(viceIp);
        dbConfInfo.setDual(racMode);
        dbConfInfo.setPort(port);
        dbConfInfo.setViceport(vicePort);
        dbConfInfo.setDbname(dbName);
        dbConfInfo.setUsername(user);

        return dbConfInfo;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getUsername() {
        return StrTool.trim(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return StrTool.trim(passwd);
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getViceip() {
        return viceip;
    }

    public void setViceip(String viceip) {
        this.viceip = viceip;
    }

    /**
     * @return the dual
     */
    public String getDual() {
        return dual;
    }

    /**
     * @param dual the dual to set
     */
    public void setDual(String dual) {
        this.dual = dual;
    }

    /**
     * @return the passEncrypt
     */
    public String getPassEncrypt() {
        return passEncrypt;
    }

    /**
     * @param passEncrypt the passEncrypt to set
     */
    public void setPassEncrypt(String passEncrypt) {
        this.passEncrypt = passEncrypt;
    }

    /**
     * @return the viceport
     */
    public String getViceport() {
        return viceport;
    }

    /**
     * @param viceport the viceport to set
     */
    public void setViceport(String viceport) {
        this.viceport = viceport;
    }

    public int getProtaldbconf() {
        return protaldbconf;
    }

    public void setProtaldbconf(int protaldbconf) {
        this.protaldbconf = protaldbconf;
    }

    public int getAuthdbconf() {
        return authdbconf;
    }

    public void setAuthdbconf(int authdbconf) {
        this.authdbconf = authdbconf;
    }

}
