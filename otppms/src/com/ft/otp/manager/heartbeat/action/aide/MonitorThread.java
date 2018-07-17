/**
 * Copyright © FEITIAN Technologies Co., Ltd. All Rights Reserved.
 * @Date 2014年7月17日,下午5:01:43
 */
package com.ft.otp.manager.heartbeat.action.aide;

import org.apache.log4j.Logger;

import com.ft.otp.common.StrConstant;
import com.ft.otp.util.tool.StrTool;


/**
 * 运行监控从服务器访问情况
 *
 * @Date 2014年7月17日,下午5:01:43
 * @version v1.0
 * @author WYJ
 */
public class MonitorThread implements Runnable{
    
    private Logger logger = Logger.getLogger(MonitorThread.class);
    
    //私有静态变量
    private static MonitorThread instance=null; 
    
    
    /**
     * 私有构造
     */
    private MonitorThread(){
        
    }
    /**
     * 获取单例实例
     * @Date   2014年7月17日,下午3:43:23
     * @author WYJ
     * @return
     * @return HeartBeatActionAide
     * @throws
     */
    public static synchronized MonitorThread getInstance(){
        if(null==instance){
            instance = new MonitorThread();
        }
        return instance;
    }
    
    public void run() {
        // TODO Auto-generated method stub
        HeartBeatActionAide hbaVo=HeartBeatActionAide.getInstance();
        String isServer=hbaVo.validateMainServer();
        try {
                if(StrTool.strEquals(isServer, StrConstant.common_number_2)){//从服务器运行 检查主服务器运行情况
                    hbaVo.doClientJob();
                }else if(StrTool.strEquals(isServer, StrConstant.common_number_1)){//主服务器运行
                   hbaVo.doServerJob();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
    }
}
