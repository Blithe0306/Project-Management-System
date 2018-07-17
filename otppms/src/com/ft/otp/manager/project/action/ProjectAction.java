package com.ft.otp.manager.project.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.PrjinfoTypeConfig;
import com.ft.otp.common.config.ProjectTypeConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.customer.entity.CustomerInfo;
import com.ft.otp.manager.customer.service.ICustomerInfoServ;
import com.ft.otp.manager.notice.customer.action.CustomerUserAction;
import com.ft.otp.manager.notice.customer.entity.CustomerUser;
import com.ft.otp.manager.notice.customer.service.ICustomerUserServ;
import com.ft.otp.manager.prjinfo.entity.Prjinfo;
import com.ft.otp.manager.prjinfo.entity.PrjinfoType;
import com.ft.otp.manager.prjinfo.service.IPrjinfoServ;
import com.ft.otp.manager.project.entity.Project;
import com.ft.otp.manager.project.entity.ProjectResult;
import com.ft.otp.manager.project.entity.ProjectType;
import com.ft.otp.manager.project.form.ProjectQueryForm;
import com.ft.otp.manager.project.service.IProjectResultServ;
import com.ft.otp.manager.project.service.IProjectServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 定制项目相关业务控制Action
 */
public class ProjectAction extends BaseAction implements IBaseAction {

	private static final long	serialVersionUID	= -1192376516078719256L;

	private Logger				logger				= Logger.getLogger(ProjectAction.class);
	private IPrjinfoServ		prjinfoServ			= (IPrjinfoServ) AppContextMgr.getObject("prjinfoServ");
	private IProjectServ		projectServ			= null;
	private ICustomerInfoServ	custInfoServ		= null;
	private ICustomerUserServ	customerUserServ	= null;
	private IProjectResultServ	projectResultServ	= null;
	private Prjinfo				prjinfo				= null;
	private ProjectResult		projectResult		= null;

	//查询Form
	private ProjectQueryForm	projectQueryForm;
	private List<ProjectType>	typeList;
	private List<PrjinfoType>	prjInfotypeList;

	private Project				project				= null;
	private Project				oldProject			= null;
	private int					prjIds				= 0;

	private static final String	GO_PRJINFO_ADD		= "goPrjInfoAdd";
	private static final String	GO_SUMMAY_ADD		= "goSummayAdd";

	/**
	 * 取出QueryForm中的实体
	 * @Date in May 5, 2011,6:30:40 PM
	 * @param ProjectQueryForm
	 * @return
	 */
	public Project getPrjInfo(ProjectQueryForm queryForm) {
		Project prj = new Project();
		if (StrTool.objNotNull(queryForm)) {
			prj.setPrjid(queryForm.getPrjid());
			prj.setPrjname(queryForm.getPrjname());
			prj.setStartTime(queryForm.getStartTime());
			prj.setEndTime(queryForm.getEndTime());
			prj.setPrjname(queryForm.getPrjname());
		}

		return prj;
	}

