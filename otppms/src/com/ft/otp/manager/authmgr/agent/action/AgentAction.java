/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.interceptor.log.LogCommonObj;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.agent.action.aide.AgentActionAide;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;
import com.ft.otp.manager.authmgr.agent.form.AgentQueryForm;
import com.ft.otp.manager.authmgr.agent.service.IAgentServ;
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ft.otp.manager.authmgr.agentconf.service.IAgentConfServ;
import com.ft.otp.manager.authmgr.agentserver.entity.AgentServerInfo;
import com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证管理-认证代理功能说明
 *
 * @Date in Apr 11, 2011,1:58:56 PM
 *
 * @author ZJY
 */
public class AgentAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 5888221187280673968L;

    private Logger logger = Logger.getLogger(AgentAction.class);

    //代理服务器服务接口
    private IAgentServ agentServ;

    //代理服务器服务接口（认证服务和代理的中间表）
    private IAgentServerServ agentServerServ = (IAgentServerServ) AppContextMgr.getObject("agentServerServ");

    //认证服务器接口
    private IServerServ serverServ = (IServerServ) AppContextMgr.getObject("serverServ");

    //认证代理配置接口
    private IAgentConfServ agentConfServ = (IAgentConfServ) AppContextMgr.getObject("agentConfServ");

    private AgentQueryForm agentQueryForm = null;
    private AgentInfo agentInfo;
    private AgentActionAide aide = new AgentActionAide();
    private AgentServerInfo agentServerInfo = new AgentServerInfo();
    private AgentInfo oldAgentInfo = new AgentInfo();

    //特殊日志记录
    LogCommonObj commonObj = new LogCommonObj();
    
	public AgentInfo getOldAgentInfo() {
		return oldAgentInfo;
	}

	public void setOldAgentInfo(AgentInfo oldAgentInfo) {
		this.oldAgentInfo = oldAgentInfo;
	}

	public IAgentServ getAgentServ() {
        return agentServ;
    }

    public void setAgentServ(IAgentServ agentServ) {
        this.agentServ = agentServ;
    }

    public AgentQueryForm getAgentQueryForm() {
        return agentQueryForm;
    }

    public void setAgentQueryForm(AgentQueryForm agentQueryForm) {
        this.agentQueryForm = agentQueryForm;
    }

    public AgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    /**
     * @return the agentServerInfo
     */
    public AgentServerInfo getAgentServerInfo() {
        return agentServerInfo;
    }

    /**
     * @param agentServerInfo the agentServerInfo to set
     */
    public void setAgentServerInfo(AgentServerInfo agentServerInfo) {
        this.agentServerInfo = agentServerInfo;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    private AgentInfo getAgentInfo(AgentQueryForm agentQueryForm) {
        AgentInfo agInfo = new AgentInfo();
        if (StrTool.objNotNull(agentQueryForm)) {
            agInfo = agentQueryForm.getAgentfo();
        }
        return agInfo;
    }

    /**
     * 添加认证代理
     */
    public String add() {
        try {
            if (StrTool.objNotNull(agentInfo)) {
            	
            	// Start,日志处理
            	agentInfo = addLog(agentInfo);
            	// End,日志处理
            	
                agentServ.addObj(agentInfo);
                if (StrTool.listNotNull(agentInfo.getHostIps())) {
                    //添加认证代理关联的认证服务器
                    addAgentHost(agentInfo);
                }

                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_add_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_add_info_is_null"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    
    /**
     * 认证代理添加日志
     * @author LXH
     * @date Mar 10, 2014 4:40:37 PM
     * @param agentInfo
     * @return
     * @throws BaseException
     */
    private AgentInfo addLog(AgentInfo agentInfo) throws BaseException{
    	// flag：1、代表显示；0、代表不显示
        if(agentInfo.getAgenttype() == 4 || agentInfo.getAgenttype() == 1073741828){
        	agentInfo.setFlag(1);
        }
    	//认证代理配置信息,认证代理配置ID不为0时关联了认证代理配置
        AgentConfInfo agentConf = new AgentConfInfo();
        if (!StrTool.strEquals(StrTool.intToString(agentInfo.getAgentconfid()), StrConstant.common_number_0)) {
            agentConf.setId(agentInfo.getAgentconfid());
            agentConf = (AgentConfInfo) agentConfServ.find(agentConf);
            if (StrTool.objNotNull(agentConf)) {
            	agentInfo.setAgentconfStr(agentConf.getConfname());
            }
        }

        //遍历存放认证代理类型的Map获取显示名称
        StringBuffer sbf = new StringBuffer();
        Iterator<String> it = AgentActionAide.agenttypeMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            int result = agentInfo.getAgenttype() & Integer.parseInt(key);
            if (result != 0) {
                sbf.append(aide.getAgentTypeStr(key));
                sbf.append("，");
            }
        }
        agentInfo.setAgenttypeStr(sbf.toString());
        return agentInfo;
    }

    /**
     * 添加代理和服务器的关系
     * @Date in Apr 14, 2011,10:51:12 AM
     * @param agentInfo
     * @throws Exception
     */
    private void addAgentHost(AgentInfo agentInfo) throws Exception {
        AgentServerInfo agentHost = null;
        List<Object> ahList = new ArrayList<Object>();
        List<?> hosts = agentInfo.getHostIps();
        Iterator<?> iter = hosts.iterator();
        while (iter.hasNext()) {
            agentHost = new AgentServerInfo();
            String host = (String) iter.next();
            agentHost.setAgentipaddr(agentInfo.getAgentipaddr());
            agentHost.setHostipaddr(host);
            ahList.add(agentHost);
        }
        agentServerServ.addAgentHost(ahList);
    }

    /**
     * 删除认证代理
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delAgentIps");
        Iterator<?> iter = keySet.iterator();
        Set<String> newSet = new HashSet<String>();

        if (StrTool.setNotNull(keySet)) {
            try {
                List<?> agenList = new ArrayList<Object>();
                AgentServerInfo aServInfo = null;
                String noDelIps = "";

                while (iter.hasNext()) {
                    String keyId = (String) iter.next();
                    aServInfo = new AgentServerInfo();
                    aServInfo.setAgentipaddr(keyId);
                    agenList = agentServerServ.query(aServInfo, new PageArgument());

                    if (StrTool.listNotNull(agenList)) {
                        noDelIps += keyId + ",";
                    } else {
                        newSet.add(keyId);
                    }
                }

                if (StrTool.setNotNull(newSet)) {
                    agentServ.delObj(newSet);
                }
                if (StrTool.strNotNull(noDelIps)) {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_agent_sel")
                            + noDelIps.substring(0, noDelIps.length() - 1)
                            + Language.getLangStr(request, "auth_agent_sel_already_ser"));
                } else {
                    SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
                }
            } catch (BaseException e) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
                logger.error(e.getMessage(), e);
            }
        }

        return null;
    }

    /**
     * 编辑认证代理，初始化认证代理信息
     */
    public String find() {
        AgentInfo agInfo = null;
        List<?> servIps = null;
        try {
            //查找代理
            agInfo = (AgentInfo) agentServ.find(agentInfo);
            if (!StrTool.objNotNull(agInfo)) {
                return init();
            }
            agInfo.setGraceperiodStr(DateTool.dateToStr(agInfo.getGraceperiod(), true));
            //查找代理关联的服务器
            servIps = aide.getAgentServ(agentServerServ, agentInfo);
            if (StrTool.listNotNull(servIps)) {
                agInfo.setHostIps(servIps);
                agInfo.setHidHostIps(servIps);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        this.setAgentInfo(agInfo);

        return SUCC_FIND;
    }

    /**
     * 新增认证代理，查找代理IP是否已经存在
     * @Date in Apr 12, 2011,11:16:55 AM
     * @return null
     * resonse true or false
     */
    public String findIpIfExist() {
        AgentInfo agentinfo = new AgentInfo();
        try {
            String agentipaddr = agentInfo.getAgentipaddr();
            if (!StrTool.strNotNull(agentipaddr)) {
                super.setResponseWrite(Constant.alert_error);
                return null;
            }
            agentipaddr = MessyCodeCheck.iso885901ForUTF8(agentipaddr);
            agentinfo.setAgentipaddr(agentipaddr);
            agentinfo = (AgentInfo) agentServ.find(agentinfo);
            if (null != agentinfo) {
                super.setResponseWrite(Constant.alert_error);
            } else {
                super.setResponseWrite(Constant.alert_succ);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 初始化认证代理列表
     */
    public String init() {
        if (isNeedClearForm()) {
            agentQueryForm = null;
        }
        try {
            PageArgument pageArg = pageArgument();
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> resultList = query(pageArg);
            String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(jsonStr);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 根据查询分页列表
     * @Date in Apr 11, 2011,7:51:15 PM
     * @param pageArg
     */
    private List<?> query(PageArgument pageArg) {
        List<?> agentList = null;
        try {
            //取得Agent
            agentList = agentServ.query(getAgentInfo(agentQueryForm), pageArg);
            //根据Agent取得ServerIP
            agentList = aide.getAgentListAll(agentServerServ, agentList);

            if (!StrTool.listNotNull(agentList)) {
                agentList = new ArrayList<Object>();
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return agentList;
    }

    /**
     * 修改认证代理
     */
    public String modify() {
        try {
            String length = request.getParameter("length");
            List<?> hostList = agentInfo.getHostIps();
            List<?> hidHostList = agentInfo.getHidHostIps();
            AgentServerInfo aServInfo = null;

            if (StrTool.strEquals(length, StrConstant.common_number_0)) {
                if (StrTool.objNotNull(hostList)) {
                    hostList.clear();
                }
            }
            
            // Start,日志处理
            AgentInfo agentIn = new AgentInfo();
            agentIn.setAgentipaddr(agentInfo.getAgentipaddr());
            agentIn = (AgentInfo) agentServ.find(agentIn);;
            agentIn = addLog(agentIn);
            agentIn.setGraceperiodStr(DateTool.dateToStr(agentIn.getGraceperiod(), true));
            agentIn.setHostIps(hidHostList);
            this.setOldAgentInfo(agentIn);
            
            agentInfo = addLog(agentInfo);
            // End,日志处理
            
            if (StrTool.isArrEquals(hostList, hidHostList)) {
                agentServ.updateObj(agentInfo);
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));

                return null;
            }
            //更新代理配置信息
            agentServ.updateObj(agentInfo);

            aServInfo = new AgentServerInfo();
            aServInfo.setAgentipaddr(agentInfo.getAgentipaddr());
            aServInfo.setAgentHostList(hidHostList);

            //旧ServIP列表不为空，新ServIP列表为空，只执行取消对应操作
            if (StrTool.listNotNull(hidHostList) && !StrTool.listNotNull(hostList)) {
                agentServerServ.delObj(aServInfo);
            }
            //旧ServIP列表为空，新ServIP列表不为空，只执行添加对应操作
            if (!StrTool.listNotNull(hidHostList) && StrTool.listNotNull(hostList)) {
                addAgentHost(agentInfo);
            }
            //旧ServIP列表不为空，新ServIP列表不为空，既执行取消对应关系又执行建立对应关系操作
            if (StrTool.listNotNull(hidHostList) && StrTool.listNotNull(hostList)) {
                agentServerServ.delObj(aServInfo);
                addAgentHost(agentInfo);
            }

            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 页面分页跳转调用
     */
    public String page() {
        PageArgument pArgument = getArgument(request, 0);
        pArgument.setCurPage(getPage());
        pArgument.setPageSize(getPagesize());
        List<?> resultList = query(pArgument);
        String jsonStr = JsonTool.getJsonFromList(pArgument.getTotalRow(), resultList, null);
        setResponseWrite(jsonStr);

        return SUCCESS;
    }

    /**
     * 认证代理详细信息
     * @throws  
     */
    public String view() {
        AgentInfo viewInfo = null;
        //      String groupname = "";
        try {
            //Agent信息
            viewInfo = (AgentInfo) agentServ.find(agentInfo);
            
            if (!StrTool.objNotNull(viewInfo)) {
                return init();
            }
            
            // 判断查看页面是否显示登录保护认证代理宽限期；4 Windows logon认证代理的值；1073741828 普通认证代理和Windows logon认证代理的值
            // flag：1、代表显示；0、代表不显示
            if(viewInfo.getAgenttype() == 4 || viewInfo.getAgenttype() == 1073741828){
            	viewInfo.setFlag(1);
            }
            viewInfo.setGraceperiodStr(DateTool.dateToStr(viewInfo.getGraceperiod(), true));

            //根据代理IP查找关联的认证服务器信息
            List<?> servList = aide.getAgentServ(agentServerServ, viewInfo);
            if (StrTool.listNotNull(servList)) {
                viewInfo.setHostIps(servList);
            }
            //认证代理配置信息,认证代理配置ID不为0时关联了认证代理配置
            AgentConfInfo agentConf = new AgentConfInfo();
            if (!StrTool.strEquals(StrTool.intToString(viewInfo.getAgentconfid()), StrConstant.common_number_0)) {
                agentConf.setId(viewInfo.getAgentconfid());
                agentConf = (AgentConfInfo) agentConfServ.find(agentConf);
                if (StrTool.objNotNull(agentConf)) {
                    viewInfo.setAgentconfStr(agentConf.getConfname());
                }
            }

            //遍历存放认证代理类型的Map获取显示名称
            StringBuffer sbf = new StringBuffer();
            Iterator<String> it = AgentActionAide.agenttypeMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                int result = viewInfo.getAgenttype() & Integer.parseInt(key);
                if (result != 0) {
                    sbf.append(aide.getAgentTypeStr(key));
                    sbf.append(";\n ");
                }
            }
            viewInfo.setAgenttypeStr(sbf.toString());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setAgentInfo(viewInfo);

        return SUCC_VIEW;
    }

    /**
     * 行数统计
     * 分页处理
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = agentServ.count(getAgentInfo(agentQueryForm));
        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 解除认证代理与认证服务器的绑定关系初始化
     * @Date in Jun 20, 2011,1:00:11 PM
     * @return
     */
    public String unBindASInit() {
        try {
            //根据代理IP查找关联的认证服务器信息
            List<?> servList = aide.getAgentServ(agentServerServ, agentInfo);
            agentInfo.setHostIps(servList);
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return "unbind_serv";
    }

    /**
     * 解除认证代理与认证服务器的绑定关系
     * @Date in Jun 20, 2011,1:26:01 PM
     * @return
     * @author csl
     */
    public String unBindAS() {
        String keys[] = request.getParameterValues("key_id");
        String hostIp = request.getParameter("hostIP");
        AgentServerInfo asInfo2 = new AgentServerInfo();
        //解绑单个服务器、多个服务器的标识
        String unbindNum = request.getParameter("unbindNum");
        try {
            if (StrTool.arrNotNull(keys) && !StrTool.strNotNull(unbindNum)) {
                List<?> keyList = Arrays.asList(keys);
                asInfo2.setAgentHostList(keyList);
            } else {
                List<Object> servIps = new ArrayList<Object>();
                servIps.add(hostIp);
                asInfo2.setAgentHostList(servIps);

            }
            asInfo2.setAgentipaddr(agentInfo.getAgentipaddr());
            agentServerServ.delObj(asInfo2);

            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);

            //记录日志使用到
            this.setAgentServerInfo(asInfo2);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "auth_agent_unbind_succ"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "auth_agent_unbind_err"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 生成代理配置文件
     * 
     * @Date in Jun 17, 2011,10:22:46 AM
     * @return
     * @throws BaseException 
     */
    public String createACF() {
        String agentIp = request.getParameter("agentIp");
        AgentInfo agInfo = new AgentInfo();
        agInfo.setAgentipaddr(agentIp);
        String fileName = "";

        try {
            //查找代理信息
            agInfo = (AgentInfo) agentServ.find(agInfo);

            //代理绑定的服务器
            String serverStr = getHostInfo(agentIp);
            if (!StrTool.strNotNull(serverStr)) {
                super.setResponseWrite("noServ");
                return null;
            }

            //File Name
            fileName = "key" + agentIp + Constant.FILE_ACF;
            String filePath = appPath(Constant.WEB_TEMP_FILE_ACF, null);
            aide.createAcfFile(filePath, fileName, agentIp, agInfo.getPubkey(), serverStr);
            //返回acfName
            super.setResponseWrite(fileName);
            commonObj
                    .addAdminLog(AdmLogConstant.log_aid_download, AdmLogConstant.log_obj_auth_agent, fileName, null, 0);
        } catch (Exception e) {
            super.setResponseWrite("failed");
            logger.error(e.getMessage(), e);
            commonObj
                    .addAdminLog(AdmLogConstant.log_aid_download, AdmLogConstant.log_obj_auth_agent, fileName, null, 1);
        }

        return null;
    }

    /**
     * 获取代理绑定的服务器信息，根据服务器的优先级别
     * 
     * @Date in Jul 17, 2013,10:46:15 AM
     * @param agentIp
     * @return
     * @throws Exception
     */
    private String getHostInfo(String agentIp) throws Exception {
        AgentServerInfo asInfo = new AgentServerInfo();
        asInfo.setAgentipaddr(agentIp);
        List<?> asList = agentServerServ.queryServORAgent(asInfo);
        if (!StrTool.listNotNull(asList)) {
            return null;
        }

        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i <= 2; i++) {
            for (int k = 0; k < asList.size(); k++) {
                asInfo = (AgentServerInfo) asList.get(k);
                String serverIp = asInfo.getHostipaddr();
                ServerInfo serverInfo = aide.getPortInfo(serverIp, serverServ);
                int priority = serverInfo.getPriority();
                if (priority == i) {
                    int port = serverInfo.getAuthport();
                    int syncPort = serverInfo.getSyncport();
                    sBuilder.append(serverIp).append(":").append(port).append(",").append(syncPort).append(";");
                }
            }
        }

        return sBuilder.toString();
    }

    /**
     * 下载代理配置文件
     * 
     * @Date in Jun 17, 2011,11:50:15 AM
     * @return
     * @throws IOException 
     */
    public String downLoadACF() throws IOException {
        String acfName = request.getParameter("acfName");
        String filePath = appPath(Constant.WEB_TEMP_FILE_ACF, acfName);

        response.reset();
        acfName = acfName.substring(0, acfName.lastIndexOf("."));
        acfName = acfName.replace(".", "_");
        acfName += Constant.FILE_ACF;
        response.setHeader("Content-Disposition", "attachment; filename=" + acfName);

        try {
            ServletOutputStream outs = response.getOutputStream();
            File file = new File(filePath);
            InputStream inStream = new FileInputStream(filePath);
            // 循环取出流中的数据
            int length = StrTool.longToInt(file.length());
            byte[] b = new byte[length];
            int len;
            while ((len = inStream.read(b)) > 0) {
                outs.write(b, 0, len);
            }
            inStream.close();
            outs.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        return null;
    }

    /**
     * 启用，禁用认证代理
     * @Date in Jan 17, 2013,4:10:58 PM
     * @return
     */
    public String enabledAgent() {
        try {
            String agentipaddr = request.getParameter("agentipaddr");
            String enabled = request.getParameter("enabled");
            AgentInfo agent = new AgentInfo();
            agent.setAgentipaddr(agentipaddr);
            agent.setEnabled(StrTool.parseInt(enabled));
            agentServ.updateEnabled(agent);
            if (StrTool.strEquals(enabled, StrConstant.common_number_1)) {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_enable_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_disabled_succ_tip"));
            }

            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 查询认证代理配置列表信息
     * @Date in Jan 29, 2013,5:17:40 PM
     * @return
     */
    public String queryConfList() {
        String type = request.getParameter("type");
        String confid = request.getParameter("confid");
        String agentId = request.getParameter("agentId");
        List<?> confList = null;
        PageArgument pageArg = new PageArgument();
        StringBuffer sbf = new StringBuffer();
        try {
            AgentConfInfo agentConf = new AgentConfInfo();
            if (StrTool.strNotNull(agentId)) {
                sbf
                        .append("<select id='agentconfid' name='agentInfo.agentconfid' onchange='setSelect();' class='select100'>");
            } else {
                sbf.append("<select id='agentconfid' name='agentInfo.agentconfid' class='select100'>");
            }
            sbf.append("<option value='0'>");
            sbf.append(Language.getLangStr(request, "common_syntax_please_sel"));
            sbf.append("</option>");
            if (StrTool.strNotNull(type)) {
                agentConf.setType(Integer.parseInt(type));
                confList = agentConfServ.queryConfList(agentConf, pageArg);
                if (StrTool.listNotNull(confList)) {
                    AgentConfInfo confInfo = null;
                    for (int i = 0; i < confList.size(); i++) {
                        confInfo = (AgentConfInfo) confList.get(i);
                        if (StrTool.strEquals(confid, StrTool.intToString(confInfo.getId()))) {
                            sbf.append("<option value='" + confInfo.getId() + "' selected>");
                        } else {
                            sbf.append("<option value='" + confInfo.getId() + "'>");
                        }
                        sbf.append(confInfo.getConfname());
                        sbf.append("</option>");
                    }
                }
            }
            sbf.append("</select>");
            outPutOperResult(Constant.alert_succ, sbf.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "auth_get_conf_info_err"));
        }

        return null;
    }

    /**
     * 添加代理服务
     * @return
     * @throws Exception
     */
    public String selServer() throws Exception {
        String agentipaddr = request.getParameter("ipaddr"); // 认证代理IP
        String hostip = request.getParameter("hostipArr"); // 新添加的认证服务器IP
        String hostIps = request.getParameter("hostIps"); // 已添加的认证服器IP
        String[] hostipArr = null;
        String[] newhostipArr = null;

        if (StrTool.strNotNull(hostip)) {
            newhostipArr = hostip.split(",");
        }

        if (StrTool.strNotNull(hostIps)) {
            hostipArr = hostIps.split(",");
        }
        try {
            List<String> hidHostList = new ArrayList<String>();
            if (StrTool.arrNotNull(hostipArr)) {
                hidHostList = Arrays.asList(hostipArr); // 已添加的认证服器IP封装LIST
            }
            List<String> hostList = Arrays.asList(newhostipArr); // 新添加的认证服务器IP封装LIST

            // 组装代理和服务器关系需要的数据
            AgentServerInfo aServInfo = new AgentServerInfo();
            aServInfo.setAgentipaddr(agentipaddr);
            aServInfo.setAgentHostList(hidHostList);

            // 组装已添加和新添加代理IP的并集且去重复
            Set<String> set = new HashSet<String>();
            set.addAll(hostList);
            set.addAll(hidHostList);

            // SET转换LIST
            List<String> hosList = new ArrayList<String>();
            hosList.addAll(set);

            // 组装认证代理需要数据
            AgentInfo agentIn = new AgentInfo();
            agentIn.setAgentipaddr(agentipaddr);
            agentIn.setHostIps(hosList);

            //旧ServIP列表为空，新ServIP列表不为空，只执行添加对应操作
            if (!StrTool.listNotNull(hidHostList) && StrTool.listNotNull(hostList)) {
                addAgentHost(agentIn);
            }
            //旧ServIP列表不为空，新ServIP列表不为空，既执行取消对应关系又执行建立对应关系操作
            if (StrTool.listNotNull(hidHostList) && StrTool.listNotNull(hostList)) {
                agentServerServ.delObj(aServInfo);
                addAgentHost(agentIn);
            }
            //Start 记录日志使用到
            AgentServerInfo asInfo2 = new AgentServerInfo();
            asInfo2.setAgentipaddr(agentipaddr);
            asInfo2.setAgentHostList(hostList);
            this.setAgentServerInfo(asInfo2);
            //End 记录日志使用到

            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10006);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_add_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    
    /**
     * 返回当前时间或者当前时间加3天
     * @return
     * @throws Exception
     */
    public String getCurTimeLastThreeDay() throws Exception {
        String flag = request.getParameter("flag");
        if (StrTool.strEquals(flag, StrConstant.common_number_1)) {
            outPutOperResultString(Constant.alert_succ, DateTool.getCurDate("yyyy-MM-dd HH:mm:ss"));
        } else {
            outPutOperResultString(Constant.alert_succ, DateTool.getCurTimeLastThreeDay());
        }
        return null;
    }
    
    /**
     * 验证认证代理服务器名称是否存在
     * @Date in Dec 17, 2012,9:55:25 AM
     * @return
     */
    public String findHostnameisExist() {
        AgentInfo agentinfo = new AgentInfo();
        try {
            String agentname = agentInfo.getAgentname();
            if (!StrTool.strNotNull(agentname)) {
                return null;
            }
            agentname = MessyCodeCheck.iso885901ForUTF8(agentname);
            agentinfo.setAgentname(agentname);
            agentinfo = (AgentInfo) agentServ.find(agentinfo);
            if (StrTool.objNotNull(agentinfo)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

}
