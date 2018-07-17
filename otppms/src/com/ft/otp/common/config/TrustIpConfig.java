/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 访问信任IP初始化配置
 *
 * @Date in Aug 15, 2013,4:57:49 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class TrustIpConfig {

    private Logger logger = Logger.getLogger(TrustIpConfig.class);
    private static TrustIpConfig config = null;
    public static Map<Integer, Object> trustipMap;

    private IAccessConServ accessConServ = (IAccessConServ) AppContextMgr.getObject("accessConServ");

    /**
     * 封装信任IP MAP
     */
    private TrustIpConfig() {
        trustipMap = new HashMap<Integer, Object>();
        try {
            AccessInfo aInfo = new AccessInfo();
            aInfo.setSystype(0);
            List<?> accesslist = accessConServ.query(aInfo, new PageArgument());
            if (StrTool.listNotNull(accesslist)) {
                Iterator<?> iter = accesslist.iterator();
                while (iter.hasNext()) {
                    AccessInfo accessInfo = (AccessInfo) iter.next();
                    trustipMap.put(accessInfo.getId(), accessInfo);
                }
            }
        } catch (Exception e) {
            logger.error("Load TrustIpConfig Failed!", e);
        }
    }

    public static TrustIpConfig loadTrustIpConfig() {
        if (config != null) {
            return config;
        }
        synchronized (TrustIpConfig.class) {
            if (config == null) {
                config = new TrustIpConfig();
            }
            return config;
        }
    }

    /**
     * 根据key值获取对象
     * @Date in Aug 15, 2013,5:07:42 PM
     * @param key
     * @return
     */
    public static AccessInfo getValue(int key) {
        if (key < 0) {
            return null;
        }
        if (!StrTool.mapNotNull(trustipMap)) {
            return null;
        }
        return (AccessInfo) trustipMap.get(key);
    }

    /**
     * 清空Map对象
     * @Date in Aug 15, 2013,5:08:21 PM
     */
    public static void clear() {
        if (null != config) {
            trustipMap.clear();
            config = null;
        }
    }

    /**
     * 重新加载
     * @Date in Aug 15, 2013,5:09:19 PM
     */
    public synchronized static void reload() {
        clear();
        loadTrustIpConfig();
    }

}
