package com.ft.otp.common.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.SmsInfoConfig;
import com.ft.otp.manager.confinfo.sms.entity.SmsInfo;
import com.ft.otp.util.tool.StrTool;

/**
 * 短信发送工具类
 *
 * @Date in Jun 14, 2013,6:11:38 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class SendSMSTool {

    private static Logger logger = Logger.getLogger(SendSMSTool.class);

    /**
     * 短信发送业务
     * 
     * @Date in Jun 17, 2013,10:38:53 AM
     * @param phone
     * @param message
     * @return
     */
    public static boolean send(String phone, String message) {
        boolean result = false;
        if (!StrTool.strNotNull(phone) || !StrTool.strNotNull(message)) {
            return result;
        }
        boolean smsLoadResult = SmsInfoConfig.getLoadResult();
        if (!smsLoadResult) {
            return result;
        }

        int smsSize = SmsInfoConfig.getSmsMap().size();
        return smsSend(phone, message, smsSize, 0);
    }

    /**
     * 获取已配置的短信网关，组装短信发送URL
     * 
     * @Date in Jun 17, 2013,4:29:59 PM
     * @param phone
     * @param message
     * @param smsSize 短信网关个数
     * @param count 下一个短信网关
     * @param retry 重试次数
     * @return
     */
    private static boolean smsSend(String phone, String message, int smsSize, int count) {
        boolean result = false; 
        SmsInfo smsInfo = SmsInfoConfig.getSmsInfo(count); //获取一个短信网关

        String requestUrl = smsSendUrl(smsInfo, phone, message); //组装短信发送URL
        String sendResult = httpRequest(requestUrl, smsInfo.getRetrysend(), 0); //短信发送

        if (StrTool.strNotNull(sendResult) && StrTool.strEquals(sendResult, smsInfo.getSendresult())) {
            result = true;
        } else {
            if (smsSize > NumConstant.common_number_1 && count < smsSize) {
                count++;
                if (count == smsSize) {
                    return result;
                }
                return smsSend(phone, message, smsSize, count);
            }
        }

        return result;
    }

    /**
     * 短信网关连接，短信内容传输
     * 
     * @Date in Jun 17, 2013,4:19:03 PM
     * @param requestUrl
     * @param retryNum
     * @param count
     * @return
     */
    private static String httpRequest(String requestUrl, int retryNum, int count) {
        String sendResult = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);

            connection.getResponseMessage();
            int code = connection.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) { //连接成功
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String lineStr;
                while (null != ((lineStr = in.readLine()))) {
                    sendResult += lineStr;
                }
            } else {
                if (retryNum > NumConstant.common_number_0 && count < retryNum) {
                    count++;
                    if (count == retryNum) {
                        return sendResult;
                    }
                    if (null != connection) {
                        connection.disconnect();
                    }
                    return httpRequest(requestUrl, retryNum, count);
                }
            }
            if (null != connection) {
                connection.disconnect();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return sendResult;
    }

    /**
     * 请求短信网关进行业务数据发送的URL
     * 
     * @Date in Jun 17, 2013,10:22:34 AM
     * @param phone
     * @param message
     * @return
     */
    public static String smsSendUrl(SmsInfo smsInfo, String phone, String message) {
        String requestUrl = "";

        requestUrl = smsInfo.getHost();
        requestUrl = setParam(requestUrl, smsInfo.getAccountattr(), smsInfo.getUsername(), true);
        requestUrl = setParam(requestUrl, smsInfo.getPasswdattr(), smsInfo.getPwd(), false);
        requestUrl = setParam(requestUrl, smsInfo.getPhoneattr(), phone, false);
        requestUrl = setParam(requestUrl, smsInfo.getMessageattr(), message, false);

        String annex = smsInfo.getParamannex();
        //oracle 数据为空时，实体对象返回null
        if (StrTool.strNotNull(annex) && !annex.startsWith("&")) {
            annex = "&" + annex;
            requestUrl += annex;
        }

        return requestUrl;
    }

    /**
     * URL参数的组装
     * 
     * @Date in Jun 17, 2013,10:26:16 AM
     * @param requestUrl
     * @param argname
     * @param argvalue
     * @param mark
     * @return
     */
    public static String setParam(String requestUrl, String argname, String argvalue, boolean mark) {
        if (StrTool.strNotNull(argname) && StrTool.strNotNull(argvalue)) {
            if (mark) {
                requestUrl = requestUrl + "?" + argname + "=";
            } else {
                requestUrl = requestUrl + "&" + argname + "=";
            }
            try {
                String encoded = URLEncoder.encode(argvalue, "UTF-8");
                requestUrl = requestUrl + encoded;
            } catch (UnsupportedEncodingException e) {
                requestUrl = requestUrl + argvalue;
            }
        }

        return requestUrl;
    }

}
