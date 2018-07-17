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
import com.ft.otp.manager.confinfo.config.entity.UserConfInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户配置业务action
 *
 * @Date in Jul 28, 2013,2:25:45 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class UserConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -8700365186532686430L;

    // 公共配置服务接口
    private Logger logger = Logger.getLogger(UserConfAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");

    private UserConfInfo userConfInfo;
    private UserConfInfo oldUserConfInfo;
    
    public UserConfInfo getUserConfInfo() {
        return userConfInfo;
    }

    public void setUserConfInfo(UserConfInfo userConfInfo) {
        this.userConfInfo = userConfInfo;
    }

    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    /**
     * 获取配置信息
     */
    public String find() {
        List<?> configList = null;
        String oper = request.getParameter("oper");
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_USER);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            userConfInfo = UserConfInfo.getUserInfoList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (StrTool.strEquals(oper, "utknconf")) {
            return "utknconf";
        } else {
            return "udefpwdconf";
        }
    }

    public String init() {
        return null;
    }

    /**
     * 编辑配置信息
     */
    public String modify() {
        String oper = request.getParameter("oper");
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_USER);
            List<?> conList = confInfoServ.queryConfInfo(config, new PageArgument());
            UserConfInfo userInfo = UserConfInfo.getUserInfoList(conList);
            userInfo.setOper(oper);
            this.setOldUserConfInfo(userInfo);
            
            List<Object> confList = UserConfInfo.getListByCommInfo(userConfInfo, oper);
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

    public UserConfInfo getOldUserConfInfo() {
        return oldUserConfInfo;
    }

    public void setOldUserConfInfo(UserConfInfo oldUserConfInfo) {
        this.oldUserConfInfo = oldUserConfInfo;
    }

}
