package com.ft.otp.manager.resords.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.notice.customer.action.CustomerUserAction;
import com.ft.otp.manager.notice.customer.entity.CustomerUser;
import com.ft.otp.manager.notice.customer.service.ICustomerUserServ;
import com.ft.otp.manager.project.service.IProjectServ;
import com.ft.otp.manager.resords.entity.Resords;
import com.ft.otp.manager.resords.form.ProjectQueryForm;
import com.ft.otp.manager.resords.service.IResordsServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

//import com.ft.otp.manager.project.service.IProjectServ;
/**
 * 类、接口等说明
 * @Date in 2015-5-4,下午5:18:42
 * @version v1.0
 * @author qiuju
 */
public class ResordsAction extends BaseAction implements IBaseAction {

	private static final long	serialVersionUID	= -3604840885597783616L;
	private Logger				logger				= Logger.getLogger(ResordsAction.class);

	private IResordsServ		resordsServ			= null;
	private IProjectServ		projectServ			= (IProjectServ) AppContextMgr.getObject("projectServ");
	private ICustomerUserServ	customerUserServ	= (ICustomerUserServ) AppContextMgr.getObject("customerUserServ");
	//查询Form
	private ProjectQueryForm	projectQueryForm;
	private Resords				resords				= null;
	private Resords				oldResords			= null;
	private int					prjIds				= 0;
	//日志信息记录
	private String				resordsIds;

	public String getResordsIds() {
		return resordsIds;
	}

	public void setResordsIds(String resordsIds) {
		this.resordsIds = resordsIds;
	}

	public ProjectQueryForm getProjectQueryForm() {
		return projectQueryForm;
	}

	public void setProjectQueryForm(ProjectQueryForm projectQueryForm) {
		this.projectQueryForm = projectQueryForm;
	}

	//跳转常量值
	public static final String	SUCC_FIND	= "find";

	public IResordsServ getResordsServ() {
		return resordsServ;
	}

	public void setResordsServ(IResordsServ resordsServ) {
		this.resordsServ = resordsServ;
	}

	public Resords getResords() {
		return resords;
	}

	public void setResords(Resords resords) {
		this.resords = resords;
	}

	public int getPrjIds() {
		return prjIds;
	}

	public void setPrjIds(int prjIds) {
		this.prjIds = prjIds;
	}

	private Resords getResords(ProjectQueryForm projectQueryForm) {
		Resords resords = new Resords();
		if (StrTool.objNotNull(projectQueryForm)) {
			resords = projectQueryForm.getResords();
		}
		return resords;
	}

