/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agentserver.entity;

import java.util.List;
import com.ft.otp.base.entity.BaseEntity;

/**
 * 代理和服务器关系实体类功能说明
 *
 * @Date in Apr 13, 2011,1:02:06 PM
 *
 * @author ZJY
 */
public class AgentServerInfo extends BaseEntity {

    private String agentipaddr;
    private String hostipaddr;
    private int confid;
    private List<?> agentHostList;

    /**
     * @return the agentipaddr
     */
    public String getAgentipaddr() {
        return agentipaddr;
    }

    /**
     * @param agentipaddr the agentipaddr to set
     */
    public void setAgentipaddr(String agentipaddr) {
        this.agentipaddr = agentipaddr;
    }

    /**
     * @return the hostipaddr
     */
    public String getHostipaddr() {
        return hostipaddr;
    }

    /**
     * @param hostipaddr the hostipaddr to set
     */
    public void setHostipaddr(String hostipaddr) {
        this.hostipaddr = hostipaddr;
    }

    /**
     * @return the agentHostList
     */
    public List<?> getAgentHostList() {
        return agentHostList;
    }

    /**
     * @param agentHostList the agentHostList to set
     */
    public void setAgentHostList(List<?> agentHostList) {
        this.agentHostList = agentHostList;
    }

    /**
     * @return the confid
     */
    public int getConfid() {
        return confid;
    }

    /**
     * @param confid the confid to set
     */
    public void setConfid(int confid) {
        this.confid = confid;
    }

}
