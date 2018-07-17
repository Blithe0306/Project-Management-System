package com.ft.otp.manager.confinfo.radius.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.radius.entity.RadProfileInfo;
import com.ft.otp.manager.confinfo.radius.service.IRadProfileServ;
import com.ft.otp.manager.user.userinfo.entity.UserInfo;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * RADIUS配置业务处理action
 * 
 * @Date in Oct 29, 2012,4:23:39 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadProfileAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(RadProfileAction.class);
    private IRadProfileServ radProfileServ;
    //用户服务接口
    private IUserInfoServ userInfoServ = (IUserInfoServ) AppContextMgr.getObject("userInfoServ");
    RadProfileInfo radProfileInfo;

    public IRadProfileServ getRadProfileServ() {
        return radProfileServ;
    }

    public void setRadProfileServ(IRadProfileServ radProfileServ) {
        this.radProfileServ = radProfileServ;
    }

    public RadProfileInfo getRadProfileInfo() {
        return radProfileInfo;
    }

    public void setRadProfileInfo(RadProfileInfo radProfileInfo) {
        this.radProfileInfo = radProfileInfo;
    }

    private RadProfileInfo getRadProInfo() {
        if (!StrTool.objNotNull(radProfileInfo)) {
            radProfileInfo = new RadProfileInfo();
        }
        return radProfileInfo;
    }

    /**
     * RADIUS配置列表初始化
     */
    public String init() {
        try {
            RadProfileInfo radProfileInfo = getRadProInfo();
            PageArgument pageArg = pageArgument(radProfileInfo);
            pageArg.setPageSize(getPagesize());
            pageArg.setCurPage(getPage());
            List<?> resultList = query(pageArg);
            String json = JsonTool.getJsonFromList(pageArg.getTotalRow(), resultList, null);
            setResponseWrite(json);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询RADIUS列表
     * 
     * @Date in Oct 30, 2012,11:21:05 AM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        List<?> radProfileList = null;
        try {
            RadProfileInfo radProfileInfo = getRadProInfo();
            radProfileList = radProfileServ.query(radProfileInfo, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return radProfileList;
    }

    /**
     * 获取封装分页信息
     * 
     * @Date in Oct 29, 2012,4:54:55 PM
     * @return
     * @throws Exception
     */
    private PageArgument pageArgument(RadProfileInfo radProfileInfo) throws Exception {
        int totalRow = 0;
        totalRow = radProfileServ.count(radProfileInfo);
        PageArgument pageArg = getArgument(totalRow);
        return pageArg;
    }

    /**
     * 添加RADIUS配置
     */
    public String add() {
        try {
            if (StrTool.objNotNull(radProfileInfo)) {
                radProfileServ.addObj(radProfileInfo);
                
                // Start,取出添加成功后的ID
                RadProfileInfo radProfile = new RadProfileInfo();
                radProfile.setProfileName(radProfileInfo.getProfileName());
                radProfile = (RadProfileInfo) radProfileServ.find(radProfile);
                // End,取出添加成功后的ID
                
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip")+ ","
                        + radProfile.getProfileId());
            } else {
                return init();
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
            logger.error(e.getMessage(), e);
        }
        PubConfConfig.clear();
        PubConfConfig.loadPubConfConfig();
        return null;
    }

    /**
     * 验证RADIUS配置名称是否存在
     * 
     * @Date in Oct 30, 2012,11:52:28 AM
     * @return
     */
    public String findRPFNameisExist() {
        RadProfileInfo radProfile = new RadProfileInfo();
        try {
            String radProfileName = radProfileInfo.getProfileName();
            if (!StrTool.strNotNull(radProfileName)) {
                return null;
            }
            radProfileName = MessyCodeCheck.iso885901ForUTF8(radProfileName);
            radProfile.setProfileName(radProfileName);
            radProfile = (RadProfileInfo) radProfileServ.find(radProfile);
            if (StrTool.objNotNull(radProfile)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 删除Radius配置
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delIds");
        Set<String> newSet = new HashSet<String>();
        try {
            if (StrTool.setNotNull(keySet)) {
            	Iterator<?> iter = keySet.iterator();
            	List<?> agenList = new ArrayList<Object>();
            	UserInfo userInfo = null;
                String noDelIps = "";
            	while (iter.hasNext()) {
                    String keyId[] = ((String) iter.next()).split(":");
                    userInfo = new UserInfo();
                    userInfo.setRadProfileId(StrTool.parseInt(keyId[0]));
                    agenList = userInfoServ.selectUserToRad(userInfo);

                    if (StrTool.listNotNull(agenList)) {
                        noDelIps += keyId[1] + ",";
                    } else {
                        newSet.add(keyId[0]);
                    }
                }
            	if (StrTool.setNotNull(newSet)) {
                    radProfileServ.delObj(newSet);
                }
            	if (StrTool.strNotNull(noDelIps)) {
                    outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "radius_name_sel")
                    		+" "
                            + noDelIps.substring(0, noDelIps.length() - 1)
                            +" "
                            + Language.getLangStr(request, "auth_agent_sel_already_ser"));
                } else {
                    outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
                }
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查找RADIUS配置信息
     */
    public String find() {
        String profileId = request.getParameter("profileId");
        RadProfileInfo radProfile = getRadProInfo();
        try {
            if (StrTool.strNotNull(profileId)) {
                radProfile.setProfileId(Integer.parseInt(profileId));
            }
            radProfile = (RadProfileInfo) radProfileServ.find(radProfile);
            if (!StrTool.objNotNull(radProfile)) {
                return init();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setRadProfileInfo(radProfile);
        return "edit";
    }

    /**
     * 编辑更新RADIUS配置
     */
    public String modify() {
        try {
            if (StrTool.objNotNull(radProfileInfo)) {
                radProfileServ.updateObj(radProfileInfo);
                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
            } else {
                outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
                return init();
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 查询Radius配置名称
     * 
     * @Date in Nov 2, 2012,11:33:58 AM
     * @return
     */
    public String findProfileName() {
        String profileId = request.getParameter("profileId");
        RadProfileInfo radProfile = getRadProInfo();
        try {
            if (StrTool.strNotNull(profileId)) {
                radProfile.setProfileId(Integer.parseInt(profileId));
            }
            radProfile = (RadProfileInfo) radProfileServ.find(radProfile);
            if (StrTool.objNotNull(radProfile)) {
                outPutOperResult(Constant.alert_succ, radProfile.getProfileName());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.action.IBaseAction#page()
     */
    public String page() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ft.otp.base.action.IBaseAction#view()
     */
    public String view() {
        return null;
    }

}
