/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Swap;

import com.ft.otp.manager.monitor.equipmentmonitor.aide.MonitorServiceAide;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;

/**
 * 获取内存基本信息属性
 *
 * @Date in Mar 11, 2013,4:56:45 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MemInfoServImpl implements IEquipmentMonitorServ {

    private Sigar sigar = null;

    public MemInfoServImpl() {
        sigar = new Sigar();
    }

    public Object getObject() throws Exception {
        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        if (null == sigar) {
            return monitor;
        }

        Mem mem = sigar.getMem();
        monitor.setMemTotal(mem.getTotal() / 1024L + " KB");
        monitor.setMemUsed(mem.getUsed() / 1024L + " KB");
        monitor.setMemFree(mem.getFree() / 1024L + " KB");
        double memUse = (double) mem.getUsed() / (double) mem.getTotal() * 100D;
        String result = String.format("%.1f", memUse);
        monitor.setMemUse(result + "%");

        monitor.setMemActualUsed(mem.getActualUsed() / 1024L + " KB");
        monitor.setMemActualFree(mem.getActualFree() / 1024L + " KB");
        double memActualUse = (double) mem.getActualUsed() / (double) mem.getTotal() * 100D;
        result = String.format("%.1f", memActualUse);
        monitor.setMemActualUse(result + "%");

        monitor.setMemRam(mem.getRam() + " MB");

        Swap swap = sigar.getSwap();
        monitor.setSwapTotal(swap.getTotal() / 1024L + " KB");
        monitor.setSwapUsed(swap.getUsed() / 1024L + " KB");
        monitor.setSwapFree(swap.getFree() / 1024L + " KB");

        double swapUse = (double) swap.getUsed() / (double) swap.getTotal() * 100D;
        result = String.format("%.1f", swapUse);
        monitor.setSwapUse(result + "%");

        return monitor;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#getObjectsToJson()
     */
    public String getObjectsToJson() throws Exception {
        EquipmentMonitorInfo monitor = (EquipmentMonitorInfo) getObject();
        List<EquipmentMonitorInfo> memList = new ArrayList<EquipmentMonitorInfo>();
        // 内存(Mem)
        EquipmentMonitorInfo monitor1 = new EquipmentMonitorInfo();
        monitor1.setMemTypeName("内存(Mem)");
        monitor1.setMemTotal(monitor.getMemTotal());
        monitor1.setMemUsed(monitor.getMemUsed());
        monitor1.setMemFree(monitor.getMemFree());
        monitor1.setMemUse(monitor.getMemUse());
        memList.add(monitor1);
        // 实际内存使用情况
        EquipmentMonitorInfo monitor2 = new EquipmentMonitorInfo();
        monitor2.setMemTypeName("实际内存使用情况(-/+ buffers/cache)");
        monitor2.setMemTotal(monitor.getMemTotal());
        monitor2.setMemUsed(monitor.getMemActualUsed());
        monitor2.setMemFree(monitor.getMemActualFree());
        monitor2.setMemUse(monitor.getMemActualUse());
        memList.add(monitor2);
        // 交换区(Swap)
        EquipmentMonitorInfo monitor3 = new EquipmentMonitorInfo();
        monitor3.setMemTypeName("交换区(Swap)");
        monitor3.setSwapTotal(monitor.getSwapTotal());
        monitor3.setMemUsed(monitor.getSwapUsed());
        monitor3.setMemFree(monitor.getSwapFree());
        monitor3.setMemUse(monitor.getSwapUse());
        memList.add(monitor3);

        return MonitorServiceAide.getJsonFromList(memList.size(), memList, null);
    }

    public List<Object> getObjects() throws Exception {
        return null;
    }

}
