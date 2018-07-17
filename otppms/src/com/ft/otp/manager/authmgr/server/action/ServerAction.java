/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.log4j.Logger;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.LicenseConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.config.LicenseConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.WebServiceFactory;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.agentserver.entity.AgentServerInfo;
import com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.form.ServerQueryForm;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证管理-认证服务器业务功能说明
 *
 * @Date in Apr 11, 2011,1:58:56 PM
 *
 * @author ZJY
 */
public class ServerAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 1328213013758006219L;

    private Logger logger = Logger.getLogger(ServerAction.class);
    //认证服务器
    private IServerServ serverServ;
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    //代理服务器（认证服务和代理的中间表）
    private IAgentServerServ agentServerServ = (IAgentServerServ) AppContextMgr.getObject("agentServerServ");

    private static JaxWsDynamicClientFactory clientFactory = null;
    
    private ServerQueryForm serverQueryForm = null;
    private ServerInfo serverInfo;
    private ServerInfo oldServerInfo;
    
    public ServerInfo getOldServerInfo() {
		return oldServerInfo;
	}

	public void setOldServerInfo(ServerInfo oldServerInfo) {
		this.oldServerInfo = oldServerInfo;
	}

	/**
     * @return the serverServ
     */
    public IServerServ getServerServ() {
        return serverServ;
    }

    /**
     * @param serverServ the serverServ to set
     */
    public void setServerServ(IServerServ serverServ) {
        this.serverServ = serverServ;
    }

    /**
     * @return the serverQueryForm
     */
    public ServerQueryForm getServerQueryForm() {
        return serverQueryForm;
    }

    /**
     * @param serverQueryForm the serverQueryForm to set
     */
    public void setServerQueryForm(ServerQueryForm serverQueryForm) {
        this.serverQueryForm = serverQueryForm;
    }

    /**
     * 取出QueryForm中的实体
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    public ServerInfo getServerInfo(ServerQueryForm serverQueryForm) {
        ServerInfo serInfo = new ServerInfo();
        if (StrTool.objNotNull(serverQueryForm)) {
            serInfo = serverQueryForm.getServerInfo();
        }
        return serInfo;
    }

    /**
     * @param serverInfo the serverInfo to set
     */
    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     * 添加认证服务器
     */
    public String add() {
        try {
            if (StrTool.objNotNull(serverInfo)) {
                //有效授权允许的服务器节点数
                int serNodes = (Integer) LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_SERVERNODES);
                //已经存在的服务器节点数
                int hostips = serverServ.count(new ServerInfo());
                if (hostips < serNodes || serNodes == -1) {
                    serverServ.addObj(serverInfo);
                    // 设置主或备服务器
                    setIpServer(serverInfo);
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "auth_ser_add_conf_succ"));
                } else {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_ser_check_lic_sernodes"));
                }
            } else {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_ser_info_is_null"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    
    /**
     * 设置主或备服务器
     * @author LXH
     * @date Nov 25, 2014 10:53:28 AM
     * @param serverInfo
     * @throws Exception 
     */
    public void setIpServer(ServerInfo serverInfo) throws Exception{
    	boolean flag = true; // 是否设置备服务的标志
    	// 主认证服务
    	String hostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.MAIN_HOSTIPADDR); 
    	// 检查主服务器是否“认证服务”不存在
    	if(checkService(hostIp)){
    		flag = false;
    		// 不存在设置为主服务
    		ConfigInfo mainHostIpaddrConf = new ConfigInfo(ConfConstant.MAIN_HOSTIPADDR,
    				serverInfo.getHostipaddr(), ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
    		// 更新主服务器配置
    		confInfoServ.updateObj(mainHostIpaddrConf);
    		//重新加载配置缓存
            ConfConfig.reLoad();
    	}
    	if(flag){
    		// 备认证服务
            String spareHostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.SPARE_HOSTIPADDR);	
        	// 检查备服务器“认证服务”是否不存在
            if(checkIp(hostIp, spareHostIp)){
            	// 不存在设置为备服务
            	ConfigInfo spareHostIpaddrConf = new ConfigInfo(ConfConstant.SPARE_HOSTIPADDR, 
            			serverInfo.getHostipaddr(), ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
            	// 更新备服务器配置
            	confInfoServ.updateObj(spareHostIpaddrConf);
            	//重新加载配置缓存
                ConfConfig.reLoad();
            }
    	}
    }
    
    /**
     * 检查IP是否在认证服务列表中
     * @author LXH
     * @date Nov 25, 2014 10:53:22 AM
     * @param ip
     * @return
     */
    public boolean checkService(String ip){
    	ServerInfo serverInfo = new ServerInfo();
    	try {
    		serverInfo.setHostipaddr(ip);
			serverInfo = (ServerInfo) serverServ.find(serverInfo);
			// 如果不存在
			if(!StrTool.objNotNull(serverInfo)){
				return true;
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    /**
     * 检查备IP是否在认证服务列表中，且主备IP是否相等
     * @author LXH
     * @date Nov 25, 2014 10:53:22 AM
     * @param ip
     * @return
     */
    public boolean checkIp(String mainIp, String spareIP){
    	try {
    		// 如果主和备服务IP相同
    		if(StrTool.strEquals(mainIp, spareIP)){
    			return true;
    		}else{
    			ServerInfo serverInfo = new ServerInfo();
        		serverInfo.setHostipaddr(spareIP);
    			serverInfo = (ServerInfo) serverServ.find(serverInfo);
    			// 如果不存在
    			if(!StrTool.objNotNull(serverInfo)){
    				return true;
    			}
    		}
		} catch (BaseException e) {
			e.printStackTrace();
		}
    	return false;
    }

    /**
     * 删除认证服务器
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delHostIps");
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
                    aServInfo.setHostipaddr(keyId);
                    agenList = agentServerServ.query(aServInfo, new PageArgument());

                    if (StrTool.listNotNull(agenList)) {
                        noDelIps += keyId + ",";
                    } else {
                        newSet.add(keyId);
                    }
                }
                if (StrTool.setNotNull(newSet) && !StrTool.strNotNull(noDelIps)) {
                    serverServ.delObj(keySet);
                }
                if (StrTool.strNotNull(noDelIps)) {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_ser_sel")
                            + noDelIps.substring(0, noDelIps.length() - 1)
                            + Language.getLangStr(request, "auth_ser_sel_already_use"));
                } else {
                    reloadAuthServer(request.getParameter("delHostIps"), SoapAttribute.RECONFIG_TYPE_10011);
                    // 设置主备服务
                    if(ipServerExist()){
                    	outPutOperResult(Constant.alert_question, Language.getLangStr(request, "auth_del_succ_skip"));
                    }else{
                    	outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
                    }
                }
            } catch (BaseException e) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
                logger.error(e.getMessage(), e);
            }
        }

        return null;
    }
    
    /**
     * 检查主和备服务在服务器列表是否都不存在
     * @author LXH
     * @date Nov 25, 2014 11:16:39 AM
     * @return
     */
    public boolean ipServerExist(){
    	try {
    		// 检查认证服务列表个数
			int totalRow = serverServ.count(new ServerInfo());
			// 至少有一个认证服务器
			if(totalRow > 0){
				// 主认证服务
		    	String hostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.MAIN_HOSTIPADDR);
		    	// 备认证服务
		        String spareHostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.SPARE_HOSTIPADDR);
		        // 如果主和备服务在服务器列表都没有
		        if(checkService(hostIp) && checkService(spareHostIp)){
		        	return true;
		        }
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}
    	return false;
    }

    /**
     * 编辑认证服务器前，初始化认证服务器
     */
    public String find() {
        ServerInfo serverinfos = new ServerInfo();
        String hostipaddr = request.getParameter("hostipaddr");
        serverinfos.setHostipaddr(hostipaddr);
        try {
            serverinfos = (ServerInfo) serverServ.find(serverinfos);
            if (!StrTool.objNotNull(serverinfos)) {
                return init();
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        this.setServerInfo(serverinfos);

        return SUCC_FIND;
    }

    /**
     * 新增认证服务器，根据IP地址查询认证服务器是否已经存在
     */
    public String findbyIPStr() throws IOException {
        ServerInfo servInfo = new ServerInfo();
        try {
            String hostip = serverInfo.getHostipaddr();
            if (!StrTool.strNotNull(hostip)) {
                return null;
            }
            servInfo.setHostipaddr(hostip);
            servInfo = (ServerInfo) serverServ.find(servInfo);
            if (StrTool.objNotNull(servInfo)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 初始化认证服务器列表
     */
    public String init() {
        if (isNeedClearForm()) {
            serverQueryForm = null;
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
        List<?> serverList = null;
        try {
            serverList = serverServ.query(getServerInfo(serverQueryForm), pageArg);

            if (!StrTool.listNotNull(serverList)) {
                serverList = new ArrayList<Object>();
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }

        return serverList;
    }

    /**  
     * 修改认证服务器
     */
    public String modify() {
        try {
        	
        	// Start,用于日志
        	ServerInfo serverIn = new ServerInfo();
        	serverIn.setHostipaddr(serverInfo.getHostipaddr());
        	serverIn = (ServerInfo) serverServ.find(serverIn);
        	this.setOldServerInfo(serverIn);
        	// End,用于日志
        	
            serverServ.updateObj(serverInfo);

            //发送服务器配置信息修改请求
            reloadAuthServer(serverInfo.getHostipaddr(), SoapAttribute.RECONFIG_TYPE_10002);

            //重新创建本地SOAP连接
            WebServiceFactory.loadWebServiceFactory();

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 通知远程的服务器进行服务器配置修改的加载
     * 
     * @Date in Jul 6, 2013,4:03:52 PM
     * @param serverIp
     */
    private void reloadAuthServer(String serverIp, int reType) {
        String[] delIdArr = null;
        if (StrTool.indexOf(serverIp, ",")) {
            delIdArr = serverIp.split(",");
        } else {
            delIdArr = new String[1];
            delIdArr[0] = serverIp;
        }
        boolean isExist = false;
        String hostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.MAIN_HOSTIPADDR);
        String spareHostIp = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER, ConfConstant.SPARE_HOSTIPADDR);
        for (int i = 0; i < delIdArr.length; i++) {
            if (StrTool.strEquals(hostIp, delIdArr[i]) || StrTool.strEquals(spareHostIp, delIdArr[i])) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            SyncHelper.replaceConf(reType);
        }
    }

    /**
     * 系统初始化时，如果服务器IP为本地的127.0.0.1，则进行IP修改
     * 
     * @Date in May 15, 2013,2:01:28 PM
     * @return
     */
    public String modifyLocalIp() {
        String newHostipaddr = request.getParameter("ipSel");
        serverInfo = new ServerInfo();
        serverInfo.setHostipaddr("127.0.0.1");
        try {
            serverInfo = (ServerInfo) serverServ.find(serverInfo);
            if (null == serverInfo) {
            	addServerInfo(newHostipaddr);
        		setMainServerIp(newHostipaddr);//设置主认证服务器地址
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10002);
                request.getSession().getServletContext().setAttribute(Constant.DATABASE_IF_LOCALIP, false);
                setResponseWrite(Constant.alert_succ);

                return null;
            }
            serverInfo.setNewHostipaddr(newHostipaddr);
            serverServ.updateHostIp(serverInfo);
            setMainServerIp(newHostipaddr);//设置主认证服务器地址
            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10002);

            request.getSession().getServletContext().setAttribute(Constant.DATABASE_IF_LOCALIP, false);
            setResponseWrite(Constant.alert_succ);

            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        setResponseWrite(Constant.alert_error);

        return null;
    }
    
    /**
     * 添加默认认证服务
     * @author LXH
     * @date Nov 18, 2014 8:59:07 PM
     * @param newHostipaddr
     */
    public void addServerInfo(String newHostipaddr){
		try {
			serverInfo = new ServerInfo();
			serverInfo.setHostipaddr(newHostipaddr);
			serverInfo.setHostname("localhost");
			serverInfo.setPriority(1);
			serverInfo.setLicid("oBHr5L5gjJumsHGWIc2lPA==");
			serverInfo.setFtradiusenabled(1);
			serverInfo.setProtocoltype("udp");
			serverInfo.setAuthport(1915);
			serverInfo.setSyncport(1916);
			serverInfo.setRadiusenabled(1);
			serverInfo.setRadauthport(1812);
			serverInfo.setSoapenabled(1);
			serverInfo.setSoapport(18081);
			serverInfo.setWebservicename("otpwebservice");
			serverServ.addObj(serverInfo);
		} catch (BaseException e) {
			e.printStackTrace();
		}
    }
    
   /**
    * 检测认证服务器ip是否为127.0.0.1 
    * @Date   Oct 24, 2014,6:05:59 PM
    * @author WYJ
    * @return
    * @return String
    * @throws
    * 系统安装成功后认证服务器ip地址一定为127.0.0.1，当在安装时选择了从备份的数据文件中恢复数据库时 认证服务器ip地址不一定
    * 为127.0.0.1。这时在安装的时候进行校验，并将系统启动时认为应该进行认证服务器设置全局变量进行修改
    */
    public String isLocalIp() {
        
        try {
            ServerInfo serverInfo = new ServerInfo();
            List<?> list = serverServ.query(serverInfo, new PageArgument());
            if (!StrTool.listNotNull(list)) {
                setResponseWrite(Constant.alert_error);
                return null;
            }
            int size = list.size();
            String servIp = "";
            if (size == NumConstant.common_number_1) {
                serverInfo = (ServerInfo) list.get(0);
                servIp = serverInfo.getHostipaddr();
                if (StrTool.strEquals(servIp, "127.0.0.1") || StrTool.strEquals(servIp, "localhost")) {//为本地IP
                    setResponseWrite(Constant.alert_error);
                    return null;
                }else{//不是系统初始的认证服务器ip时不进行认证服务器ip设置
                    request.getSession().getServletContext().setAttribute(Constant.DATABASE_IF_LOCALIP, false);
                    setResponseWrite(Constant.alert_succ);
                    return null;
                }
            }else{//通过数据文件恢复到数据库多个认证服务器时，也不进行认证服务器ip设置
                request.getSession().getServletContext().setAttribute(Constant.DATABASE_IF_LOCALIP, false);
                setResponseWrite(Constant.alert_succ);
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        setResponseWrite(Constant.alert_error);

        return null;
    }

    /**
     * 设置主认证服务器地址
     * 
     * @Date in Apr 28, 2013,10:55:17 AM
     */
    private void setMainServerIp(String hostIp) {
        try {
            ConfigInfo langConf = new ConfigInfo(ConfConstant.MAIN_HOSTIPADDR, hostIp, ConfConstant.CONF_TYPE_CENTER,
                    NumConstant.common_number_0, "主认证服务器地址");
            confInfoServ.updateObj(langConf);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 页面分页跳转
     */
    public String page() {
        PageArgument pageArg = getArgument(request, 0);
        pageArg.setCurPage(getPage());
        pageArg.setPageSize(getPagesize());
        List<?> resultList = query(pageArg);
        String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
        setResponseWrite(jsonStr);
        //请求来源 － 添加代理时，选择认证服务器，初始化服务器列表时的请求标识
        if (StrTool.strNotNull(super.getSource(request))) {
            return "sel_agent";
        }

        return null;
    }

    /**
     * 行数统计
     * 分页处理
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    private PageArgument pageArgument() throws BaseException {
        int totalRow = serverServ.count(getServerInfo(serverQueryForm));
        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 查看认证服务器详细信息
     */
    public String view() {
        ServerInfo servInfo = null;
        List<?> agentIps = null;
        try {
            //服务器信息
            servInfo = (ServerInfo) serverServ.find(serverInfo);
            if (!StrTool.objNotNull(servInfo)) {
                return init();
            }
            //代理列表
            agentIps = getServToAgent();
            if (StrTool.listNotNull(agentIps)) {
                servInfo.setAgentAddrips(agentIps);
            }
        } catch (BaseException e) {
            logger.error(e.getMessage(), e);
        }
        this.setServerInfo(servInfo);

        return SUCC_VIEW;
    }

    /**
     * 根据认证服务器IP去中间表查询关联的认证代理IP
     * @Date in Jun 16, 2011,5:54:57 PM
     * @return
     * @throws BaseException
     */
    public List<?> getServToAgent() throws BaseException {
        List<?> agenList = new ArrayList<Object>();
        AgentServerInfo aServInfo = new AgentServerInfo();
        aServInfo.setHostipaddr(serverInfo.getHostipaddr());
        agenList = agentServerServ.query(aServInfo, new PageArgument());

        return agenList;
    }

    /**
     * 验证认证服务器名称是否存在
     * @Date in Dec 17, 2012,9:55:25 AM
     * @return
     */
    public String findHostnameisExist() {
        ServerInfo servInfo = new ServerInfo();
        try {
            String hostname = serverInfo.getHostname();
            if (!StrTool.strNotNull(hostname)) {
                return null;
            }
            hostname = MessyCodeCheck.iso885901ForUTF8(hostname);
            servInfo.setHostname(hostname);
            servInfo = (ServerInfo) serverServ.find(servInfo);
            if (StrTool.objNotNull(servInfo)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 验证授权文件允许添加服务器授权节点数
     * @Date in Jul 1, 2013,3:18:34 PM
     * @return
     */
    public String checkLicSerNodes() {
    	// 重新加载授权
    	LicenseConfig.reloadLicenceInfo();
        //有效授权允许的服务器节点数
        int serNodes = (Integer) LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_SERVERNODES);
        //授权类型
        int licType = (Integer) LicenseConfig.getValue(LicenseConstant.OTP_SERVER_LIC_LICTYPE);
        //已经存在的服务器节点数
        int hostips;
        try {
            hostips = serverServ.count(new ServerInfo());
            if (hostips < serNodes || serNodes == -1) {
                outPutOperResult(Constant.alert_succ, "success");
            } else {
                if (licType == LicenseConstant.LICENSE_TYPE_EVALUATION) {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_ser_check_lic_eval_tip"));
                } else if (licType == LicenseConstant.LICENSE_TYPE_ENTERPRISE) {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_ser_check_lic_busi_tip"));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
    
    /**
     * 获取认证服务器状态
     * @return String
     * @throws
     */
    public String loadServerState() {
        String hostIp = request.getParameter("hostip");
        Client client = null;
        try {
            ServerInfo servInfo = new ServerInfo();
            servInfo.setHostipaddr(hostIp);
            servInfo = (ServerInfo) serverServ.find(servInfo);
            
            if (null == clientFactory) {
                clientFactory = JaxWsDynamicClientFactory.newInstance();
            }
            if (!StrTool.objNotNull(servInfo)) {
                outPutOperResult(Constant.alert_error, "error");
            }
            //sop请求URL
            String sopUrl = "http://" + servInfo.getHostipaddr() + ":" + servInfo.getSoapport() + "/" 
                            + servInfo.getWebservicename() + "?WSDL";
            client = clientFactory.createClient(sopUrl);
            connTest(client); //连接测试
            
            if (client == null) {
                outPutOperResult(Constant.alert_error, "error");
            } else {
                outPutOperResult(Constant.alert_succ, "succ");
            }
            
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            outPutOperResult(Constant.alert_error, "error");
        }
        
        return null;
    }
    
    /**
     * 连接测试，通过调用获取服务器状态接口
     * @return
     */
    private void connTest(Client client) throws Exception {
        Object[] param = new Object[] {};
        client.invoke(SoapAttribute.REQUEST_METHOD_SERVERSTATE, param);
    }

}
