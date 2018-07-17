package com.ft.otp.core.springext;

import org.springframework.context.ApplicationContext;

/**
 * 
 * 必要时方便直接获取Spring的BEAN
 *
 * @Date in Apr 2, 2011,6:52:03 PM
 *
 * @author TBM
 */
public class AppContextMgr {

    private static ApplicationContext appContext = null;

    public static void setAppContext(ApplicationContext context) {
        appContext = context;
    }

    public static ApplicationContext getAppContext() {
        return appContext;
    }

    public static String getWebAppRoot() {
        return System.getProperty("otp.root");
    }

    public static Object getObject(String beanId) {
        if (appContext == null) {
            return null;
        }
        return appContext.getBean(beanId);
    }

}