	public String init() {
		if (isNeedClearForm()) {
			//    setProjectQueryForm(null);
		}
		try {
			PageArgument pageArg = pageArgument();
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			List<?> resultList = query(pageArg);
			String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
			setResponseWrite(jsonStr);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public String toAddResords() {
		if (!StrTool.objNotNull(resords)) {
			setResords(new Resords());
		} else {
			setResords(resords);
		}
		return SUCC_FIND;
	}

	public String add() {
		try {
			resords.setOperator(getCurLoginUser());
			resords.setCreatetime(DateTool.currentUTC());

			Date expTime = DateTool.stringToDate(resords.getRecordtimeStr());
			resords.setRecordtime(DateTool.dateToInt(expTime));

			Date endExpTime = DateTool.stringToDate(resords.getEndrecordtimeStr());
			resords.setEndrecordtime(DateTool.dateToInt(endExpTime));

			resordsServ.addObj(resords);
			setActionResult(true);
			outPutOperResult(Constant.alert_succ, "添加成功!");
			//发送邮件给监视人
			CustomerUser cUser = new CustomerUser();
			cUser.setProjectId(resords.getPrjid());
			List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
			if (StrTool.objNotNull(cUserList) && cUserList.size() > 0) {

				String[] emailArr = new String[cUserList.size()];
				for (int i = 0; i < cUserList.size(); i++) {
					cUser = (CustomerUser) cUserList.get(i);
					emailArr[i] = cUser.getUserEmail();
				}
				String content = addsendMailContent(resords);
				CustomerUserAction.emailSeed(emailArr, "OTP项目管理系统：" + resords.getPrjid(), content);
			}
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, "添加失败，出现异常信息!");
		}
		setActionResult(true);
		return null;
	}

	public String delete() {

		Set<?> keys = super.getDelIds("delPrjIds");
		List<String> projnameList = new ArrayList<String>();
		List<String> emailList = new ArrayList<String>();
		try {

			if (StrTool.setNotNull(keys)) {

				Iterator it = keys.iterator();
				while (it.hasNext()) {
					Resords resords = new Resords();
					resords.setId(Integer.parseInt(it.next().toString())); //获取要删除的ID号
					resords = (Resords) resordsServ.find(resords); //根据id查询对象
					projnameList.add(resords.getPrjid());
				}

				resordsServ.delObj(keys);
				setActionResult(true);
				outPutOperResult(Constant.alert_succ, "删除成功!");
				setResordsIds("" + keys.size()); //日志中需要记录删除个数
				for (int i = 0; i < projnameList.size(); i++) {
					String prjd = projnameList.get(i);
					CustomerUser cUser = new CustomerUser();
					cUser.setProjectId(prjd);
					List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
					for (int j = 0; j < cUserList.size(); j++) {
						CustomerUser cTemp = cUserList.get(j);
						emailList.add(cTemp.getUserEmail());
					}
					if (emailList.size() > 0) {
						String[] emailArr = emailList.toArray(new String[emailList.size()]);
						CustomerUserAction.emailSeed(emailArr, prjd + "上门记录", "OTP项目管理系统，已删除 \"" + prjd + "\" 上门记录" + "<br/>操作人：" + getCurLoginUser()
								+ "<br/>操作时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss"));
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

	public String modify() {

		try {
			if (StrTool.objNotNull(resords)) {//不是空的

				Date expTime = DateTool.stringToDate(resords.getRecordtimeStr());
				resords.setRecordtime(DateTool.dateToInt(expTime));

				expTime = DateTool.stringToDate(resords.getEndrecordtimeStr());
				resords.setEndrecordtime(DateTool.dateToInt(expTime));

				Resords resordsTemp = new Resords();
				resordsTemp.setId(resords.getId());

				oldResords = (Resords) resordsServ.find(resordsTemp);

				resordsServ.updateObj(resords);
				setResords(resords);//修改后的项目定制信息
				outPutOperResult(Constant.alert_succ, "修改成功!");
				//修改成功后，发送邮件给监视人
				CustomerUser cUser = new CustomerUser();
				cUser.setProjectId(resords.getPrjid());
				List<CustomerUser> cUserList = (List<CustomerUser>) customerUserServ.query(cUser, new PageArgument());
				if (StrTool.objNotNull(cUserList) && cUserList.size() > 0) {

					String[] emailArr = new String[cUserList.size()];
					for (int i = 0; i < cUserList.size(); i++) {
						cUser = (CustomerUser) cUserList.get(i);
						emailArr[i] = cUser.getUserEmail();
					}

					String content = sendMailContent(resords, oldResords); //发送邮件内容
					CustomerUserAction.emailSeed(emailArr, "OTP项目管理系统：" + resords.getPrjid(), content);
				}
			}
		} catch (Exception e) {
			outPutOperResult(Constant.alert_error, "修改失败，出现异常!");
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 方法说明
	 * @Date in 2015-5-13,上午11:31:00
	 * @param resords2
	 * @param oldResords2
	 * @return
	 */
	private String sendMailContent(Resords resords, Resords oldResords) {
		StringBuffer content = new StringBuffer();
		content.append("OTP项目管理工具-->上门记录：<br/>");
		content.append("操作人：" + getCurLoginUser() + "<br/>" + "时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss") + "<br/>");
		if (!resords.getPrjid().equals(oldResords.getPrjid())) {
			content.append("<br/>定制项目名称：" + oldResords.getPrjid() + "，修改为：" + resords.getPrjid());
		} else {
			content.append("<br/>定制项目名称：" + oldResords.getPrjid());
		}
		if (resords.getRecordtime() != (oldResords.getRecordtime())) {

			content.append("<br/>上门开始时间：" + oldResords.getRecordtimeshowStr() + "，修改为：" + resords.getRecordtimeStr());
		}
		if (!resords.getEndrecordtimeStr().equals(oldResords.getEndrecordtimeStr())) {
			content.append("<br/>上门结束时间：" + oldResords.getEndrecordtimeStr() + "，修改为：" + resords.getEndrecordtimeStr());
		}
		if (!resords.getRecordUser().equals(oldResords.getRecordUser())) {
			content.append("<br/>上门人员：" + oldResords.getRecordUser() + "，修改为：" + resords.getRecordUser());
		}
		if (!resords.getReason().equals(oldResords.getReason())) {
			content.append("<br/>上门原因：" + oldResords.getReason() + "，修改为：" + resords.getReason());
		}
		if (!resords.getResults().equals(oldResords.getResults())) {
			content.append("<br/>上门成果：" + oldResords.getResults() + "，修改为：" + resords.getResults());
		}
		if (!resords.getRemark().equals(oldResords.getRemark())) {
			content.append("<br/>备注：" + oldResords.getRemark() + "，修改为：" + resords.getRemark());
		}
		return content.toString();
	}

	private String addsendMailContent(Resords resords) {
		StringBuffer content = new StringBuffer();
		content.append("OTP项目管理工具-->添加上门记录：<br/>");
		content.append("操作人：" + getCurLoginUser() + "<br/>" + "时间：" + DateTool.getCurDate("yyyy-MM-dd HH:mm:ss") + "<br/>");
		if (resords.getPrjid() != null) {
			content.append("<br/>定制项目名称：" + resords.getPrjid());
		}
		if (resords.getRecordtime() != 0) {

			content.append("<br/>上门开始时间：" + resords.getRecordtimeStr());
		}
		if (resords.getEndrecordtime() != 0) {

			content.append("<br/>上门结束时间：" + resords.getEndrecordtimeStr());
		}
		if (resords.getRecordUser() != null) {
			content.append("<br/>上门人员：" + resords.getRecordUser());
		}
		if (resords.getReason() != null) {
			content.append("<br/>上门原因：" + resords.getReason());
		}
		if (resords.getResults() != null) {
			content.append("<br/>上门成果：" + resords.getResults());
		}
		if (resords.getRemark() != null) {
			content.append("<br/>备注：" + resords.getRemark());
		}
		return content.toString();
	}

	public String page() {
		return null;
	}

	public String find() {
		try {
			resords = (Resords) resordsServ.find(resords);
			resords.setRecordtimeStr(DateTool.dateToStr(resords.getRecordtime(), true));
			setResords(resords);

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
	 * 显示详细信息
	 */
	public String view() {
		try {
			setResords((Resords) resordsServ.find(resords));
		} catch (BaseException e) {
			e.printStackTrace();
		}
		return SUCC_VIEW;
	}

	/**
	 * 处理数据查询
	 * @param pageArg
	 */
	private List<?> query(PageArgument pageArg) {
		Resords resords = getResords(projectQueryForm);
		List<?> resordsList = null;
		try {
			resordsList = resordsServ.query(resords, pageArg);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return resordsList;
	}

	/**
	 * 行数统计 分页处理
	 * @return
	 */
	private PageArgument pageArgument() throws BaseException {
		Resords resords = getResords(projectQueryForm);
		int totalRow = 0;
		totalRow = resordsServ.count(resords);
		PageArgument pageArg = getArgument(totalRow);
		return pageArg;
	}

	public Resords getOldResords() {
		return oldResords;
	}

	public void setOldResords(Resords oldResords) {
		this.oldResords = oldResords;
	}

}
