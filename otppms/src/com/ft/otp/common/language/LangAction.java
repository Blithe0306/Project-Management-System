/**
 *Administrator
 */
package com.ft.otp.common.language;

import com.ft.otp.base.action.BaseAction;

/**
 * JS获取多语言标签属性值
 *
 * @Date in May 13, 2013,2:58:41 PM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class LangAction extends BaseAction {

    private static final long serialVersionUID = -8598518797346372764L;

    /**
     * 获取多语言信息
     * 
     * @Date in Aug 22, 2013,2:57:06 PM
     * @return
     */
    public String getLangTagVal() {
        String langKey = request.getParameter("langkey");
        try {
            langKey = Language.getLangStr(request, langKey);
        } catch (Exception e) {
            langKey = "";
        }

        setResponseWrite(langKey);
        return null;
    }

}
