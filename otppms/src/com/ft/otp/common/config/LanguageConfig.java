package com.ft.otp.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ft.otp.common.AdmLogConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.util.properties.BaseProperties;
import com.ft.otp.util.tool.StrTool;

/**
 * 多语言资源初始化
 *
 * @Date in Apr 8, 2010,3:08:54 PM
 *
 * @author TBM
 */
public class LanguageConfig {
    private Logger logger = Logger.getLogger(LanguageConfig.class);

    private static LanguageConfig config = null;

    //对外调用多语言map
    public static Map<Object, Object> langMap;
    public static Map<Object, Object> enLangMap;

    /**
     * Map 存放语言资源
     */
    private static Map<Object, Object> zh_CN_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> en_US_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> ja_JP_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> ar_SA_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> zh_TW_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> nl_NL_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> en_AU_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> en_CA_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> en_GB_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> fr_CA_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> fr_FR_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> de_DE_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> he_IL_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> hi_IN_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> it_IT_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> ko_KR_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> es_ES_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> pt_BR_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> sv_SE_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> th_TH_Map = new HashMap<Object, Object>();
    private static Map<Object, Object> th_TH_TH_Map = new HashMap<Object, Object>();

    private LanguageConfig() {
        try {
            if (getZone(Constant.zh_CN)) {
                loadLangFile(zh_CN_Map, Constant.LANGUAGE_PATH + Constant.zh_CN);

                //封装查询日志信息对外调用langMap
                if (StrTool.mapNotNull(zh_CN_Map)) {
                    Set<Object> set = zh_CN_Map.keySet();
                    langMap = new HashMap<Object, Object>();
                    for (Iterator<Object> iter = set.iterator(); iter.hasNext();) {
                        String key = (String) iter.next();
                        if (key.contains(AdmLogConstant.lang_action_id) || key.contains(AdmLogConstant.lang_action_obj)) {
                            langMap.put(key, zh_CN_Map.get(key));
                        }
                    }
                }
            }
            if (getZone(Constant.en_US)) {
                loadLangFile(en_US_Map, Constant.LANGUAGE_PATH + Constant.en_US);

                if (StrTool.mapNotNull(en_US_Map)) {
                    Set<Object> enset = en_US_Map.keySet();
                    enLangMap = new HashMap<Object, Object>();
                    for (Iterator<Object> eniter = enset.iterator(); eniter.hasNext();) {
                        String key = (String) eniter.next();
                        if (key.contains(AdmLogConstant.lang_action_id) || key.contains(AdmLogConstant.lang_action_obj)) {
                            enLangMap.put(key, en_US_Map.get(key));
                        }
                    }
                }
            }
            if (getZone(Constant.ja_JP)) {
                loadLangFile(ja_JP_Map, Constant.LANGUAGE_PATH + Constant.ja_JP);
            }
            if (getZone(Constant.ar_SA)) {
                loadLangFile(ar_SA_Map, Constant.LANGUAGE_PATH + Constant.ar_SA);
            }
            if (getZone(Constant.zh_TW)) {
                loadLangFile(zh_TW_Map, Constant.LANGUAGE_PATH + Constant.zh_TW);
            }
            if (getZone(Constant.nl_NL)) {
                loadLangFile(nl_NL_Map, Constant.LANGUAGE_PATH + Constant.nl_NL);
            }
            if (getZone(Constant.en_AU)) {
                loadLangFile(en_AU_Map, Constant.LANGUAGE_PATH + Constant.en_AU);
            }
            if (getZone(Constant.en_CA)) {
                loadLangFile(en_CA_Map, Constant.LANGUAGE_PATH + Constant.en_CA);
            }
            if (getZone(Constant.en_GB)) {
                loadLangFile(en_GB_Map, Constant.LANGUAGE_PATH + Constant.en_GB);
            }
            if (getZone(Constant.fr_FR)) {
                loadLangFile(fr_FR_Map, Constant.LANGUAGE_PATH + Constant.fr_FR);
            }
            if (getZone(Constant.fr_CA)) {
                loadLangFile(fr_CA_Map, Constant.LANGUAGE_PATH + Constant.fr_CA);
            }
            if (getZone(Constant.de_DE)) {
                loadLangFile(de_DE_Map, Constant.LANGUAGE_PATH + Constant.de_DE);
            }
            if (getZone(Constant.he_IL)) {
                loadLangFile(he_IL_Map, Constant.LANGUAGE_PATH + Constant.he_IL);
            }
            if (getZone(Constant.hi_IN)) {
                loadLangFile(hi_IN_Map, Constant.LANGUAGE_PATH + Constant.hi_IN);
            }
            if (getZone(Constant.it_IT)) {
                loadLangFile(it_IT_Map, Constant.LANGUAGE_PATH + Constant.it_IT);
            }
            if (getZone(Constant.ko_KR)) {
                loadLangFile(ko_KR_Map, Constant.LANGUAGE_PATH + Constant.ko_KR);
            }
            if (getZone(Constant.es_ES)) {
                loadLangFile(es_ES_Map, Constant.LANGUAGE_PATH + Constant.es_ES);
            }
            if (getZone(Constant.pt_BR)) {
                loadLangFile(pt_BR_Map, Constant.LANGUAGE_PATH + Constant.pt_BR);
            }
            if (getZone(Constant.sv_SE)) {
                loadLangFile(sv_SE_Map, Constant.LANGUAGE_PATH + Constant.sv_SE);
            }
            if (getZone(Constant.th_TH)) {
                loadLangFile(th_TH_Map, Constant.LANGUAGE_PATH + Constant.th_TH);
            }
            if (getZone(Constant.th_TH_TH)) {
                loadLangFile(th_TH_TH_Map, Constant.LANGUAGE_PATH + Constant.th_TH_TH);
            }

        } catch (Exception e) {
            logger.error("Language file loaded failure!", e);
        }
    }

