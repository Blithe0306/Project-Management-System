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
import com.ft.otp.manager.user.userinfo.action.aide.UserUnBindActionAide;
import com.ft.otp.manager.user.userinfo.service.IUserInfoServ;
import com.ft.otp.util.tool.MessyCodeCheck;
import com.ft.otp.util.tool.StrTool;

/**
 * 用户令牌批量解绑Action
 * 
 * @Date in May 16, 2011,4:01:28 PM
 * 
 * @author TBM
 */
public class UserUnBindAction extends BaseAction {

	private static final long serialVersionUID = -7063141751377887283L;

	private Logger logger = Logger.getLogger(UserUnBindAction.class);

	private IUserInfoServ userUnBindServ = null;

	private UserUnBindActionAide aide = null;
	private UserImportActionAide importActionAide = null;

	public UserUnBindAction() {
		if (null == aide) {
			aide = new UserUnBindActionAide();
		}
		if (null == importActionAide) {
			importActionAide = new UserImportActionAide();
		}
	}

	// 批量解绑用户文件
	private File userFile;

	/**
	 * @return the userFile
	 */
	public File getUserFile() {
		return userFile;
	}

	/**
	 * @param userFile
	 *            the userFile to set
	 */
	public void setUserFile(File userFile) {
		this.userFile = userFile;
	}

	/**
	 * @return the userUnBindServ
	 */
	public IUserInfoServ getUserUnBindServ() {
		return userUnBindServ;
	}

	/**
	 * @param userUnBindServ
	 *            the userUnBindServ to set
	 */
	public void setUserUnBindServ(IUserInfoServ userUnBindServ) {
		this.userUnBindServ = userUnBindServ;
	}

	/**
	 * 批量解绑文件下载初始化
	 * 
	 * @Date in May 16, 2011,4:03:39 PM
	 * @return
	 */
	public String downLoadIni() {
		String raType = request.getParameter("raType");
		String filePath = "";
		String fileName = "";
		String usrAttr = "1,20,";
		try {
			// CSV文件
			if (StrTool.strEquals(raType, StrConstant.common_number_2)) {

			}
			// Excel文件
			else {
				fileName = Language.getLangStr(request, "user_batch_unbind_templet") + Constant.FILE_XLS;
				filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);
				importActionAide.createUserXls(filePath, usrAttr, request);
			}
			outPutOperResult(Constant.alert_succ, fileName);
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
			outPutOperResult(Constant.alert_error, Language.getLangStr(request, "user_batch_templet_download_fail"));
			return null;
		}
		return null;
	}

	/**
	 * 批量解绑文件下载
	 * 
	 * @Date in May 16, 2011,4:03:39 PM
	 * @return
	 * @throws Exception
	 */
	public String downLoad() throws Exception {
		String fileName = request.getParameter("fileName");
		if (StrTool.strNotNull(fileName)) {
			fileName = MessyCodeCheck.iso885901ForUTF8(fileName);
		}
		String filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);
		try {
			importActionAide.downLoadFile(fileName, filePath, response);
		} catch (Exception e) {
		    logger.error(e.getMessage(), e);
		}
		return null;
	}

	
	/**
     * 导入解绑模板
     * @Date in May 10, 2011,6:32:47 PM
     * @return
     */
    public String unBindUsrTkn() {
        Map<String, Object> map = null;
        int[] attrArr = new int[2];
		attrArr[0] = 1;
		attrArr[1] = 20;
        PrintWriter out = null;
        try {
        	//判断模板文件是否合法
        	String fileName = Language.getLangStr(request, "user_fail_record_file") + Constant.FILE_XLS; //失败记录文件名
            String filePath = appPath(Constant.WEB_TEMP_FILE_USER, fileName);//失败记录文件目录
            out = response.getWriter();
            map = aide.fileUpLoad(userFile,filePath, request, attrArr);
            if (!StrTool.mapNotNull(map)) {
                setResponseWrite("false");
                return null;
            }
            String totalUsr = Language.getLangStr(request, "user_vd_import_total") + map.get("totalUsr");
            String tBindUsr = Language.getLangStr(request, "user_vd_import_unbind_total") + map.get("tBindUsr");
            int faildRowCount=((Integer)map.get("faildRecord")).intValue();
            String faildRecordCount = Language.getLangStr(request, "user_fail_message") + faildRowCount;
            String faildFileStr= Language.getLangStr(request, "user_fail_file")+"<a href=\"javascript:downFaildFile('"+fileName+"');\">"+fileName+"</a>";
            //& 是分割符
            String successInfo="";
            if(faildRowCount>NumConstant.common_number_0){//有失败记录
            	successInfo= Language.getLangStr(request, "user_upload_unbind_succ")+"<br>" + totalUsr + "<br>" + tBindUsr + "<br>" + faildRecordCount+"<br>"+faildFileStr;
            }else{
            	successInfo= Language.getLangStr(request, "user_upload_unbind_succ")+"<br>" + totalUsr + "<br>" + tBindUsr + "<br>" + faildRecordCount+"<br>";
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

}
