/**
 *Administrator
 */
package com.ft.otp.core.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 基础拦截器，常用设置检查、拦截
 *
 * @Date in Jul 1, 2011,4:03:39 PM
 *
 * @author TBM
 */
public class BaseInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = -2375191696745378880L;

    //拦截器过滤的非法字符、字符串
    private String filterStr = "&amp;_alert_alter _ database _ table _drop _exec _insert _select _delete _update _ count_ mid_master _truncate _declare _ or _ like ";

    /**
     * 基础拦截器
     */
    public String intercept(ActionInvocation invo) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        //请求URL
        boolean paramCheck = getParamValue(request);
        if (!paramCheck) {
            return Action.ERROR;//错误
        }

        return invo.invoke();
    }

    /**
     * 参数内容格式合法性检查
     * 
     * @Date in Jul 14, 2012,1:57:46 PM
     * @param str
     * @return
     */
    private boolean checkParam(String str) {
        String[] attrArr = filterStr.split("_");
        for (int i = 0; i < attrArr.length; i++) {
            if (str.toLowerCase().indexOf(attrArr[i].toLowerCase()) != -1) {
                return false;
            }
        }

        return true;
    }

    /**
     * 获取Action业务请求时传入的参数值
     * 
     * @Date in Jul 14, 2012,1:42:15 PM
     * @param request
     * @return
     */
    private boolean getParamValue(HttpServletRequest request) {
        Enumeration enume = request.getParameterNames();
        String paramName = null;
        while (enume.hasMoreElements()) {
            paramName = (String) enume.nextElement();
            String values[] = request.getParameterValues(paramName);
            for (int i = 0; i < values.length; i++) {
                if (!checkParam(values[i])) {
                    return false;
                }
            }
        }

        return true;
    }

}
