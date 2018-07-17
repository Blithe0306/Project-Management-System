package com.ft.otp.common.mail;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 具体发送邮件的类，注意使用时要先初始化Session，
 * 再初始化Transport，然后才能发送邮件，
 * 邮件全部发送完毕后要将Transport关闭
 *
 * @Date in Oct 19, 2011,3:35:59 PM
 *
 * @author TBM
 */
public class SendMail {
    private static Logger logger = Logger.getLogger(SendMail.class);
    private static Transport transport = null;

    private static Session session = null;

    /**
     * 初始化Session
     * @param mailInfo 邮件发送信息
     * @param debug 是否输出调试信息
     * @return 是否初始化成功
     */
    public static boolean initSession(MailInfo mailInfo, boolean debug) {
        // 得到session
        try {
            MyAuthenticator authenticator = null;
            Properties pro = mailInfo.getProperties();
            if (mailInfo.isValidate()) {
                authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
            }
            session = Session.getInstance(pro, authenticator);
            session.setDebug(debug);

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 初始化Transport
     * @param mailInfo 邮件发送信息
     * @return 是否初始化成功
     */
    public static boolean initTransport(MailInfo mailInfo) {
        // 得到连接
        try {
            transport = session.getTransport("smtp");
            transport.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
            if (isTransportConnected()) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 是否Transport已联接
     * @return
     */
    public static boolean isTransportConnected() {
        return transport.isConnected();
    }

    /**
     * 当前Session对象是否为NULL
     * @return
     */
    public static boolean currSessionIsNULL() {
        return null == session;
    }

    /**
     * 当前Transport对象是否为NULL
     * @return
     */
    public static boolean currTransportIsNULL() {
        return null == transport;
    }

    /**
     * 关闭Transport联接
     * 
     * @return
     */
    public static boolean closeTransportConnected() {
        boolean result = false;
        try {
            if (transport.isConnected()) {
                transport.close();
                transport = null;
                session = null;
                result = true;
            }
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * 以HTML格式发送邮件
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailInfo mailInfo) {
        try {
            // 发送邮件的地址
            Address from = new InternetAddress("\"" + MimeUtility.encodeText(mailInfo.getFromAddress(),MimeUtility.mimeCharset("utf-8"), null) + "\" <" +mailInfo.getUserName()+ ">");
                              //new InternetAddress("\"" + MimeUtility.encodeText(mailInfo.getFromAddress() + "\" <xusen@mail.page>"));

            // 接收邮件的地址
            String[] toArray = mailInfo.getToAddress();
            if (toArray.length == 0) {
                return false;
            }
            Address[] toAddrArray = new InternetAddress[toArray.length];
            for (int i = 0; i < toArray.length; i++) {
                toAddrArray[i] = new InternetAddress(toArray[i]);
            }
            // 设置邮件内容及附件
            Multipart multi = new MimeMultipart("related");//混合MIME消息
            multi.addBodyPart(createContent(mailInfo), 0);
            //设置附件
            if (StrTool.arrNotNull(mailInfo.getAttachFileNames())) {
                BodyPart attach = createAttachment(mailInfo);
                if (!StrTool.objNotNull(attach)) {
                    return false;
                }
                multi.addBodyPart(attach, 1);
            }
            //设置二维码图片附件
//          if (StrTool.strNotNull(mailInfo.getImgpath())) {
//              multi.addBodyPart(createTwoCodeAttach(new File(mailInfo.getImgpath())));
//          }
            
            if (StrTool.strNotNull(mailInfo.getImgpath())) {
                BodyPart imgBodyPart = new MimeBodyPart(); // 附件图标   
                imgBodyPart.setDataHandler(new DataHandler(new FileDataSource(mailInfo.getImgpath())));   
                imgBodyPart.setFileName("Two-dimensional code.gif");   
                imgBodyPart.setHeader("Content-ID", "IMG1");// 在html中使用该图片方法src="cid:IMG1"   
                multi.addBodyPart(imgBodyPart); 
            }

            //设置邮件内容
            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(from);
            mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);
            mailMessage.setSubject(MimeUtility.encodeText(mailInfo.getSubject(),MimeUtility.mimeCharset("utf-8"), null));
            mailMessage.setSentDate(new Date());
            mailMessage.setContent(multi); //将MiniMultipart对象设置为邮件内容
            mailMessage.saveChanges(); //保存更改

            // 发送邮件
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }
    
    /**
     * 创建正文
     * @throws
     */
    private static BodyPart createContent(MailInfo mailInfo) throws MessagingException{
        BodyPart content = new MimeBodyPart();
        Multipart relate = new MimeMultipart("related");//组合MIME消息

        relate.addBodyPart(createHtmlBody(mailInfo));
        content.setContent(relate);
        return content;
    }
    
    /** 
     * 创建html消息
     * @return
     * @throws MessagingException
     */
    private static BodyPart createHtmlBody(MailInfo mailInfo) throws MessagingException{
        BodyPart html = new MimeBodyPart();
        html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
        return html;
    }
    
    /** 
     * 创建附件
     * @param file
     * @return
     * @throws MessagingException
     */
    private static BodyPart createAttachment(MailInfo mailInfo) throws MessagingException{
        BodyPart attach = new MimeBodyPart();
        String[] attachFiles = mailInfo.getAttachFileNames();
        try {
            if (null != attachFiles && attachFiles.length > 0) {
                for (int i = 0; i < attachFiles.length; i++) {
                    String attachFile = attachFiles[i];
                    File path = new File(attachFile);
                    if (!path.exists() || path.isDirectory()) {
                        return null;
                    }
                    String strFileName = path.getName();
                    attach.setDataHandler(new DataHandler(new FileDataSource(attachFile)));
                    attach.setFileName(MimeUtility.encodeText(strFileName));
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return attach;
    }
    
    /**
     * 创建二维码图片附件
     * @throws
     */
    private static BodyPart createTwoCodeAttach(File file) throws Exception{
        BodyPart attach = new MimeBodyPart();
        DataSource ds = new FileDataSource(file);
        attach.setDataHandler(new DataHandler(ds));
        attach.setFileName(MimeUtility.encodeText("Two-dimensional code.gif"));
        return attach;
    }
    
    /**
     * 创建图片
     * @param file
     * @param name
     * @return
     * @throws MessagingException
     */
    private static BodyPart createImagePart(MailInfo mailInfo, String name) throws MessagingException{
        BodyPart image = new MimeBodyPart();
        image.setDataHandler(new DataHandler(new FileDataSource(mailInfo.getImgpath())));   
        image.setHeader("Content-ID", "IMG1");// 在html中使用该图片方法src="cid:IMG1" 
        return image;
    }

    /**
     * 测试连接smtp服务器
     * @param mailInfo
     * @return
     */
    public static boolean testConnectionSMTP(MailInfo mailInfo) {
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        Session sendMailSession = Session.getInstance(pro, authenticator);
        sendMailSession.setDebug(true);
        try {
            Transport transport = sendMailSession.getTransport("smtp");
            transport.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
            if (transport.isConnected()) {
                transport.close();
                return true;
            }
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

}