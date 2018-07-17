/**
 *Administrator
 */
package com.ft.otp.manager.logs.adminlog.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.dao.namespace.AdminNSpace;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.LanguageConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.logs.adminlog.entity.AdminLogInfo;
import com.ft.otp.manager.logs.adminlog.form.AdminLogQueryForm;
import com.ft.otp.manager.logs.adminlog.service.IAdminLogServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理员日志实现类功能说明
 * @Date in May 3, 2011,1:16:12 PM
 * @author ZJY
 */
public class AdminLogAction extends BaseAction implements IBaseAction {

	private static final long	serialVersionUID	= 8377176804075186124L;

	private Logger				logger				= Logger.getLogger(AdminLogAction.class);
	//管理员日志接口服务
	private IAdminLogServ		adminLogServ;

	// 管理员服务接口
	private IAdminUserServ		adminUserServ		= (IAdminUserServ) AppContextMgr.getObject("adminUserServ");
	//管理员日志查询Form
	private AdminLogQueryForm	adminLogQueryForm;
	//管理员日志对象
	private AdminLogInfo		adminLog;

	public IAdminLogServ getAdminLogServ() {
		return adminLogServ;
	}

	public void setAdminLogServ(IAdminLogServ adminLogServ) {
		this.adminLogServ = adminLogServ;
	}

	public AdminLogQueryForm getAdminLogQueryForm() {
		return adminLogQueryForm;
	}

	public AdminLogInfo getAdminLog() {
		return adminLog;
	}

	public void setAdminLog(AdminLogInfo adminLog) {
		this.adminLog = adminLog;
	}

	public void setAdminLogQueryForm(AdminLogQueryForm adminLogQueryForm) {
		this.adminLogQueryForm = adminLogQueryForm;
	}

	public String add() {
		return null;
	}

	public String delete() {
		return null;
	}

	public String find() {
		return null;
	}

	/**
	 * 取出QueryForm中的实体
	 * @Date in May 5, 2011,6:30:40 PM
	 * @param agentQueryForm
	 * @return
	 * @throws BaseException
	 */
	public AdminLogInfo getAdminLogInfo(AdminLogQueryForm adminLogQueryForm) throws BaseException {
		AdminLogInfo adminLogInfo = new AdminLogInfo();
		if (StrTool.objNotNull(adminLogQueryForm)) {
			adminLogInfo = adminLogQueryForm.getAdminLogInfo();

			// 非超级管理员
			if (!StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole()) && "".equals(adminLogInfo.getOperator())) {
				adminLogInfo.setOperator(null);
			}
		} else {

			// 超级管理员
			if (StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
				adminLogInfo.setOperator("");
			}
		}

		String[] adminArr = getAdminUsers();
		if (!StrTool.arrNotNull(adminArr)) { return null; }
		if (StrTool.objNotNull(adminLogInfo.getOperator())) {
			adminLogInfo.setQueryMark(1);
		} else {
			adminLogInfo.setBatchIds(adminArr);
		}

		//封装关键字查询条件
		if (StrTool.objNotNull(adminLogQueryForm) && StrTool.strNotNull(adminLogQueryForm.getDescp())) {
			adminLogInfo = setQueryFilter(adminLogQueryForm.getDescp(), adminLogInfo);
		}

