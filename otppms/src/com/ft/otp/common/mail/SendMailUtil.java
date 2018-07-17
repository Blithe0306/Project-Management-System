/**
 *Administrator
 */
package com.ft.otp.common.mail;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.NumConstant;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.email.entity.EmailInfo;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 邮件发送工具类，用于组装邮件发送的信息，实现邮件发送的过程
 *
 * @Date in Sep 20, 2012,1:10:49 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class SendMailUtil {

    private static IEmailInfoServ emailServ = (IEmailInfoServ) AppContextMgr.getObject("emailServ");

    /**
     * 邮件连接测试
     * 
     * @Date in Apr 5, 2013,2:51:01 PM
     * @param mailInfo
     * @return
     */
    public static boolean emailConn(MailInfo mailInfo) {
        if (!StrTool.objNotNull(mailInfo)) {
            return false;
        }
        try {
            //初始化Session
            if (SendMail.initSession(mailInfo, false) && SendMail.initTransport(mailInfo)) {
                return true;
            }
        } finally {
            //关闭Transport对象
            SendMail.closeTransportConnected();
        }

        return false;
    }

    /**
     * 邮件发送
     * 
     * @Date in May 26, 2012,6:41:08 PM
     * @param mailInfo
     * @return
     */
    public static boolean emailSeed(MailInfo mailInfo) {
        if (!StrTool.objNotNull(mailInfo)) {
            return false;
        }
        try {
            //初始化Session
            if (SendMail.initSession(mailInfo, false) && SendMail.initTransport(mailInfo)) {
                //发送邮件
                if (SendMail.isTransportConnected()) {
                    if (SendMail.sendHtmlMail(mailInfo)) {
                        return true;
                    }
                }
            }
        } finally {
            //邮件发送完毕关闭Transport对象
            SendMail.closeTransportConnected();
        }

        return false;
    }

    /**
     * 填充邮件发送实体信息，邮件连接测试使用
     *
     * @Date in Apr 5, 2013,2:55:24 PM
     * @return
     */
    public MailInfo getMailInfo() {
        return getMailInfo(null, null, null, null);
    }

    /**
     * 填充邮件发送实体信息
     * 
     * @Date in Oct 19, 2011,4:02:17 PM
     * @param toAddress 发送地址
     * @param subject 主题
     * @param content 邮件主体内容
     * @param accessories 附件地址
     * @return
     */
    public static MailInfo getMailInfo(String[] toAddress, String subject, String content, String[] accessories) {
        //邮件发送服务器为otppms_config表中默认的邮件服务器
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setIsdefault(NumConstant.common_number_1);
        try {
            emailInfo = (EmailInfo) emailServ.find(emailInfo);
        } catch (BaseException ex) {
            return null;
        }
        if (null == emailInfo) {
            return null;
        }

        MailInfo mailInfo = new MailInfo();
        if (null != toAddress && toAddress.length > 0) {
            mailInfo.setToAddress(toAddress);
        }
        mailInfo.setMailServerHost(emailInfo.getHost());
        mailInfo.setMailServerPort(StrTool.intToString(emailInfo.getPort()));
        mailInfo.setFromAddress(emailInfo.getSender());

        mailInfo.setValidate(emailInfo.getValidated() == NumConstant.common_number_1 ? true : false);
        mailInfo.setUserName(emailInfo.getAccount());
        mailInfo.setPassword(emailInfo.getPwd());
        if (null != subject && subject.length() > 0) {
            mailInfo.setSubject(subject);
        }
        if (null != content && content.length() > 0) {
            mailInfo.setContent(content);
        }
        if (null != accessories && accessories.length > 0) {
            mailInfo.setAttachFileNames(accessories);
        }

        return mailInfo;
    }

}
