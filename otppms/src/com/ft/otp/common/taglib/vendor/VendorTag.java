/**
 *Administrator
 */
package com.ft.otp.common.taglib.vendor;

import java.util.Map;
import org.apache.log4j.Logger;

import com.ft.otp.common.config.VendorConfig;
import com.ft.otp.common.taglib.BaseTag;
import com.ft.otp.manager.token.tokenimport.entity.VendorInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 多厂商标签
 *
 * @Date in Apr 1, 2013,1:42:46 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class VendorTag extends BaseTag {

    private static final long serialVersionUID = 5060922173169180360L;
    private Logger logger = Logger.getLogger(VendorTag.class);

    public VendorTag() {
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
        Map<String, VendorInfo> vendorMap = VendorConfig.getVendorMap();
        if (!StrTool.mapNotNull(vendorMap)) {
            return sBuilder.toString();
        }

        VendorInfo vendorInfo = null;
        for (String vendorId : vendorMap.keySet()) {
            vendorInfo = vendorMap.get(vendorId);
            String vendorName = vendorInfo.getName();
            sBuilder.append("<option value='").append(vendorId).append("' ");
            if (StrTool.strEquals(dataSrc, vendorId)) {
                sBuilder.append(" selected='selected' ");
            }
            sBuilder.append(">");
            sBuilder.append(vendorName).append("\n");
            sBuilder.append("</option>");
        }
        String result = sBuilder.toString();

        return result;
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

}
