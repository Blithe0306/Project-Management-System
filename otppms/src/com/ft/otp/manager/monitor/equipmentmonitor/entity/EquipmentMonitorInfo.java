/**
 *Administrator
 */
package com.ft.otp.manager.monitor.equipmentmonitor.entity;

import java.util.List;

/**
 * 系统监控业务实体
 *
 * @Date in Sep 18, 2012,10:07:50 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class EquipmentMonitorInfo {

    /**
     * 系统环境基本信息属性
     */
    private String osDescription; //系统描述
    private String osName; //操作系统类型
    private String osArch; //操作系统内核类型如：386、486、586等x86
    private String osMachine; //硬件设备制造商
    private String osVersion; //操作系统版本号
    private String osPatchLevel; //操作系统补丁
    private String osVendor; //操作系统制造商
    private String osVendorVersion; //操作系统版本类型
    private String osVendorCodeName; //制造商名称
    private String osDataModel; //操作系统数据模型
    private String osCpuEndian; //操作系统CPU字节序类型

    /**
     * 计算机用户及其它信息
     */
    private String otUserName; //用户名
    private String otUserDomain;//计算机域名
    private String otHostAddress;//IP地址
    private String otHostName;//主机名

    private String otActiveUser;//当前在线用户

    /**
     * JVM虚拟机基本信息
     */
    private String jvmVersion;//Java的运行环境版本
    private String jvmVendor;//Java的运行环境供应商
    private String jvmHome;//Java的安装路径
    private String jvmVmVersion;//Java的虚拟机实现版本
    private String jvmVmVendor;//Java的虚拟机规范供应商
    private String jvmVmName;//Java的虚拟机规范名称

    /**
     * CPU基本信息属性
     */
    private String cpuVendor; //CPU制造商
    private String cpuModel; //CPU类别
    private String cpuMhz;//CPU总量
    private String cpuTotalCores; //CPU总数
    private String cpuCacheSize; //缓冲存储器数量

    private String cpuUserTime;//用户使用率
    private String cpuSysTime;//系统使用率
    private String cpuWaitTime;//当前等待率
    private String cpuIdleTime;//当前空闲率
    private String cpuCombined;//总的使用率
    private String cpuIrqTime; //中断请求
    private String cpuNiceTime;//

    /**
     * 内存基本信息属性
     */
    private String memTypeName;//内存类型名称
    private String memTotal;//内存总量
    private String memUsed;//当前内存使用量
    private String memFree;//当前内存剩余量
    private String memUse;//当前内存利用率

    private String memActualUsed;//实际内存使用量
    private String memActualFree;//实际内存剩余量
    private String memActualUse;//实际内存利用率

    private String memRam;//随机存取存储器，缓存

    //交换区信息
    private String swapTotal;//交换区总量
    private String swapUsed;//当前交换区使用量
    private String swapFree;//当前交换区剩余量
    private String swapUse;//交换区利用率

    /**
     * 磁盘空间基本信息属性
     */
    private String dfDevName = ""; //盘符名称
    private String dfDirName = ""; //盘符名称
    private String dfSysTypeName = ""; //文件系统类型，比如 FAT32、NTFS
    private String dfTypeName = ""; //文件系统类型名，比如本地硬盘、光驱、网络文件系统等
    private String dfType = "";//文件系统类型 0:TYPE_UNKNOWN 未知 1:TYPE_NONE 2:TYPE_LOCAL_DISK 本地硬盘 
    //3:TYPE_NETWORK 网络 4:TYPE_RAM_DISK 闪存 5:TYPE_CDROM 光驱 6:TYPE_SWAP 页面交换
    private String dfTotal = ""; //文件系统总大小
    private String dfFree = ""; //文件系统剩余大小
    private String dfAvail = ""; //文件系统可用大小
    private String dfUsed = ""; //文件系统已经使用量
    private String dfUsePercent = "";//文件系统资源的利用率

    List<Object> diskInfo;//多个磁盘信息

    /**
     * 网络流量等信息
     */
    private String netDescription;//网卡描述信息
    private String netName; //网络设备名
    private String netHwaddr;//网卡MAC地址   
    private String netType;//网络类型
    private String netAddress;//IP地址
    private String netBcast;//广播地址
    private String netNetmask;//子网掩码
    private String netMtu;//最大传输单元
    private String netMetric;//路由尺度，可不显示
    private String netGateway;//默认网关
    private String netDns;//域名服务器

    private String netRxPackets; //接收的总包裹数
    private String netTxPackets;//发送的总包裹数
    private String netRxBytes;//接收到的总字节数
    private String netTxBytes;//发送的总字节数
    private String netRxErrors;//接收到的错误包数
    private String netTxErrors;//发送数据包时的错误数
    private String netRxDropped;//接收时丢弃的包数
    private String netTxDropped;//发送时丢弃的包数

    /**
     * 系统进行信息
     */
    private String psPid; //进程ID
    private String psUser; //进程用户
    private String psStartTime;//开始时间
    private String psMemSize;//虚拟内存
    private String psMemRss;//已经使用的内存
    private String psMemShare;//分享内存
    private String psState;//进程状态
    private String psCpuTime;//CPU占用率
    private String psName;//进程名称

    /**
     * @return the osDescription
     */
    public String getOsDescription() {
        return osDescription;
    }

    /**
     * @param osDescription the osDescription to set
     */
    public void setOsDescription(String osDescription) {
        this.osDescription = osDescription;
    }

    /**
     * @return the osName
     */
    public String getOsName() {
        return osName;
    }

    /**
     * @param osName the osName to set
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * @return the osArch
     */
    public String getOsArch() {
        return osArch;
    }

    /**
     * @param osArch the osArch to set
     */
    public void setOsArch(String osArch) {
        this.osArch = osArch;
    }

    /**
     * @return the osMachine
     */
    public String getOsMachine() {
        return osMachine;
    }

    /**
     * @param osMachine the osMachine to set
     */
    public void setOsMachine(String osMachine) {
        this.osMachine = osMachine;
    }

    /**
     * @return the osVersion
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * @param osVersion the osVersion to set
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * @return the osPatchLevel
     */
    public String getOsPatchLevel() {
        return osPatchLevel;
    }

    /**
     * @param osPatchLevel the osPatchLevel to set
     */
    public void setOsPatchLevel(String osPatchLevel) {
        this.osPatchLevel = osPatchLevel;
    }

    /**
     * @return the osVendor
     */
    public String getOsVendor() {
        return osVendor;
    }

    /**
     * @param osVendor the osVendor to set
     */
    public void setOsVendor(String osVendor) {
        this.osVendor = osVendor;
    }

    /**
     * @return the osVendorVersion
     */
    public String getOsVendorVersion() {
        return osVendorVersion;
    }

    /**
     * @param osVendorVersion the osVendorVersion to set
     */
    public void setOsVendorVersion(String osVendorVersion) {
        this.osVendorVersion = osVendorVersion;
    }

    /**
     * @return the osVendorCodeName
     */
    public String getOsVendorCodeName() {
        return osVendorCodeName;
    }

    /**
     * @param osVendorCodeName the osVendorCodeName to set
     */
    public void setOsVendorCodeName(String osVendorCodeName) {
        this.osVendorCodeName = osVendorCodeName;
    }

    /**
     * @return the osDataModel
     */
    public String getOsDataModel() {
        return osDataModel;
    }

    /**
     * @param osDataModel the osDataModel to set
     */
    public void setOsDataModel(String osDataModel) {
        this.osDataModel = osDataModel;
    }

    /**
     * @return the osCpuEndian
     */
    public String getOsCpuEndian() {
        return osCpuEndian;
    }

    /**
     * @param osCpuEndian the osCpuEndian to set
     */
    public void setOsCpuEndian(String osCpuEndian) {
        this.osCpuEndian = osCpuEndian;
    }

    /**
     * @return the otUserName
     */
    public String getOtUserName() {
        return otUserName;
    }

    /**
     * @param otUserName the otUserName to set
     */
    public void setOtUserName(String otUserName) {
        this.otUserName = otUserName;
    }

    /**
     * @return the otUserDomain
     */
    public String getOtUserDomain() {
        return otUserDomain;
    }

    /**
     * @param otUserDomain the otUserDomain to set
     */
    public void setOtUserDomain(String otUserDomain) {
        this.otUserDomain = otUserDomain;
    }

    /**
     * @return the otHostAddress
     */
    public String getOtHostAddress() {
        return otHostAddress;
    }

    /**
     * @param otHostAddress the otHostAddress to set
     */
    public void setOtHostAddress(String otHostAddress) {
        this.otHostAddress = otHostAddress;
    }

    /**
     * @return the otHostName
     */
    public String getOtHostName() {
        return otHostName;
    }

    /**
     * @param otHostName the otHostName to set
     */
    public void setOtHostName(String otHostName) {
        this.otHostName = otHostName;
    }

    /**
     * @return the otActiveUser
     */
    public String getOtActiveUser() {
        return otActiveUser;
    }

    /**
     * @param otActiveUser the otActiveUser to set
     */
    public void setOtActiveUser(String otActiveUser) {
        this.otActiveUser = otActiveUser;
    }

    /**
     * @return the cpuVendor
     */
    public String getCpuVendor() {
        return cpuVendor;
    }

    /**
     * @param cpuVendor the cpuVendor to set
     */
    public void setCpuVendor(String cpuVendor) {
        this.cpuVendor = cpuVendor;
    }

    /**
     * @return the cpuModel
     */
    public String getCpuModel() {
        return cpuModel;
    }

    /**
     * @param cpuModel the cpuModel to set
     */
    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    /**
     * @return the cpuMhz
     */
    public String getCpuMhz() {
        return cpuMhz;
    }

    /**
     * @param cpuMhz the cpuMhz to set
     */
    public void setCpuMhz(String cpuMhz) {
        this.cpuMhz = cpuMhz;
    }

    /**
     * @return the cpuTotalCores
     */
    public String getCpuTotalCores() {
        return cpuTotalCores;
    }

    /**
     * @param cpuTotalCores the cpuTotalCores to set
     */
    public void setCpuTotalCores(String cpuTotalCores) {
        this.cpuTotalCores = cpuTotalCores;
    }

    /**
     * @return the cpuCacheSize
     */
    public String getCpuCacheSize() {
        return cpuCacheSize;
    }

    /**
     * @param cpuCacheSize the cpuCacheSize to set
     */
    public void setCpuCacheSize(String cpuCacheSize) {
        this.cpuCacheSize = cpuCacheSize;
    }

    /**
     * @return the cpuUserTime
     */
    public String getCpuUserTime() {
        return cpuUserTime;
    }

    /**
     * @param cpuUserTime the cpuUserTime to set
     */
    public void setCpuUserTime(String cpuUserTime) {
        this.cpuUserTime = cpuUserTime;
    }

    /**
     * @return the cpuSysTime
     */
    public String getCpuSysTime() {
        return cpuSysTime;
    }

    /**
     * @param cpuSysTime the cpuSysTime to set
     */
    public void setCpuSysTime(String cpuSysTime) {
        this.cpuSysTime = cpuSysTime;
    }

    /**
     * @return the cpuWaitTime
     */
    public String getCpuWaitTime() {
        return cpuWaitTime;
    }

    /**
     * @param cpuWaitTime the cpuWaitTime to set
     */
    public void setCpuWaitTime(String cpuWaitTime) {
        this.cpuWaitTime = cpuWaitTime;
    }

    /**
     * @return the cpuIdleTime
     */
    public String getCpuIdleTime() {
        return cpuIdleTime;
    }

    /**
     * @param cpuIdleTime the cpuIdleTime to set
     */
    public void setCpuIdleTime(String cpuIdleTime) {
        this.cpuIdleTime = cpuIdleTime;
    }

    /**
     * @return the cpuCombined
     */
    public String getCpuCombined() {
        return cpuCombined;
    }

    /**
     * @param cpuCombined the cpuCombined to set
     */
    public void setCpuCombined(String cpuCombined) {
        this.cpuCombined = cpuCombined;
    }

    /**
     * @return the cpuIrqTime
     */
    public String getCpuIrqTime() {
        return cpuIrqTime;
    }

    /**
     * @param cpuIrqTime the cpuIrqTime to set
     */
    public void setCpuIrqTime(String cpuIrqTime) {
        this.cpuIrqTime = cpuIrqTime;
    }

    /**
     * @return the cpuNiceTime
     */
    public String getCpuNiceTime() {
        return cpuNiceTime;
    }

    /**
     * @param cpuNiceTime the cpuNiceTime to set
     */
    public void setCpuNiceTime(String cpuNiceTime) {
        this.cpuNiceTime = cpuNiceTime;
    }

    /**
     * @return the memTotal
     */
    public String getMemTotal() {
        return memTotal;
    }

    /**
     * @param memTotal the memTotal to set
     */
    public void setMemTotal(String memTotal) {
        this.memTotal = memTotal;
    }

    /**
     * @return the memUsed
     */
    public String getMemUsed() {
        return memUsed;
    }

    /**
     * @param memUsed the memUsed to set
     */
    public void setMemUsed(String memUsed) {
        this.memUsed = memUsed;
    }

    /**
     * @return the memFree
     */
    public String getMemFree() {
        return memFree;
    }

    /**
     * @param memFree the memFree to set
     */
    public void setMemFree(String memFree) {
        this.memFree = memFree;
    }

    /**
     * @return the memActualUsed
     */
    public String getMemActualUsed() {
        return memActualUsed;
    }

    /**
     * @param memActualUsed the memActualUsed to set
     */
    public void setMemActualUsed(String memActualUsed) {
        this.memActualUsed = memActualUsed;
    }

    /**
     * @return the memActualFree
     */
    public String getMemActualFree() {
        return memActualFree;
    }

    /**
     * @param memActualFree the memActualFree to set
     */
    public void setMemActualFree(String memActualFree) {
        this.memActualFree = memActualFree;
    }

    /**
     * @return the memRam
     */
    public String getMemRam() {
        return memRam;
    }

    /**
     * @param memRam the memRam to set
     */
    public void setMemRam(String memRam) {
        this.memRam = memRam;
    }

    /**
     * @return the swapTotal
     */
    public String getSwapTotal() {
        return swapTotal;
    }

    /**
     * @param swapTotal the swapTotal to set
     */
    public void setSwapTotal(String swapTotal) {
        this.swapTotal = swapTotal;
    }

    /**
     * @return the swapUsed
     */
    public String getSwapUsed() {
        return swapUsed;
    }

    /**
     * @param swapUsed the swapUsed to set
     */
    public void setSwapUsed(String swapUsed) {
        this.swapUsed = swapUsed;
    }

    /**
     * @return the swapFree
     */
    public String getSwapFree() {
        return swapFree;
    }

    /**
     * @param swapFree the swapFree to set
     */
    public void setSwapFree(String swapFree) {
        this.swapFree = swapFree;
    }

    /**
     * @return the memUse
     */
    public String getMemUse() {
        return memUse;
    }

    /**
     * @param memUse the memUse to set
     */
    public void setMemUse(String memUse) {
        this.memUse = memUse;
    }

    /**
     * @return the memActualUse
     */
    public String getMemActualUse() {
        return memActualUse;
    }

    /**
     * @param memActualUse the memActualUse to set
     */
    public void setMemActualUse(String memActualUse) {
        this.memActualUse = memActualUse;
    }

    /**
     * @return the swapUse
     */
    public String getSwapUse() {
        return swapUse;
    }

    /**
     * @param swapUse the swapUse to set
     */
    public void setSwapUse(String swapUse) {
        this.swapUse = swapUse;
    }

    /**
     * @return the dfDevName
     */
    public String getDfDevName() {
        return dfDevName;
    }

    /**
     * @param dfDevName the dfDevName to set
     */
    public void setDfDevName(String dfDevName) {
        if (null == dfDevName) {
            dfDevName = "";
        }
        this.dfDevName = dfDevName;
    }

    /**
     * @return the dfDirName
     */
    public String getDfDirName() {
        return dfDirName;
    }

    /**
     * @param dfDirName the dfDirName to set
     */
    public void setDfDirName(String dfDirName) {
        if (null == dfDirName) {
            dfDirName = "";
        }
        this.dfDirName = dfDirName;
    }

    /**
     * @return the dfSysTypeName
     */
    public String getDfSysTypeName() {
        return dfSysTypeName;
    }

    /**
     * @param dfSysTypeName the dfSysTypeName to set
     */
    public void setDfSysTypeName(String dfSysTypeName) {
        if (null == dfSysTypeName) {
            dfSysTypeName = "";
        }
        this.dfSysTypeName = dfSysTypeName;
    }

    /**
     * @return the dfTypeName
     */
    public String getDfTypeName() {
        return dfTypeName;
    }

    /**
     * @param dfTypeName the dfTypeName to set
     */
    public void setDfTypeName(String dfTypeName) {
        if (null == dfTypeName) {
            dfTypeName = "";
        }
        this.dfTypeName = dfTypeName;
    }

    /**
     * @return the dfType
     */
    public String getDfType() {
        return dfType;
    }

    /**
     * @param dfType the dfType to set
     */
    public void setDfType(String dfType) {
        if (null == dfType) {
            dfType = "";
        }
        this.dfType = dfType;
    }

    /**
     * @return the dfTotal
     */
    public String getDfTotal() {
        return dfTotal;
    }

    /**
     * @param dfTotal the dfTotal to set
     */
    public void setDfTotal(String dfTotal) {
        if (null == dfTotal) {
            dfTotal = "";
        }
        this.dfTotal = dfTotal;
    }

    /**
     * @return the dfFree
     */
    public String getDfFree() {
        return dfFree;
    }

    /**
     * @param dfFree the dfFree to set
     */
    public void setDfFree(String dfFree) {
        if (null == dfFree) {
            dfFree = "";
        }
        this.dfFree = dfFree;
    }

    /**
     * @return the dfAvail
     */
    public String getDfAvail() {
        return dfAvail;
    }

    /**
     * @param dfAvail the dfAvail to set
     */
    public void setDfAvail(String dfAvail) {
        if (null == dfAvail) {
            dfAvail = "";
        }
        this.dfAvail = dfAvail;
    }

    /**
     * @return the dfUsed
     */
    public String getDfUsed() {
        return dfUsed;
    }

    /**
     * @param dfUsed the dfUsed to set
     */
    public void setDfUsed(String dfUsed) {
        if (null == dfUsed) {
            dfUsed = "";
        }
        this.dfUsed = dfUsed;
    }

    /**
     * @return the dfUsePercent
     */
    public String getDfUsePercent() {
        return dfUsePercent;
    }

    /**
     * @param dfUsePercent the dfUsePercent to set
     */
    public void setDfUsePercent(String dfUsePercent) {
        if (null == dfUsePercent) {
            dfUsePercent = "";
        }
        this.dfUsePercent = dfUsePercent;
    }

    /**
     * @return the netName
     */
    public String getNetName() {
        return netName;
    }

    /**
     * @param netName the netName to set
     */
    public void setNetName(String netName) {
        this.netName = netName;
    }

    /**
     * @return the netAddress
     */
    public String getNetAddress() {
        return netAddress;
    }

    /**
     * @param netAddress the netAddress to set
     */
    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }

    /**
     * @return the netBcast
     */
    public String getNetBcast() {
        return netBcast;
    }

    /**
     * @param netBcast the netBcast to set
     */
    public void setNetBcast(String netBcast) {
        this.netBcast = netBcast;
    }

    /**
     * @return the netNetmask
     */
    public String getNetNetmask() {
        return netNetmask;
    }

    /**
     * @param netNetmask the netNetmask to set
     */
    public void setNetNetmask(String netNetmask) {
        this.netNetmask = netNetmask;
    }

    /**
     * @return the netRxPackets
     */
    public String getNetRxPackets() {
        return netRxPackets;
    }

    /**
     * @param netRxPackets the netRxPackets to set
     */
    public void setNetRxPackets(String netRxPackets) {
        this.netRxPackets = netRxPackets;
    }

    /**
     * @return the netTxPackets
     */
    public String getNetTxPackets() {
        return netTxPackets;
    }

    /**
     * @param netTxPackets the netTxPackets to set
     */
    public void setNetTxPackets(String netTxPackets) {
        this.netTxPackets = netTxPackets;
    }

    /**
     * @return the netRxBytes
     */
    public String getNetRxBytes() {
        return netRxBytes;
    }

    /**
     * @param netRxBytes the netRxBytes to set
     */
    public void setNetRxBytes(String netRxBytes) {
        this.netRxBytes = netRxBytes;
    }

    /**
     * @return the netTxBytes
     */
    public String getNetTxBytes() {
        return netTxBytes;
    }

    /**
     * @param netTxBytes the netTxBytes to set
     */
    public void setNetTxBytes(String netTxBytes) {
        this.netTxBytes = netTxBytes;
    }

    /**
     * @return the netRxErrors
     */
    public String getNetRxErrors() {
        return netRxErrors;
    }

    /**
     * @param netRxErrors the netRxErrors to set
     */
    public void setNetRxErrors(String netRxErrors) {
        this.netRxErrors = netRxErrors;
    }

    /**
     * @return the netTxErrors
     */
    public String getNetTxErrors() {
        return netTxErrors;
    }

    /**
     * @param netTxErrors the netTxErrors to set
     */
    public void setNetTxErrors(String netTxErrors) {
        this.netTxErrors = netTxErrors;
    }

    /**
     * @return the netRxDropped
     */
    public String getNetRxDropped() {
        return netRxDropped;
    }

    /**
     * @param netRxDropped the netRxDropped to set
     */
    public void setNetRxDropped(String netRxDropped) {
        this.netRxDropped = netRxDropped;
    }

    /**
     * @return the netTxDropped
     */
    public String getNetTxDropped() {
        return netTxDropped;
    }

    /**
     * @param netTxDropped the netTxDropped to set
     */
    public void setNetTxDropped(String netTxDropped) {
        this.netTxDropped = netTxDropped;
    }

    /**
     * @return the netHwaddr
     */
    public String getNetHwaddr() {
        return netHwaddr;
    }

    /**
     * @param netHwaddr the netHwaddr to set
     */
    public void setNetHwaddr(String netHwaddr) {
        this.netHwaddr = netHwaddr;
    }

    /**
     * @return the netDescription
     */
    public String getNetDescription() {
        return netDescription;
    }

    /**
     * @param netDescription the netDescription to set
     */
    public void setNetDescription(String netDescription) {
        this.netDescription = netDescription;
    }

    /**
     * @return the netType
     */
    public String getNetType() {
        return netType;
    }

    /**
     * @param netType the netType to set
     */
    public void setNetType(String netType) {
        this.netType = netType;
    }

    /**
     * @return the netMtu
     */
    public String getNetMtu() {
        return netMtu;
    }

    /**
     * @param netMtu the netMtu to set
     */
    public void setNetMtu(String netMtu) {
        this.netMtu = netMtu;
    }

    /**
     * @return the netMetric
     */
    public String getNetMetric() {
        return netMetric;
    }

    /**
     * @param netMetric the netMetric to set
     */
    public void setNetMetric(String netMetric) {
        this.netMetric = netMetric;
    }

    /**
     * @return the netGateway
     */
    public String getNetGateway() {
        return netGateway;
    }

    /**
     * @param netGateway the netGateway to set
     */
    public void setNetGateway(String netGateway) {
        this.netGateway = netGateway;
    }

    /**
     * @return the netDns
     */
    public String getNetDns() {
        return netDns;
    }

    /**
     * @param netDns the netDns to set
     */
    public void setNetDns(String netDns) {
        this.netDns = netDns;
    }

    /**
     * @return the jvmVersion
     */
    public String getJvmVersion() {
        return jvmVersion;
    }

    /**
     * @param jvmVersion the jvmVersion to set
     */
    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    /**
     * @return the jvmVendor
     */
    public String getJvmVendor() {
        return jvmVendor;
    }

    /**
     * @param jvmVendor the jvmVendor to set
     */
    public void setJvmVendor(String jvmVendor) {
        this.jvmVendor = jvmVendor;
    }

    /**
     * @return the jvmHome
     */
    public String getJvmHome() {
        return jvmHome;
    }

    /**
     * @param jvmHome the jvmHome to set
     */
    public void setJvmHome(String jvmHome) {
        this.jvmHome = jvmHome;
    }

    /**
     * @return the jvmVmVersion
     */
    public String getJvmVmVersion() {
        return jvmVmVersion;
    }

    /**
     * @param jvmVmVersion the jvmVmVersion to set
     */
    public void setJvmVmVersion(String jvmVmVersion) {
        this.jvmVmVersion = jvmVmVersion;
    }

    /**
     * @return the jvmVmVendor
     */
    public String getJvmVmVendor() {
        return jvmVmVendor;
    }

    /**
     * @param jvmVmVendor the jvmVmVendor to set
     */
    public void setJvmVmVendor(String jvmVmVendor) {
        this.jvmVmVendor = jvmVmVendor;
    }

    /**
     * @return the jvmVmName
     */
    public String getJvmVmName() {
        return jvmVmName;
    }

    /**
     * @param jvmVmName the jvmVmName to set
     */
    public void setJvmVmName(String jvmVmName) {
        this.jvmVmName = jvmVmName;
    }

    /**
     * @return the psPid
     */
    public String getPsPid() {
        return psPid;
    }

    /**
     * @param psPid the psPid to set
     */
    public void setPsPid(String psPid) {
        this.psPid = psPid;
    }

    /**
     * @return the psUser
     */
    public String getPsUser() {
        return psUser;
    }

    /**
     * @param psUser the psUser to set
     */
    public void setPsUser(String psUser) {
        this.psUser = psUser;
    }

    /**
     * @return the psStartTime
     */
    public String getPsStartTime() {
        return psStartTime;
    }

    /**
     * @param psStartTime the psStartTime to set
     */
    public void setPsStartTime(String psStartTime) {
        this.psStartTime = psStartTime;
    }

    /**
     * @return the psMemSize
     */
    public String getPsMemSize() {
        return psMemSize;
    }

    /**
     * @param psMemSize the psMemSize to set
     */
    public void setPsMemSize(String psMemSize) {
        this.psMemSize = psMemSize;
    }

    /**
     * @return the psMemRss
     */
    public String getPsMemRss() {
        return psMemRss;
    }

    /**
     * @param psMemRss the psMemRss to set
     */
    public void setPsMemRss(String psMemRss) {
        this.psMemRss = psMemRss;
    }

    /**
     * @return the psMemShare
     */
    public String getPsMemShare() {
        return psMemShare;
    }

    /**
     * @param psMemShare the psMemShare to set
     */
    public void setPsMemShare(String psMemShare) {
        this.psMemShare = psMemShare;
    }

    /**
     * @return the psState
     */
    public String getPsState() {
        return psState;
    }

    /**
     * @param psState the psState to set
     */
    public void setPsState(String psState) {
        this.psState = psState;
    }

    /**
     * @return the psCpuTime
     */
    public String getPsCpuTime() {
        return psCpuTime;
    }

    /**
     * @param psCpuTime the psCpuTime to set
     */
    public void setPsCpuTime(String psCpuTime) {
        this.psCpuTime = psCpuTime;
    }

    /**
     * @return the psName
     */
    public String getPsName() {
        return psName;
    }

    /**
     * @param psName the psName to set
     */
    public void setPsName(String psName) {
        this.psName = psName;
    }

    /**
     * @return the memTypeName
     */
    public String getMemTypeName() {
        return memTypeName;
    }

    /**
     * @param memTypeName the memTypeName to set
     */
    public void setMemTypeName(String memTypeName) {
        this.memTypeName = memTypeName;
    }

    /**
     * @return the diskInfo
     */
    public List<Object> getDiskInfo() {
        return diskInfo;
    }

    /**
     * @param diskInfo the diskInfo to set
     */
    public void setDiskInfo(List<Object> diskInfo) {
        this.diskInfo = diskInfo;
    }

}
