package com.ft.otp.common.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 短信网关测试明
 *
 * @Date in Jun 20, 2011,2:05:45 PM
 *
 * @author ZJY
 */
public class SendSMS {

    public static String server;
    public static String user;
    public static String password;
    public static String phonenumber;
    public static String message;

    private static String name;
    private static String pass;
    private static String mobile;
    private static String text;
    private static String addparam;

    public static String url_str;

    public static void init(String name, String pass, String mobile, String text, String addparam) {
        SendSMS.name = name;
        SendSMS.pass = pass;
        SendSMS.mobile = mobile;
        SendSMS.text = text;
        SendSMS.addparam = addparam;
    }

    public static void setvar(String argname, String argvalue, boolean mark) {
        if (argname != null && !"".equals(argname)) {
            if (argvalue != null && !"".equals(argvalue)) {
                if (mark) {
                    url_str = url_str + "?" + argname + "=";
                } else {
                    url_str = url_str + "&" + argname + "=";
                }
                try {
                    String encoded = URLEncoder.encode(argvalue, "UTF-8");
                    url_str = url_str + encoded;
                } catch (UnsupportedEncodingException e) {
                    url_str = url_str + argvalue;
                }
            }
        } else {
            if (argvalue != null && !"".equals(argvalue)) {
                url_str = url_str + argvalue;
            }
        }
    }

    public static String send() throws Exception {
        String result = "";

        if (server == null) {
            return result;
        }

        url_str = server;
        setvar(name, user, true);
        setvar(pass, password, false);
        setvar(mobile, phonenumber, false);
        setvar(text, message, false);
        setvar(null, addparam, false);

        URL url2 = new URL(url_str);

        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);

        connection.getResponseMessage();
        int code = connection.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String lineStr = "";
            while (null != ((lineStr = in.readLine()))) {
                result += lineStr;
            }

        }
        if (null != connection) {
            connection.disconnect();
        }

        return result;
    }

}
