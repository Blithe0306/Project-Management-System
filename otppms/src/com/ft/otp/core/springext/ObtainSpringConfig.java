package com.ft.otp.core.springext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * ApplicationContext 实例化
 *
 * @Date in Apr 15, 2011,4:46:18 PM
 *
 * @author TBM
 */
public class ObtainSpringConfig {

    private static final long serialVersionUID = 4754901613002405547L;

    /**
     * 初始化ApplicationContext
     */
    public static void init(ServletContextEvent context)
            throws ServletException {
        ApplicationContext appContext = null;
        appContext = WebApplicationContextUtils
                .getWebApplicationContext(context.getServletContext());
        AppContextMgr.setAppContext(appContext);
    }

    /**
     * 销毁ApplicationContext
     * @Date in Apr 15, 2011,4:47:15 PM
     */
    public static void destroy() {
        ((AbstractApplicationContext) AppContextMgr.getAppContext()).close();
    }
}