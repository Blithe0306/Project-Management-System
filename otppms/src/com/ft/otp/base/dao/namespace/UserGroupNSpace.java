/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * iBatis命名空间及引用标识
 * 用户与用户组对应
 *
 * @Date in Apr 21, 2011,9:00:54 PM
 *
 * @author TBM
 */
public class UserGroupNSpace {

    //命名空间
    public static final String USER_AND_GROUP_NS = "user_and_group";

    //引用标识
    public static final String USER_AND_GROUP_ADD_UG = "insertUAG";

    public static final String USER_AND_GROUP_SEL_UG = "selectUAG";

    public static final String USER_AND_GROUP_COU_UG = "countUAG";

    //根据用户名或组ID批量查询用户或组，使用IN()方式
    public static final String USER_AND_GROUP_SEL_UG_IN = "selectUAG_In";

    public static final String USER_AND_GROUP_DEL_UG = "deleteUAG";

}
