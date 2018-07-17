/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action;

import java.util.List;
import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.user.userinfo.action.aide.UserExportActionAide;
import com.ft.otp.manager.user.userinfo.action.aide.UserImportActionAide;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 导出用户业务控制Action
 * 
 * @Date in May 14, 2011,10:49:20 AM
 * 
 * @author TBM
 */
public class UserExportAction extends BaseAction {

	private static final long serialVersionUID = 6117335514423797800L;

	private Logger logger = Logger.getLogger(UserExportAction.class);

	private IUserInfoServ userExportServ = null;

	private UserExportActionAide aide = null;
	private UserImportActionAide importActionAide = null;
	//记录日志实体
    private UserInfo oldUserInfo;
    
	public UserInfo getOldUserInfo() {
		return oldUserInfo;
	}

	public void setOldUserInfo(UserInfo oldUserInfo) {
		this.oldUserInfo = oldUserInfo;
	}

	public UserExportAction() {
		if (null == aide) {
			aide = new UserExportActionAide();
		}
		if (null == importActionAide) {
			importActionAide = new UserImportActionAide();
		}
	}

	/**
	 * @return the userExportServ
	 */
	public IUserInfoServ getUserExportServ() {
		return userExportServ;
	}

	/**
	 * @param userExportServ
	 *            the userExportServ to set
	 */
	public void setUserExportServ(IUserInfoServ userExportServ) {
		this.userExportServ = userExportServ;
	}

	/**
	 * 导出用户
	 * 
	 * @Date in May 14, 2011,10:51:01 AM
	 * @return
	 */
	public String exportUser() {
		String raType = request.getParameter("raType"); // 导出的文件类型 excel pdf
		String usrAttr = request.getParameter("usrAttr"); // 用户字段选择 在写文件时过滤
		// 组织机构,domainId:orguntid;
		String udOrgunitIdsStr = request.getParameter("dOrgunitIds");
		String orgFlag = request.getParameter("orgFlag");
		// 用户选择：0 、所有用户 1 、未绑定  2、已绑定
		String userSel = request.getParameter("userSel"); 
		List<?> utgList = null;
		try {
			// 根据条件获取相应的用户数据
			String curLoginUserId = (String) super.getCurLoginUser();// 获得当前管理员id号
			// 当前管理员所拥有的角色,对应角色表中的rolemark字段
			String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); 
			utgList = aide.getUsrTknGpInfo(userExportServ, userSel,
					udOrgunitIdsStr, curLoginUserId, curLoginUserRoleMark, usrAttr, orgFlag);
			
			//Start 日志专用
			UserInfo userInfo = new UserInfo();
			userInfo.setUserIds(utgList);
			this.setOldUserInfo(userInfo);
			//End 日志专用
			
			if (!StrTool.listNotNull(utgList)) {
				outPutOperResult(Constant.alert_error,
						StrConstant.common_number_1);
				return null;
			}
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, StrConstant.common_number_1);
			return null;
		}

		String filePath = "";
		// PDF格式
		if (StrTool.strEquals(raType, StrConstant.common_number_2)) {

		}
		// Excel格式
		else {
			String fileName = Language.getLangStr(request, "user_vd_data") + Constant.FILE_XLS;
			filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);
			try {
				aide.createExcel(filePath, utgList, usrAttr, importActionAide, request);
				outPutOperResult(Constant.alert_succ, fileName);
			} catch (Exception e) {
			    logger.error(e.getMessage(), e);
				outPutOperResult(Constant.alert_error,
						StrConstant.common_number_1);
				super.setActionResult(false);
			}
		}
		return null;
	}

	/**
	 * 下载用户模板文件
	 * 
	 * @Date in May 11, 2011,4:26:18 PM
	 * @return
	 * @throws Exception
	 */
	public String downLoad() throws Exception {
		String fileName = request.getParameter("fileName");
		if (StrTool.strNotNull(fileName)) {
			fileName = MessyCodeCheck.iso885901ForUTF8(fileName);
		}
		String filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);
		try {
			importActionAide.downLoadFile(fileName, filePath, response);
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
		}
		return null;
	}

}
