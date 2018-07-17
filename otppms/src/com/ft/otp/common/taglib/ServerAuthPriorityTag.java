package com.ft.otp.common.taglib;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ft.otp.common.language.Language;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证服务器级别Tag
 *
 * @Date in Apr 13, 2011,9:39:43 AM
 *
 * @author ZJY
 */
public class ServerAuthPriorityTag extends BaseTag {
 
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(ServerAuthPriorityTag.class);

    public ServerAuthPriorityTag() {
        super();
    }

    private String dataSrc;

    public int doEndTag() {
        // 输出
        try {
            pageContext.getOut().print(this.optionStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return EVAL_PAGE;
    }

    private String optionStr() {
        HttpSession session = pageContext.getSession();
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<String, String>();
        String advanced = Language.getLangValue("common_syntax_advanced", Language.getCurrLang(session), null);
        String ordinary = Language.getLangValue("common_syntax_ordinary", Language.getCurrLang(session), null);
        String low = Language.getLangValue("common_syntax_low", Language.getCurrLang(session), null);
        map.put("0", advanced);
        map.put("1", ordinary);
        map.put("2", low);

        // 遍历获取Map键和值
        for (String key : map.keySet()) {
            String value = map.get(key);
            sb.append("<option value='").append(key).append("' ");
            if (StrTool.strEquals(dataSrc, key)) {
                sb.append(" selected='selected'");
            }
            if (!StrTool.strNotNull(dataSrc) && "1".equals(key)) {
                sb.append(" selected='selected'");
            }
            sb.append(">");
            sb.append(value).append("\n");
        }

        return sb.toString();
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

}
