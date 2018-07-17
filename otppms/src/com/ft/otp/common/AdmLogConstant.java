/**
 *Administrator
 */
package com.ft.otp.common;


/**
 * 管理员日志记录专用常量类
 *
 * @Date in Jun 8, 2011,3:24:14 PM
 *
 * @author TBM
 */
public class AdmLogConstant {

    /**
     * 多语言文件中取得日志描述信息的标识
     */
    //actionId的前缀
    public static String lang_action_id = "log_action_id_";
    //actionObject的前缀
    public static String lang_action_obj = "log_action_obj_";
    //用户日志操作前缀
    public static String lang_user_action_id = "packet_";
    /**
     * invoke执行结果
     */
    //成功
    public static String invoke_succ = "success";

    /**
     * Service层将要进行拦截的接口名
     */
    //域管理接口
    public static String if_idomaininfoserv = "IDomainInfoServ";
    //组织机构
    public static String if_iorgunitinfoserv = "IOrgunitInfoServ";
    //用户和令牌关系接口
    public static String if_iusertokenserv = "IUserTokenServ";
    //用户和用户组关系接口
    public static String if_iuserandgroupserv = "IUserAndGroupServ";
    //用户组接口
    public static String if_iusergroupserv = "IUserGroupServ";
    //用户接口
    public static String if_iuserinfoserv = "IUserInfoServ";
    //令牌接口
    public static String if_itokenserv = "ITokenServ";
    //令牌分发接口
    public static String if_idistmanagerserv = "IDistManagerServ";
    //认证服务接口
    public static String if_iserverserv = "IServerServ";
    //认证代理接口
    public static String if_iagentserv = "IAgentServ";
    //认证服务和代理关系接口
    public static String if_iagentserverserv = "IAgentServerServ";
    //认证代理配置接口
    public static String if_iagentconfserv = "IAgentConfServ";
    //后端认证接口
    public static String if_ibackendserv = "IBackendServ";

    //邮件服务器配置接口
    public static String if_iemailconfserv = "IEmailInfoServ";
    //手机令牌分发配置接口
    public static String if_idistconfserv = "IDistConfServ";
    //授权接口
    public static String if_ilicenseserv = "ILicenseServ";
    //服务器配置接口
    public static String if_iserverconfigserv = "IServerConfigServ";
    //数据库配置接口
    public static String if_idbconfserv = "IDBConfServ";
    //基本运行配置接口
    public static String if_icenterbasesetserv = "ICenterBaseSetServ";
    //用户来源配置接口
    public static String if_iusersourceserv = "IUserSourceServ";
    public static String if_itaskinfoserv = "ITaskInfoServ";
    //核心功能配置接口
    public static String if_icoreconfserv = "ICoreConfServ";
    //短信网关配置接口
    public static String if_ismsconfserv = "ISmsInfoServ";
    //访问控制策略配置接口
    public static String if_iaccessconserv = "IAccessConServ";
    //Radius配置接口
    public static String if_iradprofileserv = "IRadProfileServ";
    //Radius属性配置接口
    public static String if_iradattrserv = "IRadAttrServ";
    //管理员角色接口IRoleInfoServ
    public static String if_iroleinfoserv = "IRoleInfoServ";
    //管理员用户接口
    public static String if_iadminuserserv = "IAdminUserServ";
    //系统通知接口
    public static String if_inoticeserserv = "IProNoticeServ";
    /**
    * Service层将要进行拦截的方法名，addObj、updateObj、delObj属于BaseService层的公共方法
    */
    //addObj
    public static String method_add = "addObj";
    //updateObj
    public static String method_update = "updateObj";
    //delObj
    public static String method_delete = "delObj";

    //域管理 设置默认域
    public static String method_modifydefaultdomain = "modifyDefaultDomain";
    //用户
    public static String method_batchimportUser = "batchimportUser";
    public static String method_batchImportUs = "batchImportUs";
    public static String method_batchUpdateUser = "batchUpdateUser";
    public static String method_batchDelUser = "batchDelUser";
    public static String method_queryUIUTUG = "queryUIUTUG";
    public static String method_updateUserLost = "updateUserLost";
    public static String method_updateUserAbled = "updateUserAbled"; // 批量启用/禁用  
    public static String method_updateUserEnable = "updateUserEnabled";
    public static String method_updateStaticPass = "updateStaticPass";
    public static String method_updateUserOrgunit = "updateUserOrgunit";
    public static String method_updateUserLocked = "updateUserLocked"; // 批量锁定/解锁
    public static String method_batchSetRadId = "batchSetRadId"; // 设置Radius配置
    public static String method_batchSetBackendId = "batchSetBackendId"; // 设置后端认证
    public static String method_batchSetLocalauth = "batchSetLocalauth"; // 设置本地认证模式

