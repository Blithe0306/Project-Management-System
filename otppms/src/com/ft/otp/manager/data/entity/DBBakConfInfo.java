/**
 *Administrator
 */
package com.ft.otp.manager.data.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.manager.confinfo.config.entity.ConfigInfo;
import com.ft.otp.manager.task.entity.TaskInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 数据库备份配置实体
 *
 * @Date in Dec 2, 2013,6:08:50 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class DBBakConfInfo extends BaseEntity {

    private int istimeauto = 0; //是否定时自动  0否 1是
    private int isbaklog = 0; //是否备份日志数据  0否 1是
    private int isremote = 0; //是否远程备份   0否 1是
    private String dir = null; //备份目的目录
    /**
     * 仅仅对远程备份时使用
     */
    private String tempdir = null; //临时存放目录 仅仅对远程备份时使用
    private String serverip = null; //远程服务器的ip
    private String port = null; //登陆远程服务器的端口
    private String user = null; //登陆远程服务器的用户名
    private String password = null; //登陆远程服务器的密码

    /**
     * 定时器对象
     */
    private TaskInfo taskInfo = null;// 定时器对象

    //日志记录需要的参数
    private int logFlag;

    /**
     * 由配置列表得到DBBakConfInfo对象
     * 
     * @Date in Dec 2, 2013,6:15:52 PM
     * @param configList
     * @return
     */
    public static DBBakConfInfo getCenterInfoByList(List<?> configList) {
        DBBakConfInfo bakConfInfo = new DBBakConfInfo();
        if (!StrTool.listNotNull(configList)) {
            return bakConfInfo;
        }

        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.DBBAK_IS_TIME_AUTO)) {
                    bakConfInfo.setIstimeauto(Integer.parseInt(configValue));
                } else if (configName.equals(ConfConstant.DBBAK_IS_BAK_LOG)) {
                    bakConfInfo.setIsbaklog(Integer.parseInt(configValue));
                } else if (configName.equals(ConfConstant.DBBAK_IS_REMOTE)) {
                    bakConfInfo.setIsremote(Integer.parseInt(configValue));
                } else if (configName.equals(ConfConstant.DBBAK_DIR)) {
                    bakConfInfo.setDir(configValue);
                } else if (configName.equals(ConfConstant.TEMP_TEMP_DIR)) {
                    bakConfInfo.setTempdir(configValue);
                } else if (configName.equals(ConfConstant.DBBAK_SERVER_IP)) {
                    bakConfInfo.setServerip(configValue);
                } else if (configName.equals(ConfConstant.DBBAK_PORT)) {
                    bakConfInfo.setPort(configValue);
                } else if (configName.equals(ConfConstant.DBBAK_USER)) {
                    bakConfInfo.setUser(configValue);
                } else if (configName.equals(ConfConstant.DBBAK_PASSWORD)) {
                    bakConfInfo.setPassword(configValue);
                }
            }
        }

        return bakConfInfo;
    }

    /**
     * 由Center配置对象得到配置列表
     * @Date in Nov 16, 2012,2:31:32 PM
     * @param centerInfo
     * @return
     */
    public static List<Object> getListByCenterInfo(DBBakConfInfo centerInfo) {
        List<Object> configList = null;
        if (StrTool.objNotNull(centerInfo)) {
            configList = new ArrayList<Object>();

            //管理员策略配置
            ConfigInfo isTimeAutoConf = new ConfigInfo(ConfConstant.DBBAK_IS_TIME_AUTO, String.valueOf(centerInfo
                    .getIstimeauto()), ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo isBakLogConf = new ConfigInfo(ConfConstant.DBBAK_IS_BAK_LOG, String.valueOf(centerInfo
                    .getIsbaklog()), ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo isRemoteConf = new ConfigInfo(ConfConstant.DBBAK_IS_REMOTE, String.valueOf(centerInfo
                    .getIsremote()), ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo dirConf = new ConfigInfo(ConfConstant.DBBAK_DIR, centerInfo.getDir(),
                    ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo tempDirConf = new ConfigInfo(ConfConstant.TEMP_TEMP_DIR, centerInfo.getTempdir(),
                    ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo serverIpConf = new ConfigInfo(ConfConstant.DBBAK_SERVER_IP, centerInfo.getServerip(),
                    ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo portConf = new ConfigInfo(ConfConstant.DBBAK_PORT, centerInfo.getPort(),
                    ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo userConf = new ConfigInfo(ConfConstant.DBBAK_USER, centerInfo.getUser(),
                    ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            ConfigInfo passwordConf = new ConfigInfo(ConfConstant.DBBAK_PASSWORD, centerInfo.getPassword(),
                    ConfConstant.CONF_TYPE_BAK, NumConstant.common_number_0, "");

            configList.add(isTimeAutoConf);
            configList.add(isBakLogConf);
            configList.add(isRemoteConf);
            configList.add(dirConf);
            configList.add(tempDirConf);
            configList.add(serverIpConf);
            configList.add(portConf);
            configList.add(userConf);
            configList.add(passwordConf);

        }
        return configList;
    }

    /**
     * @return the istimeauto
     */
    public int getIstimeauto() {
        return istimeauto;
    }

    /**
     * @param istimeauto the istimeauto to set
     */
    public void setIstimeauto(int istimeauto) {
        this.istimeauto = istimeauto;
    }

    /**
     * @return the isbaklog
     */
    public int getIsbaklog() {
        return isbaklog;
    }

    /**
     * @return the isremote
     */
    public int getIsremote() {
        return isremote;
    }

    /**
     * @param isremote the isremote to set
     */
    public void setIsremote(int isremote) {
        this.isremote = isremote;
    }

    /**
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * @return the tempdir
     */
    public String getTempdir() {
        return tempdir;
    }

    /**
     * @param tempdir the tempdir to set
     */
    public void setTempdir(String tempdir) {
        this.tempdir = tempdir;
    }

    /**
     * @return the serverip
     */
    public String getServerip() {
        return serverip;
    }

    /**
     * @param serverip the serverip to set
     */
    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param isbaklog the isbaklog to set
     */
    public void setIsbaklog(int isbaklog) {
        this.isbaklog = isbaklog;
    }

    /**
     * @param taskInfo the taskInfo to set
     */
    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    /**
     * 获取定时器相关信息
     * 
     * @Date in Dec 3, 2013,11:22:07 AM
     * @param taskInfo
     */
    public TaskInfo getTaskInfo() {
        if (taskInfo == null) {
            taskInfo = new TaskInfo();
        }

        if(!StrTool.strNotNull(taskInfo.getTaskminute())){
            taskInfo.setTaskminute("0");
        }
        
        if(!StrTool.strNotNull(taskInfo.getTaskhour())){
            taskInfo.setTaskhour("0");
        }
        
        if (!StrTool.strNotNull(taskInfo.getTaskday())) {
            taskInfo.setTaskday("*");
        }

        if (!StrTool.strNotNull(taskInfo.getTaskweek())) {
            taskInfo.setTaskweek("*");
        }

        if (!StrTool.strNotNull(taskInfo.getTaskmonth())) {
            taskInfo.setTaskmonth("*");
        }

        taskInfo.setSourcetype(3);//3定时备份
        taskInfo.setSourceid(0);
        taskInfo.setTaskname(StrConstant.BAK_SCHEDULED_BACKUP);
        taskInfo.setDescp("database bak task");

        return taskInfo;
    }

    /**
     * @return the logFlag
     */
    public int getLogFlag() {
        return logFlag;
    }

    /**
     * @param logFlag the logFlag to set
     */
    public void setLogFlag(int logFlag) {
        this.logFlag = logFlag;
    }

}
