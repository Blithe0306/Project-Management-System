/**
 *Administrator
 */
package com.ft.otp.util.tool;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.confinfo.access.entity.AccessInfo;
import com.ft.otp.manager.confinfo.access.service.IAccessConServ;

/**
 * IP获取工具类
 *
 * @Date in May 14, 2013,6:37:31 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class IpTool {

    private static Logger logger = Logger.getLogger(IpTool.class);

    //信任IP列表服务接口
    private static IAccessConServ accessConServ = (IAccessConServ) AppContextMgr.getObject("accessConServ");

    /**
     * 获取本机所有的IP
     * 
     * @Date in May 14, 2013,6:47:29 PM
     * @return
     * @throws Exception
     */
    public static List<String> localHostIp() {
        List<String> ipList = new ArrayList<String>();
        String localIp = "";
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ipAddress = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ipAddress = address.nextElement();
                    if (!ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress()
                            && ipAddress.getHostAddress().indexOf(":") == -1) {//外网IP  
                        localIp = ipAddress.getHostAddress();
                        ipList.add(localIp);
                    } else if (ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress()
                            && ipAddress.getHostAddress().indexOf(":") == -1) {//内网IP  
                        localIp = ipAddress.getHostAddress();
                        ipList.add(localIp);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Get localhost ip failed!", e);
        }

        return ipList;
    }

    /**
     * 检查本机IP地址列表中是否存在传入的IP
     * 
     * @Date in May 14, 2013,6:55:08 PM
     * @param ipAddr
     * @return
     */
    public static boolean verifyIp(String ipAddr) {
        List<String> ipList = localHostIp();
        if (!StrTool.listNotNull(ipList)) {
            return false;
        }

        String hostIp = "";
        Iterator<String> iter = ipList.iterator();
        while (iter.hasNext()) {
            hostIp = iter.next();
            if (StrTool.strEquals(ipAddr, hostIp)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取客户端IP地址
     * @Date in Aug 27, 2013,9:39:33 AM
     * @param request
     * @return
     * @throws UnknownHostException
     */
    public static String getIpAddr(HttpServletRequest request) throws UnknownHostException {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * 添加认证服务器信任的客户端IP
     * 
     * @Date in Oct 10, 2013,6:56:52 PM
     * @param clientPort
     */
    public static void addTrustIp() {
        try {
            List<String> ipList = IpTool.localHostIp();
            if (!StrTool.listNotNull(ipList)) {
                return;
            }

            String centerIp = "";
            Iterator<String> iter = ipList.iterator();
            while (iter.hasNext()) {
                centerIp = iter.next();

                AccessInfo accessInfo = new AccessInfo();
                accessInfo.setStartip(centerIp);
                accessInfo.setSystype(1);
                //添加信任IP
                Object object = accessConServ.find(accessInfo);
                if (null == object) {
                    accessConServ.addObj(accessInfo);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
