/**
 *Administrator
 */
package com.ft.otp.manager.monitor.action.aide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ft.otp.common.NumConstant;
import com.ft.otp.common.StrConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.common.soap.help.SyncHelper;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;
import com.ft.otp.manager.monitor.entity.EquipmentInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.aide.MonitorServiceAide;
import com.ft.otp.manager.monitor.equipmentmonitor.entity.EquipmentMonitorInfo;
import com.ft.otp.manager.monitor.equipmentmonitor.service.IEquipmentMonitorServ;
import com.ft.otp.manager.monitor.equipmentmonitor.service.impl.AllInfoServImpl;
import com.ft.otp.util.tool.StrTool;

/**
 * 监控预警获取其它设备信息请求帮助类
 *
 * @Date in Mar 11, 2013,3:40:08 PM
 *
 * @version v1.0
 *
 * @author ZXH
 */
public class MonitorActionAide {

    private Logger logger = Logger.getLogger(MonitorActionAide.class);
    // 访问控制策略服务接口
    private IAccessConServ accessConServ = (IAccessConServ) AppContextMgr.getObject("accessConServ");
    // 认证服务器服务接口
    private IServerServ serverServ = (IServerServ) AppContextMgr.getObject("serverServ");

    private SyncHelper syncHelper = new SyncHelper();

    /**
     * 根据访问信息 获取请求后的设备相关信息
     * 方法说明
     * @Date in Mar 11, 2013,4:05:34 PM
     * @param eInfo
     * @param infoType 信息类型：如 0 cpu信息，1 mem信息，2 磁盘信息，3 服务器状态或在线用户数 ，4 cpu、mem、磁盘信息
     * @return
     */
    public String getRequestInfo(EquipmentInfo eInfo, int infoType) throws Exception {
        String info = null;

        if (StrTool.strEquals(eInfo.getServerType(), StrConstant.SERVER_TYPE_PORTAL)) {
            // 用户门户
            // demo "http://localhost:8090/MyWebProject/Test"
            String serveletUrl = "http://" + eInfo.getIpAddr() + ":" + eInfo.getPort() + eInfo.getClientServPath()
                    + infoType;
            info = getResponseInfo(serveletUrl);

        } else if (StrTool.strEquals(eInfo.getServerType(), StrConstant.SERVER_TYPE_SERVER)) {
            // 认证服务器
            // demo "http://192.168.16.51:18081/otpwebservice?WSDL"
            String soapUrl = "http://" + eInfo.getIpAddr() + ":" + eInfo.getPort() + "/" + eInfo.getClientServPath()
                    + "?WSDL";
            info = syncHelper.getDeviceInfo(soapUrl, infoType);
        }

        return info;
    }

