/**
 *Administrator
 */
package com.ft.otp.manager.admin.user.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.admin_orgunit.entity.AdminAndOrgunit;
import com.ft.otp.manager.admin.admin_orgunit.service.IAdminAndOrgunitServ;
import com.ft.otp.manager.admin.admin_perm.entity.AdminPermCode;
import com.ft.otp.manager.admin.admin_perm.service.IAdminPermServ;
import com.ft.otp.manager.admin.admin_role.entity.AdminAndRole;
import com.ft.otp.manager.admin.admin_role.service.IAdminAndRoleServ;
import com.ft.otp.manager.admin.role.entity.RoleInfo;
import com.ft.otp.manager.admin.role.service.IRoleInfoServ;
import com.ft.otp.manager.admin.user.action.aide.AdminUserActionAide;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.form.AdminUserQueryForm;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.orgunit.domain.service.IDomainInfoServ;
import com.ft.otp.manager.orgunit.orgunit.entity.OrgunitInfo;
import com.ft.otp.manager.orgunit.orgunit.service.IOrgunitInfoServ;
import com.ft.otp.manager.project.service.IProjectServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.manager.user_token.service.IUserTokenServ;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.alg.base.digest.IDigest;
import com.ft.otp.util.alg.base.digest.MD5;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.PwdEncTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员业务操作Action
 * @Date in Jun 29, 2011,10:20:13 AM
 * @author TBM
 */
public class AdmUserAction extends BaseAction implements IBaseAction {

	private static final long	serialVersionUID	= -2837284105342887574L;
	private Logger				logger				= Logger.getLogger(AdmUserAction.class);
	//管理员服务接口
	private IAdminUserServ		adminUserServ		= null;
	//管理员-角色关系服务接口
	public IAdminAndRoleServ	adminAndRoleServ	= (IAdminAndRoleServ) AppContextMgr.getObject("adminAndRoleServ");
	//管理员常用功能-权限码服务接口
	public IAdminPermServ		adminPermCodeServ	= (IAdminPermServ) AppContextMgr.getObject("adminPermCodeServ");

	//管理员角色接口
	private IRoleInfoServ		roleInfoServ		= (IRoleInfoServ) AppContextMgr.getObject("roleInfoServ");

	//管理员和组织机构关系业务操作
	public IAdminAndOrgunitServ	adminAndOrgunitServ	= (IAdminAndOrgunitServ) AppContextMgr.getObject("adminAndOrgunitServ");
	//组织机构操作类
	public IOrgunitInfoServ		orgunitInfoServ		= (IOrgunitInfoServ) AppContextMgr.getObject("orgunitInfoServ");
	//域业务操作类
	public IDomainInfoServ		domainInfoServ		= (IDomainInfoServ) AppContextMgr.getObject("domainInfoServ");
	//用户令牌服务接口
	private IUserTokenServ		userTokenServ		= (IUserTokenServ) AppContextMgr.getObject("userTokenServ");
	// 令牌服务接口
	private ITokenServ			tokenServ			= (ITokenServ) AppContextMgr.getObject("tokenServ");
	//定制项目服务接口
	private IProjectServ		projectServ			= null;

	//管理员业务操作辅助类
	private AdminUserActionAide	aide				= new AdminUserActionAide();
	private AdminUserQueryForm	queryForm			= null;
	private AdminUser			adminUser			= null;
	private AdminUser			oldAdminUser		= null;

	public AdminUser getOldAdminUser() {
		return oldAdminUser;
	}

	public void setOldAdminUser(AdminUser oldAdminUser) {
		this.oldAdminUser = oldAdminUser;
	}

	/**
	 * @return the adminUserServ
	 */
	public IAdminUserServ getAdminUserServ() {
		return adminUserServ;
	}

	/**
	 * @param adminUserServ the adminUserServ to set
	 */
	public void setAdminUserServ(IAdminUserServ adminUserServ) {
		this.adminUserServ = adminUserServ;
	}

	/**
	 * @return the queryForm
	 */
	public AdminUserQueryForm getQueryForm() {
		return queryForm;
	}

	/**
	 * @param queryForm the queryForm to set
	 */
	public void setQueryForm(AdminUserQueryForm queryForm) {
		this.queryForm = queryForm;
	}

	/**
	 * @return the adminUser
	 */
	public AdminUser getAdminUser() {
		return adminUser;
	}

	/**
	 * @param adminUser the adminUser to set
	 */
	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	/**
	 * 取出AdminUserQueryForm中的实体
	 * @Date in May 5, 2011,6:30:40 PM
	 * @param qForm
	 * @return
	 */
	public AdminUser getAdminUser(AdminUserQueryForm qForm) {
		AdminUser adUser = new AdminUser();
		adUser.setLocked(-1);
		if (StrTool.objNotNull(qForm)) {
			adUser = qForm.getAdminUser();
		}

		if (StrTool.strNotNull(super.getCurLoginUser())) {
			adUser.setLoginUser(super.getCurLoginUser());
		}
		if (StrTool.strNotNull(super.getCurLoginUserRole())) {
			adUser.setCurLoginUserRole(super.getCurLoginUserRole());
		}

		return adUser;
	}

