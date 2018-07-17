/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ft.otp.base.entity.BaseEntity;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.util.tool.StrTool;

/**
 * 管理中心配置
 *
 * @Date in Nov 15, 2012,4:33:06 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class CenterConfInfo extends BaseEntity {

    private String loginerrorretrytemp = null; //管理员登录密码错误临时锁定最大重试次数
    private String loginerrorretrylong = null; //管理员登录密码错误永久锁定最大重试次数
    private String loginlockexpire = null; //已锁定管理员自动解锁周期，秒
    private String passwdupdateperiod = null; //提示管理员修改密码周期，天
    private String prohibitadmin = null; //是否禁止使用超级管理员，y是，n否
    private String mainhostipaddr = null; //主认证服务器
    private String sparehostipaddr = null; //备用认证服务器
    
    private String trustipenabled; //管理中心访问控制策略

    private String oper; //操作类型
    /**
     * 由配置列表得到CenterConfigInfo对象
     * @Date in Nov 16, 2012,9:50:26 AM
     * @param configList
     * @return
     */
    public static CenterConfInfo getCenterInfoByList(List<?> configList) {
        CenterConfInfo centerInfo = new CenterConfInfo();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.LOGIN_ERROR_RETRY_TEMP)) {
                    centerInfo.setLoginerrorretrytemp(configValue);
                } else if (configName.equals(ConfConstant.LOGIN_ERROR_RETRY_LONG)) {
                    centerInfo.setLoginerrorretrylong(configValue);
                } else if (configName.equals(ConfConstant.LOGIN_LOCK_EXPIRE)) {
                    centerInfo.setLoginlockexpire(StrTool.intToString(StrTool.parseInt(configValue) / 60));
                } else if (configName.equals(ConfConstant.PROHIBIT_ADMIN)) {
                    centerInfo.setProhibitadmin(configValue);
                } else if (configName.equals(ConfConstant.PASSWD_UPDATE_PERIOD)) {
                    centerInfo.setPasswdupdateperiod(configValue);
                } else if (configName.equals(ConfConstant.MAIN_HOSTIPADDR)) {
                    centerInfo.setMainhostipaddr(configValue);
                } else if (configName.equals(ConfConstant.SPARE_HOSTIPADDR)) {
                    centerInfo.setSparehostipaddr(configValue);
                } else if (configName.equals(ConfConstant.ENABLED_TRUSTIP_CHECK)) {
                    centerInfo.setTrustipenabled(configValue);
                } 
            }
        }
        return centerInfo;
    }

    /**
     * 由Center配置对象得到配置列表
     * @Date in Nov 16, 2012,2:31:32 PM
     * @param centerInfo
     * @return
     */
    public static List<Object> getListByCenterInfo(CenterConfInfo centerInfo, String oper) {
        List<Object> configList = null;
        if (StrTool.objNotNull(centerInfo)) {
            configList = new ArrayList<Object>();

            //管理员策略配置
            if (StrTool.strEquals(oper, "adminconf")) {
                ConfigInfo loginErrtempRetryConf = new ConfigInfo(ConfConstant.LOGIN_ERROR_RETRY_TEMP, centerInfo.getLoginerrorretrytemp(),
                        ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                ConfigInfo loginErrlongRetryConf = new ConfigInfo(ConfConstant.LOGIN_ERROR_RETRY_LONG, centerInfo.getLoginerrorretrylong(),
                        ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                ConfigInfo loginLockExpireConf = new ConfigInfo(ConfConstant.LOGIN_LOCK_EXPIRE, 
                        StrTool.intToString(StrTool.parseInt(centerInfo.getLoginlockexpire()) * 60), 
                        ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                ConfigInfo prohibitAdminConf = new ConfigInfo(ConfConstant.PROHIBIT_ADMIN, centerInfo.getProhibitadmin(), 
                        ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                ConfigInfo pwdUpdatePeriodConf = new ConfigInfo(ConfConstant.PASSWD_UPDATE_PERIOD, centerInfo
                        .getPasswdupdateperiod(), ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                
                configList.add(loginErrtempRetryConf);
                configList.add(loginErrlongRetryConf);
                configList.add(loginLockExpireConf);
                configList.add(prohibitAdminConf);
                configList.add(pwdUpdatePeriodConf);
            }
            //认证服务器选择
            else if (StrTool.strEquals(oper, "authser")) {
                ConfigInfo mainHostIpaddrConf = new ConfigInfo(ConfConstant.MAIN_HOSTIPADDR,
                        centerInfo.getMainhostipaddr(), ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                ConfigInfo spareHostIpaddrConf = new ConfigInfo(ConfConstant.SPARE_HOSTIPADDR, centerInfo
                        .getSparehostipaddr(), ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");
                
                
                configList.add(mainHostIpaddrConf);
                configList.add(spareHostIpaddrConf);
            }
            //访问控制策略
            else if (StrTool.strEquals(oper, "trustip")) {
                ConfigInfo trustipConf = new ConfigInfo(ConfConstant.ENABLED_TRUSTIP_CHECK, centerInfo
                        .getTrustipenabled(), ConfConstant.CONF_TYPE_CENTER, NumConstant.common_number_0, "");

                configList.add(trustipConf);
            }
            
        }
        return configList;
    }

    public String getLoginerrorretrytemp() {
        return loginerrorretrytemp;
    }

    public void setLoginerrorretrytemp(String loginerrorretrytemp) {
        this.loginerrorretrytemp = loginerrorretrytemp;
    }

    public String getLoginerrorretrylong() {
        return loginerrorretrylong;
    }

    public void setLoginerrorretrylong(String loginerrorretrylong) {
        this.loginerrorretrylong = loginerrorretrylong;
    }

    public String getLoginlockexpire() {
        return loginlockexpire;
    }

    public void setLoginlockexpire(String loginlockexpire) {
        this.loginlockexpire = loginlockexpire;
    }

    public String getProhibitadmin() {
        return prohibitadmin;
    }

    public void setProhibitadmin(String prohibitadmin) {
        this.prohibitadmin = prohibitadmin;
    }

    public String getPasswdupdateperiod() {
        return passwdupdateperiod;
    }

    public void setPasswdupdateperiod(String passwdupdateperiod) {
        this.passwdupdateperiod = passwdupdateperiod;
    }

    public String getMainhostipaddr() {
        return mainhostipaddr;
    }

    public void setMainhostipaddr(String mainhostipaddr) {
        this.mainhostipaddr = mainhostipaddr;
    }

    public String getSparehostipaddr() {
        return sparehostipaddr;
    }

    public void setSparehostipaddr(String sparehostipaddr) {
        this.sparehostipaddr = sparehostipaddr;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getTrustipenabled() {
        return trustipenabled;
    }

    public void setTrustipenabled(String trustipenabled) {
        this.trustipenabled = trustipenabled;
    }

}
