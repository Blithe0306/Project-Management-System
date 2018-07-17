/**
 *Administrator
 */
package com.ft.otp.manager.report.form.fusioncharts;

import java.util.List;

/**
 * 构建 报表的xml的信息  获取xml时 将转入值的此对像传入即可
 *
 * @Date in Jan 26, 2013,1:34:38 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class ElementInfo {
    // 根节点Element
    private ElementRootInfo elementRootInfo = null;

    // 单系列柱状图 或 饼图使用集合
    private List<ElementSetInfo> elementSets = null; // set 集合 

    // 多系列 柱状图和线型图 使用集合
    private List<String> categories = null; // 种类名称 即 x轴上的名称
    private List<ElementDataSetInfo> dataSets = null; // x轴上的数据

    /**
     * @return the elementRootInfo
     */
    public ElementRootInfo getElementRootInfo() {
        return elementRootInfo;
    }

    /**
     * @param elementRootInfo the elementRootInfo to set
     */
    public void setElementRootInfo(ElementRootInfo elementRootInfo) {
        this.elementRootInfo = elementRootInfo;
    }

    /**
     * @return the elementSets
     */
    public List<ElementSetInfo> getElementSets() {
        return elementSets;
    }

    /**
     * @param elementSets the elementSets to set
     */
    public void setElementSets(List<ElementSetInfo> elementSets) {
        this.elementSets = elementSets;
    }

    /**
     * @return the categories
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * @param categories the categories to set
     */
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    /**
     * @return the dataSets
     */
    public List<ElementDataSetInfo> getDataSets() {
        return dataSets;
    }

    /**
     * @param dataSets the dataSets to set
     */
    public void setDataSets(List<ElementDataSetInfo> dataSets) {
        this.dataSets = dataSets;
    }

}
