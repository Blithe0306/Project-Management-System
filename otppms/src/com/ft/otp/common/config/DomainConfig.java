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
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 域键值对Map
 *
 * @Date in Jan 31, 2013,6:26:18 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class DomainConfig {

    private Logger logger = Logger.getLogger(DomainConfig.class);

    private static DomainConfig config = null;
    private static Map<Integer, String> domainMap;

    //域服务接口
    private IDomainInfoServ domainInfoServ = (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");

    private DomainConfig() {
        domainMap = new HashMap<Integer, String>();

        try {
            List<?> list = domainInfoServ.query(new DomainInfo(), new PageArgument());
            if (StrTool.listNotNull(list)) {
                Iterator<?> iter = list.iterator();
                while (iter.hasNext()) {
                    DomainInfo domainInfo = (DomainInfo) iter.next();
                    domainMap.put(domainInfo.getDomainId(), domainInfo.getDomainName());
                }
            }
        } catch (Exception e) {
            logger.error("Load domain data Failed!", e);
        }
    }

    public static DomainConfig loadDomainConfig() {
        if (config != null) {
            return config;
        }
        synchronized (DomainConfig.class) {
            if (config == null) {
                config = new DomainConfig();
            }
            return config;
        }
    }

    /**
     * 根据key取得Value
     * @Date in Jan 31, 2013,6:37:46 PM
     * @param key
     * @return
     */
    public static String getValue(int key) {
        if (key < 0) {
            return "";
        }
        if (!StrTool.mapNotNull(domainMap)) {
            return "";
        }

        return (String) domainMap.get(key);
    }

    /**
     * 清空域Map
     * @Date in Jan 31, 2013,6:37:00 PM
     */
    public static void clear() {
        if (null != config) {
            domainMap.clear();
            config = null;
        }
    }

    /**
     * 重新加载
     * 
     * @Date in Jun 8, 2013,4:36:07 PM
     */
    public synchronized static void reload() {
        clear();
        loadDomainConfig();

        SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10004);
    }
}
