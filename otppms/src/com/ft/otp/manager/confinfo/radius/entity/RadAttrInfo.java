package com.ft.otp.manager.confinfo.radius.entity;

import java.util.Map;
import com.ft.otp.base.entity.BaseEntity;

/**
 * Radius配置属性实体
 * 
 * @Date in Oct 29, 2012,3:20:38 PM
 * 
 * @version v1.0
 * 
 * @author YXS
 */
public class RadAttrInfo extends BaseEntity {

    private int id;//主键
    private int attrType;//
    private String attrId; //属性ID
    private String attrName; //属性名称
    private String attrValue;//属性值
    private String attrValueType;//属性值类型
    private int profileId = -1;//RADIUS配置ID
    private int vendorid = -1;// 厂商ID
    private String vendorname;
    private int flag; // SQL拼接标志
    private Map valueMap; //radius配置属性的子属性
    private String attrValueToName; // 存储属性值
    
    public String getAttrValueToName() {
		return attrValueToName;
	}
	public void setAttrValueToName(String attrValueToName) {
		this.attrValueToName = attrValueToName;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getVendorname() {
		return vendorname;
	}
	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	public int getVendorid() {
		return vendorid;
	}
	public void setVendorid(int vendorid) {
		this.vendorid = vendorid;
	}
	public String getAttrId() {
        return attrId;
    }
    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }
    public String getAttrName() {
        return attrName;
    }
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }
    public String getAttrValue() {
        return attrValue;
    }
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
    public String getAttrValueType() {
        return attrValueType;
    }
    public void setAttrValueType(String attrValueType) {
        this.attrValueType = attrValueType;
    }
    public int getProfileId() {
        return profileId;
    }
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }
    public Map getValueMap() {
        return valueMap;
    }
    public void setValueMap(Map valueMap) {
        this.valueMap = valueMap;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getAttrType() {
        return attrType;
    }
    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

}
