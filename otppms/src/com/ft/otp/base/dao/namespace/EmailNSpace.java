/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 邮件服务器配置命名空间
 *
 * @Date in Nov 19, 2012,4:04:24 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class EmailNSpace {

    //邮件服务器配置
    public static final String EMAIL_INFO = "email_info";
    
    //查询emailInfo信息
    public static final String SELECT_EMAIL_INFO = "selectEI"; 
    //添加邮件服务器
    public static final String ADD_EMAIL_INFO = "addEI"; 
    //查找邮件服务器
    public static final String FIND_EMAIL_INFO = "findEI"; 
    //更新邮件服务器
    public static final String UPDATE_EMAIL_INFO = "updateEI"; 
    //删除邮件服务器
    public static final String DEL_EMAIL_INFO = "deleteEI"; 
    //取消默认服务器
    public static final String CANCEL_EMAIL_DEFAULT = "canceldefaultEI";
    //设置默认服务器
    public static final String SET_EMAIL_DEFAULT = "setdefaultEI";
}
