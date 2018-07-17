/**
 *Administrator
 */
package com.ft.otp.common.soap;

import javax.servlet.http.HttpServletRequest;
import com.ft.otp.common.Constant;
import com.ft.otp.common.language.Language;
import com.ft.otp.util.tool.StrTool;

/**
 * 客户端与服务端交互返回失败码
 * 
 * 经过多语言处理
 *
 * @Date in Jun 24, 2011,11:43:24 AM
 *
 * @author TBM
 */
public class LangResultCode {

    /**
     * 返回根据认证错误码匹配的多语言描述性语言
     * 
     * @Date in Jun 24, 2011,11:55:26 AM
     * @param request
     * @param result
     * @return
     */
    public static String getLangStr(HttpServletRequest request, int resultCode) {
        String lang = "";

        String codeKey = Constant.LANG_CODE_KEY + resultCode;
        lang = Language.getLangStr(request, codeKey);
        if (!StrTool.strNotNull(lang)) {
            lang = String.valueOf(resultCode);
        }
        lang = Language.getLangStr(request, "colon") + lang;

        return lang;
    }

}
