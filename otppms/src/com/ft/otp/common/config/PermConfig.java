/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.LinkedHashMap;
import java.util.Map;
import com.ft.otp.util.tool.StrTool;

/**
 * 类、接口等说明
 *
 * @Date in Jan 22, 2013,2:59:08 PM
 *
 * @version v1.0
 *
 * @author ZJY
 */
public class PermConfig {

    private static PermConfig config = null;
    private static Map<String, Object> permMap;

    public PermConfig() {

    }

    static {
        permMap = new LinkedHashMap<String, Object>();
    }

    public static PermConfig loadPerm() {
        if (config != null) {
            return config;
        }

        synchronized (PermConfig.class) {
            if (config == null) {
                config = new PermConfig();
            }
            return config;
        }
    }

    /**
     * 存放KEY/VALUE 方法说明
     * 
     * @Date in Feb 7, 2012,2:08:03 PM
     * @param key
     * @param object
     */
    public static void putObject(String key, Object object) {
        if (!StrTool.strNotNull(key)) {
            return;
        }
        if (!StrTool.objNotNull(object)) {
            return;
        }
        permMap.put(key, object);
    }

    /**
     * 根据KEY获取VALUE 方法说明
     * 
     * @Date in Feb 7, 2012,2:08:28 PM
     * @param key
     * @return
     */
    public static Object getValue(String key) {
        if (!StrTool.mapNotNull(permMap)) {
            return null;
        }
        return permMap.get(key);
    }

    /**
     * 清除MAP 方法说明
     * 
     * @Date in Feb 7, 2012,1:57:15 PM
     */
    public static void clear() {
        if (!StrTool.objNotNull(config)) {
            permMap.clear();
            config = null;
        }
    }

}
