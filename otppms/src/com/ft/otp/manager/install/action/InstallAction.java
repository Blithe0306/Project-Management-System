/**
 *Administrator
 */
package com.ft.otp.manager.install.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.DbConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.common.database.DBconnection;
import com.ft.otp.common.database.DbFactory;
import com.ft.otp.common.database.pool.DbEnv;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.WebServiceFactory;
import com.ft.otp.core.listener.InitializationLoad;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.core.springext.SqlMapClientFactoryBean;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.entity.DBConfInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.alg.AESUtil;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.properties.BaseProperties;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统初始化业务处理Action
 * 
 * @Date in Sep 27, 2011,4:53:45 PM
 * 
 * @author TBM
 */
public class InstallAction extends BaseAction {

    private static final long serialVersionUID = 6317544711481700925L;
    private Logger logger = Logger.getLogger(InstallAction.class);

    public static String IF_RELOAD_SERV = ""; //服务是否已经重启

    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");

    private IRoleInfoServ roleInfoServ = (IRoleInfoServ) AppContextMgr.getObject("roleInfoServ");

    private DBConfInfo dbConfInfo;//数据库连接属性实体对象

    /**
     * @return the dbConfInfo
     */
    public DBConfInfo getDbConfInfo() {
        return dbConfInfo;
    }

    /**
     * @param dbConfInfo the dbConfInfo to set
     */
    public void setDbConfInfo(DBConfInfo dbConfInfo) {
        this.dbConfInfo = dbConfInfo;
    }

    /**
     * 查找数据库配置信息
     * 
     * @Date in Apr 5, 2013,6:12:16 PM
     * @return
     */
    public String find() {
        try {
            dbConfInfo = DBConfInfo.getDbConfInfo();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            dbConfInfo = new DBConfInfo();
        }

        return "toDbPage";
    }

    /**
     * 测试数据库连接
     * 
     * @Date in Mar 27, 2012,4:01:50 PM
     * @return
     * @throws BaseException
     */
    public String testConn() {
        boolean result = false;
        try {
            result = dbCoon(false); //数据库连接成功
            if (result) {
                result = dbCoon(true); //数据库中是否已经创建表成功
                if (!result) {
                    //连接成功，提示创建数据库表
                    setResponseWrite(StrConstant.common_number_2);
                } else {
                    //连接成功，并且不需要创建数据库表
                    setResponseWrite(StrConstant.common_number_0);
                }

                IF_RELOAD_SERV = "" + StrTool.timeSecond();
            } else {
                //数据库连接失败
                setResponseWrite(StrConstant.common_number_1);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setResponseWrite(StrConstant.common_number_1);
        }

        return null;
    }

    /**
     * 创建数据库表结构，插入初始化数据
     * 
     * @Date in Sep 28, 2011,3:43:42 PM
     * @return
     */
    public String importDB() {
        boolean result = false;
        try {
            result = dbCoon(false); //数据库连接成功
            if (!result) {
                //数据库连接失败
                setResponseWrite(StrConstant.common_number_1);
                return null;
            }

            result = dbCoon(true); //数据库中是否已经创建表成功
            if (result) {
                //连接成功，并且不需要创建数据库表
                setResponseWrite(StrConstant.common_number_0);
                return null;
            }

            String path = Constant.WEB_SQLCONFIG_PATH;
            String tablePath = path + sqlJsStr(dbConfInfo.getDbtype());
            String initPath = path + "otpdb_v4_init.sql";

            File tableFile = new File(tablePath);
            File initFile = new File(initPath);
            if (!tableFile.exists() || !initFile.exists()) {//未找到对应的SQL脚本文件！
                setResponseWrite(StrConstant.common_number_2);
                return null;
            }

            String driver = dbConfInfo.getDriver();
            String url = dbConfInfo.getUrl();
            String userName = dbConfInfo.getUsername();
            String passwd = dbConfInfo.getPasswd();
            //创建数据库表结构
            result = DBconnection.importDBTable(driver, url, userName, passwd, tablePath);
            if (!result) {
                setResponseWrite(StrConstant.common_number_3);
                return null;
            }
            //插入初始化化数据
            result = DBconnection.importDBTable(driver, url, userName, passwd, initPath);
            if (result) {
                setResponseWrite(StrConstant.common_number_0);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        setResponseWrite(StrConstant.common_number_3);
        return null;
    }

    /**
     * 保存数据库连接信息
     * 
     * @Date in Mar 31, 2012,3:28:12 PM
     * @return
     * @throws BaseException 
     */
    public String saveDbConf() {
        boolean result = false;
        String selType = request.getParameter("selType");

        if (!dbCoon(true)) {
            setResponseWrite(StrConstant.common_number_1);
            return null;
        }

        //更新管理中心数据库配置文件信息
        String dbConfPath = Constant.WEB_CONFIG_PATH + Constant.DB_CONF_CONF;
        result = DbEnv.dbConfToProperties(dbConfInfo, dbConfPath);
        if (!result) {
            setResponseWrite(StrConstant.common_number_2);
            return null;
        }

        String dbConf = "";
        //更新用户门户数据库配置文件信息
        if (dbConfInfo.getProtaldbconf() == NumConstant.common_number_1) {
            File file = new File(Constant.WEB_APP_PATH);
            String portalPath = file.getParentFile().getPath() + Constant.PORTAL_NAME;

            dbConf = portalPath + Constant.DB_CONF_FILE;
            DbEnv.dbConfToProperties(dbConfInfo, dbConf);
        }

        //更新认证服务数据库配置文件信息
        if (dbConfInfo.getAuthdbconf() == NumConstant.common_number_1) {
            String basePath = request.getSession().getServletContext().getRealPath("/");
            String serverPath = "";
            if (basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE) != -1) {
                serverPath = basePath.substring(0, basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE));
                serverPath += Constant.ROOT_OTP_AUTH_SERVICE;
            } else if (basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE_L) != -1) {
                serverPath = basePath.substring(0, basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE_L));
                serverPath += Constant.ROOT_OTP_AUTH_SERVICE_L;
            }

            dbConf = serverPath + Constant.DB_CONF_FILE;
            DbEnv.dbConfToProperties(dbConfInfo, dbConf);
        }
        setConfigVal(selType);

