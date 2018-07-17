/**
 *Administrator
 */
package com.ft.otp.manager.monitor.entity;

/**
 * 设备信息实体类
 *
 * @Date in Feb 27, 2013,3:29:40 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class EquipmentInfo {
    private String ipAddr; // ip地址
    private int port;// 端口
    private String clientServPath; // 管理中心请求的服务路径
    private String serverName; // 服务器名称
    private String serverType; // 服务器类型
    private long onlineUsers; // 在线用户数
    private String runState; // 运行状态

    /**
     * @return the ipAddr
     */
    public String getIpAddr() {
        return ipAddr;
    }

    /**
     * @param ipAddr the ipAddr to set
     */
    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return the serverType
     */
    public String getServerType() {
        return serverType;
    }

    /**
     * @param serverType the serverType to set
     */
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    /**
     * @return the onlineUsers
     */
    public long getOnlineUsers() {
        return onlineUsers;
    }

    /**
     * @param onlineUsers the onlineUsers to set
     */
    public void setOnlineUsers(long onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    /**
     * @return the runState
     */
    public String getRunState() {
        return runState;
    }

    /**
     * @param runState the runState to set
     */
    public void setRunState(String runState) {
        this.runState = runState;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the clientServPath
     */
    public String getClientServPath() {
        return clientServPath;
    }

    /**
     * @param clientServPath the clientServPath to set
     */
    public void setClientServPath(String clientServPath) {
        this.clientServPath = clientServPath;
    }

}
