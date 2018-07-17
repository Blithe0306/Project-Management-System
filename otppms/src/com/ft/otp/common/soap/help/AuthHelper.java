/**
 *Administrator
 */
package com.ft.otp.common.soap.help;

import com.ft.otp.common.soap.MessageBean;
import com.ft.otp.common.soap.WebServiceFactory;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.util.tool.StrTool;

/**
 * 认证助手
 *
 * @Date in May 21, 2013,3:02:19 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class AuthHelper {

    /**
     * 动态口令认证测试
     * 
     * @Date in May 21, 2013,3:15:21 PM
     * @param bean
     * @return 返回数组，是因为
     */
    public int[] authTest(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), bean.getOtp(), null };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_AUTH, param);
        int[] result = new int[2];
        result[0] = StrTool.parseInt(resultArr[0]);
        if (StrTool.strNotNull(resultArr[1])) {
            result[1] = StrTool.parseInt(resultArr[1]);
        } else {
            result[1] = 0;
        }

        return result;
    }

    /**
     * 获取挑战码
     * 
     * @Date in May 22, 2013,11:49:50 AM
     * @param bean
     * @return
     */
    public String[] getChallengeCode(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_CHALLENGECODE, param);

        return resultArr;
    }

    /**
     * 验证应答码
     * 
     * @Date in May 22, 2013,1:21:43 PM
     * @param bean
     * @return
     */
    public int[] verifyResponseCode(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), null, bean.getChallengeCode(), bean.getResponseCode() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_VERIFYRESPONSECODE, param);
        int[] result = new int[2];
        result[0] = StrTool.parseInt(resultArr[0]);
        if (StrTool.strNotNull(resultArr[1])) {
            result[1] = StrTool.parseInt(resultArr[1]);
        } else {
            result[1] = 0;
        }

        return result;
    }

    /**
     * 获取一级解锁码
     * 
     * @Date in May 22, 2013,3:08:45 PM
     * @param bean
     * @return
     */
    public String[] genPUK1(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), bean.getChallengeCode() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_GENPUK1, param);

        return resultArr;
    }

    /**
     * 获取二级解锁码
     * 
     * @Date in May 22, 2013,3:08:45 PM
     * @param bean
     * @return
     */
    public String[] genPUK2(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), bean.getChallengeCode() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_GENPUK2, param);

        return resultArr;
    }

    /**
     * 获取挑战应答令牌激活码
     * 
     * @Date in Jun 4, 2013,2:52:57 PM
     * @param bean
     * @return
     */
    public String[] genAC(MessageBean bean) {
        Object[] param = new Object[] { bean.getTokenSN(), null };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_GENAC, param);

        return resultArr;
    }

    /**
     * 手机令牌离线激活业务
     * 
     * @Date in Jun 4, 2013,3:14:13 PM
     * @param bean
     * @return
     */
    public String[] offLineActivate(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), bean.getOtp(), bean.getUdid() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_OFFLINEACTIVATE, param);

        return resultArr;
    }

    /**
     * 手机令牌在线分发业务
     * 
     * @Date in Jun 4, 2013,3:31:07 PM
     * @param bean
     * @return
     */
    public int onLineDistribute(MessageBean bean) {
        Object[] param = new Object[] { null, bean.getTokenSN(), bean.getOtp(), bean.getUdid() };
        String[] resultArr = WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_ONLINEDISTRIBUTE, param);

        return StrTool.parseInt(resultArr[0]);
    }

    /**
     * 手机令牌在线激活
     * 
     * @Date in Aug 21, 2013,8:16:26 PM
     * @param bean
     * @return
     */
    public String[] onLineActivate(MessageBean bean) {
        Object[] param = new Object[] { bean.getUserId(), bean.getTokenSN(), bean.getOtp(), bean.getUdid() };
        return WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_ONLINEACTIVATE, param);
    }

    /**
     * 手机令牌：从服务端获取离线激活二维码数据
     * 
     * @Date in Aug 21, 2013,8:16:26 PM
     * @param bean
     * @return
     */
    public String[] genAcData(MessageBean bean) {
        Object[] param = new Object[] { bean.getUserId(), bean.getTokenSN(), bean.getOtp(), bean.getUdid() };
        return WebServiceFactory.soapRequest(SoapAttribute.REQUEST_METHOD_GENACDATA, param);
    }

}
