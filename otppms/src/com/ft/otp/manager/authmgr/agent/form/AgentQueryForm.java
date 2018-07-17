/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;

/**
 * 认证代理查询Form
 *
 * @Date in Apr 12, 2011,6:44:14 PM
 *
 * @author ZJY
 */
public class AgentQueryForm extends BaseQueryForm {

    private String agentIPaddr;//查询条件
    private int agentType; //认证代理类型，多选。1：扩展Radius客户端；2：标准Radius客户端；4：Windows登录保护认证代理；8：Linux登录保护认证代理；
    //16：IIS认证代理；32：Apache认证代理；64：SOAP客户端；128：HTTP客户端；256：HTTPS客户端
    private int agentConfid; //认证代理配置ID
    private String hostIPaddr;

    AgentInfo agentfo = new AgentInfo();

    /**
     * @return the agentfo
     */
    public AgentInfo getAgentfo() {
        return agentfo;
    }

    /**
     * @param agentfo the agentfo to set
     */
    public void setAgentfo(AgentInfo agentfo) {
        this.agentfo = agentfo;
    }

    /**
     * @return the agentIPaddr
     */
    public String getAgentIPaddr() {
        return agentIPaddr;
    }

    /**
     * @param agentIPaddr the agentIPaddr to set
     */
    public void setAgentIPaddr(String agentIPaddr) {
        this.agentIPaddr = agentIPaddr;
        agentfo.setAgentipaddr(agentIPaddr.trim());
    }

    /**
     * @return the agentType
     */
    public int getAgentType() {
        return agentType;
    }

    /**
     * @param agentType the agentType to set
     */
    public void setAgentType(int agentType) {
        this.agentType = agentType;
        if(agentType!=-1){
            int[] types = new int[3];
            types[0] = agentType;
            if(agentType!=1073741824){
                types[1] = 1073741824+ agentType;
            }else{
                types[1] = 1073741824 + 4;
                types[2] = 1073741824 + 8;
            }
            
            agentfo.setBatchIdsInt(types);
        }
        agentfo.setAgenttype(agentType);
    }

    /**
     * @return the agentConfid
     */
    public int getAgentConfid() {
        return agentConfid;
    }

    /**
     * @param agentConfid the agentConfid to set
     */
    public void setAgentConfid(int agentConfid) {
        this.agentConfid = agentConfid;
        agentfo.setAgentconfid(agentConfid);
    }

    /**
     * @return the hostIPaddr
     */
    public String getHostIPaddr() {
        return hostIPaddr;
    }

    /**
     * @param hostIPaddr the hostIPaddr to set
     */
    public void setHostIPaddr(String hostIPaddr) {
        this.hostIPaddr = hostIPaddr;
        agentfo.setHostIpAddr(hostIPaddr.trim());
    }

}
