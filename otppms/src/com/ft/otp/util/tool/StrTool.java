/**
 *Administrator
 */
package com.ft.otp.util.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.common.Constant;
import com.ft.otp.util.conf.ConfDataFormat;

/**
 * 字符串处理(格式化)类
 *
 * @Date in Apr 8, 2011,6:52:04 PM
 *
 * @author TBM
 */
public class StrTool {

    /**
     * 判断字符串是否为空，为空返回""，否则返回str
     * 
     * @Date in Mar 25, 2010,9:48:53 AM
     * @param str
     * @return
     */
    public static String toString(String str) {
        if (null == str || str.length() == -1) {
            return "";
        }
        return str;
    }

    /**
     * 字符串是否不为空， null或""均视为空
     * 
     * @param value
     * @return
     */
    public static boolean strNotNull(String value) {
        if (null != value && !"".equals(value.trim())) {
            return true;
        }
        return false;
    }
    
    /**
     * 字符串是否不为空， null或""均视为空
     * 
     * @param value
     * @return
     */
    public static boolean strIsNotNull(String value) {
        if (null != value && !"".equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * byte数组是否不为空
     * 
     * @param value
     * @return
     */
    public static boolean byteNotNull(byte[] value) {
        if (null != value && value.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断A字符串不为NULL并且equals字符串B
     * @Date in Apr 14, 2011,5:40:43 PM
     * @param valueA
     * @param valueB
     * @return
     */
    public static boolean strEquals(String valueA, String valueB) {
        if ((strNotNull(valueA) && strNotNull(valueB)) && (valueA.trim().equals(valueB.trim()))) {
            return true;
        }
        return false;
    }
    
    /**
     * 判断A字符串equals字符串B
     * @author LXH
     * @date Mar 7, 2014 6:15:34 PM
     * @param valueA
     * @param valueB
     * @return
     */
    public static boolean strEqualsToNull(String valueA, String valueB) {
        if ((valueA.trim().equals(valueB.trim()))) {
            return true;
        }
        return false;
    }

    /**
     * 判断A字符串不为NULL并且equalsIgnoreCase字符串B
     * @Date in Apr 14, 2011,5:40:43 PM
     * @param valueA
     * @param valueB
     * @return
     */
    public static boolean strEqualsIgnoreCase(String valueA, String valueB) {
        if ((strNotNull(valueA) && strNotNull(valueB)) && (valueA.trim().equalsIgnoreCase(valueB.trim()))) {
            return true;
        }
        return false;
    }

    /**
     * 对象是否为空
     * @Date in Apr 8, 2011,7:07:21 PM
     * @param object
     * @return
     */
    public static boolean objNotNull(Object object) {
        if (null != object) {
            return true;
        }
        return false;
    }

    /**
     * 列表是否为空
     * @Date in Apr 8, 2011,7:09:45 PM
     * @param list
     * @return
     */
    public static boolean listNotNull(List<?> list) {
        if (null != list && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Set是否为空
     * @Date in Apr 10, 2011,3:03:40 PM
     * @param set
     * @return
     */
    public static boolean setNotNull(Set<?> set) {
        if (null != set && set.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * Map是否为空
     * @Date in Apr 10, 2011,3:04:50 PM
     * @param map
     * @return
     */
    public static boolean mapNotNull(Map<?, ?> map) {
        if (null != map && map.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * String 数组是否为空
     * @Date in Apr 8, 2011,7:10:45 PM
     * @param arr
     * @return
     */
    public static boolean arrNotNull(String[] arr) {
        if (null != arr && arr.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * int 数组是否为空
     * @Date in Apr 8, 2011,7:10:45 PM
     * @param arr
     * @return
     */
    public static boolean arrNotNull(int[] arr) {
        if (null != arr && arr.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断A字符串中是否包含B字符串
     * @Date in Apr 14, 2011,4:56:37 PM
     * @param allStr
     * @param str
     * @return
     */
    public static boolean indexOf(String allStr, String str) {
        if (!strNotNull(allStr) || !strNotNull(str)) {
            return false;
        }
        if (allStr.indexOf(str) != -1) {
            return true;
        }

        return false;
    }

    /**
     * 打印系统信息
     * @param msg
     */
    public static void print(String msg) {
        System.out.println(msg);
    }

    /**
     * String -> int
     * @param str
     * @return
     */
    public static int parseInt(String str) {
        if (null != str && !"".equals(str)) {
            try {
                return Integer.parseInt(str);
            } catch (Exception ex) {
            }
        }
        return 0;
    }

    /**
     * String -> Long
     * @param str
     * @return
     */
    public static long parseLong(String str) {
        if (null != str && !"".equals(str)) {
            try {
                return Long.parseLong(str);
            } catch (Exception ex) {
            }
        }
        return 0;
    }

    /**
     * String -> double
     * @Date in Apr 8, 2011,6:59:03 PM
     * @param str
     * @return
     */
    public static double parseDouble(String str) {
        if (null != str && !"".equals(str)) {
            try {
                return Double.parseDouble(str);
            } catch (Exception ex) {
            }
        }
        return 0.0;
    }

    /**
     * long -> int
     * @Date in Apr 28, 2011,5:30:12 PM
     * @param value
     * @return
     */
    public static int longToInt(long value) {
        Long lValue = value;
        try {
            return lValue.intValue();
        } catch (Exception ex) {
        }
        return 0;
    }

    /**
     * int -> String
     * @Date in Jun 2, 2011,1:42:16 PM
     * @param value
     * @return
     */
    public static String intToString(int value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * long -> String
     * @Date in Jun 2, 2011,1:42:53 PM
     * @param value
     * @return
     */
    public static String longToString(long value) {
        try {
            return String.valueOf(value);
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 返回当前时间毫秒字符串
     * 
     * @Date in May 11, 2011,4:21:14 PM
     * @return
     */
    public static String timeMillis() {
        long time = System.currentTimeMillis();
        try {
            return String.valueOf(time);
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 返回当前时间秒数
     * 
     * @Date in Jun 8, 2011,6:08:16 PM
     * @return
     */
    public static int timeSecond() {
        try {
            long timeL = System.currentTimeMillis() / 1000;
            return longToInt(timeL);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 返回当前时间秒数
     * 
     * @Date in Jun 8, 2011,6:08:16 PM
     * @return
     */
    public static long timeSecondL() {
        try {
            long timeL = System.currentTimeMillis() / 1000;
            return timeL;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * UTF-8 to Str
     * 
     * @Date in May 16, 2011,4:48:12 PM
     * @param str
     * @return
     */
    public static String utf8ToStr(String str) {
        String newStr = "";
        if (strNotNull(str)) {
            try {
                newStr = URLDecoder.decode(str, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
            }
        }
        return newStr;
    }

    /**
     * 字符串转码：参数转换成-utf8
     *
     * @Date in Jun 3, 2011,1:10:26 PM
     *
     * @author CSL
     */
    public static String strToUTF8(String str) {
        try {
            str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return str;
    }

    /**
     * 检查判断两个列表的数据是否全部相等
     * @Date in Apr 28, 2011,9:34:42 AM
     * @param list1
     * @param listTo
     * @return
     */
    public static boolean isArrEquals(List<?> list1, List<?> listTo) {
        if ((StrTool.listNotNull(list1) && StrTool.listNotNull(listTo)) && (list1.size() == listTo.size())) {
            boolean unLike = false;
            for (int i = 0; i < list1.size(); i++) {
                unLike = false;
                String token = (String) list1.get(i);
                for (int k = 0; k < listTo.size(); k++) {
                    if (token.equals((String) listTo.get(k))) {
                        unLike = true;
                        break;
                    }
                }
                if (!unLike) {
                    break;
                }
            }
            return unLike;
        } else {
            return false;
        }
    }

    /**
     * 比对两个数组元素的重复记录，取出一端比另一端多出的元素
     */
    public static List<String> BLessToA(String[] a, String[] b) {
        List<String> temp = new ArrayList<String>();
        for (int i = 0; i < b.length; i++) {
            boolean flag = false;
            for (int j = 0; j < a.length; j++) {
                if (b[i].equals(a[j])) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                }
            }
            if (!flag) {
                temp.add(b[i]);
            }
        }
        return temp;
    }

    /**
     * 字符串清空两边空格
     * @Date in Jul 11, 2012,1:21:30 PM
     * @param str
     * @return
     */
    public static String trim(String str) {
        return null == str ? str : str.trim();
    }

    /**
     * 判断指定字符或字符串在一个字符串中出现的次数
     * 
     * @Date in Aug 29, 2013,11:18:44 AM
     * @param allStr
     * @param con
     * @return
     */
    public static int strCount(String allStr, String con) {
        allStr = " " + allStr;
        if (allStr.endsWith(con)) {
            return allStr.split(con).length;
        } else {
            return allStr.split(con).length - 1;
        }
    }

    /**
     * 取得系统默认语言
     * 
     * @Date in Mar 31, 2012,6:31:21 PM
     * @return
     */
    public static String systemLanguage() {
        String currLang = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.DEFAULT_SYSTEM_LANGUAGE);
        if (!StrTool.strNotNull(currLang)) {
            Locale sysLocale = Locale.getDefault();
            if (StrTool.objNotNull(sysLocale)) {
                currLang = sysLocale.getLanguage() + "_" + sysLocale.getCountry();
            }
        }

        return currLang;
    }

    /**
     * 判断认证服务器安装目录是否存在
     * 
     * @Date in May 14, 2013,9:06:24 PM
     * @param basePath
     * @return
     */
    public static boolean verifyFilePath(String basePath) {
        String filePath = "";
        boolean result = false;
        try {
            if (basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE) != -1) {
                filePath = basePath.substring(0, basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE));
                filePath += Constant.ROOT_OTP_AUTH_SERVICE;
                result = true;
            } else if (basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE_L) != -1) {
                filePath = basePath.substring(0, basePath.indexOf(Constant.ROOT_OTP_WEB_SERVICE_L));
                filePath += Constant.ROOT_OTP_AUTH_SERVICE_L;
                result = true;
            }
            if (result) {
                File tempFile = new File(filePath);//认证服务器安装目录是否存在
                String[] fileList = tempFile.list();
                if (StrTool.arrNotNull(fileList)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }

        return false;
    }

    /**
     * 对表单描述内容中的特殊字符进行ASCII编码
     * 
     * @Date in Sep 17, 2013,10:25:31 AM
     * @param descp
     * @return
     */
    public static String cleanXSS(String descp) {
        if (!strNotNull(descp)) {
            return descp;
        }
        
        descp = descp.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        descp = descp.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        descp = descp.replaceAll("'", "&#39;");
        descp = descp.replaceAll("eval\\((.*)\\)", "");
        descp = descp.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        descp = descp.replaceAll("script", "");

        return descp;
    }
    
    /**
     * 对表单的特殊字符进行ASCII编码
     * 
     * @Date in Sep 17, 2013,10:25:31 AM
     * @param descp
     * @return
     */
    public static String cleanActivate(String data) {
        if (!strNotNull(data)) {
            return data;
        }
        
        data = data.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        data = data.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        data = data.replaceAll("'", "&#39;");
        data = data.replaceAll("eval\\((.*)\\)", "");
        data = data.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

        return data;
    }

}
