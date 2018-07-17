/**
 *Administrator
 */
package com.ft.otp.manager.monitor.task.equipment;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.util.List;

import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.monitor.action.aide.MonitorActionAide;
import com.ft.otp.manager.monitor.entity.EquipmentInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.service.IMonitorServ;
import com.ft.otp.manager.monitor.task.aide.MonitorTaskAide;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警 设备监控预警定时器执行任务类
 *
 * @Date in Mar 6, 2013,4:20:28 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class EquipmentMonitorTask extends Task {

    private Logger logger = Logger.getLogger(EquipmentMonitorTask.class);
    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#execute(it.sauronsoftware.cron4j.TaskExecutionContext)
     */
    private IMonitorServ monitorServ = null;
    private TaskInfo taskInfo = null;
    // 预警信息发送和更新帮助类
    private MonitorTaskAide taskAide = new MonitorTaskAide();
    // 监控预警功能帮助类
    private MonitorActionAide actionAide = new MonitorActionAide();

    /**
     * 初始化方法
     * @param object
     */
    public EquipmentMonitorTask(Object object) {
        monitorServ = (IMonitorServ) AppContextMgr.getObject("monitorServ");
        if (object instanceof TaskInfo) {
            taskInfo = (TaskInfo) object;
        }
    }

    /**
     * 主执行方法
     */
    public void execute(TaskExecutionContext executor) throws RuntimeException {
        String result = "";
        try {
            if (StrTool.parseInt(ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                    ConfConstant.MONITOR_SB_ENABLED)) == 1) {// 启用状态
                // 预警信息
                StringBuffer warnMsg = new StringBuffer();

                // 获取预警方式
                String sendType = ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                        ConfConstant.MONITOR_SB_SEND_TYPE);
                // 获取所有的设备信息
                List<EquipmentInfo> eList = actionAide.getEquipmentData(null);

                for (EquipmentInfo equipmentInfo : eList) {
                    // 获取该设备的所有信息
                    EquipmentMonitorInfo allInfo = actionAide.getEquipmentMonitorInfo(equipmentInfo);

                    if (!StrTool.objNotNull(allInfo)) {
                        continue;
                    }

                    // cpu info
                    String cpuWarnMsg = getCpuWarnInfo(allInfo, sendType);
                    // mem info
                    String memWarnMsg = getMemWarnInfo(allInfo, sendType);
                    // df info
                    String diskWarnMsg = getDiskWarnInfo(allInfo.getDiskInfo(), sendType);

                    // 判断一个设备的消息
                    if (StrTool.strNotNull(cpuWarnMsg) || StrTool.strNotNull(memWarnMsg)
                            || StrTool.strNotNull(diskWarnMsg)) {
                        warnMsg.append(Language.getLangValue("monitor_app_ip", Language.getCurrLang(null), null));
                        warnMsg.append(equipmentInfo.getIpAddr());
                        warnMsg.append(Language.getLangValue("monitor_equipment", Language.getCurrLang(null), null));
                        warnMsg.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
                        warnMsg.append(cpuWarnMsg);
                        warnMsg.append(memWarnMsg);
                        warnMsg.append(diskWarnMsg);
                    }
                }

                result = taskAide.warnInfoDeal(warnMsg.toString(), sendType, ConfConstant.MONITOR_SB_CONF, 0, Language
                        .getLangValue("monitor_equipment_title", Language.getCurrLang(null), null));
            } else {
                result = Language.getLangValue("monitor_equipment_no_enabled", Language.getCurrLang(null), null);
            }

            //System.out.println("设备监控预警执行定时任务，执行时间" + DateTool.dateToStr(System.currentTimeMillis() / 1000, true)
            //        + ",执行结果==>" + result);
            // 监控状态信息 
            executor.setStatusMessage(result);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        executor.pauseIfRequested();
        if (executor.isStopped()) {
            return;
        }

    }

    /**
     * 获取某一设备的cpu 预警信息
     * 方法说明
     * @Date in Mar 13, 2013,1:19:37 PM
     * @param allInfo
     * @param sendType
     * @return
     * @throws Exception
     */
    private String getCpuWarnInfo(EquipmentMonitorInfo allInfo, String sendType) throws Exception {
        // 判断返回信息
        if (!StrTool.objNotNull(allInfo)) {
            return null;
        }

        StringBuilder cpuWarnMsg = new StringBuilder();
        // 获取配置的cpu上限
        double cpuConf = StrTool.parseDouble(ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                ConfConstant.MONITOR_SB_CPUUPPER));
        String cpuStr = actionAide.replaceChar(allInfo.getCpuCombined(), "%");

        if (Double.parseDouble(cpuStr) > cpuConf) {
            if (StrTool.strEquals(sendType, StrConstant.common_number_0)) {
                // 邮件
                cpuWarnMsg
                        .append(Language.getLangValue("monitor_equipment_cpu_info", Language.getCurrLang(null), null));
                cpuWarnMsg.append(allInfo.getCpuCombined());
                cpuWarnMsg.append(Language.getLangValue("monitor_equipment_too_max", Language.getCurrLang(null), null));
                cpuWarnMsg.append("\n");
            } else {
                // 短信
                cpuWarnMsg
                        .append(Language.getLangValue("monitor_equipment_cpu_info", Language.getCurrLang(null), null));
                cpuWarnMsg.append(allInfo.getCpuCombined());
                cpuWarnMsg.append(Language.getLangValue("monitor_equipment_too_max", Language.getCurrLang(null), null));
                cpuWarnMsg.append("\n");
            }
        }

        return cpuWarnMsg.toString();
    }

    /**
     * 获取某一设备的内存mem 预警信息
     * 方法说明
     * @Date in Mar 13, 2013,1:25:22 PM
     * @param allInfo
     * @param sendType
     * @return
     * @throws Exception
     */
    private String getMemWarnInfo(EquipmentMonitorInfo allInfo, String sendType) throws Exception {
        if (!StrTool.objNotNull(allInfo)) {
            return null;
        }
        StringBuilder memWarnMsg = new StringBuilder();

        // 获取配置的内存上限
        double memConf = StrTool.parseDouble(ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                ConfConstant.MONITOR_SB_MEMUPPER));
        String memStr = actionAide.replaceChar(allInfo.getMemUse(), "%");

        if (Double.parseDouble(memStr) > memConf) {
            if (StrTool.strEquals(sendType, StrConstant.common_number_0)) {
                // 邮件
                memWarnMsg
                        .append(Language.getLangValue("monitor_equipment_mem_info", Language.getCurrLang(null), null));
                memWarnMsg.append(allInfo.getMemUse());
                memWarnMsg.append(Language.getLangValue("monitor_equipment_too_max", Language.getCurrLang(null), null));
                memWarnMsg.append("\n");
            } else {
                // 短信
                memWarnMsg
                        .append(Language.getLangValue("monitor_equipment_mem_info", Language.getCurrLang(null), null));
                memWarnMsg.append(allInfo.getMemUse());
                memWarnMsg.append(Language.getLangValue("monitor_equipment_too_max", Language.getCurrLang(null), null));
                memWarnMsg.append("\n");
            }
        }

        return memWarnMsg.toString();
    }

    /**
     * 获取某一设备的内存磁盘disk 预警信息
     * 方法说明
     * @Date in Mar 13, 2013,1:27:22 PM
     * @param equipmentInfo
     * @param sendType
     * @return
     * @throws Exception
     */
    private String getDiskWarnInfo(List<Object> dfList, String sendType) throws Exception {
        StringBuffer diskWarnMsg = new StringBuffer();
        if (!StrTool.listNotNull(dfList)) {
            return null;
        }

        // 获取配置的磁盘上限
        double diskConf = StrTool.parseDouble(ConfDataFormat.getConfValue(ConfConstant.MONITOR_SB_CONF,
                ConfConstant.MONITOR_SB_DISKUPPER));

        for (int i = 0; i < dfList.size(); i++) {
            try {
                EquipmentMonitorInfo monitorInfo = (EquipmentMonitorInfo) dfList.get(i);
                String diskStr = actionAide.replaceChar(monitorInfo.getDfUsePercent(), "%");

                // 有的盘符使用率会返回-
                diskStr = diskStr.indexOf("-") != -1 ? "0" : diskStr;

                if (Double.parseDouble(diskStr) > diskConf) {
                    if (StrTool.strEquals(sendType, StrConstant.common_number_0)) {
                        // 邮件
                        diskWarnMsg.append(Language.getLangValue("monitor_equipment_disk", Language.getCurrLang(null),
                                null));
                        diskWarnMsg.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
                        diskWarnMsg.append(monitorInfo.getDfDirName());
                        diskWarnMsg.append(Language.getLangValue("monitor_equipment_use_percent_too_max", Language
                                .getCurrLang(null), null));
                        diskWarnMsg.append(monitorInfo.getDfUsePercent());
                        diskWarnMsg.append(Language.getLangValue("monitor_equipment_too_max", Language
                                .getCurrLang(null), null));
                        diskWarnMsg.append("\n");
                    } else {
                        // 短信
                        diskWarnMsg.append(Language.getLangValue("monitor_equipment_disk", Language.getCurrLang(null),
                                null));
                        diskWarnMsg.append(Language.getLangValue("colon", Language.getCurrLang(null), null));
                        diskWarnMsg.append(monitorInfo.getDfDirName());
                        diskWarnMsg.append(Language.getLangValue("monitor_equipment_use_percent_too_max", Language
                                .getCurrLang(null), null));
                        diskWarnMsg.append(monitorInfo.getDfUsePercent());
                        diskWarnMsg.append(Language.getLangValue("monitor_equipment_too_max", Language
                                .getCurrLang(null), null));
                        diskWarnMsg.append("\n");
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }

        return diskWarnMsg.toString();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#canBePaused()
     */
    public boolean canBePaused() {
        return super.canBePaused();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#canBeStopped()
     */
    public boolean canBeStopped() {
        return super.canBeStopped();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#supportsCompletenessTracking()
     */
    public boolean supportsCompletenessTracking() {
        return super.supportsCompletenessTracking();
    }

    /* (non-Javadoc)
     * @see it.sauronsoftware.cron4j.Task#supportsStatusTracking()
     */
    public boolean supportsStatusTracking() {
        return super.supportsStatusTracking();
    }

}
