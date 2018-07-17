/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.entity;

import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证代理实体类功能说明
 *
 * @Date in Apr 12, 2011,6:44:24 PM
 *
 * @author ZJY
 */
public class AgentInfo extends BaseEntity {

    private String agentipaddr;
    private String pubkey;
    private String descp;
    private int enabled = 1;
    
    private int agenttype; //认证代理类型，多选。1：扩展Radius客户端；2：标准Radius客户端；4：Windows登录保护认证代理；8：Linux登录保护认证代理；
                            //16：IIS认证代理；32：Apache认证代理；64：SOAP客户端；128：HTTP客户端；256：HTTPS客户端
    private int agentconfid; //认证代理配置ID
    
    private int graceperiod;//Windows登录保护认证代理的宽限期，宽限期时间段内允许不使用动态口令登录保护
    private String graceperiodStr;//宽限期字符串
    
    private List<?> hostIps; //认证服务器IP列表

    private List<?> hidHostIps; //隐藏域认证服务器IP列表

    private String hostIpAddr;
    
    private String agenttypeStr; //
    private String agentconfStr;
    private int flag; // 判断页面查看是否显示登录保护认证代理宽限期
    private String agentname;//认证代理名称
    
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getAgentconfStr() {
        return agentconfStr;
    }

    public void setAgentconfStr(String agentconfStr) {
        this.agentconfStr = agentconfStr;
    }

    public String getAgenttypeStr() {
        return agenttypeStr;
    }

    public void setAgenttypeStr(String agenttypeStr) {
        this.agenttypeStr = agenttypeStr;
    }

    public AgentInfo() {
    }

    public AgentInfo(String strAgent, String strPubkey, String strDesc,
            int groupId) {
        this.agentipaddr = strAgent;
        this.pubkey = strPubkey;
        this.descp = StrTool.cleanXSS(strDesc);
    }

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

    public String getDescp() {
        return descp;
    }

    public void setDescp(String strDescp) {
        this.descp = StrTool.cleanXSS(strDescp);
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String strKey) {
        this.pubkey = strKey;
    }

    /**
     * @return the hostIps
     */
    public List<?> getHostIps() {
        return hostIps;
    }

    /**
     * @param hostIps the hostIps to set
     */
    public void setHostIps(List<?> hostIps) {
        this.hostIps = hostIps;
    }

    /**
     * @return the hidHostIps
     */
    public List<?> getHidHostIps() {
        return hidHostIps;
    }

    /**
     * @param hidHostIps the hidHostIps to set
     */
    public void setHidHostIps(List<?> hidHostIps) {
        this.hidHostIps = hidHostIps;
    }

    /**
     * @return the hostIpAddr
     */
    public String getHostIpAddr() {
        return hostIpAddr;
    }

    /**
     * @param hostIpAddr the hostIpAddr to set
     */
    public void setHostIpAddr(String hostIpAddr) {
        this.hostIpAddr = hostIpAddr;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getAgenttype() {
        return agenttype;
    }

    public void setAgenttype(int agenttype) {
        this.agenttype = agenttype;
    }

    public int getAgentconfid() {
        return agentconfid;
    }

    public void setAgentconfid(int agentconfid) {
        this.agentconfid = agentconfid;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public void setGraceperiod(int graceperiod) {
        this.graceperiod = graceperiod;
    }

    public String getGraceperiodStr() {
        return graceperiodStr;
    }

    public void setGraceperiodStr(String graceperiodStr) {
        this.graceperiodStr = graceperiodStr;
        if (StrTool.strNotNull(graceperiodStr)) {
            this.graceperiod = DateTool.dateToInt(DateTool.stringToDate(graceperiodStr));
        }
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

}
