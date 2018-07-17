/**
 *Administrator
 */
package com.ft.otp.common.soap;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;
import com.ft.otp.common.NumConstant;
import com.ft.otp.common.config.WSUrlConfig;
import com.ft.otp.common.soap.code.SoapAttribute;
import com.ft.otp.util.tool.StrTool;

/**
 * WebService工厂
 *
 * @Date in Jan 15, 2013,11:28:36 AM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class WebServiceFactory {

    private static Logger log = Logger.getLogger(WebServiceFactory.class);

    private static JaxWsDynamicClientFactory clientFactory = null;
    private static Client client = null;

    private WebServiceFactory() {
        if (null == clientFactory) {
            clientFactory = JaxWsDynamicClientFactory.newInstance();
        }

        String soapUrl = null;
        try {
            soapUrl = createClient(NumConstant.common_number_0);
            client = clientFactory.createClient(soapUrl);
            connTest(); //连接测试
        } catch (Exception e) {
            log.error(e);
            try {
                String soapUrl2 = createClient(NumConstant.common_number_1);
                if (!StrTool.strEqualsIgnoreCase(soapUrl2, soapUrl)) {
                    client = clientFactory.createClient(soapUrl2);
                    connTest(); //连接测试
                }
            } catch (Exception e1) {
                log.error(e1);
            }
        }

        setClientTimeOut();
    }

    /**
     * 设置客户端超时，单位为毫秒  
     * 
     * @Date in Jul 30, 2013,5:04:11 PM
     */
    private void setClientTimeOut() {
        if (null == client) {
            return;
        }

        HTTPConduit http = (HTTPConduit) client.getConduit();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(3500); //连接超时，毫秒
        httpClientPolicy.setAllowChunking(false); //取消块编码
        httpClientPolicy.setReceiveTimeout(3000); //响应超时，毫秒
        http.setClient(httpClientPolicy);
    }

    /**
     * 加载Web Service工厂
     * 
     * @Date in May 23, 2013,11:47:32 AM
     */
    public static void loadWebServiceFactory() {
        synchronized (WebServiceFactory.class) {
            WSUrlConfig.reLoad();
            new WebServiceFactory();

            return;
        }
    }

    /**
     * WebService访问请求URL
     * 
     * @Date in May 23, 2013,11:35:41 AM
     * @param type
     * @return
     */
    private String createClient(int type) {
        String soapUrl = null;
        if (type == NumConstant.common_number_0) {
            soapUrl = WSUrlConfig.getMainUrl();
        } else {
            soapUrl = WSUrlConfig.getSpareUrl();
        }

        return soapUrl;
    }

    /**
     * SOAP方式请求
     * 
     * @Date in May 21, 2013,2:38:35 PM
     * @param method
     * @param param
     * @return
     */
    public static String[] soapRequest(String method, Object param[]) {
        String[] result = new String[2];
        if (!soapConnState()) {
            loadWebServiceFactory();

            if (!soapConnState()) {
                //创建连接失败
                result[0] = String.valueOf(SoapAttribute.LOCAL_URL_NULL);
                return result;
            }
        }

        Object[] objects = invokeWSInterface(method, param);
        if (null == objects || objects.length <= 0) {
            if (null != client) {
                client.destroy();
                client = null;
                clientFactory = null;
            }

            loadWebServiceFactory();
            if (!soapConnState()) {
                //创建连接失败
                result[0] = String.valueOf(SoapAttribute.LOCAL_URL_NULL);
                return result;
            } else {
                objects = invokeWSInterface(method, param);
                if (null == objects || objects.length <= 0) {
                    //SOAP请求失败，请求异常
                    result[0] = String.valueOf(SoapAttribute.LOCAL_URL_NULL);
                    return result;
                }
            }
        }
        String xmlData = objects[0].toString();

        return returnInfoDec(xmlData);
    }

    /**
     * SOAP请求的连接状态
     * 
     * @Date in Jul 6, 2013,12:36:44 PM
     * @return
     */
    public synchronized static boolean soapConnState() {
        if (null == clientFactory || null == client) {
            clientFactory = null;
            if (null != client) {
                client.destroy();
                client = null;
            }

            return false;
        }

        return true;
    }

    /**
     * 返回的XML结果解析
     * 
     * @Date in May 22, 2013,10:38:00 AM
     * @param xmlData
     * @return
     */
    public static String[] returnInfoDec(String xmlData) {
        String[] result = null;
        int dataLen = StrTool.strCount(xmlData, "<returnData>");
        if (dataLen <= 0) {
            result = new String[2];
        } else {
            result = new String[dataLen + 1];
        }

        if (xmlData.indexOf("<returnCode>") != -1) {
            result[0] = xmlData.substring(xmlData.indexOf("<returnCode>") + "<returnCode>".length(), xmlData
                    .indexOf("</returnCode>"));
        } else {
            result[0] = String.valueOf(SoapAttribute.LOCAL_URL_NULL);
            result[1] = "";

            return result;
        }

        if (dataLen > 0) {
            String[] dataArr = null;
            dataArr = xmlData.split("</returnData>");
            int k = 1;
            for (int i = 0; i < dataLen; i++) {
                result[k] = dataArr[i].substring(dataArr[i].indexOf("<returnData>") + "<returnData>".length(),
                        dataArr[i].length());
                k++;
            }
        } else {
            result[1] = "";
        }

        return result;
    }

    /**
     * WebService客户端请求集中发送/接收方法
     * 
     * @Date in May 21, 2013,1:27:58 PM
     * @param method
     * @param param
     * @return
     */
    private static Object[] invokeWSInterface(String method, Object param[]) {
        Object[] obj = null;
        try {
            if (client == null) {
                log.error("In invokeWSInterface:WebService client is null, not create success?");
                return null;
            }
            obj = client.invoke(method, param);
        } catch (Exception ex) {
            log.error(ex);
        }

        return obj;
    }

    /**
     * 针对URL变化的SOAP请求，使用下列方式
     * 
     * @Date in May 23, 2013,1:40:52 PM
     * @param soapUrl
     * @param method
     * @param param
     * @return
     */
    public static Object[] soapRequest(String soapUrl, String method, Object param[]) {
        Object[] obj = null;
        Client client = null;
        try {
            client = clientFactory.createClient(soapUrl);
            if (client == null) {
                log.error("In soapRequest:WebService client is not create success");
                return null;
            }
            obj = client.invoke(method, param);
        } catch (Exception ex) {
            log.error(ex);
        } finally {
            if (null != client) {
                client.destroy();
                client = null;
            }
        }

        return obj;
    }

    /**
     * 连接测试，通过调用获取服务器状态接口
     * 
     * @Date in Sep 9, 2013,1:48:46 PM
     * @return
     */
    private void connTest() throws Exception {
        Object[] param = new Object[] {};
        if (client == null) {
            log.error("In connTest:WebService client is null, not create success?");
            return;
        }
        client.invoke(SoapAttribute.REQUEST_METHOD_SERVERSTATE, param);
    }

}
