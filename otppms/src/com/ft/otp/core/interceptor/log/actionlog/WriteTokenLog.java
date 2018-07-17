/**
 *Administrator
 */
package com.ft.otp.core.interceptor.log.actionlog;

import java.util.List;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.interceptor.log.actionlog.helper.WriteLogHelper;
import com.ft.otp.manager.token.distmanager.action.DistManagerAction;
import com.ft.otp.manager.token.distmanager.entity.DistManagerInfo;
import com.ft.otp.manager.token.tokenimport.action.TokenImportAction;
import com.ft.otp.util.tool.StrTool;

/**
 * 令牌业务action日志记录
 *
 * @Date in Feb 21, 2013,2:12:36 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class WriteTokenLog {

    //引入日志帮助类
    private WriteLogHelper helper = new WriteLogHelper();

    int acid = 0, acobj = 0;
    int result = 0;
    boolean actionResult;
    String desc = "";
    List<String> descList = null;

    /**
     * 令牌导入
     * 
     * @Date in Aug 28, 2013,3:58:33 PM
     * @param object
     * @param method
     * @throws BaseException
     */
    public void writeImportToken(Object object, int method) throws BaseException {
        if (method == NumConstant.common_number_0) {
            TokenImportAction tokenImportAction = (TokenImportAction) object;
            acid = AdmLogConstant.log_aid_import;
            desc = tokenImportAction.getImportTknInfo();
            acobj = AdmLogConstant.log_obj_tkn;
            actionResult = tokenImportAction.getActionResult();
            helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
        }
    }

    /**
     * 手机令牌分发
     * 
     * @Date in Aug 28, 2013,4:52:39 PM
     * @param object
     * @param acid
     */
    public void writeMobileDist(Object object, int acid) {
        DistManagerAction managerAction = (DistManagerAction) object;
        DistManagerInfo distManagerInfo = managerAction.getDistManagerInfo();
        if(StrTool.strNotNull(managerAction.getDistDesc())){
        	desc = managerAction.getDistDesc();
        }else{
        	desc = Language.getLangValue("tkn_comm_tknum", Language.getCurrLang(null), null) 
        		+ Language.getLangValue("colon", Language.getCurrLang(null), null)
        		+ distManagerInfo.getToken();
        }
        
        acobj = AdmLogConstant.log_obj_tkn;
        actionResult = managerAction.getActionResult();

        helper.getLogCommonObj().addAdminLog(acid, acobj, desc, descList, actionResult ? 0 : 1);
    }

}
