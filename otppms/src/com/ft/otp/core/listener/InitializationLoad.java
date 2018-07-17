/**
 *Administrator
 */
package com.ft.otp.core.listener;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.config.AgentPubConfig;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.common.config.DBUpGradeConfig;
import com.ft.otp.common.config.DomainConfig;
import com.ft.otp.common.config.LanguageConfig;
import com.ft.otp.common.config.LicenseConfig;
import com.ft.otp.common.config.OtpPermConfig;
import com.ft.otp.common.config.ProxoolConfig;
import com.ft.otp.common.config.PubConfConfig;
import com.ft.otp.common.config.SmsInfoConfig;
import com.ft.otp.common.config.TaskConfig;
import com.ft.otp.common.config.TrustIpConfig;
import com.ft.otp.common.soap.WebServiceFactory;
import com.ft.otp.manager.token.tokenimport.service.impl.ft.helper.TokenCryptoUtil;
import com.ft.otp.util.alg.AlgHelper;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 初始化加载
 *
 * @Date in Mar 28, 2013,6:14:05 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class InitializationLoad {

    /**
     * 数据库连接成功后，继续系统加载工作
     * 
     * @Date in Mar 29, 2012,3:22:55 PM
     * @param context
     * @return
     */
    public static boolean configLoad() {
        //数据库连接测试
        boolean result = testDBConn();
        if (result) {
        	 //加载系统配置项
            ConfConfig.loadConfConfig();
            
            //数据库升级配置项
            DBUpGradeConfig.loadDbUpGradeConfig();
            
            //加载Domain配置
            DomainConfig.loadDomainConfig();

            //加载系统权限码
            OtpPermConfig.loadOtpPerm();

            //加载配置管理模块配置属性MAP
            PubConfConfig.loadPubConfConfig();

            //短信网关配置属性MAP
            //SmsInfoConfig.loadSmsInfoConfig();

            //加载定时任务
            //TaskConfig.loadTaskConfig();

            //后端认证代理配置
            //AgentPubConfig.loadAgentPubConfig();

            //加载信任IP
            TrustIpConfig.loadTrustIpConfig();

            //加载已经启用的授权
            LicenseConfig.loadLicenceInfo();

            //加载Web Service工厂
            WebServiceFactory.loadWebServiceFactory();
            
            //initSeedPriKey();

            IpTool.addTrustIp();
        }

        return result;
    }

    /**
     * 系统加载的资源销毁
     * 
     * @Date in Apr 4, 2013,12:13:59 PM
     */
    public static void destroyed() {
        //销毁多语言资源文件
        LanguageConfig.clear();

        //销毁Domain配置
        DomainConfig.clear();
        
        //销毁系统权限码
        OtpPermConfig.clear();

        //销毁配置管理模块配置属性MAP
        PubConfConfig.clear();

        //销毁系统配置项
        ConfConfig.clear();
        
        //销毁升级配置项
        DBUpGradeConfig.clear();
        
        //销毁定时任务
        TaskConfig.stop();

        //销毁后端认证代理配置
        AgentPubConfig.clear();

        //销毁已经启用的授权
        LicenseConfig.clear();
    }

    /**
     * 数据库连接测试
     * @Date in Nov 23, 2011,6:05:14 PM
     * @return
     */
    public static boolean testDBConn() {
        ProxoolConfig.loadDBConfig();//加载连接池配置文件

        return ProxoolConfig.getLoadResult();
    }

    /**
     * 种子加解密密钥初始化
     * 
     * @Date in Sep 24, 2013,5:37:21 PM
     */
    private static void initSeedPriKey() {
        //种子加密密钥初始化
        String encKeyStr = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.SEED_PRIVATE_KEY_RANDOM);
        byte[] seedEncKey = null;
        if (StrTool.strNotNull(encKeyStr)) {
            seedEncKey = AlgHelper.hexStringToBytes(encKeyStr);
            TokenCryptoUtil.init(seedEncKey, null, false);
        }
    }

}
