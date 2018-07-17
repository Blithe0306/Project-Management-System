/**
 *Administrator
 */
package com.ft.otp.manager.admin.role.action.aide;

import java.util.ArrayList;
import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.manager.admin.role_perm.service.IRolePermServ;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员角色辅助类功能说明
 *
 * @Date in Jul 1, 2011,5:19:19 PM
 *
 * @author ZJY
 */
public class RoleInfoActionAide {

    /**
     * 添加角色与权限的对应关系
     * @param roleid
     * @param adminpermList
     * @param rolePermServ
     */
    public void addRolePerm(int roleid, List<?> adminpermList, IRolePermServ rolePermServ) throws BaseException {
        List<Object> permList = new ArrayList<Object>();
        RolePerm rolePerm = null;

        for (int i = 0; i < adminpermList.size(); i++) {
            rolePerm = new RolePerm();
            rolePerm.setRoleId(roleid);
            rolePerm.setPermcode((String) (adminpermList.get(i)));
            permList.add(rolePerm);
        }
        rolePermServ.addRolePerm(permList);
    }

    /**
     * 根据角色id获取角色对应的权限列表
     * @Date in Jul 1, 2011,5:20:58 PM
     * @param rolePermServ
     * @param roleid
     * @return permList
     * @throws BaseException
     */
    public List<?> getPermServ(IRolePermServ rolePermServ, int roleid) throws BaseException {
        PageArgument pageArg = new PageArgument();
        RolePerm rolePerm = new RolePerm();
        rolePerm.setRoleId(roleid);

        return rolePermServ.query(rolePerm, pageArg);
    }

    /**
     * 更新角色对应的权限
     * @Date in Jul 5, 2011,5:47:41 PM
     * @param roleid
     * @param adminpermList
     * @param rolePermServ
     * @throws BaseException
     */
    public void updateRolePerm(int roleid, List<?> adminpermList, IRolePermServ rolePermServ) throws BaseException {
        List<Object> permList = new ArrayList<Object>();

        RolePerm rolePerm = null;
        if (StrTool.listNotNull(adminpermList)) {
            for (int i = 0; i < adminpermList.size(); i++) {
                rolePerm = new RolePerm();
                rolePerm.setRoleId(roleid);
                rolePerm.setPermcode((String) (adminpermList.get(i)));
                permList.add(rolePerm);
            }
        }

        if (StrTool.listNotNull(permList)) {
            rolePermServ.updateRolePerm(permList);
        }
    }

    /**
    * 更改莫个权限的同时，更改此角色关联管理员的子管理员的权限
    * @Date in Aug 8, 2011,5:42:30 PM
    * @param roles
    * @param perms
    * @param rolePermServ
    * @return
    */
    public void deleteChildRolePerm(List<?> roles, List<?> perms, IRolePermServ rolePermServ) throws BaseException {
        if (StrTool.listNotNull(roles) && StrTool.listNotNull(perms)) {
            RolePerm rolePerm = new RolePerm();
            int roleid[] = new int[roles.size()];
            String permcodes[] = new String[perms.size()];
            for (int i = 0; i < roles.size(); i++) {
                AdminAndRole adminAndRole = (AdminAndRole) roles.get(i);
                roleid[i] = adminAndRole.getRoleId();
            }
            for (int j = 0; j < perms.size(); j++) {
                permcodes[j] = (String) perms.get(j);
            }

            rolePerm.setBatchIdsInt(roleid);
            rolePerm.setPerms(permcodes);
            rolePermServ.delObj(rolePerm);
        }
    }

    /**
     * 某个角色关联的所有管理员
     * @Date in Dec 22, 2011,10:01:30 AM
     * @param adminAndRoleServ
     * @param adminUserServ
     * @param str
     * @return amrolesList
     * @throws BaseException
     */
    @SuppressWarnings("unchecked")
    public List<?> getAllChildAdminAndChildRole(IAdminAndRoleServ adminAndRoleServ, IAdminUserServ adminUserServ,
            int str[]) throws BaseException {
        // 是修改权限后，新权限中没有的已有的旧权限permcode数组
        List<?> amrolesList = null;
        List<AdminUser> results = new ArrayList<AdminUser>();

        //角色关联的用户
        AdminAndRole usRole = new AdminAndRole();
        if (StrTool.arrNotNull(str)) {
            usRole.setBatchIdsInt(str);
        }
        List<?> userList = adminAndRoleServ.queryAdmins(usRole);

        //角色关联的所有的用戶下的子用戶
        List<AdminUser> allotpuserList = (List<AdminUser>) adminUserServ.query(new AdminUser(), new PageArgument());

        if (StrTool.listNotNull(userList)) {
            for (int i = 0; i < userList.size(); i++) {
                AdminAndRole userAndRole = (AdminAndRole) userList.get(i);
                AdminUser otpuser = new AdminUser();
                otpuser.setAdminid(userAndRole.getAdminId());
                getAdminChilds(allotpuserList, otpuser, results, "");
            }
        }

        // 所有的用戶创建的所有的角色
        if (StrTool.listNotNull(results)) {
            String[] tempAdm = new String[results.size()];
            for (int i = 0; i < results.size(); i++) {
                AdminUser au = (AdminUser) results.get(i);
                tempAdm[i] = (String) au.getAdminid();
            }
            AdminAndRole amAndRole = new AdminAndRole();
            amAndRole.setAdminIds(tempAdm);
            amrolesList = adminAndRoleServ.queryAdmins(amAndRole);
        }

        return amrolesList;
    }

    /**
     * 所有的角色对应的管理员的子级管理员，递归获取
     * @Date in Aug 8, 2011,5:42:30 PM
     * @param admins
     * @param _admin
     * @param results
     * @param loginuser
     * @return results
     */
    public List<AdminUser> getAdminChilds(List<AdminUser> users, AdminUser _user, List<AdminUser> results,
            String loginuser) {
        for (AdminUser user : users) {
            if (StrTool.strNotNull(user.getCreateuser())) {
                if (user.getCreateuser().equals(_user.getAdminid())) {
                    results.add(user);
                    getAdminChilds(users, user, results, null);
                }
            }
            if (StrTool.strNotNull(loginuser) && user.getAdminid().equals(loginuser)) {
                results.add(user);
            }
        }
        return results;
    }

}
