/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.backend.action;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.AgentPubConfig;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.manager.authmgr.backend.entity.BackendInfo;
import com.ft.otp.manager.authmgr.backend.service.IBackendServ;
import com.ft.otp.util.json.JsonTool;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 后端认证业务action
 *
 * @Date in Jan 21, 2013,8:27:49 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class BackendAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 5395650014740624394L;
    // 公共配置服务接口
    private Logger logger = Logger.getLogger(BackendAction.class);

    private IBackendServ backendServ;
    private BackendInfo backendInfo;
    private BackendInfo oldBackendInfo;
    
    public BackendInfo getOldBackendInfo() {
		return oldBackendInfo;
	}

	public void setOldBackendInfo(BackendInfo oldBackendInfo) {
		this.oldBackendInfo = oldBackendInfo;
	}

	public IBackendServ getBackendServ() {
        return backendServ;
    }

    public void setBackendServ(IBackendServ backendServ) {
        this.backendServ = backendServ;
    }

    public BackendInfo getBackendInfo() {
        return backendInfo;
    }

    public void setBackendInfo(BackendInfo backendInfo) {
        this.backendInfo = backendInfo;
    }

    public BackendInfo getBackendAuthInfo() {
        if (!StrTool.objNotNull(backendInfo)) {
            backendInfo = new BackendInfo();
        }
        return backendInfo;
    }

    /**
     * 添加后端认证配置信息
     */
    public String add() {
        try {
            backendInfo.setEnabled(1);
            backendServ.addObj(backendInfo);

            AgentPubConfig.reload();
            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10010);

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_add_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_add_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 删除后端认证操作
     */
    public String delete() {
        Set<?> keySet = super.getDelIds("delIds");
        try {
            if (StrTool.setNotNull(keySet)) {
                backendServ.delObj(keySet);

                AgentPubConfig.reload();
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10010);

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_del_succ_tip"));
            }
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_del_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 查询后端认证实体信息
     */
    public String find() {
        String id = request.getParameter("backendid");
        BackendInfo backend = getBackendAuthInfo();
        try {
            backend.setId(Integer.parseInt(id));
            backend = (BackendInfo) backendServ.find(backend);
            if (!StrTool.objNotNull(backend)) {
                return init();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setBackendInfo(backend);

        return SUCC_EDIT;
    }

    /**
     * 后端认证初始化列表
     */
    public String init() {
        try {
            BackendInfo backend = getBackendAuthInfo();
            PageArgument pageArg = pageArgument(backend);
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
     * 获取分页信息
     * @Date in Jan 22, 2013,9:31:00 AM
     * @param sms
     * @return
     * @throws Exception
     */
    private PageArgument pageArgument(BackendInfo backend) throws Exception {
        int totalRow = 0;
        totalRow = backendServ.count(backend);
        PageArgument pageArg = getArgument(totalRow);

        return pageArg;
    }

    /**
     * 获取后端认证数据
     * 
     * @Date in Jan 21, 2013,9:17:20 PM
     * @param pageArg
     * @return
     */
    private List<?> query(PageArgument pageArg) {
        List<?> backendList = null;
        try {
            backendList = backendServ.query(backendInfo, pageArg);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return backendList;
    }

    /**
     * 编辑后端认证实体信息
     */
    public String modify() {
        try {
        	// Start,日志处理
        	BackendInfo backend = new BackendInfo();
        	backend.setId(backendInfo.getId());
        	backend = (BackendInfo) backendServ.find(backend);
        	this.setOldBackendInfo(backend);
        	// End,日志处理
        	
            backendServ.updateObj(backendInfo);

            AgentPubConfig.reload();
            SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10010);

            outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_edit_succ_tip"));
        } catch (Exception e) {
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_edit_error_tip"));
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public String page() {
        return null;
    }

    /**
     * 查看后端认证实体信息
     */
    public String view() {
        BackendInfo backend = null;
        try {
            backend = (BackendInfo) backendServ.find(backendInfo);
            if (!StrTool.objNotNull(backend)) {
                return init();
            }
            backend.setDomainStr(DomainConfig.getValue(backend.getDomainid()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        this.setBackendInfo(backend);

        return SUCC_VIEW;
    }

    /** 
     * 验证后端认证配置名称是否已经存在
     * @Date in Jan 22, 2013,8:55:15 PM
     * @return
     */
    public String checkBackendName() {
        BackendInfo backend = new BackendInfo();
        try {
            String backendname = backendInfo.getBackendname();
            if (!StrTool.strNotNull(backendname)) {
                return null;
            }
            backendname = MessyCodeCheck.iso885901ForUTF8(backendname);
            backend.setBackendname(backendname);
            backend = (BackendInfo) backendServ.find(backend);
            if (StrTool.objNotNull(backend)) {
                super.setResponseWrite("false");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 验证host, port, domainid联合唯一是否已经存在
     * @Date in Jan 23, 2013,3:58:52 PM
     * @return
     */
    public String checkUKIsExist() {
        BackendInfo backend = new BackendInfo();
        String host = request.getParameter("host");
        String port = request.getParameter("port");
        String domainid = request.getParameter("domainid");
        if (!StrTool.strNotNull(host) || !StrTool.strNotNull(port) || !StrTool.strNotNull(domainid)) {
            outPutOperResult(Constant.alert_succ, "success");
            return null;
        }
        try {
            backend.setHost(host);
            backend.setPort(Integer.parseInt(port));
            backend.setDomainid(Integer.parseInt(domainid));
            backend = (BackendInfo) backendServ.queryUKIsExist(backend);
            if (StrTool.objNotNull(backend)) {
                outPutOperResult(Constant.alert_error, "error");
            } else {
                outPutOperResult(Constant.alert_succ, "success");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 启用、禁用后端认证
     */
    public String enableAuth() throws BaseException {
        try {
            backendInfo.setBackendname(AgentPubConfig.getBackendValue(backendInfo.getId()));
            if (backendInfo.getEnabled() == NumConstant.common_number_0) {
                backendServ.updateEnabled(backendInfo, NumConstant.common_number_1);

                AgentPubConfig.reload();
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10010);

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_enable_succ_tip"));
            } else {
                backendServ.updateEnabled(backendInfo, NumConstant.common_number_0);

                AgentPubConfig.reload();
                SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10010);

                outPutOperResult(Constant.alert_succ, Language.getLangStr(request, "common_disabled_succ_tip"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "common_opera_error_tip"));
        }

        return null;
    }
    
    /**
     * 验证一个域下只能添加一个后端认证节点数
     * @return
     */
    public String checkBackNodes() {
        //已经存在的服务器节点数
        int backNode;
        BackendInfo backendInfo = new BackendInfo();
        try {
        	backNode = backendServ.count(backendInfo);
            if (backNode == NumConstant.common_number_0) {
                outPutOperResult(Constant.alert_succ, "success");
            } else {
                outPutOperResult(Constant.alert_warn, Language.getLangStr(request, "auth_bk_vd_add_one"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
