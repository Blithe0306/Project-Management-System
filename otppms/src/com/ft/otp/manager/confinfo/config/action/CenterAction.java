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
import com.ft.otp.common.soap.WebServiceFactory;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.config.entity.CenterConfInfo;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理中心配置业务Action
 * @Date in Nov 15, 2012,5:16:00 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class CenterAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -8900169465924479784L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(CenterAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");

    // 定时删除日志帮助类
    //private LogTaskAide logTaskAide = new LogTaskAide();

    private CenterConfInfo centerInfo;
    private CenterConfInfo oldcenterInfo;

    public CenterConfInfo getCenterInfo() {
        return centerInfo;
    }

    public void setCenterInfo(CenterConfInfo centerInfo) {
        this.centerInfo = centerInfo;
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
     * 管理中心配置初始化配置
     */
    public String find() {
        List<?> configList = null;
        String oper = request.getParameter("oper");
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_CENTER);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            centerInfo = CenterConfInfo.getCenterInfoByList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (StrTool.strEquals(oper, "adminconf")) {
            //管理员策略配置
            return "center";
        } else if (StrTool.strEquals(oper, "authsel")) {
            //认证服务器选择配置
            return "authser";
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.base.action.IBaseAction#init()
     */
    public String init() {
        return null;
    }

    /**
     * 管理中心配置修改操作
     */
    public String modify() {
        try {
            String oper = request.getParameter("oper");
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_CENTER);
            List<?> configList = confInfoServ.queryConfInfo(config, new PageArgument());
            CenterConfInfo cenConfInfo = CenterConfInfo.getCenterInfoByList(configList);
            cenConfInfo.setOper(oper);
            this.setOldcenterInfo(cenConfInfo);

            List<Object> confList = CenterConfInfo.getListByCenterInfo(centerInfo, oper);
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

    /**
     * 重新连接主、备服务器SOAP请求，重新加载配置
     * 分开处理
     * 
     * TBM
     * @Date in Jul 6, 2013,4:30:14 PM
     * @return
     */
    public String reloadConf() {
        //重新创建SOAP请求
        WebServiceFactory.loadWebServiceFactory();

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
     * @return the oldcenterInfo
     */
    public CenterConfInfo getOldcenterInfo() {
        return oldcenterInfo;
    }

    /**
     * @param oldcenterInfo the oldcenterInfo to set
     */
    public void setOldcenterInfo(CenterConfInfo oldcenterInfo) {
        this.oldcenterInfo = oldcenterInfo;
    }

}
