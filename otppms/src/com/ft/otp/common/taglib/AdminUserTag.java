package com.ft.otp.common.taglib;

import java.util.*;
import org.apache.log4j.Logger;

import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.entity.AdminUser;
import com.ft.otp.manager.admin.user.form.AdminUserQueryForm;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 获取所有的管理员
 *
 * @Date in Apr 19, 2011,5:00:15 PM
 *
 * @author ZJY
 */
public class AdminUserTag extends BaseTag {

    private static final long serialVersionUID = 5475607532839174775L;

    private Logger logger = Logger.getLogger(AdminUserTag.class);

    //管理员服务接口
    private IAdminUserServ adminUserServ;

    public AdminUserTag() {
        super();
        adminUserServ = (IAdminUserServ) AppContextMgr
                .getObject("adminUserServ");
    }

    private String dataSrc;

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
        List<?> adminList = new ArrayList<Object>();
        AdminUserQueryForm queryForm = new AdminUserQueryForm();
        PageArgument pageArgument = new PageArgument();

        //获取所有管理员
        adminList = adminUserServ.query(queryForm.getAdminUser(), pageArgument);
        Iterator<?> it = adminList.iterator();
        while (it.hasNext()) {
            AdminUser adminUser = (AdminUser) it.next();
            sBuilder.append("<option value='").append(adminUser.getAdminid())
                    .append("' ");
            if (StrTool.strNotNull(dataSrc)
                    && StrTool.strEquals(dataSrc, adminUser.getAdminid())) {
                sBuilder.append("selected");
            }
            sBuilder.append(">");
            sBuilder.append(adminUser.getAdminid()).append("\n");
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
