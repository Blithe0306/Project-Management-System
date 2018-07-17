/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 25, 2014,10:24:59 AM
 */
package com.ft.otp.manager.prjinfo.action;

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
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.notice.customer.action.CustomerUserAction;
import com.ft.otp.manager.notice.customer.entity.CustomerUser;
import com.ft.otp.manager.notice.customer.service.ICustomerUserServ;
import com.ft.otp.manager.prjinfo.entity.Prjinfo;
import com.ft.otp.manager.prjinfo.entity.PrjinfoType;
import com.ft.otp.manager.prjinfo.service.IPrjinfoServ;
import com.ft.otp.manager.project.entity.Project;
import com.ft.otp.manager.project.service.IProjectServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 类、接口等说明
 * @Date Dec 25, 2014,10:24:59 AM
 * @version v1.0
 * @author ZWX
 */
public class PrjinfoAction extends BaseAction implements IBaseAction {

	private static final long	serialVersionUID	= 8733143149782249762L;

	private IPrjinfoServ		prjinfoServ;
	private IProjectServ		projectServ;
	private ICustomerUserServ	customerUserServ	= null;

	private Prjinfo				prjinfo;
	private Prjinfo				oldPrjinfo;
	private List<PrjinfoType>	typeList;
	//日志信息记录
	private String				prjinfoIds;

	private Logger				logger				= Logger.getLogger(PrjinfoAction.class);

	public IProjectServ getProjectServ() {
		return projectServ;
	}

	public void setProjectServ(IProjectServ projectServ) {
		this.projectServ = projectServ;
	}

	public IPrjinfoServ getPrjinfoServ() {
		return prjinfoServ;
	}

	public void setPrjinfoServ(IPrjinfoServ prjinfoServ) {
		this.prjinfoServ = prjinfoServ;
	}

	/**
	 * 新增页面跳转
	 * @Date Dec 24, 2014,11:08:01 PM
	 * @author ZWX
	 * @return
	 * @return String
	 * @throws
	 */
	public String toAddPrjinfo() {
		if (!StrTool.objNotNull(prjinfo)) {
			setPrjinfo(new Prjinfo());
		} else {
			setPrjinfo(prjinfo);
		}
		setTypeList(PrjinfoTypeConfig.getPrjTypeList());
		return SUCC_FIND;
	}

