/**
 *Administrator
 */
package com.ft.otp.common.taglib.lic;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ft.otp.common.language.Language;
import com.ft.otp.common.taglib.BaseTag;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.lic.entity.LicInfo;
import com.ft.otp.manager.lic.service.ILicInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 启用授权文件标签封装
 * @Date in Mar 13, 2013,5:08:05 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class LicNameTag extends BaseTag {

    private static final long serialVersionUID = 4409266130721657470L;
    private Logger logger = Logger.getLogger(LicNameTag.class);

    //授权服务接口
    private ILicInfoServ licInfoServ = (ILicInfoServ) AppContextMgr.getObject("licInfoServ");

    private String dataSrc;

    public LicNameTag() {
        super();
    }

    public int doEndTag() {
        try {
            pageContext.getOut().print(this.optionStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return EVAL_PAGE;
    }

    private String optionStr() throws Exception {
        HttpSession session = pageContext.getSession();
        StringBuilder sBuilder = new StringBuilder();
        LicInfo licInfo = new LicInfo();
        licInfo.setLicstate(1);
        try {
            licInfo = (LicInfo) licInfoServ.find(licInfo);
            if (StrTool.objNotNull(licInfo)) {
                int lictype = licInfo.getLictype();
                if (lictype == 0) {
                    sBuilder.append(Language.getLangValue("lic_type_eval", Language.getCurrLang(session), null));
                } else if (lictype == 1) {
                    sBuilder.append(Language.getLangValue("lic_type_busi", Language.getCurrLang(session), null));
                } else if (lictype == 2) {
                    sBuilder.append(Language.getLangValue("lic_type_advanced", Language.getCurrLang(session), null));
                }
                sBuilder.append("<input name='").append("serverInfo.licid").append("' "); 
                sBuilder.append("type='").append("hidden").append("' "); 
                sBuilder.append("id='").append("licid").append("' "); 
                sBuilder.append("value='").append(licInfo.getLicid()).append("' "); 
                sBuilder.append("/>");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return sBuilder.toString();
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

}
