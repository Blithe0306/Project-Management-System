/**
 *Administrator
 */
package com.ft.otp.core.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.ft.otp.manager.login.service.OnlineUsers;

/**
 * 自实现会话监听器
 *
 * @Date in Apr 14, 2011,5:09:53 PM
 *
 * @author TBM
 */
public class MyHttpSessionListener implements HttpSessionListener {

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent event) {
    }

    /**
     * 销毁Session库
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        if (null != session) {
            OnlineUsers.remove(session.getId());
        }
    }

}
