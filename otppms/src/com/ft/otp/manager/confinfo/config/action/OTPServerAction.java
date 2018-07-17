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
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.entity.OTPServerConfInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;

/**
 * OTP Server 配置业务操作action
 *
 * @Date in Mar 11, 2013,4:24:07 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class OTPServerAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -939886900230374514L;
    // 日志记录接口
    private Logger logger = Logger.getLogger(OTPServerAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    
    OTPServerConfInfo serverInfo = null;
    OTPServerConfInfo oldServerConfInfo = new OTPServerConfInfo();

    public OTPServerConfInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(OTPServerConfInfo serverInfo) {
        this.serverInfo = serverInfo;
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
    * 查找OTP Server配置
    */
    public String find() {
        List<?> configList = null;
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.OTPSERVER_CONFIG);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            serverInfo = OTPServerConfInfo.getOTPServerInfoByList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
     * 编辑OTP Server配置
     */
    public String modify() {
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.OTPSERVER_CONFIG);
            List<?> configList = confInfoServ.queryConfInfo(config, new PageArgument());
            OTPServerConfInfo oldserverInfo = OTPServerConfInfo.getOTPServerInfoByList(configList);
            this.setOldServerConfInfo(oldserverInfo);
            
            List<Object> confList = OTPServerConfInfo.getListByOTPServerInfo(serverInfo);
            confInfoServ.batchUpdateConf(confList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "otpserconf_save_err"));
        }
        outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "otpserconf_save_succ"));
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
     * @return the oldServerConfInfo
     */
    public OTPServerConfInfo getOldServerConfInfo() {
        return oldServerConfInfo;
    }

    /**
     * @param oldServerConfInfo the oldServerConfInfo to set
     */
    public void setOldServerConfInfo(OTPServerConfInfo oldServerConfInfo) {
        this.oldServerConfInfo = oldServerConfInfo;
    }

}
