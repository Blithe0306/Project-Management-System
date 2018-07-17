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
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.CommonConfInfo;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.manager.task.log.LogTaskAide;
import com.ft.otp.util.tool.StrTool;

/**
 * 系统公共配置-其它公共配置业务action
 *
 * @Date in Jul 27, 2013,9:34:23 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class CommConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 6363919979702558075L;

    // 公共配置服务接口
    private Logger logger = Logger.getLogger(CommConfAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");

    // 定时删除日志帮助类
    private LogTaskAide logTaskAide = new LogTaskAide();
    private CommonConfInfo commInfo;
    private CommonConfInfo oldCommInfo; //记录日志
    
    public CommonConfInfo getCommInfo() {
        return commInfo;
    }

    public void setCommInfo(CommonConfInfo commInfo) {
        this.commInfo = commInfo;
    }
    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }
    
    /**
     * 公共配置初始化查询
     */
    public String find() {
        List<?> configList = null;
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_COMMON);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            commInfo = CommonConfInfo.getCommInfoList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_COMMON);
            List<?> conList = confInfoServ.queryConfInfo(config, new PageArgument());
            CommonConfInfo commonInfo = CommonConfInfo.getCommInfoList(conList);
            this.setOldCommInfo(commonInfo);
            
            List<Object> confList = CommonConfInfo.getListByCommInfo(commInfo);
            confInfoServ.batchUpdateConf(confList);
                        
            //重新加载配置缓存
            ConfConfig.reLoad();
            // 重新加载定时删除日志任务  此处要放到重新加载配置缓存之后 上边的modify方法是没有重新加载缓存的
            logTaskAide.addLogTask(NumConstant.common_number_1);
            
            String currLang = (String) request.getSession(true).getAttribute(Constant.LANGUAGE_SESSION_KEY);
            String newLang = ConfConfig.getConfValue(ConfConstant.CONF_TYPE_COMMON + "_" + ConfConstant.DEFAULT_SYSTEM_LANGUAGE);
            if (!StrTool.strEquals(currLang, newLang)) {
                request.getSession(true).setAttribute(Constant.SET_NEW_LANGUAGE, "yes");
            } else {
                request.getSession(true).setAttribute(Constant.SET_NEW_LANGUAGE, "no");
            }
            
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

    public CommonConfInfo getOldCommInfo() {
        return oldCommInfo;
    }

    public void setOldCommInfo(CommonConfInfo oldCommInfo) {
        this.oldCommInfo = oldCommInfo;
    }

}
