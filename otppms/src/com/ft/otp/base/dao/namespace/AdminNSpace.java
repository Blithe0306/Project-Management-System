/**
 *Administrator
 */
package com.ft.otp.base.dao.namespace;

/**
 * 类功能说明
 *
 * @Date in Apr 6, 2011,4:05:06 PM
 *
 * @author TBM
 */
public class AdminNSpace {
    /**
     * 管理员
     */
    //管理员命名空间
    public static final String ADMIN_USER_NS = "admin_user";

    //管理员角色命名空间
    public static final String ADMIN_ROLE_NS = "admin_role";

    //管理员权限命名空间
    public static final String ADMIN_PERM_NS = "admin_perm";

    //角色-权限命名空间
    public static final String ROLE_PERM_NS = "role_perm";

    //管理员-角色命名空间
    public static final String ADMIN_AND_ROLE_NS = "admin_and_role";

    //管理员-组织机构命名空间
    public static final String ADMIN_AND_ORGUNIT_NS = "admin_and_orgunit";
    
    //根据管理员查询管理员邮箱
    public static final String ADMIN_INFO_QUERYUI_EMAIL = "selectAIEmail";

    //引用标识
    //查询管理员
    public static final String ADMIN_USER_SELECT_AU = "selectAU";
    //查询管理员顾虑掉超级管理员 
    public static final String ADMIN_USER_SELECT_AUEPA = "selectAUExceptPowerAdmin";
    //插入管理员
    public static final String ADMIN_USER_INSERT_AU = "insertAU";
    //查找管理员
    public static final String ADMIN_USER_FIND_AU = "findAU";
    //删除管理员
    public static final String ADMIN_USER_DELETE_AU = "deleteAU";
    //更新管理员
    public static final String ADMIN_USER_UPDATE_AU = "updateAdminUser";
    //找回密码，更新管理员
    public static final String ADMIN_USER_UPDATE_AP = "updateAdminPass";
    //静态密码找回有效期
    public static final String ADMIN_USER_UPDATE_PWDEATH = "updatePwddeath";
    //统计管理员
    public static final String ADMIN_USER_COUNT_AU = "countAU";
    //查询子管理员
    public static final String ADMIN_USER_SELECT_CHILDREN_AU = "selectChildrenAU";
    //启用、禁用某个管理员
    public static final String ADMIN_USER_ENABLED_AU = "enabledAU";
    //启用、禁用某个管理员
    public static final String ADMIN_USER_LOCKED_AU = "lockedAU";

    //修改管理员密码
    public static final String PASSWORD_INFO_UPDATE_AU = "pwdUpdateAU";
    //更新管理员登录时间和登录次数
    public static final String ADMIN_MODIFY_AU = "updateAU";
    //查询管理员指派人的上级
    public static final String ADMIN_FIND_SUPER_DESIGNATE = "findSuperDesignte";
    //变更管理员创建人
    public static final String ADMIN_MODIFY_DESIGNATE = "updateDesignteAU";
    //查询角色变更指派人
    public static final String ADMIN_SELECT_CHANGE_CREATER_AU = "selectDesignteAU";
    //变更角色创建人
    public static final String ROLE_UPDATEDDESIGNATE_AR = "updateDesignateAR";

    //查询某个管理员被分配的角色
    public static final String ADMIN_ROLE_ARS = "selectARS";
    //查询管理员角色
    public static final String ADMIN_ROLE_SELECT_AR = "selectAR";
    //插入管理员角色
    public static final String ADMIN_ROLE_INSERT_AR = "insertAR";
    //删除管理员角色
    public static final String ADMIN_ROLE_DELETE_AR = "deleteAR";
    //查找管理员角色
    public static final String ADMIN_ROLE_FIND_AR = "findAR";
    //统计管理员角色
    public static final String ADMIN_ROLE_COUNT_AR = "countAR";
    //更新管理员角色
    public static final String ADMIN_ROLE_UPDATE_AR = "updateAR";

    //查询管理员权限
    public static final String ADMIN_PERM_SELECT_AP = "selectAP";
    public static final String ADMIN_PERM_COUNT_AP = "countAP";
    //查询角色权限
    public static final String ADMIN_PERM_SELECT_ROLEAP = "selectRoleAP";
    //插入管理员常用功能权限码
    public static final String ADMIN_PERM_ADD_PERMCODE = "addPermCode";
    //删除管理员常用功能权限码
    public static final String ADMIN_PERM_DEL_PERMCODE = "delPermCode";
    //查询管理员常用功能权限码
    public static final String ADMIN_PERM_FIND_PERMCODE = "selectPermCode";

    //插入角色-权限关系
    public static final String ROLE_PERM_INSERT_RP = "insertRP";
    //删除角色对应的权限关系
    public static final String ROLE_PERM_DELETE_RP = "deleteRP";
    public static final String ROLE_PERM_SELECT_RP = "selectRP";
    public static final String ADMIN_ROLE_PERM_SELECT_ARP = "selectARP";

    //插入管理员-角色关系
    public static final String ADMIN_AND_ROLE_INSERT_ANR = "insertANR";
    //删除管理员-角色对应的权限关系
    public static final String ADMIN_AND_ROLE_DELETE_ANR = "deleteANR";
    //查询管理员-角色对应关系
    public static final String ADMIN_AND_ROLE_SELECT_ANR = "selectANR";
    //统计管理-角色对应关系
    public static final String ADMIN_AND_ROLE_COUNT_ANR = "countANR";
    //多个角色对应的多个管理员
    public static final String ADMIN_AND_ROLE_ALL_ANR = "selectALLANR";
    //根据管理员ID查角色ID
    public static final String ADMIN_AND_ADMIN_ALL_ROLE = "selectALLRL";
    //根据管理员ID查角色ID总数
    public static final String ADMIN_AND_ADMIN_COUNT_ROLE = "countRL";

    //查询自己和直接下级
    public static final String ADMIN_SELECT_SELF_CHILDREN_OU = "selectSelfChildOU";

    //根据组织机构id查询
    public static final String ADMIN_ORGUNIT_SELECT_AAO = "selectAAO";

    //删除
    public static final String ADMIN_ORGUNIT_DELETE = "deleteAAO";

    //查询 全部 列表 管理员组织机构关系
    public static final String ADMIN_ORGUNIT_SELECT_AOS = "selectAOS";
    //查询某个 管理员组织机构关系
    public static final String ADMIN_ORGUNIT_FIND_AO = "findAO";

    //插入 管理员组织机构关系
    public static final String ADMIN_ORGUNIT_INSERT_AO = "insertAO";
    //删除管理员组织机构关系
    public static final String ADMIN_ORGUNIT_DELETE_AR = "deleteAO";

}
