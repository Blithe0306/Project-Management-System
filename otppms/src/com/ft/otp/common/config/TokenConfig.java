/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.Map;
import java.util.TreeMap;

import com.ft.otp.util.tool.StrTool;

/**
 * 令牌类型和物理类型属性对应类
 *
 * @Date in Jan 13, 2012,5:53:31 PM
 *
 * @author ZJY
 */
public class TokenConfig {

    private static Map<String, String> phyMap = new TreeMap<String, String>();
    private static Map<String, String> prdMap = new TreeMap<String, String>();

    private static Map<String, String> phycolorMap = new TreeMap<String, String>();
    private static Map<String, String> prdcolorMap = new TreeMap<String, String>();
    private static Map<String, String> stateColorMap = new TreeMap<String, String>();
    static {
        //令牌物理类型种类
        phyMap.put("0", "硬件令牌");
        phyMap.put("1", "手机令牌");
        phyMap.put("2", "软件令牌");
        phyMap.put("4", "刮刮卡令牌");
        phyMap.put("5", "矩阵卡令牌");
        phyMap.put("6", "短信令牌");
        phyMap.put("7", " SD卡令牌");

        //每种类型在图表中对应的颜色
        phycolorMap.put("0", "AFD8F8");
        phycolorMap.put("1", "F6BD0F");
        phycolorMap.put("2", "8BBA00");
        phycolorMap.put("4", "A66EDD");
        phycolorMap.put("5", "588526");
        phycolorMap.put("6", "B3AA00");
        phycolorMap.put("7", "F984A1");
        setPhyMap(phyMap);

        //令牌的产品类型
        prdMap.put("0", "c100");
        prdMap.put("1", "c200");
        prdMap.put("2", "c300");
        prdMap.put("3", "c400");
        prdMap.put("4", "c500");
        setPrdMap(prdMap);

        //每种产品类型在图表中对应的颜色
        prdcolorMap.put("0", "AFD8F8");
        prdcolorMap.put("1", "F6BD0F");
        prdcolorMap.put("2", "8BBA00");
        prdcolorMap.put("3", "A66EDD");
        prdcolorMap.put("4", "F984A1");
        setPrdcolorMap(prdcolorMap);

        //令牌的每种状态对应图表中的颜色
        stateColorMap.put("已启用", "AFD8F8");
        stateColorMap.put("未启用", "8BBA00");
        stateColorMap.put("已锁定", "AFD8F8");
        stateColorMap.put("未锁定", "8BBA00");
        stateColorMap.put("已挂失", "AFD8F8");
        stateColorMap.put("未挂失", "8BBA00");
        stateColorMap.put("已注销", "AFD8F8");
        stateColorMap.put("未注销", "8BBA00");
        stateColorMap.put("已绑定", "AFD8F8");
        stateColorMap.put("未绑定", "8BBA00");
        setStateColorMap(stateColorMap);
    }

    /**
     * 根据key取得Value
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static String getPhyValue(String key) {
        if (!StrTool.mapNotNull(phyMap)) {
            return null;
        }

        return phyMap.get(key);
    }

    public static String getPrdValue(String key) {
        if (!StrTool.mapNotNull(prdMap)) {
            return null;
        }

        return prdMap.get(key);

    }

    public static String getPrdColorValue(String key) {
        if (!StrTool.mapNotNull(prdcolorMap)) {
            return null;
        }

        return prdcolorMap.get(key);

    }

    public static String getphyColorValue(String key) {
        if (!StrTool.mapNotNull(phycolorMap)) {
            return null;
        }

        return phycolorMap.get(key);
    }

    public static String getStateColorValue(String key) {
        if (!StrTool.mapNotNull(stateColorMap)) {
            return null;
        }

        return stateColorMap.get(key);
    }

    /**
     * @return the phyMap
     */
    public static Map<String, String> getPhyMap() {
        return phyMap;
    }

    /**
     * @param phyMap the phyMap to set
     */
    public static void setPhyMap(Map<String, String> phyMap) {
        TokenConfig.phyMap = phyMap;
    }

    /**
     * @return the prdMap
     */
    public static Map<String, String> getPrdMap() {
        return prdMap;
    }

    /**
     * @param prdMap the prdMap to set
     */
    public static void setPrdMap(Map<String, String> prdMap) {
        TokenConfig.prdMap = prdMap;
    }

    /**
     * @return the prdcolorMap
     */
    public static Map<String, String> getPrdcolorMap() {
        return prdcolorMap;
    }

    /**
     * @param prdcolorMap the prdcolorMap to set
     */
    public static void setPrdcolorMap(Map<String, String> prdcolorMap) {
        TokenConfig.prdcolorMap = prdcolorMap;
    }

    /**
     * @return the phycolorMap
     */
    public static Map<String, String> getPhycolorMap() {
        return phycolorMap;
    }

    /**
     * @param phycolorMap the phycolorMap to set
     */
    public static void setPhycolorMap(Map<String, String> phycolorMap) {
        TokenConfig.phycolorMap = phycolorMap;
    }

    /**
     * @return the stateColorMap
     */
    public static Map<String, String> getStateColorMap() {
        return stateColorMap;
    }

    /**
     * @param stateColorMap the stateColorMap to set
     */
    public static void setStateColorMap(Map<String, String> stateColorMap) {
        TokenConfig.stateColorMap = stateColorMap;
    }

}
