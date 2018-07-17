/**
 *Administrator
 */
package com.ft.otp.manager.authmgr.agent.action.aide;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.ft.otp.base.exception.BaseException;
import com.ft.otp.common.Constant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.language.Language;
import com.ft.otp.common.page.PageArgument;
import com.ft.otp.core.springext.AppContextMgr;
import com.ft.otp.manager.authmgr.agent.entity.AgentInfo;
import com.ft.otp.manager.authmgr.agentconf.entity.AgentConfInfo;
import com.ft.otp.manager.authmgr.agentconf.service.IAgentConfServ;
import com.ft.otp.manager.authmgr.agentserver.entity.AgentServerInfo;
import com.ft.otp.manager.authmgr.agentserver.service.IAgentServerServ;
import com.ft.otp.manager.authmgr.server.entity.ServerInfo; //import com.ft.otp.manager.config.entity.ConfInfo;
//import com.ft.otp.manager.config.server.entity.ServerConfigInfo;
//import com.ft.otp.manager.config.server.service.IServerConfigServ;
import com.ft.otp.manager.authmgr.server.service.IServerServ;
import com.ft.otp.util.tool.StrTool;

/**
 * AgentAction 辅助类
 *
 * @Date in May 16, 2011,5:55:27 PM
 *
 * @author ZJY
 */
public class AgentActionAide {
    //认证代理配置接口
    private IAgentConfServ agentConfServ = (IAgentConfServ) AppContextMgr.getObject("agentConfServ");

    //存放认证代理类型
    public static HashMap<String, String> agenttypeMap;
    static {
        agenttypeMap = new HashMap<String, String>();
        agenttypeMap.put("1", "");
        agenttypeMap.put("2", "");
        agenttypeMap.put("4", "");
        agenttypeMap.put("8", "");
        agenttypeMap.put("16", "");
        agenttypeMap.put("32", "");
        agenttypeMap.put("64", "");
        agenttypeMap.put("128", "");
        agenttypeMap.put("256", "");
        agenttypeMap.put("1073741824", "");
    }

    /**
     * 根据认证代理IP去中间表查询关联的认证服务器IP
     * @Date in Jun 16, 2011,1:28:48 PM
     * @param agentServerServ
     * @param agentInfo
     * @return asList
     */
    public List<?> getAgentServ(IAgentServerServ agentServerServ, AgentInfo agentInfo) throws BaseException {
        PageArgument pageArg = new PageArgument();
        AgentServerInfo aServInfo = new AgentServerInfo();
        aServInfo.setAgentipaddr(agentInfo.getAgentipaddr());
        List<?> asList = agentServerServ.query(aServInfo, pageArg);

        return asList;
    }

    /**
     * 获取代理信息和代理关联的服务器信息
     * @Date in Jun 16, 2011,4:11:28 PM
     * @param agentServerServ
     * @param agentList
     * @return aservList
     */
    public List<AgentInfo> getAgentListAll(IAgentServerServ agentServerServ, List<?> agentList) throws BaseException {
        List<AgentInfo> aservList = new ArrayList<AgentInfo>();
        AgentInfo agentInfo = null;
        List<?> hostList = null;
        AgentServerInfo aServerInfo = null;
        PageArgument pageArg = new PageArgument();
        StringBuffer sbf = null;
        for (int i = 0; i < agentList.size(); i++) {
            sbf = new StringBuffer();
            agentInfo = (AgentInfo) agentList.get(i);
            aServerInfo = new AgentServerInfo();
            aServerInfo.setAgentipaddr(agentInfo.getAgentipaddr());
            hostList = agentServerServ.query(aServerInfo, pageArg);
            agentInfo.setHostIps(hostList);

            //遍历存放认证代理类型的Map获取显示名称
            Iterator<String> it = AgentActionAide.agenttypeMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                int result = agentInfo.getAgenttype() & Integer.parseInt(key);
                if (result != 0) {
                    sbf.append(getAgentTypeStr(key));
                    sbf.append("；");
                }
            }
            String agentTypeStr = sbf.toString();
            if (agentTypeStr.endsWith("；")) {
                agentTypeStr = agentTypeStr.substring(0, agentTypeStr.length() - 1);
            }
            agentInfo.setAgenttypeStr(agentTypeStr);
            AgentConfInfo agentConf = new AgentConfInfo();
            if (agentInfo.getAgentconfid() != NumConstant.common_number_0) {

                agentConf.setId(agentInfo.getAgentconfid());
                agentConf = (AgentConfInfo) agentConfServ.find(agentConf);
                if (StrTool.objNotNull(agentConf)) {
                    agentInfo.setAgentconfStr(agentConf.getConfname());
                }
            }
            aservList.add(agentInfo);
        }

