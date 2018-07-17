package com.ft.otp.common.taglib;

import java.util.List;
import org.apache.log4j.Logger;

import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 获取所有的本地IP
 *
 * @Date in Apr 19, 2011,5:00:15 PM
 *
 * @author TBM
 */
public class LocalIpTag extends BaseTag {

    private static final long serialVersionUID = 2530620655192186661L;

    private Logger logger = Logger.getLogger(LocalIpTag.class);

    public LocalIpTag() {
        super();

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
        List<String> ipList = IpTool.localHostIp();
        for (int i = 0; i < ipList.size(); i++) {
            String ipAddr = ipList.get(i);
            sBuilder.append("<option value='").append(ipAddr).append("' ");
            if (StrTool.strNotNull(dataSrc) && StrTool.strEquals(dataSrc, ipAddr)) {
                sBuilder.append("selected");
            }
            sBuilder.append(">");
            sBuilder.append(ipAddr).append("\n");
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
