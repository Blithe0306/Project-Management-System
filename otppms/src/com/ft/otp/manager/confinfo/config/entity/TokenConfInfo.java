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
 * 令牌配置
 *
 * @Date in Jul 28, 2013,4:21:01 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class TokenConfInfo extends BaseEntity {

    private String softtkdistpwd; //软件令牌分发默认PIN码

    // 手机令牌配置-1、激活密码策略
    private String apperiod; //默认的激活密码的有效周期
    private String apretry; //默认的激活密码的最大重试次数
    private String defultap; //默认的激活密码，加密存储
    private String apsmssend; //激活密码是否通过短信分发
    private String mobileactivatecodemessage; //手机令牌激活码离线分发提示信息
    private String mobileonlinedistmessage;//手机在线分发短信提示系统
    private String distemailsend;//单用户绑定令牌邮件发送
    //2分发站点策略
    private String siteenabled; //分发站点是否启用，启用(y)、未启用(n)
    private String sitetype; //分发（Web Download）站点的类型，整数http(1)、https(2)、all(3)
    private String siteurl; //默认的分发URL
    private String apgenmethod = "2"; //激活密码产生方式
    private String urlparams = "2"; //URL带的参数 整数 参数可能有:用户ID(1)，令牌(2)
    private String ip;
    private String protocol;
    private String port;
    private String path;

    //短信令牌配置
    private String smstokenauthexpire; //短信令牌的OTP认证过期时间（秒），没有重新产生的情况下，在多长时间内有效
    private String smstokengenexpire; //短信令牌的OTP产生过期时间(秒)，在没有认证成功的情况下，多长时间之后会生成新的口令
    private String smsotpseedmessage; //短信OTP的提示信息配置，默认提示为（短信口令：）
    
    private String smstokenreqattr; //Radius请求短信OTP附加属性名称
    private String smstokenreqval; //Radius请求短信OTP附加属性值
    private String smstokenreqsend; //发送短信OTP前身份认证方式
    private String smstokenreqreturn;//短信OTP发送成功后的响应包属性Code值

    //应急口令配置
    private String tokenempin2otp; //是否在令牌为empin方式认证失败后，检查是否otp认证成功，如果成功则自动将authtype方式修改为otp
    private String empinotpleneq; //是否限制应急口令与动态口令长度必须相等。1是，0否
    private String epassdefvalidtime = null; //应急口令默认设置的有效时长，单位：小时
    private String epassmaxvalidtime = null; //应急口令最大设置的有效时长，单位：小时

    private String oper; //操作类型

    public static TokenConfInfo getTknconfInfoList(List<?> configList) {
        TokenConfInfo tknconfInfo = new TokenConfInfo();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.SOFT_TK_DIST_PWD)) {
                    tknconfInfo.setSofttkdistpwd(configValue);
                } else if (configName.equals(ConfConstant.AP_PERIOD)) {
                    tknconfInfo.setApperiod(configValue);
                } else if (configName.equals(ConfConstant.AP_RETRY)) {
                    tknconfInfo.setApretry(configValue);
                } else if (configName.equals(ConfConstant.SITE_TYPE)) {
                    tknconfInfo.setSitetype(configValue);
                } else if (configName.equals(ConfConstant.AP_SMS_SEND)) {
                    tknconfInfo.setApsmssend(configValue);
                } else if (configName.equals(ConfConstant.SMS_MOBILE_ACTIVATE_CODE_MESSAGE)) {
                    tknconfInfo.setMobileactivatecodemessage(configValue);
                } else if (configName.equals(ConfConstant.SMS_MOBILE_ONLINE_DIST_MESSAGE)) {
                    tknconfInfo.setMobileonlinedistmessage(configValue);
                } else if (configName.equals(ConfConstant.DIST_EMAIL_SEND)) {
                    tknconfInfo.setDistemailsend(configValue);
                } else if (configName.equals(ConfConstant.AP_GEN_METHOD)) {
                    tknconfInfo.setApgenmethod(configValue);
                } else if (configName.equals(ConfConstant.URL_PARAMS)) {
                    tknconfInfo.setUrlparams(configValue);
                } else if (configName.equals(ConfConstant.SITE_ENABLED)) {
                    tknconfInfo.setSiteenabled(configValue);
                } else if (configName.equals(ConfConstant.SITE_URL)) {
                    tknconfInfo.setSiteurl(configValue);
                } else if (configName.equals(ConfConstant.DEFAULT_AP)) {
                    tknconfInfo.setDefultap(configValue);
                } else if (configName.equals(ConfConstant.SMS_TOKEN_AUTH_EXPIRE)) {
                    tknconfInfo.setSmstokenauthexpire(configValue);
                } else if (configName.equals(ConfConstant.SMS_TOKEN_GEN_EXPIRE)) {
                    tknconfInfo.setSmstokengenexpire(configValue);
                } else if (configName.equals(ConfConstant.SMS_OTP_SEED_MESSAGE)) {
                    tknconfInfo.setSmsotpseedmessage(configValue);
                } else if (configName.equals(ConfConstant.CORE_TOKEN_EMPIN2OTP)) {
                    tknconfInfo.setTokenempin2otp(configValue);
                } else if (configName.equals(ConfConstant.CORE_EMPIN_OTP_LENEQ)) {
                    tknconfInfo.setEmpinotpleneq(configValue);
                } else if (configName.equals(ConfConstant.EMERGENCY_PASS_DEF_VALIDTIME)) {
                    tknconfInfo.setEpassdefvalidtime(configValue);
                } else if (configName.equals(ConfConstant.EMERGENCY_PASS_MAX_VALIDTIME)) {
                    tknconfInfo.setEpassmaxvalidtime(configValue);
                } else if (configName.equals(ConfConstant.SMS_TOKEN_REQ_MORE_ATTR)) {
                    tknconfInfo.setSmstokenreqattr(configValue);
                } else if (configName.equals(ConfConstant.SMS_TOKEN_REQ_MORE_VAL)) {
                    tknconfInfo.setSmstokenreqval(configValue);
                } else if (configName.equals(ConfConstant.SMS_OTP_REQ_SEND_CHECK)) {
                    tknconfInfo.setSmstokenreqsend(configValue);
                } else if (configName.equals(ConfConstant.SMS_OTP_REQ_RETURN_CODE)) {
                    tknconfInfo.setSmstokenreqreturn(configValue);
                }
            }
        }

        return tknconfInfo;
    }

    /**
     * 根据配置对象和操作类型转为配置列表
     * @Date in Jul 29, 2013,8:54:57 AM
     * @param userconfInfo
     * @param oper
     * @return
     */
    public static List<Object> getListByTknconfInfo(TokenConfInfo tknconf, String oper) {
        List<Object> configList = null;
        if (StrTool.objNotNull(tknconf)) {
            configList = new ArrayList<Object>();

            //软件令牌配置
            if (StrTool.strEquals(oper, "softtkn")) {
                ConfigInfo softtkdistpwdConf = new ConfigInfo(ConfConstant.SOFT_TK_DIST_PWD,
                        tknconf.getSofttkdistpwd(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                configList.add(softtkdistpwdConf);
            }
            //手机令牌配置1、激活密码策略2、分发站点策略
            else if (StrTool.strEquals(oper, "mobiletkn")) {
                ConfigInfo apperiod = new ConfigInfo(ConfConstant.AP_PERIOD, tknconf.getApperiod(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo apretry = new ConfigInfo(ConfConstant.AP_RETRY, tknconf.getApretry(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo defultap = new ConfigInfo(ConfConstant.DEFAULT_AP, tknconf.getDefultap(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo apsmssend = new ConfigInfo(ConfConstant.AP_SMS_SEND, tknconf.getApsmssend(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo mobileactivatecodemessageConf = new ConfigInfo(
                        ConfConstant.SMS_MOBILE_ACTIVATE_CODE_MESSAGE, tknconf.getMobileactivatecodemessage(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo monlineDistMConf = new ConfigInfo(
                        ConfConstant.SMS_MOBILE_ONLINE_DIST_MESSAGE, tknconf.getMobileonlinedistmessage(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo distemailsend = new ConfigInfo(ConfConstant.DIST_EMAIL_SEND, tknconf.getDistemailsend(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                
                ConfigInfo sitetype = new ConfigInfo(ConfConstant.SITE_TYPE, tknconf.getSitetype(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo siteenabled = new ConfigInfo(ConfConstant.SITE_ENABLED, tknconf.getSiteenabled(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo siteurl = new ConfigInfo(ConfConstant.SITE_URL, tknconf.getSiteurl(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo apgenmethod = new ConfigInfo(ConfConstant.AP_GEN_METHOD, tknconf.getApgenmethod(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo urlparams = new ConfigInfo(ConfConstant.URL_PARAMS, tknconf.getUrlparams(),
                        ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");

                configList.add(apperiod);
                configList.add(apretry);
                configList.add(defultap);
                configList.add(apsmssend);
                configList.add(mobileactivatecodemessageConf);
                configList.add(monlineDistMConf);
                configList.add(distemailsend);
                configList.add(sitetype);
                configList.add(siteenabled);
                configList.add(siteurl);
                configList.add(apgenmethod);
                configList.add(urlparams);
            }

            //短信令牌配置
            else if (StrTool.strEquals(oper, "smstkn")) {
                ConfigInfo smstokenauthexpireConf = new ConfigInfo(ConfConstant.SMS_TOKEN_AUTH_EXPIRE, tknconf
                        .getSmstokenauthexpire(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo smstokengenexpireConf = new ConfigInfo(ConfConstant.SMS_TOKEN_GEN_EXPIRE, tknconf
                        .getSmstokengenexpire(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo smsotpseedmessageConf = new ConfigInfo(ConfConstant.SMS_OTP_SEED_MESSAGE, tknconf
                        .getSmsotpseedmessage(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                
                ConfigInfo smstokenreqattrConf = new ConfigInfo(ConfConstant.SMS_TOKEN_REQ_MORE_ATTR, tknconf
                        .getSmstokenreqattr(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo smstokenreqvalConf = new ConfigInfo(ConfConstant.SMS_TOKEN_REQ_MORE_VAL, tknconf
                        .getSmstokenreqval(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo smstokenreqsendConf = new ConfigInfo(ConfConstant.SMS_OTP_REQ_SEND_CHECK, tknconf
                        .getSmstokenreqsend(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo smstokenreqreturnConf = new ConfigInfo(ConfConstant.SMS_OTP_REQ_RETURN_CODE, tknconf
                        .getSmstokenreqreturn(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");

                configList.add(smstokenauthexpireConf);
                configList.add(smstokengenexpireConf);
                configList.add(smsotpseedmessageConf);
                
                configList.add(smstokenreqattrConf);
                configList.add(smstokenreqvalConf);
                configList.add(smstokenreqsendConf);
                configList.add(smstokenreqreturnConf);
            }

            //应急口令配置
            else if (StrTool.strEquals(oper, "emeypin")) {
                ConfigInfo tkempin2otpConf = new ConfigInfo(ConfConstant.CORE_TOKEN_EMPIN2OTP, tknconf
                        .getTokenempin2otp(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo empinotpleneqConf = new ConfigInfo(ConfConstant.CORE_EMPIN_OTP_LENEQ, tknconf
                        .getEmpinotpleneq(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo ePassDefVTimeConf = new ConfigInfo(ConfConstant.EMERGENCY_PASS_DEF_VALIDTIME, tknconf
                        .getEpassdefvalidtime(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                ConfigInfo ePassMaxVTimeConf = new ConfigInfo(ConfConstant.EMERGENCY_PASS_MAX_VALIDTIME, tknconf
                        .getEpassmaxvalidtime(), ConfConstant.CONF_TYPE_TOKEN, NumConstant.common_number_0, "");
                configList.add(tkempin2otpConf);
                configList.add(empinotpleneqConf);
                configList.add(ePassDefVTimeConf);
                configList.add(ePassMaxVTimeConf);
            }
        }
        return configList;
    }

    public String getSofttkdistpwd() {
        return softtkdistpwd;
    }

    public void setSofttkdistpwd(String softtkdistpwd) {
        this.softtkdistpwd = softtkdistpwd;
    }

    public String getApperiod() {
        return apperiod;
    }

    public void setApperiod(String apperiod) {
        this.apperiod = apperiod;
    }

    public String getApretry() {
        return apretry;
    }

    public void setApretry(String apretry) {
        this.apretry = apretry;
    }

    public String getDefultap() {
        return defultap;
    }

    public void setDefultap(String defultap) {
        this.defultap = defultap;
    }

    public String getApsmssend() {
        return apsmssend;
    }

    public void setApsmssend(String apsmssend) {
        this.apsmssend = apsmssend;
    }

    public String getMobileactivatecodemessage() {
        return mobileactivatecodemessage;
    }

    public void setMobileactivatecodemessage(String mobileactivatecodemessage) {
        this.mobileactivatecodemessage = mobileactivatecodemessage;
    }

    public String getSiteenabled() {
        return siteenabled;
    }

    public void setSiteenabled(String siteenabled) {
        this.siteenabled = siteenabled;
    }

    public String getSitetype() {
        return sitetype;
    }

    public void setSitetype(String sitetype) {
        this.sitetype = sitetype;
    }

    public String getSiteurl() {
        if (siteurl != null && !"".equals(siteurl.trim())) {
            return siteurl;
        } else {
            if ((protocol != null && !"".equals(protocol.trim())) && (ip != null && !"".equals(ip.trim()))
                    && (path != null) && !"".equals(path.trim())) {
                if (port != null && !"".equals(port.trim())) {
                    return protocol.trim() + "://" + ip.trim() + ":" + port.trim() + "/" + path.trim();
                } else {
                    return protocol.trim() + "://" + ip.trim() + "/" + path.trim();
                }
            } else {
                return " ";
            }
        }
    }

    public void setSiteurl(String siteurl) {
        if (siteurl == null || "".equals(siteurl.trim())) {
            return;
        }
        String[] url = siteurl.split("://");
        protocol = url[0];
        if (url.length > 1) {
            if (url[1].indexOf(":") != -1) {
                ip = url[1].substring(0, url[1].indexOf(":"));
                port = url[1].substring(url[1].indexOf(":") + 1, url[1].indexOf("/"));
                path = url[1].substring(url[1].indexOf("/") + 1);
            } else {
                ip = url[1].substring(0, url[1].indexOf("/"));
                path = url[1].substring(url[1].indexOf("/") + 1);
            }
        }
        this.siteurl = siteurl;
    }

    public String getApgenmethod() {
        return apgenmethod;
    }

    public void setApgenmethod(String apgenmethod) {
        this.apgenmethod = apgenmethod;
    }

    public String getUrlparams() {
        return urlparams;
    }

    public void setUrlparams(String urlparams) {
        this.urlparams = urlparams;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSmstokenauthexpire() {
        return smstokenauthexpire;
    }

    public void setSmstokenauthexpire(String smstokenauthexpire) {
        this.smstokenauthexpire = smstokenauthexpire;
    }

    public String getSmstokengenexpire() {
        return smstokengenexpire;
    }

    public void setSmstokengenexpire(String smstokengenexpire) {
        this.smstokengenexpire = smstokengenexpire;
    }

    public String getSmsotpseedmessage() {
        return smsotpseedmessage;
    }

    public void setSmsotpseedmessage(String smsotpseedmessage) {
        this.smsotpseedmessage = smsotpseedmessage;
    }

    public String getTokenempin2otp() {
        return tokenempin2otp;
    }

    public void setTokenempin2otp(String tokenempin2otp) {
        this.tokenempin2otp = tokenempin2otp;
    }

    public String getEmpinotpleneq() {
        return empinotpleneq;
    }

    public void setEmpinotpleneq(String empinotpleneq) {
        this.empinotpleneq = empinotpleneq;
    }

    public String getEpassdefvalidtime() {
        return epassdefvalidtime;
    }

    public void setEpassdefvalidtime(String epassdefvalidtime) {
        this.epassdefvalidtime = epassdefvalidtime;
    }

    public String getEpassmaxvalidtime() {
        return epassmaxvalidtime;
    }

    public void setEpassmaxvalidtime(String epassmaxvalidtime) {
        this.epassmaxvalidtime = epassmaxvalidtime;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getMobileonlinedistmessage() {
        return mobileonlinedistmessage;
    }

    public void setMobileonlinedistmessage(String mobileonlinedistmessage) {
        this.mobileonlinedistmessage = mobileonlinedistmessage;
    }

    public String getDistemailsend() {
        return distemailsend;
    }

    public void setDistemailsend(String distemailsend) {
        this.distemailsend = distemailsend;
    }

	public String getSmstokenreqattr() {
		return smstokenreqattr;
	}

	public void setSmstokenreqattr(String smstokenreqattr) {
		this.smstokenreqattr = smstokenreqattr;
	}

	public String getSmstokenreqval() {
		return smstokenreqval;
	}

	public void setSmstokenreqval(String smstokenreqval) {
		this.smstokenreqval = smstokenreqval;
	}

	public String getSmstokenreqsend() {
		return smstokenreqsend;
	}

	public void setSmstokenreqsend(String smstokenreqsend) {
		this.smstokenreqsend = smstokenreqsend;
	}

	public String getSmstokenreqreturn() {
		return smstokenreqreturn;
	}

	public void setSmstokenreqreturn(String smstokenreqreturn) {
		this.smstokenreqreturn = smstokenreqreturn;
	}
}
