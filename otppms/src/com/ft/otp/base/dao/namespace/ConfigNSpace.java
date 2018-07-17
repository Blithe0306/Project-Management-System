/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 配置管理命名空间
 *
 * @Date in Apr 6, 2011,4:05:31 PM
 *
 * @author TBM
 */
public class ConfigNSpace {

 
    //公共配置
    public static final String CONF_INFO_NS = "conf_Info";
 
 

    //通过conftype查询配置信息
    public static final String QUERY_CONF_BY_CONFTYPE = "queryConfInfo"; 
    //更新配置信息
    public static final String UPDATE_CENTER_CONF_INFO = "updateCenterConfInfo"; 
    //通过conftype,confname查找单个属性信息
    public static final String FIND_CONF_BY_CONFTYPE = "findConfInfo"; 
}