	public String add() {
		try {
			if (StrTool.objNotNull(prjinfo)) {

				Prjinfo prjinfoBean = new Prjinfo();
				prjinfoBean.setPrjdesc(prjinfo.getPrjdesc());
				prjinfoBean = (Prjinfo) prjinfoServ.find(prjinfoBean);
				if (StrTool.objNotNull(prjinfoBean)) {
					outPutOperResult(Constant.alert_error, "定制信息摘要已存在！");
					return null;
				}

				prjinfo.setOperator(getCurLoginUser());
				prjinfo.setCreatetime(DateTool.currentUTC());
				prjinfo.setUpdatetime(DateTool.currentUTC());
				prjinfoServ.addObj(prjinfo);

				AdminUser adminUser = (AdminUser) projectServ.selectPowerAdmin();
				if (StrTool.objNotNull(adminUser) && StrTool.strNotNull(adminUser.getEmail())) {
					//TODO 为超级管理员发送邮件
					CustomerUser cUser = new CustomerUser();
					cUser.setUserid(adminUser.getAdminid());
					cUser.setUserEmail(adminUser.getEmail());
					cUser.setProjectId(prjinfo.getPrjdesc());
					customerUserServ.addObj(cUser);

					String content = sendMailContent4Add(prjinfo); //发送邮件内容
					CustomerUserAction.emailSeed(new String[] { adminUser.getEmail() }, "OTP项目管理系统：" + prjinfo.getPrjname(), content);
				}

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
	 * 删除操作
	 */
	public String delete() {
		Set<?> keys = super.getDelIds("delPrjIds");
		List<String> prjinfoList = new ArrayList<String>();
		List<String> emailList = new ArrayList<String>();
		try {
			if (StrTool.setNotNull(keys)) {
				//TODO 删除定制信息后发送邮件
				Iterator<?> iter = keys.iterator();
				while (iter.hasNext()) {
					String id = (String) iter.next();
					Prjinfo ptemp = new Prjinfo();
					ptemp.setId(id);
					ptemp = (Prjinfo) prjinfoServ.find(ptemp);
					if (StrTool.objNotNull(ptemp)) {
						prjinfoList.add(ptemp.getPrjdesc());
					}
				}
				prjinfoServ.delObj(keys);
				setPrjinfoIds("" + keys.size());
				outPutOperResult(Constant.alert_succ, "删除成功!");

				//发送定制信息已删除邮件
				for (int i = 0; i < prjinfoList.size(); i++) {
					String prjinfoname = prjinfoList.get(i);
					CustomerUser cUser = new CustomerUser();
					cUser.setProjectId(prjinfoname);
					List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
					for (int j = 0; j < cUserList.size(); j++) {
						CustomerUser cTemp = cUserList.get(j);
						emailList.add(cTemp.getUserEmail());
					}
					if (emailList.size() > 0) {
						String[] emailArr = emailList.toArray(new String[emailList.size()]);
						CustomerUserAction.emailSeed(emailArr, prjinfoname + "定制信息", "OTP项目管理系统，已删除 \"" + prjinfoname + "\" 定制信息" + "，操作人："
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
	 * 特殊查询，查询定制信息摘要是否已经存在
	 */
	public String findPrjectIsExist() {
		//projectServ.findExceptself(project);
		try {
			Prjinfo prjdescName = new Prjinfo();
			if (StrTool.strIsNotNull(prjinfo.getPrjdesc())) {
				String[] prjdesc = StrTool.strToUTF8(prjinfo.getPrjdesc()).split(",");
				prjdescName.setPrjdesc(prjdesc[0]);
				Object obj = prjinfoServ.find(prjdescName);
				if (StrTool.objNotNull(obj)) {
					System.out.println("1.存在");
					setResponseWrite("exist");
				}
			}
		} catch (BaseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String find() {
		Prjinfo prj = null;
		try {
			prj = (Prjinfo) prjinfoServ.find(prjinfo);
			setPrjinfo(prj);
			setTypeList(PrjinfoTypeConfig.getPrjTypeList());

			if (!StrTool.objNotNull(prj)) {
				return init();
			} else { //查询定制项目信息
				if (StrTool.strNotNull(prj.getPrjid())) {
					Project project = new Project();
					project.setId(prj.getPrjid());
					project = (Project) projectServ.find(project);
					prj.setPrjname(project.getPrjname());
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

	public String init() {
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
	 * 处理数据查询
	 * @Date in Apr 20, 2011,12:04:36 PM
	 * @param pageArg
	 */
	private List<?> query(PageArgument pageArg) {
		List<?> prjinfoList = null;
		try {
			prjinfoList = prjinfoServ.query(prjinfo, pageArg);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return prjinfoList;
	}

	/**
	 * 行数统计 分页处理
	 * @Date in Apr 15, 2011,11:33:36 AM
	 * @return
	 */
	private PageArgument pageArgument() throws BaseException {

		int totalRow = prjinfoServ.count(prjinfo);
		PageArgument pageArg = getArgument(totalRow);

		return pageArg;
	}

	public String modify() {

		try {
			if (StrTool.objNotNull(prjinfo)) {//不是空的
				Prjinfo prjinfoBean = new Prjinfo();

				//查询修改前的定制项目信息
				prjinfoBean = new Prjinfo();
				prjinfoBean.setId(prjinfo.getId());
				setOldPrjinfo((Prjinfo) prjinfoServ.find(prjinfoBean));

				if (!oldPrjinfo.getPrjdesc().equals(prjinfo.getPrjdesc())) {
					prjinfoBean.setPrjdesc(prjinfo.getPrjdesc());
					prjinfoBean = (Prjinfo) prjinfoServ.find(prjinfoBean);
					if (StrTool.objNotNull(prjinfoBean)) {
						outPutOperResult(Constant.alert_error, "定制信息摘要已存在，请更换！");
						return null;
					}
				}

				prjinfo.setUpdatetime(DateTool.currentUTC());
				prjinfoServ.updateObj(prjinfo);
				setPrjinfo(prjinfo);//修改后的项目定制信息
				outPutOperResult(Constant.alert_succ, "修改成功!");

				//修改成功后，发送邮件给监视人
				CustomerUser cUser = new CustomerUser();
				cUser.setProjectId(prjinfo.getPrjdesc());

				if (!oldPrjinfo.getPrjdesc().equals(prjinfo.getPrjdesc())) {
					cUser.setOldprojectId(oldPrjinfo.getPrjdesc());
					customerUserServ.updateObj(cUser);
				}

				List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
				if (StrTool.objNotNull(cUserList) && cUserList.size() > 0) {

					String[] emailArr = new String[cUserList.size()];
					for (int i = 0; i < cUserList.size(); i++) {
						cUser = (CustomerUser) cUserList.get(i);
						emailArr[i] = cUser.getUserEmail();
					}

					String content = sendMailContent(prjinfo, oldPrjinfo); //发送邮件内容
					CustomerUserAction.emailSeed(emailArr, "OTP项目管理系统：" + prjinfo.getPrjname(), content);
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
	 * @param prjinfo
	 * @param oldPrjinfo
	 * @return
	 */
	public String sendMailContent(Prjinfo prjinfo, Prjinfo oldPrjinfo) {
		StringBuffer content = new StringBuffer();
		content.append("OTP项目管理工具-->定制项目信息：<br/>");
		content.append("操作人：" + getCurLoginUser() + "<br/>" + "时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss") + "<br/>");
		if (!prjinfo.getPrjdesc().equals(oldPrjinfo.getPrjdesc())) {
			content.append("定制信息摘要：" + oldPrjinfo.getPrjdesc() + "，修改为：" + prjinfo.getPrjdesc());
		}
		if (!prjinfo.getTypeStr().equals(oldPrjinfo.getTypeStr())) {
			content.append("<br/>");
			content.append("定制信息归类：" + oldPrjinfo.getTypeStr() + "，修改为：" + prjinfo.getTypeStr());
		}
		if (!prjinfo.getPrjname().equals(oldPrjinfo.getPrjname())) {
			content.append("<br/>");
			content.append("定制项目名称：" + oldPrjinfo.getPrjname() + "，修改为：" + prjinfo.getPrjname());
		}
		if (!prjinfo.getSvn().equals(oldPrjinfo.getSvn())) {
			content.append("<br/>");
			content.append("SVN：" + oldPrjinfo.getSvn() + "，修改为：" + prjinfo.getSvn());
		}
		if (!prjinfo.getBug().equals(oldPrjinfo.getBug())) {
			content.append("<br/>");
			content.append("Bug号：" + oldPrjinfo.getBug() + "，修改为：" + prjinfo.getBug());
		}
		if (!prjinfo.getPath().equals(oldPrjinfo.getPath())) {
			content.append("<br/>");
			content.append("信息位置：" + oldPrjinfo.getPath() + "，修改为：" + prjinfo.getPath());
		}
		if (!prjinfo.getDeveloper().equals(oldPrjinfo.getDeveloper())) {
			content.append("<br/>");
			content.append("开发人员：" + oldPrjinfo.getDeveloper() + "，修改为：" + prjinfo.getDeveloper());
		}
		if (!prjinfo.getTester().equals(oldPrjinfo.getTester())) {
			content.append("<br/>");
			content.append("测试人员：" + oldPrjinfo.getTester() + "，修改为：" + prjinfo.getTester());
		}

		if (!prjinfo.getResults().equals(oldPrjinfo.getResults())) {
			content.append("<br/>");
			content.append("测试结果：" + oldPrjinfo.getResults() + "，修改为：" + prjinfo.getResults());
		}

		if (!prjinfo.getContent().equals(oldPrjinfo.getContent())) {
			content.append("<br/>");
			content.append("信息内容：" + oldPrjinfo.getContent() + "，修改为：" + prjinfo.getContent());
		}
		return content.toString();
	}

	/**
	 * 发送邮件内容
	 * @param prjinfo
	 * @param oldPrjinfo
	 * @return
	 */
	public String sendMailContent4Add(Prjinfo prjinfo) {
		StringBuffer content = new StringBuffer();
		content.append("OTP项目管理工具-->定制项目信息：<br/>");
		content.append("操作人：" + getCurLoginUser() + "<br/>" + "时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss") + "<br/>");
		if (StrTool.strNotNull(prjinfo.getPrjdesc())) {
			content.append("定制信息摘要：" + prjinfo.getPrjdesc());
		}
		if (StrTool.strNotNull(prjinfo.getTypeStr())) {
			content.append("<br/>");
			content.append("定制信息归类：" + prjinfo.getTypeStr());
		}
		if (StrTool.strNotNull(prjinfo.getPrjname())) {
			content.append("<br/>");
			content.append("定制项目名称：" + prjinfo.getPrjname());
		}
		if (StrTool.strNotNull(prjinfo.getSvn())) {
			content.append("<br/>");
			content.append("SVN：" + prjinfo.getSvn());
		}
		if (StrTool.strNotNull(prjinfo.getBug())) {
			content.append("<br/>");
			content.append("Bug号：" + prjinfo.getBug());
		}
		if (StrTool.strNotNull(prjinfo.getPath())) {
			content.append("<br/>");
			content.append("信息位置：" + prjinfo.getPath());
		}
		if (StrTool.strNotNull(prjinfo.getDeveloper())) {
			content.append("<br/>");
			content.append("开发人员：" + prjinfo.getDeveloper());
		}
		if (StrTool.strNotNull(prjinfo.getTester())) {
			content.append("<br/>");
			content.append("测试人员：" + prjinfo.getTester());
		}

		if (StrTool.strNotNull(prjinfo.getResults())) {
			content.append("<br/>");
			content.append("测试结果：" + prjinfo.getResults());
		}

		if (StrTool.strNotNull(prjinfo.getContent())) {
			content.append("<br/>");
			content.append("信息内容：" + prjinfo.getContent());
		}
		return content.toString();
	}

	public String page() {
		return null;
	}

	public String view() {
		try {
			setPrjinfo((Prjinfo) prjinfoServ.find(prjinfo));
		} catch (BaseException ex) {
			ex.printStackTrace();
		}
		return SUCC_VIEW;
	}

	public Prjinfo getPrjinfo() {
		return prjinfo;
	}

	public void setPrjinfo(Prjinfo prjinfo) {
		this.prjinfo = prjinfo;
	}

	public Prjinfo getOldPrjinfo() {
		return oldPrjinfo;
	}

	public void setOldPrjinfo(Prjinfo oldPrjinfo) {
		this.oldPrjinfo = oldPrjinfo;
	}

	public List<PrjinfoType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<PrjinfoType> typeList) {
		this.typeList = typeList;
	}

	public String getPrjinfoIds() {
		return prjinfoIds;
	}

	public void setPrjinfoIds(String prjinfoIds) {
		this.prjinfoIds = prjinfoIds;
	}

	public ICustomerUserServ getCustomerUserServ() {
		return customerUserServ;
	}

	public void setCustomerUserServ(ICustomerUserServ customerUserServ) {
		this.customerUserServ = customerUserServ;
	}

}
