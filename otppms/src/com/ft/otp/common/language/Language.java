/**
 *Administrator
 */
package com.ft.otp.common.language;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.LanguageConfig;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 公共类
 * 
 * 用于在Action层输入提示信息，取得提示信息的多语言版本
 *
 * @Date in Apr 19, 2011,5:49:06 PM
 *
 * @author TBM
 */
public class Language {

    /**
     * Action层获取多语言数据，传入request
     * @Date in Apr 19, 2011,5:57:31 PM
     * @param request
     * @param langKey 多语言唯一KEY
     * @param replaceVal 要替换%d的对象值
     * @return
     */
    public static String getLangStr(HttpServletRequest request, String langKey) {
        HttpSession session = request.getSession();
        String currLang = getCurrLang(session);

        return getLangValue(langKey, currLang, null);
    }

    /**
     * Action层获取多语言数据，传入session
     * @Date in Apr 19, 2011,6:05:37 PM
     * @param session
     * @param langKey 多语言唯一KEY
     * @param replaceVal 要替换%d的对象值
     * @return
     */
    public static String getLangStr(HttpSession session, String langKey) {
        String currLang = getCurrLang(session);

        return getLangValue(langKey, currLang, null);
    }

    /**
     * 取得当前会话语言类型
     * @Date in Apr 20, 2011,9:50:47 AM
     * @param session
     * @return
     */
    public static String getCurrLang(HttpSession session) {
        String currLang = "";
        if (null == session && ServletActionContext.getContext()!=null) {//会话为null，取request对象，获取session
            HttpServletRequest request = ServletActionContext.getRequest();
            if (null != request) {
                session = request.getSession();
            }
        }
        if (null != session) {//取会话中的当前语言
            Object object = session.getAttribute(Constant.LANGUAGE_SESSION_KEY);
            if (null != object) {
                currLang = (String) object;

                return currLang;
            }
        }

        //系统公共配置中的语言
        currLang = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DEFAULT_SYSTEM_LANGUAGE);

        if (!StrTool.strNotNull(currLang)) {
            //当前计算机的语言环境
            Locale sysLocale = Locale.getDefault();
            if (StrTool.objNotNull(sysLocale)) {
                currLang = sysLocale.getLanguage() + "_" + sysLocale.getCountry();
            } else {
                currLang = Constant.DEFAULT_LANGUAGE;
            }
        }

        return currLang;
    }

    /**
     * 根据langKey及currLang取得对应语言资源
     * @Date in Apr 19, 2011,6:09:06 PM
     * @param langKey
     * @param currLang
     * @param replaceVal
     * @return
     */
    public static String getLangValue(String langKey, String currLang, String replaceVal) {
        String langValue = LanguageConfig.getLangValue(langKey, currLang);

        if (StrTool.strNotNull(langValue)) {
            if (StrTool.indexOf(langValue, "%d") && StrTool.strNotNull(replaceVal)) {
                langValue = langValue.replace("%d", replaceVal);
            }
        }

        return StrTool.toString(langValue);
    }

    /**
     * 根据langKey取得对应语言资源
     * @Date in Apr 19, 2011,6:09:06 PM
     * @param request
     * @param langKey
     * @param replaceVal
     * @return
     */
    public static String getReplaceLangValue(HttpServletRequest request, String langKey, String replaceVal) {
        String langValue = getLangStr(request, langKey);

        if (StrTool.strNotNull(langValue)) {
            if (StrTool.indexOf(langValue, "%d") && StrTool.strNotNull(replaceVal)) {
                langValue = langValue.replace("%d", replaceVal);
            }
        }

        return StrTool.toString(langValue);
    }

}
