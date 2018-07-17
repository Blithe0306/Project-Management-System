/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.email.entity.EmailInfo;
import com.ft.otp.manager.confinfo.email.service.IEmailInfoServ;
import com.ft.otp.manager.confinfo.portal.entity.ProNoticeInfo;
import com.ft.otp.manager.confinfo.portal.service.IProNoticeServ;
import com.ft.otp.manager.confinfo.radius.entity.RadAttrInfo;
import com.ft.otp.manager.confinfo.radius.entity.RadProfileInfo;
import com.ft.otp.manager.confinfo.radius.service.IRadAttrServ;
import com.ft.otp.manager.confinfo.radius.service.IRadProfileServ;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;
import com.ft.otp.manager.confinfo.usersource.service.IUserSourceServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 公共配置属性配置MAP，邮件
 *
 * @Date in Feb 21, 2013,7:26:52 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class PubConfConfig {

    private Logger logger = Logger.getLogger(PubConfConfig.class);
    private static PubConfConfig config = null;
    private static Map<Integer, String> emailMap; //邮件服务器
    private static Map<Integer, String> radiusProMap; //Radius配置
    private static Map<String, String> radAttrMap; //RADIUS配置属性
    private static Map<Integer, String> noticeMap; //通知公告
    private static Map<Integer, String> usourceMap; //用户来源

    private IEmailInfoServ emailServ = (IEmailInfoServ) AppContextMgr.getObject("emailServ");
    private IRadProfileServ radProfileServ = (IRadProfileServ) AppContextMgr.getObject("radProfileServ");
    private IRadAttrServ radAttrServ = (IRadAttrServ) AppContextMgr.getObject("radAttrServ");
    private IProNoticeServ noticeServ = (IProNoticeServ) AppContextMgr.getObject("noticeServ");
//    private IUserSourceServ userSourceServ = (IUserSourceServ) AppContextMgr.getObject("userSourceServ");

    private PubConfConfig() {
        emailMap = new HashMap<Integer, String>();
        radiusProMap = new HashMap<Integer, String>();
        radAttrMap = new HashMap<String, String>();
        noticeMap = new HashMap<Integer, String>();
        usourceMap = new HashMap<Integer, String>();
        try {
            List<?> list = emailServ.query(new EmailInfo(), new PageArgument());
            List<?> radProlist = radProfileServ.query(new RadProfileInfo(), new PageArgument());
            List<?> attrlist = radAttrServ.query(new RadAttrInfo(), new PageArgument());
            List<?> noticelist = noticeServ.query(new ProNoticeInfo(), new PageArgument());
//            List<?> usourcelist = userSourceServ.query(new UserSourceInfo(), new PageArgument());

            //邮件服务器配置
            if (StrTool.listNotNull(list)) {
                Iterator<?> iter = list.iterator();
                while (iter.hasNext()) {
                    EmailInfo emailInfo = (EmailInfo) iter.next();
                    emailMap.put(emailInfo.getId(), emailInfo.getHost());
                }
            }
            //系统通知配置
            if (StrTool.listNotNull(noticelist)) {
                Iterator<?> iter = noticelist.iterator();
                while (iter.hasNext()) {
                    ProNoticeInfo notice = (ProNoticeInfo) iter.next();
                    noticeMap.put(notice.getId(), notice.getTitle());
                }
            }
//            //用户来源
//            if (StrTool.listNotNull(usourcelist)) {
//                Iterator<?> iter = usourcelist.iterator();
//                while (iter.hasNext()) {
//                    UserSourceInfo usource = (UserSourceInfo) iter.next();
//                    usourceMap.put(usource.getId(), usource.getSourcename());
//                }
//            }
            //Radius配置
            if (StrTool.listNotNull(radProlist)) {
                Iterator<?> iter = radProlist.iterator();
                while (iter.hasNext()) {
                    RadProfileInfo radProInfo = (RadProfileInfo) iter.next();
                    radiusProMap.put(radProInfo.getProfileId(), radProInfo.getProfileName());
                }
            }
            //RADIUS配置属性
            if (StrTool.listNotNull(attrlist)) {
                Iterator<?> iter = attrlist.iterator();
                while (iter.hasNext()) {
                    RadAttrInfo attrInfo = (RadAttrInfo) iter.next();
                    radAttrMap.put(attrInfo.getAttrId(), attrInfo.getAttrName());
                }
            }
        } catch (Exception e) {
            logger.error("Load PubConfConfig Failed!", e);
        }
    }

    public static PubConfConfig loadPubConfConfig() {
        if (config != null) {
            return config;
        }
        synchronized (PubConfConfig.class) {
            if (config == null) {
                config = new PubConfConfig();
            }
            return config;
        }
    }

    /**
     * 根据KEY获取VALUE值
     * @Date in Feb 21, 2013,7:37:12 PM
     * @param key
     * @return
     */
    public static String getEmailValue(Integer key) {
        if (!StrTool.mapNotNull(emailMap)) {
            return "";
        }
        return (String) emailMap.get(key);
    }

    public static String getNoticeTitle(Integer key) {
        if (!StrTool.mapNotNull(noticeMap)) {
            return "";
        }
        return (String) noticeMap.get(key);
    }

    public static String getRadProValue(Integer key) {
        if (!StrTool.mapNotNull(radiusProMap)) {
            return "";
        }
        return (String) radiusProMap.get(key);
    }

    public static String getUsourceValue(Integer key) {
        if (!StrTool.mapNotNull(usourceMap)) {
            return "";
        }
        return (String) usourceMap.get(key);
    }

    public static String getRadAttrValue(String key) {
        if (!StrTool.mapNotNull(radAttrMap)) {
            return "";
        }
        return (String) radAttrMap.get(key);
    }

    /**
     * 获取邮件服务器是否为默认状态
     * 
     * @Date in Jun 8, 2013,10:36:45 AM
     * @return
     */
    public static boolean getDefEmail() {
        if (!StrTool.mapNotNull(emailMap)) {
            return false;
        }
        if (emailMap.size() == 1) {
            //判断是否是初始化脚本里面的默认邮件服务器
            Iterator<Integer> key = emailMap.keySet().iterator();
            String host = emailMap.get(key.next());
            if (StrTool.strEquals(host, "default")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 清空MAP
     * @Date in Feb 21, 2013,7:37:00 PM
     */
    public static void clear() {
        if (null != config) {
            emailMap.clear();
            radiusProMap.clear();
            radAttrMap.clear();
            noticeMap.clear();
            config = null;
        }
    }

    /**
     * 重新加载
     * @Date in Aug 17, 2013,10:45:03 AM
     */
    public synchronized static void reload() {
        clear();
        loadPubConfConfig();
    }

}
