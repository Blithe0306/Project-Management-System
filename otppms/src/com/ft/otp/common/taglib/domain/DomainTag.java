/**
 *Administrator
 */
package com.ft.otp.common.taglib.domain;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.taglib.BaseTag;
import com.ft.otp.manager.orgunit.domain.action.aide.DomainInfoActionAide;
import com.ft.otp.manager.orgunit.domain.entity.DomainInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 域标签封装
 *
 * @Date in Jan 31, 2013,10:58:19 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class DomainTag extends BaseTag {

    private static final long serialVersionUID = -7366334203485555815L;
    private Logger logger = Logger.getLogger(DomainTag.class);
    private String dataSrc;
    private String index1Lang;
    private String index1Val;
    
    public DomainTag() {
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
        StringBuilder sBuilder = new StringBuilder();
        try {
            
            String curLoginUserId = (String)pageContext.getSession().getAttribute(Constant.CUR_LOGINUSER);
            String curLoginUserRole = (String)pageContext.getSession().getAttribute(Constant.CUR_LOGINUSER_ROLE);
            DomainInfoActionAide domainInfoAide = new DomainInfoActionAide();
            // 判断index1Val从页面传过来的值是否为1，后端认证、用户导入,域名称的处理;
            if(StrTool.strEquals("1", index1Val)){
            	curLoginUserRole = StrConstant.SUPER_ADMIN; // 超级管理员权限
            }
            //当前管理员管理的域信息
            List<?> domainInfos= domainInfoAide.getDomainList(curLoginUserId,curLoginUserRole,null,null);
        	
        	if(StrTool.listNotNull(domainInfos)){ //如果非空
	            Iterator<?> it = domainInfos.iterator();
	            while(it.hasNext()) {
	                DomainInfo domain = (DomainInfo) it.next();
	                String id = StrTool.intToString(domain.getDomainId());
	                sBuilder.append("<option value='").append(id).append("' ");
	                if (StrTool.strEquals(dataSrc, id)) {
	                    sBuilder.append(" selected='selected' ");
	                }
	                sBuilder.append(">");
	                sBuilder.append(domain.getDomainName()).append("\n");
	                sBuilder.append("</option>");
	            }
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
    public String getIndex1Lang() {
        return index1Lang;
    }
    public void setIndex1Lang(String index1Lang) {
        this.index1Lang = index1Lang;
    }
    public String getIndex1Val() {
        return index1Val;
    }
    public void setIndex1Val(String index1Val) {
        this.index1Val = index1Val;
    }
}
