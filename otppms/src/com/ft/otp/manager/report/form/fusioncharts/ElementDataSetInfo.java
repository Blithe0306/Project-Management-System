/**
 *Administrator
 */
package com.ft.otp.manager.report.form.fusioncharts;

import java.util.List;

/**
 * 报表的dataset节点即系列对象 与 categories 集合配合使用 
 *
 * @Date in Jan 26, 2013,2:30:07 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ElementDataSetInfo {
    private String seriesName = "";// 系列的名称
    private String color = ""; // 系列的颜色
    private List<String> setValues = null; // 系列的所有set值

    /**
     * @return the seriesName
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * @param seriesName the seriesName to set
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the setValues
     */
    public List<String> getSetValues() {
        return setValues;
    }

    /**
     * @param setValues the setValues to set
     */
    public void setSetValues(List<String> setValues) {
        this.setValues = setValues;
    }

}
