/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.email.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.mail.MailInfo;
import com.ft.otp.common.mail.SendMailUtil;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.email.entity.EmailInfo;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 邮件服务器配置业务action
 * @Date in Nov 19, 2012,1:26:34 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class EmailInfoAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 1482980523477646418L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(EmailInfoAction.class);

    private IEmailInfoServ emailServ;
    private EmailInfo emailInfo;

    public IEmailInfoServ getEmailServ() {
        return emailServ;
    }

    public void setEmailServ(IEmailInfoServ emailServ) {
        this.emailServ = emailServ;
    }

    public EmailInfo getEmailInfo() {
        return emailInfo;
    }

    public void setEmailInfo(EmailInfo emailInfo) {
        this.emailInfo = emailInfo;
    }

    /**
     * 添加邮件服务器
     */
    public String add() {
        try {
            if (StrTool.objNotNull(emailInfo)) {
                emailServ.addObj(emailInfo);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_add_succ_tip"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
        }
        PubConfConfig.clear();
        PubConfConfig.loadPubConfConfig();
        return null;
    }

    /**
     * 邮箱服务器删除操作
     */
    public String delete() {
        String delIdStr = request.getParameter("delIds");
        String[] delIds = delIdStr.split(",");
        try {
            if (delIds.length > 0) {
                int[] ids = new int[delIds.length];
                for (int i = 0; i < delIds.length; i++) {
                    ids[i] = Integer.parseInt(delIds[i]);
                }
                EmailInfo email = new EmailInfo();
                email.setBatchIdsInt(ids);
                emailServ.delObj(email);
            }
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
        } catch (Exception e) {
            e.printStackTrace();
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
        }
        return null;
    }

    /**
     * 查找邮件服务器信息
     */
    public String find() {
        String emailId = request.getParameter("emailId");
        EmailInfo email = new EmailInfo();
        email.setId(Integer.parseInt(emailId));
        try {
            email = (EmailInfo) emailServ.find(email);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setEmailInfo(email);
        return SUCC_ADD;
    }

    /**
     * 系统初始化查找默认的邮件服务器
     * 
     * @Date in Apr 5, 2013,6:21:23 PM
     * @return
     */
    public String initFind() {
        EmailInfo email = new EmailInfo();
        email.setIsdefault(1);
        try {
            email = (EmailInfo) emailServ.find(email);
            setEmailInfo(email);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return "initFind";
    }

    /**
     * 邮件服务器初始化列表
     */
    public String init() {
        try {
            PageArgument pageArg = new PageArgument();
            List<?> resultList = query(pageArg);

            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询邮件服务器列表
     * @Date in Nov 19, 2012,3:55:43 PM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        List<?> mailList = null;
        try {
            mailList = emailServ.query(emailInfo, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return mailList;
    }

    /**
     * 编辑邮件服务器配置
     */
    public String modify() {
        String isdefault = request.getParameter("isdefault");
        try {
            if (StrTool.objNotNull(emailInfo)) {
                emailInfo.setIsdefault(Integer.parseInt(isdefault));
                emailServ.updateObj(emailInfo);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
        }
        return null;
    }

    /**
     * 设置默认邮件服务器
     * @Date in Nov 19, 2012,7:15:10 PM
     * @return
     */
    public String setDefEmail() {
        String emailId = request.getParameter("emailId");
        try {
            EmailInfo email = new EmailInfo();
            email.setId(Integer.parseInt(emailId));
            email.setIsdefault(0);
            //记录日志
            EmailInfo emailInfo = (EmailInfo) emailServ.find(email);
            if (StrTool.objNotNull(emailInfo)) {
                emailInfo.setIsdefault(0);
            }
            emailServ.updateIsdefaultOE(emailInfo);
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_opera_succ_tip"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
        }
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
     * 验证邮件服务器是否存在
     * @Date in Nov 20, 2012,9:48:58 AM
     * @return
     */
    public String findOEisExist() {
        EmailInfo email = new EmailInfo();
        try {
            String emailHost = emailInfo.getHost();
            if (!StrTool.strNotNull(emailHost)) {
                return null;
            }
            email.setHost(emailHost);
            email = (EmailInfo) emailServ.find(email);
            if (StrTool.objNotNull(email)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 验证邮箱服务器名称是否已经存在
     * @Date in Nov 20, 2012,9:51:29 AM
     * @return
     */
    public String findOENameisExist() {
        EmailInfo email = new EmailInfo();
        try {
            String serverName = emailInfo.getServname();
            if (!StrTool.strNotNull(serverName)) {
                return null;
            }
            serverName = MessyCodeCheck.iso885901ForUTF8(serverName);
            email.setServname(serverName);
            email = (EmailInfo) emailServ.find(email);
            if (StrTool.objNotNull(email)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     *  
     * 邮件连接测试
     * 
     * @Date in Apr 5, 2013,2:57:46 PM
     * @return
     */
    public String emailConnTest() {
        MailInfo mailInfo = new MailInfo();

        mailInfo.setMailServerHost(emailInfo.getHost());
        mailInfo.setMailServerPort(StrTool.intToString(emailInfo.getPort()));
        mailInfo.setFromAddress(emailInfo.getSender());

        mailInfo.setValidate(emailInfo.getValidated() == NumConstant.common_number_1 ? true : false);
        mailInfo.setUserName(emailInfo.getAccount());
        mailInfo.setPassword(emailInfo.getPwd());

        boolean result = SendMailUtil.emailConn(mailInfo);
        if (result) {
            setResponseWrite(StrConstant.common_number_0);
        } else {
            setResponseWrite(StrConstant.common_number_1);
        }

        return null;
    }

    /**
     * 系统初始化时添加邮件服务器配置
     * 
     * @Date in Apr 5, 2013,5:53:55 PM
     * @return
     */
    public String initAdd() {
        boolean result = false;
        try {
            if (null != emailInfo) {
                emailServ.updateObj(emailInfo);

                result = true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            setResponseWrite(StrConstant.common_number_1);
        }

        if (result) {
            setResponseWrite(StrConstant.common_number_0);
            PubConfConfig.clear();
            PubConfConfig.loadPubConfConfig();
        } else {
            setResponseWrite(StrConstant.common_number_0);
        }

        return null;
    }

}
