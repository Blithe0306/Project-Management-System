package com.ft.otp.common.taglib;

import java.util.*;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.radius.entity.RadProfileInfo;
import com.ft.otp.manager.confinfo.radius.service.IRadProfileServ;


/**
 * 
 * 获取所有的raduis属性
 *
 * @Date in Mar 19, 2013,5:00:15 PM
 *
 * @author BYL
 */
public class RadProfileTag extends BaseTag {

	private static final long serialVersionUID = 4856991656225448542L;

	private Logger logger = Logger.getLogger(RadProfileTag.class);

    //管理员服务接口
    private IRadProfileServ radProfileServ;

    public RadProfileTag() {
        super();
        radProfileServ = (IRadProfileServ) AppContextMgr
                .getObject("radProfileServ");
    }

    private int dataSrc;

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
        List<?> radProfileInfoList = new ArrayList<Object>();
        RadProfileInfo radProfileInfo = new RadProfileInfo();
        PageArgument pageArgument = new PageArgument();
        
        //特殊项
        sBuilder.append("<option value='0'>"+Language.getLangValue("common_syntax_not_return", Language.getCurrLang(session), null)+"\n");
        
        
        //获取所有radProfile
        radProfileInfoList = radProfileServ.query(radProfileInfo, pageArgument);
        Iterator<?> it = radProfileInfoList.iterator();
        while (it.hasNext()) {
        	RadProfileInfo radProfile = (RadProfileInfo) it.next();
            sBuilder.append("<option value='").append(radProfile.getProfileId())
                    .append("' ");
            if (dataSrc==radProfile.getProfileId()) {
                sBuilder.append("selected");
            }
            sBuilder.append(">");
            sBuilder.append(radProfile.getProfileName()).append("\n");
        }
        return sBuilder.toString();
    }

    public int getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(int dataSrc) {
        this.dataSrc = dataSrc;
    }
}
