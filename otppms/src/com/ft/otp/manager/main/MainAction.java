/**
 *Administrator
 */
package com.ft.otp.manager.main;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.manager.login.entity.LinkUser;

/**
 * 单独拿出一个Action，用于存放公共通用的action方法
 *
 * @Date in Jul 19, 2013,2:11:25 PM
 *
 * @version v1.0
 *
 * @author TBM
 */
public class MainAction extends BaseAction {

    private static final long serialVersionUID = 8939404461420256280L;

    /**
     * 取得进度条百分比的值
     * 用于类似令牌导入等较长等待时间的操作
     * 
     * @Date in Jun 21, 2012,3:33:21 PM
     * @return
     */
    public String getPercentVal() {
        int percent = 0;
        int lastPercent = 0;
        LinkUser linkUser = getLinkUser();

        if (null != linkUser) {
            percent = linkUser.getPercent();
            lastPercent = linkUser.getLastPercent();
        }

        if (percent == lastPercent) {
            setResponseWrite(String.valueOf(percent));
            return null;
        } else {
            linkUser.setLastPercent(percent);
        }

        setResponseWrite(String.valueOf(percent));
        return null;
    }

}
