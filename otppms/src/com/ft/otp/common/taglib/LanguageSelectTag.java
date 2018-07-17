/**
 *Administrator
 */
package com.ft.otp.common.taglib;

import java.util.Locale;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.common.config.LanguageConfig;
import com.ft.otp.common.language.Language;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 登录页面多语言选择Tag
 *
 * @Date in Apr 19, 2011,5:13:36 PM
 *
 * @author TBM
 */
public class LanguageSelectTag extends BaseTag {

    private static final long serialVersionUID = -8742740743838592897L;

    private Logger logger = Logger.getLogger(LanguageSelectTag.class);

    public LanguageSelectTag() {
        super();
    }

    private String key;

    public int doEndTag() {
        try {
            pageContext.getOut().print(this.optionStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return EVAL_PAGE;
    }

    private String optionStr() {
        HttpSession session = pageContext.getSession();
        StringBuilder builder = new StringBuilder();

        if (LanguageConfig.getZone(Constant.zh_CN)) {
            langSelect(builder, session, Constant.zh_CN, "language_zh_CN");
        }
        if (LanguageConfig.getZone(Constant.en_US)) {
            langSelect(builder, session, Constant.en_US, "language_en_US");
        }
        if (LanguageConfig.getZone(Constant.ja_JP)) {
            langSelect(builder, session, Constant.ja_JP, "language_ja_JP");
        }
        if (LanguageConfig.getZone(Constant.ar_SA)) {
            langSelect(builder, session, Constant.ar_SA, "language_ar_SA");
        }
        if (LanguageConfig.getZone(Constant.zh_TW)) {
            langSelect(builder, session, Constant.zh_TW, "language_zh_TW");
        }
        if (LanguageConfig.getZone(Constant.nl_NL)) {
            langSelect(builder, session, Constant.nl_NL, "language_nl_NL");
        }
        if (LanguageConfig.getZone(Constant.en_AU)) {
            langSelect(builder, session, Constant.en_AU, "language_en_AU");
        }
        if (LanguageConfig.getZone(Constant.en_CA)) {
            langSelect(builder, session, Constant.en_CA, "language_en_CA");
        }
        if (LanguageConfig.getZone(Constant.en_GB)) {
            langSelect(builder, session, Constant.en_GB, "language_en_GB");
        }
        if (LanguageConfig.getZone(Constant.fr_FR)) {
            langSelect(builder, session, Constant.fr_FR, "language_fr_FR");
        }
        if (LanguageConfig.getZone(Constant.fr_CA)) {
            langSelect(builder, session, Constant.fr_CA, "language_fr_CA");
        }
        if (LanguageConfig.getZone(Constant.de_DE)) {
            langSelect(builder, session, Constant.de_DE, "language_de_DE");
        }
        if (LanguageConfig.getZone(Constant.he_IL)) {
            langSelect(builder, session, Constant.he_IL, "language_he_IL");
        }
        if (LanguageConfig.getZone(Constant.hi_IN)) {
            langSelect(builder, session, Constant.hi_IN, "language_hi_IN");
        }
        if (LanguageConfig.getZone(Constant.it_IT)) {
            langSelect(builder, session, Constant.it_IT, "language_it_IT");
        }
        if (LanguageConfig.getZone(Constant.ko_KR)) {
            langSelect(builder, session, Constant.ko_KR, "language_ko_KR");
        }
        if (LanguageConfig.getZone(Constant.es_ES)) {
            langSelect(builder, session, Constant.es_ES, "language_es_ES");
        }
        if (LanguageConfig.getZone(Constant.pt_BR)) {
            langSelect(builder, session, Constant.pt_BR, "language_pt_BR");
        }
        if (LanguageConfig.getZone(Constant.sv_SE)) {
            langSelect(builder, session, Constant.sv_SE, "language_sv_SE");
        }
        if (LanguageConfig.getZone(Constant.th_TH)) {
            langSelect(builder, session, Constant.th_TH, "language_th_TH");
        }
        if (LanguageConfig.getZone(Constant.th_TH_TH)) {
            langSelect(builder, session, Constant.th_TH_TH, "language_th_TH_TH");
        }

        return builder.toString();
    }

    /**
     * 多语言选择组织
     * 
     * @Date in May 10, 2013,3:39:18 PM
     * @param builder
     * @param session
     * @param lang
     * @param langKey
     */
    private void langSelect(StringBuilder builder, HttpSession session, String lang, String langKey) {
        builder.append("<option value='").append(lang).append("' ");
        
        if (!StrTool.strNotNull(key)) {
            Object object = session.getAttribute(Constant.LANGUAGE_SESSION_KEY);
            if (null != object) { // 已经设置了会话语言
                key = (String) object;
            } else {
              //系统公共配置中的语言
                String currLang = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON, ConfConstant.DEFAULT_SYSTEM_LANGUAGE);

                if (StrTool.strNotNull(currLang)) { // 系统已经配置过默认语言
                    key = currLang;
                } else {
                  //当前计算机的语言环境
                    Locale sysLocale = Locale.getDefault();
                    if (StrTool.objNotNull(sysLocale)) {
                        currLang = sysLocale.getLanguage() + "_" + sysLocale.getCountry();
                    } else {
                        currLang = Constant.DEFAULT_LANGUAGE;
                    }
                    key = currLang;
                }
            }
        }
        
        if (StrTool.strEquals(key, lang)) {
            builder.append("selected");
        }

        builder.append(">");
        builder.append(Language.getLangStr(session, langKey)).append("\n");
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

}
