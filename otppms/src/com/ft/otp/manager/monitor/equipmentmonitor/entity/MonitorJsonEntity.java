/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.entity;

import java.util.List;

/**
 * JSON实体对象类
 * 用于临时存放格式化前数据
 *
 * @Date in Mar 11, 2013,11:33:49 AM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorJsonEntity {

    //结果总数
    private long results;
    //数据列表
    private List<?> items;
    //错误提示
    private String errorStr;
    //实体对象
    private Object object;

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }

    public long getResults() {
        return results;
    }

    public void setResults(long results) {
        this.results = results;
    }

    /**
     * @return the errorStr
     */
    public String getErrorStr() {
        return errorStr;
    }

    /**
     * @param errorStr the errorStr to set
     */
    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }

}
