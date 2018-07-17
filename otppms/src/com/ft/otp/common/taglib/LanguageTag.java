/**
 *Administrator
 */
package com.ft.otp.common.taglib;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.ft.otp.common.language.Language;

/**
 * 多语言标签
 * 用于获取多语言资源数据
 *
 * @Date in Apr 14, 2011,2:23:37 PM
 *
 * @author TBM
 */
public class LanguageTag extends BaseTag {

    private static final long serialVersionUID = 2172690469738561403L;
    private Logger logger = Logger.getLogger(LanguageTag.class);

    private String key;
    private String replaceVal;

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

    /**
     * @return the replaceVal
     */
    public String getReplaceVal() {
        return replaceVal;
    }

    /**
     * @param replaceVal the replaceVal to set
     */
    public void setReplaceVal(String replaceVal) {
        this.replaceVal = replaceVal;
    }

    public int doEndTag() {
        // 输出
        try {
            pageContext.getOut().print(this.resourceStr());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return EVAL_PAGE;
    }

    /**
     * 根据Key取得对应的多语言值
     * @Date in Apr 14, 2011,4:04:25 PM
     * @return
     */
    private String resourceStr() {
        HttpSession session = pageContext.getSession();
        String currLang = Language.getCurrLang(session);

        return Language.getLangValue(key, currLang, replaceVal);
    }

}
