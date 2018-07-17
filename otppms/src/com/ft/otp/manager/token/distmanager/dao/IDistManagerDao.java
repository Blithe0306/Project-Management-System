/**
 *Administrator
 */
package com.ft.otp.manager.token.distmanager.dao;

import java.util.List;

import com.ft.otp.base.dao.IBaseDao;
import com.ft.otp.base.exception.BaseException;

/**
 * 令牌分发接口类功能说明
 *
 * @Date in Apr 18, 2011,11:19:14 AM
 *
 * @author ZJY
 */
public interface IDistManagerDao extends IBaseDao {
    /**
     * 批量导入手机令牌
     * 方法说明
     * @Date in Feb 13, 2012,3:37:07 PM
     * @param objList
     * @throws BaseException
     */
    int importExtToken(List<Object> objList) throws BaseException;
}