    //用户组
    public static String method_batchAddUGroup = "batchAddUGroup";

    //用户-用户组
    public static String method_addUsrGroup = "addUsrGroup";

    //用户-令牌
    //addUsrTkn
    public static String method_addUsrTkn = "addUsrTkn";
    //batchUnBindUT
    public static String method_batchUnBindUT = "batchUnBindUT";

    //令牌
    public static String method_updateTokenEnable = "updateTokenEnable";
    public static String method_updateTokenLocked = "updateTokenLocked";
    public static String method_updateTokenLost = "updateTokenLost";
    public static String method_updateTokenLogout = "updateTokenLogout";
    public static String method_importToken = "importToken";
    public static String method_updateSoftTkn = "updateSoftTkn";
    public static String method_updateTokenCancel = "updateTokenCancelAssign";
    public static String method_updateTokenState = "updateTokenState";
    public static String method_updateTokenOrg = "updateTokenOrg";
    public static String method_updateObj = "updateObj";

    //数据库配置
    public static String method_testDbConn = "testDbConn";
    public static String method_importDbTable = "importDbTable";

    //启用禁用认证代理
    public static String method_updateenabled = "updateEnabled";
    //启用/禁用后端认证
    public static String method_upEnabled = "updateEnabled";
    //认证服务器
    public static String method_updateList = "updateList";

    //认证服务器－代理
    public static String method_addAgentHost = "addAgentHost";

    //用户来源配置
    public static String method_testUsConn = "testUsConn";

    //管理员
    public static String method_updateEnabled = "updateEnabled";
    public static String method_updateLocked = "updateLocked";
    public static String method_updatePassword = "updatePassword";
    public static String method_updateDesignate = "updateDsignate";

    /**
     * 配置管理
     */
    //设置默认邮箱
    public static String method_updateIsdefaultOE = "updateIsdefaultOE";
    
    //启用/禁用短信网关配置
    public static String method_enable = "updateEnabled";

    /**
     * actionId
     */
    //添加
    public static int log_aid_add = 1001;
    //更新
    public static int log_aid_edit = 1002;
    //删除
    public static int log_aid_del = 1003;
    //查询
    public static int log_aid_query = 1004;
    //绑定
    public static int log_aid_bind = 1005;
    //解绑
    public static int log_aid_unbind = 1006;
    //导入
    public static int log_aid_import = 1007;
    //导出
    public static int log_aid_export = 1008;
    //更换
    public static int log_aid_replace = 1009;
    //启用
    public static int log_aid_enable = 1010;
    //停用
    public static int log_aid_disable = 1011;
    //锁定
    public static int log_aid_lock = 1012;
    //解锁
    public static int log_aid_unlock = 1013;
    //挂失
    public static int log_aid_lost = 1014;
    //解挂
    public static int log_aid_unlost = 1015;
    //注销
    public static int log_aid_logout = 1016;
    //恢复使用
    public static int log_aid_unlogout = 1017;
    //批量启用
    public static int log_aid_b_enable = 1018;
    //批量停用
    public static int log_aid_b_disable = 1019;
    //批量锁定
    public static int log_aid_b_lock = 1020;
    //批量解锁
    public static int log_aid_b_unlock = 1021;
    //批量挂失
    public static int log_aid_b_lost = 1022;
    //批量解挂
    public static int log_aid_b_unlost = 1023;
    //批量注销
    public static int log_aid_b_logout = 1024;
    //批量恢复使用
    public static int log_aid_b_unlogout = 1025;
    //分发
    public static int log_aid_dist = 1026;
    //设定标识码
    public static int log_aid_setmark = 1027;
    //重置
    public static int log_aid_rest = 1028;
    //在线分发
    public static int log_aid_ondist = 1029;
    //离线分发
    public static int log_aid_undist = 1030;
    //查看
    public static int log_aid_view = 1031;
    //批量解绑
    public static int log_aid_batchunbind = 1032;
    //批量更新
    public static int log_aid_batcedit = 1033;
    //批量添加
    public static int log_aid_batcadd = 1034;
    //验证
    public static int log_aid_verif = 1035;
    //测试连接
    public static int log_aid_testconn = 1036;
    //初始化创建数据库表
    public static int log_aid_importdbtable = 1037;
    //登录
    public static int log_aid_login = 1038;
    //退出
    public static int log_aid_lgout = 1039;
    //下载
    public static int log_aid_download = 1040;
    //设置默认
    public static int log_aid_setdefault = 1041;
    //变更
    public static int log_aid_change = 1042;
    //分配
    public static int log_aid_assign = 1043;
    //取消分配
    public static int log_aid_cancel = 1044;
    //变更创建人
    public static int log_aid_dsignate = 1045;
    //迁移
    public static int log_aid_move = 1046;
    //设置
    public static int log_aid_set = 1047;
    //备份
    public static int log_aid_bak = 1048;
    //永久锁定
    public static int log_aid_perm_lock = 1049;
    //临时锁定
    public static int log_aid_temp_lock = 1050;
    
