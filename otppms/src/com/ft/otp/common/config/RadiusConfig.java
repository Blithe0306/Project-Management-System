/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.admin.user.service.IAdminUserServ;
import com.ft.otp.manager.confinfo.radius.entity.RadAttrInfo;
import com.ft.otp.manager.confinfo.radius.service.IRadAttrServ;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * RADIUS配置属性名称和RADIUS配置属性值类型
 * 放在MAP中，形成键值对
 * @Date in Oct 31, 2012,10:54:47 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class RadiusConfig {

    private static RadiusConfig config = null;
    public static Map<String, Object> radiusMap;

    private RadiusConfig() {
        radiusMap = new TreeMap<String, Object>();
        Document document = XmlUtil.getDocument(Constant.RADIUS_ATTR_XML);

        if (StrTool.objNotNull(document)) {
            radiusMap = radAttrXMLData(document);
        }
    }

    /**
     * 读取Radius配置xml文件封装到Map当中
     * @Date in Nov 1, 2012,5:26:12 PM
     * @param document
     * @return
     */
    public static Map<String, Object> radAttrXMLData(Document document) {
        //取得XML文件中的配置属性及值
        Element root = document.getRootElement();
        RadAttrInfo atrrInfo = null;
        String attrId;
        String attrName;
        String vendorid;
        String vendorname;
        String attrValueType;
        Element addElt;
        Map<String, Object> resMap = new TreeMap<String, Object>();
        for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
            atrrInfo = new RadAttrInfo();
            Element node = (Element) iter.next();
            // 获取当前元素的名字
            attrId = node.element("attr_id").getTextTrim();
            attrName = node.element("attr_name").getTextTrim();
            vendorid = node.element("vendor_id").getTextTrim();
            vendorname = node.element("vendor_name").getTextTrim();
            attrValueType = node.element("attr_value_type").getTextTrim();
            Element addNodes = node.element("attr_value_options");
            //获取属性下子属性节点
            if (addNodes != null) {
                Map<String, String> valueMap = new TreeMap<String, String>();
                String code;
                for (Iterator<?> addItor = addNodes.elementIterator(); addItor.hasNext();) {
                    addElt = (Element) addItor.next();
                    code = addElt.attributeValue("value");
                    if (code != null) {
                        valueMap.put(code, addElt.getTextTrim());
                    }
                }
                atrrInfo.setValueMap(valueMap);
            }
            atrrInfo.setAttrId(attrId);
            atrrInfo.setAttrName(attrName);
            atrrInfo.setVendorname(vendorname);
            atrrInfo.setVendorid(Integer.parseInt(vendorid));
            atrrInfo.setAttrValueType(attrValueType);
            resMap.put(attrId+":"+vendorid, atrrInfo);
        }

        return resMap;
    }

    public static RadiusConfig loadRadiusConfig() {
        if (config != null) {
            return config;
        }

        synchronized (RadiusConfig.class) {
            if (config == null) {
                config = new RadiusConfig();
            }
            return config;
        }
    }

    /**
     * 根据key取得Value
     * @param key
     * @return
     */

    public static Object getValue(String key) {
        if (!StrTool.mapNotNull(radiusMap)) {
            return "";
        }

        return (Object) radiusMap.get(key);
    }

    /**
     * 清空Map，重置config
     */
    public static void clear() {
        if (null != config) {
            radiusMap.clear();
            config = null;
        }
    }
}
