/**
 *Administrator
 */
package com.ft.otp.manager.notice.customer.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.notice.customer.entity.CustomerUser;
import com.ft.otp.manager.notice.customer.service.ICustomerUserServ;
import com.ft.otp.manager.project.service.IProjectServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 派单监视通知业务处理Action
 *
 * @Date in May 24, 2012,2:22:08 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class CustomerUserAction extends BaseAction implements IBaseAction {

	private static final long serialVersionUID = -7297302215061137401L;
	private Log log = LogFactory.getLog(CustomerUserAction.class);

	private ICustomerUserServ customerUserServ = null;
	private IProjectServ projectServ = null;

	private CustomerUser customerUser;

	/**
	 * 添加用户、派单对应关系
	 */
	public String add() {
		super.setActionResult(false);
		String userIds = customerUser.getUserid();
		String emails = customerUser.getUserEmail();
		String projectId = customerUser.getProjectId();
		String ptype = request.getParameter("ptype");	//0：定制项目，1：定制信息

		if (!StrTool.strNotNull(userIds)) {
			outPutOperResult(Constant.alert_error, "添加监视人失败！");
			return null;
		}
		try {
			Object[] obj = processUser(userIds,emails, projectId);
			List<String> userList = (List<String>)obj[0];
			List<String> emailList = (List<String>)obj[1];
			String[] toAddressEmail = (String[])obj[2];

			for(int i = 0;i<userList.size();i++){
				String userId = userList.get(i);
				String userEmail = emailList.get(i);
				CustomerUser cUser = new CustomerUser();
				cUser.setUserid(userId);
				cUser.setUserEmail(userEmail);
				cUser.setProjectId(projectId);
				customerUserServ.addObj(cUser);
			}
			boolean seeded = emailSeed(toAddressEmail, projectId, getCurLoginUser()+"添加你为"+"“"+projectId+"”"+swtichTitle(ptype)+"的监视人");
			if(seeded){
				outPutOperResult(Constant.alert_succ, null);
			}else{
				outPutOperResult(Constant.alert_succ, "添加监视人成功，邮件发送失败！");
			}
			super.setActionResult(true);
			
		} catch (Exception ex) {
			outPutOperResult(Constant.alert_error, "添加监视人失败！");
		}
		return null;
	}
	/**
	 * 发送邮件
	 * @param toAddressEmail
	 * @param emailTitle
	 * @param content
	 * @return
	 */
	public static boolean emailSeed(String[] toAddressEmail, String emailTitle, String content){
		MailInfo mailInfo = SendMailUtil.getMailInfo(toAddressEmail,emailTitle , content, null);
		if (SendMailUtil.emailSeed(mailInfo)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String swtichTitle(String ptype){
		String title = "";
		switch (Integer.parseInt(ptype)) {
		case 0:
			title = "定制项目";
			break;
		case 1:
			title = "定制信息";
			break;
		}
		return title;
	}
	
	/**
	 * 找出已存在的用户并删除
	 * @param userIds
	 * @param projectId
	 */
	public Object[] processUser(String userIds ,String emails,String projectId){
		PageArgument pageArg = new PageArgument();
		List<CustomerUser> list = null;
		String[] useridArr = userIds.split(",");
		List<String> userList = new ArrayList(useridArr.length);
		for(int i=0;i<useridArr.length;i++){
			userList.add(useridArr[i]);
		}

		String[] emailArr = emails.split(",");
		List<String> emailList = new ArrayList(emailArr.length);
		for(int i=0;i<emailArr.length;i++){
			emailList.add(emailArr[i]);
		}

		CustomerUser cUser = new CustomerUser();
		cUser.setProjectId(projectId);
		try {
			list = (List<CustomerUser>)customerUserServ.query(cUser, pageArg);
			if(list.size()>0){
				for(int i =list.size()-1;i>=0;i--){
					CustomerUser temp = list.get(i);
					for(int j=userList.size()-1;j>=0;j--){
						String userid = userList.get(j);
						if(userid.equals(temp.getUserid())){
							userList.remove(j);
						}
					}
				}
			}
			
		} catch (BaseException e) {
			e.printStackTrace();
		}
		Object[] obj = new Object[3];
		obj[0] = userList;
		obj[1] = emailList;
		obj[2] =  (String[])emailList.toArray(new String[emailList.size()]);
		
		return obj;
	}

	/**
	 * 删除派单监视用户
	 */
	public String delete() {
		super.setActionResult(false);
		String userIds = customerUser.getUserid();
		String projectId = customerUser.getProjectId();
		if (!StrTool.strNotNull(userIds) && !StrTool.strNotNull(projectId)) {
			outPutOperResult(Constant.alert_error, "删除监视人失败！");
			return null;
		}

		try {
			customerUserServ.delObj(customerUser);
			outPutOperResult(Constant.alert_succ, null);
			super.setActionResult(true);

			//TODO 组织邮件发送内容

		} catch (Exception ex) {
			outPutOperResult(Constant.alert_error, "删除监视人失败！");
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.ft.otp.base.action.IBaseAction#find()
	 */
	public String find() {
		return null;
	}

	/**
	 * 查询定单监视的用户
	 */
	public String init() {
		PageArgument pageArg = new PageArgument();
		List<?> resultList = null;
		try {
			resultList = query(pageArg);
			if(StrTool.objNotNull(resultList) && resultList.size()>0){
				AdminUser adminUser = (AdminUser)projectServ.selectPowerAdmin();
				for (int i = 0; i < resultList.size(); i++) {
					CustomerUser temp = (CustomerUser)resultList.get(i);
					if(adminUser.getAdminid().equals(temp.getUserid())){
						resultList.remove(i);
						break;
					}
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
		setResponseWrite(json);

		return null;
	}

	private List<?> query(PageArgument pageArg) throws Exception {
		List<?> list = customerUserServ.query(customerUser, pageArg);
//		for (int j = 0; j < list.size(); j++) {
//		CustomerUser cusUser = (CustomerUser) list.get(j);
//		if (StrTool.strEquals(customerUser.getCreateuser(), cusUser.getUserIdStr())) {
//		list.remove(j);
//		break;
//		}
//		}

		return list;
	}

	/* (non-Javadoc)
	 * @see com.ft.otp.base.action.IBaseAction#modify()
	 */
	public String modify() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ft.otp.base.action.IBaseAction#page()
	 */
	public String page() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ft.otp.base.action.IBaseAction#view()
	 */
	public String view() {
		return null;
	}

	/**
	 * @return the customerUserServ
	 */
	public ICustomerUserServ getCustomerUserServ() {
		return customerUserServ;
	}

	/**
	 * @param customerUserServ the customerUserServ to set
	 */
	public void setCustomerUserServ(ICustomerUserServ customerUserServ) {
		this.customerUserServ = customerUserServ;
	}

	/**
	 * @return the customerUser
	 */
	public CustomerUser getCustomerUser() {
		return customerUser;
	}

	/**
	 * @param customerUser the customerUser to set
	 */
	public void setCustomerUser(CustomerUser customerUser) {
		this.customerUser = customerUser;
	}
	public IProjectServ getProjectServ() {
		return projectServ;
	}
	public void setProjectServ(IProjectServ projectServ) {
		this.projectServ = projectServ;
	}

}
