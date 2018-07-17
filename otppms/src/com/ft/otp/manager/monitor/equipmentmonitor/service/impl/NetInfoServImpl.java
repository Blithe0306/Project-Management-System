/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hyperic.sigar.NetFlags;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;

/**
 * 获取网络流量等信息
 *
 * @Date in Sep 19, 2012,10:11:42 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class NetInfoServImpl implements IEquipmentMonitorServ {

    private Sigar sigar = null;

    public NetInfoServImpl() {
        sigar = new Sigar();
    }

    public Object getObject() throws Exception {
        return null;
    }

    public List<Object> getObjects() throws Exception {
        List<Object> list = new ArrayList<Object>();
        if (null == sigar) {
            return list;
        }

        EquipmentMonitorInfo monitor = null;
        String ifNames[] = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            monitor = setMonitor(name);

            list.add(monitor);
        }

        return list;
    }

    public EquipmentMonitorInfo setMonitor(String name) throws SigarException {
        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
        long flags = ifconfig.getFlags();

        String hwaddr = "";
        if (!NetFlags.NULL_HWADDR.equals(ifconfig.getHwaddr())) {
            hwaddr = ifconfig.getHwaddr();
        }

        if (!ifconfig.getName().equals(ifconfig.getDescription())) {
            monitor.setNetDescription(ifconfig.getDescription());
        }

        monitor.setNetName(ifconfig.getName());
        monitor.setNetType(ifconfig.getType());
        monitor.setNetHwaddr(hwaddr);

        String ptp = "";
        if ((flags & NetFlags.IFF_POINTOPOINT) > 0) {
            ptp = "  P-t-P:" + ifconfig.getDestination();
        }

        String bcast = "";
        if ((flags & NetFlags.IFF_BROADCAST) > 0) {
            bcast = ifconfig.getBroadcast();
        }
        monitor.setNetBcast(bcast);
        monitor.setNetAddress(ifconfig.getAddress() + ptp);
        monitor.setNetNetmask(ifconfig.getNetmask());

        NetInfo info = sigar.getNetInfo();
        monitor.setNetGateway(info.getDefaultGateway());
        monitor.setNetDns(info.getPrimaryDns());

        monitor.setNetMtu(NetFlags.getIfFlagsString(flags) + "&nbsp;&nbsp;&nbsp;&nbsp;MTU：" + ifconfig.getMtu());
        monitor.setNetMetric(String.valueOf(ifconfig.getMetric()));

        NetInterfaceStat ifstat = this.sigar.getNetInterfaceStat(name);

        monitor.setNetRxPackets(String.valueOf(ifstat.getRxPackets()));
        monitor.setNetTxPackets(String.valueOf(ifstat.getTxPackets()));
        monitor.setNetRxErrors(String.valueOf(ifstat.getRxErrors()));
        monitor.setNetTxErrors(String.valueOf(ifstat.getTxErrors()));
        monitor.setNetRxDropped(String.valueOf(ifstat.getRxDropped()));
        monitor.setNetTxDropped(String.valueOf(ifstat.getTxDropped()));

        long rxBytes = ifstat.getRxBytes();
        long txBytes = ifstat.getTxBytes();
        monitor.setNetRxBytes(rxBytes + " (" + Sigar.formatSize(rxBytes) + ")");
        monitor.setNetTxBytes(txBytes + " (" + Sigar.formatSize(txBytes) + ")");

        return monitor;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#getObjectsToJson()
     */
    public String getObjectsToJson() throws Exception {
        return null;
    }

}
