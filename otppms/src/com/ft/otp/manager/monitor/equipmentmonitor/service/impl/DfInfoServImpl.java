/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;

import com.ft.otp.manager.monitor.equipmentmonitor.aide.MonitorServiceAide;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;


/**
 * 获取磁盘空间基本信息属性
 *
 * @Date in Mar 11, 2013,4:56:35 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class DfInfoServImpl implements IEquipmentMonitorServ {

    private Sigar sigar = null;

    public DfInfoServImpl() {
        sigar = new Sigar();
    }

    public Object getObject() throws Exception {
        return null;
    }

    public List<Object> getObjects() throws Exception {
        List<Object> fsList = new ArrayList<Object>();
        if (null == sigar) {
            return fsList;
        }

        FileSystem fslist[] = sigar.getFileSystemList();
        EquipmentMonitorInfo monitor = null;
        for (int i = 0; i < fslist.length; i++) {
            FileSystem fs = fslist[i];
            monitor = new EquipmentMonitorInfo();
            monitor.setDfDevName(fs.getDevName());
            monitor.setDfDirName(fs.getDirName());
            monitor.setDfSysTypeName(fs.getSysTypeName());
            monitor.setDfTypeName(fs.getTypeName());

            FileSystemUsage usage = null;
            int type = fs.getType();
            try {
                usage = sigar.getFileSystemUsage(fs.getDirName());
                //TYPE_LOCAL_DISK 本地硬盘
                if (type == 2) {
                    // 文件系统总大小
                    monitor.setDfTotal(Sigar.formatSize(usage.getTotal() * 1024));
                    // 文件系统剩余大小
                    monitor.setDfFree(Sigar.formatSize(usage.getFree() * 1024));
                    // 文件系统可用大小
                    monitor.setDfAvail(Sigar.formatSize(usage.getAvail() * 1024));
                    // 文件系统已经使用量
                    monitor.setDfUsed(Sigar.formatSize(usage.getUsed() * 1024));
                    // 文件系统资源的利用率
                    double usePercent = usage.getUsePercent() * 100D;
                    String result = String.format("%.1f", usePercent);
                    monitor.setDfUsePercent(result + "%");
                }
            } catch (Exception e) {
                monitor.setDfTotal("0");
                monitor.setDfFree("0");
                monitor.setDfAvail("0");
                monitor.setDfUsed("0");
                monitor.setDfUsePercent("-");
            }

            monitor.setDfType(getType(type));
            fsList.add(monitor);
        }

        return fsList;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#getObjectsToJson()
     */
    public String getObjectsToJson() throws Exception {
        List<Object> fsList = getObjects();
        return MonitorServiceAide.getJsonFromList(fsList.size(), fsList, null);
    }

    private String getType(int type) {
        switch (type) {
            case 0: //TYPE_UNKNOWN 未知
                return "UNKNOWN";
            case 1: //TYPE_NONE
                return "NONE";
            case 2: //TYPE_LOCAL_DISK 本地硬盘
                return "本地硬盘";
            case 3://TYPE_NETWORK 网络
                return "NETWORK";
            case 4://TYPE_RAM_DISK 闪存
                return "RAM 闪存";
            case 5://TYPE_CDROM 光驱
                return "CDROM";
            case 6://TYPE_SWAP 页面交换
                return "SWAP";
        }

        return "";
    }

}
