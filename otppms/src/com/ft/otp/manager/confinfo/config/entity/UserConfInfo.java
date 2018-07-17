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
 * 用户配置
 *
 * @Date in Jul 28, 2013,1:44:49 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class UserConfInfo extends BaseEntity {

    //用户令牌绑定策略
    private String maxbindtokens; //每用户最大可绑定令牌数量
    private String adduserwhenbind; //在绑定用户时候，如果用户不存在，是否新增加该用户
    private String authotpwhenbind; // 绑定用户时，是否验证动态口令
    private String maxbindusers; //每令牌最大可绑定用户数量
    private String tkbindischangeorg; //令牌绑定后是否迁移至用户所在的组织机构下。0否，1是
    private String tkunbindischangeorg; //令牌解绑后是否回到所属的域中。0，否，1是
    private String unbindselect; //解绑令牌时，被解绑的令牌是否停用：0:否，1:是
    private String replaceselect; //更换令牌时，被更换的令牌状态选择：0：继续使用，1：停用，2：作废
    
    private String defaultuserpwd; //默认用户密码
    private String defaultlocalauth; // 默认的本地登录模式
    private String defaultbackendauth; // 默认的后端登录模式

    private String oper; //操作类型
    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    /**
     * 由配置列表获取实体对象信息
     * @Date in Jul 28, 2013,2:58:36 PM
     * @param configList
     * @return
     */
    public static UserConfInfo getUserInfoList(List<?> configList) {
        UserConfInfo userConfInfo = new UserConfInfo();
        Iterator<?> iter = configList.iterator();
        while (iter.hasNext()) {
            ConfigInfo config = (ConfigInfo) iter.next();
            String configName = config.getConfname();
            String configValue = config.getConfvalue();
            
            if (StrTool.strNotNull(configName)) {
                if (configName.equals(ConfConstant.CORE_MAX_BIND_TOKENS)) {
                    userConfInfo.setMaxbindtokens(configValue);
                } else if (configName.equals(ConfConstant.CORE_ADD_USER_WHEN_BIND)) {
                    userConfInfo.setAdduserwhenbind(configValue);
                } else if (configName.equals(ConfConstant.CORE_MAX_BIND_USERS)) {
                    userConfInfo.setMaxbindusers(configValue);
                } else if (configName.equals(ConfConstant.TK_BIND_IS_CHANGE_ORG)) {
                    userConfInfo.setTkbindischangeorg(configValue);
                } else if (configName.equals(ConfConstant.TK_UNBIND_IS_CHANGE_ORG)) {
                    userConfInfo.setTkunbindischangeorg(configValue);
                } else if (configName.equals(ConfConstant.CORE_UNBIND_STATE_SELECT)) {
                    userConfInfo.setUnbindselect(configValue);
                } else if (configName.equals(ConfConstant.CORE_REPLACE_STATE_SELECT)) {
                    userConfInfo.setReplaceselect(configValue);
                } else if (configName.equals(ConfConstant.DEFAULT_USER_PWD)) {
                    userConfInfo.setDefaultuserpwd(configValue);
                } else if (configName.equals(ConfConstant.CORE_AUTH_OTP_WHEN_BIND)) {
                    userConfInfo.setAuthotpwhenbind(configValue);
                } else if (configName.equals(ConfConstant.DEFAULT_LOCALAUTH)) {
                    userConfInfo.setDefaultlocalauth(configValue);
                } else if (configName.equals(ConfConstant.DEFAULT_BACKENDAUTH)) {
                    userConfInfo.setDefaultbackendauth(configValue);
                }
            }
        }
        return userConfInfo;
    }
    
    /**
     * 由配置对象转为配置列表信息
     * @Date in Jul 28, 2013,3:34:15 PM
     * @param userconfInfo
     * @param oper 操作类型
     * @return
     */
    public static List<Object> getListByCommInfo(UserConfInfo userconfInfo, String oper) {
        List<Object> configList = null;
        if (StrTool.objNotNull(userconfInfo)) {
            configList = new ArrayList<Object>();
            //用户-令牌绑定策略
            if (StrTool.strEquals(oper, "utknconf")) {
                ConfigInfo maxbindtokensConf = new ConfigInfo(ConfConstant.CORE_MAX_BIND_TOKENS, userconfInfo
                        .getMaxbindtokens(), ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo maxbindusersConf = new ConfigInfo(ConfConstant.CORE_MAX_BIND_USERS, userconfInfo
                        .getMaxbindusers(), ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo adduserwhenbindConf = new ConfigInfo(ConfConstant.CORE_ADD_USER_WHEN_BIND, userconfInfo
                        .getAdduserwhenbind(), ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo unbindselectConf = new ConfigInfo(ConfConstant.CORE_UNBIND_STATE_SELECT, userconfInfo
                        .getUnbindselect(), ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo replaceselectConf = new ConfigInfo(ConfConstant.CORE_REPLACE_STATE_SELECT, userconfInfo
                        .getReplaceselect(), ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo tkbindischangeorgConf = new ConfigInfo(ConfConstant.TK_BIND_IS_CHANGE_ORG, userconfInfo.getTkbindischangeorg(),
                        ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo tkunbindischangeorgConf = new ConfigInfo(ConfConstant.TK_UNBIND_IS_CHANGE_ORG, userconfInfo.getTkunbindischangeorg(),
                        ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo authotpwhenbindConf = new ConfigInfo(ConfConstant.CORE_AUTH_OTP_WHEN_BIND, userconfInfo.getAuthotpwhenbind(), ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                
                configList.add(maxbindtokensConf);
                configList.add(maxbindusersConf);
                configList.add(adduserwhenbindConf);
                configList.add(unbindselectConf);
                configList.add(replaceselectConf);
                configList.add(tkbindischangeorgConf);
                configList.add(tkunbindischangeorgConf);
                configList.add(authotpwhenbindConf);
            } else if (StrTool.strEquals(oper, "upwdconf")) { //用户登录默认密码配置
                ConfigInfo defaultpwdConf = new ConfigInfo(ConfConstant.DEFAULT_USER_PWD, userconfInfo.getDefaultuserpwd(),
                        ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo deflocalauthConf = new ConfigInfo(ConfConstant.DEFAULT_LOCALAUTH, userconfInfo.getDefaultlocalauth(),
                        ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                ConfigInfo defbackendauthConf = new ConfigInfo(ConfConstant.DEFAULT_BACKENDAUTH, userconfInfo.getDefaultbackendauth(),
                        ConfConstant.CONF_TYPE_USER, NumConstant.common_number_0, "");
                
                configList.add(defaultpwdConf);
                configList.add(deflocalauthConf);
                configList.add(defbackendauthConf);
            }
        }
        
        return configList;
    }
    public String getMaxbindtokens() {
        return maxbindtokens;
    }

    public void setMaxbindtokens(String maxbindtokens) {
        this.maxbindtokens = maxbindtokens;
    }

    public String getAdduserwhenbind() {
        return adduserwhenbind;
    }

    public void setAdduserwhenbind(String adduserwhenbind) {
        this.adduserwhenbind = adduserwhenbind;
    }

    public String getMaxbindusers() {
        return maxbindusers;
    }

    public void setMaxbindusers(String maxbindusers) {
        this.maxbindusers = maxbindusers;
    }

    public String getTkbindischangeorg() {
        return tkbindischangeorg;
    }

    public void setTkbindischangeorg(String tkbindischangeorg) {
        this.tkbindischangeorg = tkbindischangeorg;
    }

    public String getTkunbindischangeorg() {
        return tkunbindischangeorg;
    }

    public void setTkunbindischangeorg(String tkunbindischangeorg) {
        this.tkunbindischangeorg = tkunbindischangeorg;
    }

    public String getUnbindselect() {
        return unbindselect;
    }

    public void setUnbindselect(String unbindselect) {
        this.unbindselect = unbindselect;
    }

    public String getReplaceselect() {
        return replaceselect;
    }

    public void setReplaceselect(String replaceselect) {
        this.replaceselect = replaceselect;
    }

    public String getDefaultuserpwd() {
        return defaultuserpwd;
    }

    public void setDefaultuserpwd(String defaultuserpwd) {
        this.defaultuserpwd = defaultuserpwd;
    }

    public String getAuthotpwhenbind() {
        return authotpwhenbind;
    }

    public void setAuthotpwhenbind(String authotpwhenbind) {
        this.authotpwhenbind = authotpwhenbind;
    }

    public String getDefaultlocalauth() {
        return defaultlocalauth;
    }

    public void setDefaultlocalauth(String defaultlocalauth) {
        this.defaultlocalauth = defaultlocalauth;
    }

    public String getDefaultbackendauth() {
        return defaultbackendauth;
    }

    public void setDefaultbackendauth(String defaultbackendauth) {
        this.defaultbackendauth = defaultbackendauth;
    }
    
}
