package com.ft.otp.manager.login.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ft.otp.common.ConfConstant;
import com.ft.otp.manager.login.entity.LinkUser;
import com.ft.otp.util.conf.ConfDataFormat;
import com.ft.otp.util.tool.StrTool;

/**
 * 
 * 在线用户信息存储集合
 *
 * @Date in Feb 19, 2011,10:59:11 AM
 *
 * @author TBM
 */
public class OnlineUsers {

    //在线用户集合
    private static final Map<String, Object> users = new LinkedHashMap<String, Object>();

    //相同用户在不同地方登录，被踢出的会话ID记录
    private static final Map<String, String> removeId = new HashMap<String, String>();

    /**
     * 添加一个在线用户
     * @param sessionId
     * @param user
     */
    public synchronized static void add(LinkUser user) {
        if (user != null) {
            users.put(user.getSessionId(), user);
        }
    }

    /**
     * 根据Session ID删除一个在线用户信息
     * @param sessionId
     * @return
     */
    public synchronized static void remove(String sessionId) {
        users.remove(sessionId);
    }

    /**
     * 根据Session ID 获取一个在线用户信息
     * @Date in Feb 19, 2011,11:01:01 AM
     * @param sessionId
     */
    public synchronized static LinkUser getUser(String sessionId) {
        if (null != sessionId) {
            return (LinkUser) users.get(sessionId);
        }
        return null;
    }

    /**
     * 获得所有在线用户
     * @return
     */
    public synchronized static List<?> getOnlineUsers() {
        long sessionMaxTime = 120;
        String sessionMaxTimeStr = ConfDataFormat.getConfValue(ConfConstant.CONF_TYPE_COMMON,
                ConfConstant.SESSION_EFFECTIVELY_TIME);
        if (StrTool.strNotNull(sessionMaxTimeStr)) {
            sessionMaxTime = StrTool.parseInt(sessionMaxTimeStr);
        }

        //当前时间
        long currTime = System.currentTimeMillis() / 1000;

        List<Object> list = new ArrayList<Object>();

        // 遍历获取Map键和值
        for (String key : users.keySet()) {
            LinkUser linkInfo = (LinkUser) users.get(key);
            if (StrTool.objNotNull(linkInfo)) {
                long loginTime = linkInfo.getLoginTime();
                long sessionTime = (currTime / 60) - (loginTime / 60);
                if (sessionTime >= sessionMaxTime) {
                    users.remove(key);
                } else {
                    list.add(linkInfo);
                }
            }
        }

        return list;
    }

    /**
     * 统计当前在线用户个数
     * @return
     */
    public synchronized static int getUsersCount() {
        return getOnlineUsers().size();
    }

    /**
     * 清空用户会话集合
     * @Date in Apr 18, 2011,1:55:52 PM
     */
    public synchronized static void clearSessions() {
        users.clear();
    }

    /**
     * 添加踢出的会话ID
     * @Date in Jul 9, 2012,6:07:07 PM
     * @param key
     */
    public synchronized static void addRemoveSession(String key, String value) {
        removeId.put(key, value);
    }

    /**
     * 获取踢出的会话ID
     * 
     * @Date in Jul 9, 2012,6:32:42 PM
     * @param key
     * @return
     */
    public synchronized static String getRemoveSession(String key) {
        String value = null;
        if (StrTool.strNotNull(key)) {
            try {
                value = removeId.get(key);
            } catch (Exception e) {
            }
        }

        return value;
    }

    /**
     * 清除踢出的会话ID
     * 
     * @Date in Jul 9, 2012,6:33:04 PM
     * @param key
     */
    public synchronized static void removeSession(String key) {
        removeId.remove(key);
    }

    /**
     * 根据HttpServletRequest取得当前会话中的会话ID
     * 
     * @Date in Apr 20, 2012,2:21:39 PM
     * @param request
     * @return
     */
    public static String getSessionId(HttpServletRequest request) {
        String sessionId = null;
        HttpSession session = null;
        if (null != request) {
            session = request.getSession();
        }
        if (null != session) {
            sessionId = session.getId();
            if (StrTool.strNotNull(sessionId)) {
                return sessionId;
            }
        }
        return null;
    }
}