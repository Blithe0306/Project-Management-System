package com.ft.otp.common.taglib;

import org.apache.log4j.Logger;

/**
 * 
 * 服务器配置列表
 * 
 * 取得所有服务器配置，selected与服务器配置对应的服务器
 *
 */
public class ServerConfigTag extends BaseTag {

    private static final long serialVersionUID = -388711329059707449L;

    private Logger logger = Logger.getLogger(ServerConfigTag.class);

    //公共配置服务接口
    //private IConfServ confServ;

    public ServerConfigTag() {
        super();
        //confServ = (IConfServ) AppContextMgr.getObject("confServ");
    }

    private String dataSrc;

    public int doEndTag() {
        try {
            pageContext.getOut().print(this.optionStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    private String optionStr() throws Exception {
        StringBuilder sBuilder = new StringBuilder();
//        PageArgument pageArg = new PageArgument();
//        ConfInfo cInfo = new ConfInfo();
//        cInfo.setConftype(ConfConstant.SERVER_CONF_MARK);
//        List<?> serverconfList = confServ.query(cInfo, pageArg);
//        
// 
//        
//        Iterator<?> it = serverconfList.iterator();
//        while (it.hasNext()) {
//            ConfInfo confInfo = (ConfInfo) it.next();
//           
//            sBuilder.append("<option value='").append(confInfo.getConfid())
//                    .append("' ");
//            if (StrTool.strNotNull(dataSrc)
//                    && dataSrc.equals(String.valueOf(confInfo.getConfid()))) {
//                sBuilder.append("selected");
//            }
//            sBuilder.append(">");
//            sBuilder.append(confInfo.getConfvalue()).append("\n");
//        }
        return sBuilder.toString();
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

}
