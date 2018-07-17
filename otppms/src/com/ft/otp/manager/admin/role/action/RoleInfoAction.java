/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.PermConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ;
import com.ft.otp.manager.admin.perm.entity.AdminPerm;
import com.ft.otp.manager.admin.perm.service.IAdmPermServ;
import com.ft.otp.manager.admin.role.action.aide.RoleInfoActionAide;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.form.RoleInfoQueryForm;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.manager.admin.role_perm.service.IRolePermServ;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员角色处理Action
 *
 * @Date in Jun 29, 2011,3:19:57 PM
 *
 * @author TBM
 */
public class RoleInfoAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -9154230034687660491L;
    private Logger logger = Logger.getLogger(RoleInfoAction.class);

    private IRoleInfoServ roleInfoServ = null;
    //管理员权限接口
    private IAdmPermServ admPermServ = (IAdmPermServ) AppContextMgr.getObject("adminPermServ");
    //角色-权限接口
    private IRolePermServ rolePermServ = (IRolePermServ) AppContextMgr.getObject("rolePermServ");
    //管理员接口
    private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
    //管理员-角色关系服务接口
    public IAdminAndRoleServ adminAndRoleServ = (IAdminAndRoleServ) AppContextMgr.getObject("adminAndRoleServ");

    //角色辅助类
    private RoleInfoActionAide aide = new RoleInfoActionAide();
    private RoleInfoQueryForm queryForm = null;
    private RoleInfo roleInfo = null;
    private RolePerm rolePerm = null;
    private RoleInfo oldroleInfo;
    private List<RoleInfo> rolestr = new ArrayList();

    public List<RoleInfo> getRolestr() {
		return rolestr;
	}

	public void setRolestr(List<RoleInfo> rolestr) {
		this.rolestr = rolestr;
	}

	/**
     * @return the rolePerm
     */
    public RolePerm getRolePerm() {
        return rolePerm;
    }

    /**
     * @param rolePerm the rolePerm to set
     */
    public void setRolePerm(RolePerm rolePerm) {
        if (!StrTool.objNotNull(rolePerm)) {
            rolePerm = new RolePerm();
        }
        this.rolePerm = rolePerm;
    }

    /**
     * @return the roleInfoServ
     */
    public IRoleInfoServ getRoleInfoServ() {
        return roleInfoServ;
    }

    /**
     * @param roleInfoServ the roleInfoServ to set
     */
    public void setRoleInfoServ(IRoleInfoServ roleInfoServ) {
        this.roleInfoServ = roleInfoServ;
    }

    /**
     * @return the queryForm
     */
    public RoleInfoQueryForm getQueryForm() {
        return queryForm;
    }

    /**
     * @param queryForm the queryForm to set
     */
    public void setQueryForm(RoleInfoQueryForm queryForm) {
        this.queryForm = queryForm;
    }

    /**
     * @return the roleInfo
     */
    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    /**
     * @param roleInfo the roleInfo to set
     */
    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    /**
     * 取出QueryForm中的实体构造 查询实体
     * 查询条件说明：sel_role参数代表来源 1、如果不是null来自管理员选择角色列表 2、如果是null角色管理列表
     *   1、管理员选择角色列表：如果是A 看到所有的，如果是B 只能看到自己创建的角色
     *      查询参数说明：QuerySource 不等于null A、超级管理员loginUser 等于ADMIN B、普通管理员loginUser 不等于ADMIN
     *   2、角色管理列表：如果是C看所有角色，如果是D 只能看到自己的角色和自己创建的角色
     *      查询参数说明：QuerySource 等于null  C、超级管理员 loginUser等于空 ，D、loginUser不等于空
     * @Date in May 5, 2011,6:30:40 PM
     * @param agentQueryForm
     * @return
     */
    public RoleInfo getAdminRole(RoleInfoQueryForm qForm) {
        RoleInfo admRole = new RoleInfo();
        if (StrTool.objNotNull(qForm)) {
            admRole = qForm.getAdminRole();
        }

        String selStr = request.getParameter("sel_role");
        if (StrTool.strNotNull(selStr)) {// 管理员选择角色列表
            admRole.setQuerySource(selStr);
            if (StrTool.strEquals(super.getCurLoginUserRole(), StrConstant.SUPER_ADMIN)) {// 如果是超级管理员角色
                admRole.setLoginUser(StrConstant.SUPER_ADMIN);// sqlmap做标识用
            } else {
                admRole.setLoginUser(super.getCurLoginUser());// 当前登录管理员
            }
        } else {// 角色管理列表
            admRole.setQuerySource("");
            if (StrTool.strEquals(super.getCurLoginUserRole(), StrConstant.SUPER_ADMIN)) {// 如果是超级管理员角色
                admRole.setLoginUser(null);
            } else {
                admRole.setLoginUser(super.getCurLoginUser());// 当前登录管理员
            }
        }

        return admRole;
    }

    /**
     * 角色列表初始化
     */
    public String init() {
        try {
            if (isNeedClearForm()) {
                setQueryForm(null);
            }

            PageArgument pageArg = pageArgument();
            pageArg.setCurPage(getPage());
            pageArg.setPageSize(getPagesize());
            List<?> resutList = query(pageArg);

            String jsonData = JsonTool.getJsonFromList(pageArg.getTotalRow(), resutList, null);
            setResponseWrite(jsonData);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * 处理角色数据查询
     * @Date in Apr 10, 2011,10:56:03 AM
     * @param pageArg
     */
    private List<?> query(PageArgument pageArg) {
        List<?> admRoleList = null;
        try {
            RoleInfo admRole = getAdminRole(queryForm);
            admRoleList = roleInfoServ.query(admRole, pageArg);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return admRoleList;
    }

    /**
     * 行数统计
     * 分页处理
     * @Date in Apr 15, 2011,11:33:36 AM
     * @return
     */
    @SuppressWarnings("unchecked")
    private PageArgument pageArgument() throws BaseException {
        // sel_role 已在getAdminRole 中赋值
        RoleInfo oRole = getAdminRole(queryForm);
        int totalRow = roleInfoServ.count(oRole);

        PageArgument pageArg = getArgument(totalRow);
        pageArg.setCurPage(getPage());
        pageArg.setPageSize(getPagesize());

        return pageArg;
    }

    /**
     * 添加管理员角色
     */
    public String add() {
        try {
            if (!StrTool.objNotNull(roleInfo)) {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "role_add_error_tip"));
                return null;
            }

            //添加角色
            roleInfo.setCreatetime(DateTool.currentUTC());
            roleInfoServ.addObj(roleInfo);
            RoleInfo admRole = findData(roleInfo, true);
            if (StrTool.objNotNull(admRole)) {
                if (StrTool.listNotNull(roleInfo.getAdminPerms())) {
                    //添加角色拥有的权限
                    aide.addRolePerm(admRole.getRoleId(), roleInfo.getAdminPerms(), rolePermServ);
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "role_add_success_tip"));
                    return null;
                }
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "role_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 查找的公共方法,传true表示根据角色名称查询。默认传false，根据id查询
     */
    public RoleInfo findData(RoleInfo aRole, boolean flag) throws BaseException {
        RoleInfo admRole = new RoleInfo();
        if (flag) {
            if (StrTool.strNotNull(aRole.getRoleName())) {
                admRole.setRoleName(aRole.getRoleName());
            }
        } else {
            if (aRole.getRoleId() > 0) {
                admRole.setRoleId(aRole.getRoleId());
            }
        }
        admRole = (RoleInfo) roleInfoServ.find(admRole);

        return admRole;
    }

    /**
     * 删除管理员角色
     */
    public String delete() {
        String delIds = request.getParameter("delRoleIds");
        String[] delIdArr = delIds.split(",");
        Set<String> delRoleSet = new HashSet<String>();
        List<RoleInfo> roleList = new ArrayList();
        RoleInfo oRole = null;
        try {
            for (int i = 0; i < delIdArr.length; i++) {
                String roleId = delIdArr[i];
                oRole = new RoleInfo();
                oRole.setRoleId(StrTool.parseInt(roleId));
                oRole = (RoleInfo) roleInfoServ.find(oRole);

                //系统预定义角色和上级分配给自己的角色不能删除
                if (StrTool.objNotNull(oRole)) {
                    if (StrTool.strNotNull(oRole.getRolemark())) {
                        outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "role_sys_predefined_roles")
                                + oRole.getRoleName() + Language.getLangStr(request, "role_can_not_del"));
                        return null;
                    }

                    //管理员只能删除自己创建的角色
                    if (!StrTool.strEquals(oRole.getCreateuser(), super.getCurLoginUser())) {
                        outPutOperResult(Constant.alert_warn, Language.getLangStr(request,
                                "role_can_not_del_no_own_created")
                                + oRole.getRoleName());
                        return null;
                    }

                    //管理员删除角色时，验证角色是否已经被分配
                    AdminAndRole admAndRole = new AdminAndRole();
                    admAndRole.setRoleId(oRole.getRoleId());
                    int usercount = adminAndRoleServ.count(admAndRole);
                    if (usercount != 0) {
                        outPutOperResult(Constant.alert_warn, oRole.getRoleName()
                                + Language.getLangStr(request, "role_already_allocation"));
                        return null;
                    }

                    delRoleSet.add(roleId);
                }
                roleList.add(oRole);
            }

            // 删除角色
            if (StrTool.setNotNull(delRoleSet)) {
                roleInfoServ.delObj(delRoleSet);
            }
            this.setRolestr(roleList); // 记录日志
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
        } catch (Exception ex) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    /**
     * 查找角色信息
     */
    public String find() {
        try {
            RoleInfo amRole = findData(roleInfo, false);
            if (!StrTool.objNotNull(amRole)) {
                return init();
            }

            RolePerm rp = new RolePerm();
            rp.setRoleId(amRole.getRoleId());
            List<?> oldPerms = rolePermServ.query(rp, new PageArgument());
            amRole.setAdminPerms(oldPerms);

            this.setRoleInfo(amRole);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return SUCC_FIND;
    }

    /**
     * 添加或修改角色时，判断角色是否已经存在,根据角色名称查询
     * @Date in Jul 4, 2011,2:43:47 PM
     * @return
     */
    public String findRoleisExist() {
        try {
            String roleName = roleInfo.getRoleName();
            String oldRoleName = roleInfo.getOldRoleName();
            roleName = MessyCodeCheck.iso885901ForUTF8(roleName);
            oldRoleName = MessyCodeCheck.iso885901ForUTF8(oldRoleName);

            if (StrTool.strEquals(roleName, oldRoleName)) {
                setResponseWrite("true");
                return null;
            }

            roleInfo.setRoleName(roleName);
            RoleInfo amRole = findData(roleInfo, true);
            if (null == amRole) {
                setResponseWrite("true");
                return null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        setResponseWrite("false");
        return null;
    }

    /**
     * 修改管理员角色
     */
    public String modify() {
        List<String> perms = new ArrayList<String>();
        try {
            //更新角色
            roleInfo.setModifytime(DateTool.currentUTC());
            roleInfoServ.updateObj(roleInfo);

            //被修改的权限
            if (StrTool.listNotNull(roleInfo.getAdminPerms())) {
                List<?> newPerms = roleInfo.getAdminPerms();
                String b[] = newPerms.toArray(new String[newPerms.size()]);

                RolePerm rolePm = new RolePerm();
                rolePm.setRoleId(roleInfo.getRoleId());
                //数据库已经存在的权限
                List<?> oldPerms = rolePermServ.query(rolePm, new PageArgument());
                String a[] = new String[oldPerms.size()];
                for (int j = 0; j < oldPerms.size(); j++) {
                    RolePerm rP = (RolePerm) oldPerms.get(j);
                    a[j] = rP.getPermcode();
                }

                //新的权限和旧的权限比对
                perms = StrTool.BLessToA(b, a);

                //记录日志
                addRolePermLog(perms, a, b, roleInfo.getRoleName());
            }
            int temp[] = new int[1];
            temp[0] = roleInfo.getRoleId();
            List<?> amrolesList = aide.getAllChildAdminAndChildRole(adminAndRoleServ, adminUserServ, temp);

            //更新角色对应的权限
            aide.updateRolePerm(roleInfo.getRoleId(), roleInfo.getAdminPerms(), rolePermServ);

            //子管理员对应的角色的权限的更新
            aide.deleteChildRolePerm(amrolesList, perms, rolePermServ);
            if (StrTool.listNotNull(roleInfo.getAdminPerms())) {
                roleInfo.getAdminPerms().clear();
            }

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "role_edit_success_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "role_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 记录日志编辑详细信息
     * @Date in Jun 21, 2013,9:20:52 AM
     * @param perms
     * @param a
     * @param b
     */
    public void addRolePermLog(List<?> perms, String a[], String b[], String roleName) {
        List<String> addperms = new ArrayList<String>();
        addperms = StrTool.BLessToA(a, b);
        StringBuilder sbr = new StringBuilder();

        if (perms.size() > 0) {
            sbr.append(Language.getLangStr(request, "role_reduce_perm") + Language.getLangStr(request, "colon"))
                    .append("</br>");
            for (int k = 0; k < perms.size(); k++) {
                sbr.append(getResVal(((String) perms.get(k)).substring(0, 2)));
                sbr.append(" ");
                sbr.append(getResVal((String) perms.get(k)));
                if (k == perms.size() - 1) {
                    sbr.append(Language.getLangStr(request, "semicolon"));
                } else {
                    sbr.append(Language.getLangStr(request, "comma"));
                }
                sbr.append("</br>");
            }
            sbr.append("</br>");

        }
        if (addperms.size() > 0) {
            sbr.append(Language.getLangStr(request, "role_add_perm") + Language.getLangStr(request, "colon")).append(
                    "</br>");

            for (int m = 0; m < addperms.size(); m++) {
                sbr.append(getResVal(((String) addperms.get(m)).substring(0, 2)));
                sbr.append(" ");
                sbr.append(getResVal((String) addperms.get(m)));
                if (m == addperms.size() - 1) {
                    sbr.append(Language.getLangStr(request, "semicolon"));
                } else {
                    sbr.append(Language.getLangStr(request, "comma"));
                }
                sbr.append("</br>");
            }
            sbr.append("</br>");

        }
        if (perms.size() <= 0 && addperms.size() <= 0) {
            sbr.append(Language.getLangStr(request, "role_perm_no_adjust")).append("</br>");
        }

        RoleInfo rInfo = new RoleInfo();
        String hidrolename = request.getParameter("hidrolename");
        rInfo.setRoletext(sbr.toString());
        rInfo.setOldRoleName(hidrolename);
        rInfo.setRoleName(roleName);
        rInfo.setDescp(roleInfo.getDescp());

        // 记录日志中使用
        this.setOldroleInfo(rInfo);
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#view()
     */
    public String view() {
        return null;
    }

    /**
     * 显示树形管理员权限菜单
     * @Date in Jun 29, 2011,6:26:19 PM
     * @return
     */
    public String viewPerm() {
        List<?> permList = null;
        List<?> oldPermList = null;
        String operStr = request.getParameter("oper");
        String roleidStr = request.getParameter("roleid");
        String adminId = request.getParameter("adminId");
        AdminPerm adminPerm = new AdminPerm();

        try {
            //从session中获取当前登录用户
            HttpSession session = request.getSession(true);
            String user = (String) session.getAttribute(Constant.CUR_LOGINUSER);

            //根据角色查看权限,标识viewrole
            if (StrTool.strEquals(operStr, "viewrole")) {
                adminPerm.setRoleId(StrTool.parseInt(roleidStr));
                permList = admPermServ.queryRolePerm(adminPerm, new PageArgument());
            } else {
                //超级管理员添加或编辑角色，可以进行分配的权限是获取所有的权限
                if (!StrTool.strEquals(operStr, "view")
                        && StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
                    adminPerm.setLoginUser(user);
                    adminPerm.setCurLoginUserRole(super.getCurLoginUserRole());
                    permList = admPermServ.query(adminPerm, new PageArgument());

                } else {
                    if (StrTool.strNotNull(user)) {
                        if (StrTool.strEquals(operStr, "view")) {
                            adminPerm.setLoginUser(adminId);
                            adminPerm.setCurLoginUserRole("");
                        } else {
                            adminPerm.setLoginUser(user);
                            adminPerm.setCurLoginUserRole(super.getCurLoginUserRole());
                        }

                    }

                    permList = admPermServ.query(adminPerm, new PageArgument());
                    if (StrTool.strEquals(operStr, "view")) {
                        RolePerm rolePerm = new RolePerm();
                        rolePerm.setAdminId(adminId);
                        oldPermList = rolePermServ.queryAdmPerms(rolePerm);
                    }

                }
            }

            //获取角色对应的权限
            if (StrTool.strNotNull(roleidStr) && !StrTool.strEquals(operStr, "view")) {
                oldPermList = aide.getPermServ(rolePermServ, StrTool.parseInt(roleidStr));
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        String jsonStr = getJsonStr(permList, operStr, oldPermList);
        super.setResponseWrite(jsonStr);

        return null;
    }

    /**
     * 根据ID从缓存中获取每个模块的权限
     * 方法说明
     * @Date in Jan 22, 2013,4:00:49 PM
     * @return
     */
    public String getJsonDataById() {
        String permId = request.getParameter("permid");

        //根据TOP顶级的权限CODE获取对应的子权限，用于Tree的显示
        List<?> permList = (List<?>) PermConfig.getValue(permId);
        int record = 0;
        if (StrTool.listNotNull(permList)) {
            record = permList.size();
        }

        //获取json tree数据
        String jsonTreeStr = JsonTool.getJsonFromList(record, permList);
        super.setResponseWrite(jsonTreeStr);

        return null;
    }

    /**
     * 获取勾选的权限一级、二级下如果存在子节点，当前节点不被选中
     */
    private AdminPerm getoldpermList(AdminPerm adminPerm, List<?> newpermsList) {
        for (int k = 0; k < newpermsList.size(); k++) {
            AdminPerm rolePerm = (AdminPerm) newpermsList.get(k);
            String permcode = rolePerm.getPermCode();

            if (adminPerm.getPermCode().equals(permcode)) {
                if (permcode.length() == 2) {
                    List<?> toList4 = getNodeList(newpermsList, permcode, NumConstant.common_number_4);
                    if (!StrTool.listNotNull(toList4)) {//一级权限下不存在子节点
                        adminPerm.setChecked("checked");
                    }

                } else if (permcode.length() == 4) {//二级权限
                    List<?> toList6 = getNodeList(newpermsList, permcode, NumConstant.common_number_6);
                    if (!StrTool.listNotNull(toList6)) {//二级权限下不存在子节点
                        adminPerm.setChecked("checked");
                    }
                } else {
                    adminPerm.setChecked("checked");
                }
                adminPerm.setChecked("checked");
            }
        }

        return adminPerm;
    }

    /**
     * 取得树型菜单数据的JSON格式
     * @Date in Jun 30, 2011,7:32:53 PM
     * @param permList
     * @return
     */

    public String getJsonStr(List<?> permsList, String operStr, List<?> oldpermsList) {
        List permList = new ArrayList();
        List<Object> adminPermsList = new ArrayList<Object>();

        //编辑角色时，加载角色对应的权限
        if (StrTool.strNotNull(operStr)
                && (StrTool.strEquals(operStr, "edit") || StrTool.strEquals(operStr, "view")
                        && !StrTool.strEquals(super.getCurLoginUserRole(), StrConstant.SUPER_ADMIN))
                && StrTool.listNotNull(oldpermsList)) {

            for (int i = 0; i < oldpermsList.size(); i++) {
                RolePerm rolePerm = (RolePerm) oldpermsList.get(i);
                AdminPerm apsPerm = new AdminPerm();
                apsPerm.setPermCode(rolePerm.getPermcode());
                apsPerm.setText(rolePerm.getPermcode());
                apsPerm.setId(rolePerm.getPermcode());
                adminPermsList.add(apsPerm);
            }
            for (int i = 0; i < permsList.size(); i++) {
                AdminPerm adminPerm = (AdminPerm) permsList.get(i);
                AdminPerm adminPerm2 = getoldpermList(adminPerm, adminPermsList);
                permList.add(adminPerm2);
            }

        } else {
            permList = permsList;
        }

        List<AdminPerm> newList = new ArrayList<AdminPerm>();
        //一级权限
        List<?> oneList = getNodeList(permList, null, NumConstant.common_number_2);
        Iterator<?> iter = oneList.iterator();
        while (iter.hasNext()) {
            AdminPerm admPerm = (AdminPerm) iter.next();
            String permCode = admPerm.getPermCode();

            //二级权限
            List<?> toList = getNodeList(permList, permCode, NumConstant.common_number_4);
            admPerm.setChildren(toList);
            if (StrTool.listNotNull(toList)) {

            }
            newList.add(admPerm);

            //三级权限
            if (StrTool.listNotNull(toList)) {
                Iterator<?> iter2 = toList.iterator();
                while (iter2.hasNext()) {
                    AdminPerm admPerm2 = (AdminPerm) iter2.next();
                    String permCode2 = admPerm2.getPermCode();
                    List<?> threeList = getNodeList(permList, permCode2, NumConstant.common_number_6);
                    admPerm2.setChildren(threeList);
                }
            }
        }

        String jsonStr = setJsonData(oneList, permList);

        return jsonStr;
    }

    /**
     * 封装json数据
     */
    public String setJsonData(List<?> oneList, List<?> newList) {

        //加载一个缓存map
        PermConfig.loadPerm();
        //在放入权限之前先清空map集合
        PermConfig.clear();
        //对每个模块实例化一个集合
        List<AdminPerm> homeList = new ArrayList<AdminPerm>();
        List<AdminPerm> adminList = new ArrayList<AdminPerm>();
        List<AdminPerm> userList = new ArrayList<AdminPerm>();
        List<AdminPerm> tokenList = new ArrayList<AdminPerm>();
        List<AdminPerm> authList = new ArrayList<AdminPerm>();
        List<AdminPerm> logList = new ArrayList<AdminPerm>();
        List<AdminPerm> reportList = new ArrayList<AdminPerm>();
        List<AdminPerm> configList = new ArrayList<AdminPerm>();
        List<AdminPerm> orgunList = new ArrayList<AdminPerm>();
        List<AdminPerm> monitorList = new ArrayList<AdminPerm>();
        List<AdminPerm> dataList = new ArrayList<AdminPerm>();
        //将每个模块的权限封装到对应的集合中
        List<String> moduelIdList = new ArrayList<String>();
        for (int j = 0; j < newList.size(); j++) {
            AdminPerm adminPerm = (AdminPerm) newList.get(j);
            String oldcode = adminPerm.getPermCode();
            if (StrTool.strEquals(oldcode, Constant.HOME_CODE)) {
                homeList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.ADMIN_CODE)) {
                adminList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.USER_CODE)) {
                userList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.TOKEN_CODE)) {
                tokenList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.AUTH_CODE)) {
                authList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.CONFIG_CODE)) {
                configList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.LOG_CODE)) {
                logList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.REPORT_CODE)) {
                reportList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.ORGUN_CODE)) {
                orgunList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.MONITOR_CODE)) {
                monitorList.add(adminPerm);
            }
            if (StrTool.strEquals(oldcode, Constant.DATA_CODE)) {
                dataList.add(adminPerm);
            }

        }
        AdminPerm adminP = null;
        boolean flag = true;
        for (int i = 0; i < oneList.size(); i++) {
            adminP = (AdminPerm) oneList.get(i);
            // 如果角色中含有首页，FLAG为false;
            if (Constant.HOME_CODE.equals(adminP.getPermCode())) {
                flag = false;
                break;
            }
        }

        for (int i = 0; i < oneList.size(); i++) {
            adminP = (AdminPerm) oneList.get(i);
            if (Constant.ORGUN_CODE.equals(adminP.getPermCode())) {
                if (flag) {
                    //将组织机构放在首位，与菜单保持一致
                    reorder(oneList, i, 0);
                } else {
                    //将组织机构放在首页后，与菜单保持一致
                    reorder(oneList, i, 1);
                }
                break;
            }
        }

        //根据顶级模块将每个模块下的权限集合放入到MAP中进行缓存
        for (int k = 0; k < oneList.size(); k++) {
            AdminPerm aPerm = (AdminPerm) oneList.get(k);
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.HOME_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(homeList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.ADMIN_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(adminList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.USER_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(userList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.TOKEN_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(tokenList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.AUTH_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(authList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.CONFIG_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(configList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.LOG_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(logList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.REPORT_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(reportList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.ORGUN_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(orgunList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.MONITOR_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(monitorList));
            }
            if (StrTool.strEquals(aPerm.getPermCode(), Constant.DATA_CODE)) {
                PermConfig.putObject(aPerm.getPermCode(), setTreeStryle(dataList));
            }
            //系统所有TOP导航模块的集合，权限码+#+权限显示文字，在前台用于lab显示
            moduelIdList.add(aPerm.getPermCode() + "#" + aPerm.getText());
        }

        return JsonTool.getJsonFromList(moduelIdList.size(), moduelIdList, null);
    }

    /**
     * 设置Tree的图标等属性值
     * 方法说明
     * @Date in Jan 25, 2013,4:59:20 PM
     * @param nTreadminTreeListeList
     * @return
     */
    public List<AdminPerm> setTreeStryle(List<AdminPerm> admintreeList) {
        if (StrTool.listNotNull(admintreeList)) {
            for (AdminPerm adminPerm : admintreeList) {
                //设置叶子节点的显示图标
                if (!StrTool.listNotNull(adminPerm.getChildren())) {//没有子节点
                    adminPerm.setIcon(request.getContextPath()
                            + "/manager/common/js/ligerUI/skins/Aqua/images/tree/tree-leaf.gif");

                } else {
                    int checkedToal = 0;
                    for (AdminPerm ap : (List<AdminPerm>) adminPerm.getChildren()) {
                        if (ap.getChecked().equals("checked")) {
                            checkedToal = checkedToal + 1;
                        }
                    }

                    //某个节点的直接下级都处于选中状态时，当前节点设置为选中状态（页面显示为勾选状态）
                    if (checkedToal == adminPerm.getChildren().size()) {
                        adminPerm.setChecked("checked");
                    }

                    //某个节点的直接下级有一个或多个（多个不包括直接下级的所有），当前节点设置为不完全选中状态（页面显示为空心绿色状态）
                    if (checkedToal > 0 & (checkedToal != adminPerm.getChildren().size())) {
                        adminPerm.setChecked("inchecked");
                    }

                    setTreeStryle((List<AdminPerm>) adminPerm.getChildren()); //存在子节点，继续递归
                }
            }
        }
        return admintreeList;
    }

    /**
     * 取得树型菜单中根节点(一级节点)CODE 两位
     * @Date in Jun 30, 2011,5:42:31 PM
     * @param permList
     * @return
     */
    private List<?> getNodeList(List<?> permList, String code, int len) {
        List<Object> list = new ArrayList<Object>();
        Iterator<?> iter = permList.iterator();
        int len1 = 0;
        if (StrTool.strNotNull(code)) {
            len1 = code.length();
        }
        while (iter.hasNext()) {
            AdminPerm admPerm = (AdminPerm) iter.next();
            String permCode = admPerm.getPermCode();
            String cStr = "";
            int len2 = permCode.length();
            if (len2 >= len1) {
                cStr = permCode.substring(0, len1);
            }
            if ((!StrTool.strNotNull(code)) && (permCode.length() == len)) {
                admPerm.setText(getResVal(permCode));
                admPerm.setId(permCode);
                admPerm.setIcon("");

                list.add(admPerm);
            } else if (StrTool.strEquals(cStr, code) && len2 == len) {
                admPerm.setText(getResVal(permCode));
                admPerm.setId(permCode);
                list.add(admPerm);
            }
        }

        return list;
    }

    /**  
     * 将"组织机构"排到首位;  
     * @param list  
     * @param inEdelement 需要插入的元素  
     * @param inLocation 插入的位置  
     * @Date in Apr 25, 2013,3:12:45 PM
    */
    private static void reorder(List list, int inEdelement, int inLocation) {
        //先把inEdelement对应的元素取出 再插入到指定位置   
        list.add(inLocation, list.remove(inEdelement));
    }

    /**
     * 从多语言文件中获取permCode表示的意义
     * @Date in Jun 30, 2011,6:11:45 PM
     * @param permCode
     * @return
     */
    private String getResVal(String permCode) {
        String actionIdkey = StrConstant.ADM_PERM_KEY + permCode;
        return Language.getLangStr(request, actionIdkey);
    }

    /**
     * 查询变更指派人
     * @throws UnsupportedEncodingException 
     */
    public String queryDesignateRole() throws UnsupportedEncodingException {
        List<?> designateList = null;
        String roleNames = java.net.URLDecoder.decode(request.getParameter("roleNames"), "UTF-8");
        try {
            AdminUser admUser = new AdminUser();
            admUser.setLoginUser(super.getCurLoginUserRole());//当前管理员的角色
            admUser.setAdminid(super.getCurLoginUser()); //当前登录管理员

            //超级管理员可以变更任何角色的创建人（除了自身所属的角色不能变更）,指派人可以是任何管理员，包括自身
            designateList = adminUserServ.queryAdminDesignate(admUser); //查询指派人
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        super.setRequestPageObj(roleNames);
        super.setRequestPageList(designateList);
        request.setAttribute("curPage", request.getParameter("curPage"));

        return "change_creater_role";
    }

    /**
     * 变更角色创建人
     * 方法说明
     * @Date in Jun 11, 2012,6:23:18 PM
     * @return
     * @throws UnsupportedEncodingException 
     */
    public String designate() throws UnsupportedEncodingException {
        String dnateRoleStr = request.getParameter("roleNameStr");
        //变更的角色
        dnateRoleStr = java.net.URLDecoder.decode(dnateRoleStr, "UTF-8");
        //角色的指派人
        String designUserId = request.getParameter("adminId");
        String roleNameStr[] = dnateRoleStr.split(",");
        Set<?> keys = null;
        List<Object> list = new ArrayList<Object>();
        RoleInfo editRole = null;
        try {
            if (StrTool.strNotNull(dnateRoleStr) && StrTool.strNotNull(designUserId)) {
            	
            	// Start, 判断变更角色权限代码与指派人角色权限代码的对比
            	// Start,取出指派人角色
            	String[] tempAdm = new String[1];
                tempAdm[0] = designUserId;
                AdminAndRole adminAndRole = new AdminAndRole();
                adminAndRole.setAdminIds(tempAdm);
                
                // 指派人可能含有多个角色
                List<?> adminRoleList = adminAndRoleServ.queryAdmins(adminAndRole);
                List<String> codeList = new ArrayList();
                for (int i=0; i<adminRoleList.size(); i++){
                	AdminAndRole adminRole = (AdminAndRole)adminRoleList.get(i);
                	RolePerm rolePem = new RolePerm();
                	rolePem.setRoleId(adminRole.getRoleId());
                	List<?> desPerms = rolePermServ.query(rolePem, new PageArgument());
                	
                	// Start,指派人权限代码的集合
                    for (int h = 0; h < desPerms.size(); h++) {
                        RolePerm rPer = (RolePerm) desPerms.get(h);
                        codeList.add(rPer.getPermcode());
                    }
                    // End,指派人权限代码的集合
                }
                
                // Start,去掉LIST中的重复项
                HashSet hash = new HashSet(codeList);      
                codeList.clear();      
                codeList.addAll(hash);
                // End,去掉LIST中的重复项
            	// End,取出指派人角色
                
                // Start,取出变更角色
                for (int j=0; j<roleNameStr.length; j++){
                	String roleName = roleNameStr[j];
                	RoleInfo roleInfo = new RoleInfo();
                	roleInfo.setRoleName(roleName);
                	roleInfo = (RoleInfo)roleInfoServ.find(roleInfo);
                	
                	RolePerm roleAd = new RolePerm();
                	roleAd.setRoleId(roleInfo.getRoleId());
                	List<?> admPerms = rolePermServ.query(roleAd, new PageArgument());
                	List<String> admCodeList = new ArrayList();
                	
                	// Start,管理员权限代码的集合
                    for (int k = 0; k < admPerms.size(); k++) {
                        RolePerm rPer = (RolePerm) admPerms.get(k);
                        admCodeList.add(rPer.getPermcode());
                    }
                    // End,管理员权限代码的集合
                    
                    if (!codeList.containsAll(admCodeList)) {
                    	
                    	// 变更管理员的权限超出指派人的权限，请调整角色权限后再进行变更！
                    	outPutOperResult(Constant.alert_error, Language.getLangStr(request,"role_change")
                    			+roleName+Language.getLangStr(request,"admin_vd_change_error")); 
                    	return null;
                    }
                }
                // End,取出变更角色
                // End, 判断变更角色权限代码与指派人角色权限代码的对比
            	
                keys = new HashSet<Object>(Arrays.asList(roleNameStr));
                Iterator<?> iter = keys.iterator();
                while (iter.hasNext()) {
                    editRole = new RoleInfo();
                    editRole.setRoleName((String) iter.next());
                    editRole.setCreateuser(designUserId);
                    list.add(editRole);
                }
                //执行变更操作=================================
                roleInfoServ.updateDsignate(list);
                outPutOperResult(Constant.alert_succ, "true");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, "false");
        }
        return null;
    }

    public RoleInfo getOldroleInfo() {
        return oldroleInfo;
    }

    public void setOldroleInfo(RoleInfo oldroleInfo) {
        this.oldroleInfo = oldroleInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#page()
     */
    public String page() {
        return null;
    }
}
