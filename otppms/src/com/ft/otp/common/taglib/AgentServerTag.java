package com.ft.otp.common.taglib;

import java.util.*;
import org.apache.log4j.Logger;

import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.agentserver.entity.AgentServerInfo;
import com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ;

/**
 * 
 * 认证代理与认证服务器列表Tag
 * 根据agentIp获取serverIp
 * 再根据serverIp得到Server
 *
 * @Date in Apr 19, 2011,7:28:04 PM
 *
 * @author TBM
 */
public class AgentServerTag extends BaseTag {

    private static final long serialVersionUID = -5602151048529392046L;

    private Logger logger = Logger.getLogger(AgentServerTag.class);

    private IAgentServerServ agentServerServ = null;

    public AgentServerTag() {
        super();
        agentServerServ = (IAgentServerServ) AppContextMgr
                .getObject("agentServerServ");
    }

    private String dataSrc;

    // 输出
    public int doEndTag() {
        try {
            pageContext.getOut().print(this.optionStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return EVAL_PAGE;
    }

    private String optionStr() throws Exception {
        StringBuilder sb = new StringBuilder();
        List<?> hostList = new ArrayList<Object>();
        AgentServerInfo aServInfo = new AgentServerInfo();
        aServInfo.setAgentipaddr(dataSrc);
        hostList = agentServerServ.query(aServInfo, new PageArgument());

        Iterator<?> it = hostList.iterator();
        while (it.hasNext()) {
            AgentServerInfo agentHost = (AgentServerInfo) it.next();
            sb.append("<option value='").append(agentHost.getHostipaddr())
                    .append("' ");
            sb.append(">");
            sb.append(agentHost.getHostipaddr()).append("\n");
        }
        return sb.toString();
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }
}