	/**
	 * 管理员信息列表初始化
	 */
	public String init() {
		String rflag = request.getParameter("rflag");
		if (isNeedClearForm()) {
			queryForm = null;
		}
		try {
			PageArgument pageArg = pageArgument();
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			List<?> resultList = query(pageArg);
			if (StrTool.strEquals(rflag, "1")) {
				if (StrTool.objNotNull(resultList) && resultList.size() > 0) {
					AdminUser adminUser = (AdminUser) projectServ.selectPowerAdmin();
					for (int i = 0; i < resultList.size(); i++) {
						AdminUser temp = (AdminUser) resultList.get(i);
						if (adminUser.getAdminid().equals(temp.getAdminid())) {
							resultList.remove(i);
							break;
						}
					}
				}
			}
			String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
			setResponseWrite(json);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	/**
	 * 获取管理员与域或组织机构对应关系实例列表
	 * @param adminId
	 * @param orgunitIds return
	 */
	public List<AdminAndOrgunit> getAAOList(String adminId, String orgunitIds) {
		List<AdminAndOrgunit> adminAndOrgunitList = new ArrayList<AdminAndOrgunit>();

		if (!StrTool.indexOf(orgunitIds, ",")) { return adminAndOrgunitList; }

		//管理员-域或组织机构
		String[] orgunitIdArray = orgunitIds.split(","); // 该数组的每一元素是 domainId:orgunitId,
		for (int i = 0; i < orgunitIdArray.length; i++) {
			String[] domainIdorguintidArray = orgunitIdArray[i].split(":");
			AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit();
			adminAndOrgunit.setAdminId(adminId);
			adminAndOrgunit.setDomainId(StrTool.parseInt(domainIdorguintidArray[0]));

			if (StrConstant.common_number_0.equals(domainIdorguintidArray[1])) { //如果机构是0  意味着该管理只管理域
				adminAndOrgunit.setOrgunitId(null); //空机构
			} else {
				adminAndOrgunit.setOrgunitId(StrTool.parseInt(domainIdorguintidArray[1])); //机构id
			}
			adminAndOrgunitList.add(adminAndOrgunit);
		}

		return adminAndOrgunitList;
	}

	/**
	 * 添加管理员
	 */
	public String add() {
		List<?> admAndRoList = null;
		boolean isActivate = false;//是否需要邮件激活
		try {
			if (!StrTool.objNotNull(adminUser)) {
				outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_info_null"));
				return null;
			}

			//是否需要邮件激活
			if (ConfDataFormat.getSysConfEmailEnabled()) {//需要激活
				isActivate = true;
				adminUser.setEnabled(NumConstant.common_number_0); //未启用、登录时需激活
			} else {
				adminUser.setEnabled(NumConstant.common_number_1); //添加管理员初始默认为启用,2013-4-17,liuxianhe
			}

			//添加管理员
			adminUser.setCreatetime(StrTool.timeSecond());
			adminUser.setPwdsettime(StrTool.timeSecond());
			adminUser.setPwd(PwdEncTool.encPwd(adminUser.getPwd()));

			// 日志
			String roles = "";
			for (int i = 0; i < adminUser.getAdminRoles().size(); i++) {
				RoleInfo roleInfo = new RoleInfo();
				roleInfo.setRoleId(StrTool.parseInt((String) adminUser.getAdminRoles().get(i)));
				roleInfo = (RoleInfo) roleInfoServ.find(roleInfo);
				roles += roleInfo.getRoleName() + "，";
			}
			adminUser.setRolenameStr(roles);
			// 日志
			adminUserServ.addObj(adminUser);

			if (StrTool.listNotNull(adminUser.getAdminRoles())) {
				admAndRoList = aide.getAdminAndRole(adminUser.getAdminid(), adminUser.getAdminRoles());
				//添加管理员对应的角色
				adminAndRoleServ.addAdminAndRole(admAndRoList);
			}

			//添加 管理员域的对应关系 管理员组织机构关系
			if (StrTool.strNotNull(adminUser.getOrgunitIds())) {
				List<AdminAndOrgunit> adminAndOrgunitList = getAAOList(adminUser.getAdminid(), adminUser.getOrgunitIds());
				adminAndOrgunitServ.addObjs(adminAndOrgunitList);
			}

			//发送管理员激活的邮件
			if (isActivate) {
				boolean result = aide.activeMailSend(adminUser, request, NumConstant.common_number_0);
				if (!result) {
					outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_add_success_email_error"));
					return null;
				}
			}

			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_info_add_success"));
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_info_add_error"));
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 管理员密码重置
	 * @Date in Apr 9, 2013,5:49:49 PM
	 * @return
	 */
	public String resetPwd() {
		try {
			String adminId = request.getParameter("adminid");

			if (!StrTool.strNotNull(adminId)) {
				outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_reset_pwd_error"));
				return null;
			}

			AdminUser adminUser = new AdminUser();
			adminUser.setAdminid(adminId);

			// adminUser.setPwd(PwdEncTool.encPwd(PwdEncTool.genAdminPwd()));
			adminUser.setPwd(PwdEncTool.encPwd(ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER, ConfConstant.DEFAULT_USER_PWD)));
			adminUser.setPwdsettime(StrTool.timeSecond());
			adminUserServ.updatePassword(adminUser);

			// 重置后启用状态改为 2 登录时需修改密码
			adminUserServ.updateEnabled(adminUser.getAdminid(), NumConstant.common_number_2);
			adminUser = findData(adminUser);

			//发送管理员激活的邮件
			boolean result = aide.activeMailSend(adminUser, request, NumConstant.common_number_1);
			if (!result) {
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_reset_pwd_success_email_error"));
			} else {
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_reset_pwd_success"));
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_reset_pwd_error"));
		}

		return null;
	}

	/**
	 * 找回密码和 激活链接点击请求方法 验证激活邮箱邮件访问此方法
	 */
	public String validEmail() {
		try {
			// 获取链接中的参数
			String userid = request.getParameter("userid");
			String pwd = request.getParameter("id");
			String source = request.getParameter("source");
			String lang = request.getParameter("lang");
			if (StrTool.strNotNull(lang)) {
				if (StrTool.strEquals(lang, Constant.zh_CN) || StrTool.strEquals(lang, Constant.en_US)) {
					request.getSession(true).setAttribute(Constant.LANGUAGE_SESSION_KEY, lang);
				}
			}

			AdminUser adminUser = new AdminUser();
			adminUser.setReAdminId(userid);
			adminUser.setRePwd(pwd);
			this.adminUser = adminUser;
			request.setAttribute("source", source);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "resetpwd";
	}

	/**
	 * 删除管理员
	 */
	public String delete() {
		try {
			Set<?> keySet = super.getDelIds("delUserIds");
			if (!StrTool.setNotNull(keySet)) {
				outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
				return null;
			}

			Iterator<?> iter = keySet.iterator();
			Set<String> delIdsSet = new HashSet<String>();
			while (iter.hasNext()) {
				String adminId = (String) iter.next();
				//当前管理员登录，不能删除自己
				if (StrTool.strEquals(adminId, super.getCurLoginUser())) {
					outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_current_login_admin") + super.getCurLoginUser()
							+ Language.getLangStr(request, "admin_not_deleted"));
					return null;
				} else {
					delIdsSet.add(adminId);

					//删除管理员令牌绑定关系
					UserToken ut = new UserToken();
					ut.setUserId(adminId);
					ut.setDomainId(-1);//删除时域 is null
					userTokenServ.delObj(ut);
				}
			}

			if (StrTool.setNotNull(delIdsSet)) {
				//管理员真正删除操作
				adminUserServ.delObj(delIdsSet);

				clearSession(request.getParameter("delUserIds"));
			}

			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
		}

		return null;
	}

	/**
	 * 查询公共方法，根据ADMINID查询
	 * @Date in Aug 1, 2011,3:37:47 PM
	 * @param adminUser
	 * @return
	 */
	public AdminUser findData(AdminUser adminUser) throws BaseException {
		AdminUser admUser = new AdminUser();
		admUser.setAdminid(adminUser.getAdminid().toLowerCase()); //大写转换小写
		admUser = (AdminUser) adminUserServ.find(admUser);

		return admUser;
	}

	/**
	 * 设置管理员管理的组织机构或域
	 * @author adminUser
	 * @date in Aug 1, 2011,3:37:47 PM
	 * @param admUser
	 * @param flag，1代表查看，2代表其它
	 * @return
	 */
	public AdminUser setOrgIdName(AdminUser admUser, int flag) {
		try {
			//查找当前管理员所管理的域 或者组织机构
			AdminAndOrgunit adminAndOrgunit = new AdminAndOrgunit(admUser.getAdminid(), NumConstant.common_number_0, NumConstant.common_number_0);
			List<?> aaoList = adminAndOrgunitServ.queryAdminAndOrgunitByAdminId(adminAndOrgunit);

			if (!StrTool.listNotNull(aaoList)) { return admUser; }

			OrgunitInfo[] oArray = null;
			//查询出所有的组织机构
			List<?> orgunitList = orgunitInfoServ.queryWholeList(new OrgunitInfo());
			if (StrTool.listNotNull(orgunitList)) {
				oArray = orgunitList.toArray(new OrgunitInfo[orgunitList.size()]);
			}

			StringBuffer orgunitIdsB = new StringBuffer();
			StringBuffer orgunitNamesB = new StringBuffer();

			AdminAndOrgunit aao = null;
			Iterator<?> it = aaoList.iterator();
			String br = ",";
			if (flag == NumConstant.common_number_1) {
				br = "<br>";
			}
			while (it.hasNext()) {
				aao = (AdminAndOrgunit) it.next();
				int domainId = aao.getDomainId();
				int orgunitId = aao.getOrgunitId() == null ? 0 : aao.getOrgunitId().intValue();

				orgunitIdsB.append(domainId + ":" + orgunitId + ",");//组装orgunitIds

				if (orgunitId == NumConstant.common_number_0) {//该条绑定关系中没有组织机构 就只取域
					orgunitNamesB.append(DomainConfig.getValue(domainId));
				} else { //机构名称
					// 取得匹配的组织机构
					if (StrTool.objNotNull(oArray)) {
						for (int i = 0; i < oArray.length; i++) {//编辑机构名称
							if (orgunitId == oArray[i].getOrgunitId()) {
								orgunitNamesB.append(oArray[i].getOrgunitName());//组装orgunitNames
								break;
							}
						}
					} else {
						orgunitNamesB.append("");//组装orgunitNames
					}
				}
				orgunitNamesB.append(br);
			}
			admUser.setOrgunitIds(orgunitIdsB.toString());
			admUser.setOrgunitNames(orgunitNamesB.toString());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return admUser;
	}

	/**
	 * 查找管理员信息
	 */
	public String find() {
		AdminUser admUser = null;
		List<?> roleList = null;
		try {
			admUser = findData(adminUser);
			if (!StrTool.objNotNull(admUser)) { return init(); }
			//管理员对应的角色
			roleList = aide.getRoleServ(adminAndRoleServ, adminUser);
			if (StrTool.listNotNull(roleList)) {
				admUser.setAdminRoles(roleList);
			}

			//查找设置当前管理员所管理的域 或者组织机构
			admUser = setOrgIdName(admUser, 2);
			this.setAdminUser(admUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return SUCC_FIND;
	}

	/**
	 * 修改管理员基本信息时 查找管理员信息
	 */
	public String baseInfoFind() {
		AdminUser admUser = null;
		try {
			admUser = findData(adminUser);
			this.setAdminUser(admUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "base_find";
	}

	/**
	 * 修改管理员信息
	 */
	public String modify() {
		List<?> admAndroleList = null;
		try {
			if (StrTool.objNotNull(adminUser)) {
				// Start,日志准备
				findToLog();
				// End,日志准备

				adminUserServ.updateObj(adminUser);
				if (StrTool.strNotNull(adminUser.getAdminid())) {
					//更新管理员对应的角色 修改后没有角色要删除已有的
					admAndroleList = aide.getAdminAndRole(adminUser.getAdminid(), adminUser.getAdminRoles());
					//更新管理员对应的角色 先删除旧的后插入新的角色
					if (StrTool.listNotNull(admAndroleList)) {
						adminAndRoleServ.updateAdminAndRole(admAndroleList);
					} else {
						//只删除
						AdminAndRole aRole = new AdminAndRole();
						aRole.setBatchIds(new String[] { adminUser.getAdminid() });
						adminAndRoleServ.delObj(aRole);
					}
				}

				// 删除该管理员对应的域 或组织机构关系
				AdminAndOrgunit aao = new AdminAndOrgunit(adminUser.getAdminid(), NumConstant.common_number_0, NumConstant.common_number_0);
				adminAndOrgunitServ.delObj(aao);
				//重新添加添加 管理员域的对应关系 管理员组织机构关系
				List<AdminAndOrgunit> adminAndOrgunitList = getAAOList(adminUser.getAdminid(), adminUser.getOrgunitIds());
				adminAndOrgunitServ.addObjs(adminAndOrgunitList);

				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
			} else {
				outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_info_null"));
			}
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 修改管理员 基本信息
	 */
	public String modifyBase() {
		try {

			if (StrTool.objNotNull(adminUser)) {
				// Start,日志准备
				AdminUser admUser = null;
				admUser = findData(adminUser);
				this.setOldAdminUser(admUser);
				// End,日志准备

				//更新管理员基本信息  enabled 也被更新了，页面有隐藏，密码有专门修改方法，此处不再修改密码
				adminUserServ.updateObj(adminUser);
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
			} else {
				outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_info_null"));
			}
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 添加管理员之前检查管理员是否已经存在
	 * @Date in Jul 5, 2011,6:06:04 PM
	 * @return
	 */
	public String findAdminisExist() {
		AdminUser admUser = null;
		try {
			//查找管理员
			admUser = findData(adminUser);
			if (null != admUser) {
				setResponseWrite("false");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setResponseWrite("false");
		}

		return null;
	}

	public String page() {
		PageArgument pageArgument = getArgument(request, 0);
		query(pageArgument);

		return SUCCESS;
	}

	/**
	 * 管理员详细信息
	 */
	public String view() {
		AdminUser admUser = null;
		List<?> list = null;
		List<?> roleList = null;
		try {
			//查找管理员
			admUser = findData(adminUser);
			if (!StrTool.objNotNull(admUser)) {
				//return init();
				setResponseWrite(Language.getLangStr(request, "tkn_view_admin_not_exist"));
				return null;
			}
			//管理员对应的角色
			list = aide.getRoleServ(adminAndRoleServ, adminUser);
			if (StrTool.listNotNull(list)) {
				roleList = aide.getAdminRoleList(list, roleInfoServ);
			}
			if (StrTool.listNotNull(roleList)) {
				admUser.setHidAdminRoles(roleList);
			}

			// 管理员锁定时间
			if (admUser.getLoginlocktime() != NumConstant.common_number_0) {
				admUser.setLoginlocktimeStr(DateTool.dateToStr(admUser.getLoginlocktime(), true));
			}

			//查找设置当前管理员所管理的域 或者组织机构
			admUser = setOrgIdName(admUser, 1);
			this.setAdminUser(admUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return SUCC_VIEW;
	}

	/**
	 * 处理数据查询
	 * @Date in Apr 10, 2011,10:56:03 AM
	 * @param pageArg
	 */

	@SuppressWarnings("unchecked")
	private List<?> query(PageArgument pageArg) {
		AdminUser admUser = null;
		List<?> adminList = null;
		List<AdminUser> admList = new ArrayList<AdminUser>();
		try {
			//超级管理员（所有管理员）
			if (StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
				adminList = adminUserServ.query(getAdminUser(queryForm), pageArg);
			} else {
				AdminUser queryUser = getAdminUser(queryForm);
				queryUser.setLoginUser(super.getCurLoginUser());

				//普通管理员（查询自己和自己创建的管理员）
				adminList = aide.getCurrLoginUserChildUsers(queryUser, null);
			}

			//查询出所有管理员绑定的令牌
			List<?> tokenInfos = userTokenServ.batchQueryUT(adminList, null, NumConstant.common_number_1);
			if (StrTool.listNotNull(adminList)) {
				//查找当前管理员所管理的域 或者组织机构
				for (int i = 0; i < adminList.size(); i++) {
					adminUser = (AdminUser) adminList.get(i);

					//查找设置管理员所管理的域 或者组织机构
					admUser = setOrgIdName(adminUser, 2);

					//设置当前管理员所绑定的令牌
					admUser = aide.setAdminTokens(adminUser, tokenInfos);
					admList.add(admUser);
				}
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return admList;
	}

	/**
	 * 行数统计 分页处理
	 * @Date in Apr 15, 2011,11:33:36 AM
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private PageArgument pageArgument() throws BaseException {
		int totalRow = 0;
		if (StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
			totalRow = adminUserServ.count(getAdminUser(queryForm));
		} else {
			AdminUser queryUser = getAdminUser(queryForm);
			queryUser.setLoginUser(super.getCurLoginUser());

			List<?> list = aide.getCurrLoginUserChildUsers(queryUser, null);
			totalRow = list.size();
		}
		String rflag = request.getParameter("rflag");
		if (rflag.equals("1")) {
			PageArgument pageArg = getArgument(totalRow - 1);
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			return pageArg;
		} else {
			PageArgument pageArg = getArgument(totalRow);
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			return pageArg;
		}

	}

	/**
	 * 启用、禁用某个管理员
	 */
	public String enabledAdmin() throws BaseException {
		try {
			if (StrTool.objNotNull(adminUser)) {
				if (adminUser.getEnabled() == NumConstant.common_number_0) {
					adminUserServ.updateEnabled(adminUser.getAdminid(), NumConstant.common_number_1);
					outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_enable_succ_tip"));
				} else {
					adminUserServ.updateEnabled(adminUser.getAdminid(), NumConstant.common_number_0);
					outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_disabled_succ_tip"));

					clearSession(adminUser.getAdminid());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
		}

		return null;
	}

	/**
	 * 锁定解锁管理员
	 * @Date in Apr 17, 2013,10:48:39 AM
	 * @return
	 */
	public String lockedAdmin() {
		String adminid = request.getParameter("adminid");
		String lockStatus = request.getParameter("locked");
		AdminUser aUser = new AdminUser();
		try {
			aUser.setAdminid(adminid);
			if (StrTool.strEquals(lockStatus, StrConstant.common_number_0)) {
				aUser.setLocked(NumConstant.common_number_2);//永久锁定
				aUser.setLoginlocktime(StrTool.timeSecond());
			} else {
				aUser.setLocked(NumConstant.common_number_0);
				aUser.setLoginlocktime(0);
			}

			aUser.setTemploginerrcnt(0);
			aUser.setLongloginerrcnt(0);
			adminUserServ.updateLocked(aUser);

			if (StrTool.strEquals(lockStatus, StrConstant.common_number_0)) {
				clearSession(adminid);
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_locked_success"));
			} else {
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_unlocked_success"));
			}
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 判断原(旧)密码是否正确
	 * @Date in Jul 15, 2011,3:51:28 PM
	 * @return
	 */
	public String pwdIsCorrect() {
		try {
			String adminId = request.getParameter("adminId");
			AdminUser admUser = new AdminUser();
			admUser.setAdminid(adminId);
			AdminUser admInfo = (AdminUser) adminUserServ.find(admUser);

			String pwdDBEnc = admInfo.getPwd();
			String pwdEnc = PwdEncTool.encPwd(adminUser.getPwdOld());
			if (StrTool.strEquals(pwdDBEnc, pwdEnc)) {
				super.setResponseWrite("true");
			} else {
				super.setResponseWrite("false");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			super.setResponseWrite("false");
		}

		return null;
	}

	/**
	 * 修改管理员密码
	 * @throws BaseException
	 */
	public String modifyPassword() throws BaseException {
		try {
			adminUser.setAdminid(adminUser.getAdminid());
			adminUser.setPwd(PwdEncTool.encPwd(adminUser.getPwd()));
			adminUser.setPwdsettime(StrTool.timeSecond());

			adminUserServ.updatePassword(adminUser);
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_update_pwd_success"));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_update_pwd_error"));
		}

		return null;
	}

	/**
	 * 编辑个人信息
	 * @throws BaseException
	 */
	public String editPassword() throws BaseException {
		try {
			adminUser.setAdminid(adminUser.getAdminid());
			adminUser.setPwd(PwdEncTool.encPwd(adminUser.getPwd()));
			adminUser.setPwdsettime(StrTool.timeSecond());

			adminUserServ.updatePassword(adminUser);
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_update_pwd_success"));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_update_pwd_error"));
		}

		return null;
	}

	/**
	 * 管理员密码找回 或者 管理员获取应急口令
	 * @Date in Apr 9, 2013,9:07:17 PM
	 * @return
	 */
	public String getAdminPwd() {
		String source = request.getParameter("source");
		try {
			if (!StrTool.strNotNull(source)) { return null; }

			AdminUser adm = (AdminUser) adminUserServ.find(adminUser);
			if (!StrTool.objNotNull(adm) || !StrTool.strEquals(adm.getEmail(), StrTool.trim(adminUser.getEmail()))) {
				String errorStr = Language.getLangStr(request, "admin_find_pwd_email_error");
				if (StrTool.strEquals(source, "1")) {
					errorStr = Language.getLangStr(request, "admin_get_pin_email_error");
				}
				outPutOperResult(Constant.alert_error, errorStr);
				return null;
			}

			if (StrTool.strEquals(source, "1")) {

				// 应急口令，管理员是否绑定令牌
				UserToken userToken = new UserToken();
				userToken.setUserId(adm.getAdminid());
				List<?> admList = userTokenServ.selectAdminTokens(userToken);
				String errorStr = "";
				if (!StrTool.listNotNull(admList)) {
					errorStr = Language.getLangStr(request, "admin_unbind_token_1");
					outPutOperResult(Constant.alert_error, errorStr);
					return null;
				} else {
					UserToken admToken = (UserToken) admList.get(0);
					TokenInfo tokenInfo = new TokenInfo();
					tokenInfo.setToken(admToken.getToken());
					tokenInfo = (TokenInfo) tokenServ.find(tokenInfo);

					if (tokenInfo.getAuthmethod() == NumConstant.common_number_0) {
						errorStr = Language.getLangStr(request, "admin_bind_token_attest");
						outPutOperResult(Constant.alert_error, errorStr);
						return null;
					}

					if (tokenInfo.getLocked() == NumConstant.common_number_1) {
						errorStr = Language.getLangStr(request, "admin_bind_token_lock_2");
						outPutOperResult(Constant.alert_error, errorStr);
						return null;
					}

					if (tokenInfo.getLocked() == NumConstant.common_number_2) {
						errorStr = Language.getLangStr(request, "admin_bind_token_lock_1");
						outPutOperResult(Constant.alert_error, errorStr);
						return null;
					}

					if (tokenInfo.getEnabled() == NumConstant.common_number_0) {
						errorStr = Language.getLangStr(request, "admin_bind_token_unenable");
						outPutOperResult(Constant.alert_error, errorStr);
						return null;
					}

					if (tokenInfo.getLogout() == NumConstant.common_number_1) {
						errorStr = Language.getLangStr(request, "admin_bind_token_cancel");
						outPutOperResult(Constant.alert_error, errorStr);
						return null;
					}

					if (tokenInfo.getLost() == NumConstant.common_number_1) {
						errorStr = Language.getLangStr(request, "admin_bind_token_loss_1");
						outPutOperResult(Constant.alert_error, errorStr);
						return null;
					}
				}
			}

			boolean result = false;
			if (StrTool.strEquals(source, "0")) {// 密码找回
				// 管理员密码找回
				result = aide.activeMailSend(adm, request, NumConstant.common_number_1);
			} else {// 获取应急口令
				result = aide.activeMailSend(adm, request, NumConstant.common_number_2);
			}

			if (result) {
				String errorStr = "";
				if (StrTool.strEquals(source, "1")) {
					errorStr = Language.getLangStr(request, "admin_get_pin_success");
				} else {
					errorStr = Language.getLangStr(request, "admin_find_pwd_success");

					// Start,回写静态密码找回有效期，当前时间+24小时
					AdminUser adminUser = new AdminUser();

					// 当前时间
					Calendar calendar = Calendar.getInstance();
					int expiretime = DateTool.dateToInt(calendar.getTime());

					// 当前时间+24小时(秒)
					int pwddeath = expiretime + 86400; // 24小时=86400秒
					adminUser.setGetpwddeath(pwddeath);
					adminUser.setAdminid(adm.getAdminid());
					adminUserServ.updatePwddeath(adminUser);
					// End,回写静态密码找回有效期，当前时间+24小时

				}
				outPutOperResult(Constant.alert_succ, errorStr);
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		String errorStr = Language.getLangStr(request, "admin_find_pwd_error");
		if (StrTool.strEquals(source, "1")) {
			errorStr = Language.getLangStr(request, "admin_get_pin_error");
		}
		outPutOperResult(Constant.alert_error, errorStr);

		return null;
	}

	/**
	 * 初始化激活管理员、设置新密码
	 * @Date in Apr 7, 2013,4:32:57 PM
	 * @return
	 */
	public String setInitAdminPwd() {
		if (!StrTool.objNotNull(adminUser)) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "admin_activation_error"));
			return null;
		}

		String source = getSource(request);// 修改密码或者激活 0 激活，1 修改

		AdminUser admUser = new AdminUser();
		try {
			String oldPwd = adminUser.getRePwd();
			admUser.setAdminid(adminUser.getReAdminId());
			admUser = (AdminUser) adminUserServ.find(admUser);
			if (null != admUser) {
				String newPwd = PwdEncTool.encPwd(adminUser.getPwd());
				IDigest md5 = new MD5();
				md5.update(newPwd.getBytes());
				String md5Str = AlgHelper.bytes2HexString(md5.digest()).toLowerCase();

				if (StrTool.strEquals(md5Str, oldPwd)) {
					outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_same_old_pwd"));
					return null;
				}
				admUser.setPwd(newPwd);//设置新密码
				admUser.setPwdsettime(StrTool.timeSecond());
				admUser.setEnabled(NumConstant.common_number_1);//设置启用

				//如果是激活，登录次数+1 
				if (StrTool.strEquals(source, StrConstant.common_number_0)) {
					int logincnt = adminUser.getLogincnt() + 1;
					admUser.setLogincnt(logincnt);
					admUser.setLogintime(StrTool.timeSecond());
				}

				adminUserServ.updatePass(admUser);
				outPutOperResult(Constant.alert_succ, null);
			} else {
				outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_illegal_account_pwd"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			String errorMsg = "";
			if (StrTool.strEquals(source, StrConstant.common_number_0)) {
				errorMsg = Language.getLangStr(request, "admin_activation_error");
			} else {
				errorMsg = Language.getLangStr(request, "admin_update_pwd_error");
			}
			outPutOperResult(Constant.alert_error, errorMsg);
		}

		return null;
	}

	/**
	 * 查询变更指派人
	 */
	public String queryDesignate() {
		List<?> designateList = null;
		String dnateUserStr = request.getParameter("adminids");
		String userStr[] = dnateUserStr.split(",");
		try {
			AdminUser otpuser = getAdminUser(queryForm);
			otpuser.setLoginUser(super.getCurLoginUserRole());
			otpuser.setAdminid(super.getCurLoginUser());

			//超级管理员可以变更任何角色的创建人（除了自身所属的角色不能变更）,指派人可以是任何管理员，包括自身(普通管理员进行变更操作时，指派人不能是自己)
			designateList = adminUserServ.queryAdminDesignate(otpuser); //查询管理员的变更指派人
			if (StrTool.arrNotNull(userStr)) {
				if (userStr.length == NumConstant.common_number_1) {
					if (StrTool.listNotNull(designateList)) {
						for (int i = 0; i < designateList.size(); i++) {
							AdminUser admin = (AdminUser) designateList.get(i);
							if (StrTool.strEquals(userStr[0], admin.getAdminid())) {
								designateList.remove(i);
							}
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		super.setRequestPageObj(request.getParameter("adminids"));
		super.setRequestPageList(designateList);
		request.setAttribute("curPage", request.getParameter("curPage"));

		return "change_creater_user";
	}

	/*******************************************************************************************************************
	 * 变更创建人
	 */
	public String designate() {
		String dnateUserStr = request.getParameter("userIdStr");
		String role = request.getParameter("roleArr");
		String userStr[] = dnateUserStr.split(",");
		String roleArr[] = role.split(",");
		Set<?> keys = null;
		List<Object> list = new ArrayList<Object>();
		String designUserId = adminUser.getAdminid();
		AdminUser editAdminUser = null;
		try {
			if (StrTool.strNotNull(dnateUserStr) && StrTool.strNotNull(designUserId)) {
				keys = new HashSet<Object>(Arrays.asList(userStr));
				//指派人存在于变更用户中，先变更指派人的创建人,
				if (keys.contains(designUserId)) {
					keys.remove(designUserId);
				}

				//指派人不存在于变更用户中，直接变更创建人
				designUserId = adminUser.getAdminid();
				Iterator<?> iter = keys.iterator();
				while (iter.hasNext()) {
					editAdminUser = new AdminUser();
					editAdminUser.setAdminid((String) iter.next());
					editAdminUser.setCreateuser(designUserId);
					list.add(editAdminUser);
				}

				// 执行变更角色处理
				List<String> roleList = Arrays.asList(roleArr);
				for (int j = 0; j < list.size(); j++) {
					AdminUser adm = (AdminUser) list.get(j);

					// 先删除
					AdminAndRole admRole = new AdminAndRole();
					admRole.setBatchIds(new String[] { adm.getAdminid() });
					adminAndRoleServ.delObj(admRole);
					for (int i = 0; i < roleList.size(); i++) {
						String roleid = roleList.get(i);
						AdminAndRole adminAndRole = new AdminAndRole();

						// 再添加
						adminAndRole.setAdminId(adm.getAdminid());
						adminAndRole.setRoleId(StrTool.parseInt(roleid));
						adminAndRoleServ.addObj(adminAndRole);
					}
				}

				//执行变更操作
				adminUserServ.updateDsignate(list);
				outPutOperResult(Constant.alert_succ, "true");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, "false");
		}

		return null;
	}

	/**
	 * 管理员列表，过虑掉超级管理员 如果普通管理则展示自己和子管理员 重新调整页码和总记录数
	 */
	public String showAdminsExceptPowerAdmin() {
		if (isNeedClearForm()) {
			queryForm = null;
		}
		try {
			//计算总记录数
			int totalRow = 0;
			if (StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
				totalRow = adminUserServ.count(getAdminUser(queryForm));
				totalRow = totalRow - 1; //因为不显示超级管理员所以见一
			} else {
				AdminUser queryUser = getAdminUser(queryForm);
				queryUser.setLoginUser(super.getCurLoginUser());
				List<?> list = aide.getCurrLoginUserChildUsers(queryUser, null);
				totalRow = list.size();
			}

			List<?> resultList;
			PageArgument pageArg = getArgument(totalRow);
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			//超级管理员（所有管理员）
			if (StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
				resultList = adminUserServ.queryExceptPowerAdmin(getAdminUser(queryForm), pageArg);
			} else {
				AdminUser queryUser = getAdminUser(queryForm);
				queryUser.setLoginUser(super.getCurLoginUser());
				//普通管理员（查询自己和自己创建的管理员）
				resultList = aide.getCurrLoginUserChildUsers(queryUser, null);
			}

			String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
			setResponseWrite(json);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	/**
	 * 系统初始化时添加第一个管理员 系统级的管理员
	 * @Date in Apr 4, 2013,2:07:54 PM
	 * @return
	 */
	public String addUnionAdmin() {
		if (null == adminUser) {
			setResponseWrite(Constant.alert_error);
			return null;
		}
		boolean isActivate = false;//是否需要邮件激活
		try {
			RoleInfo roleInfo = new RoleInfo();
			//查询角色标识为ADMIN的超级管理角色
			roleInfo.setRolemark(StrConstant.SUPER_ADMIN);
			roleInfo = (RoleInfo) roleInfoServ.find(roleInfo);

			adminUser.setPwd(PwdEncTool.encPwd(adminUser.getPwd()));
			adminUser.setPwdsettime(StrTool.timeSecond());
			AdminUser admUser = findData(adminUser);

			//是否需要邮件激活
			if (ConfDataFormat.getSysConfEmailEnabled()) {//需要激活
				isActivate = true;
				adminUser.setEnabled(NumConstant.common_number_0); //未启用、登录时需激活
			} else {
				adminUser.setEnabled(NumConstant.common_number_1); //设置为启用
			}

			if (null == admUser) {//添加管理员
				adminUser.setCreatetime(StrTool.timeSecond());
				adminUser.setCreateuser(adminUser.getAdminid());
				adminUserServ.addObj(adminUser);

				//添加管理与角色的绑定关系
				AdminAndRole adminAndRole = new AdminAndRole();
				adminAndRole.setAdminId(adminUser.getAdminid());
				adminAndRole.setRoleId(roleInfo.getRoleId());
				adminAndRoleServ.addObj(adminAndRole);
			} else {
				adminUserServ.updateObj(adminUser);
				//修改密码
				adminUserServ.updatePassword(adminUser);
			}

			//修改角色的创建人
			roleInfo.setCreateuser(adminUser.getAdminid());
			List<RoleInfo> roles = new ArrayList<RoleInfo>();
			roles.add(roleInfo);
			roleInfoServ.updateDsignate(roles);

			//发送管理员激活的邮件
			if (isActivate) {
				boolean result = aide.activeMailSend(adminUser, request, NumConstant.common_number_0);
				if (!result) {
					outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_add_success_email_error"));
					return null;
				}
			}

			request.getSession().getServletContext().setAttribute(Constant.DATABASE_IF_SUPERMAN, true);
			setResponseWrite(Constant.alert_succ);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setResponseWrite(Constant.alert_error);
		}

		return null;
	}

	/**
	 * 添加管理员常用功能
	 * @Date in May 14, 2013,3:26:35 PM
	 * @return
	 */
	public String addPermCode() {
		try {
			AdminPermCode adminPermCode = new AdminPermCode();
			adminPermCode.setAdminid(super.getCurLoginUser());
			adminPermCode.setPermcode(request.getParameter("permcode"));

			//先查询是否已存在此常用功能
			if (adminPermCodeServ.isOftenUsed(adminPermCode)) {
				outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_already_add_comm_fun"));
				return null;
			}

			adminPermCodeServ.addObj(adminPermCode);
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "admin_add_succ_index_use"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
		}

		return null;
	}

	/**
	 * 删除管理员常用功能
	 * @Date in May 14, 2013,3:26:35 PM
	 * @return
	 */
	public String delPermCode() {
		try {
			AdminPermCode adminPermCode = new AdminPermCode();
			adminPermCode.setAdminid(super.getCurLoginUser());
			adminPermCode.setPermcode(request.getParameter("permcode"));

			adminPermCodeServ.delObj(adminPermCode);
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
		}

		return null;
	}

	/**
	 * 当删除或禁用，锁定某管理员时，销毁该管理员的会话session，强制下线
	 * @Date in Aug 2, 2012,3:14:54 PM
	 * @return
	 */
	public void clearSession(String strIds) {
		String[] ids = strIds.split(",");
		for (int i = 0; i < ids.length; i++) {
			List<?> users = OnlineUsers.getOnlineUsers();
			if (StrTool.listNotNull(users)) {
				Iterator<?> iter = users.iterator();
				while (iter.hasNext()) {
					LinkUser lUser = (LinkUser) iter.next();
					String lUserId = lUser.getUserId();
					if (StrTool.strEquals(ids[i], lUserId)) {
						String sessId = lUser.getSessionId();
						OnlineUsers.remove(sessId);
					}
				}
			}
		}
	}

	/**
	 * 验证找回密码链接是否过期
	 * @return
	 */
	public String getPwdDeath() {
		String adminId = request.getParameter("adminId");
		String pwd = request.getParameter("passwd");
		AdminUser adminUser = new AdminUser();
		try {
			adminUser.setAdminid(adminId);
			adminUser = (AdminUser) adminUserServ.find(adminUser);

			if (StrTool.objNotNull(adminUser)) {
				IDigest md5 = new MD5();
				md5.update(adminUser.getPwd().getBytes());
				String md5Str = AlgHelper.bytes2HexString(md5.digest()).toLowerCase();
				if (!StrTool.strEquals(pwd, md5Str)) {
					outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_sys_find_pwd_Illegal"));
					return null;
				}

				// 当前时间
				Calendar calendar = Calendar.getInstance();
				int expiretime = DateTool.dateToInt(calendar.getTime());
				if (expiretime > adminUser.getGetpwddeath()) {

					// 置为0
					adminUser.setGetpwddeath(0);
					adminUserServ.updatePwddeath(adminUser); // 更新密码找回有效期
					outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_sys_find_pwd_expired"));
				} else {
					outPutOperResult(Constant.alert_succ, "success");
				}
			} else {
				outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "admin_sys_find_pwd_Illegal"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public RoleInfo getRole() {
		String adminid = request.getParameter("adminid");
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setCreateuser(adminid);
		return roleInfo;
	}

	/**
	 * 查总数
	 * @return
	 * @throws BaseException
	 */
	private PageArgument pageDesArgument() throws BaseException {
		int totalRow = roleInfoServ.countDes(getRole());
		PageArgument pageArg = getArgument(totalRow);
		return pageArg;
	}

	/**
	 * 变更创建人，初始化
	 * @return
	 * @throws Exception
	 */
	public String designInit() throws Exception {
		try {
			PageArgument pageArg = pageDesArgument();
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			List<?> resultList = queryRole(pageArg);
			String jsonStr = JsonTool.getJsonFromList(resultList.size(), resultList, null);
			setResponseWrite(jsonStr);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 变更创建人，查询指派人创建的角色
	 * @param pageArg
	 * @return
	 * @throws Exception
	 */
	private List<?> queryRole(PageArgument pageArg) throws Exception {
		List<RoleInfo> admList = new ArrayList<RoleInfo>();
		try {
			admList = (List<RoleInfo>) roleInfoServ.queryDes(getRole());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return admList;
	}

	/**
	 * 编辑管理员日志
	 * @author LXH
	 * @date Mar 7, 2014 4:14:25 PM
	 * @return
	 */
	public String findToLog() {
		AdminUser admUser = null;
		List<?> roleList = null;
		try {
			admUser = findData(adminUser);
			if (!StrTool.objNotNull(admUser)) { return init(); }
			// 原管理员对应的角色
			roleList = aide.getRoleServ(adminAndRoleServ, adminUser);
			String a[] = new String[roleList.size()];
			List<String> roleIds = new ArrayList();
			if (StrTool.listNotNull(roleList)) {
				String roles = "";
				for (int j = 0; j < roleList.size(); j++) {
					AdminAndRole rP = (AdminAndRole) roleList.get(j);
					roles += rP.getRoleName() + "，";
					roleIds.add(rP.getRoleId() + "");
				}
				admUser.setRolenameStr(roles);
				admUser.setAdminRoles(roleIds);
			}

			//管理员对应的角色
			String roles = "";
			for (int i = 0; i < adminUser.getAdminRoles().size(); i++) {
				RoleInfo roleInfo = new RoleInfo();
				roleInfo.setRoleId(StrTool.parseInt((String) adminUser.getAdminRoles().get(i)));
				roleInfo = (RoleInfo) roleInfoServ.find(roleInfo);
				roles += roleInfo.getRoleName() + "，";
			}
			adminUser.setRolenameStr(roles);

			//查找设置当前管理员所管理的域 或者组织机构
			admUser = setOrgIdName(admUser, 2);

			// 组装原组织机构ID
			String[] orgunitIdArray = admUser.getOrgunitIds().split(","); // 该数组的每一元素是 domainId:orgunitId,
			List<String> orgunitds = new ArrayList();
			if (!"".equals(orgunitIdArray[0])) {
				for (int i = 0; i < orgunitIdArray.length; i++) {
					String[] domainIdorguintidArray = orgunitIdArray[i].split(":");
					orgunitds.add(domainIdorguintidArray[1]);
				}
			}
			admUser.setOrgunitIdList(orgunitds);

			// 组装组织机构ID
			String[] orgunitIdArr = adminUser.getOrgunitIds().split(","); // 该数组的每一元素是 domainId:orgunitId,
			List<String> orgunits = new ArrayList();
			if (!"".equals(orgunitIdArr[0])) {
				for (int i = 0; i < orgunitIdArr.length; i++) {
					String[] domainIdorguintidArray = orgunitIdArr[i].split(":");
					orgunits.add(domainIdorguintidArray[1]);
				}
			}
			adminUser.setOrgunitIdList(orgunits);

			this.setOldAdminUser(admUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return SUCC_FIND;
	}

	public IProjectServ getProjectServ() {
		return projectServ;
	}

	public void setProjectServ(IProjectServ projectServ) {
		this.projectServ = projectServ;
	}
}
