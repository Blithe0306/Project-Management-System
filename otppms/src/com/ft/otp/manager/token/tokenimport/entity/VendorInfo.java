/**
 *Administrator
 */
package com.ft.otp.manager.token.tokenimport.entity;

/**
 * 多厂商实体信息
 *
 * @Date in Apr 1, 2013,1:40:17 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class VendorInfo {

    private String vendorId;
    private String name;
    private boolean normal;
    private String classPath;
    private String jarPath;

    /**
     * @return the vendorId
     */
    public String getVendorId() {
        return vendorId;
    }

    /**
     * @param vendorId the vendorId to set
     */
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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
     * @return the normal
     */
    public boolean getNormal() {
        return normal;
    }

    /**
     * @param normal the normal to set
     */
    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    /**
     * @return the classPath
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * @param classPath the classPath to set
     */
    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    /**
     * @return the jarPath
     */
    public String getJarPath() {
        return jarPath;
    }

    /**
     * @param jarPath the jarPath to set
     */
    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

}
