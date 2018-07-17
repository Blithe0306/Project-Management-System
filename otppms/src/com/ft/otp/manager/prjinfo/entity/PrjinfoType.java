/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date Dec 25, 2014,10:37:58 AM
 */
package com.ft.otp.manager.prjinfo.entity;

/**
 * 定制信息归类
 * @Date Dec 25, 2014,10:37:58 AM
 * @version v1.0
 * @author ZWX
 */
public class PrjinfoType {

    private String  typeid;     // 定制信息归类编号
    private String  typeName;   // 定制信息归类名称
    
    public String getTypeid() {
        return typeid;
    }
    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