        return aservList;
    }

    /**
     * 获取代理关联的服务的认证端口和同步端口
     */
    public ServerInfo getPortInfo(String hostipaddr, IServerServ serverServ) throws BaseException {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setHostipaddr(hostipaddr);
        serverInfo = (ServerInfo) serverServ.find(serverInfo);

        return serverInfo;
    }

    /**
     * 生成ACF代理文件
     * @Date in Jun 17, 2011,11:20:39 AM
     * @param filePath
     * @param clientIp
     * @param pubKey
     * @param hostStr
     * @return
     * @throws Exception
     */
    public int createAcfFile(String filePath, String fileName, String clientIp, String pubKey, String hostStr)
            throws Exception {
        boolean isWrite = false;

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Constant.OTP_SERV_CLI_SECRET.getBytes("8859_1"));
        byte[] key = md.digest();

        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "RC4"));

        String[] servs = hostStr.split(";");
        int npos = 0;
        int authport, syncport;

        byte[] bacf = new byte[64 + 32 + 136 * servs.length];
        // Agent IP
        otp_bytes_copy(bacf, 0, clientIp.getBytes());
        npos += 64;
        // Pubkey
        otp_bytes_copy(bacf, 64, pubKey.getBytes());
        npos += 32;

        for (int i = 0; i < servs.length; i++) {
            String serv = servs[i];
            String[] ip_ports = serv.split(":");
            String[] ports = ip_ports[1].split(",");
            authport = Integer.parseInt(ports[0]);
            syncport = Integer.parseInt(ports[1]);

            otp_bytes_copy(bacf, npos, ip_ports[0].getBytes());
            npos += 64;
            otp_bytes_copy(bacf, npos, ip_ports[0].getBytes());
            npos += 64;
            otp_bytes_copy(bacf, npos, intToByte(authport));
            npos += 4;
            otp_bytes_copy(bacf, npos, intToByte(syncport));
            npos += 4;
        }

        byte[] bfinal = cipher.doFinal(bacf);
        writeByteToFile(filePath, fileName, bfinal, isWrite);

        return 0;
    }

    /**
     * 将字符串以字节形式写入缓存 int start 指定写的初始位置
     * @param dest
     * @param start
     * @param src
     * @return
     */
    public byte[] otp_bytes_copy(byte[] dest, int start, byte[] src) {
        for (int i = 0; i < src.length; i++) {
            dest[i + start] = src[i];
        }

        return dest;
    }

    /**
     * 认证端口、同步端口 int 转 byte[]
     * 
     * @param i
     * @return
     */
    public byte[] intToByte(int i) {
        byte[] bt = new byte[4];
        bt[3] = (byte) (0xff & i);
        bt[2] = (byte) ((0xff00 & i) >> 8);
        bt[1] = (byte) ((0xff0000 & i) >> 16);
        bt[0] = (byte) ((0xff000000 & i) >> 24);
        return bt;
    }

    /**
     * 将ACF流信息写入文件
     * @Date in Jun 17, 2011,11:24:57 AM
     * @param filePath
     * @param cip_puk
     * @param isWrite
     * @throws Exception
     */
    public void writeByteToFile(String filePath, String fileName, byte[] cip_puk, boolean isWrite) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(filePath + fileName, isWrite);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(cip_puk);
        bos.flush();
        bos.close();
        fos.close();
    }
    
    /**
     *  获取认证代理类型
     * @Date in Jul 19, 2013,9:55:39 AM
     * @param agenttype
     * @return
     */
    public String getAgentTypeStr(String  agenttype) {
        String returnVal="";
        int val = StrTool.parseInt(agenttype);
        switch (val) {
            case 1:
                returnVal = Language.getLangValue("auth_agent_ext_rad_client", Language.getCurrLang(null), null);
                break;
            case 2:
                returnVal = Language.getLangValue("auth_agent_standard_rad_client", Language.getCurrLang(null), null);
                break;
            case 4:
                returnVal = Language.getLangValue("auth_agent_wins_login_protect", Language.getCurrLang(null), null);
                break;
            case 8:
                returnVal = Language.getLangValue("auth_agent_linux_login_protect", Language.getCurrLang(null), null);
                break;
            case 16:
                returnVal = Language.getLangValue("auth_agent_iis_agent", Language.getCurrLang(null), null);
                break;
            case 32:
                returnVal = Language.getLangValue("auth_agent_apache_agent", Language.getCurrLang(null), null);
                break;
            case 64:
                returnVal = Language.getLangValue("auth_agent_soap_client", Language.getCurrLang(null), null);
                break;
            case 128:
                returnVal = Language.getLangValue("auth_agent_http_client", Language.getCurrLang(null), null);
                break;
            case 256:
                returnVal = Language.getLangValue("auth_agent_https_client", Language.getCurrLang(null), null);
                break;
            case 1073741824:
                returnVal = Language.getLangValue("auth_agent_other_proxy", Language.getCurrLang(null), null);
                break;    
            default:
                break;
        }
        return returnVal;
    }
    
    

}