    /**
     * 根据提供的url路径获取请求返回值
     * 
     * @Date in Mar 11, 2013,3:47:26 PM
     * @param requestUrl
     * @return
     * @throws Exception
     */
    private String getResponseInfo(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //获取连接状态码 等于200 连接成功
            int code = urlConnection.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {
                //Post Request Define: 
                //urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                //Connection Response From Test Servlet   
                InputStream inputStream = urlConnection.getInputStream();
                //Convert Stream to String   

                return ConvertToString(inputStream);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 将inputstream转换为字符串
     * 
     * @Date in Mar 11, 2013,4:36:44 PM
     * @param inputStream
     * @return
     */
    private String ConvertToString(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line + "\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                inputStreamReader.close();
                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

        return result.toString();
    }

    /**
     * 获取本地ip地址
     * 方法说明
     * @Date in Mar 9, 2013,11:33:04 AM
     * @return
     */
    private String getLocalIp() {
        String ipAddr = "";
        try {
            InetAddress net = InetAddress.getLocalHost();
            if (StrTool.objNotNull(net)) {
                ipAddr = net.getHostAddress().toString();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return ipAddr;
    }

    /**
     * 获取所有的设备数据
     * 
     * @Date in Feb 27, 2013,4:03:12 PM
     * @param request 监控任务中 传入参数为null
     * @return
     */
    public List<EquipmentInfo> getEquipmentData(HttpServletRequest request) throws Exception {
        List<EquipmentInfo> equipmentList = new ArrayList<EquipmentInfo>();
        // 获取所有认证服务器
        List<?> serverList = serverServ.query(new ServerInfo(), new PageArgument());

        if (StrTool.listNotNull(serverList)) {
            ServerInfo[] arrServers = serverList.toArray(new ServerInfo[serverList.size()]);
            for (ServerInfo serverInfo : arrServers) {
                EquipmentInfo eInfo = new EquipmentInfo();

                eInfo.setIpAddr(serverInfo.getHostipaddr());
                eInfo.setPort(serverInfo.getSoapport());
                eInfo.setClientServPath(serverInfo.getWebservicename());
                eInfo.setServerName(serverInfo.getHostname());
                eInfo.setServerType(StrConstant.SERVER_TYPE_SERVER);
                eInfo.setRunState(getEquipmentState(eInfo, request));
                equipmentList.add(eInfo);
            }
        }

        // 获取用户门户IP和管理中心服务IP
        AccessInfo accessInfoQueryForm = new AccessInfo();
        accessInfoQueryForm.setSystype(NumConstant.common_number_1);
        accessInfoQueryForm.setClientapptype(-1);
        List<?> portalList = accessConServ.query(accessInfoQueryForm, new PageArgument());

        if (!StrTool.listNotNull(portalList)) {
            return equipmentList;
        }

        AccessInfo[] arrPortals = portalList.toArray(new AccessInfo[portalList.size()]);
        for (AccessInfo accessInfo : arrPortals) {
            EquipmentInfo eInfo = new EquipmentInfo();
            int clientAppType = accessInfo.getClientapptype();
            eInfo.setIpAddr(accessInfo.getStartip());
            eInfo.setPort(accessInfo.getClientappport());

            if (clientAppType == NumConstant.common_number_0) {
                // 管理中心
                if (StrTool.objNotNull(request)) {
                    eInfo.setServerName(Language.getLangStr(request, "monitor_otpcenter_server"));
                } else {
                    eInfo.setServerName(Language.getLangValue("monitor_otpcenter_server", Language.getCurrLang(null),
                            null));
                }
                eInfo.setServerType(StrConstant.SERVER_TYPE_CENTER);
            } else {
                // 用户门户
                if (StrTool.objNotNull(request)) {
                    eInfo.setServerName(Language.getLangStr(request, "monitor_otpportal_server"));
                } else {
                    eInfo.setServerName(Language.getLangValue("monitor_otpportal_server", Language.getCurrLang(null),
                            null));
                }
                eInfo.setServerName(Language.getLangStr(request, "monitor_otpportal_server"));
                eInfo.setServerType(StrConstant.SERVER_TYPE_PORTAL);
                eInfo.setClientServPath(accessInfo.getClientservpath());
            }

            eInfo.setRunState(getEquipmentState(eInfo, request));
            equipmentList.add(eInfo);
        }

        return equipmentList;
    }

    /**
     * 获取设备的健康状况  
     * 判断规则：cpu、mem、disk 使用率全部80%以下 优；
     *         cpu、mem、disk 只有一个使用率大于80% 良；
     *         cpu、mem、disk 有两个使用率都大于80% 中；
     *         cpu、mem、disk 使用率都大于80% 差； 
     *                 
     * 方法说明
     * @Date in Mar 25, 2013,2:21:56 PM
     * @param eInfo
     * @return
     * @throws Exception
     */
    private String getEquipmentState(EquipmentInfo eInfo, HttpServletRequest request) {
        String state = "";
        try {
            if (StrTool.objNotNull(request)) {
                state = Language.getLangStr(request, "monitor_no");// 未知
            } else {
                state = Language.getLangValue("monitor_no", Language.getCurrLang(null), null);
            }

            // 获取信息
            EquipmentMonitorInfo allInfo = getEquipmentMonitorInfo(eInfo);

            if (!StrTool.objNotNull(allInfo)) {
                return state;
            }

            // 处理信息 、判断健康状况
            // cpu使用率
            double cpuNum = StrTool.parseDouble(replaceChar(allInfo.getCpuCombined(), "%"));
            // 内存使用率
            double memNum = StrTool.parseDouble(replaceChar(allInfo.getMemUse(), "%"));
            // 磁盘使用率
            double dfNum = StrTool.parseDouble(replaceChar(allInfo.getDfUsePercent(), "%"));
            // 使用率达到80%的个数
            int count = 0;

            if (cpuNum > 80) {
                count++;
            }
            if (memNum > 80) {
                count++;
            }
            if (dfNum > 80) {
                count++;
            }

            switch (count) {
                case 0:
                    state = StrTool.objNotNull(request) ? Language.getLangStr(request, "monitor_a") : Language
                            .getLangValue("monitor_a", Language.getCurrLang(null), null);// 优
                    break;
                case 1:
                    state = StrTool.objNotNull(request) ? Language.getLangStr(request, "monitor_b") : Language
                            .getLangValue("monitor_b", Language.getCurrLang(null), null);// 良
                    break;
                case 2:
                    state = StrTool.objNotNull(request) ? Language.getLangStr(request, "monitor_c") : Language
                            .getLangValue("monitor_c", Language.getCurrLang(null), null);// 中
                    break;
                case 3:
                    state = StrTool.objNotNull(request) ? Language.getLangStr(request, "monitor_d") : Language
                            .getLangValue("monitor_d", Language.getCurrLang(null), null);// 差 
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return state;
    }

    /**
     * 根据设备相关信息获取设备的所有cpu，mem，disk信息
     * 
     * @Date in Jul 3, 2013,4:22:21 PM
     * @param eInfo
     * @return
     */
    public EquipmentMonitorInfo getEquipmentMonitorInfo(EquipmentInfo eInfo) throws Exception {
        // 获取信息
        List<Object> allList = null;
        if (StrTool.strEquals(eInfo.getServerType(), StrConstant.SERVER_TYPE_CENTER)
                || StrTool.strEquals(StrConstant.LOCALHOST_IP, eInfo.getIpAddr())
                || StrTool.strEquals(getLocalIp(), eInfo.getIpAddr())) {
            // 管理中心
            IEquipmentMonitorServ equipmentMonitorServ = new AllInfoServImpl();
            allList = equipmentMonitorServ.getObjects();
        } else {
            // 用户门户 和 认证服务器
            String diskJson = getRequestInfo(eInfo, NumConstant.common_number_4);
            if (!StrTool.strEquals(diskJson, "-1") && StrTool.strNotNull(diskJson)) {
                allList = (List<Object>) MonitorServiceAide.getEntityFromJson(diskJson);
            }
        }

        if (!StrTool.listNotNull(allList)) {
            return null;
        }

        return (EquipmentMonitorInfo) allList.get(0);
    }

    /**
     * 替换掉%符号，如果为空返回0
     * 方法说明
     * @Date in Mar 18, 2013,4:10:18 PM
     * @param percentStr
     * @return
     */
    public String replaceChar(String percentStr, String charStr) {
        String numStr = "0";
        if (StrTool.strNotNull(percentStr)) {
            numStr = percentStr.replace(charStr, "");
        } else {
            numStr = StrConstant.common_number_0;
        }

        return numStr;
    }

    //    public static void main(String[] args) {
    //        try {
    //            JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
    //            Client client = (Client) clientFactory.createClient("http://192.168.16.51:18081/otpwebservice?WSDL");
    //            Object[] result = client.invoke("getDeviceInfo", 1);
    //            System.out.println(result[0]);
    //
    //        } catch (Exception e) {
    //            // TODO: handle exception
    //        }
    //    }
}
