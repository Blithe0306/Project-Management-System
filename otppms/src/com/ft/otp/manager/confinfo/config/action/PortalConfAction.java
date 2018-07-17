/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.entity.PortalInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 自助门户配置业务Action
 *
 * @Date in Dec 25, 2012,9:52:33 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class PortalConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 6826328685965687848L;

    // 公共配置服务接口
    private Logger logger = Logger.getLogger(PortalConfAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    
    PortalInfo portalInfo;
    PortalInfo oldPortalInfo;

    public PortalInfo getPortalInfo() {
        return portalInfo;
    }

    public void setPortalInfo(PortalInfo portalInfo) {
        this.portalInfo = portalInfo;
    }
    
    
    
    /**
     * @return the oldPortalInfo
     */
    public PortalInfo getOldPortalInfo() {
        return oldPortalInfo;
    }

    /**
     * @param oldPortalInfo the oldPortalInfo to set
     */
    public void setOldPortalInfo(PortalInfo oldPortalInfo) {
        this.oldPortalInfo = oldPortalInfo;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#add()
     */
    public String add() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#delete()
     */
    public String delete() {
        return null;
    }

    /**
     * 查找自助门户配置信息
     */
    public String find() {
        List<?> configList = null;
        String oper = request.getParameter("oper");
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.PORTAL_CONFIG);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            portalInfo = PortalInfo.getPortalInfoByList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        
        if (StrTool.strEquals(oper, "initpwd")) {
            return "initpwd";
        }
        return SUCCESS;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#init()
     */
    public String init() {
        return null;
    }

    /**
     * 自助门户配置修改
     */
    public String modify() {
        String oper = request.getParameter("oper");
        try {
        	
        	// Start,用于记录日志
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_PORTAL);
            List<?> conList = confInfoServ.queryConfInfo(config, new PageArgument());
            PortalInfo pInfo = PortalInfo.getPortalInfoByList(conList);
            pInfo.setOper(oper);
            this.setOldPortalInfo(pInfo);
            // End,用于记录日志
            
            List<Object> confList = PortalInfo.getListByPortalInfo(portalInfo, oper);
            confInfoServ.batchUpdateConf(confList);
            
            //重新加载配置缓存
            ConfConfig.reLoad();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_save_error_tip"));
        }
        outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_save_succ_tip"));
        return null;
    }
    
    
    /**
     * 获取用户默认密码
     */
    public String defaultPwd() {
    	//获取用户默认秘密
    	String defaulrPwd= ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_USER, ConfConstant.DEFAULT_USER_PWD);
        outPutOperResult(Constant.alert_succ, defaulrPwd);
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

}
