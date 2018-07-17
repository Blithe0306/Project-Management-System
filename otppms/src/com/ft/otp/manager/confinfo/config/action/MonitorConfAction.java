/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.CenterConfInfo;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.entity.MonitorConf;
import com.ft.otp.manager.confinfo.config.entity.MonitorConfig;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.monitor.entity.MonitorAndAdminInfo;
import com.ft.otp.manager.monitor.service.IMonitorServ;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警配置业务action
 *
 * @Date in Aug 19, 2013,2:57:11 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class MonitorConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 833901261651274408L;
    private Logger logger = Logger.getLogger(MonitorConfAction.class);
    
    private IMonitorServ monitorServ = (IMonitorServ) AppContextMgr.getObject("monitorServ");
    // 配置公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");

    // 监控预警帮助类
    private MonitorTaskAide monitorTaskAide = new MonitorTaskAide();
    private MonitorConf monitorConfInfo;
    private MonitorConf oldMonitorConfInfo;
    
    public MonitorConf getOldMonitorConfInfo() {
		return oldMonitorConfInfo;
	}

	public void setOldMonitorConfInfo(MonitorConf oldMonitorConfInfo) {
		this.oldMonitorConfInfo = oldMonitorConfInfo;
	}

	public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    public String find() {
    	List<?> configList = null;
        try {
            // 预警基本配置
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.MONITOR_BASE_CONF);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            monitorConfInfo = MonitorConf.getMonitorInfoByList(configList);
            
            // 所有的预警接收人
            List<?> baseList = monitorServ.queryMonitorAndAdmin(new MonitorAndAdminInfo(), new PageArgument());
            // 基本配置的接收人
            monitorConfInfo.setBaserecvusers(getRecvuserSByConfType(baseList, ConfConstant.MONITOR_BASE_CONF));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return SUCCESS;
    }
    
    /**
     * 通过预警信息表中的配置类型 获得接收人信息
     * @Date in Aug 19, 2013,3:15:16 PM
     * @param baseList
     * @param confType
     * @return
     * @throws Exception
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

    public String init() {
        return null;
    }

    public String modify() {
        try {
            List<Object> confList = MonitorConf.getListByMonitorInfo(monitorConfInfo);
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.MONITOR_BASE_CONF);
            List<?> configList = confInfoServ.queryConfInfo(config, new PageArgument());
            MonitorConf monitorConf = MonitorConf.getMonitorInfoByList(configList);
            confInfoServ.batchUpdateConf(confList);

            boolean baseIsUpdate = false;
            // 如果各个预警配置的执行时间段、定时器启用与否改变则重新加载预警相关任务
            if (!StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                    ConfConstant.MONITOR_BASE_TIMEINTERVAL), monitorConfInfo.getBasetimeinterval())
                    || !StrTool.strEquals(ConfDataFormat.getConfValue(ConfConstant.MONITOR_BASE_CONF,
                            ConfConstant.MONITOR_BASE_ENABLED), monitorConfInfo.getBaseenabled())) {
                baseIsUpdate = true;
            }

            // 重新加载配置缓存
            ConfConfig.reLoad();

            // 重新加载预警的判断要放到加载配置的后边 
            if (baseIsUpdate) {
                // 重新加载定时器的监控预警任务
                monitorTaskAide.addOrUpMonitorTask(NumConstant.common_number_1);
            }

            // 对发送人的处理
            // 添加的管理员
            if (StrTool.arrNotNull(monitorConfInfo.getBaserecvusers())) {
                List<Object> addList = new ArrayList<Object>();
                // 删除的管理员
                List<Object> delList = new ArrayList<Object>();
                
                // 所有的预警接收人
                MonitorAndAdminInfo maAdminInfo=new MonitorAndAdminInfo();
                maAdminInfo.setConftype(ConfConstant.MONITOR_BASE_CONF);
                List<?> baseList = monitorServ.queryMonitorAndAdmin(maAdminInfo, new PageArgument());
                
                // 获取基本预警需要添加和删除的管理员
                setAddAndDelList(monitorConfInfo.getBaserecvusers(), baseList, addList, delList,
                        ConfConstant.MONITOR_BASE_CONF);
                
                // 执行删除
                if (StrTool.listNotNull(addList)) {
                    monitorServ.batchAddMonitorAndAdmin(addList);
                }
                
                // 执行添加
                if (StrTool.listNotNull(delList)) {
                    monitorServ.batchDelMonitorAndAdmin(delList);
                }
                
                // 取出修改前的预警接收人，用于日志
                String [] oldAdmin = new String[baseList.size()];
                for (int j=0; j<baseList.size(); j++){
                	MonitorAndAdminInfo monitorAndAdmin = (MonitorAndAdminInfo)baseList.get(j);
                	oldAdmin[j] = monitorAndAdmin.getAdminid();
                }
                monitorConf.setBaserecvusers(oldAdmin);
            }
            
            this.setOldMonitorConfInfo(monitorConf);
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

    public String page() {
        return null;
    }

    public String view() {
        return null;
    }

    public MonitorConf getMonitorConfInfo() {
        return monitorConfInfo;
    }

    public void setMonitorConfInfo(MonitorConf monitorConfInfo) {
        this.monitorConfInfo = monitorConfInfo;
    }

}
