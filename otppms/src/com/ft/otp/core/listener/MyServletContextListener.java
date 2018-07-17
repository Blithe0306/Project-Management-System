package com.ft.otp.core.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import org.apache.log4j.Logger;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.DepartmentConfig;
import com.ft.otp.common.config.LanguageConfig;
import com.ft.otp.common.config.LicenseConfig;
import com.ft.otp.common.config.PermConfig;
import com.ft.otp.common.config.PrjinfoTypeConfig;
import com.ft.otp.common.config.ProjectTypeConfig;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.config.RadiusConfig;
import com.ft.otp.common.config.RunLogConfig;
import com.ft.otp.common.config.SystemConfig;
import com.ft.otp.common.config.VendorConfig;
import com.ft.otp.common.database.DBconnection;
import com.ft.otp.common.database.DbFactory;
import com.ft.otp.core.springext.ObtainSpringConfig;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 自定义系统初始化监听器类
 *
 * @Date in Apr 6, 2011,10:36:09 AM
 *
 * @author TBM
 */
public class MyServletContextListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(MyServletContextListener.class);

    /**
     * 销毁监听处理
     */
    public void contextDestroyed(ServletContextEvent context) {
        //销毁proxool数据库连接池
        DbFactory.destroyed();

        ObtainSpringConfig.destroy();

        SystemConfig.clear();//管理系统配置文件销毁
        RadiusConfig.clear();//Radius配置属性销毁
        VendorConfig.clear();//多厂商实体信息配置销毁

        OnlineUsers.clearSessions();//清除在线用户Map
        PermConfig.clear(); //清除权限Map

        InitializationLoad.destroyed();//销毁其它配置
    }

    /**
     * 加载监听处理
     */
    public void contextInitialized(ServletContextEvent context) {
        // 先设置全局静态路径的根目录
        String basePath = context.getServletContext().getRealPath("/");
        Constant.setBasePath(basePath);

        //加载管理系统配置文件
        SystemConfig.loadSystemConf();

        //加载系统运行日志
        RunLogConfig.loadRunLogConfig();

        //实例化Spring ApplicationContext
        try {
            ObtainSpringConfig.init(context);
            logger.info("Spring ApplicationContext loading successfully!");

        } catch (ServletException ex) {
            logger.info("Spring ApplicationContext loading failure!", ex);
        }

        //加载Radius配置属性
        RadiusConfig.loadRadiusConfig();

        //加载多厂商实体信息配置
        VendorConfig.loadVendorConfig();
        DepartmentConfig.loadDeptConfig();
        //加载定制项目类型配置
        ProjectTypeConfig.loadTypeConfig();
        //加载定制信息归类配置
        PrjinfoTypeConfig.loadTypeConfig();
        

        //加载多语言资源文件
        LanguageConfig.loadLanguage();
        
        //系统初始化加载
        boolean dbInit = InitializationLoad.configLoad();

        boolean licInit = LicenseConfig.getLoadResult() == 0 ? true : false;
        boolean superManExist = false;
        boolean existLocalIp = false;
        boolean setEmailServ = true;
        boolean dbSrcType = ConfDataFormat.getDbDataSrcType();//初始化数据来源方式
        if (dbInit) {
            superManExist = DBconnection.ifCreateAdminUser();
            existLocalIp = DBconnection.ifLocalServerIp();
            if (existLocalIp) {
                existLocalIp = StrTool.verifyFilePath(basePath);
            }

            boolean ifDefEmailServ = PubConfConfig.getDefEmail();
            boolean mailEnable = ConfDataFormat.getSysConfEmailEnabled();
            if (ifDefEmailServ && mailEnable) {
                setEmailServ = false;
            }
        }

        if (dbSrcType) {
            licInit = true;
            superManExist = true;
            setEmailServ = true;
            existLocalIp = false;
        }

        ServletContext servletContext = context.getServletContext();
        servletContext.setAttribute(Constant.DATABASE_IF_CONN, dbInit);
        servletContext.setAttribute(Constant.LICENCE_IF_EFFECTIVE, licInit);
        servletContext.setAttribute(Constant.DATABASE_IF_SUPERMAN, superManExist);
        servletContext.setAttribute(Constant.DATABASE_IF_LOCALIP, existLocalIp);
        servletContext.setAttribute(Constant.EMAILSERVER_IF_CONF, setEmailServ);
    }

}