/**
 *Administrator
 */
package com.ft.otp.util.json;

import java.util.ArrayList;
import java.util.List;
import com.ft.otp.util.tool.StrTool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSON工具类
 *
 * @Date in May 4, 2011,9:26:39 AM
 *
 * @author TBM
 */
public class JsonTool {

    /**
     * 封装对象返回JSON格式化数据
     * @Date in May 4, 2011,9:27:05 AM
     * @param recordTotal
     * @param obj
     * @return
     */
    public static String getJsonFromObj(long recordTotal, Object object) {
        JsonEntity entity = new JsonEntity();
        if (StrTool.objNotNull(object)) {
            entity.setObject(object);
        } else {
            object = new Object();
        }

        JSONObject JsonObject = JSONObject.fromObject(entity);
        return JsonObject.toString();
    }

    /**
     * 封装对象列表返回JSON格式化数据
     * @Date in May 4, 2011,6:30:44 PM
     * @param recordTotal
     * @param list 数据列表
     * @param object 分页对象
     * @return
     */
    public static String getJsonFromList(long recordTotal, List<?> list, Object object) {
        JsonEntity entity = new JsonEntity();
        //设置分页信息
        if (StrTool.objNotNull(object)) {
            entity.setObject(object);
        }
        //设置列表数据
        if (StrTool.listNotNull(list)) {
            entity.setItems(list);
            entity.setResults(recordTotal);
        } else {
            list = new ArrayList<Object>();
        }

        JSONObject JsonObject = JSONObject.fromObject(entity);
        return JsonObject.toString();
    }

    /**
     * 返回JSON封装后的错误信息
     * @Date in Apr 6, 2012,2:16:56 PM
     * @param errorStr
     * @return
     */
    public static String getJsonErrorMeg(String error, Object object) {
        JsonEntity entity = new JsonEntity();
        if (StrTool.objNotNull(object)) {
            entity.setObject(object);
        }
        if (StrTool.strNotNull(error)) {
            entity.setErrorStr(error);
        }

        JSONObject JsonObject = JSONObject.fromObject(entity);
        return JsonObject.toString();
    }

    /**
     * 封装对象列表返回JSON格式化数据
     * @Date in May 4, 2011,6:30:44 PM
     * @param recordTotal
     * @param list 数据列表
     * @param object 分页对象
     * @return
     */
    public static String getJsonFromList(long recordTotal, List<?> list) {
        JsonEntity entity = new JsonEntity();
        //设置列表数据
        if (StrTool.listNotNull(list)) {
            entity.setItems(list);
            entity.setResults(recordTotal);
        }
        JSONObject JsonObject = JSONObject.fromObject(entity);
        JSONArray jsonArray = JsonObject.getJSONArray("items");
        return jsonArray.toString();
    }

}
