/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

import com.ft.otp.manager.monitor.equipmentmonitor.aide.MonitorServiceAide;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;

/**
 * 获取CPU基本信息属性
 *
 * @Date in Mar 11, 2013,4:56:22 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class CPUInfoServImpl implements IEquipmentMonitorServ {

    private Sigar sigar = null;

    public CPUInfoServImpl() {
        sigar = new Sigar();
    }

    public Object getObject() throws Exception {
        return null;
    }

    public List<Object> getObjects() throws Exception {
        List<Object> cpuList = new ArrayList<Object>();
        if (null == sigar) {
            return cpuList;
        }
        CpuInfo[] infos = sigar.getCpuInfoList();
        CpuInfo cpuInfo = infos[0];
        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        monitor.setCpuVendor(cpuInfo.getVendor());
        monitor.setCpuModel(cpuInfo.getModel());
        monitor.setCpuMhz(String.valueOf(cpuInfo.getMhz()));
        monitor.setCpuTotalCores(String.valueOf(cpuInfo.getTotalCores()));

        long cacheSize = cpuInfo.getCacheSize();
        if (cacheSize != Sigar.FIELD_NOTIMPL) {
            monitor.setCpuCacheSize(String.valueOf(cpuInfo.getCacheSize()));
        } else {
            monitor.setCpuCacheSize("0");
        }

        CpuPerc cpuPerc = sigar.getCpuPerc();
        monitor = setMonitor(monitor, cpuPerc);//不管是单块CPU还是多CPU都适用

        cpuList.add(monitor);

        /*
        CpuPerc[] cpus = this.sigar.getCpuPercList();
        for (int i = 0; i < cpus.length; i++) {
            CpuPerc perc = cpus[i];
            monitor = new MonitorInfo();
            monitor = setMonitor(monitor, perc);
            cpuList.add(monitor);
        }*/

        return cpuList;
    }

    /**
     * 获取cpu对象列表的json数据
     * 方法说明
     * @Date in Feb 28, 2013,10:20:59 AM
     * @return
     * @throws Exception
     */
    public String getObjectsToJson() throws Exception {
        List<Object> cpuList = getObjects();
        String jsonData = MonitorServiceAide.getJsonFromList(cpuList.size(), cpuList, null);

        return jsonData;
    }

    private EquipmentMonitorInfo setMonitor(EquipmentMonitorInfo monitor, CpuPerc cpu) {
        monitor.setCpuUserTime(CpuPerc.format(cpu.getUser()));
        monitor.setCpuSysTime(CpuPerc.format(cpu.getSys()));
        monitor.setCpuIdleTime(CpuPerc.format(cpu.getIdle()));
        monitor.setCpuWaitTime(CpuPerc.format(cpu.getWait()));
        monitor.setCpuNiceTime(CpuPerc.format(cpu.getNice()));
        monitor.setCpuCombined(CpuPerc.format(cpu.getCombined()));
        monitor.setCpuIrqTime(CpuPerc.format(cpu.getIrq()));

        return monitor;
    }

}
