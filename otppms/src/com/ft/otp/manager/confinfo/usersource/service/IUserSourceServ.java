/**
 *Administrator
 */
package com.ft.otp.manager.confinfo.usersource.service;

import java.util.Map;

import com.ft.otp.base.exception.BaseException;
import com.ft.otp.base.service.IBaseService;
import com.ft.otp.manager.confinfo.usersource.entity.UserSourceInfo;

/**
 * 用户来源配置业务接口说明
 * 
 * @Date in Jun 7, 2011,11:15:16 AM
 * 
 * @author YYF
 */
public interface IUserSourceServ extends IBaseService {
    /**
     * 得到用户来源详细配置信息
     * 
     * @param object
     * @return
     * @throws BaseException
     */
    public UserSourceInfo getUserSourceInfo(Object object) throws BaseException;

    //    public Connection getDominoConnection(DominoInfo dominoInfo) throws BaseException;
    /**
     * 测试用户来源连接库连接
     */
    public boolean testUsConn(UserSourceInfo userSourceInfo) throws BaseException;

    /**
     * 获取远程数据库所有表
     * 方法说明
     * @Date in Dec 6, 2012,11:35:17 AM
     * @param databaseInfo
     * @return
     * @throws BaseException
     */
    public String getAllTableName(UserSourceInfo databaseInfo) throws BaseException;

    /**
     * 根据表名获取表的所有属性
     * 方法说明
     * @Date in Dec 6, 2012,11:35:30 AM
     * @param databaseInfo
     * @return
     * @throws BaseException
     */
    public String queryFieldsByTableName(UserSourceInfo databaseInfo) throws BaseException;

    /**
     * 更新用户来源信息
     * 方法说明
     * @Date in Dec 6, 2012,11:35:51 AM
     * @param userSourceInfo
     * @return
     * @throws BaseException
     */
    public Map updateUserInfo(UserSourceInfo userSourceInfo) throws BaseException;
}