	/**
	 * 初始化项目定制列表
	 */
	public String init() {
		if (isNeedClearForm()) {
			//            projectQueryForm = null;
		}
		try {
			PageArgument pageArg = pageArgument();
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			List<?> resultList = query(pageArg);
			String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
			setResponseWrite(jsonStr);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 编辑项目总结
	 * @return
	 */
	public String addProjSummary() {
		try {
			project = (Project) projectServ.find(project);
			setProject(project); //定制项目

			prjinfo = new Prjinfo();
			prjinfo.setPrjid(project.getId());
			List<?> prjLists = prjinfoServ.query(prjinfo, new PageArgument());
			request.setAttribute("prjs", prjLists);

			ProjectResult projectResult = new ProjectResult();
			projectResult.setPrjid(project.getId());
			List<?> resultLists = projectResultServ.query(projectResult, new PageArgument());
			request.setAttribute("results", resultLists); //项目总结
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}
		return GO_SUMMAY_ADD;
	}

	/**
	 * 新增项目总结
	 */
	public String addProjectResult() {

		try {
			if (StrTool.objNotNull(projectResult)) {
				projectResult.setOperator(getCurLoginUser());
				projectResult.setCreatetime(DateTool.getCurDate("yyyy-MM-dd HH:mm:ss"));
				projectResultServ.addObj(projectResult);
				projectResult = (ProjectResult) projectResultServ.find(projectResult);
				outPutOperResult(Constant.alert_succ, projectResult.getId());
				//发送项目总结给监视人
				sendEmail4Result(projectResult.getPrjid(), projectResult.getResults(), null, 1, projectResult.getOperator(), projectResult
						.getCreatetime());
			}
		} catch (BaseException e) {
			e.printStackTrace();
			outPutOperResult(Constant.alert_error, "添加失败！");
			return null;
		}
		return null;
	}

	/**
	 * 发送项目总结邮件
	 * @param id
	 * @param result
	 * @param oldResult
	 * @param isAdd
	 * @param operator
	 * @param currTime
	 */
	@SuppressWarnings("unchecked")
	private void sendEmail4Result(String id, String result, String oldResult, int isAdd, String operator, String currTime) {

		if (StrTool.strNotNull(id)) {
			Project ptemp = new Project();
			ptemp.setId(id);

			List<CustomerUser> cUserList;
			try {
				ptemp = (Project) projectServ.find(ptemp);
				if (StrTool.objNotNull(ptemp)) {
					//修改成功后，发送邮件给监视人
					CustomerUser cUser = new CustomerUser();
					cUser.setProjectId(ptemp.getPrjname());

					cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
					if (StrTool.objNotNull(cUserList) && cUserList.size() > 0) {

						String[] emailArr = new String[cUserList.size()];
						for (int i = 0; i < cUserList.size(); i++) {
							cUser = (CustomerUser) cUserList.get(i);
							emailArr[i] = cUser.getUserEmail();
						}
						String content = sendEmail4ResultContent(isAdd, result, oldResult, operator, currTime); //发送邮件内容
						CustomerUserAction.emailSeed(emailArr, "OTP项目管理系统：" + ptemp.getPrjname(), content);
					}

				}
			} catch (BaseException e) {
				e.printStackTrace();
			}
		} else {
			logger.error("因定制项目ID为空，无法发送！");
		}
	}

	/**
	 * 发送项目总结
	 * @param isAdd 1:add,2:update,3:delete
	 * @param result 项目总结
	 * @param oldResult 旧版本总结
	 * @return
	 */
	public String sendEmail4ResultContent(int isAdd, String result, String oldResult, String operator, String currTime) {
		StringBuffer sb = new StringBuffer();
		if (isAdd == 1) {
			sb.append("操作人：" + operator + "<br/>");
			sb.append("操作时间：" + currTime + "<br/>");
			sb.append("项目总结：" + result);
		} else if (isAdd == 2) {
			sb.append("操作人：" + operator + "<br/>");
			sb.append("操作时间：" + currTime + "<br/>");
			sb.append("项目总结：" + oldResult + "，修改为：" + result);
		} else {
			sb.append("操作人：" + operator + "<br/>");
			sb.append("操作时间：" + currTime + "<br/>");
			sb.append("项目总结：" + result + " 已删除！");
		}
		return sb.toString();
	}

	/**
	 * 修改项目总结
	 */
	public String modifyProjectResult() {
		try {

			ProjectResult oldPrjResult = new ProjectResult();
			oldPrjResult.setId(projectResult.getId());
			oldPrjResult = (ProjectResult) projectResultServ.find(oldPrjResult);
			projectResultServ.updateObj(projectResult);
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
			String oldResults = "无";
			if (StrTool.objNotNull(oldPrjResult)) {
				oldResults = oldPrjResult.getResults();
			}
			//发送项目总结给监视人
			sendEmail4Result(projectResult.getPrjid(), projectResult.getResults(), oldResults, 2, getCurLoginUser(), DateTool
					.getCurDate("yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			e.printStackTrace();
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_error_tip"));
			return null;
		}
		return null;
	}

	/**
	 * 删除项目总结
	 */
	public String deleteProjectResult() {
		try {
			projectResult = (ProjectResult) projectResultServ.find(projectResult);
			if (StrTool.objNotNull(projectResult)) {
				projectResultServ.delObj(projectResult);
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
				sendEmail4Result(projectResult.getPrjid(), projectResult.getResults(), null, 3, getCurLoginUser(), DateTool
						.getCurDate("yyyy-MM-dd HH:mm:ss"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_error_tip"));
			return null;
		}
		return null;
	}

	public String goPrjinfoAdd() {
		try {
			String projId = request.getParameter("projId"); //定制项目ID
			project = new Project();
			project.setId(projId);
			project = (Project) projectServ.find(project);
			if (StrTool.objNotNull(project)) {
				prjinfo = new Prjinfo();
				prjinfo.setPrjid(project.getId());
				prjinfo.setPrjname(project.getPrjname());
				setPrjinfo(prjinfo);
			}
			setPrjInfotypeList(PrjinfoTypeConfig.getPrjTypeList());
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return GO_PRJINFO_ADD;
	}

	/**
	 * 处理数据查询
	 * @Date in Apr 20, 2011,12:04:36 PM
	 * @param pageArg
	 */
	private List<?> query(PageArgument pageArg) {
		List<?> projectList = null;
		try {
			projectList = projectServ.query(project, pageArg);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return projectList;
	}

	/**
	 * 行数统计 分页处理
	 * @Date in Apr 15, 2011,11:33:36 AM
	 * @return
	 */
	private PageArgument pageArgument() throws BaseException {

		int totalRow = projectServ.count(getProjectBean(projectQueryForm));
		PageArgument pageArg = getArgument(totalRow);

		return pageArg;
	}

	/**
	 * 取出ProjectQueryForm中的实体
	 * @Date in May 5, 2011,6:30:40 PM
	 * @param qForm
	 * @return
	 */
	public Project getProjectBean(ProjectQueryForm qForm) {
		project = new Project();
		if (StrTool.objNotNull(qForm)) {

			if (StrTool.strNotNull(qForm.getId())) {
				project.setId(qForm.getId());
			}
			if (StrTool.strNotNull(qForm.getPrjname())) {
				project.setPrjname(qForm.getPrjname());
			}
			if (StrTool.strNotNull(qForm.getPrjid())) {
				project.setPrjid(qForm.getPrjid());
			}
			if (qForm.getStartTime() != 0) {
				project.setStartTime(qForm.getStartTime());
			}
			if (qForm.getEndTime() != 0) {
				project.setEndTime(qForm.getEndTime());
			}
			if (StrTool.strNotNull(qForm.getCustid())) {
				project.setCustid(qForm.getCustid());
			}

			if (StrTool.strNotNull(super.getCurLoginUser())) {
				project.setLoginUser(super.getCurLoginUser());
			}
			if (StrTool.strNotNull(super.getCurLoginUserRole())) {
				project.setCurLoginUserRole(super.getCurLoginUserRole());
			}
		}
		return project;
	}

	/**
	 * add
	 */
	public String add() {
		try {
			if (StrTool.objNotNull(project)) {

				if (StrTool.strIsNotNull(project.getPrjid())) {
					Project projectBean = new Project();
					projectBean.setPrjid(project.getPrjid());
					Project prj = (Project) projectServ.find(projectBean);
					if (StrTool.objNotNull(prj)) {
						outPutOperResult(Constant.alert_error, "项目编号已存在，请更换项目编号后重试!");
						return null;
					}
				}

				if (StrTool.strIsNotNull(project.getPrjname())) {
					Project projectBean = new Project();
					projectBean.setPrjname(project.getPrjname());
					Project prj = (Project) projectServ.find(projectBean);
					if (StrTool.objNotNull(prj)) {
						outPutOperResult(Constant.alert_error, "项目名称已存在，请更换项目名称后重试!");
						return null;
					}
				}
				project.setOperator(getCurLoginUser());
				project.setCreatetime(DateTool.currentUTC());
				project.setUpdatetime(DateTool.currentUTC());
				projectServ.addObj(project);

				AdminUser adminUser = (AdminUser) projectServ.selectPowerAdmin();
				if (StrTool.objNotNull(adminUser) && StrTool.strNotNull(adminUser.getEmail())) {
					//TODO 为超级管理员发送邮件
					CustomerUser cUser = new CustomerUser();
					cUser.setUserid(adminUser.getAdminid());
					cUser.setUserEmail(adminUser.getEmail());
					cUser.setProjectId(project.getPrjname());
					customerUserServ.addObj(cUser);

					String content = sendMailContent4Aadd(project); //发送邮件内容
					CustomerUserAction.emailSeed(new String[] { adminUser.getEmail() }, "OTP项目管理系统：" + project.getPrjname(), content);
				}
				setActionResult(true);
				outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip") + ",");
				return null;

			} else {
				outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
				return null;
			}
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
		}
		return null;
	}

	/**
	 * edit
	 */
	public String edit() {
		Project prj = new Project();
		String id = request.getParameter("id");
		try {
			if (StrTool.objNotNull(id)) {
				prj.setId(id);
				prj = (Project) projectServ.find(prj);
				request.setAttribute("project", prj);
			} else {
				outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
				return null;
			}
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
		}
		//跳转到编辑页面
		return SUCC_FIND;
	}

	/**
	 * 删除操作
	 */
	public String delete() {
		Set<?> keys = super.getDelIds("delPrjIds");
		List<String> projnameList = new ArrayList<String>();
		List<String> emailList = new ArrayList<String>();
		try {
			if (StrTool.setNotNull(keys)) {

				Iterator it = keys.iterator();
				while (it.hasNext()) {
					Prjinfo prjInfo = new Prjinfo();
					prjInfo.setPrjid(it.next().toString());
					int results = prjinfoServ.count(prjInfo);
					if (results != 0) {
						outPutOperResult(Constant.alert_error, " 定制项目已存在定制信息，不能删除!");
						return null;
					}
					Project ptemp = new Project();
					ptemp.setId(prjInfo.getPrjid());
					ptemp = (Project) projectServ.find(ptemp);
					projnameList.add(ptemp.getPrjname());
				}

				projectServ.delObj(keys);
				setPrjIds(keys.size());
				setActionResult(true);
				outPutOperResult(Constant.alert_succ, "删除成功!");
				//发送定制信息已删除邮件
				for (int i = 0; i < projnameList.size(); i++) {
					String projname = projnameList.get(i);
					CustomerUser cUser = new CustomerUser();
					cUser.setProjectId(projname);
					List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
					for (int j = 0; j < cUserList.size(); j++) {
						CustomerUser cTemp = cUserList.get(j);
						emailList.add(cTemp.getUserEmail());
					}
					if (emailList.size() > 0) {
						String[] emailArr = emailList.toArray(new String[emailList.size()]);
						CustomerUserAction.emailSeed(emailArr, projname + "定制项目", "OTP项目管理系统，已删除 \"" + projname + "\" 定制项目" + "，操作人："
								+ getCurLoginUser() + "，操作时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss"));
					}
					//发送邮件后，删除监视人
					customerUserServ.delObj(cUser);
				}
			}
		} catch (BaseException e) {
			outPutOperResult(Constant.alert_error, "删除失败!");
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 新增页面跳转
	 * @Date Dec 24, 2014,11:08:01 PM
	 * @author ZWX
	 * @return
	 * @return String
	 * @throws
	 */
	public String toAddProject() {
		if (!StrTool.objNotNull(project)) {
			setProject(new Project());
		} else {
			setProject(project);
		}
		setTypeList(ProjectTypeConfig.getPrjTypeList());
		return SUCC_FIND;
	}

	/**
	 * 定制信息初始化，查询一条定制信息
	 */
	public String find() {
		Project prj = null;
		try {
			prj = (Project) projectServ.find(project);
			setProject(prj);
			setTypeList(ProjectTypeConfig.getPrjTypeList());

			if (!StrTool.objNotNull(prj)) {
				return init();
			} else { //查询客户信息
				if (StrTool.strNotNull(prj.getCustid())) {
					CustomerInfo customerInfo = new CustomerInfo();
					customerInfo.setId(Integer.parseInt(prj.getCustid()));
					customerInfo = (CustomerInfo) custInfoServ.find(customerInfo);
					project.setCustname(customerInfo.getCustname());
				}
			}

		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		// 设置静态密码，跳转到静态密码页面
		//跳转到编辑页面
		return SUCC_FIND;
	}

	/**
	 * 特殊查询，查询定制项目名称是否已经存在
	 */
	public String findPrjectIsExist() {
		//projectServ.findExceptself(project);
		try {
			//除本身外的是否存在其他
			if (StrTool.strIsNotNull(project.getId())) {
				project.setPrjname(StrTool.strToUTF8(project.getPrjname()));
				Object obj = projectServ.findExceptself(project);
				if (StrTool.objNotNull(obj)) {
					System.out.println("1.存在");
					setResponseWrite("exist");
				}
			} else {
				//查询是否存在
				project.setPrjname(StrTool.strToUTF8(project.getPrjname()));
				Object obj = projectServ.find(project);
				if (StrTool.objNotNull(obj)) {
					System.out.println("2.存在");
					setResponseWrite("exist");
				}
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 编辑定制项目
	 */
	public String modify() {

		try {
			if (StrTool.objNotNull(project)) {//不是空的

				Project prj = new Project();
				prj.setId(project.getId());
				prj.setPrjname(project.getPrjname());
				Object obj = projectServ.findExceptself(prj);
				if (StrTool.objNotNull(obj)) {
					outPutOperResult(Constant.alert_error, "项目名称已存在，请更换！");
					return null;
				}

				project.setOperator(getCurLoginUser());
				project.setUpdatetime(DateTool.currentUTC());
				if (!StrTool.strNotNull(project.getTypeversion())) {
					project.setTypeversion(request.getParameter("project.typeversion"));
				}
				Project proj = new Project();
				proj.setId(project.getId());
				oldProject = (Project) projectServ.find(proj);
				projectServ.updateObj(project);
				outPutOperResult(Constant.alert_succ, "修改成功!");
				//修改成功后，发送邮件给监视人
				CustomerUser cUser = new CustomerUser();
				cUser.setProjectId(project.getPrjname());

				if (!oldProject.getPrjname().equals(project.getPrjname())) {
					cUser.setOldprojectId(oldProject.getPrjname());
					customerUserServ.updateObj(cUser);
				}

				List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
				if (StrTool.objNotNull(cUserList) && cUserList.size() > 0) {

					String[] emailArr = new String[cUserList.size()];
					for (int i = 0; i < cUserList.size(); i++) {
						cUser = (CustomerUser) cUserList.get(i);
						emailArr[i] = cUser.getUserEmail();
					}

					String content = sendMailContent(project, oldProject); //发送邮件内容
					CustomerUserAction.emailSeed(emailArr, "OTP项目管理系统：" + project.getPrjname(), content);
				}
				return null;

			} else {
				outPutOperResult(Constant.alert_error, "修改内容不能为空!");
				return null;
			}
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, "修改失败，出现异常!");
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 发送邮件内容
	 * @param project
	 * @param oldProject
	 * @return
	 */
	public String sendMailContent(Project project, Project oldProject) {
		StringBuffer content = new StringBuffer();
		content.append("OTP项目管理工具-->定制项目：<br/>");
		content.append("操作人：" + getCurLoginUser() + "<br/>" + "时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss") + "<br/>");
		if (!project.getPrjname().equals(oldProject.getPrjname())) {
			content.append("定制项目名称：" + oldProject.getPrjname() + "，修改为：" + project.getPrjname());
		}
		if (!project.getPrjid().equals(oldProject.getPrjid())) {
			content.append("<br/>");
			content.append("定制项目编号：" + oldProject.getPrjid() + "，修改为：" + project.getPrjid());
		}
		if (!project.getCustname().equals(oldProject.getCustname())) {
			content.append("<br/>");
			content.append("定制项目客户名称：" + oldProject.getCustname() + "，修改为：" + project.getCustname());
		}
		if (!project.getTypeStr().equals(oldProject.getTypeStr())) {
			content.append("<br/>");
			content.append("定制类型：" + oldProject.getTypeStr() + "，修改为：" + project.getTypeStr());
		}
		if (!project.getTypeversion().equals(oldProject.getTypeversion())) {
			content.append("<br/>");
			content.append("基础版本：" + oldProject.getTypeversion() + "，修改为：" + project.getTypeversion());
		}
		if (!project.getIfpayStr().equals(oldProject.getIfpayStr())) {
			content.append("<br/>");
			content.append("是否收费：" + oldProject.getIfpayStr() + "，修改为：" + project.getIfpayStr());
		}
		if (!project.getPrjstate().equals(oldProject.getPrjstate())) {
			content.append("<br/>");
			content.append("项目状态：" + oldProject.getPrjstate() + "，修改为：" + project.getPrjstate());
		}
		if (!project.getNeeddept().equals(oldProject.getNeeddept())) {
			content.append("<br/>");
			content.append("需求部门：" + oldProject.getNeeddept() + "，修改为：" + project.getNeeddept());
		}
		if (!project.getSvn().equals(oldProject.getSvn())) {
			content.append("<br/>");
			content.append("SVN：" + oldProject.getSvn() + "，修改为：" + project.getSvn());
		}
		if (!project.getBug().equals(oldProject.getBug())) {
			content.append("<br/>");
			content.append("Bug号：" + oldProject.getBug() + "，修改为：" + project.getBug());
		}
		if (!project.getSales().equals(oldProject.getSales())) {
			content.append("<br/>");
			content.append("销售人员及联系方式：" + oldProject.getSales() + "，修改为：" + project.getSales());
		}
		if (!project.getTechsupport().equals(oldProject.getTechsupport())) {
			content.append("<br/>");
			content.append("技术支持及联系方式：" + oldProject.getTechsupport() + "，修改为：" + project.getTechsupport());
		}
		if (!project.getPrjdesc().equals(oldProject.getPrjdesc())) {
			content.append("<br/>");
			content.append("详细描述：" + oldProject.getPrjdesc() + "，修改为：" + project.getPrjdesc());
		}
		return content.toString();
	}

	/**
	 * 发送邮件内容
	 * @param project
	 * @param oldProject
	 * @return
	 */
	public String sendMailContent4Aadd(Project project) {
		StringBuffer content = new StringBuffer();
		content.append("OTP项目管理工具-->定制项目：<br/>");
		content.append("操作人：" + getCurLoginUser() + "<br/>" + "时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss") + "<br/>");
		if (StrTool.strNotNull(project.getPrjname())) {
			content.append("定制项目名称：" + project.getPrjname());
		}
		if (StrTool.strNotNull(project.getPrjid())) {
			content.append("<br/>");
			content.append("定制项目编号：" + project.getPrjid());
		}
		if (StrTool.strNotNull(project.getCustname())) {
			content.append("<br/>");
			content.append("定制项目客户名称：" + project.getCustname());
		}
		if (StrTool.strNotNull(project.getTypeStr())) {
			content.append("<br/>");
			content.append("定制类型：" + project.getTypeStr());
		}
		if (StrTool.strNotNull(project.getTypeversion())) {
			content.append("<br/>");
			content.append("基础版本：" + project.getTypeversion());
		}
		if (StrTool.strNotNull(project.getIfpayStr())) {
			content.append("<br/>");
			content.append("是否收费：" + project.getIfpayStr());
		}
		if (StrTool.strNotNull(project.getPrjstate())) {
			content.append("<br/>");
			content.append("项目状态：" + project.getPrjstateStr());
		}
		if (StrTool.strNotNull(project.getNeeddept())) {
			content.append("<br/>");
			content.append("需求部门：" + project.getNeeddept());
		}
		if (StrTool.strNotNull(project.getSvn())) {
			content.append("<br/>");
			content.append("SVN：" + project.getSvn());
		}
		if (StrTool.strNotNull(project.getBug())) {
			content.append("<br/>");
			content.append("Bug号：" + project.getBug());
		}
		if (StrTool.strNotNull(project.getSales())) {
			content.append("<br/>");
			content.append("销售人员及联系方式：" + project.getSales());
		}
		if (StrTool.strNotNull(project.getTechsupport())) {
			content.append("<br/>");
			content.append("技术支持及联系方式：" + project.getTechsupport());
		}
		if (StrTool.strNotNull(project.getPrjdesc())) {
			content.append("<br/>");
			content.append("详细描述：" + project.getPrjdesc());
		}
		return content.toString();
	}

	/**
	 * 获取定制类型的基础版本
	 */
	public String getBaseVer() {
		String prjtype = request.getParameter("prjtype");
		ProjectType prjType = ProjectTypeConfig.getTypeMap().get(prjtype);
		setResponseWrite(prjType.getVersion());
		return null;
	}

	/**
	 * 用户信息分页处理
	 */
	public String page() {

		return SUCCESS;
	}

	/**
	 * 显示详细信息
	 */
	public String view() {
		try {
			setProject((Project) projectServ.find(project));
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCC_VIEW;
	}

	public ProjectQueryForm getProjectQueryForm() {
		return projectQueryForm;
	}

	public void setProjectQueryForm(ProjectQueryForm projectQueryForm) {
		this.projectQueryForm = projectQueryForm;
	}

	public List<ProjectType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ProjectType> typeList) {
		this.typeList = typeList;
	}

	public ICustomerInfoServ getCustInfoServ() {
		return custInfoServ;
	}

	public void setCustInfoServ(ICustomerInfoServ custInfoServ) {
		this.custInfoServ = custInfoServ;
	}

	public IProjectServ getProjectServ() {
		return projectServ;
	}

	public void setProjectServ(IProjectServ projectServ) {
		this.projectServ = projectServ;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Project getOldProject() {
		return oldProject;
	}

	public void setOldProject(Project oldProject) {
		this.oldProject = oldProject;
	}

	public int getPrjIds() {
		return prjIds;
	}

	public void setPrjIds(int prjIds) {
		this.prjIds = prjIds;
	}

	public Prjinfo getPrjinfo() {
		return prjinfo;
	}

	public void setPrjinfo(Prjinfo prjinfo) {
		this.prjinfo = prjinfo;
	}

	public List<PrjinfoType> getPrjInfotypeList() {
		return prjInfotypeList;
	}

	public void setPrjInfotypeList(List<PrjinfoType> prjInfotypeList) {
		this.prjInfotypeList = prjInfotypeList;
	}

	public ICustomerUserServ getCustomerUserServ() {
		return customerUserServ;
	}

	public void setCustomerUserServ(ICustomerUserServ customerUserServ) {
		this.customerUserServ = customerUserServ;
	}

	public IProjectResultServ getProjectResultServ() {
		return projectResultServ;
	}

	public void setProjectResultServ(IProjectResultServ projectResultServ) {
		this.projectResultServ = projectResultServ;
	}

	public ProjectResult getProjectResult() {
		return projectResult;
	}

	public void setProjectResult(ProjectResult projectResult) {
		this.projectResult = projectResult;
	}
}
