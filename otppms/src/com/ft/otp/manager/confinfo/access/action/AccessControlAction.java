/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.access.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.config.TrustIpConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 访问控制策略业务action
 *
 * @Date in Dec 27, 2012,5:35:57 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AccessControlAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 5722381778155186173L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(AccessControlAction.class);

    private IAccessConServ accessConServ;
    private AccessInfo accessInfo;

    public IAccessConServ getAccessConServ() {
        return accessConServ;
    }

    public void setAccessConServ(IAccessConServ accessConServ) {
        this.accessConServ = accessConServ;
    }

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }

    private AccessInfo getAccInfo() {
        if (!StrTool.objNotNull(accessInfo)) {
            accessInfo = new AccessInfo();
        }
        return accessInfo;
    }

    /**
     * 访问控制IP添加
     */
    public String add() {
        try {
            String trustIPList = request.getParameter("trustIPList");
            AccessInfo aInfo = new AccessInfo();
            aInfo.setSystype(0);
            if (!StrTool.strNotNull(trustIPList)) {
                accessConServ.delObj(aInfo);
                PubConfConfig.reload();
                TrustIpConfig.reload();
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
                return null;
            }
            String iplistattr[] = (trustIPList.substring(0, trustIPList.length() - 1)).split(",");
            //先将管理中心信任IP删除
          
            accessConServ.delObj(aInfo);
            AccessInfo access = null;
            for (int i = 0; i < iplistattr.length; i++) {
                access = new AccessInfo();
                if (iplistattr[i].indexOf("-") >= 0) {
                    String ipattr[] = iplistattr[i].split("-");
                    if (StrTool.arrNotNull(ipattr)) {
                        access.setStartip(ipattr[0]);
                        access.setEndip(ipattr[1]);
                    }
                } else {
                    access.setStartip(iplistattr[i]);
                }
                accessConServ.addObj(access);
            }
 
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
            logger.error(e.getMessage(), e);
        }
        PubConfConfig.reload();
        TrustIpConfig.reload();
        outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
        return null;
    }

    /**
     * 删除访问控制策略IP地址
     */
    public String delete() {
        String selIdStr = request.getParameter("selIdStr");
        String[] delIds = (selIdStr.substring(0, selIdStr.length() - 1)).split(",");
        try {
            if (delIds.length > 0) {
                int[] ids = new int[delIds.length];
                for (int i = 0; i < delIds.length; i++) {
                    ids[i] = Integer.parseInt(delIds[i]);
                }
                AccessInfo access = new AccessInfo();
                access.setBatchIdsInt(ids);
                accessConServ.delObj(access);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_move_error_tip"));
        }
        TrustIpConfig.reload();
        outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_move_succ_tip"));
        return null;
    }

    /**
     * 查询获取访问控制IP数据
     */
    public String find() {
        return null;
    }

    /**
     * 访问控制策略配置初始化
     */
    public String init() {
        PageArgument pageArg = new PageArgument();
        try {
//            String localIP = InetAddress.getLocalHost().getHostAddress();
            String localIP = IpTool.getIpAddr(request);
            AccessInfo access = getAccInfo();
            List<?> resultList = query(pageArg);
            String accessIPStr = getAccessIPStr(resultList);
            request.setAttribute("accessIPStr", accessIPStr);
            request.setAttribute("localIP", localIP);
            
            request.setAttribute("trustipenable", ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_CENTER,ConfConstant.ENABLED_TRUSTIP_CHECK));
            this.setAccessInfo(access);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return SUCCESS;
    }

    /**
     * 获取封装要展示的IP列表
     * @Date in Dec 31, 2012,10:22:09 AM
     * @param resultList
     * @return
     */
    private String getAccessIPStr(List<?> resultList) {
        StringBuffer sbf = new StringBuffer();
        if (StrTool.listNotNull(resultList)) {
            for (int i = 0; i < resultList.size(); i++) {
                AccessInfo accInfo = (AccessInfo) resultList.get(i);
                if (StrTool.strNotNull(accInfo.getStartip())) {
                    if (StrTool.strNotNull(accInfo.getEndip())) {
                        sbf.append(accInfo.getId() + "," + accInfo.getStartip() + "-" + accInfo.getEndip());
                    } else {
                        sbf.append(accInfo.getId() + "," + accInfo.getStartip());
                    }
                    sbf.append(":");
                }
            }
        }
        return sbf.toString();
    }

    /**
     * 获取访问控制IP数据
     * @Date in Dec 27, 2012,9:26:51 PM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        List<?> accessList = null;
        try {
            accessList = accessConServ.query(accessInfo, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return accessList;
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
     * 获取允许访问IP列串
     * @Date in Dec 31, 2012,2:17:37 PM
     * @return
     */
    public String getIPStr() {
        PageArgument pageArg = new PageArgument();
        String accessIPStr = "";
        try {
            List<?> resultList = query(pageArg);
            accessIPStr = getAccessIPStr(resultList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        outPutOperResult(Constant.alert_succ, accessIPStr);
        return null;
    }

    /**
     * 校验输入IP是否通过校验
     * @Date in Jan 4, 2013,3:47:31 PM
     * @return
     */    
    public String findIPIsExist() {
        String returnVal = "SUCCESS";
        try {
            String allowIP = request.getParameter("allowIP");
            String allowIPList = request.getParameter("allowIPList");
            
            if (!StrTool.strNotNull(allowIPList)) {
                outPutOperResult(Constant.alert_succ, returnVal);
                return null;
            }
            
            String iplistattr[] = (allowIPList.substring(0, allowIPList.length() - 1)).split(",");
            for (int i = 0; i < iplistattr.length; i++) {
                if (StrTool.strEquals(allowIP, iplistattr[i])) {
                    returnVal = Language.getLangStr(request, "trustip_ip_already_exists");
                    break;
                } else {
                    if (iplistattr[i].indexOf("-") >= 0) {
                        String ipattr[] = iplistattr[i].split("-");
                        if (StrTool.arrNotNull(ipattr)) {
                            if (betweenIP(ipattr[0], ipattr[1], allowIP)) {
                                returnVal = Language.getLangStr(request, "trustip_ip_exist_in") + ipattr[0] + "-"
                                + ipattr[1] + Language.getLangStr(request, "trustip_ip_duan");
                                break;
                            }
                        }
                    }
                }
            }
            outPutOperResult(Constant.alert_succ, returnVal);
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 验证开始IP是否已存在
     * @Date in Dec 31, 2012,3:58:17 PM
     * @return
     */
    public String findSIPIsExist() {
        String returnVal = "SUCCESS";
        try {
            String startip = request.getParameter("startIP");
            String allowIPList = request.getParameter("allowIPList");
            
            if (!StrTool.strNotNull(allowIPList)) {
                outPutOperResult(Constant.alert_succ, returnVal);
                return null;
            }
            
            String iplistattr[] = (allowIPList.substring(0, allowIPList.length() - 1)).split(",");
            for (int i = 0; i < iplistattr.length; i++) {
                if (StrTool.strEquals(startip, iplistattr[i])) {
                    returnVal = Language.getLangStr(request, "startip_already_exists");
                    break;
                } else {
                    if (iplistattr[i].indexOf("-") >= 0) {
                        String ipattr[] = iplistattr[i].split("-");
                        if (StrTool.arrNotNull(ipattr)) {
                            if (betweenIP(ipattr[0], ipattr[1], startip)) {
                                returnVal = Language.getLangStr(request, "startip_ip_exist_in") + ipattr[0] + "-"
                                + ipattr[1] + Language.getLangStr(request, "trustip_ip_duan");
                                break;
                            }
                        }
                    }
                }
            }

            outPutOperResult(Constant.alert_succ, returnVal);
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }
   
    /**
     * 验证结束IP是否已存在
     * @Date in Dec 31, 2012,4:07:55 PM
     * @return
     */
    public String findEIPIsExist() {
        String returnVal = "SUCCESS";
        try {
            String endip = request.getParameter("endIP");
            String allowIPList = request.getParameter("allowIPList");
            
            if (!StrTool.strNotNull(allowIPList)) {
                outPutOperResult(Constant.alert_succ, returnVal);
                return null;
            }
            
            String iplistattr[] = (allowIPList.substring(0, allowIPList.length() - 1)).split(",");
            for (int i = 0; i < iplistattr.length; i++) {
                if (StrTool.strEquals(endip, iplistattr[i])) {
                    returnVal = Language.getLangStr(request, "endtip_already_exists");
                    break;
                } else {
                    if (iplistattr[i].indexOf("-") >= 0) {
                        String ipattr[] = iplistattr[i].split("-");
                        if (StrTool.arrNotNull(ipattr)) {
                            if (betweenIP(ipattr[0], ipattr[1], endip)) {
                                returnVal = Language.getLangStr(request, "endtip_ip_exist_in") + ipattr[0] + "-"
                                + ipattr[1] + Language.getLangStr(request, "trustip_ip_duan");
                                break;
                            }
                        }
                    }
                }
            }

            outPutOperResult(Constant.alert_succ, returnVal);
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }
   
    /**
     * 校验起始IP和结束IP是否已存在
     * @Date in Jan 4, 2013,8:15:24 PM
     * @return
     */
    public String checkIPDExist() {
        String returnVal = "SUCCESS";
        try {
            String startip = request.getParameter("startip");
            String endip = request.getParameter("endip");
            String allowIPList = request.getParameter("allowIPList");
            
            if (!StrTool.strNotNull(allowIPList)) {
                outPutOperResult(Constant.alert_succ, returnVal);
                return null;
            }
            
            String iplistattr[] = (allowIPList.substring(0, allowIPList.length() - 1)).split(",");
            for (int i = 0; i < iplistattr.length; i++) {
                if (iplistattr[i].indexOf("-") >= 0) {
                    String ipattr[] = iplistattr[i].split("-");
                    if (StrTool.arrNotNull(ipattr)) {
                        if (betweenIP(ipattr[0], ipattr[1], startip, endip)) {
                           returnVal = Language.getLangStr(request, "ip_duan_exist_in") + ipattr[0] + "-"
                           + ipattr[1] + Language.getLangStr(request, "trustip_ip_duan");
                           break;
                        } else if(betweenIP(ipattr[0], ipattr[1], startip)) {
                            returnVal = Language.getLangStr(request, "startip_ip_exist_in") + ipattr[0] + "-"
                            + ipattr[1] + Language.getLangStr(request, "trustip_ip_duan");
                            break;
                        } else if(betweenIP(ipattr[0], ipattr[1], endip)) {
                            returnVal = Language.getLangStr(request, "endtip_ip_exist_in") + ipattr[0] + "-"
                            + ipattr[1] + Language.getLangStr(request, "trustip_ip_duan");
                            break;
                        }
                    } 
                } else {
                    if (betweenIP(iplistattr[i], null, startip, endip)) {
                        returnVal = Language.getLangStr(request, "ip_duan_already_ip_exists") + iplistattr[i] + Language.getLangStr(request, "ip_duan_zoom_in");
                        break;
                    }
                }
            }
            outPutOperResult(Constant.alert_succ, returnVal);
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 验证IP是否存在一个IP段
     * @Date in Jan 4, 2013,4:51:49 PM
     * @param start
     * @param end
     * @param current
     * @return
     */
    public boolean betweenIP(String start, String end, String current) {
        boolean result = false;

        start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        
        if (StrTool.strNotNull(end)) {
            end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
            end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
            end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
            end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        }

        current = current.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        current = current.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        current = current.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        current = current.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        
        if ((current.compareTo(start) >= 0) && (current.compareTo(end) <= 0)) {
            result = true;
        }
        return result;
    }

    /**
     * 验证开始IP和结束IP之间是否包含已存在IP地址
     * @Date in Jan 4, 2013,9:05:18 PM
     * @param start
     * @param end
     * @param startip
     * @param endip
     * @return
     */
    public boolean betweenIP(String start, String end, String startip, String endip) {
        boolean result = false;

        start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        start = start.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        start = start.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

        if (StrTool.strNotNull(end)) {
            end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
            end = end.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
            end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
            end = end.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        }

        startip = startip.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        startip = startip.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        startip = startip.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        startip = startip.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

        endip = endip.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        endip = endip.replaceAll("(^|\\.)(\\d)(\\.|$)", "$100$2$3");
        endip = endip.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");
        endip = endip.replaceAll("(^|\\.)(\\d{2})(\\.|$)", "$10$2$3");

        if (StrTool.strNotNull(end)) {
            if ((startip.compareTo(start) < 0) && (endip.compareTo(end) > 0)) {
                result = true;
            }
        } else {
            if ((start.compareTo(startip) >= 0) && (start.compareTo(endip) <= 0)) {
                result = true;
            }
        }
        return result;
    }
    
    
 
}
