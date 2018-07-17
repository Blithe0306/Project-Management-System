/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.server.form;

import com.ft.otp.base.form.BaseQueryForm;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;

/**
 * Server查询Form
 *
 * @Date in Apr 11, 2011,1:49:57 PM
 *
 * @author ZJY
 */
public class ServerQueryForm extends BaseQueryForm {

    private String hostipaddr; //查询条件认证服务器IP
    private String hostname;
    private ServerInfo serverInfo = new ServerInfo();

    /**
     * @return the serverInfo
     */
    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    /**
     * @param serverInfo the serverInfo to set
     */
    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
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
        serverInfo.setHostipaddr(hostipaddr.trim());
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
        serverInfo.setHostname(hostname.trim());
    }

}
