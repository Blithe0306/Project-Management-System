/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ft.otp.common.Constant;
import com.ft.otp.common.VendorConstant;
import com.ft.otp.manager.token.tokenimport.entity.VendorInfo;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 令牌导入多厂商配置文件加载 vendor-conf.xml
 *
 * @Date in Apr 1, 2013,11:51:50 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class VendorConfig {

    private static VendorConfig config = null;
    private static Map<String, VendorInfo> vendorMap;

    private VendorConfig() {
        vendorMap = new TreeMap<String, VendorInfo>();
        Document document = XmlUtil.getDocument(Constant.VENDOR_CONF_XML);

        if (StrTool.objNotNull(document)) {
            vendorXMLData(document);
        }
    }

    /**
     * 读取令牌导入多厂商配置文件，封装到Map中
     * @Date in Apr 1, 2013,3:02:24 PM
     * @param document
     * @return
     */
    private void vendorXMLData(Document document) {
        Element root = document.getRootElement();
        VendorInfo vendorInfo = null;
        String vendorId;
        String name;
        String normal;
        String classPath;
        String jarPath;

        for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
            vendorInfo = new VendorInfo();
            Element node = (Element) iter.next();
            vendorId = node.attributeValue(VendorConstant.VENDOR_ID);
            name = node.element(VendorConstant.VENDOR_NAME).getTextTrim();
            normal = node.element(VendorConstant.VENDOR_NORMAL).getTextTrim();
            classPath = node.element(VendorConstant.VENDOR_CLASSPATH).getTextTrim();
            jarPath = node.element(VendorConstant.VENDOR_JARPATH).getTextTrim();

            vendorInfo.setVendorId(vendorId);
            vendorInfo.setName(name);
            vendorInfo.setNormal(Boolean.valueOf(normal));
            vendorInfo.setClassPath(classPath);
            vendorInfo.setJarPath(jarPath);

            vendorMap.put(vendorId, vendorInfo);
        }
    }

    public static VendorConfig loadVendorConfig() {
        if (config != null) {
            return config;
        }

        synchronized (VendorConfig.class) {
            if (config == null) {
                config = new VendorConfig();
            }
            return config;
        }
    }

    /**
     * 根据Key获取Value值
     * @Date in Apr 1, 2013,4:12:12 PM
     * @param key
     * @return
     */
    public static VendorInfo getValue(String key) {
        if (!StrTool.mapNotNull(vendorMap)) {
            return null;
        }

        return vendorMap.get(key);
    }

    /**
     * 返回厂商数据Map
     * 
     * @Date in Apr 8, 2013,3:33:32 PM
     * @return
     */
    public static Map<String, VendorInfo> getVendorMap() {
        if (!StrTool.mapNotNull(vendorMap)) {
            return null;
        }

        return vendorMap;
    }

    /**
     * 清空Map，重置config
     */
    public static void clear() {
        if (null != config) {
            vendorMap.clear();
            config = null;
        }
    }

}