		return adminLogInfo;
	}

	/**
	 * 封装查询条件，将关键字查询条件从多语言中过滤转换为查询code
	 * @Date in Jul 11, 2013,8:30:36 PM
	 * @return
	 */
	public AdminLogInfo setQueryFilter(String keyStr, AdminLogInfo aLogInfo) throws BaseException {
		//获取语言标识
		String currLang = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DEFAULT_SYSTEM_LANGUAGE);
		HashMap<Object, Object> lanmap = new HashMap<Object, Object>();

		//lidArr 存放actionid数组,lobjArr存放actionobject数组
		int[] lidArr = null;
		int[] lobjArr = null;

		if (StrTool.strEquals(currLang, Constant.zh_CN)) {
			lanmap = (HashMap<Object, Object>) LanguageConfig.langMap;
		} else if (StrTool.strEquals(currLang, Constant.en_US)) {
			lanmap = (HashMap<Object, Object>) LanguageConfig.enLangMap;
		}

		//存放多语言前缀的list
		List<String> lidList = new ArrayList<String>();
		List<String> lobjList = new ArrayList<String>();

		//遍历查询多语言
		Set<Object> set = lanmap.keySet();
		for (Iterator<Object> iter = set.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			String value = (String) lanmap.get(key);
			if (value.contains(keyStr) && key.contains(AdmLogConstant.lang_action_id)) {
				lidList.add(key.substring(14));
			}
			if (value.contains(keyStr) && key.contains(AdmLogConstant.lang_action_obj)) {
				lobjList.add(key.substring(15));
			}
		}

		if (StrTool.listNotNull(lidList)) {
			lidArr = new int[lidList.size()];
			for (int i = 0; i < lidList.size(); i++) {
				lidArr[i] = Integer.parseInt(lidList.get(i));
			}
		}
		if (StrTool.listNotNull(lobjList)) {
			lobjArr = new int[lobjList.size()];
			for (int i = 0; i < lobjList.size(); i++) {
				lobjArr[i] = Integer.parseInt(lobjList.get(i));
			}
		}

		aLogInfo.setLidArr(lidArr);
		aLogInfo.setLobjArr(lobjArr);

		return aLogInfo;
	}

	private String[] getAdminUsers() {
		String[] adminArr = null;
		LinkUser linkUser = getLinkUser();
		if (null == linkUser) { return adminArr; }

		Map<String, Object> permMap = linkUser.getPermMap();
		Object allObject = permMap.get(StrConstant.LOG_ALL_LOG);
		Object myToObject = permMap.get(StrConstant.LOG_MY_TO_LOG);
		List<?> adminList = null;
		try {
			AdminUser adminUser = new AdminUser();
			String loginUser = getCurLoginUser();
			if (null != allObject) {
				//查询所有管理员
				adminList = adminUserServ.query(adminUser, new PageArgument());
			} else if (null == allObject && null != myToObject) {
				//查询个人及由个人创建的管理员
				adminUser.setLoginUser(loginUser);
				adminList = adminUserServ.getChildAdmins(adminUser, new PageArgument(), AdminNSpace.ADMIN_SELECT_SELF_CHILDREN_OU);
			}
			if (!StrTool.listNotNull(adminList)) {
				//查询个人
				adminArr = new String[1];
				adminArr[0] = loginUser;
			} else {
				adminArr = new String[adminList.size()];
				for (int i = 0; i < adminList.size(); i++) {
					adminUser = (AdminUser) adminList.get(i);
					adminArr[i] = adminUser.getAdminid();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return adminArr;
	}

	/**
	 * 行数统计 分页处理
	 * @Date in Apr 15, 2011,11:33:36 AM
	 * @return
	 */
	private PageArgument pageArgument() throws BaseException {
		int totalRow = adminLogServ.count(getAdminLogInfo(adminLogQueryForm));
		PageArgument pageArg = getArgument(totalRow);

		return pageArg;
	}

	/*******************************************************************************************************************
	 * 初始化管理员日志
	 */
	public String init() {
		if (isNeedClearForm()) {
			adminLogQueryForm = null;
		}
		try {
			PageArgument pageArgument = pageArgument();
			pageArgument.setCurPage(getPage());
			pageArgument.setPageSize(getPagesize());
			List<?> resultList = query(pageArgument);
			String jsonStr = JsonTool.getJsonFromList(pageArgument.getTotalRow(), resultList, null);
			setResponseWrite(jsonStr);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;

	}

	/**
	 * 查询管理员日志
	 */
	public List<?> query(PageArgument pageArgument) {
		List<?> logList = null;
		try {
			logList = adminLogServ.query(getAdminLogInfo(adminLogQueryForm), pageArgument);
			if (!StrTool.listNotNull(logList)) {
				logList = new ArrayList<Object>();
			}
			//格式化日志描述信息
			if (StrTool.listNotNull(logList)) {
				logList = descFormat(logList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return logList;

	}

	/**
	 * 格式化日志描述信息
	 * @Date in Mar 12, 2013,10:35:53 AM
	 * @param list
	 * @return
	 */
	private List<?> descFormat(List<?> list) {
		List<Object> logList = new ArrayList<Object>();
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			AdminLogInfo logInfo = (AdminLogInfo) iter.next();
			logList.add(setDescFormatLogObj(logInfo));
		}
		return logList;
	}

	/**
	 * 格式化日志描述信息以及操作人信息
	 * @Date in Jun 8, 2011,9:39:33 PM
	 * @param list
	 * @return
	 */
	private AdminLogInfo setDescFormatLogObj(AdminLogInfo logInfo) {
		StringBuilder sBuilder = null;
		int actionId = logInfo.getActionid();
		int actionObject = logInfo.getActionobject();
		String desc = logInfo.getDescp();
		String operator = logInfo.getOperator();

		if (StrTool.strEquals(desc, "NO_ENTRY")) {
			logInfo.setDescp("");
		}
		if (StrTool.strEquals(operator, "NO_ENTRY")) {
			logInfo.setOperator("");
		}

		//从多语言文件中获取actionId表示的意义
		String actionIdkey = AdmLogConstant.lang_action_id + actionId;
		String actionIdStr = Language.getLangStr(request, actionIdkey);
		//从多语言文件中获取actionObject表示的意义
		String actionObjkey = AdmLogConstant.lang_action_obj + actionObject;
		String actionObjStr = Language.getLangStr(request, actionObjkey);

		sBuilder = new StringBuilder();
		sBuilder.append(actionIdStr).append(actionObjStr);
		logInfo.setActionDesc(sBuilder.toString());
		return logInfo;
	}

	public String modify() {
		return null;
	}

	/**
	 * 管理员日志分页处理
	 */
	public String page() {
		PageArgument pArgument = getArgument(request, 0);
		query(pArgument);

		return SUCCESS;
	}

	/**
	 * 查看日志信息
	 */
	public String view() {
		String id = request.getParameter("id");
		AdminLogInfo logInfo = new AdminLogInfo();
		logInfo.setId(Integer.parseInt(id));
		try {
			logInfo = (AdminLogInfo) adminLogServ.find(logInfo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		request.setAttribute("adminLog", setDescFormatLogObj(logInfo));
		return "view";
	}

	/**
	 * 检测输入管理员是否有权限
	 * @return
	 * @throws BaseException
	 */
	public String checkAdminId() throws BaseException {
		String adminid = request.getParameter("adminId");
		if (StrTool.strNotNull(adminid)) {

			// 检验当前管理员是否为超级管理员
			if (!StrTool.strEquals(StrConstant.SUPER_ADMIN, super.getCurLoginUserRole())) {
				List<?> adminList = null;
				LinkUser linkUser = getLinkUser();
				Map<String, Object> permMap = linkUser.getPermMap();
				Object allObject = permMap.get(StrConstant.LOG_ALL_LOG); // 所有管理员日志
				Object myToObject = permMap.get(StrConstant.LOG_MY_TO_LOG); // 个人及个人创建的管理员日志

				AdminUser adminUser = new AdminUser();
				adminUser.setAdminid(adminid);
				AdminUser adminU = (AdminUser) adminUserServ.find(adminUser);

				String loginUser = getCurLoginUser();
				if (StrTool.objNotNull(adminU)) {
					if (allObject != null) {
						outPutOperResult(Constant.alert_succ, "true");
						return null;
					} else if (myToObject != null) {
						AdminUser admin = new AdminUser();
						admin.setLoginUser(loginUser);
						adminList = adminUserServ.getChildAdmins(admin, new PageArgument(), AdminNSpace.ADMIN_SELECT_SELF_CHILDREN_OU);

						String[] adminArr = new String[adminList.size()];
						for (int i = 0; i < adminList.size(); i++) {
							adminUser = (AdminUser) adminList.get(i);
							adminArr[i] = adminUser.getAdminid();
						}

						List<String> tempList = Arrays.asList(adminArr);
						if (!tempList.contains(adminid)) {
							outPutOperResult(Constant.alert_error, "false");
							return null;
						}
					} else {
						if (!loginUser.equals(adminid)) {
							outPutOperResult(Constant.alert_error, "false");
							return null;
						}
					}
				}
			}
		}
		outPutOperResult(Constant.alert_succ, "true");
		return null;
	}
}
