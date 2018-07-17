/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service;

import java.util.List;

/**
 * 系统监控功能模块业务集中处理接口
 *
 * @Date in Sep 18, 2012,3:57:22 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public interface IEquipmentMonitorServ {

    /**
     * 获取系统资源信息对象(系统信息、CUP、内存等)
     * 
     * @Date in Sep 18, 2012,3:59:17 PM
     * @return
     * @throws Exception
     */
    Object getObject() throws Exception;

    /**
     * 获取系统资源信息对象(系统信息、CUP、内存等)
     * 
     * @Date in Sep 18, 2012,4:00:50 PM
     * @return
     * @throws Exception
     */
    List<Object> getObjects() throws Exception;

    /**
     * 获取系统资源对象并以JSON格式返回
     * 方法说明
     * @Date in Feb 28, 2013,10:24:47 AM
     * @return
     * @throws Exception
     */
    String getObjectsToJson() throws Exception;

}
