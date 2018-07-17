/**
 *Administrator
 */
package com.ft.otp.common.soap.help;

import com.ft.otp.common.soap.MessageBean;
import com.ft.otp.common.soap.WebServiceFactory;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.util.tool.StrTool;

/**
 * 同步助手
 *
 * @Date in May 22, 2013,2:39:04 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class SyncHelper {

    /**
     * 令牌同步
     * 
     * @Date in May 22, 2013,11:05:10 AM
     * @param bean
     * @return
     */
    public int syncToken(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), bean.getOtp(), bean.getNextOtp(),
                bean.getChallengeCode() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_SYNC, param);

        return StrTool.parseInt(resultArr[0]);
    }

    /**
     * 获取设备信息
     * 
     * @Date in May 23, 2013,1:47:00 PM
     * @param soapUrl
     * @param type
     * @return
     */
    public String getDeviceInfo(String soapUrl, int type) {
        Object[] param = new Object[] { type };
        Object[] objects = WebServiceFactory.soapRequest(soapUrl, SoapAttribute.REQUEST_METHOD_DEVICEINFO, param);
        if (null == objects || objects.length <= 0) {
            return null;
        }

        return objects[0].toString();
    }

    /**
     * 请求服务器更新配置
     * 
     * @Date in Jun 8, 2013,4:15:38 PM
     * @param confType
     */
    public synchronized static void replaceConf(int confType) {
        //if (!WebServiceFactory.soapConnState()) {
        //    return;
        //}
        try {
            Object[] param = new Object[] { confType };
            WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_RECONFIG, param);
        } catch (Exception e) {

        }
    }

}
