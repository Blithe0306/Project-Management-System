/**
 *Administrator
 */
package com.ft.otp.common.config;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ft.otp.common.NumConstant;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.sms.action.SmsInfoAction;
import com.ft.otp.manager.confinfo.sms.entity.SmsInfo;
import com.ft.otp.manager.confinfo.sms.service.ISmsInfoServ;
import com.ft.otp.util.tool.StrTool;

/**
 * 短信网关信息初始化加载
 * 
 * @Date in Apr 8, 2010,3:08:54 PM
 * 
 * @author ZXH
 */
public class SmsInfoConfig {

    private static Logger logger = Logger.getLogger(SmsInfoAction.class);

    private static ConcurrentHashMap<Integer, SmsInfo> smsMap;
    private static SmsInfoConfig config = null;
    private static volatile boolean loadResult = false;
    private ISmsInfoServ smsInfoServ = (ISmsInfoServ) AppContextMgr.getObject("smsInfoServ");

    private SmsInfoConfig() {
        List<?> smsList = null;
        try {
            smsList = smsInfoServ.query(new SmsInfo(), new PageArgument());
        } catch (Exception ex) {
            logger.error("Config: Get smsinfo failure!", ex);
            return;
        }

        if (!StrTool.listNotNull(smsList)) {
            return;
        }

        smsMap = new ConcurrentHashMap<Integer, SmsInfo>();
        if (smsList.size() > NumConstant.common_number_1) {//多个短信网关
            for (int i = NumConstant.common_number_2; i >= 0; i--) {
                smsSequence(smsList, i);
            }
        } else {
            smsSequence(smsList, 0);
        }

        if (StrTool.mapNotNull(smsMap)) { //启用状态的短信网关不为空
            loadResult = true;
        }
    }

    /**
     * 根据优先级存放短信网关，由高到低
     * 
     * @Date in Jun 17, 2013,6:26:25 PM
     * @param smsList
     * @param priority
     */
    private void smsSequence(List<?> smsList, int priority) {
        Iterator<?> iter = smsList.iterator();
        while (iter.hasNext()) {
            SmsInfo smsInfo = (SmsInfo) iter.next();
            int enabled = smsInfo.getEnabled();
            if (enabled != NumConstant.common_number_0) {//不是停用状态
                if (priority == NumConstant.common_number_2 && smsInfo.getPriority() == priority) {
                    smsMap.put(smsMap.size(), smsInfo);
                } else if (priority == NumConstant.common_number_1 && smsInfo.getPriority() == priority) {
                    smsMap.put(smsMap.size(), smsInfo);
                } else if (priority == NumConstant.common_number_0 && smsInfo.getPriority() == priority) {
                    smsMap.put(smsMap.size(), smsInfo);
                } else {
                    smsMap.put(smsMap.size(), smsInfo);
                }
            }
        }
    }

    public static SmsInfoConfig loadSmsInfoConfig() {
        if (config != null) {
            return config;
        }

        synchronized (SmsInfoConfig.class) {
            if (config == null) {
                config = new SmsInfoConfig();
            }
            return config;
        }
    }

    /**
     * 获取可用的短信网关列表
     * 
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static ConcurrentHashMap<Integer, SmsInfo> getSmsMap() {
        return smsMap;
    }

    /**
     * 获取短信网关列表中一个短信网关
     * 
     * @Date in Jun 17, 2013,4:33:24 PM
     * @param currNum
     * @return
     */
    public static SmsInfo getSmsInfo(int currNum) {
        if (!StrTool.mapNotNull(smsMap)) {
            return null;
        }

        return smsMap.get(currNum);
    }

    /**
     * 初始化加载结果
     * 
     * @Date in Feb 19, 2013,11:25:02 AM
     * @return
     */
    public static boolean getLoadResult() {
        return loadResult;
    }

    /**
     * 清空Map，重置config
     * 
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void destroyed() {
        if (null != config) {
            if (null != smsMap) {
                smsMap.clear();
            }
            config = null;
            loadResult = false;
        }
    }

    /**
     * 重新加载
     * 
     * @Date in Jun 6, 2013,1:57:24 PM
     */
    public synchronized static void reload() {
        destroyed();
        loadSmsInfoConfig();

        SyncHelper.replaceConf(SoapAttribute.RECONFIG_TYPE_10009);
    }

}
