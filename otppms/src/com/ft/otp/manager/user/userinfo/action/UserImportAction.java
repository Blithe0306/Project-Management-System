/**
 *Administrator
 */
package com.ft.otp.manager.user.userinfo.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.user.userinfo.action.aide.UserImportActionAide;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户导入操作业务逻辑控制Action
 *
 * @Date in May 10, 2011,6:31:23 PM
 *
 * @author TBM
 */
public class UserImportAction extends BaseAction {

    private static final long serialVersionUID = -6465103819212763892L;

    private Logger logger = Logger.getLogger(UserImportAction.class);

    private IUserInfoServ userImportServ = null;

    private UserImportActionAide aide = null;

    private boolean actionResult = false;
    public UserImportAction() {
        if (null == aide) {
            aide = new UserImportActionAide();
        }
    }

    //用户文件
    private File userFile;

    /**
     * @return the userImportServ
     */
    public IUserInfoServ getUserImportServ() {
        return userImportServ;
    }

    /**
     * @param userImportServ the userImportServ to set
     */
    public void setUserImportServ(IUserInfoServ userImportServ) {
        this.userImportServ = userImportServ;
    }

    /**
     * @return the userFile
     */
    public File getUserFile() {
        return userFile;
    }

    /**
     * @param userFile the userFile to set
     */
    public void setUserFile(File userFile) {
        this.userFile = userFile;
    }

    /**
     * 导入用户操作方法
     * @Date in May 10, 2011,6:32:47 PM
     * @return
     */
    public String importUser() {
        Map<String, Object> map = null;
        PrintWriter out = null;
        try {
        	//判断模板文件是否合法
        	String domainId=request.getParameter("domainId");
        	String fileName = Language.getLangStr(request, "user_fail_record_file") + Constant.FILE_XLS; //失败记录文件名
            String filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);//失败记录文件目录
            out = response.getWriter();
            map = aide.fileUpLoad(userImportServ, userFile,domainId,filePath, request);
            if (!StrTool.mapNotNull(map)) {
            	//outPutOperResult(Constant.alert_succ, "false");
                setResponseWrite("false");
                return null;
            }
            String totalUsr = Language.getLangStr(request, "user_total_data") + map.get("totalUsr");
            String tAddUsr = Language.getLangStr(request, "user_add_tip") + map.get("tAddUsr");
            String tUsrTknOK = Language.getLangStr(request, "user_add_token_bind") + map.get("tUsrTknOK");
            int faildRowCount=((Integer)map.get("faildRecord")).intValue();
            String faildRecordCount = Language.getLangStr(request, "user_fail_message") + faildRowCount;
            String faildFileStr= Language.getLangStr(request, "user_fail_file")+"<a href=\"javascript:downFaildFile('"+fileName+"');\">"+fileName+"</a>";
            //& 是分割符
            String successInfo="";
            if(faildRowCount>NumConstant.common_number_0){//有失败记录
            	successInfo= Language.getLangStr(request, "user_upload_import_succ")+"<br>" + totalUsr + "<br>" + tAddUsr + "<br>" + tUsrTknOK + "<br>" + faildRecordCount+"<br>"+faildFileStr;
            }else{
            	successInfo= Language.getLangStr(request, "user_upload_import_succ")+"<br>" + totalUsr + "<br>" + tAddUsr + "<br>" + tUsrTknOK + "<br>" + faildRecordCount+"<br>";
            }
            setResponseWrite(successInfo);
            out.flush(); 
        } catch (Exception e) {  
            logger.error(e.getMessage(), e);
            setResponseWrite("false");
            out.flush(); 
            return null;
        }

        return null;
    }

    /**
     * 生成用户模板文件
     * @Date in May 11, 2011,11:03:26 AM
     * @return
     */
    public String downLoadIni() {
        String raType = request.getParameter("raType");
        String usrAttr = request.getParameter("usrAttr");
        String filePath = "";
        String fileName = "";
        try {
            //XML文件
            if (StrTool.strEquals(raType, StrConstant.common_number_2)) {

            }
            //Excel文件
            else {
                fileName = Language.getLangStr(request, "user_vd_templet")+ Constant.FILE_XLS;
                filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);
                aide.createUserXls(filePath, usrAttr, request);
            }
            outPutOperResult(Constant.alert_succ, fileName);
            super.setActionResult(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            outPutOperResult(Constant.alert_error, Language.getLangStr(request, "user_vd_templet_download_fail"));
            super.setActionResult(false);
        }
        return null;
    }

    /**
     * 下载用户模板文件
     * @Date in May 11, 2011,4:26:18 PM
     * @return
     * @throws Exception 
     */
    public String downLoad() throws Exception {
        String fileName = request.getParameter("fileName");
        if(StrTool.strNotNull(fileName)){
        	fileName = MessyCodeCheck.iso885901ForUTF8(fileName);
        }
        String filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);
        try {
            aide.downLoadFile(fileName, filePath, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public boolean getActionResult() {
        return actionResult;
    }

    public void setActionResult(boolean actionResult) {
        this.actionResult = actionResult;
    }

}
