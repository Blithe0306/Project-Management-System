/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ft.otp.common.Constant;
import com.ft.otp.common.DepartmentConstant;
import com.ft.otp.manager.customer.entity.DepartmentInfo;
import com.ft.otp.util.tool.StrTool;
import com.ft.otp.util.xml.XmlUtil;

/**
 * 部门配置文件加载 dept-conf.xml
 */
public class DepartmentConfig {

    private static DepartmentConfig config = null;
    private static Map<String, DepartmentInfo> deptMap;
    private static List<DepartmentInfo> deptList;

    private DepartmentConfig() {
    	deptMap = new TreeMap<String, DepartmentInfo>();
    	deptList = new ArrayList<DepartmentInfo>();
        Document document = XmlUtil.getDocument(Constant.DEPARTMENT_CONF_XML);

        if (StrTool.objNotNull(document)) {
        	deptXMLData(document);
        }
    }

    /**
     * 读取商配置文件，封装到Map中
     */
    private void deptXMLData(Document document) {
        Element root = document.getRootElement();
        DepartmentInfo deptInfo = null;
        String deptId;
        String deptName;

        for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
        	deptInfo = new DepartmentInfo();
            Element node = (Element) iter.next();
            deptId = node.attributeValue(DepartmentConstant.DEPT_ID);
            deptName = node.element(DepartmentConstant.DEPT_NAME).getTextTrim();

            deptInfo.setDeptid(deptId);
            deptInfo.setDeptName(deptName);

            deptMap.put(deptId, deptInfo);
            deptList.add(deptInfo);
        }
    }

    public static DepartmentConfig loadDeptConfig() {
        if (config != null) {
            return config;
        }

        synchronized (DepartmentConfig.class) {
            if (config == null) {
                config = new DepartmentConfig();
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
    public static DepartmentInfo getValue(String key) {
        if (!StrTool.mapNotNull(deptMap)) {
            return null;
        }

        return deptMap.get(key);
    }

    /**
     * 返回厂商数据Map
     * 
     * @Date in Apr 8, 2013,3:33:32 PM
     * @return
     */
    public static Map<String, DepartmentInfo> getDeptMap() {
        if (!StrTool.mapNotNull(deptMap)) {
            return null;
        }

        return deptMap;
    }

    /**
     * 清空Map，重置config
     */
    public static void clear() {
        if (null != config) {
        	deptMap.clear();
            config = null;
        }
    }
    
    public static List<DepartmentInfo> getDeptList(){
    	if (!StrTool.listNotNull(deptList)) {
            return null;
        }
    	
    	return deptList;
    }
    
}
