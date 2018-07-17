/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.action;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.LangResultCode;
import com.ft.otp.common.soap.MessageBean;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.AuthHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.confinfo.config.entity.TokenConfInfo;
import com.ft.otp.manager.confinfo.email.entity.EmailInfo;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;
import com.ft.otp.manager.confinfo.sms.entity.SmsInfo;
import com.ft.otp.manager.confinfo.sms.service.ISmsInfoServ;
import com.ft.otp.manager.token.distmanager.action.aide.DistManagerActionAide;
import com.ft.otp.manager.token.distmanager.action.aide.TwoDimensionCodeAide;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
import com.ft.otp.manager.token.distmanager.form.DistManagerQueryForm;
import com.ft.otp.manager.token.distmanager.service.IDistManagerServ;
import com.ft.otp.manager.token.entity.TokenInfo;
import com.ft.otp.manager.token.service.ITokenServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.manager.user_token.entity.UserToken;
import com.ft.otp.util.alg.dist.RandomPassUtil;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.CreateFileUtil;
import com.ft.otp.util.tool.DateTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 令牌分发Action控制类
 * 
 * @Date in Apr 28, 2011,3:05:25 PM
 * 
 * @author TBM
 */
public class DistManagerAction extends BaseAction implements IBaseAction {

	private static final long serialVersionUID = 5783679697486689359L;
	private Logger logger = Logger.getLogger(DistManagerAction.class);

	// 手机令牌分发服务接口
	private IDistManagerServ distManagerServ;

	// 用户服务接口
	private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr
			.getObject("userInfoServ");
	// 管理员接口
	private IAdminUserServ adminUserServ = (IAdminUserServ) AppContextMgr
			.getObject("adminUserServ");

	// 令牌服务接口
	private ITokenServ tokenServ = (ITokenServ) AppContextMgr
			.getObject("tokenServ");
	//邮箱服务器接口
	private IEmailInfoServ emailServ = (IEmailInfoServ) AppContextMgr
			.getObject("emailServ");
	//短信服务器接口
	private ISmsInfoServ smsInfoServ= (ISmsInfoServ) AppContextMgr.getObject("smsInfoServ");
	
	private DistManagerActionAide aide = null;
	private AuthHelper authHelper = new AuthHelper();

	// 二维码帮助类
	private TwoDimensionCodeAide twoDscAide = new TwoDimensionCodeAide();

	// 手机令牌分发form
	private DistManagerQueryForm distManagerQueryForm;
	private DistManagerInfo distManagerInfo;

	// 手机令牌分发配置
	private TokenConfInfo distConfInfo;

	private String distDesc;

	public DistManagerAction() {
		if (null == aide) {
			aide = new DistManagerActionAide();
		}

		distConfInfo = getDistConfInfo();
	}

	/**
	 * @return the distManagerServ
	 */
	public IDistManagerServ getDistManagerServ() {
		return distManagerServ;
	}

	/**
	 * @param distManagerServ
	 *            the distManagerServ to set
	 */
	public void setDistManagerServ(IDistManagerServ distManagerServ) {
		this.distManagerServ = distManagerServ;
	}

	/**
	 * @return the distManagerQueryForm
	 */
	public DistManagerQueryForm getDistManagerQueryForm() {
		return distManagerQueryForm;
	}

	/**
	 * @param distManagerQueryForm
	 *            the distManagerQueryForm to set
	 */
	public void setDistManagerQueryForm(
			DistManagerQueryForm distManagerQueryForm) {
		this.distManagerQueryForm = distManagerQueryForm;
	}

	/**
	 * @return the distManagerInfo
	 */
	public DistManagerInfo getDistManagerInfo() {
		return distManagerInfo;
	}

	/**
	 * @param distManagerInfo
	 *            the distManagerInfo to set
	 */
	public void setDistManagerInfo(DistManagerInfo distManagerInfo) {
		this.distManagerInfo = distManagerInfo;
	}

	/**
	 * @return the distDesc
	 */
	public String getDistDesc() {
		return distDesc;
	}

	/**
	 * @param distDesc
	 *            the distDesc to set
	 */
	public void setDistDesc(String distDesc) {
		this.distDesc = distDesc;
	}

	/**
	 * 取出QueryForm中的实体
	 * 
	 * @Date in May 5, 2011,6:30:40 PM
	 * @param distManagerQueryForm
	 * @return
	 */
	public DistManagerInfo getDistManagerInfo(
			DistManagerQueryForm distManagerQueryForm) {
		DistManagerInfo managerInfo = new DistManagerInfo();
		if (StrTool.objNotNull(distManagerQueryForm)) {
			managerInfo = distManagerQueryForm.getDistManagerInfo();
		}
		String curLoginUserId = (String) super.getCurLoginUser();// 获得当前管理员id号
		String curLoginUserRoleMark = (String) super.getCurLoginUserRole(); // 当前管理员所拥有的角色
		// 对应角色表中的rolemark字段
		if (StrTool.strEquals(curLoginUserRoleMark, "ADMIN")) {// 如果是超级管理员
			managerInfo.setIsFliterTag(null); // 不根据组织机构顾虑
		} else {
			managerInfo.setIsFliterTag(1); // 根据组织机构顾虑
			managerInfo.setCurrentAdminId(curLoginUserId);
		}
		return managerInfo;
	}