    //用户来源，导入
    public static int log_aid_usersoure_import = 1051;
    //用户来源，更新
    public static int log_aid_usersoure_update = 1052;
    //用户来源，删除
    public static int log_aid_usersoure_delete = 1053;
    // 在线二维码
    public static int log_aid_online_dist = 1054;
    // 离线二维码
    public static int log_aid_offline_dist = 1055;
    //双机热备运行监控
    public static int log_aid_heart_beat_ware = 1056;

    /**
     * actionObject
     */
    //组织机构
    public static int log_obj_orgunit = 2001;
    //域
    public static int log_obj_domain = 2002;
    //管理员
    public static int log_obj_admin = 2101;
    //管理员角色
    public static int log_obj_adminrole = 2102;
    //用户
    public static int log_obj_user = 2201;
    //令牌管理
    public static int log_obj_mob_token = 2301;
    //认证服务器
    public static int log_obj_auth_server = 2401;
    //认证代理
    public static int log_obj_auth_agent = 2402;
    //认证代理配置
    public static int log_obj_auth_conf = 2403;
    //后端认证
    public static int log_obj_auth_backend = 2404;
    //本地认证模式
    public static int log_obj_auth_local = 2405;
    //管理员日志
    public static int log_obj_admin_log = 2501;
    //用户日志
    public static int log_obj_user_log = 2502;
    //报表
    public static int log_obj_report = 2601;
    //OTP Server 配置
    public static int log_obj_conf_serv = 2701;
    //管理中心
    public static int log_obj_conf_center = 2702;
    //用户门户配置
    public static int log_obj_conf_portal = 2703;
    //访问控制配置
    public static int log_obj_conf_access = 2704;
    //数据库配置
    public static int log_obj_conf_db = 2705;
    //用户来源配置
    public static int log_obj_conf_us = 2706;
    //手机令牌分发配置
    public static int log_obj_conf_phone = 2707;
    //邮件服务器配置
    public static int log_obj_conf_mail = 2708;
    //短信网关配置 
    public static int log_obj_conf_sms = 2709;
    //Radius配置 
    public static int log_obj_conf_radius = 2710;
    //Radius属性配置 
    public static int log_obj_conf_radattr = 2711;
    //核心业务配置 
    public static int log_obj_conf_core = 2712;
    //授权
    public static int log_obj_conf_lic = 2713;
    //系统通知
    public static int log_obj_conf_notice = 2714;
    //短信令牌基本功能配置
    public static int log_obj_conf_smstoken = 2715;
    //系统公共配置
    public static int log_obj_conf_comm = 2716;
    //认证配置
    public static int log_obj_conf_auth = 2717;
    //用户配置
    public static int log_obj_conf_user = 2718;
    //令牌配置
    public static int log_obj_conf_token = 2719;
    //OTP Server管理中心
    public static int log_obj_otpserver = 2801;
    //管理员密码
    public static int log_obj_admin_pwd = 2802;
    //监控预警策略
    public static int log_obj_monitor_conf = 2803;
    //导入用户模版文件
    public static int log_obj_temp_file = 2804;
    //静态密码
    public static int log_obj_static_pwd = 2805;
    //令牌
    public static int log_obj_tkn = 2806;
    //软件令牌
    public static int log_obj_soft_tkn = 2807;
    //应	急口令
    public static int log_obj_mergency = 2808;
    //数据
    public static int log_obj_data = 2809;
    //备份数据
    public static int log_obj_bakdata = 2810;
    //IP访问控制策略
    public static int log_obj_trustip_check = 2811;
    //用户来源
    public static int log_obj_usersource = 2812;
    //用户来源定时更新
    public static int log_obj_timing_update_us = 2813;
    //密码错误
    public static int log_obj_pwd_error = 2814;
    //登录账号
    public static int log_obj_admin_amount = 2815;
    //批量解绑模版文件
    public static int log_obj_unbind_file = 2816;
    //双机热备运行监控
    public static int log_obj_heart_beat_ware = 2817;
    //预警
    public static int log_obj_heart_beat_warning = 2818;
    //恢复正常
    public static int log_obj_heart_beat_ok = 2819;
    //客户管理
    public static int log_obj_customer = 3001;
    public static int log_obj_project = 3002;
    public static int log_obj_prjinfo = 3003;
    public static int log_obj_resords = 3004;    
}
