/**
 *Administrator
 */
package com.ft.otp.manager.report.form.fusioncharts;

/**
 * 报表根节点主要属性对应
 *
 * @Date in Jan 26, 2013,1:48:13 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ElementRootInfo {
    private String caption = ""; // 一级标题
    private String subCaption = ""; // 二级标题
    private String xAxisName = ""; // x轴名称
    private String yAxisName = ""; // y轴名称  
    private String numberSuffix = ""; // 单位

    /**
     * @return the caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption the caption to set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @return the subCaption
     */
    public String getSubCaption() {
        return subCaption;
    }

    /**
     * @param subCaption the subCaption to set
     */
    public void setSubCaption(String subCaption) {
        this.subCaption = subCaption;
    }

    /**
     * @return the xAxisName
     */
    public String getXAxisName() {
        return xAxisName;
    }

    /**
     * @param axisName the xAxisName to set
     */
    public void setXAxisName(String axisName) {
        xAxisName = axisName;
    }

    /**
     * @return the yAxisName
     */
    public String getYAxisName() {
        return yAxisName;
    }

    /**
     * @param axisName the yAxisName to set
     */
    public void setYAxisName(String axisName) {
        yAxisName = axisName;
    }

    /**
     * @return the numberSuffix
     */
    public String getNumberSuffix() {
        return numberSuffix;
    }

    /**
     * @param numberSuffix the numberSuffix to set
     */
    public void setNumberSuffix(String numberSuffix) {
        this.numberSuffix = numberSuffix;
    }

}
