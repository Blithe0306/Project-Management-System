/**
 *Administrator
 */
package com.ft.otp.common.taglib;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.manager.admin.role_perm.entity.RolePerm;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.util.tool.StrTool;

/**
 * 多语言标签
 * 用于获取多语言资源数据
 *
 * @Date in Apr 14, 2011,2:23:37 PM
 *
 * @author TBM
 */
public class AdminPermTag extends BaseTag {

    private static final long serialVersionUID = -2682997428794695145L;
    private Logger logger = Logger.getLogger(AdminPermTag.class);

    //权限CODE，与数据库、Session中的管理员权限CODE一致
    private String key;
    //应用根路径，request.getContextPath()值
    private String path;
    //多语言key
    private String langKey;
    //权限CODE指代的类型 0->文字，1->图片img，2->按钮button 3-->按钮是图片和文字组合
    private String type;

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the langKey
     */
    public String getLangKey() {
        return langKey;
    }

    /**
     * @param langKey the langKey to set
     */
    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public int doEndTag() {
        // 输出
        try {
            pageContext.getOut().print(this.resourceStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return EVAL_PAGE;
    }

    /**
     * 根据Key取得对应的权限值
     * @Date in Apr 14, 2011,4:04:25 PM
     * @return
     */
    private String resourceStr() {
        String permVal = "";

        //key与权限列表中的权限CODE比对，存在返回权限值Link，不存在返回""
        //获取在线用户
        HttpSession session = pageContext.getSession();
        String sessionId = session.getId();
        LinkUser linkUser = OnlineUsers.getUser(sessionId);
        Map<String, Object> permMap = linkUser.getPermMap();
        if (!StrTool.mapNotNull(permMap)) {
            return permVal;
        }
        String permKey = "";
        RolePerm rp = (RolePerm) permMap.get(key);
        if (StrTool.objNotNull(rp)) {
            permKey = rp.getPermcode();
        }

        if (StrTool.strNotNull(permKey)) {
            permVal = rp.getSrcname();
            String langVal = Language.getLangStr(session, langKey);
            if (StrTool.strEquals(type, StrConstant.common_number_0)) {
                permVal = langVal;
            }
            if (StrTool.strEquals(type, StrConstant.common_number_1)) {
                permVal = permVal.replaceAll("<%=path%>", path);
                String imgStr = permVal.substring(0, permVal.lastIndexOf(">"));
                permVal = imgStr + " title=" + '"' + langVal + '"' + ">";
            }
            if (StrTool.strEquals(type, StrConstant.common_number_2)) {
                permVal = permVal.replaceAll("<span></span>", "<span>" + langVal + "</span>");
            }
            if (StrTool.strEquals(type, StrConstant.common_number_3)) {
                permVal = permVal.replaceAll("<%=path%>", path);
                permVal = permVal.replaceAll("<span></span>", langVal);
            }

        }

        return permVal;
    }
}
