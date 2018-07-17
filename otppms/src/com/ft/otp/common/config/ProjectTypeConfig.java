/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 24, 2014,10:31:00 PM
 */
package com.ft.otp.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ft.otp.common.Constant;
import com.ft.otp.common.DepartmentConstant;
import com.ft.otp.common.ProjectTypeConstant;
import com.ft.otp.manager.project.entity.ProjectType;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 定制项目类型
 * @Date Dec 24, 2014,10:31:00 PM
 * @version v1.0
 * @author ZWX
 */
public class ProjectTypeConfig {
    private static ProjectTypeConfig config = null;
    private static Map<String, ProjectType> typeMap;
    private static List<ProjectType> typeList;

    private ProjectTypeConfig() {
        typeMap = new TreeMap<String, ProjectType>();
        typeList = new ArrayList<ProjectType>();
        Document document = XmlUtil.getDocument(Constant.PROJECT_TYPE_CONF_XML);

        if (StrTool.objNotNull(document)) {
            deptXMLData(document);
        }
    }

    /**
     * 读取商配置文件，封装到Map中
     */
    private void deptXMLData(Document document) {
        Element root = document.getRootElement();
        ProjectType prjType = null;
        String typeid;
        String typeName;
        String verStr;
        for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
            prjType = new ProjectType();
            Element node = (Element) iter.next();
            typeid = node.attributeValue(ProjectTypeConstant.TYPE_ID);
            typeName = node.element(ProjectTypeConstant.TYPE_NAME).getTextTrim();
            verStr = node.element(ProjectTypeConstant.TYPE_VERSION).getTextTrim();

            prjType.setTypeid(typeid);
            prjType.setTypeName(typeName);
            prjType.setVersion(verStr);

            typeMap.put(typeid, prjType);
            typeList.add(prjType);
        }
    }

    public static ProjectTypeConfig loadTypeConfig() {
        if (config != null) {
            return config;
        }

        synchronized (ProjectTypeConfig.class) {
            if (config == null) {
                config = new ProjectTypeConfig();
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
    public static ProjectType getValue(String key) {
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
    public static Map<String, ProjectType> getTypeMap() {
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
    
    public static List<ProjectType> getPrjTypeList(){
        if (!StrTool.listNotNull(typeList)) {
            return null;
        }
        
        return typeList;
    }
}
