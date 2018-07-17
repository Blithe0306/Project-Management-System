/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 配置管理命名空间
 *
 * @Date in Apr 6, 2011,4:05:31 PM
 *
 * @author ZJY
 */
public class UserSourceNSpace {

    /**
     * 命名空间
     */

    //公共配置
    public static final String USERSOURCE_INFO_NS = "usersource_info";

    //用户来源定时时间信息
    public static final String TIMING_INFO = "timing_info";

    //插入定时信息
    public static final String INSERT_TIMING_INFO = "insertTG";
    //插入定时信息
    public static final String UPDATE_TIMING_INFO = "updateTG";
    //查找定时信息
    public static final String FIND_TIMING_INFO = "findTG";
    //查找定时信息
    public static final String QUERY_TIMING_INFO = "queryTG";
    //查找定时信息
    public static final String UPDATE_TIMING_STATE = "updateTGState";

    public static final String DEL_TIMING_INFO = "deleteTG";

    //用户来源
    public static final String INSERT_USERSOURCE_INFO = "insertUS";
    public static final String DEL_USERSOURCE_INFO = "deleteUS";
    public static final String UPDATE_USERSOURCE_INFO = "updateUS";
    public static final String COUNT_USERSOURCE_INFO = "countUS";
    public static final String QUERY_USERSOURCE_INFO = "queryUS";
    public static final String FIND_USERSOURCE_INFO = "findUS";
    
    //插入用户来源关联表属性
    public static final String INSERT_USERSOURCE_ATTR_INFO = "insertAttrUS";
    //查询用户来源关联表属性
    public static final String QUERY_USERSOURCE_ATTR_INFO = "queryAttrUS";
    //查询用户来源关联表属性
    public static final String DEL_USERSOURCE_ATTR_INFO = "deleteAttrUS";
    
}
