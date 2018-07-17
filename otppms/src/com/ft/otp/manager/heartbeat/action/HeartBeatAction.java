/**
 *Administrator
 */
package com.ft.otp.manager.heartbeat.action;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.common.Constant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.manager.confinfo.config.entity.MonitorConfig;
import com.ft.otp.manager.heartbeat.action.aide.HeartBeatActionAide;
import com.ft.otp.util.tool.IpTool;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 双机热备分运行监控Action控制类 scope单例模式
 *
 * @Date in 2014年7月17日 13:06:31
 *
 * @author WYJ
 */
public class HeartBeatAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = 5783679697486689359L;
    private Logger logger = Logger.getLogger(HeartBeatAction.class);
    
   // 监控预警帮助类
    private MonitorConfig monitorConfig; 
    /**
     * @throws UnknownHostException 
     * 获取当前服务运行状态
     * @Date   2014年7月17日,下午1:09:25
     * @author WYJ
     * @return
     * @return String
     * @throws
     */
    public String getRunningState() throws UnknownHostException{
        HeartBeatActionAide heartBeatActionAide= HeartBeatActionAide.getInstance();
        String remoteAddr= IpTool.getIpAddr(request);
        heartBeatActionAide.updateVisiTime(remoteAddr);//把访问ip服务器的时间更新
        try {
            heartBeatActionAide.dealSerRunRecord(remoteAddr, StrConstant.common_number_1, StrConstant.common_number_2);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }
    
    
    /**
     * 测试主、从服务器是否正常
     * @Date   Oct 23, 2014,6:23:54 PM
     * @author WYJ
     * @return
     * @return String
     * @throws
     */
    public String testServerState(){
        int port = request.getServerPort();
        String servertype=request.getParameter("type");
        String val = request.getParameter("val");
        
        String ip = null;
        if(null!=monitorConfig){
            if(null!=servertype){
                if(servertype.equals("main")){
                    ip=monitorConfig.getMainIpAddress();
                }else if (servertype.equals("spare")) {
                    ip=monitorConfig.getSpareIpAddress();
                }
            }
        }else if(StrTool.strNotNull(val)){
            ip = val;
        }
        if(ip!=null){
        StringBuffer httpUrl=new StringBuffer();
        httpUrl.append(HeartBeatActionAide.SERVER_ACTION_HTTP).append(ip);
        httpUrl.append(HeartBeatActionAide.SERVER_ACTION_CHAR).append(port).append(HeartBeatActionAide.TEST_SERVER_ACTION_URL);
        HttpURLConnection conn =null;
        try{
            URL url = new URL(httpUrl.toString()); //创建URL对象
            //返回一个URLConnection对象，它表示到URL所引用的远程对象的连接
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000); //设置连接超时为2秒
            conn.setRequestMethod(HeartBeatActionAide.SERVER_REQUEST_METHOD); //设定请求方式
            conn.connect(); //建立到远程对象的实际连接
            int response_code = conn.getResponseCode();
            //如果获取的连接的相应码为200 标识目标服务运行正常
            if (response_code == HttpURLConnection.HTTP_OK) {
                setResponseWrite(Constant.alert_succ);
                return null;
            }else{//目标服务器没有相应
                setResponseWrite(Constant.alert_error);
                return null;
            }
            
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            setResponseWrite(Constant.alert_error);
            return null;
        }finally{
            if(conn != null){
                conn.disconnect(); //中断连接
            }
        }
        }
        return null;
    }
    


    public String init() {
        return null;
    }

    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    public String modify() {
        return null;
    }

    public String page() {
        return null;
    }

    public String find() {
        return null;
    }

    public String view() {
        return null;
    }

    public MonitorConfig getMonitorConfig() {
        return monitorConfig;
    }


    public void setMonitorConfig(MonitorConfig monitorConfig) {
        this.monitorConfig = monitorConfig;
    }

}
