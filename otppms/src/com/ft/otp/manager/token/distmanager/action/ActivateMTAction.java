/**
 *TBM
 */
package com.ft.otp.manager.token.distmanager.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.DistConstant;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.soap.MessageBean;
import com.ft.otp.common.soap.help.AuthHelper;
import com.ft.otp.manager.confinfo.config.entity.TokenConfInfo;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 手机令牌在线激活业务Action
 *
 * @Date in Aug 28, 2013,7:01:13 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class ActivateMTAction {

    /**
     * 手机令牌在线激活
     * 
     * @Date in Aug 29, 2013,11:50:34 AM
     * @param request
     * @return
     */
    public String execute(HttpServletRequest request) {
        String uid = request.getParameter("uid");//用户名
        if (StrTool.strNotNull(uid)) {
            uid = uid.trim().toLowerCase();
        }
        String tkid = request.getParameter("tkid");//令牌号
        String udid = request.getParameter("udid");//手机令牌标识码
        String ap = request.getParameter("ap");//激活密码
        String scheme = request.getScheme();
        
//        String mobiletype = request.getParameter("mobiletype");
//        String systemver = request.getParameter("systemver");
        
        try {
            if (StrTool.strEqualsIgnoreCase(scheme, "https")) {
                if (StrTool.strNotNull(uid)) {
                    uid = new String(uid.getBytes("ISO8859-1"), "UTF-8");
                }
            }

            //URL参数检查
            if (!StrTool.strNotNull(uid) && !StrTool.strNotNull(tkid)) {
                return response(DistConstant.OTPR_CORE_DISP_PARANOUK, tkid, uid);
            }

            //激活密码是否为空
            if (!StrTool.strNotNull(ap)) {
                return response(DistConstant.OTPR_CORE_DISP_PARANOAP, tkid, uid);
            }

            TokenConfInfo distConfInfo = getDistConfInfo();

            // 判断手机令牌分发站点策略是否开启
            if (StrTool.strEquals(distConfInfo.getSiteenabled(), "n")) {
                return response(DistConstant.OTPR_CORE_DISP_SITECLOSED, tkid, uid);
            }

            //判断站点访问类型是否正确
            if (StrTool.strEquals(distConfInfo.getSitetype(), "2")) {
                if (!StrTool.strEqualsIgnoreCase(scheme, "https")) {
                    return response(DistConstant.OTPR_CORE_DISP_PROTOCOLERROR, tkid, uid);
                }
            }

            //请求在线激活
            AuthHelper authHelper = new AuthHelper();
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(uid);
            messageBean.setTokenSN(tkid);
            messageBean.setOtp(ap);
            messageBean.setUdid(udid);

            int resultCode = 0;
            String[] retArr = authHelper.onLineActivate(messageBean);
            resultCode = StrTool.parseInt(retArr[0]);
            if (resultCode == NumConstant.common_number_0) {
//            	String path = Constant.WEB_APP_PATH + Constant.WEB_TEMP_FILE_MOBILE_TYPE;
//            	String content = tkid+" "+ uid +" "+ mobiletype+" "+ systemver +"\r\n";
//            	FileUtils.writeTxtContent(path, content);
            	 
                return response(resultCode, retArr[1]);
            } else {
                resultCode = getClientRetCode(resultCode);

                return response(resultCode, tkid, uid);
            }
        } catch (Exception e) {
            return response(DistConstant.OTPR_CORE_DISP_RUNRROR, tkid, uid);
        }
    }

    /**
     * 根据服务端返回码转译本地错误码
     * 
     * @Date in Aug 29, 2013,9:37:47 AM
     * @param serverCode
     * @return
     */
    private int getClientRetCode(int serverCode) {
        switch (serverCode) {
            case 12:
                return DistConstant.OTPR_CORE_DISP_NOUSER;

            case 6:
                return DistConstant.OTPR_CORE_DISP_UNOT;

            case 91:
                return DistConstant.OTPR_CORE_DISP_NOUT;

            case 5:
                return DistConstant.OTPR_CORE_DISP_NOTOKEN;

            case 104:
                return DistConstant.OTPR_CORE_DISP_TOKENA;

            case 106:
                return DistConstant.OTPR_CORE_DISP_UDIDE;

            case 107:
                return DistConstant.OTPR_CORE_DISP_APERRORMAX;

            case 105:
                return DistConstant.OTPR_CORE_DISP_APEXP;

            case 103:
                return DistConstant.OTPR_CORE_DISP_APERROR;

            case 114:
                return DistConstant.OTPR_CORE_DISP_NODISP;

            default:
                return DistConstant.OTPR_CORE_DISP_RUNRROR;
        }
    }

    /**
     * 返回给客户端的结果信息
     * 
     * @Date in Aug 29, 2013,9:08:10 AM
     * @param status
     * @param tkid
     * @param uid
     * @return
     */
    private String response(int status, String tkid, String uid) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("status=").append(status).append("\r\n");
        buffer.append("ac=").append("").append("\r\n");
        buffer.append("r=").append("").append("\r\n");
        buffer.append("tkid=").append(tkid);

        return buffer.toString();
    }

    /**
     * 返回给客户端的结果信息
     * 
     * @Date in Sep 2, 2013,8:10:38 PM
     * @param status
     * @param data
     * @return
     */
    private String response(int status, String data) {
        if (null == data) {
            data = "";
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("status=").append(status).append("\r\n");
        buffer.append(data);

        return buffer.toString();
    }

    /**
     * 获取分发配置的实体信息
     */
    public TokenConfInfo getDistConfInfo() {
        List<?> configList = ConfDataFormat.getConfList(ConfConstant.CONF_TYPE_TOKEN);
        return TokenConfInfo.getTknconfInfoList(configList);
    }

}
