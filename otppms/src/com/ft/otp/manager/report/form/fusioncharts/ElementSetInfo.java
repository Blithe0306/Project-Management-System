/**
 *Administrator
 */
package com.ft.otp.manager.report.form.fusioncharts;

/**
 * 统计报表的Set元素 节点对象
 *
 * @Date in Jan 26, 2013,1:35:41 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ElementSetInfo {
    private String name = ""; // set 的名称
    private String value = ""; // set 的值
    private String color = ""; // set 的颜色

    public ElementSetInfo(String name, String value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
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

}
