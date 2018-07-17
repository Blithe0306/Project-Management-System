/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.perm.entity.AdminPerm;
import com.ft.otp.manager.admin.perm.service.IAdmPermServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 权限码初始化
 *
 * @Date in Apr 29, 2014,10:07:01 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class OtpPermConfig {

    private Logger logger = Logger.getLogger(OtpPermConfig.class);

    private static Map<String, String> permMap; //所有的权限连接
    private static IAdmPermServ adminPermServ;
    private static OtpPermConfig config = null;

    private OtpPermConfig() {
        permMap = new HashMap<String, String>();
        adminPermServ = (IAdmPermServ) AppContextMgr.getObject("adminPermServ");
        try {
            AdminPerm queryAp = new AdminPerm();
            queryAp.setCurLoginUserRole(StrConstant.SUPER_ADMIN);
            List<?> permList = adminPermServ.query(queryAp, new PageArgument());
            Iterator<?> it = permList.iterator();
            while (it.hasNext()) {
                AdminPerm perm = (AdminPerm) it.next();
                putValue(perm.getPermLink(), perm.getPermCode());
            }

            setPermMap(permMap);
        } catch (BaseException ex) {
            StrTool.print("-->> Load the otpperm Data Info Failure!");
            logger.error(ex.getMessage(), ex);
        }
    }

    public static OtpPermConfig loadOtpPerm() {
        if (config != null) {
            return config;
        }
        synchronized (OtpPermConfig.class) {
            if (config == null) {
                config = new OtpPermConfig();
            }
            return config;
        }
    }

    /**
     * 存放KEY/VALUE 方法说明
     * 
     * @Date in Feb 7, 2012,2:08:03 PM
     * @param key
     * @param object
     */
    public static void putValue(String keys, String value) {
        if (!StrTool.strNotNull(keys)) {
            return;
        }
        if (!StrTool.strNotNull(value)) {
            return;
        }

        String[] keyArr = keys.split(",");
        for (String key : keyArr) {
            permMap.put(key, value);
        }

    }

    /**
     * 根据KEY获取VALUE 方法说明
     * 
     * @Date in Feb 7, 2012,2:08:28 PM
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        if (!StrTool.mapNotNull(permMap)) {
            return null;
        }
        return permMap.get(key);
    }

    /**
     * @return the permMap
     */
    public static Map<String, String> getPermMap() {
        return permMap;
    }

    /**
     * @param permMap the permMap to set
     */
    public static void setPermMap(Map<String, String> permMap) {
        OtpPermConfig.permMap = permMap;
    }

    /**
     * 清空Map，重置config
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void clear() {
        if (null != config) {
            permMap.clear();
            config = null;
        }
    }

}
