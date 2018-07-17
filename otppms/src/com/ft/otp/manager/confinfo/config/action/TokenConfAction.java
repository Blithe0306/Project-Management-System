/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.action;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.ft.otp.manager.confinfo.config.entity.TokenConfInfo;
import com.ft.otp.manager.confinfo.config.service.IConfigInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌配置业务action
 *
 * @Date in Jul 28, 2013,5:29:53 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class TokenConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -6645136065902063778L;

    // 公共配置服务接口
    private Logger logger = Logger.getLogger(TokenConfAction.class);
    // 公共配置服务接口
    private IConfigInfoServ confInfoServ = (IConfigInfoServ) AppContextMgr.getObject("confInfoServ");
    
    private TokenConfInfo tokenConfInfo;
    private TokenConfInfo oldTokenConfInfo;
    
    public TokenConfInfo getTokenConfInfo() {
        return tokenConfInfo;
    }

    public void setTokenConfInfo(TokenConfInfo tokenConfInfo) {
        this.tokenConfInfo = tokenConfInfo;
    }
    
    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    /**
     * 查询令牌配置信息
     */
    public String find() {
        List<?> configList = null;
        String oper = request.getParameter("oper");
        try {
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_TOKEN);
            configList = confInfoServ.queryConfInfo(config, new PageArgument());
            tokenConfInfo = TokenConfInfo.getTknconfInfoList(configList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (StrTool.strEquals(oper, "softtkn")) {
            return "softtkn";
        } else if(StrTool.strEquals(oper, "mobiletkn")){
            String ip = tokenConfInfo.getIp();
            if (!StrTool.strNotNull(ip)) {
                tokenConfInfo.setIp(getLocalIp());
            }
            String port = tokenConfInfo.getPort();
            if ((!StrTool.strNotNull(ip)) && (!StrTool.strNotNull(port))) {
                tokenConfInfo.setPort(Integer.toString(request.getLocalPort()));
            }
            String protocol = tokenConfInfo.getProtocol();
            if (!StrTool.strNotNull(protocol)) {
                tokenConfInfo.setProtocol("http");
            }
            String path = tokenConfInfo.getPath();
            if (!StrTool.strNotNull(path)) {
                String p = request.getContextPath();
                if (p.startsWith("/")) {
                    p = p.substring(1, p.length());
                }
                tokenConfInfo.setPath(p + Constant.MOBILE_DIST);
            }
            this.setTokenConfInfo(tokenConfInfo);
            return "mobiletkn";
        } else if(StrTool.strEquals(oper, "smstkn")){
            return "smstkn";
        } else if(StrTool.strEquals(oper, "emeypin")){
            return "emeypin";
        }
        return null;
    }

    public String init() {
        return null;
    }
    
    /**
     * 获取本地IP
     * @author LXH
     * @date Dec 8, 2014 4:23:57 PM
     * @return
     */
    public String getLocalIp(){
    	String localIp = request.getLocalAddr();
    	try {
    		if(!StrTool.objNotNull(localIp) || 
    				StrTool.strEquals("0.0.0.0", localIp)){
    			// 取得本机IP地址
    			localIp = InetAddress.getLocalHost().getHostAddress();
    		}
    		return localIp;
        }catch(UnknownHostException e){
          e.printStackTrace();
        }
        return null;
    }

    /**
     * 编辑令牌配置信息
     */
    public String modify() {
        try {
            String oper = request.getParameter("oper");
            ConfigInfo config = new ConfigInfo();
            config.setConftype(ConfConstant.CONF_TYPE_TOKEN);
            List<?> conList = confInfoServ.queryConfInfo(config, new PageArgument());
            TokenConfInfo tknInfo = TokenConfInfo.getTknconfInfoList(conList);
            tknInfo.setOper(oper);
            this.setOldTokenConfInfo(tknInfo);
            
            List<Object> confList = TokenConfInfo.getListByTknconfInfo(tokenConfInfo, oper);
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

    public TokenConfInfo getOldTokenConfInfo() {
        return oldTokenConfInfo;
    }

    public void setOldTokenConfInfo(TokenConfInfo oldTokenConfInfo) {
        this.oldTokenConfInfo = oldTokenConfInfo;
    }

}
