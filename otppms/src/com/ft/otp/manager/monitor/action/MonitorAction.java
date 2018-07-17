/**
 *Administrator
 */
package com.ft.otp.manager.monitor.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.monitor.action.aide.MonitorActionAide;
import com.ft.otp.manager.monitor.entity.EquipmentInfo;
import com.ft.otp.manager.monitor.entity.MonitorAndAdminInfo;
import com.ft.otp.manager.monitor.entity.MonitorConfInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;
import com.ft.otp.manager.monitor.equipmentmonitor.service.impl.CPUInfoServImpl;
import com.ft.otp.manager.monitor.equipmentmonitor.service.impl.DfInfoServImpl;
import com.ft.otp.manager.monitor.equipmentmonitor.service.impl.MemInfoServImpl;
import com.ft.otp.manager.monitor.service.IMonitorServ;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统监控业务处理Action
 *
 * @Date in Sep 18, 2012,2:52:15 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorAction extends BaseAction {

    private static final long serialVersionUID = -2217084120213654165L;
    private Logger logger = Logger.getLogger(MonitorAction.class);

    // 监控预警服务service
    private IMonitorServ monitorServ = null;

    // 获取设备信息的service
    private IEquipmentMonitorServ equipmentMonitorServ = null;
    private EquipmentMonitorInfo equipmentMonitorInfo;

    // 访问控制策略服务接口
    private IAccessConServ accessConServ = (IAccessConServ) AppContextMgr.getObject("accessConServ");
    // 认证服务器服务接口
    private IServerServ serverServ = (IServerServ) AppContextMgr.getObject("serverServ");
    // 配置公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    // 监控预警功能帮助类
    private MonitorActionAide aide = new MonitorActionAide();

    // 监控预警帮助类
    private MonitorTaskAide monitorTaskAide = new MonitorTaskAide();

    private MonitorConfInfo monitorConfInfo;

    /**
     * @return the monitorConfInfo
     */
    public MonitorConfInfo getMonitorConfInfo() {
        return monitorConfInfo;
    }

    /**
     * @param monitorConfInfo the monitorConfInfo to set
     */
    public void setMonitorConfInfo(MonitorConfInfo monitorConfInfo) {
        this.monitorConfInfo = monitorConfInfo;
    }

    /**
     * @return the equipmentMonitorInfo
     */
    public EquipmentMonitorInfo getEquipmentMonitorInfo() {
        return equipmentMonitorInfo;
    }

    /**
     * @param equipmentMonitorInfo the equipmentMonitorInfo to set
     */
    public void setEquipmentMonitorInfo(EquipmentMonitorInfo equipmentMonitorInfo) {
        this.equipmentMonitorInfo = equipmentMonitorInfo;
    }

    /**
     * @return the monitorServ
     */
    public IMonitorServ getMonitorServ() {
        return monitorServ;
    }

    /**
     * @param monitorServ the monitorServ to set
     */
    public void setMonitorServ(IMonitorServ monitorServ) {
        this.monitorServ = monitorServ;
    }

    /**
     * 获取用户门户的在线用户数
     * 
     * @Date in Mar 8, 2013,6:37:23 PM
     * @return
     */
    public void getProtalOnlineUsers() {
        setResponseWrite("test");
    }

    /**
     * 获取设备列表数据
     * 
     * @Date in Feb 27, 2013,4:03:12 PM
     * @return
     */
    public String getEquipmentList() {
        try {
            // 组织列表数据
            List<EquipmentInfo> equipmentList = aide.getEquipmentData(request);
            PageArgument pageArg = getArgument(equipmentList.size());
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());

            String jsonData = JsonTool.getJsonFromList(pageArg.getTotalRow(), equipmentList, null);
            setResponseWrite(jsonData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取 应用系统状态信息
     * 
     * @Date in Mar 9, 2013,11:25:24 AM
     * @return
     */
    public String getAppStateInfo() {
        try {
            List<EquipmentInfo> equipmentList = new ArrayList<EquipmentInfo>();

            // 获取用户门户IP和管理中心服务IP
            AccessInfo accessInfoQueryForm = new AccessInfo();
            accessInfoQueryForm.setSystype(NumConstant.common_number_1);
            accessInfoQueryForm.setClientapptype(-1);
            List<?> portalList = accessConServ.query(accessInfoQueryForm, new PageArgument());

            if (StrTool.listNotNull(portalList)) {
                AccessInfo[] arrPortals = portalList.toArray(new AccessInfo[portalList.size()]);
                for (AccessInfo accessInfo : arrPortals) {
                    EquipmentInfo eInfo = new EquipmentInfo();
                    int clientAppType = accessInfo.getClientapptype();
                    eInfo.setIpAddr(accessInfo.getStartip());

                    if (clientAppType == NumConstant.common_number_0) {
                        // 管理中心
                        eInfo.setServerName(Language.getLangStr(request, "monitor_otpcenter"));
                        eInfo.setOnlineUsers(OnlineUsers.getUsersCount());
                        eInfo.setServerType(StrConstant.SERVER_TYPE_CENTER);

                    } else {
                        // 用户门户
                        eInfo.setPort(accessInfo.getClientappport());
                        eInfo.setClientServPath(accessInfo.getClientservpath());
                        eInfo.setServerName(Language.getLangStr(request, "monitor_otpportal"));
                        eInfo.setServerType(StrConstant.SERVER_TYPE_PORTAL);

                        String portalOnlineCount = "0";
                        try {
                            // 有的用户门户可能会请求异常
                            portalOnlineCount = aide.getRequestInfo(eInfo, NumConstant.common_number_3);
                            // 读取请求的返回值时 有换行符
                            if (portalOnlineCount.indexOf("\n") != -1) {
                                portalOnlineCount = portalOnlineCount.replace("\n", "");
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        eInfo.setOnlineUsers(StrTool.parseInt(portalOnlineCount));
                    }

                    equipmentList.add(eInfo);
                }
            }

            // 组织列表数据
            PageArgument pageArg = getArgument(equipmentList.size());
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());

            String jsonData = JsonTool.getJsonFromList(pageArg.getTotalRow(), equipmentList, null);
            setResponseWrite(jsonData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取认证服务器状态 信息列表
     * 
     * @Date in Mar 9, 2013,11:26:09 AM
     * @return
     */
    public String getServerStateInfo() {
        try {
            List<EquipmentInfo> equipmentList = new ArrayList<EquipmentInfo>();
            // 获取所有认证服务器
            List<?> serverList = serverServ.query(new ServerInfo(), new PageArgument());

            if (StrTool.listNotNull(serverList)) {
                ServerInfo[] arrServers = serverList.toArray(new ServerInfo[serverList.size()]);
                for (ServerInfo serverInfo : arrServers) {

                    EquipmentInfo eInfo = new EquipmentInfo();
                    eInfo.setIpAddr(serverInfo.getHostipaddr());
                    eInfo.setPort(serverInfo.getSoapport());
                    eInfo.setClientServPath(serverInfo.getWebservicename());
                    eInfo.setServerName(serverInfo.getHostname());
                    eInfo.setServerType(StrConstant.SERVER_TYPE_SERVER);

                    // 获取状态码
                    String runState = aide.getRequestInfo(eInfo, NumConstant.common_number_3);
                    String stateStr = "";// 状态多语言
                    if (StrTool.strNotNull(runState)) {
                        stateStr = Language.getLangStr(request, Constant.LANG_CODE_KEY + runState);
                        if (!StrTool.strNotNull(stateStr)) {
                            stateStr = runState;
                        }
                    } else {
                        stateStr = Language.getLangStr(request, "monitor_runstate_get_error");
                    }
                    eInfo.setRunState(stateStr);

                    equipmentList.add(eInfo);
                }
            }

            // 组织列表数据
            PageArgument pageArg = getArgument(equipmentList.size());
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());

            String jsonData = JsonTool.getJsonFromList(pageArg.getTotalRow(), equipmentList, null);
            setResponseWrite(jsonData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取url请求的参数
     * 
     * @Date in Mar 12, 2013,3:16:20 PM
     * @return
     */
    private EquipmentInfo getEquipmentInfo() {
        EquipmentInfo eInfo = new EquipmentInfo();
        String ipAddr = request.getParameter("ipaddr");
        if (StrTool.strEquals(ipAddr, "localhost")) {
            eInfo.setIpAddr(StrConstant.LOCALHOST_IP);
        } else {
            eInfo.setIpAddr(ipAddr);
        }

        eInfo.setServerType(request.getParameter("servertype"));
        eInfo.setClientServPath(request.getParameter("clientservpath"));
        String port = request.getParameter("port");
        eInfo.setPort(port == null ? 0 : StrTool.parseInt(port));

        return eInfo;
    }

    /**
     * 获取CPU基本信息属性
     * 
     * @Date in Sep 18, 2012,2:59:12 PM
     * @return
     */
    public String getCpuInfo() {
        String cpuInfoJson = null;
        try {
            EquipmentInfo equipmentInfo = getEquipmentInfo();

            // 判断是否是本地服务IP地址
            if (StrTool.strEquals(StrConstant.LOCALHOST_IP, equipmentInfo.getIpAddr())
                    || StrTool.strEquals(getLocalIpAddr(), equipmentInfo.getIpAddr())) {
                // 本地 管理中心
                equipmentMonitorServ = new CPUInfoServImpl();
                cpuInfoJson = equipmentMonitorServ.getObjectsToJson();
            } else {
                cpuInfoJson = aide.getRequestInfo(equipmentInfo, NumConstant.common_number_0);
            }

            // 写回列表格式的json数据
            setResponseWrite(cpuInfoJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取内存基本信息属性
     * 
     * @Date in Sep 18, 2012,3:00:16 PM
     * @return
     */
    public String getMemInfo() {
        String memInfoJson = null;
        try {
            EquipmentInfo equipmentInfo = getEquipmentInfo();

            // 判断是否是本地服务IP地址
            if (StrTool.strEquals(StrConstant.LOCALHOST_IP, equipmentInfo.getIpAddr())
                    || StrTool.strEquals(getLocalIpAddr(), equipmentInfo.getIpAddr())) {
                // 本地 管理中心
                equipmentMonitorServ = new MemInfoServImpl();
                memInfoJson = equipmentMonitorServ.getObjectsToJson();
            } else {
                memInfoJson = aide.getRequestInfo(equipmentInfo, NumConstant.common_number_1);
            }

            // 写回列表格式的json数据
            setResponseWrite(memInfoJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 获取磁盘空间基本信息属性
     * 
     * @Date in Sep 18, 2012,3:02:15 PM
     * @return
     */
    public String getDfInfo() {
        String dfInfoJson = null;
        try {
            EquipmentInfo equipmentInfo = getEquipmentInfo();

            // 判断是否是本地服务IP地址
            if (StrTool.strEquals(StrConstant.LOCALHOST_IP, equipmentInfo.getIpAddr())
                    || StrTool.strEquals(getLocalIpAddr(), equipmentInfo.getIpAddr())) {
                // 本地 管理中心
                equipmentMonitorServ = new DfInfoServImpl();
                dfInfoJson = equipmentMonitorServ.getObjectsToJson();
            } else {
                dfInfoJson = aide.getRequestInfo(equipmentInfo, NumConstant.common_number_2);
            }

            // 写回列表格式的json数据
            setResponseWrite(dfInfoJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 修改时查询数据
     * 
     * @Date in Mar 5, 2013,5:38:29 PM
     * @return
     */
    public String find() {
        try {
            MonitorConfInfo monitorConfInfoTemp = new MonitorConfInfo();

            // 所有的预警接收人
            List<?> baseList = monitorServ.queryMonitorAndAdmin(new MonitorAndAdminInfo(), new PageArgument());

            // 预警基本配置
            List<?> configListBase = ConfDataFormat.getConfList(ConfConstant.MONITOR_BASE_CONF);
            monitorConfInfoTemp = monitorConfInfo.getMonitorInfoByList(monitorConfInfoTemp, configListBase);

            // 基本配置的接收人
            monitorConfInfoTemp.setBaserecvusers(getRecvuserSByConfType(baseList, ConfConstant.MONITOR_BASE_CONF));

            // 设备预警配置
            List<?> configListSb = ConfDataFormat.getConfList(ConfConstant.MONITOR_SB_CONF);
            monitorConfInfoTemp = monitorConfInfo.getMonitorInfoByList(monitorConfInfoTemp, configListSb);

            // 设备预警配置的接收人
            monitorConfInfoTemp.setSbrecvusers(getRecvuserSByConfType(baseList, ConfConstant.MONITOR_SB_CONF));

            // 应用系统预警配置
            List<?> configListApp = ConfDataFormat.getConfList(ConfConstant.MONITOR_APP_CONF);
            monitorConfInfo = monitorConfInfo.getMonitorInfoByList(monitorConfInfoTemp, configListApp);

            // 应用系统预警配置的接收人
            monitorConfInfo.setApprecvusers(getRecvuserSByConfType(baseList, ConfConstant.MONITOR_APP_CONF));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return SUCC_FIND;
    }

    /**
     * 核心业务操作配置修改操作
     */
    public String modify() {
        try {
            List<Object> confList = MonitorConfInfo.getListByMonitorInfo(monitorConfInfo);
            confInfoServ.batchUpdateConf(confList);

            boolean baseIsUpdate = false;
            // 如果各个预警配置的执行时间段、定时器启用与否改变则重新加载预警相关任务
            if (!StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                    ConfConstant.MONITOR_BASE_TIMEINTERVAL), monitorConfInfo.getBasetimeinterval())
                    || !StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                            ConfConstant.MONITOR_BASE_ENABLED), monitorConfInfo.getBaseenabled())) {
                baseIsUpdate = true;
            }

            boolean sbIsUpdate = false;
            if (!StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                    ConfConstant.MONITOR_SB_TIMEINTERVAL), monitorConfInfo.getSbtimeinterval())
                    || !StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                            ConfConstant.MONITOR_SB_ENABLED), monitorConfInfo.getSbenabled())) {
                sbIsUpdate = true;
            }

            boolean appIsUpdate = false;
            if (!StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_APP_CONF,
                    ConfConstant.MONITOR_APP_TIMEINTERVAL), monitorConfInfo.getApptimeinterval())
                    || !StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_APP_CONF,
                            ConfConstant.MONITOR_APP_ENABLED), monitorConfInfo.getAppenabled())) {
                appIsUpdate = true;
            }

            // 重新加载配置缓存
            ConfConfig.reLoad();

            // 重新加载预警的判断要放到加载配置的后边 
            if (baseIsUpdate) {
                // 重新加载定时器的监控预警任务
                monitorTaskAide.addOrUpMonitorTask(NumConstant.common_number_1);
            }

            if (sbIsUpdate) {
                // 重新加载定时器的监控预警任务
                monitorTaskAide.addOrUpMonitorTask(NumConstant.common_number_2);
            }

            if (appIsUpdate) {
                // 重新加载定时器的监控预警任务
                monitorTaskAide.addOrUpMonitorTask(NumConstant.common_number_3);
            }

            // 对发送人的处理
            // 添加的管理员
            List<Object> addList = new ArrayList<Object>();
            // 删除的管理员
            List<Object> delList = new ArrayList<Object>();

            // 所有的预警接收人
            List<?> baseList = monitorServ.queryMonitorAndAdmin(new MonitorAndAdminInfo(), new PageArgument());

            // 获取基本预警需要添加和删除的管理员
            setAddAndDelList(monitorConfInfo.getBaserecvusers(), baseList, addList, delList,
                    ConfConstant.MONITOR_BASE_CONF);

            // 获取设备预警需要添加和删除的管理员
            setAddAndDelList(monitorConfInfo.getSbrecvusers(), baseList, addList, delList, ConfConstant.MONITOR_SB_CONF);

            // 获取应用系统预警需要添加和删除的管理员
            setAddAndDelList(monitorConfInfo.getApprecvusers(), baseList, addList, delList,
                    ConfConstant.MONITOR_APP_CONF);

            // 执行删除
            if (StrTool.listNotNull(addList)) {
                monitorServ.batchAddMonitorAndAdmin(addList);
            }

            // 执行添加
            if (StrTool.listNotNull(delList)) {
                monitorServ.batchDelMonitorAndAdmin(delList);
            }

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "monitor_conf_succ"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "monitor_conf_failed"));
        }

        return null;
    }

    /**
     * 根据选择的管理员判断删除和添加的预警信息记录
     * 
     * @Date in Mar 5, 2013,3:19:59 PM
     * @param recvUsers 新的预警接收人
     * @param allRecvUsers 所有预警接收人
     * @param addList 旧的预警接收人
     * @param delList 删除list
     * @param confType 配置的预警类型
     * @throws Exception
     */
    private void setAddAndDelList(String[] newRecvUsers, List<?> allRecvUsers, List<Object> addList,
            List<Object> delList, String confType) throws Exception {
        // 根据所有接收人管理员 获取该种预警类型的接收管理员
        String[] oldRecvUsers = getRecvuserSByConfType(allRecvUsers, confType);

        if (!StrTool.arrNotNull(newRecvUsers)) {
            for (String oldU : oldRecvUsers) {
                MonitorAndAdminInfo temp = new MonitorAndAdminInfo();
                temp.setAdminid(oldU);
                temp.setConftype(confType);
                delList.add(temp);
            }
        } else {// 新的用户不为空
            // 得到旧的管理员比新的管理员多出的部分 删除
            List<String> delAdminList = StrTool.BLessToA(newRecvUsers, oldRecvUsers);
            for (String delAdmin : delAdminList) {
                MonitorAndAdminInfo temp = new MonitorAndAdminInfo();
                temp.setAdminid(delAdmin);
                temp.setConftype(confType);
                delList.add(temp);
            }

            // 得到新的管理员比旧的管理员多出的部分 添加
            List<String> addAdminList = StrTool.BLessToA(oldRecvUsers, newRecvUsers);
            for (String addAdmin : addAdminList) {
                MonitorAndAdminInfo temp = new MonitorAndAdminInfo();
                temp.setAdminid(addAdmin);
                temp.setConftype(confType);
                addList.add(temp);
            }
        }
    }

    /**
     * 通过预警信息表中的配置类型 获得接收人信息
     * 方法说明
     * @Date in Mar 5, 2013,4:19:19 PM
     * @param confType
     * @return
     */
    private String[] getRecvuserSByConfType(List<?> baseList, String confType) throws Exception {
        if (!StrTool.listNotNull(baseList)) {
            return new String[] {};
        }

        Set<String> baseUsers = new HashSet<String>();
        MonitorAndAdminInfo[] arrMA = baseList.toArray(new MonitorAndAdminInfo[baseList.size()]);
        for (MonitorAndAdminInfo monitorAndAdminInfo : arrMA) {
            if (StrTool.strEquals(monitorAndAdminInfo.getConftype(), confType)) {
                baseUsers.add(monitorAndAdminInfo.getAdminid());
            }
        }

        return baseUsers.toArray(new String[baseUsers.size()]);
    }

}
