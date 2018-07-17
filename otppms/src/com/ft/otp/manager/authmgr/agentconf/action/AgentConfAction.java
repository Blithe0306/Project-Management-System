/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentconf.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.AgentPubConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;
import com.ft.otp.manager.authmgr.agent.service.IAgentServ;
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ft.otp.manager.authmgr.agentconf.service.IAgentConfServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证代理配置表业务action
 *
 * @Date in Jan 28, 2013,4:20:25 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AgentConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 1579716950160508387L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(AgentConfAction.class);

    //代理服务器服务接口
    private IAgentServ agentServ = (IAgentServ) AppContextMgr.getObject("agentServ");

    private IAgentConfServ agentConfServ;
    private AgentConfInfo agentConfInfo;
    private AgentConfInfo oldAgentConfInfo;
    
    public AgentConfInfo getOldAgentConfInfo() {
		return oldAgentConfInfo;
	}

	public void setOldAgentConfInfo(AgentConfInfo oldAgentConfInfo) {
		this.oldAgentConfInfo = oldAgentConfInfo;
	}

	public IAgentConfServ getAgentConfServ() {
        return agentConfServ;
    }

    public void setAgentConfServ(IAgentConfServ agentConfServ) {
        this.agentConfServ = agentConfServ;
    }

    public AgentConfInfo getAgentConfInfo() {
        return agentConfInfo;
    }

    public void setAgentConfInfo(AgentConfInfo agentConfInfo) {
        this.agentConfInfo = agentConfInfo;
    }

    public AgentConfInfo getAgentConf() {
        if (!StrTool.objNotNull(agentConfInfo)) {
            agentConfInfo = new AgentConfInfo();
        }
        return agentConfInfo;
    }

    /**
     * 认证代理配置添加操作
     */
    public String add() {
        try {
            if (StrTool.objNotNull(agentConfInfo)) {
                agentConfServ.addObj(agentConfInfo);

                AgentPubConfig.reload();
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10007);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_add_succ_tip"));
            } else {
                return init();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
        }

        return null;
    }

    /**
     * 删除认证代理配置信息
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delIds");
        Iterator<?> iter = keySet.iterator();
        try {
            if (StrTool.setNotNull(keySet)) {

                AgentInfo agentInfo = null;
                List<?> agenList = new ArrayList<Object>();
                String noDelIps = "";
                String noDelNames = "";
                while (iter.hasNext()) {
                    String keyId = (String) iter.next();
                    AgentConfInfo agentConf = getAgentConf();
                    agentConf.setId(Integer.parseInt(keyId));

                    // 根据ID查询代表理置的信息
                    agentConf = (AgentConfInfo) agentConfServ.find(agentConf);

                    agentInfo = new AgentInfo();
                    agentInfo.setAgentconfid(Integer.parseInt(keyId));

                    // 根据认证代理配置ID查询认证代理表，是否此代理配置已经被认证代理使用
                    agenList = agentServ.findAgent(agentInfo);

                    // 拼接认证代理配置名称
                    if (StrTool.listNotNull(agenList)) {
                        noDelIps += keyId + ",";
                        noDelNames += agentConf.getConfname() + ",";
                    }
                }

                if (!StrTool.strNotNull(noDelIps)) {
                    agentConfServ.delObj(keySet);

                    AgentPubConfig.reload();
                    SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10007);

                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
                } else {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_conf_agent_sel")
                            + noDelNames.substring(0, noDelNames.length() - 1)
                            + Language.getLangStr(request, "auth_agent_sel_already_ser"));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
        }

        return null;
    }

    /**
     * 查找认证代理配置信息
     */
    public String find() {
        String id = request.getParameter("confid");
        AgentConfInfo agentConf = getAgentConf();
        try {
            agentConf.setId(Integer.parseInt(id));
            agentConf = (AgentConfInfo) agentConfServ.find(agentConf);
            if (!StrTool.objNotNull(agentConf)) {
                return init();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setAgentConfInfo(agentConf);
        return "edit";
    }

    /**
     * 认证代理配置初始化
     */
    public String init() {
        AgentConfInfo agentConf = getAgentConf();
        try {
            PageArgument pageArg = pageArgument(agentConf);
            pageArg.setPageSize(getPagesize());
            pageArg.setCurPage(getPage());

            List<?> resultList = query(pageArg);
            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取总数分页信息
     * @Date in Jan 28, 2013,5:04:30 PM
     * @param agentConf
     * @return
     * @throws Exception
     */
    private PageArgument pageArgument(AgentConfInfo agentConf) throws Exception {
        int totalRow = 0;
        totalRow = agentConfServ.count(agentConf);
        PageArgument pageArg = getArgument(totalRow);
        return pageArg;
    }

    /**
     * 获取认证代理配置数据
     * @Date in Jan 28, 2013,5:10:05 PM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        List<?> agentConfList = null;
        try {
            agentConfList = agentConfServ.query(agentConfInfo, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return agentConfList;
    }

    /**
     * 编辑修改
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(agentConfInfo)) {
            	
            	// Start,日志处理
            	AgentConfInfo agentConf = new AgentConfInfo();
            	agentConf.setId(agentConfInfo.getId());
            	agentConf = (AgentConfInfo) agentConfServ.find(agentConf);
            	this.setOldAgentConfInfo(agentConf);
                // End,日志处理
            	
            	agentConfServ.updateObj(agentConfInfo);

                AgentPubConfig.reload();
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10007);

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
                return init();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#page()
     */
    public String page() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#view()
     */
    public String view() {
        return null;
    }

    public String checkConfName() {
        AgentConfInfo agentConf = new AgentConfInfo();
        try {
            String confName = agentConfInfo.getConfname();
            if (!StrTool.strNotNull(confName)) {
                return null;
            }
            confName = MessyCodeCheck.iso885901ForUTF8(confName);
            agentConf.setConfname(confName);
            agentConf = (AgentConfInfo) agentConfServ.find(agentConf);
            if (StrTool.objNotNull(agentConf)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
