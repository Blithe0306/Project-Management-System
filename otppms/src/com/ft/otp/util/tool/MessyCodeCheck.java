/**
 *Administrator
 */
package com.ft.otp.util.tool;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串是否为乱码的检查，如果为乱码转为正常的字符
 *
 * @Date in May 19, 2012,7:11:11 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class MessyCodeCheck {

    public static boolean isChinese(char c) throws Exception {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 是否为乱码检查
     * 
     * @Date in May 19, 2012,7:13:05 PM
     * @param strName
     * @return
     */
    public static boolean isMessyCode(String strName) throws Exception {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                    System.out.print(c);
                }
            }
        }
        float result = count / chLength;
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ISO-8859-1 编码转为 UTF8
     * 
     * @Date in May 19, 2012,7:15:01 PM
     * @param str
     * @return
     */
    public static String iso885901ForUTF8(String str) {
        if (!StrTool.strNotNull(str)) {
            return str;
        }

        try {
            if (isMessyCode(str)) {
                str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            }
        } catch (Exception e) {
            return str;
        }

        return str;
    }



            public static void main(String[] args) throws InterruptedException {
                    System.getProperties().list(System.out); 
                    
                    System.out.println("******************");
                    
                    
                    final String encoding = System.getProperty("file.encoding");  
                    System.out.println("encoding:"+encoding); 
                    
                    
                    String path= "./哈haha哈AAA璎玥.txt";
                    System.out.println(path);
                    
                    // TODO file.encoding=iso8859-1
                    try {
                        String newp = new String(path.getBytes("gbk"),encoding);
                        System.out.println(newp);
                        File file = new File(newp);
                        boolean b = file.createNewFile();
                        System.out.println("file create:"+b);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } 


    }
 
