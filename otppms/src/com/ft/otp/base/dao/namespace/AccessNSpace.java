/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 访问控制策略包空间
 *
 * @Date in Dec 27, 2012,9:09:01 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AccessNSpace {

    //访问控制策略配置
    public static final String ACCESS_INFO = "access_info";
    //查询accessInfo信息
    public static final String SELECT_ACCESS_INFO = "selectAccessI"; 
    //删除accessInfo信息
    public static final String DELETE_ACCESS_INFO = "delAccessI"; 
    //保存accessInfo信息
    public static final String ADD_ACCESS_INFO = "addAccessI"; 
    //查询accessInfo信息
    public static final String FIND_ACCESS_INFO = "findAccessI"; 
    //查询允许访问IP段列表
    public static final String SELECT_ACCESS_IPD = "selectAccIPD"; 

}
