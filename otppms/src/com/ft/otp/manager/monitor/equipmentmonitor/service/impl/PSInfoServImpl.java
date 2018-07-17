/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcTime;
import org.hyperic.sigar.ProcUtil;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;
import org.hyperic.sigar.SigarProxyCache;
import org.hyperic.sigar.shell.ShellCommandBase;

import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;

/**
 * 获取系统进行信息
 *
 * @Date in Sep 19, 2012,4:43:52 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class PSInfoServImpl extends ShellCommandBase implements IEquipmentMonitorServ {

    private Sigar sigar = null;
    private SigarProxy proxy = null;

    public PSInfoServImpl() {
        sigar = new Sigar();
        proxy = SigarProxyCache.newInstance(sigar);
    }

    public Object getObject() throws Exception {
        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        return monitor;
    }

    public List<Object> getObjects() throws Exception {
        List<Object> list = new ArrayList<Object>();
        long[] pids = proxy.getProcList();

        EquipmentMonitorInfo monitor = null;
        for (int i = 0; i < pids.length; i++) {
            long pid = pids[i];
            monitor = getMonitorInfo(proxy, pid);

            list.add(monitor);
        }

        return list;
    }

    private EquipmentMonitorInfo getMonitorInfo(SigarProxy proxy, long pid) throws Exception {
        ProcState state = proxy.getProcState(pid);
        ProcTime time = null;
        String unknown = "???";

        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        monitor.setPsPid(String.valueOf(pid));

        try {
            ProcCredName cred = proxy.getProcCredName(pid);
            monitor.setPsUser(cred.getUser());
        } catch (SigarException e) {
            monitor.setPsUser(unknown);
        }

        try {
            time = proxy.getProcTime(pid);
            monitor.setPsStartTime(getStartTime(time.getStartTime()));
        } catch (SigarException e) {
            monitor.setPsStartTime(unknown);
        }

        try {
            ProcMem mem = proxy.getProcMem(pid);
            monitor.setPsMemSize(Sigar.formatSize(mem.getSize()));
            monitor.setPsMemRss(Sigar.formatSize(mem.getResident()));
            monitor.setPsMemShare(Sigar.formatSize(mem.getShare()));
        } catch (SigarException e) {
            monitor.setPsMemSize(unknown);
        }
        monitor.setPsState(String.valueOf(state.getState()));

        String cpuPerc = "?";
        ProcCpu cpu = proxy.getProcCpu(pid);
        cpuPerc = CpuPerc.format(cpu.getPercent());
        if (time != null) {
            monitor.setPsCpuTime(cpuPerc);
        } else {
            monitor.setPsCpuTime(unknown);
        }

        String name = ProcUtil.getDescription(proxy, pid);
        monitor.setPsName(name);

        return monitor;
    }

    private String getStartTime(long time) {
        if (time == 0) {
            return "00:00";
        }
        long timeNow = System.currentTimeMillis();
        String fmt = "MMMd";

        if ((timeNow - time) < ((60 * 60 * 24) * 1000)) {
            fmt = "HH:mm";
        }

        return new SimpleDateFormat(fmt).format(new Date(time));
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#getObjectsToJson()
     */
    public String getObjectsToJson() throws Exception {
        return null;
    }

}
