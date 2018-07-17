/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.action;

import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.ConfConfig;
import com.ft.otp.manager.heartbeat.action.aide.HeartBeatActionAide;

/**
 * 更新缓存和定时任务调度信息
 *
 * @Date in 2014年7月23日 09:00:25
 *
 * @version v1.0
 *
 * @author WYJ
 */
public class CacheAndTaskThread implements Runnable{
	boolean baseIsUpdate=false;
    public CacheAndTaskThread(boolean baseIsUpdate){
        this.baseIsUpdate = baseIsUpdate;
    }
    public void run() {
        // 重新加载配置缓存 使缓存中数据和数据表的配置信息保持一致
        ConfConfig.clear();
        ConfConfig.loadConfConfig();
        // 重新加载预警的判断要放到加载配置的后边 
        if (baseIsUpdate) {
            // 重新加载定时器的监控预警任务
            HeartBeatActionAide.getInstance().addOrUpMonitorTask(NumConstant.common_number_4);
        }
    }
}
