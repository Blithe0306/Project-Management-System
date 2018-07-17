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
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ft.otp.manager.authmgr.agentconf.service.IAgentConfServ;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;
import com.ft.otp.manager.authmgr.backend.service.IBackendServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证管理模块，后端认证，认证代理配置
 *
 * @Date in Feb 23, 2013,10:54:29 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AgentPubConfig {

    private Logger logger = Logger.getLogger(AgentPubConfig.class);
    private static AgentPubConfig config = null;
    private static Map<Integer, String> backendMap;
    private static Map<Integer, String> atentConfMap;

    private IBackendServ backendServ = (IBackendServ) AppContextMgr.getObject("backendServ");
    private IAgentConfServ agentConfServ = (IAgentConfServ) AppContextMgr.getObject("agentConfServ");

    private AgentPubConfig() {
        backendMap = new HashMap<Integer, String>();
        atentConfMap = new HashMap<Integer, String>();
        try {
            List<?> list = backendServ.query(new BackendInfo(), new PageArgument());
            List<?> agentConflist = agentConfServ.query(new AgentConfInfo(), new PageArgument());
            //后端认证
            if (StrTool.listNotNull(list)) {
                Iterator<?> iter = list.iterator();
                while (iter.hasNext()) {
                    BackendInfo backend = (BackendInfo) iter.next();
                    backendMap.put(backend.getId(), backend.getBackendname());
                }
            }
            //认证代理配置
            if (StrTool.listNotNull(agentConflist)) {
                Iterator<?> iter = agentConflist.iterator();
                while (iter.hasNext()) {
                    AgentConfInfo agentConf = (AgentConfInfo) iter.next();
                    atentConfMap.put(agentConf.getId(), agentConf.getConfname());
                }
            }
        } catch (Exception e) {
            logger.error("Load AgentPubConfig Failed!", e);
        }
    }

    public static AgentPubConfig loadAgentPubConfig() {
        if (config != null) {
            return config;
        }
        synchronized (AgentPubConfig.class) {
            if (config == null) {
                config = new AgentPubConfig();
            }
            return config;
        }
    }

    /**
     * 根据键值获取Value值
     * @Date in Feb 23, 2013,11:01:58 AM
     * @param key
     * @return
     */
    public static String getBackendValue(Integer key) {
        if (!StrTool.mapNotNull(backendMap)) {
            return "";
        }
        return (String) backendMap.get(key);
    }

    public static String getAgentConfValue(Integer key) {
        if (!StrTool.mapNotNull(atentConfMap)) {
            return "";
        }
        return (String) atentConfMap.get(key);
    }

    public static void clear() {
        if (null != config) {
            backendMap.clear();
            atentConfMap.clear();
            config = null;
        }
    }

    /**
     * 重新加载
     * 
     * @Date in Jun 8, 2013,4:43:57 PM
     */
    public synchronized static void reload() {
        clear();
        loadAgentPubConfig();
    }

}
