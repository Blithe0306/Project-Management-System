package com.ft.otp.common.taglib;

import java.util.*;
import org.apache.log4j.Logger;

import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 获取所有的服务器IP地址
 *
 * @Date in Apr 19, 2011,5:00:15 PM
 *
 * @author TBM
 */
public class ServerTag extends BaseTag {

    private static final long serialVersionUID = 2530620655192186661L;

    private Logger logger = Logger.getLogger(ServerTag.class);
    private IServerServ serverServ;

    public ServerTag() {
        super();
        serverServ = (IServerServ) AppContextMgr.getObject("serverServ");
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
        List<?> serverList = new ArrayList<Object>();
        ServerInfo serverInfo = new ServerInfo();
        PageArgument pageArgument = new PageArgument();
        serverList = serverServ.query(serverInfo, pageArgument);

        Iterator<?> it = serverList.iterator();
        while (it.hasNext()) {
            ServerInfo hostInfo = (ServerInfo) it.next();
            sBuilder.append("<option value='").append(hostInfo.getHostipaddr())
                    .append("' ");
            if (StrTool.strNotNull(dataSrc)
                    && StrTool.strEquals(dataSrc, hostInfo.getHostipaddr())) {
                sBuilder.append("selected");
            }
            sBuilder.append(">");
            sBuilder.append(hostInfo.getHostipaddr()).append("\n");
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
