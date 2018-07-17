/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 23, 2014,1:40:53 PM
 */
package com.ft.otp.base.dao.namespace;

/**
 * iBatis命名空间及引用标识
 * 定制项目命名
 * @Date Dec 23, 2014,1:40:53 PM
 * @version v1.0
 * @author ZWX
 */
public class ProjectNSpace {
    //命名空间
    public static final String PROJECT_GROUP_NS = "project_group";
    public static final String PROJECT_NS = "project";

    //引用标识
    public static final String PROJECT_COUNT = "countPrjUI";
    public static final String PROJECT_QUERY = "selectPrj";
    public static final String PROJECT_FIND = "findPrj";
    public static final String PROJECT_SELECT_POWERADMIN = "selectPowerAdmin";
    public static final String PROJECT_FIND_EXCEPT_SELF = "findPrjExceptself";
    public static final String PROJECT_ADD = "insertPrjUI";
    public static final String PROJECT_UPDA = "updatePrjUI";
    public static final String PROJECT_DEL = "deletePrjUI";
}
