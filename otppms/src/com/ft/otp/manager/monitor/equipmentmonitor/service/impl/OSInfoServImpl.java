/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.service.impl;

import java.util.List;
import java.util.Properties;

import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;

import com.ft.otp.manager.login.service.OnlineUsers;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;

/**
 * 获取系统环境基本信息属性
 *
 * @Date in Sep 18, 2012,4:02:51 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class OSInfoServImpl implements IEquipmentMonitorServ {

    private OperatingSystem os = null;
    private Sigar sigar = null;

    public OSInfoServImpl() {
        os = OperatingSystem.getInstance();
        sigar = new Sigar();
    }

    public Object getObject() throws Exception {
        EquipmentMonitorInfo monitor = new EquipmentMonitorInfo();
        if (null == os) {
            return monitor;
        }

        //系统环境基本信息属性
        monitor.setOsDescription(os.getDescription());
        monitor.setOsName(os.getName());
        monitor.setOsArch(os.getArch());
        monitor.setOsMachine(os.getMachine());
        monitor.setOsVersion(os.getVersion());
        monitor.setOsPatchLevel(os.getPatchLevel());
        monitor.setOsVendor(os.getVendor());
        monitor.setOsVendorVersion(os.getVendorVersion());
        monitor.setOsVendorCodeName(os.getVendorCodeName());
        monitor.setOsDataModel(os.getDataModel());
        monitor.setOsCpuEndian(os.getCpuEndian());

        //计算机用户及其它信息
        NetInterfaceConfig config = sigar.getNetInterfaceConfig(null);
        NetInfo info = sigar.getNetInfo();
        monitor.setOtUserName(System.getProperty("user.name"));
        monitor.setOtUserDomain(info.getDomainName());
        monitor.setOtHostAddress(config.getAddress());
        monitor.setOtHostName(info.getHostName());

        int usersCount = OnlineUsers.getUsersCount();
        monitor.setOtActiveUser(String.valueOf(usersCount));

        //Java环境属性信息
        Properties props = System.getProperties();
        monitor.setJvmVersion(props.getProperty("java.version"));
        monitor.setJvmVendor(props.getProperty("java.vendor"));
        monitor.setJvmHome(props.getProperty("java.home"));
        monitor.setJvmVmVersion(props.getProperty("java.vm.version"));
        monitor.setJvmVmVendor(props.getProperty("java.vm.specification.vendor"));
        monitor.setJvmVmName(props.getProperty("java.vm.specification.name"));

        return monitor;
    }

    public List<Object> getObjects() throws Exception {
        return null;
    }

    /* (non-Javadoc)
     * @see com.ft.otp.manager.monitor.service.IMonitorServ#getObjectsToJson()
     */
    public String getObjectsToJson() throws Exception {
        return null;
    }

}
