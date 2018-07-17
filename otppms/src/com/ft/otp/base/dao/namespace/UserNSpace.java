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
public class UserNSpace {

    //命名空间
    public static final String USER_GROUP_NS = "user_group";
    public static final String USER_INFO_NS = "user_info";

    //引用标识
    public static final String USER_GROUP_SEL_UG = "selectUG";
    public static final String USER_INFO_SEL_UI = "selectUI";
    public static final String USER_INFO_SEL_UC = "selectUC";
    public static final String USER_INFO_SEL_UO = "selectU0";
    public static final String USER_INFO_SEL_BIND = "selectBind";
    public static final String USER_SEL_US= "selectUser";
    public static final String USER_SEL_RAD_US= "selectUserToRad";
    public static final String USER_INFO_SEL_USER_SMS = "selectUserToSms";

    public static final String USER_GROUP_COT_UG = "countUG";
    public static final String USER_GROUP_COT_UI = "countUI";
    public static final String USER_GROUP_COT_BIND = "countBind";
    public static final String USER_GROUP_COT_UC = "countUC";

    public static final String USER_GROUP_DEL_UG = "deleteUG";
    public static final String USER_GROUP_ADD_UG = "insertUG";
    public static final String USER_GROUP_UPDA_UG = "updateUG";
    public static final String USER_GROUP_FIND_UG = "findUG";
    
   
    
    //修改用户绑定令牌的状态
    public static final String USER_INFO_BIND_UT = "bindUT";

    public static final String USER_INFO_DEL_UI = "deleteUI";
    public static final String USER_INFO_DEL_UTI = "deleteUTI";
    public static final String USER_INFO_ADD_UI = "insertUI";
    public static final String USER_INFO_UPDA_UI = "updateUI";
    public static final String USER_INFO_FIND_UI = "findUI";
    public static final String USER_INFO_UPDATE_LOCKED = "updateUILocked";
    public static final String USER_INFO_UPDATE_ENABLED = "updateUIEnabled";
    public static final String USER_INFO_UPDATE_ORGUNIT = "updateUIOrgunit";
    public static final String USER_INFO_UPDATE_ORGUNITS = "updateUIOrgunits";
    public static final String USER_INFO_UPDATE_STATICPASS = "updateUIStaticPass";
    //导出用户查询用户、令牌、用户组
    public static final String USER_INFO_QUERYUI_TI_UG = "queryUI_TI_UG";
    //导出用户查询用户、令牌、用户组,无令牌
    public static final String USER_INFO_QUERYUI_UG = "queryUI_UG";
    //根据用户查询用户邮箱
    public static final String USER_INFO_QUERYUI_EMAIL = "selectUIEmail";
    //批量更新radius配置
    public static final String USER_INFO_UPDATE_RADIUS_ID = "updateRadId";
    //批量设置后端认证
    public static final String USER_INFO_UPDATE_BACKEND_ID = "updateBackEndId";
    //批量设置本地认证模式
    public static final String USER_INFO_UPDATE_LOCALAUTH_ID = "updateLocalAuthId";
    
}