	/**
	 * 初始化手机令牌列表
	 */
	public String init() {
		try {
			PageArgument pageArg = pageArgument();
			pageArg.setCurPage(getPage());
			pageArg.setPageSize(getPagesize());
			List<?> resultList = query(pageArg);
			String jsonStr = JsonTool.getJsonFromList(pageArg.getTotalRow(),
					resultList, null);
			setResponseWrite(jsonStr);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public String add() {
		return null;
	}

	/**
	 * 手机令牌的删除
	 */
	public String delete() {
		return null;
	}

	/**
	 * 初始化分发页面和设定标识码页面调用方法
	 */
	public String find() {
		String dist = request.getParameter("dist");
		DistManagerInfo managerInfo = null;
		String userName = distManagerInfo.getUserName(); // 临时取出令牌绑定用户的名称
		try {
			managerInfo = (DistManagerInfo) distManagerServ
					.find(distManagerInfo);
			if (!StrTool.objNotNull(managerInfo)) {
				return init();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		managerInfo.setUserName(userName);
		this.setDistManagerInfo(managerInfo);

		// 如果请求来源是分发功能，直接转向分发页面
		if (StrTool.strNotNull(dist)) {
			if (StrTool.strNotNull(userName)) {
				if (userName.split("]").length <= 1) {
					distManagerInfo.setEmFlag(1);
				}
			}
			return "dist_find";
		}

		// 如果请求来源是设定手机标识码，转向设定手机令牌标识码页面
		return SUCC_FIND;
	}

	/**
	 * 行数统计 分页处理
	 * 
	 * @Date in Apr 15, 2011,11:33:36 AM
	 * @return
	 */
	private PageArgument pageArgument() throws BaseException {
		int totalRow = distManagerServ
				.count(getDistManagerInfo(distManagerQueryForm));
		PageArgument pageArg = getArgument(totalRow);

		return pageArg;
	}

	/**
	 * 更改手机令牌的标识码、更新分发信息
	 */
	public String modify() {
		String oper = request.getParameter("oper");
		DistManagerInfo info = null;
		try {
			// 重置
			info = (DistManagerInfo) distManagerServ.find(distManagerInfo);
			if (StrTool.strNotNull(oper)) {
				if (StrTool.objNotNull(info)) {
					info.setActived(0);
					info.setActivepass("");
					info.setApdeath(0);
					info.setApretry(0);
					info.setActivetime(0);
					info.setProvtype(-1);
					info.setPhoneudid("");
					info.setMark(0);
					distManagerServ.updateObj(info);
				}
			} else {
				// 设定标识码
				info.setMark(1);
				info.setPhoneudid(distManagerInfo.getPhoneudid());
				distManagerServ.updateObj(info);
			}

			outPutOperResult(Constant.alert_succ, Language.getLangStr(request,
					"common_opera_succ_tip"));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"common_opera_error_tip"));
		}

		return null;
	}

	/***************************************************************************
	 * 批量更改状态、删除操作
	 * 
	 * @Date in Apr 25, 2011,3:03:50 PM
	 * @return
	 */
	public String modifyBatch() {
		return null;
	}

	/**
	 * 手机令牌列表查询数据处理
	 * 
	 * @Date in Apr 18, 2011,11:48:33 AM
	 * @param pageArg
	 */
	private List<?> query(PageArgument pageArg) {
		List<?> tokenList = null;
		try {
			tokenList = distManagerServ.query(
					getDistManagerInfo(distManagerQueryForm), pageArg);

			// 查询出所有令牌绑定的用户
			if (StrTool.listNotNull(tokenList)) {
				DistManagerInfo distTknInfo = null;
				for (int i = 0; i < tokenList.size(); i++) {
					distTknInfo = (DistManagerInfo) tokenList.get(i);
					String username = aide.getUserIds(distTknInfo.getToken());
					distTknInfo.setUserName(username);
				}
			}
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return tokenList;
	}

	/**
	 * 分页跳转
	 */
	public String page() {
		PageArgument pArgument = getArgument(request, 0);
		query(pArgument);
		return SUCCESS;
	}

	/**
	 * 获取分发配置的实体信息
	 */
	public TokenConfInfo getDistConfInfo() {
		List<?> configList = ConfDataFormat
				.getConfList(ConfConstant.CONF_TYPE_TOKEN);
		return TokenConfInfo.getTknconfInfoList(configList);
	}

	/**
	 * 激活密码生成
	 */
	public String getActivePass(String apnewm) {
		String activePass = "";
		if (StrTool.strEquals(StrConstant.common_number_2, apnewm)) {// 2 随机
			activePass = RandomPassUtil.genActivePass(6);
		} else if (StrTool.strEquals(StrConstant.common_number_8, apnewm)) {// 8
			// 手动输入
			activePass = distManagerInfo.getActivepass();
		} else {// 默认
			activePass = distConfInfo.getDefultap();
		}

		return activePass;
	}

	/**
	 * 手机令牌离线分发
	 * 
	 * @Date in Aug 27, 2013,10:31:04 AM
	 * @return
	 */
	public String offLineActivate() {
		String apnewm = request.getParameter("apnewm");
		String activePass = getActivePass(apnewm);
		String activeCode = "";// 激活码

		MessageBean messageBean = new MessageBean();
		messageBean.setTokenSN(distManagerInfo.getToken());
		messageBean.setOtp(activePass);
		messageBean.setUdid(distManagerInfo.getPhoneudid());

		String[] resultArr = authHelper.offLineActivate(messageBean);
		int resultCode = StrTool.parseInt(resultArr[0]);
		if (resultCode == SoapAttribute.OTP_SUCCESS) {
			activeCode = resultArr[1];

			String retStr = "";
			// 是否通过邮件发送
			String isSendEmail = ConfDataFormat.getConfValue(
					ConfConstant.CONF_TYPE_TOKEN, ConfConstant.DIST_EMAIL_SEND);
			if (isSendEmail.equals(StrConstant.common_number_1)) {
				String[] retArr = emailSendDistInfo(
						NumConstant.common_number_0, activePass, activeCode,
						null);
				if (StrTool.strEquals(retArr[0], StrConstant.common_number_1)) {
					retStr = retArr[1];
				}
				this.setDistDesc(retArr[1]);
			}

			// 是否通过短信分发
			String isSendSms = ConfDataFormat.getConfValue(
					ConfConstant.CONF_TYPE_TOKEN, ConfConstant.AP_SMS_SEND);
			if (isSendSms.equals(StrConstant.common_number_1)) {
				String message = ConfDataFormat.getConfValue(
						ConfConstant.CONF_TYPE_TOKEN,
						ConfConstant.SMS_MOBILE_ACTIVATE_CODE_MESSAGE);
				if (message.indexOf("[SN]") != -1) {
					message = message.replace("[SN]", distManagerInfo
							.getToken());
				}
				if (message.indexOf("[AP]") != -1) {
					message = message.replace("[AP]", activePass);
				}
				if (message.indexOf("[AC]") != -1) {
					message = message.replace("[AC]", activeCode);
				}

				aide.sendDistMessage(distManagerInfo.getToken(), message);
			}
			outPutOperResult(retStr, activePass + activeCode);
			super.setActionResult(true);

		} else {
			String errMeg = Language.getLangStr(request,
					"tkn_dist_mobile_off_fail")
					+ LangResultCode.getLangStr(request, resultCode);
			outPutOperResult(Constant.alert_error, errMeg);
			super.setActionResult(false);
			this.setDistDesc(errMeg);
		}

		return null;
	}

	/**
	 * 手机令牌在线分发
	 * 
	 * @Date in Aug 27, 2013,11:16:01 AM
	 * @return
	 */
	public String onLineDistribute() {
		// 判断手机令牌分发站点策略是否开启
		if (StrTool.strEquals(distConfInfo.getSiteenabled(), "n")) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_dist_mobile_error"));
			return null;
		}

		// 判断分发站点访问URL中IP地址是否合法有效
		if (StrTool.strEquals(distConfInfo.getIp().trim(), "127.0.0.1")
				|| StrTool.strEquals(distConfInfo.getIp().trim(), "localhost")) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_dist_mobile_error_ip"));
			return null;
		}

		// 分发URL
		String distUrl = distConfInfo.getSiteurl();
		if (!StrTool.strNotNull(distUrl)) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_dist_mobile_error_url"));
			return null;
		}

		// 分发站点访问参数
		String distParam = request.getParameter("distParam");
		String userId = distManagerInfo.getUserName();
		if ((StrTool.strEquals(distParam, "1") || StrTool.strEquals(distParam,
				"3"))
				&& userId.split("]").length <= 1) {
			try {
				distUrl = distUrl + "?uid="
						+ URLEncoder.encode(userId, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (StrTool.strEquals(distParam, "2")
				|| StrTool.strEquals(distParam, "3")) {
			if (distUrl.indexOf("?") != -1) {
				distUrl = distUrl + "&tkid=" + distManagerInfo.getToken();
			} else {
				distUrl = distUrl + "?tkid=" + distManagerInfo.getToken();
			}
		}

		String apnewm = request.getParameter("apnewm");
		String activePass = getActivePass(apnewm);

		MessageBean messageBean = new MessageBean();
		messageBean.setTokenSN(distManagerInfo.getToken());
		messageBean.setOtp(activePass);
		messageBean.setUdid(distManagerInfo.getPhoneudid());
		int resultCode = authHelper.onLineDistribute(messageBean);
		if (resultCode == SoapAttribute.OTP_SUCCESS) {
			// 邮件发送
			String retStr = "";
			// 是否通过邮件发送
			String isSendEmail = ConfDataFormat.getConfValue(
					ConfConstant.CONF_TYPE_TOKEN, ConfConstant.DIST_EMAIL_SEND);
			if (isSendEmail.equals(StrConstant.common_number_1)) {
				String[] retArr = emailSendDistInfo(
						NumConstant.common_number_1, activePass, null, distUrl);
				if (StrTool.strEquals(retArr[0], StrConstant.common_number_1)) {
					retStr = retArr[1];
				}
				this.setDistDesc(retArr[1]);
			}
			// 是否通过短信分发
			String isSendSms = ConfDataFormat.getConfValue(
					ConfConstant.CONF_TYPE_TOKEN, ConfConstant.AP_SMS_SEND);
			if (isSendSms.equals(StrConstant.common_number_1)) {
				String message = ConfDataFormat.getConfValue(
						ConfConstant.CONF_TYPE_TOKEN,
						ConfConstant.SMS_MOBILE_ONLINE_DIST_MESSAGE);
				if (message.indexOf("[URL]") != -1) {
					message = message.replace("[URL]", distUrl);
				}
				if (message.indexOf("[AP]") != -1) {
					message = message.replace("[AP]", activePass);
				}

				aide.sendDistMessage(distManagerInfo.getToken(), message);
			}

			outPutOperResult(retStr, activePass + distUrl);
			super.setActionResult(true);
		} else {
			String errMeg = Language.getLangStr(request,
					"tkn_dist_mobile_on_fail")
					+ LangResultCode.getLangStr(request, resultCode);
			outPutOperResult(Constant.alert_error, errMeg);
			super.setActionResult(false);
			this.setDistDesc(errMeg);
		}

		return null;
	}

	/**
	 * 邮件发送手机令牌激活信息
	 * 
	 * @Date in Aug 27, 2013,1:31:53 PM
	 * @param userId
	 * @param distType
	 * @param activeCode
	 */
	private String[] emailSendDistInfo(int distType, String activePwd,
			String activeCode, String distUrl) {
		String[] retArr = new String[2];

		String userId = distManagerInfo.getUserName();
		String sendmail = request.getParameter("sendMail");
		String tokenSn = distManagerInfo.getToken();

		// 是否发送分发邮件
		if (!StrTool.strNotNull(sendmail) || !StrTool.strEquals(sendmail, "on")) {
			return retArr;
		}

		if (!StrTool.strNotNull(userId)) {
			retArr[0] = StrConstant.common_number_1;
			retArr[1] = Language
					.getLangStr(request, "tkn_warn_assign_result_4");

			return retArr;
		}

		UserToken userToken = null;
		List<?> userTokens = aide.getUserTokens(userId, tokenSn);
		if (!StrTool.listNotNull(userTokens)) {
			return retArr;
		}
		userToken = (UserToken) userTokens.get(0);

		String distTypeLang = Language.getLangStr(request, "tkn_dist_offline");
		if (distType == NumConstant.common_number_1) {
			distTypeLang = Language.getLangStr(request, "tkn_dist_online")
					+ Language.getLangStr(request, "tkn_dist_type_online");
		} else if (distType == NumConstant.common_number_2) {
			distTypeLang = Language.getLangStr(request, "tkn_dist_online")
					+ Language.getLangStr(request, "tkn_dist_type_offline");
		}

		// 获取发送邮件的内容
		String htmlStr = getEmailHtmlStr(distManagerInfo, distType,
				distTypeLang, activePwd, activeCode, distUrl);
		if (null != userToken.getDomainId()) {
			// 用户
			UserInfo userInfo = getUserInfo(userId);
			// 邮件发送
			return sendEmail(userInfo, tokenSn, htmlStr, distUrl, 1);
		} else {
			// 管理员
			AdminUser adminUser = getAdminUser(userId);
			// 邮件发送
			return sendEmail(adminUser, tokenSn, htmlStr, distUrl, 2);
		}
	}

	/**
	 * 手机令牌详细信息，分发时调用
	 */
	public String view() {

		return null;
	}

	/**
	 * 组织手机令牌激活邮件内容
	 */
	public String getEmailHtmlStr(DistManagerInfo managerInfo, int distType,
			String distTypeLang, String activePwd, String activeCode,
			String distUrl) {
		StringBuffer buffer = new StringBuffer();
		buffer
				.append("<table width='60%' border='0' align='center' cellpadding='4' cellspacing='1' bgcolor='#CCCCCC'>");

		buffer.append("<tr>");
		buffer
				.append("<td colspan='2' align='center' bgcolor='#EBEBEB'><strong>");
		buffer.append(Language.getLangStr(request, "tkn_dist_info"));
		buffer.append("</strong></td>");
		buffer.append("</tr>");

		buffer.append("<tr>");
		buffer.append("<td width='35%' bgcolor='#FFFFFF' align='right'>");
		buffer.append(Language.getLangStr(request, "tkn_dist_type")
				+ Language.getLangStr(request, "colon"));
		buffer.append("</td>");
		buffer.append("<td width='65%' bgcolor='#FFFFFF'>");
		buffer.append(distTypeLang);
		buffer.append("</td>");
		buffer.append("</tr>");

		buffer.append("<tr>");
		buffer.append("<td width='35%' bgcolor='#EBEBEB' align='right'>");
		buffer.append(Language.getLangStr(request, "user_info_account")
				+ Language.getLangStr(request, "colon"));
		buffer.append("</td>");
		buffer.append("<td width='65%' bgcolor='#EBEBEB'>");
		buffer.append(managerInfo.getUserName());
		buffer.append("</td>");
		buffer.append("</tr>");

		buffer.append("<tr>");
		buffer.append("<td width='35%' bgcolor='#FFFFFF' align='right'>");
		buffer.append(Language.getLangStr(request, "tkn_comm_tknum")
				+ Language.getLangStr(request, "colon"));
		buffer.append("</td>");
		buffer.append("<td width='65%' bgcolor='#FFFFFF'>");
		buffer.append(managerInfo.getToken());
		buffer.append("</td>");
		buffer.append("</tr>");

		buffer.append("<tr>");
		buffer.append("<td width='35%' bgcolor='#EBEBEB' align='right'>");
		buffer.append(Language.getLangStr(request, "tkn_activation_pwd")
				+ Language.getLangStr(request, "colon"));
		buffer.append("</td>");
		buffer.append("<td width='65%' bgcolor='#EBEBEB'>");
		buffer.append(activePwd);
		buffer.append("</td>");
		buffer.append("</tr>");

		if (distType == 1 || distType == 2) {
			buffer.append("<tr>");
			buffer.append("<td width='35%' bgcolor='#FFFFFF' align='right'>");
			buffer.append(Language.getLangStr(request,
					"tkn_dist_two_dimensional_code_imag")
					+ Language.getLangStr(request, "colon"));
			buffer.append("</td>");
			buffer
					.append("<td width='65%' bgcolor='#EBEBEB'><img  src='cid:IMG1' width='150' height='150' />");
			buffer.append("</td>");
			buffer.append("</tr>");


			buffer.append("<tr>");
            buffer.append("<td width='35%' bgcolor='#FFFFFF' align='right'>");
            buffer.append(Language.getLangStr(request,
                    "common_syntax_tip")
                    + Language.getLangStr(request, "colon"));
            buffer.append("</td>");
            buffer
                    .append("<td width='65%' bgcolor='#FFFFFF'>");
            buffer.append(Language.getLangStr(request, "tkn_dist_info_tips_1"));
            buffer.append("</td>");
            buffer.append("</tr>");
		} else {
			buffer.append("<tr>");
			buffer.append("<td width='35%' bgcolor='#FFFFFF' align='right'>");
			buffer.append(Language.getLangStr(request, "tkn_activation_code")
					+ Language.getLangStr(request, "colon"));
			buffer.append("</td>");
			buffer.append("<td width='65%' bgcolor='#FFFFFF'>");
			buffer.append(activeCode);
			buffer.append("</td>");
			buffer.append("</tr>");
			
			buffer.append("<tr>");
            buffer.append("<td width='35%' bgcolor='#FFFFFF' align='right'>");
            buffer.append(Language.getLangStr(request,
                    "common_syntax_tip")
                    + Language.getLangStr(request, "colon"));
            buffer.append("</td>");
            buffer.append("<td width='65%' bgcolor='#FFFFFF'>");
            buffer.append(Language.getLangStr(request, "tkn_dist_info_tips_2"));
            buffer.append("</td>");
            buffer.append("</tr>");
		}

		buffer.append("</table>");

		return buffer.toString();
	}

	/**
	 * 邮件发送
	 * 
	 * @Date in Aug 28, 2013,10:54:56 AM
	 * @param object
	 * @param token
	 * @param content
	 * @param flag
	 * @return
	 */
	public String[] sendEmail(Object object, String token, String content,
			String imageurl, int flag) {
		String[] retArr = new String[2];
		// 发送邮件
		String logStr = "";
		String email = "";
		if (flag == NumConstant.common_number_1) {
			UserInfo userInfo = (UserInfo) object;
			email = userInfo.getEmail();
			if (!StrTool.strNotNull(email)) {
				logStr = Language.getLangStr(request, "tkn_dist_info_user")
						+ userInfo.getUserId()
						+ Language.getLangStr(request, "tkn_warn_no_set_email");
				retArr[0] = StrConstant.common_number_1;
				retArr[1] = logStr;

				return retArr;
			}

			// 得到发送邮件信息
			String subject = Language.getLangStr(request, "tkn_dist_info");
			MailInfo mailInfo = SendMailUtil.getMailInfo(
					new String[] { email }, subject, content, null);
			if(StrTool.objNotNull(mailInfo)){
			    mailInfo.setImgpath(imageurl);
			}
			if (SendMailUtil.emailSeed(mailInfo)) {
				logStr = Language.getLangStr(request,
						"tkn_email_to_user_success_1")
						+ userInfo.getUserId()
						+ Language.getLangStr(request,
								"tkn_email_to_user_success_2")
						+ email
						+ Language.getLangStr(request,
								"tkn_email_to_user_success_3")
						+ token
						+ Language.getLangStr(request,
								"tkn_email_to_user_success_4");
				retArr[0] = StrConstant.common_number_0;
			} else {
				logStr = Language.getLangStr(request,
						"tkn_email_to_user_failed");
				retArr[0] = StrConstant.common_number_1;
			}
		} else {
			AdminUser adminUser = (AdminUser) object;
			email = adminUser.getEmail();
			if (!StrTool.strNotNull(email)) {
				logStr = Language.getLangStr(request, "tkn_dist_info_user")
						+ adminUser.getAdminid()
						+ Language.getLangStr(request, "tkn_warn_no_set_email");
				retArr[0] = StrConstant.common_number_1;
				retArr[1] = logStr;

				return retArr;
			}

			// 得到发送邮件信息
			String subject = Language.getLangStr(request, "tkn_dist_info");
			MailInfo mailInfo = SendMailUtil.getMailInfo(
					new String[] { email }, subject, content, null);
			if (SendMailUtil.emailSeed(mailInfo)) {
				logStr = Language.getLangStr(request,
						"tkn_email_to_user_success_1")
						+ adminUser.getAdminid()
						+ Language.getLangStr(request,
								"tkn_email_to_user_success_2")
						+ email
						+ Language.getLangStr(request,
								"tkn_email_to_user_success_3")
						+ token
						+ Language.getLangStr(request,
								"tkn_email_to_user_success_4");
				retArr[0] = StrConstant.common_number_0;
			} else {
				logStr = Language.getLangStr(request,
						"tkn_email_to_user_failed");
				retArr[0] = StrConstant.common_number_1;
			}
		}
		retArr[1] = logStr;

		return retArr;
	}

	/**
	 * 根据用户ID获取用户基本信息
	 * 
	 */
	public UserInfo getUserInfo(String userid) {
		UserInfo userInfo = new UserInfo();
		if (StrTool.strNotNull(userid)) {
			userInfo.setUserId(userid);
		}
		try {
			userInfo = (UserInfo) userInfoServ.find(userInfo);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return userInfo;
	}

	/**
	 * 根据管理员ID获取管理员基本信息
	 * 
	 */
	public AdminUser getAdminUser(String adminid) {
		AdminUser adminUser = new AdminUser();
		if (StrTool.strNotNull(adminid)) {
			adminUser.setAdminid(adminid);
		}
		try {
			adminUser = (AdminUser) adminUserServ.find(adminUser);
		} catch (BaseException e) {
			logger.error(e.getMessage(), e);
		}

		return adminUser;
	}

	/**
	 * 对分发信息做处理
	 * 
	 * @Date in May 11, 2011,3:12:13 PM
	 * @param distInfos
	 * @param username
	 * @param activeCodeSpit
	 */
	public void updateDistInfo(DistManagerInfo distInfos, String username,
			String activeCodeSpit) {
		DistManagerInfo distInfo = null;
		try {
			// 先做更新
			distInfos.setMark(2);
			distManagerServ.updateObj(distInfos);
			// 查询更新的数据,页面显示
			distInfo = (DistManagerInfo) distManagerServ.find(distInfos);
			if (StrTool.strNotNull(username)) {
				distInfo.setUserName(username);
			}
			if (StrTool.strNotNull(distInfos.getDistUrl())) {
				distInfo.setDistUrl(distInfos.getDistUrl());
			}
			distInfo.setActiveCode(activeCodeSpit);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.setDistManagerInfo(distInfo);
	}

	/**
	 * 激活令牌产生二维码,对于重复激活同样适用 二维码数据soap请求服务器获得
	 * 
	 * @return
	 */
	public String twoCodeActive() throws BaseException {
		try {
			// SOAP请求获取二维码激活数据
			String isNeedPwd = request.getParameter("isneedPwd");
			String activePass = "";
			if (StrTool.strEquals(isNeedPwd, "1")) {
				String apnewm = request.getParameter("apnewm");
				activePass = getActivePass(apnewm);
			}
			MessageBean messageBean = new MessageBean();
			messageBean.setTokenSN(distManagerInfo.getToken());
			messageBean.setOtp(activePass);
			messageBean.setUdid(distManagerInfo.getPhoneudid());

			String[] resultArr = authHelper.genAcData(messageBean);
			int resultCode = StrTool.parseInt(resultArr[0]);
			if (resultCode != SoapAttribute.OTP_SUCCESS) {
				String errMeg = Language.getLangStr(request,
                "tkn_dist_mobile_dimensional_fail")
                +LangResultCode.getLangStr(request, resultCode);
				outPutOperResult(Constant.alert_error, errMeg);
				return null;
			}

			// 二维码激活数据
			String twoCode = resultArr[1];
			String viewPath = "";
			if (StrTool.strNotNull(twoCode)) {
				// 生成二维码图片存放的地址和名称
				String fileName = System.currentTimeMillis() + "image.png";
				String imgPath = getFilePath(Constant.WEB_TEMP_FILE_BARCODE,
						fileName);

				// 如果已存在file则删除
				if (CreateFileUtil.isFileExists(imgPath)) {
					CreateFileUtil.deleteFile(imgPath);
				}

				if (CreateFileUtil.createFile(imgPath)) {
					createImage(twoCode, imgPath);
				}

				viewPath = imgPath.substring(imgPath
						.indexOf(Constant.WEB_TEMP_FILE_BARCODE), imgPath
						.length());

				outPutOperResult(Constant.alert_succ, viewPath + ","
						+ activePass);
			} else {
				outPutOperResult(Constant.alert_error, Language.getLangStr(
						request, "tkn_gen_barcode_error"));
			}
			// 是否通过邮件发送
			String isSendEmail = ConfDataFormat.getConfValue(
					ConfConstant.CONF_TYPE_TOKEN, ConfConstant.DIST_EMAIL_SEND);
			if (isSendEmail.equals(StrConstant.common_number_1)) {
				emailSendDistInfo(NumConstant.common_number_2, activePass,
						null, Constant.WEB_APP_PATH + viewPath);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_gen_barcode_error"));
		}
		return null;
	}

	/**
	 * 生成在线激活二维码图片
	 */
	public String onLineDist() {
		// 判断手机令牌分发站点策略是否开启
		if (StrTool.strEquals(distConfInfo.getSiteenabled(), "n")) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_dist_mobile_error"));
			return null;
		}
		// 判断分发站点访问URL中IP地址是否合法有效
		if (StrTool.strEquals(distConfInfo.getIp().trim(), "127.0.0.1")
				|| StrTool.strEquals(distConfInfo.getIp().trim(), "localhost")) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_dist_mobile_error_ip"));
			return null;
		}
		// 分发URL
		String distUrl = distConfInfo.getSiteurl();
		if (!StrTool.strNotNull(distUrl)) {
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_dist_mobile_error_url"));
			return null;
		}
		// 分发令牌
		String userId = distManagerInfo.getUserName();
		if (StrTool.strNotNull(userId) && userId.split("]").length <= 1) {
			try {
				if (distUrl.indexOf("?") != -1) {
					distUrl = distUrl + "&uid="
							+ URLEncoder.encode(userId, "utf-8");
				} else {
					distUrl = distUrl + "?uid="
							+ URLEncoder.encode(userId, "utf-8");
				}
			} catch (UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (distUrl.indexOf("?") != -1) {
			distUrl = distUrl + "&tkid=" + distManagerInfo.getToken();
		} else {
			distUrl = distUrl + "?tkid=" + distManagerInfo.getToken();
		}
		try {
			// SOAP请求获取二维码激活数据
			String isNeedPwd = request.getParameter("isneedPwd");
			String activePass = "";
			if (StrTool.strEquals(isNeedPwd, "1")) {
				String apnewm = request.getParameter("apnewm");
				activePass = getActivePass(apnewm);
			} else {
				// 不设置，就默认设置一个默认密码
				activePass = "12345678";
			}
			MessageBean messageBean = new MessageBean();
			messageBean.setTokenSN(distManagerInfo.getToken());
			messageBean.setOtp(activePass);
			messageBean.setUdid(distManagerInfo.getPhoneudid());
			int resultCode = authHelper.onLineDistribute(messageBean);
			if (resultCode != SoapAttribute.OTP_SUCCESS) {
				String errMeg = Language.getLangStr(request,
                "tkn_dist_mobile_dimensional_online_fail")
                +LangResultCode.getLangStr(request, resultCode);
				outPutOperResult(Constant.alert_error, errMeg);
				return null;
			}

			// 拼装二维码在线激活URL
			StringBuffer data = new StringBuffer();
			data.append("10");
			data.append(isNeedPwd);
			data.append(distUrl);
			String twoCodeUrl = data.toString();
			String viewPath = "";
			if (StrTool.strNotNull(twoCodeUrl)) {
				// 生成二维码图片存放的地址和名称
				String fileName = System.currentTimeMillis() + "image.png";
				String imgPath = getFilePath(Constant.WEB_TEMP_FILE_BARCODE,
						fileName);

				// 如果已存在file则删除
				if (CreateFileUtil.isFileExists(imgPath)) {
					CreateFileUtil.deleteFile(imgPath);
				}

				if (CreateFileUtil.createFile(imgPath)) {
					createImage(twoCodeUrl, imgPath);
				}

				viewPath = imgPath.substring(imgPath
						.indexOf(Constant.WEB_TEMP_FILE_BARCODE), imgPath
						.length());

				outPutOperResult(Constant.alert_succ, viewPath + ","
						+ activePass);
			} else {
				outPutOperResult(Constant.alert_error, Language.getLangStr(
						request, "tkn_gen_barcode_error"));
			}

			// 是否通过邮件发送
			String isSendEmail = ConfDataFormat.getConfValue(
					ConfConstant.CONF_TYPE_TOKEN, ConfConstant.DIST_EMAIL_SEND);
			if (isSendEmail.equals(StrConstant.common_number_1)) {
			    if (!StrTool.strEquals(isNeedPwd, "1")) {
			        activePass = "";
			    }
				emailSendDistInfo(NumConstant.common_number_1, activePass,
						null, Constant.WEB_APP_PATH + viewPath);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_gen_barcode_error"));
		}
		return null;
	}

	/**
	 * 产生二维码图片
	 * 
	 * @param twoCode
	 */
	@SuppressWarnings("deprecation")
	private void createImage(String twoCode, String imgPath) {
		twoDscAide.encoderQRCode(twoCode, imgPath, "png");
		OutputStream output = null;
		try {
			output = new FileOutputStream(imgPath, false);
			twoDscAide.encoderQRCode(twoCode, output);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 校验令牌是否过期
	 * 
	 * @return
	 * @return String
	 * @throws
	 */
	public String checkTknExpTime() {
		String tkn = request.getParameter("token");
		try {
			TokenInfo tkInfo = new TokenInfo();
			tkInfo.setToken(tkn);
			tkInfo = (TokenInfo) tokenServ.find(tkInfo);
			if (!StrTool.objNotNull(tkInfo)) {
				outPutOperResult(Constant.alert_error, Language.getLangStr(
						request, "tkn_get_tkninfo_failed"));
				return null;
			}
			int tknExptime = tkInfo.getExpiretime();
			int curUtcTime = (int) (DateTool.currentUTC());
			if (curUtcTime > tknExptime) {
				outPutOperResult(Constant.alert_error, "error");
			} else {
				outPutOperResult(Constant.alert_succ, "succ");
			}
		} catch (BaseException ex) {
			logger.error(ex.getMessage(), ex);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,
					"tkn_get_tkninfo_failed"));
		}

		return null;
	}

	/**
	 * 校验邮箱服务器有没有配置，如果分发的令牌已经绑定用户检查该用户有没有设置邮箱
	 */
	public String checkMailSMSConfig() {
	    //单用户绑定令牌邮件发送
	    String distEmailSend=ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_TOKEN,
                ConfConstant.DIST_EMAIL_SEND);
	    //激活信息是否通过短信分发
	    String apSmsSend=ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_TOKEN,
                ConfConstant.AP_SMS_SEND);
	    
		String userId = request.getParameter("userid");
		//扫一扫 或 离线分发
		String activationType = request.getParameter("activationType");
		String [] emailTipInfo = null;
		String [] msmTipInfo = null;
		String tipInfo = null;
		UserInfo uinfo =null;
        
		try {
		    if (StrTool.strNotNull(userId)) {
		        uinfo = new UserInfo();
    		    uinfo.setUserId(userId);
    		    uinfo = (UserInfo) userInfoServ.find(uinfo);
		    }
		    //如果单用户绑定令牌邮件发送配置为“是”、用户信息不为空
	        if(StrTool.strEquals(distEmailSend, StrConstant.common_number_1) && StrTool.objNotNull(uinfo)){
	            emailTipInfo = emailConfig(uinfo);
	        }
		    
		    //激活信息是否通过短信分发配置为“是” 、用户信息不为空、离线分发时 
		    if(StrTool.strEquals(activationType, "offline") &&
		            StrTool.strEquals(apSmsSend, StrConstant.common_number_1) && StrTool.objNotNull(uinfo)){
		        msmTipInfo=smsConfig(uinfo);
		    }
		    
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			outPutOperResult(Constant.alert_error, Language.getLangStr(request,"tnk_dist_mail_error_info"));
			return null;
		}
		if (StrTool.objNotNull(emailTipInfo) || StrTool.objNotNull(msmTipInfo)) {
			tipInfo =constructMsg(emailTipInfo,msmTipInfo);
			if(StrTool.strNotNull(tipInfo)){
			    outPutOperResult(Constant.alert_error, tipInfo);
	            return null;
			}
		}
		
		outPutOperResult(Constant.alert_succ, tipInfo);
		return null;
	}
	
	/**
	 * 构造提示消息
	 * @Date   Oct 16, 2014,6:04:06 PM
	 * @author WYJ
	 * @param emailTipInfo
	 * @param msmTipInfo
	 * @return
	 * @return String
	 * @throws
	 */
	public String constructMsg(String [] emailTipInfo,String [] msmTipInfo){
	    //装配那些配置信息不完整 如：服务邮箱、短信网关、用户邮箱、用户手机
	    StringBuffer rsMsg=new StringBuffer();
	    //装配用户的哪种接收分发途径会受到影响 如：邮箱、手机
	    StringBuffer affectInfo=new StringBuffer();
	    if(emailTipInfo!=null){
	        if(StrTool.strNotNull(emailTipInfo[0])){//用户邮箱为空
	            //装配用户邮箱不完整
	            rsMsg.append(emailTipInfo[0]);
	            //用户邮箱收不到分发信息进行装配
	            affectInfo.append(emailTipInfo[0]);
	        }else if(StrTool.strNotNull(emailTipInfo[1])){
	            //用户的邮箱不为空时判断服务邮箱配置没有的话把用户邮箱不到分发信息进行装配
	            affectInfo.append(Language.getLangStr(request, "tnk_warn_person_mail"));
	        }  
	      //邮件服务器信息不完整
	        if(StrTool.strNotNull(emailTipInfo[1])){
	            if(rsMsg!=null && rsMsg.length()>0){
	                rsMsg.append("，").append(emailTipInfo[1]);
	            }else{
	                rsMsg.append(emailTipInfo[1]);
	            }
	        }
	    }
	    if(msmTipInfo!=null){
	        if(StrTool.strNotNull(msmTipInfo[0])){//用户短信为空
	            //装配用户手机信息不完整
	            if(rsMsg!=null && rsMsg.length()>0){
	                rsMsg.append("，").append(msmTipInfo[0]);
	            }else{
	                rsMsg.append(msmTipInfo[0]);
	            }
	            //用户手机收不到分发信息进行装配
	            if(affectInfo!=null && affectInfo.length()>0 ){
	                affectInfo.append("，").append(msmTipInfo[0]);
	            }else {
	                affectInfo.append(msmTipInfo[0]);
	            }
	        }else if(StrTool.strNotNull(msmTipInfo[1])){//短信网关
	             //用户的短信不为空时判断短信网关配置没有的话把用户手机不到分发信息进行装配
	            if(affectInfo!=null && affectInfo.length()>0 ){
	                affectInfo.append("，").append(Language.getLangStr(request,"tnk_ware_person_msm"));
	            }else {
	                affectInfo.append(Language.getLangStr(request,"tnk_ware_person_msm"));
	            }
	        }
	        
	        //短信网关信息不完整
	        if(StrTool.strNotNull(msmTipInfo[1])){
	           if(rsMsg!=null && rsMsg.length()>0){
	                rsMsg.append("，").append(msmTipInfo[1]);
	            }else{
	                rsMsg.append(msmTipInfo[1]);
	            }
	        }
	    }
	    
	    if(rsMsg!=null && rsMsg.length()>0){
	        rsMsg.append(Language.getLangStr(request, "tnk_warn_no_set_mail_msm_info_1"))
	        .append(affectInfo).append(Language.getLangStr(request, "tnk_warn_no_set_mail_msm_info_2"));
	    }
	    return rsMsg.toString();
	}
	
	/**
	 * 验证邮箱服务器配置和用户邮箱信息
	 * @Date   Oct 16, 2014,5:24:46 PM
	 * @author WYJ
	 * @param uinfo
	 * @return
	 * @throws BaseException
	 * @return String
	 * @throws
	 */
	public String[] emailConfig(UserInfo uinfo) throws BaseException{
	    EmailInfo email = new EmailInfo();
        email.setIsdefault(1);
        String tipInfo []= new String[2];
        email = (EmailInfo) emailServ.find(email);
        // 如果默认服务邮箱未设置
        if(StrTool.objNotNull(email)){
           //没有配置完整的服务邮箱，需要进行提醒
            if (!StrTool.strNotNull(email.getAccount())|| !StrTool.strNotNull(email.getPwd())) {
                tipInfo[1] = Language.getLangStr(request, "tnk_warn_server_mail");
            }else{
                tipInfo[1] ="";
            }
        }else{//没有配置服务邮箱，需要进行提醒
            tipInfo[1] = Language.getLangStr(request, "tnk_warn_server_mail");
        }
        // 如果用户未设置邮箱
        if (StrTool.objNotNull(uinfo)
                && !StrTool.strNotNull(uinfo.getEmail())) {
            tipInfo[0] =Language.getLangStr(request, "tnk_warn_person_mail");
        }else{
            tipInfo[0] ="";
        }
	    return tipInfo;
	}
	
	/**
     * 验证短信服务器配置和用户短信信息
     * @Date   Oct 16, 2014,5:24:46 PM
     * @author WYJ
     * @param uinfo
     * @return
     * @throws BaseException
     * @return String
     * @throws
     */
    public String [] smsConfig(UserInfo uinfo) throws BaseException{
        SmsInfo smsInfo = new SmsInfo();
        String tipInfo []= new String[2];
        //获取是否配置短信网关
        int smsCount= smsInfoServ.count(smsInfo);
        
        if(smsCount == NumConstant.common_number_0){//没有配置短信网关，需要进行提醒
            tipInfo[1] = Language.getLangStr(request, "adm_perm_050004");
        }else{
            tipInfo[1] ="";
        }
        // 如果用户未设置手机
        if (StrTool.objNotNull(uinfo)
                && !StrTool.strNotNull(uinfo.getCellPhone())) {
            tipInfo[0] =  Language.getLangStr(request,"tnk_ware_person_msm");
        }else{
            tipInfo[0] ="";
        }
        return tipInfo;
    }
	

}
