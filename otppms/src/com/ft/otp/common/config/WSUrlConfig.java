/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * WebService请求地址初始化
 *
 * @Date in Apr 18, 2011,4:24:51 PM
 *
 * @author TBM
 */
public class WSUrlConfig {

    private Logger logger = Logger.getLogger(WSUrlConfig.class);
    private static String mainUrl = null;
    private static String spareUrl = null;
    private static WSUrlConfig config = null;
    //认证服务器接口
    private IServerServ serverServ = null;

    private WSUrlConfig() {
        String hostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.MAIN_HOSTIPADDR);
        String spareHostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.SPARE_HOSTIPADDR);

        serverServ = (IServerServ) AppContextMgr.getObject("serverServ");
        ServerInfo serverInfo = new ServerInfo();
        try {
            String httpStr = "http://";
            List<?> hosts = serverServ.query(serverInfo, new PageArgument());
            Iterator<?> iter = hosts.iterator();
            while (iter.hasNext()) {
                serverInfo = (ServerInfo) iter.next();
                String tempIp = serverInfo.getHostipaddr();
                if (StrTool.strEquals(hostIp, tempIp)) {
                    mainUrl = httpStr + tempIp + ":" + serverInfo.getSoapport() + "/" + serverInfo.getWebservicename()
                            + "?WSDL";
                }
                if (StrTool.strEquals(spareHostIp, tempIp)) {
                    spareUrl = httpStr + tempIp + ":" + serverInfo.getSoapport() + "/" + serverInfo.getWebservicename()
                            + "?WSDL";
                }
            }
        } catch (Exception ex) {
            logger.error("Loading WebService URL Failed!", ex);
        }
    }

    public static WSUrlConfig loadWSUrl() {
        if (config != null) {
            return config;
        }

        synchronized (WSUrlConfig.class) {
            if (config == null) {
                config = new WSUrlConfig();
            }
            return config;
        }
    }

    /**
     * @return the mainUrl
     */
    public static String getMainUrl() {
        return mainUrl;
    }

    /**
     * @return the spareUrl
     */
    public static String getSpareUrl() {
        return spareUrl;
    }

    /**
     * 清空Map，重置config
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void clear() {
        if (null != config) {
            config = null;
        }
    }

    /**
     * 重新加载
     * 
     * @Date in May 23, 2013,11:51:41 AM
     */
    public static void reLoad() {
        clear();
        loadWSUrl();
    }

}
