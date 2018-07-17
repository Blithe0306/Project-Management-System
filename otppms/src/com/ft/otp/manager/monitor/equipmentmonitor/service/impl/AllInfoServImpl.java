/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ft.otp.manager.monitor.equipmentmonitor.aide.MonitorServiceAide;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;

/**
 * 获取设备所有信息实现类
 *
 * @Date in Mar 25, 2013,4:57:26 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class AllInfoServImpl implements IEquipmentMonitorServ {

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ#getObject()
     */
    public Object getObject() throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ#getObjects()
     */
    public List<Object> getObjects() throws Exception {
        List<Object> allList = new ArrayList<Object>();
        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        // 获取各个信息的服务接口
        // cpu 
        IEquipmentMonitorServ cpuServ = new CPUInfoServImpl();
        List<Object> cpuList = cpuServ.getObjects();
        if (cpuList != null && cpuList.size() > 0) {
            monitor = (EquipmentMonitorInfo) cpuList.get(0);
        }

        //mem
        IEquipmentMonitorServ memServ = new MemInfoServImpl();
        EquipmentMonitorInfo memMonitor = (EquipmentMonitorInfo) memServ.getObject();
        if (memMonitor != null) {
            monitor.setMemActualFree(memMonitor.getMemActualFree());
            monitor.setMemActualUse(memMonitor.getMemActualUse());
            monitor.setMemActualUsed(memMonitor.getMemActualUsed());
            monitor.setMemFree(memMonitor.getMemFree());
            monitor.setMemRam(memMonitor.getMemRam());
            monitor.setMemTotal(memMonitor.getMemTotal());
            monitor.setMemTypeName(memMonitor.getMemTypeName());
            monitor.setMemUse(memMonitor.getMemUse());
            monitor.setMemUsed(memMonitor.getMemUsed());
        }

        // disk 此处装入的是几个磁盘之和的值 
        IEquipmentMonitorServ diskServ = new DfInfoServImpl();
        List<Object> dfList = diskServ.getObjects();
        monitor.setDiskInfo(dfList);
        if (dfList != null && dfList.size() > 0) {
            double dfTotal = 0;
            double dfUsed = 0;
            for (int i = 0; i < dfList.size(); i++) {
                try {
                    EquipmentMonitorInfo monitorInfo = (EquipmentMonitorInfo) dfList.get(i);
                    dfTotal += MonitorServiceAide.parseDouble((MonitorServiceAide.replaceChar(monitorInfo.getDfTotal(),
                            "G")));
                    dfUsed += MonitorServiceAide.parseDouble(MonitorServiceAide.replaceChar(monitorInfo.getDfUsed(),
                            "G"));
                } catch (Exception e) {
                    continue;
                }
            }
            monitor.setDfTotal(dfTotal + "G");
            monitor.setDfTotal(dfUsed + "G");
            String userPercent = "0";
            if (dfTotal != 0) {
                userPercent = dfUsed * 100 / dfTotal + "%";
            }
            monitor.setDfUsePercent(userPercent);
        }
        allList.add(monitor);

        return allList;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ#getObjectsToJson()
     */
    public String getObjectsToJson() throws Exception {
        List<Object> allList = getObjects();
        String jsonData = MonitorServiceAide.getJsonFromList(allList.size(), allList, null);

        return jsonData;
    }

}
