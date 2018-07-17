/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.aide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.MonitorJsonEntity;

/**
 * 监控预警服务帮助类说明
 *
 * @Date in Feb 28, 2013,11:03:36 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorServiceAide {
    /**
     * 封装对象列表返回JSON格式化数据
     * @Date in May 4, 2011,6:30:44 PM
     * @param recordTotal
     * @param list 数据列表
     * @param object 分页对象
     * @return
     */
    public static String getJsonFromList(long recordTotal, List<?> list, Object object) throws Exception {
        MonitorJsonEntity entity = new MonitorJsonEntity();
        //设置分页信息
        if (object == null) {
            entity.setObject(object);
        }
        //设置列表数据
        if (null != list && list.size() > 0) {
            entity.setItems(list);
            entity.setResults(recordTotal);
        } else {
            list = new ArrayList<Object>();
        }
        JSONObject JsonObject = JSONObject.fromObject(entity);

        return JsonObject.toString();
    }

    /**
     * 将json字符串转换为级联实体
     * 
     * @Date in Mar 14, 2013,10:54:36 AM
     * @param jsonStr
     * @return
     */
    public static List<?> getEntityFromJson(String jsonStr) throws Exception {
        List<?> resultList = new ArrayList<Object>();
        if (jsonStr != null && jsonStr != "") {
            JSONObject jso = JSONObject.fromObject(jsonStr);
            JsonConfig conf = new JsonConfig();
            conf.setRootClass(MonitorJsonEntity.class);
            Map<String, Class> m = new HashMap<String, Class>();
            m.put("items", EquipmentMonitorInfo.class);
            conf.setClassMap(m);
            conf.setCollectionType(List.class);
            // 过滤两个属性
            //conf.setExcludes( new String[]{ “execludeField” , “handlist” } ) ;
            MonitorJsonEntity vo = (MonitorJsonEntity) JSONObject.toBean(jso, conf);
            if (vo != null) {
                resultList = (List<?>) vo.getItems();
            }
        }

        return resultList;
    }

    /**
     * 将json字符串转换为列表对象
     * @Date in Mar 14, 2013,10:57:08 AM
     * @param jsonStr
     * @return
     */
    private static List<?> getListFromJson(String jsonStr) throws Exception {
        List<MonitorJsonEntity> mList = new ArrayList<MonitorJsonEntity>();
        if (jsonStr != null && jsonStr != "") {
            JSONArray ja = JSONArray.fromObject(jsonStr);
            mList = (List<MonitorJsonEntity>) JSONArray.toCollection(ja, MonitorJsonEntity.class);
        }
        return mList;
    }
    
    /**
     * String -> double
     * @Date in Apr 8, 2011,6:59:03 PM
     * @param str
     * @return
     */
    public static double parseDouble(String str) {
        if (null != str && !"".equals(str)) {
            try {
                return Double.parseDouble(str);
            } catch (Exception ex) {
            }
        }
        return 0.0;
    }
    
    /**
     * 替换掉%符号，如果为空返回0
     * 方法说明
     * @Date in Mar 18, 2013,4:10:18 PM
     * @param percentStr
     * @return
     */
    public static String replaceChar(String percentStr,String charStr) {
        String numStr = "0";
        if (percentStr!=null&&!percentStr.equals("")) {
            numStr = percentStr.replace(charStr, "");
        }
        
        return numStr;
    }

}
