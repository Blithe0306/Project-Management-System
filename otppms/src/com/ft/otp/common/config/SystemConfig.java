/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import com.ft.otp.common.Constant;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 系统配置文件加载
 *
 * @Date in Apr 18, 2011,4:24:51 PM
 *
 * @author TBM
 */
public class SystemConfig {

    private static Map<String, String> systemMap;
    private static SystemConfig config = null;

    private SystemConfig() {
        systemMap = new HashMap<String, String>();
        Document document = XmlUtil.getDocument(Constant.SYSTEM_CONF_XML);

        if (StrTool.objNotNull(document)) {
            //取得XML文件中的配置属性及值
            Element rootElement = document.getRootElement();
            systemMap = XmlUtil.recursionXML(rootElement);
        }
    }

    public static SystemConfig loadSystemConf() {
        if (config != null) {
            return config;
        }

        synchronized (SystemConfig.class) {
            if (config == null) {
                config = new SystemConfig();
            }
            return config;
        }
    }

    /**
     * 根据key取得Value
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static String getValue(String key) {
        if (!StrTool.mapNotNull(systemMap)) {
            return "";
        }
        return (String) systemMap.get(key);
    }

    /**
     * 清空Map，重置config
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void clear() {
        if (null != config) {
            systemMap.clear();
            config = null;
        }
    }

}