        return null;
    }

    /**
     * 设置系统使用的语言和数据产生方式
     * 
     * @Date in Apr 28, 2013,10:55:17 AM
     */
    private void setConfigVal(String dbDataSel) {
        if (!StrTool.strNotNull(IF_RELOAD_SERV)) {
            return;
        }

        String currLang = (String) getSession().getAttribute(Constant.LANGUAGE_SESSION_KEY);
        String dbSql = "update otppms_configinfo set confvalue='" + dbDataSel
                + "' where confname='db_data_create_type' and conftype='common'";
        String langSql = "update otppms_configinfo set confvalue='" + currLang
                + "' where confname='default_system_language' and conftype='common'";
        String timeSql = "update otppms_roleinfo set createtime=" + StrTool.timeSecond() + " where rolemark='ADMIN'";

        String driver = dbConfInfo.getDriver();
        String url = dbConfInfo.getUrl();
        String userName = dbConfInfo.getUsername();
        String passwd = dbConfInfo.getPasswd();
        if (StrTool.strEquals(dbDataSel, StrConstant.common_number_1)) {
            DBconnection.executeSql(driver, url, userName, passwd, dbSql);
        }
        DBconnection.executeSql(driver, url, userName, passwd, langSql);
        DBconnection.executeSql(driver, url, userName, passwd, timeSql);
    }

    /**
     * 获取服务器是否重启
     * 
     * @Date in Sep 7, 2013,5:31:09 PM
     * @return
     */
    public String getServState() {
        setResponseWrite(IF_RELOAD_SERV);

        return null;
    }

    /**
     * 重新加载系统配置
     * 
     * @Date in Apr 7, 2013,3:59:14 PM
     * @return
     * @throws Exception
     */
    public boolean reloadSystemConf() throws Exception {
        //刷新连接池配置
        ProxoolConfig.destroyed();
        InitializationLoad.destroyed();//销毁其它配置

        ProxoolConfig.loadDBConfig();
        ServletContext context = getSession().getServletContext();
        //重新加载Spring配置            
        ContextLoader contextLoader = new ContextLoader();
        contextLoader.closeWebApplicationContext(context);
        contextLoader.initWebApplicationContext(context);

        //重新初始化连接池
        DbFactory dbFactory = new DbFactory();
        String[] poolNames = ProxoolFacade.getAliases();
        if (StrTool.arrNotNull(poolNames)) {
            DbFactory.destroyed();
        }
        try {
            dbFactory.init();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        //重新加载SQLMAP
        SqlMapClientFactoryBean factoryBean = new SqlMapClientFactoryBean();
        ApplicationContext appContext = (WebApplicationContext) context
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        Resource resource = appContext.getResource("classpath:ibatis/sqlMapConfig.xml");
        factoryBean.setConfigLocation(resource);
        factoryBean.setDataSource(dbFactory);
        factoryBean.afterPropertiesSet();

        //重新加载系统配置、权限配置
        return InitializationLoad.configLoad();
    }

    /**
     * 系统初始化完成进行相关配置的更新
     * 
     * @Date in May 15, 2013,4:45:20 PM
     * @return
     */
    public String finish() {
        initSeedEncKey();

        addTrustIp();

        setDefaultDbSrcType();

        setDefaultLang();

        updateCreateTime();

        ConfConfig.reLoad();

        WebServiceFactory.loadWebServiceFactory();

        return "toFinish";
    }

    /**
     * 令牌种子加解密密钥初始化
     * 
     * @Date in May 2, 2013,1:50:00 PM
     * @return
     */
    private void initSeedEncKey() {
        try {
            ConfigInfo configInfo = new ConfigInfo();
            configInfo.setConftype(ConfConstant.CONF_TYPE_COMMON);
            configInfo.setConfname(ConfConstant.SEED_PRIVATE_KEY_RANDOM);

            configInfo = (ConfigInfo) confInfoServ.find(configInfo);
            if (null != configInfo && StrTool.strNotNull(configInfo.getConfvalue())) {//密钥已经存在
                return;
            }

            byte[] key = AESUtil.genRandomEncKey(16);
            String keyStr = AlgHelper.bytesToHexs(key);
            ConfigInfo seedKeyConf = new ConfigInfo(ConfConstant.SEED_PRIVATE_KEY_RANDOM, keyStr,
                    ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, Language.getLangStr(request,
                            "sys_install_decry_key_tkn_seed"));
            confInfoServ.updateObj(seedKeyConf);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 添加本地IP等信息到服务器信任IP列表
     * 
     * @Date in Apr 7, 2013,4:05:25 PM
     */
    private void addTrustIp() {
        IpTool.addTrustIp();
    }

    /**
     * 返回数据库连接测试结果
     * 
     * @Date in Mar 31, 2012,3:01:56 PM
     * @return
     */
    private boolean dbCoon(boolean mark) {
        if (!addDiver()) {
            return false;
        }

        //测试连接是否成功
        int result = DBconnection.testDBConn(dbConfInfo.getDriver(), dbConfInfo.getUrl(), dbConfInfo.getUsername(),
                dbConfInfo.getPasswd(), mark);

        return result == 0;
    }

    /**
     * 设置数据库连接信息到实体对象
     * 
     * @Date in Sep 27, 2011,5:24:11 PM
     * @param dbtype
     * @param dbip
     * @param dbport
     * @param dbname
     * @param dbuser
     * @param dbpassword
     * @return
     */
    private boolean addDiver() {
        String dbType = dbConfInfo.getDbtype();
        String ip = dbConfInfo.getIp();
        String ip2 = dbConfInfo.getViceip();
        String port = dbConfInfo.getPort();
        String dbName = dbConfInfo.getDbname();

        dbType = DbEnv.getDbTypeNum(dbType, dbConfInfo.getDual());
        String[] retArr = DbEnv.getDbDriverUrl(dbType, ip, ip2, port, dbName);
        if (!StrTool.strNotNull(retArr[0]) || !StrTool.strNotNull(retArr[1])) {
            return false;
        }

        dbConfInfo.setDriver(retArr[0]);
        dbConfInfo.setUrl(retArr[1]);

        return true;
    }

    /**
     * 获得要导入的DBSQL文件名
     * 
     * @Date in Sep 28, 2011,11:45:33 AM
     * @param dbtype
     * @return
     */
    public String sqlJsStr(String dbtype) {
        String sqlFile = "";
        if (DbConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dbtype)) {
            sqlFile = "otpdb_v4_mysql.sql";
        } else if (DbConstant.DB_TYPE_POSTGRESQL.equalsIgnoreCase(dbtype)) {
            sqlFile = "otpdb_v4_postgresql.sql";
        } else if (DbConstant.DB_TYPE_ORACLE.equalsIgnoreCase(dbtype)) {
            sqlFile = "otpdb_v4_oracle.sql";
        } else if (DbConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(dbtype)) {
            sqlFile = "otpdb_v4_sqlserver.sql";
        }

        return sqlFile;
    }

    /**
     * 多语言初始化设置
     * 
     * @Date in Mar 16, 2012,3:49:04 PM
     * @return
     */
    public String initLanguage() {
        String currLang = request.getParameter("currLang");
        setLanguage(currLang);

        return null;
    }

    /**
     * 设置当前语言
     * 
     * @Date in Mar 16, 2012,3:48:44 PM
     * @param currLang
     */
    public void setLanguage(String currLang) {
        if (!StrTool.strNotNull(currLang)) {
            currLang = StrTool.systemLanguage();
        }

        getSession().setAttribute(Constant.LANGUAGE_SESSION_KEY, currLang);
    }

    /**
     * 判断用户门户安装目录是否存在
     * 
     * @Date in May 23, 2013,8:13:34 AM
     * @return
     */
    public String porAPPIsExists() {
        try {
            File file = new File(Constant.WEB_APP_PATH);
            String portalPath = file.getParentFile().getPath() + Constant.PORTAL_NAME;
            File portalFile = new File(portalPath);

            //返回0：目录不存在，1：存在
            if (portalFile.exists()) {
                setResponseWrite(StrConstant.common_number_1);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        setResponseWrite(StrConstant.common_number_0);
        return null;
    }

    /**
     * 判断认证服务安装目录是否存在
     * 
     * @Date in May 23, 2013,8:14:00 AM
     * @return
     */
    public String authSerIsExists() {
        String basePath = request.getSession().getServletContext().getRealPath("/");

        boolean flag = StrTool.verifyFilePath(basePath);
        if (flag) {
            setResponseWrite(StrConstant.common_number_1);
        } else {
            setResponseWrite(StrConstant.common_number_0);
        }

        return null;
    }

    private void setDefaultDbSrcType() {
        try {
            ConfigInfo dbSrcConf = new ConfigInfo(ConfConstant.DB_DATA_CREATE_TYPE, "0", ConfConstant.CONF_TYPE_COMMON,
                    NumConstant.common_number_0, "");
            confInfoServ.updateObj(dbSrcConf);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void setDefaultLang() {
        String dbConfLang = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.DEFAULT_SYSTEM_LANGUAGE);
        if (StrTool.strNotNull(dbConfLang)) {
            return;
        }
        String currLang = (String) getSession().getAttribute(Constant.LANGUAGE_SESSION_KEY);
        if (!StrTool.strNotNull(currLang)) {
            currLang = Constant.DEFAULT_LANGUAGE;
        }
        try {
            ConfigInfo langConf = new ConfigInfo(ConfConstant.DEFAULT_SYSTEM_LANGUAGE, currLang,
                    ConfConstant.CONF_TYPE_COMMON, NumConstant.common_number_0, "");
            confInfoServ.updateObj(langConf);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void updateCreateTime() {
        RoleInfo roleInfo = new RoleInfo();
        List<?> lists = null;
        try {
            lists = roleInfoServ.query(roleInfo, new PageArgument());
            if (!StrTool.listNotNull(lists)) {
                return;
            }
            roleInfo = (RoleInfo) lists.get(0);
            roleInfo.setCreatetime(StrTool.timeSecond());

            roleInfoServ.updateObj(roleInfo);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    /**
     * 数据库备份返回
     * @author LXH
     * @date Mar 3, 2014 11:41:41 AM
     */
    public void dbBackInitSel() {
        String filePath = Constant.WEB_CONFIG_PATH + Constant.DB_CONF_CONF;
        //连接池配置信息写入文件
        Properties bps = new BaseProperties();
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(filePath);
            bps.load(iStream);

            String dbSql = "update otppms_configinfo set confvalue='0' where confname='db_data_create_type' and conftype='common'";

            String driver = "";
            String url = "";
            String dbType = bps.getProperty(Constant.DB_TYPE);
            String dbIp = bps.getProperty(Constant.DB_IP);
            String dbPort = bps.getProperty(Constant.DB_PORT);
            String dbName = bps.getProperty(Constant.DB_NAME);

            //获取Driver和Url
            String[] dbArr = DbEnv.getDbDriverUrl(dbType, dbIp, null, dbPort, dbName);
            driver = dbArr[0];
            url = dbArr[1];

            String dbUser = bps.getProperty(Constant.DB_USERNAME);
            String dbPwd = bps.getProperty(Constant.DB_PASSWORD);
            String pwdEnc = bps.getProperty(Constant.DB_PWD_ENCRYPT);
            if (StrTool.strEqualsIgnoreCase(pwdEnc, StrConstant.common_yes)) {
                //解密数据库连接密码
                byte[] bKey = DbEnv.genPwdEncKey();
                String decPwd = PwdEncTool.decDbPasswd(dbPwd, bKey);
                if (StrTool.strNotNull(decPwd)) {
                    dbPwd = decPwd;
                }
            }

            DBconnection.executeSql(driver, url, dbUser, dbPwd, dbSql);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {

            try {
                if (null != iStream) {
                    iStream.close();
                }
            } catch (Exception ex) {
            }
        }

        getSession().getServletContext().setAttribute(Constant.DATABASE_IF_CONN, false);
        Constant.DATABASE_CONN = 0;
    }
}
