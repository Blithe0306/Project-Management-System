/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.config.action;

import org.apache.log4j.Logger;

import com.ft.otp.base.action.BaseAction;
import com.ft.otp.base.action.IBaseAction;
import com.ft.otp.manager.confinfo.config.entity.DBConfInfo;

/**
 * 数据库连接池配置业务action
 * @Date in Nov 20, 2012,10:41:42 AM
 *
 * @version v1.0
 *
 * @author YXS
 */
public class DBConfAction extends BaseAction implements IBaseAction {

    private static final long serialVersionUID = -4157051074561267451L;

    //公共配置服务接口
    private Logger logger = Logger.getLogger(DBConfAction.class);

    private DBConfInfo dbConfInfo;
    private DBConfInfo oldConfInfo;

    public DBConfInfo getDbConfInfo() {
        return dbConfInfo;
    }

    public void setDbConfInfo(DBConfInfo dbConfInfo) {
        this.dbConfInfo = dbConfInfo;
    }

    /**
     * @return the oldConfInfo
     */
    public DBConfInfo getOldConfInfo() {
        return oldConfInfo;
    }

    /**
     * @param oldConfInfo the oldConfInfo to set
     */
    public void setOldConfInfo(DBConfInfo oldConfInfo) {
        this.oldConfInfo = oldConfInfo;
    }

    public String add() {
        return null;
    }

    public String delete() {
        return null;
    }

    /**
     * 编辑数据库配置，初始化数据库配置
     */
    public String find() {
        try {
            dbConfInfo = DBConfInfo.getDbConfInfo();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return SUCCESS;
    }

    public String init() {
        return null;
    }

    /**
     * 修改数据库配置信息到配置表
     */
    public String modify() {

        return null;
    }

    public String page() {
        return null;
    }

    public String view() {
        return null;
    }

}