    /**
     * 加载多语言配置文件
     * 
     * @Date in May 10, 2013,3:04:48 PM
     * @param objMap
     * @param path
     */
    private void loadLangFile(Map<Object, Object> objMap, String path) throws Exception {
        InputStream inStream = null;
        BaseProperties properties = new BaseProperties();
        File file = new File(path);
        String[] tempList = file.list();
        File tempFile = null;

        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                tempFile = new File(path + tempList[i]);
            } else {
                tempFile = new File(path + File.separator + tempList[i]);
            }
            inStream = new FileInputStream(tempFile);
            properties.load(inStream);
            if (null != inStream) {
                inStream.close();
            }
        }

        objMap.putAll(properties);
        properties.clear();
    }

    /**
     * 判断系统支持并要加载的语言
     * 
     * @Date in Apr 19, 2011,1:10:50 PM
     * @param key
     * @return
     */
    public static boolean getZone(String key) {
        boolean ifLoad = false;
        String lang = SystemConfig.getValue(key);
        if (!StrTool.strNotNull(lang)) {
            return ifLoad;
        }

        try {
            ifLoad = Boolean.valueOf(lang);
        } catch (Exception e) {
            ifLoad = false;
        }

        return ifLoad;
    }

    /**
     * 加载多语言资源
     * @Date in Apr 14, 2011,3:38:39 PM
     * @return
     */
    public static LanguageConfig loadLanguage() {
        if (config != null) {
            return config;
        }

        synchronized (ProxoolConfig.class) {
            if (config == null) {
                config = new LanguageConfig();
            }
            return config;
        }
    }

    /**
     * 根据key取得Value
     * @Date in Mar 30, 2010,4:26:51 PM
     * @param key
     * @return
     */
    public static String getLangValue(String key, String lang) {
        String value = "";

        if (StrTool.strEquals(lang, Constant.DEFAULT_LANGUAGE)) {
            value = (String) en_US_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.zh_CN)) {
            value = (String) zh_CN_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.ja_JP)) {
            value = (String) ja_JP_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.ar_SA)) {
            value = (String) ar_SA_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.zh_TW)) {
            value = (String) zh_TW_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.nl_NL)) {
            value = (String) nl_NL_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.en_AU)) {
            value = (String) en_AU_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.en_CA)) {
            value = (String) en_CA_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.en_GB)) {
            value = (String) en_GB_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.fr_CA)) {
            value = (String) fr_CA_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.fr_FR)) {
            value = (String) fr_FR_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.de_DE)) {
            value = (String) de_DE_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.he_IL)) {
            value = (String) he_IL_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.hi_IN)) {
            value = (String) hi_IN_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.it_IT)) {
            value = (String) it_IT_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.ko_KR)) {
            value = (String) ko_KR_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.es_ES)) {
            value = (String) es_ES_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.pt_BR)) {
            value = (String) pt_BR_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.sv_SE)) {
            value = (String) sv_SE_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.th_TH)) {
            value = (String) th_TH_Map.get(key);
        } else if (StrTool.strEquals(lang, Constant.th_TH_TH)) {
            value = (String) th_TH_TH_Map.get(key);
        }
        
        if(!StrTool.strNotNull(value)){
        	value = (String) en_US_Map.get(key);
        }

        return value;
    }

    /**
     * 清空Map，重置config
     * @Date in Mar 30, 2010,4:27:29 PM
     */
    public static void clear() {
        if (null != config) {
            if (null != zh_CN_Map) {
                zh_CN_Map.clear();
            }
            if (null != en_US_Map) {
                en_US_Map.clear();
            }
            if (null != ja_JP_Map) {
                ja_JP_Map.clear();
            }
            if (null != ar_SA_Map) {
                ar_SA_Map.clear();
            }
            if (null != zh_TW_Map) {
                zh_TW_Map.clear();
            }
            if (null != nl_NL_Map) {
                nl_NL_Map.clear();
            }
            if (null != en_AU_Map) {
                en_AU_Map.clear();
            }
            if (null != en_CA_Map) {
                en_CA_Map.clear();
            }
            if (null != en_GB_Map) {
                en_GB_Map.clear();
            }
            if (null != fr_CA_Map) {
                fr_CA_Map.clear();
            }
            if (null != fr_FR_Map) {
                fr_FR_Map.clear();
            }
            if (null != de_DE_Map) {
                de_DE_Map.clear();
            }
            if (null != he_IL_Map) {
                he_IL_Map.clear();
            }
            if (null != hi_IN_Map) {
                hi_IN_Map.clear();
            }
            if (null != it_IT_Map) {
                it_IT_Map.clear();
            }
            if (null != ko_KR_Map) {
                ko_KR_Map.clear();
            }
            if (null != es_ES_Map) {
                es_ES_Map.clear();
            }
            if (null != pt_BR_Map) {
                pt_BR_Map.clear();
            }
            if (null != sv_SE_Map) {
                sv_SE_Map.clear();
            }
            if (null != th_TH_Map) {
                th_TH_Map.clear();
            }
            if (null != th_TH_TH_Map) {
                th_TH_TH_Map.clear();
            }

            config = null;
        }
    }
}