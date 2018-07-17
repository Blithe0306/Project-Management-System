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
 * 自助门户服务配置实体
 *
 * @Date in Dec 24, 2012,4:45:44 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class PortalInfo extends BaseEntity {

    private String selfservice; //是否启用自助服务，0：否，1：是
    private String openfunctionconfig; //用户门户开启哪些功能的配置
    private String pwdemailactiveexpire; //小时，密码找回邮件链接激活有效周期
    
    //登录初始密码验证方式 0:默认密码; 1:发送邮件激活; 2:发送短信验证; 3:使用AD密码验证
    private String initpwdloginverifytype; 
    private String initpwdemailactexpire; //邮件激活密码有效期（小时）
    private String initpwdsmsverifyexpire; //短信码验证账号有效期
    private String adserverip; //AD验证服务器IP
    private String adserverport; //AD验证服务器端口
    private String adserverdn;//AD验证服务器DN
    private String defaultPwd;
    
    private String oper; //操作类型
    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    /**
     * 由配置列表得到PortalInfo对象
     * @Date in Dec 26, 2012,2:00:37 PM
     * @param configList
     * @return
     */
    public static PortalInfo getPortalInfoByList(List<?> configList) {
        PortalInfo portalInfo = new PortalInfo();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.SELF_SERVICE_ENABLE)) {
                    portalInfo.setSelfservice(configValue);
                } else if (configName.equals(ConfConstant.OPEN_FUNCTION_CONFIG)) {
                    portalInfo.setOpenfunctionconfig(configValue);
                } else if (configName.equals(ConfConstant.OPEN_FUNCTION_CONFIG)) {
                    portalInfo.setOpenfunctionconfig(configValue);
                } else if (configName.equals(ConfConstant.PWD_EMAIL_ACTIVE_EXPIRE)) {
                    portalInfo.setPwdemailactiveexpire(configValue);
                } else if (configName.equals(ConfConstant.INIT_PWD_LOGIN_VERIFY_TYPE)) {
                    portalInfo.setInitpwdloginverifytype(configValue);
                } else if (configName.equals(ConfConstant.INIT_PWD_EMAIL_ACT_EXPIRE)) {
                    portalInfo.setInitpwdemailactexpire(configValue);
                } else if (configName.equals(ConfConstant.INIT_PWD_SMS_VERIFY_EXPIRE)) {
                    portalInfo.setInitpwdsmsverifyexpire(configValue);
                } else if (configName.equals(ConfConstant.AD_VERIFY_pwd_IP)) {
                    portalInfo.setAdserverip(configValue);
                } else if (configName.equals(ConfConstant.AD_VERIFY_pwd_port)) {
                    portalInfo.setAdserverport(configValue);
                } else if(configName.equals(ConfConstant.AD_VERIFY_PWD_DN)){
                    portalInfo.setAdserverdn(configValue);
                }
            }
        }
        return portalInfo;
    }
    
    /**
     * 由PortalInfo配置对象得到配置列表
     * @Date in Dec 26, 2012,2:01:51 PM
     * @param portalInfo
     * @return
     */
    public static List<Object> getListByPortalInfo(PortalInfo portalInfo, String oper) {
        List<Object> configList = null;
        if (StrTool.objNotNull(portalInfo)) {
            configList = new ArrayList<Object>();
            //基本配置
            if (StrTool.strEquals(oper, "conf")) {
                ConfigInfo selfserviceConf = new ConfigInfo(ConfConstant.SELF_SERVICE_ENABLE, portalInfo.getSelfservice(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo openfunctionConf = new ConfigInfo(ConfConstant.OPEN_FUNCTION_CONFIG, portalInfo.getOpenfunctionconfig(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo pwdemailactiveConf = new ConfigInfo(ConfConstant.PWD_EMAIL_ACTIVE_EXPIRE, portalInfo.getPwdemailactiveexpire(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                
                configList.add(selfserviceConf);
                configList.add(pwdemailactiveConf);
                configList.add(openfunctionConf);
            } else if (StrTool.strEquals(oper, "initpwd")){
                //初始密码验证配置
                ConfigInfo initpwdverifytypeConf = new ConfigInfo(ConfConstant.INIT_PWD_LOGIN_VERIFY_TYPE, portalInfo.getInitpwdloginverifytype(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo initpwdemailactexpConf = new ConfigInfo(ConfConstant.INIT_PWD_EMAIL_ACT_EXPIRE, portalInfo.getInitpwdemailactexpire(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo initpwdsmsverifyexpConf = new ConfigInfo(ConfConstant.INIT_PWD_SMS_VERIFY_EXPIRE, portalInfo.getInitpwdsmsverifyexpire(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo adverifypwdipConf = new ConfigInfo(ConfConstant.AD_VERIFY_pwd_IP, portalInfo.getAdserverip(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo adverifypwdportConf = new ConfigInfo(ConfConstant.AD_VERIFY_pwd_port, portalInfo.getAdserverport(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                ConfigInfo adverifypwddnConf = new ConfigInfo(ConfConstant.AD_VERIFY_PWD_DN, portalInfo.getAdserverdn(), 
                        ConfConstant.PORTAL_CONFIG, NumConstant.common_number_0, "");
                configList.add(initpwdverifytypeConf);
                configList.add(initpwdemailactexpConf);
                configList.add(initpwdsmsverifyexpConf);
                configList.add(adverifypwdipConf);
                configList.add(adverifypwdportConf);
                configList.add(adverifypwddnConf);
            }
        }
        return configList;
    }
    
    public String getSelfservice() {
        return selfservice;
    }
    public void setSelfservice(String selfservice) {
        this.selfservice = selfservice;
    }

    public String getOpenfunctionconfig() {
        return openfunctionconfig;
    }

    public void setOpenfunctionconfig(String openfunctionconfig) {
        this.openfunctionconfig = openfunctionconfig;
    }

    public String getPwdemailactiveexpire() {
        return pwdemailactiveexpire;
    }

    public void setPwdemailactiveexpire(String pwdemailactiveexpire) {
        this.pwdemailactiveexpire = pwdemailactiveexpire;
    }

    public String getInitpwdloginverifytype() {
        return initpwdloginverifytype;
    }

    public void setInitpwdloginverifytype(String initpwdloginverifytype) {
        this.initpwdloginverifytype = initpwdloginverifytype;
    }

    public String getInitpwdemailactexpire() {
        return initpwdemailactexpire;
    }

    public void setInitpwdemailactexpire(String initpwdemailactexpire) {
        this.initpwdemailactexpire = initpwdemailactexpire;
    }

    public String getInitpwdsmsverifyexpire() {
        return initpwdsmsverifyexpire;
    }

    public void setInitpwdsmsverifyexpire(String initpwdsmsverifyexpire) {
        this.initpwdsmsverifyexpire = initpwdsmsverifyexpire;
    }

    public String getAdserverip() {
        return adserverip;
    }

    public void setAdserverip(String adserverip) {
        this.adserverip = adserverip;
    }

    public String getAdserverport() {
        return adserverport;
    }

    public void setAdserverport(String adserverport) {
        this.adserverport = adserverport;
    }

	public String getDefaultPwd() {
		return defaultPwd;
	}

	public void setDefaultPwd(String defaultPwd) {
		this.defaultPwd = defaultPwd;
	}

    public String getAdserverdn() {
        return adserverdn;
    }

    public void setAdserverdn(String adserverdn) {
        this.adserverdn = adserverdn;
    }

}
