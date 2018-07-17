/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * iBatis命名空间及引用标识
 * 用户、用户组
 *
 * @Date in Apr 6, 2011,4:04:34 PM
 *
 * @author TBM
 */
public class CustomerNSpace {

    //命名空间
    public static final String CUST_INFO_NS = "customer_info";

    //引用标识
    public static final String CUST_INFO_SEL_UI = "selectUI";
    public static final String CUST_INFO_SEL_CUST_UI = "selectCust";
    public static final String CUST_INFO_SEL_CUST_EQUAL_UI = "selectCust4Equal";
    public static final String cUST_INFO_SEL_MAX_CUSTID = "selectMaxCustid";
    public static final String CUST_INFO_FIND_UI = "findUI";
    public static final String CUST_INFO_COT_UI = "countUI";
    public static final String CUST_INFO_DEL_UI = "deleteUI";
    public static final String CUST_INFO_ADD_UI = "insertUI";
    public static final String CUST_INFO_UPDA_UI = "updateUI";
    
}
