/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 认证管理DAO NSpace
 *
 * @Date in Apr 6, 2011,4:05:17 PM
 *
 * @author TBM
 */
public class AuthMgrNSpace {

    //命名空间

    //认证服务器
    public static final String SERVER_INFO_NS = "server_info";
    //认证代理配置
    public static final String AGENTCONF_INFO_NS = "agentconf_info";
    //认证代理
    public static final String AGENT_INFO_NS = "agent_info";
    //代理服务器
    public static final String AGENT_SERVER_INFO_NS = "agent_server_info";
    //后端认证
    public static final String Backend_INFO_NS = "backend_info";

    //引用标识

    //认证服务器
    public static final String SERVER_INFO_INSERT_SR = "insertSR";
    public static final String SERVER_INFO_DELETE_SR = "deleteSR";
    public static final String SERVER_INFO_UPDATE_SR = "updateSR";
    public static final String SERVER_INFO_COUNT_SR = "countSR";
    public static final String SERVER_INFO_SELECT_SR = "selectSR";
    public static final String SERVER_INFO_FIND_SR = "findSR";
    public static final String SERVER_INFO_UPDATE_IP = "updateHostIp";//修改服务器IP
    public static final String SERVER_INFO_UPDATE_LICID = "updateNewLicId";//更新最新授权文件

    //认证代理
    public static final String AGENT_INFO_INSERT_AT = "insertAT";
    public static final String AGENT_INFO_DELETE_AT = "deleteAT";
    public static final String AGENT_INFO_UPDATE_AT = "updateAT";
    public static final String AGENT_INFO_COUNT_AT = "countAT";
    public static final String AGENT_INFO_SELECT_AT = "selectAT";
    public static final String AGENT_INFO_FIND_AG = "findAG";
    public static final String AGENT_INFO_FIND_AT = "findAT";
    public static final String AGENT_INFO_UPDATE_ENABLED = "updateEnabled";

    //代理服务器（认证服务器和认证代理的关联表）
    public static final String AGENT_SERVER_INFO_INSERT_AS = "insertAS";
    //根据代理服务器IP地址去，中间表中查找服务器信息
    public static final String AGENT_SERVER_INFO_SELECT_AS = "selectAS";
    //根據代理服務器IP地址，刪除中間表的數據
    public static final String AGENT_SERVER_INFO_DELETE_AS = "deleteAS";
    //根据代理IP关联查询对应的认证服务器信息  或 根据认证服务器IP关联查询对应的代理信息
    public static final String SELECTAS_JOIN_SERV = "selectAS_Join_Serv";

    //后端认证
    public static final String Backend_INFO_SELECT_BD = "selectBD";
    public static final String Backend_INFO_COUNT_BD = "countBD";
    public static final String Backend_INFO_ADD_BD = "insertBD";
    public static final String Backend_INFO_DEL_BD = "deleteBD";
    public static final String Backend_INFO_FIND_BD = "findBD";
    public static final String Backend_INFO_UPDATE_BD = "updateBD";
    public static final String Backend_INFO_FIND_UKBD = "findUKBD";
    //启用、禁用后端认证
    public static final String Backend_INFO_ENABLED_BD = "enabledBD";

    //认证代理配置
    public static final String AGENTCONF_INFO_SELECT_ATCONF = "selectATConf";
    public static final String AGENTCONF_INFO_COUNT_ATCONF = "countATConf";
    public static final String AGENTCONF_INFO_INSERT_ATCONF = "insertATConf";
    public static final String AGENTCONF_INFO_FIND_ATCONF = "findATConf";
    public static final String AGENTCONF_INFO_UPDATE_ATCONF = "updateATConf";
    public static final String AGENTCONF_INFO_DELETE_ATCONF = "deleteATConf";
    public static final String AGENTCONF_INFO_SELECT_ATCONFLIST = "selectATConfList";
}
