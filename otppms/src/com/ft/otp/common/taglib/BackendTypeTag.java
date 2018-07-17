package com.ft.otp.common.taglib;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import com.ft.otp.util.tool.StrTool;

/**
 * 后端认证级别Tag
 *
 * @author LXH
 */
public class BackendTypeTag extends BaseTag {
 
    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(BackendTypeTag.class);

    public BackendTypeTag() {
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
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<String, String>();
        String radius = "Radius";
        String ad = "AD";
        map.put("0", radius);
        map.put("1", ad);

        // 遍历获取Map键和值
        for (String key : map.keySet()) {
            String value = map.get(key);
            sb.append("<option value='").append(key).append("' ");
            if (StrTool.strEquals(dataSrc, key)) {
                sb.append(" selected='selected'");
            }
            if (!StrTool.strNotNull(dataSrc) && "0".equals(key)) {
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
