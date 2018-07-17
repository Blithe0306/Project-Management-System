/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 24, 2014,10:31:00 PM
 */
package com.ft.otp.common.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ft.otp.common.Constant;
import com.ft.otp.common.PrjinfoTypeConstant;
import com.ft.otp.manager.prjinfo.entity.PrjinfoType;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 定制信息归类
 * @Date Dec 24, 2014,10:31:00 PM
 * @version v1.0
 * @author ZWX
 */
public class PrjinfoTypeConfig {
    private static PrjinfoTypeConfig config = null;
    private static Map<String, PrjinfoType> typeMap;
    private static List<PrjinfoType> typeList;

    private PrjinfoTypeConfig() {
        typeMap = new TreeMap<String, PrjinfoType>();
        typeList = new ArrayList<PrjinfoType>();
        Document document = XmlUtil.getDocument(Constant.PRJINFO_TYPE_CONF_XML);

        if (StrTool.objNotNull(document)) {
            deptXMLData(document);
        }
    }

    /**
     * 读取配置文件，封装到Map中
     */
    private void deptXMLData(Document document) {
        Element root = document.getRootElement();
        PrjinfoType prjType = null;
        String typeid;
        String typeName;

        for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
            prjType = new PrjinfoType();
            Element node = (Element) iter.next();
            typeid = node.attributeValue(PrjinfoTypeConstant.TYPE_ID);
            typeName = node.element(PrjinfoTypeConstant.TYPE_NAME).getTextTrim();

            prjType.setTypeid(typeid);
            prjType.setTypeName(typeName);

            typeMap.put(typeid, prjType);
            typeList.add(prjType);
        }
    }

    public static PrjinfoTypeConfig loadTypeConfig() {
        if (config != null) {
            return config;
        }

        synchronized (PrjinfoTypeConfig.class) {
            if (config == null) {
                config = new PrjinfoTypeConfig();
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
    public static PrjinfoType getValue(String key) {
        if (!StrTool.mapNotNull(typeMap)) {
            return null;
        }

        return typeMap.get(key);
    }

    /**
     * 返回定制类型数据Map
     * 
     * @Date in Apr 8, 2013,3:33:32 PM
     * @return
     */
    public static Map<String, PrjinfoType> getTypeMap() {
        if (!StrTool.mapNotNull(typeMap)) {
            return null;
        }

        return typeMap;
    }

    /**
     * 清空Map，重置config
     */
    public static void clear() {
        if (null != config) {
            typeMap.clear();
            config = null;
        }
    }
    
    public static List<PrjinfoType> getPrjTypeList(){
        if (!StrTool.listNotNull(typeList)) {
            return null;
        }
        
        return typeList;
    }
}
