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
import com.ft.otp.manager.confinfo.config.entity.AuthConfInfo;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证基本配置业务action
 *
 * @Date in Jul 27, 2013,11:31:04 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class AuthConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 6302111162171474107L;

    // 公共配置服务接口
    private Logger logger = Logger.getLogger(AuthConfAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");

    private AuthConfInfo authConfInfo;
    private AuthConfInfo oldAuthConfInfo;
    
    public AuthConfInfo getAuthConfInfo() {
        return authConfInfo;
    }

    public void setAuthConfInfo(AuthConfInfo authConfInfo) {
        this.authConfInfo = authConfInfo;
    }
    
    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    /**
     * 获取认证配置实体信息
     */
    public String find() {
        List<?> configList = null;
        String oper = request.getParameter("oper");
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_AUTH);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            authConfInfo = AuthConfInfo.getAuthInfoByList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (StrTool.strEquals(oper, "initpeap")) {
            return "initpeap";
        }
        return SUCCESS;
    }
    
    public String init() {
        return null;
    }

    /**
     * 编辑配置信息
     */
    public String modify() {
        try {
        	String oper = request.getParameter("oper");
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_AUTH);
            List<?> conList = confInfoServ.queryConfInfo(config, new PageArgument());
            AuthConfInfo authInfo = AuthConfInfo.getAuthInfoByList(conList);
            authInfo.setOper(oper);
            this.setOldAuthConfInfo(authInfo);
            
            List<Object> confList = AuthConfInfo.getListByAuthInfo(authConfInfo,oper);
            confInfoServ.batchUpdateConf(confList);
            
            //重新加载配置缓存
            ConfConfig.reLoad();
            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
        }
        return null;
    }

    public String page() {
        return null;
    }

    public String view() {
        return null;
    }

    public AuthConfInfo getOldAuthConfInfo() {
        return oldAuthConfInfo;
    }

    public void setOldAuthConfInfo(AuthConfInfo oldAuthConfInfo) {
        this.oldAuthConfInfo = oldAuthConfInfo;
    }

}
